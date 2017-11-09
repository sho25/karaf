begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|eventadmin
operator|.
name|impl
package|;
end_package

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
name|StringTokenizer
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
name|eventadmin
operator|.
name|impl
operator|.
name|adapter
operator|.
name|AbstractAdapter
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
name|eventadmin
operator|.
name|impl
operator|.
name|adapter
operator|.
name|BundleEventAdapter
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
name|eventadmin
operator|.
name|impl
operator|.
name|adapter
operator|.
name|FrameworkEventAdapter
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
name|eventadmin
operator|.
name|impl
operator|.
name|adapter
operator|.
name|LogEventAdapter
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
name|eventadmin
operator|.
name|impl
operator|.
name|adapter
operator|.
name|ServiceEventAdapter
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
name|eventadmin
operator|.
name|impl
operator|.
name|handler
operator|.
name|EventAdminImpl
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
name|eventadmin
operator|.
name|impl
operator|.
name|security
operator|.
name|SecureEventAdminFactory
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
name|eventadmin
operator|.
name|impl
operator|.
name|tasks
operator|.
name|DefaultThreadPool
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
name|eventadmin
operator|.
name|impl
operator|.
name|util
operator|.
name|LogWrapper
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
name|ServiceRegistration
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
name|metatype
operator|.
name|MetaTypeProvider
import|;
end_import

begin_comment
comment|/**  *<p>The<code>Configuration</code> class encapsules the  * configuration for the event admin.</p>  *  *<p>The service knows about the following properties which are read at bundle startup:</p>  *<p>  *<tt>org.apache.felix.eventadmin.ThreadPoolSize</tt> - The size of the thread  *          pool.  *</p>  *  *<p>The default value is 10. Increase in case of a large amount of synchronous events  * where the<tt>EventHandler</tt> services in turn send new synchronous events in  * the event dispatching thread or a lot of timeouts are to be expected. A value of  * less then 2 triggers the default value. A value of 2 effectively disables thread  * pooling.</p>  *  *<p>  *<tt>org.apache.felix.eventadmin.Timeout</tt> - The black-listing timeout in  *          milliseconds  *</p>  *  *<p>The default value is 5000. Increase or decrease at own discretion. A value of less  * then 100 turns timeouts off. Any other value is the time in milliseconds granted  * to each<tt>EventHandler</tt> before it gets blacklisted.</p>  *  *<p>  *<tt>org.apache.felix.eventadmin.RequireTopic</tt> - Are<tt>EventHandler</tt>  *          required to be registered with a topic?  *</p>  *  *<p>The default is<tt>true</tt>. The specification says that<tt>EventHandler</tt>  * must register with a list of topics they are interested in. Setting this value to  *<tt>false</tt> will enable that handlers without a topic are receiving all events  * (i.e., they are treated the same as with a topic=*).</p>  *  *<p>  *<tt>org.apache.felix.eventadmin.IgnoreTimeout</tt> - Configure  *<tt>EventHandler</tt>s to be called without a timeout.  *</p>  *  *<p>If a timeout is configured by default all event handlers are called using the timeout.  * For performance optimization it is possible to configure event handlers where the  * timeout handling is not used - this reduces the thread usage from the thread pools  * as the timout handling requires an additional thread to call the event handler.  * However, the application should work without this configuration property. It is a  * pure optimization.  * The value is a list of string (separated by comma). If the string ends with a dot,  * all handlers in exactly this package are ignored. If the string ends with a star,  * all handlers in this package and all subpackages are ignored. If the string neither  * ends with a dot nor with a start, this is assumed to define an exact class name.</p>  *  *<p>These properties are read at startup and serve as a default configuration.  * If a configuration admin is configured, the event admin can be configured  * through the config admin.</p>  *  * @author<a href="mailto:dev@felix.apache.org">Felix Project Team</a>  */
end_comment

