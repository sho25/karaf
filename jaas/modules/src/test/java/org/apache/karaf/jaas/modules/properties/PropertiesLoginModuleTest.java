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
name|*
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|PropertiesLoginModuleTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBasicLogin
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|PropertiesBackingEngine
name|pbe
init|=
operator|new
name|PropertiesBackingEngine
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addRole
argument_list|(
literal|"abc"
argument_list|,
literal|"myrole"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"pqr"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|cb
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|cb
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|cb
operator|)
operator|.
name|setName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cb
operator|instanceof
name|PasswordCallback
condition|)
block|{
operator|(
operator|(
name|PasswordCallback
operator|)
name|cb
operator|)
operator|.
name|setPassword
argument_list|(
literal|"xyz"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|cb
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|module
operator|.
name|commit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|foundUser
init|=
literal|false
decl_stmt|;
name|boolean
name|foundRole
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Principal
name|pr
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|pr
operator|instanceof
name|UserPrincipal
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundUser
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pr
operator|instanceof
name|RolePrincipal
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"myrole"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundRole
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
name|foundUser
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|foundRole
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|module
operator|.
name|logout
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Principals should be gone as the user has logged out"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Could not delete temporary file: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoginIncorrectPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|PropertiesBackingEngine
name|pbe
init|=
operator|new
name|PropertiesBackingEngine
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"pqr"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|cb
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|cb
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|cb
operator|)
operator|.
name|setName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cb
operator|instanceof
name|PasswordCallback
condition|)
block|{
operator|(
operator|(
name|PasswordCallback
operator|)
name|cb
operator|)
operator|.
name|setPassword
argument_list|(
literal|"abc"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
operator|new
name|Subject
argument_list|()
argument_list|,
name|cb
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
try|try
block|{
name|module
operator|.
name|login
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"The login should have failed as the passwords didn't match"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FailedLoginException
name|fle
parameter_list|)
block|{
comment|// good
block|}
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Could not delete temporary file: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoginWithGroups
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|PropertiesBackingEngine
name|pbe
init|=
operator|new
name|PropertiesBackingEngine
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addRole
argument_list|(
literal|"abc"
argument_list|,
literal|"myrole"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"pqr"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addGroup
argument_list|(
literal|"pqr"
argument_list|,
literal|"group1"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addGroupRole
argument_list|(
literal|"group1"
argument_list|,
literal|"r1"
argument_list|)
expr_stmt|;
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|cb
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|cb
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|cb
operator|)
operator|.
name|setName
argument_list|(
literal|"pqr"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cb
operator|instanceof
name|PasswordCallback
condition|)
block|{
operator|(
operator|(
name|PasswordCallback
operator|)
name|cb
operator|)
operator|.
name|setPassword
argument_list|(
literal|"abc"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|cb
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|module
operator|.
name|commit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|foundUser
init|=
literal|false
decl_stmt|;
name|boolean
name|foundRole
init|=
literal|false
decl_stmt|;
name|boolean
name|foundGroup
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Principal
name|pr
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|pr
operator|instanceof
name|UserPrincipal
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"pqr"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundUser
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pr
operator|instanceof
name|GroupPrincipal
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"group1"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundGroup
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pr
operator|instanceof
name|RolePrincipal
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"r1"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundRole
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
name|foundUser
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|foundGroup
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|foundRole
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Could not delete temporary file: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// This is a fairly important test that ensures that you cannot log in under the name of a
comment|// group directly.
annotation|@
name|Test
specifier|public
name|void
name|testCannotLoginAsGroupDirectly
parameter_list|()
throws|throws
name|Exception
block|{
name|testCannotLoginAsGroupDirectly
argument_list|(
literal|"group1"
argument_list|)
expr_stmt|;
name|testCannotLoginAsGroupDirectly
argument_list|(
literal|"_g_:group1"
argument_list|)
expr_stmt|;
name|testCannotLoginAsGroupDirectly
argument_list|(
name|PropertiesBackingEngine
operator|.
name|GROUP_PREFIX
operator|+
literal|"group1"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testCannotLoginAsGroupDirectly
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|IOException
throws|,
name|LoginException
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|PropertiesBackingEngine
name|pbe
init|=
operator|new
name|PropertiesBackingEngine
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addRole
argument_list|(
literal|"abc"
argument_list|,
literal|"myrole"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addUser
argument_list|(
literal|"pqr"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addGroup
argument_list|(
literal|"pqr"
argument_list|,
literal|"group1"
argument_list|)
expr_stmt|;
name|pbe
operator|.
name|addGroupRole
argument_list|(
literal|"group1"
argument_list|,
literal|"r1"
argument_list|)
expr_stmt|;
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|cb
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|cb
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|cb
operator|)
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cb
operator|instanceof
name|PasswordCallback
condition|)
block|{
operator|(
operator|(
name|PasswordCallback
operator|)
name|cb
operator|)
operator|.
name|setPassword
argument_list|(
literal|"group"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
operator|new
name|Subject
argument_list|()
argument_list|,
name|cb
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
try|try
block|{
name|module
operator|.
name|login
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"The login should have failed as you cannot log in under a group name directly"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FailedLoginException
name|fle
parameter_list|)
block|{
comment|// good
block|}
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Could not delete temporary file: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullUsersFile
parameter_list|()
block|{
try|try
block|{
name|testWithUsersFile
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"LoginException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"The property users may not be null"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonExistantPropertiesFile
parameter_list|()
throws|throws
name|LoginException
throws|,
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
try|try
block|{
name|testWithUsersFile
argument_list|(
name|File
operator|.
name|separator
operator|+
literal|"test"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"users.properties"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Users file not found at "
operator|+
name|File
operator|.
name|separator
operator|+
literal|"test"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"users.properties"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|CallbackHandler
name|handler
init|=
operator|new
name|NullHandler
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|getTestUsersFile
argument_list|()
argument_list|)
expr_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|handler
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
try|try
block|{
name|module
operator|.
name|login
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"LoginException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Password can not be null"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|testWithUsersFile
parameter_list|(
name|String
name|usersFilePath
parameter_list|)
throws|throws
name|LoginException
block|{
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|sub
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|CallbackHandler
name|handler
init|=
operator|new
name|NamePasswordHandler
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|usersFilePath
argument_list|)
expr_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|sub
argument_list|,
name|handler
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|module
operator|.
name|login
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullCallbackHandler
parameter_list|()
block|{
name|PropertiesLoginModule
name|module
init|=
operator|new
name|PropertiesLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
name|PropertiesLoginModule
operator|.
name|USER_FILE
argument_list|,
name|getTestUsersFile
argument_list|()
argument_list|)
expr_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
try|try
block|{
name|module
operator|.
name|login
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"LoginException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Username can not be null"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getTestUsersFile
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/karaf/jaas/modules/properties/test.properties"
argument_list|)
operator|.
name|getFile
argument_list|()
return|;
block|}
block|}
end_class

end_unit

