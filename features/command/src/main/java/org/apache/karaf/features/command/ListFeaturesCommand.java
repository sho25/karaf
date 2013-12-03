begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|table
operator|.
name|ShellTable
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"feature"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists all existing features available from the defined repositories."
argument_list|)
specifier|public
class|class
name|ListFeaturesCommand
extends|extends
name|FeaturesCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--installed"
block|}
argument_list|,
name|description
operator|=
literal|"Display a list of all installed features only"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|onlyInstalled
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ordered"
block|}
argument_list|,
name|description
operator|=
literal|"Display a list using alphabetical order "
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|ordered
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-format"
argument_list|,
name|description
operator|=
literal|"Disable table rendered output"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|noFormat
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|needsLegend
init|=
literal|false
decl_stmt|;
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Installed"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Repository"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Description"
argument_list|)
operator|.
name|maxSize
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|table
operator|.
name|emptyTableText
argument_list|(
name|onlyInstalled
condition|?
literal|"No features installed"
else|:
literal|"No features available"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Repository
argument_list|>
name|repos
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresService
operator|.
name|listRepositories
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Repository
name|r
range|:
name|repos
control|)
block|{
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|r
operator|.
name|getFeatures
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ordered
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|features
argument_list|,
operator|new
name|FeatureComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Feature
name|f
range|:
name|features
control|)
block|{
if|if
condition|(
name|onlyInstalled
operator|&&
operator|!
name|featuresService
operator|.
name|isInstalled
argument_list|(
name|f
argument_list|)
condition|)
block|{
comment|// Filter out not installed features if we only want to see the installed ones
continue|continue;
block|}
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
operator|.
name|getVersion
argument_list|()
argument_list|,
name|featuresService
operator|.
name|isInstalled
argument_list|(
name|f
argument_list|)
condition|?
literal|"x"
else|:
literal|""
argument_list|,
name|r
operator|.
name|getName
argument_list|()
argument_list|,
name|f
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isInstalledViaDeployDir
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|needsLegend
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|!
name|noFormat
argument_list|)
expr_stmt|;
if|if
condition|(
name|needsLegend
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"* Installed via deploy directory"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isInstalledViaDeployDir
parameter_list|(
name|String
name|st
parameter_list|)
block|{
return|return
operator|(
name|st
operator|==
literal|null
operator|||
name|st
operator|.
name|length
argument_list|()
operator|<=
literal|1
operator|)
condition|?
literal|false
else|:
operator|(
name|st
operator|.
name|charAt
argument_list|(
name|st
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'*'
operator|)
return|;
block|}
class|class
name|FeatureComparator
implements|implements
name|Comparator
argument_list|<
name|Feature
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Feature
name|o1
parameter_list|,
name|Feature
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

