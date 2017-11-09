begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|security
operator|.
name|Principal
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
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
name|callback
operator|.
name|NameCallback
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
name|PasswordCallback
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
name|UnsupportedCallbackException
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
name|FailedLoginException
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
name|karaf
operator|.
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|ClientPrincipal
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|RolePrincipal
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
name|jaas
operator|.
name|modules
operator|.
name|publickey
operator|.
name|PublickeyCallback
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
name|common
operator|.
name|session
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
name|sshd
operator|.
name|server
operator|.
name|auth
operator|.
name|password
operator|.
name|PasswordAuthenticator
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
name|server
operator|.
name|auth
operator|.
name|pubkey
operator|.
name|PublickeyAuthenticator
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
name|server
operator|.
name|session
operator|.
name|ServerSession
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

begin_class
specifier|public
class|class
name|KarafJaasAuthenticator
implements|implements
name|PasswordAuthenticator
implements|,
name|PublickeyAuthenticator
block|{
specifier|public
specifier|static
specifier|final
name|Session
operator|.
name|AttributeKey
argument_list|<
name|Subject
argument_list|>
name|SUBJECT_ATTRIBUTE_KEY
init|=
operator|new
name|Session
operator|.
name|AttributeKey
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KarafJaasAuthenticator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|realm
decl_stmt|;
specifier|private
name|String
name|role
decl_stmt|;
specifier|public
name|KarafJaasAuthenticator
parameter_list|(
name|String
name|realm
parameter_list|,
name|String
name|role
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
name|this
operator|.
name|role
operator|=
name|role
expr_stmt|;
block|}
specifier|public
name|boolean
name|authenticate
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|String
name|password
parameter_list|,
specifier|final
name|ServerSession
name|session
parameter_list|)
block|{
name|CallbackHandler
name|callbackHandler
init|=
name|callbacks
lambda|->
block|{
for|for
control|(
name|Callback
name|callback
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|callback
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|callback
operator|)
operator|.
name|setName
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|callback
operator|instanceof
name|PasswordCallback
condition|)
block|{
operator|(
operator|(
name|PasswordCallback
operator|)
name|callback
operator|)
operator|.
name|setPassword
argument_list|(
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callback
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
return|return
name|doLogin
argument_list|(
name|session
argument_list|,
name|callbackHandler
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|authenticate
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|PublicKey
name|key
parameter_list|,
specifier|final
name|ServerSession
name|session
parameter_list|)
block|{
name|CallbackHandler
name|callbackHandler
init|=
name|callbacks
lambda|->
block|{
for|for
control|(
name|Callback
name|callback
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|callback
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|callback
operator|)
operator|.
name|setName
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|callback
operator|instanceof
name|PublickeyCallback
condition|)
block|{
operator|(
operator|(
name|PublickeyCallback
operator|)
name|callback
operator|)
operator|.
name|setPublicKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callback
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
return|return
name|doLogin
argument_list|(
name|session
argument_list|,
name|callbackHandler
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|doLogin
parameter_list|(
specifier|final
name|ServerSession
name|session
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|)
block|{
try|try
block|{
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ClientPrincipal
argument_list|(
literal|"ssh"
argument_list|,
name|session
operator|.
name|getClientAddress
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|LoginContext
name|loginContext
init|=
operator|new
name|LoginContext
argument_list|(
name|realm
argument_list|,
name|subject
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|loginContext
operator|.
name|login
argument_list|()
expr_stmt|;
name|assertRolePresent
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|SUBJECT_ATTRIBUTE_KEY
argument_list|,
name|subject
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"User authentication failed with "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|void
name|assertRolePresent
parameter_list|(
name|Subject
name|subject
parameter_list|)
throws|throws
name|FailedLoginException
block|{
name|boolean
name|hasCorrectRole
init|=
name|role
operator|==
literal|null
operator|||
name|role
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|int
name|roleCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Principal
name|principal
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|principal
operator|instanceof
name|RolePrincipal
condition|)
block|{
if|if
condition|(
operator|!
name|hasCorrectRole
condition|)
block|{
name|hasCorrectRole
operator|=
name|role
operator|.
name|equals
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|roleCount
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|roleCount
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"User doesn't have role defined"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|hasCorrectRole
condition|)
block|{
throw|throw
operator|new
name|FailedLoginException
argument_list|(
literal|"User doesn't have the required role "
operator|+
name|role
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

