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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|jline
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
name|sshd
operator|.
name|server
operator|.
name|ShellFactory
import|;
end_import

begin_class
specifier|public
class|class
name|SshTerminal
extends|extends
name|Terminal
implements|implements
name|ShellFactory
operator|.
name|SignalListener
block|{
specifier|private
name|ShellFactory
operator|.
name|Environment
name|environment
decl_stmt|;
specifier|public
name|SshTerminal
parameter_list|(
name|ShellFactory
operator|.
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
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initializeTerminal
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|public
name|void
name|restoreTerminal
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|public
name|int
name|getTerminalWidth
parameter_list|()
block|{
return|return
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
literal|"COLUMNS"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|int
name|getTerminalHeight
parameter_list|()
block|{
return|return
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
literal|"LINES"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|getEcho
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isEchoEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|enableEcho
parameter_list|()
block|{     }
specifier|public
name|void
name|disableEcho
parameter_list|()
block|{     }
specifier|public
name|void
name|signal
parameter_list|(
name|int
name|signal
parameter_list|)
block|{      }
block|}
end_class

end_unit

