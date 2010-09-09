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
name|jasypt
operator|.
name|util
operator|.
name|password
operator|.
name|ConfigurablePasswordEncryptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jasypt
operator|.
name|util
operator|.
name|password
operator|.
name|PasswordEncryptor
import|;
end_import

begin_comment
comment|/**  *<p>  * Abstract JAAS login module extended by all Karaf Login Modules.  *</p>  *   * @author iocanel, jbonofre  */
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
comment|/**       * Algorithm used to encrypt password such as:      *  MD2      *  MD5      *  SHA-1      *  SHA-256      *  SHA-384      *  SHA-512       */
specifier|protected
name|String
name|encryptionAlgorithm
init|=
literal|"MD5"
decl_stmt|;
comment|/** Jasypt password encryptor */
specifier|private
name|ConfigurablePasswordEncryptor
name|passwordEncryptor
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
comment|/**      *<p>      * Util method to encrypt a password given in plain format.      *</p>      *       * @param plain the plain password.      * @return the encrypted password.      */
specifier|public
name|String
name|encryptPassword
parameter_list|(
name|String
name|plain
parameter_list|)
block|{
name|ConfigurablePasswordEncryptor
name|encryptor
init|=
operator|new
name|ConfigurablePasswordEncryptor
argument_list|()
decl_stmt|;
name|encryptor
operator|.
name|setAlgorithm
argument_list|(
name|encryptionAlgorithm
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**      *<p>      * Check a password in plain with an encrypted password.      *</p>      *       * @param plainPassword a plain password in plain.      * @param encryptedPassword an encrypted password.      * @return true if the plain password match the encrypted one, false else.      */
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|plainPassword
parameter_list|,
name|String
name|encryptedPassword
parameter_list|)
block|{
return|return
name|passwordEncryptor
operator|.
name|checkPassword
argument_list|(
name|plainPassword
argument_list|,
name|encryptedPassword
argument_list|)
return|;
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
name|rolePolicy
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"rolePolicy"
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
literal|"roleDisciriminator"
argument_list|)
expr_stmt|;
name|this
operator|.
name|debug
operator|=
literal|"true"
operator|.
name|equalsIgnoreCase
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
name|encryptionAlgorithm
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"encryptionAlgorithm"
argument_list|)
expr_stmt|;
name|this
operator|.
name|passwordEncryptor
operator|=
operator|new
name|ConfigurablePasswordEncryptor
argument_list|()
expr_stmt|;
name|this
operator|.
name|passwordEncryptor
operator|.
name|setAlgorithm
argument_list|(
name|encryptionAlgorithm
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

