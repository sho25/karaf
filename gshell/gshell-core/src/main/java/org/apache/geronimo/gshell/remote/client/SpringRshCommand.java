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
name|client
package|;
end_package

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
name|ArrayList
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
name|jline
operator|.
name|Terminal
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
name|ExitNotification
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
name|support
operator|.
name|OsgiCommandSupport
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
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
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
name|console
operator|.
name|PromptReader
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
name|proxy
operator|.
name|RemoteShellProxy
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
name|whisper
operator|.
name|transport
operator|.
name|TransportFactoryLocator
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Dec 6, 2007  * Time: 8:38:02 AM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"gshell-remote:rsh"
argument_list|,
name|description
operator|=
literal|"Connect to a remote GShell server"
argument_list|)
specifier|public
class|class
name|SpringRshCommand
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
block|{
literal|"--bind"
block|}
argument_list|,
name|metaVar
operator|=
literal|"URI"
argument_list|,
name|description
operator|=
literal|"Bind local address to URI"
argument_list|)
specifier|private
name|URI
name|local
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
name|metaVar
operator|=
literal|"USERNAME"
argument_list|,
name|description
operator|=
literal|"Remote user name"
argument_list|)
specifier|private
name|String
name|username
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
name|metaVar
operator|=
literal|"PASSWORD"
argument_list|,
name|description
operator|=
literal|"Remote user password"
argument_list|)
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|metaVar
operator|=
literal|"URI"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|index
operator|=
literal|0
argument_list|,
name|description
operator|=
literal|"Connect to remote server at URI"
argument_list|)
specifier|private
name|URI
name|remote
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|metaVar
operator|=
literal|"COMMAND"
argument_list|,
name|index
operator|=
literal|1
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Execute COMMAND in remote shell"
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|command
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Terminal
name|terminal
decl_stmt|;
specifier|private
name|CryptoContext
name|crypto
decl_stmt|;
specifier|private
name|TransportFactoryLocator
name|locator
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ClientMessageHandler
argument_list|>
name|handlers
decl_stmt|;
specifier|public
name|SpringRshCommand
parameter_list|(
specifier|final
name|Terminal
name|terminal
parameter_list|,
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
name|terminal
operator|=
name|terminal
expr_stmt|;
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
specifier|protected
name|OsgiCommandSupport
name|createCommand
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringRshCommand
argument_list|(
name|terminal
argument_list|,
name|crypto
argument_list|,
name|locator
argument_list|,
name|handlers
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|io
operator|.
name|info
argument_list|(
literal|"Connecting to: {}"
argument_list|,
name|remote
argument_list|)
expr_stmt|;
name|RshClient
name|client
init|=
operator|new
name|RshClient
argument_list|(
name|crypto
argument_list|,
name|locator
argument_list|,
name|handlers
argument_list|)
decl_stmt|;
name|client
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|PromptReader
name|prompter
init|=
operator|new
name|PromptReader
argument_list|(
name|terminal
argument_list|,
name|io
argument_list|)
decl_stmt|;
name|prompter
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|client
operator|.
name|connect
argument_list|(
name|remote
argument_list|,
name|local
argument_list|)
expr_stmt|;
name|io
operator|.
name|info
argument_list|(
literal|"Connected"
argument_list|)
expr_stmt|;
comment|// If the username/password was not configured via cli, then prompt the user for the values
if|if
condition|(
name|username
operator|==
literal|null
operator|||
name|password
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
name|prompter
operator|.
name|readLine
argument_list|(
literal|"Username: "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
name|prompter
operator|.
name|readPassword
argument_list|(
literal|"Password: "
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// TODO: Handle null inputs...
comment|//
block|}
name|client
operator|.
name|login
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
comment|// client.echo("HELLO");
comment|// Thread.sleep(1 * 1000);
name|RemoteShellProxy
name|shell
init|=
operator|new
name|RemoteShellProxy
argument_list|(
name|client
argument_list|,
name|io
argument_list|,
name|terminal
argument_list|)
decl_stmt|;
name|Object
name|rv
init|=
name|SUCCESS
decl_stmt|;
try|try
block|{
name|shell
operator|.
name|run
argument_list|(
name|command
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExitNotification
name|n
parameter_list|)
block|{
comment|// Make sure that we catch this notification, so that our parent shell doesn't exit when the remote shell does
name|rv
operator|=
name|n
operator|.
name|code
expr_stmt|;
block|}
name|shell
operator|.
name|close
argument_list|()
expr_stmt|;
name|io
operator|.
name|verbose
argument_list|(
literal|"Disconnecting"
argument_list|)
expr_stmt|;
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
name|io
operator|.
name|verbose
argument_list|(
literal|"Disconnected"
argument_list|)
expr_stmt|;
return|return
name|rv
return|;
block|}
block|}
end_class

end_unit

