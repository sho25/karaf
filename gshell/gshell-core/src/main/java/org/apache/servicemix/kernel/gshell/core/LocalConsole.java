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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|notification
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
name|shell
operator|.
name|Shell
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
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
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

begin_class
specifier|public
class|class
name|LocalConsole
implements|implements
name|Runnable
implements|,
name|BundleContextAware
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|Shell
name|shell
decl_stmt|;
specifier|private
name|boolean
name|createLocalShell
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|CountDownLatch
name|frameworkStarted
decl_stmt|;
specifier|private
name|ServiceTracker
name|mainServiceTracker
decl_stmt|;
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
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
name|Shell
name|getShell
parameter_list|()
block|{
return|return
name|shell
return|;
block|}
specifier|public
name|void
name|setShell
parameter_list|(
name|Shell
name|shell
parameter_list|)
block|{
name|this
operator|.
name|shell
operator|=
name|shell
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCreateLocalShell
parameter_list|()
block|{
return|return
name|createLocalShell
return|;
block|}
specifier|public
name|void
name|setCreateLocalShell
parameter_list|(
name|boolean
name|createLocalShell
parameter_list|)
block|{
name|this
operator|.
name|createLocalShell
operator|=
name|createLocalShell
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{
name|mainServiceTracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
literal|"org.apache.servicemix.kernel.main.spi.MainService"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mainServiceTracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|shell
operator|.
name|getContext
argument_list|()
operator|.
name|getVariables
argument_list|()
operator|.
name|set
argument_list|(
literal|"gshell.username"
argument_list|,
literal|"smx"
argument_list|)
expr_stmt|;
name|frameworkStarted
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
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
name|log
operator|.
name|debug
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
name|createLocalShell
condition|)
block|{
operator|new
name|Thread
argument_list|(
name|this
argument_list|,
literal|"localShell"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|mainServiceTracker
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|createLocalShell
condition|)
block|{
name|shell
operator|.
name|close
argument_list|()
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
name|String
index|[]
name|args
init|=
name|getMainServiceArgs
argument_list|()
decl_stmt|;
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
name|Arrays
operator|.
name|toString
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|arg
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|Object
name|value
init|=
name|shell
operator|.
name|execute
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|setMainServiceExitCode
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
name|setMainServiceExitCode
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
name|log
operator|.
name|info
argument_list|(
literal|"Exiting shell due to terminated command"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|shell
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ExitNotification
name|e
parameter_list|)
block|{
name|setMainServiceExitCode
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
name|Throwable
name|e
parameter_list|)
block|{
name|setMainServiceExitCode
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|log
operator|.
name|error
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
name|shell
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{}
name|asyncShutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
index|[]
name|getMainServiceArgs
parameter_list|()
block|{
try|try
block|{
name|Object
name|mainService
init|=
name|mainServiceTracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
name|Method
name|mth
init|=
name|mainService
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getArgs"
argument_list|)
decl_stmt|;
return|return
operator|(
name|String
index|[]
operator|)
name|mth
operator|.
name|invoke
argument_list|(
name|mainService
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Error getting MainService args"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|setMainServiceExitCode
parameter_list|(
name|int
name|exitCode
parameter_list|)
block|{
try|try
block|{
name|Object
name|mainService
init|=
name|mainServiceTracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|mainService
operator|!=
literal|null
condition|)
block|{
name|Method
name|mth
init|=
name|mainService
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setExitCode"
argument_list|,
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|mth
operator|.
name|invoke
argument_list|(
name|mainService
argument_list|,
name|exitCode
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Error setting MainService exit code"
argument_list|,
name|t
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|info
argument_list|(
literal|"Waiting from framework to start."
argument_list|)
expr_stmt|;
if|if
condition|(
name|frameworkStarted
operator|.
name|await
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"System completed startup."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"System took too long startup... continuing"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|asyncShutdown
parameter_list|()
block|{
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
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
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

