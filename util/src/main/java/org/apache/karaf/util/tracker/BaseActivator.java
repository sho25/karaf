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
name|util
operator|.
name|tracker
package|;
end_package

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
name|net
operator|.
name|URL
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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Queue
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
name|ConcurrentLinkedQueue
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
name|LinkedBlockingQueue
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
name|ThreadFactory
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
name|ThreadPoolExecutor
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|StreamSupport
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
name|BundleActivator
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
name|BundleContext
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
name|Constants
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
name|framework
operator|.
name|ServiceRegistration
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
name|BaseActivator
implements|implements
name|BundleActivator
implements|,
name|Runnable
implements|,
name|ThreadFactory
block|{
specifier|protected
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|protected
name|ExecutorService
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|0L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
specifier|private
name|AtomicBoolean
name|scheduled
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
specifier|private
name|long
name|schedulerStopTimeout
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Queue
argument_list|<
name|ServiceRegistration
argument_list|>
name|registrations
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|SingleServiceTracker
argument_list|>
name|trackers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|ServiceRegistration
name|managedServiceRegistration
decl_stmt|;
specifier|private
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|configuration
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|poolNumber
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ThreadGroup
name|group
decl_stmt|;
specifier|private
specifier|final
name|AtomicInteger
name|threadNumber
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|namePrefix
decl_stmt|;
specifier|public
name|BaseActivator
parameter_list|()
block|{
name|SecurityManager
name|s
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
name|group
operator|=
operator|(
name|s
operator|!=
literal|null
operator|)
condition|?
name|s
operator|.
name|getThreadGroup
argument_list|()
else|:
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getThreadGroup
argument_list|()
expr_stmt|;
name|namePrefix
operator|=
literal|"activator-"
operator|+
name|poolNumber
operator|.
name|getAndIncrement
argument_list|()
operator|+
literal|"-thread-"
expr_stmt|;
block|}
specifier|public
name|long
name|getSchedulerStopTimeout
parameter_list|()
block|{
return|return
name|schedulerStopTimeout
return|;
block|}
specifier|public
name|void
name|setSchedulerStopTimeout
parameter_list|(
name|long
name|schedulerStopTimeout
parameter_list|)
block|{
name|this
operator|.
name|schedulerStopTimeout
operator|=
name|schedulerStopTimeout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|bundleContext
operator|=
name|context
expr_stmt|;
name|scheduled
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doOpen
argument_list|()
expr_stmt|;
name|scheduled
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|managedServiceRegistration
operator|==
literal|null
operator|&&
name|trackers
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|allMatch
argument_list|(
name|t
lambda|->
name|t
operator|.
name|getService
argument_list|()
operator|!=
literal|null
argument_list|)
condition|)
block|{
try|try
block|{
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error starting activator"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|reconfigure
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|scheduled
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doClose
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|executor
operator|.
name|awaitTermination
argument_list|(
name|schedulerStopTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|doStop
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|data
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"OSGI-INF/karaf-tracker/"
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|data
operator|.
name|openStream
argument_list|()
init|)
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|key
range|:
name|props
operator|.
name|stringPropertyNames
argument_list|()
control|)
block|{
if|if
condition|(
literal|"pid"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|manage
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|trackService
argument_list|(
name|key
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|doClose
parameter_list|()
block|{
if|if
condition|(
name|managedServiceRegistration
operator|!=
literal|null
condition|)
block|{
name|managedServiceRegistration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|SingleServiceTracker
name|tracker
range|:
name|trackers
operator|.
name|values
argument_list|()
control|)
block|{
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|ServiceRegistration
name|reg
init|=
name|registrations
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doOpen()}.      *      * @param pid The configuration PID to manage (ManagedService).      */
specifier|protected
name|void
name|manage
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|Hashtable
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
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|managedServiceRegistration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
literal|"org.osgi.service.cm.ManagedService"
argument_list|,
name|this
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updated
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|properties
expr_stmt|;
name|reconfigure
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param key The configuration key      * @param def The default value.      * @return The value of the configuration key if found, the default value else.      */
specifier|protected
name|int
name|getInt
parameter_list|(
name|String
name|key
parameter_list|,
name|int
name|def
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|configuration
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|val
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|def
return|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param key The configuration key.      * @param def The default value.      * @return The value of the configuration key if found, the default value else.      */
specifier|protected
name|boolean
name|getBoolean
parameter_list|(
name|String
name|key
parameter_list|,
name|boolean
name|def
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|configuration
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|val
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|def
return|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param key The configuration key.      * @param def The default value.      * @return The value of the configuration key if found, the default value else.      */
specifier|protected
name|long
name|getLong
parameter_list|(
name|String
name|key
parameter_list|,
name|long
name|def
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|configuration
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|val
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|def
return|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param key The configuration key.      * @param def The default value.      * @return The value of the configuration key if found, the default value else.      */
specifier|protected
name|String
name|getString
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|def
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|configuration
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|val
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
name|def
return|;
block|}
specifier|protected
name|String
index|[]
name|getStringArray
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|def
parameter_list|)
block|{
name|Object
name|val
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|val
operator|=
name|configuration
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|def
expr_stmt|;
block|}
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Stream
argument_list|<
name|String
argument_list|>
name|s
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|String
index|[]
condition|)
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|val
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|instanceof
name|Iterable
condition|)
block|{
return|return
name|StreamSupport
operator|.
name|stream
argument_list|(
operator|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|val
operator|)
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|map
argument_list|(
name|Object
operator|::
name|toString
argument_list|)
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|val
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
return|;
block|}
block|}
specifier|protected
name|void
name|reconfigure
parameter_list|()
block|{
if|if
condition|(
name|scheduled
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|executor
operator|.
name|submit
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|scheduled
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doStop
argument_list|()
expr_stmt|;
try|try
block|{
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error starting activator"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doOpen()}.      *      * @param clazz The service interface to track.      * @throws InvalidSyntaxException If the tracker syntax is not correct.      */
specifier|protected
name|void
name|trackService
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
if|if
condition|(
operator|!
name|trackers
operator|.
name|containsKey
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|SingleServiceTracker
name|tracker
init|=
operator|new
name|SingleServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|clazz
argument_list|,
parameter_list|(
name|u
parameter_list|,
name|v
parameter_list|)
lambda|->
name|reconfigure
argument_list|()
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|trackers
operator|.
name|put
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|tracker
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doOpen()}.      *      * @param clazz The service interface to track.      * @param filter The filter to use to select the services to track.      * @throws InvalidSyntaxException If the tracker syntax is not correct (in the filter especially).      */
specifier|protected
name|void
name|trackService
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|filter
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
if|if
condition|(
operator|!
name|trackers
operator|.
name|containsKey
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|filter
operator|!=
literal|null
operator|&&
name|filter
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|filter
operator|=
literal|null
expr_stmt|;
block|}
name|SingleServiceTracker
name|tracker
init|=
operator|new
name|SingleServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|clazz
argument_list|,
name|filter
argument_list|,
parameter_list|(
name|u
parameter_list|,
name|v
parameter_list|)
lambda|->
name|reconfigure
argument_list|()
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|trackers
operator|.
name|put
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|tracker
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|trackService
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|filter
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
if|if
condition|(
operator|!
name|trackers
operator|.
name|containsKey
argument_list|(
name|className
argument_list|)
condition|)
block|{
name|SingleServiceTracker
name|tracker
init|=
operator|new
name|SingleServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|className
argument_list|,
name|filter
argument_list|,
parameter_list|(
name|u
parameter_list|,
name|v
parameter_list|)
lambda|->
name|reconfigure
argument_list|()
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|trackers
operator|.
name|put
argument_list|(
name|className
argument_list|,
name|tracker
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param clazz The service interface to get.      * @param<T> The service type.      * @return The actual tracker service object.      */
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getTrackedService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|SingleServiceTracker
name|tracker
init|=
name|trackers
operator|.
name|get
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tracker
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Service not tracked for class "
operator|+
name|clazz
argument_list|)
throw|;
block|}
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|tracker
operator|.
name|getService
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param mbean The MBean to register.      * @param type The MBean type to register.      */
specifier|protected
name|void
name|registerMBean
parameter_list|(
name|Object
name|mbean
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|name
init|=
literal|"org.apache.karaf:"
operator|+
name|type
operator|+
literal|",name="
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|)
decl_stmt|;
name|registerMBeanWithName
argument_list|(
name|mbean
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param mbean The MBean to register.      * @param name The MBean name.      */
specifier|protected
name|void
name|registerMBeanWithName
parameter_list|(
name|Object
name|mbean
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Hashtable
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
literal|"jmx.objectname"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|trackRegistration
argument_list|(
name|bundleContext
operator|.
name|registerService
argument_list|(
name|getInterfaceNames
argument_list|(
name|mbean
argument_list|)
argument_list|,
name|mbean
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param clazz The service interface to register.      * @param<T> The service type.      * @param service The actual service instance to register.      */
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|void
name|register
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|T
name|service
parameter_list|)
block|{
name|register
argument_list|(
name|clazz
argument_list|,
name|service
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param clazz The service interface to register.      * @param<T> The service type.      * @param service The actual service instance to register.      * @param props The service properties to register.      */
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|void
name|register
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|T
name|service
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|props
parameter_list|)
block|{
name|trackRegistration
argument_list|(
name|bundleContext
operator|.
name|registerService
argument_list|(
name|clazz
argument_list|,
name|service
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param clazz The service interfaces to register.      * @param service The actual service instance to register.      */
specifier|protected
name|void
name|register
parameter_list|(
name|Class
index|[]
name|clazz
parameter_list|,
name|Object
name|service
parameter_list|)
block|{
name|register
argument_list|(
name|clazz
argument_list|,
name|service
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called in {@link #doStart()}.      *      * @param clazz The service interfaces to register.      * @param service The actual service instance to register.      * @param props The service properties to register.      */
specifier|protected
name|void
name|register
parameter_list|(
name|Class
index|[]
name|clazz
parameter_list|,
name|Object
name|service
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|props
parameter_list|)
block|{
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[
name|clazz
operator|.
name|length
index|]
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
name|clazz
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|names
index|[
name|i
index|]
operator|=
name|clazz
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|trackRegistration
argument_list|(
name|bundleContext
operator|.
name|registerService
argument_list|(
name|names
argument_list|,
name|service
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|trackRegistration
parameter_list|(
name|ServiceRegistration
name|registration
parameter_list|)
block|{
name|registrations
operator|.
name|add
argument_list|(
name|registration
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
index|[]
name|getInterfaceNames
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|cl
init|=
name|object
operator|.
name|getClass
argument_list|()
init|;
name|cl
operator|!=
name|Object
operator|.
name|class
condition|;
name|cl
operator|=
name|cl
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
name|addSuperInterfaces
argument_list|(
name|names
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
return|return
name|names
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|names
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|void
name|addSuperInterfaces
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|,
name|Class
name|clazz
parameter_list|)
block|{
for|for
control|(
name|Class
name|cl
range|:
name|clazz
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addSuperInterfaces
argument_list|(
name|names
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
name|group
argument_list|,
name|r
argument_list|,
name|namePrefix
operator|+
name|threadNumber
operator|.
name|getAndIncrement
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|isDaemon
argument_list|()
condition|)
name|t
operator|.
name|setDaemon
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|.
name|getPriority
argument_list|()
operator|!=
name|Thread
operator|.
name|NORM_PRIORITY
condition|)
name|t
operator|.
name|setPriority
argument_list|(
name|Thread
operator|.
name|NORM_PRIORITY
argument_list|)
expr_stmt|;
return|return
name|t
return|;
block|}
block|}
end_class

end_unit

