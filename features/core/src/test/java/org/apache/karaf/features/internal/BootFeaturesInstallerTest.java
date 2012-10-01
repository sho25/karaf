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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|Feature
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
name|FeaturesService
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

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
name|BootFeaturesInstallerTest
extends|extends
name|TestBase
block|{
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|testParser
parameter_list|()
block|{
name|BootFeaturesInstaller
name|installer
init|=
operator|new
name|BootFeaturesInstaller
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|asList
argument_list|(
name|setOf
argument_list|(
literal|"test1"
argument_list|,
literal|"test2"
argument_list|)
argument_list|,
name|setOf
argument_list|(
literal|"test3"
argument_list|)
argument_list|)
argument_list|,
name|installer
operator|.
name|parseBootFeatures
argument_list|(
literal|"(test1, test2), test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|asList
argument_list|(
name|setOf
argument_list|(
literal|"test1"
argument_list|,
literal|"test2"
argument_list|,
literal|"test3"
argument_list|)
argument_list|)
argument_list|,
name|installer
operator|.
name|parseBootFeatures
argument_list|(
literal|"test1, test2, test3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultBootFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesServiceImpl
name|impl
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|FeaturesServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Feature
name|configFeature
init|=
name|feature
argument_list|(
literal|"config"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|Feature
name|standardFeature
init|=
name|feature
argument_list|(
literal|"standard"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|Feature
name|regionFeature
init|=
name|feature
argument_list|(
literal|"region"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
operator|new
name|Feature
index|[]
block|{}
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"config"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|configFeature
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"standard"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|standardFeature
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"region"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|regionFeature
argument_list|)
expr_stmt|;
name|impl
operator|.
name|installFeatures
argument_list|(
name|setOf
argument_list|(
name|configFeature
argument_list|,
name|standardFeature
argument_list|,
name|regionFeature
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|Option
operator|.
name|NoCleanIfFailure
argument_list|,
name|Option
operator|.
name|ContinueBatchOnFailure
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|impl
argument_list|)
expr_stmt|;
name|BootFeaturesInstaller
name|bootFeatures
init|=
operator|new
name|BootFeaturesInstaller
argument_list|(
name|impl
argument_list|,
literal|"config,standard,region"
argument_list|)
decl_stmt|;
name|bootFeatures
operator|.
name|installBootFeatures
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test checks KARAF-388 which allows you to specify version of boot feature.      * @throws Exception       */
annotation|@
name|Test
specifier|public
name|void
name|testStartDoesNotFailWithNonExistentVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesServiceImpl
name|impl
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|FeaturesServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Feature
index|[]
block|{}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Feature
name|sshFeature
init|=
name|feature
argument_list|(
literal|"ssh"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"ssh"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sshFeature
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.2"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Only the ssh feature should get installed
name|impl
operator|.
name|installFeatures
argument_list|(
name|setOf
argument_list|(
name|sshFeature
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|Option
operator|.
name|NoCleanIfFailure
argument_list|,
name|Option
operator|.
name|ContinueBatchOnFailure
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|impl
argument_list|)
expr_stmt|;
name|BootFeaturesInstaller
name|bootFeatures
init|=
operator|new
name|BootFeaturesInstaller
argument_list|(
name|impl
argument_list|,
literal|"transaction;version=1.2,ssh;version=1.0.0"
argument_list|)
decl_stmt|;
name|bootFeatures
operator|.
name|installBootFeatures
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStagedBoot
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesServiceImpl
name|impl
init|=
name|EasyMock
operator|.
name|createStrictMock
argument_list|(
name|FeaturesServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Feature
name|sshFeature
init|=
name|feature
argument_list|(
literal|"ssh"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|Feature
name|transactionFeature
init|=
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"2.0.0"
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
operator|new
name|Feature
index|[]
block|{}
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"transaction"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|transactionFeature
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|impl
operator|.
name|getFeature
argument_list|(
literal|"ssh"
argument_list|,
literal|"0.0.0"
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|sshFeature
argument_list|)
expr_stmt|;
name|impl
operator|.
name|installFeatures
argument_list|(
name|setOf
argument_list|(
name|transactionFeature
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|Option
operator|.
name|NoCleanIfFailure
argument_list|,
name|Option
operator|.
name|ContinueBatchOnFailure
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|impl
operator|.
name|installFeatures
argument_list|(
name|setOf
argument_list|(
name|sshFeature
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|Option
operator|.
name|NoCleanIfFailure
argument_list|,
name|Option
operator|.
name|ContinueBatchOnFailure
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|impl
argument_list|)
expr_stmt|;
name|BootFeaturesInstaller
name|bootFeatures
init|=
operator|new
name|BootFeaturesInstaller
argument_list|(
name|impl
argument_list|,
literal|"(transaction), ssh"
argument_list|)
decl_stmt|;
name|bootFeatures
operator|.
name|installBootFeatures
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

