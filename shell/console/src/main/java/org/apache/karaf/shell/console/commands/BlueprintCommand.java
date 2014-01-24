begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
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
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|shell
operator|.
name|commands
operator|.
name|basic
operator|.
name|AbstractCommand
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
name|basic
operator|.
name|ActionPreparator
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
name|basic
operator|.
name|DefaultActionPreparator
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
name|BlueprintContainerAware
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
name|BundleContextAware
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
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
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
name|blueprint
operator|.
name|container
operator|.
name|Converter
import|;
end_import

begin_class
specifier|public
class|class
name|BlueprintCommand
extends|extends
name|AbstractCommand
implements|implements
name|CompletableFunction
block|{
specifier|protected
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
specifier|protected
name|Converter
name|blueprintConverter
decl_stmt|;
specifier|protected
name|String
name|actionId
decl_stmt|;
specifier|protected
name|String
name|shell
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|optionalCompleters
decl_stmt|;
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
specifier|public
name|void
name|setBlueprintConverter
parameter_list|(
name|Converter
name|blueprintConverter
parameter_list|)
block|{
name|this
operator|.
name|blueprintConverter
operator|=
name|blueprintConverter
expr_stmt|;
block|}
specifier|public
name|void
name|setActionId
parameter_list|(
name|String
name|actionId
parameter_list|)
block|{
name|this
operator|.
name|actionId
operator|=
name|actionId
expr_stmt|;
block|}
specifier|public
name|void
name|setShell
parameter_list|(
name|String
name|shell
parameter_list|)
block|{
name|this
operator|.
name|shell
operator|=
name|shell
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Completer
argument_list|>
name|getCompleters
parameter_list|()
block|{
return|return
name|completers
return|;
block|}
specifier|public
name|void
name|setCompleters
parameter_list|(
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
parameter_list|)
block|{
name|this
operator|.
name|completers
operator|=
name|completers
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|getOptionalCompleters
parameter_list|()
block|{
return|return
name|optionalCompleters
return|;
block|}
specifier|public
name|void
name|setOptionalCompleters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|optionalCompleters
parameter_list|)
block|{
name|this
operator|.
name|optionalCompleters
operator|=
name|optionalCompleters
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ActionPreparator
name|getPreparator
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|BlueprintActionPreparator
argument_list|()
return|;
block|}
specifier|protected
class|class
name|BlueprintActionPreparator
extends|extends
name|DefaultActionPreparator
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|convert
parameter_list|(
name|Action
name|action
parameter_list|,
name|CommandSession
name|commandSession
parameter_list|,
name|Object
name|o
parameter_list|,
name|Type
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|GenericType
name|t
init|=
operator|new
name|GenericType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|getRawClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|o
operator|!=
literal|null
condition|?
name|o
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
return|return
name|blueprintConverter
operator|.
name|convert
argument_list|(
name|o
argument_list|,
name|t
argument_list|)
return|;
block|}
block|}
specifier|public
name|Action
name|createNewAction
parameter_list|()
block|{
name|Action
name|action
init|=
operator|(
name|Action
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|actionId
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|BlueprintContainerAware
condition|)
block|{
operator|(
operator|(
name|BlueprintContainerAware
operator|)
name|action
operator|)
operator|.
name|setBlueprintContainer
argument_list|(
name|blueprintContainer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|instanceof
name|BundleContextAware
condition|)
block|{
name|BundleContext
name|context
init|=
operator|(
name|BundleContext
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
literal|"blueprintBundleContext"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BundleContextAware
operator|)
name|action
operator|)
operator|.
name|setBundleContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|action
return|;
block|}
block|}
end_class

end_unit

