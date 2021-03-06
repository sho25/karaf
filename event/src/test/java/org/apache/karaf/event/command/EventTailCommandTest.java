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
name|mock
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|EventTailCommand
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
name|EventTailCommandTest
block|{
specifier|private
name|Exception
name|exception
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testTail
parameter_list|()
throws|throws
name|Exception
block|{
name|EventTailCommand
name|tail
init|=
operator|new
name|EventTailCommand
argument_list|()
decl_stmt|;
name|tail
operator|.
name|session
operator|=
name|mock
argument_list|(
name|Session
operator|.
name|class
argument_list|)
expr_stmt|;
name|tail
operator|.
name|collector
operator|=
operator|new
name|EventCollector
argument_list|()
expr_stmt|;
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
name|expect
argument_list|(
name|tail
operator|.
name|session
operator|.
name|getConsole
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|exception
operator|=
literal|null
expr_stmt|;
name|replay
argument_list|(
name|tail
operator|.
name|session
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|tail
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exception
operator|=
name|e
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|tail
operator|.
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
comment|// Will interrupt the tail
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
name|verify
argument_list|(
name|tail
operator|.
name|session
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Event
name|event
parameter_list|()
block|{
return|return
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
return|;
block|}
block|}
end_class

end_unit

