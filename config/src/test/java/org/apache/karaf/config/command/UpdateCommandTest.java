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
name|config
operator|.
name|command
package|;
end_package

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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|TypedProperties
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
name|config
operator|.
name|core
operator|.
name|ConfigRepository
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link EditCommand}  */
end_comment

begin_class
specifier|public
class|class
name|UpdateCommandTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FACTORY_PID
init|=
literal|"myFactoryPid"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PID
init|=
literal|"myPid"
decl_stmt|;
specifier|public
name|void
name|testupdateRegularConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|UpdateCommand
name|command
init|=
operator|new
name|UpdateCommand
argument_list|()
decl_stmt|;
name|ConfigRepository
name|configRepo
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConfigRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|configRepo
operator|.
name|update
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|PID
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|props
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|command
operator|.
name|setConfigRepository
argument_list|(
name|configRepo
argument_list|)
expr_stmt|;
name|MockCommandSession
name|session
init|=
name|createMockSessionForFactoryEdit
argument_list|(
name|PID
argument_list|,
literal|false
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|command
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configRepo
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|configRepo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testupdateOnNewFactoryPid
parameter_list|()
throws|throws
name|Exception
block|{
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|UpdateCommand
name|command
init|=
operator|new
name|UpdateCommand
argument_list|()
decl_stmt|;
name|ConfigRepository
name|configRepo
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConfigRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|configRepo
operator|.
name|createFactoryConfiguration
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|FACTORY_PID
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
literal|null
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|props
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|PID
operator|+
literal|".35326647"
argument_list|)
expr_stmt|;
name|command
operator|.
name|setConfigRepository
argument_list|(
name|configRepo
argument_list|)
expr_stmt|;
name|MockCommandSession
name|session
init|=
name|createMockSessionForFactoryEdit
argument_list|(
name|FACTORY_PID
argument_list|,
literal|true
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|command
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configRepo
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|configRepo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MockCommandSession
name|createMockSessionForFactoryEdit
parameter_list|(
name|String
name|pid
parameter_list|,
name|boolean
name|isFactory
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
name|MockCommandSession
name|session
init|=
operator|new
name|MockCommandSession
argument_list|()
decl_stmt|;
name|session
operator|.
name|put
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PID
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_FACTORY
argument_list|,
name|isFactory
argument_list|)
expr_stmt|;
name|TypedProperties
name|tp
init|=
operator|new
name|TypedProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|props
operator|.
name|keys
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|tp
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|put
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PROPS
argument_list|,
name|tp
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
block|}
end_class

end_unit

