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
name|ServiceTracker
argument_list|<
name|LogService
argument_list|,
name|LogService
argument_list|>
argument_list|(
name|context
argument_list|,
name|LogService
operator|.
name|class
argument_list|,
literal|null
argument_list|)
block|{
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
name|service
operator|.
name|removeAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|LogTail
operator|.
name|this
init|)
block|{
name|LogTail
operator|.
name|this
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
empty_stmt|;
block|}
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
block|}
end_class

end_unit

