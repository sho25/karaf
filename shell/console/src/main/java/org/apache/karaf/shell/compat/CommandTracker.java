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
name|compat
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandProcessor
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|commands
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
name|commands
operator|.
name|CommandWithAction
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
name|CommandLine
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
name|Completer
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
name|Filter
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
specifier|public
class|class
name|CommandTracker
implements|implements
name|ServiceTrackerCustomizer
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
name|SessionFactory
name|sessionFactory
decl_stmt|;
name|BundleContext
name|context
decl_stmt|;
name|ServiceTracker
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|tracker
decl_stmt|;
specifier|public
name|void
name|setSessionFactory
parameter_list|(
name|SessionFactory
name|sessionFactory
parameter_list|)
block|{
name|this
operator|.
name|sessionFactory
operator|=
name|sessionFactory
expr_stmt|;
block|}
specifier|public
name|void
name|setContext
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|Filter
name|filter
init|=
name|context
operator|.
name|createFilter
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"(&(%s=*)(%s=*))"
argument_list|,
name|CommandProcessor
operator|.
name|COMMAND_SCOPE
argument_list|,
name|CommandProcessor
operator|.
name|COMMAND_FUNCTION
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|context
argument_list|,
name|filter
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|addingService
parameter_list|(
specifier|final
name|ServiceReference
name|reference
parameter_list|)
block|{
name|Object
name|service
init|=
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|CommandWithAction
condition|)
block|{
specifier|final
name|CommandWithAction
name|oldCommand
init|=
operator|(
name|CommandWithAction
operator|)
name|service
decl_stmt|;
specifier|final
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
name|Command
name|command
init|=
operator|new
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
name|Command
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|reference
operator|.
name|getProperty
argument_list|(
name|CommandProcessor
operator|.
name|COMMAND_SCOPE
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|reference
operator|.
name|getProperty
argument_list|(
name|CommandProcessor
operator|.
name|COMMAND_FUNCTION
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
specifier|final
name|Command
name|cmd
init|=
name|oldCommand
operator|.
name|getActionClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmd
operator|!=
literal|null
condition|)
block|{
return|return
name|cmd
operator|.
name|description
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Completer
name|getCompleter
parameter_list|(
specifier|final
name|boolean
name|scoped
parameter_list|)
block|{
specifier|final
name|ArgumentCompleter
name|completer
init|=
operator|new
name|ArgumentCompleter
argument_list|(
name|oldCommand
argument_list|,
name|getScope
argument_list|()
argument_list|,
name|getName
argument_list|()
argument_list|,
name|scoped
argument_list|)
decl_stmt|;
return|return
operator|new
name|Completer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
return|return
name|completer
operator|.
name|complete
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|Session
name|session
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: remove not really nice cast
name|CommandSession
name|commandSession
init|=
operator|(
name|CommandSession
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".commandSession"
argument_list|)
decl_stmt|;
return|return
name|oldCommand
operator|.
name|execute
argument_list|(
name|commandSession
argument_list|,
name|arguments
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|sessionFactory
operator|.
name|getRegistry
argument_list|()
operator|.
name|register
argument_list|(
name|command
argument_list|)
expr_stmt|;
return|return
name|command
return|;
block|}
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|CommandWithAction
condition|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|CommandWithAction
name|oldCommand
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|CommandWithAction
operator|)
name|service
decl_stmt|;
specifier|final
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
name|Command
name|command
init|=
operator|new
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
name|Command
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|reference
operator|.
name|getProperty
argument_list|(
name|CommandProcessor
operator|.
name|COMMAND_SCOPE
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|reference
operator|.
name|getProperty
argument_list|(
name|CommandProcessor
operator|.
name|COMMAND_FUNCTION
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
name|cmd
init|=
name|oldCommand
operator|.
name|getActionClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmd
operator|!=
literal|null
condition|)
block|{
return|return
name|cmd
operator|.
name|description
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Completer
name|getCompleter
parameter_list|(
specifier|final
name|boolean
name|scoped
parameter_list|)
block|{
specifier|final
name|OldArgumentCompleter
name|completer
init|=
operator|new
name|OldArgumentCompleter
argument_list|(
name|oldCommand
argument_list|,
name|getScope
argument_list|()
argument_list|,
name|getName
argument_list|()
argument_list|,
name|scoped
argument_list|)
decl_stmt|;
return|return
operator|new
name|Completer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
return|return
name|completer
operator|.
name|complete
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|Session
name|session
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: remove not really nice cast
name|CommandSession
name|commandSession
init|=
operator|(
name|CommandSession
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".commandSession"
argument_list|)
decl_stmt|;
return|return
name|oldCommand
operator|.
name|execute
argument_list|(
name|commandSession
argument_list|,
name|arguments
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|sessionFactory
operator|.
name|getRegistry
argument_list|()
operator|.
name|register
argument_list|(
name|command
argument_list|)
expr_stmt|;
return|return
name|command
return|;
block|}
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
name|reference
parameter_list|,
name|Object
name|service
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
name|reference
parameter_list|,
name|Object
name|service
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
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
name|Command
condition|)
block|{
name|sessionFactory
operator|.
name|getRegistry
argument_list|()
operator|.
name|unregister
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

