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
name|client
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
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
name|console
operator|.
name|impl
operator|.
name|jline
operator|.
name|TerminalFactory
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
name|agent
operator|.
name|local
operator|.
name|AgentImpl
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
name|local
operator|.
name|LocalAgentFactory
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
name|RuntimeSshException
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
name|fusesource
operator|.
name|jansi
operator|.
name|AnsiConsole
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|impl
operator|.
name|SimpleLogger
import|;
end_import

begin_comment
comment|/**  * A very simple  */
end_comment

begin_class
specifier|public
class|class
name|Main
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
name|int
name|port
init|=
literal|8101
decl_stmt|;
name|String
name|user
init|=
literal|"karaf"
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|level
init|=
literal|1
decl_stmt|;
name|int
name|retryAttempts
init|=
literal|0
decl_stmt|;
name|int
name|retryDelay
init|=
literal|2
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'-'
condition|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-a"
argument_list|)
condition|)
block|{
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
operator|++
name|i
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-h"
argument_list|)
condition|)
block|{
name|host
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-u"
argument_list|)
condition|)
block|{
name|user
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-v"
argument_list|)
condition|)
block|{
name|level
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-r"
argument_list|)
condition|)
block|{
name|retryAttempts
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
operator|++
name|i
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-d"
argument_list|)
condition|)
block|{
name|retryDelay
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
operator|++
name|i
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"--help"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Apache Karaf client"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -a [port]     specify the port to connect to"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -h [host]     specify the host to connect to"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -u [user]     specify the user name"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  --help        shows this help message"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -v            raise verbosity"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -r [attempts] retry connection establishment (up to attempts times)"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  -d [delay]    intra-retry delay (defaults to 2 seconds)"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  [commands]    commands to run"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"If no commands are specified, the client will be put in an interactive mode"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown option: "
operator|+
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Run with --help for usage"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
name|SimpleLogger
operator|.
name|setLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
name|SshClient
name|client
init|=
literal|null
decl_stmt|;
name|Terminal
name|terminal
init|=
literal|null
decl_stmt|;
name|SshAgent
name|agent
init|=
literal|null
decl_stmt|;
try|try
block|{
name|agent
operator|=
name|startAgent
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|client
operator|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
expr_stmt|;
name|client
operator|.
name|setAgentFactory
argument_list|(
operator|new
name|LocalAgentFactory
argument_list|(
name|agent
argument_list|)
argument_list|)
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
literal|"local"
argument_list|)
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
name|int
name|retries
init|=
literal|0
decl_stmt|;
name|ClientSession
name|session
init|=
literal|null
decl_stmt|;
do|do
block|{
name|ConnectFuture
name|future
init|=
name|client
operator|.
name|connect
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|future
operator|.
name|await
argument_list|()
expr_stmt|;
try|try
block|{
name|session
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeSshException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|retries
operator|++
operator|<
name|retryAttempts
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|retryDelay
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"retrying (attempt "
operator|+
name|retries
operator|+
literal|") ..."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
do|while
condition|(
name|session
operator|==
literal|null
condition|)
do|;
if|if
condition|(
operator|!
name|session
operator|.
name|authAgent
argument_list|(
name|user
argument_list|)
operator|.
name|await
argument_list|()
operator|.
name|isSuccess
argument_list|()
condition|)
block|{
name|String
name|password
init|=
name|readLine
argument_list|(
literal|"Password: "
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|session
operator|.
name|authPassword
argument_list|(
name|user
argument_list|,
name|password
argument_list|)
operator|.
name|await
argument_list|()
operator|.
name|isSuccess
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Authentication failure"
argument_list|)
throw|;
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
name|session
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
name|terminal
operator|=
operator|new
name|TerminalFactory
argument_list|()
operator|.
name|getTerminal
argument_list|()
expr_stmt|;
name|channel
operator|=
name|session
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
block|}
name|channel
operator|.
name|setOut
argument_list|(
name|AnsiConsole
operator|.
name|wrapOutputStream
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
name|AnsiConsole
operator|.
name|wrapOutputStream
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
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|level
operator|>
literal|1
condition|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{ }
try|try
block|{
if|if
condition|(
name|terminal
operator|!=
literal|null
condition|)
block|{
name|terminal
operator|.
name|restore
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{ }
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|SshAgent
name|startAgent
parameter_list|(
name|String
name|user
parameter_list|)
block|{
try|try
block|{
name|SshAgent
name|local
init|=
operator|new
name|AgentImpl
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|Main
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"karaf.key"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|ObjectInputStream
name|r
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|KeyPair
name|keyPair
init|=
operator|(
name|KeyPair
operator|)
name|r
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|local
operator|.
name|addIdentity
argument_list|(
name|keyPair
argument_list|,
literal|"karaf"
argument_list|)
expr_stmt|;
return|return
name|local
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error starting ssh agent for: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
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
name|System
operator|.
name|in
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

