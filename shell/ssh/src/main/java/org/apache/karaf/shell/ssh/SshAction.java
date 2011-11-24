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
name|karaf
operator|.
name|shell
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|BlueprintContainerAware
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
name|OsgiCommandSupport
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
name|jline
operator|.
name|Console
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|ClientChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|ClientSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|SshClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|channel
operator|.
name|ChannelShell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|future
operator|.
name|ConnectFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|NoCloseInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|NoCloseOutputStream
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

begin_comment
comment|/**  * Connect to a SSH server.  *  * @version $Rev: 721244 $ $Date: 2008-11-27 18:19:56 +0100 (Thu, 27 Nov 2008) $  */
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
literal|"ssh"
argument_list|,
name|description
operator|=
literal|"Connects to a remote SSH server"
argument_list|)
specifier|public
class|class
name|SshAction
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
literal|"-l"
argument_list|,
name|aliases
operator|=
block|{
literal|"--username"
block|}
argument_list|,
name|description
operator|=
literal|"The user name for remote login"
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
literal|"-P"
argument_list|,
name|aliases
operator|=
block|{
literal|"--password"
block|}
argument_list|,
name|description
operator|=
literal|"The password for remote login"
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
literal|"The port to use for SSH connection"
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
literal|22
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
literal|"hostname"
argument_list|,
name|description
operator|=
literal|"The host name to connect to via SSH"
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
name|hostname
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
specifier|private
name|BlueprintContainer
name|container
decl_stmt|;
specifier|private
name|ClientSession
name|sshSession
decl_stmt|;
specifier|private
name|String
name|sshClientId
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
name|setSshClientId
parameter_list|(
name|String
name|sshClientId
parameter_list|)
block|{
name|this
operator|.
name|sshClientId
operator|=
name|sshClientId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
comment|//
comment|// TODO: Parse hostname for<username>@<hostname>
comment|//
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connecting to host "
operator|+
name|hostname
operator|+
literal|" on port "
operator|+
name|port
argument_list|)
expr_stmt|;
comment|// If the username/password was not configured via cli, then prompt the user for the values
if|if
condition|(
name|username
operator|==
literal|null
operator|||
name|password
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Prompting user for credentials"
argument_list|)
expr_stmt|;
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
name|readLine
argument_list|(
literal|"Login: "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
name|readLine
argument_list|(
literal|"Password: "
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Create the client from prototype
name|SshClient
name|client
init|=
operator|(
name|SshClient
operator|)
name|container
operator|.
name|getComponentInstance
argument_list|(
name|sshClientId
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created client: {}"
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|ConnectFuture
name|future
init|=
name|client
operator|.
name|connect
argument_list|(
name|hostname
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|future
operator|.
name|await
argument_list|()
expr_stmt|;
name|sshSession
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|Object
name|oldIgnoreInterrupts
init|=
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|Console
operator|.
name|IGNORE_INTERRUPTS
argument_list|)
decl_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|Console
operator|.
name|IGNORE_INTERRUPTS
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connected"
argument_list|)
expr_stmt|;
name|sshSession
operator|.
name|authPassword
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|int
name|ret
init|=
name|sshSession
operator|.
name|waitFor
argument_list|(
name|ClientSession
operator|.
name|WAIT_AUTH
operator||
name|ClientSession
operator|.
name|CLOSED
operator||
name|ClientSession
operator|.
name|AUTHED
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|ret
operator|&
name|ClientSession
operator|.
name|AUTHED
operator|)
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Authentication failed"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|command
operator|!=
literal|null
condition|)
block|{
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
block|}
name|ClientChannel
name|channel
decl_stmt|;
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
name|channel
operator|=
name|sshSession
operator|.
name|createChannel
argument_list|(
literal|"exec"
argument_list|,
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setIn
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|channel
operator|=
name|sshSession
operator|.
name|createChannel
argument_list|(
literal|"shell"
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setIn
argument_list|(
operator|new
name|NoCloseInputStream
argument_list|(
name|System
operator|.
name|in
argument_list|)
argument_list|)
expr_stmt|;
operator|(
operator|(
name|ChannelShell
operator|)
name|channel
operator|)
operator|.
name|setupSensibleDefaultPty
argument_list|()
expr_stmt|;
block|}
name|channel
operator|.
name|setOut
argument_list|(
operator|new
name|NoCloseOutputStream
argument_list|(
name|System
operator|.
name|out
argument_list|)
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setErr
argument_list|(
operator|new
name|NoCloseOutputStream
argument_list|(
name|System
operator|.
name|err
argument_list|)
argument_list|)
expr_stmt|;
name|channel
operator|.
name|open
argument_list|()
expr_stmt|;
name|channel
operator|.
name|waitFor
argument_list|(
name|ClientChannel
operator|.
name|CLOSED
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|session
operator|.
name|put
argument_list|(
name|Console
operator|.
name|IGNORE_INTERRUPTS
argument_list|,
name|oldIgnoreInterrupts
argument_list|)
expr_stmt|;
name|sshSession
operator|.
name|close
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|readLine
parameter_list|(
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|c
init|=
name|super
operator|.
name|session
operator|.
name|getKeyboard
argument_list|()
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\r'
operator|||
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

