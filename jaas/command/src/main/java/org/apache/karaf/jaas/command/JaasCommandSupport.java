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
name|jaas
operator|.
name|modules
operator|.
name|BackingEngineService
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
name|console
operator|.
name|OsgiCommandSupport
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
name|List
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

begin_class
specifier|public
specifier|abstract
class|class
name|JaasCommandSupport
extends|extends
name|OsgiCommandSupport
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JAAS_REALM
init|=
literal|"JaasCommand.REALM"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAAS_ENTRY
init|=
literal|"JaasCommand.ENTRY"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAAS_CMDS
init|=
literal|"JaasCommand.COMMANDS"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JaasRealm
argument_list|>
name|realms
decl_stmt|;
specifier|protected
name|BackingEngineService
name|backingEngineService
decl_stmt|;
specifier|protected
specifier|abstract
name|Object
name|doExecute
parameter_list|(
name|BackingEngine
name|engine
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Add the command to the command queue.      *      * @return      * @throws Exception      */
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|JaasRealm
name|realm
init|=
operator|(
name|JaasRealm
operator|)
name|session
operator|.
name|get
argument_list|(
name|JAAS_REALM
argument_list|)
decl_stmt|;
name|AppConfigurationEntry
name|entry
init|=
operator|(
name|AppConfigurationEntry
operator|)
name|session
operator|.
name|get
argument_list|(
name|JAAS_ENTRY
argument_list|)
decl_stmt|;
name|Queue
name|commandQueue
init|=
operator|(
name|Queue
operator|)
name|session
operator|.
name|get
argument_list|(
name|JAAS_CMDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
operator|&&
name|entry
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|commandQueue
operator|!=
literal|null
condition|)
block|{
name|commandQueue
operator|.
name|add
argument_list|(
name|this
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
literal|"No JAAS Realm / Module has been selected."
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the Jaas Realm named as realmName.      *      * @param realmName      * @return      */
specifier|public
name|JaasRealm
name|findRealmByNameOrIndex
parameter_list|(
name|String
name|realmName
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|JaasRealm
name|realm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|realms
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|realms
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|realms
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|realmName
argument_list|)
operator|||
name|index
operator|==
name|i
condition|)
return|return
name|realms
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
return|;
block|}
block|}
return|return
name|realm
return|;
block|}
comment|/**      * Returns the Jaas Module entry of the specified realm, named as moduleName.      *      * @param moduleName      * @return      */
specifier|public
name|AppConfigurationEntry
name|findEntryByRealmAndName
parameter_list|(
name|JaasRealm
name|realm
parameter_list|,
name|String
name|moduleName
parameter_list|)
block|{
name|AppConfigurationEntry
name|appConfigurationEntry
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|AppConfigurationEntry
index|[]
name|entries
init|=
name|realm
operator|.
name|getEntries
argument_list|()
decl_stmt|;
comment|//If no moduleName provided and a there is a single module in the realm.
if|if
condition|(
name|entries
operator|!=
literal|null
operator|&&
name|entries
operator|.
name|length
operator|==
literal|1
operator|&&
name|moduleName
operator|==
literal|null
condition|)
block|{
return|return
name|entries
index|[
literal|0
index|]
return|;
block|}
for|for
control|(
name|AppConfigurationEntry
name|entry
range|:
name|entries
control|)
block|{
if|if
condition|(
name|moduleName
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getLoginModuleName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
return|;
block|}
block|}
block|}
return|return
name|appConfigurationEntry
return|;
block|}
specifier|public
name|List
argument_list|<
name|JaasRealm
argument_list|>
name|getRealms
parameter_list|()
block|{
return|return
name|realms
return|;
block|}
specifier|public
name|void
name|setRealms
parameter_list|(
name|List
argument_list|<
name|JaasRealm
argument_list|>
name|realms
parameter_list|)
block|{
name|this
operator|.
name|realms
operator|=
name|realms
expr_stmt|;
block|}
specifier|public
name|BackingEngineService
name|getBackingEngineService
parameter_list|()
block|{
return|return
name|backingEngineService
return|;
block|}
specifier|public
name|void
name|setBackingEngineService
parameter_list|(
name|BackingEngineService
name|backingEngineService
parameter_list|)
block|{
name|this
operator|.
name|backingEngineService
operator|=
name|backingEngineService
expr_stmt|;
block|}
block|}
end_class

end_unit

