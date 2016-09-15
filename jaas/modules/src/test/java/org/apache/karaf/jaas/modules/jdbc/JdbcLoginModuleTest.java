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
name|jdbc
package|;
end_package

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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|derby
operator|.
name|jdbc
operator|.
name|EmbeddedDataSource40
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
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|ServiceReference
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
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

begin_class
specifier|public
class|class
name|JdbcLoginModuleTest
block|{
specifier|private
name|EmbeddedDataSource40
name|dataSource
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"derby.stream.error.file"
argument_list|,
literal|"target/derby.log"
argument_list|)
expr_stmt|;
comment|// Create datasource
name|dataSource
operator|=
operator|new
name|EmbeddedDataSource40
argument_list|()
expr_stmt|;
name|dataSource
operator|.
name|setDatabaseName
argument_list|(
literal|"memory:db"
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|setCreateDatabase
argument_list|(
literal|"create"
argument_list|)
expr_stmt|;
comment|// Delete tables
try|try
init|(
name|Connection
name|connection
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|connection
operator|.
name|setAutoCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
try|try
init|(
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"drop table USERS"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
try|try
block|{
try|try
init|(
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"drop table ROLES"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|// Create tables
try|try
init|(
name|Connection
name|connection
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
init|)
block|{
try|try
init|(
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"create table USERS (USERNAME VARCHAR(32) PRIMARY KEY, PASSWORD VARCHAR(32))"
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"create table ROLES (USERNAME VARCHAR(32), ROLE VARCHAR(1024))"
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|// Mocks
name|BundleContext
name|context
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServiceReference
name|reference
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Create options
name|options
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|options
operator|.
name|put
argument_list|(
name|JDBCUtils
operator|.
name|DATASOURCE
argument_list|,
literal|"osgi:"
operator|+
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|options
operator|.
name|put
argument_list|(
name|BundleContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|context
operator|.
name|getServiceReferences
argument_list|(
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ServiceReference
index|[]
block|{
name|reference
block|}
argument_list|)
expr_stmt|;
name|expect
argument_list|(
operator|(
name|DataSource
operator|)
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|context
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoginModule
parameter_list|()
throws|throws
name|Exception
block|{
name|JDBCBackingEngine
name|engine
init|=
operator|new
name|JDBCBackingEngine
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|engine
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addRole
argument_list|(
literal|"abc"
argument_list|,
literal|"role1"
argument_list|)
expr_stmt|;
name|JDBCLoginModule
name|module
init|=
operator|new
name|JDBCLoginModule
argument_list|()
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
name|getCallbackHandler
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
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
name|module
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|(
name|UserPrincipal
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|(
name|UserPrincipal
operator|.
name|class
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|(
name|RolePrincipal
operator|.
name|class
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"role1"
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|(
name|RolePrincipal
operator|.
name|class
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoginModuleWithGroups
parameter_list|()
throws|throws
name|Exception
block|{
name|JDBCBackingEngine
name|engine
init|=
operator|new
name|JDBCBackingEngine
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|engine
operator|.
name|addGroupRole
argument_list|(
literal|"group1"
argument_list|,
literal|"role2"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addRole
argument_list|(
literal|"abc"
argument_list|,
literal|"role1"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroup
argument_list|(
literal|"abc"
argument_list|,
literal|"group1"
argument_list|)
expr_stmt|;
name|JDBCLoginModule
name|module
init|=
operator|new
name|JDBCLoginModule
argument_list|()
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
name|getCallbackHandler
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
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
name|module
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEngine
parameter_list|()
throws|throws
name|Exception
block|{
name|JDBCBackingEngine
name|engine
init|=
operator|new
name|JDBCBackingEngine
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addUser
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addRole
argument_list|(
literal|"abc"
argument_list|,
literal|"role1"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroupRole
argument_list|(
literal|"group1"
argument_list|,
literal|"role2"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroup
argument_list|(
literal|"abc"
argument_list|,
literal|"group1"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteRole
argument_list|(
literal|"abc"
argument_list|,
literal|"role1"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteGroupRole
argument_list|(
literal|"group1"
argument_list|,
literal|"role2"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroupRole
argument_list|(
literal|"group1"
argument_list|,
literal|"role3"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role3"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"role3"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteGroup
argument_list|(
literal|"abc"
argument_list|,
literal|"group1"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteUser
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
literal|"group1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|engine
operator|.
name|listGroups
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
specifier|final
name|String
name|user
parameter_list|,
specifier|final
name|String
name|password
parameter_list|)
block|{
return|return
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
name|user
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
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

