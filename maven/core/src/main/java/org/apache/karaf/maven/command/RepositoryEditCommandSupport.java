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
name|maven
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
name|Dictionary
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
name|maven
operator|.
name|core
operator|.
name|MavenRepositoryURL
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

begin_class
specifier|public
specifier|abstract
class|class
name|RepositoryEditCommandSupport
extends|extends
name|MavenSecuritySupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-id"
argument_list|,
name|description
operator|=
literal|"Identifier of repository"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|id
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--default"
block|}
argument_list|,
name|description
operator|=
literal|"Edit default repository instead of remote one"
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
name|defaultRepository
init|=
literal|false
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|doAction
parameter_list|(
name|String
name|prefix
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|edit
argument_list|(
name|prefix
argument_list|,
name|config
argument_list|)
expr_stmt|;
if|if
condition|(
name|success
condition|)
block|{
if|if
condition|(
name|showPasswords
condition|)
block|{
name|session
operator|.
name|execute
argument_list|(
literal|"maven:repository-list -x"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|execute
argument_list|(
literal|"maven:repository-list"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|edit
parameter_list|(
name|String
name|prefix
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

