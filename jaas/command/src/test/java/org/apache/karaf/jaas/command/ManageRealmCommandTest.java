begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
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
name|command
package|;
end_package

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
name|config
operator|.
name|impl
operator|.
name|Config
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
name|config
operator|.
name|impl
operator|.
name|Module
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|Capture
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
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
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
name|anyObject
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
name|capture
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
name|createMock
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
name|eq
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
name|newCapture
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
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|ManageRealmCommandTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSelectByIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|ManageRealmCommand
name|cmd
init|=
operator|new
name|ManageRealmCommand
argument_list|()
decl_stmt|;
comment|// set up two realms, each containing 1 module
name|Config
name|realm1
init|=
name|newConfigNamed
argument_list|(
literal|"realm1"
argument_list|)
decl_stmt|;
name|realm1
operator|.
name|setModules
argument_list|(
operator|new
name|Module
index|[]
block|{
name|newModuleNamed
argument_list|(
literal|"module1"
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|Config
name|realm2
init|=
name|newConfigNamed
argument_list|(
literal|"realm2"
argument_list|)
decl_stmt|;
name|realm2
operator|.
name|setModules
argument_list|(
operator|new
name|Module
index|[]
block|{
name|newModuleNamed
argument_list|(
literal|"module2"
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|Config
index|[]
name|realms
init|=
block|{
name|realm1
block|,
name|realm2
block|}
decl_stmt|;
name|doVerifyIndex
argument_list|(
name|cmd
argument_list|,
literal|1
argument_list|,
name|realms
argument_list|)
expr_stmt|;
name|doVerifyIndex
argument_list|(
name|cmd
argument_list|,
literal|2
argument_list|,
name|realms
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRealmAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|RealmAddCommand
name|cmd
init|=
operator|new
name|RealmAddCommand
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|setRealmname
argument_list|(
literal|"myDummyRealm"
argument_list|)
expr_stmt|;
comment|// prepare mocks
name|Session
name|session
init|=
name|createMock
argument_list|(
name|Session
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// prepare command
name|cmd
operator|.
name|setContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|Object
index|[]
name|mocks
init|=
block|{
name|session
block|,
name|bundleContext
block|,
name|bundle
block|}
decl_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|registerService
argument_list|(
name|anyObject
argument_list|(
name|Class
operator|.
name|class
argument_list|)
argument_list|,
operator|(
name|Object
operator|)
name|anyObject
argument_list|()
argument_list|,
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|mocks
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|execute
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mocks
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testModuleAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|RealmAddCommand
name|cmd
init|=
operator|new
name|RealmAddCommand
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|setRealmname
argument_list|(
literal|"myDummyRealm"
argument_list|)
expr_stmt|;
name|ModuleAddCommand
name|addCmd
init|=
operator|new
name|ModuleAddCommand
argument_list|()
decl_stmt|;
name|addCmd
operator|.
name|setLoginModule
argument_list|(
name|DummyClass
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addCmd
operator|.
name|setPropertiesList
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
comment|// prepare mocks
name|Session
name|session
init|=
name|createMock
argument_list|(
name|Session
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// prepare command
name|cmd
operator|.
name|setContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|addCmd
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|Object
index|[]
name|mocks
init|=
block|{
name|session
block|,
name|bundleContext
block|,
name|bundle
block|}
decl_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|get
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_ENTRY
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|get
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_CMDS
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|4711L
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|Capture
argument_list|<
name|Object
argument_list|>
name|captureSingleArgument
init|=
name|newCapture
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|registerService
argument_list|(
name|anyObject
argument_list|(
name|Class
operator|.
name|class
argument_list|)
argument_list|,
operator|(
name|Object
operator|)
name|capture
argument_list|(
name|captureSingleArgument
argument_list|)
argument_list|,
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|get
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_REALM
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
parameter_list|()
lambda|->
name|captureSingleArgument
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|mocks
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|execute
argument_list|()
expr_stmt|;
name|addCmd
operator|.
name|execute
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mocks
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
operator|(
name|Config
operator|)
name|captureSingleArgument
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now check if two modules are installed (1 initial + 1 addon)
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Config
operator|)
name|captureSingleArgument
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|getModules
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|DummyClass
block|{}
comment|/**      * Verify that command selects the correct realm, given some index.      *      * @param cmd the command to use.      * @param index the index to verify.      * @param realms the array of realms.      * @throws Exception in case of failure.      */
specifier|private
name|void
name|doVerifyIndex
parameter_list|(
name|ManageRealmCommand
name|cmd
parameter_list|,
name|int
name|index
parameter_list|,
name|Config
index|[]
name|realms
parameter_list|)
throws|throws
name|Exception
block|{
comment|// prepare mocks
name|Session
name|session
init|=
name|createMock
argument_list|(
name|Session
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// prepare command
name|cmd
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|cmd
operator|.
name|setRealms
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|realms
argument_list|)
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
for|for
control|(
name|Config
name|realm
range|:
name|realms
control|)
name|realm
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|Object
index|[]
name|mocks
init|=
block|{
name|session
block|,
name|bundleContext
block|,
name|bundle
block|}
decl_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|get
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_REALM
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|get
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_ENTRY
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|get
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_CMDS
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|4711L
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
comment|// verify that the correct realm is returned -- cmd.index is 1-based
name|session
operator|.
name|put
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_REALM
argument_list|,
name|realms
index|[
name|index
operator|-
literal|1
index|]
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|eq
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_ENTRY
argument_list|)
argument_list|,
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|eq
argument_list|(
name|ManageRealmCommand
operator|.
name|JAAS_CMDS
argument_list|)
argument_list|,
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// start the test
name|replay
argument_list|(
name|mocks
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|execute
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mocks
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Config
name|newConfigNamed
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Config
name|res
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|res
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
specifier|private
name|Module
name|newModuleNamed
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Module
name|res
init|=
operator|new
name|Module
argument_list|()
decl_stmt|;
name|res
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|res
operator|.
name|setOptions
argument_list|(
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFlags
argument_list|(
literal|"required"
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

