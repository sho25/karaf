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
name|client
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
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
name|assertThat
import|;
end_import

begin_class
specifier|public
class|class
name|ClientConfigTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDefaultUser
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|etc
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
literal|"src/test/resources/etc1"
argument_list|)
expr_stmt|;
name|ClientConfig
name|cc
init|=
operator|new
name|ClientConfig
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cc
operator|.
name|getUser
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"karaf"
argument_list|)
argument_list|)
expr_stmt|;
name|cc
operator|=
operator|new
name|ClientConfig
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-u"
block|,
literal|"different-one"
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cc
operator|.
name|getUser
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"different-one"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
literal|"src/test/resources/etc2"
argument_list|)
expr_stmt|;
name|cc
operator|=
operator|new
name|ClientConfig
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cc
operator|.
name|getUser
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|etc
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
name|etc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
