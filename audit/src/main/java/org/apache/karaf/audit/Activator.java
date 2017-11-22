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
name|audit
package|;
end_package

begin_import
import|import
name|com
operator|.
name|conversantmedia
operator|.
name|util
operator|.
name|concurrent
operator|.
name|DisruptorBlockingQueue
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
name|audit
operator|.
name|layout
operator|.
name|GelfLayout
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
name|audit
operator|.
name|layout
operator|.
name|Rfc3164Layout
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
name|audit
operator|.
name|layout
operator|.
name|Rfc5424Layout
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
name|audit
operator|.
name|layout
operator|.
name|SimpleLayout
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
name|audit
operator|.
name|logger
operator|.
name|FileEventLogger
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
name|audit
operator|.
name|logger
operator|.
name|JulEventLogger
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
name|audit
operator|.
name|logger
operator|.
name|UdpEventLogger
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
name|tracker
operator|.
name|BaseActivator
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
name|tracker
operator|.
name|annotation
operator|.
name|Managed
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
name|tracker
operator|.
name|annotation
operator|.
name|RequireService
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
name|tracker
operator|.
name|annotation
operator|.
name|Services
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
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
name|ManagedService
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
name|event
operator|.
name|Event
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
name|event
operator|.
name|EventAdmin
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
name|event
operator|.
name|EventConstants
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
name|event
operator|.
name|EventHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|util
operator|.
name|AbstractMap
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Locale
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
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
annotation|@
name|RequireService
argument_list|(
name|EventAdmin
operator|.
name|class
argument_list|)
argument_list|)
annotation|@
name|Managed
argument_list|(
literal|"org.apache.karaf.audit"
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
implements|implements
name|ManagedService
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FILTER
init|=
literal|"filter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE_TYPE
init|=
literal|"queue.type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE_SIZE
init|=
literal|"queue.size"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RUNNER_IDLE_TIMEOUT
init|=
literal|"runner.idle-timeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RUNNER_FLUSH_TIMEOUT
init|=
literal|"runner.flush-timeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_PREFIX
init|=
literal|"file."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_LAYOUT
init|=
name|FILE_PREFIX
operator|+
literal|"layout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_ENABLED
init|=
name|FILE_PREFIX
operator|+
literal|"enabled"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_TARGET
init|=
name|FILE_PREFIX
operator|+
literal|"target"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_ENCODING
init|=
name|FILE_PREFIX
operator|+
literal|"encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_POLICY
init|=
name|FILE_PREFIX
operator|+
literal|"policy"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_FILES
init|=
name|FILE_PREFIX
operator|+
literal|"files"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_COMPRESS
init|=
name|FILE_PREFIX
operator|+
literal|"compress"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UDP_PREFIX
init|=
literal|"udp."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UDP_LAYOUT
init|=
name|UDP_PREFIX
operator|+
literal|"layout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UDP_ENABLED
init|=
name|UDP_PREFIX
operator|+
literal|"enabled"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UDP_HOST
init|=
name|UDP_PREFIX
operator|+
literal|"host"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UDP_PORT
init|=
name|UDP_PREFIX
operator|+
literal|"port"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UDP_ENCODING
init|=
name|UDP_PREFIX
operator|+
literal|"encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TCP_PREFIX
init|=
literal|"tcp."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TCP_LAYOUT
init|=
name|TCP_PREFIX
operator|+
literal|"layout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TCP_ENABLED
init|=
name|TCP_PREFIX
operator|+
literal|"enabled"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TCP_HOST
init|=
name|TCP_PREFIX
operator|+
literal|"host"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TCP_PORT
init|=
name|TCP_PREFIX
operator|+
literal|"port"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TCP_ENCODING
init|=
name|TCP_PREFIX
operator|+
literal|"encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JUL_PREFIX
init|=
literal|"jul."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JUL_LAYOUT
init|=
name|JUL_PREFIX
operator|+
literal|"layout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JUL_ENABLED
init|=
name|JUL_PREFIX
operator|+
literal|"enabled"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JUL_LOGGER
init|=
name|JUL_PREFIX
operator|+
literal|"logger"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JUL_LEVEL
init|=
name|JUL_PREFIX
operator|+
literal|"level"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPICS
init|=
literal|"topics"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|EventImpl
name|STOP_EVENT
init|=
operator|new
name|EventImpl
argument_list|(
operator|new
name|Event
argument_list|(
literal|"stop"
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|BlockingQueue
argument_list|<
name|EventImpl
argument_list|>
name|queue
decl_stmt|;
specifier|private
specifier|volatile
name|Thread
name|runner
decl_stmt|;
specifier|private
name|List
argument_list|<
name|EventLogger
argument_list|>
name|eventLoggers
decl_stmt|;
specifier|private
name|Filter
name|filter
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|queue
operator|=
name|createQueue
argument_list|()
expr_stmt|;
name|eventLoggers
operator|=
name|createLoggers
argument_list|()
expr_stmt|;
name|filter
operator|=
name|createFilter
argument_list|()
expr_stmt|;
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|EVENT_TOPIC
argument_list|,
name|getTopics
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|EventHandler
operator|.
name|class
argument_list|,
name|this
operator|::
name|handleEvent
argument_list|,
name|props
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|queue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|startRunner
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
index|[]
name|getTopics
parameter_list|()
block|{
return|return
name|getString
argument_list|(
name|TOPICS
argument_list|,
literal|"*"
argument_list|)
operator|.
name|split
argument_list|(
literal|"\\s*,\\s*"
argument_list|)
return|;
block|}
specifier|private
name|Filter
name|createFilter
parameter_list|()
throws|throws
name|InvalidSyntaxException
block|{
name|String
name|str
init|=
name|getString
argument_list|(
name|FILTER
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|str
operator|!=
literal|null
condition|?
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
name|str
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|BlockingQueue
argument_list|<
name|EventImpl
argument_list|>
name|createQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|type
init|=
name|getString
argument_list|(
name|QUEUE_TYPE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|int
name|size
init|=
name|getInt
argument_list|(
name|QUEUE_SIZE
argument_list|,
literal|1024
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"ArrayBlockingQueue"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|ArrayBlockingQueue
argument_list|<>
argument_list|(
name|size
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"DisruptorBlockingQueue"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|new
name|DisruptorBlockingQueue
argument_list|(
name|size
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Unknown queue type: "
operator|+
name|type
operator|+
literal|""
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
operator|new
name|DisruptorBlockingQueue
argument_list|(
name|size
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|t
parameter_list|)
block|{
return|return
operator|new
name|ArrayBlockingQueue
argument_list|<>
argument_list|(
name|size
argument_list|)
return|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|EventLogger
argument_list|>
name|createLoggers
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|List
argument_list|<
name|EventLogger
argument_list|>
name|loggers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|getBoolean
argument_list|(
name|FILE_ENABLED
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|getString
argument_list|(
name|FILE_TARGET
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|)
operator|+
literal|"/log/audit.txt"
argument_list|)
decl_stmt|;
name|String
name|encoding
init|=
name|getString
argument_list|(
name|FILE_ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|String
name|policy
init|=
name|getString
argument_list|(
name|FILE_POLICY
argument_list|,
literal|"size(8mb)"
argument_list|)
decl_stmt|;
name|int
name|files
init|=
name|getInt
argument_list|(
name|FILE_FILES
argument_list|,
literal|32
argument_list|)
decl_stmt|;
name|boolean
name|compress
init|=
name|getBoolean
argument_list|(
name|FILE_COMPRESS
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|EventLayout
name|layout
init|=
name|createLayout
argument_list|(
name|getString
argument_list|(
name|FILE_LAYOUT
argument_list|,
name|FILE_LAYOUT
argument_list|)
argument_list|)
decl_stmt|;
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|FileEventLogger
argument_list|(
name|path
argument_list|,
name|encoding
argument_list|,
name|policy
argument_list|,
name|files
argument_list|,
name|compress
argument_list|,
name|this
argument_list|,
name|layout
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getBoolean
argument_list|(
name|UDP_ENABLED
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|String
name|host
init|=
name|getString
argument_list|(
name|UDP_HOST
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|getInt
argument_list|(
name|UDP_PORT
argument_list|,
literal|514
argument_list|)
decl_stmt|;
name|String
name|encoding
init|=
name|getString
argument_list|(
name|UDP_ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|EventLayout
name|layout
init|=
name|createLayout
argument_list|(
name|getString
argument_list|(
name|UDP_LAYOUT
argument_list|,
name|UDP_LAYOUT
argument_list|)
argument_list|)
decl_stmt|;
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|UdpEventLogger
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|encoding
argument_list|,
name|layout
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getBoolean
argument_list|(
name|TCP_ENABLED
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|String
name|host
init|=
name|getString
argument_list|(
name|TCP_HOST
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|getInt
argument_list|(
name|TCP_PORT
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|encoding
init|=
name|getString
argument_list|(
name|TCP_ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|EventLayout
name|layout
init|=
name|createLayout
argument_list|(
name|getString
argument_list|(
name|TCP_LAYOUT
argument_list|,
name|TCP_LAYOUT
argument_list|)
argument_list|)
decl_stmt|;
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|UdpEventLogger
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|encoding
argument_list|,
name|layout
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getBoolean
argument_list|(
name|JUL_ENABLED
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|String
name|logger
init|=
name|getString
argument_list|(
name|Activator
operator|.
name|JUL_LOGGER
argument_list|,
literal|"audit"
argument_list|)
decl_stmt|;
name|String
name|level
init|=
name|getString
argument_list|(
name|Activator
operator|.
name|JUL_LEVEL
argument_list|,
literal|"info"
argument_list|)
decl_stmt|;
name|EventLayout
name|layout
init|=
name|createLayout
argument_list|(
name|getString
argument_list|(
name|JUL_LAYOUT
argument_list|,
name|JUL_LAYOUT
argument_list|)
argument_list|)
decl_stmt|;
name|loggers
operator|.
name|add
argument_list|(
operator|new
name|JulEventLogger
argument_list|(
name|logger
argument_list|,
name|level
argument_list|,
name|layout
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|loggers
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
name|Exception
argument_list|(
literal|"Error creating audit logger"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|EventLayout
name|createLayout
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|type
init|=
name|getString
argument_list|(
name|prefix
operator|+
literal|".type"
argument_list|,
literal|"simple"
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
literal|"simple"
case|:
return|return
operator|new
name|SimpleLayout
argument_list|()
return|;
case|case
literal|"rfc3164"
case|:
return|return
operator|new
name|Rfc3164Layout
argument_list|(
name|getInt
argument_list|(
name|prefix
operator|+
literal|".facility"
argument_list|,
literal|16
argument_list|)
argument_list|,
name|getInt
argument_list|(
name|prefix
operator|+
literal|".priority"
argument_list|,
literal|5
argument_list|)
argument_list|,
name|getInt
argument_list|(
name|prefix
operator|+
literal|".enterprise"
argument_list|,
name|Rfc5424Layout
operator|.
name|DEFAULT_ENTERPRISE_NUMBER
argument_list|)
argument_list|,
name|TimeZone
operator|.
name|getDefault
argument_list|()
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
return|;
case|case
literal|"rfc5424"
case|:
return|return
operator|new
name|Rfc5424Layout
argument_list|(
name|getInt
argument_list|(
name|prefix
operator|+
literal|".facility"
argument_list|,
literal|16
argument_list|)
argument_list|,
name|getInt
argument_list|(
name|prefix
operator|+
literal|".priority"
argument_list|,
literal|5
argument_list|)
argument_list|,
name|getInt
argument_list|(
name|prefix
operator|+
literal|".enterprise"
argument_list|,
name|Rfc5424Layout
operator|.
name|DEFAULT_ENTERPRISE_NUMBER
argument_list|)
argument_list|,
name|TimeZone
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
case|case
literal|"gelf"
case|:
return|return
operator|new
name|GelfLayout
argument_list|()
return|;
default|default:
name|logger
operator|.
name|warn
argument_list|(
literal|"Unknown layout: "
operator|+
name|type
operator|+
literal|". Using a simple layout."
argument_list|)
expr_stmt|;
return|return
operator|new
name|SimpleLayout
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
name|Thread
name|runner
init|=
name|this
operator|.
name|runner
decl_stmt|;
if|if
condition|(
name|runner
operator|!=
literal|null
operator|&&
name|runner
operator|.
name|isAlive
argument_list|()
condition|)
block|{
try|try
block|{
name|queue
operator|.
name|add
argument_list|(
name|STOP_EVENT
argument_list|)
expr_stmt|;
name|runner
operator|.
name|join
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
if|if
condition|(
name|runner
operator|.
name|isAlive
argument_list|()
condition|)
block|{
name|runner
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Error waiting for audit runner buffer stop"
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|EventLogger
argument_list|>
name|eventLoggers
init|=
name|this
operator|.
name|eventLoggers
decl_stmt|;
if|if
condition|(
name|eventLoggers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|EventLogger
name|eventLogger
range|:
name|eventLoggers
control|)
block|{
try|try
block|{
name|eventLogger
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
name|logger
operator|.
name|debug
argument_list|(
literal|"Error closing audit logger"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|eventLoggers
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|handleEvent
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
try|try
block|{
name|EventImpl
name|ev
init|=
operator|new
name|EventImpl
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|==
literal|null
operator|||
name|filter
operator|.
name|matches
argument_list|(
name|ev
operator|.
name|getFilterMap
argument_list|()
argument_list|)
condition|)
block|{
name|queue
operator|.
name|put
argument_list|(
operator|new
name|EventImpl
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
name|startRunner
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Interrupted while putting event in queue"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|startRunner
parameter_list|()
block|{
if|if
condition|(
name|eventLoggers
operator|!=
literal|null
operator|&&
operator|!
name|eventLoggers
operator|.
name|isEmpty
argument_list|()
operator|&&
name|runner
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|runner
operator|==
literal|null
condition|)
block|{
name|runner
operator|=
operator|new
name|Thread
argument_list|(
name|this
operator|::
name|consume
argument_list|,
literal|"audit-logger"
argument_list|)
expr_stmt|;
name|runner
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|consume
parameter_list|()
block|{
name|long
name|maxIdle
init|=
name|getLong
argument_list|(
name|RUNNER_IDLE_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
operator|.
name|toMillis
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|flushDelay
init|=
name|getLong
argument_list|(
name|RUNNER_FLUSH_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toMillis
argument_list|(
literal|100
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|EventLogger
argument_list|>
name|eventLoggers
init|=
name|this
operator|.
name|eventLoggers
decl_stmt|;
name|BlockingQueue
argument_list|<
name|EventImpl
argument_list|>
name|queue
init|=
name|this
operator|.
name|queue
decl_stmt|;
name|EventImpl
name|event
decl_stmt|;
while|while
condition|(
operator|(
name|event
operator|=
name|queue
operator|.
name|poll
argument_list|(
name|maxIdle
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|event
operator|==
name|STOP_EVENT
condition|)
block|{
return|return;
block|}
for|for
control|(
name|EventLogger
name|eventLogger
range|:
name|eventLoggers
control|)
block|{
name|eventLogger
operator|.
name|write
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|flushDelay
operator|>
literal|0
condition|)
block|{
while|while
condition|(
operator|(
name|event
operator|=
name|queue
operator|.
name|poll
argument_list|(
name|flushDelay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|event
operator|==
name|STOP_EVENT
condition|)
block|{
return|return;
block|}
for|for
control|(
name|EventLogger
name|eventLogger
range|:
name|eventLoggers
control|)
block|{
name|eventLogger
operator|.
name|write
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|EventLogger
name|eventLogger
range|:
name|eventLoggers
control|)
block|{
name|eventLogger
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error writing audit log"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|runner
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|EventImpl
implements|implements
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|audit
operator|.
name|Event
block|{
specifier|private
specifier|final
name|Event
name|event
decl_stmt|;
specifier|private
specifier|final
name|long
name|timestamp
decl_stmt|;
specifier|private
specifier|final
name|String
name|type
decl_stmt|;
specifier|private
specifier|final
name|String
name|subtype
decl_stmt|;
name|EventImpl
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|_timestamp
argument_list|()
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|_type
argument_list|()
expr_stmt|;
name|this
operator|.
name|subtype
operator|=
name|_subtype
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|timestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
specifier|private
name|long
name|_timestamp
parameter_list|()
block|{
name|Long
name|l
init|=
operator|(
name|Long
operator|)
name|event
operator|.
name|getProperty
argument_list|(
literal|"timestamp"
argument_list|)
decl_stmt|;
return|return
name|l
operator|!=
literal|null
condition|?
name|l
else|:
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Subject
name|subject
parameter_list|()
block|{
return|return
operator|(
name|Subject
operator|)
name|event
operator|.
name|getProperty
argument_list|(
literal|"subject"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|type
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|private
name|String
name|_type
parameter_list|()
block|{
switch|switch
condition|(
name|event
operator|.
name|getTopic
argument_list|()
condition|)
block|{
case|case
literal|"org/apache/karaf/shell/console/EXECUTED"
case|:
return|return
name|TYPE_SHELL
return|;
case|case
literal|"org/osgi/service/log/LogEntry/LOG_ERROR"
case|:
case|case
literal|"org/osgi/service/log/LogEntry/LOG_WARNING"
case|:
case|case
literal|"org/osgi/service/log/LogEntry/LOG_INFO"
case|:
case|case
literal|"org/osgi/service/log/LogEntry/LOG_DEBUG"
case|:
case|case
literal|"org/osgi/service/log/LogEntry/LOG_OTHER"
case|:
return|return
name|TYPE_LOG
return|;
case|case
literal|"org/osgi/framework/ServiceEvent/REGISTERED"
case|:
case|case
literal|"org/osgi/framework/ServiceEvent/MODIFIED"
case|:
case|case
literal|"org/osgi/framework/ServiceEvent/UNREGISTERING"
case|:
return|return
name|TYPE_SERVICE
return|;
case|case
literal|"org/osgi/framework/BundleEvent/INSTALLED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/STARTED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/STOPPED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/UPDATED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/UNINSTALLED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/RESOLVED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/UNRESOLVED"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/STARTING"
case|:
case|case
literal|"org/osgi/framework/BundleEvent/STOPPING"
case|:
return|return
name|TYPE_BUNDLE
return|;
case|case
literal|"org/apache/karaf/login/ATTEMPT"
case|:
case|case
literal|"org/apache/karaf/login/SUCCESS"
case|:
case|case
literal|"org/apache/karaf/login/FAILURE"
case|:
case|case
literal|"org/apache/karaf/login/LOGOUT"
case|:
return|return
name|TYPE_LOGIN
return|;
case|case
literal|"javax/management/MBeanServer/CREATEMBEAN"
case|:
case|case
literal|"javax/management/MBeanServer/REGISTERMBEAN"
case|:
case|case
literal|"javax/management/MBeanServer/UNREGISTERMBEAN"
case|:
case|case
literal|"javax/management/MBeanServer/GETOBJECTINSTANCE"
case|:
case|case
literal|"javax/management/MBeanServer/QUERYMBEANS"
case|:
case|case
literal|"javax/management/MBeanServer/ISREGISTERED"
case|:
case|case
literal|"javax/management/MBeanServer/GETMBEANCOUNT"
case|:
case|case
literal|"javax/management/MBeanServer/GETATTRIBUTE"
case|:
case|case
literal|"javax/management/MBeanServer/GETATTRIBUTES"
case|:
case|case
literal|"javax/management/MBeanServer/SETATTRIBUTE"
case|:
case|case
literal|"javax/management/MBeanServer/SETATTRIBUTES"
case|:
case|case
literal|"javax/management/MBeanServer/INVOKE"
case|:
case|case
literal|"javax/management/MBeanServer/GETDEFAULTDOMAIN"
case|:
case|case
literal|"javax/management/MBeanServer/GETDOMAINS"
case|:
case|case
literal|"javax/management/MBeanServer/ADDNOTIFICATIONLISTENER"
case|:
case|case
literal|"javax/management/MBeanServer/GETMBEANINFO"
case|:
case|case
literal|"javax/management/MBeanServer/ISINSTANCEOF"
case|:
case|case
literal|"javax/management/MBeanServer/INSTANTIATE"
case|:
case|case
literal|"javax/management/MBeanServer/DESERIALIZE"
case|:
case|case
literal|"javax/management/MBeanServer/GETCLASSLOADERFOR"
case|:
case|case
literal|"javax/management/MBeanServer/GETCLASSLOADER"
case|:
return|return
name|TYPE_JMX
return|;
case|case
literal|"org/osgi/framework/FrameworkEvent/STARTED"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/ERROR"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/PACKAGES_REFRESHED"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/STARTLEVEL_CHANGED"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/WARNING"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/INFO"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/STOPPED"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/STOPPED_UPDATE"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/STOPPED_BOOTCLASSPATH_MODIFIED"
case|:
case|case
literal|"org/osgi/framework/FrameworkEvent/WAIT_TIMEDOUT"
case|:
return|return
name|TYPE_FRAMEWORK
return|;
case|case
literal|"org/osgi/service/web/DEPLOYING"
case|:
case|case
literal|"org/osgi/service/web/DEPLOYED"
case|:
case|case
literal|"org/osgi/service/web/UNDEPLOYING"
case|:
case|case
literal|"org/osgi/service/web/UNDEPLOYED"
case|:
return|return
name|TYPE_WEB
return|;
case|case
literal|"org/apache/karaf/features/repositories/ADDED"
case|:
case|case
literal|"org/apache/karaf/features/repositories/REMOVED"
case|:
return|return
name|TYPE_REPOSITORIES
return|;
case|case
literal|"org/apache/karaf/features/features/INSTALLED"
case|:
case|case
literal|"org/apache/karaf/features/features/UNINSTALLED"
case|:
return|return
name|TYPE_FEATURES
return|;
case|case
literal|"org/osgi/service/blueprint/container/CREATING"
case|:
case|case
literal|"org/osgi/service/blueprint/container/CREATED"
case|:
case|case
literal|"org/osgi/service/blueprint/container/DESTROYING"
case|:
case|case
literal|"org/osgi/service/blueprint/container/DESTROYED"
case|:
case|case
literal|"org/osgi/service/blueprint/container/FAILURE"
case|:
case|case
literal|"org/osgi/service/blueprint/container/GRACE_PERIOD"
case|:
case|case
literal|"org/osgi/service/blueprint/container/WAITING"
case|:
return|return
name|TYPE_BLUEPRINT
return|;
default|default:
return|return
name|TYPE_UNKNOWN
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|subtype
parameter_list|()
block|{
return|return
name|subtype
return|;
block|}
specifier|private
name|String
name|_subtype
parameter_list|()
block|{
name|String
name|topic
init|=
name|event
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|String
name|subtype
init|=
name|topic
operator|.
name|substring
argument_list|(
name|topic
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
if|if
condition|(
name|subtype
operator|.
name|startsWith
argument_list|(
literal|"log_"
argument_list|)
condition|)
block|{
name|subtype
operator|=
name|subtype
operator|.
name|substring
argument_list|(
literal|"log_"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|subtype
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|()
block|{
name|String
index|[]
name|keys
init|=
name|event
operator|.
name|getPropertyNames
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
return|return
parameter_list|()
lambda|->
operator|new
name|Iterator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
name|String
name|next
return|;
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
while|while
condition|(
operator|++
name|index
operator|<
name|keys
operator|.
name|length
condition|)
block|{
switch|switch
condition|(
name|keys
index|[
name|index
index|]
condition|)
block|{
case|case
literal|"timestamp"
case|:
case|case
literal|"event.topics"
case|:
case|case
literal|"subject"
case|:
case|case
literal|"type"
case|:
case|case
literal|"subtype"
case|:
break|break;
default|default:
name|next
operator|=
name|keys
index|[
name|index
index|]
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|String
name|str
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|str
return|;
block|}
block|}
empty_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|event
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getFilterMap
parameter_list|()
block|{
return|return
operator|new
name|AbstractMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|s
init|=
name|key
operator|.
name|toString
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|s
condition|)
block|{
case|case
literal|"timestamp"
case|:
return|return
name|timestamp
argument_list|()
return|;
case|case
literal|"type"
case|:
return|return
name|type
argument_list|()
return|;
case|case
literal|"subtype"
case|:
return|return
name|subtype
argument_list|()
return|;
case|case
literal|"subject"
case|:
return|return
name|subject
argument_list|()
return|;
default|default:
return|return
name|event
operator|.
name|getProperty
argument_list|(
name|s
argument_list|)
return|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

unit|}
end_unit

