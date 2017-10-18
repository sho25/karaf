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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
implements|,
name|PaxAppender
block|{
specifier|static
specifier|final
name|String
name|CONFIGURATION_PID
init|=
literal|"org.ops4j.pax.logging"
decl_stmt|;
specifier|private
specifier|final
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
specifier|private
specifier|final
name|CircularBuffer
argument_list|<
name|PaxLoggingEvent
argument_list|>
name|buffer
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PaxAppender
argument_list|>
name|appenders
decl_stmt|;
specifier|public
name|LogServiceImpl
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|,
name|int
name|size
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
name|appenders
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|buffer
operator|=
operator|new
name|CircularBuffer
argument_list|<>
argument_list|(
name|size
argument_list|,
name|PaxLoggingEvent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|LogServiceInternal
name|getDelegate
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|.
name|get
argument_list|(
literal|"log4j.rootLogger"
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|LogServiceLog4j1Impl
argument_list|(
name|config
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|get
argument_list|(
literal|"log4j2.rootLogger.level"
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|LogServiceLog4j2Impl
argument_list|(
name|config
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|get
argument_list|(
literal|"org.ops4j.pax.logging.log4j2.config.file"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|file
init|=
name|config
operator|.
name|get
argument_list|(
literal|"org.ops4j.pax.logging.log4j2.config.file"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
return|return
operator|new
name|LogServiceLog4j2XmlImpl
argument_list|(
name|file
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported Log4j2 configuration type: "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unrecognized configuration"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getLevel
parameter_list|()
block|{
return|return
name|getLevel
argument_list|(
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
argument_list|)
operator|.
name|get
argument_list|(
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
expr_stmt|;
block|}
return|return
name|getDelegate
argument_list|(
name|cfg
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|getLevel
argument_list|(
name|logger
argument_list|)
return|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|setLevel
argument_list|(
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|logger
parameter_list|,
name|String
name|level
parameter_list|)
block|{
comment|// make sure both uppercase and lowercase levels are supported
name|level
operator|=
name|level
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
comment|// check if the level is valid
name|Level
name|lvl
init|=
name|Level
operator|.
name|valueOf
argument_list|(
name|level
argument_list|)
decl_stmt|;
comment|// Default logger
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
expr_stmt|;
block|}
comment|// Verify
if|if
condition|(
name|lvl
operator|==
name|Level
operator|.
name|DEFAULT
operator|&&
name|LogServiceInternal
operator|.
name|ROOT_LOGGER
operator|.
name|equals
argument_list|(
name|logger
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can not unset the ROOT logger"
argument_list|)
throw|;
block|}
comment|// Get config
name|Configuration
name|cfg
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
name|cfg
operator|.
name|getProperties
argument_list|()
decl_stmt|;
comment|// Update
name|getDelegate
argument_list|(
name|props
argument_list|)
operator|.
name|setLevel
argument_list|(
name|logger
argument_list|,
name|level
argument_list|)
expr_stmt|;
comment|// Save
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
return|;
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
name|buffer
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
name|buffer
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
name|buffer
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
name|this
operator|.
name|appenders
operator|.
name|add
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
name|this
operator|.
name|appenders
operator|.
name|remove
argument_list|(
name|appender
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|doAppend
parameter_list|(
name|PaxLoggingEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|getProperties
argument_list|()
expr_stmt|;
comment|// ensure MDC properties are copied
name|KarafLogEvent
name|eventCopy
init|=
operator|new
name|KarafLogEvent
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|this
operator|.
name|buffer
operator|.
name|add
argument_list|(
name|eventCopy
argument_list|)
expr_stmt|;
for|for
control|(
name|PaxAppender
name|appender
range|:
name|appenders
control|)
block|{
try|try
block|{
name|appender
operator|.
name|doAppend
argument_list|(
name|eventCopy
argument_list|)
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
block|}
end_class

end_unit

