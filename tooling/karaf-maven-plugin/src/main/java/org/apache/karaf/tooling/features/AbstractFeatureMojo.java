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
name|utils
operator|.
name|version
operator|.
name|VersionRange
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
name|utils
operator|.
name|version
operator|.
name|VersionTable
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
name|features
operator|.
name|model
operator|.
name|ArtifactRef
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
name|features
operator|.
name|model
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
name|tooling
operator|.
name|features
operator|.
name|model
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
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
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
name|Version
import|;
end_import

begin_comment
comment|/**  * Common functionality for mojos that need to reolve features  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractFeatureMojo
extends|extends
name|MojoSupport
block|{
annotation|@
name|Parameter
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|descriptors
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|Artifact
argument_list|>
name|descriptorArtifacts
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|features
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|boolean
name|addTransitiveFeatures
init|=
literal|true
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|boolean
name|includeMvnBasedDescriptors
init|=
literal|false
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|boolean
name|failOnArtifactResolutionError
init|=
literal|true
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|boolean
name|resolveDefinedRepositoriesRecursively
init|=
literal|true
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|boolean
name|skipNonMavenProtocols
init|=
literal|true
decl_stmt|;
comment|/**      * The start level exported when no explicit start level is set for a bundle      */
annotation|@
name|Parameter
specifier|private
name|int
name|defaultStartLevel
init|=
literal|80
decl_stmt|;
comment|/**      * Internal counter for garbage collection      */
specifier|private
name|int
name|resolveCount
init|=
literal|0
decl_stmt|;
specifier|public
name|AbstractFeatureMojo
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|descriptorArtifacts
operator|=
operator|new
name|HashSet
argument_list|<
name|Artifact
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|addFeatureRepo
parameter_list|(
name|String
name|featureUrl
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|Artifact
name|featureDescArtifact
init|=
name|resourceToArtifact
argument_list|(
name|featureUrl
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|featureDescArtifact
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|resolveArtifact
argument_list|(
name|featureDescArtifact
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
name|featureUrl
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
name|featureUrl
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
specifier|protected
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
block|{
comment|// let's ensure a mvn: based url is sitting in the local repo before we try reading it
name|Artifact
name|descriptor
decl_stmt|;
try|try
block|{
name|descriptor
operator|=
name|resourceToArtifact
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|descriptor
operator|!=
literal|null
condition|)
block|{
name|resolveArtifact
argument_list|(
name|descriptor
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
name|descriptorArtifacts
operator|.
name|add
argument_list|(
name|descriptor
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
name|URI
name|repoURI
init|=
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
decl_stmt|;
name|Repository
name|repo
init|=
operator|new
name|Repository
argument_list|(
name|repoURI
argument_list|,
name|defaultStartLevel
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
operator|+
literal|"/"
operator|+
name|f
operator|.
name|getVersion
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
comment|/**      * Resolves and copies the given artifact to the repository path.      * Prefers to resolve using the repository of the artifact if present.      *       * @param artifact      * @param remoteRepos      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|protected
name|void
name|resolveArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|List
argument_list|<
name|ArtifactRepository
argument_list|>
name|remoteRepos
parameter_list|)
block|{
try|try
block|{
name|List
argument_list|<
name|ArtifactRepository
argument_list|>
name|usedRemoteRepos
init|=
name|artifact
operator|.
name|getRepository
argument_list|()
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|singletonList
argument_list|(
name|artifact
operator|.
name|getRepository
argument_list|()
argument_list|)
else|:
name|remoteRepos
decl_stmt|;
name|artifactResolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|usedRemoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|RuntimeException
argument_list|(
literal|"Can't resolve artifact "
operator|+
name|artifact
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Can't resolve artifact "
operator|+
name|artifact
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
comment|/**      * Populate the features by traversing the listed features and their      * dependencies if transitive is true      *        * @param featureNames      * @param features      * @param featuresMap      * @param transitive      */
specifier|protected
name|void
name|addFeatures
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|featureNames
parameter_list|,
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featuresMap
parameter_list|,
name|boolean
name|transitive
parameter_list|)
block|{
for|for
control|(
name|String
name|feature
range|:
name|featureNames
control|)
block|{
name|Feature
name|f
init|=
name|getMatchingFeature
argument_list|(
name|featuresMap
argument_list|,
name|feature
argument_list|)
decl_stmt|;
name|features
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
if|if
condition|(
name|transitive
condition|)
block|{
name|addFeatures
argument_list|(
name|f
operator|.
name|getDependencies
argument_list|()
argument_list|,
name|features
argument_list|,
name|featuresMap
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Feature
name|getMatchingFeature
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featuresMap
parameter_list|,
name|String
name|feature
parameter_list|)
block|{
comment|// feature could be only the name or name/version
name|int
name|delimIndex
init|=
name|feature
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|delimIndex
operator|>
literal|0
condition|)
block|{
name|version
operator|=
name|feature
operator|.
name|substring
argument_list|(
name|delimIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|feature
operator|=
name|feature
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|delimIndex
argument_list|)
expr_stmt|;
block|}
name|Feature
name|f
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
comment|// looking for a specific feature with name and version
name|f
operator|=
name|featuresMap
operator|.
name|get
argument_list|(
name|feature
operator|+
literal|"/"
operator|+
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
comment|//it's probably is a version range so try to use VersionRange Utils
name|VersionRange
name|versionRange
init|=
operator|new
name|VersionRange
argument_list|(
name|version
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|featuresMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
index|[]
name|nameVersion
init|=
name|key
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|feature
operator|.
name|equals
argument_list|(
name|nameVersion
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|String
name|verStr
init|=
name|featuresMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|Version
name|ver
init|=
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|verStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionRange
operator|.
name|contains
argument_list|(
name|ver
argument_list|)
condition|)
block|{
if|if
condition|(
name|f
operator|==
literal|null
operator|||
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|f
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|compareTo
argument_list|(
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|featuresMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
operator|<
literal|0
condition|)
block|{
name|f
operator|=
name|featuresMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
else|else
block|{
comment|// looking for the first feature name (whatever the version is)
for|for
control|(
name|String
name|key
range|:
name|featuresMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
index|[]
name|nameVersion
init|=
name|key
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|feature
operator|.
name|equals
argument_list|(
name|nameVersion
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|f
operator|=
name|featuresMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
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
return|return
name|f
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|Feature
argument_list|>
name|resolveFeatures
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresSet
init|=
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|artifactsToCopy
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
name|artifactsToCopy
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
name|addFeatures
argument_list|(
name|features
argument_list|,
name|featuresSet
argument_list|,
name|featuresMap
argument_list|,
name|addTransitiveFeatures
argument_list|)
expr_stmt|;
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
name|Feature
name|feature
range|:
name|featuresSet
control|)
block|{
try|try
block|{
name|resolveArtifacts
argument_list|(
name|feature
operator|.
name|getBundles
argument_list|()
argument_list|)
expr_stmt|;
name|resolveArtifacts
argument_list|(
name|feature
operator|.
name|getConfigFiles
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error resolving feature "
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|MojoExecutionException
argument_list|(
literal|"Error populating repository"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|featuresSet
return|;
block|}
specifier|private
name|void
name|resolveArtifacts
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|ArtifactRef
argument_list|>
name|artifactRefs
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
for|for
control|(
name|ArtifactRef
name|artifactRef
range|:
name|artifactRefs
control|)
block|{
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
name|artifactRef
operator|.
name|getUrl
argument_list|()
argument_list|,
name|skipNonMavenProtocols
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|artifactRef
operator|.
name|setArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
try|try
block|{
name|resolveArtifact
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error resolving artifact "
operator|+
name|artifactRef
operator|.
name|getUrl
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|checkDoGarbageCollect
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Maven ArtifactResolver leaves file handles around so need to clean up      * or we will run out of file descriptors      */
specifier|protected
name|void
name|checkDoGarbageCollect
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|resolveCount
operator|++
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

