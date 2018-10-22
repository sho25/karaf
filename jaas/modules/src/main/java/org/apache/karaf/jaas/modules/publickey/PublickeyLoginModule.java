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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutputStream
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
name|RSAKey
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
name|RSAPublicKey
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

begin_import
import|import static
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
name|BasicEncryption
operator|.
name|base64Encode
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
name|getString
argument_list|(
name|key
argument_list|)
operator|.
name|equals
argument_list|(
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
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|String
name|getString
parameter_list|(
name|PublicKey
name|key
parameter_list|)
throws|throws
name|FailedLoginException
block|{
try|try
block|{
if|if
condition|(
name|key
operator|instanceof
name|DSAPublicKey
condition|)
block|{
name|DSAPublicKey
name|dsa
init|=
operator|(
name|DSAPublicKey
operator|)
name|key
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DataOutputStream
name|dos
init|=
operator|new
name|DataOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
literal|"ssh-dss"
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
name|dsa
operator|.
name|getParams
argument_list|()
operator|.
name|getP
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
name|dsa
operator|.
name|getParams
argument_list|()
operator|.
name|getQ
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
name|dsa
operator|.
name|getParams
argument_list|()
operator|.
name|getG
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
name|dsa
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|dos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|base64Encode
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
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
name|RSAPublicKey
name|rsa
init|=
operator|(
name|RSAPublicKey
operator|)
name|key
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DataOutputStream
name|dos
init|=
operator|new
name|DataOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
literal|"ssh-rsa"
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
name|rsa
operator|.
name|getPublicExponent
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|dos
argument_list|,
name|rsa
operator|.
name|getModulus
argument_list|()
argument_list|)
expr_stmt|;
name|dos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|base64Encode
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
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
name|void
name|write
parameter_list|(
name|DataOutputStream
name|dos
parameter_list|,
name|BigInteger
name|integer
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
name|integer
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|dos
operator|.
name|writeInt
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|dos
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|write
parameter_list|(
name|DataOutputStream
name|dos
parameter_list|,
name|String
name|str
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
name|str
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|dos
operator|.
name|writeInt
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|dos
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|abort
parameter_list|()
throws|throws
name|LoginException
block|{
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
literal|"abort"
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
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
name|principals
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
literal|"logout"
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

