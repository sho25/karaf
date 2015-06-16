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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|DirectoryStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|TarArchiveEntry
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
name|TarArchiveOutputStream
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
name|TarConstants
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
name|UnixStat
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
name|ZipArchiveEntry
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
name|ZipArchiveOutputStream
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
name|GzipCompressorOutputStream
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
comment|/**  * Package a server archive from an assembled server  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"archive"
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
name|ArchiveMojo
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
comment|/**      * use symbolic links in tar.gz or zip archives      *      * Symbolic links are not very well supported by windows Platform.      * At least, is does not work on WinXP + NTFS, so do not include them      * for now. So the default is false.      */
annotation|@
name|Parameter
specifier|private
name|boolean
name|useSymLinks
init|=
literal|false
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
try|try
init|(
name|OutputStream
name|fOut
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|dest
operator|.
name|toPath
argument_list|()
argument_list|)
init|;                     OutputStream bOut = new BufferedOutputStream(fOut)
empty_stmt|;
name|OutputStream
name|gzOut
init|=
operator|new
name|GzipCompressorOutputStream
argument_list|(
name|bOut
argument_list|)
decl_stmt|;
name|TarArchiveOutputStream
name|tOut
init|=
operator|new
name|TarArchiveOutputStream
argument_list|(
name|gzOut
argument_list|)
decl_stmt|;
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|children
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|source
operator|.
name|toPath
argument_list|()
argument_list|)
init|)
block|{
name|tOut
operator|.
name|setLongFileMode
argument_list|(
name|TarArchiveOutputStream
operator|.
name|LONGFILE_POSIX
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|setBigNumberMode
argument_list|(
name|TarArchiveOutputStream
operator|.
name|BIGNUMBER_POSIX
argument_list|)
expr_stmt|;
for|for
control|(
name|Path
name|child
range|:
name|children
control|)
block|{
name|addFileToTarGz
argument_list|(
name|tOut
argument_list|,
name|child
argument_list|,
name|serverName
operator|+
literal|"/"
argument_list|)
expr_stmt|;
block|}
block|}
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
try|try
init|(
name|OutputStream
name|fOut
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|dest
operator|.
name|toPath
argument_list|()
argument_list|)
init|;                     OutputStream bOut = new BufferedOutputStream(fOut)
empty_stmt|;
name|ZipArchiveOutputStream
name|tOut
init|=
operator|new
name|ZipArchiveOutputStream
argument_list|(
name|bOut
argument_list|)
decl_stmt|;
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|children
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|source
operator|.
name|toPath
argument_list|()
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|child
range|:
name|children
control|)
block|{
name|addFileToZip
argument_list|(
name|tOut
argument_list|,
name|child
argument_list|,
name|serverName
operator|+
literal|"/"
argument_list|)
expr_stmt|;
block|}
block|}
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
return|return
name|dest
return|;
block|}
specifier|private
name|void
name|addFileToTarGz
parameter_list|(
name|TarArchiveOutputStream
name|tOut
parameter_list|,
name|Path
name|f
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|String
name|entryName
init|=
name|base
operator|+
name|f
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
decl_stmt|;
name|TarArchiveEntry
name|tarEntry
init|=
operator|new
name|TarArchiveEntry
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
name|tOut
operator|.
name|putArchiveEntry
argument_list|(
name|tarEntry
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|children
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|f
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|child
range|:
name|children
control|)
block|{
name|addFileToTarGz
argument_list|(
name|tOut
argument_list|,
name|child
argument_list|,
name|entryName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|useSymLinks
operator|&&
name|Files
operator|.
name|isSymbolicLink
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|String
name|entryName
init|=
name|base
operator|+
name|f
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|TarArchiveEntry
name|tarEntry
init|=
operator|new
name|TarArchiveEntry
argument_list|(
name|entryName
argument_list|,
name|TarConstants
operator|.
name|LF_SYMLINK
argument_list|)
decl_stmt|;
name|tarEntry
operator|.
name|setLinkName
argument_list|(
name|Files
operator|.
name|readSymbolicLink
argument_list|(
name|f
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|putArchiveEntry
argument_list|(
name|tarEntry
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|entryName
init|=
name|base
operator|+
name|f
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|TarArchiveEntry
name|tarEntry
init|=
operator|new
name|TarArchiveEntry
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
name|tarEntry
operator|.
name|setSize
argument_list|(
name|Files
operator|.
name|size
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|entryName
operator|.
name|contains
argument_list|(
literal|"/bin/"
argument_list|)
condition|)
block|{
if|if
condition|(
name|entryName
operator|.
name|endsWith
argument_list|(
literal|".bat"
argument_list|)
condition|)
block|{
name|tarEntry
operator|.
name|setMode
argument_list|(
literal|0644
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tarEntry
operator|.
name|setMode
argument_list|(
literal|0755
argument_list|)
expr_stmt|;
block|}
block|}
name|tOut
operator|.
name|putArchiveEntry
argument_list|(
name|tarEntry
argument_list|)
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|f
argument_list|,
name|tOut
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addFileToZip
parameter_list|(
name|ZipArchiveOutputStream
name|tOut
parameter_list|,
name|Path
name|f
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|String
name|entryName
init|=
name|base
operator|+
name|f
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
decl_stmt|;
name|ZipArchiveEntry
name|zipEntry
init|=
operator|new
name|ZipArchiveEntry
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
name|tOut
operator|.
name|putArchiveEntry
argument_list|(
name|zipEntry
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|children
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|f
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|child
range|:
name|children
control|)
block|{
name|addFileToZip
argument_list|(
name|tOut
argument_list|,
name|child
argument_list|,
name|entryName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|useSymLinks
operator|&&
name|Files
operator|.
name|isSymbolicLink
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|String
name|entryName
init|=
name|base
operator|+
name|f
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ZipArchiveEntry
name|zipEntry
init|=
operator|new
name|ZipArchiveEntry
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
name|zipEntry
operator|.
name|setUnixMode
argument_list|(
name|UnixStat
operator|.
name|LINK_FLAG
operator||
name|UnixStat
operator|.
name|DEFAULT_FILE_PERM
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|putArchiveEntry
argument_list|(
name|zipEntry
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|write
argument_list|(
name|Files
operator|.
name|readSymbolicLink
argument_list|(
name|f
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|entryName
init|=
name|base
operator|+
name|f
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ZipArchiveEntry
name|zipEntry
init|=
operator|new
name|ZipArchiveEntry
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
name|zipEntry
operator|.
name|setSize
argument_list|(
name|Files
operator|.
name|size
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|entryName
operator|.
name|contains
argument_list|(
literal|"/bin/"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|entryName
operator|.
name|endsWith
argument_list|(
literal|".bat"
argument_list|)
condition|)
block|{
name|zipEntry
operator|.
name|setUnixMode
argument_list|(
literal|0755
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|zipEntry
operator|.
name|setUnixMode
argument_list|(
literal|0644
argument_list|)
expr_stmt|;
block|}
block|}
name|tOut
operator|.
name|putArchiveEntry
argument_list|(
name|zipEntry
argument_list|)
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|f
argument_list|,
name|tOut
argument_list|)
expr_stmt|;
name|tOut
operator|.
name|closeArchiveEntry
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

