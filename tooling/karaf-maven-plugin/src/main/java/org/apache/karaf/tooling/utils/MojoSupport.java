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
name|utils
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
name|IOException
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|StreamUtils
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
name|execution
operator|.
name|MavenSession
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
name|plugins
operator|.
name|annotations
operator|.
name|Component
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|settings
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusContainer
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"deprecation"
block|,
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|MojoSupport
extends|extends
name|AbstractMojo
block|{
comment|/**      * Maven ProjectHelper      */
annotation|@
name|Component
specifier|protected
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * The Maven project.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project}"
argument_list|,
name|readonly
operator|=
literal|true
argument_list|)
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * Directory that resources are copied to during the build.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/${project.artifactId}-${project.version}-installer"
argument_list|)
specifier|protected
name|File
name|workDirectory
decl_stmt|;
annotation|@
name|Component
specifier|protected
name|MavenProjectBuilder
name|projectBuilder
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${localRepository}"
argument_list|)
specifier|protected
name|ArtifactRepository
name|localRepo
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.remoteArtifactRepositories}"
argument_list|)
specifier|protected
name|List
argument_list|<
name|ArtifactRepository
argument_list|>
name|remoteRepos
decl_stmt|;
annotation|@
name|Component
specifier|protected
name|ArtifactMetadataSource
name|artifactMetadataSource
decl_stmt|;
annotation|@
name|Component
specifier|protected
name|ArtifactResolver
name|artifactResolver
decl_stmt|;
annotation|@
name|Component
specifier|protected
name|ArtifactFactory
name|factory
decl_stmt|;
comment|/**      * The artifact type of a feature.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"xml"
argument_list|)
specifier|private
name|String
name|featureArtifactType
init|=
literal|"xml"
decl_stmt|;
comment|/**      * The Maven session.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${session}"
argument_list|,
name|readonly
operator|=
literal|true
argument_list|)
specifier|protected
name|MavenSession
name|mavenSession
decl_stmt|;
comment|/**      *<p>We can't autowire strongly typed RepositorySystem from Aether because it may be Sonatype (Maven 3.0.x)      * or Eclipse (Maven 3.1.x/3.2.x) version, so we switch to service locator by autowiring entire {@link PlexusContainer}</p>      *      *<p>It's a bit of a hack but we have not choice when we want to be usable both in Maven 3.0.x and 3.1.x/3.2.x</p>      */
annotation|@
name|Component
specifier|protected
name|PlexusContainer
name|container
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
comment|// called by Plexus when injecting the mojo's session
specifier|public
name|void
name|setMavenSession
parameter_list|(
name|MavenSession
name|mavenSession
parameter_list|)
block|{
name|this
operator|.
name|mavenSession
operator|=
name|mavenSession
expr_stmt|;
if|if
condition|(
name|mavenSession
operator|!=
literal|null
condition|)
block|{
comment|// check for custom settings.xml and pass it onto pax-url-aether
name|File
name|settingsFile
init|=
name|mavenSession
operator|.
name|getRequest
argument_list|()
operator|.
name|getUserSettingsFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|settingsFile
operator|!=
literal|null
operator|&&
name|settingsFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.ops4j.pax.url.mvn.settings"
argument_list|,
name|settingsFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Required because Maven 3 returns null in {@link ArtifactRepository#getProtocol()} (see KARAF-244)      */
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
comment|// Basically this should not happen; if it does though cancel the process
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
comment|/**      * Convert a feature resourceLocation (bundle or configuration file) into an artifact.      *      * @param resourceLocation the feature resource location (bundle or configuration file).      * @param skipNonMavenProtocols flag to skip protocol different than mvn:      * @return the artifact corresponding to the resource.      * @throws MojoExecutionException      */
specifier|protected
name|Artifact
name|resourceToArtifact
parameter_list|(
name|String
name|resourceLocation
parameter_list|,
name|boolean
name|skipNonMavenProtocols
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|resourceLocation
operator|=
name|resourceLocation
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
name|resourceLocation
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
literal|"Resource URL is not a Maven URL: "
operator|+
name|resourceLocation
argument_list|)
throw|;
block|}
else|else
block|{
name|resourceLocation
operator|=
name|resourceLocation
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
name|resourceLocation
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
name|resourceLocation
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
name|resourceLocation
operator|=
name|resourceLocation
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
name|resourceLocation
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
name|resourceLocation
operator|=
name|resourceLocation
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index3
argument_list|)
expr_stmt|;
block|}
comment|//check if the resourceLocation descriptor contains also remote repository information.
name|ArtifactRepository
name|repo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resourceLocation
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
name|resourceLocation
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
decl_stmt|;
name|String
name|repoUrl
init|=
name|resourceLocation
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|Proxy
name|mavenProxy
init|=
name|configureProxyToInlineRepo
argument_list|()
decl_stmt|;
if|if
condition|(
name|mavenProxy
operator|!=
literal|null
condition|)
block|{
name|repo
operator|.
name|setProxy
argument_list|(
name|mavenProxy
argument_list|)
expr_stmt|;
block|}
name|resourceLocation
operator|=
name|resourceLocation
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
name|resourceLocation
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
name|resourceLocation
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
specifier|private
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|Proxy
name|configureProxyToInlineRepo
parameter_list|()
block|{
if|if
condition|(
name|mavenSession
operator|!=
literal|null
operator|&&
name|mavenSession
operator|.
name|getSettings
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Proxy
name|proxy
init|=
name|mavenSession
operator|.
name|getSettings
argument_list|()
operator|.
name|getActiveProxy
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|Proxy
name|mavenProxy
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|Proxy
argument_list|()
decl_stmt|;
if|if
condition|(
name|proxy
operator|!=
literal|null
condition|)
block|{
name|mavenProxy
operator|.
name|setProtocol
argument_list|(
name|proxy
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|mavenProxy
operator|.
name|setHost
argument_list|(
name|proxy
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|mavenProxy
operator|.
name|setPort
argument_list|(
name|proxy
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|mavenProxy
operator|.
name|setNonProxyHosts
argument_list|(
name|proxy
operator|.
name|getNonProxyHosts
argument_list|()
argument_list|)
expr_stmt|;
name|mavenProxy
operator|.
name|setUserName
argument_list|(
name|proxy
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|mavenProxy
operator|.
name|setPassword
argument_list|(
name|proxy
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mavenProxy
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|void
name|copy
parameter_list|(
name|File
name|sourceFile
parameter_list|,
name|File
name|destFile
parameter_list|)
block|{
name|File
name|targetDir
init|=
name|destFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|ensureDirExists
argument_list|(
name|targetDir
argument_list|)
expr_stmt|;
try|try
block|{
try|try
init|(
name|FileInputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|sourceFile
argument_list|)
init|;
name|FileOutputStream
name|bos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|destFile
argument_list|)
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
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
block|}
comment|/**      * Make sure the target directory exists and      * that is actually a directory      * @param targetDir      * @throws IOException      */
specifier|private
specifier|static
name|void
name|ensureDirExists
parameter_list|(
name|File
name|targetDir
parameter_list|)
block|{
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
name|RuntimeException
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
name|RuntimeException
argument_list|(
literal|"Target is not a directory: "
operator|+
name|targetDir
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

