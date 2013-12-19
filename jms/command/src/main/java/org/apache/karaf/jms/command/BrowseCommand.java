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
name|command
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
name|shell
operator|.
name|commands
operator|.
name|Argument
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
name|commands
operator|.
name|Command
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
name|commands
operator|.
name|Option
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
name|table
operator|.
name|ShellTable
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jms"
argument_list|,
name|name
operator|=
literal|"browse"
argument_list|,
name|description
operator|=
literal|"Browse a JMS queue"
argument_list|)
specifier|public
class|class
name|BrowseCommand
extends|extends
name|JmsCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"connectionFactory"
argument_list|,
name|description
operator|=
literal|"The JMS connection factory name"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|connectionFactory
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"queue"
argument_list|,
name|description
operator|=
literal|"The JMS queue to browse"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|queue
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|aliases
operator|=
block|{
literal|"--selector"
block|}
argument_list|,
name|description
operator|=
literal|"The selector to select the messages to browse"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|selector
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{
literal|"--username"
block|}
argument_list|,
name|description
operator|=
literal|"Username to connect to the JMS broker"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|username
init|=
literal|"karaf"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--password"
block|}
argument_list|,
name|description
operator|=
literal|"Password to connect to the JMS broker"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|password
init|=
literal|"karaf"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|aliases
operator|=
block|{
literal|"--verbose"
block|}
argument_list|,
name|description
operator|=
literal|"Display JMS properties"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|verbose
init|=
literal|false
decl_stmt|;
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Message ID"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Content"
argument_list|)
operator|.
name|maxSize
argument_list|(
literal|80
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Charset"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Type"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Correlation ID"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Delivery Mode"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Destination"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Expiration"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Priority"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Redelivered"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"ReplyTo"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Timestamp"
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Properties"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|JmsMessage
argument_list|>
name|messages
init|=
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
decl_stmt|;
for|for
control|(
name|JmsMessage
name|message
range|:
name|messages
control|)
block|{
if|if
condition|(
name|verbose
condition|)
block|{
name|StringBuilder
name|properties
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|property
range|:
name|message
operator|.
name|getProperties
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|properties
operator|.
name|append
argument_list|(
name|property
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|message
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|property
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|message
operator|.
name|getContent
argument_list|()
argument_list|,
name|message
operator|.
name|getCharset
argument_list|()
argument_list|,
name|message
operator|.
name|getType
argument_list|()
argument_list|,
name|message
operator|.
name|getCorrelationID
argument_list|()
argument_list|,
name|message
operator|.
name|getDeliveryMode
argument_list|()
argument_list|,
name|message
operator|.
name|getDestination
argument_list|()
argument_list|,
name|message
operator|.
name|getExpiration
argument_list|()
argument_list|,
name|message
operator|.
name|getPriority
argument_list|()
argument_list|,
name|message
operator|.
name|isRedelivered
argument_list|()
argument_list|,
name|message
operator|.
name|getReplyTo
argument_list|()
argument_list|,
name|message
operator|.
name|getTimestamp
argument_list|()
argument_list|,
name|properties
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|message
operator|.
name|getContent
argument_list|()
argument_list|,
name|message
operator|.
name|getCharset
argument_list|()
argument_list|,
name|message
operator|.
name|getType
argument_list|()
argument_list|,
name|message
operator|.
name|getCorrelationID
argument_list|()
argument_list|,
name|message
operator|.
name|getDeliveryMode
argument_list|()
argument_list|,
name|message
operator|.
name|getDestination
argument_list|()
argument_list|,
name|message
operator|.
name|getExpiration
argument_list|()
argument_list|,
name|message
operator|.
name|getPriority
argument_list|()
argument_list|,
name|message
operator|.
name|isRedelivered
argument_list|()
argument_list|,
name|message
operator|.
name|getReplyTo
argument_list|()
argument_list|,
name|message
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

