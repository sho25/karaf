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
name|SingleServiceTracker
operator|.
name|SingleServiceListener
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
argument_list|<
name|Runnable
argument_list|>
argument_list|()
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
name|List
argument_list|<
name|ServiceRegistration
argument_list|>
name|registrations
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
argument_list|<
name|String
argument_list|,
name|SingleServiceTracker
argument_list|>
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
name|isEmpty
argument_list|()
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
block|{     }
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
if|if
condition|(
name|registrations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceRegistration
name|reg
range|:
name|registrations
control|)
block|{
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
name|registrations
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doOpen()}      */
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|/**      * Called in {@link #doStart()}      */
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
comment|/**      * Called in {@link #doStart()}      */
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
comment|/**      * Called in {@link #doStart()}      */
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
comment|/**      * Called in {@link #doStart()}      */
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
annotation|@
name|Override
specifier|public
name|void
name|serviceFound
parameter_list|()
block|{
name|reconfigure
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|serviceLost
parameter_list|()
block|{
name|reconfigure
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|serviceReplaced
parameter_list|()
block|{
name|reconfigure
argument_list|()
expr_stmt|;
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
operator|new
name|Runnable
argument_list|()
block|{
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
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doOpen()}      */
specifier|protected
name|void
name|trackService
parameter_list|(
name|Class
name|clazz
parameter_list|)
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
argument_list|(
name|bundleContext
argument_list|,
name|clazz
argument_list|,
name|this
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
comment|/**      * Called in {@link #doOpen()}      */
specifier|protected
name|void
name|trackService
parameter_list|(
name|String
name|clazz
parameter_list|)
block|{
if|if
condition|(
operator|!
name|trackers
operator|.
name|containsKey
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|SingleServiceTracker
name|tracker
init|=
operator|new
name|SingleServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|clazz
argument_list|,
name|this
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
argument_list|,
name|tracker
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called in {@link #doOpen()}      */
specifier|protected
name|void
name|trackService
parameter_list|(
name|Class
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
name|SingleServiceTracker
name|tracker
init|=
operator|new
name|SingleServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|clazz
argument_list|,
name|filter
argument_list|,
name|this
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
comment|/**      * Called in {@link #doStart()}      */
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|Object
name|getTrackedService
parameter_list|(
name|String
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
name|tracker
operator|.
name|getService
argument_list|()
return|;
block|}
comment|/**      * Called in {@link #doStart()}      */
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"jmx.objectname"
argument_list|,
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|void
name|register
parameter_list|(
name|Class
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|void
name|register
parameter_list|(
name|Class
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|void
name|register
parameter_list|(
name|String
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|void
name|register
parameter_list|(
name|String
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|void
name|register
parameter_list|(
name|String
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
comment|/**      * Called in {@link #doStart()}      */
specifier|protected
name|void
name|register
parameter_list|(
name|String
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
specifier|private
name|void
name|trackRegistration
parameter_list|(
name|ServiceRegistration
name|registration
parameter_list|)
block|{
if|if
condition|(
name|registrations
operator|==
literal|null
condition|)
block|{
name|registrations
operator|=
operator|new
name|ArrayList
argument_list|<
name|ServiceRegistration
argument_list|>
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|String
argument_list|>
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
block|}
end_class

end_unit

