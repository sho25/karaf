begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|obr
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|jar
operator|.
name|JarEntry
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
name|JarInputStream
import|;
end_import

begin_class
specifier|public
class|class
name|FileUtil
block|{
specifier|public
specifier|static
name|void
name|downloadSource
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|URL
name|srcURL
parameter_list|,
name|String
name|dirStr
parameter_list|,
name|boolean
name|extract
parameter_list|)
block|{
comment|// Get the file name from the URL.
name|String
name|fileName
init|=
operator|(
name|srcURL
operator|.
name|getFile
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|>
literal|0
operator|)
condition|?
name|srcURL
operator|.
name|getFile
argument_list|()
operator|.
name|substring
argument_list|(
name|srcURL
operator|.
name|getFile
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
else|:
name|srcURL
operator|.
name|getFile
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Connecting..."
argument_list|)
expr_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|dirStr
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|dir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"Destination directory does not exist."
argument_list|)
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|fileName
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|URLConnection
name|conn
init|=
name|srcURL
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|int
name|total
init|=
name|conn
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|conn
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|total
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Downloading "
operator|+
name|fileName
operator|+
literal|" ( "
operator|+
name|total
operator|+
literal|" bytes )."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Downloading "
operator|+
name|fileName
operator|+
literal|"."
argument_list|)
expr_stmt|;
block|}
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
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|len
init|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
init|;
name|len
operator|>
literal|0
condition|;
name|len
operator|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
control|)
block|{
name|count
operator|+=
name|len
expr_stmt|;
name|os
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
block|}
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|extract
condition|)
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Extracting..."
argument_list|)
expr_stmt|;
name|unjar
argument_list|(
name|jis
argument_list|,
name|dir
argument_list|)
expr_stmt|;
name|jis
operator|.
name|close
argument_list|()
expr_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|err
operator|.
name|println
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|unjar
parameter_list|(
name|JarInputStream
name|jis
parameter_list|,
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Reusable buffer.
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
comment|// Loop through JAR entries.
for|for
control|(
name|JarEntry
name|je
init|=
name|jis
operator|.
name|getNextJarEntry
argument_list|()
init|;
name|je
operator|!=
literal|null
condition|;
name|je
operator|=
name|jis
operator|.
name|getNextJarEntry
argument_list|()
control|)
block|{
if|if
condition|(
name|je
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"JAR resource cannot contain absolute paths."
argument_list|)
throw|;
block|}
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|je
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check to see if the JAR entry is a directory.
if|if
condition|(
name|je
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|target
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|target
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to create target directory: "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
comment|// Just continue since directories do not have content to copy.
continue|continue;
block|}
name|int
name|lastIndex
init|=
name|je
operator|.
name|getName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|lastIndex
operator|>=
literal|0
operator|)
condition|?
name|je
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|lastIndex
operator|+
literal|1
argument_list|)
else|:
name|je
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|destination
init|=
operator|(
name|lastIndex
operator|>=
literal|0
operator|)
condition|?
name|je
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastIndex
argument_list|)
else|:
literal|""
decl_stmt|;
comment|// JAR files use '/', so convert it to platform separator.
name|destination
operator|=
name|destination
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
expr_stmt|;
name|copy
argument_list|(
name|jis
argument_list|,
name|dir
argument_list|,
name|name
argument_list|,
name|destination
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|File
name|dir
parameter_list|,
name|String
name|destName
parameter_list|,
name|String
name|destDir
parameter_list|,
name|byte
index|[]
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|destDir
operator|==
literal|null
condition|)
block|{
name|destDir
operator|=
literal|""
expr_stmt|;
block|}
comment|// Make sure the target directory exists and
comment|// that is actually a directory.
name|File
name|targetDir
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|destDir
argument_list|)
decl_stmt|;
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
name|IOException
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
name|IOException
argument_list|(
literal|"Target is not a directory: "
operator|+
name|targetDir
argument_list|)
throw|;
block|}
name|BufferedOutputStream
name|bos
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
name|destName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|count
operator|=
name|is
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
block|}
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

