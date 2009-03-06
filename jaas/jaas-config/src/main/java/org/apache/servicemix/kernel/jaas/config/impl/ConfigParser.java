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
name|servicemix
operator|.
name|kernel
operator|.
name|jaas
operator|.
name|config
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|jaas
operator|.
name|boot
operator|.
name|ProxyLoginModule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|jaas
operator|.
name|config
operator|.
name|JaasRealm
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
name|config
operator|.
name|BeanDefinition
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
name|config
operator|.
name|BeanDefinitionHolder
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
name|parsing
operator|.
name|BeanComponentDefinition
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
name|support
operator|.
name|BeanDefinitionBuilder
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
name|support
operator|.
name|ManagedList
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
name|xml
operator|.
name|AbstractSingleBeanDefinitionParser
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
name|xml
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|service
operator|.
name|exporter
operator|.
name|support
operator|.
name|OsgiServiceFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|xml
operator|.
name|DomUtils
import|;
end_import

begin_class
specifier|public
class|class
name|ConfigParser
extends|extends
name|AbstractSingleBeanDefinitionParser
block|{
specifier|protected
name|Class
name|getBeanClass
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|Config
operator|.
name|class
return|;
block|}
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|,
name|BeanDefinitionBuilder
name|builder
parameter_list|)
block|{
name|String
name|name
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|String
name|rank
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"rank"
argument_list|)
decl_stmt|;
if|if
condition|(
name|rank
operator|!=
literal|null
operator|&&
name|rank
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"rank"
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|rank
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
name|childElements
init|=
name|DomUtils
operator|.
name|getChildElementsByTagName
argument_list|(
name|element
argument_list|,
literal|"module"
argument_list|)
decl_stmt|;
if|if
condition|(
name|childElements
operator|!=
literal|null
operator|&&
name|childElements
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ManagedList
name|children
init|=
operator|new
name|ManagedList
argument_list|(
name|childElements
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|childElements
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|childElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|BeanDefinitionBuilder
name|bd
init|=
name|BeanDefinitionBuilder
operator|.
name|genericBeanDefinition
argument_list|(
name|Module
operator|.
name|class
argument_list|)
decl_stmt|;
name|bd
operator|.
name|addPropertyValue
argument_list|(
literal|"className"
argument_list|,
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"className"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"flags"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|bd
operator|.
name|addPropertyValue
argument_list|(
literal|"flags"
argument_list|,
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"flags"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|options
init|=
name|DomUtils
operator|.
name|getTextValue
argument_list|(
name|childElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
operator|&&
name|options
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|options
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can not load options for JAAS module "
operator|+
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"className"
argument_list|)
operator|+
literal|" in config "
operator|+
name|name
argument_list|)
throw|;
block|}
name|bd
operator|.
name|addPropertyValue
argument_list|(
literal|"options"
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
name|children
operator|.
name|add
argument_list|(
name|bd
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"modules"
argument_list|,
name|children
argument_list|)
expr_stmt|;
block|}
comment|// Publish to OSGi
name|String
name|publish
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"publish"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|publish
argument_list|)
condition|)
block|{
comment|// Publish Config
name|BeanDefinitionBuilder
name|bd
init|=
name|BeanDefinitionBuilder
operator|.
name|genericBeanDefinition
argument_list|(
name|OsgiServiceFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|bd
operator|.
name|addPropertyValue
argument_list|(
literal|"target"
argument_list|,
name|builder
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|bd
operator|.
name|addPropertyValue
argument_list|(
literal|"interfaces"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|JaasRealm
operator|.
name|class
block|}
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|bd
operator|.
name|addPropertyValue
argument_list|(
literal|"serviceProperties"
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|BeanDefinition
name|def
init|=
name|bd
operator|.
name|getBeanDefinition
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|parserContext
operator|.
name|getReaderContext
argument_list|()
operator|.
name|generateBeanName
argument_list|(
name|def
argument_list|)
decl_stmt|;
name|BeanDefinitionHolder
name|holder
init|=
operator|new
name|BeanDefinitionHolder
argument_list|(
name|def
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|registerBeanDefinition
argument_list|(
name|holder
argument_list|,
name|parserContext
operator|.
name|getRegistry
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|shouldFireEvents
argument_list|()
condition|)
block|{
name|BeanComponentDefinition
name|componentDefinition
init|=
operator|new
name|BeanComponentDefinition
argument_list|(
name|holder
argument_list|)
decl_stmt|;
name|postProcessComponentDefinition
argument_list|(
name|componentDefinition
argument_list|)
expr_stmt|;
name|parserContext
operator|.
name|registerComponent
argument_list|(
name|componentDefinition
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

