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
literal|"display-exception"
argument_list|,
name|description
operator|=
literal|"Displays the last occurred exception from the log."
argument_list|)
specifier|public
class|class
name|DisplayException
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"logger"
argument_list|,
name|description
operator|=
literal|"The name of the logger. This can be ROOT, ALL, or the name of a logger specified in the org.ops4j.pax.logger.cfg file."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|logger
decl_stmt|;
specifier|protected
name|LruList
name|events
decl_stmt|;
specifier|public
name|LruList
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
specifier|public
name|void
name|setEvents
parameter_list|(
name|LruList
name|events
parameter_list|)
block|{
name|this
operator|.
name|events
operator|=
name|events
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|PaxLoggingEvent
name|throwableEvent
init|=
literal|null
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
name|Integer
operator|.
name|MAX_VALUE
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
comment|// if this is an exception, and the log is the same as the requested log,
comment|// then save this exception and continue iterating from oldest to newest
if|if
condition|(
operator|(
name|event
operator|.
name|getThrowableStrRep
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|logger
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
name|throwableEvent
operator|=
name|event
expr_stmt|;
comment|// Do not break, as we iterate from the oldest to the newest event
block|}
elseif|else
if|if
condition|(
operator|(
name|event
operator|.
name|getThrowableStrRep
argument_list|()
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
comment|// now check if there has been no log passed in, and if this is an exception
comment|// then save this exception and continue iterating from oldest to newest
name|throwableEvent
operator|=
name|event
expr_stmt|;
block|}
block|}
if|if
condition|(
name|throwableEvent
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|r
range|:
name|throwableEvent
operator|.
name|getThrowableStrRep
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|boolean
name|checkIfFromRequestedLog
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|)
block|{
return|return
operator|(
name|event
operator|.
name|getLoggerName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|logger
argument_list|)
operator|>=
literal|0
operator|)
condition|?
literal|true
else|:
literal|false
return|;
block|}
block|}
end_class

end_unit

