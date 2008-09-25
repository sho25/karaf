begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|auth
operator|.
name|RemoteLoginModule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|handler
operator|.
name|ClientMessageHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|handler
operator|.
name|ClientSessionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|RemoteExecuteException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|client
operator|.
name|RshClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|crypto
operator|.
name|CryptoContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|jaas
operator|.
name|JaasConfigurationHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|jaas
operator|.
name|UsernamePasswordCallbackHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|message
operator|.
name|CloseShellMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|message
operator|.
name|ConnectMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|message
operator|.
name|EchoMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|message
operator|.
name|ExecuteMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|message
operator|.
name|OpenShellMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|message
operator|.
name|MessageHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|transport
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|transport
operator|.
name|Transport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|transport
operator|.
name|TransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|transport
operator|.
name|TransportFactoryLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|handler
operator|.
name|demux
operator|.
name|DemuxingIoHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|component
operator|.
name|annotations
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|component
operator|.
name|annotations
operator|.
name|Requirement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
import|;
end_import

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

begin_comment
comment|/**  * Provides support for the client-side of the remote shell protocol.  *  * @version $Rev: 638824 $ $Date: 2008-03-19 14:30:40 +0100 (Wed, 19 Mar 2008) $  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
name|role
operator|=
name|SpringRshClient
operator|.
name|class
argument_list|,
name|instantiationStrategy
operator|=
literal|"per-lookup"
argument_list|)
specifier|public
class|class
name|SpringRshClient
extends|extends
name|RshClient
implements|implements
name|Initializable
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Requirement
specifier|private
name|CryptoContext
name|crypto
decl_stmt|;
annotation|@
name|Requirement
specifier|private
name|TransportFactoryLocator
name|locator
decl_stmt|;
specifier|private
name|Transport
name|transport
decl_stmt|;
specifier|private
name|Session
name|session
decl_stmt|;
annotation|@
name|Requirement
argument_list|(
name|role
operator|=
name|ClientMessageHandler
operator|.
name|class
argument_list|)
specifier|private
name|List
argument_list|<
name|ClientMessageHandler
argument_list|>
name|handlers
decl_stmt|;
specifier|public
name|SpringRshClient
parameter_list|()
block|{     }
specifier|public
name|SpringRshClient
parameter_list|(
specifier|final
name|CryptoContext
name|crypto
parameter_list|,
specifier|final
name|TransportFactoryLocator
name|locator
parameter_list|,
specifier|final
name|List
argument_list|<
name|ClientMessageHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|crypto
operator|=
name|crypto
expr_stmt|;
name|this
operator|.
name|locator
operator|=
name|locator
expr_stmt|;
name|this
operator|.
name|handlers
operator|=
name|handlers
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
operator|new
name|JaasConfigurationHelper
argument_list|(
literal|"client.login.conf"
argument_list|)
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|connect
parameter_list|(
specifier|final
name|URI
name|remote
parameter_list|,
specifier|final
name|URI
name|local
parameter_list|)
throws|throws
name|Exception
block|{
name|TransportFactory
name|factory
init|=
name|locator
operator|.
name|locate
argument_list|(
name|remote
argument_list|)
decl_stmt|;
name|transport
operator|=
name|factory
operator|.
name|connect
argument_list|(
name|remote
argument_list|,
name|local
argument_list|,
operator|new
name|Handler
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|=
name|transport
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Connected to: {}"
argument_list|,
name|remote
argument_list|)
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
return|return
name|session
operator|.
name|getInputStream
argument_list|()
return|;
block|}
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
block|{
return|return
name|session
operator|.
name|getOutputStream
argument_list|()
return|;
block|}
specifier|public
name|Transport
name|getTransport
parameter_list|()
block|{
return|return
name|transport
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|transport
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|login
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
name|doHandshake
argument_list|()
expr_stmt|;
name|doLogin
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doHandshake
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Handshaking"
argument_list|)
expr_stmt|;
name|ClientSessionContext
name|context
init|=
name|ClientSessionContext
operator|.
name|BINDER
operator|.
name|lookup
argument_list|(
name|session
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|response
init|=
name|session
operator|.
name|request
argument_list|(
operator|new
name|ConnectMessage
argument_list|(
name|crypto
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|ConnectMessage
operator|.
name|Result
condition|)
block|{
name|ConnectMessage
operator|.
name|Result
name|result
init|=
operator|(
name|ConnectMessage
operator|.
name|Result
operator|)
name|response
decl_stmt|;
name|context
operator|.
name|pk
operator|=
name|result
operator|.
name|getPublicKey
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InternalError
argument_list|(
literal|"Unexpected handshake response: "
operator|+
name|response
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|doLogin
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Logging in: {}"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|ClientSessionContext
name|context
init|=
name|ClientSessionContext
operator|.
name|BINDER
operator|.
name|lookup
argument_list|(
name|session
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|UsernamePasswordCallbackHandler
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|LoginContext
name|loginContext
init|=
operator|new
name|LoginContext
argument_list|(
literal|"RshClient"
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|RemoteLoginModule
operator|.
name|setTransport
argument_list|(
name|transport
argument_list|)
expr_stmt|;
try|try
block|{
name|loginContext
operator|.
name|login
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|RemoteLoginModule
operator|.
name|unsetTransport
argument_list|()
expr_stmt|;
block|}
name|context
operator|.
name|subject
operator|=
name|loginContext
operator|.
name|getSubject
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Subject: {}"
argument_list|,
name|context
operator|.
name|subject
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|echo
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|text
operator|!=
literal|null
assert|;
name|log
operator|.
name|debug
argument_list|(
literal|"Echoing: {}"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|session
operator|.
name|send
argument_list|(
operator|new
name|EchoMessage
argument_list|(
name|text
argument_list|)
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|openShell
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Opening remote shell"
argument_list|)
expr_stmt|;
name|Message
name|resp
init|=
name|session
operator|.
name|request
argument_list|(
operator|new
name|OpenShellMessage
argument_list|()
argument_list|)
decl_stmt|;
comment|//
comment|// TODO: Need some context from the response
comment|//
comment|// log.debug("Response: {}", resp);
block|}
specifier|public
name|void
name|closeShell
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Closing remote shell"
argument_list|)
expr_stmt|;
name|Message
name|resp
init|=
name|session
operator|.
name|request
argument_list|(
operator|new
name|CloseShellMessage
argument_list|()
argument_list|)
decl_stmt|;
comment|//
comment|// TODO: Need some context from the response
comment|//
comment|// log.debug("Response: {}", resp);
block|}
specifier|private
name|Object
name|doExecute
parameter_list|(
specifier|final
name|ExecuteMessage
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|msg
operator|!=
literal|null
assert|;
name|ExecuteMessage
operator|.
name|Result
name|result
init|=
operator|(
name|ExecuteMessage
operator|.
name|Result
operator|)
name|session
operator|.
name|request
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// Handle result notifications
if|if
condition|(
name|result
operator|instanceof
name|ExecuteMessage
operator|.
name|Notification
condition|)
block|{
name|ExecuteMessage
operator|.
name|Notification
name|n
init|=
operator|(
name|ExecuteMessage
operator|.
name|Notification
operator|)
name|result
decl_stmt|;
throw|throw
name|n
operator|.
name|getNotification
argument_list|()
throw|;
block|}
comment|// Handle result faults
if|if
condition|(
name|result
operator|instanceof
name|ExecuteMessage
operator|.
name|Fault
condition|)
block|{
name|ExecuteMessage
operator|.
name|Fault
name|fault
init|=
operator|(
name|ExecuteMessage
operator|.
name|Fault
operator|)
name|result
decl_stmt|;
throw|throw
operator|new
name|RemoteExecuteException
argument_list|(
name|fault
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
name|Object
name|rv
init|=
name|result
operator|.
name|getResult
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Command result: {}"
argument_list|,
name|rv
argument_list|)
expr_stmt|;
return|return
name|rv
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|String
name|line
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|line
operator|!=
literal|null
assert|;
return|return
name|doExecute
argument_list|(
operator|new
name|ExecuteMessage
argument_list|(
name|line
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|args
operator|!=
literal|null
assert|;
return|return
name|doExecute
argument_list|(
operator|new
name|ExecuteMessage
argument_list|(
name|args
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|path
operator|!=
literal|null
assert|;
assert|assert
name|args
operator|!=
literal|null
assert|;
return|return
name|doExecute
argument_list|(
operator|new
name|ExecuteMessage
argument_list|(
name|path
argument_list|,
name|args
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|Object
index|[]
index|[]
name|cmds
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|cmds
operator|!=
literal|null
assert|;
return|return
name|doExecute
argument_list|(
operator|new
name|ExecuteMessage
argument_list|(
name|cmds
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|onSessionClosed
parameter_list|()
block|{
comment|// nothing
block|}
comment|//
comment|// IO Handler
comment|//
specifier|private
class|class
name|Handler
extends|extends
name|DemuxingIoHandler
block|{
specifier|public
name|Handler
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Complain if we don't have any handlers
if|if
condition|(
name|handlers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Error
argument_list|(
literal|"No message handlers were discovered"
argument_list|)
throw|;
block|}
for|for
control|(
name|ClientMessageHandler
name|handler
range|:
name|handlers
control|)
block|{
name|register
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|MessageHandler
name|handler
parameter_list|)
block|{
assert|assert
name|handler
operator|!=
literal|null
assert|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|handler
operator|.
name|getType
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Registering handler: {} -> {}"
argument_list|,
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
comment|// noinspection unchecked
name|addMessageHandler
argument_list|(
name|type
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sessionOpened
parameter_list|(
specifier|final
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|session
operator|!=
literal|null
assert|;
comment|// Install the session context
name|ClientSessionContext
name|context
init|=
name|ClientSessionContext
operator|.
name|BINDER
operator|.
name|bind
argument_list|(
name|session
argument_list|,
operator|new
name|ClientSessionContext
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created session context: {}"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sessionClosed
parameter_list|(
specifier|final
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
assert|assert
name|session
operator|!=
literal|null
assert|;
name|ClientSessionContext
name|context
init|=
name|ClientSessionContext
operator|.
name|BINDER
operator|.
name|unbind
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Removed session context: {}"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|onSessionClosed
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

