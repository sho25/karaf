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
name|features
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|features
operator|.
name|internal
operator|.
name|RepositoryImpl
import|;
end_import

begin_class
specifier|public
class|class
name|ConditionalTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|r
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"internal/f06.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check repo
name|Feature
index|[]
name|features
init|=
name|r
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
operator|.
name|length
argument_list|)
expr_stmt|;
name|Feature
name|feature
init|=
name|features
index|[
literal|0
index|]
decl_stmt|;
name|assertNotNull
argument_list|(
name|feature
operator|.
name|getConditional
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|feature
operator|.
name|getConditional
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Conditional
name|conditional
init|=
name|feature
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|conditional
operator|.
name|getCondition
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|conditional
operator|.
name|getCondition
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Dependency
name|dependency
init|=
name|conditional
operator|.
name|getCondition
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|conditional
operator|.
name|getBundles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|feature
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|wrapperName
init|=
literal|"my6/1.5.3-beta-3"
operator|.
name|replaceAll
argument_list|(
literal|"[^A-Za-z0-9 ]"
argument_list|,
literal|"_"
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

