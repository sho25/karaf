begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|core
operator|.
name|providers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|CompilationMXBean
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
name|MemoryUsage
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
name|text
operator|.
name|DateFormat
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
name|Date
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
name|Map
operator|.
name|Entry
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
name|diagnostic
operator|.
name|core
operator|.
name|common
operator|.
name|TextDumpProvider
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
name|Bundle
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

begin_comment
comment|/**  * Provider which dumps runtime environment information to file named environment.txt.  */
end_comment

begin_class
specifier|public
class|class
name|EnvironmentDumpProvider
extends|extends
name|TextDumpProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KEY_VALUE_FORMAT
init|=
literal|"%1$s\t: %2$s"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INDENT_KEY_VALUE_FORMAT
init|=
literal|"    "
operator|+
name|KEY_VALUE_FORMAT
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
comment|/**      * Create new dump entry which contains information about the runtime environment.      *      * @param context The bundle context to use in the MBean.      */
specifier|public
name|EnvironmentDumpProvider
parameter_list|(
specifier|final
name|BundleContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
literal|"environment.txt"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeDump
parameter_list|(
specifier|final
name|OutputStreamWriter
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
literal|null
operator|==
name|outputStream
condition|)
block|{
return|return;
block|}
specifier|final
name|PrintWriter
name|outPW
init|=
operator|new
name|PrintWriter
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
comment|// current date/time
specifier|final
name|DateFormat
name|dateTimeFormatInstance
init|=
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|(
name|DateFormat
operator|.
name|FULL
argument_list|,
name|DateFormat
operator|.
name|FULL
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Dump timestamp"
argument_list|,
name|dateTimeFormatInstance
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// karaf information
name|dumpKarafInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// OSGi information
name|dumpOSGiInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// OS information
name|dumpOSInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// general information about JVM
name|dumpVMInformation
argument_list|(
name|outPW
argument_list|,
name|dateTimeFormatInstance
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// threads
name|dumpThreadsInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// classes
name|dumpClassesInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// memory
name|dumpMemoryInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// garbage collector
name|dumpGCInformation
argument_list|(
name|outPW
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|dumpKarafInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Karaf"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
operator|+
literal|' '
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"home"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"base"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|dumpOSGiInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|bundleContext
condition|)
block|{
return|return;
block|}
name|outPW
operator|.
name|println
argument_list|(
literal|"OSGi:"
argument_list|)
expr_stmt|;
specifier|final
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
if|if
condition|(
literal|null
operator|==
name|bundle
operator|||
operator|!
operator|!
operator|!
literal|"osgi.core"
operator|.
name|equals
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"version"
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
break|break;
block|}
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"framework"
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
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|dumpOSInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
specifier|final
name|OperatingSystemMXBean
name|mxBean
init|=
name|ManagementFactory
operator|.
name|getOperatingSystemMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mxBean
condition|)
block|{
return|return;
block|}
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Operating System"
argument_list|,
name|mxBean
operator|.
name|getName
argument_list|()
operator|+
literal|' '
operator|+
name|mxBean
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"architecture"
argument_list|,
name|mxBean
operator|.
name|getArch
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"processors"
argument_list|,
name|mxBean
operator|.
name|getAvailableProcessors
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
comment|//        outPW.printf(INDENT_KEY_VALUE_FORMAT, "current system load average", mxBean.getSystemLoadAverage()).println();
block|}
specifier|private
name|void
name|dumpVMInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|,
specifier|final
name|DateFormat
name|dateTimeFormatInstance
parameter_list|)
block|{
specifier|final
name|RuntimeMXBean
name|mxBean
init|=
name|ManagementFactory
operator|.
name|getRuntimeMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|mxBean
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Instance name"
argument_list|,
name|mxBean
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Start time"
argument_list|,
name|dateTimeFormatInstance
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|mxBean
operator|.
name|getStartTime
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Uptime"
argument_list|,
name|printDuration
argument_list|(
name|mxBean
operator|.
name|getUptime
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Java VM"
argument_list|,
name|mxBean
operator|.
name|getVmName
argument_list|()
operator|+
literal|" "
operator|+
name|mxBean
operator|.
name|getVmVersion
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"vendor"
argument_list|,
name|mxBean
operator|.
name|getVmVendor
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"version"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|(
literal|"Input arguments:"
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|inputArguments
init|=
name|mxBean
operator|.
name|getInputArguments
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|argument
range|:
name|inputArguments
control|)
block|{
if|if
condition|(
name|argument
operator|!=
literal|null
operator|&&
name|argument
operator|.
name|contains
argument_list|(
literal|"="
argument_list|)
condition|)
block|{
specifier|final
name|String
index|[]
name|split
init|=
name|argument
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
name|split
index|[
literal|0
index|]
argument_list|,
name|split
index|[
literal|1
index|]
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
name|argument
argument_list|,
literal|""
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
name|outPW
operator|.
name|println
argument_list|(
literal|"Classpath:"
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"boot classpath"
argument_list|,
name|mxBean
operator|.
name|getBootClassPath
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"library path"
argument_list|,
name|mxBean
operator|.
name|getLibraryPath
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"classpath"
argument_list|,
name|mxBean
operator|.
name|getClassPath
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|println
argument_list|(
literal|"System properties:"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|systemProperties
init|=
name|mxBean
operator|.
name|getSystemProperties
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|property
range|:
name|systemProperties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
name|property
operator|.
name|getKey
argument_list|()
argument_list|,
name|property
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// JIT information
specifier|final
name|CompilationMXBean
name|compilationMXBean
init|=
name|ManagementFactory
operator|.
name|getCompilationMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|compilationMXBean
operator|!=
literal|null
condition|)
block|{
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"JIT compiler"
argument_list|,
name|compilationMXBean
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"total compile time"
argument_list|,
name|printDuration
argument_list|(
name|compilationMXBean
operator|.
name|getTotalCompilationTime
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dumpThreadsInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
specifier|final
name|ThreadMXBean
name|mxBean
init|=
name|ManagementFactory
operator|.
name|getThreadMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mxBean
condition|)
block|{
return|return;
block|}
name|outPW
operator|.
name|println
argument_list|(
literal|"Threads:"
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"live"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getThreadCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"daemon"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getDaemonThreadCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"peak"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getPeakThreadCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"total"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getTotalStartedThreadCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|dumpClassesInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
specifier|final
name|ClassLoadingMXBean
name|mxBean
init|=
name|ManagementFactory
operator|.
name|getClassLoadingMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mxBean
condition|)
block|{
return|return;
block|}
name|outPW
operator|.
name|println
argument_list|(
literal|"Classes:"
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"loaded"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getLoadedClassCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"total"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getTotalLoadedClassCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"unloaded"
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getUnloadedClassCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|dumpMemoryInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
specifier|final
name|MemoryMXBean
name|mxBean
init|=
name|ManagementFactory
operator|.
name|getMemoryMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mxBean
condition|)
block|{
return|return;
block|}
specifier|final
name|MemoryUsage
name|heapMemoryUsage
init|=
name|mxBean
operator|.
name|getHeapMemoryUsage
argument_list|()
decl_stmt|;
specifier|final
name|MemoryUsage
name|nonHeapMemoryUsage
init|=
name|mxBean
operator|.
name|getNonHeapMemoryUsage
argument_list|()
decl_stmt|;
if|if
condition|(
name|heapMemoryUsage
operator|!=
literal|null
condition|)
block|{
name|outPW
operator|.
name|println
argument_list|(
literal|"HEAP Memory:"
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"commited"
argument_list|,
name|printMemory
argument_list|(
name|heapMemoryUsage
operator|.
name|getCommitted
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"init"
argument_list|,
name|printMemory
argument_list|(
name|heapMemoryUsage
operator|.
name|getInit
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"used"
argument_list|,
name|printMemory
argument_list|(
name|heapMemoryUsage
operator|.
name|getUsed
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"maximal"
argument_list|,
name|printMemory
argument_list|(
name|heapMemoryUsage
operator|.
name|getMax
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|nonHeapMemoryUsage
operator|!=
literal|null
condition|)
block|{
name|outPW
operator|.
name|println
argument_list|(
literal|"NON-HEAP Memory:"
argument_list|)
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"commited"
argument_list|,
name|printMemory
argument_list|(
name|nonHeapMemoryUsage
operator|.
name|getCommitted
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"init"
argument_list|,
name|printMemory
argument_list|(
name|nonHeapMemoryUsage
operator|.
name|getInit
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"used"
argument_list|,
name|printMemory
argument_list|(
name|nonHeapMemoryUsage
operator|.
name|getUsed
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"maximal"
argument_list|,
name|printMemory
argument_list|(
name|nonHeapMemoryUsage
operator|.
name|getMax
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dumpGCInformation
parameter_list|(
specifier|final
name|PrintWriter
name|outPW
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|GarbageCollectorMXBean
argument_list|>
name|mxBeans
init|=
name|ManagementFactory
operator|.
name|getGarbageCollectorMXBeans
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mxBeans
operator|||
name|mxBeans
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|MemoryMXBean
name|memoryMxBean
init|=
name|ManagementFactory
operator|.
name|getMemoryMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|memoryMxBean
operator|!=
literal|null
condition|)
block|{
name|outPW
operator|.
name|printf
argument_list|(
name|INDENT_KEY_VALUE_FORMAT
argument_list|,
literal|"pending objects"
argument_list|,
name|formatLong
argument_list|(
name|memoryMxBean
operator|.
name|getObjectPendingFinalizationCount
argument_list|()
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|final
name|String
name|gcFormat
init|=
literal|"'%1$s' collections: %2$s\ttime: %3$s"
decl_stmt|;
name|outPW
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|GarbageCollectorMXBean
name|mxBean
range|:
name|mxBeans
control|)
block|{
if|if
condition|(
literal|null
operator|==
name|mxBean
condition|)
block|{
continue|continue;
block|}
name|outPW
operator|.
name|printf
argument_list|(
name|KEY_VALUE_FORMAT
argument_list|,
literal|"Garbage Collectors"
argument_list|,
name|String
operator|.
name|format
argument_list|(
name|gcFormat
argument_list|,
name|mxBean
operator|.
name|getName
argument_list|()
argument_list|,
name|formatLong
argument_list|(
name|mxBean
operator|.
name|getCollectionCount
argument_list|()
argument_list|)
argument_list|,
name|printDuration
argument_list|(
name|mxBean
operator|.
name|getCollectionTime
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|formatLong
parameter_list|(
specifier|final
name|long
name|longValue
parameter_list|)
block|{
specifier|final
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
return|return
name|fmtI
operator|.
name|format
argument_list|(
name|longValue
argument_list|)
return|;
block|}
specifier|private
name|String
name|printMemory
parameter_list|(
specifier|final
name|long
name|bytes
parameter_list|)
block|{
if|if
condition|(
name|bytes
operator|<=
literal|1024
condition|)
block|{
return|return
name|formatLong
argument_list|(
name|bytes
argument_list|)
operator|+
literal|" bytes"
return|;
block|}
return|return
name|formatLong
argument_list|(
name|bytes
operator|/
literal|1024
argument_list|)
operator|+
literal|" kbytes"
return|;
block|}
comment|/**      * Print the duration in a human readable format as X days Y hours Z minutes etc.      *      * @param uptime The uptime in millis.      * @return The time used for displaying on screen or in logs.      */
specifier|private
name|String
name|printDuration
parameter_list|(
name|double
name|uptime
parameter_list|)
block|{
comment|// Code based on code taken from Karaf
comment|// https://svn.apache.org/repos/asf/karaf/trunk/shell/commands/src/main/java/org/apache/karaf/shell/commands/impl/InfoAction.java
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
specifier|final
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
specifier|final
name|long
name|minutes
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
specifier|final
name|String
name|s
init|=
name|formatLong
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
specifier|final
name|long
name|hours
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
specifier|final
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
name|formatLong
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
name|formatLong
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
specifier|final
name|long
name|days
init|=
operator|(
name|long
operator|)
name|uptime
decl_stmt|;
specifier|final
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
name|formatLong
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
name|formatLong
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
block|}
end_class

end_unit

