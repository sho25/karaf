begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|commands
operator|.
name|basic
package|;
end_package

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
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|Function
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
name|karaf
operator|.
name|shell
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
name|shell
operator|.
name|commands
operator|.
name|CommandWithAction
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
name|ServiceRegistration
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

begin_comment
comment|/**  * A very simple {@link Function} which creates {@link Action} based on a class name.  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|SimpleCommand
extends|extends
name|AbstractCommand
block|{
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
decl_stmt|;
specifier|public
name|SimpleCommand
parameter_list|()
block|{     }
specifier|public
name|SimpleCommand
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|this
operator|.
name|actionClass
operator|=
name|actionClass
expr_stmt|;
block|}
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
name|actionClass
return|;
block|}
specifier|public
name|void
name|setActionClass
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|this
operator|.
name|actionClass
operator|=
name|actionClass
expr_stmt|;
block|}
specifier|public
name|Action
name|createNewAction
parameter_list|()
block|{
try|try
block|{
return|return
name|actionClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|ServiceRegistration
name|export
parameter_list|(
name|BundleContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|Command
name|cmd
init|=
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmd
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Action class is not annotated with @Command"
argument_list|)
throw|;
block|}
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"osgi.command.scope"
argument_list|,
name|cmd
operator|.
name|scope
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"osgi.command.function"
argument_list|,
name|cmd
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleCommand
name|command
init|=
operator|new
name|SimpleCommand
argument_list|(
name|actionClass
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|registerService
argument_list|(
operator|new
name|String
index|[]
block|{
name|Function
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|CommandWithAction
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
argument_list|,
name|command
argument_list|,
name|props
argument_list|)
return|;
block|}
block|}
end_class

end_unit

