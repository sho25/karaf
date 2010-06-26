begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|webconsole
operator|.
name|gogo
package|;
end_package

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
name|IOException
import|;
end_import

begin_class
specifier|public
class|class
name|WebTerminal
extends|extends
name|jline
operator|.
name|Terminal
block|{
specifier|public
specifier|static
specifier|final
name|short
name|ARROW_START
init|=
literal|27
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|ARROW_PREFIX
init|=
literal|91
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|ARROW_LEFT
init|=
literal|68
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|ARROW_RIGHT
init|=
literal|67
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|ARROW_UP
init|=
literal|65
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|ARROW_DOWN
init|=
literal|66
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|O_PREFIX
init|=
literal|79
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|HOME_CODE
init|=
literal|72
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|END_CODE
init|=
literal|70
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|DEL_THIRD
init|=
literal|51
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|short
name|DEL_SECOND
init|=
literal|126
decl_stmt|;
specifier|private
name|int
name|width
decl_stmt|;
specifier|private
name|int
name|height
decl_stmt|;
specifier|private
name|boolean
name|backspaceDeleteSwitched
init|=
literal|false
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
specifier|public
name|WebTerminal
parameter_list|(
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|)
block|{
name|this
operator|.
name|width
operator|=
name|width
expr_stmt|;
name|this
operator|.
name|height
operator|=
name|height
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
name|width
return|;
block|}
specifier|public
name|int
name|getTerminalHeight
parameter_list|()
block|{
return|return
name|height
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
name|int
name|readVirtualKey
parameter_list|(
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
name|backspaceDeleteSwitched
condition|)
block|{
if|if
condition|(
name|c
operator|==
name|DELETE
condition|)
block|{
name|c
operator|=
literal|'\b'
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\b'
condition|)
block|{
name|c
operator|=
name|DELETE
expr_stmt|;
block|}
block|}
comment|// in Unix terminals, arrow keys are represented by
comment|// a sequence of 3 characters. E.g., the up arrow
comment|// key yields 27, 91, 68
if|if
condition|(
name|c
operator|==
name|ARROW_START
condition|)
block|{
comment|//also the escape key is 27
comment|//thats why we read until we
comment|//have something different than 27
comment|//this is a bugfix, because otherwise
comment|//pressing escape and than an arrow key
comment|//was an undefined state
while|while
condition|(
name|c
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
block|}
if|if
condition|(
name|c
operator|==
name|ARROW_PREFIX
operator|||
name|c
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
if|if
condition|(
name|c
operator|==
name|ARROW_UP
condition|)
block|{
return|return
name|CTRL_P
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|ARROW_DOWN
condition|)
block|{
return|return
name|CTRL_N
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|ARROW_LEFT
condition|)
block|{
return|return
name|CTRL_B
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|ARROW_RIGHT
condition|)
block|{
return|return
name|CTRL_F
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|HOME_CODE
condition|)
block|{
return|return
name|CTRL_A
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|END_CODE
condition|)
block|{
return|return
name|CTRL_E
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
name|DEL_THIRD
condition|)
block|{
name|c
operator|=
name|readCharacter
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|// read 4th
return|return
name|DELETE
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
comment|/**      * This is awkward and inefficient, but probably the minimal way to add      * UTF-8 support to JLine      *      * @author<a href="mailto:Marc.Herbert@continuent.com">Marc Herbert</a>      */
specifier|static
class|class
name|ReplayPrefixOneCharInputStream
extends|extends
name|InputStream
block|{
name|byte
name|firstByte
decl_stmt|;
name|int
name|byteLength
decl_stmt|;
name|InputStream
name|wrappedStream
decl_stmt|;
name|int
name|byteRead
decl_stmt|;
specifier|final
name|String
name|encoding
decl_stmt|;
specifier|public
name|ReplayPrefixOneCharInputStream
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
specifier|public
name|void
name|setInput
parameter_list|(
name|int
name|recorded
parameter_list|,
name|InputStream
name|wrapped
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|byteRead
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|firstByte
operator|=
operator|(
name|byte
operator|)
name|recorded
expr_stmt|;
name|this
operator|.
name|wrappedStream
operator|=
name|wrapped
expr_stmt|;
name|byteLength
operator|=
literal|1
expr_stmt|;
if|if
condition|(
name|encoding
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"UTF-8"
argument_list|)
condition|)
block|{
name|setInputUTF8
argument_list|(
name|recorded
argument_list|,
name|wrapped
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|encoding
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"UTF-16"
argument_list|)
condition|)
block|{
name|byteLength
operator|=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|encoding
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"UTF-32"
argument_list|)
condition|)
block|{
name|byteLength
operator|=
literal|4
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setInputUTF8
parameter_list|(
name|int
name|recorded
parameter_list|,
name|InputStream
name|wrapped
parameter_list|)
throws|throws
name|IOException
block|{
comment|// 110yyyyy 10zzzzzz
if|if
condition|(
operator|(
name|firstByte
operator|&
operator|(
name|byte
operator|)
literal|0xE0
operator|)
operator|==
operator|(
name|byte
operator|)
literal|0xC0
condition|)
block|{
name|this
operator|.
name|byteLength
operator|=
literal|2
expr_stmt|;
comment|// 1110xxxx 10yyyyyy 10zzzzzz
block|}
elseif|else
if|if
condition|(
operator|(
name|firstByte
operator|&
operator|(
name|byte
operator|)
literal|0xF0
operator|)
operator|==
operator|(
name|byte
operator|)
literal|0xE0
condition|)
block|{
name|this
operator|.
name|byteLength
operator|=
literal|3
expr_stmt|;
comment|// 11110www 10xxxxxx 10yyyyyy 10zzzzzz
block|}
elseif|else
if|if
condition|(
operator|(
name|firstByte
operator|&
operator|(
name|byte
operator|)
literal|0xF8
operator|)
operator|==
operator|(
name|byte
operator|)
literal|0xF0
condition|)
block|{
name|this
operator|.
name|byteLength
operator|=
literal|4
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"invalid UTF-8 first byte: "
operator|+
name|firstByte
argument_list|)
throw|;
block|}
block|}
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|available
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|byteRead
operator|++
expr_stmt|;
if|if
condition|(
name|byteRead
operator|==
literal|1
condition|)
block|{
return|return
name|firstByte
return|;
block|}
return|return
name|wrappedStream
operator|.
name|read
argument_list|()
return|;
block|}
comment|/**         * InputStreamReader is greedy and will try to read bytes in advance. We         * do NOT want this to happen since we use a temporary/"losing bytes"         * InputStreamReader above, that's why we hide the real         * wrappedStream.available() here.         */
specifier|public
name|int
name|available
parameter_list|()
block|{
return|return
name|byteLength
operator|-
name|byteRead
return|;
block|}
block|}
block|}
end_class

end_unit

