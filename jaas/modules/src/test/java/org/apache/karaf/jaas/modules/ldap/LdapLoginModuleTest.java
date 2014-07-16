begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|ldap
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|AbstractLdapTestUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|FrameworkRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateLdapServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateTransport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|ApplyLdifFiles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreateDS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreatePartition
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|LoginException
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
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|FrameworkRunner
operator|.
name|class
argument_list|)
annotation|@
name|CreateLdapServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"LDAP"
argument_list|,
name|port
operator|=
literal|9999
argument_list|)
block|}
argument_list|)
annotation|@
name|CreateDS
argument_list|(
name|name
operator|=
literal|"LdapLoginModuleTest-class"
argument_list|,
name|partitions
operator|=
block|{
annotation|@
name|CreatePartition
argument_list|(
name|name
operator|=
literal|"example"
argument_list|,
name|suffix
operator|=
literal|"dc=example,dc=com"
argument_list|)
block|}
argument_list|)
annotation|@
name|ApplyLdifFiles
argument_list|(
literal|"org/apache/karaf/jaas/modules/ldap/example.com.ldif"
argument_list|)
specifier|public
class|class
name|LdapLoginModuleTest
extends|extends
name|AbstractLdapTestUnit
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAdminLogin
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|LDAPLoginModule
name|module
init|=
operator|new
name|LDAPLoginModule
argument_list|()
decl_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
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
literal|"admin"
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
literal|"admin123"
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
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|commit
argument_list|()
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"admin"
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
name|assertEquals
argument_list|(
literal|"admin"
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
name|assertTrue
argument_list|(
name|foundUser
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|foundRole
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|logout
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|protected
name|Properties
name|ldapLoginModuleOptions
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|Properties
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/karaf/jaas/modules/ldap/ldap.properties"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonAdminLogin
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|LDAPLoginModule
name|module
init|=
operator|new
name|LDAPLoginModule
argument_list|()
decl_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
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
literal|"cheese"
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
literal|"foodie"
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
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|commit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
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
name|assertEquals
argument_list|(
literal|"cheese"
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
name|assertEquals
argument_list|(
literal|"admin"
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
name|assertTrue
argument_list|(
name|foundUser
argument_list|)
expr_stmt|;
comment|// cheese is not an admin so no roles should be returned
name|assertFalse
argument_list|(
name|foundRole
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|logout
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testBadPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|LDAPLoginModule
name|module
init|=
operator|new
name|LDAPLoginModule
argument_list|()
decl_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
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
literal|"admin"
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
literal|"blahblah"
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
name|assertFalse
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUserNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|LDAPLoginModule
name|module
init|=
operator|new
name|LDAPLoginModule
argument_list|()
decl_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
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
literal|"imnothere"
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
literal|"admin123"
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
name|assertFalse
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|LDAPLoginModule
name|module
init|=
operator|new
name|LDAPLoginModule
argument_list|()
decl_stmt|;
name|CallbackHandler
name|cb
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
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
literal|"imnothere"
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
literal|""
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
try|try
block|{
name|module
operator|.
name|login
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Empty passwords not allowed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

