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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedOutputStream
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Mar 18, 2008  * Time: 4:35:05 PM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
specifier|public
class|class
name|NoCloseInputStreamTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testStream
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|InputStream
name|in
init|=
operator|new
name|NoCloseInputStream
argument_list|(
name|System
operator|.
name|in
argument_list|)
decl_stmt|;
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|int
name|c
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Reading from in..."
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Read from in: "
operator|+
name|c
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Exiting thread..."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

