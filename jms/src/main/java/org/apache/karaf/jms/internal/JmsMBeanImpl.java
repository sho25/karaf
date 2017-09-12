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
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jms
operator|.
name|JmsMBean
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
name|jms
operator|.
name|JmsMessage
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
name|jms
operator|.
name|JmsService
import|;
end_import

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
name|*
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
comment|/**  * Default implementation of the JMS MBean.  */
end_comment

begin_class
specifier|public
class|class
name|JmsMBeanImpl
implements|implements
name|JmsMBean
block|{
specifier|private
name|JmsService
name|jmsService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConnectionfactories
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|connectionFactories
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
name|jmsService
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|jmsService
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|jmsService
operator|.
name|delete
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|info
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|count
argument_list|(
name|connectionFactory
argument_list|,
name|queue
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|queues
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|topics
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
name|jmsService
operator|.
name|send
argument_list|(
name|connectionFactory
argument_list|,
name|queue
argument_list|,
name|content
argument_list|,
name|replyTo
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|consume
argument_list|(
name|connectionFactory
argument_list|,
name|queue
argument_list|,
name|selector
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
return|return
name|jmsService
operator|.
name|move
argument_list|(
name|connectionFactory
argument_list|,
name|source
argument_list|,
name|destination
argument_list|,
name|selector
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
block|{
name|CompositeType
name|type
init|=
operator|new
name|CompositeType
argument_list|(
literal|"message"
argument_list|,
literal|"JMS Message"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"content"
block|,
literal|"charset"
block|,
literal|"type"
block|,
literal|"correlation"
block|,
literal|"delivery"
block|,
literal|"destination"
block|,
literal|"expiration"
block|,
literal|"priority"
block|,
literal|"redelivered"
block|,
literal|"replyto"
block|,
literal|"timestamp"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Message ID"
block|,
literal|"Content"
block|,
literal|"Charset"
block|,
literal|"Type"
block|,
literal|"Correlation ID"
block|,
literal|"Delivery Mode"
block|,
literal|"Destination"
block|,
literal|"Expiration Date"
block|,
literal|"Priority"
block|,
literal|"Redelivered"
block|,
literal|"Reply-To"
block|,
literal|"Timestamp"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|INTEGER
block|,
name|SimpleType
operator|.
name|BOOLEAN
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"messages"
argument_list|,
literal|"JMS Messages"
argument_list|,
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
for|for
control|(
name|JmsMessage
name|message
range|:
name|getJmsService
argument_list|()
operator|.
name|browse
argument_list|(
name|connectionFactory
argument_list|,
name|queue
argument_list|,
name|selector
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"content"
block|,
literal|"charset"
block|,
literal|"type"
block|,
literal|"correlation"
block|,
literal|"delivery"
block|,
literal|"destination"
block|,
literal|"expiration"
block|,
literal|"priority"
block|,
literal|"redelivered"
block|,
literal|"replyto"
block|,
literal|"timestamp"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|message
operator|.
name|getMessageId
argument_list|()
block|,
name|message
operator|.
name|getContent
argument_list|()
block|,
name|message
operator|.
name|getCharset
argument_list|()
block|,
name|message
operator|.
name|getType
argument_list|()
block|,
name|message
operator|.
name|getCorrelationID
argument_list|()
block|,
name|message
operator|.
name|getDeliveryMode
argument_list|()
block|,
name|message
operator|.
name|getDestination
argument_list|()
block|,
name|message
operator|.
name|getExpiration
argument_list|()
block|,
name|message
operator|.
name|getPriority
argument_list|()
block|,
name|message
operator|.
name|isRedelivered
argument_list|()
block|,
name|message
operator|.
name|getReplyTo
argument_list|()
block|,
name|message
operator|.
name|getTimestamp
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|JmsService
name|getJmsService
parameter_list|()
block|{
return|return
name|jmsService
return|;
block|}
specifier|public
name|void
name|setJmsService
parameter_list|(
name|JmsService
name|jmsService
parameter_list|)
block|{
name|this
operator|.
name|jmsService
operator|=
name|jmsService
expr_stmt|;
block|}
block|}
end_class

end_unit
