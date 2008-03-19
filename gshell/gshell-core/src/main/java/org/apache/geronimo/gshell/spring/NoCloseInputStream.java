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
name|geronimo
operator|.
name|gshell
operator|.
name|spring
package|;
end_package

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
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedOutputStream
import|;
end_import

begin_comment
comment|/**  * Wrap an InputStream in a complex structure so that it can  * be reused and closed even when a read() method is blocking.  *  * This stream uses a PipedInputStream / PipedOutputStream to  * decouple the read InputStream.  When the close() method is  * called, the pipe will be broken, and a single character will  * be eaten from the reader thread.  */
end_comment

begin_class
specifier|public
class|class
name|NoCloseInputStream
extends|extends
name|PipedInputStream
block|{
specifier|private
specifier|final
name|InputStream
name|in
decl_stmt|;
specifier|private
specifier|final
name|PipedOutputStream
name|pos
decl_stmt|;
specifier|private
specifier|final
name|Thread
name|thread
decl_stmt|;
specifier|private
name|IOException
name|exception
decl_stmt|;
specifier|public
name|NoCloseInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|pos
operator|=
operator|new
name|PipedOutputStream
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|thread
operator|=
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|doRead
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
return|return
name|super
operator|.
name|read
argument_list|()
return|;
block|}
specifier|public
specifier|synchronized
name|int
name|read
parameter_list|(
name|byte
name|b
index|[]
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
return|return
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|pos
operator|.
name|close
argument_list|()
expr_stmt|;
name|thread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doRead
parameter_list|()
block|{
try|try
block|{
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|pos
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
comment|// Need to notify, else there is a 1 sec lag for the
comment|// echo to be displayed on the terminal.  The notify
comment|// will unblock the reader thread.
synchronized|synchronized
init|(
name|this
init|)
block|{
name|this
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|exception
operator|=
name|e
expr_stmt|;
try|try
block|{
name|pos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e2
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

