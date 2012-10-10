begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|*
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|CommandException
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
name|basic
operator|.
name|AbstractCommand
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
name|gogo
operator|.
name|runtime
operator|.
name|CommandNotFoundException
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
name|gogo
operator|.
name|runtime
operator|.
name|CommandProcessorImpl
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
name|gogo
operator|.
name|runtime
operator|.
name|threadio
operator|.
name|ThreadIOImpl
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
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|Function
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
name|threadio
operator|.
name|ThreadIO
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
name|NameScoping
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
name|impl
operator|.
name|jline
operator|.
name|ConsoleImpl
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
name|impl
operator|.
name|jline
operator|.
name|TerminalFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|AnsiConsole
import|;
end_import

begin_class
specifier|public
class|class
name|Main
block|{
specifier|private
name|String
name|application
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
decl_stmt|;
specifier|private
name|String
name|user
init|=
literal|"karaf"
decl_stmt|;
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use this method when the shell is being executed as a top level shell.      *      * @param args      * @throws Exception      */
specifier|public
name|void
name|run
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|ThreadIOImpl
name|threadio
init|=
operator|new
name|ThreadIOImpl
argument_list|()
decl_stmt|;
name|threadio
operator|.
name|start
argument_list|()
expr_stmt|;
name|CommandProcessorImpl
name|commandProcessor
init|=
operator|new
name|CommandProcessorImpl
argument_list|(
name|threadio
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|unwrap
argument_list|(
name|System
operator|.
name|in
argument_list|)
decl_stmt|;
name|PrintStream
name|out
init|=
name|wrap
argument_list|(
name|unwrap
argument_list|(
name|System
operator|.
name|out
argument_list|)
argument_list|)
decl_stmt|;
name|PrintStream
name|err
init|=
name|wrap
argument_list|(
name|unwrap
argument_list|(
name|System
operator|.
name|err
argument_list|)
argument_list|)
decl_stmt|;
name|run
argument_list|(
name|commandProcessor
argument_list|,
name|args
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
comment|// TODO: do we need to stop the threadio that was started?
comment|// threadio.stop();
block|}
comment|/**      * Use this method when the shell is being executed as a command      * of another shell.      *      * @param parent      * @param args      * @throws Exception      */
specifier|public
name|void
name|run
parameter_list|(
name|CommandSession
name|parent
parameter_list|,
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: find out what the down side of not using a real ThreadIO implementation is.
name|CommandProcessorImpl
name|commandProcessor
init|=
operator|new
name|CommandProcessorImpl
argument_list|(
operator|new
name|ThreadIO
argument_list|()
block|{
specifier|public
name|void
name|setStreams
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
block|{             }
specifier|public
name|void
name|close
parameter_list|()
block|{             }
block|}
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|parent
operator|.
name|getKeyboard
argument_list|()
decl_stmt|;
name|PrintStream
name|out
init|=
name|parent
operator|.
name|getConsole
argument_list|()
decl_stmt|;
name|PrintStream
name|err
init|=
name|parent
operator|.
name|getConsole
argument_list|()
decl_stmt|;
name|run
argument_list|(
name|commandProcessor
argument_list|,
name|args
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|run
parameter_list|(
name|CommandProcessorImpl
name|commandProcessor
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|classpath
init|=
literal|null
decl_stmt|;
name|boolean
name|batch
init|=
literal|false
decl_stmt|;
name|String
name|file
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|arg
init|=
name|args
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"--classpath="
argument_list|)
condition|)
block|{
name|classpath
operator|=
name|arg
operator|.
name|substring
argument_list|(
literal|"--classpath="
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"-c="
argument_list|)
condition|)
block|{
name|classpath
operator|=
name|arg
operator|.
name|substring
argument_list|(
literal|"-c="
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
literal|"--classpath"
argument_list|)
operator|||
name|arg
operator|.
name|equals
argument_list|(
literal|"-c"
argument_list|)
condition|)
block|{
name|classpath
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
literal|"-b"
argument_list|)
operator|||
name|arg
operator|.
name|equals
argument_list|(
literal|"--batch"
argument_list|)
condition|)
block|{
name|batch
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"--file="
argument_list|)
condition|)
block|{
name|file
operator|=
name|arg
operator|.
name|substring
argument_list|(
literal|"--file="
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|startsWith
argument_list|(
literal|"-f="
argument_list|)
condition|)
block|{
name|file
operator|=
name|arg
operator|.
name|substring
argument_list|(
literal|"-f="
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
literal|"--file"
argument_list|)
operator|||
name|arg
operator|.
name|equals
argument_list|(
literal|"-f"
argument_list|)
condition|)
block|{
name|file
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|arg
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|Reader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
name|reader
operator|.
name|read
argument_list|()
init|;
name|c
operator|>=
literal|0
condition|;
name|c
operator|=
name|reader
operator|.
name|read
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|batch
condition|)
block|{
name|Reader
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
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
name|reader
operator|.
name|read
argument_list|()
init|;
name|c
operator|>=
literal|0
condition|;
name|reader
operator|.
name|read
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|ClassLoader
name|cl
init|=
name|Main
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|classpath
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|getFiles
argument_list|(
operator|new
name|File
argument_list|(
name|classpath
argument_list|)
argument_list|)
decl_stmt|;
name|cl
operator|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
operator|.
name|toArray
argument_list|(
operator|new
name|URL
index|[
name|urls
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
name|discoverCommands
argument_list|(
name|commandProcessor
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|run
argument_list|(
name|commandProcessor
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|run
parameter_list|(
specifier|final
name|CommandProcessorImpl
name|commandProcessor
parameter_list|,
name|String
name|command
parameter_list|,
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|PrintStream
name|out
parameter_list|,
specifier|final
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|command
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// Shell is directly executing a sub/command, we don't setup a terminal and console
comment|// in this case, this avoids us reading from stdin un-necessarily.
name|CommandSession
name|session
init|=
name|commandProcessor
operator|.
name|createSession
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
decl_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"USER"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"APPLICATION"
argument_list|,
name|application
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|NameScoping
operator|.
name|MULTI_SCOPE_MODE_KEY
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|isMultiScopeMode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|CommandNotFoundException
condition|)
block|{
name|String
name|str
init|=
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|a
argument_list|(
literal|"Command not found: "
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
operator|.
name|a
argument_list|(
operator|(
operator|(
name|CommandNotFoundException
operator|)
name|t
operator|)
operator|.
name|getCommand
argument_list|()
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD_OFF
argument_list|)
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|instanceof
name|CommandException
condition|)
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
operator|(
operator|(
name|CommandException
operator|)
name|t
operator|)
operator|.
name|getNiceHelp
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|(
name|session
operator|.
name|getConsole
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|print
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// We are going into full blown interactive shell mode.
specifier|final
name|TerminalFactory
name|terminalFactory
init|=
operator|new
name|TerminalFactory
argument_list|()
decl_stmt|;
specifier|final
name|Terminal
name|terminal
init|=
name|terminalFactory
operator|.
name|getTerminal
argument_list|()
decl_stmt|;
name|ConsoleImpl
name|console
init|=
name|createConsole
argument_list|(
name|commandProcessor
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|terminal
argument_list|)
decl_stmt|;
name|CommandSession
name|session
init|=
name|console
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"USER"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"APPLICATION"
argument_list|,
name|application
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|NameScoping
operator|.
name|MULTI_SCOPE_MODE_KEY
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|isMultiScopeMode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"#LINES"
argument_list|,
operator|new
name|Function
argument_list|()
block|{
specifier|public
name|Object
name|execute
parameter_list|(
name|CommandSession
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
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|terminal
operator|.
name|getHeight
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"#COLUMNS"
argument_list|,
operator|new
name|Function
argument_list|()
block|{
specifier|public
name|Object
name|execute
parameter_list|(
name|CommandSession
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
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|terminal
operator|.
name|getWidth
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|".jline.terminal"
argument_list|,
name|terminal
argument_list|)
expr_stmt|;
name|console
operator|.
name|run
argument_list|()
expr_stmt|;
name|terminalFactory
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Allow sub classes of main to change the ConsoleImpl implementation used.      *      * @param commandProcessor      * @param in      * @param out      * @param err      * @param terminal      * @return      * @throws Exception      */
specifier|protected
name|ConsoleImpl
name|createConsole
parameter_list|(
name|CommandProcessorImpl
name|commandProcessor
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Terminal
name|terminal
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ConsoleImpl
argument_list|(
name|commandProcessor
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|terminal
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Sub classes can override so that their registered commands do not conflict with the default shell      * implementation.      *      * @return      */
specifier|public
name|String
name|getDiscoveryResource
parameter_list|()
block|{
return|return
literal|"META-INF/services/org/apache/karaf/shell/commands"
return|;
block|}
specifier|private
name|void
name|discoverCommands
parameter_list|(
name|CommandProcessorImpl
name|commandProcessor
parameter_list|,
name|ClassLoader
name|cl
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|cl
operator|.
name|getResources
argument_list|(
name|getDiscoveryResource
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|line
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|!=
literal|'#'
condition|)
block|{
specifier|final
name|Class
argument_list|<
name|Action
argument_list|>
name|actionClass
init|=
operator|(
name|Class
argument_list|<
name|Action
argument_list|>
operator|)
name|cl
operator|.
name|loadClass
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|Command
name|cmd
init|=
name|actionClass
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|Function
name|function
init|=
operator|new
name|AbstractCommand
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Action
name|createNewAction
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
operator|)
name|actionClass
operator|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
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
catch|catch
parameter_list|(
name|IllegalAccessException
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
block|}
decl_stmt|;
name|addCommand
argument_list|(
name|cmd
argument_list|,
name|function
argument_list|,
name|commandProcessor
argument_list|)
expr_stmt|;
block|}
name|line
operator|=
name|r
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|addCommand
parameter_list|(
name|Command
name|cmd
parameter_list|,
name|Function
name|function
parameter_list|,
name|CommandProcessorImpl
name|commandProcessor
parameter_list|)
block|{
try|try
block|{
name|commandProcessor
operator|.
name|addCommand
argument_list|(
name|cmd
operator|.
name|scope
argument_list|()
argument_list|,
name|function
argument_list|,
name|cmd
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
block|}
specifier|public
name|String
name|getApplication
parameter_list|()
block|{
return|return
name|application
return|;
block|}
specifier|public
name|void
name|setApplication
parameter_list|(
name|String
name|application
parameter_list|)
block|{
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
block|}
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
comment|/**      * Returns whether or not we are in multi-scope mode.      *<p/>      * The default mode is multi-scoped where we prefix commands by their scope. If we are in single      * scoped mode then we don't use scope prefixes when registering or tab completing commands.      */
specifier|public
name|boolean
name|isMultiScopeMode
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
name|PrintStream
name|wrap
parameter_list|(
name|PrintStream
name|stream
parameter_list|)
block|{
name|OutputStream
name|o
init|=
name|AnsiConsole
operator|.
name|wrapOutputStream
argument_list|(
name|stream
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|PrintStream
condition|)
block|{
return|return
operator|(
operator|(
name|PrintStream
operator|)
name|o
operator|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|PrintStream
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|T
name|stream
parameter_list|)
block|{
try|try
block|{
name|Method
name|mth
init|=
name|stream
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getRoot"
argument_list|)
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|mth
operator|.
name|invoke
argument_list|(
name|stream
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
name|stream
return|;
block|}
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|URL
argument_list|>
name|getFiles
parameter_list|(
name|File
name|base
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
name|getFiles
argument_list|(
name|base
argument_list|,
name|urls
argument_list|)
expr_stmt|;
return|return
name|urls
return|;
block|}
specifier|private
specifier|static
name|void
name|getFiles
parameter_list|(
name|File
name|base
parameter_list|,
name|List
argument_list|<
name|URL
argument_list|>
name|urls
parameter_list|)
throws|throws
name|MalformedURLException
block|{
for|for
control|(
name|File
name|f
range|:
name|base
operator|.
name|listFiles
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|getFiles
argument_list|(
name|f
argument_list|,
name|urls
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
name|urls
operator|.
name|add
argument_list|(
name|f
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

