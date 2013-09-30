begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|GroupPrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|RolePrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|UserPrincipal
import|;
end_import

begin_interface
specifier|public
interface|interface
name|BackingEngine
block|{
comment|/**      * Create a new User.      *      * @param username      * @param password      */
name|void
name|addUser
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
function_decl|;
comment|/**      * Delete User      *      * @param username      */
name|void
name|deleteUser
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
comment|/**      * List Users      */
name|List
argument_list|<
name|UserPrincipal
argument_list|>
name|listUsers
parameter_list|()
function_decl|;
comment|/**      * List groups that an user is in.      *      * @param user      * @return      */
name|List
argument_list|<
name|GroupPrincipal
argument_list|>
name|listGroups
parameter_list|(
name|UserPrincipal
name|user
parameter_list|)
function_decl|;
comment|/**      * Add an user to a group.      *      * @param username      * @param group      */
name|void
name|addGroup
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|group
parameter_list|)
function_decl|;
comment|/**      * Remove an user from a group.      *      * @param username      * @param group      */
name|void
name|deleteGroup
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|group
parameter_list|)
function_decl|;
comment|/**      * List Roles for {@param principal}. This could either be a      * {@link UserPrincipal} or a {@link GroupPrincipal}.      *      * @param principal      * @return      */
name|List
argument_list|<
name|RolePrincipal
argument_list|>
name|listRoles
parameter_list|(
name|Principal
name|principal
parameter_list|)
function_decl|;
comment|/**      * Add a role to the user      *      * @param username      * @param role      */
name|void
name|addRole
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|role
parameter_list|)
function_decl|;
comment|/**      * Remove a role from a user.      *      * @param username      * @param role      */
name|void
name|deleteRole
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|role
parameter_list|)
function_decl|;
comment|/**      * Add a role in a group.      *      * @param group      * @param role      */
name|void
name|addGroupRole
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|role
parameter_list|)
function_decl|;
comment|/**      * Remove a role from a group.      *      * @param group      * @param role      */
name|void
name|deleteGroupRole
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|role
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

