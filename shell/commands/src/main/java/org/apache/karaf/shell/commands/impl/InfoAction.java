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
name|ClassLoadingMXBean
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
name|GarbageCollectorMXBean
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
name|MemoryMXBean
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
name|OperatingSystemMXBean
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
name|RuntimeMXBean
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
name|text
operator|.
name|DecimalFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormatSymbols
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|NumberFormat
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
name|java
operator|.
name|util
operator|.
name|Locale
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
name|Properties
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
name|InfoProvider
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
name|OsgiCommandSupport
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
literal|"info"
argument_list|,
name|description
operator|=
literal|"Prints system information."
argument_list|)
specifier|public
class|class
name|InfoAction
extends|extends
name|OsgiCommandSupport
block|{
specifier|private
name|NumberFormat
name|fmtI
init|=
operator|new
name|DecimalFormat
argument_list|(
literal|"###,###"
argument_list|,
operator|new
name|DecimalFormatSymbols
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|NumberFormat
name|fmtD
init|=
operator|new
name|DecimalFormat
argument_list|(
literal|"###,##0.000"
argument_list|,
operator|new
name|DecimalFormatSymbols
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|InfoProvider
argument_list|>
name|infoProviders
init|=
operator|new
name|LinkedList
argument_list|<
name|InfoProvider
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|maxNameLen
decl_stmt|;
name|RuntimeMXBean
name|runtime
init|=
name|ManagementFactory
operator|.
name|getRuntimeMXBean
argument_list|()
decl_stmt|;
name|OperatingSystemMXBean
name|os
init|=
name|ManagementFactory
operator|.
name|getOperatingSystemMXBean
argument_list|()
decl_stmt|;
name|ThreadMXBean
name|threads
init|=
name|ManagementFactory
operator|.
name|getThreadMXBean
argument_list|()
decl_stmt|;
name|MemoryMXBean
name|mem
init|=
name|ManagementFactory
operator|.
name|getMemoryMXBean
argument_list|()
decl_stmt|;
name|ClassLoadingMXBean
name|cl
init|=
name|ManagementFactory
operator|.
name|getClassLoadingMXBean
argument_list|()
decl_stmt|;
comment|//
comment|// print Karaf informations
comment|//
name|maxNameLen
operator|=
literal|25
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Karaf"
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Karaf version"
argument_list|,
name|maxNameLen
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Karaf home"
argument_list|,
name|maxNameLen
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Karaf base"
argument_list|,
name|maxNameLen
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"OSGi Framework"
argument_list|,
name|maxNameLen
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" - "
operator|+
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JVM"
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Java Virtual Machine"
argument_list|,
name|maxNameLen
argument_list|,
name|runtime
operator|.
name|getVmName
argument_list|()
operator|+
literal|" version "
operator|+
name|runtime
operator|.
name|getVmVersion
argument_list|()
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Version"
argument_list|,
name|maxNameLen
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Vendor"
argument_list|,
name|maxNameLen
argument_list|,
name|runtime
operator|.
name|getVmVendor
argument_list|()
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Uptime"
argument_list|,
name|maxNameLen
argument_list|,
name|printDuration
argument_list|(
name|runtime
operator|.
name|getUptime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|printValue
argument_list|(
literal|"Process CPU time"
argument_list|,
name|maxNameLen
argument_list|,
name|printDuration
argument_list|(
name|getSunOsValueAsLong
argument_list|(
name|os
argument_list|,
literal|"getProcessCpuTime"
argument_list|)
operator|/
literal|1000000
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{         }
name|printValue
argument_list|(
literal|"Total compile time"
argument_list|,
name|maxNameLen
argument_list|,
name|printDuration
argument_list|(
name|ManagementFactory
operator|.
name|getCompilationMXBean
argument_list|()
operator|.
name|getTotalCompilationTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Threads"
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Live threads"
argument_list|,
name|maxNameLen
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|threads
operator|.
name|getThreadCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Daemon threads"
argument_list|,
name|maxNameLen
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|threads
operator|.
name|getDaemonThreadCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Peak"
argument_list|,
name|maxNameLen
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|threads
operator|.
name|getPeakThreadCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Total started"
argument_list|,
name|maxNameLen
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|threads
operator|.
name|getTotalStartedThreadCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Memory"
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Current heap size"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|mem
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getUsed
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Maximum heap size"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|mem
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getMax
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Committed heap size"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|mem
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getCommitted
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Pending objects"
argument_list|,
name|maxNameLen
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|mem
operator|.
name|getObjectPendingFinalizationCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|GarbageCollectorMXBean
name|gc
range|:
name|ManagementFactory
operator|.
name|getGarbageCollectorMXBeans
argument_list|()
control|)
block|{
name|String
name|val
init|=
literal|"Name = '"
operator|+
name|gc
operator|.
name|getName
argument_list|()
operator|+
literal|"', Collections = "
operator|+
name|gc
operator|.
name|getCollectionCount
argument_list|()
operator|+
literal|", Time = "
operator|+
name|printDuration
argument_list|(
name|gc
operator|.
name|getCollectionTime
argument_list|()
argument_list|)
decl_stmt|;
name|printValue
argument_list|(
literal|"Garbage collector"
argument_list|,
name|maxNameLen
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Classes"
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Current classes loaded"
argument_list|,
name|maxNameLen
argument_list|,
name|printLong
argument_list|(
name|cl
operator|.
name|getLoadedClassCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Total classes loaded"
argument_list|,
name|maxNameLen
argument_list|,
name|printLong
argument_list|(
name|cl
operator|.
name|getTotalLoadedClassCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Total classes unloaded"
argument_list|,
name|maxNameLen
argument_list|,
name|printLong
argument_list|(
name|cl
operator|.
name|getUnloadedClassCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Operating system"
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Name"
argument_list|,
name|maxNameLen
argument_list|,
name|os
operator|.
name|getName
argument_list|()
operator|+
literal|" version "
operator|+
name|os
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Architecture"
argument_list|,
name|maxNameLen
argument_list|,
name|os
operator|.
name|getArch
argument_list|()
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Processors"
argument_list|,
name|maxNameLen
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|os
operator|.
name|getAvailableProcessors
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|printValue
argument_list|(
literal|"Total physical memory"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|getSunOsValueAsLong
argument_list|(
name|os
argument_list|,
literal|"getTotalPhysicalMemorySize"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Free physical memory"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|getSunOsValueAsLong
argument_list|(
name|os
argument_list|,
literal|"getFreePhysicalMemorySize"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Committed virtual memory"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|getSunOsValueAsLong
argument_list|(
name|os
argument_list|,
literal|"getCommittedVirtualMemorySize"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Total swap space"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|getSunOsValueAsLong
argument_list|(
name|os
argument_list|,
literal|"getTotalSwapSpaceSize"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|printValue
argument_list|(
literal|"Free swap space"
argument_list|,
name|maxNameLen
argument_list|,
name|printSizeInKb
argument_list|(
name|getSunOsValueAsLong
argument_list|(
name|os
argument_list|,
literal|"getFreeSwapSpaceSize"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{         }
comment|//Display Information from external information providers.
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|infoProviders
operator|!=
literal|null
condition|)
block|{
comment|// dump all properties to Map, KARAF-425
for|for
control|(
name|InfoProvider
name|provider
range|:
name|infoProviders
control|)
block|{
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
name|provider
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|provider
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|get
argument_list|(
name|provider
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|putAll
argument_list|(
name|provider
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|section
range|:
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|section
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|properties
operator|.
name|get
argument_list|(
name|section
argument_list|)
operator|.
name|keySet
argument_list|()
control|)
block|{
name|printValue
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|key
argument_list|)
argument_list|,
name|maxNameLen
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|section
argument_list|)
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|long
name|getSunOsValueAsLong
parameter_list|(
name|OperatingSystemMXBean
name|os
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Method
name|mth
init|=
name|os
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|(
name|Long
operator|)
name|mth
operator|.
name|invoke
argument_list|(
name|os
argument_list|)
return|;
block|}
specifier|private
name|String
name|printLong
parameter_list|(
name|long
name|i
parameter_list|)
block|{
return|return
name|fmtI
operator|.
name|format
argument_list|(
name|i
argument_list|)
return|;
block|}
specifier|private
name|String
name|printSizeInKb
parameter_list|(
name|double
name|size
parameter_list|)
block|{
return|return
name|fmtI
operator|.
name|format
argument_list|(
call|(
name|long
call|)
argument_list|(
name|size
operator|/
literal|1024
argument_list|)
argument_list|)
operator|+
literal|" kbytes"
return|;
block|}
specifier|protected
name|String
name|printDuration
parameter_list|(
name|double
name|uptime
parameter_list|)
block|{
name|uptime
operator|/=
literal|1000
expr_stmt|;
if|if
condition|(
name|uptime
operator|<
literal|60
condition|)
block|{
return|return
name|fmtD
operator|.
name|format
argument_list|(
name|uptime
argument_list|)
operator|+
literal|" seconds"
return|;
block|}
name|uptime
operator|/=
literal|60
expr_stmt|;
if|if
condition|(
name|uptime
operator|<
literal|60
condition|)
block|{
name|long
name|minutes
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
name|String
name|s
init|=
name|fmtI
operator|.
name|format
argument_list|(
name|minutes
argument_list|)
operator|+
operator|(
name|minutes
operator|>
literal|1
condition|?
literal|" minutes"
else|:
literal|" minute"
operator|)
decl_stmt|;
return|return
name|s
return|;
block|}
name|uptime
operator|/=
literal|60
expr_stmt|;
if|if
condition|(
name|uptime
operator|<
literal|24
condition|)
block|{
name|long
name|hours
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
name|long
name|minutes
init|=
call|(
name|long
call|)
argument_list|(
operator|(
name|uptime
operator|-
name|hours
operator|)
operator|*
literal|60
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|fmtI
operator|.
name|format
argument_list|(
name|hours
argument_list|)
operator|+
operator|(
name|hours
operator|>
literal|1
condition|?
literal|" hours"
else|:
literal|" hour"
operator|)
decl_stmt|;
if|if
condition|(
name|minutes
operator|!=
literal|0
condition|)
block|{
name|s
operator|+=
literal|" "
operator|+
name|fmtI
operator|.
name|format
argument_list|(
name|minutes
argument_list|)
operator|+
operator|(
name|minutes
operator|>
literal|1
condition|?
literal|" minutes"
else|:
literal|" minute"
operator|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
name|uptime
operator|/=
literal|24
expr_stmt|;
name|long
name|days
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
name|long
name|hours
init|=
call|(
name|long
call|)
argument_list|(
operator|(
name|uptime
operator|-
name|days
operator|)
operator|*
literal|24
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|fmtI
operator|.
name|format
argument_list|(
name|days
argument_list|)
operator|+
operator|(
name|days
operator|>
literal|1
condition|?
literal|" days"
else|:
literal|" day"
operator|)
decl_stmt|;
if|if
condition|(
name|hours
operator|!=
literal|0
condition|)
block|{
name|s
operator|+=
literal|" "
operator|+
name|fmtI
operator|.
name|format
argument_list|(
name|hours
argument_list|)
operator|+
operator|(
name|hours
operator|>
literal|1
condition|?
literal|" hours"
else|:
literal|" hour"
operator|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
name|void
name|printSysValue
parameter_list|(
name|String
name|prop
parameter_list|,
name|int
name|pad
parameter_list|)
block|{
name|printValue
argument_list|(
name|prop
argument_list|,
name|pad
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|prop
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|printValue
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|pad
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|System
operator|.
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
literal|"  "
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
name|name
argument_list|)
operator|.
name|a
argument_list|(
name|spaces
argument_list|(
name|pad
operator|-
name|name
operator|.
name|length
argument_list|()
argument_list|)
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
operator|.
name|a
argument_list|(
literal|"   "
argument_list|)
operator|.
name|a
argument_list|(
name|value
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|spaces
parameter_list|(
name|int
name|nb
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
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
name|nb
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|InfoProvider
argument_list|>
name|getInfoProviders
parameter_list|()
block|{
return|return
name|infoProviders
return|;
block|}
specifier|public
name|void
name|setInfoProviders
parameter_list|(
name|List
argument_list|<
name|InfoProvider
argument_list|>
name|infoProviders
parameter_list|)
block|{
name|this
operator|.
name|infoProviders
operator|=
name|infoProviders
expr_stmt|;
block|}
block|}
end_class

end_unit

