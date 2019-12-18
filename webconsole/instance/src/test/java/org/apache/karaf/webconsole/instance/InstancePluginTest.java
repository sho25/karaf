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
name|webconsole
operator|.
name|instance
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|HashMap
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|core
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

begin_class
specifier|public
class|class
name|InstancePluginTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testParseStringList
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|,
name|testParseStringList
argument_list|(
literal|" a ,b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|testParseStringList
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"hello"
argument_list|)
argument_list|,
name|testParseStringList
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"b"
argument_list|)
argument_list|,
name|testParseStringList
argument_list|(
literal|",b,"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|testParseStringList
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|Exception
block|{
name|InstancePlugin
name|ap
init|=
operator|new
name|InstancePlugin
argument_list|()
decl_stmt|;
name|Method
name|m
init|=
name|ap
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"parseStringList"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|ap
argument_list|,
name|s
argument_list|)
return|;
block|}
specifier|public
name|void
name|testDoPostCreate
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|null
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://someURL"
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"abc"
argument_list|,
literal|"def"
argument_list|)
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
literal|"instance1"
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
block|{}
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
name|InstancePlugin
name|ap
init|=
operator|new
name|InstancePlugin
argument_list|()
decl_stmt|;
name|ap
operator|.
name|setInstanceService
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"create"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"instance1"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"sshPort"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"rmiRegistryPort"
argument_list|,
literal|"456"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"rmiServerPort"
argument_list|,
literal|"789"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"featureURLs"
argument_list|,
literal|"http://someURL"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"features"
argument_list|,
literal|"abc,def"
argument_list|)
expr_stmt|;
name|HttpServletRequest
name|req
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
name|EasyMock
operator|.
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
parameter_list|()
lambda|->
name|params
operator|.
name|get
argument_list|(
name|EasyMock
operator|.
name|getCurrentArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|HttpServletResponse
name|res
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|res
operator|.
name|getWriter
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|ap
operator|.
name|doPost
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
comment|// Check that the operation has succeeded. This will cause some information to be written to
comment|// the outputstream...
name|pw
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"instance"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

