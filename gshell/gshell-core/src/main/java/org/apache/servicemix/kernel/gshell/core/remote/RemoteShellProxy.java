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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|ansi
operator|.
name|AnsiRenderer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|console
operator|.
name|Console
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|console
operator|.
name|JLineConsole
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|io
operator|.
name|IO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|io
operator|.
name|Closer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|notification
operator|.
name|ExitNotification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|RshClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|proxy
operator|.
name|RemoteHistoryProxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|proxy
operator|.
name|RemoteCompleterProxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|ShellContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|Shell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|ShellContextHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|stream
operator|.
name|StreamFeeder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|Variables
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * Provides a shell interface which will proxy to a remote shell instance.  *  * @version $Rev: 707952 $ $Date: 2008-10-26 08:51:45 +0100 (Sun, 26 Oct 2008) $  */
end_comment

begin_class
specifier|public
class|class
name|RemoteShellProxy
implements|implements
name|Shell
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
specifier|private
specifier|final
name|RshClient
name|client
decl_stmt|;
specifier|private
specifier|final
name|IO
name|io
decl_stmt|;
specifier|private
specifier|final
name|StreamFeeder
name|outputFeeder
decl_stmt|;
specifier|private
specifier|final
name|ShellContext
name|context
decl_stmt|;
specifier|private
specifier|final
name|RemoteHistoryProxy
name|history
decl_stmt|;
specifier|private
name|boolean
name|opened
decl_stmt|;
specifier|private
name|String
name|instance
decl_stmt|;
specifier|private
name|String
name|user
decl_stmt|;
specifier|public
name|RemoteShellProxy
parameter_list|(
specifier|final
name|RshClient
name|client
parameter_list|,
specifier|final
name|IO
name|io
parameter_list|,
specifier|final
name|String
name|instance
parameter_list|,
specifier|final
name|String
name|user
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|client
operator|!=
literal|null
assert|;
assert|assert
name|io
operator|!=
literal|null
assert|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|io
operator|=
name|io
expr_stmt|;
name|this
operator|.
name|instance
operator|=
name|instance
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
comment|//
comment|// TODO: send over some client-side details, like the terminal features, etc, as well, verbosity too)
comment|//       If any problem or denial occurs, throw an exception, once created the proxy is considered valid.
comment|//
name|client
operator|.
name|openShell
argument_list|()
expr_stmt|;
comment|// Setup other proxies
name|history
operator|=
operator|new
name|RemoteHistoryProxy
argument_list|(
name|client
argument_list|)
expr_stmt|;
comment|// Copy the client's input stream to our outputstream so users see command output
name|outputFeeder
operator|=
operator|new
name|StreamFeeder
argument_list|(
name|client
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|io
operator|.
name|outputStream
argument_list|)
expr_stmt|;
name|outputFeeder
operator|.
name|createThread
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|=
operator|new
name|ShellContext
argument_list|()
block|{
specifier|private
name|Variables
name|vars
init|=
operator|new
name|Variables
argument_list|()
decl_stmt|;
specifier|public
name|Shell
name|getShell
parameter_list|()
block|{
return|return
name|RemoteShellProxy
operator|.
name|this
return|;
block|}
specifier|public
name|IO
name|getIo
parameter_list|()
block|{
return|return
name|io
return|;
block|}
specifier|public
name|Variables
name|getVariables
parameter_list|()
block|{
return|return
name|vars
return|;
block|}
block|}
expr_stmt|;
name|opened
operator|=
literal|true
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getVariables
argument_list|()
operator|.
name|set
argument_list|(
literal|"gshell.group.name"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getVariables
argument_list|()
operator|.
name|set
argument_list|(
literal|"gshell.prompt"
argument_list|,
literal|"@|bold %{gshell.user}|@%{gshell.instance}:@|bold %{gshell.group.name}|> "
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getVariables
argument_list|()
operator|.
name|set
argument_list|(
literal|"gshell.user"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getVariables
argument_list|()
operator|.
name|set
argument_list|(
literal|"gshell.instance"
argument_list|,
name|instance
operator|!=
literal|null
condition|?
name|instance
else|:
literal|"unknown"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|ensureOpened
parameter_list|()
block|{
if|if
condition|(
operator|!
name|opened
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Remote shell proxy has been closed"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isOpened
parameter_list|()
block|{
return|return
name|opened
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
name|client
operator|.
name|closeShell
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{}
name|Closer
operator|.
name|close
argument_list|(
name|outputFeeder
argument_list|)
expr_stmt|;
name|opened
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|ShellContext
name|getContext
parameter_list|()
block|{
name|ensureOpened
argument_list|()
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|String
name|line
parameter_list|)
throws|throws
name|Exception
block|{
name|ensureOpened
argument_list|()
expr_stmt|;
name|ShellContext
name|ctx
init|=
name|ShellContextHolder
operator|.
name|get
argument_list|(
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|client
operator|.
name|execute
argument_list|(
name|line
argument_list|)
return|;
block|}
finally|finally
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|String
name|command
parameter_list|,
specifier|final
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|ensureOpened
argument_list|()
expr_stmt|;
name|ShellContext
name|ctx
init|=
name|ShellContextHolder
operator|.
name|get
argument_list|(
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|client
operator|.
name|execute
argument_list|(
name|command
argument_list|,
name|args
argument_list|)
return|;
block|}
finally|finally
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|ensureOpened
argument_list|()
expr_stmt|;
name|ShellContext
name|ctx
init|=
name|ShellContextHolder
operator|.
name|get
argument_list|(
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|client
operator|.
name|execute
argument_list|(
name|args
argument_list|)
return|;
block|}
finally|finally
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|Object
index|[]
index|[]
name|commands
parameter_list|)
throws|throws
name|Exception
block|{
name|ensureOpened
argument_list|()
expr_stmt|;
name|ShellContext
name|ctx
init|=
name|ShellContextHolder
operator|.
name|get
argument_list|(
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|client
operator|.
name|execute
argument_list|(
name|commands
argument_list|)
return|;
block|}
finally|finally
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isInteractive
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|args
operator|!=
literal|null
assert|;
name|ensureOpened
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Starting interactive console; args: {}"
argument_list|,
name|args
argument_list|)
expr_stmt|;
comment|//
comment|// TODO: We need a hook into the session state here so that we can abort the console muck when the session closes
comment|//
comment|//
comment|// TODO: Request server to load...
comment|//
comment|// loadUserScript(branding.getInteractiveScriptName());
specifier|final
name|AtomicReference
argument_list|<
name|ExitNotification
argument_list|>
name|exitNotifHolder
init|=
operator|new
name|AtomicReference
argument_list|<
name|ExitNotification
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Object
argument_list|>
name|lastResultHolder
init|=
operator|new
name|AtomicReference
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Console
operator|.
name|Executor
name|executor
init|=
operator|new
name|Console
operator|.
name|Executor
argument_list|()
block|{
specifier|public
name|Result
name|execute
parameter_list|(
specifier|final
name|String
name|line
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|line
operator|!=
literal|null
assert|;
try|try
block|{
name|Object
name|result
init|=
name|RemoteShellProxy
operator|.
name|this
operator|.
name|execute
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|lastResultHolder
operator|.
name|set
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExitNotification
name|n
parameter_list|)
block|{
name|exitNotifHolder
operator|.
name|set
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|Result
operator|.
name|STOP
return|;
block|}
return|return
name|Result
operator|.
name|CONTINUE
return|;
block|}
block|}
decl_stmt|;
name|JLineConsole
name|console
init|=
operator|new
name|JLineConsole
argument_list|(
name|executor
argument_list|,
name|io
argument_list|)
decl_stmt|;
name|console
operator|.
name|setPrompter
argument_list|(
operator|new
name|RemotePrompter
argument_list|()
argument_list|)
expr_stmt|;
name|console
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|Console
operator|.
name|ErrorHandler
argument_list|()
block|{
specifier|public
name|Result
name|handleError
parameter_list|(
specifier|final
name|Throwable
name|error
parameter_list|)
block|{
assert|assert
name|error
operator|!=
literal|null
assert|;
comment|//
comment|// TODO: Do something here...
comment|//
return|return
name|Result
operator|.
name|CONTINUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|console
operator|.
name|addCompleter
argument_list|(
operator|new
name|RemoteCompleterProxy
argument_list|(
name|client
argument_list|)
argument_list|)
expr_stmt|;
comment|//
comment|// TODO: What are we to do with history here?  Really should be history on the server...
comment|//
comment|/*         // Hook up a nice history file (we gotta hold on to the history object at some point so the 'history' command can get to it)         History history = new History();         console.setHistory(history);         console.setHistoryFile(new File(branding.getUserDirectory(), branding.getHistoryFileName()));         */
comment|// Unless the user wants us to shut up, then display a nice welcome banner
comment|/*         if (!io.isQuiet()) {             io.out.println(branding.getWelcomeBanner());         }         */
comment|// Check if there are args, and run them and then enter interactive
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
name|execute
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|// And then spin up the console and go for a jog
name|ShellContext
name|ctx
init|=
name|ShellContextHolder
operator|.
name|get
argument_list|(
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|console
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|ShellContextHolder
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
comment|// If any exit notification occured while running, then puke it up
name|ExitNotification
name|n
init|=
name|exitNotifHolder
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
throw|throw
name|n
throw|;
block|}
block|}
specifier|protected
class|class
name|RemotePrompter
implements|implements
name|Console
operator|.
name|Prompter
block|{
specifier|public
name|String
name|prompt
parameter_list|()
block|{
name|String
name|prompt
init|=
literal|"@|bold "
operator|+
name|user
operator|+
literal|"|@"
operator|+
name|instance
operator|+
literal|":> "
decl_stmt|;
comment|// Encode ANSI muck if it looks like there are codes encoded
if|if
condition|(
name|AnsiRenderer
operator|.
name|test
argument_list|(
name|prompt
argument_list|)
condition|)
block|{
name|prompt
operator|=
operator|new
name|AnsiRenderer
argument_list|()
operator|.
name|render
argument_list|(
name|prompt
argument_list|)
expr_stmt|;
block|}
return|return
name|prompt
return|;
block|}
block|}
block|}
end_class

end_unit

