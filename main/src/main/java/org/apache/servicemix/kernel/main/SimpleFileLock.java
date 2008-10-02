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
name|servicemix
operator|.
name|kernel
operator|.
name|main
package|;
end_package

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
name|nio
operator|.
name|channels
operator|.
name|FileLock
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

begin_class
specifier|public
class|class
name|SimpleFileLock
implements|implements
name|Lock
block|{
specifier|private
name|RandomAccessFile
name|lockFile
decl_stmt|;
specifier|private
name|FileLock
name|lock
decl_stmt|;
specifier|public
name|SimpleFileLock
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
try|try
block|{
name|File
name|base
init|=
operator|new
name|File
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"servicemix.base"
argument_list|)
argument_list|)
decl_stmt|;
name|lockFile
operator|=
operator|new
name|RandomAccessFile
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"lock"
argument_list|)
argument_list|,
literal|"rw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not create file lock"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|lock
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|lock
operator|==
literal|null
condition|)
block|{
name|lock
operator|=
name|lockFile
operator|.
name|getChannel
argument_list|()
operator|.
name|tryLock
argument_list|()
expr_stmt|;
block|}
return|return
name|lock
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|release
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|lock
operator|!=
literal|null
operator|&&
name|lock
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|lock
operator|.
name|channel
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|lock
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

