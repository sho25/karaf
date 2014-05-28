begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
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
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|AppConfigurationEntry
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
name|ProxyLoginModule
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
name|command
operator|.
name|completers
operator|.
name|LoginModuleNameCompleter
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
name|command
operator|.
name|completers
operator|.
name|RealmCompleter
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
name|JaasRealm
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
name|shell
operator|.
name|api
operator|.
name|action
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
name|action
operator|.
name|Completion
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
name|action
operator|.
name|Option
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
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jaas"
argument_list|,
name|name
operator|=
literal|"realm-manage"
argument_list|,
name|description
operator|=
literal|"Manage users and roles of a JAAS Realm"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ManageRealmCommand
extends|extends
name|JaasCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--realm"
argument_list|,
name|description
operator|=
literal|"JAAS Realm"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|RealmCompleter
operator|.
name|class
argument_list|)
name|String
name|realmName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--index"
argument_list|,
name|description
operator|=
literal|"Realm Index"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|index
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--module"
argument_list|,
name|description
operator|=
literal|"JAAS Login Module Class Name"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|LoginModuleNameCompleter
operator|.
name|class
argument_list|)
name|String
name|moduleName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-f"
argument_list|,
name|aliases
operator|=
block|{
literal|"--force"
block|}
argument_list|,
name|description
operator|=
literal|"Force the management of this realm, even if another one was under management"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|force
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|realmName
operator|==
literal|null
operator|&&
name|index
operator|<=
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"A valid realm or the realm index need to be specified"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|JaasRealm
name|oldRealm
init|=
operator|(
name|JaasRealm
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|JAAS_REALM
argument_list|)
decl_stmt|;
name|AppConfigurationEntry
name|oldEntry
init|=
operator|(
name|AppConfigurationEntry
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|JAAS_ENTRY
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldRealm
operator|!=
literal|null
operator|&&
operator|!
name|oldRealm
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|realmName
argument_list|)
operator|&&
operator|!
name|force
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Another JAAS Realm is being edited. Cancel/update first, or use the --force option."
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|oldEntry
operator|!=
literal|null
operator|&&
operator|!
name|oldEntry
operator|.
name|getLoginModuleName
argument_list|()
operator|.
name|equals
argument_list|(
name|moduleName
argument_list|)
operator|&&
operator|!
name|force
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Another JAAS Login Module is being edited. Cancel/update first, or use the --force option."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|JaasRealm
name|realm
init|=
literal|null
decl_stmt|;
name|AppConfigurationEntry
name|entry
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
comment|// user provided the index, get the realm AND entry from the index
if|if
condition|(
name|realms
operator|!=
literal|null
operator|&&
name|realms
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|i
init|=
literal|1
decl_stmt|;
name|realms_loop
label|:
for|for
control|(
name|JaasRealm
name|r
range|:
name|realms
control|)
block|{
name|AppConfigurationEntry
index|[]
name|entries
init|=
name|r
operator|.
name|getEntries
argument_list|()
decl_stmt|;
if|if
condition|(
name|entries
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|entries
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
name|index
condition|)
block|{
name|realm
operator|=
name|r
expr_stmt|;
name|entry
operator|=
name|entries
index|[
name|j
index|]
expr_stmt|;
break|break
name|realms_loop
break|;
block|}
name|i
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|realms
operator|!=
literal|null
operator|&&
name|realms
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|JaasRealm
name|r
range|:
name|realms
control|)
block|{
if|if
condition|(
name|r
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|realmName
argument_list|)
condition|)
block|{
name|realm
operator|=
name|r
expr_stmt|;
name|AppConfigurationEntry
index|[]
name|entries
init|=
name|realm
operator|.
name|getEntries
argument_list|()
decl_stmt|;
if|if
condition|(
name|entries
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AppConfigurationEntry
name|e
range|:
name|entries
control|)
block|{
name|String
name|moduleClass
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|getOptions
argument_list|()
operator|.
name|get
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|)
decl_stmt|;
if|if
condition|(
name|moduleName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getBackingEngine
argument_list|(
name|e
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|entry
operator|=
name|e
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
if|if
condition|(
name|moduleName
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getLoginModuleName
argument_list|()
argument_list|)
operator|||
name|moduleName
operator|.
name|equals
argument_list|(
name|moduleClass
argument_list|)
condition|)
block|{
name|entry
operator|=
name|e
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
name|realm
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"JAAS realm has not been found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"JAAS module has not been found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|Queue
argument_list|<
name|JaasCommandSupport
argument_list|>
name|commands
init|=
literal|null
decl_stmt|;
name|commands
operator|=
operator|(
name|Queue
argument_list|<
name|JaasCommandSupport
argument_list|>
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|JAAS_CMDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|commands
operator|==
literal|null
condition|)
block|{
name|commands
operator|=
operator|new
name|LinkedList
argument_list|<
name|JaasCommandSupport
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|JAAS_REALM
argument_list|,
name|realm
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|JAAS_ENTRY
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|JAAS_CMDS
argument_list|,
name|commands
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|(
name|BackingEngine
name|engine
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

