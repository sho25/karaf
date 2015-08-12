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
name|syncope
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|AuthScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|auth
operator|.
name|UsernamePasswordCredentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|CloseableHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|DefaultHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
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
name|boot
operator|.
name|principal
operator|.
name|UserPrincipal
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
name|AbstractKarafLoginModule
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
name|*
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
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Karaf login module which uses Apache Syncope backend.  */
end_comment

begin_class
specifier|public
class|class
name|SyncopeLoginModule
extends|extends
name|AbstractKarafLoginModule
block|{
specifier|private
specifier|final
specifier|static
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SyncopeLoginModule
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ADDRESS
init|=
literal|"address"
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|String
name|ADMIN_USER
init|=
literal|"admin.user"
decl_stmt|;
comment|// for the backing engine
specifier|public
specifier|final
specifier|static
name|String
name|ADMIN_PASSWORD
init|=
literal|"admin.password"
decl_stmt|;
comment|// for the backing engine
specifier|private
name|String
name|address
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
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sharedState
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|super
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
name|callbackHandler
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|address
operator|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|ADDRESS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
name|Callback
index|[]
name|callbacks
init|=
operator|new
name|Callback
index|[
literal|2
index|]
decl_stmt|;
name|callbacks
index|[
literal|0
index|]
operator|=
operator|new
name|NameCallback
argument_list|(
literal|"Username: "
argument_list|)
expr_stmt|;
name|callbacks
index|[
literal|1
index|]
operator|=
operator|new
name|PasswordCallback
argument_list|(
literal|"Password: "
argument_list|,
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|callbackHandler
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioException
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|ioException
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCallbackException
name|unsupportedCallbackException
parameter_list|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
name|unsupportedCallbackException
operator|.
name|getMessage
argument_list|()
operator|+
literal|" not available to obtain information from user."
argument_list|)
throw|;
block|}
name|user
operator|=
operator|(
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
literal|0
index|]
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|char
index|[]
name|tmpPassword
init|=
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|getPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmpPassword
operator|==
literal|null
condition|)
block|{
name|tmpPassword
operator|=
operator|new
name|char
index|[
literal|0
index|]
expr_stmt|;
block|}
name|String
name|password
init|=
operator|new
name|String
argument_list|(
name|tmpPassword
argument_list|)
decl_stmt|;
name|principals
operator|=
operator|new
name|HashSet
argument_list|<
name|Principal
argument_list|>
argument_list|()
expr_stmt|;
comment|// authenticate the user on Syncope
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Authenticate user {} on Syncope located {}"
argument_list|,
name|user
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|DefaultHttpClient
name|client
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
name|Credentials
name|creds
init|=
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|user
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|client
operator|.
name|getCredentialsProvider
argument_list|()
operator|.
name|setCredentials
argument_list|(
name|AuthScope
operator|.
name|ANY
argument_list|,
name|creds
argument_list|)
expr_stmt|;
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|address
operator|+
literal|"/users/self"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Syncope HTTP response status code: {}"
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
operator|!=
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"User {} not authenticated"
argument_list|,
name|user
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"User {} authenticated"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Populating principals with user"
argument_list|)
expr_stmt|;
name|principals
operator|.
name|add
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Retrieving user {} roles"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|roles
operator|=
name|extractingRoles
argument_list|(
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"User {} authentication failed"
argument_list|,
name|user
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"User "
operator|+
name|user
operator|+
literal|" authentication failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Populating principals with roles"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|role
range|:
name|roles
control|)
block|{
name|principals
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|role
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Extract the user roles from the Syncope entity response.      *      * @param response the HTTP response from Syncope.      * @return the list of user roles.      * @throws Exception in case of extraction failure.      */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|extractingRoles
parameter_list|(
name|String
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
operator|&&
operator|!
name|response
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// extract the<memberships> element if it exists
name|int
name|index
init|=
name|response
operator|.
name|indexOf
argument_list|(
literal|"<memberships>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|response
operator|=
name|response
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|"<memberships>"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|index
operator|=
name|response
operator|.
name|indexOf
argument_list|(
literal|"</memberships>"
argument_list|)
expr_stmt|;
name|response
operator|=
name|response
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
comment|// looking for the roleName elements
name|index
operator|=
name|response
operator|.
name|indexOf
argument_list|(
literal|"<roleName>"
argument_list|)
expr_stmt|;
while|while
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|response
operator|=
name|response
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|"<roleName>"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|end
init|=
name|response
operator|.
name|indexOf
argument_list|(
literal|"</roleName>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|==
operator|-
literal|1
condition|)
block|{
name|index
operator|=
operator|-
literal|1
expr_stmt|;
block|}
name|String
name|role
init|=
name|response
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|end
argument_list|)
decl_stmt|;
name|roles
operator|.
name|add
argument_list|(
name|role
argument_list|)
expr_stmt|;
name|response
operator|=
name|response
operator|.
name|substring
argument_list|(
name|end
operator|+
literal|"</roleName>"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|index
operator|=
name|response
operator|.
name|indexOf
argument_list|(
literal|"<roleName>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|roles
return|;
block|}
specifier|public
name|boolean
name|abort
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|removeAll
argument_list|(
name|principals
argument_list|)
expr_stmt|;
name|principals
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

