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
name|deployer
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|InputStreamReader
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
name|URLConnection
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
name|Properties
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
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|DeployerUtils
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
name|XmlUtils
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|NodeList
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
name|Constants
import|;
end_import

begin_class
specifier|public
class|class
name|BlueprintTransformer
block|{
specifier|public
specifier|static
name|void
name|transform
parameter_list|(
name|URL
name|url
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Build dom document
name|Document
name|doc
init|=
name|parse
argument_list|(
name|url
argument_list|)
decl_stmt|;
comment|// Heuristicly retrieve name and version
name|String
name|name
init|=
name|getPath
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|str
init|=
name|DeployerUtils
operator|.
name|extractNameVersionType
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// Create manifest
name|Manifest
name|m
init|=
operator|new
name|Manifest
argument_list|()
decl_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_MANIFESTVERSION
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|str
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
name|str
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|String
name|importPkgs
init|=
name|getImportPackages
argument_list|(
name|analyze
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|importPkgs
operator|!=
literal|null
operator|&&
name|importPkgs
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|IMPORT_PACKAGE
argument_list|,
name|importPkgs
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
comment|// Extract manifest entries from the DOM
name|NodeList
name|l
init|=
name|doc
operator|.
name|getElementsByTagName
argument_list|(
literal|"manifest"
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|l
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|e
init|=
operator|(
name|Element
operator|)
name|l
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|e
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|text
operator|.
name|trim
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Enumeration
name|en
init|=
name|props
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|en
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|k
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|v
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// get original last modification date
name|long
name|lastModified
init|=
name|getLastModified
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|JarOutputStream
name|out
init|=
operator|new
name|JarOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|ZipEntry
name|e
init|=
operator|new
name|ZipEntry
argument_list|(
name|JarFile
operator|.
name|MANIFEST_NAME
argument_list|)
decl_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|m
operator|.
name|write
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"OSGI-INF/"
argument_list|)
expr_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"OSGI-INF/blueprint/"
argument_list|)
expr_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
comment|// check .xml file extension
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|name
operator|+=
literal|".xml"
expr_stmt|;
block|}
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"OSGI-INF/blueprint/"
operator|+
name|name
argument_list|)
expr_stmt|;
name|e
operator|.
name|setTime
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// Copy the new DOM
name|XmlUtils
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|analyze
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|refers
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Result
name|r
init|=
operator|new
name|StreamResult
argument_list|(
name|bout
argument_list|)
decl_stmt|;
name|XmlUtils
operator|.
name|transform
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|BlueprintTransformer
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"extract.xsl"
argument_list|)
argument_list|)
argument_list|,
name|source
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bin
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bout
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|bout
operator|.
name|close
argument_list|()
expr_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|bin
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|br
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|parts
index|[]
init|=
name|line
operator|.
name|split
argument_list|(
literal|"\\s*,\\s*"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|int
name|n
init|=
name|part
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|String
name|pkg
init|=
name|part
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pkg
operator|.
name|startsWith
argument_list|(
literal|"java."
argument_list|)
condition|)
block|{
name|refers
operator|.
name|add
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|br
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|refers
return|;
block|}
specifier|protected
specifier|static
name|String
name|getImportPackages
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|packages
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packages
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
specifier|static
name|Document
name|parse
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
return|return
name|XmlUtils
operator|.
name|parse
argument_list|(
name|is
argument_list|)
return|;
block|}
block|}
specifier|protected
specifier|static
name|long
name|getLastModified
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|URLConnection
name|urlConnection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|urlConnection
operator|.
name|getInputStream
argument_list|()
init|)
block|{
return|return
name|urlConnection
operator|.
name|getLastModified
argument_list|()
return|;
block|}
block|}
specifier|protected
specifier|static
name|String
name|getPath
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mvn"
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|url
operator|.
name|toExternalForm
argument_list|()
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|groupId
decl_stmt|;
name|String
name|artifactId
decl_stmt|;
name|String
name|version
decl_stmt|;
name|String
name|type
decl_stmt|;
name|String
name|qualifier
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
argument_list|<
literal|3
operator|||
name|parts
operator|.
name|length
argument_list|>
literal|5
condition|)
block|{
return|return
name|url
operator|.
name|getPath
argument_list|()
return|;
block|}
name|groupId
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|artifactId
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
name|version
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
name|type
operator|=
operator|(
name|parts
operator|.
name|length
operator|>=
literal|4
operator|)
condition|?
literal|"."
operator|+
name|parts
index|[
literal|3
index|]
else|:
literal|".jar"
expr_stmt|;
name|qualifier
operator|=
operator|(
name|parts
operator|.
name|length
operator|>=
literal|5
operator|)
condition|?
literal|"-"
operator|+
name|parts
index|[
literal|4
index|]
else|:
literal|""
expr_stmt|;
return|return
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
operator|+
name|artifactId
operator|+
literal|"-"
operator|+
name|version
operator|+
name|qualifier
operator|+
name|type
return|;
block|}
return|return
name|url
operator|.
name|getPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

