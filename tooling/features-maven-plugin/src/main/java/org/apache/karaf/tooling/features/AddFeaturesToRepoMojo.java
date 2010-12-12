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
package|;
end_package

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
name|ArrayList
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
name|HashMap
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
name|Set
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
name|IOException
import|;
end_import

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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
name|FileInputStream
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|resolver
operator|.
name|ArtifactNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|resolver
operator|.
name|ArtifactResolutionException
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
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
comment|/**  * Generates the features XML file  *  * @version $Revision: 1.1 $  * @goal add-features-to-repo  * @phase compile  * @execute phase="compile"  * @requiresDependencyResolution runtime  * @inheritByDefault true  * @description Add the features to the repository  */
end_comment

begin_class
specifier|public
class|class
name|AddFeaturesToRepoMojo
extends|extends
name|MojoSupport
block|{
comment|/**      * @parameter      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|descriptors
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|features
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|File
name|repository
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|boolean
name|skipNonMavenProtocols
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featuresMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|descriptors
control|)
block|{
name|Repository
name|repo
init|=
operator|new
name|Repository
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|translateFromMaven
argument_list|(
name|uri
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
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
name|featuresMap
operator|.
name|put
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|transitiveFeatures
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|addFeatures
argument_list|(
name|features
argument_list|,
name|transitiveFeatures
argument_list|,
name|featuresMap
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|bundles
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|feature
range|:
name|transitiveFeatures
control|)
block|{
name|bundles
operator|.
name|addAll
argument_list|(
name|featuresMap
operator|.
name|get
argument_list|(
name|feature
argument_list|)
operator|.
name|getBundles
argument_list|()
argument_list|)
expr_stmt|;
comment|//Treat the config files as bundles, since it is only copying
name|bundles
operator|.
name|addAll
argument_list|(
name|featuresMap
operator|.
name|get
argument_list|(
name|feature
argument_list|)
operator|.
name|getConfigFiles
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Base repo: "
operator|+
name|localRepo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|bundle
range|:
name|bundles
control|)
block|{
specifier|final
name|int
name|index
init|=
name|bundle
operator|.
name|indexOf
argument_list|(
literal|"mvn:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
if|if
condition|(
name|skipNonMavenProtocols
condition|)
block|{
continue|continue;
block|}
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Bundle url is not a maven url: "
operator|+
name|bundle
argument_list|)
throw|;
block|}
else|else
block|{
name|bundle
operator|=
name|bundle
operator|.
name|substring
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
comment|// Truncate the URL when a '#', a '?' or a '$' is encountered
specifier|final
name|int
name|index1
init|=
name|bundle
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
specifier|final
name|int
name|index2
init|=
name|bundle
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
decl_stmt|;
name|int
name|endIndex
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|index1
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|index2
operator|>
literal|0
condition|)
block|{
name|endIndex
operator|=
name|Math
operator|.
name|min
argument_list|(
name|index1
argument_list|,
name|index2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endIndex
operator|=
name|index1
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|index2
operator|>
literal|0
condition|)
block|{
name|endIndex
operator|=
name|index2
expr_stmt|;
block|}
if|if
condition|(
name|endIndex
operator|>=
literal|0
condition|)
block|{
name|bundle
operator|=
name|bundle
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endIndex
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|index3
init|=
name|bundle
operator|.
name|indexOf
argument_list|(
literal|'$'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index3
operator|>
literal|0
condition|)
block|{
name|bundle
operator|=
name|bundle
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index3
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|parts
init|=
name|bundle
operator|.
name|substring
argument_list|(
literal|"mvn:"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|groupId
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|String
name|artifactId
init|=
name|parts
index|[
literal|1
index|]
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
name|version
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|3
condition|)
block|{
name|type
operator|=
name|parts
index|[
literal|3
index|]
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|4
condition|)
block|{
name|classifier
operator|=
name|parts
index|[
literal|4
index|]
expr_stmt|;
block|}
block|}
block|}
name|String
name|dir
init|=
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|version
operator|+
literal|"/"
decl_stmt|;
name|String
name|name
init|=
name|artifactId
operator|+
literal|"-"
operator|+
name|version
operator|+
operator|(
name|classifier
operator|!=
literal|null
condition|?
literal|"-"
operator|+
name|classifier
else|:
literal|""
operator|)
operator|+
literal|"."
operator|+
name|type
decl_stmt|;
name|Artifact
name|artifact
decl_stmt|;
try|try
block|{
name|artifact
operator|=
name|this
operator|.
name|factory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Copying bundle: "
operator|+
name|bundle
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|this
operator|.
name|remoteRepos
argument_list|,
name|this
operator|.
name|localRepo
argument_list|)
expr_stmt|;
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
argument_list|,
name|repository
argument_list|,
name|name
argument_list|,
name|dir
argument_list|,
operator|new
name|byte
index|[
literal|8192
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Can't resolve bundle "
operator|+
name|bundle
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactNotFoundException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Can't resolve bundle "
operator|+
name|bundle
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|MojoFailureException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error populating repository"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|addFeatures
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|transitiveFeatures
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featuresMap
parameter_list|)
block|{
for|for
control|(
name|String
name|feature
range|:
name|features
control|)
block|{
name|Feature
name|f
init|=
name|featuresMap
operator|.
name|get
argument_list|(
name|feature
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
literal|"Unable to find the feature '"
operator|+
name|feature
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|transitiveFeatures
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|addFeatures
argument_list|(
name|f
operator|.
name|getDependencies
argument_list|()
argument_list|,
name|transitiveFeatures
argument_list|,
name|featuresMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|File
name|dir
parameter_list|,
name|String
name|destName
parameter_list|,
name|String
name|destDir
parameter_list|,
name|byte
index|[]
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|destDir
operator|==
literal|null
condition|)
block|{
name|destDir
operator|=
literal|""
expr_stmt|;
block|}
comment|// Make sure the target directory exists and
comment|// that is actually a directory.
name|File
name|targetDir
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|destDir
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|targetDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|targetDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to create target directory: "
operator|+
name|targetDir
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|targetDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Target is not a directory: "
operator|+
name|targetDir
argument_list|)
throw|;
block|}
name|BufferedOutputStream
name|bos
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
name|destName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|count
operator|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|bos
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|Feature
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bundles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|configs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|configFiles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Feature
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBundles
parameter_list|()
block|{
return|return
name|bundles
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configs
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConfigFiles
parameter_list|()
block|{
return|return
name|configFiles
return|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|String
name|dependency
parameter_list|)
block|{
name|dependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addBundle
parameter_list|(
name|String
name|bundle
parameter_list|)
block|{
name|bundles
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfig
parameter_list|(
name|String
name|name
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|configs
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfigFile
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|configFiles
operator|.
name|add
argument_list|(
name|configFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
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
specifier|public
name|Repository
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
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
throws|throws
name|Exception
block|{
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|load
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
name|Feature
name|f
init|=
operator|new
name|Feature
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
name|c
operator|.
name|getTextContent
argument_list|()
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
block|}
end_class

end_unit

