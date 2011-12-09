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
name|admin
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
name|admin
operator|.
name|Instance
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"admin"
argument_list|,
name|name
operator|=
literal|"start"
argument_list|,
name|description
operator|=
literal|"Starts an existing container instance."
argument_list|)
specifier|public
class|class
name|StartCommand
extends|extends
name|AdminCommandSupport
block|{
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
literal|"--debug"
block|}
argument_list|,
name|description
operator|=
literal|"Start the instance in debug mode"
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
name|debug
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
literal|"--java-opts"
block|}
argument_list|,
name|description
operator|=
literal|"Java options when launching the instance"
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
name|javaOpts
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-w"
argument_list|,
name|aliases
operator|=
block|{
literal|"--wait"
block|}
argument_list|,
name|description
operator|=
literal|"Wait for the instance to be fully started"
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
name|wait
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
specifier|private
name|String
name|instance
init|=
literal|null
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEBUG_OPTS
init|=
literal|" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEFAULT_OPTS
init|=
literal|"-server -Xmx512M -Dcom.sun.management.jmxremote"
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|child
init|=
name|getExistingInstance
argument_list|(
name|instance
argument_list|)
decl_stmt|;
name|String
name|opts
init|=
name|javaOpts
decl_stmt|;
if|if
condition|(
name|opts
operator|==
literal|null
condition|)
block|{
name|opts
operator|=
name|child
operator|.
name|getJavaOpts
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|opts
operator|==
literal|null
condition|)
block|{
name|opts
operator|=
name|DEFAULT_OPTS
expr_stmt|;
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|opts
operator|+=
name|DEBUG_OPTS
expr_stmt|;
block|}
if|if
condition|(
name|wait
condition|)
block|{
name|String
name|state
init|=
name|child
operator|.
name|getState
argument_list|()
decl_stmt|;
if|if
condition|(
name|Instance
operator|.
name|STOPPED
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
block|{
name|child
operator|.
name|start
argument_list|(
name|opts
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Instance
operator|.
name|STARTED
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
block|{
do|do
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|state
operator|=
name|child
operator|.
name|getState
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|Instance
operator|.
name|STARTING
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
do|;
block|}
block|}
else|else
block|{
name|child
operator|.
name|start
argument_list|(
name|opts
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

