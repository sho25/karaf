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
name|kar
operator|.
name|internal
package|;
end_package

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
name|karaf
operator|.
name|kar
operator|.
name|KarService
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
name|EnumSet
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
name|List
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

begin_comment
comment|/**  * Implementation of the KAR service.  */
end_comment

begin_class
specifier|public
class|class
name|KarServiceImpl
implements|implements
name|KarService
block|{
specifier|public
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KarServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|storage
init|=
literal|"./target/data/kar"
decl_stmt|;
comment|// ${KARAF.DATA}/kar
specifier|private
name|String
name|base
init|=
literal|"./"
decl_stmt|;
specifier|private
name|String
name|localRepo
init|=
literal|"./target/local-repo"
decl_stmt|;
comment|// ${KARAF.BASE}/system
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|private
name|DocumentBuilderFactory
name|dbf
decl_stmt|;
comment|/**      * Init method.      *      * @throws Exception in case of init failure.      */
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
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
name|File
name|karStorageDir
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|karStorageDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Create the KAR storage directory"
argument_list|)
expr_stmt|;
name|karStorageDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|karStorageDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"KAR storage "
operator|+
name|storage
operator|+
literal|" is not a directory"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|install
parameter_list|(
name|URI
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURL
argument_list|()
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|karName
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Installing KAR {} from {}"
argument_list|,
name|karName
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Check if the KAR file already exists in the storage directory"
argument_list|)
expr_stmt|;
name|File
name|karStorage
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|)
decl_stmt|;
name|File
name|karFile
init|=
operator|new
name|File
argument_list|(
name|karStorage
argument_list|,
name|karName
argument_list|)
decl_stmt|;
if|if
condition|(
name|karFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"The KAR file "
operator|+
name|karName
operator|+
literal|" is already installed. Override it."
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Copy the KAR file from {} to {}"
argument_list|,
name|url
argument_list|,
name|karFile
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|copy
argument_list|(
name|url
argument_list|,
name|karFile
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Uncompress the KAR file {} into the system repository"
argument_list|,
name|karName
argument_list|)
expr_stmt|;
name|ZipFile
name|zipFile
init|=
operator|new
name|ZipFile
argument_list|(
name|karFile
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositoriesInKar
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
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
name|entry
argument_list|,
name|repoEntryName
argument_list|,
name|localRepo
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
name|extract
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|featuresRepositoriesInKar
operator|.
name|add
argument_list|(
name|extract
operator|.
name|toURI
argument_list|()
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
name|entry
argument_list|,
name|resourceEntryName
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
block|}
name|installFeatures
argument_list|(
name|featuresRepositoriesInKar
argument_list|)
expr_stmt|;
name|zipFile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|uninstall
parameter_list|(
name|String
name|karName
parameter_list|)
throws|throws
name|Exception
block|{
name|uninstall
argument_list|(
name|karName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|uninstall
parameter_list|(
name|String
name|karName
parameter_list|,
name|boolean
name|clean
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|karStorage
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|)
decl_stmt|;
name|File
name|karFile
init|=
operator|new
name|File
argument_list|(
name|karStorage
argument_list|,
name|karName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|karFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The KAR "
operator|+
name|karName
operator|+
literal|" is not installed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|clean
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Looking for KAR entries to purge the local repository"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositories
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|ZipFile
name|zipFile
init|=
operator|new
name|ZipFile
argument_list|(
name|karFile
argument_list|)
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
name|toDelete
init|=
operator|new
name|File
argument_list|(
name|localRepo
operator|+
name|File
operator|.
name|separator
operator|+
name|repoEntryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFeaturesRepository
argument_list|(
name|toDelete
argument_list|)
condition|)
block|{
name|featuresRepositories
operator|.
name|add
argument_list|(
name|toDelete
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|toDelete
operator|.
name|isFile
argument_list|()
operator|&&
name|toDelete
operator|.
name|exists
argument_list|()
condition|)
block|{
name|toDelete
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
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
name|File
name|toDelete
init|=
operator|new
name|File
argument_list|(
name|base
operator|+
name|File
operator|.
name|separator
operator|+
name|resourceEntryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|toDelete
operator|.
name|isFile
argument_list|()
operator|&&
name|toDelete
operator|.
name|exists
argument_list|()
condition|)
block|{
name|toDelete
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|zipFile
operator|.
name|close
argument_list|()
expr_stmt|;
name|uninstallFeatures
argument_list|(
name|featuresRepositories
argument_list|)
expr_stmt|;
for|for
control|(
name|URI
name|featuresRepository
range|:
name|featuresRepositories
control|)
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|featuresRepository
argument_list|)
expr_stmt|;
name|File
name|toDelete
init|=
operator|new
name|File
argument_list|(
name|featuresRepository
argument_list|)
decl_stmt|;
if|if
condition|(
name|toDelete
operator|.
name|exists
argument_list|()
operator|&&
name|toDelete
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|toDelete
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|karFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|karStorage
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|kars
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|kar
range|:
name|karStorage
operator|.
name|listFiles
argument_list|()
control|)
block|{
name|kars
operator|.
name|add
argument_list|(
name|kar
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|kars
return|;
block|}
comment|/**      * Create a destination file using a source URL.      *       * @param url the source URL.       * @param file the destination file      * @throws Exception in case of copy failure      */
specifier|private
name|void
name|copy
parameter_list|(
name|URI
name|url
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|url
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|count
init|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
name|count
operator|>=
literal|0
condition|)
block|{
name|fos
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
name|count
operator|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fos
operator|!=
literal|null
condition|)
block|{
name|fos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Get the entry name filtering the repository  folder.      *      * @param entry the "normal" entry name.      * @return the entry with the repository folder filtered.      */
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
comment|/**      * Extract an entry from a KAR file.      *       * @param zipFile the KAR file (zip file).      * @param zipEntry the entry in the KAR file.      * @param repoEntryName the target extract name.      * @param base the base directory where to extract the file.      * @return the extracted file.      * @throws Exception in the extraction fails.      */
specifier|private
name|File
name|extract
parameter_list|(
name|ZipFile
name|zipFile
parameter_list|,
name|ZipEntry
name|zipEntry
parameter_list|,
name|String
name|repoEntryName
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|extract
decl_stmt|;
if|if
condition|(
name|zipEntry
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Creating directory {}"
argument_list|,
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
name|extract
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|InputStream
name|in
init|=
name|zipFile
operator|.
name|getInputStream
argument_list|(
name|zipEntry
argument_list|)
decl_stmt|;
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|extract
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|count
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|int
name|totalBytes
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|count
operator|>=
literal|0
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
name|count
argument_list|)
expr_stmt|;
name|totalBytes
operator|+=
name|count
expr_stmt|;
name|count
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Extracted {} bytes to {}"
argument_list|,
name|totalBytes
argument_list|,
name|extract
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|extract
return|;
block|}
comment|/**      * Check if a file is a features XML.      *      * @param artifact the file to check.      * @return true if the artifact is a features XML, false else.      */
specifier|private
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"File '{}' is not a features file."
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Parse a features XML.      *      * @param artifact the features XML to parse.      * @return the parsed document.      * @throws Exception in case of parsing failure.      */
specifier|private
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
comment|/**      * Add an URI to the list of features repositories.      *      * @param uri the URI to add.      * @throws Exception in case of add failure.      */
specifier|private
name|void
name|addToFeaturesRepositories
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|featuresService
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Added feature repository '{}'"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Unable to add repository '{}'"
argument_list|,
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Install all features contained in the list of features XML.      *      * @param featuresRepositories the list of features XML.      */
specifier|private
name|void
name|installFeatures
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositories
parameter_list|)
block|{
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
for|for
control|(
name|URI
name|karFeatureRepoUri
range|:
name|featuresRepositories
control|)
block|{
if|if
condition|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|karFeatureRepoUri
argument_list|)
condition|)
block|{
try|try
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
try|try
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Unable to install Kar feature {}"
argument_list|,
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
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't get features for KAR {}"
argument_list|,
name|karFeatureRepoUri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Uninstall all features contained in the list of features XML.      *      * @param featuresRepositories the list of features XML.      */
specifier|private
name|void
name|uninstallFeatures
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositories
parameter_list|)
block|{
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
for|for
control|(
name|URI
name|karFeatureRepoUri
range|:
name|featuresRepositories
control|)
block|{
if|if
condition|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|karFeatureRepoUri
argument_list|)
condition|)
block|{
try|try
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
try|try
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|feature
operator|.
name|getVersion
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
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Unable to uninstall Kar feature {}"
argument_list|,
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
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't get features for KAR {}"
argument_list|,
name|karFeatureRepoUri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|FeaturesService
name|getFeaturesService
parameter_list|()
block|{
return|return
name|featuresService
return|;
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
specifier|public
name|String
name|getStorage
parameter_list|()
block|{
return|return
name|storage
return|;
block|}
specifier|public
name|void
name|setStorage
parameter_list|(
name|String
name|storage
parameter_list|)
block|{
name|this
operator|.
name|storage
operator|=
name|storage
expr_stmt|;
block|}
specifier|public
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
specifier|public
name|void
name|setBase
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
name|String
name|getLocalRepo
parameter_list|()
block|{
return|return
name|localRepo
return|;
block|}
specifier|public
name|void
name|setLocalRepo
parameter_list|(
name|String
name|localRepo
parameter_list|)
block|{
name|this
operator|.
name|localRepo
operator|=
name|localRepo
expr_stmt|;
block|}
block|}
end_class

end_unit

