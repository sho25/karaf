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
name|bundle
operator|.
name|command
operator|.
name|bundletree
package|;
end_package

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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|bundle
operator|.
name|command
operator|.
name|bundletree
operator|.
name|Node
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
name|bundle
operator|.
name|command
operator|.
name|bundletree
operator|.
name|Tree
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

begin_comment
comment|/**  * Test cases for {@link org.apache.karaf.shell.dev.util.Tree}  * and {@link org.apache.karaf.shell.dev.util.Node}  */
end_comment

begin_class
specifier|public
class|class
name|TreeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|writeTreeWithOneChild
parameter_list|()
throws|throws
name|IOException
block|{
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
init|=
operator|new
name|Tree
argument_list|<>
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addChild
argument_list|(
literal|"child"
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
name|read
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+- child"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|writeTreeWithOneChildAndNodeConverter
parameter_list|()
throws|throws
name|IOException
block|{
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
init|=
operator|new
name|Tree
argument_list|<>
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addChild
argument_list|(
literal|"child"
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|tree
operator|.
name|write
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|writer
argument_list|)
argument_list|,
operator|new
name|Tree
operator|.
name|Converter
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|toString
parameter_list|(
name|Node
argument_list|<
name|String
argument_list|>
name|node
parameter_list|)
block|{
return|return
literal|"my "
operator|+
name|node
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"my root"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+- my child"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|writeTreeWithChildAndGrandChild
parameter_list|()
throws|throws
name|IOException
block|{
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
init|=
operator|new
name|Tree
argument_list|<>
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|node
init|=
name|tree
operator|.
name|addChild
argument_list|(
literal|"child"
argument_list|)
decl_stmt|;
name|node
operator|.
name|addChild
argument_list|(
literal|"grandchild"
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
name|read
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+- child"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"   +- grandchild"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|writeTreeWithTwoChildrenAndOneGrandchild
parameter_list|()
throws|throws
name|IOException
block|{
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
init|=
operator|new
name|Tree
argument_list|<>
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|child
init|=
name|tree
operator|.
name|addChild
argument_list|(
literal|"child1"
argument_list|)
decl_stmt|;
name|child
operator|.
name|addChild
argument_list|(
literal|"grandchild"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addChild
argument_list|(
literal|"child2"
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
name|read
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+- child1"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"|  +- grandchild"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"+- child2"
argument_list|,
name|reader
operator|.
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|flattenTree
parameter_list|()
throws|throws
name|IOException
block|{
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
init|=
operator|new
name|Tree
argument_list|<>
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|child1
init|=
name|tree
operator|.
name|addChild
argument_list|(
literal|"child1"
argument_list|)
decl_stmt|;
name|child1
operator|.
name|addChild
argument_list|(
literal|"grandchild"
argument_list|)
expr_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|child2
init|=
name|tree
operator|.
name|addChild
argument_list|(
literal|"child2"
argument_list|)
decl_stmt|;
name|child2
operator|.
name|addChild
argument_list|(
literal|"grandchild"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|elements
init|=
name|tree
operator|.
name|flatten
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|contains
argument_list|(
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|contains
argument_list|(
literal|"child1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|contains
argument_list|(
literal|"child2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|contains
argument_list|(
literal|"grandchild"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|hasAncestor
parameter_list|()
throws|throws
name|IOException
block|{
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
init|=
operator|new
name|Tree
argument_list|<>
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|child1
init|=
name|tree
operator|.
name|addChild
argument_list|(
literal|"child1"
argument_list|)
decl_stmt|;
name|child1
operator|.
name|addChild
argument_list|(
literal|"grandchild"
argument_list|)
expr_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|child2
init|=
name|tree
operator|.
name|addChild
argument_list|(
literal|"child2"
argument_list|)
decl_stmt|;
name|Node
argument_list|<
name|String
argument_list|>
name|node
init|=
name|child2
operator|.
name|addChild
argument_list|(
literal|"grandchild2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|node
operator|.
name|hasAncestor
argument_list|(
literal|"child2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|node
operator|.
name|hasAncestor
argument_list|(
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|node
operator|.
name|hasAncestor
argument_list|(
literal|"child1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BufferedReader
name|read
parameter_list|(
name|Tree
argument_list|<
name|String
argument_list|>
name|tree
parameter_list|)
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|tree
operator|.
name|write
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|reader
return|;
block|}
block|}
end_class

end_unit

