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
name|startlevel
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
name|Bundle
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
name|BundleReference
import|;
end_import

begin_comment
comment|/**  * Query and modify the start level information for a bundle. The start level  * object for a bundle can be obtained by calling {@link Bundle#adapt(Class)  * bundle.adapt(BundleStartLevel.class)} on the bundle.  *   *<p>  * The bundle associated with this BundleStartLevel object can be obtained by  * calling {@link BundleReference#getBundle()}.  *   * @ThreadSafe  * @noimplement  * @version $Id: 9a000be191fe3cb4ae82535a30940db0340d5356 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundleStartLevel
extends|extends
name|BundleReference
block|{
comment|/** 	 * Return the assigned start level value for the bundle. 	 *  	 * @return The start level value of the bundle. 	 * @see #setStartLevel(int) 	 * @throws IllegalStateException If the bundle has been uninstalled. 	 */
name|int
name|getStartLevel
parameter_list|()
function_decl|;
comment|/** 	 * Assign a start level value to the bundle. 	 *  	 *<p> 	 * The bundle will be assigned the specified start level. The start level 	 * value assigned to the bundle will be persistently recorded by the 	 * Framework. 	 *<p> 	 * If the new start level for the bundle is lower than or equal to the 	 * active start level of the Framework and the bundle's autostart setting 	 * indicates this bundle must be started, the Framework will start the 	 * bundle as described in the {@link Bundle#start(int)} method using the 	 * {@link Bundle#START_TRANSIENT} option. The 	 * {@link Bundle#START_ACTIVATION_POLICY} option must also be used if 	 * {@link #isActivationPolicyUsed()} returns {@code true}. The actual 	 * starting of the bundle must occur asynchronously. 	 *<p> 	 * If the new start level for the bundle is higher than the active start 	 * level of the Framework, the Framework will stop the bundle as described 	 * in the {@link Bundle#stop(int)} method using the 	 * {@link Bundle#STOP_TRANSIENT} option. The actual stopping of the bundle 	 * must occur asynchronously. 	 *  	 * @param startlevel The new start level for the bundle. 	 * @throws IllegalArgumentException If the specified start level is less 	 *         than or equal to zero, or if the bundle is the system bundle. 	 * @throws IllegalStateException If the bundle has been uninstalled. 	 * @throws SecurityException If the caller does not have 	 *         {@code AdminPermission[bundle,EXECUTE]} and the Java runtime 	 *         environment supports permissions. 	 */
name|void
name|setStartLevel
parameter_list|(
name|int
name|startlevel
parameter_list|)
function_decl|;
comment|/** 	 * Returns whether the bundle's autostart setting indicates it must be 	 * started. 	 *<p> 	 * The autostart setting of a bundle indicates whether the bundle is to be 	 * started when its start level is reached. 	 *  	 * @return {@code true} if the autostart setting of the bundle indicates it 	 *         is to be started. {@code false} otherwise. 	 * @throws IllegalStateException If this bundle has been uninstalled. 	 * @see Bundle#START_TRANSIENT 	 */
name|boolean
name|isPersistentlyStarted
parameter_list|()
function_decl|;
comment|/** 	 * Returns whether the bundle's autostart setting indicates that the 	 * activation policy declared in the bundle manifest must be used. 	 *<p> 	 * The autostart setting of a bundle indicates whether the bundle's declared 	 * activation policy is to be used when the bundle is started. 	 *  	 * @return {@code true} if the bundle's autostart setting indicates the 	 *         activation policy declared in the manifest must be used. 	 *         {@code false} if the bundle must be eagerly activated. 	 * @throws IllegalStateException If the bundle has been uninstalled. 	 * @see Bundle#START_ACTIVATION_POLICY 	 */
name|boolean
name|isActivationPolicyUsed
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

