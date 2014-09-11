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
name|util
operator|.
name|Properties
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
name|service
operator|.
name|RepositoryImpl
import|;
end_import

begin_class
specifier|public
class|class
name|AppendTest
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.data"
argument_list|,
literal|"data"
argument_list|)
expr_stmt|;
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
literal|"internal/service/f08.xml"
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
name|ConfigInfo
name|configInfo
init|=
name|feature
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|configInfo
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configInfo
operator|.
name|isAppend
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
name|configInfo
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|String
name|property
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|property
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|property
operator|.
name|contains
argument_list|(
literal|"${"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

