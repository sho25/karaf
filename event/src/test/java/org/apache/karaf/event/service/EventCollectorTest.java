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
name|service
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
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
name|assertThat
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
name|assertTrue
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
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
name|IntStream
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
name|EventCollectorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testHandleEvent
parameter_list|()
throws|throws
name|Exception
block|{
name|EventCollector
name|collector
init|=
operator|new
name|EventCollector
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|count
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|0l
argument_list|)
argument_list|)
expr_stmt|;
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|count
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1l
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|findFirst
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLimit
parameter_list|()
block|{
name|EventCollector
name|collector
init|=
operator|new
name|EventCollector
argument_list|()
decl_stmt|;
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"first"
argument_list|)
argument_list|)
expr_stmt|;
name|IntStream
operator|.
name|rangeClosed
argument_list|(
literal|1
argument_list|,
literal|99
argument_list|)
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|count
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|100l
argument_list|)
argument_list|)
expr_stmt|;
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"last"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|count
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|100l
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|noneMatch
argument_list|(
name|event
lambda|->
name|event
operator|.
name|getTopic
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"first"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|collector
operator|.
name|getEvents
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|event
lambda|->
name|event
operator|.
name|getTopic
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"last"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddRemoveConsumer
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Consumer
argument_list|<
name|Event
argument_list|>
name|countingConsumer
init|=
name|event
lambda|->
name|count
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
name|EventCollector
name|collector
init|=
operator|new
name|EventCollector
argument_list|()
decl_stmt|;
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
expr_stmt|;
name|collector
operator|.
name|addConsumer
argument_list|(
name|countingConsumer
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|count
operator|.
name|get
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"another"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|count
operator|.
name|get
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|collector
operator|.
name|removeConsumer
argument_list|(
name|countingConsumer
argument_list|)
expr_stmt|;
name|collector
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|(
literal|"and/another"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|count
operator|.
name|get
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Event
name|event
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
return|return
operator|new
name|Event
argument_list|(
name|topic
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

