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
name|service
operator|.
name|packageadmin
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
name|Version
import|;
end_import

begin_comment
comment|/**  * A required bundle.  *   * Objects implementing this interface are created by the Package Admin service.  *   *<p>  * The term<i>required bundle</i> refers to a resolved bundle that has a bundle  * symbolic name and is not a fragment. That is, a bundle that may be required  * by other bundles. This bundle may or may not be currently required by other  * bundles.  *   *<p>  * The information about a required bundle provided by this object may change. A  * {@code RequiredBundle} object becomes stale if an exported package of  * the bundle it references has been updated or removed as a result of calling  * {@code PackageAdmin.refreshPackages()}).  *   * If this object becomes stale, its {@code getSymbolicName()} and  * {@code getVersion()} methods continue to return their original values,  * {@code isRemovalPending()} returns true, and {@code getBundle()}  * and {@code getRequiringBundles()} return {@code null}.  *   * @since 1.2  * @ThreadSafe  * @noimplement  * @deprecated The PackageAdmin service has been replaced by the  *<code>org.osgi.framework.wiring</code> package.  * @version $Id: 1606b0422cae6769b7eedc2d565df61841da1e22 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|RequiredBundle
block|{
comment|/** 	 * Returns the symbolic name of this required bundle. 	 *  	 * @return The symbolic name of this required bundle. 	 */
specifier|public
name|String
name|getSymbolicName
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundle associated with this required bundle. 	 *  	 * @return The bundle, or {@code null} if this 	 *         {@code RequiredBundle} object has become stale. 	 */
specifier|public
name|Bundle
name|getBundle
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundles that currently require this required bundle. 	 *  	 *<p> 	 * If this required bundle is required and then re-exported by another 	 * bundle then all the requiring bundles of the re-exporting bundle are 	 * included in the returned array. 	 *  	 * @return An array of bundles currently requiring this required bundle, or 	 *         {@code null} if this {@code RequiredBundle} object 	 *         has become stale. The array will be empty if no bundles require 	 *         this required package. 	 */
specifier|public
name|Bundle
index|[]
name|getRequiringBundles
parameter_list|()
function_decl|;
comment|/** 	 * Returns the version of this required bundle. 	 *  	 * @return The version of this required bundle, or 	 *         {@link Version#emptyVersion} if no version information is 	 *         available. 	 */
specifier|public
name|Version
name|getVersion
parameter_list|()
function_decl|;
comment|/** 	 * Returns {@code true} if the bundle associated with this 	 * {@code RequiredBundle} object has been updated or uninstalled. 	 *  	 * @return {@code true} if the required bundle has been updated or 	 *         uninstalled, or if the {@code RequiredBundle} object has 	 *         become stale; {@code false} otherwise. 	 */
specifier|public
name|boolean
name|isRemovalPending
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

