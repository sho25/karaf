begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|support
operator|.
name|ansi
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
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|AnsiSplitterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWindow
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|text
init|=
literal|"\u001B[1mThis is bold.\u001B[22m"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"\u001B[1mis\u001B[0m"
argument_list|,
name|AnsiSplitter
operator|.
name|substring
argument_list|(
name|text
argument_list|,
literal|5
argument_list|,
literal|7
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|13
argument_list|,
name|AnsiSplitter
operator|.
name|length
argument_list|(
name|text
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

