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
name|jaas
operator|.
name|modules
operator|.
name|properties
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

begin_class
class|class
name|NamePasswordHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|public
name|NamePasswordHandler
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
annotation|@
name|Override
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
name|NameCallback
name|nameCallback
init|=
operator|(
name|NameCallback
operator|)
name|callback
decl_stmt|;
name|nameCallback
operator|.
name|setName
argument_list|(
name|name
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
name|PasswordCallback
name|passwordCallback
init|=
operator|(
name|PasswordCallback
operator|)
name|callback
decl_stmt|;
name|passwordCallback
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
block|}
block|}
block|}
end_class

end_unit

