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
name|net
operator|.
name|URI
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
name|shell
operator|.
name|commands
operator|.
name|Argument
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
literal|"add-url"
argument_list|,
name|description
operator|=
literal|"Adds a list of repository URLs to the features service."
argument_list|)
specifier|public
class|class
name|AddUrlCommand
extends|extends
name|FeaturesCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"urls"
argument_list|,
name|description
operator|=
literal|"One or more repository URLs separated by whitespaces"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|urls
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
for|for
control|(
name|String
name|url
range|:
name|urls
control|)
block|{
try|try
block|{
name|Boolean
name|alreadyInstalled
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
name|Repository
index|[]
name|repositories
init|=
name|admin
operator|.
name|listRepositories
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
name|String
name|repositoryUrl
init|=
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//Check if the repository is already installed.
if|if
condition|(
name|repositoryUrl
operator|.
name|equals
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|alreadyInstalled
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|alreadyInstalled
condition|)
block|{
name|admin
operator|.
name|addRepository
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|refreshUrl
argument_list|(
name|admin
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Could not add Feature Repository:\n"
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

