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
name|ops4j
operator|.
name|pax
operator|.
name|jms
operator|.
name|service
operator|.
name|ConnectionFactoryFactory
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
name|InvalidSyntaxException
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
name|javax
operator|.
name|jms
operator|.
name|Queue
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
specifier|private
name|Path
name|deployFolder
decl_stmt|;
specifier|public
name|JmsServiceImpl
parameter_list|()
block|{
name|deployFolder
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
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
name|create
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"jmspooled"
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
block|{
if|if
condition|(
name|type
operator|==
literal|null
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
if|if
condition|(
name|connectionFactories
argument_list|()
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There is already a ConnectionFactory with the name "
operator|+
name|name
argument_list|)
throw|;
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|,
literal|"jms/"
operator|+
name|name
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|ConnectionFactoryFactory
operator|.
name|JMS_CONNECTIONFACTORY_NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|ConnectionFactoryFactory
operator|.
name|JMS_CONNECTIONFACTORY_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|ConnectionFactoryFactory
operator|.
name|JMS_URL
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|ConnectionFactoryFactory
operator|.
name|JMS_USER
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|ConnectionFactoryFactory
operator|.
name|JMS_PASSWORD
argument_list|,
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|pool
operator|.
name|equals
argument_list|(
literal|"narayana"
argument_list|)
condition|)
block|{
name|put
argument_list|(
name|properties
argument_list|,
literal|"pool"
argument_list|,
literal|"narayana"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pool
operator|.
name|equals
argument_list|(
literal|"transx"
argument_list|)
operator|||
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"activemq"
argument_list|)
condition|)
block|{
name|put
argument_list|(
name|properties
argument_list|,
literal|"pool"
argument_list|,
literal|"transx"
argument_list|)
expr_stmt|;
block|}
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
literal|"org.ops4j.connectionfactory"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|config
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|put
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
name|Exception
block|{
name|String
name|filter
init|=
name|String
operator|.
name|format
argument_list|(
literal|"(&(service.factoryPid=org.ops4j.connectionfactory)(%s=%s))"
argument_list|,
name|ConnectionFactoryFactory
operator|.
name|JMS_CONNECTIONFACTORY_NAME
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|Configuration
index|[]
name|configs
init|=
name|configAdmin
operator|.
name|listConfigurations
argument_list|(
name|filter
argument_list|)
decl_stmt|;
for|for
control|(
name|Configuration
name|config
range|:
name|configs
control|)
block|{
name|config
operator|.
name|delete
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
name|connectionFactories
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|,
literal|null
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|this
operator|::
name|getConnectionFactoryName
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|getConnectionFactoryName
parameter_list|(
name|ServiceReference
argument_list|<
name|ConnectionFactory
argument_list|>
name|reference
parameter_list|)
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
return|return
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
return|;
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
return|return
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
else|else
block|{
return|return
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
name|connectionFactoryFileNames
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|Files
operator|.
name|list
argument_list|(
name|deployFolder
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|getFileName
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|toString
argument_list|)
operator|.
name|filter
argument_list|(
name|name
lambda|->
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
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
name|ConnectionMetaData
name|metaData
init|=
name|context
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
argument_list|<>
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
try|try
init|(
name|QueueBrowser
name|browser
init|=
name|context
operator|.
name|createBrowser
argument_list|(
name|context
operator|.
name|createQueue
argument_list|(
name|destination
argument_list|)
argument_list|)
init|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
return|return
name|count
return|;
block|}
block|}
block|}
specifier|private
name|JMSContext
name|createContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
name|createContext
argument_list|(
name|name
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
name|JMSContext
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
return|;
block|}
specifier|private
name|JMSContext
name|createContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|sessionMode
parameter_list|)
block|{
name|ServiceReference
argument_list|<
name|ConnectionFactory
argument_list|>
name|sr
init|=
name|lookupConnectionFactory
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|ConnectionFactory
name|cf
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|sr
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|cf
operator|.
name|createContext
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
name|sessionMode
argument_list|)
return|;
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|sr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ServiceReference
argument_list|<
name|ConnectionFactory
argument_list|>
name|lookupConnectionFactory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|ConnectionFactory
argument_list|>
argument_list|>
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|,
literal|"(|(osgi.jndi.service.name="
operator|+
name|name
operator|+
literal|")(name="
operator|+
name|name
operator|+
literal|")(service.id="
operator|+
name|name
operator|+
literal|"))"
argument_list|)
decl_stmt|;
return|return
name|references
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
name|Comparator
operator|.
expr|<
name|ServiceReference
argument_list|<
name|?
argument_list|>
operator|>
name|naturalOrder
argument_list|()
operator|.
name|reversed
argument_list|()
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No JMS connection factory found for "
operator|+
name|name
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error finding connection factory service "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|DestinationSource
name|getDestinationSource
parameter_list|(
name|JMSContext
name|context
parameter_list|)
throws|throws
name|JMSException
block|{
name|List
argument_list|<
name|DestinationSource
operator|.
name|Factory
argument_list|>
name|factories
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ActiveMQDestinationSourceFactory
argument_list|()
argument_list|,
operator|new
name|ArtemisDestinationSourceFactory
argument_list|()
argument_list|)
decl_stmt|;
name|DestinationSource
name|source
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DestinationSource
operator|.
name|Factory
name|factory
range|:
name|factories
control|)
block|{
name|source
operator|=
name|factory
operator|.
name|create
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
name|source
operator|=
name|d
lambda|->
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
return|return
name|source
return|;
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
return|return
name|getDestinationSource
argument_list|(
name|context
argument_list|)
operator|.
name|getNames
argument_list|(
name|DestinationSource
operator|.
name|DestinationType
operator|.
name|Queue
argument_list|)
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
return|return
name|getDestinationSource
argument_list|(
name|context
argument_list|)
operator|.
name|getNames
argument_list|(
name|DestinationSource
operator|.
name|DestinationType
operator|.
name|Topic
argument_list|)
return|;
block|}
block|}
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
try|try
init|(
name|QueueBrowser
name|browser
init|=
name|context
operator|.
name|createBrowser
argument_list|(
name|context
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|,
name|filter
argument_list|)
init|)
block|{
name|List
argument_list|<
name|JmsMessage
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
return|return
name|messages
return|;
block|}
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
name|JMSProducer
name|producer
init|=
name|context
operator|.
name|createProducer
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
name|producer
operator|.
name|setJMSReplyTo
argument_list|(
name|context
operator|.
name|createQueue
argument_list|(
name|replyTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|send
argument_list|(
name|context
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|,
name|body
argument_list|)
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
init|)
block|{
try|try
init|(
name|JMSConsumer
name|consumer
init|=
name|context
operator|.
name|createConsumer
argument_list|(
name|context
operator|.
name|createQueue
argument_list|(
name|queue
argument_list|)
argument_list|,
name|selector
argument_list|)
init|)
block|{
name|int
name|count
init|=
literal|0
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
literal|500L
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
try|try
init|(
name|JMSContext
name|context
init|=
name|createContext
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
name|JMSContext
operator|.
name|SESSION_TRANSACTED
argument_list|)
init|)
block|{
name|Queue
name|source
init|=
name|context
operator|.
name|createQueue
argument_list|(
name|sourceQueue
argument_list|)
decl_stmt|;
name|Queue
name|target
init|=
name|context
operator|.
name|createQueue
argument_list|(
name|targetQueue
argument_list|)
decl_stmt|;
try|try
init|(
name|JMSConsumer
name|consumer
init|=
name|context
operator|.
name|createConsumer
argument_list|(
name|source
argument_list|,
name|selector
argument_list|)
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Message
name|message
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|500L
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|createProducer
argument_list|()
operator|.
name|send
argument_list|(
name|target
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|context
operator|.
name|commit
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
return|return
name|count
return|;
block|}
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
specifier|public
name|void
name|setConfigAdmin
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

