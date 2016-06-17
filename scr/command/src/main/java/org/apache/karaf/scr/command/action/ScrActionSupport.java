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
name|scr
operator|.
name|command
operator|.
name|action
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
name|java
operator|.
name|util
operator|.
name|List
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
name|scr
operator|.
name|command
operator|.
name|ScrCommandConstants
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
name|CommandLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|ServiceComponentRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|component
operator|.
name|runtime
operator|.
name|dto
operator|.
name|ComponentConfigurationDTO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|ScrActionSupport
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
name|ScrActionSupport
operator|.
name|SHOW_ALL_OPTION
argument_list|,
name|aliases
operator|=
block|{
name|ScrActionSupport
operator|.
name|SHOW_ALL_ALIAS
block|}
argument_list|,
name|description
operator|=
literal|"Show all Components including the System Components (hidden by default)"
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
name|showHidden
init|=
literal|false
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_ALL_OPTION
init|=
literal|"-s"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SHOW_ALL_ALIAS
init|=
literal|"--show-hidden"
decl_stmt|;
specifier|protected
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|ServiceComponentRuntime
name|serviceComponentRuntime
decl_stmt|;
annotation|@
name|Reference
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|ScrActionSupport
parameter_list|()
block|{     }
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
name|serviceComponentRuntime
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"ServiceComponentRuntime is unavailable"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|logger
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doScrAction
argument_list|(
name|serviceComponentRuntime
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
specifier|abstract
name|Object
name|doScrAction
parameter_list|(
name|ServiceComponentRuntime
name|serviceComponentRuntime
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|public
specifier|static
name|boolean
name|showHiddenComponent
parameter_list|(
name|CommandLine
name|commandLine
parameter_list|)
block|{
comment|// first look to see if the show all option is there
comment|// if it is we set showAllFlag to true so the next section will be skipped
name|List
argument_list|<
name|String
argument_list|>
name|arguments
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|commandLine
operator|.
name|getArguments
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|arguments
operator|.
name|contains
argument_list|(
name|ScrActionSupport
operator|.
name|SHOW_ALL_OPTION
argument_list|)
operator|||
name|arguments
operator|.
name|contains
argument_list|(
name|ScrActionSupport
operator|.
name|SHOW_ALL_ALIAS
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
specifier|static
name|boolean
name|isHiddenComponent
parameter_list|(
name|ComponentConfigurationDTO
name|config
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|properties
operator|!=
literal|null
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|properties
operator|.
name|get
argument_list|(
name|ScrCommandConstants
operator|.
name|HIDDEN_COMPONENT_KEY
argument_list|)
decl_stmt|;
comment|// if the value is false, show the hidden
comment|// then someone wants us to display the name of a hidden component
name|answer
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Get the bundleContext Object associated with this instance of      * ScrActionSupport.      *       * @return the bundleContext      */
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
comment|/**      * Get the ServiceComponentRuntime Object associated with this instance of      * ScrActionSupport.      *       * @return the ServiceComponentRuntime      */
specifier|public
name|ServiceComponentRuntime
name|getServiceComponentRuntime
parameter_list|()
block|{
return|return
name|serviceComponentRuntime
return|;
block|}
comment|/**      * Sets the ServiceComponentRuntime Object for this ScrActionSupport instance.      *       * @param serviceComponentRuntime the ServiceComponentRuntime to set      */
specifier|public
name|void
name|setServiceComponentRuntime
parameter_list|(
name|ServiceComponentRuntime
name|serviceComponentRuntime
parameter_list|)
block|{
name|this
operator|.
name|serviceComponentRuntime
operator|=
name|serviceComponentRuntime
expr_stmt|;
block|}
block|}
end_class

end_unit

