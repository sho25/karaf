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
name|providers
package|;
end_package

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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|File
name|heapDumpFile
init|=
literal|null
decl_stmt|;
name|FileInputStream
name|in
init|=
literal|null
decl_stmt|;
name|OutputStream
name|out
init|=
literal|null
decl_stmt|;
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
name|Class
argument_list|<
name|?
argument_list|>
name|diagnosticMXBeanClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.sun.management.HotSpotDiagnosticMXBean"
argument_list|)
decl_stmt|;
name|Object
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
name|diagnosticMXBeanClass
argument_list|)
decl_stmt|;
name|heapDumpFile
operator|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|"heapdump"
argument_list|,
literal|".hprof"
argument_list|)
operator|.
name|toFile
argument_list|()
expr_stmt|;
name|heapDumpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Method
name|method
init|=
name|diagnosticMXBeanClass
operator|.
name|getMethod
argument_list|(
literal|"dumpHeap"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|diagnosticMXBean
argument_list|,
name|heapDumpFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// copy the dump in the destination
name|in
operator|=
operator|new
name|FileInputStream
argument_list|(
name|heapDumpFile
argument_list|)
expr_stmt|;
name|out
operator|=
name|destination
operator|.
name|add
argument_list|(
literal|"heapdump.hprof"
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|l
decl_stmt|;
while|while
condition|(
operator|(
operator|(
name|l
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
operator|)
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// remove the original dump
if|if
condition|(
name|heapDumpFile
operator|!=
literal|null
operator|&&
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
block|}
block|}
end_class

end_unit

