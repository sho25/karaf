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
name|impl
operator|.
name|console
package|;
end_package

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
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|Function
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
name|threadio
operator|.
name|ThreadIO
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
name|console
operator|.
name|History
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
name|Registry
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
name|SessionFactory
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
name|impl
operator|.
name|console
operator|.
name|parsing
operator|.
name|CommandLineParser
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
name|impl
operator|.
name|console
operator|.
name|parsing
operator|.
name|KarafParser
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
name|support
operator|.
name|ShellUtil
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
name|support
operator|.
name|completers
operator|.
name|FileCompleter
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
name|support
operator|.
name|completers
operator|.
name|FileOrUriCompleter
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
name|support
operator|.
name|completers
operator|.
name|UriCompleter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|EndOfFileException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|UserInterruptException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|impl
operator|.
name|LineReaderImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|impl
operator|.
name|history
operator|.
name|history
operator|.
name|FileHistory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|impl
operator|.
name|history
operator|.
name|history
operator|.
name|MemoryHistory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|impl
operator|.
name|DumbTerminal
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
name|ConsoleSessionImpl
implements|implements
name|Session
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
name|SHELL_HISTORY_MAXSIZE
init|=
literal|"karaf.shell.history.maxSize"
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
literal|"\u001B[1m${USER}\u001B[0m@${APPLICATION}(${SUBSHELL})> "
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
name|ConsoleSessionImpl
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Input stream
specifier|volatile
name|boolean
name|running
decl_stmt|;
specifier|final
name|SessionFactory
name|factory
decl_stmt|;
specifier|final
name|ThreadIO
name|threadIO
decl_stmt|;
specifier|final
name|InputStream
name|in
decl_stmt|;
specifier|final
name|PrintStream
name|out
decl_stmt|;
specifier|final
name|PrintStream
name|err
decl_stmt|;
specifier|private
name|Runnable
name|closeCallback
decl_stmt|;
specifier|final
name|CommandSession
name|session
decl_stmt|;
specifier|final
name|Registry
name|registry
decl_stmt|;
specifier|final
name|Terminal
name|terminal
decl_stmt|;
specifier|final
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
name|jlineTerminal
decl_stmt|;
specifier|final
name|History
name|history
decl_stmt|;
specifier|final
name|LineReaderImpl
name|reader
decl_stmt|;
specifier|private
name|Thread
name|thread
decl_stmt|;
specifier|public
name|ConsoleSessionImpl
parameter_list|(
name|SessionFactory
name|factory
parameter_list|,
name|CommandProcessor
name|processor
parameter_list|,
name|ThreadIO
name|threadIO
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
name|String
name|encoding
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|)
block|{
comment|// Arguments
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|threadIO
operator|=
name|threadIO
expr_stmt|;
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
name|closeCallback
operator|=
name|closeCallback
expr_stmt|;
comment|// Terminal
if|if
condition|(
name|term
operator|instanceof
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
condition|)
block|{
name|terminal
operator|=
name|term
expr_stmt|;
name|jlineTerminal
operator|=
operator|(
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
operator|)
name|term
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|term
operator|!=
literal|null
condition|)
block|{
name|terminal
operator|=
name|term
expr_stmt|;
comment|//            jlineTerminal = new KarafTerminal(term);
comment|// TODO:JLINE
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
else|else
block|{
try|try
block|{
name|jlineTerminal
operator|=
operator|new
name|DumbTerminal
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|terminal
operator|=
operator|new
name|JLineTerminal
argument_list|(
name|jlineTerminal
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to create terminal"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Console reader
try|try
block|{
name|reader
operator|=
operator|new
name|LineReaderImpl
argument_list|(
name|jlineTerminal
argument_list|,
literal|"karaf"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error opening console reader"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// History
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
name|FileHistory
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
if|if
condition|(
name|reader
operator|.
name|getHistory
argument_list|()
operator|instanceof
name|MemoryHistory
condition|)
block|{
name|String
name|maxSizeStr
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SHELL_HISTORY_MAXSIZE
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxSizeStr
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|MemoryHistory
operator|)
name|this
operator|.
name|reader
operator|.
name|getHistory
argument_list|()
operator|)
operator|.
name|setMaxSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|maxSizeStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|history
operator|=
operator|new
name|HistoryWrapper
argument_list|(
name|reader
operator|.
name|getHistory
argument_list|()
argument_list|)
expr_stmt|;
comment|// Registry
name|registry
operator|=
operator|new
name|RegistryImpl
argument_list|(
name|factory
operator|.
name|getRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|terminal
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|history
argument_list|)
expr_stmt|;
comment|// Completers
name|CommandsCompleter
name|completer
init|=
operator|new
name|CommandsCompleter
argument_list|(
name|factory
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|reader
operator|.
name|setCompleter
argument_list|(
name|completer
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|completer
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|CommandNamesCompleter
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|FileCompleter
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|UriCompleter
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|FileOrUriCompleter
argument_list|()
argument_list|)
expr_stmt|;
comment|// Session
name|session
operator|=
name|processor
operator|.
name|createSession
argument_list|(
name|jlineTerminal
operator|.
name|input
argument_list|()
argument_list|,
name|jlineTerminal
operator|.
name|output
argument_list|()
argument_list|,
name|jlineTerminal
operator|.
name|output
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|sysProps
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|sysProps
operator|.
name|keySet
argument_list|()
control|)
block|{
name|session
operator|.
name|put
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|sysProps
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|put
argument_list|(
literal|".session"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|".commandSession"
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|".jline.reader"
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|".jline.terminal"
argument_list|,
name|reader
operator|.
name|getTerminal
argument_list|()
argument_list|)
expr_stmt|;
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
name|session
operator|.
name|put
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|,
literal|"shell:bundle:*"
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|Session
operator|.
name|SUBSHELL
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|Session
operator|.
name|COMPLETION_MODE
argument_list|,
name|loadCompletionMode
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"USER"
argument_list|,
name|ShellUtil
operator|.
name|getCurrentUserName
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"TERM"
argument_list|,
name|terminal
operator|.
name|getType
argument_list|()
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
name|session
operator|.
name|put
argument_list|(
literal|"#LINES"
argument_list|,
call|(
name|Function
call|)
argument_list|(
name|session
argument_list|,
name|arguments
argument_list|)
operator|->
name|Integer
operator|.
name|toString
argument_list|(
name|terminal
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"#COLUMNS"
argument_list|,
call|(
name|Function
call|)
argument_list|(
name|session
argument_list|,
name|arguments
argument_list|)
operator|->
name|Integer
operator|.
name|toString
argument_list|(
name|terminal
operator|.
name|getWidth
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"pid"
argument_list|,
name|getPid
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|currentDir
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setHighlighter
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|jline
operator|.
name|Highlighter
argument_list|(
name|session
argument_list|)
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setParser
argument_list|(
operator|new
name|KarafParser
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Subclasses can override to use a different history file.      *      * @return the history file      */
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
annotation|@
name|Override
specifier|public
name|Terminal
name|getTerminal
parameter_list|()
block|{
return|return
name|terminal
return|;
block|}
specifier|public
name|History
name|getHistory
parameter_list|()
block|{
return|return
name|history
return|;
block|}
annotation|@
name|Override
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
annotation|@
name|Override
specifier|public
name|SessionFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
operator|!
name|running
condition|)
block|{
return|return;
block|}
comment|//        out.println();
try|try
block|{
name|reader
operator|.
name|getHistory
argument_list|()
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
name|running
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|thread
operator|!=
name|Thread
operator|.
name|currentThread
argument_list|()
condition|)
block|{
name|thread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|terminal
operator|instanceof
name|AutoCloseable
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|AutoCloseable
operator|)
name|terminal
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|threadIO
operator|.
name|setStreams
argument_list|(
name|session
operator|.
name|getKeyboard
argument_list|()
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|thread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
name|running
operator|=
literal|true
expr_stmt|;
name|Properties
name|brandingProps
init|=
name|Branding
operator|.
name|loadBrandingProperties
argument_list|(
name|terminal
argument_list|)
decl_stmt|;
name|welcome
argument_list|(
name|brandingProps
argument_list|)
expr_stmt|;
name|setSessionProperties
argument_list|(
name|brandingProps
argument_list|)
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
name|reader
operator|.
name|readLine
argument_list|(
name|getPrompt
argument_list|()
argument_list|)
decl_stmt|;
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
name|UserInterruptException
name|e
parameter_list|)
block|{
comment|// Ignore, loop again
block|}
catch|catch
parameter_list|(
name|EndOfFileException
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
name|ShellUtil
operator|.
name|logException
argument_list|(
name|this
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|threadIO
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|CharSequence
name|commandline
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|command
init|=
name|CommandLineParser
operator|.
name|parse
argument_list|(
name|this
argument_list|,
name|commandline
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|session
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|put
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|session
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getKeyboard
parameter_list|()
block|{
return|return
name|session
operator|.
name|getKeyboard
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrintStream
name|getConsole
parameter_list|()
block|{
return|return
name|session
operator|.
name|getConsole
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|resolveCommand
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// TODO: optimize
if|if
condition|(
operator|!
name|name
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|String
index|[]
name|scopes
init|=
operator|(
operator|(
name|String
operator|)
name|get
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|)
operator|)
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Command
argument_list|>
name|commands
init|=
name|registry
operator|.
name|getCommands
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|scope
range|:
name|scopes
control|)
block|{
for|for
control|(
name|Command
name|command
range|:
name|commands
control|)
block|{
if|if
condition|(
operator|(
name|Session
operator|.
name|SCOPE_GLOBAL
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
operator|||
name|command
operator|.
name|getScope
argument_list|()
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
operator|)
operator|&&
name|command
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|command
operator|.
name|getScope
argument_list|()
operator|+
literal|":"
operator|+
name|name
return|;
block|}
block|}
block|}
block|}
return|return
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|readLine
parameter_list|(
name|String
name|prompt
parameter_list|,
name|Character
name|mask
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|reader
operator|.
name|readLine
argument_list|(
name|prompt
argument_list|,
name|mask
argument_list|)
return|;
block|}
specifier|private
name|String
name|loadCompletionMode
parameter_list|()
block|{
name|String
name|mode
decl_stmt|;
try|try
block|{
name|File
name|shellCfg
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"/org.apache.karaf.shell.cfg"
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|shellCfg
argument_list|)
argument_list|)
expr_stmt|;
name|mode
operator|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"completionMode"
argument_list|)
expr_stmt|;
if|if
condition|(
name|mode
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"completionMode property is not defined in etc/org.apache.karaf.shell.cfg file. Using default completion mode."
argument_list|)
expr_stmt|;
name|mode
operator|=
name|Session
operator|.
name|COMPLETION_MODE_GLOBAL
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't read {}/org.apache.karaf.shell.cfg file. The completion is set to default."
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|)
expr_stmt|;
name|mode
operator|=
name|Session
operator|.
name|COMPLETION_MODE_GLOBAL
expr_stmt|;
block|}
return|return
name|mode
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
try|try
block|{
name|String
name|script
init|=
name|String
operator|.
name|join
argument_list|(
literal|"\n"
argument_list|,
name|Files
operator|.
name|readAllLines
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|scriptFileName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|session
operator|.
name|execute
argument_list|(
name|script
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
block|}
block|}
specifier|protected
name|void
name|welcome
parameter_list|(
name|Properties
name|brandingProps
parameter_list|)
block|{
name|String
name|welcome
init|=
name|brandingProps
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
parameter_list|(
name|Properties
name|brandingProps
parameter_list|)
block|{
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
name|brandingProps
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
name|Branding
operator|.
name|loadBrandingProperties
argument_list|(
name|terminal
argument_list|)
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
name|String
name|getPid
parameter_list|()
block|{
name|String
name|name
init|=
name|ManagementFactory
operator|.
name|getRuntimeMXBean
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|name
operator|.
name|split
argument_list|(
literal|"@"
argument_list|)
decl_stmt|;
return|return
name|parts
index|[
literal|0
index|]
return|;
block|}
block|}
end_class

end_unit

