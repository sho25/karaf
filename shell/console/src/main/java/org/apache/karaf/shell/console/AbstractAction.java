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
package|;
end_package

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
name|Action
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
annotation|@
name|Deprecated
specifier|public
specifier|abstract
class|class
name|AbstractAction
implements|implements
name|Action
block|{
specifier|protected
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
specifier|protected
name|CommandSession
name|session
decl_stmt|;
specifier|public
name|Object
name|execute
parameter_list|(
name|CommandSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
return|return
name|doExecute
argument_list|()
return|;
block|}
specifier|protected
specifier|abstract
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * This is for long running commands to be interrupted by ctrl-c.      *       * @throws InterruptedException If the action is interrupted.      */
specifier|public
specifier|static
name|void
name|checkInterrupted
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InterruptedException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

