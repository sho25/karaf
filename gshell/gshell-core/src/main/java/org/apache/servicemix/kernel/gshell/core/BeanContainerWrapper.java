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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
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
name|Map
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|spring
operator|.
name|BeanContainer
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
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|NoSuchBeanDefinitionException
import|;
end_import

begin_class
specifier|public
class|class
name|BeanContainerWrapper
implements|implements
name|BeanContainer
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|ApplicationContext
name|context
decl_stmt|;
specifier|public
name|BeanContainerWrapper
parameter_list|(
name|ApplicationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|BeanContainer
name|getParent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|context
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
specifier|public
name|void
name|loadBeans
parameter_list|(
name|String
index|[]
name|strings
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBean
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
assert|assert
name|type
operator|!=
literal|null
assert|;
name|log
operator|.
name|trace
argument_list|(
literal|"Getting bean of type: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|String
index|[]
name|names
init|=
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanDefinitionException
argument_list|(
name|type
argument_list|,
literal|"No bean defined for type: "
operator|+
name|type
argument_list|)
throw|;
block|}
if|if
condition|(
name|names
operator|.
name|length
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanDefinitionException
argument_list|(
name|type
argument_list|,
literal|"No unique bean defined for type: "
operator|+
name|type
operator|+
literal|", found matches: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBean
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|requiredType
parameter_list|)
block|{
assert|assert
name|name
operator|!=
literal|null
assert|;
assert|assert
name|requiredType
operator|!=
literal|null
assert|;
name|log
operator|.
name|trace
argument_list|(
literal|"Getting bean named '{}' of type: {}"
argument_list|,
name|name
argument_list|,
name|requiredType
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|context
operator|.
name|getBean
argument_list|(
name|name
argument_list|,
name|requiredType
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|getBeans
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
assert|assert
name|type
operator|!=
literal|null
assert|;
name|log
operator|.
name|trace
argument_list|(
literal|"Getting beans of type: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
operator|)
name|context
operator|.
name|getBeansOfType
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getBeanNames
parameter_list|()
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Getting bean names"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getBeanDefinitionNames
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getBeanNames
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
assert|assert
name|type
operator|!=
literal|null
assert|;
name|log
operator|.
name|trace
argument_list|(
literal|"Getting bean names of type: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|BeanContainer
name|createChild
parameter_list|(
name|Collection
argument_list|<
name|URL
argument_list|>
name|urls
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|BeanContainer
name|createChild
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

