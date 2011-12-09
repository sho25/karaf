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
name|ByteArrayInputStream
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
name|IOException
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
name|Collections
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|karaf
operator|.
name|tooling
operator|.
name|utils
operator|.
name|MojoSupport
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_comment
comment|/**  * Generates the features XML file  *  * @version $Revision$  * @goal features-add-to-repository  * @phase compile  * @execute phase="compile"  * @requiresDependencyResolution runtime  * @inheritByDefault true  * @description Add the features to the repository  */
end_comment

begin_class
specifier|public
class|class
name|AddToRepositoryMojo
extends|extends
name|MojoSupport
block|{
specifier|private
specifier|final
specifier|static
name|String
name|KARAF_CORE_STANDARD_FEATURE_URL
init|=
literal|"mvn:org.apache.karaf.assemblies.features/standard/%s/xml/features"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|KARAF_CORE_ENTERPRISE_FEATURE_URL
init|=
literal|"mvn:org.apache.karaf.assemblies.features/enterprise/%s/xml/features"
decl_stmt|;
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
comment|/**      * the target karaf version used to resolve Karaf core features descriptors      *      * @parameter      */
specifier|private
name|String
name|karafVersion
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|boolean
name|includeMvnBasedDescriptors
init|=
literal|false
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|List
argument_list|<
name|CopyFileBasedDescriptor
argument_list|>
name|copyFileBasedDescriptors
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|boolean
name|skipNonMavenProtocols
init|=
literal|true
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|boolean
name|failOnArtifactResolutionError
init|=
literal|true
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|boolean
name|resolveDefinedRepositoriesRecursively
init|=
literal|true
decl_stmt|;
comment|/**      * @parameter      */
specifier|private
name|boolean
name|addTransitiveFeatures
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
if|if
condition|(
name|karafVersion
operator|==
literal|null
condition|)
block|{
name|Package
name|p
init|=
name|Package
operator|.
name|getPackage
argument_list|(
literal|"org.apache.karaf.tooling.features"
argument_list|)
decl_stmt|;
name|karafVersion
operator|=
name|p
operator|.
name|getImplementationVersion
argument_list|()
expr_stmt|;
block|}
name|String
name|karafCoreEnterpriseFeatureUrl
init|=
name|String
operator|.
name|format
argument_list|(
name|KARAF_CORE_ENTERPRISE_FEATURE_URL
argument_list|,
name|karafVersion
argument_list|)
decl_stmt|;
name|Artifact
name|enterpriseFeatureDescriptor
init|=
name|resourceToArtifact
argument_list|(
name|karafCoreEnterpriseFeatureUrl
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|enterpriseFeatureDescriptor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|resolveBundle
argument_list|(
name|enterpriseFeatureDescriptor
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
name|descriptors
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|karafCoreEnterpriseFeatureUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Can't add "
operator|+
name|karafCoreEnterpriseFeatureUrl
operator|+
literal|" in the descriptors set"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|karafCoreStandardFeatureUrl
init|=
name|String
operator|.
name|format
argument_list|(
name|KARAF_CORE_STANDARD_FEATURE_URL
argument_list|,
name|karafVersion
argument_list|)
decl_stmt|;
name|Artifact
name|standardFeatureDescriptor
init|=
name|resourceToArtifact
argument_list|(
name|karafCoreStandardFeatureUrl
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|standardFeatureDescriptor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|resolveBundle
argument_list|(
name|standardFeatureDescriptor
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
name|descriptors
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|karafCoreStandardFeatureUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Can't add "
operator|+
name|karafCoreStandardFeatureUrl
operator|+
literal|" in the descriptors set"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
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
name|retrieveDescriptorsRecursively
argument_list|(
name|uri
argument_list|,
name|bundles
argument_list|,
name|featuresMap
argument_list|)
expr_stmt|;
block|}
comment|// no features specified, handle all of them
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|features
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|featuresMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|featuresBundles
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|featuresBundles
argument_list|,
name|transitiveFeatures
argument_list|,
name|featuresMap
argument_list|)
expr_stmt|;
comment|// add the bundles of the configured features to the bundles list
name|bundles
operator|.
name|addAll
argument_list|(
name|featuresBundles
argument_list|)
expr_stmt|;
comment|// if transitive features are enabled we add the contents of those
comment|// features to the bundles list
if|if
condition|(
name|addTransitiveFeatures
condition|)
block|{
for|for
control|(
name|String
name|feature
range|:
name|transitiveFeatures
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Adding contents of transitive feature: "
operator|+
name|feature
argument_list|)
expr_stmt|;
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
comment|// Treat the config files as bundles, since it is only copying
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
block|}
comment|// bundles with explicitely specified remote repos. key -> bundle, value -> remote repo
name|List
argument_list|<
name|Artifact
argument_list|>
name|explicitRepoBundles
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
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
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
name|bundle
argument_list|,
name|skipNonMavenProtocols
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|artifact
operator|.
name|getRepository
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|explicitRepoBundles
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// bundle URL without repository information are resolved now
name|resolveBundle
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
block|}
block|}
comment|// resolving all bundles with explicitly specified remote repository
for|for
control|(
name|Artifact
name|explicitBundle
range|:
name|explicitRepoBundles
control|)
block|{
name|resolveBundle
argument_list|(
name|explicitBundle
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|explicitBundle
operator|.
name|getRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|copyFileBasedDescriptors
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CopyFileBasedDescriptor
name|fileBasedDescriptor
range|:
name|copyFileBasedDescriptors
control|)
block|{
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|fileBasedDescriptor
operator|.
name|getSourceFile
argument_list|()
argument_list|)
argument_list|,
name|repository
argument_list|,
name|fileBasedDescriptor
operator|.
name|getTargetFileName
argument_list|()
argument_list|,
name|fileBasedDescriptor
operator|.
name|getTargetDirectory
argument_list|()
argument_list|,
operator|new
name|byte
index|[
literal|8192
index|]
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
name|retrieveDescriptorsRecursively
parameter_list|(
name|String
name|uri
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|bundles
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featuresMap
parameter_list|)
throws|throws
name|Exception
block|{
comment|// let's ensure a mvn: based url is sitting in the local repo before we try reading it
name|Artifact
name|descriptor
init|=
name|resourceToArtifact
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|!=
literal|null
condition|)
block|{
name|resolveBundle
argument_list|(
name|descriptor
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeMvnBasedDescriptors
condition|)
block|{
name|bundles
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
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
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
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
if|if
condition|(
name|resolveDefinedRepositoriesRecursively
condition|)
block|{
for|for
control|(
name|String
name|r
range|:
name|repo
operator|.
name|getDefinedRepositories
argument_list|()
control|)
block|{
name|retrieveDescriptorsRecursively
argument_list|(
name|r
argument_list|,
name|bundles
argument_list|,
name|featuresMap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// resolves the bundle in question
comment|// TODO neither remoteRepos nor bundle's Repository are used, only the local repo?????
specifier|private
name|void
name|resolveBundle
parameter_list|(
name|Artifact
name|bundle
parameter_list|,
name|List
argument_list|<
name|ArtifactRepository
argument_list|>
name|remoteRepos
parameter_list|)
throws|throws
name|IOException
throws|,
name|MojoFailureException
block|{
comment|// TODO consider DefaultRepositoryLayout
name|String
name|dir
init|=
name|bundle
operator|.
name|getGroupId
argument_list|()
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
name|bundle
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"/"
operator|+
name|bundle
operator|.
name|getBaseVersion
argument_list|()
operator|+
literal|"/"
decl_stmt|;
name|String
name|name
init|=
name|bundle
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|bundle
operator|.
name|getBaseVersion
argument_list|()
operator|+
operator|(
name|bundle
operator|.
name|getClassifier
argument_list|()
operator|!=
literal|null
condition|?
literal|"-"
operator|+
name|bundle
operator|.
name|getClassifier
argument_list|()
else|:
literal|""
operator|)
operator|+
literal|"."
operator|+
name|bundle
operator|.
name|getType
argument_list|()
decl_stmt|;
try|try
block|{
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
name|bundle
argument_list|,
name|remoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|bundle
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
if|if
condition|(
name|failOnArtifactResolutionError
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Can't resolve bundle "
operator|+
name|bundle
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
if|if
condition|(
name|failOnArtifactResolutionError
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Can't resolve bundle "
operator|+
name|bundle
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|featuresBundles
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
comment|// only add the feature to transitives if it is not
comment|// listed in the features list defined by the config
if|if
condition|(
operator|!
name|this
operator|.
name|features
operator|.
name|contains
argument_list|(
name|feature
argument_list|)
condition|)
block|{
name|transitiveFeatures
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add the bundles of the feature to the bundle set
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Adding contents for feature: "
operator|+
name|feature
argument_list|)
expr_stmt|;
name|featuresBundles
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
comment|// Treat the config files as bundles, since it is only copying
name|featuresBundles
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
name|addFeatures
argument_list|(
name|f
operator|.
name|getDependencies
argument_list|()
argument_list|,
name|featuresBundles
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
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|repositories
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
throws|throws
name|Exception
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
throws|throws
name|IOException
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
specifier|private
name|void
name|loadFeatures
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

