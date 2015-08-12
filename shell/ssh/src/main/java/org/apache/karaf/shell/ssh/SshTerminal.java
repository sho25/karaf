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
name|ssh
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
name|api
operator|.
name|console
operator|.
name|Signal
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
name|support
operator|.
name|terminal
operator|.
name|SignalSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Environment
import|;
end_import

begin_class
specifier|public
class|class
name|SshTerminal
extends|extends
name|SignalSupport
implements|implements
name|Terminal
block|{
specifier|private
name|Environment
name|environment
decl_stmt|;
specifier|public
name|SshTerminal
parameter_list|(
name|Environment
name|environment
parameter_list|)
block|{
name|this
operator|.
name|environment
operator|=
name|environment
expr_stmt|;
name|this
operator|.
name|environment
operator|.
name|addSignalListener
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|SignalListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|signal
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Signal
name|signal
parameter_list|)
block|{
name|SshTerminal
operator|.
name|this
operator|.
name|signal
argument_list|(
name|Signal
operator|.
name|WINCH
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Signal
operator|.
name|WINCH
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
name|int
name|width
init|=
literal|0
decl_stmt|;
try|try
block|{
name|width
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|environment
operator|.
name|getEnv
argument_list|()
operator|.
name|get
argument_list|(
name|Environment
operator|.
name|ENV_COLUMNS
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
return|return
name|width
operator|>
literal|0
condition|?
name|width
else|:
literal|80
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
name|int
name|height
init|=
literal|0
decl_stmt|;
try|try
block|{
name|height
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|environment
operator|.
name|getEnv
argument_list|()
operator|.
name|get
argument_list|(
name|Environment
operator|.
name|ENV_LINES
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore
block|}
return|return
name|height
operator|>
literal|0
condition|?
name|height
else|:
literal|24
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAnsiSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEchoEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEchoEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
comment|// TODO: how to disable echo over ssh ?
block|}
block|}
end_class

end_unit

