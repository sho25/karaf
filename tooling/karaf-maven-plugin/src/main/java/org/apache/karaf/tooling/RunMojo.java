begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|ArchiveEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|ArchiveInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|tar
operator|.
name|TarArchiveInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|archivers
operator|.
name|zip
operator|.
name|ZipArchiveInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|compressors
operator|.
name|gzip
operator|.
name|GzipCompressorInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|compress
operator|.
name|utils
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|BootFinished
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
name|main
operator|.
name|Main
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
name|ServiceReference
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Run a Karaf instance  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"run"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PACKAGE
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|,
name|threadSafe
operator|=
literal|false
argument_list|)
specifier|public
class|class
name|RunMojo
extends|extends
name|MojoSupport
block|{
comment|/**      * Directory containing Karaf container base directory.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/karaf"
argument_list|)
specifier|private
name|File
name|karafDirectory
init|=
literal|null
decl_stmt|;
comment|/**      * Location where to download the Karaf distribution      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"mvn:org.apache.karaf/apache-karaf/LATEST/zip"
argument_list|)
specifier|private
name|String
name|karafDistribution
init|=
literal|null
decl_stmt|;
comment|/**      * Define if the project artifact should be deployed in the started container or not      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|deployProjectArtifact
init|=
literal|true
decl_stmt|;
comment|/**      * A list of URLs referencing feature repositories that will be added      * to the karaf instance started by this goal.      */
annotation|@
name|Parameter
specifier|private
name|String
index|[]
name|featureRepositories
init|=
literal|null
decl_stmt|;
comment|/**      * Comma-separated list of features to install.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|""
argument_list|)
specifier|private
name|String
name|featuresToInstall
init|=
literal|null
decl_stmt|;
comment|/**      * Define if the Karaf container keep running or stop just after the goal execution      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|keepRunning
init|=
literal|true
decl_stmt|;
comment|/**      * Define if the Karaf embedded sshd should be started or not      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|String
name|startSsh
init|=
literal|"false"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|mvnPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"mvn:([^/ ]+)/([^/ ]+)/([^/ ]*)(/([^/ ]+)(/([^/ ]+))?)?"
argument_list|)
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
name|karafDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Using Karaf container located "
operator|+
name|karafDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Extracting Karaf container"
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|karafArchiveFile
init|=
name|resolveFile
argument_list|(
name|karafDistribution
argument_list|)
decl_stmt|;
name|extract
argument_list|(
name|karafArchiveFile
argument_list|,
name|karafDirectory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Can't extract Karaf container"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Starting Karaf container"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.home"
argument_list|,
name|karafDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.base"
argument_list|,
name|karafDirectory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.data"
argument_list|,
name|karafDirectory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/data"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
name|karafDirectory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/etc"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.instances"
argument_list|,
name|karafDirectory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/instances"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.startLocalConsole"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.startRemoteShell"
argument_list|,
name|startSsh
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.lock"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|Main
name|main
init|=
operator|new
name|Main
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
try|try
block|{
name|main
operator|.
name|launch
argument_list|()
expr_stmt|;
while|while
condition|(
name|main
operator|.
name|getFramework
argument_list|()
operator|.
name|getState
argument_list|()
operator|!=
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
name|BundleContext
name|bundleContext
init|=
name|main
operator|.
name|getFramework
argument_list|()
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|Object
name|bootFinished
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|bootFinished
operator|==
literal|null
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|ServiceReference
name|ref
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|BootFinished
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|bootFinished
operator|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
name|featureService
init|=
name|findFeatureService
argument_list|(
name|bundleContext
argument_list|)
decl_stmt|;
name|addFeatureRepositories
argument_list|(
name|featureService
argument_list|)
expr_stmt|;
name|deploy
argument_list|(
name|bundleContext
argument_list|,
name|featureService
argument_list|)
expr_stmt|;
name|addFeatures
argument_list|(
name|featureService
argument_list|)
expr_stmt|;
if|if
condition|(
name|keepRunning
condition|)
name|main
operator|.
name|awaitShutdown
argument_list|()
expr_stmt|;
name|main
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Can't start container"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|addFeatureRepositories
parameter_list|(
name|Object
name|featureService
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|featureRepositories
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|serviceClass
init|=
name|featureService
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
name|addRepositoryMethod
init|=
name|serviceClass
operator|.
name|getMethod
argument_list|(
literal|"addRepository"
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|featureRepo
range|:
name|featureRepositories
control|)
block|{
name|addRepositoryMethod
operator|.
name|invoke
argument_list|(
name|featureService
argument_list|,
name|URI
operator|.
name|create
argument_list|(
name|featureRepo
argument_list|)
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
name|MojoExecutionException
argument_list|(
literal|"Failed to add feature repositories to karaf"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|void
name|deploy
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Object
name|featureService
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|deployProjectArtifact
condition|)
block|{
name|File
name|artifact
init|=
name|project
operator|.
name|getArtifact
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|File
name|attachedFeatureFile
init|=
name|getAttachedFeatureFile
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|boolean
name|artifactExists
init|=
name|artifact
operator|!=
literal|null
operator|&&
name|artifact
operator|.
name|exists
argument_list|()
decl_stmt|;
name|boolean
name|attachedFeatureFileExists
init|=
name|attachedFeatureFile
operator|!=
literal|null
operator|&&
name|attachedFeatureFile
operator|.
name|exists
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactExists
condition|)
block|{
if|if
condition|(
name|project
operator|.
name|getPackaging
argument_list|()
operator|.
name|equals
argument_list|(
literal|"bundle"
argument_list|)
condition|)
block|{
try|try
block|{
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|artifact
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
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
literal|"Can't deploy project artifact in container"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Packaging "
operator|+
name|project
operator|.
name|getPackaging
argument_list|()
operator|+
literal|" is not supported"
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|attachedFeatureFileExists
condition|)
block|{
name|addFeaturesAttachmentAsFeatureRepository
argument_list|(
name|featureService
argument_list|,
name|attachedFeatureFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Project artifact doesn't exist"
argument_list|)
throw|;
block|}
block|}
block|}
name|void
name|addFeatures
parameter_list|(
name|Object
name|featureService
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|featuresToInstall
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|serviceClass
init|=
name|featureService
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
name|installFeatureMethod
init|=
name|serviceClass
operator|.
name|getMethod
argument_list|(
literal|"installFeature"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
index|[]
name|features
init|=
name|featuresToInstall
operator|.
name|split
argument_list|(
literal|" *, *"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|feature
range|:
name|features
control|)
block|{
name|installFeatureMethod
operator|.
name|invoke
argument_list|(
name|featureService
argument_list|,
name|feature
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000L
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
name|MojoExecutionException
argument_list|(
literal|"Failed to add features to karaf"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|extract
parameter_list|(
name|File
name|sourceFile
parameter_list|,
name|File
name|targetFolder
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|sourceFile
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|".zip"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|extractZipDistribution
argument_list|(
name|sourceFile
argument_list|,
name|targetFolder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sourceFile
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|".tar.gz"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|extractTarGzDistribution
argument_list|(
name|sourceFile
argument_list|,
name|targetFolder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown packaging of distribution; only zip or tar.gz could be handled."
argument_list|)
throw|;
block|}
return|return;
block|}
specifier|private
specifier|static
name|void
name|extractTarGzDistribution
parameter_list|(
name|File
name|sourceDistribution
parameter_list|,
name|File
name|_targetFolder
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|uncompressedFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"uncompressedTarGz-"
argument_list|,
literal|".tar"
argument_list|)
decl_stmt|;
name|extractGzArchive
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|sourceDistribution
argument_list|)
argument_list|,
name|uncompressedFile
argument_list|)
expr_stmt|;
name|extract
argument_list|(
operator|new
name|TarArchiveInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|uncompressedFile
argument_list|)
argument_list|)
argument_list|,
name|_targetFolder
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|forceDelete
argument_list|(
name|uncompressedFile
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|extractZipDistribution
parameter_list|(
name|File
name|sourceDistribution
parameter_list|,
name|File
name|_targetFolder
parameter_list|)
throws|throws
name|IOException
block|{
name|extract
argument_list|(
operator|new
name|ZipArchiveInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|sourceDistribution
argument_list|)
argument_list|)
argument_list|,
name|_targetFolder
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|extractGzArchive
parameter_list|(
name|InputStream
name|tarGz
parameter_list|,
name|File
name|tar
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedInputStream
name|in
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|tarGz
argument_list|)
decl_stmt|;
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|tar
argument_list|)
decl_stmt|;
name|GzipCompressorInputStream
name|gzIn
init|=
operator|new
name|GzipCompressorInputStream
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1000
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
operator|(
name|n
operator|=
name|gzIn
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|gzIn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|extract
parameter_list|(
name|ArchiveInputStream
name|is
parameter_list|,
name|File
name|targetDir
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|targetDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|forceDelete
argument_list|(
name|targetDir
argument_list|)
expr_stmt|;
block|}
name|targetDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|ArchiveEntry
name|entry
init|=
name|is
operator|.
name|getNextEntry
argument_list|()
decl_stmt|;
while|while
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
name|entry
operator|=
name|is
operator|.
name|getNextEntry
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|boolean
name|isMavenUrl
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|mvnPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|matches
argument_list|()
return|;
block|}
specifier|private
name|File
name|resolveFile
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|File
name|fileResolved
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isMavenUrl
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|fileResolved
operator|=
operator|new
name|File
argument_list|(
name|fromMaven
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Artifact
name|artifactTemp
init|=
name|resourceToArtifact
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fileResolved
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|artifactResolver
operator|.
name|resolve
argument_list|(
name|artifactTemp
argument_list|,
name|remoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
name|fileResolved
operator|=
name|artifactTemp
operator|.
name|getFile
argument_list|()
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
literal|"Artifact was not resolved"
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
literal|"Artifact was not found"
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
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|fileResolved
operator|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|fileResolved
return|;
block|}
comment|/**      * Return a path for an artifact:      * - if the input is already a path (doesn't contain ':'), the same path is returned.      * - if the input is a Maven URL, the input is converted to a default repository location path, type and classifier      *   are optional.      *      * @param name artifact data      * @return path as supplied or a default Maven repository path      */
specifier|private
specifier|static
name|String
name|fromMaven
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|mvnPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|name
return|;
block|}
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
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
name|b
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|b
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'.'
condition|)
block|{
name|b
operator|.
name|setCharAt
argument_list|(
name|i
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
block|}
name|b
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|// groupId
name|String
name|artifactId
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|m
operator|.
name|group
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|m
operator|.
name|group
argument_list|(
literal|7
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|// artifactId
name|b
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|// version
name|b
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|present
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|present
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|".jar"
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|boolean
name|present
parameter_list|(
name|String
name|part
parameter_list|)
block|{
return|return
name|part
operator|!=
literal|null
operator|&&
operator|!
name|part
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|private
name|File
name|getAttachedFeatureFile
parameter_list|(
name|MavenProject
name|project
parameter_list|)
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|attachedArtifacts
init|=
name|project
operator|.
name|getAttachedArtifacts
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|attachedArtifacts
control|)
block|{
if|if
condition|(
literal|"features"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
operator|&&
literal|"xml"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|artifact
operator|.
name|getFile
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
name|Object
name|findFeatureService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
comment|// Use Object as the service type and use reflection when calling the service,
comment|// because the returned services use the OSGi classloader
name|ServiceReference
name|ref
init|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|Object
name|featureService
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
return|return
name|featureService
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|addFeaturesAttachmentAsFeatureRepository
parameter_list|(
name|Object
name|featureService
parameter_list|,
name|File
name|attachedFeatureFile
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|featureService
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|serviceClass
init|=
name|featureService
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
name|addRepositoryMethod
init|=
name|serviceClass
operator|.
name|getMethod
argument_list|(
literal|"addRepository"
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
name|addRepositoryMethod
operator|.
name|invoke
argument_list|(
name|featureService
argument_list|,
name|attachedFeatureFile
operator|.
name|toURI
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
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed to register attachment as feature repository"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed to find the FeatureService when adding a feature repository"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

