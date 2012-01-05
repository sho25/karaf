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
name|EventObject
import|;
end_import

begin_comment
comment|/**  * An event from the Framework describing a bundle lifecycle change.  *<p>  * {@code BundleEvent} objects are delivered to  * {@code SynchronousBundleListener}s and {@code BundleListener}s  * when a change occurs in a bundle's lifecycle. A type code is used to identify  * the event type for future extendability.  *   *<p>  * OSGi Alliance reserves the right to extend the set of types.  *   * @Immutable  * @see BundleListener  * @see SynchronousBundleListener  * @version $Id: ed3c40cd707bed45681cadce114a6cc5db27a844 $  */
end_comment

begin_class
specifier|public
class|class
name|BundleEvent
extends|extends
name|EventObject
block|{
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4080640865971756012L
decl_stmt|;
comment|/** 	 * Bundle that had a change occur in its lifecycle. 	 */
specifier|private
specifier|final
name|Bundle
name|bundle
decl_stmt|;
comment|/** 	 * Type of bundle lifecycle change. 	 */
specifier|private
specifier|final
name|int
name|type
decl_stmt|;
comment|/** 	 * The bundle has been installed. 	 *  	 * @see BundleContext#installBundle(String) 	 */
specifier|public
specifier|final
specifier|static
name|int
name|INSTALLED
init|=
literal|0x00000001
decl_stmt|;
comment|/** 	 * The bundle has been started. 	 *<p> 	 * The bundle's 	 * {@link BundleActivator#start(BundleContext) BundleActivator start} method 	 * has been executed if the bundle has a bundle activator class. 	 *  	 * @see Bundle#start() 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STARTED
init|=
literal|0x00000002
decl_stmt|;
comment|/** 	 * The bundle has been stopped. 	 *<p> 	 * The bundle's 	 * {@link BundleActivator#stop(BundleContext) BundleActivator stop} method 	 * has been executed if the bundle has a bundle activator class. 	 *  	 * @see Bundle#stop() 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STOPPED
init|=
literal|0x00000004
decl_stmt|;
comment|/** 	 * The bundle has been updated. 	 *  	 * @see Bundle#update() 	 */
specifier|public
specifier|final
specifier|static
name|int
name|UPDATED
init|=
literal|0x00000008
decl_stmt|;
comment|/** 	 * The bundle has been uninstalled. 	 *  	 * @see Bundle#uninstall 	 */
specifier|public
specifier|final
specifier|static
name|int
name|UNINSTALLED
init|=
literal|0x00000010
decl_stmt|;
comment|/** 	 * The bundle has been resolved. 	 *  	 * @see Bundle#RESOLVED 	 * @since 1.3 	 */
specifier|public
specifier|final
specifier|static
name|int
name|RESOLVED
init|=
literal|0x00000020
decl_stmt|;
comment|/** 	 * The bundle has been unresolved. 	 *  	 * @see Bundle#INSTALLED 	 * @since 1.3 	 */
specifier|public
specifier|final
specifier|static
name|int
name|UNRESOLVED
init|=
literal|0x00000040
decl_stmt|;
comment|/** 	 * The bundle is about to be activated. 	 *<p> 	 * The bundle's 	 * {@link BundleActivator#start(BundleContext) BundleActivator start} method 	 * is about to be called if the bundle has a bundle activator class. This 	 * event is only delivered to {@link SynchronousBundleListener}s. It is not 	 * delivered to {@code BundleListener}s. 	 *  	 * @see Bundle#start() 	 * @since 1.3 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STARTING
init|=
literal|0x00000080
decl_stmt|;
comment|/** 	 * The bundle is about to deactivated. 	 *<p> 	 * The bundle's 	 * {@link BundleActivator#stop(BundleContext) BundleActivator stop} method 	 * is about to be called if the bundle has a bundle activator class. This 	 * event is only delivered to {@link SynchronousBundleListener}s. It is not 	 * delivered to {@code BundleListener}s. 	 *  	 * @see Bundle#stop() 	 * @since 1.3 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STOPPING
init|=
literal|0x00000100
decl_stmt|;
comment|/** 	 * The bundle will be lazily activated. 	 *<p> 	 * The bundle has a {@link Constants#ACTIVATION_LAZY lazy activation policy} 	 * and is waiting to be activated. It is now in the 	 * {@link Bundle#STARTING STARTING} state and has a valid 	 * {@code BundleContext}. This event is only delivered to 	 * {@link SynchronousBundleListener}s. It is not delivered to 	 * {@code BundleListener}s. 	 *  	 * @since 1.4 	 */
specifier|public
specifier|final
specifier|static
name|int
name|LAZY_ACTIVATION
init|=
literal|0x00000200
decl_stmt|;
comment|/** 	 * Bundle that was the origin of the event. For install event type, this is 	 * the bundle whose context was used to install the bundle. Otherwise it is 	 * the bundle itself. 	 *  	 * @since 1.6 	 */
specifier|private
specifier|final
name|Bundle
name|origin
decl_stmt|;
comment|/** 	 * Creates a bundle event of the specified type. 	 *  	 * @param type The event type. 	 * @param bundle The bundle which had a lifecycle change. 	 * @param origin The bundle which is the origin of the event. For the event 	 *        type {@link #INSTALLED}, this is the bundle whose context was used 	 *        to install the bundle. Otherwise it is the bundle itself. 	 * @since 1.6 	 */
specifier|public
name|BundleEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|Bundle
name|bundle
parameter_list|,
name|Bundle
name|origin
parameter_list|)
block|{
name|super
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
if|if
condition|(
name|origin
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"null origin"
argument_list|)
throw|;
block|}
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|origin
operator|=
name|origin
expr_stmt|;
block|}
comment|/** 	 * Creates a bundle event of the specified type. 	 *  	 * @param type The event type. 	 * @param bundle The bundle which had a lifecycle change. This bundle is 	 *        used as the origin of the event. 	 */
specifier|public
name|BundleEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
name|super
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|origin
operator|=
name|bundle
expr_stmt|;
block|}
comment|/** 	 * Returns the bundle which had a lifecycle change. This bundle is the 	 * source of the event. 	 *  	 * @return The bundle that had a change occur in its lifecycle. 	 */
specifier|public
name|Bundle
name|getBundle
parameter_list|()
block|{
return|return
name|bundle
return|;
block|}
comment|/** 	 * Returns the type of lifecyle event. The type values are: 	 *<ul> 	 *<li>{@link #INSTALLED} 	 *<li>{@link #RESOLVED} 	 *<li>{@link #LAZY_ACTIVATION} 	 *<li>{@link #STARTING} 	 *<li>{@link #STARTED} 	 *<li>{@link #STOPPING} 	 *<li>{@link #STOPPED} 	 *<li>{@link #UPDATED} 	 *<li>{@link #UNRESOLVED} 	 *<li>{@link #UNINSTALLED} 	 *</ul> 	 *  	 * @return The type of lifecycle event. 	 */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/** 	 * Returns the bundle that was the origin of the event. 	 *  	 *<p> 	 * For the event type {@link #INSTALLED}, this is the bundle whose context 	 * was used to install the bundle. Otherwise it is the bundle itself. 	 *  	 * @return The bundle that was the origin of the event. 	 * @since 1.6 	 */
specifier|public
name|Bundle
name|getOrigin
parameter_list|()
block|{
return|return
name|origin
return|;
block|}
block|}
end_class

end_unit

