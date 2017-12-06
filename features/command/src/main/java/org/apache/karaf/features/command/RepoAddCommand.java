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
name|command
operator|.
name|completers
operator|.
name|AvailableRepoNameCompleter
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|Completion
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
literal|"repo-add"
argument_list|,
name|description
operator|=
literal|"Add a features repository"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|RepoAddCommand
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
literal|"name/url"
argument_list|,
name|description
operator|=
literal|"Shortcut name of the features repository or the full URL"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|AvailableRepoNameCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|nameOrUrl
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"version"
argument_list|,
name|description
operator|=
literal|"The version of the features repository if using features repository name as first argument. It should be empty if using the URL"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|version
decl_stmt|;
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
literal|"--install"
block|}
argument_list|,
name|description
operator|=
literal|"Install all features contained in the features repository"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|install
decl_stmt|;
annotation|@
name|Override
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
name|URI
name|uri
init|=
name|featuresService
operator|.
name|getRepositoryUriFor
argument_list|(
name|nameOrUrl
argument_list|,
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|nameOrUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|featuresService
operator|.
name|isRepositoryUriBlacklisted
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Feature URL "
operator|+
name|uri
operator|+
literal|" is blacklisted"
argument_list|)
expr_stmt|;
return|return;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Adding feature url "
operator|+
name|uri
argument_list|)
expr_stmt|;
name|featuresService
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|,
name|install
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

