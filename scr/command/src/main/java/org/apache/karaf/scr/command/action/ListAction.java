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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|scr
operator|.
name|command
operator|.
name|ScrUtils
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
name|support
operator|.
name|IdComparator
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
name|support
operator|.
name|table
operator|.
name|ShellTable
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
name|ComponentDescriptionDTO
import|;
end_import

begin_comment
comment|/**  * List all the components currently installed.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
name|ScrCommandConstants
operator|.
name|SCR_COMMAND
argument_list|,
name|name
operator|=
name|ScrCommandConstants
operator|.
name|LIST_FUNCTION
argument_list|,
name|description
operator|=
literal|"Display available components"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ListAction
extends|extends
name|ScrActionSupport
block|{
specifier|private
specifier|final
name|IdComparator
name|idComparator
init|=
operator|new
name|IdComparator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doScrAction
parameter_list|(
name|ServiceComponentRuntime
name|serviceComponentRuntime
parameter_list|)
throws|throws
name|Exception
block|{
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
literal|"ID"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"State"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Component Name"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ComponentConfigurationDTO
argument_list|>
name|configs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ComponentDescriptionDTO
name|component
range|:
name|serviceComponentRuntime
operator|.
name|getComponentDescriptionDTOs
argument_list|()
control|)
block|{
name|configs
operator|.
name|addAll
argument_list|(
name|serviceComponentRuntime
operator|.
name|getComponentConfigurationDTOs
argument_list|(
name|component
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|configs
argument_list|,
name|idComparator
argument_list|)
expr_stmt|;
for|for
control|(
name|ComponentConfigurationDTO
name|config
range|:
name|configs
control|)
block|{
comment|// Display only non hidden components, or all if showHidden is true
if|if
condition|(
name|showHidden
operator|||
operator|!
name|ScrActionSupport
operator|.
name|isHiddenComponent
argument_list|(
name|config
argument_list|)
condition|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|config
operator|.
name|id
argument_list|,
name|ScrUtils
operator|.
name|getState
argument_list|(
name|config
operator|.
name|state
argument_list|)
argument_list|,
name|config
operator|.
name|description
operator|.
name|name
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
block|}
end_class

end_unit

