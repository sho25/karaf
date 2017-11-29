begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|osgi
operator|.
name|secured
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessControlContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|runtime
operator|.
name|CommandNotFoundException
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
name|gogo
operator|.
name|runtime
operator|.
name|CommandSessionImpl
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
name|service
operator|.
name|command
operator|.
name|Function
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
name|service
operator|.
name|threadio
operator|.
name|ThreadIO
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
name|service
operator|.
name|guard
operator|.
name|tools
operator|.
name|ACLConfigurationParser
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Command
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
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
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|SessionFactoryImpl
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
name|util
operator|.
name|tracker
operator|.
name|SingleServiceTracker
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
name|Constants
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
name|InvalidSyntaxException
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
name|ServiceRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationListener
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
name|SecuredSessionFactoryImpl
extends|extends
name|SessionFactoryImpl
implements|implements
name|ConfigurationListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROXY_COMMAND_ACL_PID_PREFIX
init|=
literal|"org.apache.karaf.command.acl."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIGURATION_FILTER
init|=
literal|"("
operator|+
name|Constants
operator|.
name|SERVICE_PID
operator|+
literal|"="
operator|+
name|PROXY_COMMAND_ACL_PID_PREFIX
operator|+
literal|"*)"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHELL_SCOPE
init|=
literal|"shell"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHELL_INVOKE
init|=
literal|".invoke"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHELL_REDIRECT
init|=
literal|".redirect"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SecuredSessionFactoryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|scopes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|SingleServiceTracker
argument_list|<
name|ConfigurationAdmin
argument_list|>
name|configAdminTracker
decl_stmt|;
specifier|private
name|ServiceRegistration
argument_list|<
name|ConfigurationListener
argument_list|>
name|registration
decl_stmt|;
specifier|private
name|ThreadLocal
argument_list|<
name|Map
argument_list|<
name|Object
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|serviceVisibleMap
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|SecuredSessionFactoryImpl
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|ThreadIO
name|threadIO
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|super
argument_list|(
name|threadIO
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|ConfigurationListener
operator|.
name|class
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|configAdminTracker
operator|=
operator|new
name|SingleServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|ConfigurationAdmin
operator|.
name|class
argument_list|,
name|this
operator|::
name|update
argument_list|)
expr_stmt|;
name|this
operator|.
name|configAdminTracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|this
operator|.
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|this
operator|.
name|configAdminTracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|invoke
parameter_list|(
name|CommandSessionImpl
name|session
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|checkSecurity
argument_list|(
name|SHELL_SCOPE
argument_list|,
name|SHELL_INVOKE
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|target
argument_list|,
name|name
argument_list|,
name|args
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|invoke
argument_list|(
name|session
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|args
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Path
name|redirect
parameter_list|(
name|CommandSessionImpl
name|session
parameter_list|,
name|Path
name|path
parameter_list|,
name|int
name|mode
parameter_list|)
block|{
name|checkSecurity
argument_list|(
name|SHELL_SCOPE
argument_list|,
name|SHELL_REDIRECT
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|path
argument_list|,
name|mode
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|redirect
argument_list|(
name|session
argument_list|,
name|path
argument_list|,
name|mode
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Function
name|wrap
parameter_list|(
name|Command
name|command
parameter_list|)
block|{
return|return
operator|new
name|SecuredCommand
argument_list|(
name|this
argument_list|,
name|command
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isVisible
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|serviceVisibleMap
operator|.
name|set
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|(
name|service
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|(
name|service
argument_list|)
return|;
block|}
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|cmd
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|boolean
name|ret
init|=
name|isVisible
argument_list|(
name|cmd
operator|.
name|getScope
argument_list|()
argument_list|,
name|cmd
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|.
name|put
argument_list|(
name|service
argument_list|,
name|ret
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
else|else
block|{
name|boolean
name|ret
init|=
name|super
operator|.
name|isVisible
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|.
name|put
argument_list|(
name|service
argument_list|,
name|ret
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
specifier|public
name|boolean
name|isVisible
parameter_list|(
name|String
name|scope
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|boolean
name|visible
init|=
literal|true
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getScopeConfig
argument_list|(
name|scope
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|visible
operator|=
literal|false
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
name|roles
argument_list|)
expr_stmt|;
if|if
condition|(
name|roles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|visible
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
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
name|currentUserHasRole
argument_list|(
name|role
argument_list|)
condition|)
block|{
name|visible
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
name|AliasCommand
name|aliasCommand
init|=
name|findAlias
argument_list|(
name|scope
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|aliasCommand
operator|!=
literal|null
condition|)
block|{
name|visible
operator|=
name|visible
operator|&&
name|isAliasVisible
argument_list|(
name|aliasCommand
operator|.
name|getScope
argument_list|()
argument_list|,
name|aliasCommand
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|visible
return|;
block|}
specifier|public
name|boolean
name|isAliasVisible
parameter_list|(
name|String
name|scope
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getScopeConfig
argument_list|(
name|scope
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
name|roles
argument_list|)
expr_stmt|;
if|if
condition|(
name|roles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
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
name|currentUserHasRole
argument_list|(
name|role
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|AliasCommand
name|findAlias
parameter_list|(
name|String
name|scope
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|vars
init|=
operator|(
operator|(
name|Set
argument_list|<
name|String
argument_list|>
operator|)
name|session
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|aliases
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|aliasScope
init|=
literal|null
decl_stmt|;
name|String
name|aliasName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|var
range|:
name|vars
control|)
block|{
name|Object
name|content
init|=
name|session
operator|.
name|get
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
operator|&&
literal|"org.apache.felix.gogo.runtime.Closure"
operator|.
name|equals
argument_list|(
name|content
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|var
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|aliasScope
operator|=
name|var
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|aliasName
operator|=
name|var
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|originalCmd
init|=
name|content
operator|.
name|toString
argument_list|()
decl_stmt|;
name|index
operator|=
name|originalCmd
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|Object
name|securityCmd
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|securityCmd
operator|=
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|runtime
operator|.
name|Closure
operator|)
name|content
operator|)
operator|.
name|get
argument_list|(
name|originalCmd
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|securityCmd
operator|instanceof
name|SecuredCommand
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|SecuredCommand
operator|)
name|securityCmd
operator|)
operator|.
name|getScope
argument_list|()
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
operator|&&
operator|(
operator|(
name|SecuredCommand
operator|)
name|securityCmd
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|new
name|AliasCommand
argument_list|(
name|aliasScope
argument_list|,
name|aliasName
argument_list|)
return|;
block|}
block|}
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
name|void
name|checkSecurity
parameter_list|(
name|String
name|scope
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getScopeConfig
argument_list|(
name|scope
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|boolean
name|passCheck
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|isVisible
argument_list|(
name|scope
argument_list|,
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CommandNotFoundException
argument_list|(
name|scope
operator|+
literal|":"
operator|+
name|name
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ACLConfigurationParser
operator|.
name|Specificity
name|s
init|=
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
name|name
argument_list|,
operator|new
name|Object
index|[]
block|{
name|arguments
operator|.
name|toString
argument_list|()
block|}
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
name|roles
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
name|ACLConfigurationParser
operator|.
name|Specificity
operator|.
name|NO_MATCH
condition|)
block|{
name|passCheck
operator|=
literal|true
expr_stmt|;
block|}
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
name|currentUserHasRole
argument_list|(
name|role
argument_list|)
condition|)
block|{
name|passCheck
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|passCheck
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Insufficient credentials."
argument_list|)
throw|;
block|}
block|}
name|AliasCommand
name|aliasCommand
init|=
name|findAlias
argument_list|(
name|scope
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|aliasCommand
operator|!=
literal|null
condition|)
block|{
comment|//this is the alias
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|isAliasVisible
argument_list|(
name|aliasCommand
operator|.
name|getScope
argument_list|()
argument_list|,
name|aliasCommand
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CommandNotFoundException
argument_list|(
name|aliasCommand
operator|.
name|getScope
argument_list|()
operator|+
literal|":"
operator|+
name|aliasCommand
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ACLConfigurationParser
operator|.
name|Specificity
name|s
init|=
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
name|aliasCommand
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|Object
index|[]
block|{
name|arguments
operator|.
name|toString
argument_list|()
block|}
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
name|roles
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
name|ACLConfigurationParser
operator|.
name|Specificity
operator|.
name|NO_MATCH
condition|)
block|{
return|return;
block|}
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
name|currentUserHasRole
argument_list|(
name|role
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Insufficient credentials."
argument_list|)
throw|;
block|}
block|}
block|}
specifier|static
name|boolean
name|currentUserHasRole
parameter_list|(
name|String
name|requestedRole
parameter_list|)
block|{
name|String
name|clazz
decl_stmt|;
name|String
name|role
decl_stmt|;
name|int
name|index
init|=
name|requestedRole
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|clazz
operator|=
name|requestedRole
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|role
operator|=
name|requestedRole
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clazz
operator|=
name|RolePrincipal
operator|.
name|class
operator|.
name|getName
argument_list|()
expr_stmt|;
name|role
operator|=
name|requestedRole
expr_stmt|;
block|}
name|AccessControlContext
name|acc
init|=
name|AccessController
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|acc
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Subject
name|subject
init|=
name|Subject
operator|.
name|getSubject
argument_list|(
name|acc
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Principal
name|p
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|clazz
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|role
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|event
operator|.
name|getPid
argument_list|()
operator|.
name|startsWith
argument_list|(
name|PROXY_COMMAND_ACL_PID_PREFIX
argument_list|)
condition|)
return|return;
try|try
block|{
synchronized|synchronized
init|(
name|this
operator|.
name|serviceVisibleMap
init|)
block|{
if|if
condition|(
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|serviceVisibleMap
operator|.
name|get
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
switch|switch
condition|(
name|event
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|ConfigurationEvent
operator|.
name|CM_DELETED
case|:
name|removeScopeConfig
argument_list|(
name|event
operator|.
name|getPid
argument_list|()
operator|.
name|substring
argument_list|(
name|PROXY_COMMAND_ACL_PID_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|ConfigurationEvent
operator|.
name|CM_UPDATED
case|:
name|ConfigurationAdmin
name|configAdmin
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|event
operator|.
name|getReference
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|addScopeConfig
argument_list|(
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|event
operator|.
name|getPid
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|event
operator|.
name|getReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Problem processing Configuration Event {}"
argument_list|,
name|event
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addScopeConfig
parameter_list|(
name|Configuration
name|config
parameter_list|)
block|{
if|if
condition|(
operator|!
name|config
operator|.
name|getPid
argument_list|()
operator|.
name|startsWith
argument_list|(
name|PROXY_COMMAND_ACL_PID_PREFIX
argument_list|)
condition|)
block|{
comment|// not a command scope configuration file
return|return;
block|}
name|String
name|scope
init|=
name|config
operator|.
name|getPid
argument_list|()
operator|.
name|substring
argument_list|(
name|PROXY_COMMAND_ACL_PID_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>=
literal|0
condition|)
block|{
comment|// scopes don't contains dots, not a command scope
return|return;
block|}
name|scope
operator|=
name|scope
operator|.
name|trim
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|scopes
init|)
block|{
name|scopes
operator|.
name|put
argument_list|(
name|scope
argument_list|,
name|config
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|removeScopeConfig
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
synchronized|synchronized
init|(
name|scopes
init|)
block|{
name|scopes
operator|.
name|remove
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getScopeConfig
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
synchronized|synchronized
init|(
name|scopes
init|)
block|{
return|return
name|scopes
operator|.
name|get
argument_list|(
name|scope
argument_list|)
return|;
block|}
block|}
specifier|protected
name|void
name|update
parameter_list|(
name|ConfigurationAdmin
name|prev
parameter_list|,
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
try|try
block|{
name|Configuration
index|[]
name|configs
init|=
name|configAdmin
operator|.
name|listConfigurations
argument_list|(
name|CONFIGURATION_FILTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|configs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Configuration
name|config
range|:
name|configs
control|)
block|{
name|addScopeConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore, should never happen
block|}
block|}
block|}
end_class

end_unit

