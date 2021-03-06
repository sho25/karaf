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
name|command
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|ObjectClassMatcherTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetShortName
parameter_list|()
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestClass"
argument_list|,
name|ObjectClassMatcher
operator|.
name|getShortName
argument_list|(
literal|"org.apache.TestClass"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|ObjectClassMatcher
operator|.
name|getShortName
argument_list|(
literal|"test."
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TestClass"
argument_list|,
name|ObjectClassMatcher
operator|.
name|getShortName
argument_list|(
literal|"TestClass"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchesName
parameter_list|()
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|ObjectClassMatcher
operator|.
name|matchesName
argument_list|(
literal|"org.apache.TestClass"
argument_list|,
literal|"TestClass"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|ObjectClassMatcher
operator|.
name|matchesName
argument_list|(
literal|"TestClass"
argument_list|,
literal|"TestClass"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchesAtLeastOneName
parameter_list|()
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|ObjectClassMatcher
operator|.
name|matchesAtLeastOneName
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"other"
block|,
literal|"org.apache.TestClass"
block|}
argument_list|,
literal|"TestClass"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|ObjectClassMatcher
operator|.
name|matchesAtLeastOneName
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"TestClass2"
block|}
argument_list|,
literal|"TestClass"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

