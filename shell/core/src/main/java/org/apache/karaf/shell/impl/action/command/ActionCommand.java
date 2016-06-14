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
name|action
operator|.
name|command
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Action
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
name|Parsing
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
name|Candidate
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
name|Parser
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

begin_class
specifier|public
class|class
name|ActionCommand
implements|implements
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
block|{
specifier|private
specifier|final
name|ManagerImpl
name|manager
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
decl_stmt|;
specifier|public
name|ActionCommand
parameter_list|(
name|ManagerImpl
name|manager
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|actionClass
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|actionClass
operator|=
name|actionClass
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|getActionClass
parameter_list|()
block|{
return|return
name|actionClass
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
operator|.
name|scope
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
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
operator|.
name|name
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
return|return
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
operator|.
name|description
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Completer
name|getCompleter
parameter_list|(
name|boolean
name|scoped
parameter_list|)
block|{
return|return
operator|new
name|ArgumentCompleter
argument_list|(
name|this
argument_list|,
name|scoped
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Parser
name|getParser
parameter_list|()
block|{
name|Parsing
name|parsing
init|=
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Parsing
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|parsing
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|DelayedParser
argument_list|(
name|parsing
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Completer
name|getCompleter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
operator|new
name|DelayedCompleter
argument_list|(
name|clazz
argument_list|)
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
name|Action
name|action
init|=
name|createNewAction
argument_list|(
name|session
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|new
name|DefaultActionPreparator
argument_list|()
operator|.
name|prepare
argument_list|(
name|action
argument_list|,
name|session
argument_list|,
name|arguments
argument_list|)
condition|)
block|{
return|return
name|action
operator|.
name|execute
argument_list|()
return|;
block|}
block|}
finally|finally
block|{
name|releaseAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Action
name|createNewAction
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
try|try
block|{
return|return
name|manager
operator|.
name|instantiate
argument_list|(
name|actionClass
argument_list|,
name|session
operator|.
name|getRegistry
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to creation command action "
operator|+
name|actionClass
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|releaseAction
parameter_list|(
name|Action
name|action
parameter_list|)
throws|throws
name|Exception
block|{
name|manager
operator|.
name|release
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|DelayedCompleter
implements|implements
name|Completer
block|{
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
specifier|public
name|DelayedCompleter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
block|}
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
name|Object
name|service
init|=
name|session
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Completer
condition|)
block|{
return|return
operator|(
operator|(
name|Completer
operator|)
name|service
operator|)
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
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeCandidates
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
name|Object
name|service
init|=
name|session
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Completer
condition|)
block|{
operator|(
operator|(
name|Completer
operator|)
name|service
operator|)
operator|.
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|DelayedParser
implements|implements
name|Parser
block|{
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
specifier|public
name|DelayedParser
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|CommandLine
name|parse
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|command
parameter_list|,
name|int
name|cursor
parameter_list|)
block|{
name|Object
name|service
init|=
name|session
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Parser
condition|)
block|{
return|return
operator|(
operator|(
name|Parser
operator|)
name|service
operator|)
operator|.
name|parse
argument_list|(
name|session
argument_list|,
name|command
argument_list|,
name|cursor
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Could not find specified parser"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|preprocess
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|)
block|{
name|Object
name|service
init|=
name|session
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Parser
condition|)
block|{
return|return
operator|(
operator|(
name|Parser
operator|)
name|service
operator|)
operator|.
name|preprocess
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Could not find specified parser"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

