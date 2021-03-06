begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|DirContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|InitialDirContext
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
name|LoginException
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
name|Hashtable
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
comment|/**  * Karaf JAAS login module which uses a LDAP backend.  */
end_comment

begin_class
specifier|public
class|class
name|LDAPLoginModule
extends|extends
name|AbstractKarafLoginModule
block|{
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LDAPLoginModule
operator|.
name|class
argument_list|)
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
name|LDAPCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|doLogin
argument_list|()
return|;
block|}
finally|finally
block|{
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|doLogin
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
name|ioException
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|ioException
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCallbackException
name|unsupportedCallbackException
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|unsupportedCallbackException
operator|.
name|getMessage
argument_list|()
operator|+
literal|" not available to obtain information from user."
argument_list|)
throw|;
block|}
name|user
operator|=
name|Util
operator|.
name|doRFC2254Encoding
argument_list|(
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
argument_list|)
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
comment|// If either a username or password is specified don't allow authentication = "none".
comment|// This is to prevent someone from logging into Karaf as any user without providing a
comment|// valid password (because if authentication = none, the password could be any
comment|// value - it is ignored).
name|LDAPOptions
name|options
init|=
operator|new
name|LDAPOptions
argument_list|(
name|this
operator|.
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|isUsernameTrim
argument_list|()
condition|)
block|{
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|user
operator|=
name|user
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|authentication
init|=
name|options
operator|.
name|getAuthentication
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"none"
operator|.
name|equals
argument_list|(
name|authentication
argument_list|)
operator|&&
operator|(
name|user
operator|!=
literal|null
operator|||
name|tmpPassword
operator|!=
literal|null
operator|)
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Changing from authentication = none to simple since user or password was specified."
argument_list|)
expr_stmt|;
comment|// default to simple so that the provided user/password will get checked
name|authentication
operator|=
literal|"simple"
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|opts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|this
operator|.
name|options
argument_list|)
decl_stmt|;
name|opts
operator|.
name|put
argument_list|(
name|LDAPOptions
operator|.
name|AUTHENTICATION
argument_list|,
name|authentication
argument_list|)
expr_stmt|;
name|options
operator|=
operator|new
name|LDAPOptions
argument_list|(
name|opts
argument_list|)
expr_stmt|;
block|}
name|boolean
name|allowEmptyPasswords
init|=
name|options
operator|.
name|getAllowEmptyPasswords
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"none"
operator|.
name|equals
argument_list|(
name|authentication
argument_list|)
operator|&&
operator|!
name|allowEmptyPasswords
operator|&&
operator|(
name|tmpPassword
operator|==
literal|null
operator|||
name|tmpPassword
operator|.
name|length
operator|==
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Empty passwords not allowed"
argument_list|)
throw|;
block|}
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
name|LDAPCache
name|cache
init|=
name|LDAPCache
operator|.
name|getCache
argument_list|(
name|options
argument_list|)
decl_stmt|;
comment|// step 1: get the user DN
specifier|final
name|String
index|[]
name|userDnAndNamespace
decl_stmt|;
try|try
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Get the user DN."
argument_list|)
expr_stmt|;
name|userDnAndNamespace
operator|=
name|cache
operator|.
name|getUserDnAndNamespace
argument_list|(
name|user
argument_list|)
expr_stmt|;
if|if
condition|(
name|userDnAndNamespace
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Can't connect to the LDAP server: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Can't connect to the LDAP server: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|// step 2: bind the user using the DN
name|DirContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// switch the credentials to the Karaf login user so that we can verify his password is correct
name|logger
operator|.
name|debug
argument_list|(
literal|"Bind user (authentication)."
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
init|=
name|options
operator|.
name|getEnv
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_AUTHENTICATION
argument_list|,
name|authentication
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Set the security principal for "
operator|+
name|userDnAndNamespace
index|[
literal|0
index|]
operator|+
literal|","
operator|+
name|options
operator|.
name|getUserBaseDn
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_PRINCIPAL
argument_list|,
name|userDnAndNamespace
index|[
literal|0
index|]
operator|+
literal|","
operator|+
name|options
operator|.
name|getUserBaseDn
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_CREDENTIALS
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Binding the user."
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" successfully bound."
argument_list|)
expr_stmt|;
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" authentication failed."
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Authentication failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
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
comment|// step 3: retrieving user roles
try|try
block|{
name|String
index|[]
name|roles
init|=
name|cache
operator|.
name|getUserRoles
argument_list|(
name|user
argument_list|,
name|userDnAndNamespace
index|[
literal|0
index|]
argument_list|,
name|userDnAndNamespace
index|[
literal|1
index|]
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
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Can't get user "
operator|+
name|user
operator|+
literal|" roles: "
operator|+
name|e
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

