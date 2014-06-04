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
name|log
operator|.
name|command
package|;
end_package

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
name|ExecutorService
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
name|LinkedBlockingQueue
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
name|log
operator|.
name|core
operator|.
name|LogService
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
name|ops4j
operator|.
name|pax
operator|.
name|logging
operator|.
name|spi
operator|.
name|PaxAppender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|logging
operator|.
name|spi
operator|.
name|PaxLoggingEvent
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"log"
argument_list|,
name|name
operator|=
literal|"tail"
argument_list|,
name|description
operator|=
literal|"Continuously display log entries. Use ctrl-c to quit this command"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|LogTail
extends|extends
name|DisplayLog
block|{
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
annotation|@
name|Reference
name|LogService
name|logService
decl_stmt|;
specifier|private
name|ExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
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
name|PrintEventThread
name|printThread
init|=
operator|new
name|PrintEventThread
argument_list|()
decl_stmt|;
name|ReadKeyBoardThread
name|readKeyboardThread
init|=
operator|new
name|ReadKeyBoardThread
argument_list|(
name|this
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
decl_stmt|;
name|executorService
operator|.
name|execute
argument_list|(
name|printThread
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|execute
argument_list|(
name|readKeyboardThread
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|InterruptedException
name|e
parameter_list|)
block|{
break|break;
block|}
block|}
name|printThread
operator|.
name|abort
argument_list|()
expr_stmt|;
name|readKeyboardThread
operator|.
name|abort
argument_list|()
expr_stmt|;
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
class|class
name|ReadKeyBoardThread
implements|implements
name|Runnable
block|{
specifier|private
name|LogTail
name|logTail
decl_stmt|;
specifier|private
name|Thread
name|sessionThread
decl_stmt|;
name|boolean
name|readKeyboard
init|=
literal|true
decl_stmt|;
specifier|public
name|ReadKeyBoardThread
parameter_list|(
name|LogTail
name|logtail
parameter_list|,
name|Thread
name|thread
parameter_list|)
block|{
name|this
operator|.
name|logTail
operator|=
name|logtail
expr_stmt|;
name|this
operator|.
name|sessionThread
operator|=
name|thread
expr_stmt|;
block|}
specifier|public
name|void
name|abort
parameter_list|()
block|{
name|readKeyboard
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
name|readKeyboard
condition|)
block|{
try|try
block|{
name|int
name|c
init|=
name|this
operator|.
name|logTail
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
name|this
operator|.
name|sessionThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
break|break;
block|}
block|}
block|}
block|}
class|class
name|PrintEventThread
implements|implements
name|Runnable
block|{
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|boolean
name|doDisplay
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|run
parameter_list|()
block|{
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|le
init|=
name|logService
operator|.
name|getEvents
argument_list|(
name|entries
operator|==
literal|0
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|entries
argument_list|)
decl_stmt|;
for|for
control|(
name|PaxLoggingEvent
name|event
range|:
name|le
control|)
block|{
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
name|printEvent
argument_list|(
name|out
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Tail
specifier|final
name|BlockingQueue
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|PaxLoggingEvent
argument_list|>
argument_list|()
decl_stmt|;
name|PaxAppender
name|appender
init|=
operator|new
name|PaxAppender
argument_list|()
block|{
specifier|public
name|void
name|doAppend
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|logService
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
while|while
condition|(
name|doDisplay
condition|)
block|{
name|PaxLoggingEvent
name|event
init|=
name|queue
operator|.
name|take
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
name|printEvent
argument_list|(
name|out
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
finally|finally
block|{
name|logService
operator|.
name|removeAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
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
block|}
block|}
end_class

end_unit

