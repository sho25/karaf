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
name|impl
operator|.
name|console
operator|.
name|standalone
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Manager
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
name|SessionFactory
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
name|impl
operator|.
name|action
operator|.
name|command
operator|.
name|ManagerImpl
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
name|JLineTerminal
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
name|SessionFactoryImpl
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
name|support
operator|.
name|ShellUtil
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
name|TerminalBuilder
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
comment|/**      * Use this method when the shell is being executed as a top level shell.      *      * @param args the arguments.      * @throws Exception in case of a failure.      */
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
name|InputStream
name|in
init|=
name|System
operator|.
name|in
decl_stmt|;
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|PrintStream
name|err
init|=
name|System
operator|.
name|err
decl_stmt|;
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
name|run
argument_list|(
name|threadio
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
specifier|private
name|void
name|run
parameter_list|(
name|ThreadIO
name|threadio
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
try|try
init|(
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
init|)
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
name|SessionFactory
name|sessionFactory
init|=
name|createSessionFactory
argument_list|(
name|threadio
argument_list|)
decl_stmt|;
name|run
argument_list|(
name|sessionFactory
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
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|run
parameter_list|(
specifier|final
name|SessionFactory
name|sessionFactory
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
parameter_list|,
name|ClassLoader
name|cl
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|Terminal
name|jlineTerminal
init|=
name|TerminalBuilder
operator|.
name|terminal
argument_list|()
init|)
block|{
specifier|final
name|Terminal
name|terminal
init|=
operator|new
name|JLineTerminal
argument_list|(
name|jlineTerminal
argument_list|)
decl_stmt|;
try|try
init|(
name|Session
name|session
init|=
name|createSession
argument_list|(
name|sessionFactory
argument_list|,
name|command
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
literal|null
else|:
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|terminal
argument_list|)
init|)
block|{
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
name|discoverCommands
argument_list|(
name|session
argument_list|,
name|cl
argument_list|,
name|getDiscoveryResource
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Shell is directly executing a sub/command, we don't setup a console
comment|// in this case, this avoids us reading from stdin un-necessarily.
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
name|Session
operator|.
name|PRINT_STACK_TRACES
argument_list|,
literal|"execution"
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
name|ShellUtil
operator|.
name|logException
argument_list|(
name|session
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// We are going into full blown interactive shell mode.
name|session
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Allow sub classes of main to change the ConsoleImpl implementation used.      *      * @param sessionFactory the session factory.      * @param in the input stream (console std in).      * @param out the output stream (console std out).      * @param err the error stream (console std err).      * @param terminal the terminal.      * @return the created session.      * @throws Exception if something goes wrong during session creation.      */
specifier|protected
name|Session
name|createSession
parameter_list|(
name|SessionFactory
name|sessionFactory
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
name|sessionFactory
operator|.
name|create
argument_list|(
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
specifier|protected
name|SessionFactory
name|createSessionFactory
parameter_list|(
name|ThreadIO
name|threadio
parameter_list|)
block|{
name|SessionFactoryImpl
name|sessionFactory
init|=
operator|new
name|SessionFactoryImpl
argument_list|(
name|threadio
argument_list|)
decl_stmt|;
name|sessionFactory
operator|.
name|register
argument_list|(
operator|new
name|ManagerImpl
argument_list|(
name|sessionFactory
argument_list|,
name|sessionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sessionFactory
return|;
block|}
comment|/**      * Sub classes can override so that their registered commands do not conflict with the default shell      * implementation.      *      * @return the location of the discovery resource.      */
specifier|public
name|String
name|getDiscoveryResource
parameter_list|()
block|{
return|return
literal|"META-INF/services/org/apache/karaf/shell/commands"
return|;
block|}
specifier|protected
name|void
name|discoverCommands
parameter_list|(
name|Session
name|session
parameter_list|,
name|ClassLoader
name|cl
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Manager
name|manager
init|=
operator|new
name|ManagerImpl
argument_list|(
name|session
operator|.
name|getRegistry
argument_list|()
argument_list|,
name|session
operator|.
name|getFactory
argument_list|()
operator|.
name|getRegistry
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
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
name|resource
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
name|?
argument_list|>
name|actionClass
init|=
name|cl
operator|.
name|loadClass
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|manager
operator|.
name|register
argument_list|(
name|actionClass
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
comment|/**      *<p>Returns whether or not we are in multi-scope mode.</p>      *      *<p>The default mode is multi-scoped where we prefix commands by their scope. If we are in single      * scoped mode then we don't use scope prefixes when registering or tab completing commands.</p>      *      * @return true if the console is multi-scope, false else.      */
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
argument_list|<>
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

