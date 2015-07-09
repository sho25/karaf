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
name|command
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
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|HashSet
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
name|Set
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
name|Destroy
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
name|Init
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
name|Manager
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
name|api
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
name|api
operator|.
name|console
operator|.
name|Parser
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
name|converter
operator|.
name|GenericType
import|;
end_import

begin_class
specifier|public
class|class
name|ManagerImpl
implements|implements
name|Manager
block|{
specifier|private
specifier|final
name|Registry
name|dependencies
decl_stmt|;
specifier|private
specifier|final
name|Registry
name|registrations
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|instances
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|allowCustomServices
decl_stmt|;
specifier|public
name|ManagerImpl
parameter_list|(
name|Registry
name|dependencies
parameter_list|,
name|Registry
name|registrations
parameter_list|)
block|{
name|this
argument_list|(
name|dependencies
argument_list|,
name|registrations
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ManagerImpl
parameter_list|(
name|Registry
name|dependencies
parameter_list|,
name|Registry
name|registrations
parameter_list|,
name|boolean
name|allowCustomServices
parameter_list|)
block|{
name|this
operator|.
name|dependencies
operator|=
name|dependencies
expr_stmt|;
name|this
operator|.
name|registrations
operator|=
name|registrations
expr_stmt|;
name|this
operator|.
name|allowCustomServices
operator|=
name|allowCustomServices
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|instantiate
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|instantiate
argument_list|(
name|clazz
argument_list|,
name|dependencies
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|instantiate
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|clazz
parameter_list|,
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|allowCustomServices
condition|)
block|{
name|Service
name|reg
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" is not annotated with @Service"
argument_list|)
throw|;
block|}
block|}
name|T
name|instance
init|=
name|clazz
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|// Inject services
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cl
init|=
name|clazz
init|;
name|cl
operator|!=
name|Object
operator|.
name|class
condition|;
name|cl
operator|=
name|cl
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
for|for
control|(
name|Field
name|field
range|:
name|cl
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|Reference
name|ref
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Reference
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|GenericType
name|type
init|=
operator|new
name|GenericType
argument_list|(
name|field
operator|.
name|getGenericType
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|value
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getRawClass
argument_list|()
operator|==
name|List
operator|.
name|class
condition|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|registry
operator|.
name|getServices
argument_list|(
name|type
operator|.
name|getActualTypeArgument
argument_list|(
literal|0
argument_list|)
operator|.
name|getRawClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|registry
operator|!=
name|this
operator|.
name|dependencies
condition|)
block|{
name|set
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|dependencies
operator|.
name|getServices
argument_list|(
name|type
operator|.
name|getActualTypeArgument
argument_list|(
literal|0
argument_list|)
operator|.
name|getRawClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|value
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|registry
operator|.
name|getService
argument_list|(
name|type
operator|.
name|getRawClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|registry
operator|!=
name|this
operator|.
name|dependencies
condition|)
block|{
name|value
operator|=
name|this
operator|.
name|dependencies
operator|.
name|getService
argument_list|(
name|type
operator|.
name|getRawClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|allowCustomServices
operator|&&
name|value
operator|==
literal|null
operator|&&
operator|!
name|ref
operator|.
name|optional
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No service matching "
operator|+
name|field
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
name|instance
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Method
name|method
range|:
name|clazz
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|Init
name|ann
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Init
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
operator|&&
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|void
operator|.
name|class
condition|)
block|{
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|instance
return|;
block|}
specifier|public
name|void
name|release
parameter_list|(
name|Object
name|instance
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|instance
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|allowCustomServices
condition|)
block|{
name|Service
name|reg
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" is not annotated with @Service"
argument_list|)
throw|;
block|}
block|}
for|for
control|(
name|Method
name|method
range|:
name|clazz
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|Destroy
name|ann
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Destroy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
operator|&&
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|void
operator|.
name|class
condition|)
block|{
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
operator|!
name|allowCustomServices
condition|)
block|{
name|Service
name|reg
init|=
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" is not annotated with @Service"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|Action
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
specifier|final
name|Command
name|cmd
init|=
name|clazz
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
literal|"Command "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" is not annotated with @Command"
argument_list|)
throw|;
block|}
name|Object
name|command
init|=
operator|new
name|ActionCommand
argument_list|(
name|this
argument_list|,
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
operator|)
name|clazz
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|instances
init|)
block|{
name|instances
operator|.
name|put
argument_list|(
name|clazz
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
name|registrations
operator|.
name|register
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowCustomServices
operator|||
name|Completer
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|||
name|Parser
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
try|try
block|{
comment|// Create completer
name|Object
name|completer
init|=
name|instantiate
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|instances
init|)
block|{
name|instances
operator|.
name|put
argument_list|(
name|clazz
argument_list|,
name|completer
argument_list|)
expr_stmt|;
block|}
name|registrations
operator|.
name|register
argument_list|(
name|completer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|unregister
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Object
name|object
decl_stmt|;
synchronized|synchronized
init|(
name|instances
init|)
block|{
name|object
operator|=
name|instances
operator|.
name|remove
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|registrations
operator|.
name|unregister
argument_list|(
name|object
argument_list|)
expr_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|Completer
condition|)
block|{
try|try
block|{
name|release
argument_list|(
name|object
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

