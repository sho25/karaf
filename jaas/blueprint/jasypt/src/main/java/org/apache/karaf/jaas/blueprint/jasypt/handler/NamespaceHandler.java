begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|jasypt
operator|.
name|handler
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
name|List
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
name|ext
operator|.
name|PlaceholdersUtils
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
name|MutableValueMetadata
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
name|BeanMetadata
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
name|CollectionMetadata
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
specifier|static
specifier|final
name|String
name|ID_ATTRIBUTE
init|=
literal|"id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PLACEHOLDER_PREFIX_ATTRIBUTE
init|=
literal|"placeholder-prefix"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PLACEHOLDER_SUFFIX_ATTRIBUTE
init|=
literal|"placeholder-suffix"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_PLACEHOLDER_ELEMENT
init|=
literal|"property-placeholder"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPTOR_REF_ATTRIBUTE
init|=
literal|"encryptor-ref"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPTOR_ELEMENT
init|=
literal|"encryptor"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JASYPT_NAMESPACE_1_0
init|=
literal|"http://karaf.apache.org/xmlns/jasypt/v1.0.0"
decl_stmt|;
specifier|private
name|int
name|idCounter
decl_stmt|;
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
literal|"http://karaf.apache.org/xmlns/jasypt/v1.0.0"
case|:
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/karaf/jaas/blueprint/jasypt/handler/karaf-jasypt-1.0.0.xsd"
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
name|EncryptablePropertyPlaceholder
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
name|PROPERTY_PLACEHOLDER_ELEMENT
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|parsePropertyPlaceholder
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
name|componentMetadata
parameter_list|,
name|ParserContext
name|parserContext
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
name|parsePropertyPlaceholder
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|metadata
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
name|metadata
operator|.
name|setProcessor
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|getId
argument_list|(
name|context
argument_list|,
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setScope
argument_list|(
name|BeanMetadata
operator|.
name|SCOPE_SINGLETON
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setRuntimeClass
argument_list|(
name|EncryptablePropertyPlaceholder
operator|.
name|class
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setInitMethod
argument_list|(
literal|"init"
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|element
operator|.
name|hasAttribute
argument_list|(
name|PLACEHOLDER_PREFIX_ATTRIBUTE
argument_list|)
condition|?
name|element
operator|.
name|getAttribute
argument_list|(
name|PLACEHOLDER_PREFIX_ATTRIBUTE
argument_list|)
else|:
literal|"ENC("
decl_stmt|;
name|metadata
operator|.
name|addProperty
argument_list|(
literal|"placeholderPrefix"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|prefix
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|suffix
init|=
name|element
operator|.
name|hasAttribute
argument_list|(
name|PLACEHOLDER_SUFFIX_ATTRIBUTE
argument_list|)
condition|?
name|element
operator|.
name|getAttribute
argument_list|(
name|PLACEHOLDER_SUFFIX_ATTRIBUTE
argument_list|)
else|:
literal|")"
decl_stmt|;
name|metadata
operator|.
name|addProperty
argument_list|(
literal|"placeholderSuffix"
argument_list|,
name|createValue
argument_list|(
name|context
argument_list|,
name|suffix
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|encryptorRef
init|=
name|element
operator|.
name|hasAttribute
argument_list|(
literal|"encryptor-ref"
argument_list|)
condition|?
name|element
operator|.
name|getAttribute
argument_list|(
literal|"encryptor-ref"
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|encryptorRef
operator|!=
literal|null
condition|)
block|{
name|metadata
operator|.
name|addProperty
argument_list|(
literal|"encryptor"
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
name|encryptorRef
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|node
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
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|e
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|JASYPT_NAMESPACE_1_0
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|e
operator|.
name|getLocalName
argument_list|()
operator|!=
literal|null
condition|?
name|e
operator|.
name|getLocalName
argument_list|()
else|:
name|e
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ENCRYPTOR_ELEMENT
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|encryptorRef
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ComponentDefinitionException
argument_list|(
literal|"Only one of "
operator|+
name|ENCRYPTOR_REF_ATTRIBUTE
operator|+
literal|" attribute or "
operator|+
name|ENCRYPTOR_ELEMENT
operator|+
literal|" element is allowed"
argument_list|)
throw|;
block|}
name|BeanMetadata
name|encryptor
init|=
name|context
operator|.
name|parseElement
argument_list|(
name|BeanMetadata
operator|.
name|class
argument_list|,
name|metadata
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addProperty
argument_list|(
literal|"encryptor"
argument_list|,
name|encryptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|PlaceholdersUtils
operator|.
name|validatePlaceholder
argument_list|(
name|metadata
argument_list|,
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|metadata
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
if|if
condition|(
name|element
operator|.
name|hasAttribute
argument_list|(
name|ID_ATTRIBUTE
argument_list|)
condition|)
block|{
return|return
name|element
operator|.
name|getAttribute
argument_list|(
name|ID_ATTRIBUTE
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|generateId
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|generateId
parameter_list|(
name|ParserContext
name|context
parameter_list|)
block|{
name|String
name|id
decl_stmt|;
do|do
block|{
name|id
operator|=
literal|".jaas-"
operator|+
operator|++
name|idCounter
expr_stmt|;
block|}
do|while
condition|(
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
operator|.
name|containsComponentDefinition
argument_list|(
name|id
argument_list|)
condition|)
do|;
return|return
name|id
return|;
block|}
specifier|private
specifier|static
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
return|return
name|createValue
argument_list|(
name|context
argument_list|,
name|value
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ValueMetadata
name|createValue
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|MutableValueMetadata
name|m
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
name|m
operator|.
name|setStringValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|m
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
specifier|private
specifier|static
name|CollectionMetadata
name|createList
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|MutableCollectionMetadata
name|m
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
name|m
operator|.
name|setCollectionClass
argument_list|(
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
name|m
operator|.
name|setValueType
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|v
range|:
name|list
control|)
block|{
name|m
operator|.
name|addValue
argument_list|(
name|createValue
argument_list|(
name|context
argument_list|,
name|v
argument_list|,
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|m
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

