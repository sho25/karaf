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
name|Set
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
name|internal
operator|.
name|model
operator|.
name|Bundle
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
name|internal
operator|.
name|model
operator|.
name|ConfigFile
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
name|internal
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
name|utils
operator|.
name|MavenUtil
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
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
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
name|Mojo
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
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_comment
comment|/**  * Add features to a repository directory  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"features-add-to-repository"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|COMPILE
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|)
specifier|public
class|class
name|AddToRepositoryMojo
extends|extends
name|AbstractFeatureMojo
block|{
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/features-repo"
argument_list|)
specifier|protected
name|File
name|repository
decl_stmt|;
comment|/**      * If set to true the exported bundles will be directly copied into the repository dir.      * If set to false the default maven repository layout will be used      */
annotation|@
name|Parameter
specifier|private
name|boolean
name|flatRepoLayout
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|List
argument_list|<
name|CopyFileBasedDescriptor
argument_list|>
name|copyFileBasedDescriptors
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
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresSet
init|=
name|resolveFeatures
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|descriptor
range|:
name|descriptorArtifacts
control|)
block|{
name|copy
argument_list|(
name|descriptor
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Feature
name|feature
range|:
name|featuresSet
control|)
block|{
name|copyBundlesToDestRepository
argument_list|(
name|feature
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
name|copyConfigFilesToDestRepository
argument_list|(
name|feature
operator|.
name|getConfigfile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|copyFileBasedDescriptorsToDestRepository
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|copyBundlesToDestRepository
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Bundle
argument_list|>
name|artifactRefs
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
for|for
control|(
name|Bundle
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
name|getLocation
argument_list|()
argument_list|,
name|skipNonMavenProtocols
argument_list|)
decl_stmt|;
comment|// Avoid getting NPE on artifact.getFile in some cases
name|resolveArtifact
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|copy
argument_list|(
name|artifact
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|copyConfigFilesToDestRepository
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|ConfigFile
argument_list|>
name|artifactRefs
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
for|for
control|(
name|ConfigFile
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
name|getLocation
argument_list|()
argument_list|,
name|skipNonMavenProtocols
argument_list|)
decl_stmt|;
comment|// Avoid getting NPE on artifact.getFile in some cases
name|resolveArtifact
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|copy
argument_list|(
name|artifact
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|copy
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|destRepository
parameter_list|)
block|{
try|try
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Copying artifact: "
operator|+
name|artifact
argument_list|)
expr_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|destRepository
argument_list|,
name|getRelativePath
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Artifact is not present in local repo."
argument_list|)
throw|;
block|}
name|copy
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|,
name|destFile
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
literal|"Error copying artifact "
operator|+
name|artifact
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get relative path for artifact      * TODO consider DefaultRepositoryLayout      * @param artifact      * @return relative path of the given artifact in a default repo layout      */
specifier|private
name|String
name|getRelativePath
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|String
name|dir
init|=
operator|(
name|this
operator|.
name|flatRepoLayout
operator|)
condition|?
literal|""
else|:
name|MavenUtil
operator|.
name|getDir
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|MavenUtil
operator|.
name|getFileName
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|dir
operator|+
name|name
return|;
block|}
specifier|private
name|void
name|copyFileBasedDescriptorsToDestRepository
parameter_list|()
block|{
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
name|File
name|destDir
init|=
operator|new
name|File
argument_list|(
name|repository
argument_list|,
name|fileBasedDescriptor
operator|.
name|getTargetDirectory
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
name|fileBasedDescriptor
operator|.
name|getTargetFileName
argument_list|()
argument_list|)
decl_stmt|;
name|copy
argument_list|(
name|fileBasedDescriptor
operator|.
name|getSourceFile
argument_list|()
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

