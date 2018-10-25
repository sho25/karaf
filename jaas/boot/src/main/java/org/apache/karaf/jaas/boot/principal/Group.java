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
name|boot
operator|.
name|principal
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
name|Enumeration
import|;
end_import

begin_interface
specifier|public
interface|interface
name|Group
extends|extends
name|Principal
block|{
comment|/**      * Adds the specified member to the group.      *      * @param user the principal to add to this group.      *      * @return true if the member was successfully added,      * false if the principal was already a member.      */
name|boolean
name|addMember
parameter_list|(
name|Principal
name|user
parameter_list|)
function_decl|;
comment|/**      * Removes the specified member from the group.      *      * @param user the principal to remove from this group.      *      * @return true if the principal was removed, or      * false if the principal was not a member.      */
name|boolean
name|removeMember
parameter_list|(
name|Principal
name|user
parameter_list|)
function_decl|;
comment|/**      * Returns true if the passed principal is a member of the group.      * This method does a recursive search, so if a principal belongs to a      * group which is a member of this group, true is returned.      *      * @param member the principal whose membership is to be checked.      *      * @return true if the principal is a member of this group,      * false otherwise.      */
name|boolean
name|isMember
parameter_list|(
name|Principal
name|member
parameter_list|)
function_decl|;
comment|/**      * Returns an enumeration of the members in the group.      * The returned objects can be instances of either Principal      * or Group (which is a subclass of Principal).      *      * @return an enumeration of the group members.      */
name|Enumeration
argument_list|<
name|?
extends|extends
name|Principal
argument_list|>
name|members
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

