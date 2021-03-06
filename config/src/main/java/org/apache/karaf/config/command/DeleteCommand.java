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
name|config
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
name|config
operator|.
name|command
operator|.
name|completers
operator|.
name|ConfigurationCompleter
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
literal|"config"
argument_list|,
name|name
operator|=
literal|"delete"
argument_list|,
name|description
operator|=
literal|"Delete a configuration."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|DeleteCommand
extends|extends
name|ConfigCommandSupport
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
literal|"pid"
argument_list|,
name|description
operator|=
literal|"PID of the configuration"
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
name|ConfigurationCompleter
operator|.
name|class
argument_list|)
name|String
name|pid
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--force"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Force the edition of this config, even if another one was under edition"
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
name|force
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|oldPid
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldPid
operator|!=
literal|null
operator|&&
name|oldPid
operator|.
name|equals
argument_list|(
name|pid
argument_list|)
operator|&&
operator|!
name|force
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"This config is being edited.  Cancel / update first, or use the --force option"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|this
operator|.
name|configRepository
operator|.
name|delete
argument_list|(
name|pid
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldPid
operator|!=
literal|null
operator|&&
name|oldPid
operator|.
name|equals
argument_list|(
name|pid
argument_list|)
operator|&&
operator|!
name|force
condition|)
block|{
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PROPS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

