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
literal|"repository-list"
argument_list|,
name|description
operator|=
literal|"Displays a list of all defined repositories."
argument_list|)
specifier|public
class|class
name|ListRepositoriesCommand
extends|extends
name|FeaturesCommandSupport
block|{
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
name|REPOSITORY_URL
init|=
literal|"Repository URL"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|description
operator|=
literal|"Display repository URLs."
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
name|showUrl
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
name|StringBuffer
name|sb
init|=
literal|null
decl_stmt|;
name|Repository
index|[]
name|repos
init|=
name|admin
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
name|int
name|maxRepositorySize
init|=
name|REPOSITORY
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|maxRepositoryUrlSize
init|=
name|REPOSITORY_URL
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|r
range|:
name|repos
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
name|r
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|maxRepositoryUrlSize
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxRepositoryUrlSize
argument_list|,
name|r
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|repos
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|repos
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
comment|// Prepare the header
name|sb
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|append
argument_list|(
name|sb
argument_list|,
name|REPOSITORY
argument_list|,
name|maxRepositorySize
operator|+
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|showUrl
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
name|REPOSITORY_URL
argument_list|,
name|maxRepositoryUrlSize
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|append
argument_list|(
name|sb
argument_list|,
name|repos
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|maxRepositorySize
operator|+
literal|2
argument_list|)
expr_stmt|;
if|if
condition|(
name|showUrl
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
name|repos
index|[
name|i
index|]
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|maxRepositoryUrlSize
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
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
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No repositories available."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|append
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|String
name|s
parameter_list|,
name|int
name|width
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|s
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|width
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
block|}
block|}
end_class

end_unit

