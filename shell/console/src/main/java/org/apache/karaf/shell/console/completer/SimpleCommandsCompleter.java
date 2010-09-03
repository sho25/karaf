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
name|console
operator|.
name|completer
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
name|felix
operator|.
name|gogo
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
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|basic
operator|.
name|DefaultActionPreparator
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
name|CompletableFunction
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
name|console
operator|.
name|Main
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
name|osgi
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
name|osgi
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * Like the {@link org.apache.karaf.shell.console.completer.CommandsCompleter} but does not use OSGi but is  * instead used from the non-OSGi {@link org.apache.karaf.shell.console.Main}  */
end_comment

begin_class
specifier|public
class|class
name|SimpleCommandsCompleter
implements|implements
name|Completer
block|{
specifier|private
specifier|final
name|Main
name|main
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Command
argument_list|,
name|Completer
argument_list|>
name|completers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Command
argument_list|,
name|Completer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Function
argument_list|>
name|functions
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Function
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Command
argument_list|>
name|commands
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Command
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|SimpleCommandsCompleter
parameter_list|(
name|Main
name|main
parameter_list|)
block|{
name|this
operator|.
name|main
operator|=
name|main
expr_stmt|;
block|}
specifier|public
name|void
name|addCommand
parameter_list|(
name|Command
name|command
parameter_list|,
name|Function
name|function
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
name|getNames
argument_list|(
name|command
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Completer
argument_list|>
name|cl
init|=
operator|new
name|ArrayList
argument_list|<
name|Completer
argument_list|>
argument_list|()
decl_stmt|;
name|cl
operator|.
name|add
argument_list|(
operator|new
name|StringsCompleter
argument_list|(
name|names
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|CompletableFunction
condition|)
block|{
name|List
argument_list|<
name|Completer
argument_list|>
name|fcl
init|=
operator|(
operator|(
name|CompletableFunction
operator|)
name|function
operator|)
operator|.
name|getCompleters
argument_list|()
decl_stmt|;
if|if
condition|(
name|fcl
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Completer
name|c
range|:
name|fcl
control|)
block|{
name|cl
operator|.
name|add
argument_list|(
name|c
operator|==
literal|null
condition|?
name|NullCompleter
operator|.
name|INSTANCE
else|:
name|c
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cl
operator|.
name|add
argument_list|(
name|NullCompleter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cl
operator|.
name|add
argument_list|(
name|NullCompleter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
name|ArgumentCompleter
name|c
init|=
operator|new
name|ArgumentCompleter
argument_list|(
name|cl
argument_list|)
decl_stmt|;
name|completers
operator|.
name|put
argument_list|(
name|command
argument_list|,
name|c
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|functions
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|function
argument_list|)
expr_stmt|;
name|commands
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|removeCommand
parameter_list|(
name|Command
name|cmd
parameter_list|)
block|{
if|if
condition|(
name|cmd
operator|!=
literal|null
condition|)
block|{
name|completers
operator|.
name|remove
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|(
name|Command
name|command
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Object
name|scope
init|=
name|command
operator|.
name|scope
argument_list|()
decl_stmt|;
name|String
name|function
init|=
name|command
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|function
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|scope
operator|==
literal|null
operator|||
name|scope
operator|.
name|equals
argument_list|(
name|main
operator|.
name|getApplication
argument_list|()
argument_list|)
operator|||
name|scope
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|function
argument_list|)
expr_stmt|;
return|return
name|names
return|;
block|}
else|else
block|{
name|names
operator|.
name|add
argument_list|(
name|scope
operator|+
literal|":"
operator|+
name|function
argument_list|)
expr_stmt|;
return|return
name|names
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|complete
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|cursor
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|completers
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|candidates
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
comment|/**      * Prints the usage for all commands      */
specifier|public
name|Object
name|printUsage
parameter_list|(
name|CommandSession
name|session
parameter_list|)
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
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
if|if
condition|(
name|commands
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
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
literal|"COMMANDS"
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Command
name|command
range|:
name|commands
operator|.
name|values
argument_list|()
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"        "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
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
name|command
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|RESET
argument_list|)
argument_list|)
expr_stmt|;
name|DefaultActionPreparator
operator|.
name|printFormatted
argument_list|(
literal|"                "
argument_list|,
name|command
operator|.
name|description
argument_list|()
argument_list|,
name|term
operator|!=
literal|null
condition|?
name|term
operator|.
name|getTerminalWidth
argument_list|()
else|:
literal|80
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Prints the usage for a single command      */
specifier|public
name|Object
name|printCommandUsage
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|command
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets just call the command if it exists using the help option
name|Function
name|function
init|=
name|functions
operator|.
name|get
argument_list|(
name|command
argument_list|)
decl_stmt|;
if|if
condition|(
name|function
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No such command: "
operator|+
name|command
argument_list|)
throw|;
block|}
return|return
name|function
operator|.
name|execute
argument_list|(
name|session
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"--help"
block|}
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

