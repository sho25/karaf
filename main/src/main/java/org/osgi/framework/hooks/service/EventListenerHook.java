begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|hooks
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|ServiceEvent
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
name|hooks
operator|.
name|service
operator|.
name|ListenerHook
operator|.
name|ListenerInfo
import|;
end_import

begin_comment
comment|/**  * OSGi Framework Service Event Listener Hook Service.  *   *<p>  * Bundles registering this service will be called during framework service  * (register, modify, and unregister service) operations.  *   * @ThreadSafe  * @since 1.1  * @version $Id: 61c6aa7e7d4c85b3e5a6a3a340155bcda0074505 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|EventListenerHook
block|{
comment|/** 	 * Event listener hook method. This method is called prior to service event 	 * delivery when a publishing bundle registers, modifies or unregisters a 	 * service. This method can filter the listeners which receive the event. 	 *  	 * @param event The service event to be delivered. 	 * @param listeners A map of Bundle Contexts to a collection of Listener 	 *        Infos for the bundle's listeners to which the specified event will 	 *        be delivered. The implementation of this method may remove bundle 	 *        contexts from the map and listener infos from the collection 	 *        values to prevent the event from being delivered to the associated 	 *        listeners. The map supports all the optional {@code Map} 	 *        operations except {@code put} and {@code putAll}. Attempting to 	 *        add to the map will result in an 	 *        {@code UnsupportedOperationException}. The collection values in 	 *        the map supports all the optional {@code Collection} operations 	 *        except {@code add} and {@code addAll}. Attempting to add to a 	 *        collection will result in an {@code UnsupportedOperationException} 	 *        . The map and the collections are not synchronized. 	 */
name|void
name|event
parameter_list|(
name|ServiceEvent
name|event
parameter_list|,
name|Map
argument_list|<
name|BundleContext
argument_list|,
name|Collection
argument_list|<
name|ListenerInfo
argument_list|>
argument_list|>
name|listeners
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

