begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jpm
operator|.
name|impl
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
name|InterruptedIOException
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
name|PrintStream
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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
import|;
end_import

begin_class
specifier|public
class|class
name|ScriptUtils
block|{
specifier|public
specifier|static
name|int
name|execute
parameter_list|(
name|String
name|name
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|script
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|"jpm."
argument_list|,
literal|".script"
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isWindows
argument_list|()
condition|)
block|{
name|String
name|res
init|=
literal|"windows/"
operator|+
name|name
operator|+
literal|".vbs"
decl_stmt|;
name|ScriptUtils
operator|.
name|copyFilteredResource
argument_list|(
name|res
argument_list|,
name|script
argument_list|,
name|props
argument_list|)
expr_stmt|;
return|return
name|executeProcess
argument_list|(
operator|new
name|java
operator|.
name|lang
operator|.
name|ProcessBuilder
argument_list|(
literal|"cscript"
argument_list|,
literal|"/NOLOGO"
argument_list|,
literal|"//E:vbs"
argument_list|,
name|script
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|res
init|=
literal|"unix/"
operator|+
name|name
operator|+
literal|".sh"
decl_stmt|;
name|ScriptUtils
operator|.
name|copyFilteredResource
argument_list|(
name|res
argument_list|,
name|script
argument_list|,
name|props
argument_list|)
expr_stmt|;
return|return
name|executeProcess
argument_list|(
operator|new
name|java
operator|.
name|lang
operator|.
name|ProcessBuilder
argument_list|(
literal|"/bin/sh"
argument_list|,
name|script
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
finally|finally
block|{
name|script
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|int
name|executeProcess
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|ProcessBuilder
name|builder
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|java
operator|.
name|lang
operator|.
name|Process
name|process
init|=
name|builder
operator|.
name|start
argument_list|()
decl_stmt|;
return|return
name|process
operator|.
name|waitFor
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InterruptedIOException
argument_list|()
throw|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copyFilteredResource
parameter_list|(
name|String
name|resource
parameter_list|,
name|File
name|outFile
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|ScriptUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
comment|// Read it line at a time so that we can use the platform line ending when we write it out.
name|PrintStream
name|out
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
name|scanner
operator|.
name|hasNextLine
argument_list|()
condition|)
block|{
name|String
name|line
init|=
name|scanner
operator|.
name|nextLine
argument_list|()
decl_stmt|;
name|line
operator|=
name|filter
argument_list|(
name|line
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|safeClose
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|safeClose
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ignore
parameter_list|)
block|{         }
block|}
specifier|private
specifier|static
name|void
name|safeClose
parameter_list|(
name|OutputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ignore
parameter_list|)
block|{         }
block|}
specifier|private
specifier|static
name|String
name|filter
parameter_list|(
name|String
name|line
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|i
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|p1
init|=
name|line
operator|.
name|indexOf
argument_list|(
name|i
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|p1
operator|>=
literal|0
condition|)
block|{
name|String
name|l1
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p1
argument_list|)
decl_stmt|;
name|String
name|l2
init|=
name|line
operator|.
name|substring
argument_list|(
name|p1
operator|+
name|i
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|line
operator|=
name|l1
operator|+
name|i
operator|.
name|getValue
argument_list|()
operator|+
name|l2
expr_stmt|;
block|}
block|}
return|return
name|line
return|;
block|}
specifier|private
specifier|static
specifier|final
name|boolean
name|windows
decl_stmt|;
static|static
block|{
name|windows
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"windows"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|isWindows
parameter_list|()
block|{
return|return
name|windows
return|;
block|}
specifier|public
specifier|static
name|String
name|getJavaCommandPath
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
argument_list|,
name|isWindows
argument_list|()
condition|?
literal|"bin\\java.exe"
else|:
literal|"bin/java"
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

