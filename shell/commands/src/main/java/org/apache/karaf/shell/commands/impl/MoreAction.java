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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
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
name|lifecycle
operator|.
name|Service
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
name|ansi
operator|.
name|AnsiSplitter
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
annotation|@
name|Service
specifier|public
class|class
name|MoreAction
implements|implements
name|Action
block|{
specifier|private
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
name|Reference
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
name|Terminal
name|terminal
decl_stmt|;
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|terminal
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
name|terminal
operator|.
name|isEchoEnabled
argument_list|()
decl_stmt|;
name|terminal
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
name|terminal
operator|.
name|getHeight
argument_list|()
expr_stmt|;
block|}
name|AnsiSplitter
operator|.
name|AnsiBufferedReader
name|reader
init|=
name|AnsiSplitter
operator|.
name|splitter
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|terminal
operator|.
name|getWidth
argument_list|()
argument_list|,
literal|4
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
literal|"Interrupted by user"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|terminal
operator|.
name|setEchoEnabled
argument_list|(
name|echo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
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
comment|/**      * This is for long running commands to be interrupted by ctrl-c.      *      * @throws InterruptedException If the command is interrupted.      */
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

