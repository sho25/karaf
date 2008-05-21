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
name|geronimo
operator|.
name|gshell
operator|.
name|remote
operator|.
name|server
operator|.
name|handler
package|;
end_package

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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
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
name|Subject
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
name|LoginMessage
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
name|remote
operator|.
name|server
operator|.
name|timeout
operator|.
name|TimeoutManager
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
name|jaas
operator|.
name|Identity
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

begin_class
annotation|@
name|Component
argument_list|(
name|role
operator|=
name|ServerMessageHandler
operator|.
name|class
argument_list|,
name|hint
operator|=
literal|"login"
argument_list|)
specifier|public
class|class
name|SpringLoginHandler
extends|extends
name|ServerMessageHandlerSupport
argument_list|<
name|LoginMessage
argument_list|>
implements|implements
name|Initializable
block|{
annotation|@
name|Requirement
specifier|private
name|TimeoutManager
name|timeoutManager
decl_stmt|;
specifier|private
name|String
name|defaultRealm
init|=
literal|"BogusLogin"
decl_stmt|;
specifier|public
name|SpringLoginHandler
parameter_list|()
block|{
name|super
argument_list|(
name|LoginMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SpringLoginHandler
parameter_list|(
specifier|final
name|TimeoutManager
name|timeoutManager
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|timeoutManager
operator|=
name|timeoutManager
expr_stmt|;
block|}
specifier|public
name|SpringLoginHandler
parameter_list|(
specifier|final
name|TimeoutManager
name|timeoutManager
parameter_list|,
specifier|final
name|String
name|defaultRealm
parameter_list|)
block|{
name|this
argument_list|(
name|timeoutManager
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultRealm
operator|=
name|defaultRealm
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
literal|"server.login.conf"
argument_list|)
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|,
specifier|final
name|ServerSessionContext
name|context
parameter_list|,
specifier|final
name|LoginMessage
name|message
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Try to cancel the timeout task
if|if
condition|(
operator|!
name|timeoutManager
operator|.
name|cancelTimeout
argument_list|(
name|session
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Aborting login processing; timeout has triggered"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|realm
init|=
name|message
operator|.
name|getRealm
argument_list|()
decl_stmt|;
if|if
condition|(
name|realm
operator|==
literal|null
condition|)
block|{
name|realm
operator|=
name|defaultRealm
expr_stmt|;
block|}
name|String
name|username
init|=
name|message
operator|.
name|getUsername
argument_list|()
decl_stmt|;
name|char
index|[]
name|password
init|=
name|message
operator|.
name|getPassword
argument_list|()
decl_stmt|;
try|try
block|{
name|LoginContext
name|loginContext
init|=
operator|new
name|LoginContext
argument_list|(
name|realm
argument_list|,
operator|new
name|UsernamePasswordCallbackHandler
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
decl_stmt|;
name|loginContext
operator|.
name|login
argument_list|()
expr_stmt|;
name|Subject
name|subject
init|=
name|loginContext
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|context
operator|.
name|identity
operator|=
operator|new
name|Identity
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Username: {}, Identity: {}"
argument_list|,
name|context
operator|.
name|getUsername
argument_list|()
argument_list|,
name|context
operator|.
name|identity
argument_list|)
expr_stmt|;
name|LoginMessage
operator|.
name|Success
name|reply
init|=
operator|new
name|LoginMessage
operator|.
name|Success
argument_list|(
name|context
operator|.
name|identity
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setCorrelationId
argument_list|(
name|message
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|send
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|String
name|reason
init|=
name|e
operator|.
name|toString
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Login failed for user: {}, cause: {}"
argument_list|,
name|username
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|LoginMessage
operator|.
name|Failure
name|reply
init|=
operator|new
name|LoginMessage
operator|.
name|Failure
argument_list|(
name|reason
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setCorrelationId
argument_list|(
name|message
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|send
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

