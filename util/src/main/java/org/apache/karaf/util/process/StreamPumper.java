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
name|util
operator|.
name|process
package|;
end_package

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
name|IOException
import|;
end_import

begin_comment
comment|/**  * Copies all data from an input stream to an output stream.  */
end_comment

begin_class
specifier|public
class|class
name|StreamPumper
implements|implements
name|Runnable
block|{
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|private
name|OutputStream
name|out
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|finish
init|=
literal|false
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|finished
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|closeWhenExhausted
decl_stmt|;
specifier|private
name|boolean
name|nonBlocking
decl_stmt|;
specifier|private
name|boolean
name|autoflush
decl_stmt|;
specifier|private
name|Throwable
name|exception
decl_stmt|;
specifier|private
name|int
name|bufferSize
init|=
literal|128
decl_stmt|;
specifier|private
name|boolean
name|started
decl_stmt|;
specifier|private
name|Thread
name|thread
decl_stmt|;
comment|/**      * Create a new stream pumper.      *      * @param in The input stream to read data from.      * @param out The output stream to write data to.      * @param closeWhenExhausted If true, the output stream will be closed when the input is exhausted.      */
specifier|public
name|StreamPumper
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|OutputStream
name|out
parameter_list|,
specifier|final
name|boolean
name|closeWhenExhausted
parameter_list|)
block|{
assert|assert
name|in
operator|!=
literal|null
assert|;
assert|assert
name|out
operator|!=
literal|null
assert|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|closeWhenExhausted
operator|=
name|closeWhenExhausted
expr_stmt|;
block|}
comment|/**      * Create a new stream pumper.      *      * @param in The input stream to read data from.      * @param out The output stream to write data to.      */
specifier|public
name|StreamPumper
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|OutputStream
name|out
parameter_list|)
block|{
name|this
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getIn
parameter_list|()
block|{
return|return
name|in
return|;
block|}
specifier|public
name|OutputStream
name|getOut
parameter_list|()
block|{
return|return
name|out
return|;
block|}
comment|/**      * Set whether data should be flushed through to the output stream.      *      * @param autoflush If true, push through data; if false, let it be buffered.      */
specifier|public
name|void
name|setAutoflush
parameter_list|(
name|boolean
name|autoflush
parameter_list|)
block|{
name|this
operator|.
name|autoflush
operator|=
name|autoflush
expr_stmt|;
block|}
comment|/**      * Set whether data should be read in a non blocking way.      *      * @param nonBlocking If true, data will be read in a non blocking mode.      */
specifier|public
name|void
name|setNonBlocking
parameter_list|(
name|boolean
name|nonBlocking
parameter_list|)
block|{
name|this
operator|.
name|nonBlocking
operator|=
name|nonBlocking
expr_stmt|;
block|}
comment|/**      * Copy data from the input stream to the output stream.      *      * Terminate as soon as the input stream is closed or an error occurs.      */
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|started
operator|=
literal|true
expr_stmt|;
name|thread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
block|}
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|length
init|=
literal|0
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|nonBlocking
condition|)
block|{
while|while
condition|(
name|in
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|length
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
if|if
condition|(
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|autoflush
condition|)
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
break|break;
block|}
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
comment|// Pause to avoid tight loop if external proc is too slow
block|}
else|else
block|{
do|do
block|{
name|length
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
if|if
condition|(
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|autoflush
condition|)
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
do|while
condition|(
name|length
operator|>
literal|0
condition|)
do|;
block|}
name|boolean
name|finish
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|finish
operator|=
name|this
operator|.
name|finish
operator|||
name|length
operator|<
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|finish
condition|)
block|{
break|break;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|exception
operator|=
name|t
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{ }
if|if
condition|(
name|closeWhenExhausted
condition|)
block|{
try|try
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{ }
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|finished
operator|=
literal|true
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Tell whether the end of the stream has been reached.      *      * @return true if the stream has been exhausted.      */
specifier|public
specifier|synchronized
name|boolean
name|isFinished
parameter_list|()
block|{
return|return
name|finished
return|;
block|}
comment|/**      * This method blocks until the stream pumper finishes.      *      * @see #isFinished()      * @throws InterruptedException if the stream pumper has been interrupted.      */
specifier|public
specifier|synchronized
name|void
name|waitFor
parameter_list|()
throws|throws
name|InterruptedException
block|{
while|while
condition|(
operator|!
name|isFinished
argument_list|()
condition|)
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Set the size in bytes of the read buffer.      *      * @param bufferSize the buffer size to use.      */
specifier|public
specifier|synchronized
name|void
name|setBufferSize
parameter_list|(
specifier|final
name|int
name|bufferSize
parameter_list|)
block|{
if|if
condition|(
name|started
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot set buffer size on a running StreamPumper"
argument_list|)
throw|;
block|}
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
comment|/**      * Get the size in bytes of the read buffer.      *      * @return The size of the read buffer.      */
specifier|public
specifier|synchronized
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
comment|/**      * Get the exception encountered, if any.      *      * @return The Exception encountered; or null if there was none.      */
specifier|public
specifier|synchronized
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
comment|/**      * Stop the pumper as soon as possible.      *      * Note that it may continue to block on the input stream      * but it will really stop the thread as soon as it gets EOF      * or any byte, and it will be marked as finished.      */
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
block|{
name|finish
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|nonBlocking
operator|&&
name|thread
operator|!=
literal|null
operator|&&
operator|!
name|finished
condition|)
block|{
name|thread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|notifyAll
argument_list|()
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
return|return
name|this
operator|.
name|in
return|;
block|}
block|}
end_class

end_unit

