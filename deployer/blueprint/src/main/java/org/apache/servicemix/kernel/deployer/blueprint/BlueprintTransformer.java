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
name|servicemix
operator|.
name|kernel
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
name|Transformer
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
name|TransformerFactory
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
specifier|static
name|Transformer
name|transformer
decl_stmt|;
specifier|static
name|DocumentBuilderFactory
name|dbf
decl_stmt|;
specifier|static
name|TransformerFactory
name|tf
decl_stmt|;
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
name|url
operator|.
name|getPath
argument_list|()
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
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// Copy the new DOM
if|if
condition|(
name|tf
operator|==
literal|null
condition|)
block|{
name|tf
operator|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
name|tf
operator|.
name|newTransformer
argument_list|()
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
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_VERSION
init|=
literal|"0.0.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|ARTIFACT_MATCHER
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(.+)(?:-(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?(?:[^a-zA-Z0-9](.*))?)(?:\\.([^\\.]+))"
argument_list|,
name|Pattern
operator|.
name|DOTALL
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|FUZZY_MODIFIDER
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(?:\\d+[.-])*(.*)"
argument_list|,
name|Pattern
operator|.
name|DOTALL
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
index|[]
name|extractNameVersionType
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|ARTIFACT_MATCHER
operator|.
name|matcher
argument_list|(
name|url
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
operator|new
name|String
index|[]
block|{
name|url
block|,
name|DEFAULT_VERSION
block|}
return|;
block|}
else|else
block|{
comment|//System.err.println(m.groupCount());
comment|//for (int i = 1; i<= m.groupCount(); i++) {
comment|//    System.err.println("Group " + i + ": " + m.group(i));
comment|//}
name|StringBuffer
name|v
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|d1
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|d2
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|d3
init|=
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|String
name|d4
init|=
name|m
operator|.
name|group
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|String
name|d5
init|=
name|m
operator|.
name|group
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|String
name|d6
init|=
name|m
operator|.
name|group
argument_list|(
literal|6
argument_list|)
decl_stmt|;
if|if
condition|(
name|d2
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
name|d2
argument_list|)
expr_stmt|;
if|if
condition|(
name|d3
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|v
operator|.
name|append
argument_list|(
name|d3
argument_list|)
expr_stmt|;
if|if
condition|(
name|d4
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|v
operator|.
name|append
argument_list|(
name|d4
argument_list|)
expr_stmt|;
if|if
condition|(
name|d5
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|cleanupModifier
argument_list|(
name|v
argument_list|,
name|d5
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|d5
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|".0."
argument_list|)
expr_stmt|;
name|cleanupModifier
argument_list|(
name|v
argument_list|,
name|d5
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|d5
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|".0.0."
argument_list|)
expr_stmt|;
name|cleanupModifier
argument_list|(
name|v
argument_list|,
name|d5
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|String
index|[]
block|{
name|d1
block|,
name|v
operator|.
name|toString
argument_list|()
block|,
name|d6
block|}
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|cleanupModifier
parameter_list|(
name|StringBuffer
name|result
parameter_list|,
name|String
name|modifier
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|FUZZY_MODIFIDER
operator|.
name|matcher
argument_list|(
name|modifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|modifier
operator|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|modifier
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|modifier
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|c
operator|>=
literal|'0'
operator|&&
name|c
operator|<=
literal|'9'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'z'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|)
operator|||
name|c
operator|==
literal|'_'
operator|||
name|c
operator|==
literal|'-'
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|transformer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|tf
operator|==
literal|null
condition|)
block|{
name|tf
operator|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
name|Source
name|s
init|=
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
decl_stmt|;
name|transformer
operator|=
name|tf
operator|.
name|newTransformer
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
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
name|transformer
operator|.
name|transform
argument_list|(
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|n
init|=
name|parts
index|[
name|i
index|]
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
name|refers
operator|.
name|add
argument_list|(
name|parts
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|n
argument_list|)
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|dbf
operator|==
literal|null
condition|)
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
block|}
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|db
operator|.
name|parse
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|void
name|copyInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|len
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
name|len
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
name|len
argument_list|)
expr_stmt|;
name|len
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

