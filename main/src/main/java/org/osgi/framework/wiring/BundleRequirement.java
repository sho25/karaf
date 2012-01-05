begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2010, 2011). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A requirement that has been declared from a {@link BundleRevision bundle  * revision}.  *   * @ThreadSafe  * @noimplement  * @version $Id: 659132c1fac7526240df377ead0e1bc8d4af2e77 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundleRequirement
block|{
comment|/** 	 * Returns the name space of this requirement. 	 *  	 * @return The name space of this requirement. 	 */
name|String
name|getNamespace
parameter_list|()
function_decl|;
comment|/** 	 * Returns the directives of this requirement. 	 *  	 * @return An unmodifiable map of directive names to directive values for 	 *         this requirement, or an empty map if this requirement has no 	 *         directives. 	 */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDirectives
parameter_list|()
function_decl|;
comment|/** 	 * Returns the attributes of this requirement. 	 *  	 * @return An unmodifiable map of attribute names to attribute values for 	 *         this requirement, or an empty map if this requirement has no 	 *         attributes. 	 */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundle revision declaring this requirement. 	 *  	 * @return The bundle revision declaring this requirement. 	 */
name|BundleRevision
name|getRevision
parameter_list|()
function_decl|;
comment|/** 	 * Returns whether the specified capability matches this requirement. 	 *  	 * @param capability The capability to match to this requirement. 	 * @return {@code true} if the specified capability has the same 	 *         {@link #getNamespace() name space} as this requirement and the 	 *         filter for this requirement matches the 	 *         {@link BundleCapability#getAttributes() attributes of the 	 *         specified capability}; {@code false} otherwise. 	 */
name|boolean
name|matches
parameter_list|(
name|BundleCapability
name|capability
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

