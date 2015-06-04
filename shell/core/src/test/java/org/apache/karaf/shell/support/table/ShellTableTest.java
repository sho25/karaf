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
name|table
package|;
end_package

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
name|PrintStream
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
name|ShellTableTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLongValueFull
parameter_list|()
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|separator
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"col1"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"col2"
argument_list|)
operator|.
name|maxSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
literal|"my first column value"
argument_list|,
literal|"my second column value is quite long"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|table
operator|.
name|print
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s%n"
argument_list|,
literal|"my first column value|my second column value is quite long"
argument_list|)
argument_list|,
name|baos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLongValueCut
parameter_list|()
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|separator
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"col1"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"col2"
argument_list|)
operator|.
name|maxSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
literal|"my first column value"
argument_list|,
literal|"my second column value is quite long"
argument_list|)
expr_stmt|;
name|table
operator|.
name|size
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|table
operator|.
name|print
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s%n"
argument_list|,
literal|"my first column value|my second column value is q"
argument_list|)
argument_list|,
name|baos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLongValueMultiline
parameter_list|()
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|separator
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"col1"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"col2"
argument_list|)
operator|.
name|maxSize
argument_list|(
operator|-
literal|1
argument_list|)
operator|.
name|wrap
argument_list|()
expr_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
literal|"my first column value"
argument_list|,
literal|"my second column value is quite long"
argument_list|)
expr_stmt|;
name|table
operator|.
name|size
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|table
operator|.
name|print
argument_list|(
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%1$s\n%2$s%n"
argument_list|,
literal|"my first column value|my second column value is"
argument_list|,
literal|"                     |quite long"
argument_list|)
argument_list|,
name|baos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

