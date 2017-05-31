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
name|Comparator
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
name|CommandProxy
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
name|CommandProcessor
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
name|karaf
operator|.
name|shell
operator|.
name|commands
operator|.
name|CommandWithAction
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
name|CommandSessionHolder
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
name|SessionProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceListener
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

begin_comment
comment|/**  * Like the {@link org.apache.karaf.shell.console.completer.CommandsCompleter} but does not use OSGi but is  * instead used from the non-OSGi {@link org.apache.karaf.shell.console.impl.Main}  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|CommandsCompleter
implements|implements
name|Completer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|COMMANDS
init|=
literal|".commands"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CommandsCompleter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CommandSession
name|session
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|globalCompleters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|localCompleters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|commands
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|CommandTracker
name|tracker
decl_stmt|;
specifier|public
name|CommandsCompleter
parameter_list|()
block|{
name|this
argument_list|(
name|CommandSessionHolder
operator|.
name|getSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CommandsCompleter
parameter_list|(
name|CommandSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
try|try
block|{
name|tracker
operator|=
operator|new
name|CommandTracker
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore in case we're not in OSGi
block|}
block|}
specifier|public
name|void
name|dispose
parameter_list|()
block|{
if|if
condition|(
name|tracker
operator|!=
literal|null
condition|)
block|{
name|tracker
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
name|session
operator|=
name|CommandSessionHolder
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|scopes
init|=
name|getCurrentScopes
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
index|[]
name|allCompleters
init|=
name|checkData
argument_list|()
decl_stmt|;
name|sort
argument_list|(
name|allCompleters
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
name|String
name|subShell
init|=
name|getCurrentSubShell
argument_list|()
decl_stmt|;
name|String
name|completion
init|=
name|getCompletionType
argument_list|()
decl_stmt|;
comment|// SUBSHELL mode
if|if
condition|(
literal|"SUBSHELL"
operator|.
name|equalsIgnoreCase
argument_list|(
name|completion
argument_list|)
condition|)
block|{
if|if
condition|(
name|subShell
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|subShell
operator|=
literal|"*"
expr_stmt|;
block|}
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|allCompleters
index|[
literal|1
index|]
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|subShell
operator|+
literal|":"
argument_list|)
condition|)
block|{
name|completers
operator|.
name|add
argument_list|(
name|allCompleters
index|[
literal|1
index|]
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|subShell
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|completers
operator|.
name|add
argument_list|(
operator|new
name|StringsCompleter
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"exit"
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|completers
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
if|if
condition|(
literal|"FIRST"
operator|.
name|equalsIgnoreCase
argument_list|(
name|completion
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|subShell
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|allCompleters
index|[
literal|1
index|]
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|subShell
operator|+
literal|":"
argument_list|)
condition|)
block|{
name|completers
operator|.
name|add
argument_list|(
name|allCompleters
index|[
literal|1
index|]
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|completers
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
if|if
condition|(
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
block|}
name|List
argument_list|<
name|Completer
argument_list|>
name|compl
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|compl
operator|.
name|add
argument_list|(
operator|new
name|StringsCompleter
argument_list|(
name|getAliases
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|compl
operator|.
name|addAll
argument_list|(
name|allCompleters
index|[
literal|0
index|]
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|compl
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
name|List
argument_list|<
name|Completer
argument_list|>
name|compl
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|compl
operator|.
name|add
argument_list|(
operator|new
name|StringsCompleter
argument_list|(
name|getAliases
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|compl
operator|.
name|addAll
argument_list|(
name|allCompleters
index|[
literal|0
index|]
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|compl
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
specifier|protected
name|void
name|sort
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
index|[]
name|completers
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|scopes
parameter_list|)
block|{
name|ScopeComparator
name|comparator
init|=
operator|new
name|ScopeComparator
argument_list|(
name|scopes
argument_list|)
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
name|completers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|map
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|completers
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|completers
index|[
name|i
index|]
operator|=
name|map
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|ScopeComparator
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|scopes
decl_stmt|;
specifier|public
name|ScopeComparator
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
name|String
index|[]
name|p1
init|=
name|o1
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
index|[]
name|p2
init|=
name|o2
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|int
name|p
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|p
operator|<
name|p1
operator|.
name|length
operator|&&
name|p
operator|<
name|p2
operator|.
name|length
condition|)
block|{
name|int
name|i1
init|=
name|scopes
operator|.
name|indexOf
argument_list|(
name|p1
index|[
name|p
index|]
argument_list|)
decl_stmt|;
name|int
name|i2
init|=
name|scopes
operator|.
name|indexOf
argument_list|(
name|p2
index|[
name|p
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|i1
operator|<
literal|0
condition|)
block|{
if|if
condition|(
name|i2
operator|<
literal|0
condition|)
block|{
name|int
name|c
init|=
name|p1
index|[
name|p
index|]
operator|.
name|compareTo
argument_list|(
name|p2
index|[
name|p
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|0
condition|)
block|{
return|return
name|c
return|;
block|}
else|else
block|{
name|p
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
return|return
operator|+
literal|1
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|i2
operator|<
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|i1
operator|<
name|i2
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|i1
operator|>
name|i2
condition|)
block|{
return|return
operator|+
literal|1
return|;
block|}
else|else
block|{
name|p
operator|++
expr_stmt|;
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getCurrentScopes
parameter_list|()
block|{
name|String
name|scopes
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|get
argument_list|(
literal|"SCOPE"
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|scopes
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getCurrentSubShell
parameter_list|()
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|get
argument_list|(
literal|"SUBSHELL"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
specifier|protected
name|String
name|getCompletionType
parameter_list|()
block|{
name|String
name|completion
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|get
argument_list|(
name|SessionProperties
operator|.
name|COMPLETION_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|completion
operator|==
literal|null
condition|)
block|{
name|completion
operator|=
literal|"GLOBAL"
expr_stmt|;
block|}
return|return
name|completion
return|;
block|}
specifier|protected
name|String
name|stripScope
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|index
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
return|return
name|index
operator|>
literal|0
condition|?
name|name
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
else|:
name|name
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"deprecation"
block|}
argument_list|)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
index|[]
name|checkData
parameter_list|()
block|{
comment|// Copy the set to avoid concurrent modification exceptions
comment|// TODO: fix that in gogo instead
name|Set
argument_list|<
name|String
argument_list|>
name|names
decl_stmt|;
name|boolean
name|update
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|names
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
operator|(
name|Set
argument_list|<
name|String
argument_list|>
operator|)
name|session
operator|.
name|get
argument_list|(
name|COMMANDS
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|=
operator|!
name|names
operator|.
name|equals
argument_list|(
name|commands
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|update
condition|)
block|{
comment|// get command aliases
name|Set
argument_list|<
name|String
argument_list|>
name|commands
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|global
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Completer
argument_list|>
name|local
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// add argument completers for each command
for|for
control|(
name|String
name|command
range|:
name|names
control|)
block|{
name|String
name|rawCommand
init|=
name|stripScope
argument_list|(
name|command
argument_list|)
decl_stmt|;
name|Function
name|function
init|=
operator|(
name|Function
operator|)
name|session
operator|.
name|get
argument_list|(
name|command
argument_list|)
decl_stmt|;
name|function
operator|=
name|unProxy
argument_list|(
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|CommandWithAction
condition|)
block|{
try|try
block|{
name|global
operator|.
name|put
argument_list|(
name|command
argument_list|,
operator|new
name|ArgumentCompleter
argument_list|(
name|session
argument_list|,
operator|(
name|CommandWithAction
operator|)
name|function
argument_list|,
name|command
argument_list|)
argument_list|)
expr_stmt|;
name|local
operator|.
name|put
argument_list|(
name|command
argument_list|,
operator|new
name|ArgumentCompleter
argument_list|(
name|session
argument_list|,
operator|(
name|CommandWithAction
operator|)
name|function
argument_list|,
name|rawCommand
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Unable to create completers for command '"
operator|+
name|command
operator|+
literal|"'"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|function
operator|instanceof
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
name|CommandWithAction
condition|)
block|{
try|try
block|{
name|global
operator|.
name|put
argument_list|(
name|command
argument_list|,
operator|new
name|OldArgumentCompleter
argument_list|(
name|session
argument_list|,
operator|(
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
name|CommandWithAction
operator|)
name|function
argument_list|,
name|command
argument_list|)
argument_list|)
expr_stmt|;
name|local
operator|.
name|put
argument_list|(
name|command
argument_list|,
operator|new
name|OldArgumentCompleter
argument_list|(
name|session
argument_list|,
operator|(
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
name|CommandWithAction
operator|)
name|function
argument_list|,
name|rawCommand
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Unable to create completers for command '"
operator|+
name|command
operator|+
literal|"'"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|commands
operator|.
name|add
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|this
operator|.
name|commands
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|globalCompleters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|localCompleters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|commands
operator|.
name|addAll
argument_list|(
name|commands
argument_list|)
expr_stmt|;
name|this
operator|.
name|globalCompleters
operator|.
name|putAll
argument_list|(
name|global
argument_list|)
expr_stmt|;
name|this
operator|.
name|localCompleters
operator|.
name|putAll
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
return|return
operator|new
name|Map
index|[]
block|{
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|this
operator|.
name|globalCompleters
argument_list|)
block|,
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|this
operator|.
name|localCompleters
argument_list|)
block|}
return|;
block|}
block|}
comment|/**      * Get the aliases defined in the console session.      *      * @return the aliases set      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getAliases
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|vars
init|=
operator|(
operator|(
name|Set
argument_list|<
name|String
argument_list|>
operator|)
name|session
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|aliases
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|var
range|:
name|vars
control|)
block|{
name|Object
name|content
init|=
name|session
operator|.
name|get
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
operator|&&
literal|"org.apache.felix.gogo.runtime.Closure"
operator|.
name|equals
argument_list|(
name|content
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|aliases
operator|.
name|add
argument_list|(
name|var
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|aliases
return|;
block|}
specifier|public
specifier|static
name|Function
name|unProxy
parameter_list|(
name|Function
name|function
parameter_list|)
block|{
if|if
condition|(
name|function
operator|==
literal|null
operator|||
name|function
operator|.
name|getClass
argument_list|()
operator|!=
name|CommandProxy
operator|.
name|class
condition|)
block|{
return|return
name|function
return|;
block|}
name|CommandProxy
name|proxy
init|=
operator|(
name|CommandProxy
operator|)
name|function
decl_stmt|;
name|Object
name|target
init|=
name|proxy
operator|.
name|getTarget
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|target
operator|instanceof
name|Function
condition|?
operator|(
name|Function
operator|)
name|target
else|:
name|function
return|;
block|}
finally|finally
block|{
name|proxy
operator|.
name|ungetTarget
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|CommandTracker
block|{
specifier|private
specifier|final
name|ServiceListener
name|listener
decl_stmt|;
specifier|public
name|CommandTracker
parameter_list|()
throws|throws
name|Exception
block|{
name|BundleContext
name|context
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Bundle is stopped"
argument_list|)
throw|;
block|}
name|listener
operator|=
operator|new
name|ServiceListener
argument_list|()
block|{
specifier|public
name|void
name|serviceChanged
parameter_list|(
name|ServiceEvent
name|event
parameter_list|)
block|{
synchronized|synchronized
init|(
name|CommandsCompleter
operator|.
name|this
init|)
block|{
name|commands
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
name|context
operator|.
name|addServiceListener
argument_list|(
name|listener
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"(&(%s=*)(%s=*))"
argument_list|,
name|CommandProcessor
operator|.
name|COMMAND_SCOPE
argument_list|,
name|CommandProcessor
operator|.
name|COMMAND_FUNCTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|BundleContext
name|context
init|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|removeServiceListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

