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
name|deployer
operator|.
name|features
package|;
end_package

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
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|net
operator|.
name|URISyntaxException
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
name|*
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
name|DocumentBuilder
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
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactUrlTransformer
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
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
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
name|features
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleListener
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
name|SAXParseException
import|;
end_import

begin_comment
comment|/**  * A deployment listener able to hot deploy a feature descriptor  */
end_comment

begin_class
specifier|public
class|class
name|FeatureDeploymentListener
implements|implements
name|ArtifactUrlTransformer
implements|,
name|BundleListener
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FEATURE_PATH
init|=
literal|"org.apache.karaf.shell.features"
decl_stmt|;
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FeatureDeploymentListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DocumentBuilderFactory
name|dbf
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
specifier|public
name|FeaturesService
name|getFeaturesService
parameter_list|()
block|{
return|return
name|featuresService
return|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|RESOLVED
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|STARTING
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|ACTIVE
condition|)
name|bundleChanged
argument_list|(
operator|new
name|BundleEvent
argument_list|(
name|BundleEvent
operator|.
name|RESOLVED
argument_list|,
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|File
name|artifact
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|artifact
operator|.
name|isFile
argument_list|()
operator|&&
name|artifact
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|parse
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|uri
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to parse deployed file "
operator|+
name|artifact
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|URL
name|transform
parameter_list|(
name|URL
name|artifact
parameter_list|)
block|{
comment|// We can't really install the feature right now and just return nothing.
comment|// We would not be aware of the fact that the bundle has been uninstalled
comment|// and therefore require the feature to be uninstalled.
comment|// So instead, create a fake bundle with the file inside, which will be listened by
comment|// this deployer: installation / uninstallation of the feature will be done
comment|// while the bundle is installed / uninstalled.
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"feature"
argument_list|,
literal|null
argument_list|,
name|artifact
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to build feature bundle"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|bundleEvent
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|bundleEvent
operator|.
name|getBundle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundleEvent
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|RESOLVED
condition|)
block|{
try|try
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|featuresUrlEnumeration
init|=
name|bundle
operator|.
name|findEntries
argument_list|(
literal|"/META-INF/"
operator|+
name|FEATURE_PATH
operator|+
literal|"/"
argument_list|,
literal|"*.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
while|while
condition|(
name|featuresUrlEnumeration
operator|!=
literal|null
operator|&&
name|featuresUrlEnumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|featuresUrlEnumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
try|try
block|{
name|featuresService
operator|.
name|addRepository
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
if|if
condition|(
name|repo
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
init|=
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|featuresService
operator|.
name|installFeatures
argument_list|(
name|features
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|urls
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to install features"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|File
name|file
init|=
name|bundleContext
operator|.
name|getDataFile
argument_list|(
literal|"FeatureDeploymentListener.cfg"
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|InputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|prefix
init|=
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|"-"
operator|+
name|bundle
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|prefix
operator|+
literal|".count"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|urls
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|urls
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|props
operator|.
name|put
argument_list|(
name|prefix
operator|+
literal|".url."
operator|+
name|i
argument_list|,
name|urls
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|OutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|store
argument_list|(
name|output
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to install deployed features for bundle: "
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" - "
operator|+
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|bundleEvent
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNINSTALLED
condition|)
block|{
try|try
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|File
name|file
init|=
name|bundleContext
operator|.
name|getDataFile
argument_list|(
literal|"FeatureDeploymentListener.cfg"
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|InputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|prefix
init|=
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|"-"
operator|+
name|bundle
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|String
name|countStr
init|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prefix
operator|+
literal|".count"
argument_list|)
decl_stmt|;
if|if
condition|(
name|countStr
operator|!=
literal|null
condition|)
block|{
name|int
name|count
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|countStr
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|prefix
operator|+
literal|".url."
operator|+
name|i
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
try|try
block|{
if|if
condition|(
name|repo
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|Feature
name|f
range|:
name|repo
operator|.
name|getFeatures
argument_list|()
control|)
block|{
try|try
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to uninstall feature: "
operator|+
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to uninstall features: "
operator|+
name|url
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to remove repository: "
operator|+
name|url
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|props
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
name|prefix
operator|+
literal|"."
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|OutputStream
name|output
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|props
operator|.
name|store
argument_list|(
name|output
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to uninstall deployed features for bundle: "
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" - "
operator|+
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Document
name|parse
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|dbf
operator|==
literal|null
condition|)
block|{
name|dbf
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|ErrorHandler
argument_list|()
block|{
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{             }
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{             }
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|exception
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|db
operator|.
name|parse
argument_list|(
name|artifact
argument_list|)
return|;
block|}
block|}
end_class

end_unit

