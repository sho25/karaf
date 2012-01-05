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
name|permissionadmin
package|;
end_package

begin_comment
comment|/**  * The Permission Admin service allows management agents to manage the  * permissions of bundles. There is at most one Permission Admin service present  * in the OSGi environment.  *<p>  * Access to the Permission Admin service is protected by corresponding  * {@code ServicePermission}. In addition {@code AdminPermission}  * is required to actually set permissions.  *   *<p>  * Bundle permissions are managed using a permission table. A bundle's location  * serves as the key into this permission table. The value of a table entry is  * the set of permissions (of type {@code PermissionInfo}) granted to  * the bundle named by the given location. A bundle may have an entry in the  * permission table prior to being installed in the Framework.  *   *<p>  * The permissions specified in {@code setDefaultPermissions} are used as  * the default permissions which are granted to all bundles that do not have an  * entry in the permission table.  *   *<p>  * Any changes to a bundle's permissions in the permission table will take  * effect no later than when bundle's  * {@code java.security.ProtectionDomain} is next involved in a  * permission check, and will be made persistent.  *   *<p>  * Only permission classes on the system classpath or from an exported package  * are considered during a permission check. Additionally, only permission  * classes that are subclasses of {@code java.security.Permission} and  * define a 2-argument constructor that takes a<i>name</i> string and an  *<i>actions</i> string can be used.  *<p>  * Permissions implicitly granted by the Framework (for example, a bundle's  * permission to access its persistent storage area) cannot be changed, and are  * not reflected in the permissions returned by {@code getPermissions}  * and {@code getDefaultPermissions}.  *   * @ThreadSafe  * @noimplement  * @version $Id: 91132d707097c085fdb3fb7241c9599335427082 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|PermissionAdmin
block|{
comment|/** 	 * Gets the permissions assigned to the bundle with the specified location. 	 *  	 * @param location The location of the bundle whose permissions are to be 	 *        returned. 	 *  	 * @return The permissions assigned to the bundle with the specified 	 *         location, or {@code null} if that bundle has not been 	 *         assigned any permissions. 	 */
name|PermissionInfo
index|[]
name|getPermissions
parameter_list|(
name|String
name|location
parameter_list|)
function_decl|;
comment|/** 	 * Assigns the specified permissions to the bundle with the specified 	 * location. 	 *  	 * @param location The location of the bundle that will be assigned the 	 *        permissions. 	 * @param permissions The permissions to be assigned, or {@code null} 	 *        if the specified location is to be removed from the permission 	 *        table. 	 * @throws SecurityException If the caller does not have 	 *         {@code AllPermission}. 	 */
name|void
name|setPermissions
parameter_list|(
name|String
name|location
parameter_list|,
name|PermissionInfo
index|[]
name|permissions
parameter_list|)
function_decl|;
comment|/** 	 * Returns the bundle locations that have permissions assigned to them, that 	 * is, bundle locations for which an entry exists in the permission table. 	 *  	 * @return The locations of bundles that have been assigned any permissions, 	 *         or {@code null} if the permission table is empty. 	 */
name|String
index|[]
name|getLocations
parameter_list|()
function_decl|;
comment|/** 	 * Gets the default permissions. 	 *  	 *<p> 	 * These are the permissions granted to any bundle that does not have 	 * permissions assigned to its location. 	 *  	 * @return The default permissions, or {@code null} if no default 	 *         permissions are set. 	 */
name|PermissionInfo
index|[]
name|getDefaultPermissions
parameter_list|()
function_decl|;
comment|/** 	 * Sets the default permissions. 	 *  	 *<p> 	 * These are the permissions granted to any bundle that does not have 	 * permissions assigned to its location. 	 *  	 * @param permissions The default permissions, or {@code null} if the 	 *        default permissions are to be removed from the permission table. 	 * @throws SecurityException If the caller does not have 	 *         {@code AllPermission}. 	 */
name|void
name|setDefaultPermissions
parameter_list|(
name|PermissionInfo
index|[]
name|permissions
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

