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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|NamedFactory
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
name|UserAuth
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
name|UserAuthKeyboardInteractive
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
name|UserAuthPassword
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
name|UserAuthPublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
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
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/**  *<p>A factory for user authentication factories to set on  * {@link org.apache.sshd.SshServer#setUserAuthFactories(java.util.List)} based on a  * comma-separated list of authentication methods.</p>  *  *<p>Currently, the following methods are supported:</p>  *<ul>  *<li><code>password</code>  *          Password authentication against a given JAAS domain.</p></li>  *<li><code>publickey</code>  *          Public key authentication against an OpenSSH<code>authorized_keys</code> file.</p></li>  *</ul>  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|UserAuthFactoriesFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_METHOD
init|=
literal|"password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PUBLICKEY_METHOD
init|=
literal|"publickey"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEYBOARD_INTERACTIVE_METHOD
init|=
literal|"keyboard-interactive"
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|methodSet
decl_stmt|;
specifier|private
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|UserAuth
argument_list|>
argument_list|>
name|factories
decl_stmt|;
specifier|public
name|void
name|setAuthMethods
parameter_list|(
name|String
name|methods
parameter_list|)
block|{
name|this
operator|.
name|methodSet
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|factories
operator|=
operator|new
name|ArrayList
argument_list|<
name|NamedFactory
argument_list|<
name|UserAuth
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|String
index|[]
name|ams
init|=
name|methods
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|am
range|:
name|ams
control|)
block|{
if|if
condition|(
name|PASSWORD_METHOD
operator|.
name|equals
argument_list|(
name|am
argument_list|)
condition|)
block|{
name|this
operator|.
name|factories
operator|.
name|add
argument_list|(
operator|new
name|UserAuthPassword
operator|.
name|Factory
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|KEYBOARD_INTERACTIVE_METHOD
operator|.
name|equals
argument_list|(
name|am
argument_list|)
condition|)
block|{
name|this
operator|.
name|factories
operator|.
name|add
argument_list|(
operator|new
name|UserAuthKeyboardInteractive
operator|.
name|Factory
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PUBLICKEY_METHOD
operator|.
name|equals
argument_list|(
name|am
argument_list|)
condition|)
block|{
name|this
operator|.
name|factories
operator|.
name|add
argument_list|(
operator|new
name|UserAuthPublicKey
operator|.
name|Factory
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid authentication method "
operator|+
name|am
operator|+
literal|" specified"
argument_list|)
throw|;
block|}
name|this
operator|.
name|methodSet
operator|.
name|add
argument_list|(
name|am
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|UserAuth
argument_list|>
argument_list|>
name|getFactories
parameter_list|()
block|{
return|return
name|factories
return|;
block|}
specifier|public
name|boolean
name|isPublickeyEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|methodSet
operator|.
name|contains
argument_list|(
name|PUBLICKEY_METHOD
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isPasswordEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|methodSet
operator|.
name|contains
argument_list|(
name|PASSWORD_METHOD
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isKeyboardInteractive
parameter_list|()
block|{
return|return
name|this
operator|.
name|methodSet
operator|.
name|contains
argument_list|(
name|KEYBOARD_INTERACTIVE_METHOD
argument_list|)
return|;
block|}
block|}
end_class

end_unit

