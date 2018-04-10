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
name|transform
package|;
end_package

begin_class
specifier|public
specifier|abstract
class|class
name|TransformerFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_IMPL
init|=
literal|"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl"
decl_stmt|;
specifier|protected
name|TransformerFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|TransformerFactory
name|newDefaultInstance
parameter_list|()
block|{
return|return
name|$FactoryFinder
operator|.
name|newInstance
argument_list|(
name|TransformerFactory
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
name|TransformerFactory
name|newInstance
parameter_list|()
throws|throws
name|TransformerFactoryConfigurationError
block|{
return|return
name|$FactoryFinder
operator|.
name|find
argument_list|(
name|TransformerFactory
operator|.
name|class
argument_list|,
name|DEFAULT_IMPL
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|TransformerFactory
name|newInstance
parameter_list|(
name|String
name|factoryClassName
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|TransformerFactoryConfigurationError
block|{
return|return
name|$FactoryFinder
operator|.
name|newInstance
argument_list|(
name|TransformerFactory
operator|.
name|class
argument_list|,
name|factoryClassName
argument_list|,
name|classLoader
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|abstract
name|Transformer
name|newTransformer
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|TransformerConfigurationException
function_decl|;
specifier|public
specifier|abstract
name|Transformer
name|newTransformer
parameter_list|()
throws|throws
name|TransformerConfigurationException
function_decl|;
specifier|public
specifier|abstract
name|Templates
name|newTemplates
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|TransformerConfigurationException
function_decl|;
specifier|public
specifier|abstract
name|Source
name|getAssociatedStylesheet
parameter_list|(
name|Source
name|source
parameter_list|,
name|String
name|media
parameter_list|,
name|String
name|title
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|TransformerConfigurationException
function_decl|;
specifier|public
specifier|abstract
name|void
name|setURIResolver
parameter_list|(
name|URIResolver
name|resolver
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|URIResolver
name|getURIResolver
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
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
name|TransformerConfigurationException
function_decl|;
specifier|public
specifier|abstract
name|boolean
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|setAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|setErrorListener
parameter_list|(
name|ErrorListener
name|listener
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|ErrorListener
name|getErrorListener
parameter_list|()
function_decl|;
block|}
end_class

end_unit
