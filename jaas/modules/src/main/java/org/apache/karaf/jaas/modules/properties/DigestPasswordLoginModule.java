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
name|modules
operator|.
name|properties
package|;
end_package

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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|MessageDigest
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
name|PasswordCallback
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
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
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

begin_comment
comment|/**  * JAAS Login module for user / password, based on two properties files.  */
end_comment

begin_class
specifier|public
class|class
name|DigestPasswordLoginModule
extends|extends
name|AbstractKarafLoginModule
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
name|DigestPasswordLoginModule
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|USER_FILE
init|=
literal|"users"
decl_stmt|;
specifier|private
name|MessageDigest
name|digest
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
name|sub
argument_list|,
name|handler
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|usersFile
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|USER_FILE
argument_list|)
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Initialized debug={} usersFile={}"
argument_list|,
name|debug
argument_list|,
name|usersFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|doPasswordDigest
parameter_list|(
name|String
name|nonce
parameter_list|,
name|String
name|created
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|String
name|passwdDigest
init|=
literal|null
decl_stmt|;
try|try
block|{
name|passwdDigest
operator|=
name|doPasswordDigest
argument_list|(
name|nonce
argument_list|,
name|created
argument_list|,
name|password
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|passwdDigest
return|;
block|}
specifier|public
name|String
name|doPasswordDigest
parameter_list|(
name|String
name|nonce
parameter_list|,
name|String
name|created
parameter_list|,
name|byte
index|[]
name|password
parameter_list|)
block|{
name|String
name|passwdDigest
init|=
literal|null
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|b1
init|=
name|nonce
operator|!=
literal|null
condition|?
operator|new
name|Base64
argument_list|()
operator|.
name|decode
argument_list|(
name|nonce
argument_list|)
else|:
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
name|byte
index|[]
name|b2
init|=
name|created
operator|!=
literal|null
condition|?
name|created
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
else|:
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
name|byte
index|[]
name|b3
init|=
name|password
decl_stmt|;
name|byte
index|[]
name|b4
init|=
operator|new
name|byte
index|[
name|b1
operator|.
name|length
operator|+
name|b2
operator|.
name|length
operator|+
name|b3
operator|.
name|length
index|]
decl_stmt|;
name|int
name|offset
init|=
literal|0
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|b1
argument_list|,
literal|0
argument_list|,
name|b4
argument_list|,
name|offset
argument_list|,
name|b1
operator|.
name|length
argument_list|)
expr_stmt|;
name|offset
operator|+=
name|b1
operator|.
name|length
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|b2
argument_list|,
literal|0
argument_list|,
name|b4
argument_list|,
name|offset
argument_list|,
name|b2
operator|.
name|length
argument_list|)
expr_stmt|;
name|offset
operator|+=
name|b2
operator|.
name|length
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|b3
argument_list|,
literal|0
argument_list|,
name|b4
argument_list|,
name|offset
argument_list|,
name|b3
operator|.
name|length
argument_list|)
expr_stmt|;
name|byte
index|[]
name|digestBytes
init|=
name|generateDigest
argument_list|(
name|b4
argument_list|)
decl_stmt|;
name|passwdDigest
operator|=
operator|new
name|String
argument_list|(
name|Base64
operator|.
name|encodeBase64
argument_list|(
name|digestBytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|passwdDigest
return|;
block|}
comment|/**      * Generate a (SHA1) digest of the input bytes. The MessageDigest instance that backs this      * method is cached for efficiency.        * @param inputBytes the bytes to digest      * @return the digest of the input bytes      */
specifier|public
specifier|synchronized
name|byte
index|[]
name|generateDigest
parameter_list|(
name|byte
index|[]
name|inputBytes
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|digest
operator|==
literal|null
condition|)
block|{
name|digest
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-1"
argument_list|)
expr_stmt|;
block|}
return|return
name|digest
operator|.
name|digest
argument_list|(
name|inputBytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error in generating digest"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|usersFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"The property users may not be null"
argument_list|)
throw|;
block|}
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|usersFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Users file not found at "
operator|+
name|f
argument_list|)
throw|;
block|}
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
name|PasswordCallback
argument_list|(
literal|"Password: "
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|callbackHandler
operator|!=
literal|null
condition|)
block|{
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
block|}
comment|// user callback get value
if|if
condition|(
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
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Username can not be null"
argument_list|)
throw|;
block|}
name|user
operator|=
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
expr_stmt|;
if|if
condition|(
name|user
operator|.
name|startsWith
argument_list|(
name|PropertiesBackingEngine
operator|.
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
comment|// you can't log in under a group name
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"login failed"
argument_list|)
throw|;
block|}
comment|// password callback get value
if|if
condition|(
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Password can not be null"
argument_list|)
throw|;
block|}
name|String
name|password
init|=
operator|new
name|String
argument_list|(
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
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
name|storedPassword
init|=
name|infos
index|[
literal|0
index|]
decl_stmt|;
name|CallbackHandler
name|myCallbackHandler
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Field
name|field
init|=
name|callbackHandler
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"ch"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|myCallbackHandler
operator|=
operator|(
name|CallbackHandler
operator|)
name|field
operator|.
name|get
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Unable to load underlying callback handler"
argument_list|)
throw|;
block|}
if|if
condition|(
name|myCallbackHandler
operator|instanceof
name|NameDigestPasswordCallbackHandler
condition|)
block|{
name|NameDigestPasswordCallbackHandler
name|digestCallbackHandler
init|=
operator|(
name|NameDigestPasswordCallbackHandler
operator|)
name|myCallbackHandler
decl_stmt|;
name|storedPassword
operator|=
name|doPasswordDigest
argument_list|(
name|digestCallbackHandler
operator|.
name|getNonce
argument_list|()
argument_list|,
name|digestCallbackHandler
operator|.
name|getCreatedTime
argument_list|()
argument_list|,
name|storedPassword
argument_list|)
expr_stmt|;
block|}
comment|// check the provided password
if|if
condition|(
operator|!
name|checkPassword
argument_list|(
name|password
argument_list|,
name|storedPassword
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
literal|"Password for "
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
name|PropertiesBackingEngine
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
name|PropertiesBackingEngine
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Successfully logged in {}"
argument_list|,
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
block|}
end_class

end_unit

