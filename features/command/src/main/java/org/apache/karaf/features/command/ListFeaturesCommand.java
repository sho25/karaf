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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
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
name|felix
operator|.
name|gogo
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"features"
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
name|installed
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STATE
init|=
literal|"State"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INSTALLED
init|=
literal|"installed  "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|UNINSTALLED
init|=
literal|"uninstalled"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"Version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"Name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY
init|=
literal|"Repository"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DESCRIPTION
init|=
literal|"Description"
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Get the feature data to print.
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Repository
argument_list|>
name|repositories
init|=
operator|new
name|ArrayList
argument_list|<
name|Repository
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|r
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|admin
operator|.
name|listRepositories
argument_list|()
argument_list|)
control|)
block|{
for|for
control|(
name|Feature
name|f
range|:
name|r
operator|.
name|getFeatures
argument_list|()
control|)
block|{
if|if
condition|(
name|installed
operator|&&
operator|!
name|admin
operator|.
name|isInstalled
argument_list|(
name|f
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|features
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|repositories
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|features
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|installed
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No features installed."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No features available."
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
comment|// Print column headers.
name|int
name|maxVersionSize
init|=
name|VERSION
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|f
range|:
name|features
control|)
block|{
name|maxVersionSize
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxVersionSize
argument_list|,
name|f
operator|.
name|getVersion
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|maxNameSize
init|=
name|NAME
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|f
range|:
name|features
control|)
block|{
name|maxNameSize
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxNameSize
argument_list|,
name|f
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|maxRepositorySize
init|=
name|REPOSITORY
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|repository
range|:
name|repositories
control|)
block|{
name|maxRepositorySize
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxRepositorySize
argument_list|,
name|repository
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|STATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"         "
argument_list|)
operator|.
name|append
argument_list|(
name|VERSION
argument_list|)
operator|.
name|append
argument_list|(
literal|"   "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|VERSION
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|maxVersionSize
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|NAME
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|NAME
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|maxNameSize
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|REPOSITORY
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|REPOSITORY
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|maxRepositorySize
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|DESCRIPTION
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Print the feature data.
name|boolean
name|needsLegend
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Feature
name|f
range|:
name|features
control|)
block|{
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
if|if
condition|(
name|admin
operator|.
name|isInstalled
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|INSTALLED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|UNINSTALLED
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"] ["
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|f
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|str
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|str
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|maxVersionSize
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"] "
argument_list|)
expr_stmt|;
name|str
operator|=
name|f
operator|.
name|getName
argument_list|()
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|str
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|str
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|maxNameSize
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repositories
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|charAt
argument_list|(
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'*'
condition|)
block|{
name|needsLegend
operator|=
literal|true
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
name|name
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|maxRepositorySize
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|String
name|description
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|description
operator|=
name|f
operator|.
name|getDescription
argument_list|()
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

