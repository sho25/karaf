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
name|Collection
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
name|concurrent
operator|.
name|Callable
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
name|CountDownLatch
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
name|TimeUnit
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
name|utils
operator|.
name|extender
operator|.
name|Extension
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
name|utils
operator|.
name|manifest
operator|.
name|Clause
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
name|utils
operator|.
name|manifest
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
name|History
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
name|api
operator|.
name|console
operator|.
name|Session
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
name|SessionFactory
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
name|Terminal
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
name|impl
operator|.
name|action
operator|.
name|command
operator|.
name|ManagerImpl
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
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
name|wiring
operator|.
name|BundleWiring
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

begin_comment
comment|/**  * Commands extension  */
end_comment

begin_class
specifier|public
class|class
name|CommandExtension
implements|implements
name|Extension
implements|,
name|Satisfiable
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CommandExtension
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Bundle
name|bundle
decl_stmt|;
specifier|private
specifier|final
name|ManagerImpl
name|manager
decl_stmt|;
specifier|private
specifier|final
name|Registry
name|registry
decl_stmt|;
specifier|private
specifier|final
name|CountDownLatch
name|started
decl_stmt|;
specifier|private
specifier|final
name|AggregateServiceTracker
name|tracker
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Satisfiable
argument_list|>
name|satisfiables
init|=
operator|new
name|ArrayList
argument_list|<
name|Satisfiable
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CommandExtension
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|Registry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
name|this
operator|.
name|registry
operator|=
operator|new
name|RegistryImpl
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|.
name|register
argument_list|(
name|bundle
operator|.
name|getBundleContext
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|manager
operator|=
operator|new
name|ManagerImpl
argument_list|(
name|this
operator|.
name|registry
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|.
name|register
argument_list|(
name|this
operator|.
name|manager
argument_list|)
expr_stmt|;
name|this
operator|.
name|started
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracker
operator|=
operator|new
name|AggregateServiceTracker
argument_list|(
name|bundle
operator|.
name|getBundleContext
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|found
parameter_list|()
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Registering commands for bundle {}/{}"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Satisfiable
name|s
range|:
name|satisfiables
control|)
block|{
name|s
operator|.
name|found
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|updated
parameter_list|()
block|{
for|for
control|(
name|Satisfiable
name|s
range|:
name|satisfiables
control|)
block|{
name|s
operator|.
name|updated
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|lost
parameter_list|()
block|{
for|for
control|(
name|Satisfiable
name|s
range|:
name|satisfiables
control|)
block|{
name|s
operator|.
name|lost
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|header
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|CommandExtender
operator|.
name|KARAF_COMMANDS
argument_list|)
decl_stmt|;
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseHeader
argument_list|(
name|header
argument_list|)
decl_stmt|;
name|BundleWiring
name|wiring
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
name|String
name|name
init|=
name|clause
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|options
init|=
name|BundleWiring
operator|.
name|LISTRESOURCES_LOCAL
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|options
operator||=
name|BundleWiring
operator|.
name|LISTRESOURCES_RECURSE
expr_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"/"
operator|+
name|name
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|classes
init|=
name|wiring
operator|.
name|listResources
argument_list|(
name|name
argument_list|,
literal|"*.class"
argument_list|,
name|options
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|className
range|:
name|classes
control|)
block|{
name|className
operator|=
name|className
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
operator|.
name|replace
argument_list|(
literal|".class"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|inspectClass
argument_list|(
name|bundle
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|tracker
operator|.
name|isSatisfied
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Command registration delayed for bundle {}/{}. Missing dependencies: {}"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|,
name|tracker
operator|.
name|getMissingServices
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|started
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
try|try
block|{
name|started
operator|.
name|await
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"The wait for bundle being started before destruction has been interrupted."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|inspectClass
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|Exception
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
return|return;
block|}
comment|// Create trackers
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
if|if
condition|(
name|field
operator|.
name|getAnnotation
argument_list|(
name|Reference
operator|.
name|class
argument_list|)
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
name|Class
name|clazzRef
init|=
name|type
operator|.
name|getRawClass
argument_list|()
operator|==
name|List
operator|.
name|class
condition|?
name|type
operator|.
name|getActualTypeArgument
argument_list|(
literal|0
argument_list|)
operator|.
name|getRawClass
argument_list|()
else|:
name|type
operator|.
name|getRawClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|clazzRef
operator|!=
name|BundleContext
operator|.
name|class
operator|&&
name|clazzRef
operator|!=
name|Session
operator|.
name|class
operator|&&
name|clazzRef
operator|!=
name|Terminal
operator|.
name|class
operator|&&
name|clazzRef
operator|!=
name|History
operator|.
name|class
operator|&&
name|clazzRef
operator|!=
name|Registry
operator|.
name|class
operator|&&
name|clazzRef
operator|!=
name|SessionFactory
operator|.
name|class
operator|&&
operator|!
name|registry
operator|.
name|hasService
argument_list|(
name|clazzRef
argument_list|)
condition|)
block|{
name|track
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|satisfiables
operator|.
name|add
argument_list|(
operator|new
name|AutoRegister
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|track
parameter_list|(
specifier|final
name|GenericType
name|type
parameter_list|)
block|{
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
specifier|final
name|Class
name|clazzRef
init|=
name|type
operator|.
name|getActualTypeArgument
argument_list|(
literal|0
argument_list|)
operator|.
name|getRawClass
argument_list|()
decl_stmt|;
name|tracker
operator|.
name|track
argument_list|(
name|clazzRef
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|Callable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|tracker
operator|.
name|getServices
argument_list|(
name|clazzRef
argument_list|)
return|;
block|}
block|}
argument_list|,
name|clazzRef
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Class
name|clazzRef
init|=
name|type
operator|.
name|getRawClass
argument_list|()
decl_stmt|;
name|tracker
operator|.
name|track
argument_list|(
name|clazzRef
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|Callable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|tracker
operator|.
name|getService
argument_list|(
name|clazzRef
argument_list|)
return|;
block|}
block|}
argument_list|,
name|clazzRef
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
class|class
name|AutoRegister
implements|implements
name|Satisfiable
block|{
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
specifier|public
name|AutoRegister
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|found
parameter_list|()
block|{
try|try
block|{
name|manager
operator|.
name|register
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
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
literal|"Unable to create service "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|updated
parameter_list|()
block|{
name|lost
argument_list|()
expr_stmt|;
name|found
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|lost
parameter_list|()
block|{
name|manager
operator|.
name|unregister
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

