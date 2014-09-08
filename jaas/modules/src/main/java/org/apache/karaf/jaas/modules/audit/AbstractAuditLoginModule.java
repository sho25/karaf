begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|audit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|spi
operator|.
name|LoginModule
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAuditLoginModule
implements|implements
name|LoginModule
block|{
enum|enum
name|Action
block|{
name|ATTEMPT
block|,
name|SUCCESS
block|,
name|FAILURE
block|,
name|LOGOUT
block|}
specifier|protected
name|Subject
name|subject
decl_stmt|;
specifier|private
name|CallbackHandler
name|handler
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|boolean
name|enabled
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|Map
name|sharedState
parameter_list|,
name|Map
name|options
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
name|enabled
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|=
name|callbackHandler
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|audit
parameter_list|(
name|Action
name|action
parameter_list|,
name|String
name|user
parameter_list|)
function_decl|;
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
name|NameCallback
name|user
init|=
operator|new
name|NameCallback
argument_list|(
literal|"User name:"
argument_list|)
decl_stmt|;
name|Callback
index|[]
name|callbacks
init|=
operator|new
name|Callback
index|[]
block|{
name|user
block|}
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|LoginException
operator|)
operator|new
name|LoginException
argument_list|(
literal|"Unable to process callback: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|callbacks
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Number of callbacks changed by server!"
argument_list|)
throw|;
block|}
name|user
operator|=
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
literal|0
index|]
expr_stmt|;
name|username
operator|=
name|user
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|enabled
operator|&&
name|username
operator|!=
literal|null
condition|)
block|{
name|audit
argument_list|(
name|Action
operator|.
name|ATTEMPT
argument_list|,
name|username
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|commit
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|enabled
operator|&&
name|username
operator|!=
literal|null
condition|)
block|{
name|audit
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|username
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|abort
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|enabled
operator|&&
name|username
operator|!=
literal|null
condition|)
block|{
comment|//work around initial "fake" login
name|audit
argument_list|(
name|Action
operator|.
name|FAILURE
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|username
operator|=
literal|null
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
if|if
condition|(
name|enabled
operator|&&
name|username
operator|!=
literal|null
condition|)
block|{
name|audit
argument_list|(
name|Action
operator|.
name|LOGOUT
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|username
operator|=
literal|null
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

