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
name|Map
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
name|jline
operator|.
name|console
operator|.
name|ConsoleReader
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|console
operator|.
name|history
operator|.
name|PersistentHistory
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
name|gogo
operator|.
name|runtime
operator|.
name|Parser
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
name|CloseShellException
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
name|Completer
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
name|completer
operator|.
name|CommandsCompleter
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
specifier|public
specifier|static
specifier|final
name|String
name|LAST_EXCEPTION
init|=
literal|"karaf.lastException"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IGNORE_INTERRUPTS
init|=
literal|"karaf.ignoreInterrupts"
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
specifier|protected
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
specifier|volatile
specifier|private
name|boolean
name|running
decl_stmt|;
specifier|volatile
specifier|private
name|boolean
name|eof
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
name|Thread
name|thread
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
name|Runnable
name|closeCallback
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
literal|"shell:bundles:*"
argument_list|)
expr_stmt|;
name|this
operator|.
name|closeCallback
operator|=
name|closeCallback
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
specifier|final
name|File
name|file
init|=
name|getHistoryFile
argument_list|()
decl_stmt|;
try|try
block|{
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
name|setHistory
argument_list|(
operator|new
name|KarafFileHistory
argument_list|(
name|file
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
name|error
argument_list|(
literal|"Can not read history from file "
operator|+
name|file
operator|+
literal|". Using in memory history"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|put
argument_list|(
literal|".jline.history"
argument_list|,
name|reader
operator|.
name|getHistory
argument_list|()
argument_list|)
expr_stmt|;
name|Completer
name|completer
init|=
name|createCompleter
argument_list|()
decl_stmt|;
if|if
condition|(
name|completer
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|addCompleter
argument_list|(
operator|new
name|CompleterAsCompletor
argument_list|(
name|completer
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"jline.nobell"
argument_list|)
condition|)
block|{
name|reader
operator|.
name|setBellEnabled
argument_list|(
literal|false
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
comment|/**      * Subclasses can override to use a different history file.      * @return      */
specifier|protected
name|File
name|getHistoryFile
parameter_list|()
block|{
name|String
name|defaultHistoryPath
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
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.history"
argument_list|,
name|defaultHistoryPath
argument_list|)
argument_list|)
return|;
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
if|if
condition|(
name|reader
operator|.
name|getHistory
argument_list|()
operator|instanceof
name|PersistentHistory
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|PersistentHistory
operator|)
name|reader
operator|.
name|getHistory
argument_list|()
operator|)
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|running
operator|=
literal|false
expr_stmt|;
name|CommandSessionHolder
operator|.
name|unset
argument_list|()
expr_stmt|;
name|pipe
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ThreadLocal
argument_list|<
name|CommandSessionHolder
argument_list|>
name|consoleState
init|=
operator|new
name|ThreadLocal
argument_list|<
name|CommandSessionHolder
argument_list|>
argument_list|()
decl_stmt|;
name|thread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
name|CommandSessionHolder
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
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
name|setSessionProperties
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
name|executeScript
argument_list|(
name|scriptFileName
argument_list|)
expr_stmt|;
while|while
condition|(
name|running
condition|)
block|{
try|try
block|{
name|String
name|command
init|=
name|readAndParseCommand
argument_list|()
decl_stmt|;
if|if
condition|(
name|command
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
name|command
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
name|CloseShellException
name|e
parameter_list|)
block|{
break|break;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|close
argument_list|()
expr_stmt|;
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
specifier|private
name|void
name|logException
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
specifier|private
name|String
name|readAndParseCommand
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|command
init|=
literal|null
decl_stmt|;
name|boolean
name|loop
init|=
literal|true
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|loop
condition|)
block|{
name|checkInterrupt
argument_list|()
expr_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|(
name|first
condition|?
name|getPrompt
argument_list|()
else|:
literal|"> "
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
if|if
condition|(
name|command
operator|==
literal|null
condition|)
block|{
name|command
operator|=
name|line
expr_stmt|;
block|}
else|else
block|{
name|command
operator|+=
literal|" "
operator|+
name|line
expr_stmt|;
block|}
if|if
condition|(
name|reader
operator|.
name|getHistory
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|reader
operator|.
name|getHistory
argument_list|()
operator|.
name|add
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reader
operator|.
name|getHistory
argument_list|()
operator|.
name|replace
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
try|try
block|{
operator|new
name|Parser
argument_list|(
name|command
argument_list|)
operator|.
name|program
argument_list|()
expr_stmt|;
name|loop
operator|=
literal|false
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|loop
operator|=
literal|true
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|command
return|;
block|}
specifier|private
name|void
name|executeScript
parameter_list|(
name|String
name|scriptFileName
parameter_list|)
block|{
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
block|}
specifier|protected
name|boolean
name|getBoolean
parameter_list|(
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
specifier|protected
name|void
name|welcome
parameter_list|()
block|{
name|Properties
name|props
init|=
name|loadBrandingProperties
argument_list|()
decl_stmt|;
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
specifier|protected
name|void
name|setSessionProperties
parameter_list|()
block|{
name|Properties
name|props
init|=
name|loadBrandingProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"session."
argument_list|)
condition|)
block|{
name|session
operator|.
name|put
argument_list|(
name|key
operator|.
name|substring
argument_list|(
literal|"session."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Completer
name|createCompleter
parameter_list|()
block|{
return|return
operator|new
name|CommandsCompleter
argument_list|(
name|session
argument_list|)
return|;
block|}
specifier|protected
name|Properties
name|loadBrandingProperties
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
literal|"org/apache/karaf/shell/console/branding.properties"
argument_list|)
expr_stmt|;
name|loadProps
argument_list|(
name|props
argument_list|,
literal|"org/apache/karaf/branding/branding.properties"
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
specifier|protected
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
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|prompt
operator|=
name|p
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Properties
name|properties
init|=
name|loadBrandingProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"prompt"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|prompt
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"prompt"
argument_list|)
expr_stmt|;
comment|// we put the PROMPT in ConsoleSession to avoid to read
comment|// the properties file each time.
name|session
operator|.
name|put
argument_list|(
name|PROMPT
argument_list|,
name|prompt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prompt
operator|=
name|DEFAULT_PROMPT
expr_stmt|;
block|}
block|}
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
name|Thread
operator|.
name|interrupted
argument_list|()
operator|||
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
name|thread
operator|.
name|interrupt
argument_list|()
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
if|if
condition|(
name|eof
operator|&&
name|queue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
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
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|queue
operator|.
name|size
argument_list|()
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
init|=
name|terminal
operator|.
name|readCharacter
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|4
operator|&&
operator|!
name|getBoolean
argument_list|(
name|IGNORE_INTERRUPTS
argument_list|)
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
operator|&&
operator|!
name|getBoolean
argument_list|(
name|IGNORE_INTERRUPTS
argument_list|)
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
name|clear
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
name|eof
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|queue
operator|.
name|put
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{                 }
block|}
block|}
block|}
block|}
end_class

end_unit

