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
name|command
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
name|FeatureState
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
name|Repository
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
name|assertTrue
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

begin_class
specifier|public
class|class
name|ListFeaturesCommandTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testHiddenFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesService
name|service
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Repository
name|repo
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Repository
operator|.
name|class
argument_list|)
decl_stmt|;
name|Feature
name|feature
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Feature
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|listRepositories
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Repository
index|[]
block|{
name|repo
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Feature
index|[]
block|{
name|feature
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|isHidden
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|service
argument_list|,
name|repo
argument_list|,
name|feature
argument_list|)
expr_stmt|;
name|ListFeaturesCommand
name|command
init|=
operator|new
name|ListFeaturesCommand
argument_list|()
decl_stmt|;
name|command
operator|.
name|setFeaturesService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|command
operator|.
name|noFormat
operator|=
literal|true
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|out
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|baos
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"feature"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|service
argument_list|,
name|repo
argument_list|,
name|feature
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testShowHiddenFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesService
name|service
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Repository
name|repo
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Repository
operator|.
name|class
argument_list|)
decl_stmt|;
name|Feature
name|feature
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Feature
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|listRepositories
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Repository
index|[]
block|{
name|repo
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Feature
index|[]
block|{
name|feature
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|isHidden
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"feature"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"feature/1.0.0"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|getState
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"feature/1.0.0"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|FeatureState
operator|.
name|Started
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"1.0.0"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|isRequired
argument_list|(
name|feature
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"repository"
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|service
argument_list|,
name|repo
argument_list|,
name|feature
argument_list|)
expr_stmt|;
name|ListFeaturesCommand
name|command
init|=
operator|new
name|ListFeaturesCommand
argument_list|()
decl_stmt|;
name|command
operator|.
name|setFeaturesService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|command
operator|.
name|noFormat
operator|=
literal|true
expr_stmt|;
name|command
operator|.
name|showHidden
operator|=
literal|true
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|out
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|baos
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"feature"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|service
argument_list|,
name|repo
argument_list|,
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

