begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|console
operator|.
name|completer
package|;
end_package

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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|ArgumentCompleterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testParser1
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
literal|"echo foo | cat bar ; ta"
argument_list|,
literal|23
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|List
argument_list|<
name|CharSequence
argument_list|>
argument_list|>
argument_list|>
name|p
init|=
name|parser
operator|.
name|program
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|parser
operator|.
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|parser
operator|.
name|c2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|parser
operator|.
name|c3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParser2
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
literal|"echo foo ; cat bar | ta"
argument_list|,
literal|23
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|List
argument_list|<
name|CharSequence
argument_list|>
argument_list|>
argument_list|>
name|p
init|=
name|parser
operator|.
name|program
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|parser
operator|.
name|c2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|parser
operator|.
name|c3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParser3
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
literal|"echo foo ; cat bar | ta"
argument_list|,
literal|22
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|List
argument_list|<
name|CharSequence
argument_list|>
argument_list|>
argument_list|>
name|p
init|=
name|parser
operator|.
name|program
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|parser
operator|.
name|c2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParser4
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
literal|"echo foo ; cat bar | ta reta"
argument_list|,
literal|27
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|List
argument_list|<
name|CharSequence
argument_list|>
argument_list|>
argument_list|>
name|p
init|=
name|parser
operator|.
name|program
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|parser
operator|.
name|c3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParser5
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
literal|"echo foo ; cat bar | ta reta"
argument_list|,
literal|24
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|List
argument_list|<
name|List
argument_list|<
name|CharSequence
argument_list|>
argument_list|>
argument_list|>
name|p
init|=
name|parser
operator|.
name|program
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parser
operator|.
name|c2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|parser
operator|.
name|c3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

