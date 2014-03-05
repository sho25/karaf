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
name|ByteArrayOutputStream
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
name|PrintStream
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
literal|true
argument_list|)
annotation|@
name|Completion
argument_list|(
name|CommandsCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
index|[]
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
argument_list|()
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
name|StringBuilder
name|command
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|arguments
control|)
block|{
name|command
operator|.
name|append
argument_list|(
name|arg
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|WatchTask
name|watchTask
init|=
operator|new
name|WatchTask
argument_list|(
name|command
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
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
name|session
operator|.
name|getKeyboard
argument_list|()
operator|.
name|read
argument_list|()
expr_stmt|;
name|watchTask
operator|.
name|abort
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|watchTask
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
class|class
name|WatchTask
implements|implements
name|Runnable
block|{
specifier|private
specifier|final
name|String
name|command
decl_stmt|;
name|Session
name|session
decl_stmt|;
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
literal|null
decl_stmt|;
name|PrintStream
name|printStream
init|=
literal|null
decl_stmt|;
name|boolean
name|doDisplay
init|=
literal|true
decl_stmt|;
specifier|public
name|WatchTask
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
name|run
parameter_list|()
block|{
try|try
block|{
name|byteArrayOutputStream
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|printStream
operator|=
operator|new
name|PrintStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
name|session
operator|=
name|sessionFactory
operator|.
name|create
argument_list|(
literal|null
argument_list|,
name|printStream
argument_list|,
name|printStream
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
name|WatchAction
operator|.
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|)
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
name|WatchAction
operator|.
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|Session
operator|.
name|SUBSHELL
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|output
init|=
literal|""
decl_stmt|;
try|try
block|{
name|session
operator|.
name|execute
argument_list|(
name|command
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
name|output
operator|=
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
name|doDisplay
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
name|output
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
name|byteArrayOutputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|session
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
comment|//Ingore
block|}
block|}
specifier|public
name|void
name|abort
parameter_list|()
block|{
name|doDisplay
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|this
operator|.
name|session
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|byteArrayOutputStream
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|byteArrayOutputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|printStream
operator|!=
literal|null
condition|)
block|{
name|printStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

