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
name|properties
package|;
end_package

begin_import
import|import static
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
name|PrincipalHelper
operator|.
name|names
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|containsInAnyOrder
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
name|fail
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|GroupPrincipal
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|UserPrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
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
name|PropertiesBackingEngineTest
block|{
specifier|private
name|File
name|f
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|IOException
block|{
name|f
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|".tmp"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUserRoles
parameter_list|()
throws|throws
name|IOException
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|PropertiesBackingEngine
name|engine
init|=
operator|new
name|PropertiesBackingEngine
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|engine
operator|.
name|addUser
argument_list|(
literal|"a"
argument_list|,
literal|"aa"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addUser
argument_list|(
literal|"b"
argument_list|,
literal|"bb"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addRole
argument_list|(
literal|"a"
argument_list|,
literal|"role1"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addRole
argument_list|(
literal|"a"
argument_list|,
literal|"role2"
argument_list|)
expr_stmt|;
name|UserPrincipal
name|upa
init|=
name|getUser
argument_list|(
name|engine
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|upa
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role1"
argument_list|,
literal|"role2"
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroup
argument_list|(
literal|"a"
argument_list|,
literal|"g"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroupRole
argument_list|(
literal|"g"
argument_list|,
literal|"role2"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroupRole
argument_list|(
literal|"g"
argument_list|,
literal|"role3"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroup
argument_list|(
literal|"b"
argument_list|,
literal|"g"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroup
argument_list|(
literal|"b"
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addGroupRole
argument_list|(
literal|"g2"
argument_list|,
literal|"role4"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listUsers
argument_list|()
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|upa
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role1"
argument_list|,
literal|"role2"
argument_list|,
literal|"role3"
argument_list|)
argument_list|)
expr_stmt|;
name|checkLoading
argument_list|()
expr_stmt|;
comment|// removing some stuff
name|UserPrincipal
name|upb
init|=
name|getUser
argument_list|(
name|engine
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|listGroups
argument_list|(
name|upa
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|engine
operator|.
name|listGroups
argument_list|(
name|upb
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|GroupPrincipal
name|gp
init|=
name|engine
operator|.
name|listGroups
argument_list|(
name|upa
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|engine
operator|.
name|deleteGroupRole
argument_list|(
literal|"g"
argument_list|,
literal|"role2"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|gp
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// role2 should still be there as it was added to the user directly too
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|upa
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role1"
argument_list|,
literal|"role2"
argument_list|,
literal|"role3"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|upb
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role3"
argument_list|,
literal|"role4"
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteGroup
argument_list|(
literal|"b"
argument_list|,
literal|"g"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteGroup
argument_list|(
literal|"b"
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|listRoles
argument_list|(
name|upb
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteUser
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|deleteUser
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Properties should be empty now"
argument_list|,
literal|0
argument_list|,
name|p
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkLoading
parameter_list|()
throws|throws
name|IOException
block|{
name|PropertiesBackingEngine
name|engine
init|=
operator|new
name|PropertiesBackingEngine
argument_list|(
operator|new
name|Properties
argument_list|(
name|f
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|UserPrincipal
name|upa_2
init|=
name|getUser
argument_list|(
name|engine
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|UserPrincipal
name|upb_2
init|=
name|getUser
argument_list|(
name|engine
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|engine
operator|.
name|listRoles
argument_list|(
name|upa_2
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|upa_2
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role1"
argument_list|,
literal|"role2"
argument_list|,
literal|"role3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|engine
operator|.
name|listRoles
argument_list|(
name|upb_2
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|names
argument_list|(
name|engine
operator|.
name|listRoles
argument_list|(
name|upb_2
argument_list|)
argument_list|)
argument_list|,
name|containsInAnyOrder
argument_list|(
literal|"role2"
argument_list|,
literal|"role3"
argument_list|,
literal|"role4"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|UserPrincipal
name|getUser
parameter_list|(
name|PropertiesBackingEngine
name|engine
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|List
argument_list|<
name|UserPrincipal
argument_list|>
name|matchingUsers
init|=
name|engine
operator|.
name|listUsers
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|user
lambda|->
name|name
operator|.
name|equals
argument_list|(
name|user
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"User with name "
operator|+
name|name
operator|+
literal|" was not found"
argument_list|,
name|matchingUsers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|matchingUsers
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|cleanup
parameter_list|()
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|delete
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Could not delete temporary file: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

