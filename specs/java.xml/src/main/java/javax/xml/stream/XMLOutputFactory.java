begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|javax
operator|.
name|xml
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|XMLOutputFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|IS_REPAIRING_NAMESPACES
init|=
literal|"javax.xml.stream.isRepairingNamespaces"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_IMPL
init|=
literal|"com.sun.xml.internal.stream.XMLOutputFactoryImpl"
decl_stmt|;
specifier|protected
name|XMLOutputFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|XMLOutputFactory
name|newDefaultFactory
parameter_list|()
block|{
return|return
name|$FactoryFinder
operator|.
name|newInstance
argument_list|(
name|XMLOutputFactory
operator|.
name|class
argument_list|,
name|DEFAULT_IMPL
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLOutputFactory
name|newInstance
parameter_list|()
throws|throws
name|FactoryConfigurationError
block|{
return|return
name|$FactoryFinder
operator|.
name|find
argument_list|(
name|XMLOutputFactory
operator|.
name|class
argument_list|,
name|DEFAULT_IMPL
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLOutputFactory
name|newFactory
parameter_list|()
throws|throws
name|FactoryConfigurationError
block|{
return|return
name|$FactoryFinder
operator|.
name|find
argument_list|(
name|XMLOutputFactory
operator|.
name|class
argument_list|,
name|DEFAULT_IMPL
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
specifier|public
specifier|static
name|XMLInputFactory
name|newInstance
parameter_list|(
name|String
name|factoryId
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|FactoryConfigurationError
block|{
return|return
name|$FactoryFinder
operator|.
name|find
argument_list|(
name|XMLInputFactory
operator|.
name|class
argument_list|,
name|factoryId
argument_list|,
name|classLoader
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLOutputFactory
name|newFactory
parameter_list|(
name|String
name|factoryId
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|FactoryConfigurationError
block|{
return|return
name|$FactoryFinder
operator|.
name|find
argument_list|(
name|XMLOutputFactory
operator|.
name|class
argument_list|,
name|factoryId
argument_list|,
name|classLoader
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|Writer
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|OutputStream
name|stream
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLStreamWriter
name|createXMLStreamWriter
parameter_list|(
name|Result
name|result
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|Result
name|result
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|OutputStream
name|stream
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|XMLEventWriter
name|createXMLEventWriter
parameter_list|(
name|Writer
name|stream
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
specifier|abstract
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
specifier|public
specifier|abstract
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
specifier|public
specifier|abstract
name|boolean
name|isPropertySupported
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_class

end_unit

