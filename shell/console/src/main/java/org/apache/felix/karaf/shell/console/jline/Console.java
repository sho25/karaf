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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|jline
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InterruptedIOException
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|AnsiWindowsTerminal
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|ConsoleReader
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
name|jline
operator|.
name|UnsupportedTerminal
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
name|Completer
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
name|completer
operator|.
name|AggregateCompleter
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
name|completer
operator|.
name|SessionScopeCompleter
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
name|command
operator|.
name|CommandProcessor
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
name|command
operator|.
name|CommandSession
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
name|command
operator|.
name|Converter
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
name|Console
implements|implements
name|Runnable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SHELL_INIT_SCRIPT
init|=
literal|"karaf.shell.init.script"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROMPT
init|=
literal|"PROMPT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PROMPT
init|=
literal|"\u001B[1m${USER}\u001B[0m@${APPLICATION}> "
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PRINT_STACK_TRACES
init|=
literal|"karaf.printStackTraces"
decl_stmt|;
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
name|Console
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CommandSession
name|session
decl_stmt|;
specifier|private
name|ConsoleReader
name|reader
decl_stmt|;
specifier|private
name|BlockingQueue
argument_list|<
name|Integer
argument_list|>
name|queue
decl_stmt|;
specifier|private
name|boolean
name|interrupt
decl_stmt|;
specifier|private
name|Thread
name|pipe
decl_stmt|;
specifier|private
name|boolean
name|running
decl_stmt|;
specifier|private
name|Runnable
name|closeCallback
decl_stmt|;
specifier|private
name|Terminal
name|terminal
decl_stmt|;
specifier|private
name|InputStream
name|consoleInput
decl_stmt|;
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|private
name|PrintStream
name|out
decl_stmt|;
specifier|private
name|PrintStream
name|err
decl_stmt|;
specifier|private
name|Callable
argument_list|<
name|Boolean
argument_list|>
name|printStackTraces
decl_stmt|;
specifier|public
name|Console
parameter_list|(
name|CommandProcessor
name|processor
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Terminal
name|term
parameter_list|,
name|Completer
name|completer
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|,
name|Callable
argument_list|<
name|Boolean
argument_list|>
name|printStackTraces
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|err
operator|=
name|err
expr_stmt|;
name|this
operator|.
name|queue
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
name|this
operator|.
name|terminal
operator|=
name|term
operator|==
literal|null
condition|?
operator|new
name|UnsupportedTerminal
argument_list|()
else|:
name|term
expr_stmt|;
name|this
operator|.
name|consoleInput
operator|=
operator|new
name|ConsoleInputStream
argument_list|()
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|processor
operator|.
name|createSession
argument_list|(
name|this
operator|.
name|consoleInput
argument_list|,
name|this
operator|.
name|out
argument_list|,
name|this
operator|.
name|err
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
literal|"SCOPE"
argument_list|,
literal|"shell:osgi:*"
argument_list|)
expr_stmt|;
name|this
operator|.
name|closeCallback
operator|=
name|closeCallback
expr_stmt|;
name|this
operator|.
name|printStackTraces
operator|=
name|printStackTraces
expr_stmt|;
name|reader
operator|=
operator|new
name|ConsoleReader
argument_list|(
name|this
operator|.
name|consoleInput
argument_list|,
operator|new
name|PrintWriter
argument_list|(
name|this
operator|.
name|out
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"keybinding.properties"
argument_list|)
argument_list|,
name|this
operator|.
name|terminal
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
argument_list|,
literal|".karaf/karaf.history"
argument_list|)
decl_stmt|;
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|reader
operator|.
name|getHistory
argument_list|()
operator|.
name|setHistoryFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|completer
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|addCompletor
argument_list|(
operator|new
name|CompleterAsCompletor
argument_list|(
operator|new
name|AggregateCompleter
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|completer
argument_list|,
operator|new
name|SessionScopeCompleter
argument_list|(
name|session
argument_list|,
name|completer
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|pipe
operator|=
operator|new
name|Thread
argument_list|(
operator|new
name|Pipe
argument_list|()
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|setName
argument_list|(
literal|"gogo shell pipe thread"
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CommandSession
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|//System.err.println("Closing");
name|running
operator|=
literal|false
expr_stmt|;
name|pipe
operator|.
name|interrupt
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|interrupted
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|running
operator|=
literal|true
expr_stmt|;
name|pipe
operator|.
name|start
argument_list|()
expr_stmt|;
name|welcome
argument_list|()
expr_stmt|;
name|String
name|scriptFileName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SHELL_INIT_SCRIPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|scriptFileName
operator|!=
literal|null
condition|)
block|{
name|Reader
name|r
init|=
literal|null
decl_stmt|;
try|try
block|{
name|File
name|scriptFile
init|=
operator|new
name|File
argument_list|(
name|scriptFileName
argument_list|)
decl_stmt|;
name|r
operator|=
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
expr_stmt|;
name|CharArrayWriter
name|w
init|=
operator|new
name|CharArrayWriter
argument_list|()
decl_stmt|;
name|int
name|n
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|8192
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|r
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|w
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|execute
argument_list|(
operator|new
name|String
argument_list|(
name|w
operator|.
name|toCharArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Error in initialization script"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error in initialization script: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|r
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
while|while
condition|(
name|running
condition|)
block|{
try|try
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|(
name|getPrompt
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
comment|//session.getConsole().println("Executing: " + line);
name|Object
name|result
init|=
name|session
operator|.
name|execute
argument_list|(
name|line
argument_list|)
decl_stmt|;
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
name|InterruptedIOException
name|e
parameter_list|)
block|{
comment|//System.err.println("^C");
comment|// TODO: interrupt current thread
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
name|printStackTraces
operator|.
name|call
argument_list|()
condition|)
block|{
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
block|}
else|else
block|{
name|session
operator|.
name|getConsole
argument_list|()
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
comment|//System.err.println("Exiting console...");
if|if
condition|(
name|closeCallback
operator|!=
literal|null
condition|)
block|{
name|closeCallback
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|welcome
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|loadProps
argument_list|(
name|props
argument_list|,
literal|"org/apache/felix/karaf/shell/console/branding.properties"
argument_list|)
expr_stmt|;
name|loadProps
argument_list|(
name|props
argument_list|,
literal|"org/apache/felix/karaf/branding/branding.properties"
argument_list|)
expr_stmt|;
name|String
name|welcome
init|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"welcome"
argument_list|)
decl_stmt|;
if|if
condition|(
name|welcome
operator|!=
literal|null
operator|&&
name|welcome
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
name|welcome
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|loadProps
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|resource
parameter_list|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
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
specifier|protected
name|String
name|getPrompt
parameter_list|()
block|{
try|try
block|{
name|String
name|prompt
decl_stmt|;
try|try
block|{
name|Object
name|p
init|=
name|session
operator|.
name|get
argument_list|(
name|PROMPT
argument_list|)
decl_stmt|;
name|prompt
operator|=
name|p
operator|!=
literal|null
condition|?
name|p
operator|.
name|toString
argument_list|()
else|:
name|DEFAULT_PROMPT
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|prompt
operator|=
name|DEFAULT_PROMPT
expr_stmt|;
block|}
name|Matcher
name|matcher
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$\\{([^}]+)\\}"
argument_list|)
operator|.
name|matcher
argument_list|(
name|prompt
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|Object
name|rep
init|=
name|session
operator|.
name|get
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rep
operator|!=
literal|null
condition|)
block|{
name|prompt
operator|=
name|prompt
operator|.
name|replace
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
argument_list|,
name|rep
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|reset
argument_list|(
name|prompt
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|prompt
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|"$ "
return|;
block|}
block|}
specifier|private
name|void
name|checkInterrupt
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|interrupt
condition|)
block|{
name|interrupt
operator|=
literal|false
expr_stmt|;
throw|throw
operator|new
name|InterruptedIOException
argument_list|(
literal|"Keyboard interruption"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|interrupt
parameter_list|()
block|{
name|interrupt
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
class|class
name|ConsoleInputStream
extends|extends
name|InputStream
block|{
specifier|private
name|int
name|read
parameter_list|(
name|boolean
name|wait
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|running
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|checkInterrupt
argument_list|()
expr_stmt|;
name|Integer
name|i
decl_stmt|;
if|if
condition|(
name|wait
condition|)
block|{
try|try
block|{
name|i
operator|=
name|queue
operator|.
name|take
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InterruptedIOException
argument_list|()
throw|;
block|}
name|checkInterrupt
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|i
operator|=
name|queue
operator|.
name|poll
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|i
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|read
argument_list|(
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
name|b
index|[]
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|off
operator|<
literal|0
operator|||
name|len
argument_list|<
literal|0
operator|||
name|len
argument_list|>
name|b
operator|.
name|length
operator|-
name|off
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|nb
init|=
literal|1
decl_stmt|;
name|int
name|i
init|=
name|read
argument_list|(
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|<
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|b
index|[
name|off
operator|++
index|]
operator|=
operator|(
name|byte
operator|)
name|i
expr_stmt|;
while|while
condition|(
name|nb
operator|<
name|len
condition|)
block|{
name|i
operator|=
name|read
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
literal|0
condition|)
block|{
return|return
name|nb
return|;
block|}
name|b
index|[
name|off
operator|++
index|]
operator|=
operator|(
name|byte
operator|)
name|i
expr_stmt|;
name|nb
operator|++
expr_stmt|;
block|}
return|return
name|nb
return|;
block|}
block|}
specifier|private
class|class
name|Pipe
implements|implements
name|Runnable
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
name|running
condition|)
block|{
try|try
block|{
name|int
name|c
decl_stmt|;
if|if
condition|(
name|terminal
operator|instanceof
name|AnsiWindowsTerminal
condition|)
block|{
name|c
operator|=
operator|(
operator|(
name|AnsiWindowsTerminal
operator|)
name|terminal
operator|)
operator|.
name|readDirectChar
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
name|terminal
operator|.
name|readCharacter
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
name|queue
operator|.
name|put
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|4
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"^D"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|3
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"^C"
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getCursorBuffer
argument_list|()
operator|.
name|clearBuffer
argument_list|()
expr_stmt|;
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|queue
operator|.
name|put
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return;
block|}
block|}
block|}
finally|finally
block|{
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

