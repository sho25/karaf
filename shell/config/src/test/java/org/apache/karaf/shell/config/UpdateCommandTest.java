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
name|shell
operator|.
name|config
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
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
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
name|PID
init|=
literal|"my.test.persistent.id-other"
decl_stmt|;
specifier|private
name|UpdateCommand
name|command
decl_stmt|;
specifier|private
name|BundleContext
name|context
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|admin
decl_stmt|;
specifier|private
name|CommandSession
name|session
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|command
operator|=
operator|new
name|UpdateCommand
argument_list|()
expr_stmt|;
name|context
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
operator|.
name|setBundleContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|admin
operator|=
name|createMock
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
operator|.
name|setConfigRepository
argument_list|(
operator|new
name|ConfigRepository
argument_list|(
literal|null
argument_list|,
name|admin
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|context
operator|.
name|getBundle
argument_list|(
literal|0
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
name|context
argument_list|)
expr_stmt|;
name|session
operator|=
operator|new
name|MockCommandSession
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testupdateOnNewFactoryPid
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
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
name|PID
argument_list|)
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PROPS
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|Configuration
name|configNew
init|=
name|createMock
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|admin
operator|.
name|getConfiguration
argument_list|(
name|PID
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|configNew
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configNew
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Configuration
name|configFac
init|=
name|createMock
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|admin
operator|.
name|createFactoryConfiguration
argument_list|(
name|PID
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|PID
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|)
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|configFac
argument_list|)
expr_stmt|;
name|configFac
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configFac
operator|.
name|getBundleLocation
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configNew
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configFac
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

