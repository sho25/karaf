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
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|karaf
operator|.
name|shell
operator|.
name|commands
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
name|commands
operator|.
name|Option
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
name|console
operator|.
name|AbstractAction
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"shell"
argument_list|,
name|name
operator|=
literal|"more"
argument_list|,
name|description
operator|=
literal|"File pager."
argument_list|)
specifier|public
class|class
name|MoreAction
extends|extends
name|AbstractAction
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--lines"
argument_list|,
name|description
operator|=
literal|"stop after N lines"
argument_list|)
name|int
name|lines
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|Terminal
name|term
init|=
operator|(
name|Terminal
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".jline.terminal"
argument_list|)
decl_stmt|;
if|if
condition|(
name|term
operator|==
literal|null
operator|||
operator|!
name|isTty
argument_list|(
name|System
operator|.
name|out
argument_list|)
condition|)
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|System
operator|.
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|checkInterrupted
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
name|boolean
name|echo
init|=
name|term
operator|.
name|isEchoEnabled
argument_list|()
decl_stmt|;
name|term
operator|.
name|setEchoEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|lines
operator|==
literal|0
condition|)
block|{
name|lines
operator|=
name|term
operator|.
name|getHeight
argument_list|()
expr_stmt|;
block|}
name|LineSplitter
name|reader
init|=
operator|new
name|LineSplitter
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|System
operator|.
name|in
argument_list|)
argument_list|)
argument_list|,
name|term
operator|.
name|getWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|c
decl_stmt|;
do|do
block|{
do|do
block|{
name|String
name|line
decl_stmt|;
if|if
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|checkInterrupted
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
operator|++
name|count
operator|<
name|lines
operator|-
literal|2
condition|)
do|;
name|c
operator|=
operator|-
literal|1
expr_stmt|;
while|while
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"--More--"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|c
operator|=
name|session
operator|.
name|getKeyboard
argument_list|()
operator|.
name|read
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'q'
case|:
case|case
operator|-
literal|1
case|:
name|c
operator|=
literal|'q'
expr_stmt|;
break|break;
case|case
literal|'\r'
case|:
case|case
literal|'\n'
case|:
case|case
literal|14
case|:
comment|// Down arrow
name|count
operator|--
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\r          \r"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|' '
case|:
name|count
operator|=
literal|0
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\r          \r"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|16
case|:
comment|// Up arrow
comment|// fall through
default|default:
name|c
operator|=
operator|-
literal|1
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\r          \r"
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|c
operator|==
literal|'q'
condition|)
block|{
break|break;
block|}
block|}
block|}
do|while
condition|(
name|c
operator|!=
literal|'q'
condition|)
do|;
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Interupted by user"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|term
operator|.
name|setEchoEnabled
argument_list|(
name|echo
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|LineSplitter
block|{
specifier|private
specifier|final
name|BufferedReader
name|reader
decl_stmt|;
specifier|private
specifier|final
name|int
name|width
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|LineSplitter
parameter_list|(
name|BufferedReader
name|reader
parameter_list|,
name|int
name|width
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|reader
expr_stmt|;
name|this
operator|.
name|width
operator|=
name|width
expr_stmt|;
block|}
specifier|public
name|String
name|readLine
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|lines
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|str
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
while|while
condition|(
name|str
operator|.
name|length
argument_list|()
operator|>
name|width
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|width
argument_list|)
argument_list|)
expr_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
name|width
argument_list|)
expr_stmt|;
block|}
name|lines
operator|.
name|add
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
return|return
name|lines
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
specifier|protected
name|boolean
name|isTty
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
try|try
block|{
name|Method
name|mth
init|=
name|out
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getCurrent"
argument_list|)
decl_stmt|;
name|mth
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|current
init|=
name|mth
operator|.
name|invoke
argument_list|(
name|out
argument_list|)
decl_stmt|;
return|return
name|current
operator|==
name|session
operator|.
name|getConsole
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

