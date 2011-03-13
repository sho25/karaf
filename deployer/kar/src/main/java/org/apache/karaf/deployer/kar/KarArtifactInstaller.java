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
name|deployer
operator|.
name|kar
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
name|Enumeration
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
name|ZipEntry
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactInstaller
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_class
specifier|public
class|class
name|KarArtifactInstaller
implements|implements
name|ArtifactInstaller
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FEATURE_CLASSIFIER
init|=
literal|"features"
decl_stmt|;
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KarArtifactInstaller
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KAR_SUFFIX
init|=
literal|".kar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ZIP_SUFFIX
init|=
literal|".zip"
decl_stmt|;
specifier|private
name|String
name|base
init|=
literal|"./"
decl_stmt|;
specifier|private
name|String
name|localRepoPath
init|=
literal|"./target/local-repo"
decl_stmt|;
specifier|private
name|String
name|timestampPath
decl_stmt|;
specifier|private
name|DocumentBuilderFactory
name|dbf
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|()
block|{
name|dbf
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|timestampPath
operator|=
name|localRepoPath
operator|+
name|File
operator|.
name|separator
operator|+
literal|".timestamps"
expr_stmt|;
if|if
condition|(
operator|new
name|File
argument_list|(
name|timestampPath
argument_list|)
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Unable to create directory for Karaf Archive timestamps. Results may vary..."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Karaf archives will be extracted to "
operator|+
name|localRepoPath
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Timestamps for Karaf archives will be extracted to "
operator|+
name|timestampPath
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Karaf archive installer destroyed."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|install
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Check to see if this file has already been extracted. For example, on restart of Karaf,
comment|// we don't necessarily want to re-extract all the Karaf Archives!
comment|//
if|if
condition|(
name|alreadyExtracted
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Ignoring '"
operator|+
name|file
operator|+
literal|"'; timestamp indicates it's already been deployed."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
name|logger
operator|.
name|info
argument_list|(
literal|"Installing "
operator|+
name|file
argument_list|)
expr_stmt|;
name|ZipFile
name|zipFile
init|=
operator|new
name|ZipFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|5
operator|*
literal|1024
index|]
decl_stmt|;
name|Enumeration
argument_list|<
name|ZipEntry
argument_list|>
name|entries
init|=
operator|(
name|Enumeration
argument_list|<
name|ZipEntry
argument_list|>
operator|)
name|zipFile
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|entries
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|ZipEntry
name|entry
init|=
name|entries
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|repoEntryName
init|=
name|getRepoEntryName
argument_list|(
name|entry
argument_list|)
decl_stmt|;
if|if
condition|(
name|repoEntryName
operator|!=
literal|null
condition|)
block|{
name|File
name|extract
init|=
name|extract
argument_list|(
name|zipFile
argument_list|,
name|buffer
argument_list|,
name|entry
argument_list|,
name|repoEntryName
argument_list|,
name|localRepoPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFeaturesRepository
argument_list|(
name|extract
argument_list|)
condition|)
block|{
name|addToFeaturesRepositories
argument_list|(
name|repoEntryName
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"resource"
argument_list|)
condition|)
block|{
name|String
name|resourceEntryName
init|=
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|"resource/"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|extract
argument_list|(
name|zipFile
argument_list|,
name|buffer
argument_list|,
name|entry
argument_list|,
name|resourceEntryName
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
block|}
name|zipFile
operator|.
name|close
argument_list|()
expr_stmt|;
name|updateTimestamp
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|extract
parameter_list|(
name|ZipFile
name|zipFile
parameter_list|,
name|byte
index|[]
name|buffer
parameter_list|,
name|ZipEntry
name|entry
parameter_list|,
name|String
name|repoEntryName
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|extract
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|extract
operator|=
operator|new
name|File
argument_list|(
name|base
operator|+
name|File
operator|.
name|separator
operator|+
name|repoEntryName
argument_list|)
expr_stmt|;
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
name|logger
operator|.
name|debug
argument_list|(
literal|"Creating directory '"
operator|+
name|extract
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|extract
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|extract
operator|=
operator|new
name|File
argument_list|(
name|base
operator|+
name|File
operator|.
name|separator
operator|+
name|repoEntryName
argument_list|)
expr_stmt|;
name|BufferedOutputStream
name|bos
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|extract
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|totalBytes
init|=
literal|0
decl_stmt|;
name|InputStream
name|inputStream
init|=
name|zipFile
operator|.
name|getInputStream
argument_list|(
name|entry
argument_list|)
decl_stmt|;
while|while
condition|(
operator|(
name|count
operator|=
name|inputStream
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
name|totalBytes
operator|+=
name|count
expr_stmt|;
block|}
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
name|logger
operator|.
name|debug
argument_list|(
literal|"Extracted "
operator|+
name|totalBytes
operator|+
literal|" bytes to "
operator|+
name|extract
argument_list|)
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|extract
return|;
block|}
specifier|private
name|String
name|getRepoEntryName
parameter_list|(
name|ZipEntry
name|entry
parameter_list|)
block|{
name|String
name|entryName
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|entryName
operator|.
name|startsWith
argument_list|(
literal|"repository"
argument_list|)
condition|)
block|{
return|return
name|entryName
operator|.
name|substring
argument_list|(
literal|"repository/"
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|entryName
operator|.
name|startsWith
argument_list|(
literal|"META-INF"
argument_list|)
operator|||
name|entryName
operator|.
name|startsWith
argument_list|(
literal|"resources"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|entryName
return|;
block|}
specifier|public
name|void
name|uninstall
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Karaf archive '"
operator|+
name|file
operator|+
literal|"' has been removed; however, it's feature URLs have not been deregistered, and it's bundles are still available in '"
operator|+
name|localRepoPath
operator|+
literal|"'."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|update
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Karaf archive "
operator|+
name|file
operator|+
literal|" has been updated; redeploying."
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|updateTimestamp
parameter_list|(
name|File
name|karafArchive
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|timestamp
init|=
name|getArchiveTimestampFile
argument_list|(
name|karafArchive
argument_list|)
decl_stmt|;
if|if
condition|(
name|timestamp
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
name|logger
operator|.
name|debug
argument_list|(
literal|"Deleting old timestamp file '"
operator|+
name|timestamp
operator|+
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|timestamp
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to delete archive timestamp '"
operator|+
name|timestamp
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Creating timestamp file '"
operator|+
name|timestamp
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|timestamp
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|boolean
name|alreadyExtracted
parameter_list|(
name|File
name|karafArchive
parameter_list|)
block|{
name|File
name|timestamp
init|=
name|getArchiveTimestampFile
argument_list|(
name|karafArchive
argument_list|)
decl_stmt|;
if|if
condition|(
name|timestamp
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|timestamp
operator|.
name|lastModified
argument_list|()
operator|>=
name|karafArchive
operator|.
name|lastModified
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|File
name|getArchiveTimestampFile
parameter_list|(
name|File
name|karafArchive
parameter_list|)
block|{
name|File
name|timestampDir
init|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|localRepoPath
argument_list|)
argument_list|,
literal|".timestamps"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|timestampDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|timestampDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|File
argument_list|(
name|timestampDir
argument_list|,
name|karafArchive
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isFeaturesRepository
parameter_list|(
name|File
name|artifact
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|artifact
operator|.
name|isFile
argument_list|()
operator|&&
name|artifact
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|parse
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|uri
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
operator|||
name|uri
operator|.
name|startsWith
argument_list|(
literal|"http://karaf.apache.org/xmlns/features/v"
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
name|logger
operator|.
name|debug
argument_list|(
literal|"File "
operator|+
name|artifact
operator|.
name|getName
argument_list|()
operator|+
literal|" is not a features file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|Document
name|parse
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|ErrorHandler
argument_list|()
block|{
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{             }
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{             }
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|exception
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|db
operator|.
name|parse
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|private
name|void
name|addToFeaturesRepositories
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|URI
name|mvnUri
init|=
name|pathToMvnUri
argument_list|(
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|featuresService
operator|.
name|addRepository
argument_list|(
name|mvnUri
argument_list|)
expr_stmt|;
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
name|logger
operator|.
name|info
argument_list|(
literal|"Added feature repository '"
operator|+
name|mvnUri
operator|+
literal|"'."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Unable to add repository '"
operator|+
name|mvnUri
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
name|URI
name|pathToMvnUri
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
index|[]
name|bits
init|=
name|path
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|FEATURE_CLASSIFIER
decl_stmt|;
name|String
name|artifactType
init|=
literal|"xml"
decl_stmt|;
name|String
name|version
init|=
name|bits
index|[
name|bits
operator|.
name|length
operator|-
literal|2
index|]
decl_stmt|;
name|String
name|artifactId
init|=
name|bits
index|[
name|bits
operator|.
name|length
operator|-
literal|3
index|]
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"mvn:"
argument_list|)
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
name|bits
operator|.
name|length
operator|-
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|bits
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|bits
operator|.
name|length
operator|-
literal|4
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
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
operator|.
name|append
argument_list|(
name|artifactType
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|URI
name|mvnUri
init|=
name|URI
operator|.
name|create
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|mvnUri
return|;
block|}
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|File
name|file
parameter_list|)
block|{
comment|// If the file ends with .kar, then we can handle it!
comment|//
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|KAR_SUFFIX
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Found a .kar file to deploy."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// Otherwise, check to see if it's a zip file containing a META-INF/KARAF.MF manifest.
comment|//
elseif|else
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
name|ZIP_SUFFIX
argument_list|)
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Found a .zip file to deploy; checking contents to see if it's a Karaf archive."
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|new
name|ZipFile
argument_list|(
name|file
argument_list|)
operator|.
name|getEntry
argument_list|(
literal|"META-INF/KARAF.MF"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Found a Karaf archive with .zip prefix; will deploy."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Problem extracting zip file '"
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"'; ignoring."
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
specifier|public
name|boolean
name|deleteLocalRepository
parameter_list|()
block|{
return|return
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|localRepoPath
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|deleteDirectory
parameter_list|(
name|File
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|exists
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|path
operator|.
name|listFiles
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
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|files
index|[
name|i
index|]
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|deleteDirectory
argument_list|(
name|files
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|files
index|[
name|i
index|]
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
operator|(
name|path
operator|.
name|delete
argument_list|()
operator|)
return|;
block|}
specifier|public
name|void
name|setBasePath
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
block|}
specifier|public
name|void
name|setLocalRepoPath
parameter_list|(
name|String
name|localRepoPath
parameter_list|)
block|{
name|this
operator|.
name|localRepoPath
operator|=
name|localRepoPath
expr_stmt|;
block|}
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
block|}
end_class

end_unit

