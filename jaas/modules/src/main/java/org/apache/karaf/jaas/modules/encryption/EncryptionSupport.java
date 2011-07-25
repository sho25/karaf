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
operator|.
name|encryption
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
name|Encryption
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
name|EncryptionService
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_class
specifier|public
class|class
name|EncryptionSupport
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EncryptionSupport
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|Encryption
name|encryption
decl_stmt|;
specifier|private
name|String
name|encryptionPrefix
decl_stmt|;
specifier|private
name|String
name|encryptionSuffix
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
name|boolean
name|debug
decl_stmt|;
specifier|public
name|EncryptionSupport
parameter_list|(
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
name|options
operator|=
name|options
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
specifier|public
name|Encryption
name|getEncryption
parameter_list|()
block|{
if|if
condition|(
name|encryption
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|encOpts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"encryption."
argument_list|)
condition|)
block|{
name|encOpts
operator|.
name|put
argument_list|(
name|key
operator|.
name|substring
argument_list|(
literal|"encryption."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|options
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|encryptionPrefix
operator|=
name|encOpts
operator|.
name|remove
argument_list|(
literal|"prefix"
argument_list|)
expr_stmt|;
name|encryptionSuffix
operator|=
name|encOpts
operator|.
name|remove
argument_list|(
literal|"suffix"
argument_list|)
expr_stmt|;
name|boolean
name|enabled
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|encOpts
operator|.
name|remove
argument_list|(
literal|"enabled"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
if|if
condition|(
name|debug
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Encryption is disabled."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|name
init|=
name|encOpts
operator|.
name|remove
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Encryption is enabled. Using service "
operator|+
name|name
operator|+
literal|" with options "
operator|+
name|encOpts
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Encryption is enabled. Using options "
operator|+
name|encOpts
argument_list|)
expr_stmt|;
block|}
block|}
comment|// lookup the encryption service reference
name|ServiceReference
index|[]
name|encryptionServiceReferences
decl_stmt|;
try|try
block|{
name|encryptionServiceReferences
operator|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|EncryptionService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
literal|"(name="
operator|+
name|name
operator|+
literal|")"
else|:
literal|null
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
operator|==
literal|null
operator|||
name|encryptionServiceReferences
operator|.
name|length
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Encryption service "
operator|+
name|name
operator|+
literal|" not found. Please check that the encryption service is correctly set up."
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No encryption service found. Please install the Karaf encryption feature and check that the encryption algorithm is supported.."
argument_list|)
throw|;
block|}
block|}
name|Arrays
operator|.
name|sort
argument_list|(
name|encryptionServiceReferences
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|encryptionServiceReferences
control|)
block|{
try|try
block|{
name|EncryptionService
name|encryptionService
init|=
operator|(
name|EncryptionService
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionService
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|encryption
operator|=
name|encryptionService
operator|.
name|createEncryption
argument_list|(
name|encOpts
argument_list|)
expr_stmt|;
if|if
condition|(
name|encryption
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// continue
block|}
block|}
if|if
condition|(
name|encryption
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No EncryptionService supporting the required options could be found."
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|encryption
return|;
block|}
specifier|public
name|String
name|getEncryptionSuffix
parameter_list|()
block|{
return|return
name|encryptionSuffix
return|;
block|}
specifier|public
name|void
name|setEncryptionSuffix
parameter_list|(
name|String
name|encryptionSuffix
parameter_list|)
block|{
name|this
operator|.
name|encryptionSuffix
operator|=
name|encryptionSuffix
expr_stmt|;
block|}
specifier|public
name|String
name|getEncryptionPrefix
parameter_list|()
block|{
return|return
name|encryptionPrefix
return|;
block|}
specifier|public
name|void
name|setEncryptionPrefix
parameter_list|(
name|String
name|encryptionPrefix
parameter_list|)
block|{
name|this
operator|.
name|encryptionPrefix
operator|=
name|encryptionPrefix
expr_stmt|;
block|}
block|}
end_class

end_unit

