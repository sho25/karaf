begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2011). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|wiring
package|;
end_package

begin_comment
comment|/**  * A wire connecting a {@link BundleCapability} to a {@link BundleRequirement}.  *   * @ThreadSafe  * @noimplement  * @version $Id: 4f936a84065762ec3267a44f86ae01b0150e44ce $  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundleWire
block|{
comment|/** 	 * Returns the {@link BundleCapability} for this wire. 	 *  	 * @return The {@link BundleCapability} for this wire. 	 */
name|BundleCapability
name|getCapability
parameter_list|()
function_decl|;
comment|/** 	 * Return the {@link BundleRequirement} for this wire. 	 *  	 * @return The {@link BundleRequirement} for this wire. 	 */
name|BundleRequirement
name|getRequirement
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundle wiring {@link BundleWiring#getProvidedWires(String) 	 * providing} the {@link #getCapability() capability}. 	 *  	 *<p> 	 * The bundle revision referenced by the returned bundle wiring may differ 	 * from the bundle revision reference by the {@link #getCapability() 	 * capability}. 	 *  	 * @return The bundle wiring providing the capability. If the bundle wiring 	 *         providing the capability is not {@link BundleWiring#isInUse() in 	 *         use}, {@code null} will be returned. 	 */
name|BundleWiring
name|getProviderWiring
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundle wiring who 	 * {@link BundleWiring#getRequiredWires(String) requires} the 	 * {@link #getCapability() capability}. 	 *  	 *<p> 	 * The bundle revision referenced by the returned bundle wiring may differ 	 * from the bundle revision reference by the {@link #getRequirement() 	 * requirement}. 	 *  	 * @return The bundle wiring whose requirement is wired to the capability. 	 *         If the bundle wiring requiring the capability is not 	 *         {@link BundleWiring#isInUse() in use}, {@code null} will be 	 *         returned. 	 */
name|BundleWiring
name|getRequirerWiring
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

