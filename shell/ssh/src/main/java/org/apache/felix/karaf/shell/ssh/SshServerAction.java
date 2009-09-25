begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|SshServer
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
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|BlueprintContainerAware
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
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|OsgiCommandSupport
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_comment
comment|/**  * Start a SSH server.  *  * @version $Rev: 720411 $ $Date: 2008-11-25 05:32:43 +0100 (Tue, 25 Nov 2008) $  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"ssh"
argument_list|,
name|name
operator|=
literal|"sshd"
argument_list|,
name|description
operator|=
literal|"Creates a SSH server"
argument_list|)
specifier|public
class|class
name|SshServerAction
extends|extends
name|OsgiCommandSupport
implements|implements
name|BlueprintContainerAware
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
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
literal|"--port"
block|}
argument_list|,
name|description
operator|=
literal|"The port to setup the SSH server (Default: 8101)"
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
name|int
name|port
init|=
literal|8101
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
block|{
literal|"--background"
block|}
argument_list|,
name|description
operator|=
literal|"The service will run in the background (Default: true)"
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
name|background
init|=
literal|true
decl_stmt|;
specifier|private
name|BlueprintContainer
name|container
decl_stmt|;
specifier|private
name|String
name|sshServerId
decl_stmt|;
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
specifier|final
name|BlueprintContainer
name|container
parameter_list|)
block|{
assert|assert
name|container
operator|!=
literal|null
assert|;
name|this
operator|.
name|container
operator|=
name|container
expr_stmt|;
block|}
specifier|public
name|void
name|setSshServerId
parameter_list|(
name|String
name|sshServerId
parameter_list|)
block|{
name|this
operator|.
name|sshServerId
operator|=
name|sshServerId
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|SshServer
name|server
init|=
operator|(
name|SshServer
operator|)
name|container
operator|.
name|getComponentInstance
argument_list|(
name|sshServerId
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created server: {}"
argument_list|,
name|server
argument_list|)
expr_stmt|;
name|server
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"SSH server listening on port "
operator|+
name|port
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|background
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Waiting for server to shutdown"
argument_list|)
expr_stmt|;
name|wait
argument_list|()
expr_stmt|;
block|}
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

