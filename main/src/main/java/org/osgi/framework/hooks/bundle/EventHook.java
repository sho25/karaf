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
name|bundle
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

begin_comment
comment|/**   * OSGi Framework Bundle Event Hook Service.  *   *<p>  * Bundles registering this service will be called during framework lifecycle  * (install, start, stop, update, and uninstall bundle) operations.  *   * @ThreadSafe  * @version $Id: 18ea1ec1f14f47410a43e99be4da3b2583149722 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|EventHook
block|{
comment|/** 	 * Bundle event hook method.  This method is called prior to bundle event 	 * delivery when a bundle is installed, resolved, started, stopped, unresolved, or 	 * uninstalled.  This method can filter the bundles which receive the event. 	 *<p> 	 * This method must be called by the framework one and only one time for each bundle  	 * event generated, this included bundle events which are generated when there are no  	 * bundle listeners registered.  This method must be called on the same thread that is  	 * performing the action which generated the specified event.  The specified  	 * collection includes bundle contexts with synchronous and asynchronous bundle  	 * listeners registered with them. 	 *  	 * @param event The bundle event to be delivered 	 * @param contexts A collection of Bundle Contexts for bundles which have 	 *        listeners to which the specified event will be delivered. The 	 *        implementation of this method may remove bundle contexts from the 	 *        collection to prevent the event from being delivered to the 	 *        associated bundles. The collection supports all the optional 	 *        {@code Collection} operations except {@code add} and 	 *        {@code addAll}. Attempting to add to the collection will 	 *        result in an {@code UnsupportedOperationException}. The 	 *        collection is not synchronized. 	 */
name|void
name|event
parameter_list|(
name|BundleEvent
name|event
parameter_list|,
name|Collection
argument_list|<
name|BundleContext
argument_list|>
name|contexts
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

