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
name|io
operator|.
name|Serializable
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
specifier|public
class|class
name|GroupPrincipal
implements|implements
name|Group
implements|,
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Principal
argument_list|>
name|members
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|GroupPrincipal
parameter_list|(
name|String
name|name
parameter_list|)
block|{
assert|assert
name|name
operator|!=
literal|null
assert|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|GroupPrincipal
name|that
init|=
operator|(
name|GroupPrincipal
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|name
argument_list|,
name|that
operator|.
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"GroupPrincipal["
operator|+
name|name
operator|+
literal|"]"
return|;
block|}
specifier|public
name|boolean
name|addMember
parameter_list|(
name|Principal
name|user
parameter_list|)
block|{
name|members
operator|.
name|put
argument_list|(
name|user
operator|.
name|getName
argument_list|()
argument_list|,
name|user
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|removeMember
parameter_list|(
name|Principal
name|user
parameter_list|)
block|{
name|members
operator|.
name|remove
argument_list|(
name|user
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isMember
parameter_list|(
name|Principal
name|member
parameter_list|)
block|{
return|return
name|members
operator|.
name|containsKey
argument_list|(
name|member
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|?
extends|extends
name|Principal
argument_list|>
name|members
parameter_list|()
block|{
return|return
name|members
operator|.
name|elements
argument_list|()
return|;
block|}
block|}
end_class

end_unit

