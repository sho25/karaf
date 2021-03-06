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
name|jms
package|;
end_package

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

begin_comment
comment|/**  * JMS Service.  */
end_comment

begin_interface
specifier|public
interface|interface
name|JmsService
block|{
comment|/**      * List the JMS connection factories.      *      * @return The {@link List} of JMS connection factory names.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|connectionFactories
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * List the JMS connection factories file names.      *      * @return The {@link List} of JMS connection factory file names.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|connectionFactoryFileNames
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Create a new JMS connection factory.      *      * @param name The JMS connection factory name.      * @param type The JMS connection factory type (ActiveMQ, WebsphereMQ, ...).      * @param url The JMS URL to use.      * @throws Exception If the service fails.      */
name|void
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Create a new JMS connection factory.      *      * @param name The JMS connection factory name.      * @param type The JMS connection factory type (ActiveMQ, WebsphereMQ, ...).      * @param url The JMS URL to use.      * @param username The username to use.      * @param password The password to use.      * @param pool Kind of pool to use.      * @throws Exception If the service fails.      */
name|void
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|pool
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Delete a JMS connection factory.      *      * @param name The JMS connection factory name.      * @throws Exception If the service fails.      */
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get details about a given JMS connection factory.      *      * @param connectionFactory The JMS connection factory name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return A {@link Map} (property/value) containing details.      * @throws Exception If the service fails.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Count the number of messages in a JMS queue.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The queue name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The number of messages in a JMS queue.      * @throws Exception If the service fails.      */
name|int
name|count
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|queue
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * List the queues.      *      * @param connectionFactory The JMS connection factory name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The {@link List} of queues.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|queues
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * List the topics.      *      * @param connectionFactory The JMS connection factory name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The {@link List} of topics.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|topics
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Browse a destination.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The queue name.      * @param selector The selector.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The {@link List} of messages.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|JmsMessage
argument_list|>
name|browse
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|queue
parameter_list|,
name|String
name|selector
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Send a message on the given queue.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The queue name.      * @param body The message body.      * @param replyTo The message replyTo header.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @throws Exception If the service fails.      */
name|void
name|send
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|queue
parameter_list|,
name|String
name|body
parameter_list|,
name|String
name|replyTo
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Consume messages from a given destination.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The queue name.      * @param selector The messages selector.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The number of messages consumed.      * @throws Exception If the service fails.      */
name|int
name|consume
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|queue
parameter_list|,
name|String
name|selector
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Move messages from a destination to another.      *      * @param connectionFactory The JMS connection factory name.      * @param sourceQueue The source queue.      * @param targetQueue The target queue.      * @param selector The messages selector on the source queue.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The number of messages moved.      * @throws Exception If the service fails.      */
name|int
name|move
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|sourceQueue
parameter_list|,
name|String
name|targetQueue
parameter_list|,
name|String
name|selector
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

