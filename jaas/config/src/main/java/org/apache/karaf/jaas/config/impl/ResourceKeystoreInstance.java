begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|config
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|UnrecoverableKeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|CertificateException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|HashMap
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManagerFactory
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
name|config
operator|.
name|KeystoreInstance
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
name|config
operator|.
name|KeystoreIsLocked
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

begin_class
specifier|public
class|class
name|ResourceKeystoreInstance
implements|implements
name|KeystoreInstance
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
name|ResourceKeystoreInstance
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JKS
init|=
literal|"JKS"
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|int
name|rank
decl_stmt|;
specifier|private
name|URL
name|path
decl_stmt|;
specifier|private
name|String
name|keystorePassword
decl_stmt|;
specifier|private
name|Map
name|keyPasswords
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|File
name|keystoreFile
decl_stmt|;
comment|// Only valid after startup and if the resource points to a file
specifier|private
name|List
name|trustCerts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|KeyStore
name|keystore
decl_stmt|;
specifier|private
name|long
name|keystoreReadDate
init|=
name|Long
operator|.
name|MIN_VALUE
decl_stmt|;
comment|/**      * @return the keystoreName      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @param keystoreName the keystoreName to set      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|keystoreName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|keystoreName
expr_stmt|;
block|}
comment|/**      * @return the rank      */
specifier|public
name|int
name|getRank
parameter_list|()
block|{
return|return
name|rank
return|;
block|}
comment|/**      * @param rank the rank to set      */
specifier|public
name|void
name|setRank
parameter_list|(
name|int
name|rank
parameter_list|)
block|{
name|this
operator|.
name|rank
operator|=
name|rank
expr_stmt|;
block|}
comment|/**      * @return the keystorePath      */
specifier|public
name|URL
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/**      * @param keystorePath the keystorePath to set.      * @throws IOException in case of failure while setting the path.      */
specifier|public
name|void
name|setPath
parameter_list|(
name|URL
name|keystorePath
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|path
operator|=
name|keystorePath
expr_stmt|;
if|if
condition|(
name|keystorePath
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|keystorePath
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|keystoreFile
operator|=
operator|new
name|File
argument_list|(
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param keystorePassword the keystorePassword to set      */
specifier|public
name|void
name|setKeystorePassword
parameter_list|(
name|String
name|keystorePassword
parameter_list|)
block|{
name|this
operator|.
name|keystorePassword
operator|=
name|keystorePassword
expr_stmt|;
block|}
comment|/**      * @param keyPasswords the keyPasswords to set      */
specifier|public
name|void
name|setKeyPasswords
parameter_list|(
name|String
name|keyPasswords
parameter_list|)
block|{
if|if
condition|(
name|keyPasswords
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|keys
init|=
name|keyPasswords
operator|.
name|split
argument_list|(
literal|"\\]\\!\\["
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|int
name|pos
init|=
name|key
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|keyPasswords
operator|.
name|put
argument_list|(
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
argument_list|,
name|key
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|Certificate
name|getCertificate
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|keystore
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to read certificate from keystore"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getCertificateAlias
parameter_list|(
name|Certificate
name|cert
parameter_list|)
block|{
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|keystore
operator|.
name|getCertificateAlias
argument_list|(
name|cert
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to read retrieve alias for given certificate from keystore"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Certificate
index|[]
name|getCertificateChain
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|keystore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to read certificate chain from keystore"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|KeyManager
index|[]
name|getKeyManager
parameter_list|(
name|String
name|algorithm
parameter_list|,
name|String
name|keyAlias
parameter_list|)
throws|throws
name|KeystoreIsLocked
throws|,
name|NoSuchAlgorithmException
throws|,
name|KeyStoreException
throws|,
name|UnrecoverableKeyException
block|{
if|if
condition|(
name|isKeystoreLocked
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|KeystoreIsLocked
argument_list|(
literal|"Keystore '"
operator|+
name|name
operator|+
literal|"' is locked."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|KeyManagerFactory
name|keyFactory
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|keyFactory
operator|.
name|init
argument_list|(
name|keystore
argument_list|,
operator|(
name|char
index|[]
operator|)
name|keyPasswords
operator|.
name|get
argument_list|(
name|keyAlias
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|keyFactory
operator|.
name|getKeyManagers
argument_list|()
return|;
block|}
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
if|if
condition|(
name|isKeyLocked
argument_list|(
name|alias
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Key
name|key
init|=
name|keystore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
operator|(
name|char
index|[]
operator|)
name|keyPasswords
operator|.
name|get
argument_list|(
name|alias
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|instanceof
name|PrivateKey
condition|)
block|{
return|return
operator|(
name|PrivateKey
operator|)
name|key
return|;
block|}
block|}
catch|catch
parameter_list|(
name|KeyStoreException
decl||
name|NoSuchAlgorithmException
decl||
name|UnrecoverableKeyException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to read private key from keystore"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|TrustManager
index|[]
name|getTrustManager
parameter_list|(
name|String
name|algorithm
parameter_list|)
throws|throws
name|KeyStoreException
throws|,
name|NoSuchAlgorithmException
throws|,
name|KeystoreIsLocked
block|{
if|if
condition|(
name|isKeystoreLocked
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|KeystoreIsLocked
argument_list|(
literal|"Keystore '"
operator|+
name|name
operator|+
literal|"' is locked."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|TrustManagerFactory
name|trustFactory
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|trustFactory
operator|.
name|init
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
return|return
name|trustFactory
operator|.
name|getTrustManagers
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isKeyLocked
parameter_list|(
name|String
name|keyAlias
parameter_list|)
block|{
return|return
name|keyPasswords
operator|.
name|get
argument_list|(
name|keyAlias
argument_list|)
operator|==
literal|null
return|;
block|}
specifier|public
name|boolean
name|isKeystoreLocked
parameter_list|()
block|{
return|return
name|keystorePassword
operator|==
literal|null
return|;
block|}
specifier|public
name|String
index|[]
name|listTrustCertificates
parameter_list|()
block|{
if|if
condition|(
operator|!
name|loadKeystoreData
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|trustCerts
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|trustCerts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|// ==================== Internals =====================
specifier|private
name|boolean
name|loadKeystoreData
parameter_list|()
block|{
comment|// Check to reload the data if needed
if|if
condition|(
name|keystoreFile
operator|!=
literal|null
operator|&&
name|keystoreReadDate
operator|>=
name|keystoreFile
operator|.
name|lastModified
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// If not a file, just not reload the data if it has already been loaded
if|if
condition|(
name|keystoreFile
operator|==
literal|null
operator|&&
name|keystore
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// Check if the file is invalid
if|if
condition|(
name|keystoreFile
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|keystoreFile
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|keystoreFile
operator|.
name|canRead
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid keystore file ("
operator|+
name|path
operator|+
literal|" = "
operator|+
name|keystoreFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
comment|// Load the keystore data
try|try
block|{
name|keystoreReadDate
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|trustCerts
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|keystore
operator|==
literal|null
condition|)
block|{
name|keystore
operator|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|JKS
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|in
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|path
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|keystore
operator|.
name|load
argument_list|(
name|in
argument_list|,
name|keystorePassword
operator|==
literal|null
condition|?
operator|new
name|char
index|[
literal|0
index|]
else|:
name|keystorePassword
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|Enumeration
name|aliases
init|=
name|keystore
operator|.
name|aliases
argument_list|()
decl_stmt|;
while|while
condition|(
name|aliases
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|alias
init|=
operator|(
name|String
operator|)
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|keystore
operator|.
name|isCertificateEntry
argument_list|(
name|alias
argument_list|)
condition|)
block|{
name|trustCerts
operator|.
name|add
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
decl||
name|IOException
decl||
name|NoSuchAlgorithmException
decl||
name|CertificateException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to open keystore with provided password"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

