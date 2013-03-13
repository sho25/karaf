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
name|karaf
operator|.
name|util
operator|.
name|properties
package|;
end_package

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
name|RandomAccessFile
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
name|FileLock
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
name|utils
operator|.
name|properties
operator|.
name|Properties
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|FileLockUtils
block|{
specifier|private
name|FileLockUtils
parameter_list|()
block|{ }
specifier|public
specifier|static
interface|interface
name|Runnable
block|{
name|void
name|run
parameter_list|(
name|RandomAccessFile
name|file
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
specifier|public
specifier|static
interface|interface
name|Callable
parameter_list|<
name|T
parameter_list|>
block|{
name|T
name|call
parameter_list|(
name|RandomAccessFile
name|file
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
specifier|public
specifier|static
interface|interface
name|RunnableWithProperties
block|{
name|void
name|run
parameter_list|(
name|Properties
name|properties
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
specifier|public
specifier|static
interface|interface
name|CallableWithProperties
parameter_list|<
name|T
parameter_list|>
block|{
name|T
name|call
parameter_list|(
name|Properties
name|properties
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
specifier|public
specifier|static
name|void
name|execute
parameter_list|(
name|File
name|file
parameter_list|,
name|Runnable
name|callback
parameter_list|)
throws|throws
name|IOException
block|{
name|RandomAccessFile
name|raf
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
decl_stmt|;
try|try
block|{
name|FileLock
name|lock
init|=
name|raf
operator|.
name|getChannel
argument_list|()
operator|.
name|lock
argument_list|()
decl_stmt|;
try|try
block|{
name|callback
operator|.
name|run
argument_list|(
name|raf
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|File
name|file
parameter_list|,
name|Callable
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
throws|throws
name|IOException
block|{
name|RandomAccessFile
name|raf
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
decl_stmt|;
try|try
block|{
name|FileLock
name|lock
init|=
name|raf
operator|.
name|getChannel
argument_list|()
operator|.
name|lock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|callback
operator|.
name|call
argument_list|(
name|raf
argument_list|)
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|execute
parameter_list|(
name|File
name|file
parameter_list|,
specifier|final
name|RunnableWithProperties
name|callback
parameter_list|)
throws|throws
name|IOException
block|{
name|execute
argument_list|(
name|file
argument_list|,
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|(
name|RandomAccessFile
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|file
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
name|file
operator|.
name|readFully
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
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
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|run
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|props
operator|.
name|store
argument_list|(
name|baos
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|file
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|file
operator|.
name|write
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|File
name|file
parameter_list|,
specifier|final
name|CallableWithProperties
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|execute
argument_list|(
name|file
argument_list|,
operator|new
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|public
name|T
name|call
parameter_list|(
name|RandomAccessFile
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|file
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
name|file
operator|.
name|readFully
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
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
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
name|T
name|result
init|=
name|callback
operator|.
name|call
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|props
operator|.
name|store
argument_list|(
name|baos
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|file
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|file
operator|.
name|write
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

