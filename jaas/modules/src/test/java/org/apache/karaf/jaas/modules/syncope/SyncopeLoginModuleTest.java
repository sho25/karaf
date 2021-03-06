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
name|jaas
operator|.
name|modules
operator|.
name|syncope
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|contains
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|SyncopeLoginModuleTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRolesExtractionSyncope1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|syncopeResponse
init|=
name|read
argument_list|(
literal|"syncope1Response.xml"
argument_list|)
decl_stmt|;
name|SyncopeLoginModule
name|syncopeLoginModule
init|=
operator|new
name|SyncopeLoginModule
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|syncopeLoginModule
operator|.
name|extractingRolesSyncope1
argument_list|(
name|syncopeResponse
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|roles
argument_list|,
name|contains
argument_list|(
literal|"admin"
argument_list|,
literal|"another"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRolesExtractionSyncope2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|syncopeResponse
init|=
name|read
argument_list|(
literal|"syncope2Response.json"
argument_list|)
decl_stmt|;
name|SyncopeLoginModule
name|syncopeLoginModule
init|=
operator|new
name|SyncopeLoginModule
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|SyncopeLoginModule
operator|.
name|USE_ROLES_FOR_SYNCOPE2
argument_list|,
literal|"true"
argument_list|)
decl_stmt|;
name|syncopeLoginModule
operator|.
name|initialize
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|syncopeLoginModule
operator|.
name|extractingRolesSyncope2
argument_list|(
name|syncopeResponse
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|roles
argument_list|,
name|contains
argument_list|(
literal|"admin"
argument_list|,
literal|"another"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupsExtractionSyncope2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|syncopeResponse
init|=
name|read
argument_list|(
literal|"syncope2Response.json"
argument_list|)
decl_stmt|;
name|SyncopeLoginModule
name|syncopeLoginModule
init|=
operator|new
name|SyncopeLoginModule
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|syncopeLoginModule
operator|.
name|extractingRolesSyncope2
argument_list|(
name|syncopeResponse
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|roles
argument_list|,
name|contains
argument_list|(
literal|"manager"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|read
parameter_list|(
name|String
name|resourceName
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|IOException
block|{
name|URI
name|response
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
return|return
name|Files
operator|.
name|lines
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|response
argument_list|)
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

