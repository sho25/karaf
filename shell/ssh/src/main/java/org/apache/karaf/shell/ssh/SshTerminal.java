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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|SignalListener
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
name|sshd
operator|.
name|common
operator|.
name|channel
operator|.
name|PtyMode
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

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Attributes
operator|.
name|ControlChar
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Attributes
operator|.
name|InputFlag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Attributes
operator|.
name|LocalFlag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Attributes
operator|.
name|OutputFlag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Size
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|impl
operator|.
name|ExternalTerminal
import|;
end_import

begin_class
specifier|public
class|class
name|SshTerminal
extends|extends
name|ExternalTerminal
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
parameter_list|,
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
literal|"Karaf SSH terminal"
argument_list|,
name|environment
operator|.
name|getEnv
argument_list|()
operator|.
name|get
argument_list|(
name|Environment
operator|.
name|ENV_TERM
argument_list|)
argument_list|,
name|input
argument_list|,
name|output
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
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
operator|::
name|handleSignal
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|PtyMode
argument_list|,
name|Integer
argument_list|>
name|e
range|:
name|environment
operator|.
name|getPtyModes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
switch|switch
condition|(
name|e
operator|.
name|getKey
argument_list|()
condition|)
block|{
case|case
name|VINTR
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VINTR
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VQUIT
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VQUIT
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VERASE
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VERASE
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VKILL
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VKILL
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VEOF
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VEOF
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VEOL
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VEOL
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VEOL2
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VEOL2
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VSTART
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VSTART
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VSTOP
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VSTOP
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VSUSP
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VSUSP
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VDSUSP
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VDSUSP
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VREPRINT
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VREPRINT
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VWERASE
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VWERASE
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VLNEXT
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VLNEXT
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VSTATUS
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VSTATUS
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|VDISCARD
case|:
name|attributes
operator|.
name|setControlChar
argument_list|(
name|ControlChar
operator|.
name|VDISCARD
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|ECHO
case|:
name|attributes
operator|.
name|setLocalFlag
argument_list|(
name|LocalFlag
operator|.
name|ECHO
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|ICANON
case|:
name|attributes
operator|.
name|setLocalFlag
argument_list|(
name|LocalFlag
operator|.
name|ICANON
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|ISIG
case|:
name|attributes
operator|.
name|setLocalFlag
argument_list|(
name|LocalFlag
operator|.
name|ISIG
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|ICRNL
case|:
name|attributes
operator|.
name|setInputFlag
argument_list|(
name|InputFlag
operator|.
name|ICRNL
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|INLCR
case|:
name|attributes
operator|.
name|setInputFlag
argument_list|(
name|InputFlag
operator|.
name|INLCR
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|IGNCR
case|:
name|attributes
operator|.
name|setInputFlag
argument_list|(
name|InputFlag
operator|.
name|IGNCR
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|OCRNL
case|:
name|attributes
operator|.
name|setOutputFlag
argument_list|(
name|OutputFlag
operator|.
name|OCRNL
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|ONLCR
case|:
name|attributes
operator|.
name|setOutputFlag
argument_list|(
name|OutputFlag
operator|.
name|ONLCR
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|ONLRET
case|:
name|attributes
operator|.
name|setOutputFlag
argument_list|(
name|OutputFlag
operator|.
name|ONLRET
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|OPOST
case|:
name|attributes
operator|.
name|setOutputFlag
argument_list|(
name|OutputFlag
operator|.
name|OPOST
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|0
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|int
name|w
init|=
name|Integer
operator|.
name|parseInt
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
decl_stmt|;
name|int
name|h
init|=
name|Integer
operator|.
name|parseInt
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
decl_stmt|;
name|setSize
argument_list|(
operator|new
name|Size
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleSignal
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
if|if
condition|(
name|signal
operator|==
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
name|INT
condition|)
block|{
name|raise
argument_list|(
name|Signal
operator|.
name|INT
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signal
operator|==
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
name|QUIT
condition|)
block|{
name|raise
argument_list|(
name|Signal
operator|.
name|QUIT
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signal
operator|==
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
name|TSTP
condition|)
block|{
name|raise
argument_list|(
name|Signal
operator|.
name|TSTP
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signal
operator|==
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
name|CONT
condition|)
block|{
name|raise
argument_list|(
name|Signal
operator|.
name|CONT
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signal
operator|==
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
condition|)
block|{
name|int
name|w
init|=
name|Integer
operator|.
name|parseInt
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
decl_stmt|;
name|int
name|h
init|=
name|Integer
operator|.
name|parseInt
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
decl_stmt|;
name|setSize
argument_list|(
operator|new
name|Size
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
argument_list|)
expr_stmt|;
name|raise
argument_list|(
name|Signal
operator|.
name|WINCH
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|size
operator|.
name|getColumns
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|size
operator|.
name|getRows
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|,
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
modifier|...
name|signal
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|,
name|EnumSet
argument_list|<
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
argument_list|>
name|signals
parameter_list|)
block|{
comment|// TODO:JLINE
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeSignalListener
parameter_list|(
name|SignalListener
name|listener
parameter_list|)
block|{
comment|// TODO:JLINE
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
name|echo
argument_list|()
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
name|echo
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

