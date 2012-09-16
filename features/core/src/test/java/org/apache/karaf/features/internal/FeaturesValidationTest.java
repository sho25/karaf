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
operator|.
name|internal
package|;
end_package

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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|FeaturesValidationTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNoNs
parameter_list|()
throws|throws
name|Exception
block|{
name|FeatureValidationUtil
operator|.
name|validate
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"f01.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNs10
parameter_list|()
throws|throws
name|Exception
block|{
name|FeatureValidationUtil
operator|.
name|validate
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"f02.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNs10NoName
parameter_list|()
throws|throws
name|Exception
block|{
name|FeatureValidationUtil
operator|.
name|validate
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"f03.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNs11
parameter_list|()
throws|throws
name|Exception
block|{
name|FeatureValidationUtil
operator|.
name|validate
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"f04.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNs12
parameter_list|()
throws|throws
name|Exception
block|{
name|FeatureValidationUtil
operator|.
name|validate
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"f06.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNs13
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|FeatureValidationUtil
operator|.
name|validate
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"f05.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Validation should have failed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
block|}
end_class

end_unit

