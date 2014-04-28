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
name|diagnostic
operator|.
name|core
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|management
operator|.
name|HotSpotDiagnosticMXBean
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
name|DumpDestination
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
name|DumpProvider
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
name|util
operator|.
name|StreamUtils
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|OutputStream
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

begin_comment
comment|/**  * Create a heap dump.  */
end_comment

begin_class
specifier|public
class|class
name|HeapDumpProvider
implements|implements
name|DumpProvider
block|{
annotation|@
name|Override
specifier|public
name|void
name|createDump
parameter_list|(
name|DumpDestination
name|destination
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|MBeanServer
name|mBeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|HotSpotDiagnosticMXBean
name|diagnosticMXBean
init|=
name|ManagementFactory
operator|.
name|newPlatformMXBeanProxy
argument_list|(
name|mBeanServer
argument_list|,
literal|"com.sun.management:type=HotSpotDiagnostic"
argument_list|,
name|HotSpotDiagnosticMXBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|diagnosticMXBean
operator|.
name|dumpHeap
argument_list|(
literal|"heapdump.txt"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// copy the dump in the destination
name|File
name|heapDumpFile
init|=
operator|new
name|File
argument_list|(
literal|"heapdump.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|heapDumpFile
argument_list|)
init|;
name|OutputStream
name|out
operator|=
name|destination
operator|.
name|add
argument_list|(
literal|"heapdump.txt"
argument_list|)
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
comment|// remove the original dump
if|if
condition|(
name|heapDumpFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|heapDumpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// nothing to do
block|}
block|}
block|}
end_class

end_unit

