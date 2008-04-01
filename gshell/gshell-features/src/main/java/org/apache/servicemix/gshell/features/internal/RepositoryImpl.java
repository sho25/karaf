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
name|gshell
operator|.
name|features
operator|.
name|internal
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|Document
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
name|NodeList
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
name|gshell
operator|.
name|features
operator|.
name|Feature
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
name|gshell
operator|.
name|features
operator|.
name|Repository
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

begin_comment
comment|/**  * The repository implementation.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryImpl
implements|implements
name|Repository
block|{
specifier|private
name|URL
name|url
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|RepositoryImpl
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|Feature
index|[]
name|getFeatures
parameter_list|()
block|{
return|return
name|features
operator|.
name|toArray
argument_list|(
operator|new
name|Feature
index|[
name|features
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|void
name|load
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|features
operator|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
expr_stmt|;
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|NodeList
name|nodes
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"feature"
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
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|e
init|=
operator|(
name|Element
operator|)
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|FeatureImpl
name|f
init|=
operator|new
name|FeatureImpl
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|NodeList
name|featureNodes
init|=
name|e
operator|.
name|getElementsByTagName
argument_list|(
literal|"feature"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|featureNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|b
init|=
operator|(
name|Element
operator|)
name|featureNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|f
operator|.
name|addDependency
argument_list|(
name|b
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|configNodes
init|=
name|e
operator|.
name|getElementsByTagName
argument_list|(
literal|"config"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|configNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|c
init|=
operator|(
name|Element
operator|)
name|configNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|String
name|cfgName
init|=
name|c
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|data
init|=
name|c
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hashtable
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|n
init|=
name|key
operator|.
name|toString
argument_list|()
decl_stmt|;
name|hashtable
operator|.
name|put
argument_list|(
name|n
argument_list|,
name|properties
operator|.
name|getProperty
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|f
operator|.
name|addConfig
argument_list|(
name|cfgName
argument_list|,
name|hashtable
argument_list|)
expr_stmt|;
block|}
name|NodeList
name|bundleNodes
init|=
name|e
operator|.
name|getElementsByTagName
argument_list|(
literal|"bundle"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|bundleNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|b
init|=
operator|(
name|Element
operator|)
name|bundleNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|f
operator|.
name|addBundle
argument_list|(
name|b
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|features
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|()
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|()
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

