begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|instance
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
name|instance
operator|.
name|command
operator|.
name|completers
operator|.
name|InstanceCompleter
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
name|Reference
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
name|console
operator|.
name|Session
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"instance"
argument_list|,
name|name
operator|=
literal|"connect"
argument_list|,
name|description
operator|=
literal|"Connects to an existing container instance."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ConnectCommand
extends|extends
name|InstanceCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{
literal|"--username"
block|}
argument_list|,
name|description
operator|=
literal|"Remote user name"
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
name|username
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--password"
block|}
argument_list|,
name|description
operator|=
literal|"Remote password"
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
name|password
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"The name of the container instance"
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
name|InstanceCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|instance
init|=
literal|null
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
literal|"command"
argument_list|,
name|description
operator|=
literal|"Optional command to execute"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|command
decl_stmt|;
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|cmdStr
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|command
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|cmd
range|:
name|command
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
name|cmdStr
operator|=
literal|"'"
operator|+
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"'"
argument_list|,
literal|"\\'"
argument_list|)
operator|+
literal|"'"
expr_stmt|;
block|}
name|int
name|port
init|=
name|getExistingInstance
argument_list|(
name|instance
argument_list|)
operator|.
name|getSshPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|session
operator|.
name|execute
argument_list|(
literal|"ssh:ssh -q -l "
operator|+
name|username
operator|+
literal|" -p "
operator|+
name|port
operator|+
literal|" localhost "
operator|+
name|cmdStr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|execute
argument_list|(
literal|"ssh:ssh -q -l "
operator|+
name|username
operator|+
literal|" -P "
operator|+
name|password
operator|+
literal|" -p "
operator|+
name|port
operator|+
literal|" localhost "
operator|+
name|cmdStr
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|session
operator|.
name|execute
argument_list|(
literal|"ssh:ssh -q -p "
operator|+
name|port
operator|+
literal|" localhost "
operator|+
name|cmdStr
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
