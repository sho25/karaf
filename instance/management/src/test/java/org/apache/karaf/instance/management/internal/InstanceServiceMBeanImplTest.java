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
name|instance
operator|.
name|management
operator|.
name|internal
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|instance
operator|.
name|InstanceService
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
name|instance
operator|.
name|Instance
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
name|instance
operator|.
name|InstanceSettings
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

begin_class
specifier|public
class|class
name|InstanceServiceMBeanImplTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCreateInstance
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|InstanceSettings
name|instanceSettings
init|=
operator|new
name|InstanceSettings
argument_list|(
literal|123
argument_list|,
literal|456
argument_list|,
literal|789
argument_list|,
literal|"somewhere"
argument_list|,
literal|"someopts"
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"webconsole"
argument_list|,
literal|"funfeat"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|inst
operator|.
name|getPid
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|42
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|createInstance
argument_list|(
literal|"t1"
argument_list|,
name|instanceSettings
argument_list|,
literal|false
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|ab
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|ab
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|ab
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|ab
operator|.
name|createInstance
argument_list|(
literal|"t1"
argument_list|,
literal|123
argument_list|,
literal|456
argument_list|,
literal|789
argument_list|,
literal|"somewhere"
argument_list|,
literal|"someopts"
argument_list|,
literal|" webconsole,  funfeat"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateInstance2
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|InstanceSettings
name|instanceSettings
init|=
operator|new
name|InstanceSettings
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|createInstance
argument_list|(
literal|"t1"
argument_list|,
name|instanceSettings
argument_list|,
literal|false
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|ab
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|ab
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|ab
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|ab
operator|.
name|createInstance
argument_list|(
literal|"t1"
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetInstances
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|i1
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getPid
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1234
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getSshPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|8818
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getRmiRegistryPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1122
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getRmiServerPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|44444
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"i1"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|isRoot
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
name|i1
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"somewhere"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getJavaOpts
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"someopts"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i1
operator|.
name|getState
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"Stopped"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|i1
argument_list|)
expr_stmt|;
name|Instance
name|i2
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|i2
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"i2"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|i2
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstances
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Instance
index|[]
block|{
name|i1
block|,
name|i2
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|TabularData
name|tabularData
init|=
name|instanceServiceMBean
operator|.
name|getInstances
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tabularData
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CompositeData
name|cd1
init|=
name|tabularData
operator|.
name|get
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"i1"
block|}
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|"i1"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|1234
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|8818
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|1122
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|44444
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|"somewhere"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|"someopts"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd1
operator|.
name|containsValue
argument_list|(
literal|"Stopped"
argument_list|)
argument_list|)
expr_stmt|;
name|CompositeData
name|cd2
init|=
name|tabularData
operator|.
name|get
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"i2"
block|}
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|cd2
operator|.
name|containsValue
argument_list|(
literal|"i2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStartInstanceWithJavaOpts
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|start
argument_list|(
literal|"-x -y -z"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|startInstance
argument_list|(
literal|"test instance"
argument_list|,
literal|"-x -y -z"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStartInstanceWithNoJavaOpts
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|start
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|startInstance
argument_list|(
literal|"test instance"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStopInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|stop
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|stopInstance
argument_list|(
literal|"test instance"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDestroyInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|destroyInstance
argument_list|(
literal|"test instance"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSshChangePort
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|changeSshPort
argument_list|(
literal|7788
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|changeSshPort
argument_list|(
literal|"test instance"
argument_list|,
literal|7788
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRmiRegistryChangePort
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|changeRmiRegistryPort
argument_list|(
literal|1123
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|changeRmiRegistryPort
argument_list|(
literal|"test instance"
argument_list|,
literal|1123
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRmiServerChangePort
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|changeRmiServerPort
argument_list|(
literal|44444
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|changeRmiServerPort
argument_list|(
literal|"test instance"
argument_list|,
literal|44444
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangeOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|inst
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Instance
operator|.
name|class
argument_list|)
decl_stmt|;
name|inst
operator|.
name|changeJavaOpts
argument_list|(
literal|"new opts"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|InstanceService
name|instanceService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InstanceService
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instanceService
operator|.
name|getInstance
argument_list|(
literal|"test instance"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inst
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstanceServiceMBeanImpl
name|instanceServiceMBean
init|=
operator|new
name|InstanceServiceMBeanImpl
argument_list|()
decl_stmt|;
name|instanceServiceMBean
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|instanceService
argument_list|,
name|instanceServiceMBean
operator|.
name|getInstanceService
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServiceMBean
operator|.
name|changeJavaOpts
argument_list|(
literal|"test instance"
argument_list|,
literal|"new opts"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|inst
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

