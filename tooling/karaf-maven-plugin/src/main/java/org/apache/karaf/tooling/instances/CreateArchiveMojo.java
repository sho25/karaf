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
operator|.
name|instances
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
name|IOException
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
name|model
operator|.
name|Resource
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
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|MatchingTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Tar
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Zip
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|TarFileSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|ZipFileSet
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

begin_comment
comment|/**  * Package a server archive from an assembled server  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"instance-create-archive"
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
argument_list|)
specifier|public
class|class
name|CreateArchiveMojo
extends|extends
name|MojoSupport
block|{
comment|/**      * The target directory of the project.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}"
argument_list|)
specifier|private
name|File
name|destDir
decl_stmt|;
comment|/**      * The location of the server repository.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/assembly"
argument_list|)
specifier|private
name|File
name|targetServerDirectory
decl_stmt|;
comment|/**      * The target file to set as the project's artifact.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.artifactId}-${project.version}"
argument_list|)
specifier|private
name|File
name|targetFile
decl_stmt|;
comment|/**      * pack a assembly as a tar.gz archive      */
annotation|@
name|Parameter
specifier|private
name|boolean
name|archiveTarGz
init|=
literal|true
decl_stmt|;
comment|/**      * pack a assembly as a zip archive      */
annotation|@
name|Parameter
specifier|private
name|boolean
name|archiveZip
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
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Setting artifact file: "
operator|+
name|targetFile
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
name|artifact
init|=
name|project
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
name|targetFile
argument_list|)
expr_stmt|;
try|try
block|{
comment|//now pack up the server.
if|if
condition|(
name|archiveTarGz
condition|)
block|{
name|archive
argument_list|(
literal|"tar.gz"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|archiveZip
condition|)
block|{
name|archive
argument_list|(
literal|"zip"
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
literal|"Could not archive plugin"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
name|void
name|archive
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|IOException
block|{
name|Artifact
name|artifact1
init|=
name|factory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|project
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|project
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|project
operator|.
name|getArtifact
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|,
name|type
argument_list|,
literal|"bin"
argument_list|)
decl_stmt|;
name|File
name|target1
init|=
name|archive
argument_list|(
name|targetServerDirectory
argument_list|,
name|destDir
argument_list|,
name|artifact1
argument_list|)
decl_stmt|;
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
name|artifact1
operator|.
name|getType
argument_list|()
argument_list|,
literal|null
argument_list|,
name|target1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|File
name|archive
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|dest
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
throws|throws
comment|//ArchiverException,
name|IOException
block|{
name|String
name|serverName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|targetFile
operator|!=
literal|null
operator|&&
name|project
operator|.
name|getPackaging
argument_list|()
operator|.
name|equals
argument_list|(
literal|"karaf-assembly"
argument_list|)
condition|)
block|{
name|serverName
operator|=
name|targetFile
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|serverName
operator|=
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|artifact
operator|.
name|getVersion
argument_list|()
expr_stmt|;
block|}
name|dest
operator|=
operator|new
name|File
argument_list|(
name|dest
argument_list|,
name|serverName
operator|+
literal|"."
operator|+
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|MatchingTask
name|archiver
decl_stmt|;
if|if
condition|(
literal|"tar.gz"
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
name|Tar
name|tar
init|=
operator|new
name|Tar
argument_list|()
decl_stmt|;
name|Tar
operator|.
name|TarCompressionMethod
name|tarCompressionMethod
init|=
operator|new
name|Tar
operator|.
name|TarCompressionMethod
argument_list|()
decl_stmt|;
name|tarCompressionMethod
operator|.
name|setValue
argument_list|(
literal|"gzip"
argument_list|)
expr_stmt|;
name|tar
operator|.
name|setCompression
argument_list|(
name|tarCompressionMethod
argument_list|)
expr_stmt|;
name|Tar
operator|.
name|TarLongFileMode
name|fileMode
init|=
operator|new
name|Tar
operator|.
name|TarLongFileMode
argument_list|()
decl_stmt|;
name|fileMode
operator|.
name|setValue
argument_list|(
name|Tar
operator|.
name|TarLongFileMode
operator|.
name|GNU
argument_list|)
expr_stmt|;
name|tar
operator|.
name|setLongfile
argument_list|(
name|fileMode
argument_list|)
expr_stmt|;
name|tar
operator|.
name|setDestFile
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|TarFileSet
name|rc
init|=
operator|new
name|TarFileSet
argument_list|()
decl_stmt|;
name|rc
operator|.
name|setDir
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setExcludes
argument_list|(
literal|"bin/"
argument_list|)
expr_stmt|;
name|tar
operator|.
name|add
argument_list|(
name|rc
argument_list|)
expr_stmt|;
name|rc
operator|=
operator|new
name|TarFileSet
argument_list|()
expr_stmt|;
name|rc
operator|.
name|setDir
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setIncludes
argument_list|(
literal|"bin/"
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setExcludes
argument_list|(
literal|"bin/*.bat"
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setFileMode
argument_list|(
literal|"755"
argument_list|)
expr_stmt|;
name|tar
operator|.
name|add
argument_list|(
name|rc
argument_list|)
expr_stmt|;
name|rc
operator|=
operator|new
name|TarFileSet
argument_list|()
expr_stmt|;
name|rc
operator|.
name|setDir
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setIncludes
argument_list|(
literal|"bin/*.bat"
argument_list|)
expr_stmt|;
name|tar
operator|.
name|add
argument_list|(
name|rc
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|resource
range|:
name|this
operator|.
name|project
operator|.
name|getResources
argument_list|()
control|)
block|{
name|File
name|resourceFile
init|=
operator|new
name|File
argument_list|(
name|resource
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|rc
operator|=
operator|new
name|TarFileSet
argument_list|()
expr_stmt|;
name|rc
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setDir
argument_list|(
name|resourceFile
argument_list|)
expr_stmt|;
name|rc
operator|.
name|appendIncludes
argument_list|(
name|resource
operator|.
name|getIncludes
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|rc
operator|.
name|appendExcludes
argument_list|(
name|resource
operator|.
name|getExcludes
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|tar
operator|.
name|add
argument_list|(
name|rc
argument_list|)
expr_stmt|;
block|}
block|}
name|archiver
operator|=
name|tar
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"zip"
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
name|Zip
name|zip
init|=
operator|new
name|Zip
argument_list|()
decl_stmt|;
name|zip
operator|.
name|setDestFile
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|ZipFileSet
name|fs
init|=
operator|new
name|ZipFileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"bin/"
argument_list|)
expr_stmt|;
name|zip
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|fs
operator|=
operator|new
name|ZipFileSet
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"bin/"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"bin/*.bat"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setFileMode
argument_list|(
literal|"755"
argument_list|)
expr_stmt|;
name|zip
operator|.
name|add
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|fs
operator|=
operator|new
name|ZipFileSet
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"bin/*.bat"
argument_list|)
expr_stmt|;
name|zip
operator|.
name|add
argument_list|(
name|fs
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|resource
range|:
name|this
operator|.
name|project
operator|.
name|getResources
argument_list|()
control|)
block|{
name|File
name|resourceFile
init|=
operator|new
name|File
argument_list|(
name|resource
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|fs
operator|=
operator|new
name|ZipFileSet
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setPrefix
argument_list|(
name|serverName
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
name|resourceFile
argument_list|)
expr_stmt|;
name|fs
operator|.
name|appendIncludes
argument_list|(
name|resource
operator|.
name|getIncludes
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|appendExcludes
argument_list|(
name|resource
operator|.
name|getExcludes
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|zip
operator|.
name|add
argument_list|(
name|fs
argument_list|)
expr_stmt|;
block|}
block|}
name|archiver
operator|=
name|zip
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown target type: "
operator|+
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|archiver
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|archiver
operator|.
name|execute
argument_list|()
expr_stmt|;
return|return
name|dest
return|;
block|}
block|}
end_class

end_unit

