begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|command
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
name|security
operator|.
name|PrivilegedExceptionAction
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Action
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|api
operator|.
name|console
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
name|karaf
operator|.
name|util
operator|.
name|jaas
operator|.
name|JaasHelper
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jaas"
argument_list|,
name|name
operator|=
literal|"sudo"
argument_list|,
name|description
operator|=
literal|"Execute a command as another user"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|SudoCommand
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--realm"
argument_list|)
name|String
name|realm
init|=
literal|"karaf"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--user"
argument_list|)
name|String
name|user
init|=
literal|"karaf"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|command
decl_stmt|;
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
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
name|user
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
name|String
name|password
init|=
name|SudoCommand
operator|.
name|this
operator|.
name|session
operator|.
name|readLine
argument_list|(
literal|"Password: "
argument_list|,
literal|'*'
argument_list|)
decl_stmt|;
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
block|}
argument_list|)
decl_stmt|;
name|loginContext
operator|.
name|login
argument_list|()
expr_stmt|;
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|command
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|JaasHelper
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|run
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|session
operator|.
name|execute
argument_list|(
name|sb
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|loginContext
operator|.
name|logout
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

