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
name|principal
operator|.
name|GroupPrincipal
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
name|shell
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
name|table
operator|.
name|ShellTable
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
name|List
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
literal|"user-list"
argument_list|,
name|description
operator|=
literal|"List the users of the selected JAAS realm/login module"
argument_list|)
specifier|public
class|class
name|ListUsersCommand
extends|extends
name|JaasCommandSupport
block|{
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
literal|"No JAAS Realm/Login Module has been selected"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|BackingEngine
name|engine
init|=
name|backingEngineService
operator|.
name|get
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
literal|"Can't get the list of users (no backing engine service found)"
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
name|List
argument_list|<
name|UserPrincipal
argument_list|>
name|users
init|=
name|engine
operator|.
name|listUsers
argument_list|()
decl_stmt|;
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"User Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Group"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Role"
argument_list|)
expr_stmt|;
for|for
control|(
name|UserPrincipal
name|user
range|:
name|users
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|reportedRoles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|userName
init|=
name|user
operator|.
name|getName
argument_list|()
decl_stmt|;
for|for
control|(
name|GroupPrincipal
name|group
range|:
name|engine
operator|.
name|listGroups
argument_list|(
name|user
argument_list|)
control|)
block|{
name|String
name|groupName
init|=
name|group
operator|.
name|getName
argument_list|()
decl_stmt|;
name|reportedRoles
operator|.
name|addAll
argument_list|(
name|displayRole
argument_list|(
name|engine
argument_list|,
name|userName
argument_list|,
name|groupName
argument_list|,
name|group
argument_list|,
name|table
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reportedRoles
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|userName
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|displayRole
parameter_list|(
name|BackingEngine
name|engine
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|groupName
parameter_list|,
name|Principal
name|principal
parameter_list|,
name|ShellTable
name|table
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RolePrincipal
argument_list|>
name|roles
init|=
name|engine
operator|.
name|listRoles
argument_list|(
name|principal
argument_list|)
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
operator|&&
name|roles
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
for|for
control|(
name|RolePrincipal
name|role
range|:
name|roles
control|)
block|{
name|String
name|roleName
init|=
name|role
operator|.
name|getName
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|roleName
argument_list|)
expr_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|userName
argument_list|,
name|groupName
argument_list|,
name|roleName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|names
return|;
block|}
block|}
end_class

end_unit

