begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
operator|.
name|model
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
name|net
operator|.
name|URI
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

begin_class
specifier|public
class|class
name|Repository
block|{
specifier|private
name|URI
name|uri
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|features
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|repositories
decl_stmt|;
specifier|private
name|Integer
name|defaultStartLevel
decl_stmt|;
specifier|public
name|Repository
parameter_list|(
name|URI
name|uri
parameter_list|,
name|Integer
name|defaultStartLevel
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|defaultStartLevel
operator|=
name|defaultStartLevel
expr_stmt|;
block|}
specifier|public
name|URI
name|getURI
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|Feature
index|[]
name|getFeatures
parameter_list|()
block|{
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|loadFeatures
argument_list|()
expr_stmt|;
block|}
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
name|String
index|[]
name|getDefinedRepositories
parameter_list|()
block|{
if|if
condition|(
name|repositories
operator|==
literal|null
condition|)
block|{
name|loadRepositories
argument_list|()
expr_stmt|;
block|}
return|return
name|repositories
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|repositories
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|void
name|loadRepositories
parameter_list|()
block|{
try|try
block|{
name|repositories
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
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
name|uri
operator|.
name|toURL
argument_list|()
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
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
name|node
init|=
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|node
operator|instanceof
name|Element
operator|)
operator|||
operator|!
literal|"repository"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
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
name|repositories
operator|.
name|add
argument_list|(
name|e
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"Error loading feature descriptors from "
operator|+
name|this
operator|.
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|loadFeatures
parameter_list|()
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
name|uri
operator|.
name|toURL
argument_list|()
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
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
name|node
init|=
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|node
operator|instanceof
name|Element
operator|)
operator|||
operator|!
literal|"feature"
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
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
name|String
name|version
init|=
name|e
operator|.
name|getAttribute
argument_list|(
literal|"version"
argument_list|)
decl_stmt|;
name|Feature
name|f
init|=
operator|new
name|Feature
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|f
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
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
name|configFileNodes
init|=
name|e
operator|.
name|getElementsByTagName
argument_list|(
literal|"configfile"
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
name|configFileNodes
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
name|configFileNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|f
operator|.
name|addConfigFile
argument_list|(
operator|new
name|ConfigFileRef
argument_list|(
name|c
operator|.
name|getTextContent
argument_list|()
argument_list|)
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
name|Integer
name|startLevel
init|=
name|getInt
argument_list|(
name|b
argument_list|,
literal|"start-level"
argument_list|,
name|defaultStartLevel
argument_list|)
decl_stmt|;
name|f
operator|.
name|addBundle
argument_list|(
operator|new
name|BundleRef
argument_list|(
name|b
operator|.
name|getTextContent
argument_list|()
argument_list|,
name|startLevel
argument_list|)
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
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error loading features for "
operator|+
name|this
operator|.
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Integer
name|getInt
parameter_list|(
name|Element
name|el
parameter_list|,
name|String
name|key
parameter_list|,
name|Integer
name|defaultValue
parameter_list|)
block|{
name|Integer
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|el
operator|.
name|getAttribute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
return|return
operator|(
name|value
operator|==
literal|null
operator|||
name|value
operator|==
literal|0
operator|)
condition|?
name|defaultValue
else|:
name|value
return|;
block|}
block|}
end_class

end_unit

