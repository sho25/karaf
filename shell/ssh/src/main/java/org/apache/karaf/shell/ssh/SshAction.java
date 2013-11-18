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
name|jline
operator|.
name|Terminal
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
name|SessionProperties
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
name|agent
operator|.
name|SshAgent
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
name|Option
argument_list|(
name|name
operator|=
literal|"-q"
argument_list|,
name|description
operator|=
literal|"Quiet Mode. Do not ask for confirmations"
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
name|quiet
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
name|ClientSession
name|sshSession
decl_stmt|;
specifier|private
name|SshClientFactory
name|sshClientFactory
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|keyChangedMessage
init|=
literal|" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n"
operator|+
literal|" @    WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED!      @ \n"
operator|+
literal|" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n"
operator|+
literal|"IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!\n"
operator|+
literal|"Someone could be eavesdropping on you right now (man-in-the-middle attack)!\n"
operator|+
literal|"It is also possible that the RSA host key has just been changed.\n"
operator|+
literal|"Please contact your system administrator.\n"
operator|+
literal|"Add correct host key in "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.sshkaraf/known_hosts to get rid of this message.\n"
operator|+
literal|"Offending key in "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.sshkaraf/known_hosts\n"
operator|+
literal|"RSA host key has changed and you have requested strict checking.\n"
operator|+
literal|"Host key verification failed."
decl_stmt|;
specifier|public
name|void
name|setSshClientFactory
parameter_list|(
name|SshClientFactory
name|sshClientFactory
parameter_list|)
block|{
name|this
operator|.
name|sshClientFactory
operator|=
name|sshClientFactory
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
if|if
condition|(
name|hostname
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
operator|>=
literal|0
condition|)
block|{
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
name|hostname
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|hostname
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|hostname
operator|=
name|hostname
operator|.
name|substring
argument_list|(
name|hostname
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
operator|+
literal|1
argument_list|,
name|hostname
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|// If not specified, assume the current user name
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
operator|(
name|String
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
literal|"USER"
argument_list|)
expr_stmt|;
block|}
comment|// If the username was not configured via cli, then prompt the user for the values
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Prompting user for login"
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
block|}
name|SshClient
name|client
init|=
name|sshClientFactory
operator|.
name|create
argument_list|(
name|quiet
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
name|String
name|agentSocket
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|SshAgent
operator|.
name|SSH_AUTHSOCKET_ENV_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|agentSocket
operator|=
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|SshAgent
operator|.
name|SSH_AUTHSOCKET_ENV_NAME
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SshAgent
operator|.
name|SSH_AUTHSOCKET_ENV_NAME
argument_list|,
name|agentSocket
argument_list|)
expr_stmt|;
block|}
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
name|SessionProperties
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
name|SessionProperties
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
name|boolean
name|authed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|agentSocket
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sshSession
operator|.
name|authAgent
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ise
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|keyChangedMessage
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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
literal|"Agent authentication failed, falling back to password authentication."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|authed
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|authed
condition|)
block|{
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Prompting user for password"
argument_list|)
expr_stmt|;
name|password
operator|=
name|readLine
argument_list|(
literal|"Password: "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Password provided using command line option"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|sshSession
operator|.
name|authPassword
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ise
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|keyChangedMessage
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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
literal|"Password authentication failed"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|authed
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|authed
condition|)
block|{
return|return
literal|null
return|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connected"
argument_list|)
expr_stmt|;
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
name|setPtyColumns
argument_list|(
name|getTermWidth
argument_list|()
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
operator|(
operator|(
name|ChannelShell
operator|)
name|channel
operator|)
operator|.
name|setAgentForwarding
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|ctype
init|=
name|session
operator|.
name|get
argument_list|(
literal|"LC_CTYPE"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctype
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|ChannelShell
operator|)
name|channel
operator|)
operator|.
name|setEnv
argument_list|(
literal|"LC_CTYPE"
argument_list|,
name|ctype
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|SessionProperties
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
specifier|private
name|int
name|getTermWidth
parameter_list|()
block|{
name|Terminal
name|term
init|=
operator|(
name|Terminal
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".jline.terminal"
argument_list|)
decl_stmt|;
return|return
name|term
operator|!=
literal|null
condition|?
name|term
operator|.
name|getWidth
argument_list|()
else|:
literal|80
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

