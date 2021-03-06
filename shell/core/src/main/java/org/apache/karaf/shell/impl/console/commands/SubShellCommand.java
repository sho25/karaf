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
name|commands
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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|atomic
operator|.
name|AtomicInteger
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
name|SubShellCommand
extends|extends
name|TopLevelCommand
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|AtomicInteger
name|references
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|public
name|SubShellCommand
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|increment
parameter_list|()
block|{
name|references
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|decrement
parameter_list|()
block|{
return|return
name|references
operator|.
name|decrementAndGet
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
name|name
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
literal|"Enter the subshell"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doExecute
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|session
operator|.
name|put
argument_list|(
name|Session
operator|.
name|SUBSHELL
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|,
name|name
operator|+
literal|":"
operator|+
name|session
operator|.
name|get
argument_list|(
name|Session
operator|.
name|SCOPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|printHelp
parameter_list|(
name|Session
name|session
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
try|try
block|{
operator|new
name|HelpCommand
argument_list|(
name|session
operator|.
name|getFactory
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|(
name|session
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"shell|"
operator|+
name|name
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"Unable to print subshell help"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

