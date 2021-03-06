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
name|event
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
name|createControl
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|event
operator|.
name|command
operator|.
name|EventDisplayCommand
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
name|event
operator|.
name|service
operator|.
name|EventCollector
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
name|IMocksControl
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
name|service
operator|.
name|event
operator|.
name|Event
import|;
end_import

begin_class
specifier|public
class|class
name|EventDisplayCommandTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|IMocksControl
name|c
init|=
name|createControl
argument_list|()
decl_stmt|;
name|EventDisplayCommand
name|display
init|=
operator|new
name|EventDisplayCommand
argument_list|()
decl_stmt|;
name|display
operator|.
name|session
operator|=
name|c
operator|.
name|createMock
argument_list|(
name|Session
operator|.
name|class
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|display
operator|.
name|session
operator|.
name|getConsole
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|display
operator|.
name|collector
operator|=
operator|new
name|EventCollector
argument_list|()
expr_stmt|;
name|display
operator|.
name|collector
operator|.
name|handleEvent
argument_list|(
operator|new
name|Event
argument_list|(
literal|"myTopic"
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|replay
argument_list|()
expr_stmt|;
name|display
operator|.
name|execute
argument_list|()
expr_stmt|;
name|c
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

