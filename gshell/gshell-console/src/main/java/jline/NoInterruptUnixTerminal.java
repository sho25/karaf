begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|jline
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
specifier|public
class|class
name|NoInterruptUnixTerminal
extends|extends
name|UnixTerminal
block|{
annotation|@
name|Override
specifier|public
name|void
name|initializeTerminal
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
name|super
operator|.
name|initializeTerminal
argument_list|()
expr_stmt|;
name|stty
argument_list|(
literal|"intr undef"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|restoreTerminal
parameter_list|()
throws|throws
name|Exception
block|{
name|stty
argument_list|(
literal|"intr ^C"
argument_list|)
expr_stmt|;
name|super
operator|.
name|restoreTerminal
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

