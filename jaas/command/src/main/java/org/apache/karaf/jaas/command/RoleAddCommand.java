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
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|BackingEngine
import|;
end_import

begin_comment
comment|/**  * Adds a role to a user of the active realm/module.  *  * @author iocanel  */
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
literal|"roleadd"
argument_list|,
name|description
operator|=
literal|"Add a role to a user."
argument_list|)
specifier|public
class|class
name|RoleAddCommand
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
literal|"username"
argument_list|,
name|description
operator|=
literal|"User Name"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"role"
argument_list|,
name|description
operator|=
literal|"Role"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|role
decl_stmt|;
comment|/**      * Execute the RoleAddCommand in the given Excecution Context.      *      * @param engine      * @return      * @throws Exception      */
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
name|engine
operator|.
name|addRole
argument_list|(
name|username
argument_list|,
name|role
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
specifier|public
name|String
name|getRole
parameter_list|()
block|{
return|return
name|role
return|;
block|}
specifier|public
name|void
name|setRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
name|this
operator|.
name|role
operator|=
name|role
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RoleAddCommand{"
operator|+
literal|"username='"
operator|+
name|username
operator|+
literal|'\''
operator|+
literal|", role='"
operator|+
name|role
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

