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
name|core
operator|.
name|internal
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
name|util
operator|.
name|Dictionary
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
name|Level
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
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
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_class
specifier|public
class|class
name|LogServiceImpl
implements|implements
name|LogService
block|{
specifier|static
specifier|final
name|String
name|CONFIGURATION_PID
init|=
literal|"org.ops4j.pax.logging"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ROOT_LOGGER_PREFIX
init|=
literal|"log4j.rootLogger"
decl_stmt|;
specifier|static
specifier|final
name|String
name|LOGGER_PREFIX
init|=
literal|"log4j.logger."
decl_stmt|;
specifier|static
specifier|final
name|String
name|ROOT_LOGGER
init|=
literal|"ROOT"
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
specifier|private
specifier|final
name|LruList
name|events
decl_stmt|;
specifier|public
name|LogServiceImpl
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|,
name|LruList
name|events
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
name|this
operator|.
name|events
operator|=
name|events
expr_stmt|;
block|}
specifier|public
name|Level
name|getLevel
parameter_list|()
block|{
return|return
name|getLevel
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Level
name|getLevel
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
name|Configuration
name|cfg
decl_stmt|;
try|try
block|{
name|cfg
operator|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|CONFIGURATION_PID
argument_list|,
literal|null
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
literal|"Error retrieving Log information from config admin"
argument_list|,
name|e
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Dictionary
name|props
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|ROOT_LOGGER
operator|.
name|equalsIgnoreCase
argument_list|(
name|logger
argument_list|)
condition|)
block|{
name|logger
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|val
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|String
name|prop
decl_stmt|;
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|prop
operator|=
name|ROOT_LOGGER_PREFIX
expr_stmt|;
block|}
else|else
block|{
name|prop
operator|=
name|LOGGER_PREFIX
operator|+
name|logger
expr_stmt|;
block|}
name|val
operator|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|val
operator|=
name|getLevelFromProperty
argument_list|(
name|val
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
operator|||
name|logger
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|int
name|idx
init|=
name|logger
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|logger
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|=
name|logger
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Level
operator|.
name|valueOf
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|Level
name|level
parameter_list|)
block|{
name|setLevel
argument_list|(
literal|null
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|logger
parameter_list|,
name|Level
name|logLevel
parameter_list|)
block|{
if|if
condition|(
name|ROOT_LOGGER
operator|.
name|equalsIgnoreCase
argument_list|(
name|logger
argument_list|)
condition|)
block|{
name|logger
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|logLevel
operator|==
name|Level
operator|.
name|DEFAULT
operator|&&
name|logger
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can not unset the ROOT logger"
argument_list|)
throw|;
block|}
name|Configuration
name|cfg
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|Dictionary
name|props
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|level
init|=
name|logLevel
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|val
decl_stmt|;
name|String
name|prop
decl_stmt|;
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|prop
operator|=
name|ROOT_LOGGER_PREFIX
expr_stmt|;
block|}
else|else
block|{
name|prop
operator|=
name|LOGGER_PREFIX
operator|+
name|logger
expr_stmt|;
block|}
name|val
operator|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
name|Level
operator|.
name|isDefault
argument_list|(
name|level
argument_list|)
condition|)
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|val
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|level
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|val
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|val
operator|=
name|level
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|level
operator|+
name|val
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|props
operator|.
name|remove
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|props
operator|.
name|put
argument_list|(
name|prop
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|cfg
operator|.
name|update
argument_list|(
name|props
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
literal|"Error writing log config to config admin"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|checkIfFromRequestedLog
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|,
name|String
name|logger
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
specifier|private
name|String
name|getLevelFromProperty
parameter_list|(
name|String
name|prop
parameter_list|)
block|{
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|String
name|val
init|=
name|prop
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|val
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|==
literal|0
condition|)
block|{
name|val
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
block|}
specifier|private
name|Configuration
name|getConfiguration
parameter_list|()
block|{
try|try
block|{
return|return
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|CONFIGURATION_PID
argument_list|,
literal|null
argument_list|)
return|;
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
literal|"Error retrieving Log information from config admin"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|events
operator|.
name|getElements
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|getEvents
parameter_list|(
name|int
name|maxNum
parameter_list|)
block|{
return|return
name|events
operator|.
name|getElements
argument_list|(
name|maxNum
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearEvents
parameter_list|()
block|{
name|events
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PaxLoggingEvent
name|getLastException
parameter_list|(
name|String
name|logger
parameter_list|)
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
name|getEvents
argument_list|()
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
argument_list|,
name|logger
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
return|return
name|throwableEvent
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addAppender
parameter_list|(
name|PaxAppender
name|appender
parameter_list|)
block|{
name|events
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAppender
parameter_list|(
name|PaxAppender
name|appender
parameter_list|)
block|{
name|events
operator|.
name|removeAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLevelSt
parameter_list|()
block|{
return|return
name|getLevel
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLevelSt
parameter_list|(
name|String
name|logger
parameter_list|)
block|{
return|return
name|getLevel
argument_list|(
name|logger
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLevelSt
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|setLevel
argument_list|(
name|convertToLevel
argument_list|(
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLevelSt
parameter_list|(
name|String
name|logger
parameter_list|,
name|String
name|level
parameter_list|)
block|{
name|setLevel
argument_list|(
name|logger
argument_list|,
name|convertToLevel
argument_list|(
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Level
name|convertToLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|level
operator|=
name|level
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
name|Level
name|res
init|=
name|Level
operator|.
name|valueOf
argument_list|(
name|level
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"level must be set to TRACE, DEBUG, INFO, WARN or ERROR (or DEFAULT to unset it)"
argument_list|)
throw|;
block|}
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

