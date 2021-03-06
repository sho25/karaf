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
name|service
operator|.
name|guard
operator|.
name|tools
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
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

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
name|apache
operator|.
name|karaf
operator|.
name|service
operator|.
name|guard
operator|.
name|tools
operator|.
name|ACLConfigurationParser
operator|.
name|Specificity
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

begin_class
specifier|public
class|class
name|ACLConfigurationParserTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testParseRoles
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"some_role"
argument_list|)
argument_list|,
name|ACLConfigurationParser
operator|.
name|parseRoles
argument_list|(
literal|" some_role   "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"C"
argument_list|)
argument_list|,
name|ACLConfigurationParser
operator|.
name|parseRoles
argument_list|(
literal|"a,b,C"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|ACLConfigurationParser
operator|.
name|parseRoles
argument_list|(
literal|"# test comment"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRolesForInvocation
parameter_list|()
block|{
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"r1, r2"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"bar(java.lang.String, int)[/aa/,/42/]"
argument_list|,
literal|"ra"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"bar(java.lang.String, int)[/bb/,/42/]"
argument_list|,
literal|"rb"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"bar(java.lang.String, int)[\"cc\", \"17\"]"
argument_list|,
literal|"rc"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"bar(java.lang.String, int)"
argument_list|,
literal|"rd"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"bar(java.lang.String)"
argument_list|,
literal|"re"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"rf"
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"ba*"
argument_list|,
literal|"rg #Wildcard"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles1
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|NAME_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
name|config
argument_list|,
name|roles1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"r1"
argument_list|,
literal|"r2"
argument_list|)
argument_list|,
name|roles1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles2
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|NAME_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"test"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|,
name|config
argument_list|,
name|roles2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"r1"
argument_list|,
literal|"r2"
argument_list|)
argument_list|,
name|roles2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles3
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|NO_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"test"
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
name|config
argument_list|,
name|roles3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|roles3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles4
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|ARGUMENT_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"aa"
block|,
literal|42
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"int"
block|}
argument_list|,
name|config
argument_list|,
name|roles4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"ra"
argument_list|)
argument_list|,
name|roles4
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles5
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|ARGUMENT_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"bb"
block|,
literal|42
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"int"
block|}
argument_list|,
name|config
argument_list|,
name|roles5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"rb"
argument_list|)
argument_list|,
name|roles5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles6
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|ARGUMENT_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"cc"
block|,
literal|17
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"int"
block|}
argument_list|,
name|config
argument_list|,
name|roles6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"rc"
argument_list|)
argument_list|,
name|roles6
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles7
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|SIGNATURE_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"aaa"
block|,
literal|42
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"int"
block|}
argument_list|,
name|config
argument_list|,
name|roles7
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"rd"
argument_list|)
argument_list|,
name|roles7
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles8
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|SIGNATURE_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"aa"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|,
name|config
argument_list|,
name|roles8
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"re"
argument_list|)
argument_list|,
name|roles8
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles9
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|NAME_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|42
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"int"
block|}
argument_list|,
name|config
argument_list|,
name|roles9
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"rf"
argument_list|)
argument_list|,
name|roles9
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles10
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Specificity
operator|.
name|WILDCARD_MATCH
argument_list|,
name|ACLConfigurationParser
operator|.
name|getRolesForInvocation
argument_list|(
literal|"barr"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|42
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"int"
block|}
argument_list|,
name|config
argument_list|,
name|roles10
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"rg"
argument_list|)
argument_list|,
name|roles10
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

