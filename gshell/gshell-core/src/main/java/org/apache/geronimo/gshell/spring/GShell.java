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
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|DefaultEnvironment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|ExitNotification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|IO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|Environment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|InteractiveShell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|main
operator|.
name|spi
operator|.
name|MainService
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
name|BundleContext
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
name|BundleException
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
name|FrameworkEvent
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
name|FrameworkListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|BundleContextAware
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Oct 11, 2007  * Time: 10:20:37 PM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
specifier|public
class|class
name|GShell
implements|implements
name|Runnable
implements|,
name|BundleContextAware
block|{
specifier|private
name|InteractiveShell
name|shell
decl_stmt|;
specifier|private
name|Thread
name|thread
decl_stmt|;
specifier|private
name|IO
name|io
decl_stmt|;
specifier|private
name|Environment
name|env
decl_stmt|;
specifier|private
name|boolean
name|start
decl_stmt|;
specifier|private
name|MainService
name|mainService
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|CountDownLatch
name|frameworkStarted
decl_stmt|;
specifier|public
name|GShell
parameter_list|(
name|InteractiveShell
name|shell
parameter_list|)
block|{
name|this
operator|.
name|shell
operator|=
name|shell
expr_stmt|;
name|this
operator|.
name|io
operator|=
operator|new
name|IO
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|this
operator|.
name|env
operator|=
operator|new
name|DefaultEnvironment
argument_list|(
name|io
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStart
parameter_list|(
name|boolean
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
name|frameworkStarted
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|start
condition|)
block|{
name|thread
operator|=
operator|new
name|Thread
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|thread
operator|!=
literal|null
condition|)
block|{
name|frameworkStarted
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|thread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
name|thread
operator|.
name|join
argument_list|()
expr_stmt|;
name|thread
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|waitForFrameworkToStart
argument_list|()
expr_stmt|;
name|IOTargetSource
operator|.
name|setIO
argument_list|(
name|io
argument_list|)
expr_stmt|;
name|EnvironmentTargetSource
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|String
index|[]
name|args
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
name|args
operator|=
name|mainService
operator|.
name|getArgs
argument_list|()
expr_stmt|;
block|}
comment|// If a command was specified on the command line, then just execute that command.
if|if
condition|(
name|args
operator|!=
literal|null
operator|&&
name|args
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Executing 1 command:"
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|shell
operator|.
name|execute
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|args
argument_list|)
decl_stmt|;
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|mainService
operator|.
name|setExitCode
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|value
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mainService
operator|.
name|setExitCode
argument_list|(
name|value
operator|!=
literal|null
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"going int interactive loop:"
argument_list|)
expr_stmt|;
comment|// Otherwise go into a command shell.
name|shell
operator|.
name|run
argument_list|()
expr_stmt|;
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
name|mainService
operator|.
name|setExitCode
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ExitNotification
name|e
parameter_list|)
block|{
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
name|mainService
operator|.
name|setExitCode
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
name|mainService
operator|.
name|setExitCode
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BundleException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Blocks until the framework has finished starting.  We do this so that any installed      * bundles for commands get fully registered.      *       * @throws InterruptedException      */
specifier|private
name|void
name|waitForFrameworkToStart
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Waiting for system to startup..."
argument_list|)
expr_stmt|;
name|getBundleContext
argument_list|()
operator|.
name|addFrameworkListener
argument_list|(
operator|new
name|FrameworkListener
argument_list|()
block|{
specifier|public
name|void
name|frameworkEvent
parameter_list|(
name|FrameworkEvent
name|event
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Got event: "
operator|+
name|event
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|FrameworkEvent
operator|.
name|STARTED
condition|)
block|{
name|frameworkStarted
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|frameworkStarted
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"System completed startup."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"System took too long startup... continuing"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|MainService
name|getMainService
parameter_list|()
block|{
return|return
name|mainService
return|;
block|}
specifier|public
name|void
name|setMainService
parameter_list|(
name|MainService
name|main
parameter_list|)
block|{
name|this
operator|.
name|mainService
operator|=
name|main
expr_stmt|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
block|}
end_class

end_unit

