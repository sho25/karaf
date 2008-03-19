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
name|geronimo
operator|.
name|gshell
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterOutputStream
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Simple OutputStream, that do not close the underlying  * stream when close() is called.  */
end_comment

begin_class
specifier|public
class|class
name|NoCloseOutputStream
extends|extends
name|FilterOutputStream
block|{
specifier|public
name|NoCloseOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|super
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignored
parameter_list|)
block|{         }
finally|finally
block|{
name|out
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

