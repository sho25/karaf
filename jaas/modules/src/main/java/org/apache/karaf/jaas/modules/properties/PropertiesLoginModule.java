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
name|PropertiesLoginModule
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
name|PropertiesLoginModule
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
name|sharedState
parameter_list|,
name|Map
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
comment|// user callback get value
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
operator|(
name|String
operator|)
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
comment|// check if the stored password is flagged as encrypted
name|String
name|encryptedPassword
init|=
name|getEncryptedPassword
argument_list|(
name|storedPassword
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|storedPassword
operator|.
name|equals
argument_list|(
name|encryptedPassword
argument_list|)
condition|)
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
literal|"The password isn't flagged as encrypted, encrypt it."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Rebuild the user informations string."
argument_list|)
expr_stmt|;
block|}
name|userInfos
operator|=
name|encryptedPassword
operator|+
literal|","
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
name|i
operator|==
operator|(
name|infos
operator|.
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|userInfos
operator|=
name|userInfos
operator|+
name|infos
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|userInfos
operator|=
name|userInfos
operator|+
name|infos
index|[
name|i
index|]
operator|+
literal|","
expr_stmt|;
block|}
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Push back the user informations in the users properties."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|user
operator|.
name|contains
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
name|users
operator|.
name|remove
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|user
operator|=
name|user
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
block|}
name|users
operator|.
name|put
argument_list|(
name|user
argument_list|,
name|userInfos
argument_list|)
expr_stmt|;
try|try
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
literal|"Store the users properties file."
argument_list|)
expr_stmt|;
block|}
name|users
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Unable to write user properties file {}"
argument_list|,
name|f
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
name|storedPassword
operator|=
name|encryptedPassword
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
argument_list|<
name|Principal
argument_list|>
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
operator|(
name|String
operator|)
name|users
operator|.
name|get
argument_list|(
name|infos
index|[
name|i
index|]
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
return|return
literal|true
return|;
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
name|LOGGER
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
name|LOGGER
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

