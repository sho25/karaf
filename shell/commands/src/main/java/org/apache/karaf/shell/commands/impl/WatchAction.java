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
name|commands
operator|.
name|impl
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
name|ByteArrayOutputStream
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
name|util
operator|.
name|ArrayList
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
name|concurrent
operator|.
name|Executors
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
name|ScheduledExecutorService
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
name|TimeUnit
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
name|Action
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
name|Parsing
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
name|CommandLine
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
name|api
operator|.
name|console
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
name|commands
operator|.
name|impl
operator|.
name|WatchAction
operator|.
name|WatchParser
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
name|CommandsCompleter
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
name|parsing
operator|.
name|CommandLineImpl
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
name|parsing
operator|.
name|DefaultParser
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
name|util
operator|.
name|ThreadUtils
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
name|Attributes
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
name|Terminal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|utils
operator|.
name|NonBlockingReader
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"shell"
argument_list|,
name|name
operator|=
literal|"watch"
argument_list|,
name|description
operator|=
literal|"Watches& refreshes the output of a command"
argument_list|)
annotation|@
name|Parsing
argument_list|(
name|WatchParser
operator|.
name|class
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|WatchAction
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|aliases
operator|=
block|{
literal|"--interval"
block|}
argument_list|,
name|description
operator|=
literal|"The interval between executions of the command in seconds"
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
name|long
name|interval
init|=
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-a"
argument_list|,
name|aliases
operator|=
block|{
literal|"--append"
block|}
argument_list|,
name|description
operator|=
literal|"The output should be appended but not clear the console"
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
name|append
init|=
literal|false
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
literal|"command"
argument_list|,
name|description
operator|=
literal|"The command to watch / refresh"
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
name|SubCommandCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|arguments
decl_stmt|;
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
annotation|@
name|Reference
name|SessionFactory
name|sessionFactory
decl_stmt|;
specifier|private
name|ScheduledExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|ThreadUtils
operator|.
name|namedThreadFactory
argument_list|(
literal|"shell:watch"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|abort
decl_stmt|;
specifier|private
name|Thread
name|reading
decl_stmt|;
specifier|private
name|Thread
name|executing
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|arguments
operator|==
literal|null
operator|||
name|arguments
operator|.
name|length
argument_list|()
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
literal|"Argument expected"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|WatchTask
name|watchTask
init|=
operator|new
name|WatchTask
argument_list|()
decl_stmt|;
name|executorService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|watchTask
argument_list|,
literal|0
argument_list|,
name|interval
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
try|try
block|{
name|Terminal
name|terminal
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
name|Terminal
operator|.
name|SignalHandler
name|prev
init|=
name|terminal
operator|.
name|handle
argument_list|(
name|Terminal
operator|.
name|Signal
operator|.
name|INT
argument_list|,
name|this
operator|::
name|abort
argument_list|)
decl_stmt|;
name|Attributes
name|attr
init|=
name|terminal
operator|.
name|enterRawMode
argument_list|()
decl_stmt|;
try|try
block|{
name|reading
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
while|while
condition|(
name|terminal
operator|.
name|reader
argument_list|()
operator|.
name|read
argument_list|(
literal|1
argument_list|)
operator|==
name|NonBlockingReader
operator|.
name|READ_EXPIRED
condition|)
empty_stmt|;
block|}
finally|finally
block|{
name|reading
operator|=
literal|null
expr_stmt|;
name|terminal
operator|.
name|setAttributes
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|terminal
operator|.
name|handle
argument_list|(
name|Terminal
operator|.
name|Signal
operator|.
name|INT
argument_list|,
name|prev
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
comment|// Ignore
block|}
finally|finally
block|{
name|abort
operator|=
literal|true
expr_stmt|;
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|abort
parameter_list|(
name|Terminal
operator|.
name|Signal
name|signal
parameter_list|)
block|{
name|abort
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|reading
operator|!=
literal|null
condition|)
block|{
name|reading
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|executing
operator|!=
literal|null
condition|)
block|{
name|executing
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
class|class
name|WatchTask
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
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|printStream
init|=
operator|new
name|PrintStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
decl_stmt|;
try|try
init|(
name|Session
name|session
init|=
name|sessionFactory
operator|.
name|create
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
argument_list|,
name|printStream
argument_list|,
name|printStream
argument_list|,
name|WatchAction
operator|.
name|this
operator|.
name|session
argument_list|)
init|)
block|{
name|executing
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
name|session
operator|.
name|execute
argument_list|(
name|arguments
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|abort
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ShellUtil
operator|.
name|logException
argument_list|(
name|session
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|executing
operator|=
literal|null
expr_stmt|;
block|}
name|printStream
operator|.
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|abort
condition|)
block|{
if|if
condition|(
operator|!
name|append
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\33[2J"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\33[1;1H"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//Ingore
block|}
block|}
block|}
annotation|@
name|Service
specifier|public
specifier|static
class|class
name|WatchParser
implements|implements
name|Parser
block|{
annotation|@
name|Override
specifier|public
name|CommandLine
name|parse
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|command
parameter_list|,
name|int
name|cursor
parameter_list|)
block|{
name|int
name|n1
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|n1
operator|<
name|command
operator|.
name|length
argument_list|()
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|command
operator|.
name|charAt
argument_list|(
name|n1
argument_list|)
argument_list|)
condition|)
block|{
name|n1
operator|++
expr_stmt|;
if|if
condition|(
name|n1
operator|==
name|command
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
name|int
name|n2
init|=
name|n1
operator|+
literal|1
decl_stmt|;
while|while
condition|(
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|command
operator|.
name|charAt
argument_list|(
name|n2
argument_list|)
argument_list|)
condition|)
block|{
name|n2
operator|++
expr_stmt|;
if|if
condition|(
name|n2
operator|==
name|command
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
operator|new
name|CommandLineImpl
argument_list|(
operator|new
name|String
index|[]
block|{
name|command
operator|.
name|substring
argument_list|(
name|n1
argument_list|)
block|}
argument_list|,
literal|0
argument_list|,
name|cursor
operator|-
name|n1
argument_list|,
name|cursor
argument_list|,
name|command
argument_list|)
return|;
block|}
block|}
name|int
name|n3
init|=
name|n2
operator|+
literal|1
decl_stmt|;
while|while
condition|(
name|n3
operator|<
name|command
operator|.
name|length
argument_list|()
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|command
operator|.
name|charAt
argument_list|(
name|n3
argument_list|)
argument_list|)
condition|)
block|{
name|n3
operator|++
expr_stmt|;
if|if
condition|(
name|n3
operator|==
name|command
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
operator|new
name|CommandLineImpl
argument_list|(
operator|new
name|String
index|[]
block|{
name|command
operator|.
name|substring
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|)
block|,
literal|""
block|}
argument_list|,
name|cursor
operator|>=
name|n2
condition|?
literal|1
else|:
literal|0
argument_list|,
name|cursor
operator|>=
name|n2
condition|?
literal|0
else|:
name|cursor
operator|-
name|n1
argument_list|,
name|cursor
argument_list|,
name|command
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|CommandLineImpl
argument_list|(
operator|new
name|String
index|[]
block|{
name|command
operator|.
name|substring
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|)
block|,
name|command
operator|.
name|substring
argument_list|(
name|n3
argument_list|)
block|}
argument_list|,
name|cursor
operator|>=
name|n3
condition|?
literal|1
else|:
literal|0
argument_list|,
name|cursor
operator|>=
name|n3
condition|?
name|cursor
operator|-
name|n3
else|:
name|cursor
operator|-
name|n1
argument_list|,
name|cursor
argument_list|,
name|command
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|preprocess
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|cmdLine
parameter_list|)
block|{
name|StringBuilder
name|parsed
init|=
operator|new
name|StringBuilder
argument_list|()
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
name|cmdLine
operator|.
name|getArguments
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|arg
init|=
name|cmdLine
operator|.
name|getArguments
argument_list|()
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|parsed
operator|.
name|append
argument_list|(
literal|" \""
argument_list|)
expr_stmt|;
block|}
name|parsed
operator|.
name|append
argument_list|(
name|arg
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|parsed
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|parsed
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
annotation|@
name|Service
specifier|public
specifier|static
class|class
name|SubCommandCompleter
implements|implements
name|Completer
block|{
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|String
name|arg
init|=
name|commandLine
operator|.
name|getCursorArgument
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|commandLine
operator|.
name|getArgumentPosition
argument_list|()
decl_stmt|;
name|CommandLine
name|cmdLine
init|=
operator|new
name|DefaultParser
argument_list|()
operator|.
name|parse
argument_list|(
name|session
argument_list|,
name|arg
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|Completer
name|completer
init|=
name|session
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|CommandsCompleter
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|cands
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|res
init|=
name|completer
operator|.
name|complete
argument_list|(
name|session
argument_list|,
name|cmdLine
argument_list|,
name|cands
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|cand
range|:
name|cands
control|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|arg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|cmdLine
operator|.
name|getBufferPosition
argument_list|()
operator|-
name|cmdLine
operator|.
name|getArgumentPosition
argument_list|()
argument_list|)
operator|+
name|cand
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|res
operator|>=
literal|0
condition|)
block|{
name|res
operator|+=
name|commandLine
operator|.
name|getBufferPosition
argument_list|()
operator|-
name|commandLine
operator|.
name|getArgumentPosition
argument_list|()
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
block|}
block|}
end_class

end_unit

