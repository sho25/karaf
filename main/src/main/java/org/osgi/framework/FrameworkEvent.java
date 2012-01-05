begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2004, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|startlevel
operator|.
name|FrameworkStartLevel
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
name|wiring
operator|.
name|FrameworkWiring
import|;
end_import

begin_comment
comment|/**  * A general event from the Framework.  *   *<p>  * {@code FrameworkEvent} objects are delivered to  * {@code FrameworkListener}s when a general event occurs within the OSGi  * environment. A type code is used to identify the event type for future  * extendability.  *   *<p>  * OSGi Alliance reserves the right to extend the set of event types.  *   * @Immutable  * @see FrameworkListener  * @version $Id: e05c6ffd542fa432835961882bf6b15b0620ffb6 $  */
end_comment

begin_class
specifier|public
class|class
name|FrameworkEvent
extends|extends
name|EventObject
block|{
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|207051004521261705L
decl_stmt|;
comment|/** 	 * Bundle related to the event. 	 */
specifier|private
specifier|final
name|Bundle
name|bundle
decl_stmt|;
comment|/** 	 * Exception related to the event. 	 */
specifier|private
specifier|final
name|Throwable
name|throwable
decl_stmt|;
comment|/** 	 * Type of event. 	 */
specifier|private
specifier|final
name|int
name|type
decl_stmt|;
comment|/** 	 * The Framework has started. 	 *  	 *<p> 	 * This event is fired when the Framework has started after all installed 	 * bundles that are marked to be started have been started and the Framework 	 * has reached the initial start level. The source of this event is the 	 * System Bundle. 	 *  	 * @see "The Start Level Specification" 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STARTED
init|=
literal|0x00000001
decl_stmt|;
comment|/** 	 * An error has occurred. 	 *  	 *<p> 	 * There was an error associated with a bundle. 	 */
specifier|public
specifier|final
specifier|static
name|int
name|ERROR
init|=
literal|0x00000002
decl_stmt|;
comment|/** 	 * A FrameworkWiring.refreshBundles operation has completed. 	 *  	 *<p> 	 * This event is fired when the Framework has completed the refresh bundles 	 * operation initiated by a call to the FrameworkWiring.refreshBundles 	 * method. The source of this event is the System Bundle. 	 *  	 * @since 1.2 	 * @see FrameworkWiring#refreshBundles(java.util.Collection, 	 *      FrameworkListener...) 	 */
specifier|public
specifier|final
specifier|static
name|int
name|PACKAGES_REFRESHED
init|=
literal|0x00000004
decl_stmt|;
comment|/** 	 * A FrameworkStartLevel.setStartLevel operation has completed. 	 *  	 *<p> 	 * This event is fired when the Framework has completed changing the active 	 * start level initiated by a call to the StartLevel.setStartLevel method. 	 * The source of this event is the System Bundle. 	 *  	 * @since 1.2 	 * @see FrameworkStartLevel#setStartLevel(int, FrameworkListener...) 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STARTLEVEL_CHANGED
init|=
literal|0x00000008
decl_stmt|;
comment|/** 	 * A warning has occurred. 	 *  	 *<p> 	 * There was a warning associated with a bundle. 	 *  	 * @since 1.3 	 */
specifier|public
specifier|final
specifier|static
name|int
name|WARNING
init|=
literal|0x00000010
decl_stmt|;
comment|/** 	 * An informational event has occurred. 	 *  	 *<p> 	 * There was an informational event associated with a bundle. 	 *  	 * @since 1.3 	 */
specifier|public
specifier|final
specifier|static
name|int
name|INFO
init|=
literal|0x00000020
decl_stmt|;
comment|/** 	 * The Framework has stopped. 	 *  	 *<p> 	 * This event is fired when the Framework has been stopped because of a stop 	 * operation on the system bundle. The source of this event is the System 	 * Bundle. 	 *  	 * @since 1.5 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STOPPED
init|=
literal|0x00000040
decl_stmt|;
comment|/** 	 * The Framework has stopped during update. 	 *  	 *<p> 	 * This event is fired when the Framework has been stopped because of an 	 * update operation on the system bundle. The Framework will be restarted 	 * after this event is fired. The source of this event is the System Bundle. 	 *  	 * @since 1.5 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STOPPED_UPDATE
init|=
literal|0x00000080
decl_stmt|;
comment|/** 	 * The Framework has stopped and the boot class path has changed. 	 *  	 *<p> 	 * This event is fired when the Framework has been stopped because of a stop 	 * operation on the system bundle and a bootclasspath extension bundle has 	 * been installed or updated. The source of this event is the System Bundle. 	 *  	 * @since 1.5 	 */
specifier|public
specifier|final
specifier|static
name|int
name|STOPPED_BOOTCLASSPATH_MODIFIED
init|=
literal|0x00000100
decl_stmt|;
comment|/** 	 * The Framework did not stop before the wait timeout expired. 	 *  	 *<p> 	 * This event is fired when the Framework did not stop before the wait 	 * timeout expired. The source of this event is the System Bundle. 	 *  	 * @since 1.5 	 */
specifier|public
specifier|final
specifier|static
name|int
name|WAIT_TIMEDOUT
init|=
literal|0x00000200
decl_stmt|;
comment|/** 	 * Creates a Framework event. 	 *  	 * @param type The event type. 	 * @param source The event source object. This may not be {@code null}. 	 * @deprecated As of 1.2. This constructor is deprecated in favor of using 	 *             the other constructor with the System Bundle as the event 	 *             source. 	 */
specifier|public
name|FrameworkEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|Object
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|bundle
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|throwable
operator|=
literal|null
expr_stmt|;
block|}
comment|/** 	 * Creates a Framework event regarding the specified bundle. 	 *  	 * @param type The event type. 	 * @param bundle The event source. 	 * @param throwable The related exception. This argument may be 	 *        {@code null} if there is no related exception. 	 */
specifier|public
name|FrameworkEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|Bundle
name|bundle
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|super
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
name|this
operator|.
name|throwable
operator|=
name|throwable
expr_stmt|;
block|}
comment|/** 	 * Returns the exception related to this event. 	 *  	 * @return The related exception or {@code null} if none. 	 */
specifier|public
name|Throwable
name|getThrowable
parameter_list|()
block|{
return|return
name|throwable
return|;
block|}
comment|/** 	 * Returns the bundle associated with the event. This bundle is also the 	 * source of the event. 	 *  	 * @return The bundle associated with the event. 	 */
specifier|public
name|Bundle
name|getBundle
parameter_list|()
block|{
return|return
name|bundle
return|;
block|}
comment|/** 	 * Returns the type of framework event. 	 *<p> 	 * The type values are: 	 *<ul> 	 *<li>{@link #STARTED} 	 *<li>{@link #ERROR} 	 *<li>{@link #WARNING} 	 *<li>{@link #INFO} 	 *<li>{@link #PACKAGES_REFRESHED} 	 *<li>{@link #STARTLEVEL_CHANGED} 	 *<li>{@link #STOPPED} 	 *<li>{@link #STOPPED_BOOTCLASSPATH_MODIFIED} 	 *<li>{@link #STOPPED_UPDATE} 	 *<li>{@link #WAIT_TIMEDOUT} 	 *</ul> 	 *  	 * @return The type of state change. 	 */
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

