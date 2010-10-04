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
name|console
operator|.
name|commands
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
name|List
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
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|*
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
name|MutableIdRefMetadata
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
name|MutableCollectionMetadata
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
name|ID
init|=
literal|"id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION
init|=
literal|"action"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_ID
init|=
literal|"actionId"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND_BUNDLE
init|=
literal|"command-bundle"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMMAND
init|=
literal|"command"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPLETERS
init|=
literal|"completers"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BEAN
init|=
literal|"bean"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REF
init|=
literal|"ref"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NULL
init|=
literal|"null"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLUEPRINT_CONTAINER
init|=
literal|"blueprintContainer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLUEPRINT_CONVERTER
init|=
literal|"blueprintConverter"
decl_stmt|;
specifier|private
name|int
name|nameCounter
init|=
literal|0
decl_stmt|;
specifier|public
name|URL
name|getSchemaLocation
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"karaf-shell.xsd"
argument_list|)
return|;
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
argument_list|<
name|Class
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|BlueprintCommand
operator|.
name|class
argument_list|)
argument_list|)
return|;
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
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|element
argument_list|,
name|COMMAND_BUNDLE
argument_list|)
condition|)
block|{
name|NodeList
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|child
decl_stmt|;
name|parseChildElement
argument_list|(
name|childElement
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected element "
operator|+
name|element
operator|.
name|getNodeName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|parseChildElement
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|element
argument_list|,
name|COMMAND
argument_list|)
condition|)
block|{
name|parseCommand
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|parseCommand
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|command
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
name|command
operator|.
name|setRuntimeClass
argument_list|(
name|BlueprintCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
operator|.
name|addProperty
argument_list|(
name|BLUEPRINT_CONTAINER
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
name|BLUEPRINT_CONTAINER
argument_list|)
argument_list|)
expr_stmt|;
name|command
operator|.
name|addProperty
argument_list|(
name|BLUEPRINT_CONVERTER
argument_list|,
name|createRef
argument_list|(
name|context
argument_list|,
name|BLUEPRINT_CONVERTER
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|element
operator|.
name|getAttribute
argument_list|(
name|NAME
argument_list|)
decl_stmt|;
name|location
operator|=
name|location
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|':'
argument_list|)
expr_stmt|;
name|String
name|scope
decl_stmt|;
name|String
name|function
decl_stmt|;
if|if
condition|(
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|scope
operator|=
name|location
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
expr_stmt|;
name|function
operator|=
name|location
operator|.
name|substring
argument_list|(
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scope
operator|=
literal|""
expr_stmt|;
name|function
operator|=
name|location
expr_stmt|;
block|}
name|NodeList
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|child
decl_stmt|;
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|childElement
argument_list|,
name|ACTION
argument_list|)
condition|)
block|{
name|MutableBeanMetadata
name|action
init|=
name|parseAction
argument_list|(
name|context
argument_list|,
name|command
argument_list|,
name|childElement
argument_list|)
decl_stmt|;
name|action
operator|.
name|setId
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
operator|.
name|registerComponentDefinition
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|command
operator|.
name|addProperty
argument_list|(
name|ACTION_ID
argument_list|,
name|createIdRef
argument_list|(
name|context
argument_list|,
name|action
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|childElement
argument_list|,
name|COMPLETERS
argument_list|)
condition|)
block|{
name|command
operator|.
name|addProperty
argument_list|(
name|COMPLETERS
argument_list|,
name|parseCompleters
argument_list|(
name|context
argument_list|,
name|command
argument_list|,
name|childElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ComponentDefinitionException
argument_list|(
literal|"Bad xml syntax: unknown element '"
operator|+
name|childElement
operator|.
name|getNodeName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
block|}
name|MutableServiceMetadata
name|commandService
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
name|commandService
operator|.
name|setActivation
argument_list|(
name|MutableServiceMetadata
operator|.
name|ACTIVATION_LAZY
argument_list|)
expr_stmt|;
name|commandService
operator|.
name|setId
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//commandService.setAutoExport(ServiceMetadata.AUTO_EXPORT_ALL_CLASSES);
name|commandService
operator|.
name|addInterface
argument_list|(
name|CompletableFunction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|commandService
operator|.
name|addInterface
argument_list|(
name|Function
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|commandService
operator|.
name|setServiceComponent
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|commandService
operator|.
name|addServiceProperty
argument_list|(
name|createStringValue
argument_list|(
name|context
argument_list|,
literal|"osgi.command.scope"
argument_list|)
argument_list|,
name|createStringValue
argument_list|(
name|context
argument_list|,
name|scope
argument_list|)
argument_list|)
expr_stmt|;
name|commandService
operator|.
name|addServiceProperty
argument_list|(
name|createStringValue
argument_list|(
name|context
argument_list|,
literal|"osgi.command.function"
argument_list|)
argument_list|,
name|createStringValue
argument_list|(
name|context
argument_list|,
name|function
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponentDefinitionRegistry
argument_list|()
operator|.
name|registerComponentDefinition
argument_list|(
name|commandService
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MutableBeanMetadata
name|parseAction
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|MutableBeanMetadata
name|action
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
name|action
operator|.
name|setActivation
argument_list|(
name|MutableBeanMetadata
operator|.
name|ACTIVATION_LAZY
argument_list|)
expr_stmt|;
name|action
operator|.
name|setScope
argument_list|(
name|MutableBeanMetadata
operator|.
name|SCOPE_PROTOTYPE
argument_list|)
expr_stmt|;
name|action
operator|.
name|setClassName
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
literal|"class"
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|child
decl_stmt|;
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|childElement
argument_list|,
literal|"argument"
argument_list|)
condition|)
block|{
name|action
operator|.
name|addArgument
argument_list|(
name|context
operator|.
name|parseElement
argument_list|(
name|BeanArgument
operator|.
name|class
argument_list|,
name|enclosingComponent
argument_list|,
name|childElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|childElement
argument_list|,
literal|"property"
argument_list|)
condition|)
block|{
name|action
operator|.
name|addProperty
argument_list|(
name|context
operator|.
name|parseElement
argument_list|(
name|BeanProperty
operator|.
name|class
argument_list|,
name|enclosingComponent
argument_list|,
name|childElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|action
return|;
block|}
specifier|private
name|Metadata
name|parseCompleters
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|ComponentMetadata
name|enclosingComponent
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|MutableCollectionMetadata
name|collection
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
name|collection
operator|.
name|setCollectionClass
argument_list|(
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
name|NodeList
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|Metadata
name|metadata
decl_stmt|;
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|child
argument_list|,
name|REF
argument_list|)
condition|)
block|{
name|metadata
operator|=
name|context
operator|.
name|parseElement
argument_list|(
name|RefMetadata
operator|.
name|class
argument_list|,
name|context
operator|.
name|getEnclosingComponent
argument_list|()
argument_list|,
operator|(
name|Element
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|child
argument_list|,
name|NULL
argument_list|)
condition|)
block|{
name|metadata
operator|=
name|context
operator|.
name|parseElement
argument_list|(
name|NullMetadata
operator|.
name|class
argument_list|,
name|context
operator|.
name|getEnclosingComponent
argument_list|()
argument_list|,
operator|(
name|Element
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeNameEquals
argument_list|(
name|child
argument_list|,
name|BEAN
argument_list|)
condition|)
block|{
name|metadata
operator|=
name|context
operator|.
name|parseElement
argument_list|(
name|BeanMetadata
operator|.
name|class
argument_list|,
name|enclosingComponent
argument_list|,
operator|(
name|Element
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected element "
operator|+
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
throw|;
block|}
name|collection
operator|.
name|addValue
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collection
return|;
block|}
specifier|private
name|ValueMetadata
name|createStringValue
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|str
parameter_list|)
block|{
name|MutableValueMetadata
name|value
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
name|value
operator|.
name|setStringValue
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
name|value
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
name|id
parameter_list|)
block|{
name|MutableRefMetadata
name|idref
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
name|idref
operator|.
name|setComponentId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|idref
return|;
block|}
specifier|private
name|IdRefMetadata
name|createIdRef
parameter_list|(
name|ParserContext
name|context
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|MutableIdRefMetadata
name|idref
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutableIdRefMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|idref
operator|.
name|setComponentId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|idref
return|;
block|}
specifier|public
specifier|synchronized
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"shell-"
operator|+
operator|++
name|nameCounter
return|;
block|}
specifier|private
specifier|static
name|boolean
name|nodeNameEquals
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
operator|||
name|name
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
end_class

end_unit

