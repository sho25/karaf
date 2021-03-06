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
name|PrintStream
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
name|LogEventFormatter
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
name|support
operator|.
name|completers
operator|.
name|StringsCompleter
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

begin_comment
comment|/**  * Displays the last log entries  */
end_comment

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
literal|"display"
argument_list|,
name|description
operator|=
literal|"Displays log entries."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|DisplayLog
implements|implements
name|Action
block|{
specifier|public
specifier|final
specifier|static
name|int
name|ERROR_INT
init|=
literal|3
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|int
name|WARN_INT
init|=
literal|4
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|int
name|INFO_INT
init|=
literal|6
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|int
name|DEBUG_INT
init|=
literal|7
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|SSHD_LOGGER
init|=
literal|"org.apache.sshd"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Number of entries to display"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|entries
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Pattern for formatting the output"
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
name|overridenPattern
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-color"
argument_list|,
name|description
operator|=
literal|"Disable syntax coloring of log events"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|noColor
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-l"
argument_list|,
name|aliases
operator|=
block|{
literal|"--level"
block|}
argument_list|,
name|description
operator|=
literal|"The minimal log level to display"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|value
operator|=
name|StringsCompleter
operator|.
name|class
argument_list|,
name|values
operator|=
block|{
literal|"TRACE"
block|,
literal|"DEBUG"
block|,
literal|"INFO"
block|,
literal|"WARN"
block|,
literal|"ERROR"
block|,
literal|"DEFAULT"
block|}
argument_list|)
name|String
name|level
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
annotation|@
name|Reference
name|LogService
name|logService
decl_stmt|;
annotation|@
name|Reference
name|LogEventFormatter
name|formatter
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
specifier|final
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|int
name|minLevel
init|=
name|getMinLevel
argument_list|(
name|level
argument_list|)
decl_stmt|;
name|String
name|sshdLoggerLevel
init|=
name|logService
operator|.
name|getLevel
argument_list|(
name|SSHD_LOGGER
argument_list|)
operator|.
name|get
argument_list|(
name|SSHD_LOGGER
argument_list|)
decl_stmt|;
name|logService
operator|.
name|setLevel
argument_list|(
name|SSHD_LOGGER
argument_list|,
literal|"ERROR"
argument_list|)
expr_stmt|;
name|display
argument_list|(
name|out
argument_list|,
name|minLevel
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|logService
operator|.
name|setLevel
argument_list|(
name|SSHD_LOGGER
argument_list|,
name|sshdLoggerLevel
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|display
parameter_list|(
specifier|final
name|PrintStream
name|out
parameter_list|,
name|int
name|minLevel
parameter_list|)
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
name|printEvent
argument_list|(
name|out
argument_list|,
name|event
argument_list|,
name|minLevel
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|int
name|getMinLevel
parameter_list|(
name|String
name|levelSt
parameter_list|)
block|{
name|int
name|minLevel
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
if|if
condition|(
name|levelSt
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|levelSt
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
literal|"debug"
case|:
name|minLevel
operator|=
name|DEBUG_INT
expr_stmt|;
break|break;
case|case
literal|"info"
case|:
name|minLevel
operator|=
name|INFO_INT
expr_stmt|;
break|break;
case|case
literal|"warn"
case|:
name|minLevel
operator|=
name|WARN_INT
expr_stmt|;
break|break;
case|case
literal|"error"
case|:
name|minLevel
operator|=
name|ERROR_INT
expr_stmt|;
break|break;
block|}
block|}
return|return
name|minLevel
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
name|event
operator|.
name|getLoggerName
argument_list|()
operator|.
name|contains
argument_list|(
name|logger
argument_list|)
return|;
block|}
specifier|protected
name|void
name|printEvent
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|PaxLoggingEvent
name|event
parameter_list|,
name|int
name|minLevel
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
name|int
name|sl
init|=
name|event
operator|.
name|getLevel
argument_list|()
operator|.
name|getSyslogEquivalent
argument_list|()
decl_stmt|;
if|if
condition|(
name|sl
operator|<=
name|minLevel
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
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
comment|// KARAF-3350: Ignore NoClassDefFoundError exceptions
comment|// Those exceptions may happen if the underlying pax-logging service
comment|// bundle has been refreshed somehow.
block|}
block|}
specifier|protected
name|void
name|printEvent
parameter_list|(
specifier|final
name|PrintStream
name|out
parameter_list|,
name|PaxLoggingEvent
name|event
parameter_list|)
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
name|out
operator|.
name|append
argument_list|(
name|formatter
operator|.
name|format
argument_list|(
name|event
argument_list|,
name|overridenPattern
argument_list|,
name|noColor
argument_list|)
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
name|out
operator|.
name|append
argument_list|(
name|formatter
operator|.
name|format
argument_list|(
name|event
argument_list|,
name|overridenPattern
argument_list|,
name|noColor
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

