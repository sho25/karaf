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
name|modules
operator|.
name|JAASUtils
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
name|USER_QUERY
init|=
literal|"query.user"
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
literal|"delete.user"
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
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|JDBCUtils
operator|.
name|DATASOURCE
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
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|PASSWORD_QUERY
argument_list|)
condition|)
block|{
name|passwordQuery
operator|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|PASSWORD_QUERY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|ROLE_QUERY
argument_list|)
condition|)
block|{
name|roleQuery
operator|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ROLE_QUERY
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
argument_list|<>
argument_list|()
expr_stmt|;
try|try
block|{
name|DataSource
name|datasource
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
try|try
init|(
name|Connection
name|connection
init|=
name|datasource
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|passwords
init|=
name|JDBCUtils
operator|.
name|rawSelect
argument_list|(
name|connection
argument_list|,
name|passwordQuery
argument_list|,
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|passwords
operator|.
name|isEmpty
argument_list|()
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
name|LoginException
argument_list|(
literal|"login failed"
argument_list|)
throw|;
block|}
else|else
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
block|}
if|if
condition|(
operator|!
name|checkPassword
argument_list|(
name|password
argument_list|,
name|passwords
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|LoginException
argument_list|(
literal|"login failed"
argument_list|)
throw|;
block|}
else|else
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
if|if
condition|(
name|roleQuery
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|roleQuery
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|JDBCUtils
operator|.
name|rawSelect
argument_list|(
name|connection
argument_list|,
name|roleQuery
argument_list|,
name|user
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|role
range|:
name|roles
control|)
block|{
if|if
condition|(
name|role
operator|.
name|startsWith
argument_list|(
name|BackingEngine
operator|.
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
name|principals
operator|.
name|add
argument_list|(
operator|new
name|GroupPrincipal
argument_list|(
name|role
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
for|for
control|(
name|String
name|r
range|:
name|JDBCUtils
operator|.
name|rawSelect
argument_list|(
name|connection
argument_list|,
name|roleQuery
argument_list|,
name|role
argument_list|)
control|)
block|{
name|principals
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
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
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"No roleQuery specified so no roles have been retrieved for the authenticated user"
argument_list|)
expr_stmt|;
block|}
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
literal|"Error has occurred while retrieving credentials from database:"
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
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

