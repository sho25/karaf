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
name|modules
package|;
end_package

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
specifier|protected
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
init|=
operator|new
name|HashSet
argument_list|<
name|Principal
argument_list|>
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
comment|/**      * the bundle context is required to use the encryption service      */
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|EncryptionSupport
name|encryptionSupport
decl_stmt|;
specifier|public
name|boolean
name|commit
parameter_list|()
throws|throws
name|LoginException
block|{
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
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"role.policy"
argument_list|)
expr_stmt|;
name|this
operator|.
name|roleDiscriminator
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
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
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
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
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
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
name|String
name|getEncryptedPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|Encryption
name|encryption
init|=
name|encryptionSupport
operator|.
name|getEncryption
argument_list|()
decl_stmt|;
name|String
name|encryptionPrefix
init|=
name|encryptionSupport
operator|.
name|getEncryptionPrefix
argument_list|()
decl_stmt|;
name|String
name|encryptionSuffix
init|=
name|encryptionSupport
operator|.
name|getEncryptionSuffix
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryption
operator|==
literal|null
condition|)
block|{
return|return
name|password
return|;
block|}
else|else
block|{
name|boolean
name|prefix
init|=
name|encryptionPrefix
operator|==
literal|null
operator|||
name|password
operator|.
name|startsWith
argument_list|(
name|encryptionPrefix
argument_list|)
decl_stmt|;
name|boolean
name|suffix
init|=
name|encryptionSuffix
operator|==
literal|null
operator|||
name|password
operator|.
name|endsWith
argument_list|(
name|encryptionSuffix
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|&&
name|suffix
condition|)
block|{
return|return
name|password
return|;
block|}
else|else
block|{
name|String
name|p
init|=
name|encryption
operator|.
name|encryptPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionPrefix
operator|!=
literal|null
condition|)
block|{
name|p
operator|=
name|encryptionPrefix
operator|+
name|p
expr_stmt|;
block|}
if|if
condition|(
name|encryptionSuffix
operator|!=
literal|null
condition|)
block|{
name|p
operator|=
name|p
operator|+
name|encryptionSuffix
expr_stmt|;
block|}
return|return
name|p
return|;
block|}
block|}
block|}
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|plain
parameter_list|,
name|String
name|encrypted
parameter_list|)
block|{
name|Encryption
name|encryption
init|=
name|encryptionSupport
operator|.
name|getEncryption
argument_list|()
decl_stmt|;
name|String
name|encryptionPrefix
init|=
name|encryptionSupport
operator|.
name|getEncryptionPrefix
argument_list|()
decl_stmt|;
name|String
name|encryptionSuffix
init|=
name|encryptionSupport
operator|.
name|getEncryptionSuffix
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryption
operator|==
literal|null
condition|)
block|{
return|return
name|plain
operator|.
name|equals
argument_list|(
name|encrypted
argument_list|)
return|;
block|}
else|else
block|{
name|boolean
name|prefix
init|=
name|encryptionPrefix
operator|==
literal|null
operator|||
name|encrypted
operator|.
name|startsWith
argument_list|(
name|encryptionPrefix
argument_list|)
decl_stmt|;
name|boolean
name|suffix
init|=
name|encryptionSuffix
operator|==
literal|null
operator|||
name|encrypted
operator|.
name|endsWith
argument_list|(
name|encryptionSuffix
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|&&
name|suffix
condition|)
block|{
name|encrypted
operator|=
name|encrypted
operator|.
name|substring
argument_list|(
name|encryptionPrefix
operator|!=
literal|null
condition|?
name|encryptionPrefix
operator|.
name|length
argument_list|()
else|:
literal|0
argument_list|,
name|encrypted
operator|.
name|length
argument_list|()
operator|-
operator|(
name|encryptionSuffix
operator|!=
literal|null
condition|?
name|encryptionSuffix
operator|.
name|length
argument_list|()
else|:
literal|0
operator|)
argument_list|)
expr_stmt|;
return|return
name|encryption
operator|.
name|checkPassword
argument_list|(
name|plain
argument_list|,
name|encrypted
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|plain
operator|.
name|equals
argument_list|(
name|encrypted
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