begin_class
specifier|public
class|class
name|Configuration
block|{
comment|/** The PID for the event admin. */
specifier|static
specifier|final
name|String
name|PID
init|=
literal|"org.apache.felix.eventadmin.impl.EventAdmin"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_THREAD_POOL_SIZE
init|=
literal|"org.apache.felix.eventadmin.ThreadPoolSize"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_ASYNC_TO_SYNC_THREAD_RATIO
init|=
literal|"org.apache.felix.eventadmin.AsyncToSyncThreadRatio"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_TIMEOUT
init|=
literal|"org.apache.felix.eventadmin.Timeout"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_REQUIRE_TOPIC
init|=
literal|"org.apache.felix.eventadmin.RequireTopic"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_IGNORE_TIMEOUT
init|=
literal|"org.apache.felix.eventadmin.IgnoreTimeout"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_IGNORE_TOPIC
init|=
literal|"org.apache.felix.eventadmin.IgnoreTopic"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_LOG_LEVEL
init|=
literal|"org.apache.felix.eventadmin.LogLevel"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_ADD_TIMESTAMP
init|=
literal|"org.apache.felix.eventadmin.AddTimestamp"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROP_ADD_SUBJECT
init|=
literal|"org.apache.felix.eventadmin.AddSubject"
decl_stmt|;
comment|/** The bundle context. */
specifier|private
specifier|final
name|BundleContext
name|m_bundleContext
decl_stmt|;
specifier|private
name|int
name|m_threadPoolSize
decl_stmt|;
specifier|private
name|double
name|m_asyncToSyncThreadRatio
decl_stmt|;
specifier|private
name|int
name|m_asyncThreadPoolSize
decl_stmt|;
specifier|private
name|int
name|m_timeout
decl_stmt|;
specifier|private
name|boolean
name|m_requireTopic
decl_stmt|;
specifier|private
name|String
index|[]
name|m_ignoreTimeout
decl_stmt|;
specifier|private
name|String
index|[]
name|m_ignoreTopics
decl_stmt|;
specifier|private
name|int
name|m_logLevel
decl_stmt|;
specifier|private
name|boolean
name|m_addTimestamp
decl_stmt|;
specifier|private
name|boolean
name|m_addSubject
decl_stmt|;
comment|// The thread pool used - this is a member because we need to close it on stop
specifier|private
specifier|volatile
name|DefaultThreadPool
name|m_sync_pool
decl_stmt|;
specifier|private
specifier|volatile
name|DefaultThreadPool
name|m_async_pool
decl_stmt|;
comment|// The actual implementation of the service - this is a member because we need to
comment|// close it on stop. Note, security is not part of this implementation but is
comment|// added via a decorator in the start method (this is the wrapped object without
comment|// the wrapper).
specifier|private
specifier|volatile
name|EventAdminImpl
name|m_admin
decl_stmt|;
comment|// The registration of the security decorator factory (i.e., the service)
specifier|private
specifier|volatile
name|ServiceRegistration
name|m_registration
decl_stmt|;
comment|// all adapters
specifier|private
name|AbstractAdapter
index|[]
name|m_adapters
decl_stmt|;
specifier|private
name|ServiceRegistration
name|m_managedServiceReg
decl_stmt|;
specifier|public
name|Configuration
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|m_bundleContext
operator|=
name|bundleContext
expr_stmt|;
comment|// default configuration
name|configure
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|startOrUpdate
argument_list|()
expr_stmt|;
comment|// check for Configuration Admin configuration
try|try
block|{
name|Object
name|service
init|=
name|tryToCreateManagedService
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
comment|// add meta type provider if interfaces are available
name|Object
name|enhancedService
init|=
name|tryToCreateMetaTypeProvider
argument_list|(
name|service
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|interfaceNames
decl_stmt|;
if|if
condition|(
name|enhancedService
operator|==
literal|null
condition|)
block|{
name|interfaceNames
operator|=
operator|new
name|String
index|[]
block|{
name|ManagedService
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
expr_stmt|;
block|}
else|else
block|{
name|interfaceNames
operator|=
operator|new
name|String
index|[]
block|{
name|ManagedService
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|MetaTypeProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
expr_stmt|;
name|service
operator|=
name|enhancedService
expr_stmt|;
block|}
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
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
name|PID
argument_list|)
expr_stmt|;
name|m_managedServiceReg
operator|=
name|m_bundleContext
operator|.
name|registerService
argument_list|(
name|interfaceNames
argument_list|,
name|service
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// don't care
block|}
block|}
name|void
name|updateFromConfigAdmin
parameter_list|(
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|config
parameter_list|)
block|{
comment|// do this in the background as we don't want to stop
comment|// the config admin
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
synchronized|synchronized
init|(
name|Configuration
operator|.
name|this
init|)
block|{
name|Configuration
operator|.
name|this
operator|.
name|configure
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|Configuration
operator|.
name|this
operator|.
name|startOrUpdate
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Configures this instance.      */
name|void
name|configure
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
comment|// The size of the internal thread pool. Note that we must execute
comment|// each synchronous event dispatch that happens in the synchronous event
comment|// dispatching thread in a new thread, hence a small thread pool is o.k.
comment|// A value of less then 2 triggers the default value. A value of 2
comment|// effectively disables thread pooling. Furthermore, this will be used by
comment|// a lazy thread pool (i.e., new threads are created when needed). Ones the
comment|// the size is reached and no cached thread is available new threads will
comment|// be created.
name|m_threadPoolSize
operator|=
name|getIntProperty
argument_list|(
name|PROP_THREAD_POOL_SIZE
argument_list|,
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_THREAD_POOL_SIZE
argument_list|)
argument_list|,
literal|20
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// The ratio of asynchronous to synchronous threads in the internal thread
comment|// pool.  Ratio must be positive and may be adjusted to represent the
comment|// distribution of post to send operations.  Applications with higher number
comment|// of post operations should have a higher ratio.
name|m_asyncToSyncThreadRatio
operator|=
name|getDoubleProperty
argument_list|(
name|PROP_ASYNC_TO_SYNC_THREAD_RATIO
argument_list|,
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_ASYNC_TO_SYNC_THREAD_RATIO
argument_list|)
argument_list|,
literal|0.5
argument_list|,
literal|0.0
argument_list|)
expr_stmt|;
comment|// The timeout in milliseconds - A value of less then 100 turns timeouts off.
comment|// Any other value is the time in milliseconds granted to each EventHandler
comment|// before it gets blacklisted.
name|m_timeout
operator|=
name|getIntProperty
argument_list|(
name|PROP_TIMEOUT
argument_list|,
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_TIMEOUT
argument_list|)
argument_list|,
literal|5000
argument_list|,
name|Integer
operator|.
name|MIN_VALUE
argument_list|)
expr_stmt|;
comment|// Are EventHandler required to be registered with a topic? - The default is
comment|// true. The specification says that EventHandler must register with a list
comment|// of topics they are interested in. Setting this value to false will enable
comment|// that handlers without a topic are receiving all events
comment|// (i.e., they are treated the same as with a topic=*).
name|m_requireTopic
operator|=
name|getBooleanProperty
argument_list|(
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_REQUIRE_TOPIC
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|String
name|value
init|=
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_IGNORE_TIMEOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|m_ignoreTimeout
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|value
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|m_ignoreTimeout
operator|=
operator|new
name|String
index|[
name|st
operator|.
name|countTokens
argument_list|()
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|m_ignoreTimeout
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|m_ignoreTimeout
index|[
name|i
index|]
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
block|}
specifier|final
name|String
name|valueIgnoreTopic
init|=
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_IGNORE_TOPIC
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueIgnoreTopic
operator|==
literal|null
condition|)
block|{
name|m_ignoreTopics
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|valueIgnoreTopic
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|m_ignoreTopics
operator|=
operator|new
name|String
index|[
name|st
operator|.
name|countTokens
argument_list|()
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|m_ignoreTopics
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|m_ignoreTopics
index|[
name|i
index|]
operator|=
name|st
operator|.
name|nextToken
argument_list|()
expr_stmt|;
block|}
block|}
name|m_logLevel
operator|=
name|getIntProperty
argument_list|(
name|PROP_LOG_LEVEL
argument_list|,
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_LOG_LEVEL
argument_list|)
argument_list|,
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
comment|// default log level is WARNING
name|LogWrapper
operator|.
name|LOG_ERROR
argument_list|)
expr_stmt|;
name|m_addTimestamp
operator|=
name|getBooleanProperty
argument_list|(
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_ADD_TIMESTAMP
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|m_addSubject
operator|=
name|getBooleanProperty
argument_list|(
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_ADD_SUBJECT
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m_threadPoolSize
operator|=
name|getIntProperty
argument_list|(
name|PROP_THREAD_POOL_SIZE
argument_list|,
name|config
operator|.
name|get
argument_list|(
name|PROP_THREAD_POOL_SIZE
argument_list|)
argument_list|,
literal|20
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|m_asyncToSyncThreadRatio
operator|=
name|getDoubleProperty
argument_list|(
name|PROP_ASYNC_TO_SYNC_THREAD_RATIO
argument_list|,
name|m_bundleContext
operator|.
name|getProperty
argument_list|(
name|PROP_ASYNC_TO_SYNC_THREAD_RATIO
argument_list|)
argument_list|,
literal|0.5
argument_list|,
literal|0.0
argument_list|)
expr_stmt|;
name|m_timeout
operator|=
name|getIntProperty
argument_list|(
name|PROP_TIMEOUT
argument_list|,
name|config
operator|.
name|get
argument_list|(
name|PROP_TIMEOUT
argument_list|)
argument_list|,
literal|5000
argument_list|,
name|Integer
operator|.
name|MIN_VALUE
argument_list|)
expr_stmt|;
name|m_requireTopic
operator|=
name|getBooleanProperty
argument_list|(
name|config
operator|.
name|get
argument_list|(
name|PROP_REQUIRE_TOPIC
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|m_ignoreTimeout
operator|=
literal|null
expr_stmt|;
specifier|final
name|Object
name|value
init|=
name|config
operator|.
name|get
argument_list|(
name|PROP_IGNORE_TIMEOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|m_ignoreTimeout
operator|=
operator|new
name|String
index|[]
block|{
operator|(
name|String
operator|)
name|value
block|}
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
index|[]
condition|)
block|{
name|m_ignoreTimeout
operator|=
operator|(
name|String
index|[]
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
literal|"Value for property: "
operator|+
name|PROP_IGNORE_TIMEOUT
operator|+
literal|" is neither a string nor a string array - Using default"
argument_list|)
expr_stmt|;
block|}
name|m_ignoreTopics
operator|=
literal|null
expr_stmt|;
specifier|final
name|Object
name|valueIT
init|=
name|config
operator|.
name|get
argument_list|(
name|PROP_IGNORE_TOPIC
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueIT
operator|instanceof
name|String
condition|)
block|{
name|m_ignoreTopics
operator|=
operator|new
name|String
index|[]
block|{
operator|(
name|String
operator|)
name|valueIT
block|}
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|valueIT
operator|instanceof
name|String
index|[]
condition|)
block|{
name|m_ignoreTopics
operator|=
operator|(
name|String
index|[]
operator|)
name|valueIT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|valueIT
operator|!=
literal|null
condition|)
block|{
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
literal|"Value for property: "
operator|+
name|PROP_IGNORE_TOPIC
operator|+
literal|" is neither a string nor a string array - Using default"
argument_list|)
expr_stmt|;
block|}
name|m_logLevel
operator|=
name|getIntProperty
argument_list|(
name|PROP_LOG_LEVEL
argument_list|,
name|config
operator|.
name|get
argument_list|(
name|PROP_LOG_LEVEL
argument_list|)
argument_list|,
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
comment|// default log level is WARNING
name|LogWrapper
operator|.
name|LOG_ERROR
argument_list|)
expr_stmt|;
name|m_addTimestamp
operator|=
name|getBooleanProperty
argument_list|(
name|config
operator|.
name|get
argument_list|(
name|PROP_ADD_TIMESTAMP
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|m_addSubject
operator|=
name|getBooleanProperty
argument_list|(
name|config
operator|.
name|get
argument_list|(
name|PROP_ADD_SUBJECT
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// a timeout less or equals to 100 means : disable timeout
if|if
condition|(
name|m_timeout
operator|<=
literal|100
condition|)
block|{
name|m_timeout
operator|=
literal|0
expr_stmt|;
block|}
name|m_asyncThreadPoolSize
operator|=
name|m_threadPoolSize
operator|>
literal|5
condition|?
operator|(
name|int
operator|)
name|Math
operator|.
name|floor
argument_list|(
name|m_threadPoolSize
operator|*
name|m_asyncToSyncThreadRatio
argument_list|)
else|:
literal|2
expr_stmt|;
block|}
specifier|private
name|void
name|startOrUpdate
parameter_list|()
block|{
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|setLogLevel
argument_list|(
name|m_logLevel
argument_list|)
expr_stmt|;
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_DEBUG
argument_list|,
name|PROP_LOG_LEVEL
operator|+
literal|"="
operator|+
name|m_logLevel
argument_list|)
expr_stmt|;
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_DEBUG
argument_list|,
name|PROP_THREAD_POOL_SIZE
operator|+
literal|"="
operator|+
name|m_threadPoolSize
argument_list|)
expr_stmt|;
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_DEBUG
argument_list|,
name|PROP_ASYNC_TO_SYNC_THREAD_RATIO
operator|+
literal|"="
operator|+
name|m_asyncToSyncThreadRatio
argument_list|)
expr_stmt|;
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_DEBUG
argument_list|,
literal|"Async Pool Size="
operator|+
name|m_asyncThreadPoolSize
argument_list|)
expr_stmt|;
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_DEBUG
argument_list|,
name|PROP_TIMEOUT
operator|+
literal|"="
operator|+
name|m_timeout
argument_list|)
expr_stmt|;
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_DEBUG
argument_list|,
name|PROP_REQUIRE_TOPIC
operator|+
literal|"="
operator|+
name|m_requireTopic
argument_list|)
expr_stmt|;
comment|// Note that this uses a lazy thread pool that will create new threads on
comment|// demand - in case none of its cached threads is free - until threadPoolSize
comment|// is reached. Subsequently, a threadPoolSize of 2 effectively disables
comment|// caching of threads.
if|if
condition|(
name|m_sync_pool
operator|==
literal|null
condition|)
block|{
name|m_sync_pool
operator|=
operator|new
name|DefaultThreadPool
argument_list|(
name|m_threadPoolSize
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m_sync_pool
operator|.
name|configure
argument_list|(
name|m_threadPoolSize
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|asyncThreadPoolSize
init|=
name|m_asyncThreadPoolSize
decl_stmt|;
if|if
condition|(
name|m_async_pool
operator|==
literal|null
condition|)
block|{
name|m_async_pool
operator|=
operator|new
name|DefaultThreadPool
argument_list|(
name|asyncThreadPoolSize
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m_async_pool
operator|.
name|configure
argument_list|(
name|asyncThreadPoolSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m_admin
operator|==
literal|null
condition|)
block|{
name|m_admin
operator|=
operator|new
name|EventAdminImpl
argument_list|(
name|m_bundleContext
argument_list|,
name|m_sync_pool
argument_list|,
name|m_async_pool
argument_list|,
name|m_timeout
argument_list|,
name|m_ignoreTimeout
argument_list|,
name|m_requireTopic
argument_list|,
name|m_ignoreTopics
argument_list|,
name|m_addTimestamp
argument_list|,
name|m_addSubject
argument_list|)
expr_stmt|;
comment|// Finally, adapt the outside events to our kind of events as per spec
name|adaptEvents
argument_list|(
name|m_admin
argument_list|)
expr_stmt|;
comment|// register the admin wrapped in a service factory (SecureEventAdminFactory)
comment|// that hands-out the m_admin object wrapped in a decorator that checks
comment|// appropriated permissions of each calling bundle
name|m_registration
operator|=
name|m_bundleContext
operator|.
name|registerService
argument_list|(
name|EventAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|SecureEventAdminFactory
argument_list|(
name|m_admin
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m_admin
operator|.
name|update
argument_list|(
name|m_timeout
argument_list|,
name|m_ignoreTimeout
argument_list|,
name|m_requireTopic
argument_list|,
name|m_ignoreTopics
argument_list|,
name|m_addTimestamp
argument_list|,
name|m_addSubject
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called upon stopping the bundle. This will block until all pending events are      * delivered. An IllegalStateException will be thrown on new events starting with      * the begin of this method. However, it might take some time until we settle      * down which is somewhat cumbersome given that the spec asks for return in      * a timely manner.      */
specifier|public
name|void
name|destroy
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|m_adapters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractAdapter
name|adapter
range|:
name|m_adapters
control|)
block|{
name|adapter
operator|.
name|destroy
argument_list|(
name|m_bundleContext
argument_list|)
expr_stmt|;
block|}
name|m_adapters
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|m_managedServiceReg
operator|!=
literal|null
condition|)
block|{
name|m_managedServiceReg
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|m_managedServiceReg
operator|=
literal|null
expr_stmt|;
block|}
comment|// We need to unregister manually
if|if
condition|(
name|m_registration
operator|!=
literal|null
condition|)
block|{
name|m_registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|m_registration
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|m_admin
operator|!=
literal|null
condition|)
block|{
name|m_admin
operator|.
name|stop
argument_list|()
expr_stmt|;
name|m_admin
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|m_async_pool
operator|!=
literal|null
condition|)
block|{
name|m_async_pool
operator|.
name|close
argument_list|()
expr_stmt|;
name|m_async_pool
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|m_sync_pool
operator|!=
literal|null
condition|)
block|{
name|m_sync_pool
operator|.
name|close
argument_list|()
expr_stmt|;
name|m_sync_pool
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Init the adapters in org.apache.felix.eventadmin.impl.adapter      */
specifier|private
name|void
name|adaptEvents
parameter_list|(
specifier|final
name|EventAdmin
name|admin
parameter_list|)
block|{
name|m_adapters
operator|=
operator|new
name|AbstractAdapter
index|[
literal|3
index|]
expr_stmt|;
name|m_adapters
index|[
literal|0
index|]
operator|=
operator|new
name|FrameworkEventAdapter
argument_list|(
name|m_bundleContext
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|m_adapters
index|[
literal|1
index|]
operator|=
operator|new
name|BundleEventAdapter
argument_list|(
name|m_bundleContext
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|m_adapters
index|[
literal|2
index|]
operator|=
operator|new
name|ServiceEventAdapter
argument_list|(
name|m_bundleContext
argument_list|,
name|admin
argument_list|)
expr_stmt|;
comment|// KARAF: disable log events as they are published by PaxLogging
comment|//m_adapters[3] = new LogEventAdapter(m_bundleContext, admin);
block|}
specifier|private
name|Object
name|tryToCreateMetaTypeProvider
parameter_list|(
specifier|final
name|Object
name|managedService
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|MetaTypeProviderImpl
argument_list|(
operator|(
name|ManagedService
operator|)
name|managedService
argument_list|,
name|m_threadPoolSize
argument_list|,
name|m_timeout
argument_list|,
name|m_requireTopic
argument_list|,
name|m_ignoreTimeout
argument_list|,
name|m_ignoreTopics
argument_list|,
name|m_asyncToSyncThreadRatio
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
comment|// we simply ignore this
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Object
name|tryToCreateManagedService
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ManagedService
operator|)
name|this
operator|::
name|updateFromConfigAdmin
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// we simply ignore this
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns either the parsed int from the value of the property if it is set and      * not less then the min value or the default. Additionally, a warning is      * generated in case the value is erroneous (i.e., can not be parsed as an int or      * is less then the min value).      */
specifier|private
name|int
name|getIntProperty
parameter_list|(
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|,
specifier|final
name|int
name|defaultValue
parameter_list|,
specifier|final
name|int
name|min
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|value
condition|)
block|{
specifier|final
name|int
name|result
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|Integer
operator|)
name|value
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|result
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
literal|"Unable to parse property: "
operator|+
name|key
operator|+
literal|" - Using default"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|defaultValue
return|;
block|}
block|}
if|if
condition|(
name|result
operator|>=
name|min
condition|)
block|{
return|return
name|result
return|;
block|}
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
literal|"Value for property: "
operator|+
name|key
operator|+
literal|" is to low - Using default"
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultValue
return|;
block|}
comment|/**      * Returns either the parsed double from the value of the property if it is set and      * not less then the min value or the default. Additionally, a warning is      * generated in case the value is erroneous (i.e., can not be parsed as an double or      * is less then the min value).      */
specifier|private
name|double
name|getDoubleProperty
parameter_list|(
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|,
specifier|final
name|double
name|defaultValue
parameter_list|,
specifier|final
name|double
name|min
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|value
condition|)
block|{
specifier|final
name|double
name|result
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Double
condition|)
block|{
name|result
operator|=
operator|(
operator|(
name|Double
operator|)
name|value
operator|)
operator|.
name|doubleValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|result
operator|=
name|Double
operator|.
name|parseDouble
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
literal|"Unable to parse property: "
operator|+
name|key
operator|+
literal|" - Using default"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|defaultValue
return|;
block|}
block|}
if|if
condition|(
name|result
operator|>=
name|min
condition|)
block|{
return|return
name|result
return|;
block|}
name|LogWrapper
operator|.
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|LogWrapper
operator|.
name|LOG_WARNING
argument_list|,
literal|"Value for property: "
operator|+
name|key
operator|+
literal|" is to low - Using default"
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultValue
return|;
block|}
comment|/**      * Returns true if the value of the property is set and is either 1, true, or yes      * Returns false if the value of the property is set and is either 0, false, or no      * Returns the defaultValue otherwise      */
specifier|private
name|boolean
name|getBooleanProperty
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|,
specifier|final
name|boolean
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|obj
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
operator|(
name|Boolean
operator|)
name|obj
operator|)
operator|.
name|booleanValue
argument_list|()
return|;
block|}
name|String
name|value
init|=
name|obj
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|value
operator|.
name|length
argument_list|()
operator|&&
operator|(
literal|"0"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|||
literal|"no"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
literal|0
operator|<
name|value
operator|.
name|length
argument_list|()
operator|&&
operator|(
literal|"1"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|||
literal|"true"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|||
literal|"yes"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
name|defaultValue
return|;
block|}
block|}
end_class

end_unit

