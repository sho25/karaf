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
name|tooling
operator|.
name|exam
operator|.
name|container
operator|.
name|internal
operator|.
name|runner
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|org
operator|.
name|ops4j
operator|.
name|io
operator|.
name|Pipe
import|;
end_import

begin_class
specifier|public
class|class
name|InternalRunner
block|{
specifier|private
name|Process
name|m_frameworkProcess
decl_stmt|;
specifier|private
name|Thread
name|m_shutdownHook
decl_stmt|;
specifier|public
specifier|synchronized
name|void
name|exec
parameter_list|(
name|CommandLineBuilder
name|commandLine
parameter_list|,
specifier|final
name|File
name|workingDirectory
parameter_list|,
specifier|final
name|String
index|[]
name|envOptions
parameter_list|)
block|{
if|if
condition|(
name|m_frameworkProcess
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Platform already started"
argument_list|)
throw|;
block|}
try|try
block|{
name|m_frameworkProcess
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|commandLine
operator|.
name|toArray
argument_list|()
argument_list|,
name|createEnvironmentVars
argument_list|(
name|envOptions
argument_list|)
argument_list|,
name|workingDirectory
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
name|IllegalStateException
argument_list|(
literal|"Could not start up the process"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|m_shutdownHook
operator|=
name|createShutdownHook
argument_list|(
name|m_frameworkProcess
argument_list|)
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|m_shutdownHook
argument_list|)
expr_stmt|;
name|waitForExit
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
index|[]
name|createEnvironmentVars
parameter_list|(
name|String
index|[]
name|envOptions
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|env
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getenv
init|=
name|System
operator|.
name|getenv
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|getenv
operator|.
name|keySet
argument_list|()
control|)
block|{
name|env
operator|.
name|add
argument_list|(
name|key
operator|+
literal|"="
operator|+
name|getenv
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|envOptions
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|addAll
argument_list|(
name|env
argument_list|,
name|envOptions
argument_list|)
expr_stmt|;
block|}
return|return
name|env
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|env
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|m_shutdownHook
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|m_shutdownHook
init|)
block|{
if|if
condition|(
name|m_shutdownHook
operator|!=
literal|null
condition|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|removeShutdownHook
argument_list|(
name|m_shutdownHook
argument_list|)
expr_stmt|;
name|m_frameworkProcess
operator|=
literal|null
expr_stmt|;
name|m_shutdownHook
operator|.
name|run
argument_list|()
expr_stmt|;
name|m_shutdownHook
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ignore
parameter_list|)
block|{
comment|// just ignore
block|}
block|}
comment|/**      * Wait till the framework process exits.      */
specifier|public
name|void
name|waitForExit
parameter_list|()
block|{
synchronized|synchronized
init|(
name|m_frameworkProcess
init|)
block|{
try|try
block|{
name|m_frameworkProcess
operator|.
name|waitFor
argument_list|()
expr_stmt|;
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Create helper thread to safely shutdown the external framework process      *      * @param process framework process      *      * @return stream handler      */
specifier|private
name|Thread
name|createShutdownHook
parameter_list|(
specifier|final
name|Process
name|process
parameter_list|)
block|{
specifier|final
name|Pipe
name|errPipe
init|=
operator|new
name|Pipe
argument_list|(
name|process
operator|.
name|getErrorStream
argument_list|()
argument_list|,
name|System
operator|.
name|err
argument_list|)
operator|.
name|start
argument_list|(
literal|"Error pipe"
argument_list|)
decl_stmt|;
specifier|final
name|Pipe
name|outPipe
init|=
operator|new
name|Pipe
argument_list|(
name|process
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|System
operator|.
name|out
argument_list|)
operator|.
name|start
argument_list|(
literal|"Out pipe"
argument_list|)
decl_stmt|;
specifier|final
name|Pipe
name|inPipe
init|=
operator|new
name|Pipe
argument_list|(
name|process
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|System
operator|.
name|in
argument_list|)
operator|.
name|start
argument_list|(
literal|"In pipe"
argument_list|)
decl_stmt|;
return|return
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|inPipe
operator|.
name|stop
argument_list|()
expr_stmt|;
name|outPipe
operator|.
name|stop
argument_list|()
expr_stmt|;
name|errPipe
operator|.
name|stop
argument_list|()
expr_stmt|;
try|try
block|{
name|process
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if already shutting down
block|}
block|}
block|}
argument_list|,
literal|"Pax-Runner shutdown hook"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

