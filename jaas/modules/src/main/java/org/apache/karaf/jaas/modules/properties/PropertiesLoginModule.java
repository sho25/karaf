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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
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
name|modules
operator|.
name|UserPrincipal
import|;
end_import

begin_comment
comment|/**  * JAAS Login module for user / password, based on two properties files.  *  */
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
name|String
name|USER_FILE
init|=
literal|"users"
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
name|PropertiesLoginModule
operator|.
name|class
argument_list|)
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
name|Properties
name|users
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|usersFile
argument_list|)
decl_stmt|;
try|try
block|{
name|users
operator|.
name|load
argument_list|(
operator|new
name|java
operator|.
name|io
operator|.
name|FileInputStream
argument_list|(
name|f
argument_list|)
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
name|char
index|[]
name|tmpPassword
init|=
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
decl_stmt|;
if|if
condition|(
name|tmpPassword
operator|==
literal|null
condition|)
block|{
name|tmpPassword
operator|=
operator|new
name|char
index|[
literal|0
index|]
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
operator|new
name|String
argument_list|(
name|tmpPassword
argument_list|)
operator|.
name|equals
argument_list|(
name|infos
index|[
literal|0
index|]
argument_list|)
condition|)
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

