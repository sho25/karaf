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
name|core
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
name|Collection
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
name|core
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
name|core
operator|.
name|internal
operator|.
name|InstanceToTableMapper
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
name|InstanceToTableMapperTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testJMXInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|instance
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
name|instance
operator|.
name|getPid
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1712
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"MyInstance"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|isRoot
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
name|expect
argument_list|(
name|instance
operator|.
name|getSshPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getRmiRegistryPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getRmiServerPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getState
argument_list|()
argument_list|)
operator|.
name|andThrow
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"gotcha"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
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
name|instance
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
name|replay
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|TabularData
name|td
init|=
name|InstanceToTableMapper
operator|.
name|tableFrom
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|instance
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|keys
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|td
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"MyInstance"
argument_list|,
name|keys
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|CompositeData
name|cd
init|=
name|td
operator|.
name|get
argument_list|(
name|keys
operator|.
name|toArray
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1712
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Pid"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"MyInstance"
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Is Root"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"SSH Port"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"RMI Registry Port"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"RMI Server Port"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Error"
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"State"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"somewhere"
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Location"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"someopts"
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"JavaOpts"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testJMXInstance2
parameter_list|()
throws|throws
name|Exception
block|{
name|Instance
name|instance
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
name|instance
operator|.
name|getPid
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1712
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"MyInstance"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
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
name|instance
operator|.
name|getSshPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getRmiRegistryPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getRmiServerPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getState
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"Started"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|instance
operator|.
name|getJavaOpts
argument_list|()
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
name|instance
argument_list|)
expr_stmt|;
name|TabularData
name|td
init|=
name|InstanceToTableMapper
operator|.
name|tableFrom
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|instance
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|keys
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|td
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"MyInstance"
argument_list|,
name|keys
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|CompositeData
name|cd
init|=
name|td
operator|.
name|get
argument_list|(
name|keys
operator|.
name|toArray
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1712
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Pid"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"MyInstance"
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"Is Root"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"SSH Port"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"RMI Registry Port"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"RMI Server Port"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|cd
operator|.
name|get
argument_list|(
literal|"State"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|cd
operator|.
name|get
argument_list|(
literal|"Location"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|cd
operator|.
name|get
argument_list|(
literal|"JavaOpts"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
