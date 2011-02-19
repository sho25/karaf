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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|net
operator|.
name|URLClassLoader
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
name|Iterator
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
name|factory
operator|.
name|ArtifactFactory
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
name|metadata
operator|.
name|ArtifactMetadataSource
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
name|ArtifactCollector
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
name|ArtifactResolver
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
name|versioning
operator|.
name|InvalidVersionSpecificationException
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
name|versioning
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
name|maven
operator|.
name|model
operator|.
name|Dependency
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
name|model
operator|.
name|DependencyManagement
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
name|AbstractMojo
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|MavenProjectBuilder
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
name|project
operator|.
name|MavenProjectHelper
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
name|project
operator|.
name|ProjectBuildingException
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|MojoSupport
extends|extends
name|AbstractMojo
block|{
comment|/**      * Maven ProjectHelper      *      * @component      */
specifier|protected
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * The maven project.      *      * @parameter expression="${project}"      * @required      * @readonly      */
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Directory that resources are copied to during the build.      *      * @parameter expression="${project.build.directory}/${project.artifactId}-${project.version}-installer"      * @required      */
specifier|protected
name|File
name|workDirectory
decl_stmt|;
comment|/**      * @component      */
specifier|protected
name|MavenProjectBuilder
name|projectBuilder
decl_stmt|;
comment|/**      * @parameter default-value="${localRepository}"      */
specifier|protected
name|ArtifactRepository
name|localRepo
decl_stmt|;
comment|/**      * @parameter default-value="${project.remoteArtifactRepositories}"      */
specifier|protected
name|List
argument_list|<
name|ArtifactRepository
argument_list|>
name|remoteRepos
decl_stmt|;
comment|/**      * @component      */
specifier|protected
name|ArtifactMetadataSource
name|artifactMetadataSource
decl_stmt|;
comment|/**      * @component      */
specifier|protected
name|ArtifactResolver
name|resolver
decl_stmt|;
specifier|protected
name|ArtifactCollector
name|collector
init|=
operator|new
name|GraphArtifactCollector
argument_list|()
decl_stmt|;
comment|/**      * @component      */
specifier|protected
name|ArtifactFactory
name|factory
decl_stmt|;
comment|/**      * The artifact type of a feature      *       * @parameter default-value="xml"      */
specifier|private
name|String
name|featureArtifactType
init|=
literal|"xml"
decl_stmt|;
specifier|protected
name|MavenProject
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
specifier|protected
name|File
name|getWorkDirectory
parameter_list|()
block|{
return|return
name|workDirectory
return|;
block|}
specifier|public
name|MavenProjectHelper
name|getProjectHelper
parameter_list|()
block|{
return|return
name|projectHelper
return|;
block|}
specifier|protected
name|void
name|removeBranch
parameter_list|(
name|ResolutionListenerImpl
name|listener
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|Node
name|n
init|=
name|listener
operator|.
name|getNode
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|it
init|=
name|n
operator|.
name|getParents
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
name|Node
name|parent
init|=
operator|(
name|Node
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|parent
operator|.
name|getChildren
argument_list|()
operator|.
name|remove
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|removeChildren
parameter_list|(
name|ResolutionListenerImpl
name|listener
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|Node
name|n
init|=
name|listener
operator|.
name|getNode
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|n
operator|.
name|getChildren
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|Set
name|getArtifacts
parameter_list|(
name|Node
name|n
parameter_list|,
name|Set
name|s
parameter_list|)
block|{
if|if
condition|(
operator|!
name|s
operator|.
name|contains
argument_list|(
name|n
operator|.
name|getArtifact
argument_list|()
argument_list|)
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
name|n
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|n
operator|.
name|getChildren
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Node
name|c
init|=
operator|(
name|Node
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|getArtifacts
argument_list|(
name|c
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|s
return|;
block|}
specifier|protected
name|void
name|excludeBranch
parameter_list|(
name|Node
name|n
parameter_list|,
name|Set
name|excludes
parameter_list|)
block|{
name|excludes
operator|.
name|add
argument_list|(
name|n
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|n
operator|.
name|getChildren
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Node
name|c
init|=
operator|(
name|Node
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|excludeBranch
argument_list|(
name|c
argument_list|,
name|excludes
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|print
parameter_list|(
name|Node
name|rootNode
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|getArtifacts
argument_list|(
name|rootNode
argument_list|,
operator|new
name|HashSet
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|a
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|" "
operator|+
name|a
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Set
name|retainArtifacts
parameter_list|(
name|Set
name|includes
parameter_list|,
name|ResolutionListenerImpl
name|listener
parameter_list|)
block|{
name|Set
name|finalIncludes
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Set
name|filteredArtifacts
init|=
name|getArtifacts
argument_list|(
name|listener
operator|.
name|getRootNode
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|includes
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter2
init|=
name|filteredArtifacts
operator|.
name|iterator
argument_list|()
init|;
name|iter2
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|filteredArtifact
init|=
operator|(
name|Artifact
operator|)
name|iter2
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|filteredArtifact
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|filteredArtifact
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|filteredArtifact
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|filteredArtifact
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Resolved artifact "
operator|+
name|artifact
operator|+
literal|" has a different version from that in dependency management "
operator|+
name|filteredArtifact
operator|+
literal|", overriding dependency management"
argument_list|)
expr_stmt|;
block|}
name|finalIncludes
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|finalIncludes
return|;
block|}
specifier|protected
name|ResolutionListenerImpl
name|resolveProject
parameter_list|()
block|{
name|Map
name|managedVersions
init|=
literal|null
decl_stmt|;
try|try
block|{
name|managedVersions
operator|=
name|createManagedVersionMap
argument_list|(
name|project
operator|.
name|getId
argument_list|()
argument_list|,
name|project
operator|.
name|getDependencyManagement
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProjectBuildingException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"An error occurred while resolving project dependencies."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|ResolutionListenerImpl
name|listener
init|=
operator|new
name|ResolutionListenerImpl
argument_list|()
decl_stmt|;
name|listener
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|collector
operator|.
name|collect
argument_list|(
name|project
operator|.
name|getDependencyArtifacts
argument_list|()
argument_list|,
name|project
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|managedVersions
argument_list|,
name|localRepo
argument_list|,
name|remoteRepos
argument_list|,
name|artifactMetadataSource
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
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
literal|"An error occurred while resolving project dependencies."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
literal|"Dependency graph"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"================"
argument_list|)
expr_stmt|;
name|print
argument_list|(
name|listener
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"================"
argument_list|)
expr_stmt|;
block|}
return|return
name|listener
return|;
block|}
specifier|protected
name|Map
name|createManagedVersionMap
parameter_list|(
name|String
name|projectId
parameter_list|,
name|DependencyManagement
name|dependencyManagement
parameter_list|)
throws|throws
name|ProjectBuildingException
block|{
name|Map
name|map
decl_stmt|;
if|if
condition|(
name|dependencyManagement
operator|!=
literal|null
operator|&&
name|dependencyManagement
operator|.
name|getDependencies
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|dependencyManagement
operator|.
name|getDependencies
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Dependency
name|d
init|=
operator|(
name|Dependency
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|VersionRange
name|versionRange
init|=
name|VersionRange
operator|.
name|createFromVersionSpec
argument_list|(
name|d
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|factory
operator|.
name|createDependencyArtifact
argument_list|(
name|d
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|d
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|versionRange
argument_list|,
name|d
operator|.
name|getType
argument_list|()
argument_list|,
name|d
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|d
operator|.
name|getScope
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|d
operator|.
name|getManagementKey
argument_list|()
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidVersionSpecificationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectBuildingException
argument_list|(
name|projectId
argument_list|,
literal|"Unable to parse version '"
operator|+
name|d
operator|.
name|getVersion
argument_list|()
operator|+
literal|"' for dependency '"
operator|+
name|d
operator|.
name|getManagementKey
argument_list|()
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
name|map
operator|=
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
comment|/**      * Set up a classloader for the execution of the main class.      *      * @return      * @throws MojoExecutionException      */
specifier|protected
name|URLClassLoader
name|getClassLoader
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|Set
name|urls
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|URL
name|mainClasses
init|=
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|)
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Adding to classpath : "
operator|+
name|mainClasses
argument_list|)
expr_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|mainClasses
argument_list|)
expr_stmt|;
name|URL
name|testClasses
init|=
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getTestOutputDirectory
argument_list|()
argument_list|)
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Adding to classpath : "
operator|+
name|testClasses
argument_list|)
expr_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|testClasses
argument_list|)
expr_stmt|;
name|Set
name|dependencies
init|=
name|project
operator|.
name|getArtifacts
argument_list|()
decl_stmt|;
name|Iterator
name|iter
init|=
name|dependencies
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artifact
name|classPathElement
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Adding artifact: "
operator|+
name|classPathElement
operator|.
name|getFile
argument_list|()
operator|+
literal|" to classpath"
argument_list|)
expr_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|classPathElement
operator|.
name|getFile
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|URLClassLoader
name|appClassloader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|(
name|URL
index|[]
operator|)
name|urls
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
name|urls
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|appClassloader
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error during setting up classpath"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|translateFromMaven
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|uri
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
return|return
name|getLocalRepoUrl
argument_list|()
operator|+
literal|"/"
operator|+
name|dir
operator|+
name|name
return|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
operator|&&
name|uri
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|String
name|baseDir
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|baseDir
decl_stmt|;
if|if
condition|(
name|baseDir
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|result
operator|=
literal|"file:///"
operator|+
name|baseDir
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
return|return
name|uri
return|;
block|}
specifier|protected
name|String
name|getLocalRepoUrl
parameter_list|()
block|{
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
condition|)
block|{
name|String
name|baseDir
init|=
name|localRepo
operator|.
name|getBasedir
argument_list|()
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
decl_stmt|;
return|return
name|extractProtocolFromLocalMavenRepo
argument_list|()
operator|+
literal|":///"
operator|+
name|baseDir
return|;
block|}
else|else
block|{
return|return
name|localRepo
operator|.
name|getUrl
argument_list|()
return|;
block|}
block|}
comment|/**      * Required because maven3 returns null in {@link ArtifactRepository#getProtocol()} (see KARAF-244)      */
specifier|private
name|String
name|extractProtocolFromLocalMavenRepo
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|localRepo
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getProtocol
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// Basically this should not happen; if though cancel the process
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Repository URL is not valid"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Dependency
name|findDependency
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|groupId
parameter_list|)
block|{
for|for
control|(
name|Dependency
name|dep
range|:
name|dependencies
control|)
block|{
if|if
condition|(
name|artifactId
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|groupId
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|&&
name|featureArtifactType
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|dep
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
return|return
name|dep
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Artifact
name|bundleToArtifact
parameter_list|(
name|String
name|bundle
parameter_list|,
name|boolean
name|skipNonMavenProtocols
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|bundle
operator|=
name|bundle
operator|.
name|replace
argument_list|(
literal|"\r\n"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"\n"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"\t"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
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
return|return
literal|null
return|;
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
operator|+
literal|"mvn:"
operator|.
name|length
argument_list|()
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
comment|//check if the bundle descriptor contains also remote repository information.
name|ArtifactRepository
name|repo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bundle
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
condition|)
block|{
specifier|final
name|int
name|repoDelimIntex
init|=
name|bundle
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
decl_stmt|;
name|String
name|repoUrl
init|=
name|bundle
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|repoDelimIntex
argument_list|)
decl_stmt|;
name|repo
operator|=
operator|new
name|DefaultArtifactRepository
argument_list|(
name|repoUrl
argument_list|,
name|repoUrl
argument_list|,
operator|new
name|DefaultRepositoryLayout
argument_list|()
argument_list|)
expr_stmt|;
name|bundle
operator|=
name|bundle
operator|.
name|substring
argument_list|(
name|repoDelimIntex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|parts
init|=
name|bundle
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
else|else
block|{
name|Dependency
name|dep
init|=
name|findDependency
argument_list|(
name|project
operator|.
name|getDependencies
argument_list|()
argument_list|,
name|artifactId
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|dep
operator|==
literal|null
operator|&&
name|project
operator|.
name|getDependencyManagement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dep
operator|=
name|findDependency
argument_list|(
name|project
operator|.
name|getDependencyManagement
argument_list|()
operator|.
name|getDependencies
argument_list|()
argument_list|,
name|artifactId
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dep
operator|!=
literal|null
condition|)
block|{
name|version
operator|=
name|dep
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|classifier
operator|=
name|dep
operator|.
name|getClassifier
argument_list|()
expr_stmt|;
name|type
operator|=
name|dep
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Cannot find version for: "
operator|+
name|bundle
argument_list|)
throw|;
block|}
name|Artifact
name|artifact
init|=
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
decl_stmt|;
name|artifact
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

