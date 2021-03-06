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
name|HashSet
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

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|login
operator|.
name|LoginException
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
name|spi
operator|.
name|LoginModule
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
name|RolePolicy
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
name|modules
operator|.
name|encryption
operator|.
name|EncryptionSupport
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
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Abstract JAAS login module extended by all Karaf Login Modules.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractKarafLoginModule
implements|implements
name|LoginModule
block|{
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractKarafLoginModule
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Subject
name|subject
decl_stmt|;
specifier|protected
name|String
name|user
decl_stmt|;
specifier|protected
name|CallbackHandler
name|callbackHandler
decl_stmt|;
specifier|protected
name|boolean
name|debug
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
decl_stmt|;
specifier|protected
name|String
name|rolePolicy
decl_stmt|;
specifier|protected
name|String
name|roleDiscriminator
decl_stmt|;
specifier|protected
name|boolean
name|detailedLoginExcepion
decl_stmt|;
comment|/** the authentication status*/
specifier|protected
name|boolean
name|succeeded
init|=
literal|false
decl_stmt|;
specifier|protected
name|boolean
name|commitSucceeded
init|=
literal|false
decl_stmt|;
comment|/**      * the bundle context is required to use the encryption service      */
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|EncryptionSupport
name|encryptionSupport
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|commit
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
operator|!
name|succeeded
operator|||
name|principals
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|clear
argument_list|()
expr_stmt|;
name|succeeded
operator|=
literal|false
expr_stmt|;
return|return
literal|false
return|;
block|}
name|RolePolicy
name|policy
init|=
name|RolePolicy
operator|.
name|getPolicy
argument_list|(
name|rolePolicy
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
name|roleDiscriminator
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|handleRoles
argument_list|(
name|subject
argument_list|,
name|principals
argument_list|,
name|roleDiscriminator
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
name|addAll
argument_list|(
name|principals
argument_list|)
expr_stmt|;
block|}
name|commitSucceeded
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|abort
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"abort"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|succeeded
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|succeeded
operator|&&
name|commitSucceeded
condition|)
block|{
comment|// we succeeded, but another required module failed
name|logout
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// our commit failed
name|clear
argument_list|()
expr_stmt|;
name|succeeded
operator|=
literal|false
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"logout"
argument_list|)
expr_stmt|;
block|}
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|removeAll
argument_list|(
name|principals
argument_list|)
expr_stmt|;
name|clear
argument_list|()
expr_stmt|;
name|succeeded
operator|=
literal|false
expr_stmt|;
name|commitSucceeded
operator|=
literal|false
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|clear
parameter_list|()
block|{
name|user
operator|=
literal|null
expr_stmt|;
name|principals
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|sub
parameter_list|,
name|CallbackHandler
name|handler
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|sub
expr_stmt|;
name|this
operator|.
name|callbackHandler
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
name|this
operator|.
name|rolePolicy
operator|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
literal|"role.policy"
argument_list|)
expr_stmt|;
name|this
operator|.
name|roleDiscriminator
operator|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
literal|"role.discriminator"
argument_list|)
expr_stmt|;
name|this
operator|.
name|debug
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
literal|"debug"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|detailedLoginExcepion
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
literal|"detailed.login.exception"
argument_list|)
argument_list|)
expr_stmt|;
comment|// the bundle context is set in the Config JaasRealm by default
name|this
operator|.
name|bundleContext
operator|=
operator|(
name|BundleContext
operator|)
name|options
operator|.
name|get
argument_list|(
name|BundleContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|encryptionSupport
operator|=
operator|new
name|EncryptionSupport
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|password
parameter_list|,
name|String
name|storedPassword
parameter_list|)
block|{
return|return
name|encryptionSupport
operator|.
name|checkPassword
argument_list|(
name|password
argument_list|,
name|storedPassword
argument_list|)
return|;
block|}
block|}
end_class

end_unit

