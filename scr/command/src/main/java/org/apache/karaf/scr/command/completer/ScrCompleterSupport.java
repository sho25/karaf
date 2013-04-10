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
name|completer
package|;
end_package

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
name|karaf
operator|.
name|scr
operator|.
name|command
operator|.
name|action
operator|.
name|ScrActionSupport
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
name|Completer
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
name|StringsCompleter
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
name|ScrCompleterSupport
implements|implements
name|Completer
block|{
specifier|protected
specifier|final
specifier|transient
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ScrCompleterSupport
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ScrService
name|scrService
decl_stmt|;
comment|/**      * Overrides the super method noted below. See super documentation for      * details.      *      * @see org.apache.karaf.shell.console.Completer#complete(java.lang.String,      *      int, java.util.List)      */
specifier|public
name|int
name|complete
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|cursor
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|Component
name|component
range|:
name|scrService
operator|.
name|getComponents
argument_list|()
control|)
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Component Name to work on: "
operator|+
name|component
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ScrActionSupport
operator|.
name|showHiddenComponent
argument_list|(
name|component
argument_list|)
condition|)
block|{
comment|// we display all because we are overridden
if|if
condition|(
name|availableComponent
argument_list|(
name|component
argument_list|)
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|component
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|ScrActionSupport
operator|.
name|isHiddenComponent
argument_list|(
name|component
argument_list|)
condition|)
block|{
comment|// do nothing
block|}
else|else
block|{
comment|// we aren't hidden so print it
if|if
condition|(
name|availableComponent
argument_list|(
name|component
argument_list|)
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|component
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Exception completing the command request: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|boolean
name|availableComponent
parameter_list|(
name|Component
name|component
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get the scrService Object associated with this instance of      * ScrCompleterSupport.      *      * @return the scrService      */
specifier|public
name|ScrService
name|getScrService
parameter_list|()
block|{
return|return
name|scrService
return|;
block|}
comment|/**      * Sets the scrService Object for this ScrCompleterSupport instance.      *      * @param scrService the scrService to set      */
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

