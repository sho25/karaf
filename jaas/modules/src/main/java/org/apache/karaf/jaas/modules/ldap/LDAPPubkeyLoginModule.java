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
name|security
operator|.
name|PublicKey
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
name|naming
operator|.
name|NamingException
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
name|publickey
operator|.
name|PublickeyCallback
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
name|publickey
operator|.
name|PublickeyLoginModule
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
name|LDAPPubkeyLoginModule
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
name|LDAPPubkeyLoginModule
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
name|PublicKey
name|remotePubkey
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
name|String
name|userFullDn
init|=
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
decl_stmt|;
comment|// step 2: pubkey authentication
try|try
block|{
name|authenticatePubkey
argument_list|(
name|userFullDn
argument_list|,
name|remotePubkey
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
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
catch|catch
parameter_list|(
name|FailedLoginException
name|e
parameter_list|)
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
literal|"Authentication failed"
argument_list|)
throw|;
block|}
else|else
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Public key authentication failed for user {}: {}"
argument_list|,
name|user
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
literal|"Public key authentication failed for user "
operator|+
name|user
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
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
specifier|private
name|void
name|authenticatePubkey
parameter_list|(
name|String
name|userDn
parameter_list|,
name|PublicKey
name|key
parameter_list|,
name|LDAPCache
name|cache
parameter_list|)
throws|throws
name|FailedLoginException
throws|,
name|NamingException
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"no public key supplied by the client"
argument_list|)
throw|;
name|String
index|[]
name|storedKeys
init|=
name|cache
operator|.
name|getUserPubkeys
argument_list|(
name|userDn
argument_list|)
decl_stmt|;
if|if
condition|(
name|storedKeys
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|storedKey
range|:
name|storedKeys
control|)
block|{
if|if
condition|(
name|PublickeyLoginModule
operator|.
name|equals
argument_list|(
name|key
argument_list|,
name|storedKey
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
block|}
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"no matching public key found"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

