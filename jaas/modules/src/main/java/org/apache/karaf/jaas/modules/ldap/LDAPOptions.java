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
name|NamingException
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|JAASUtils
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
name|FrameworkUtil
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

begin_class
specifier|public
class|class
name|LDAPOptions
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_URL
init|=
literal|"connection.url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_USERNAME
init|=
literal|"connection.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_PASSWORD
init|=
literal|"connection.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_BASE_DN
init|=
literal|"user.base.dn"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_FILTER
init|=
literal|"user.filter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_SEARCH_SUBTREE
init|=
literal|"user.search.subtree"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_PUBKEY_ATTRIBUTE
init|=
literal|"user.pubkey.attribute"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_BASE_DN
init|=
literal|"role.base.dn"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_FILTER
init|=
literal|"role.filter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_NAME_ATTRIBUTE
init|=
literal|"role.name.attribute"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_SEARCH_SUBTREE
init|=
literal|"role.search.subtree"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROLE_MAPPING
init|=
literal|"role.mapping"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTHENTICATION
init|=
literal|"authentication"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALLOW_EMPTY_PASSWORDS
init|=
literal|"allowEmptyPasswords"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DISABLE_CACHE
init|=
literal|"disableCache"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INITIAL_CONTEXT_FACTORY
init|=
literal|"initial.context.factory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_PREFIX
init|=
literal|"context."
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL
init|=
literal|"ssl"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_PROVIDER
init|=
literal|"ssl.provider"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_PROTOCOL
init|=
literal|"ssl.protocol"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_ALGORITHM
init|=
literal|"ssl.algorithm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_KEYSTORE
init|=
literal|"ssl.keystore"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_KEYALIAS
init|=
literal|"ssl.keyalias"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_TRUSTSTORE
init|=
literal|"ssl.truststore"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SSL_TIMEOUT
init|=
literal|"ssl.timeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USERNAMES_TRIM
init|=
literal|"usernames.trim"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_INITIAL_CONTEXT_FACTORY
init|=
literal|"com.sun.jndi.ldap.LdapCtxFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_AUTHENTICATION
init|=
literal|"simple"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IGNORE_PARTIAL_RESULT_EXCEPTION
init|=
literal|"ignorePartialResultException"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SSL_TIMEOUT
init|=
literal|10
decl_stmt|;
specifier|private
specifier|static
name|Logger
name|LOGGER
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
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
decl_stmt|;
specifier|public
name|LDAPOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|LDAPOptions
name|that
init|=
operator|(
name|LDAPOptions
operator|)
name|o
decl_stmt|;
return|return
name|options
operator|.
name|equals
argument_list|(
name|that
operator|.
name|options
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|options
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isUsernameTrim
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|USERNAMES_TRIM
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUserFilter
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|USER_FILTER
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUserBaseDn
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|USER_BASE_DN
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getUserSearchSubtree
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|USER_SEARCH_SUBTREE
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUserPubkeyAttribute
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|USER_PUBKEY_ATTRIBUTE
argument_list|)
return|;
block|}
specifier|public
name|String
name|getRoleFilter
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ROLE_FILTER
argument_list|)
return|;
block|}
specifier|public
name|String
name|getRoleBaseDn
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ROLE_BASE_DN
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getRoleSearchSubtree
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ROLE_SEARCH_SUBTREE
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getRoleNameAttribute
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ROLE_NAME_ATTRIBUTE
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRoleMapping
parameter_list|()
block|{
return|return
name|parseRoleMapping
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ROLE_MAPPING
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|parseRoleMapping
parameter_list|(
name|String
name|option
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|roleMapping
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Parse role mapping {}"
argument_list|,
name|option
argument_list|)
expr_stmt|;
name|String
index|[]
name|mappings
init|=
name|option
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|mapping
range|:
name|mappings
control|)
block|{
name|int
name|index
init|=
name|mapping
operator|.
name|lastIndexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|String
name|ldapRole
init|=
name|mapping
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
index|[]
name|karafRoles
init|=
name|mapping
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|karafRolesSet
init|=
name|roleMapping
operator|.
name|computeIfAbsent
argument_list|(
name|ldapRole
argument_list|,
name|k
lambda|->
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|karafRole
range|:
name|karafRoles
control|)
block|{
name|karafRolesSet
operator|.
name|add
argument_list|(
name|karafRole
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|roleMapping
return|;
block|}
specifier|public
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEnv
parameter_list|()
throws|throws
name|NamingException
block|{
specifier|final
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|options
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|CONTEXT_PREFIX
argument_list|)
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|key
operator|.
name|substring
argument_list|(
name|CONTEXT_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|options
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
name|getInitialContextFactory
argument_list|()
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
name|getConnectionURL
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConnectionUsername
argument_list|()
operator|!=
literal|null
operator|&&
name|getConnectionUsername
argument_list|()
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
name|String
name|auth
init|=
name|getAuthentication
argument_list|()
decl_stmt|;
if|if
condition|(
name|auth
operator|==
literal|null
condition|)
block|{
name|auth
operator|=
name|DEFAULT_AUTHENTICATION
expr_stmt|;
block|}
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|SECURITY_AUTHENTICATION
argument_list|,
name|auth
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
name|getConnectionUsername
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
name|getConnectionPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getAuthentication
argument_list|()
operator|!=
literal|null
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
name|getAuthentication
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getSsl
argument_list|()
condition|)
block|{
name|setupSsl
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
return|return
name|env
return|;
block|}
specifier|protected
name|void
name|setupSsl
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
parameter_list|)
throws|throws
name|NamingException
block|{
name|BundleContext
name|bundleContext
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|LDAPOptions
operator|.
name|class
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|ServiceReference
argument_list|<
name|KeystoreManager
argument_list|>
name|ref
init|=
literal|null
decl_stmt|;
try|try
block|{
name|LOGGER
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
argument_list|)
expr_stmt|;
name|KeystoreManager
name|manager
init|=
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
name|getSslProvider
argument_list|()
argument_list|,
name|getSslProtocol
argument_list|()
argument_list|,
name|getSslAlgorithm
argument_list|()
argument_list|,
name|getSslKeystore
argument_list|()
argument_list|,
name|getSslKeyAlias
argument_list|()
argument_list|,
name|getSslTrustStore
argument_list|()
argument_list|,
name|getSslTimeout
argument_list|()
argument_list|)
decl_stmt|;
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
operator|new
name|ManagedSSLSocketFactory
argument_list|(
name|factory
argument_list|)
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
name|NamingException
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
name|Object
name|getInitialContextFactory
parameter_list|()
block|{
name|String
name|initialContextFactory
init|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|INITIAL_CONTEXT_FACTORY
argument_list|)
decl_stmt|;
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
return|return
name|initialContextFactory
return|;
block|}
specifier|public
name|String
name|getConnectionURL
parameter_list|()
block|{
name|String
name|connectionURL
init|=
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|CONNECTION_URL
argument_list|)
decl_stmt|;
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
name|LOGGER
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
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Invalid LDAP URL: "
operator|+
name|connectionURL
argument_list|)
expr_stmt|;
block|}
return|return
name|connectionURL
return|;
block|}
specifier|public
name|String
name|getConnectionUsername
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|CONNECTION_USERNAME
argument_list|)
return|;
block|}
specifier|public
name|String
name|getConnectionPassword
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|CONNECTION_PASSWORD
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAuthentication
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|AUTHENTICATION
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getSsl
parameter_list|()
block|{
name|Object
name|val
init|=
name|options
operator|.
name|get
argument_list|(
name|SSL
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|val
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getConnectionURL
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"ldaps:"
argument_list|)
return|;
block|}
block|}
specifier|public
name|String
name|getSslProvider
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|SSL_PROVIDER
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSslProtocol
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|SSL_PROTOCOL
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSslAlgorithm
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|SSL_ALGORITHM
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSslKeystore
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|SSL_KEYSTORE
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSslKeyAlias
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|SSL_KEYALIAS
argument_list|)
return|;
block|}
specifier|public
name|String
name|getSslTrustStore
parameter_list|()
block|{
return|return
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|SSL_TRUSTSTORE
argument_list|)
return|;
block|}
specifier|public
name|int
name|getSslTimeout
parameter_list|()
block|{
name|Object
name|val
init|=
name|options
operator|.
name|get
argument_list|(
name|SSL_TIMEOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|val
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|val
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|DEFAULT_SSL_TIMEOUT
return|;
block|}
block|}
specifier|public
name|boolean
name|getAllowEmptyPasswords
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|JAASUtils
operator|.
name|getString
argument_list|(
name|options
argument_list|,
name|ALLOW_EMPTY_PASSWORDS
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getDisableCache
parameter_list|()
block|{
specifier|final
name|Object
name|object
init|=
name|options
operator|.
name|get
argument_list|(
name|DISABLE_CACHE
argument_list|)
decl_stmt|;
return|return
name|object
operator|==
literal|null
operator|||
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|object
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getIgnorePartialResultException
parameter_list|()
block|{
return|return
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
name|IGNORE_PARTIAL_RESULT_EXCEPTION
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

