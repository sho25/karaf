begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2000, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
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
name|EventObject
import|;
end_import

begin_comment
comment|/**  * An event from the Framework describing a service lifecycle change.  *<p>  * {@code ServiceEvent} objects are delivered to  * {@code ServiceListener}s and {@code AllServiceListener}s when a  * change occurs in this service's lifecycle. A type code is used to identify  * the event type for future extendability.  *   *<p>  * OSGi Alliance reserves the right to extend the set of types.  *   * @Immutable  * @see ServiceListener  * @see AllServiceListener  * @version $Id: 2b9458d90004411b6ca0cb4b361bc282b04c85eb $  */
end_comment

begin_class
specifier|public
class|class
name|ServiceEvent
extends|extends
name|EventObject
block|{
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8792901483909409299L
decl_stmt|;
comment|/** 	 * Reference to the service that had a change occur in its lifecycle. 	 */
specifier|private
specifier|final
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|reference
decl_stmt|;
comment|/** 	 * Type of service lifecycle change. 	 */
specifier|private
specifier|final
name|int
name|type
decl_stmt|;
comment|/** 	 * This service has been registered. 	 *<p> 	 * This event is synchronously delivered<strong>after</strong> the service 	 * has been registered with the Framework. 	 *  	 * @see BundleContext#registerService(String[],Object,Dictionary) 	 */
specifier|public
specifier|final
specifier|static
name|int
name|REGISTERED
init|=
literal|0x00000001
decl_stmt|;
comment|/** 	 * The properties of a registered service have been modified. 	 *<p> 	 * This event is synchronously delivered<strong>after</strong> the service 	 * properties have been modified. 	 *  	 * @see ServiceRegistration#setProperties 	 */
specifier|public
specifier|final
specifier|static
name|int
name|MODIFIED
init|=
literal|0x00000002
decl_stmt|;
comment|/** 	 * This service is in the process of being unregistered. 	 *<p> 	 * This event is synchronously delivered<strong>before</strong> the service 	 * has completed unregistering. 	 *  	 *<p> 	 * If a bundle is using a service that is {@code UNREGISTERING}, the 	 * bundle should release its use of the service when it receives this event. 	 * If the bundle does not release its use of the service when it receives 	 * this event, the Framework will automatically release the bundle's use of 	 * the service while completing the service unregistration operation. 	 *  	 * @see ServiceRegistration#unregister 	 * @see BundleContext#ungetService 	 */
specifier|public
specifier|final
specifier|static
name|int
name|UNREGISTERING
init|=
literal|0x00000004
decl_stmt|;
comment|/** 	 * The properties of a registered service have been modified and the new 	 * properties no longer match the listener's filter. 	 *<p> 	 * This event is synchronously delivered<strong>after</strong> the service 	 * properties have been modified. This event is only delivered to listeners 	 * which were added with a non-{@code null} filter where the filter 	 * matched the service properties prior to the modification but the filter 	 * does not match the modified service properties. 	 *  	 * @see ServiceRegistration#setProperties 	 * @since 1.5 	 */
specifier|public
specifier|final
specifier|static
name|int
name|MODIFIED_ENDMATCH
init|=
literal|0x00000008
decl_stmt|;
comment|/** 	 * Creates a new service event object. 	 *  	 * @param type The event type. 	 * @param reference A {@code ServiceReference} object to the service 	 * 	that had a lifecycle change. 	 */
specifier|public
name|ServiceEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|reference
parameter_list|)
block|{
name|super
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|this
operator|.
name|reference
operator|=
name|reference
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/** 	 * Returns a reference to the service that had a change occur in its 	 * lifecycle. 	 *<p> 	 * This reference is the source of the event. 	 *  	 * @return Reference to the service that had a lifecycle change. 	 */
specifier|public
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|getServiceReference
parameter_list|()
block|{
return|return
name|reference
return|;
block|}
comment|/** 	 * Returns the type of event. The event type values are: 	 *<ul> 	 *<li>{@link #REGISTERED}</li>  	 *<li>{@link #MODIFIED}</li>  	 *<li>{@link #MODIFIED_ENDMATCH}</li>  	 *<li>{@link #UNREGISTERING}</li> 	 *</ul> 	 *  	 * @return Type of service lifecycle change. 	 */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

