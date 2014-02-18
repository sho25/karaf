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
name|BackingEngineFactory
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Queue
argument_list|<
name|JaasCommandSupport
argument_list|>
name|commandQueue
init|=
operator|(
name|Queue
argument_list|<
name|JaasCommandSupport
argument_list|>
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
literal|"No JAAS Realm / Module has been selected"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
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
name|List
argument_list|<
name|JaasRealm
argument_list|>
name|getRealms
parameter_list|()
block|{
if|if
condition|(
name|realms
operator|==
literal|null
condition|)
block|{
return|return
name|getAllServices
argument_list|(
name|JaasRealm
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|realms
return|;
block|}
block|}
specifier|public
name|BackingEngine
name|getBackingEngine
parameter_list|(
name|AppConfigurationEntry
name|entry
parameter_list|)
block|{
name|List
argument_list|<
name|BackingEngineFactory
argument_list|>
name|engineFactories
init|=
name|getAllServices
argument_list|(
name|BackingEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|engineFactories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BackingEngineFactory
name|factory
range|:
name|engineFactories
control|)
block|{
name|String
name|loginModuleClass
init|=
operator|(
name|String
operator|)
name|entry
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
name|factory
operator|.
name|getModuleClass
argument_list|()
operator|.
name|equals
argument_list|(
name|loginModuleClass
argument_list|)
condition|)
block|{
return|return
name|factory
operator|.
name|build
argument_list|(
name|entry
operator|.
name|getOptions
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

