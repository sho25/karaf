begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|utils
package|;
end_package

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
name|nio
operator|.
name|channels
operator|.
name|FileChannel
import|;
end_import

begin_class
specifier|public
class|class
name|IoUtils
block|{
specifier|private
specifier|static
specifier|final
name|long
name|FILE_COPY_BUFFER_SIZE
init|=
literal|1024
operator|*
literal|30
decl_stmt|;
specifier|public
specifier|static
name|void
name|deleteRecursive
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|children
init|=
name|file
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|child
range|:
name|children
control|)
block|{
name|deleteRecursive
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copyDirectory
parameter_list|(
specifier|final
name|File
name|srcDir
parameter_list|,
specifier|final
name|File
name|destDir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|srcDir
operator|==
literal|null
operator|||
operator|!
name|srcDir
operator|.
name|exists
argument_list|()
condition|)
return|return;
if|if
condition|(
name|destDir
operator|==
literal|null
operator|||
operator|!
name|destDir
operator|.
name|exists
argument_list|()
condition|)
name|destDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// recurse
specifier|final
name|File
index|[]
name|srcFiles
init|=
name|srcDir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcFiles
operator|==
literal|null
condition|)
block|{
comment|// null if abstract pathname does not denote a directory, or if an I/O error occurs
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to list contents of "
operator|+
name|srcDir
argument_list|)
throw|;
block|}
if|if
condition|(
name|destDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|destDir
operator|.
name|isDirectory
argument_list|()
operator|==
literal|false
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Destination '"
operator|+
name|destDir
operator|+
literal|"' exists but is not a directory"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|destDir
operator|.
name|mkdirs
argument_list|()
operator|&&
operator|!
name|destDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Destination '"
operator|+
name|destDir
operator|+
literal|"' directory cannot be created"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|destDir
operator|.
name|canWrite
argument_list|()
operator|==
literal|false
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Destination '"
operator|+
name|destDir
operator|+
literal|"' cannot be written to"
argument_list|)
throw|;
block|}
for|for
control|(
specifier|final
name|File
name|srcFile
range|:
name|srcFiles
control|)
block|{
specifier|final
name|File
name|dstFile
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
name|srcFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|srcFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|copyDirectory
argument_list|(
name|srcFile
argument_list|,
name|dstFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|copyFile
argument_list|(
name|srcFile
argument_list|,
name|dstFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|copyFile
parameter_list|(
specifier|final
name|File
name|srcFile
parameter_list|,
specifier|final
name|File
name|destFile
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|destFile
operator|.
name|exists
argument_list|()
operator|&&
name|destFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Destination '"
operator|+
name|destFile
operator|+
literal|"' exists but is a directory"
argument_list|)
throw|;
block|}
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
name|FileChannel
name|input
init|=
literal|null
decl_stmt|;
name|FileChannel
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|srcFile
argument_list|)
expr_stmt|;
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|destFile
argument_list|)
expr_stmt|;
name|input
operator|=
name|fis
operator|.
name|getChannel
argument_list|()
expr_stmt|;
name|output
operator|=
name|fos
operator|.
name|getChannel
argument_list|()
expr_stmt|;
specifier|final
name|long
name|size
init|=
name|input
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// TODO See IO-386
name|long
name|pos
init|=
literal|0
decl_stmt|;
name|long
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|pos
operator|<
name|size
condition|)
block|{
specifier|final
name|long
name|remain
init|=
name|size
operator|-
name|pos
decl_stmt|;
name|count
operator|=
name|remain
operator|>
name|FILE_COPY_BUFFER_SIZE
condition|?
name|FILE_COPY_BUFFER_SIZE
else|:
name|remain
expr_stmt|;
specifier|final
name|long
name|bytesCopied
init|=
name|output
operator|.
name|transferFrom
argument_list|(
name|input
argument_list|,
name|pos
argument_list|,
name|count
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytesCopied
operator|==
literal|0
condition|)
block|{
comment|// IO-385 - can happen if file is truncated after caching the size
break|break;
comment|// ensure we don't loop forever
block|}
name|pos
operator|+=
name|bytesCopied
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
block|}
if|if
condition|(
name|fos
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
block|}
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
block|}
if|if
condition|(
name|fis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
block|}
block|}
specifier|final
name|long
name|srcLen
init|=
name|srcFile
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// TODO See IO-386
specifier|final
name|long
name|dstLen
init|=
name|destFile
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// TODO See IO-386
if|if
condition|(
name|srcLen
operator|!=
name|dstLen
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to copy full contents from '"
operator|+
name|srcFile
operator|+
literal|"' to '"
operator|+
name|destFile
operator|+
literal|"' Expected length: "
operator|+
name|srcLen
operator|+
literal|" Actual: "
operator|+
name|dstLen
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

