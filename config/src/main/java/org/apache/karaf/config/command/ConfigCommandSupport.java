begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|config
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
name|Arrays
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
name|utils
operator|.
name|properties
operator|.
name|TypedProperties
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
name|config
operator|.
name|core
operator|.
name|ConfigRepository
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
name|Action
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
name|Reference
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

begin_comment
comment|/**  * Abstract class from which all commands related to the ConfigurationAdmin  * service should derive.  * This command retrieves a reference to the ConfigurationAdmin service before  * calling another method to actually process the command.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ConfigCommandSupport
implements|implements
name|Action
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_CONFIG_PID
init|=
literal|"ConfigCommand.PID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_CONFIG_PROPS
init|=
literal|"ConfigCommand.Props"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_FACTORY
init|=
literal|"ConfigCommand.Factory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_ALIAS
init|=
literal|"ConfigCommand.Alias"
decl_stmt|;
annotation|@
name|Reference
specifier|protected
name|ConfigRepository
name|configRepository
decl_stmt|;
annotation|@
name|Reference
specifier|protected
name|Session
name|session
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|doExecute
argument_list|()
return|;
block|}
specifier|protected
specifier|abstract
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
name|TypedProperties
name|getEditedProps
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|TypedProperties
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_CONFIG_PROPS
argument_list|)
return|;
block|}
specifier|public
name|void
name|setConfigRepository
parameter_list|(
name|ConfigRepository
name|configRepository
parameter_list|)
block|{
name|this
operator|.
name|configRepository
operator|=
name|configRepository
expr_stmt|;
block|}
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
specifier|protected
name|String
name|displayValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|"<null>"
return|;
block|}
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
name|Arrays
operator|.
name|toString
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|value
argument_list|)
return|;
block|}
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

