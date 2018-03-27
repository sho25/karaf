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
name|validation
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSResourceResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXNotRecognizedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXNotSupportedException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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

begin_class
specifier|public
specifier|abstract
class|class
name|SchemaFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_IMPL
init|=
literal|"com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory"
decl_stmt|;
specifier|protected
name|SchemaFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|SchemaFactory
name|newDefaultInstance
parameter_list|()
block|{
return|return
operator|new
name|$SchemaFactoryFinder
argument_list|(
literal|null
argument_list|)
operator|.
name|createInstance
argument_list|(
name|DEFAULT_IMPL
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SchemaFactory
name|newInstance
parameter_list|(
name|String
name|schemaLanguage
parameter_list|)
block|{
name|ClassLoader
name|cl
init|=
name|$SchemaFactoryFinder
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|==
literal|null
condition|)
block|{
name|cl
operator|=
name|SchemaFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
name|SchemaFactory
name|f
init|=
operator|new
name|$SchemaFactoryFinder
argument_list|(
name|cl
argument_list|)
operator|.
name|newFactory
argument_list|(
name|schemaLanguage
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No SchemaFactory"
operator|+
literal|" that implements the schema language specified by: "
operator|+
name|schemaLanguage
operator|+
literal|" could be loaded"
argument_list|)
throw|;
block|}
return|return
name|f
return|;
block|}
specifier|public
specifier|static
name|SchemaFactory
name|newInstance
parameter_list|(
name|String
name|schemaLanguage
parameter_list|,
name|String
name|factoryClassName
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|ClassLoader
name|cl
init|=
name|classLoader
decl_stmt|;
if|if
condition|(
name|cl
operator|==
literal|null
condition|)
block|{
name|cl
operator|=
name|$SchemaFactoryFinder
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
name|SchemaFactory
name|f
init|=
operator|new
name|$SchemaFactoryFinder
argument_list|(
name|cl
argument_list|)
operator|.
name|createInstance
argument_list|(
name|factoryClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Factory "
operator|+
name|factoryClassName
operator|+
literal|" could not be loaded to implement the schema language specified by: "
operator|+
name|schemaLanguage
argument_list|)
throw|;
block|}
if|if
condition|(
name|f
operator|.
name|isSchemaLanguageSupported
argument_list|(
name|schemaLanguage
argument_list|)
condition|)
block|{
return|return
name|f
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Factory "
operator|+
name|f
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" does not implement the schema language specified by: "
operator|+
name|schemaLanguage
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|abstract
name|boolean
name|isSchemaLanguageSupported
parameter_list|(
name|String
name|schemaLanguage
parameter_list|)
function_decl|;
specifier|public
name|boolean
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"the name parameter is null"
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|SAXNotRecognizedException
argument_list|(
name|name
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"the name parameter is null"
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|SAXNotRecognizedException
argument_list|(
name|name
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"the name parameter is null"
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|SAXNotRecognizedException
argument_list|(
name|name
argument_list|)
throw|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"the name parameter is null"
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|SAXNotRecognizedException
argument_list|(
name|name
argument_list|)
throw|;
block|}
specifier|public
specifier|abstract
name|void
name|setErrorHandler
parameter_list|(
name|ErrorHandler
name|errorHandler
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|ErrorHandler
name|getErrorHandler
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|setResourceResolver
parameter_list|(
name|LSResourceResolver
name|resourceResolver
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|LSResourceResolver
name|getResourceResolver
parameter_list|()
function_decl|;
specifier|public
name|Schema
name|newSchema
parameter_list|(
name|Source
name|schema
parameter_list|)
throws|throws
name|SAXException
block|{
return|return
name|newSchema
argument_list|(
operator|new
name|Source
index|[]
block|{
name|schema
block|}
argument_list|)
return|;
block|}
specifier|public
name|Schema
name|newSchema
parameter_list|(
name|File
name|schema
parameter_list|)
throws|throws
name|SAXException
block|{
return|return
name|newSchema
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|schema
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Schema
name|newSchema
parameter_list|(
name|URL
name|schema
parameter_list|)
throws|throws
name|SAXException
block|{
return|return
name|newSchema
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|schema
operator|.
name|toExternalForm
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|Schema
name|newSchema
parameter_list|(
name|Source
index|[]
name|schemas
parameter_list|)
throws|throws
name|SAXException
function_decl|;
specifier|public
specifier|abstract
name|Schema
name|newSchema
parameter_list|()
throws|throws
name|SAXException
function_decl|;
block|}
end_class

end_unit

