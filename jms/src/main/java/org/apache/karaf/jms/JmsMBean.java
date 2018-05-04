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
name|javax
operator|.
name|management
operator|.
name|MBeanException
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
comment|/**  * JMS MBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|JmsMBean
block|{
comment|/**      * List the JMS connection factories.      *      * @return The {@link List} of the JMS connection factories name.      * @throws MBeanException If the MBean fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|getConnectionfactories
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create a JMS connection factory.      *      * @param name The JMS connection factory name.      * @param type The JMS connection factory type (ActiveMQ or WebsphereMQ).      * @param url The JMS connection factory URL. NB: when type is WebsphereMQ, the URL has the format host/port/queuemanager/channel.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * Create a JMS connection factory.      *      * @param name The JMS connection factory name.      * @param type The JMS connection factory type (ActiveMQ or WebsphereMQ).      * @param url The JMS connection factory URL. NB: when type is WebsphereMQ, the URL has the format host/port/queuemanager/channel.      * @param username The JMS connection factory authentication username.      * @param password The JMS connection factory authentication password.      * @param pool The JMS connection factory pooling to use.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * Delete a JMS connection factory.      *      * @param name The JMS connection factory name.      * @throws MBeanException If the MBean fails.      */
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get details about a JMS connection factory.      *      * @param connectionFactory The JMS connection factory name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return A {@link Map} (property/value) containing details.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * Count the messages on a given JMS queue.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The JMS queue name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The number of messages in the queue.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * List the JMS queues.      *      * @param connectionFactory The JMS connection factory name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The {@link List} of JMS queues.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * List the JMS topics.      *      * @param connectionFactory The JMS connection factory name.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The @link List} of JMS topics.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * Browse the messages in a JMS queue.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The JMS queue name.      * @param selector A selector to use to browse only certain messages.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return A {@link TabularData} containing messages details.      * @throws MBeanException If the MBean fails.      */
name|TabularData
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
name|MBeanException
function_decl|;
comment|/**      * Send a JMS message to given queue.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The JMS queue name.      * @param content The message content.      * @param replyTo The message ReplyTo.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @throws MBeanException If the MBean fails.      */
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
name|content
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
name|MBeanException
function_decl|;
comment|/**      * Consume JMS messages from a given queue.      *      * @param connectionFactory The JMS connection factory name.      * @param queue The JMS queue name.      * @param selector A selector to use to consume only certain messages.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The number of messages consumed.      * @throws MBeanException If the MBean fails.      */
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
name|MBeanException
function_decl|;
comment|/**      * Move JMS messages from one queue to another.      *      * @param connectionFactory The JMS connection factory name.      * @param source The source JMS queue name.      * @param destination The destination JMS queue name.      * @param selector A selector to move only certain messages.      * @param username The (optional) username to connect to the JMS broker.      * @param password The (optional) password to connect to the JMS broker.      * @return The number of messages moved.      * @throws MBeanException If the MBean fails.      */
name|int
name|move
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|destination
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
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

