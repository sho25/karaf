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
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|command
operator|.
name|completers
operator|.
name|ConfigurationCompleter
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

begin_comment
comment|/**  * Abstract class which commands that are related to property processing should extend.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ConfigPropertyCommandSupport
extends|extends
name|ConfigCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
literal|"--pid"
argument_list|,
name|description
operator|=
literal|"The configuration pid"
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
name|ConfigurationCompleter
operator|.
name|class
argument_list|)
specifier|protected
name|String
name|pid
decl_stmt|;
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
operator|&&
name|pid
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
block|}
else|else
block|{
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|propertyAction
argument_list|(
name|props
argument_list|)
expr_stmt|;
if|if
condition|(
name|requiresUpdate
argument_list|(
name|pid
argument_list|)
condition|)
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
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Perform an action on the properties.      *      * @param props the dictionary where to apply the action.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
specifier|abstract
name|void
name|propertyAction
parameter_list|(
name|Dictionary
name|props
parameter_list|)
function_decl|;
comment|/**      * Check if the configuration requires to be updated.      * The default behavior is to update if a valid pid has been passed to the method.      *      * @param pid the PID to check.      * @return<code>true</code> if the configuration requires an update,<code>false</code> else.      */
specifier|protected
name|boolean
name|requiresUpdate
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
return|return
name|pid
operator|!=
literal|null
return|;
block|}
comment|/**      * Retrieve configuration from the pid, if used or delegates to session from getting the configuration.      *      * @return the edited dictionary.      * @throws Exception in case of configuration failure.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|protected
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEditedProps
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
name|this
operator|.
name|configRepository
operator|.
name|getConfigProperties
argument_list|(
name|pid
argument_list|)
decl_stmt|;
return|return
operator|(
name|props
operator|!=
literal|null
operator|)
condition|?
name|props
else|:
name|super
operator|.
name|getEditedProps
argument_list|()
return|;
block|}
block|}
end_class

end_unit

