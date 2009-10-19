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
name|felix
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
operator|.
name|ManifestUtils
operator|.
name|getExports
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
operator|.
name|ManifestUtils
operator|.
name|getMandatoryImports
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
operator|.
name|ManifestUtils
operator|.
name|matches
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
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
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipFile
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
name|felix
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
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|RepositoryImpl
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
name|repository
operator|.
name|DefaultArtifactRepository
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
name|layout
operator|.
name|DefaultRepositoryLayout
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
name|artifact
operator|.
name|resolver
operator|.
name|DefaultArtifactCollector
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
name|filter
operator|.
name|ArtifactFilter
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyNode
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilder
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|traversal
operator|.
name|DependencyNodeVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|impl
operator|.
name|bundle
operator|.
name|obr
operator|.
name|resource
operator|.
name|Manifest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|impl
operator|.
name|bundle
operator|.
name|obr
operator|.
name|resource
operator|.
name|ManifestEntry
import|;
end_import

begin_comment
comment|/**  * Validates a features XML file  *   * @version $Revision: 1.1 $  * @goal validate  * @execute phase="process-resources"  * @requiresDependencyResolution runtime  * @inheritByDefault true  * @description Validates the features XML file  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
class|class
name|ValidateFeaturesMojo
extends|extends
name|MojoSupport
block|{
specifier|private
specifier|static
specifier|final
name|String
name|MVN_URI_PREFIX
init|=
literal|"mvn:"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MVN_REPO_SEPARATOR
init|=
literal|"!"
decl_stmt|;
comment|/**      * The dependency tree builder to use.      *      * @component      * @required      * @readonly      */
specifier|private
name|DependencyTreeBuilder
name|dependencyTreeBuilder
decl_stmt|;
comment|/**      * The file to generate      *       * @parameter default-value="${project.build.directory}/classes/features.xml"      */
specifier|private
name|File
name|file
decl_stmt|;
comment|/*      * A map to cache the mvn: uris and the artifacts that correspond with them      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Artifact
argument_list|>
name|bundles
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
comment|/*      * A map to cache manifests that have been extracted from the bundles      */
specifier|private
name|Map
argument_list|<
name|Artifact
argument_list|,
name|Manifest
argument_list|>
name|manifests
init|=
operator|new
name|HashMap
argument_list|<
name|Artifact
argument_list|,
name|Manifest
argument_list|>
argument_list|()
decl_stmt|;
comment|/*      * The list of features, includes both the features to be validated and the features from included<repository>s      */
specifier|private
name|Features
name|features
init|=
operator|new
name|Features
argument_list|()
decl_stmt|;
comment|/*      * The packages exported by the features themselves -- useful when features depend on other features      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ManifestEntry
argument_list|>
argument_list|>
name|featureExports
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ManifestEntry
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/*      * The set of packages exported by the system bundle and by Karaf itself      */
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|systemExports
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * The Mojo's main method      */
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
name|prepare
argument_list|()
expr_stmt|;
name|Repository
name|repository
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|analyze
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unable to validate %s: %s"
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*      * Prepare for validation by determing system and Karaf exports      */
specifier|private
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|info
argument_list|(
literal|"== Preparing for validation =="
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|" - getting list of system bundle exports"
argument_list|)
expr_stmt|;
name|readSystemPackages
argument_list|()
expr_stmt|;
name|info
argument_list|(
literal|" - getting list of provided bundle exports"
argument_list|)
expr_stmt|;
name|readProvidedBundles
argument_list|()
expr_stmt|;
block|}
comment|/*      * Analyse the descriptor and any<repository>s that might be part of it      */
specifier|private
name|void
name|analyze
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|Exception
block|{
name|info
argument_list|(
literal|"== Analyzing feature descriptor =="
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|" - read %s"
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|features
operator|.
name|add
argument_list|(
name|repository
operator|.
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|URI
name|uri
range|:
name|repository
operator|.
name|getRepositories
argument_list|()
control|)
block|{
name|Artifact
name|artifact
init|=
name|resolve
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Repository
name|dependency
init|=
operator|new
name|RepositoryImpl
argument_list|(
operator|new
name|File
argument_list|(
name|localRepo
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|localRepo
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|" - adding %d known features from %s"
argument_list|,
name|dependency
operator|.
name|getFeatures
argument_list|()
operator|.
name|length
argument_list|,
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|features
operator|.
name|add
argument_list|(
name|dependency
operator|.
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
comment|// we need to do this to get all the information ready for further processing
name|validateBundlesAvailable
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|analyzeExports
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Perform the actual validation      */
specifier|private
name|void
name|validate
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|Exception
block|{
name|info
argument_list|(
literal|"== Validating feature descriptor =="
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|" - validating %d features"
argument_list|,
name|repository
operator|.
name|getFeatures
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|" - step 1: Checking if all artifacts exist"
argument_list|)
expr_stmt|;
name|validateBundlesAvailable
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|"    OK: all %d OSGi bundles have been found"
argument_list|,
name|bundles
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|" - step 2: Checking if all imports for bundles can be resolved"
argument_list|)
expr_stmt|;
name|validateImportsExports
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|"== Done! =========================="
argument_list|)
expr_stmt|;
block|}
comment|/*      * Determine list of exports by bundles that have been marked provided in the pom      * //TODO: we probably want to figure this out somewhere from the Karaf build itself instead of putting the burden on the user      */
specifier|private
name|void
name|readProvidedBundles
parameter_list|()
throws|throws
name|Exception
block|{
name|DependencyNode
name|tree
init|=
name|dependencyTreeBuilder
operator|.
name|buildDependencyTree
argument_list|(
name|project
argument_list|,
name|localRepo
argument_list|,
name|factory
argument_list|,
name|artifactMetadataSource
argument_list|,
operator|new
name|ArtifactFilter
argument_list|()
block|{
specifier|public
name|boolean
name|include
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|,
operator|new
name|DefaultArtifactCollector
argument_list|()
argument_list|)
decl_stmt|;
name|tree
operator|.
name|accept
argument_list|(
operator|new
name|DependencyNodeVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|endVisit
parameter_list|(
name|DependencyNode
name|node
parameter_list|)
block|{
comment|// we want the next sibling too
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visit
parameter_list|(
name|DependencyNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getState
argument_list|()
operator|!=
name|DependencyNode
operator|.
name|OMITTED_FOR_CONFLICT
condition|)
block|{
name|Artifact
name|artifact
init|=
name|node
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
name|info
argument_list|(
literal|"    scanning %s for exports"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
if|if
condition|(
name|Artifact
operator|.
name|SCOPE_PROVIDED
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getScope
argument_list|()
argument_list|)
operator|&&
operator|!
name|artifact
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"pom"
argument_list|)
condition|)
block|{
try|try
block|{
for|for
control|(
name|ManifestEntry
name|entry
range|:
name|ManifestUtils
operator|.
name|getExports
argument_list|(
name|getManifest
argument_list|(
name|artifact
argument_list|)
argument_list|)
control|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|" adding "
operator|+
name|entry
operator|.
name|getName
argument_list|()
operator|+
literal|" to list of available packages"
argument_list|)
expr_stmt|;
name|systemExports
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|e
parameter_list|)
block|{
name|error
argument_list|(
literal|"Unable to find bundle exports for %s: %s"
argument_list|,
name|e
argument_list|,
name|artifact
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactNotFoundException
name|e
parameter_list|)
block|{
name|error
argument_list|(
literal|"Unable to find bundle exports for %s: %s"
argument_list|,
name|e
argument_list|,
name|artifact
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|error
argument_list|(
literal|"Unable to find bundle exports for %s: %s"
argument_list|,
name|e
argument_list|,
name|artifact
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// we want the children too
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/*      * Read system packages from a properties file      * //TODO: we should probably grab this file from the Karaf distro itself instead of duplicating it in the plugin      */
specifier|private
name|void
name|readSystemPackages
parameter_list|()
throws|throws
name|IOException
block|{
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
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"config.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|packages
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"jre-1.5"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packages
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
control|)
block|{
name|systemExports
operator|.
name|add
argument_list|(
name|pkg
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Analyze exports in all features in the repository without validating the features      * (e.g. used for<repository> elements found in a descriptor)      */
specifier|private
name|void
name|analyzeExports
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|ManifestEntry
argument_list|>
name|exports
init|=
operator|new
name|HashSet
argument_list|<
name|ManifestEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|bundle
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|exports
operator|.
name|addAll
argument_list|(
name|getExports
argument_list|(
name|getManifest
argument_list|(
name|bundles
operator|.
name|get
argument_list|(
name|bundle
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|info
argument_list|(
literal|"    scanning feature %s for exports"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|featureExports
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|exports
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Check if all the bundles can be downloaded and are actually OSGi bundles and not plain JARs      */
specifier|private
name|void
name|validateBundlesAvailable
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|bundle
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
comment|// this will throw an exception if the artifact can not be resolved
specifier|final
name|Artifact
name|artifact
init|=
name|resolve
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|bundles
operator|.
name|put
argument_list|(
name|bundle
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
if|if
condition|(
name|isBundle
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|manifests
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|getManifest
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s is not an OSGi bundle"
argument_list|,
name|bundle
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/*      * Validate if all features in a repository have bundles which can be resolved      */
specifier|private
name|void
name|validateImportsExports
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|ArtifactResolutionException
throws|,
name|ArtifactNotFoundException
throws|,
name|Exception
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
comment|// make sure the feature hasn't been validated before as a dependency
if|if
condition|(
operator|!
name|featureExports
operator|.
name|containsKey
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validateImportsExports
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*      * Validate if all imports for a feature are being matched with exports      */
specifier|private
name|void
name|validateImportsExports
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|ManifestEntry
argument_list|,
name|String
argument_list|>
name|imports
init|=
operator|new
name|HashMap
argument_list|<
name|ManifestEntry
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ManifestEntry
argument_list|>
name|exports
init|=
operator|new
name|HashSet
argument_list|<
name|ManifestEntry
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|dependency
range|:
name|feature
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|featureExports
operator|.
name|containsKey
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|exports
operator|.
name|addAll
argument_list|(
name|featureExports
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|validateImportsExports
argument_list|(
name|features
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|,
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|bundle
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|Manifest
name|meta
init|=
name|manifests
operator|.
name|get
argument_list|(
name|bundles
operator|.
name|get
argument_list|(
name|bundle
argument_list|)
argument_list|)
decl_stmt|;
name|exports
operator|.
name|addAll
argument_list|(
name|getExports
argument_list|(
name|meta
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ManifestEntry
name|entry
range|:
name|getMandatoryImports
argument_list|(
name|meta
argument_list|)
control|)
block|{
name|imports
operator|.
name|put
argument_list|(
name|entry
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
comment|// setting up the set of required imports
name|Set
argument_list|<
name|ManifestEntry
argument_list|>
name|requirements
init|=
operator|new
name|HashSet
argument_list|<
name|ManifestEntry
argument_list|>
argument_list|()
decl_stmt|;
name|requirements
operator|.
name|addAll
argument_list|(
name|imports
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
comment|// now, let's remove requirements whenever we find a matching export for them
for|for
control|(
name|ManifestEntry
name|element
range|:
name|imports
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|systemExports
operator|.
name|contains
argument_list|(
name|element
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|debug
argument_list|(
literal|"%s is resolved by a system bundle export or provided bundle"
argument_list|,
name|element
argument_list|)
expr_stmt|;
name|requirements
operator|.
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
continue|continue;
block|}
for|for
control|(
name|ManifestEntry
name|export
range|:
name|exports
control|)
block|{
if|if
condition|(
name|matches
argument_list|(
name|element
argument_list|,
name|export
argument_list|)
condition|)
block|{
name|debug
argument_list|(
literal|"%s is resolved by export %s"
argument_list|,
name|element
argument_list|,
name|export
argument_list|)
expr_stmt|;
name|requirements
operator|.
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|debug
argument_list|(
literal|"%s is not resolved by export %s"
argument_list|,
name|element
argument_list|,
name|export
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if there are any more requirements left here, there's a problem with the feature
if|if
condition|(
operator|!
name|requirements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|warn
argument_list|(
literal|"Failed to validate feature %s"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ManifestEntry
name|entry
range|:
name|requirements
control|)
block|{
name|warn
argument_list|(
literal|"No export found to match %s (imported by %s)"
argument_list|,
name|entry
argument_list|,
name|imports
operator|.
name|get
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|Exception
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%d unresolved imports in feature %s"
argument_list|,
name|requirements
operator|.
name|size
argument_list|()
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|info
argument_list|(
literal|"    OK: imports resolved for %s"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|featureExports
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|exports
argument_list|)
expr_stmt|;
block|}
comment|/*      * Check if the artifact is an OSGi bundle      */
specifier|private
name|boolean
name|isBundle
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
literal|"bundle"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactHandler
argument_list|()
operator|.
name|getPackaging
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
try|try
block|{
return|return
name|ManifestUtils
operator|.
name|isBundle
argument_list|(
name|getManifest
argument_list|(
name|artifact
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ZipException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Unable to determine if "
operator|+
name|artifact
operator|+
literal|" is a bundle; defaulting to false"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Unable to determine if "
operator|+
name|artifact
operator|+
literal|" is a bundle; defaulting to false"
argument_list|,
name|e
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
name|debug
argument_list|(
literal|"Unable to determine if "
operator|+
name|artifact
operator|+
literal|" is a bundle; defaulting to false"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/*      * Extract the META-INF/MANIFEST.MF file from an artifact      */
specifier|private
name|Manifest
name|getManifest
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|ArtifactResolutionException
throws|,
name|ArtifactNotFoundException
throws|,
name|ZipException
throws|,
name|IOException
block|{
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|localRepo
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
name|ZipFile
name|file
decl_stmt|;
if|if
condition|(
name|localFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// avoid going over to the repository if the file is already on the disk
name|file
operator|=
operator|new
name|ZipFile
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
name|file
operator|=
operator|new
name|ZipFile
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// let's replace syserr for now to hide warnings being issues by the Manifest reading process
name|PrintStream
name|original
init|=
name|System
operator|.
name|err
decl_stmt|;
try|try
block|{
name|System
operator|.
name|setErr
argument_list|(
operator|new
name|PrintStream
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|Manifest
argument_list|(
name|file
operator|.
name|getInputStream
argument_list|(
name|file
operator|.
name|getEntry
argument_list|(
literal|"META-INF/MANIFEST.MF"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|System
operator|.
name|setErr
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Resolve an artifact, downloading it from remote repositories when necessary      */
specifier|private
name|Artifact
name|resolve
parameter_list|(
name|String
name|bundle
parameter_list|)
throws|throws
name|Exception
throws|,
name|ArtifactNotFoundException
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|.
name|indexOf
argument_list|(
name|MVN_REPO_SEPARATOR
argument_list|)
operator|>=
literal|0
condition|)
block|{
if|if
condition|(
name|bundle
operator|.
name|startsWith
argument_list|(
name|MVN_URI_PREFIX
argument_list|)
condition|)
block|{
name|bundle
operator|=
name|bundle
operator|.
name|substring
argument_list|(
name|MVN_URI_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|repo
init|=
name|bundle
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|bundle
operator|.
name|indexOf
argument_list|(
name|MVN_REPO_SEPARATOR
argument_list|)
argument_list|)
decl_stmt|;
name|ArtifactRepository
name|repository
init|=
operator|new
name|DefaultArtifactRepository
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-repo"
argument_list|,
name|repo
argument_list|,
operator|new
name|DefaultRepositoryLayout
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactRepository
argument_list|>
name|repos
init|=
operator|new
name|LinkedList
argument_list|<
name|ArtifactRepository
argument_list|>
argument_list|()
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|repos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to resolve artifact for uri "
operator|+
name|bundle
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|artifact
return|;
block|}
block|}
comment|/*      * Create an artifact for a given mvn: uri      */
specifier|private
name|Artifact
name|getArtifact
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// remove the mvn: prefix when necessary
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
name|MVN_URI_PREFIX
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|MVN_URI_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// remove the repository url when specified
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
name|MVN_REPO_SEPARATOR
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|split
argument_list|(
name|MVN_REPO_SEPARATOR
argument_list|)
index|[
literal|1
index|]
expr_stmt|;
block|}
name|String
index|[]
name|elements
init|=
name|uri
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|elements
operator|.
name|length
condition|)
block|{
case|case
literal|5
case|:
return|return
name|factory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|,
name|elements
index|[
literal|1
index|]
argument_list|,
name|elements
index|[
literal|2
index|]
argument_list|,
name|elements
index|[
literal|3
index|]
argument_list|,
name|elements
index|[
literal|4
index|]
argument_list|)
return|;
case|case
literal|3
case|:
return|return
name|factory
operator|.
name|createArtifact
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|,
name|elements
index|[
literal|1
index|]
argument_list|,
name|elements
index|[
literal|2
index|]
argument_list|,
name|Artifact
operator|.
name|SCOPE_PROVIDED
argument_list|,
literal|"jar"
argument_list|)
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
comment|/*      * Helper method for debug logging      */
specifier|private
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
modifier|...
name|parms
parameter_list|)
block|{
if|if
condition|(
name|getLog
argument_list|()
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|message
argument_list|,
name|parms
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Helper method for info logging      */
specifier|private
name|void
name|info
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
modifier|...
name|parms
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|message
argument_list|,
name|parms
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Helper method for warn logging      */
specifier|private
name|void
name|warn
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
modifier|...
name|parms
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|message
argument_list|,
name|parms
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Helper method for error logging      */
specifier|private
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|,
name|Exception
name|error
parameter_list|,
name|Object
modifier|...
name|parms
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|message
argument_list|,
name|parms
argument_list|)
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
comment|/*      * Convenience collection for holding features      */
specifier|private
class|class
name|Features
block|{
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|features
init|=
operator|new
name|LinkedList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
name|features
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Feature
name|get
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|features
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|version
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|feature
return|;
block|}
block|}
throw|throw
operator|new
name|Exception
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unable to find definition for feature %s (version %s)"
argument_list|,
name|name
argument_list|,
name|version
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|Feature
index|[]
name|array
parameter_list|)
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|array
control|)
block|{
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

