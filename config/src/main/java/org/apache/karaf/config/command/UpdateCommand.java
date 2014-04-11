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
name|Dictionary
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
literal|"config"
argument_list|,
name|name
operator|=
literal|"update"
argument_list|,
name|description
operator|=
literal|"Saves and propagates changes from the configuration being edited."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|UpdateCommand
extends|extends
name|ConfigCommandSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
name|props
init|=
name|getEditedProps
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
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
literal|"No configuration is being edited--run the edit command first"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|pid
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|)
decl_stmt|;
name|boolean
name|isFactory
init|=
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_FACTORY
argument_list|)
operator|!=
literal|null
operator|&&
operator|(
name|Boolean
operator|)
name|this
operator|.
name|session
operator|.
name|get
argument_list|(
name|PROPERTY_FACTORY
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFactory
condition|)
block|{
name|this
operator|.
name|configRepository
operator|.
name|createFactoryConfiguration
argument_list|(
name|pid
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|configRepository
operator|.
name|update
argument_list|(
name|pid
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_FACTORY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|.
name|put
argument_list|(
name|PROPERTY_CONFIG_PROPS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
