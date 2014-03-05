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
name|lang
operator|.
name|management
operator|.
name|LockInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|MonitorInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ThreadInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ThreadMXBean
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
name|Argument
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
name|support
operator|.
name|table
operator|.
name|ShellTable
import|;
end_import

begin_comment
comment|/**  * Command for showing the full tree of bundles that have been used to resolve  * a given bundle.  */
end_comment

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
literal|"threads"
argument_list|,
name|description
operator|=
literal|"Prints the current threads (optionally with stacktraces)"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ThreadsAction
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--tree"
argument_list|,
name|description
operator|=
literal|"Display threads as a tree"
argument_list|)
name|boolean
name|tree
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--list"
argument_list|,
name|description
operator|=
literal|"Display threads as a list"
argument_list|)
name|boolean
name|list
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-e"
argument_list|,
name|aliases
operator|=
block|{
literal|"--empty-groups"
block|}
argument_list|,
name|description
operator|=
literal|"Show empty groups"
argument_list|)
name|boolean
name|empty
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-t"
argument_list|,
name|aliases
operator|=
block|{
literal|"--threshold"
block|}
argument_list|,
name|description
operator|=
literal|"Minimal number of interesting stack trace line to display a thread"
argument_list|)
name|int
name|threshold
init|=
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--locks"
argument_list|,
name|description
operator|=
literal|"Display locks"
argument_list|)
name|boolean
name|locks
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--monitors"
argument_list|,
name|description
operator|=
literal|"Display monitors"
argument_list|)
name|boolean
name|monitors
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--packages"
argument_list|,
name|description
operator|=
literal|"Pruned packages"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|packages
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"java."
argument_list|,
literal|"sun."
argument_list|)
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"id"
argument_list|,
name|description
operator|=
literal|"Show details for thread with this Id"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|Long
name|id
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-format"
argument_list|,
name|description
operator|=
literal|"Disable table rendered output"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|noFormat
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
name|Map
argument_list|<
name|Long
argument_list|,
name|ThreadInfo
argument_list|>
name|threadInfos
init|=
operator|new
name|TreeMap
argument_list|<
name|Long
argument_list|,
name|ThreadInfo
argument_list|>
argument_list|()
decl_stmt|;
name|ThreadMXBean
name|threadsBean
init|=
name|ManagementFactory
operator|.
name|getThreadMXBean
argument_list|()
decl_stmt|;
name|ThreadInfo
index|[]
name|infos
decl_stmt|;
if|if
condition|(
name|threadsBean
operator|.
name|isObjectMonitorUsageSupported
argument_list|()
operator|&&
name|threadsBean
operator|.
name|isSynchronizerUsageSupported
argument_list|()
condition|)
block|{
name|infos
operator|=
name|threadsBean
operator|.
name|dumpAllThreads
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|infos
operator|=
name|threadsBean
operator|.
name|getThreadInfo
argument_list|(
name|threadsBean
operator|.
name|getAllThreadIds
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ThreadInfo
name|info
range|:
name|infos
control|)
block|{
name|threadInfos
operator|.
name|put
argument_list|(
name|info
operator|.
name|getThreadId
argument_list|()
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|ThreadInfo
name|ti
init|=
name|threadInfos
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|ti
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
literal|"Thread "
operator|+
name|ti
operator|.
name|getThreadId
argument_list|()
operator|+
literal|" "
operator|+
name|ti
operator|.
name|getThreadName
argument_list|()
operator|+
literal|" "
operator|+
name|ti
operator|.
name|getThreadState
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Stacktrace:"
argument_list|)
expr_stmt|;
name|StackTraceElement
index|[]
name|st
init|=
name|ti
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
for|for
control|(
name|StackTraceElement
name|ste
range|:
name|st
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|ste
operator|.
name|getClassName
argument_list|()
operator|+
literal|"."
operator|+
name|ste
operator|.
name|getMethodName
argument_list|()
operator|+
literal|" line: "
operator|+
name|ste
operator|.
name|getLineNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|list
condition|)
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Id"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"State"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"CPU time"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Usr time"
argument_list|)
expr_stmt|;
for|for
control|(
name|ThreadInfo
name|thread
range|:
name|threadInfos
operator|.
name|values
argument_list|()
control|)
block|{
name|long
name|id
init|=
name|thread
operator|.
name|getThreadId
argument_list|()
decl_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|id
argument_list|,
name|thread
operator|.
name|getThreadName
argument_list|()
argument_list|,
name|thread
operator|.
name|getThreadState
argument_list|()
argument_list|,
name|threadsBean
operator|.
name|getThreadCpuTime
argument_list|(
name|id
argument_list|)
operator|/
literal|1000000
argument_list|,
name|threadsBean
operator|.
name|getThreadUserTime
argument_list|(
name|id
argument_list|)
operator|/
literal|1000000
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|!
name|noFormat
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ThreadGroup
name|group
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getThreadGroup
argument_list|()
decl_stmt|;
while|while
condition|(
name|group
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|group
operator|=
name|group
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
name|ThreadGroupData
name|data
init|=
operator|new
name|ThreadGroupData
argument_list|(
name|group
argument_list|,
name|threadInfos
argument_list|)
decl_stmt|;
name|data
operator|.
name|print
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
class|class
name|ThreadGroupData
block|{
specifier|private
specifier|final
name|ThreadGroup
name|group
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ThreadGroupData
argument_list|>
name|groups
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ThreadData
argument_list|>
name|threads
decl_stmt|;
specifier|public
name|ThreadGroupData
parameter_list|(
name|ThreadGroup
name|group
parameter_list|,
name|Map
argument_list|<
name|Long
argument_list|,
name|ThreadInfo
argument_list|>
name|infos
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
name|int
name|nbGroups
decl_stmt|;
name|int
name|nbThreads
decl_stmt|;
name|ThreadGroup
index|[]
name|childGroups
init|=
operator|new
name|ThreadGroup
index|[
literal|32
index|]
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|nbGroups
operator|=
name|group
operator|.
name|enumerate
argument_list|(
name|childGroups
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|nbGroups
operator|==
name|childGroups
operator|.
name|length
condition|)
block|{
name|childGroups
operator|=
operator|new
name|ThreadGroup
index|[
name|childGroups
operator|.
name|length
operator|*
literal|2
index|]
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|groups
operator|=
operator|new
name|ArrayList
argument_list|<
name|ThreadGroupData
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|ThreadGroup
name|tg
range|:
name|childGroups
control|)
block|{
if|if
condition|(
name|tg
operator|!=
literal|null
condition|)
block|{
name|groups
operator|.
name|add
argument_list|(
operator|new
name|ThreadGroupData
argument_list|(
name|tg
argument_list|,
name|infos
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Thread
index|[]
name|childThreads
init|=
operator|new
name|Thread
index|[
literal|32
index|]
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|nbThreads
operator|=
name|group
operator|.
name|enumerate
argument_list|(
name|childThreads
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|nbThreads
operator|==
name|childThreads
operator|.
name|length
condition|)
block|{
name|childThreads
operator|=
operator|new
name|Thread
index|[
name|childThreads
operator|.
name|length
operator|*
literal|2
index|]
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|threads
operator|=
operator|new
name|ArrayList
argument_list|<
name|ThreadData
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Thread
name|t
range|:
name|childThreads
control|)
block|{
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|threads
operator|.
name|add
argument_list|(
operator|new
name|ThreadData
argument_list|(
name|t
argument_list|,
name|infos
operator|.
name|get
argument_list|(
name|t
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|print
parameter_list|()
block|{
if|if
condition|(
name|tree
condition|)
block|{
name|printTree
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|printDump
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printTree
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
if|if
condition|(
name|empty
operator|||
name|hasInterestingThreads
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"Thread Group \""
operator|+
name|group
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
for|for
control|(
name|ThreadGroupData
name|tgd
range|:
name|groups
control|)
block|{
name|tgd
operator|.
name|printTree
argument_list|(
name|indent
operator|+
literal|"    "
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ThreadData
name|td
range|:
name|threads
control|)
block|{
if|if
condition|(
name|td
operator|.
name|isInteresting
argument_list|()
condition|)
block|{
name|td
operator|.
name|printTree
argument_list|(
name|indent
operator|+
literal|"    "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|printDump
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
if|if
condition|(
name|empty
operator|||
name|hasInterestingThreads
argument_list|()
condition|)
block|{
for|for
control|(
name|ThreadGroupData
name|tgd
range|:
name|groups
control|)
block|{
name|tgd
operator|.
name|printDump
argument_list|(
name|indent
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ThreadData
name|td
range|:
name|threads
control|)
block|{
if|if
condition|(
name|td
operator|.
name|isInteresting
argument_list|()
condition|)
block|{
name|td
operator|.
name|printDump
argument_list|(
name|indent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|boolean
name|hasInterestingThreads
parameter_list|()
block|{
for|for
control|(
name|ThreadData
name|td
range|:
name|threads
control|)
block|{
if|if
condition|(
name|td
operator|.
name|isInteresting
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|ThreadGroupData
name|tgd
range|:
name|groups
control|)
block|{
if|if
condition|(
name|tgd
operator|.
name|hasInterestingThreads
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|public
class|class
name|ThreadData
block|{
specifier|private
specifier|final
name|Thread
name|thread
decl_stmt|;
specifier|private
name|ThreadInfo
name|info
decl_stmt|;
specifier|public
name|ThreadData
parameter_list|(
name|Thread
name|thread
parameter_list|,
name|ThreadInfo
name|info
parameter_list|)
block|{
name|this
operator|.
name|thread
operator|=
name|thread
expr_stmt|;
name|this
operator|.
name|info
operator|=
name|info
expr_stmt|;
block|}
specifier|public
name|void
name|printTree
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"    "
operator|+
literal|"\""
operator|+
name|thread
operator|.
name|getName
argument_list|()
operator|+
literal|"\": "
operator|+
name|thread
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|printDump
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
if|if
condition|(
name|info
operator|!=
literal|null
operator|&&
name|isInteresting
argument_list|()
condition|)
block|{
name|printThreadInfo
argument_list|(
literal|"    "
argument_list|)
expr_stmt|;
if|if
condition|(
name|locks
condition|)
block|{
name|printLockInfo
argument_list|(
literal|"    "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|monitors
condition|)
block|{
name|printMonitorInfo
argument_list|(
literal|"    "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isInteresting
parameter_list|()
block|{
name|int
name|nb
init|=
literal|0
decl_stmt|;
name|StackTraceElement
index|[]
name|stacktrace
init|=
name|info
operator|.
name|getStackTrace
argument_list|()
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
name|stacktrace
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|StackTraceElement
name|ste
init|=
name|stacktrace
index|[
name|i
index|]
decl_stmt|;
name|boolean
name|interestingLine
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packages
control|)
block|{
if|if
condition|(
name|ste
operator|.
name|getClassName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|pkg
argument_list|)
condition|)
block|{
name|interestingLine
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|interestingLine
condition|)
block|{
name|nb
operator|++
expr_stmt|;
block|}
block|}
return|return
name|nb
operator|>=
name|threshold
return|;
block|}
specifier|private
name|void
name|printThreadInfo
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
comment|// print thread information
name|printThread
argument_list|(
name|indent
argument_list|)
expr_stmt|;
comment|// print stack trace with locks
name|StackTraceElement
index|[]
name|stacktrace
init|=
name|info
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
name|MonitorInfo
index|[]
name|monitors
init|=
name|info
operator|.
name|getLockedMonitors
argument_list|()
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
name|stacktrace
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|StackTraceElement
name|ste
init|=
name|stacktrace
index|[
name|i
index|]
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"at "
operator|+
name|ste
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|MonitorInfo
name|mi
range|:
name|monitors
control|)
block|{
if|if
condition|(
name|mi
operator|.
name|getLockedStackDepth
argument_list|()
operator|==
name|i
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"  - locked "
operator|+
name|mi
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|printThread
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"\""
operator|+
name|info
operator|.
name|getThreadName
argument_list|()
operator|+
literal|"\""
operator|+
literal|" Id="
operator|+
name|info
operator|.
name|getThreadId
argument_list|()
operator|+
literal|" in "
operator|+
name|info
operator|.
name|getThreadState
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getLockName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" on lock="
operator|+
name|info
operator|.
name|getLockName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" (suspended)"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|isInNative
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" (running in native)"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getLockOwnerName
argument_list|()
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
name|indent
operator|+
literal|" owned by "
operator|+
name|info
operator|.
name|getLockOwnerName
argument_list|()
operator|+
literal|" Id="
operator|+
name|info
operator|.
name|getLockOwnerId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printMonitorInfo
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
name|MonitorInfo
index|[]
name|monitors
init|=
name|info
operator|.
name|getLockedMonitors
argument_list|()
decl_stmt|;
if|if
condition|(
name|monitors
operator|!=
literal|null
operator|&&
name|monitors
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"Locked monitors: count = "
operator|+
name|monitors
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|MonitorInfo
name|mi
range|:
name|monitors
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"  - "
operator|+
name|mi
operator|+
literal|" locked at "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"      "
operator|+
name|mi
operator|.
name|getLockedStackDepth
argument_list|()
operator|+
literal|" "
operator|+
name|mi
operator|.
name|getLockedStackFrame
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printLockInfo
parameter_list|(
name|String
name|indent
parameter_list|)
block|{
name|LockInfo
index|[]
name|locks
init|=
name|info
operator|.
name|getLockedSynchronizers
argument_list|()
decl_stmt|;
if|if
condition|(
name|locks
operator|!=
literal|null
operator|&&
name|locks
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"Locked synchronizers: count = "
operator|+
name|locks
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|LockInfo
name|li
range|:
name|locks
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|indent
operator|+
literal|"  - "
operator|+
name|li
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

