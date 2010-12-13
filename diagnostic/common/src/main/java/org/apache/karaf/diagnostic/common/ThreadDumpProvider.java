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
name|common
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

begin_comment
comment|/**  * Provider which dumps thread info to file named threads.txt.  *   * @author ldywicki  */
end_comment

begin_class
specifier|public
class|class
name|ThreadDumpProvider
extends|extends
name|TextDumpProvider
block|{
comment|/**      * Creates new dump entry which contains information about threads.      */
specifier|public
name|ThreadDumpProvider
parameter_list|()
block|{
name|super
argument_list|(
literal|"threads.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeDump
parameter_list|(
name|OutputStreamWriter
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|ThreadMXBean
name|threadMXBean
init|=
name|ManagementFactory
operator|.
name|getThreadMXBean
argument_list|()
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
literal|"Number of threads: "
operator|+
name|threadMXBean
operator|.
name|getThreadCount
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|ThreadInfo
name|threadInfo
range|:
name|threadMXBean
operator|.
name|getThreadInfo
argument_list|(
name|threadMXBean
operator|.
name|getAllThreadIds
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
control|)
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|threadInfo
operator|.
name|toString
argument_list|()
operator|+
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

