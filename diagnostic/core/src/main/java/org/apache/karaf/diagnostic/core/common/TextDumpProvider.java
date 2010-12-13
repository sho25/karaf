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
name|common
package|;
end_package

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
name|io
operator|.
name|OutputStreamWriter
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

begin_class
specifier|public
specifier|abstract
class|class
name|TextDumpProvider
implements|implements
name|DumpProvider
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|protected
name|TextDumpProvider
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
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
name|OutputStream
name|outputStream
init|=
name|destination
operator|.
name|add
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|writeDump
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|)
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|writeDump
parameter_list|(
name|OutputStreamWriter
name|outputStream
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

