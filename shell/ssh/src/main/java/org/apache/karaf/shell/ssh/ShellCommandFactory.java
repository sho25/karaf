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
name|Closeable
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|CommandException
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
name|runtime
operator|.
name|CommandNotFoundException
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
name|service
operator|.
name|command
operator|.
name|CommandProcessor
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|service
operator|.
name|command
operator|.
name|Converter
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
name|ConsoleImpl
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
name|server
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
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
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
name|server
operator|.
name|Environment
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
name|server
operator|.
name|ExitCallback
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
name|server
operator|.
name|SessionAware
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
name|server
operator|.
name|session
operator|.
name|ServerSession
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
name|Ansi
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
specifier|public
class|class
name|ShellCommandFactory
implements|implements
name|CommandFactory
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ShellCommandFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CommandProcessor
name|commandProcessor
decl_stmt|;
specifier|public
name|void
name|setCommandProcessor
parameter_list|(
name|CommandProcessor
name|commandProcessor
parameter_list|)
block|{
name|this
operator|.
name|commandProcessor
operator|=
name|commandProcessor
expr_stmt|;
block|}
specifier|public
name|Command
name|createCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
return|return
operator|new
name|ShellCommand
argument_list|(
name|command
argument_list|)
return|;
block|}
specifier|public
class|class
name|ShellCommand
implements|implements
name|Command
implements|,
name|SessionAware
block|{
specifier|private
name|String
name|command
decl_stmt|;
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|private
name|OutputStream
name|out
decl_stmt|;
specifier|private
name|OutputStream
name|err
decl_stmt|;
specifier|private
name|ExitCallback
name|callback
decl_stmt|;
specifier|private
name|ServerSession
name|session
decl_stmt|;
specifier|public
name|ShellCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
specifier|public
name|void
name|setInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
specifier|public
name|void
name|setOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
specifier|public
name|void
name|setErrorStream
parameter_list|(
name|OutputStream
name|err
parameter_list|)
block|{
name|this
operator|.
name|err
operator|=
name|err
expr_stmt|;
block|}
specifier|public
name|void
name|setExitCallback
parameter_list|(
name|ExitCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
specifier|public
name|void
name|setSession
parameter_list|(
name|ServerSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|Environment
name|env
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|CommandSession
name|session
init|=
name|commandProcessor
operator|.
name|createSession
argument_list|(
name|in
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|out
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|err
argument_list|)
argument_list|)
decl_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"SCOPE"
argument_list|,
literal|"shell:osgi:*"
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"APPLICATION"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|env
operator|.
name|getEnv
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|session
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Subject
name|subject
init|=
name|this
operator|.
name|session
operator|!=
literal|null
condition|?
name|this
operator|.
name|session
operator|.
name|getAttribute
argument_list|(
name|KarafJaasAuthenticator
operator|.
name|SUBJECT_ATTRIBUTE_KEY
argument_list|)
else|:
literal|null
decl_stmt|;
name|Object
name|result
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|result
operator|=
name|Subject
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
name|session
operator|.
name|format
argument_list|(
name|result
argument_list|,
name|Converter
operator|.
name|INSPECT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|t
operator|instanceof
name|CommandNotFoundException
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Unknown command entered"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Exception caught while executing command"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|put
argument_list|(
name|ConsoleImpl
operator|.
name|LAST_EXCEPTION
argument_list|,
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|instanceof
name|CommandException
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
operator|(
operator|(
name|CommandException
operator|)
name|t
operator|)
operator|.
name|getNiceHelp
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|instanceof
name|CommandNotFoundException
condition|)
block|{
name|String
name|str
init|=
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|a
argument_list|(
literal|"Command not found: "
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
operator|(
operator|(
name|CommandNotFoundException
operator|)
name|t
operator|)
operator|.
name|getCommand
argument_list|()
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD_OFF
argument_list|)
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getBoolean
argument_list|(
name|session
argument_list|,
name|ConsoleImpl
operator|.
name|PRINT_STACK_TRACES
argument_list|)
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|(
name|session
operator|.
name|getConsole
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|t
operator|instanceof
name|CommandException
operator|)
operator|&&
operator|!
operator|(
name|t
operator|instanceof
name|CommandNotFoundException
operator|)
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
literal|"Error executing command: "
operator|+
operator|(
name|t
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
name|t
operator|.
name|getMessage
argument_list|()
else|:
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|(
literal|"Unable to start shell"
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onExit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{ 		}
specifier|protected
name|boolean
name|getBoolean
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|s
init|=
name|session
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|s
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|s
return|;
block|}
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|s
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
modifier|...
name|closeables
parameter_list|)
block|{
for|for
control|(
name|Closeable
name|c
range|:
name|closeables
control|)
block|{
try|try
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
end_class

end_unit

