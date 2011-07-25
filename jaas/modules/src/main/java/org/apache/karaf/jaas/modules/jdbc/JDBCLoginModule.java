begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *   *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|properties
operator|.
name|PropertiesLoginModule
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|XADataSource
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
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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

begin_class
specifier|public
class|class
name|JDBCLoginModule
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
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_QUERY
init|=
literal|"query.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_QUERY
init|=
literal|"query.role"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INSERT_USER_STATEMENT
init|=
literal|"insert.user"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INSERT_ROLE_STATEMENT
init|=
literal|"insert.role"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_ROLE_STATEMENT
init|=
literal|"delete.role"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_ROLES_STATEMENT
init|=
literal|"delete.roles"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_USER_STATEMENT
init|=
literal|"delete.roles"
decl_stmt|;
specifier|private
name|String
name|datasourceURL
decl_stmt|;
specifier|protected
name|String
name|passwordQuery
init|=
literal|"SELECT PASSWORD FROM USERS WHERE USERNAME=?"
decl_stmt|;
specifier|protected
name|String
name|roleQuery
init|=
literal|"SELECT ROLE FROM ROLES WHERE USERNAME=?"
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
name|datasourceURL
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCUtils
operator|.
name|DATASOURCE
argument_list|)
expr_stmt|;
name|passwordQuery
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|PASSWORD_QUERY
argument_list|)
expr_stmt|;
name|roleQuery
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ROLE_QUERY
argument_list|)
expr_stmt|;
if|if
condition|(
name|datasourceURL
operator|==
literal|null
operator|||
name|datasourceURL
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"No datasource was specified "
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|datasourceURL
operator|.
name|startsWith
argument_list|(
name|JDBCUtils
operator|.
name|JNDI
argument_list|)
operator|&&
operator|!
name|datasourceURL
operator|.
name|startsWith
argument_list|(
name|JDBCUtils
operator|.
name|OSGI
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Invalid datasource lookup protocol"
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
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
name|PreparedStatement
name|passwordStatement
init|=
literal|null
decl_stmt|;
name|PreparedStatement
name|roleStatement
init|=
literal|null
decl_stmt|;
name|ResultSet
name|passwordResultSet
init|=
literal|null
decl_stmt|;
name|ResultSet
name|roleResultSet
init|=
literal|null
decl_stmt|;
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
name|password
init|=
operator|new
name|String
argument_list|(
name|tmpPassword
argument_list|)
decl_stmt|;
name|principals
operator|=
operator|new
name|HashSet
argument_list|<
name|Principal
argument_list|>
argument_list|()
expr_stmt|;
try|try
block|{
name|Object
name|credentialsDatasource
init|=
name|JDBCUtils
operator|.
name|createDatasource
argument_list|(
name|bundleContext
argument_list|,
name|datasourceURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|credentialsDatasource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Cannot obtain data source:"
operator|+
name|datasourceURL
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|credentialsDatasource
operator|instanceof
name|DataSource
condition|)
block|{
name|connection
operator|=
operator|(
operator|(
name|DataSource
operator|)
name|credentialsDatasource
operator|)
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|credentialsDatasource
operator|instanceof
name|XADataSource
condition|)
block|{
name|connection
operator|=
operator|(
operator|(
name|XADataSource
operator|)
name|credentialsDatasource
operator|)
operator|.
name|getXAConnection
argument_list|()
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Unknow dataSource type "
operator|+
name|credentialsDatasource
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
comment|//Retrieve user credentials from database.
name|passwordStatement
operator|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|passwordQuery
argument_list|)
expr_stmt|;
name|passwordStatement
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|passwordResultSet
operator|=
name|passwordStatement
operator|.
name|executeQuery
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|passwordResultSet
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|storedPassword
init|=
name|passwordResultSet
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
decl_stmt|;
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
throw|throw
operator|new
name|LoginException
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
block|}
comment|//Retrieve user roles from database
name|roleStatement
operator|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|roleQuery
argument_list|)
expr_stmt|;
name|roleStatement
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|roleResultSet
operator|=
name|roleStatement
operator|.
name|executeQuery
argument_list|()
expr_stmt|;
while|while
condition|(
name|roleResultSet
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|role
init|=
name|roleResultSet
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|principals
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|role
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Error has occured while retrieving credentials from database:"
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|passwordResultSet
operator|!=
literal|null
condition|)
block|{
name|passwordResultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|passwordStatement
operator|!=
literal|null
condition|)
block|{
name|passwordStatement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|roleResultSet
operator|!=
literal|null
condition|)
block|{
name|roleResultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|roleStatement
operator|!=
literal|null
condition|)
block|{
name|roleStatement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Failed to clearly close connection to the database:"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
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

