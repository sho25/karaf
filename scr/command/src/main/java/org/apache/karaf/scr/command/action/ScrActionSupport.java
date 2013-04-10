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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|scr
operator|.
name|Component
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
name|scr
operator|.
name|ScrService
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|shell
operator|.
name|console
operator|.
name|AbstractAction
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
name|CommandSessionHolder
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
name|SubShellAction
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
name|completer
operator|.
name|ArgumentCompleter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
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
name|framework
operator|.
name|FrameworkUtil
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
name|Hashtable
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
specifier|public
specifier|abstract
class|class
name|ScrActionSupport
extends|extends
name|SubShellAction
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
specifier|private
name|ScrService
name|scrService
decl_stmt|;
specifier|public
name|ScrActionSupport
parameter_list|()
block|{
name|setSubShell
argument_list|(
literal|"scr"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|scrService
operator|==
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"ScrService is unavailable"
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
name|scrService
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
name|ScrService
name|scrService
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|protected
name|boolean
name|isActionable
parameter_list|(
name|Component
name|component
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
return|return
name|answer
return|;
block|}
specifier|public
specifier|static
name|boolean
name|showHiddenComponent
parameter_list|(
name|Component
name|component
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|false
decl_stmt|;
comment|// first look to see if the show all option is there
comment|// if it is we set showAllFlag to true so the next section will be skipped
name|CommandSession
name|commandSession
init|=
name|CommandSessionHolder
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|ArgumentCompleter
operator|.
name|ArgumentList
name|list
init|=
operator|(
name|ArgumentCompleter
operator|.
name|ArgumentList
operator|)
name|commandSession
operator|.
name|get
argument_list|(
name|ArgumentCompleter
operator|.
name|ARGUMENTS_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
name|list
operator|.
name|getArguments
argument_list|()
operator|!=
literal|null
operator|&&
name|list
operator|.
name|getArguments
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
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
name|list
operator|.
name|getArguments
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
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
condition|)
block|{
name|answer
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|answer
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
name|Component
name|component
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|false
decl_stmt|;
name|Hashtable
name|properties
init|=
operator|(
name|Hashtable
operator|)
name|component
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
operator|&&
name|properties
operator|.
name|containsKey
argument_list|(
name|ScrCommandConstants
operator|.
name|HIDDEN_COMPONENT_KEY
argument_list|)
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
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
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|equals
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|answer
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
specifier|public
name|String
name|getBoldString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
name|value
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD_OFF
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|buildRightPadBracketDisplay
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|max
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$-"
operator|+
name|max
operator|+
literal|"s"
argument_list|,
name|s
argument_list|)
return|;
block|}
specifier|public
name|String
name|buildLeftPadBracketDisplay
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|max
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$-"
operator|+
name|max
operator|+
literal|"s"
argument_list|,
name|s
argument_list|)
return|;
block|}
comment|/**      * Get the bundleContext Object associated with this instance of      * ScrActionSupport.      *       * @return the bundleContext      */
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|ListAction
operator|.
name|class
argument_list|)
operator|.
name|getBundleContext
argument_list|()
return|;
block|}
comment|/**      * Get the scrService Object associated with this instance of      * ScrActionSupport.      *       * @return the scrService      */
specifier|public
name|ScrService
name|getScrService
parameter_list|()
block|{
return|return
name|scrService
return|;
block|}
comment|/**      * Sets the scrService Object for this ScrActionSupport instance.      *       * @param scrService the scrService to set      */
specifier|public
name|void
name|setScrService
parameter_list|(
name|ScrService
name|scrService
parameter_list|)
block|{
name|this
operator|.
name|scrService
operator|=
name|scrService
expr_stmt|;
block|}
block|}
end_class

end_unit

