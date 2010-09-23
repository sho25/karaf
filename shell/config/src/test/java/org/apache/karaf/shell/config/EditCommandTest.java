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
name|io
operator|.
name|InputStream
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
name|HashMap
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
name|framework
operator|.
name|ServiceReference
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
name|EditCommandTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PID
init|=
literal|"my.test.persistent.id"
decl_stmt|;
specifier|private
name|EditCommand
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
name|EditCommand
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
name|ServiceReference
name|reference
init|=
name|createMock
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|context
operator|.
name|getServiceReference
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|reference
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
name|expect
argument_list|(
name|context
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|context
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
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
name|testExecuteOnExistingPid
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
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
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|admin
argument_list|)
expr_stmt|;
comment|// the ConfigAdmin service returns a Dictionary for an existing PID
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|config
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|command
operator|.
name|pid
operator|=
name|PID
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|session
argument_list|)
expr_stmt|;
comment|// the PID and Dictionary should have been set on the session
name|assertEquals
argument_list|(
literal|"The PID should be set on the session"
argument_list|,
name|PID
argument_list|,
name|session
operator|.
name|get
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PID
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"The Dictionary returned by the ConfigAdmin service should be set on the session"
argument_list|,
name|props
argument_list|,
name|session
operator|.
name|get
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PROPS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|testExecuteOnNewPid
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|config
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
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|admin
argument_list|)
expr_stmt|;
comment|// the ConfigAdmin service does not return a Dictionary for a new PID
name|expect
argument_list|(
name|config
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
name|replay
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|command
operator|.
name|pid
operator|=
name|PID
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|session
argument_list|)
expr_stmt|;
comment|// the PID and an empty Dictionary should have been set on the session
name|assertEquals
argument_list|(
literal|"The PID should be set on the session"
argument_list|,
name|PID
argument_list|,
name|session
operator|.
name|get
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PID
argument_list|)
argument_list|)
expr_stmt|;
name|Dictionary
name|props
init|=
operator|(
name|Dictionary
operator|)
name|session
operator|.
name|get
argument_list|(
name|ConfigCommandSupport
operator|.
name|PROPERTY_CONFIG_PROPS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a Dictionary on the session"
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have an empty Dictionary on the session"
argument_list|,
name|props
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/*      * A mock CommandSession implementation that only cares about the properties set on the session      */
specifier|private
class|class
name|MockCommandSession
implements|implements
name|CommandSession
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// not implemented
block|}
specifier|public
name|Object
name|convert
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
comment|// not implemented
return|return
literal|null
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
name|CharSequence
name|commandline
parameter_list|)
throws|throws
name|Exception
block|{
comment|// not implemented
return|return
literal|null
return|;
block|}
specifier|public
name|CharSequence
name|format
parameter_list|(
name|Object
name|target
parameter_list|,
name|int
name|level
parameter_list|)
block|{
comment|// not implemented
return|return
literal|null
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|PrintStream
name|getConsole
parameter_list|()
block|{
comment|// not implemented
return|return
literal|null
return|;
block|}
specifier|public
name|InputStream
name|getKeyboard
parameter_list|()
block|{
comment|// not implemented
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

