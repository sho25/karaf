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
name|NamingEnumeration
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
name|naming
operator|.
name|directory
operator|.
name|Attribute
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
name|Attributes
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
name|naming
operator|.
name|directory
operator|.
name|SearchControls
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
name|SearchResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|event
operator|.
name|EventDirContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|event
operator|.
name|NamespaceChangeListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|event
operator|.
name|NamingEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|event
operator|.
name|NamingExceptionEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|event
operator|.
name|ObjectChangeListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|LDAPCache
implements|implements
name|Closeable
implements|,
name|NamespaceChangeListener
implements|,
name|ObjectChangeListener
block|{
specifier|private
specifier|static
specifier|final
name|ConcurrentMap
argument_list|<
name|LDAPOptions
argument_list|,
name|LDAPCache
argument_list|>
name|CACHES
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
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
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
while|while
condition|(
operator|!
name|CACHES
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LDAPOptions
name|options
init|=
name|CACHES
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|LDAPCache
name|cache
init|=
name|CACHES
operator|.
name|remove
argument_list|(
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|clearCache
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|LDAPCache
name|getCache
parameter_list|(
name|LDAPOptions
name|options
parameter_list|)
block|{
name|LDAPCache
name|cache
init|=
name|CACHES
operator|.
name|get
argument_list|(
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
name|CACHES
operator|.
name|putIfAbsent
argument_list|(
name|options
argument_list|,
operator|new
name|LDAPCache
argument_list|(
name|options
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|=
name|CACHES
operator|.
name|get
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|userDnAndNamespace
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|userRoles
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|userPubkeys
decl_stmt|;
specifier|private
specifier|final
name|LDAPOptions
name|options
decl_stmt|;
specifier|private
name|DirContext
name|context
decl_stmt|;
specifier|public
name|LDAPCache
parameter_list|(
name|LDAPOptions
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
name|userDnAndNamespace
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|userRoles
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|userPubkeys
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
block|{
name|clearCache
argument_list|()
expr_stmt|;
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
name|NamingException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
finally|finally
block|{
name|context
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isContextAlive
parameter_list|()
block|{
name|boolean
name|alive
init|=
literal|false
decl_stmt|;
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
name|getAttributes
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|alive
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
return|return
name|alive
return|;
block|}
specifier|public
specifier|synchronized
name|DirContext
name|open
parameter_list|()
throws|throws
name|NamingException
block|{
if|if
condition|(
name|isContextAlive
argument_list|()
condition|)
block|{
return|return
name|context
return|;
block|}
name|clearCache
argument_list|()
expr_stmt|;
name|context
operator|=
operator|new
name|InitialDirContext
argument_list|(
name|options
operator|.
name|getEnv
argument_list|()
argument_list|)
expr_stmt|;
name|EventDirContext
name|eventContext
init|=
operator|(
operator|(
name|EventDirContext
operator|)
name|context
operator|.
name|lookup
argument_list|(
literal|""
argument_list|)
operator|)
decl_stmt|;
specifier|final
name|SearchControls
name|constraints
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
name|constraints
operator|.
name|setSearchScope
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|options
operator|.
name|getDisableCache
argument_list|()
condition|)
block|{
name|String
name|filter
init|=
name|options
operator|.
name|getUserFilter
argument_list|()
decl_stmt|;
name|filter
operator|=
name|filter
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
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
name|filter
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
name|eventContext
operator|.
name|addNamingListener
argument_list|(
name|options
operator|.
name|getUserBaseDn
argument_list|()
argument_list|,
name|filter
argument_list|,
name|constraints
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|filter
operator|=
name|options
operator|.
name|getRoleFilter
argument_list|()
expr_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|filter
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
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
name|filter
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
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
name|filter
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
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
name|filter
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
name|eventContext
operator|.
name|addNamingListener
argument_list|(
name|options
operator|.
name|getRoleBaseDn
argument_list|()
argument_list|,
name|filter
argument_list|,
name|constraints
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|context
return|;
block|}
specifier|public
specifier|synchronized
name|String
index|[]
name|getUserDnAndNamespace
parameter_list|(
name|String
name|user
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|result
init|=
name|userDnAndNamespace
operator|.
name|get
argument_list|(
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|doGetUserDnAndNamespace
argument_list|(
name|user
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
name|options
operator|.
name|getDisableCache
argument_list|()
condition|)
block|{
name|userDnAndNamespace
operator|.
name|put
argument_list|(
name|user
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|String
index|[]
name|doGetUserDnAndNamespace
parameter_list|(
name|String
name|user
parameter_list|)
throws|throws
name|NamingException
block|{
name|DirContext
name|context
init|=
name|open
argument_list|()
decl_stmt|;
name|SearchControls
name|controls
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|getUserSearchSubtree
argument_list|()
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
name|String
name|filter
init|=
name|options
operator|.
name|getUserFilter
argument_list|()
decl_stmt|;
name|filter
operator|=
name|filter
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
name|filter
operator|=
name|filter
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Looking for the user in LDAP with "
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"  base DN: "
operator|+
name|options
operator|.
name|getUserBaseDn
argument_list|()
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"  filter: "
operator|+
name|filter
argument_list|)
expr_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|namingEnumeration
init|=
name|context
operator|.
name|search
argument_list|(
name|options
operator|.
name|getUserBaseDn
argument_list|()
argument_list|,
name|filter
argument_list|,
name|controls
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|namingEnumeration
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|LOGGER
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
literal|null
return|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Found the user DN."
argument_list|)
expr_stmt|;
name|SearchResult
name|result
init|=
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
name|String
name|userDNNamespace
init|=
name|result
operator|.
name|getNameInNamespace
argument_list|()
decl_stmt|;
comment|// handle case where cn, ou, dc case doesn't match
name|int
name|indexOfUserBaseDN
init|=
name|userDNNamespace
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|","
operator|+
name|options
operator|.
name|getUserBaseDn
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|userDN
init|=
operator|(
name|indexOfUserBaseDN
operator|>
literal|0
operator|)
condition|?
name|userDNNamespace
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|indexOfUserBaseDN
argument_list|)
else|:
name|result
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
operator|new
name|String
index|[]
block|{
name|userDN
block|,
name|userDNNamespace
block|}
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|namingEnumeration
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|namingEnumeration
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
specifier|public
specifier|synchronized
name|String
index|[]
name|getUserRoles
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|userDn
parameter_list|,
name|String
name|userDnNamespace
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|result
init|=
name|userRoles
operator|.
name|get
argument_list|(
name|userDn
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|doGetUserRoles
argument_list|(
name|user
argument_list|,
name|userDn
argument_list|,
name|userDnNamespace
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|options
operator|.
name|getDisableCache
argument_list|()
condition|)
block|{
name|userRoles
operator|.
name|put
argument_list|(
name|userDn
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|synchronized
name|String
index|[]
name|getUserPubkeys
parameter_list|(
name|String
name|userDn
parameter_list|)
throws|throws
name|NamingException
block|{
name|String
index|[]
name|result
init|=
name|userPubkeys
operator|.
name|get
argument_list|(
name|userDn
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|doGetUserPubkeys
argument_list|(
name|userDn
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|options
operator|.
name|getDisableCache
argument_list|()
condition|)
block|{
name|userPubkeys
operator|.
name|put
argument_list|(
name|userDn
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|tryMappingRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|getRoleMapping
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|roles
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|karafRoles
init|=
name|options
operator|.
name|getRoleMapping
argument_list|()
operator|.
name|get
argument_list|(
name|role
argument_list|)
decl_stmt|;
if|if
condition|(
name|karafRoles
operator|!=
literal|null
condition|)
block|{
comment|// add all mapped roles
for|for
control|(
name|String
name|karafRole
range|:
name|karafRoles
control|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"LDAP role {} is mapped to Karaf role {}"
argument_list|,
name|role
argument_list|,
name|karafRole
argument_list|)
expr_stmt|;
name|roles
operator|.
name|add
argument_list|(
name|karafRole
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|roles
return|;
block|}
specifier|private
name|String
index|[]
name|doGetUserRoles
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|userDn
parameter_list|,
name|String
name|userDnNamespace
parameter_list|)
throws|throws
name|NamingException
block|{
name|DirContext
name|context
init|=
name|open
argument_list|()
decl_stmt|;
name|SearchControls
name|controls
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|getRoleSearchSubtree
argument_list|()
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
name|String
name|filter
init|=
name|options
operator|.
name|getRoleFilter
argument_list|()
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|filter
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
name|filter
operator|=
name|filter
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
name|userDn
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
name|filter
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
name|userDnNamespace
argument_list|)
argument_list|)
expr_stmt|;
name|filter
operator|=
name|filter
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Looking for the user roles in LDAP with "
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"  base DN: "
operator|+
name|options
operator|.
name|getRoleBaseDn
argument_list|()
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"  filter: "
operator|+
name|filter
argument_list|)
expr_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|namingEnumeration
init|=
name|context
operator|.
name|search
argument_list|(
name|options
operator|.
name|getRoleBaseDn
argument_list|()
argument_list|,
name|filter
argument_list|,
name|controls
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|rolesList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|roles1
init|=
name|attributes
operator|.
name|get
argument_list|(
name|options
operator|.
name|getRoleNameAttribute
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|roles1
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
name|roles1
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
name|roles1
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"User {} is a member of role {}"
argument_list|,
name|user
argument_list|,
name|role
argument_list|)
expr_stmt|;
comment|// handle role mapping
name|Set
argument_list|<
name|String
argument_list|>
name|roleMappings
init|=
name|tryMappingRole
argument_list|(
name|role
argument_list|)
decl_stmt|;
if|if
condition|(
name|roleMappings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rolesList
operator|.
name|add
argument_list|(
name|role
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rolesList
operator|.
name|addAll
argument_list|(
name|roleMappings
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|rolesList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|rolesList
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|namingEnumeration
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|namingEnumeration
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"The user role filter is null so no roles are retrieved"
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{}
return|;
block|}
block|}
specifier|private
name|String
index|[]
name|doGetUserPubkeys
parameter_list|(
name|String
name|userDn
parameter_list|)
throws|throws
name|NamingException
block|{
name|DirContext
name|context
init|=
name|open
argument_list|()
decl_stmt|;
name|String
name|userPubkeyAttribute
init|=
name|options
operator|.
name|getUserPubkeyAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|userPubkeyAttribute
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Looking for public keys of user {} in attribute {}"
argument_list|,
name|userDn
argument_list|,
name|userPubkeyAttribute
argument_list|)
expr_stmt|;
name|Attributes
name|attributes
init|=
name|context
operator|.
name|getAttributes
argument_list|(
name|userDn
argument_list|,
operator|new
name|String
index|[]
block|{
name|userPubkeyAttribute
block|}
argument_list|)
decl_stmt|;
name|Attribute
name|pubkeyAttribute
init|=
name|attributes
operator|.
name|get
argument_list|(
name|userPubkeyAttribute
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|pubkeyList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|pubkeyAttribute
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
name|pubkeyAttribute
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|pk
init|=
operator|(
name|String
operator|)
name|pubkeyAttribute
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|pk
operator|!=
literal|null
condition|)
block|{
name|pubkeyList
operator|.
name|add
argument_list|(
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|pubkeyList
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|pubkeyList
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"The user public key attribute is null so no keys were retrieved"
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{}
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|objectAdded
parameter_list|(
name|NamingEvent
name|evt
parameter_list|)
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objectRemoved
parameter_list|(
name|NamingEvent
name|evt
parameter_list|)
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objectRenamed
parameter_list|(
name|NamingEvent
name|evt
parameter_list|)
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objectChanged
parameter_list|(
name|NamingEvent
name|evt
parameter_list|)
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|namingExceptionThrown
parameter_list|(
name|NamingExceptionEvent
name|evt
parameter_list|)
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|synchronized
name|void
name|clearCache
parameter_list|()
block|{
name|userDnAndNamespace
operator|.
name|clear
argument_list|()
expr_stmt|;
name|userRoles
operator|.
name|clear
argument_list|()
expr_stmt|;
name|userPubkeys
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

