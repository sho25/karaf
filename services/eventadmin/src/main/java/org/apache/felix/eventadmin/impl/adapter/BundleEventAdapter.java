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
name|adapter
package|;
end_package

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
name|BundleEvent
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
name|SynchronousBundleListener
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

begin_comment
comment|/**  * This class registers itself as a listener for bundle events and posts them via  * the EventAdmin as specified in 113.6.4 OSGi R4 compendium.  *  * @author<a href="mailto:dev@felix.apache.org">Felix Project Team</a>  */
end_comment

begin_class
specifier|public
class|class
name|BundleEventAdapter
extends|extends
name|AbstractAdapter
implements|implements
name|SynchronousBundleListener
block|{
comment|/**      * The constructor of the adapter. This will register the adapter with the given      * context as a<code>BundleListener</code> and subsequently, will post received      * events via the given EventAdmin.      *      * @param context The bundle context with which to register as a listener.      * @param admin The<code>EventAdmin</code> to use for posting events.      */
specifier|public
name|BundleEventAdapter
parameter_list|(
specifier|final
name|BundleContext
name|context
parameter_list|,
specifier|final
name|EventAdmin
name|admin
parameter_list|)
block|{
name|super
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|context
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * Once a bundle event is received this method assembles and posts an event via      * the<code>EventAdmin</code> as specified in 113.6.4 OSGi R4 compendium.      *      * @param event The event to adapt.      */
annotation|@
name|Override
specifier|public
name|void
name|bundleChanged
parameter_list|(
specifier|final
name|BundleEvent
name|event
parameter_list|)
block|{
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|EVENT
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"bundle.id"
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|String
name|symbolicName
init|=
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|symbolicName
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|EventConstants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|symbolicName
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
literal|"bundle"
argument_list|,
name|event
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|StringBuilder
name|topic
init|=
operator|new
name|StringBuilder
argument_list|(
name|BundleEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|event
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|BundleEvent
operator|.
name|INSTALLED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"INSTALLED"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|STARTING
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"STARTING"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|STARTED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"STARTED"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|STOPPING
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"STOPPING"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|STOPPED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"STOPPED"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|UPDATED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"UPDATED"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|UNINSTALLED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"UNINSTALLED"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|RESOLVED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"RESOLVED"
argument_list|)
expr_stmt|;
break|break;
case|case
name|BundleEvent
operator|.
name|UNRESOLVED
case|:
name|topic
operator|.
name|append
argument_list|(
literal|"UNRESOLVED"
argument_list|)
expr_stmt|;
break|break;
default|default:
return|return;
comment|// IGNORE EVENT
block|}
try|try
block|{
name|getEventAdmin
argument_list|()
operator|.
name|postEvent
argument_list|(
operator|new
name|Event
argument_list|(
name|topic
operator|.
name|toString
argument_list|()
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// This is o.k. - indicates that we are stopped.
block|}
block|}
block|}
end_class

end_unit

