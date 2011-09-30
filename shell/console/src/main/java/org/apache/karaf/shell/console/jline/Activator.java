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
name|console
operator|.
name|jline
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
name|PrintStream
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
name|CommandProcessorImpl
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
name|CommandSessionImpl
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
name|activator
operator|.
name|EventAdminListener
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
name|felix
operator|.
name|service
operator|.
name|threadio
operator|.
name|ThreadIO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|AnsiConsole
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
name|ServiceRegistration
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
name|activator
operator|.
name|Activator
name|activator
init|=
operator|new
name|WrappedActivator
argument_list|()
decl_stmt|;
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|AnsiConsole
operator|.
name|systemInstall
argument_list|()
expr_stmt|;
name|activator
operator|.
name|start
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
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
name|activator
operator|.
name|stop
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|AnsiConsole
operator|.
name|systemUninstall
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|static
class|class
name|WrappedActivator
extends|extends
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
name|activator
operator|.
name|Activator
block|{
annotation|@
name|Override
specifier|protected
name|ServiceRegistration
name|newProcessor
parameter_list|(
name|ThreadIO
name|tio
parameter_list|,
name|BundleContext
name|context
parameter_list|)
block|{
name|processor
operator|=
operator|new
name|WrappedCommandProcessor
argument_list|(
name|tio
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|addListener
argument_list|(
operator|new
name|EventAdminListener
argument_list|(
name|context
argument_list|)
argument_list|)
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
comment|// Setup the variables and commands exposed in an OSGi environment.
name|processor
operator|.
name|addConstant
argument_list|(
name|CONTEXT
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addCommand
argument_list|(
literal|"osgi"
argument_list|,
name|processor
argument_list|,
literal|"addCommand"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addCommand
argument_list|(
literal|"osgi"
argument_list|,
name|processor
argument_list|,
literal|"removeCommand"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addCommand
argument_list|(
literal|"osgi"
argument_list|,
name|processor
argument_list|,
literal|"eval"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|registerService
argument_list|(
name|CommandProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|processor
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|WrappedCommandProcessor
extends|extends
name|CommandProcessorImpl
block|{
specifier|public
name|WrappedCommandProcessor
parameter_list|(
name|ThreadIO
name|tio
parameter_list|)
block|{
name|super
argument_list|(
name|tio
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|CommandSession
name|createSession
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
block|{
name|CommandSessionImpl
name|session
init|=
operator|new
name|WrappedCommandSession
argument_list|(
name|this
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
decl_stmt|;
name|sessions
operator|.
name|put
argument_list|(
name|session
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|WrappedCommandSession
extends|extends
name|CommandSessionImpl
block|{
specifier|public
name|WrappedCommandSession
parameter_list|(
name|CommandProcessorImpl
name|shell
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
block|{
name|super
argument_list|(
name|shell
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|val
init|=
name|super
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
block|}
block|}
end_class

end_unit

