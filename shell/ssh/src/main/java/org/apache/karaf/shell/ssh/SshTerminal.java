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
name|InputStreamReader
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
name|Map
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|TerminalSupport
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|console
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|jline
operator|.
name|internal
operator|.
name|ReplayPrefixOneCharInputStream
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
import|import static
name|jline
operator|.
name|console
operator|.
name|Key
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|ssh
operator|.
name|SshTerminal
operator|.
name|UnixKey
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|SshTerminal
extends|extends
name|TerminalSupport
block|{
specifier|private
name|Environment
name|environment
decl_stmt|;
specifier|private
name|String
name|encoding
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"input.encoding"
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
specifier|private
name|ReplayPrefixOneCharInputStream
name|replayStream
init|=
operator|new
name|ReplayPrefixOneCharInputStream
argument_list|(
name|encoding
argument_list|)
decl_stmt|;
specifier|private
name|InputStreamReader
name|replayReader
decl_stmt|;
specifier|private
name|boolean
name|deleteSendsBackspace
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|backspaceSendsDelete
init|=
literal|false
decl_stmt|;
specifier|public
name|SshTerminal
parameter_list|(
name|Environment
name|environment
parameter_list|)
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setAnsiSupported
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|environment
operator|=
name|environment
expr_stmt|;
try|try
block|{
name|replayReader
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|replayStream
argument_list|,
name|encoding
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
name|e
argument_list|)
throw|;
block|}
name|Integer
name|verase
init|=
name|this
operator|.
name|environment
operator|.
name|getPtyModes
argument_list|()
operator|.
name|get
argument_list|(
name|PtyMode
operator|.
name|VERASE
argument_list|)
decl_stmt|;
name|deleteSendsBackspace
operator|=
name|verase
operator|!=
literal|null
operator|&&
name|verase
operator|==
name|Key
operator|.
name|DELETE
operator|.
name|code
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|public
name|void
name|restore
parameter_list|()
throws|throws
name|Exception
block|{     }
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
name|super
operator|.
name|getWidth
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
name|super
operator|.
name|getHeight
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|readVirtualKey
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|c
init|=
name|readCharacter
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|Key
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
operator|==
name|DELETE
operator|&&
name|deleteSendsBackspace
condition|)
block|{
name|c
operator|=
name|BACKSPACE
operator|.
name|code
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Key
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
operator|==
name|BACKSPACE
operator|&&
name|backspaceSendsDelete
condition|)
block|{
name|c
operator|=
name|DELETE
operator|.
name|code
expr_stmt|;
block|}
name|UnixKey
name|key
init|=
name|UnixKey
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
decl_stmt|;
comment|// in Unix terminals, arrow keys are represented by a sequence of 3 characters. E.g., the up arrow key yields 27, 91, 68
if|if
condition|(
name|key
operator|==
name|ARROW_START
condition|)
block|{
comment|// also the escape key is 27 thats why we read until we have something different than 27
comment|// this is a bugfix, because otherwise pressing escape and than an arrow key was an undefined state
while|while
condition|(
name|key
operator|==
name|ARROW_START
condition|)
block|{
name|c
operator|=
name|readCharacter
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|key
operator|=
name|UnixKey
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|key
operator|==
name|ARROW_PREFIX
operator|||
name|key
operator|==
name|O_PREFIX
condition|)
block|{
name|c
operator|=
name|readCharacter
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|key
operator|=
name|UnixKey
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
name|key
operator|==
name|ARROW_UP
condition|)
block|{
return|return
name|CTRL_P
operator|.
name|code
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|ARROW_DOWN
condition|)
block|{
return|return
name|CTRL_N
operator|.
name|code
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|ARROW_LEFT
condition|)
block|{
return|return
name|CTRL_B
operator|.
name|code
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|ARROW_RIGHT
condition|)
block|{
return|return
name|CTRL_F
operator|.
name|code
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|HOME_CODE
condition|)
block|{
return|return
name|CTRL_A
operator|.
name|code
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|END_CODE
condition|)
block|{
return|return
name|CTRL_E
operator|.
name|code
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|==
name|DEL_THIRD
condition|)
block|{
name|readCharacter
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|// read 4th& ignore
return|return
name|DELETE
operator|.
name|code
return|;
block|}
block|}
block|}
comment|// handle unicode characters, thanks for a patch from amyi@inf.ed.ac.uk
if|if
condition|(
name|c
operator|>
literal|128
condition|)
block|{
comment|// handle unicode characters longer than 2 bytes,
comment|// thanks to Marc.Herbert@continuent.com
name|replayStream
operator|.
name|setInput
argument_list|(
name|c
argument_list|,
name|in
argument_list|)
expr_stmt|;
comment|// replayReader = new InputStreamReader(replayStream, encoding);
name|c
operator|=
name|replayReader
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
comment|/**      * Unix keys.      */
specifier|public
specifier|static
enum|enum
name|UnixKey
block|{
name|ARROW_START
argument_list|(
literal|27
argument_list|)
block|,
name|ARROW_PREFIX
argument_list|(
literal|91
argument_list|)
block|,
name|ARROW_LEFT
argument_list|(
literal|68
argument_list|)
block|,
name|ARROW_RIGHT
argument_list|(
literal|67
argument_list|)
block|,
name|ARROW_UP
argument_list|(
literal|65
argument_list|)
block|,
name|ARROW_DOWN
argument_list|(
literal|66
argument_list|)
block|,
name|O_PREFIX
argument_list|(
literal|79
argument_list|)
block|,
name|HOME_CODE
argument_list|(
literal|72
argument_list|)
block|,
name|END_CODE
argument_list|(
literal|70
argument_list|)
block|,
name|DEL_THIRD
argument_list|(
literal|51
argument_list|)
block|,
name|DEL_SECOND
argument_list|(
literal|126
argument_list|)
block|,;
specifier|public
specifier|final
name|short
name|code
decl_stmt|;
name|UnixKey
parameter_list|(
specifier|final
name|int
name|code
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
operator|(
name|short
operator|)
name|code
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Short
argument_list|,
name|UnixKey
argument_list|>
name|codes
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Short
argument_list|,
name|UnixKey
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Short
argument_list|,
name|UnixKey
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UnixKey
name|key
range|:
name|UnixKey
operator|.
name|values
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
operator|.
name|code
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|codes
operator|=
name|map
expr_stmt|;
block|}
specifier|public
specifier|static
name|UnixKey
name|valueOf
parameter_list|(
specifier|final
name|int
name|code
parameter_list|)
block|{
return|return
name|codes
operator|.
name|get
argument_list|(
operator|(
name|short
operator|)
name|code
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

