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
comment|/**  * A capability that has been declared from a {@link BundleRevision bundle  * revision}.  *   * @ThreadSafe  * @noimplement  * @version $Id: 0fde13c3228af1aa97872b37ccf0aa6e23123b11 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundleCapability
block|{
comment|/** 	 * Returns the name space of this capability. 	 *  	 * @return The name space of this capability. 	 */
name|String
name|getNamespace
parameter_list|()
function_decl|;
comment|/** 	 * Returns the directives of this capability. 	 *  	 * @return An unmodifiable map of directive names to directive values for 	 *         this capability, or an empty map if this capability has no 	 *         directives. 	 */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDirectives
parameter_list|()
function_decl|;
comment|/** 	 * Returns the attributes of this capability. 	 *  	 * @return An unmodifiable map of attribute names to attribute values for 	 *         this capability, or an empty map if this capability has no 	 *         attributes. 	 */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundle revision declaring this capability. 	 *  	 * @return The bundle revision declaring this capability. 	 */
name|BundleRevision
name|getRevision
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

