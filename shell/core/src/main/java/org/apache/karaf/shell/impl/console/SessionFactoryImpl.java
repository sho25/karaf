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
name|impl
operator|.
name|console
package|;
end_package

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
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|List
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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|jline
operator|.
name|Builtin
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
name|jline
operator|.
name|Posix
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
name|jline
operator|.
name|Procedural
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
name|CommandSessionImpl
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
name|Reflective
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
name|api
operator|.
name|console
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
name|console
operator|.
name|Completer
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
name|Parser
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
name|Registry
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
name|console
operator|.
name|commands
operator|.
name|ExitCommand
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
name|commands
operator|.
name|SubShellCommand
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
name|commands
operator|.
name|help
operator|.
name|HelpCommand
import|;
end_import

begin_class
specifier|public
class|class
name|SessionFactoryImpl
extends|extends
name|RegistryImpl
implements|implements
name|SessionFactory
implements|,
name|Registry
block|{
specifier|final
name|CommandProcessorImpl
name|commandProcessor
decl_stmt|;
specifier|final
name|ThreadIO
name|threadIO
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SubShellCommand
argument_list|>
name|subshells
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|closed
decl_stmt|;
specifier|public
name|SessionFactoryImpl
parameter_list|(
name|ThreadIO
name|threadIO
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|threadIO
operator|=
name|threadIO
expr_stmt|;
name|commandProcessor
operator|=
operator|new
name|CommandProcessorImpl
argument_list|(
name|threadIO
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|CommandSessionImpl
name|session
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|args
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|SessionFactoryImpl
operator|.
name|this
operator|.
name|invoke
argument_list|(
name|session
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|args
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Path
name|redirect
parameter_list|(
name|CommandSessionImpl
name|session
parameter_list|,
name|Path
name|path
parameter_list|,
name|int
name|mode
parameter_list|)
block|{
return|return
name|SessionFactoryImpl
operator|.
name|this
operator|.
name|redirect
argument_list|(
name|session
argument_list|,
name|path
argument_list|,
name|mode
argument_list|)
return|;
block|}
block|}
expr_stmt|;
name|register
argument_list|(
operator|new
name|ExitCommand
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|HelpCommand
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|register
argument_list|(
operator|new
name|ShellCommand
argument_list|(
literal|"addCommand"
argument_list|,
literal|"Add a command"
argument_list|,
name|commandProcessor
argument_list|,
literal|"addCommand"
argument_list|)
argument_list|)
expr_stmt|;
name|register
argument_list|(
operator|new
name|ShellCommand
argument_list|(
literal|"removeCommand"
argument_list|,
literal|"Remove a command"
argument_list|,
name|commandProcessor
argument_list|,
literal|"removeCommand"
argument_list|)
argument_list|)
expr_stmt|;
name|register
argument_list|(
operator|new
name|ShellCommand
argument_list|(
literal|"eval"
argument_list|,
literal|"Evaluate"
argument_list|,
name|commandProcessor
argument_list|,
literal|"eval"
argument_list|)
argument_list|)
expr_stmt|;
name|Builtin
name|builtin
init|=
operator|new
name|Builtin
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
operator|new
name|String
index|[]
block|{
literal|"format"
block|,
literal|"getopt"
block|,
literal|"new"
block|,
literal|"set"
block|,
literal|"tac"
block|,
literal|"type"
block|,
literal|"jobs"
block|,
literal|"fg"
block|,
literal|"bg"
block|,
literal|"keymap"
block|,
literal|"setopt"
block|,
literal|"unsetopt"
block|,
literal|"complete"
block|,
literal|"history"
block|,
literal|"widget"
block|,
literal|"__files"
block|,
literal|"__directories"
block|,
literal|"__usage_completion"
block|}
control|)
block|{
name|register
argument_list|(
operator|new
name|ShellCommand
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|builtin
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Procedural
name|procedural
init|=
operator|new
name|Procedural
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
operator|new
name|String
index|[]
block|{
literal|"each"
block|,
literal|"if"
block|,
literal|"not"
block|,
literal|"throw"
block|,
literal|"try"
block|,
literal|"until"
block|,
literal|"while"
block|,
literal|"break"
block|,
literal|"continue"
block|}
control|)
block|{
name|register
argument_list|(
operator|new
name|ShellCommand
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|procedural
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Posix
name|posix
init|=
operator|new
name|Posix
argument_list|(
name|commandProcessor
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
operator|new
name|String
index|[]
block|{
literal|"cat"
block|,
literal|"echo"
block|,
literal|"grep"
block|,
literal|"sort"
block|,
literal|"sleep"
block|,
literal|"cd"
block|,
literal|"pwd"
block|,
literal|"ls"
block|,
literal|"less"
block|,
literal|"nano"
block|,
literal|"head"
block|,
literal|"tail"
block|,
literal|"clear"
block|,
literal|"wc"
block|,
literal|"date"
block|,
literal|"tmux"
block|,
literal|"ttop"
block|}
control|)
block|{
name|register
argument_list|(
operator|new
name|ShellCommand
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|posix
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Object
name|invoke
parameter_list|(
name|CommandSessionImpl
name|session
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|args
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|Reflective
operator|.
name|invoke
argument_list|(
name|session
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|args
argument_list|)
return|;
block|}
specifier|protected
name|Path
name|redirect
parameter_list|(
name|CommandSessionImpl
name|session
parameter_list|,
name|Path
name|path
parameter_list|,
name|int
name|mode
parameter_list|)
block|{
return|return
name|session
operator|.
name|currentDir
argument_list|()
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
return|;
block|}
specifier|public
name|CommandProcessorImpl
name|getCommandProcessor
parameter_list|()
block|{
return|return
name|commandProcessor
return|;
block|}
annotation|@
name|Override
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|command
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|String
name|scope
init|=
name|command
operator|.
name|getScope
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|command
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Session
operator|.
name|SCOPE_GLOBAL
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|subshells
operator|.
name|containsKey
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|SubShellCommand
name|subShell
init|=
operator|new
name|SubShellCommand
argument_list|(
name|scope
argument_list|)
decl_stmt|;
name|subshells
operator|.
name|put
argument_list|(
name|scope
argument_list|,
name|subShell
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|subShell
argument_list|)
expr_stmt|;
block|}
name|subshells
operator|.
name|get
argument_list|(
name|scope
argument_list|)
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
name|commandProcessor
operator|.
name|addCommand
argument_list|(
name|scope
argument_list|,
name|wrap
argument_list|(
name|command
argument_list|)
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|register
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Function
name|wrap
parameter_list|(
name|Command
name|command
parameter_list|)
block|{
return|return
operator|new
name|CommandWrapper
argument_list|(
name|command
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|unregister
parameter_list|(
name|Object
name|service
parameter_list|)
block|{
synchronized|synchronized
init|(
name|services
init|)
block|{
name|super
operator|.
name|unregister
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Command
condition|)
block|{
name|Command
name|command
init|=
operator|(
name|Command
operator|)
name|service
decl_stmt|;
name|String
name|scope
init|=
name|command
operator|.
name|getScope
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|command
operator|.
name|getName
argument_list|()
decl_stmt|;
name|commandProcessor
operator|.
name|removeCommand
argument_list|(
name|scope
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Session
operator|.
name|SCOPE_GLOBAL
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
if|if
condition|(
name|subshells
operator|.
name|get
argument_list|(
name|scope
argument_list|)
operator|.
name|decrement
argument_list|()
operator|==
literal|0
condition|)
block|{
name|SubShellCommand
name|subShell
init|=
name|subshells
operator|.
name|remove
argument_list|(
name|scope
argument_list|)
decl_stmt|;
name|unregister
argument_list|(
name|subShell
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|Session
name|create
parameter_list|(
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
name|term
parameter_list|,
name|String
name|encoding
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|)
block|{
synchronized|synchronized
init|(
name|commandProcessor
init|)
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SessionFactory has been closed"
argument_list|)
throw|;
block|}
specifier|final
name|Session
name|session
init|=
operator|new
name|ConsoleSessionImpl
argument_list|(
name|this
argument_list|,
name|commandProcessor
argument_list|,
name|threadIO
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|term
argument_list|,
name|encoding
argument_list|,
name|closeCallback
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|session
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
return|return
name|session
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Session
name|create
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
block|{
return|return
name|create
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Session
name|create
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Session
name|parent
parameter_list|)
block|{
synchronized|synchronized
init|(
name|commandProcessor
init|)
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SessionFactory has been closed"
argument_list|)
throw|;
block|}
specifier|final
name|Session
name|session
init|=
operator|new
name|HeadlessSessionImpl
argument_list|(
name|this
argument_list|,
name|commandProcessor
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|parent
argument_list|)
decl_stmt|;
return|return
name|session
return|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
synchronized|synchronized
init|(
name|commandProcessor
init|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
name|commandProcessor
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ShellCommand
implements|implements
name|Command
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|String
name|desc
decl_stmt|;
specifier|private
specifier|final
name|Executable
name|consumer
decl_stmt|;
interface|interface
name|Executable
block|{
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
name|args
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
interface|interface
name|ExecutableStr
block|{
name|void
name|execute
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
specifier|public
name|ShellCommand
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|desc
parameter_list|,
name|Executable
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|desc
operator|=
name|desc
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
specifier|public
name|ShellCommand
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|desc
parameter_list|,
name|ExecutableStr
name|consumer
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|desc
argument_list|,
name|wrap
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ShellCommand
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|desc
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|desc
argument_list|,
name|wrap
argument_list|(
name|target
argument_list|,
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Executable
name|wrap
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
parameter_list|(
name|session
parameter_list|,
name|args
parameter_list|)
lambda|->
name|Reflective
operator|.
name|invoke
argument_list|(
name|session
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|args
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Executable
name|wrap
parameter_list|(
name|ExecutableStr
name|command
parameter_list|)
block|{
return|return
parameter_list|(
name|session
parameter_list|,
name|args
parameter_list|)
lambda|->
block|{
name|command
operator|.
name|execute
argument_list|(
name|session
argument_list|,
name|asStringArray
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|;
block|}
specifier|private
specifier|static
name|String
index|[]
name|asStringArray
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|args
parameter_list|)
block|{
name|String
index|[]
name|argv
init|=
operator|new
name|String
index|[
name|args
operator|.
name|size
argument_list|()
index|]
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
name|argv
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|argv
index|[
name|i
index|]
operator|=
name|Objects
operator|.
name|toString
argument_list|(
name|args
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|argv
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
literal|"shell"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|desc
return|;
block|}
annotation|@
name|Override
specifier|public
name|Completer
name|getCompleter
parameter_list|(
name|boolean
name|scoped
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Parser
name|getParser
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|Session
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
name|CommandSession
name|cmdSession
init|=
operator|(
name|CommandSession
operator|)
name|session
operator|.
name|get
argument_list|(
literal|".commandSession"
argument_list|)
decl_stmt|;
return|return
name|consumer
operator|.
name|execute
argument_list|(
name|cmdSession
argument_list|,
name|arguments
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

