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
comment|/**  * Copies standard output and error of children streams to standard output and error of the parent.  */
end_comment

begin_class
specifier|public
class|class
name|PumpStreamHandler
block|{
specifier|private
specifier|final
name|InputStream
name|in
decl_stmt|;
specifier|private
specifier|final
name|OutputStream
name|out
decl_stmt|;
specifier|private
specifier|final
name|OutputStream
name|err
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
name|StreamPumper
name|outputPump
decl_stmt|;
specifier|private
name|StreamPumper
name|errorPump
decl_stmt|;
specifier|private
name|StreamPumper
name|inputPump
decl_stmt|;
comment|//
comment|// NOTE: May want to use a ThreadPool here, 3 threads per/pair seems kinda expensive :-(
comment|//
specifier|public
name|PumpStreamHandler
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
name|OutputStream
name|err
parameter_list|,
name|String
name|name
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
assert|assert
name|err
operator|!=
literal|null
assert|;
assert|assert
name|name
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
name|err
operator|=
name|err
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|PumpStreamHandler
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
name|OutputStream
name|err
parameter_list|)
block|{
name|this
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
literal|"<unknown>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PumpStreamHandler
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|,
specifier|final
name|OutputStream
name|err
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PumpStreamHandler
parameter_list|(
specifier|final
name|OutputStream
name|outAndErr
parameter_list|)
block|{
name|this
argument_list|(
name|outAndErr
argument_list|,
name|outAndErr
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the input stream from which to read the standard output of the child.      *      * @param in the the child output stream.      */
specifier|public
name|void
name|setChildOutputStream
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
assert|assert
name|in
operator|!=
literal|null
assert|;
name|createChildOutputPump
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the input stream from which to read the standard error of the child.      *      * @param in set the child error stream.      */
specifier|public
name|void
name|setChildErrorStream
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
block|{
assert|assert
name|in
operator|!=
literal|null
assert|;
if|if
condition|(
name|err
operator|!=
literal|null
condition|)
block|{
name|createChildErrorPump
argument_list|(
name|in
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the output stream by means of which input can be sent to the child.      *      * @param out set the child output stream.      */
specifier|public
name|void
name|setChildInputStream
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
block|{
assert|assert
name|out
operator|!=
literal|null
assert|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|inputPump
operator|=
name|createInputPump
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
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
block|}
comment|/**      * Attach to a child streams from the given process.      *      * @param p The process to attach to.      */
specifier|public
name|void
name|attach
parameter_list|(
specifier|final
name|Process
name|p
parameter_list|)
block|{
assert|assert
name|p
operator|!=
literal|null
assert|;
name|setChildInputStream
argument_list|(
name|p
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|setChildOutputStream
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|setChildErrorStream
argument_list|(
name|p
operator|.
name|getErrorStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Start pumping the streams.      */
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|outputPump
operator|!=
literal|null
condition|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|outputPump
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setName
argument_list|(
literal|"Output pump for "
operator|+
name|this
operator|.
name|name
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|errorPump
operator|!=
literal|null
condition|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|errorPump
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setName
argument_list|(
literal|"Error pump for "
operator|+
name|this
operator|.
name|name
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|inputPump
operator|!=
literal|null
condition|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|inputPump
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setName
argument_list|(
literal|"Input pump for "
operator|+
name|this
operator|.
name|name
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Stop pumping the streams.      */
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|outputPump
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|outputPump
operator|.
name|stop
argument_list|()
expr_stmt|;
name|outputPump
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
name|outputPump
operator|.
name|getIn
argument_list|()
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
if|if
condition|(
name|errorPump
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|errorPump
operator|.
name|stop
argument_list|()
expr_stmt|;
name|errorPump
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
name|errorPump
operator|.
name|getIn
argument_list|()
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
if|if
condition|(
name|inputPump
operator|!=
literal|null
condition|)
block|{
name|inputPump
operator|.
name|stop
argument_list|()
expr_stmt|;
try|try
block|{
name|inputPump
operator|.
name|getOut
argument_list|()
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
try|try
block|{
name|err
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
block|}
comment|/**      * Create the pump to handle child output.      *      * @param in the child input stream.      * @param out the child output stream.      */
specifier|protected
name|void
name|createChildOutputPump
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
name|outputPump
operator|=
name|createPump
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create the pump to handle error output.      *      * @param in the child input stream.      * @param out the child output stream.      */
specifier|protected
name|void
name|createChildErrorPump
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
name|errorPump
operator|=
name|createPump
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a stream pumper to copy the given input stream to the given output stream.      *      * @param in the child input stream.      * @param out the child output stream.      * @return A thread object that does the pumping.      */
specifier|protected
name|StreamPumper
name|createPump
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
return|return
name|createPump
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Create a stream pumper to copy the given input stream to the      * given output stream.      *      * @param in The input stream to copy from.      * @param out The output stream to copy to.      * @param closeWhenExhausted If true close the input stream.      * @return A thread object that does the pumping.      */
specifier|protected
name|StreamPumper
name|createPump
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
name|StreamPumper
name|pumper
init|=
operator|new
name|StreamPumper
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|closeWhenExhausted
argument_list|)
decl_stmt|;
return|return
name|pumper
return|;
block|}
comment|/**      * Create a stream pumper to copy the given input stream to the      * given output stream. Used for standard input.      *      * @param in The input stream to copy from.      * @param out The output stream to copy to.      * @param closeWhenExhausted If true close the input stream.      * @return A thread object that does the pumping.      */
specifier|protected
name|StreamPumper
name|createInputPump
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
name|StreamPumper
name|pumper
init|=
operator|new
name|StreamPumper
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|closeWhenExhausted
argument_list|)
decl_stmt|;
name|pumper
operator|.
name|setNonBlocking
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|pumper
operator|.
name|setAutoflush
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|pumper
return|;
block|}
specifier|public
name|StreamPumper
name|getOutputPump
parameter_list|()
block|{
return|return
name|this
operator|.
name|outputPump
return|;
block|}
specifier|public
name|StreamPumper
name|getErrorPump
parameter_list|()
block|{
return|return
name|this
operator|.
name|errorPump
return|;
block|}
block|}
end_class

end_unit

