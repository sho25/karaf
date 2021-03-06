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
name|apache
operator|.
name|karaf
operator|.
name|event
operator|.
name|service
operator|.
name|TopicPredicate
operator|.
name|matchTopic
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
name|assertFalse
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
name|function
operator|.
name|Predicate
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
name|TopicPredicateTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMatchAll
parameter_list|()
block|{
name|Predicate
argument_list|<
name|Event
argument_list|>
name|matcher
init|=
name|matchTopic
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"my/other"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchSpecific
parameter_list|()
block|{
name|Predicate
argument_list|<
name|Event
argument_list|>
name|matcher
init|=
name|matchTopic
argument_list|(
literal|"myTopic"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"myTopic/test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"my/other"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchSubTopics
parameter_list|()
block|{
name|Predicate
argument_list|<
name|Event
argument_list|>
name|matcher
init|=
name|matchTopic
argument_list|(
literal|"myTopic*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"myTopic"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"myTopic/test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|test
argument_list|(
name|event
argument_list|(
literal|"my/other"
argument_list|)
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

