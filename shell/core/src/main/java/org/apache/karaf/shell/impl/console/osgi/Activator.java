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
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|runtime
operator|.
name|threadio
operator|.
name|ThreadIOImpl
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
name|CommandLoggingFilter
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
name|SessionFactory
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
name|impl
operator|.
name|action
operator|.
name|command
operator|.
name|ManagerImpl
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
name|impl
operator|.
name|action
operator|.
name|osgi
operator|.
name|CommandExtender
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
name|impl
operator|.
name|console
operator|.
name|SessionFactoryImpl
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
name|impl
operator|.
name|console
operator|.
name|osgi
operator|.
name|secured
operator|.
name|SecuredSessionFactoryImpl
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
name|BundleActivator
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
name|framework
operator|.
name|ServiceRegistration
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

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|START_CONSOLE
init|=
literal|"karaf.startLocalConsole"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Activator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ThreadIOImpl
name|threadIO
decl_stmt|;
specifier|private
name|SessionFactoryImpl
name|sessionFactory
decl_stmt|;
specifier|private
name|ServiceRegistration
name|sessionFactoryRegistration
decl_stmt|;
specifier|private
name|CommandExtender
name|actionExtender
decl_stmt|;
specifier|private
name|Closeable
name|eventAdminListener
decl_stmt|;
specifier|private
name|LocalConsoleManager
name|localConsoleManager
decl_stmt|;
name|ServiceTracker
argument_list|<
name|CommandLoggingFilter
argument_list|,
name|CommandLoggingFilter
argument_list|>
name|filterTracker
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|threadIO
operator|=
operator|new
name|ThreadIOImpl
argument_list|()
expr_stmt|;
name|threadIO
operator|.
name|start
argument_list|()
expr_stmt|;
name|sessionFactory
operator|=
operator|new
name|SecuredSessionFactoryImpl
argument_list|(
name|context
argument_list|,
name|threadIO
argument_list|)
expr_stmt|;
name|sessionFactory
operator|.
name|getCommandProcessor
argument_list|()
operator|.
name|addConverter
argument_list|(
operator|new
name|Converters
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|sessionFactory
operator|.
name|getCommandProcessor
argument_list|()
operator|.
name|addConstant
argument_list|(
literal|".context"
argument_list|,
name|context
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundleContext
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|CopyOnWriteArraySet
argument_list|<
name|CommandLoggingFilter
argument_list|>
name|listeners
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
name|filterTracker
operator|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|context
argument_list|,
name|CommandLoggingFilter
operator|.
name|class
argument_list|,
operator|new
name|ServiceTrackerCustomizer
argument_list|<
name|CommandLoggingFilter
argument_list|,
name|CommandLoggingFilter
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CommandLoggingFilter
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|CommandLoggingFilter
argument_list|>
name|reference
parameter_list|)
block|{
name|CommandLoggingFilter
name|service
init|=
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|listeners
operator|.
name|add
argument_list|(
name|service
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
name|modifiedService
parameter_list|(
name|ServiceReference
argument_list|<
name|CommandLoggingFilter
argument_list|>
name|reference
parameter_list|,
name|CommandLoggingFilter
name|service
parameter_list|)
block|{             }
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|CommandLoggingFilter
argument_list|>
name|reference
parameter_list|,
name|CommandLoggingFilter
name|service
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|context
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|filterTracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|LoggingCommandSessionListener
name|loggingCommandSessionListener
init|=
operator|new
name|LoggingCommandSessionListener
argument_list|()
decl_stmt|;
name|loggingCommandSessionListener
operator|.
name|setFilters
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
name|sessionFactory
operator|.
name|getCommandProcessor
argument_list|()
operator|.
name|addListener
argument_list|(
name|loggingCommandSessionListener
argument_list|)
expr_stmt|;
try|try
block|{
name|EventAdminListener
name|listener
init|=
operator|new
name|EventAdminListener
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|sessionFactory
operator|.
name|getCommandProcessor
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|eventAdminListener
operator|=
name|listener
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|error
parameter_list|)
block|{
comment|// Ignore the listener if EventAdmin package isn't present
block|}
name|sessionFactory
operator|.
name|register
argument_list|(
operator|new
name|ManagerImpl
argument_list|(
name|sessionFactory
argument_list|,
name|sessionFactory
argument_list|)
argument_list|)
expr_stmt|;
name|sessionFactoryRegistration
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|SessionFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|sessionFactory
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|actionExtender
operator|=
operator|new
name|CommandExtender
argument_list|(
name|sessionFactory
argument_list|)
expr_stmt|;
name|actionExtender
operator|.
name|start
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|context
operator|.
name|getProperty
argument_list|(
name|START_CONSOLE
argument_list|)
argument_list|)
condition|)
block|{
name|localConsoleManager
operator|=
operator|new
name|LocalConsoleManager
argument_list|(
name|context
argument_list|,
name|sessionFactory
argument_list|)
expr_stmt|;
name|localConsoleManager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Not starting local console. To activate set "
operator|+
name|START_CONSOLE
operator|+
literal|"=true"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|filterTracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|sessionFactoryRegistration
operator|.
name|unregister
argument_list|()
expr_stmt|;
if|if
condition|(
name|localConsoleManager
operator|!=
literal|null
condition|)
block|{
name|localConsoleManager
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|sessionFactory
operator|.
name|stop
argument_list|()
expr_stmt|;
name|actionExtender
operator|.
name|stop
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|threadIO
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|eventAdminListener
operator|!=
literal|null
condition|)
block|{
name|eventAdminListener
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

