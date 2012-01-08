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
name|shell
operator|.
name|commands
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
name|DeactivateAction
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
name|completer
operator|.
name|DeactivateCompleter
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
name|CompletableFunction
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
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Activate
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Deactivate
import|;
end_import

begin_import
import|import
name|aQute
operator|.
name|bnd
operator|.
name|annotation
operator|.
name|component
operator|.
name|Reference
import|;
end_import

begin_comment
comment|/**  * Karaf Shell Command used to deactivate a Declarative Service Component.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
name|provide
operator|=
name|CompletableFunction
operator|.
name|class
argument_list|,
name|name
operator|=
name|DeactivateCommandComponent
operator|.
name|COMPONENT_NAME
argument_list|,
name|enabled
operator|=
literal|true
argument_list|,
name|immediate
operator|=
literal|true
argument_list|,
name|properties
operator|=
block|{
name|ScrCommandConstants
operator|.
name|OSGI_COMMAND_SCOPE_KEY
operator|+
literal|"="
operator|+
name|ScrCommandConstants
operator|.
name|SCR_COMMAND
block|,
name|ScrCommandConstants
operator|.
name|OSGI_COMMAND_FUNCTION_KEY
operator|+
literal|"="
operator|+
name|ScrCommandConstants
operator|.
name|DEACTIVATE_FUNCTION
block|}
argument_list|)
specifier|public
class|class
name|DeactivateCommandComponent
extends|extends
name|ScrCommandSupport
block|{
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_NAME
init|=
literal|"DeactivateCommand"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_LABEL
init|=
literal|"Apache Karaf SCR Deactivate Command"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|getActionClass
parameter_list|()
block|{
return|return
name|DeactivateAction
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Completer
argument_list|>
argument_list|>
name|getCompleterClasses
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Completer
argument_list|>
argument_list|>
name|completers
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Completer
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|completers
operator|.
name|add
argument_list|(
name|DeactivateCompleter
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|completers
return|;
block|}
annotation|@
name|Activate
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Activating the "
operator|+
name|COMPONENT_LABEL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deactivate
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Deactivating the "
operator|+
name|COMPONENT_LABEL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Reference
annotation|@
name|Override
specifier|public
name|void
name|setScrService
parameter_list|(
name|ScrService
name|scrService
parameter_list|)
block|{
name|super
operator|.
name|setScrService
argument_list|(
name|scrService
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

