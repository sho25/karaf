begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|impl
operator|.
name|action
operator|.
name|osgi
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|console
operator|.
name|Registry
import|;
end_import

begin_class
specifier|public
class|class
name|RegistryImpl
implements|implements
name|Registry
block|{
specifier|private
specifier|final
name|Registry
name|parent
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|services
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Command
argument_list|>
argument_list|>
name|commands
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Command
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|RegistryImpl
parameter_list|(
name|Registry
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Command
argument_list|>
name|getCommands
parameter_list|()
block|{
return|return
name|getServices
argument_list|(
name|Command
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Command
name|getCommand
parameter_list|(
name|String
name|scope
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|Command
name|command
init|=
name|parent
operator|.
name|getCommand
argument_list|(
name|scope
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|command
operator|!=
literal|null
condition|)
block|{
return|return
name|command
return|;
block|}
block|}
synchronized|synchronized
init|(
name|services
init|)
block|{
name|List
argument_list|<
name|Command
argument_list|>
name|cmds
init|=
name|commands
operator|.
name|get
argument_list|(
name|scope
operator|+
literal|":"
operator|+
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmds
operator|!=
literal|null
operator|&&
operator|!
name|cmds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|cmds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|register
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|factory
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
name|services
operator|.
name|put
argument_list|(
name|clazz
argument_list|,
operator|new
name|Factory
argument_list|<
name|T
argument_list|>
argument_list|(
name|clazz
argument_list|,
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
name|services
operator|.
name|put
argument_list|(
name|service
argument_list|,
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|cmd
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|String
name|key
init|=
name|cmd
operator|.
name|getScope
argument_list|()
operator|+
literal|":"
operator|+
name|cmd
operator|.
name|getName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Command
argument_list|>
name|cmds
init|=
name|commands
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmds
operator|==
literal|null
condition|)
block|{
name|cmds
operator|=
operator|new
name|ArrayList
argument_list|<
name|Command
argument_list|>
argument_list|()
expr_stmt|;
name|commands
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|cmds
argument_list|)
expr_stmt|;
block|}
name|cmds
operator|.
name|add
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|unregister
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
name|services
operator|.
name|remove
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|cmd
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|String
name|key
init|=
name|cmd
operator|.
name|getScope
argument_list|()
operator|+
literal|":"
operator|+
name|cmd
operator|.
name|getName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Command
argument_list|>
name|cmds
init|=
name|commands
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmds
operator|!=
literal|null
condition|)
block|{
name|cmds
operator|.
name|remove
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|commands
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
for|for
control|(
name|Object
name|service
range|:
name|services
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Factory
condition|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isAssignableFrom
argument_list|(
operator|(
operator|(
name|Factory
argument_list|<
name|?
argument_list|>
operator|)
name|service
operator|)
operator|.
name|clazz
argument_list|)
condition|)
block|{
if|if
condition|(
name|isVisible
argument_list|(
name|service
argument_list|)
condition|)
block|{
try|try
block|{
name|Object
name|value
init|=
operator|(
operator|(
name|Factory
argument_list|<
name|?
argument_list|>
operator|)
name|service
operator|)
operator|.
name|callable
operator|.
name|call
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
for|for
control|(
name|Object
name|v
range|:
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
control|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|v
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO: log exception
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|clazz
operator|.
name|isInstance
argument_list|(
name|service
argument_list|)
condition|)
block|{
if|if
condition|(
name|isVisible
argument_list|(
name|service
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|service
argument_list|)
return|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getService
argument_list|(
name|clazz
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getServices
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|services
init|)
block|{
for|for
control|(
name|Object
name|service
range|:
name|services
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Factory
condition|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isAssignableFrom
argument_list|(
operator|(
operator|(
name|Factory
argument_list|<
name|?
argument_list|>
operator|)
name|service
operator|)
operator|.
name|clazz
argument_list|)
condition|)
block|{
if|if
condition|(
name|isVisible
argument_list|(
name|service
argument_list|)
condition|)
block|{
try|try
block|{
name|Object
name|value
init|=
operator|(
operator|(
name|Factory
argument_list|<
name|?
argument_list|>
operator|)
name|service
operator|)
operator|.
name|callable
operator|.
name|call
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
for|for
control|(
name|Object
name|v
range|:
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|clazz
operator|.
name|cast
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|list
operator|.
name|add
argument_list|(
name|clazz
operator|.
name|cast
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO: log exception
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|clazz
operator|.
name|isInstance
argument_list|(
name|service
argument_list|)
condition|)
block|{
if|if
condition|(
name|isVisible
argument_list|(
name|service
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|clazz
operator|.
name|cast
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|parent
operator|.
name|getServices
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasService
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
for|for
control|(
name|Object
name|service
range|:
name|services
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Factory
condition|)
block|{
if|if
condition|(
name|clazz
operator|.
name|isAssignableFrom
argument_list|(
operator|(
operator|(
name|Factory
argument_list|<
name|?
argument_list|>
operator|)
name|service
operator|)
operator|.
name|clazz
argument_list|)
condition|)
block|{
if|if
condition|(
name|isVisible
argument_list|(
name|service
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|clazz
operator|.
name|isInstance
argument_list|(
name|service
argument_list|)
condition|)
block|{
if|if
condition|(
name|isVisible
argument_list|(
name|service
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|hasService
argument_list|(
name|clazz
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|boolean
name|isVisible
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|static
class|class
name|Factory
parameter_list|<
name|T
parameter_list|>
block|{
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
decl_stmt|;
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
decl_stmt|;
name|Factory
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
name|this
operator|.
name|callable
operator|=
name|callable
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

