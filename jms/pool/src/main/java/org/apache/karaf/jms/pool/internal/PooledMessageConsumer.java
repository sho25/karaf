begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|pool
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_comment
comment|/**  * A {@link MessageConsumer} which was created by {@link PooledSession}.  */
end_comment

begin_class
specifier|public
class|class
name|PooledMessageConsumer
implements|implements
name|MessageConsumer
block|{
specifier|private
specifier|final
name|PooledSession
name|session
decl_stmt|;
specifier|private
specifier|final
name|MessageConsumer
name|delegate
decl_stmt|;
comment|/**      * Wraps the message consumer.      *      * @param session  the pooled session      * @param delegate the created consumer to wrap      */
specifier|public
name|PooledMessageConsumer
parameter_list|(
name|PooledSession
name|session
parameter_list|,
name|MessageConsumer
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|JMSException
block|{
comment|// ensure session removes consumer as its closed now
name|session
operator|.
name|onConsumerClose
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|MessageListener
name|getMessageListener
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|delegate
operator|.
name|getMessageListener
argument_list|()
return|;
block|}
specifier|public
name|String
name|getMessageSelector
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|delegate
operator|.
name|getMessageSelector
argument_list|()
return|;
block|}
specifier|public
name|Message
name|receive
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|delegate
operator|.
name|receive
argument_list|()
return|;
block|}
specifier|public
name|Message
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|delegate
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
return|;
block|}
specifier|public
name|Message
name|receiveNoWait
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|delegate
operator|.
name|receiveNoWait
argument_list|()
return|;
block|}
specifier|public
name|void
name|setMessageListener
parameter_list|(
name|MessageListener
name|listener
parameter_list|)
throws|throws
name|JMSException
block|{
name|delegate
operator|.
name|setMessageListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

