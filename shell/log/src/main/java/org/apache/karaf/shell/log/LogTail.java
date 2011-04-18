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
name|shell
operator|.
name|log
package|;
end_package

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
name|LinkedBlockingQueue
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
name|log
operator|.
name|layout
operator|.
name|PatternConverter
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
name|log
operator|.
name|layout
operator|.
name|PatternParser
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
literal|"Continuously display log entries."
argument_list|)
specifier|public
class|class
name|LogTail
extends|extends
name|DisplayLog
block|{
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|PatternConverter
name|cnv
init|=
operator|new
name|PatternParser
argument_list|(
name|overridenPattern
operator|!=
literal|null
condition|?
name|overridenPattern
else|:
name|pattern
argument_list|)
operator|.
name|parse
argument_list|()
decl_stmt|;
specifier|final
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|le
init|=
name|events
operator|.
name|getElements
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
operator|(
name|logger
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|event
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|checkIfFromRequestedLog
argument_list|(
name|event
argument_list|)
operator|)
condition|)
block|{
name|display
argument_list|(
name|cnv
argument_list|,
name|event
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|event
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|logger
operator|==
literal|null
operator|)
condition|)
block|{
name|display
argument_list|(
name|cnv
argument_list|,
name|event
argument_list|,
name|out
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
name|events
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
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
operator|(
name|logger
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|event
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|checkIfFromRequestedLog
argument_list|(
name|event
argument_list|)
operator|)
condition|)
block|{
name|display
argument_list|(
name|cnv
argument_list|,
name|event
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|event
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|logger
operator|==
literal|null
operator|)
condition|)
block|{
name|display
argument_list|(
name|cnv
argument_list|,
name|event
argument_list|,
name|out
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
name|events
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

