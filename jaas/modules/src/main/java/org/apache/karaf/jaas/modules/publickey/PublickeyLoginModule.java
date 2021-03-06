begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
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
name|publickey
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataInputStream
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
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AlgorithmParameters
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyFactory
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
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|DSAPublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|ECPublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|DSAPublicKeySpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|ECGenParameterSpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|ECParameterSpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|ECPoint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|ECPublicKeySpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidParameterSpecException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|KeySpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|RSAPublicKeySpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Callback
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
name|callback
operator|.
name|NameCallback
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
name|UnsupportedCallbackException
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
name|FailedLoginException
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
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
name|BackingEngine
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
name|AbstractKarafLoginModule
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
name|PublickeyLoginModule
extends|extends
name|AbstractKarafLoginModule
block|{
specifier|private
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PublickeyLoginModule
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USERS_FILE
init|=
literal|"users"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nistSecMap
decl_stmt|;
static|static
block|{
comment|// From RFC-5656
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"nistp256"
argument_list|,
literal|"secp256r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"nistp384"
argument_list|,
literal|"secp384r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"nistp521"
argument_list|,
literal|"secp521r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.1"
argument_list|,
literal|"sect163k1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.2.840.10045.3.1.1"
argument_list|,
literal|"secp192r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.33"
argument_list|,
literal|"secp224r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.26"
argument_list|,
literal|"sect233k1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.27"
argument_list|,
literal|"sect233r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.16"
argument_list|,
literal|"sect283k1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.36"
argument_list|,
literal|"sect409k1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.37"
argument_list|,
literal|"sect409r1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1.3.132.0.38"
argument_list|,
literal|"sect571k1"
argument_list|)
expr_stmt|;
name|nistSecMap
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|usersFile
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sharedState
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
name|super
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|callbackHandler
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|usersFile
operator|=
name|options
operator|.
name|get
argument_list|(
name|USERS_FILE
argument_list|)
operator|+
literal|""
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Initialized debug="
operator|+
name|debug
operator|+
literal|" usersFile="
operator|+
name|usersFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|usersFile
argument_list|)
decl_stmt|;
name|Properties
name|users
decl_stmt|;
try|try
block|{
name|users
operator|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Unable to load user properties file "
operator|+
name|f
argument_list|)
throw|;
block|}
name|Callback
index|[]
name|callbacks
init|=
operator|new
name|Callback
index|[
literal|2
index|]
decl_stmt|;
name|callbacks
index|[
literal|0
index|]
operator|=
operator|new
name|NameCallback
argument_list|(
literal|"Username: "
argument_list|)
expr_stmt|;
name|callbacks
index|[
literal|1
index|]
operator|=
operator|new
name|PublickeyCallback
argument_list|()
expr_stmt|;
try|try
block|{
name|callbackHandler
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCallbackException
name|uce
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|uce
operator|.
name|getMessage
argument_list|()
operator|+
literal|" not available to obtain information from user"
argument_list|)
throw|;
block|}
name|String
name|user
init|=
operator|(
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
literal|0
index|]
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"Unable to retrieve user name"
argument_list|)
throw|;
block|}
name|PublicKey
name|key
init|=
operator|(
operator|(
name|PublickeyCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"Unable to retrieve public key"
argument_list|)
throw|;
block|}
comment|// user infos container read from the users properties file
name|String
name|userInfos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|userInfos
operator|=
name|users
operator|.
name|get
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|//error handled in the next statement
block|}
if|if
condition|(
name|userInfos
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|detailedLoginExcepion
condition|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"login failed"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
block|}
comment|// the password is in the first position
name|String
index|[]
name|infos
init|=
name|userInfos
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|String
name|storedKey
init|=
name|infos
index|[
literal|0
index|]
decl_stmt|;
comment|// check the provided password
if|if
condition|(
operator|!
name|equals
argument_list|(
name|key
argument_list|,
name|storedKey
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|detailedLoginExcepion
condition|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"login failed"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"Public key for "
operator|+
name|user
operator|+
literal|" does not match"
argument_list|)
throw|;
block|}
block|}
name|principals
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|principals
operator|.
name|add
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|infos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|infos
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
operator|.
name|startsWith
argument_list|(
name|BackingEngine
operator|.
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
comment|// it's a group reference
name|principals
operator|.
name|add
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
name|infos
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
operator|.
name|substring
argument_list|(
name|BackingEngine
operator|.
name|GROUP_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|groupInfo
init|=
name|users
operator|.
name|get
argument_list|(
name|infos
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupInfo
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|roles
init|=
name|groupInfo
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|1
init|;
name|j
operator|<
name|roles
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|principals
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|roles
index|[
name|j
index|]
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// it's an user reference
name|principals
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|infos
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|users
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Successfully logged in "
operator|+
name|user
argument_list|)
expr_stmt|;
block|}
name|succeeded
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|boolean
name|equals
parameter_list|(
name|PublicKey
name|key
parameter_list|,
name|String
name|storedKey
parameter_list|)
throws|throws
name|FailedLoginException
block|{
try|try
block|{
name|DataInputStream
name|dis
init|=
operator|new
name|DataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|Base64
operator|.
name|getDecoder
argument_list|()
operator|.
name|decode
argument_list|(
name|storedKey
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|identifier
init|=
name|readString
argument_list|(
name|dis
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|instanceof
name|DSAPublicKey
condition|)
block|{
if|if
condition|(
operator|!
literal|"ssh-dss"
operator|.
name|equals
argument_list|(
name|identifier
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|BigInteger
name|p
init|=
name|readBigInteger
argument_list|(
name|dis
argument_list|)
decl_stmt|;
name|BigInteger
name|q
init|=
name|readBigInteger
argument_list|(
name|dis
argument_list|)
decl_stmt|;
name|BigInteger
name|g
init|=
name|readBigInteger
argument_list|(
name|dis
argument_list|)
decl_stmt|;
name|BigInteger
name|y
init|=
name|readBigInteger
argument_list|(
name|dis
argument_list|)
decl_stmt|;
name|KeyFactory
name|keyFactory
init|=
name|KeyFactory
operator|.
name|getInstance
argument_list|(
literal|"DSA"
argument_list|)
decl_stmt|;
name|KeySpec
name|publicKeySpec
init|=
operator|new
name|DSAPublicKeySpec
argument_list|(
name|y
argument_list|,
name|p
argument_list|,
name|q
argument_list|,
name|g
argument_list|)
decl_stmt|;
name|PublicKey
name|generatedPublicKey
init|=
name|keyFactory
operator|.
name|generatePublic
argument_list|(
name|publicKeySpec
argument_list|)
decl_stmt|;
return|return
name|key
operator|.
name|equals
argument_list|(
name|generatedPublicKey
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|instanceof
name|RSAKey
condition|)
block|{
if|if
condition|(
operator|!
literal|"ssh-rsa"
operator|.
name|equals
argument_list|(
name|identifier
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|BigInteger
name|exponent
init|=
name|readBigInteger
argument_list|(
name|dis
argument_list|)
decl_stmt|;
name|BigInteger
name|modulus
init|=
name|readBigInteger
argument_list|(
name|dis
argument_list|)
decl_stmt|;
name|KeyFactory
name|keyFactory
init|=
name|KeyFactory
operator|.
name|getInstance
argument_list|(
literal|"RSA"
argument_list|)
decl_stmt|;
name|KeySpec
name|publicKeySpec
init|=
operator|new
name|RSAPublicKeySpec
argument_list|(
name|modulus
argument_list|,
name|exponent
argument_list|)
decl_stmt|;
name|PublicKey
name|generatedPublicKey
init|=
name|keyFactory
operator|.
name|generatePublic
argument_list|(
name|publicKeySpec
argument_list|)
decl_stmt|;
return|return
name|key
operator|.
name|equals
argument_list|(
name|generatedPublicKey
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|instanceof
name|ECPublicKey
condition|)
block|{
name|String
name|ecIdentifier
init|=
name|readString
argument_list|(
name|dis
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|identifier
operator|.
name|equals
argument_list|(
literal|"ecdsa-sha2-"
operator|+
name|ecIdentifier
argument_list|)
operator|||
operator|!
name|nistSecMap
operator|.
name|containsKey
argument_list|(
name|ecIdentifier
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Overall size of the x + y coordinates. We only support uncompressed points here, so
comment|// to read x + y we ignore the "04" byte using (size - 1) / 2
name|int
name|size
init|=
name|dis
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
operator|(
name|size
operator|-
literal|1
operator|)
operator|/
literal|2
index|]
decl_stmt|;
name|dis
operator|.
name|skipBytes
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dis
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|BigInteger
name|x
init|=
operator|new
name|BigInteger
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|dis
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|BigInteger
name|y
init|=
operator|new
name|BigInteger
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|KeyFactory
name|keyFactory
init|=
name|KeyFactory
operator|.
name|getInstance
argument_list|(
literal|"EC"
argument_list|)
decl_stmt|;
name|AlgorithmParameters
name|parameters
init|=
name|AlgorithmParameters
operator|.
name|getInstance
argument_list|(
literal|"EC"
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|init
argument_list|(
operator|new
name|ECGenParameterSpec
argument_list|(
name|nistSecMap
operator|.
name|get
argument_list|(
name|ecIdentifier
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ECParameterSpec
name|ecParameters
init|=
name|parameters
operator|.
name|getParameterSpec
argument_list|(
name|ECParameterSpec
operator|.
name|class
argument_list|)
decl_stmt|;
name|ECPoint
name|pubPoint
init|=
operator|new
name|ECPoint
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
name|KeySpec
name|keySpec
init|=
operator|new
name|ECPublicKeySpec
argument_list|(
name|pubPoint
argument_list|,
name|ecParameters
argument_list|)
decl_stmt|;
name|PublicKey
name|generatedPublicKey
init|=
name|keyFactory
operator|.
name|generatePublic
argument_list|(
name|keySpec
argument_list|)
decl_stmt|;
return|return
name|key
operator|.
name|equals
argument_list|(
name|generatedPublicKey
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"Unsupported key type "
operator|+
name|key
operator|.
name|getClass
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|NoSuchAlgorithmException
decl||
name|InvalidKeySpecException
decl||
name|InvalidParameterSpecException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"Unable to check public key"
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|String
name|readString
parameter_list|(
name|DataInputStream
name|dis
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|size
init|=
name|dis
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|dis
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|BigInteger
name|readBigInteger
parameter_list|(
name|DataInputStream
name|dis
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|size
init|=
name|dis
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|dis
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|new
name|BigInteger
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

