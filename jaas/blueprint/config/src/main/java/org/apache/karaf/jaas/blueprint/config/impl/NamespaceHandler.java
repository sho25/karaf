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
name|jaas
operator|.
name|blueprint
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
name|net
operator|.
name|URL
import|;
end_import

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
name|HashSet
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
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableBeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableCollectionMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableRefMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableServiceMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableValueMetadata
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
name|karaf
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
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|config
operator|.
name|KeystoreInstance
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
name|jaas
operator|.
name|config
operator|.
name|impl
operator|.
name|Config
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
name|jaas
operator|.
name|config
operator|.
name|impl
operator|.
name|Module
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
name|jaas
operator|.
name|config
operator|.
name|impl
operator|.
name|ResourceKeystoreInstance
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
name|ComponentDefinitionException
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
name|reflect
operator|.
name|ComponentMetadata
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
name|reflect
operator|.
name|Metadata
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
name|reflect
operator|.
name|RefMetadata
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
name|reflect
operator|.
name|ValueMetadata
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
name|CharacterData
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
name|Comment
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
name|w3c
operator|.
name|dom
operator|.
name|EntityReference
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
name|Node
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
name|NodeList
import|;
end_import

begin_class
specifier|public
class|class
name|NamespaceHandler
implements|implements
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|NamespaceHandler
block|{
specifier|public
name|URL
name|getSchemaLocation
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
switch|switch
condition|(
name|namespace
condition|)
block|{
case|case
literal|"http://karaf.apache.org/xmlns/jaas/v1.0.0"
case|:
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/karaf/jaas/blueprint/config/karaf-jaas-1.0.0.xsd"
argument_list|)
return|;
case|case
literal|"http://karaf.apache.org/xmlns/jaas/v1.1.0"
case|:
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/karaf/jaas/blueprint/config/karaf-jaas-1.1.0.xsd"
argument_list|)
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|Set
argument_list|<
name|Class
argument_list|>
name|getManagedClasses
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Config
operator|.
name|class
argument_list|,
name|ResourceKeystoreInstance
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|String
name|name
init|=
name|element
operator|.
name|getLocalName
argument_list|()
operator|!=
literal|null
condition|?
name|element
operator|.
name|getLocalName
argument_list|()
else|:
name|element
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"config"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|parseConfig
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"keystore"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|parseKeystore
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ComponentDefinitionException
argument_list|(
literal|"Bad xml syntax: unknown element '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
throw|;
block|}
specifier|public
name|ComponentMetadata
name|decorate
parameter_list|(
name|Node
name|node
parameter_list|,
name|ComponentMetadata
name|component
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
throw|throw
operator|new
name|ComponentDefinitionException
argument_list|(
literal|"Bad xml syntax: node decoration is not supported"
argument_list|)
throw|;
block|}
specifier|public
name|ComponentMetadata
name|parseConfig
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|bean
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setRuntimeClass
argument_list|(
name|Config
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|bean
operator|.
name|addProperty
argument_list|(
literal|"bundleContext"
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
literal|"blueprintBundleContext"
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|addProperty
argument_list|(
literal|"name"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
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
name|bean
operator|.
name|addProperty
argument_list|(
literal|"rank"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|rank
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|childElements
init|=
name|element
operator|.
name|getElementsByTagNameNS
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
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
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|MutableCollectionMetadata
name|children
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableCollectionMetadata
operator|.
name|class
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
name|getLength
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
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|MutableBeanMetadata
name|md
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|md
operator|.
name|setRuntimeClass
argument_list|(
name|Module
operator|.
name|class
argument_list|)
expr_stmt|;
name|md
operator|.
name|addProperty
argument_list|(
literal|"className"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"className"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|addProperty
argument_list|(
literal|"name"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|md
operator|.
name|addProperty
argument_list|(
literal|"flags"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"flags"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|options
init|=
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
name|md
operator|.
name|addProperty
argument_list|(
literal|"options"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|options
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|children
operator|.
name|addValue
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|addProperty
argument_list|(
literal|"modules"
argument_list|,
name|children
argument_list|)
expr_stmt|;
block|}
comment|// Publish Config
name|MutableServiceMetadata
name|service
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableServiceMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|.
name|setId
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|service
operator|.
name|setServiceComponent
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|service
operator|.
name|addInterface
argument_list|(
name|JaasRealm
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|addServiceProperty
argument_list|(
name|createValue
argument_list|(
name|context
argument_list|,
name|ProxyLoginModule
operator|.
name|PROPERTY_MODULE
argument_list|)
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
specifier|public
name|ComponentMetadata
name|parseKeystore
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|bean
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableBeanMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setRuntimeClass
argument_list|(
name|ResourceKeystoreInstance
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Parse name
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
name|bean
operator|.
name|addProperty
argument_list|(
literal|"name"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
comment|// Parse rank
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
name|bean
operator|.
name|addProperty
argument_list|(
literal|"rank"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|rank
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Parse path
name|String
name|path
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"path"
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
literal|"path"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Parse keystorePassword
name|String
name|keystorePassword
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"keystorePassword"
argument_list|)
decl_stmt|;
if|if
condition|(
name|keystorePassword
operator|!=
literal|null
operator|&&
name|keystorePassword
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
literal|"keystorePassword"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|keystorePassword
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Parse keyPasswords
name|String
name|keyPasswords
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"keyPasswords"
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyPasswords
operator|!=
literal|null
operator|&&
name|keyPasswords
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bean
operator|.
name|addProperty
argument_list|(
literal|"keyPasswords"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|keyPasswords
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Publish Config
name|MutableServiceMetadata
name|service
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableServiceMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|.
name|setId
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|service
operator|.
name|setServiceComponent
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|service
operator|.
name|addInterface
argument_list|(
name|KeystoreInstance
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
specifier|private
name|ValueMetadata
name|createValue
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MutableValueMetadata
name|v
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableValueMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|v
operator|.
name|setStringValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|v
return|;
block|}
specifier|private
name|RefMetadata
name|createRef
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MutableRefMetadata
name|r
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableRefMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|r
operator|.
name|setComponentId
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|private
specifier|static
name|String
name|getTextValue
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|StringBuilder
name|value
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|NodeList
name|nl
init|=
name|element
operator|.
name|getChildNodes
argument_list|()
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
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|item
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|item
operator|instanceof
name|CharacterData
operator|&&
operator|!
operator|(
name|item
operator|instanceof
name|Comment
operator|)
operator|)
operator|||
name|item
operator|instanceof
name|EntityReference
condition|)
block|{
name|value
operator|.
name|append
argument_list|(
name|item
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

