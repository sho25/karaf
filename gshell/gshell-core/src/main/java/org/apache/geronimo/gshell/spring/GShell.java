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
name|common
operator|.
name|Arguments
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
name|runtime
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * This class represents the local shell console and is also used when passing a command to execute on the command line.  * Such mechanism is done using the {@link MainService} service registered in the OSGi registry, which contains the  * command line arguments and a place holder for the exit code.  */
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
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GShell
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|Thread
operator|.
name|dumpStack
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
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
name|waitForFrameworkToStart
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Executing Shell with arguments: "
operator|+
name|Arguments
operator|.
name|asString
argument_list|(
name|args
argument_list|)
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
name|log
operator|.
name|info
argument_list|(
literal|"Exiting shell due received exit notification"
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|info
argument_list|(
literal|"Exiting shell due to caught exception "
operator|+
name|e
argument_list|,
name|e
argument_list|)
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
name|log
operator|.
name|info
argument_list|(
literal|"Caught exception while shutting down framework: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Blocks until the framework has finished starting.  We do this so that any installed      * bundles for commands get fully registered.      *      * @throws InterruptedException      */
specifier|private
name|void
name|waitForFrameworkToStart
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|//		getBundleContext().addFrameworkListener(new FrameworkListener(){
comment|//			public void frameworkEvent(FrameworkEvent event) {
comment|//				System.out.println("Got event: "+event.getType());
comment|//				if( event.getType() == FrameworkEvent.STARTED ) {
comment|//					frameworkStarted.countDown();
comment|//				}
comment|//			}
comment|//		});
comment|//
comment|//		if( frameworkStarted.await(5, TimeUnit.SECONDS) ) {
comment|//			System.out.println("System completed startup.");
comment|//		} else {
comment|//			System.out.println("System took too long startup... continuing");
comment|//		}
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

