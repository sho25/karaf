begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2008, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_comment
comment|/**  * OSGi Framework Service Listener Hook Service.  *   *<p>  * Bundles registering this service will be called during service listener  * addition and removal.  *   * @ThreadSafe  * @version $Id: c1687e95e568589cf3e6d927b7d372c9f88c5d16 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|ListenerHook
block|{
comment|/** 	 * Added listeners hook method. This method is called to provide the hook 	 * implementation with information on newly added service listeners. This 	 * method will be called as service listeners are added while this hook is 	 * registered. Also, immediately after registration of this hook, this 	 * method will be called to provide the current collection of service 	 * listeners which had been added prior to the hook being registered. 	 *  	 * @param listeners A collection of {@link ListenerInfo}s for newly added 	 *        service listeners which are now listening to service events. 	 *        Attempting to add to or remove from the collection will result in 	 *        an {@code UnsupportedOperationException}. The collection is 	 *        not synchronized. 	 */
name|void
name|added
parameter_list|(
name|Collection
argument_list|<
name|ListenerInfo
argument_list|>
name|listeners
parameter_list|)
function_decl|;
comment|/** 	 * Removed listeners hook method. This method is called to provide the hook 	 * implementation with information on newly removed service listeners. This 	 * method will be called as service listeners are removed while this hook is 	 * registered. 	 *  	 * @param listeners A collection of {@link ListenerInfo}s for newly removed 	 *        service listeners which are no longer listening to service events. 	 *        Attempting to add to or remove from the collection will result in 	 *        an {@code UnsupportedOperationException}. The collection is 	 *        not synchronized. 	 */
name|void
name|removed
parameter_list|(
name|Collection
argument_list|<
name|ListenerInfo
argument_list|>
name|listeners
parameter_list|)
function_decl|;
comment|/** 	 * Information about a Service Listener. This interface describes the bundle 	 * which added the Service Listener and the filter with which it was added. 	 *  	 * @ThreadSafe 	 * @noimplement 	 */
specifier|public
interface|interface
name|ListenerInfo
block|{
comment|/** 		 * Return the context of the bundle which added the listener. 		 *  		 * @return The context of the bundle which added the listener. 		 */
name|BundleContext
name|getBundleContext
parameter_list|()
function_decl|;
comment|/** 		 * Return the filter string with which the listener was added. 		 *  		 * @return The filter string with which the listener was added. This may 		 *         be {@code null} if the listener was added without a 		 *         filter. 		 */
name|String
name|getFilter
parameter_list|()
function_decl|;
comment|/** 		 * Return the state of the listener for this addition and removal life 		 * cycle. Initially this method will return {@code false} 		 * indicating the listener has been added but has not been removed. 		 * After the listener has been removed, this method must always return 		 * {@code true}. 		 *  		 *<p> 		 * There is an extremely rare case in which removed notification to 		 * {@link ListenerHook}s can be made before added notification if two 		 * threads are racing to add and remove the same service listener. 		 * Because {@link ListenerHook}s are called synchronously during service 		 * listener addition and removal, the Framework cannot guarantee 		 * in-order delivery of added and removed notification for a given 		 * service listener. This method can be used to detect this rare 		 * occurrence. 		 *  		 * @return {@code false} if the listener has not been been removed, 		 *         {@code true} otherwise. 		 */
name|boolean
name|isRemoved
parameter_list|()
function_decl|;
comment|/** 		 * Compares this {@code ListenerInfo} to another 		 * {@code ListenerInfo}. Two {@code ListenerInfo}s are equals 		 * if they refer to the same listener for a given addition and removal 		 * life cycle. If the same listener is added again, it must have a 		 * different {@code ListenerInfo} which is not equal to this 		 * {@code ListenerInfo}. 		 *  		 * @param obj The object to compare against this 		 *        {@code ListenerInfo}. 		 * @return {@code true} if the other object is a 		 *         {@code ListenerInfo} object and both objects refer to 		 *         the same listener for a given addition and removal life 		 *         cycle. 		 */
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
function_decl|;
comment|/** 		 * Returns the hash code for this {@code ListenerInfo}. 		 *  		 * @return The hash code of this {@code ListenerInfo}. 		 */
name|int
name|hashCode
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

