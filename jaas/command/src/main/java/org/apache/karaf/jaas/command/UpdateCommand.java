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
literal|"update"
argument_list|,
name|description
operator|=
literal|"Apply pending modification on the edited JAAS Realm"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|UpdateCommand
extends|extends
name|JaasCommandSupport
block|{
annotation|@
name|Override
specifier|public
name|Object
name|execute
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
if|if
condition|(
name|realm
operator|==
literal|null
operator|||
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
literal|"No JAAS Realm/Login Module selected"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|BackingEngine
name|engine
init|=
name|getBackingEngine
argument_list|(
name|entry
argument_list|)
decl_stmt|;
if|if
condition|(
name|engine
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
literal|"Can't update the JAAS realm (no backing engine service registered)"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|doExecute
argument_list|(
name|engine
argument_list|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Queue
argument_list|<
name|?
extends|extends
name|JaasCommandSupport
argument_list|>
name|commands
init|=
operator|(
name|Queue
argument_list|<
name|?
extends|extends
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
name|commands
operator|==
literal|null
operator|||
name|commands
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No JAAS pending modification"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|//Loop throught the commands and execute them.
while|while
condition|(
operator|!
name|commands
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|commands
operator|.
name|remove
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|JaasCommandSupport
condition|)
block|{
operator|(
operator|(
name|JaasCommandSupport
operator|)
name|obj
operator|)
operator|.
name|doExecute
argument_list|(
name|engine
argument_list|)
expr_stmt|;
block|}
block|}
comment|//Cleanup the session
name|session
operator|.
name|put
argument_list|(
name|JAAS_REALM
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|JAAS_ENTRY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|JAAS_CMDS
argument_list|,
operator|new
name|LinkedList
argument_list|<
name|JaasCommandSupport
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

