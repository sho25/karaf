begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|security
operator|.
name|acl
operator|.
name|Group
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_enum
specifier|public
enum|enum
name|RolePolicy
block|{
name|PREFIXED_ROLES
argument_list|(
literal|"prefix"
argument_list|)
block|{
specifier|public
name|void
name|handleRoles
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
parameter_list|,
name|String
name|discriminator
parameter_list|)
block|{
for|for
control|(
name|Principal
name|p
range|:
name|principals
control|)
block|{
if|if
condition|(
name|p
operator|instanceof
name|RolePrincipal
condition|)
block|{
name|RolePrincipal
name|rolePrincipal
init|=
operator|new
name|RolePrincipal
argument_list|(
name|discriminator
operator|+
name|p
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|rolePrincipal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|,
name|GROUP_ROLES
argument_list|(
literal|"group"
argument_list|)
block|{
specifier|public
name|void
name|handleRoles
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
parameter_list|,
name|String
name|discriminator
parameter_list|)
block|{
name|Group
name|group
init|=
operator|new
name|GroupPrincipal
argument_list|(
name|discriminator
argument_list|)
decl_stmt|;
for|for
control|(
name|Principal
name|p
range|:
name|principals
control|)
block|{
if|if
condition|(
name|p
operator|instanceof
name|RolePrincipal
condition|)
block|{
name|group
operator|.
name|addMember
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
block|}
block|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|RolePolicy
argument_list|>
name|policies
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
for|for
control|(
name|RolePolicy
name|s
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|RolePolicy
operator|.
name|class
argument_list|)
control|)
block|{
name|policies
operator|.
name|put
argument_list|(
name|s
operator|.
name|getValue
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|RolePolicy
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
specifier|static
name|RolePolicy
name|getPolicy
parameter_list|(
name|String
name|code
parameter_list|)
block|{
return|return
name|policies
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|void
name|handleRoles
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
parameter_list|,
name|String
name|discriminator
parameter_list|)
function_decl|;
block|}
end_enum

end_unit

