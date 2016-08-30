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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|PrintStream
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|impl
operator|.
name|action
operator|.
name|command
operator|.
name|DefaultActionPreparator
import|;
end_import

begin_class
specifier|public
class|class
name|GrepTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGrep
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|input
init|=
name|System
operator|.
name|in
decl_stmt|;
try|try
block|{
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"1\n2\n3\n4\n5\n6\n7\n8\n9\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|setIn
argument_list|(
name|bais
argument_list|)
expr_stmt|;
name|GrepAction
name|grep
init|=
operator|new
name|GrepAction
argument_list|()
decl_stmt|;
name|DefaultActionPreparator
name|preparator
init|=
operator|new
name|DefaultActionPreparator
argument_list|()
decl_stmt|;
name|preparator
operator|.
name|prepare
argument_list|(
name|grep
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
expr|<
name|Object
operator|>
name|asList
argument_list|(
literal|"-C"
argument_list|,
literal|"100"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|grep
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setIn
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testHonorColorNever
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|input
init|=
name|System
operator|.
name|in
decl_stmt|;
name|PrintStream
name|output
init|=
name|System
operator|.
name|out
decl_stmt|;
try|try
block|{
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"abc"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|setIn
argument_list|(
name|bais
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|outContent
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|System
operator|.
name|setOut
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|outContent
argument_list|)
argument_list|)
expr_stmt|;
name|GrepAction
name|grep
init|=
operator|new
name|GrepAction
argument_list|()
decl_stmt|;
name|DefaultActionPreparator
name|preparator
init|=
operator|new
name|DefaultActionPreparator
argument_list|()
decl_stmt|;
name|preparator
operator|.
name|prepare
argument_list|(
name|grep
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
expr|<
name|Object
operator|>
name|asList
argument_list|(
literal|"--color"
argument_list|,
literal|"never"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|grep
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|outContent
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setIn
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

