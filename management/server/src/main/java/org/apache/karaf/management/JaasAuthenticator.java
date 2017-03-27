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
name|management
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
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXAuthenticator
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

begin_class
specifier|public
class|class
name|JaasAuthenticator
implements|implements
name|JMXAuthenticator
block|{
specifier|private
name|String
name|realm
decl_stmt|;
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|realm
return|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
specifier|public
name|Subject
name|authenticate
parameter_list|(
name|Object
name|credentials
parameter_list|)
throws|throws
name|SecurityException
block|{
if|if
condition|(
operator|!
operator|(
name|credentials
operator|instanceof
name|String
index|[]
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected String[2], got "
operator|+
operator|(
name|credentials
operator|!=
literal|null
condition|?
name|credentials
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
operator|)
argument_list|)
throw|;
block|}
specifier|final
name|String
index|[]
name|params
init|=
operator|(
name|String
index|[]
operator|)
name|credentials
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected String[2] but length was "
operator|+
name|params
operator|.
name|length
argument_list|)
throw|;
block|}
try|try
block|{
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
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
operator|new
name|CallbackHandler
argument_list|()
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|NameCallback
condition|)
block|{
operator|(
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
name|i
index|]
operator|)
operator|.
name|setName
argument_list|(
name|params
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|PasswordCallback
condition|)
block|{
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
name|i
index|]
operator|)
operator|.
name|setPassword
argument_list|(
operator|(
name|params
index|[
literal|1
index|]
operator|.
name|toCharArray
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callbacks
index|[
name|i
index|]
argument_list|)
throw|;
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|loginContext
operator|.
name|login
argument_list|()
expr_stmt|;
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
return|return
name|subject
return|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Authentication failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

