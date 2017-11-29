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
name|Collection
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Candidate
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
name|CommandLine
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
name|impl
operator|.
name|console
operator|.
name|osgi
operator|.
name|secured
operator|.
name|SecuredCommand
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
name|osgi
operator|.
name|secured
operator|.
name|SecuredSessionFactoryImpl
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
name|completers
operator|.
name|ArgumentCommandLine
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
name|completers
operator|.
name|StringsCompleter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|LineReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|ParsedLine
import|;
end_import

begin_comment
comment|/**  * Overall command line completer.  */
end_comment

begin_class
specifier|public
class|class
name|CommandsCompleter
extends|extends
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
name|completers
operator|.
name|CommandsCompleter
implements|implements
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|Completer
block|{
specifier|private
specifier|final
name|SessionFactory
name|factory
decl_stmt|;
specifier|private
specifier|final
name|Session
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
name|Completer
name|aliasesCompleter
init|=
operator|new
name|SimpleCommandCompleter
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
return|return
name|getAliases
argument_list|(
name|session
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Command
argument_list|>
name|commands
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|CommandsCompleter
parameter_list|(
name|SessionFactory
name|factory
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|complete
parameter_list|(
name|LineReader
name|reader
parameter_list|,
name|ParsedLine
name|line
parameter_list|,
name|List
argument_list|<
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
name|CommandLine
name|commandLine
init|=
operator|new
name|CommandLineImpl
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Candidate
argument_list|>
name|cands
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|cands
argument_list|)
expr_stmt|;
for|for
control|(
name|Candidate
name|cand
range|:
name|cands
control|)
block|{
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|Candidate
argument_list|(
name|cand
operator|.
name|value
argument_list|()
argument_list|,
name|cand
operator|.
name|displ
argument_list|()
argument_list|,
name|cand
operator|.
name|group
argument_list|()
argument_list|,
name|cand
operator|.
name|descr
argument_list|()
argument_list|,
name|cand
operator|.
name|suffix
argument_list|()
argument_list|,
name|cand
operator|.
name|key
argument_list|()
argument_list|,
name|cand
operator|.
name|complete
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|completeCandidates
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
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
name|List
argument_list|<
name|String
argument_list|>
name|scopes
init|=
name|getCurrentScopes
argument_list|(
name|session
argument_list|)
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
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|String
name|completion
init|=
name|getCompletionType
argument_list|(
name|session
argument_list|)
decl_stmt|;
comment|// SUBSHELL mode
if|if
condition|(
name|Session
operator|.
name|COMPLETION_MODE_SUBSHELL
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
name|Session
operator|.
name|SCOPE_GLOBAL
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
name|Session
operator|.
name|SCOPE_GLOBAL
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
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"exit"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|completers
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|c
operator|.
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// FIRST mode
if|if
condition|(
name|Session
operator|.
name|COMPLETION_MODE_FIRST
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
name|completers
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|c
operator|.
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
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
name|aliasesCompleter
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
name|compl
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|c
operator|.
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
argument_list|)
expr_stmt|;
return|return;
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
name|aliasesCompleter
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
name|compl
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|c
operator|.
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|List
argument_list|<
name|Candidate
argument_list|>
name|cands
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|cands
argument_list|)
expr_stmt|;
for|for
control|(
name|Candidate
name|cand
range|:
name|cands
control|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|cand
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|0
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
parameter_list|(
name|Session
name|session
parameter_list|)
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
name|Session
operator|.
name|SCOPE
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
parameter_list|(
name|Session
name|session
parameter_list|)
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
name|Session
operator|.
name|SUBSHELL
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
parameter_list|(
name|Session
name|session
parameter_list|)
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
name|Session
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
name|Session
operator|.
name|COMPLETION_MODE_GLOBAL
expr_stmt|;
block|}
return|return
name|completion
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
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
name|Collection
argument_list|<
name|Command
argument_list|>
name|commands
decl_stmt|;
name|boolean
name|update
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|commands
operator|=
name|factory
operator|.
name|getRegistry
argument_list|()
operator|.
name|getCommands
argument_list|()
expr_stmt|;
name|update
operator|=
operator|!
name|commands
operator|.
name|equals
argument_list|(
name|this
operator|.
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
name|Command
name|command
range|:
name|commands
control|)
block|{
name|String
name|key
init|=
name|command
operator|.
name|getScope
argument_list|()
operator|+
literal|":"
operator|+
name|command
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Completer
name|cg
init|=
name|command
operator|.
name|getCompleter
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|Completer
name|cl
init|=
name|command
operator|.
name|getCompleter
argument_list|(
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|cg
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|Session
operator|.
name|SCOPE_GLOBAL
operator|.
name|equals
argument_list|(
name|command
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|cg
operator|=
operator|new
name|FixedSimpleCommandCompleter
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|command
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cg
operator|=
operator|new
name|FixedSimpleCommandCompleter
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|key
argument_list|,
name|command
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cl
operator|==
literal|null
condition|)
block|{
name|cl
operator|=
operator|new
name|FixedSimpleCommandCompleter
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|command
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|global
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|cg
argument_list|)
expr_stmt|;
name|local
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|cl
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
parameter_list|(
name|Session
name|session
parameter_list|)
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
comment|//check both acl for alias and original cmd to determine if it should be visible
name|int
name|index
init|=
name|var
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
operator|&&
operator|(
name|factory
operator|instanceof
name|SecuredSessionFactoryImpl
operator|)
condition|)
block|{
name|String
name|scope
init|=
name|var
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|String
name|command
init|=
name|var
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
name|originalCmd
init|=
name|content
operator|.
name|toString
argument_list|()
decl_stmt|;
name|index
operator|=
name|originalCmd
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|Object
name|securityCmd
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|securityCmd
operator|=
operator|(
operator|(
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
name|Closure
operator|)
name|content
operator|)
operator|.
name|get
argument_list|(
name|originalCmd
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|securityCmd
operator|instanceof
name|SecuredCommand
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|SecuredSessionFactoryImpl
operator|)
name|factory
operator|)
operator|.
name|isAliasVisible
argument_list|(
name|scope
argument_list|,
name|command
argument_list|)
operator|&&
operator|(
operator|(
name|SecuredSessionFactoryImpl
operator|)
name|factory
operator|)
operator|.
name|isVisible
argument_list|(
operator|(
operator|(
name|SecuredCommand
operator|)
name|securityCmd
operator|)
operator|.
name|getScope
argument_list|()
argument_list|,
operator|(
operator|(
name|SecuredCommand
operator|)
name|securityCmd
operator|)
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
else|else
block|{
if|if
condition|(
operator|(
operator|(
name|SecuredSessionFactoryImpl
operator|)
name|factory
operator|)
operator|.
name|isVisible
argument_list|(
name|scope
argument_list|,
name|command
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
block|}
else|else
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
block|}
return|return
name|aliases
return|;
block|}
specifier|static
specifier|abstract
class|class
name|SimpleCommandCompleter
implements|implements
name|Completer
block|{
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|String
index|[]
name|args
init|=
name|commandLine
operator|.
name|getArguments
argument_list|()
decl_stmt|;
name|int
name|argIndex
init|=
name|commandLine
operator|.
name|getCursorArgumentIndex
argument_list|()
decl_stmt|;
name|StringsCompleter
name|completer
init|=
operator|new
name|StringsCompleter
argument_list|(
name|getNames
argument_list|(
name|session
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|argIndex
operator|==
literal|0
condition|)
block|{
name|int
name|res
init|=
name|completer
operator|.
name|complete
argument_list|(
name|session
argument_list|,
operator|new
name|ArgumentCommandLine
argument_list|(
name|args
index|[
name|argIndex
index|]
argument_list|,
name|commandLine
operator|.
name|getArgumentPosition
argument_list|()
argument_list|)
argument_list|,
name|candidates
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|>
operator|-
literal|1
condition|)
block|{
name|res
operator|+=
name|commandLine
operator|.
name|getBufferPosition
argument_list|()
operator|-
name|commandLine
operator|.
name|getArgumentPosition
argument_list|()
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|verifyCompleter
argument_list|(
name|session
argument_list|,
name|completer
argument_list|,
name|args
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|protected
specifier|abstract
name|Collection
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|(
name|Session
name|session
parameter_list|)
function_decl|;
specifier|private
name|boolean
name|verifyCompleter
parameter_list|(
name|Session
name|session
parameter_list|,
name|Completer
name|completer
parameter_list|,
name|String
name|argument
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
return|return
name|completer
operator|.
name|complete
argument_list|(
name|session
argument_list|,
operator|new
name|ArgumentCommandLine
argument_list|(
name|argument
argument_list|,
name|argument
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|candidates
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
specifier|static
class|class
name|FixedSimpleCommandCompleter
extends|extends
name|SimpleCommandCompleter
block|{
specifier|private
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|names
decl_stmt|;
name|FixedSimpleCommandCompleter
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|this
operator|.
name|names
operator|=
name|names
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
return|return
name|names
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|CommandLineImpl
implements|implements
name|CommandLine
block|{
specifier|private
specifier|final
name|ParsedLine
name|line
decl_stmt|;
specifier|public
name|CommandLineImpl
parameter_list|(
name|ParsedLine
name|line
parameter_list|)
block|{
name|this
operator|.
name|line
operator|=
name|line
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCursorArgumentIndex
parameter_list|()
block|{
return|return
name|line
operator|.
name|wordIndex
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCursorArgument
parameter_list|()
block|{
return|return
name|line
operator|.
name|word
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getArgumentPosition
parameter_list|()
block|{
return|return
name|line
operator|.
name|wordCursor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getArguments
parameter_list|()
block|{
return|return
name|line
operator|.
name|words
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|line
operator|.
name|words
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getBufferPosition
parameter_list|()
block|{
return|return
name|line
operator|.
name|cursor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getBuffer
parameter_list|()
block|{
return|return
name|line
operator|.
name|line
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

