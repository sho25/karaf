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
name|activemq
operator|.
name|ActiveMQConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|advisory
operator|.
name|DestinationSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|command
operator|.
name|ActiveMQQueue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|command
operator|.
name|ActiveMQTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|pool
operator|.
name|PooledConnection
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|TemplateUtils
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
name|Constants
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
name|javax
operator|.
name|jms
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|IllegalStateException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Default implementation of the JMS Service.  */
end_comment

begin_class
specifier|public
class|class
name|JmsServiceImpl
implements|implements
name|JmsService
block|{
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|File
name|deployFolder
decl_stmt|;
specifier|public
name|JmsServiceImpl
parameter_list|()
block|{
name|File
name|karafBase
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|)
decl_stmt|;
name|deployFolder
operator|=
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"deploy"
argument_list|)
expr_stmt|;
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
name|Exception
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"activemq"
argument_list|)
operator|&&
operator|!
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"webspheremq"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"JMS connection factory type not known"
argument_list|)
throw|;
block|}
name|File
name|outFile
init|=
name|getConnectionFactoryFile
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|template
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"activemq"
argument_list|)
condition|)
block|{
comment|// activemq
name|properties
operator|.
name|put
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|template
operator|=
literal|"connectionfactory-activemq.xml"
expr_stmt|;
block|}
else|else
block|{
comment|// webspheremq
name|String
index|[]
name|splitted
init|=
name|url
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitted
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"WebsphereMQ URI should be in the following format: host/port/queuemanager/channel"
argument_list|)
throw|;
block|}
name|properties
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
name|splitted
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
name|splitted
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"queuemanager"
argument_list|,
name|splitted
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"channel"
argument_list|,
name|splitted
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|template
operator|=
literal|"connectionfactory-webspheremq.xml"
expr_stmt|;
block|}
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|template
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Template resource "
operator|+
name|template
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|TemplateUtils
operator|.
name|createFromTemplate
argument_list|(
name|outFile
argument_list|,
name|is
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|getConnectionFactoryFile
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|deployFolder
argument_list|,
literal|"connectionfactory-"
operator|+
name|name
operator|+
literal|".xml"
argument_list|)
return|;
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
name|Exception
block|{
name|File
name|connectionFactoryFile
init|=
name|getConnectionFactoryFile
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|connectionFactoryFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The JMS connection factory file "
operator|+
name|connectionFactoryFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|connectionFactoryFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|connectionFactories
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|connectionFactories
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ServiceReference
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ConnectionFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|connectionFactories
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|connectionFactories
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connectionFactories
operator|.
name|add
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|connectionFactories
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|connectionFactoryFileNames
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|connectionFactoryFileNames
init|=
name|deployFolder
operator|.
name|list
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
literal|"connectionfactory-"
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|connectionFactoryFileNames
argument_list|)
return|;
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
name|IOException
throws|,
name|JMSException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|ConnectionMetaData
name|metaData
init|=
name|connector
operator|.
name|connect
argument_list|()
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"product"
argument_list|,
name|metaData
operator|.
name|getJMSProviderName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|metaData
operator|.
name|getProviderVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|int
name|count
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
specifier|final
name|String
name|destination
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|IOException
throws|,
name|JMSException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|Session
name|session
init|=
name|connector
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|QueueBrowser
name|browser
init|=
name|session
operator|.
name|createBrowser
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|destination
argument_list|)
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|Message
argument_list|>
name|enumeration
init|=
name|browser
operator|.
name|getEnumeration
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|enumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|enumeration
operator|.
name|nextElement
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|browser
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|count
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|DestinationSource
name|getDestinationSource
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|connection
operator|instanceof
name|PooledConnection
condition|)
block|{
name|connection
operator|=
operator|(
operator|(
name|PooledConnection
operator|)
name|connection
operator|)
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|instanceof
name|ActiveMQConnection
condition|)
block|{
return|return
operator|(
operator|(
name|ActiveMQConnection
operator|)
name|connection
operator|)
operator|.
name|getDestinationSource
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
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
name|JMSException
throws|,
name|IOException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|queues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|DestinationSource
name|destinationSource
init|=
name|getDestinationSource
argument_list|(
name|connector
operator|.
name|connect
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinationSource
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|ActiveMQQueue
argument_list|>
name|activeMQQueues
init|=
name|destinationSource
operator|.
name|getQueues
argument_list|()
decl_stmt|;
for|for
control|(
name|ActiveMQQueue
name|activeMQQueue
range|:
name|activeMQQueues
control|)
block|{
name|queues
operator|.
name|add
argument_list|(
name|activeMQQueue
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|queues
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|IOException
throws|,
name|JMSException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|DestinationSource
name|destinationSource
init|=
name|getDestinationSource
argument_list|(
name|connector
operator|.
name|connect
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|topics
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|destinationSource
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|ActiveMQTopic
argument_list|>
name|activeMQTopics
init|=
name|destinationSource
operator|.
name|getTopics
argument_list|()
decl_stmt|;
for|for
control|(
name|ActiveMQTopic
name|activeMQTopic
range|:
name|activeMQTopics
control|)
block|{
name|topics
operator|.
name|add
argument_list|(
name|activeMQTopic
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|topics
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|JmsMessage
argument_list|>
name|browse
parameter_list|(
name|String
name|connectionFactory
parameter_list|,
specifier|final
name|String
name|queue
parameter_list|,
specifier|final
name|String
name|filter
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|JMSException
throws|,
name|IOException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|JmsMessage
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<
name|JmsMessage
argument_list|>
argument_list|()
decl_stmt|;
name|Session
name|session
init|=
name|connector
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|QueueBrowser
name|browser
init|=
name|session
operator|.
name|createBrowser
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|,
name|filter
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|Message
argument_list|>
name|enumeration
init|=
name|browser
operator|.
name|getEnumeration
argument_list|()
decl_stmt|;
while|while
condition|(
name|enumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Message
name|message
init|=
name|enumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|messages
operator|.
name|add
argument_list|(
operator|new
name|JmsMessage
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|browser
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|messages
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|final
name|String
name|queue
parameter_list|,
specifier|final
name|String
name|body
parameter_list|,
specifier|final
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
name|IOException
throws|,
name|JMSException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|Session
name|session
init|=
name|connector
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|session
operator|.
name|createTextMessage
argument_list|(
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setJMSReplyTo
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|replyTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|MessageProducer
name|producer
init|=
name|session
operator|.
name|createProducer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|)
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|producer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|final
name|String
name|queue
parameter_list|,
specifier|final
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
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Session
name|session
init|=
name|connector
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Message
name|message
decl_stmt|;
do|do
block|{
name|message
operator|=
name|consumer
operator|.
name|receive
argument_list|(
literal|5000L
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
do|while
condition|(
name|message
operator|!=
literal|null
condition|)
do|;
return|return
name|count
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|final
name|String
name|sourceQueue
parameter_list|,
specifier|final
name|String
name|targetQueue
parameter_list|,
specifier|final
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
name|IOException
throws|,
name|JMSException
block|{
name|JmsConnector
name|connector
init|=
operator|new
name|JmsConnector
argument_list|(
name|bundleContext
argument_list|,
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Session
name|session
init|=
name|connector
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|sourceQueue
argument_list|)
argument_list|,
name|selector
argument_list|)
decl_stmt|;
name|Message
name|message
decl_stmt|;
do|do
block|{
name|message
operator|=
name|consumer
operator|.
name|receive
argument_list|(
literal|5000L
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|MessageProducer
name|producer
init|=
name|session
operator|.
name|createProducer
argument_list|(
name|session
operator|.
name|createQueue
argument_list|(
name|targetQueue
argument_list|)
argument_list|)
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
block|}
do|while
condition|(
name|message
operator|!=
literal|null
condition|)
do|;
name|consumer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|count
return|;
block|}
finally|finally
block|{
name|connector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
block|}
end_class

end_unit

