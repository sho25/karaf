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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
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
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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

begin_comment
comment|/**  * @author iocanel  */
end_comment

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
literal|"manage"
argument_list|,
name|description
operator|=
literal|"Manage user and roles of a Jaas Realm."
argument_list|)
specifier|public
class|class
name|ManageRealmCommand
extends|extends
name|JaasCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"realm"
argument_list|,
name|description
operator|=
literal|"Jaas Realm"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|realmName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--module"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Realm Module"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|moduleName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--force"
argument_list|,
name|aliases
operator|=
block|{}
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
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"Another realm is being edited.  Cancel / update first, or use the --force option"
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
literal|"Another module is being edited.  Cancel / update first, or use the --force option"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|JaasRealm
name|realm
init|=
name|findRealmByName
argument_list|(
name|realmName
argument_list|)
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|AppConfigurationEntry
name|entry
init|=
name|findEntryByRealmAndName
argument_list|(
name|realm
argument_list|,
name|moduleName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
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
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Could not find module: %s in realm:%s"
argument_list|,
name|moduleName
argument_list|,
name|realmName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Could not find realm:%s"
argument_list|,
name|realmName
argument_list|)
argument_list|)
expr_stmt|;
block|}
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

