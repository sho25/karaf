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
name|ArrayList
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
name|console
operator|.
name|MultiException
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
literal|"repo-list"
argument_list|,
name|description
operator|=
literal|"Displays a list of all defined repositories."
argument_list|)
specifier|public
class|class
name|RepoListCommand
extends|extends
name|FeaturesCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-r"
argument_list|,
name|description
operator|=
literal|"Reload all feature urls"
argument_list|)
name|boolean
name|reload
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
if|if
condition|(
name|reload
condition|)
block|{
name|reloadAllRepos
argument_list|(
name|featuresService
argument_list|)
expr_stmt|;
block|}
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
literal|"Repository"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"URL"
argument_list|)
expr_stmt|;
name|table
operator|.
name|emptyTableText
argument_list|(
literal|"No repositories available"
argument_list|)
expr_stmt|;
name|Repository
index|[]
name|repos
init|=
name|featuresService
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|repos
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|repo
operator|.
name|getName
argument_list|()
argument_list|,
name|repo
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|reloadAllRepos
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
throws|throws
name|MultiException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Reloading all repositories from their urls"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exception
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|Exception
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
try|try
block|{
name|featuresService
operator|.
name|addRepository
argument_list|(
name|repo
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exceptions
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|MultiException
operator|.
name|throwIf
argument_list|(
literal|"Unable to reload repositories"
argument_list|,
name|exceptions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

