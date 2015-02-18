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
name|HashMap
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
name|service
operator|.
name|command
operator|.
name|Function
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
name|Registry
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
name|Terminal
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
name|commands
operator|.
name|ExitCommand
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
name|commands
operator|.
name|SubShellCommand
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
name|commands
operator|.
name|help
operator|.
name|HelpCommand
import|;
end_import

begin_class
specifier|public
class|class
name|SessionFactoryImpl
extends|extends
name|RegistryImpl
implements|implements
name|SessionFactory
implements|,
name|Registry
block|{
specifier|final
name|CommandProcessorImpl
name|commandProcessor
decl_stmt|;
specifier|final
name|ThreadIO
name|threadIO
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Session
argument_list|>
name|sessions
init|=
operator|new
name|ArrayList
argument_list|<
name|Session
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SubShellCommand
argument_list|>
name|subshells
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SubShellCommand
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|closed
decl_stmt|;
specifier|public
name|SessionFactoryImpl
parameter_list|(
name|ThreadIO
name|threadIO
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|threadIO
operator|=
name|threadIO
expr_stmt|;
name|commandProcessor
operator|=
operator|new
name|CommandProcessorImpl
argument_list|(
name|threadIO
argument_list|)
expr_stmt|;
name|register
argument_list|(
operator|new
name|ExitCommand
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|HelpCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CommandProcessorImpl
name|getCommandProcessor
parameter_list|()
block|{
return|return
name|commandProcessor
return|;
block|}
annotation|@
name|Override
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|command
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|String
name|scope
init|=
name|command
operator|.
name|getScope
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|command
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Session
operator|.
name|SCOPE_GLOBAL
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|subshells
operator|.
name|containsKey
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|SubShellCommand
name|subShell
init|=
operator|new
name|SubShellCommand
argument_list|(
name|scope
argument_list|)
decl_stmt|;
name|subshells
operator|.
name|put
argument_list|(
name|scope
argument_list|,
name|subShell
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|subShell
argument_list|)
expr_stmt|;
block|}
name|subshells
operator|.
name|get
argument_list|(
name|scope
argument_list|)
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
name|commandProcessor
operator|.
name|addCommand
argument_list|(
name|scope
argument_list|,
name|wrap
argument_list|(
name|command
argument_list|)
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|register
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Function
name|wrap
parameter_list|(
name|Command
name|command
parameter_list|)
block|{
return|return
operator|new
name|CommandWrapper
argument_list|(
name|command
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|unregister
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
name|super
operator|.
name|unregister
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|command
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|String
name|scope
init|=
name|command
operator|.
name|getScope
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|command
operator|.
name|getName
argument_list|()
decl_stmt|;
name|commandProcessor
operator|.
name|removeCommand
argument_list|(
name|scope
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Session
operator|.
name|SCOPE_GLOBAL
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
if|if
condition|(
name|subshells
operator|.
name|get
argument_list|(
name|scope
argument_list|)
operator|.
name|decrement
argument_list|()
operator|==
literal|0
condition|)
block|{
name|SubShellCommand
name|subShell
init|=
name|subshells
operator|.
name|remove
argument_list|(
name|scope
argument_list|)
decl_stmt|;
name|unregister
argument_list|(
name|subShell
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|Session
name|create
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Terminal
name|term
parameter_list|,
name|String
name|encoding
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|)
block|{
synchronized|synchronized
init|(
name|sessions
init|)
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SessionFactory has been closed"
argument_list|)
throw|;
block|}
specifier|final
name|Session
name|session
init|=
operator|new
name|ConsoleSessionImpl
argument_list|(
name|this
argument_list|,
name|commandProcessor
argument_list|,
name|threadIO
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|term
argument_list|,
name|encoding
argument_list|,
name|closeCallback
argument_list|)
decl_stmt|;
name|sessions
operator|.
name|add
argument_list|(
name|session
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Session
name|create
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
return|return
name|create
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Session
name|create
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Session
name|parent
parameter_list|)
block|{
synchronized|synchronized
init|(
name|sessions
init|)
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SessionFactory has been closed"
argument_list|)
throw|;
block|}
specifier|final
name|Session
name|session
init|=
operator|new
name|HeadlessSessionImpl
argument_list|(
name|this
argument_list|,
name|commandProcessor
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|parent
argument_list|)
decl_stmt|;
name|sessions
operator|.
name|add
argument_list|(
name|session
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
synchronized|synchronized
init|(
name|sessions
init|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|Session
name|session
range|:
name|sessions
control|)
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|commandProcessor
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

