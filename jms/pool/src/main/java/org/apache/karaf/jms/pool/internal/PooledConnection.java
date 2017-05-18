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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionMetaData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ExceptionListener
import|;
end_import

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
name|Queue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueSession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ServerSessionPool
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TemporaryQueue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TemporaryTopic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Topic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicSession
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
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_comment
comment|/**  * Represents a proxy {@link Connection} which is-a {@link TopicConnection} and  * {@link QueueConnection} which is pooled and on {@link #close()} will return  * itself to the sessionPool.  *  *<b>NOTE</b> this implementation is only intended for use when sending  * messages. It does not deal with pooling of consumers; for that look at a  * library like<a href="http://jencks.org/">Jencks</a> such as in<a  * href="http://jencks.org/Message+Driven+POJOs">this example</a>  *  */
end_comment

begin_class
specifier|public
class|class
name|PooledConnection
implements|implements
name|TopicConnection
implements|,
name|QueueConnection
implements|,
name|PooledSessionEventListener
block|{
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PooledConnection
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ConnectionPool
name|pool
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|stopped
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|TemporaryQueue
argument_list|>
name|connTempQueues
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|TemporaryQueue
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|TemporaryTopic
argument_list|>
name|connTempTopics
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|TemporaryTopic
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|PooledSession
argument_list|>
name|loanedSessions
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|PooledSession
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new PooledConnection instance that uses the given ConnectionPool to create      * and manage its resources.  The ConnectionPool instance can be shared amongst many      * PooledConnection instances.      *      * @param pool      *      The connection and pool manager backing this proxy connection object.      */
specifier|public
name|PooledConnection
parameter_list|(
name|ConnectionPool
name|pool
parameter_list|)
block|{
name|this
operator|.
name|pool
operator|=
name|pool
expr_stmt|;
block|}
comment|/**      * Factory method to create a new instance.      */
specifier|public
name|PooledConnection
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|PooledConnection
argument_list|(
name|pool
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|JMSException
block|{
name|this
operator|.
name|cleanupConnectionTemporaryDestinations
argument_list|()
expr_stmt|;
name|this
operator|.
name|cleanupAllLoanedSessions
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|pool
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|pool
operator|.
name|decrementReferenceCount
argument_list|()
expr_stmt|;
name|this
operator|.
name|pool
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|JMSException
block|{
name|assertNotClosed
argument_list|()
expr_stmt|;
name|pool
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|JMSException
block|{
name|stopped
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ConnectionConsumer
name|createConnectionConsumer
parameter_list|(
name|Destination
name|destination
parameter_list|,
name|String
name|selector
parameter_list|,
name|ServerSessionPool
name|serverSessionPool
parameter_list|,
name|int
name|maxMessages
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|createConnectionConsumer
argument_list|(
name|destination
argument_list|,
name|selector
argument_list|,
name|serverSessionPool
argument_list|,
name|maxMessages
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConnectionConsumer
name|createConnectionConsumer
parameter_list|(
name|Topic
name|topic
parameter_list|,
name|String
name|s
parameter_list|,
name|ServerSessionPool
name|serverSessionPool
parameter_list|,
name|int
name|maxMessages
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|createConnectionConsumer
argument_list|(
name|topic
argument_list|,
name|s
argument_list|,
name|serverSessionPool
argument_list|,
name|maxMessages
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConnectionConsumer
name|createDurableConnectionConsumer
parameter_list|(
name|Topic
name|topic
parameter_list|,
name|String
name|selector
parameter_list|,
name|String
name|s1
parameter_list|,
name|ServerSessionPool
name|serverSessionPool
parameter_list|,
name|int
name|i
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|createDurableConnectionConsumer
argument_list|(
name|topic
argument_list|,
name|selector
argument_list|,
name|s1
argument_list|,
name|serverSessionPool
argument_list|,
name|i
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClientID
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|getClientID
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ExceptionListener
name|getExceptionListener
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|getExceptionListener
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConnectionMetaData
name|getMetaData
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|getMetaData
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setExceptionListener
parameter_list|(
name|ExceptionListener
name|exceptionListener
parameter_list|)
throws|throws
name|JMSException
block|{
name|getConnection
argument_list|()
operator|.
name|setExceptionListener
argument_list|(
name|exceptionListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setClientID
parameter_list|(
name|String
name|clientID
parameter_list|)
throws|throws
name|JMSException
block|{
comment|// ignore repeated calls to setClientID() with the same client id
comment|// this could happen when a JMS component such as Spring that uses a
comment|// PooledConnectionFactory shuts down and reinitializes.
if|if
condition|(
name|this
operator|.
name|getConnection
argument_list|()
operator|.
name|getClientID
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|this
operator|.
name|getClientID
argument_list|()
operator|.
name|equals
argument_list|(
name|clientID
argument_list|)
condition|)
block|{
name|getConnection
argument_list|()
operator|.
name|setClientID
argument_list|(
name|clientID
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ConnectionConsumer
name|createConnectionConsumer
parameter_list|(
name|Queue
name|queue
parameter_list|,
name|String
name|selector
parameter_list|,
name|ServerSessionPool
name|serverSessionPool
parameter_list|,
name|int
name|maxMessages
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|createConnectionConsumer
argument_list|(
name|queue
argument_list|,
name|selector
argument_list|,
name|serverSessionPool
argument_list|,
name|maxMessages
argument_list|)
return|;
block|}
comment|// Session factory methods
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
specifier|public
name|QueueSession
name|createQueueSession
parameter_list|(
name|boolean
name|transacted
parameter_list|,
name|int
name|ackMode
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
operator|(
name|QueueSession
operator|)
name|createSession
argument_list|(
name|transacted
argument_list|,
name|ackMode
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|TopicSession
name|createTopicSession
parameter_list|(
name|boolean
name|transacted
parameter_list|,
name|int
name|ackMode
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
operator|(
name|TopicSession
operator|)
name|createSession
argument_list|(
name|transacted
argument_list|,
name|ackMode
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Session
name|createSession
parameter_list|(
name|boolean
name|transacted
parameter_list|,
name|int
name|ackMode
parameter_list|)
throws|throws
name|JMSException
block|{
name|PooledSession
name|result
decl_stmt|;
name|result
operator|=
operator|(
name|PooledSession
operator|)
name|pool
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|ackMode
argument_list|)
expr_stmt|;
comment|// Store the session so we can close the sessions that this PooledConnection
comment|// created in order to ensure that consumers etc are closed per the JMS contract.
name|loanedSessions
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// Add a event listener to the session that notifies us when the session
comment|// creates / destroys temporary destinations and closes etc.
name|result
operator|.
name|addSessionEventListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
specifier|public
name|void
name|onTemporaryQueueCreate
parameter_list|(
name|TemporaryQueue
name|tempQueue
parameter_list|)
block|{
name|connTempQueues
operator|.
name|add
argument_list|(
name|tempQueue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onTemporaryTopicCreate
parameter_list|(
name|TemporaryTopic
name|tempTopic
parameter_list|)
block|{
name|connTempTopics
operator|.
name|add
argument_list|(
name|tempTopic
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSessionClosed
parameter_list|(
name|PooledSession
name|session
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|loanedSessions
operator|.
name|remove
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|JMSException
block|{
name|assertNotClosed
argument_list|()
expr_stmt|;
return|return
name|pool
operator|.
name|getConnection
argument_list|()
return|;
block|}
specifier|protected
name|void
name|assertNotClosed
parameter_list|()
throws|throws
name|javax
operator|.
name|jms
operator|.
name|IllegalStateException
block|{
if|if
condition|(
name|stopped
operator|||
name|pool
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|javax
operator|.
name|jms
operator|.
name|IllegalStateException
argument_list|(
literal|"Connection closed"
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Session
name|createSession
parameter_list|(
name|SessionKey
name|key
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|getConnection
argument_list|()
operator|.
name|createSession
argument_list|(
name|key
operator|.
name|isTransacted
argument_list|()
argument_list|,
name|key
operator|.
name|getAckMode
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PooledConnection { "
operator|+
name|pool
operator|+
literal|" }"
return|;
block|}
comment|/**      * Remove all of the temporary destinations created for this connection.      * This is important since the underlying connection may be reused over a      * long period of time, accumulating all of the temporary destinations from      * each use. However, from the perspective of the lifecycle from the      * client's view, close() closes the connection and, therefore, deletes all      * of the temporary destinations created.      */
specifier|protected
name|void
name|cleanupConnectionTemporaryDestinations
parameter_list|()
block|{
for|for
control|(
name|TemporaryQueue
name|tempQueue
range|:
name|connTempQueues
control|)
block|{
try|try
block|{
name|tempQueue
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"failed to delete Temporary Queue \""
operator|+
name|tempQueue
operator|.
name|toString
argument_list|()
operator|+
literal|"\" on closing pooled connection: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|connTempQueues
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|TemporaryTopic
name|tempTopic
range|:
name|connTempTopics
control|)
block|{
try|try
block|{
name|tempTopic
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"failed to delete Temporary Topic \""
operator|+
name|tempTopic
operator|.
name|toString
argument_list|()
operator|+
literal|"\" on closing pooled connection: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|connTempTopics
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * The PooledSession tracks all Sessions that it created and now we close them.  Closing the      * PooledSession will return the internal Session to the Pool of Session after cleaning up      * all the resources that the Session had allocated for this PooledConnection.      */
specifier|protected
name|void
name|cleanupAllLoanedSessions
parameter_list|()
block|{
for|for
control|(
name|PooledSession
name|session
range|:
name|loanedSessions
control|)
block|{
try|try
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"failed to close laoned Session \""
operator|+
name|session
operator|+
literal|"\" on closing pooled connection: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|loanedSessions
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return the total number of Pooled session including idle sessions that are not      *          currently loaned out to any client.      */
specifier|public
name|int
name|getNumSessions
parameter_list|()
block|{
return|return
name|this
operator|.
name|pool
operator|.
name|getNumSessions
argument_list|()
return|;
block|}
comment|/**      * @return the number of Sessions that are currently checked out of this Connection's session pool.      */
specifier|public
name|int
name|getNumActiveSessions
parameter_list|()
block|{
return|return
name|this
operator|.
name|pool
operator|.
name|getNumActiveSessions
argument_list|()
return|;
block|}
comment|/**      * @return the number of Sessions that are idle in this Connection's sessions pool.      */
specifier|public
name|int
name|getNumtIdleSessions
parameter_list|()
block|{
return|return
name|this
operator|.
name|pool
operator|.
name|getNumIdleSessions
argument_list|()
return|;
block|}
block|}
end_class

end_unit

