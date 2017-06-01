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
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
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
name|EventHandlerTracker
operator|.
name|Matcher
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
name|AsyncDeliverTasks
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
name|tasks
operator|.
name|SyncDeliverTasks
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

begin_comment
comment|/**  * This is the actual implementation of the OSGi R4 Event Admin Service (see the  * Compendium 113 for details). The implementation uses a<tt>HandlerTasks</tt>  * in order to determine applicable<tt>EventHandler</tt> for a specific event and  * subsequently dispatches the event to the handlers via<tt>DeliverTasks</tt>.  * To do this, it uses two different<tt>DeliverTasks</tt> one for asynchronous and  * one for synchronous event delivery depending on whether its<tt>post()</tt> or  * its<tt>send()</tt> method is called. Note that the actual work is done in the  * implementations of the<tt>DeliverTasks</tt>. Additionally, a stop method is  * provided that prevents subsequent events to be delivered.  *  * @author<a href="mailto:dev@felix.apache.org">Felix Project Team</a>  */
end_comment

begin_class
specifier|public
class|class
name|EventAdminImpl
implements|implements
name|EventAdmin
block|{
comment|/** The tracker for the event handlers. */
specifier|private
specifier|volatile
name|EventHandlerTracker
name|tracker
decl_stmt|;
comment|// The asynchronous event dispatcher
specifier|private
specifier|final
name|AsyncDeliverTasks
name|m_postManager
decl_stmt|;
comment|// The synchronous event dispatcher
specifier|private
specifier|final
name|SyncDeliverTasks
name|m_sendManager
decl_stmt|;
comment|// matchers for ignore topics
specifier|private
name|Matcher
index|[]
name|m_ignoreTopics
decl_stmt|;
specifier|private
name|boolean
name|addTimestamp
decl_stmt|;
specifier|private
name|boolean
name|addSubject
decl_stmt|;
comment|/**      * The constructor of the<code>EventAdmin</code> implementation.      *      * @param bundleContext The bundle context to use.      * @param syncPool The synchronous thread pool.      * @param asyncPool The asynchronous thread pool.      * @param timeout The thread execution timeout.      * @param ignoreTimeout The thread ignore timeout.      * @param requireTopic True to define the topic as required, false else.      * @param ignoreTopics The array of topics to ignore.      * @param addTimestamp True to add timestamp to the event, false else.      * @param addSubject True to add subject to the event, false else.      */
specifier|public
name|EventAdminImpl
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|,
specifier|final
name|DefaultThreadPool
name|syncPool
parameter_list|,
specifier|final
name|DefaultThreadPool
name|asyncPool
parameter_list|,
specifier|final
name|int
name|timeout
parameter_list|,
specifier|final
name|String
index|[]
name|ignoreTimeout
parameter_list|,
specifier|final
name|boolean
name|requireTopic
parameter_list|,
specifier|final
name|String
index|[]
name|ignoreTopics
parameter_list|,
specifier|final
name|boolean
name|addTimestamp
parameter_list|,
specifier|final
name|boolean
name|addSubject
parameter_list|)
block|{
name|checkNull
argument_list|(
name|syncPool
argument_list|,
literal|"syncPool"
argument_list|)
expr_stmt|;
name|checkNull
argument_list|(
name|asyncPool
argument_list|,
literal|"asyncPool"
argument_list|)
expr_stmt|;
name|this
operator|.
name|addTimestamp
operator|=
name|addTimestamp
expr_stmt|;
name|this
operator|.
name|addSubject
operator|=
name|addSubject
expr_stmt|;
name|this
operator|.
name|tracker
operator|=
operator|new
name|EventHandlerTracker
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|update
argument_list|(
name|ignoreTimeout
argument_list|,
name|requireTopic
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|m_sendManager
operator|=
operator|new
name|SyncDeliverTasks
argument_list|(
name|syncPool
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|m_postManager
operator|=
operator|new
name|AsyncDeliverTasks
argument_list|(
name|asyncPool
argument_list|,
name|m_sendManager
argument_list|)
expr_stmt|;
name|m_ignoreTopics
operator|=
name|EventHandlerTracker
operator|.
name|createMatchers
argument_list|(
name|ignoreTopics
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check if the event admin is active and return the tracker.      *      * @return The event tracker.      * @throws IllegalArgumentException If the event admin has been stopped.      */
specifier|private
name|EventHandlerTracker
name|getTracker
parameter_list|()
block|{
specifier|final
name|EventHandlerTracker
name|localTracker
init|=
name|tracker
decl_stmt|;
if|if
condition|(
name|localTracker
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The EventAdmin is stopped"
argument_list|)
throw|;
block|}
return|return
name|localTracker
return|;
block|}
comment|/**      * Check whether the topic should be delivered at all.      *      * @param event The event.      * @return True if the topic is delivered, false else.      */
specifier|private
name|boolean
name|checkTopic
parameter_list|(
specifier|final
name|Event
name|event
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|m_ignoreTopics
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Matcher
name|m
range|:
name|this
operator|.
name|m_ignoreTopics
control|)
block|{
if|if
condition|(
name|m
operator|.
name|match
argument_list|(
name|event
operator|.
name|getTopic
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|static
specifier|final
name|String
name|SUBJECT
init|=
literal|"subject"
decl_stmt|;
specifier|private
name|Event
name|prepareEvent
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|boolean
name|needTimeStamp
init|=
name|addTimestamp
operator|&&
operator|!
name|event
operator|.
name|containsProperty
argument_list|(
name|EventConstants
operator|.
name|TIMESTAMP
argument_list|)
decl_stmt|;
name|boolean
name|needSubject
init|=
name|addSubject
operator|&&
operator|!
name|event
operator|.
name|containsProperty
argument_list|(
name|SUBJECT
argument_list|)
decl_stmt|;
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|needSubject
condition|)
block|{
name|subject
operator|=
name|Subject
operator|.
name|getSubject
argument_list|(
name|AccessController
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|needSubject
operator|=
operator|(
name|subject
operator|!=
literal|null
operator|)
expr_stmt|;
block|}
if|if
condition|(
name|needTimeStamp
operator|||
name|needSubject
condition|)
block|{
name|String
index|[]
name|names
init|=
name|event
operator|.
name|getPropertyNames
argument_list|()
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|names
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
if|if
condition|(
operator|!
name|EventConstants
operator|.
name|EVENT_TOPIC
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|event
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|needTimeStamp
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|needSubject
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|SUBJECT
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
name|event
operator|=
operator|new
name|Event
argument_list|(
name|event
operator|.
name|getTopic
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|event
return|;
block|}
comment|/**      * Post an asynchronous event.      *      * @param event The event to be posted by this service.      * @throws IllegalStateException In case we are stopped.      *      * @see org.osgi.service.event.EventAdmin#postEvent(org.osgi.service.event.Event)      */
specifier|public
name|void
name|postEvent
parameter_list|(
specifier|final
name|Event
name|event
parameter_list|)
block|{
if|if
condition|(
name|checkTopic
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|m_postManager
operator|.
name|execute
argument_list|(
name|this
operator|.
name|getTracker
argument_list|()
operator|.
name|getHandlers
argument_list|(
name|event
argument_list|)
argument_list|,
name|prepareEvent
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Send a synchronous event.      *      * @param event The event to be send by this service.      * @throws IllegalStateException In case we are stopped.      *      * @see org.osgi.service.event.EventAdmin#sendEvent(org.osgi.service.event.Event)      */
specifier|public
name|void
name|sendEvent
parameter_list|(
specifier|final
name|Event
name|event
parameter_list|)
block|{
if|if
condition|(
name|checkTopic
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|m_sendManager
operator|.
name|execute
argument_list|(
name|this
operator|.
name|getTracker
argument_list|()
operator|.
name|getHandlers
argument_list|(
name|event
argument_list|)
argument_list|,
name|prepareEvent
argument_list|(
name|event
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method can be used to stop the delivery of events.      */
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|this
operator|.
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|this
operator|.
name|tracker
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Update the event admin with new configuration.      *      * @param timeout The event timeout.      * @param ignoreTimeout The event ignore timeout.      * @param requireTopic True if the event require a topic, false else.      * @param ignoreTopics The array of topic to ignore.      * @param addTimestamp True to add timestamp to the event, false else.      * @param addSubject True to add subject to the event, false else.      */
specifier|public
name|void
name|update
parameter_list|(
specifier|final
name|int
name|timeout
parameter_list|,
specifier|final
name|String
index|[]
name|ignoreTimeout
parameter_list|,
specifier|final
name|boolean
name|requireTopic
parameter_list|,
specifier|final
name|String
index|[]
name|ignoreTopics
parameter_list|,
specifier|final
name|boolean
name|addTimestamp
parameter_list|,
specifier|final
name|boolean
name|addSubject
parameter_list|)
block|{
name|this
operator|.
name|addTimestamp
operator|=
name|addTimestamp
expr_stmt|;
name|this
operator|.
name|addSubject
operator|=
name|addSubject
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|update
argument_list|(
name|ignoreTimeout
argument_list|,
name|requireTopic
argument_list|)
expr_stmt|;
name|this
operator|.
name|m_sendManager
operator|.
name|update
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|this
operator|.
name|m_ignoreTopics
operator|=
name|EventHandlerTracker
operator|.
name|createMatchers
argument_list|(
name|ignoreTopics
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is a utility method that will throw a<code>NullPointerException</code>      * in case that the given object is null. The message will be of the form      * "${name} + may not be null".      *      * @param object The object to check.      * @param name The object name (in the event).      * @throws NullPointerException If the object is null.      */
specifier|private
name|void
name|checkNull
parameter_list|(
specifier|final
name|Object
name|object
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|object
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
name|name
operator|+
literal|" may not be null"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

