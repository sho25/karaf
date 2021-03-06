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
name|log
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|log
operator|.
name|core
operator|.
name|LogService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|logging
operator|.
name|spi
operator|.
name|PaxAppender
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
name|ServiceReference
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
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTrackerCustomizer
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"log"
argument_list|,
name|name
operator|=
literal|"tail"
argument_list|,
name|description
operator|=
literal|"Continuously display log entries. Use ctrl-c to quit this command"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|LogTail
extends|extends
name|DisplayLog
block|{
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
annotation|@
name|Reference
name|BundleContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|entries
operator|==
literal|0
condition|)
block|{
name|entries
operator|=
literal|50
expr_stmt|;
block|}
name|int
name|minLevel
init|=
name|getMinLevel
argument_list|(
name|level
argument_list|)
decl_stmt|;
comment|// Do not use System.out as it may write to the wrong console depending on the thread that calls our log handler
name|PrintStream
name|out
init|=
name|session
operator|.
name|getConsole
argument_list|()
decl_stmt|;
name|display
argument_list|(
name|out
argument_list|,
name|minLevel
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|PaxAppender
name|appender
init|=
name|event
lambda|->
name|printEvent
argument_list|(
name|out
argument_list|,
name|event
argument_list|,
name|minLevel
argument_list|)
decl_stmt|;
name|ServiceTracker
argument_list|<
name|LogService
argument_list|,
name|LogService
argument_list|>
name|tracker
init|=
operator|new
name|LogServiceTracker
argument_list|(
name|context
argument_list|,
name|LogService
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|appender
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
try|try
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"Stopping tail as log.core bundle was stopped."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Ignore as it will happen if the user breaks the tail using Ctrl-C
block|}
finally|finally
block|{
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
specifier|synchronized
name|void
name|stopTail
parameter_list|()
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
comment|/**      * Track LogService dynamically so we can react when the log core bundle stops even while we block for the tail      */
specifier|private
specifier|final
class|class
name|LogServiceTracker
extends|extends
name|ServiceTracker
argument_list|<
name|LogService
argument_list|,
name|LogService
argument_list|>
block|{
specifier|private
specifier|final
specifier|static
name|String
name|SSHD_LOGGER
init|=
literal|"org.apache.sshd"
decl_stmt|;
specifier|private
specifier|final
name|PaxAppender
name|appender
decl_stmt|;
specifier|private
name|String
name|sshdLoggerLevel
decl_stmt|;
specifier|private
name|LogServiceTracker
parameter_list|(
name|BundleContext
name|context
parameter_list|,
name|Class
argument_list|<
name|LogService
argument_list|>
name|clazz
parameter_list|,
name|ServiceTrackerCustomizer
argument_list|<
name|LogService
argument_list|,
name|LogService
argument_list|>
name|customizer
parameter_list|,
name|PaxAppender
name|appender
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|clazz
argument_list|,
name|customizer
argument_list|)
expr_stmt|;
name|this
operator|.
name|appender
operator|=
name|appender
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|LogService
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|LogService
argument_list|>
name|reference
parameter_list|)
block|{
name|LogService
name|service
init|=
name|super
operator|.
name|addingService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|sshdLoggerLevel
operator|=
name|service
operator|.
name|getLevel
argument_list|(
name|SSHD_LOGGER
argument_list|)
operator|.
name|get
argument_list|(
name|SSHD_LOGGER
argument_list|)
expr_stmt|;
name|service
operator|.
name|setLevel
argument_list|(
name|SSHD_LOGGER
argument_list|,
literal|"ERROR"
argument_list|)
expr_stmt|;
name|service
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|LogService
argument_list|>
name|reference
parameter_list|,
name|LogService
name|service
parameter_list|)
block|{
if|if
condition|(
name|sshdLoggerLevel
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|setLevel
argument_list|(
name|SSHD_LOGGER
argument_list|,
name|sshdLoggerLevel
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|removeAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
name|stopTail
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

