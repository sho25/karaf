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
name|config
operator|.
name|KeystoreManager
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
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
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
name|NamingEnumeration
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
name|*
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocketFactory
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
specifier|final
name|String
name|DEFAULT_AUTHENTICATION
init|=
literal|"simple"
decl_stmt|;
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
specifier|final
specifier|static
name|String
name|CONNECTION_URL
init|=
literal|"connection.url"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|CONNECTION_USERNAME
init|=
literal|"connection.username"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|CONNECTION_PASSWORD
init|=
literal|"connection.password"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|USER_BASE_DN
init|=
literal|"user.base.dn"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|USER_FILTER
init|=
literal|"user.filter"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|USER_SEARCH_SUBTREE
init|=
literal|"user.search.subtree"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ROLE_BASE_DN
init|=
literal|"role.base.dn"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ROLE_FILTER
init|=
literal|"role.filter"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ROLE_NAME_ATTRIBUTE
init|=
literal|"role.name.attribute"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ROLE_SEARCH_SUBTREE
init|=
literal|"role.search.subtree"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|AUTHENTICATION
init|=
literal|"authentication"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ALLOW_EMPTY_PASSWORDS
init|=
literal|"allowEmptyPasswords"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|INITIAL_CONTEXT_FACTORY
init|=
literal|"initial.context.factory"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL
init|=
literal|"ssl"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_PROVIDER
init|=
literal|"ssl.provider"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_PROTOCOL
init|=
literal|"ssl.protocol"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_ALGORITHM
init|=
literal|"ssl.algorithm"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_KEYSTORE
init|=
literal|"ssl.keystore"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_KEYALIAS
init|=
literal|"ssl.keyalias"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_TRUSTSTORE
init|=
literal|"ssl.truststore"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|SSL_TIMEOUT
init|=
literal|"ssl.timeout"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|DEFAULT_INITIAL_CONTEXT_FACTORY
init|=
literal|"com.sun.jndi.ldap.LdapCtxFactory"
decl_stmt|;
specifier|private
name|String
name|connectionURL
decl_stmt|;
specifier|private
name|String
name|connectionUsername
decl_stmt|;
specifier|private
name|String
name|connectionPassword
decl_stmt|;
specifier|private
name|String
name|userBaseDN
decl_stmt|;
specifier|private
name|String
name|userFilter
decl_stmt|;
specifier|private
name|boolean
name|userSearchSubtree
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|roleBaseDN
decl_stmt|;
specifier|private
name|String
name|roleFilter
decl_stmt|;
specifier|private
name|String
name|roleNameAttribute
decl_stmt|;
specifier|private
name|boolean
name|roleSearchSubtree
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|authentication
init|=
name|DEFAULT_AUTHENTICATION
decl_stmt|;
specifier|private
name|boolean
name|allowEmptyPasswords
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|initialContextFactory
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|ssl
decl_stmt|;
specifier|private
name|String
name|sslProvider
decl_stmt|;
specifier|private
name|String
name|sslProtocol
decl_stmt|;
specifier|private
name|String
name|sslAlgorithm
decl_stmt|;
specifier|private
name|String
name|sslKeystore
decl_stmt|;
specifier|private
name|String
name|sslKeyAlias
decl_stmt|;
specifier|private
name|String
name|sslTrustStore
decl_stmt|;
specifier|private
name|int
name|sslTimeout
init|=
literal|10
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
name|connectionURL
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CONNECTION_URL
argument_list|)
expr_stmt|;
name|connectionUsername
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CONNECTION_USERNAME
argument_list|)
expr_stmt|;
name|connectionPassword
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|CONNECTION_PASSWORD
argument_list|)
expr_stmt|;
name|userBaseDN
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|USER_BASE_DN
argument_list|)
expr_stmt|;
name|userFilter
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|USER_FILTER
argument_list|)
expr_stmt|;
name|userSearchSubtree
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|USER_SEARCH_SUBTREE
argument_list|)
argument_list|)
expr_stmt|;
name|roleBaseDN
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ROLE_BASE_DN
argument_list|)
expr_stmt|;
name|roleFilter
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ROLE_FILTER
argument_list|)
expr_stmt|;
name|roleNameAttribute
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ROLE_NAME_ATTRIBUTE
argument_list|)
expr_stmt|;
name|roleSearchSubtree
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ROLE_SEARCH_SUBTREE
argument_list|)
argument_list|)
expr_stmt|;
name|initialContextFactory
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|INITIAL_CONTEXT_FACTORY
argument_list|)
expr_stmt|;
if|if
condition|(
name|initialContextFactory
operator|==
literal|null
condition|)
block|{
name|initialContextFactory
operator|=
name|DEFAULT_INITIAL_CONTEXT_FACTORY
expr_stmt|;
block|}
name|authentication
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|AUTHENTICATION
argument_list|)
expr_stmt|;
if|if
condition|(
name|authentication
operator|==
literal|null
condition|)
block|{
name|authentication
operator|=
name|DEFAULT_AUTHENTICATION
expr_stmt|;
block|}
name|allowEmptyPasswords
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ALLOW_EMPTY_PASSWORDS
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|connectionURL
operator|==
literal|null
operator|||
name|connectionURL
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
name|logger
operator|.
name|error
argument_list|(
literal|"No LDAP URL specified."
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|connectionURL
operator|.
name|startsWith
argument_list|(
literal|"ldap:"
argument_list|)
operator|&&
operator|!
name|connectionURL
operator|.
name|startsWith
argument_list|(
literal|"ldaps:"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Invalid LDAP URL: "
operator|+
name|connectionURL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|get
argument_list|(
name|SSL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ssl
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ssl
operator|=
name|connectionURL
operator|.
name|startsWith
argument_list|(
literal|"ldaps:"
argument_list|)
expr_stmt|;
block|}
name|sslProvider
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_PROVIDER
argument_list|)
expr_stmt|;
name|sslProtocol
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_PROTOCOL
argument_list|)
expr_stmt|;
name|sslAlgorithm
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_ALGORITHM
argument_list|)
expr_stmt|;
name|sslKeystore
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_KEYSTORE
argument_list|)
expr_stmt|;
name|sslKeyAlias
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_KEYALIAS
argument_list|)
expr_stmt|;
name|sslTrustStore
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_TRUSTSTORE
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|get
argument_list|(
name|SSL_TIMEOUT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|sslTimeout
operator|=
operator|(
name|Integer
operator|)
name|options
operator|.
name|get
argument_list|(
name|SSL_TIMEOUT
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
comment|// If either a username or password is specified don't allow authentication = "none".
comment|// This is to prevent someone from logging into Karaf as any user without providing a
comment|// valid password (because if authentication = none, the password could be any
comment|// value - it is ignored).
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
block|}
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
argument_list|<
name|Principal
argument_list|>
argument_list|()
expr_stmt|;
comment|// step 1: get the user DN
name|Hashtable
name|env
init|=
operator|new
name|Hashtable
argument_list|()
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Create the LDAP initial context."
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
name|initialContextFactory
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|PROVIDER_URL
argument_list|,
name|connectionURL
argument_list|)
expr_stmt|;
if|if
condition|(
name|connectionUsername
operator|!=
literal|null
operator|&&
name|connectionUsername
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Bound access requested."
argument_list|)
expr_stmt|;
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
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_PRINCIPAL
argument_list|,
name|connectionUsername
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
name|connectionPassword
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ssl
condition|)
block|{
name|setupSsl
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Get the user DN."
argument_list|)
expr_stmt|;
name|String
name|userDN
decl_stmt|;
name|String
name|userDNNamespace
decl_stmt|;
name|DirContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Initialize the JNDI LDAP Dir Context."
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
literal|"Define the subtree scope search control."
argument_list|)
expr_stmt|;
name|SearchControls
name|controls
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
if|if
condition|(
name|userSearchSubtree
condition|)
block|{
name|controls
operator|.
name|setSearchScope
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|controls
operator|.
name|setSearchScope
argument_list|(
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Looking for the user in LDAP with "
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"  base DN: "
operator|+
name|userBaseDN
argument_list|)
expr_stmt|;
name|userFilter
operator|=
name|userFilter
operator|.
name|replaceAll
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"%u"
argument_list|)
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|userFilter
operator|=
name|userFilter
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"  filter: "
operator|+
name|userFilter
argument_list|)
expr_stmt|;
name|NamingEnumeration
name|namingEnumeration
init|=
name|context
operator|.
name|search
argument_list|(
name|userBaseDN
argument_list|,
name|userFilter
argument_list|,
name|controls
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|namingEnumeration
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" not found in LDAP."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Get the user DN."
argument_list|)
expr_stmt|;
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|namingEnumeration
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// We need to do the following because slashes are handled badly. For example, when searching
comment|// for a user with lots of special characters like cn=admin,=+<>#;\
comment|// SearchResult contains 2 different results:
comment|//
comment|// SearchResult.getName = cn=admin\,\=\+\<\>\#\;\\\\
comment|// SearchResult.getNameInNamespace = cn=admin\,\=\+\<\>#\;\\,ou=people,dc=example,dc=com
comment|//
comment|// the second escapes the slashes correctly.
name|userDN
operator|=
name|result
operator|.
name|getNameInNamespace
argument_list|()
operator|.
name|replace
argument_list|(
literal|","
operator|+
name|userBaseDN
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|userDNNamespace
operator|=
operator|(
name|String
operator|)
name|result
operator|.
name|getNameInNamespace
argument_list|()
expr_stmt|;
name|namingEnumeration
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
comment|// step 2: bind the user using the DN
name|context
operator|=
literal|null
expr_stmt|;
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
name|userDN
operator|+
literal|","
operator|+
name|userBaseDN
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
name|userDN
operator|+
literal|","
operator|+
name|userBaseDN
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
return|return
literal|false
return|;
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
name|context
operator|=
literal|null
expr_stmt|;
try|try
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Get user roles."
argument_list|)
expr_stmt|;
comment|// switch back to the connection credentials for the role search like we did for the user search in step 1
if|if
condition|(
name|connectionUsername
operator|!=
literal|null
operator|&&
name|connectionUsername
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
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
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_PRINCIPAL
argument_list|,
name|connectionUsername
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
name|connectionPassword
argument_list|)
expr_stmt|;
block|}
name|context
operator|=
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|SearchControls
name|controls
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
if|if
condition|(
name|roleSearchSubtree
condition|)
block|{
name|controls
operator|.
name|setSearchScope
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|controls
operator|.
name|setSearchScope
argument_list|(
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|roleNameAttribute
operator|!=
literal|null
condition|)
block|{
name|controls
operator|.
name|setReturningAttributes
argument_list|(
operator|new
name|String
index|[]
block|{
name|roleNameAttribute
block|}
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Looking for the user roles in LDAP with "
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"  base DN: "
operator|+
name|roleBaseDN
argument_list|)
expr_stmt|;
name|roleFilter
operator|=
name|roleFilter
operator|.
name|replaceAll
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"%u"
argument_list|)
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|roleFilter
operator|=
name|roleFilter
operator|.
name|replaceAll
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"%dn"
argument_list|)
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|userDN
argument_list|)
argument_list|)
expr_stmt|;
name|roleFilter
operator|=
name|roleFilter
operator|.
name|replaceAll
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"%fqdn"
argument_list|)
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|userDN
operator|+
literal|","
operator|+
name|userBaseDN
argument_list|)
argument_list|)
expr_stmt|;
name|roleFilter
operator|=
name|roleFilter
operator|.
name|replaceAll
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
literal|"%nsdn"
argument_list|)
argument_list|,
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|userDNNamespace
argument_list|)
argument_list|)
expr_stmt|;
name|roleFilter
operator|=
name|roleFilter
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"  filter: "
operator|+
name|roleFilter
argument_list|)
expr_stmt|;
name|NamingEnumeration
name|namingEnumeration
init|=
name|context
operator|.
name|search
argument_list|(
name|roleBaseDN
argument_list|,
name|roleFilter
argument_list|,
name|controls
argument_list|)
decl_stmt|;
while|while
condition|(
name|namingEnumeration
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|namingEnumeration
operator|.
name|next
argument_list|()
decl_stmt|;
name|Attributes
name|attributes
init|=
name|result
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Attribute
name|roles
init|=
name|attributes
operator|.
name|get
argument_list|(
name|roleNameAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|roles
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|role
init|=
operator|(
name|String
operator|)
name|roles
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|role
operator|!=
literal|null
condition|)
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
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|setupSsl
parameter_list|(
name|Hashtable
name|env
parameter_list|)
throws|throws
name|LoginException
block|{
name|ServiceReference
name|ref
init|=
literal|null
decl_stmt|;
try|try
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Setting up SSL"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_PROTOCOL
argument_list|,
literal|"ssl"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.ldap.factory.socket"
argument_list|,
name|ManagedSSLSocketFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|KeystoreManager
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|KeystoreManager
name|manager
init|=
operator|(
name|KeystoreManager
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|SSLSocketFactory
name|factory
init|=
name|manager
operator|.
name|createSSLFactory
argument_list|(
name|sslProvider
argument_list|,
name|sslProtocol
argument_list|,
name|sslAlgorithm
argument_list|,
name|sslKeystore
argument_list|,
name|sslKeyAlias
argument_list|,
name|sslTrustStore
argument_list|,
name|sslTimeout
argument_list|)
decl_stmt|;
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|ManagedSSLSocketFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
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
literal|"Unable to setup SSL support for LDAP: "
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
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
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
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
specifier|abstract
class|class
name|ManagedSSLSocketFactory
extends|extends
name|SSLSocketFactory
block|{
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|SSLSocketFactory
argument_list|>
name|factories
init|=
operator|new
name|ThreadLocal
argument_list|<
name|SSLSocketFactory
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|void
name|setSocketFactory
parameter_list|(
name|SSLSocketFactory
name|factory
parameter_list|)
block|{
name|factories
operator|.
name|set
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|SSLSocketFactory
name|getDefault
parameter_list|()
block|{
name|SSLSocketFactory
name|factory
init|=
name|factories
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No SSLSocketFactory parameters have been set!"
argument_list|)
throw|;
block|}
return|return
name|factory
return|;
block|}
block|}
block|}
end_class

end_unit

