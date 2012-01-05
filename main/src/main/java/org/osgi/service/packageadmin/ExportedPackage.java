begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2001, 2010). All Rights Reserved.  *   * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An exported package.  *   * Objects implementing this interface are created by the Package Admin service.  *   *<p>  * The term<i>exported package</i> refers to a package that has been exported  * from a resolved bundle. This package may or may not be currently wired to  * other bundles.  *   *<p>  * The information about an exported package provided by this object may change.  * An {@code ExportedPackage} object becomes stale if the package it  * references has been updated or removed as a result of calling  * {@code PackageAdmin.refreshPackages()}.  *   * If this object becomes stale, its {@code getName()} and  * {@code getVersion()} methods continue to return their original values,  * {@code isRemovalPending()} returns {@code true}, and  * {@code getExportingBundle()} and {@code getImportingBundles()}  * return {@code null}.  *   * @ThreadSafe  * @noimplement  * @deprecated The PackageAdmin service has been replaced by the  *<code>org.osgi.framework.wiring</code> package.  * @version $Id: c56b99465e3f62a9808297a47de8cb7edb802119 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExportedPackage
block|{
comment|/** 	 * Returns the name of the package associated with this exported package. 	 *  	 * @return The name of this exported package. 	 */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/** 	 * Returns the bundle exporting the package associated with this exported 	 * package. 	 *  	 * @return The exporting bundle, or {@code null} if this 	 *         {@code ExportedPackage} object has become stale. 	 */
specifier|public
name|Bundle
name|getExportingBundle
parameter_list|()
function_decl|;
comment|/** 	 * Returns the resolved bundles that are currently wired to this exported 	 * package. 	 *  	 *<p> 	 * Bundles which require the exporting bundle associated with this exported 	 * package are considered to be wired to this exported package are included 	 * in the returned array. See {@link RequiredBundle#getRequiringBundles()}. 	 *  	 * @return The array of resolved bundles currently wired to this exported 	 *         package, or {@code null} if this 	 *         {@code ExportedPackage} object has become stale. The array 	 *         will be empty if no bundles are wired to this exported package. 	 */
specifier|public
name|Bundle
index|[]
name|getImportingBundles
parameter_list|()
function_decl|;
comment|/** 	 * Returns the version of this exported package. 	 *  	 * @return The version of this exported package, or {@code null} if 	 *         no version information is available. 	 * @deprecated As of 1.2, replaced by {@link #getVersion}. 	 */
specifier|public
name|String
name|getSpecificationVersion
parameter_list|()
function_decl|;
comment|/** 	 * Returns the version of this exported package. 	 *  	 * @return The version of this exported package, or 	 *         {@link Version#emptyVersion} if no version information is 	 *         available. 	 * @since 1.2 	 */
specifier|public
name|Version
name|getVersion
parameter_list|()
function_decl|;
comment|/** 	 * Returns {@code true} if the package associated with this 	 * {@code ExportedPackage} object has been exported by a bundle that 	 * has been updated or uninstalled. 	 *  	 * @return {@code true} if the associated package is being exported 	 *         by a bundle that has been updated or uninstalled, or if this 	 *         {@code ExportedPackage} object has become stale; 	 *         {@code false} otherwise. 	 */
specifier|public
name|boolean
name|isRemovalPending
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

