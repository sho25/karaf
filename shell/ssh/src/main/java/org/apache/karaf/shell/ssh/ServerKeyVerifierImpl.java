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
name|karaf
operator|.
name|shell
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|ClientSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|ServerKeyVerifier
import|;
end_import

begin_class
specifier|public
class|class
name|ServerKeyVerifierImpl
implements|implements
name|ServerKeyVerifier
block|{
specifier|private
specifier|final
name|KnownHostsManager
name|knownHostsManager
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|quiet
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|keyChangedMessage
init|=
literal|" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n"
operator|+
literal|" @    WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED!      @ \n"
operator|+
literal|" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n"
operator|+
literal|"IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!\n"
operator|+
literal|"Someone could be eavesdropping on you right now (man-in-the-middle attack)!\n"
operator|+
literal|"It is also possible that the RSA host key has just been changed.\n"
operator|+
literal|"Please contact your system administrator.\n"
operator|+
literal|"Add correct host key in "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.sshkaraf/known_hosts to get rid of this message.\n"
operator|+
literal|"Offending key in "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.sshkaraf/known_hosts\n"
operator|+
literal|"RSA host key has changed and you have requested strict checking.\n"
operator|+
literal|"Host key verification failed."
decl_stmt|;
specifier|public
name|ServerKeyVerifierImpl
parameter_list|(
name|KnownHostsManager
name|knownHostsManager
parameter_list|,
name|boolean
name|quiet
parameter_list|)
block|{
name|this
operator|.
name|knownHostsManager
operator|=
name|knownHostsManager
expr_stmt|;
name|this
operator|.
name|quiet
operator|=
name|quiet
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|verifyServerKey
parameter_list|(
name|ClientSession
name|sshClientSession
parameter_list|,
name|SocketAddress
name|remoteAddress
parameter_list|,
name|PublicKey
name|serverKey
parameter_list|)
block|{
name|PublicKey
name|knownKey
decl_stmt|;
try|try
block|{
name|knownKey
operator|=
name|knownHostsManager
operator|.
name|getKnownKey
argument_list|(
name|remoteAddress
argument_list|,
name|serverKey
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidKeySpecException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Invalid key stored for host "
operator|+
name|remoteAddress
operator|+
literal|". Terminating session."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|knownKey
operator|==
literal|null
condition|)
block|{
name|boolean
name|confirm
decl_stmt|;
if|if
condition|(
operator|!
name|quiet
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connecting to unknown server. Add this server to known hosts ? (y/n)"
argument_list|)
expr_stmt|;
name|confirm
operator|=
name|getConfirmation
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Connecting to unknown server. Automatically adding to known hosts."
argument_list|)
expr_stmt|;
name|confirm
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|confirm
condition|)
block|{
name|knownHostsManager
operator|.
name|storeKeyForHost
argument_list|(
name|remoteAddress
argument_list|,
name|serverKey
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Storing the server key in known_hosts."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Aborting connection"
argument_list|)
expr_stmt|;
block|}
return|return
name|confirm
return|;
block|}
name|boolean
name|verifed
init|=
operator|(
name|knownKey
operator|.
name|equals
argument_list|(
name|serverKey
argument_list|)
operator|)
decl_stmt|;
if|if
condition|(
operator|!
name|verifed
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Server key for host "
operator|+
name|remoteAddress
operator|+
literal|" does not match the stored key !! Terminating session."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|keyChangedMessage
argument_list|)
expr_stmt|;
block|}
return|return
name|verifed
return|;
block|}
specifier|private
name|boolean
name|getConfirmation
parameter_list|()
block|{
name|int
name|ch
decl_stmt|;
try|try
block|{
do|do
block|{
name|ch
operator|=
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|ch
operator|!=
literal|'y'
operator|&&
name|ch
operator|!=
literal|'n'
condition|)
do|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|boolean
name|confirm
init|=
name|ch
operator|==
literal|'y'
decl_stmt|;
return|return
name|confirm
return|;
block|}
block|}
end_class

end_unit

