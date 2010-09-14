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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
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
name|ServiceReference
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
comment|/** define the encryption algorithm to use to encrypt password */
specifier|protected
name|String
name|encryption
decl_stmt|;
comment|/** the bundle context is required to use the encryption service */
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|AbstractKarafLoginModule
operator|.
name|class
argument_list|)
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
literal|"roleDiscriminator"
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
name|encryption
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"encryption"
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
block|}
comment|/**      *<p>      * Encrypt password.      *</p>      *       * @param password the password in plain format.      * @return the encrypted password format.      */
specifier|public
name|String
name|encryptPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|encryption
operator|==
literal|null
operator|||
name|this
operator|.
name|encryption
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encryption is disabled."
argument_list|)
expr_stmt|;
block|}
return|return
name|password
return|;
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encryption is enabled and use "
operator|+
name|encryption
operator|+
literal|" encryption algorithm."
argument_list|)
expr_stmt|;
block|}
comment|// lookup the encryption service reference
name|ServiceReference
index|[]
name|encryptionServiceReferences
init|=
operator|new
name|ServiceReference
index|[
literal|0
index|]
decl_stmt|;
try|try
block|{
name|encryptionServiceReferences
operator|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|Encryption
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"(algorithm="
operator|+
name|encryption
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The encryption service filter is not well formed."
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|encryptionServiceReferences
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Encryption service not found for encryption algorithm "
operator|+
name|encryption
operator|+
literal|". Please install the Karaf encryption feature and check that the encryption algorithm is supported.."
argument_list|)
throw|;
block|}
comment|// get the encryption service implementation
name|Encryption
name|encryptionService
init|=
operator|(
name|Encryption
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|encryptionServiceReferences
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Encryption service not found. Please install the Karaf encryption feature."
argument_list|)
throw|;
block|}
comment|// encrypt the password
name|String
name|encryptedPassword
init|=
name|encryptionService
operator|.
name|encryptPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
comment|// release the encryption service reference
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|encryptionServiceReferences
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
name|encryptedPassword
return|;
block|}
comment|/**      *<p>      * Check if the provided password match the reference one.      *</p>      *       * @param input the provided password (plain format).      * @param password the reference one (encrypted format).      * @return true if the passwords match, false else.      */
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|input
parameter_list|,
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|encryption
operator|==
literal|null
operator|||
name|this
operator|.
name|encryption
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encryption is disabled."
argument_list|)
expr_stmt|;
block|}
return|return
name|input
operator|.
name|equals
argument_list|(
name|password
argument_list|)
return|;
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encryption is enabled and use "
operator|+
name|encryption
operator|+
literal|" encryption algorithm."
argument_list|)
expr_stmt|;
block|}
comment|// lookup the encryption service reference
name|ServiceReference
index|[]
name|encryptionServiceReferences
init|=
operator|new
name|ServiceReference
index|[
literal|0
index|]
decl_stmt|;
try|try
block|{
name|encryptionServiceReferences
operator|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|Encryption
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"(algorithm="
operator|+
name|encryption
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The encryption service filter is not well formed."
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|encryptionServiceReferences
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Encryption service not found for encryption algorithm "
operator|+
name|encryption
operator|+
literal|". Please install the Karaf encryption feature and check that the encryption algorithm is supported.."
argument_list|)
throw|;
block|}
comment|// get the encryption service implementation
name|Encryption
name|encryptionService
init|=
operator|(
name|Encryption
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|encryptionServiceReferences
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Encryption service not found. Please install the Karaf encryption feature."
argument_list|)
throw|;
block|}
comment|// check password
name|boolean
name|equals
init|=
name|encryptionService
operator|.
name|checkPassword
argument_list|(
name|input
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|String
name|encryptedPassword
init|=
name|encryptionService
operator|.
name|encryptPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
comment|// release the encryption service reference
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|encryptionServiceReferences
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
name|equals
return|;
block|}
block|}
end_class

end_unit

