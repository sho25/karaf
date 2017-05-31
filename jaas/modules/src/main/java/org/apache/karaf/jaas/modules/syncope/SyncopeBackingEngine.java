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
name|HttpResponse
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
name|HttpDelete
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
name|client
operator|.
name|methods
operator|.
name|HttpPost
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
name|entity
operator|.
name|StringEntity
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
name|GroupPrincipal
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
name|BackingEngine
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
name|naming
operator|.
name|OperationNotSupportedException
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|SyncopeBackingEngine
implements|implements
name|BackingEngine
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SyncopeBackingEngine
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|address
decl_stmt|;
specifier|private
name|DefaultHttpClient
name|client
decl_stmt|;
specifier|public
name|SyncopeBackingEngine
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|adminUser
parameter_list|,
name|String
name|adminPassword
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
name|client
operator|=
operator|new
name|DefaultHttpClient
argument_list|()
expr_stmt|;
name|Credentials
name|creds
init|=
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|adminUser
argument_list|,
name|adminPassword
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
block|}
specifier|public
name|void
name|addUser
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|username
operator|.
name|startsWith
argument_list|(
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Group prefix "
operator|+
name|GROUP_PREFIX
operator|+
literal|" not permitted with Syncope backend"
argument_list|)
throw|;
block|}
name|HttpPost
name|request
init|=
operator|new
name|HttpPost
argument_list|(
name|address
operator|+
literal|"/users"
argument_list|)
decl_stmt|;
name|request
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|String
name|userTO
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
operator|+
literal|"<user>"
operator|+
literal|"<attributes>"
operator|+
literal|"<attribute><readonly>false</readonly><schema>fullname</schema><value>"
operator|+
name|username
operator|+
literal|"</value></attribute>"
operator|+
literal|"<attribute><readonly>false</readonly><schema>surname</schema><value>"
operator|+
name|username
operator|+
literal|"</value></attribute>"
operator|+
literal|"<attribute><readonly>false</readonly><schema>userId</schema><value>"
operator|+
name|username
operator|+
literal|"@karaf.apache.org</value></attribute>"
operator|+
literal|"</attributes>"
operator|+
literal|"<password>"
operator|+
name|password
operator|+
literal|"</password>"
operator|+
literal|"<username>"
operator|+
name|username
operator|+
literal|"</username>"
operator|+
literal|"</user>"
decl_stmt|;
try|try
block|{
name|StringEntity
name|entity
init|=
operator|new
name|StringEntity
argument_list|(
name|userTO
argument_list|)
decl_stmt|;
name|request
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|request
argument_list|)
decl_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Can't add user {}"
argument_list|,
name|username
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can't add user "
operator|+
name|username
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|deleteUser
parameter_list|(
name|String
name|username
parameter_list|)
block|{
if|if
condition|(
name|username
operator|.
name|startsWith
argument_list|(
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Group prefix "
operator|+
name|GROUP_PREFIX
operator|+
literal|" not permitted with Syncope backend"
argument_list|)
throw|;
block|}
name|HttpDelete
name|request
init|=
operator|new
name|HttpDelete
argument_list|(
name|address
operator|+
literal|"/users/"
operator|+
name|username
argument_list|)
decl_stmt|;
name|request
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|client
operator|.
name|execute
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Can't delete user {}"
argument_list|,
name|username
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can't delete user "
operator|+
name|username
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|UserPrincipal
argument_list|>
name|listUsers
parameter_list|()
block|{
name|List
argument_list|<
name|UserPrincipal
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|HttpGet
name|request
init|=
operator|new
name|HttpGet
argument_list|(
name|address
operator|+
literal|"/users"
argument_list|)
decl_stmt|;
name|request
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|String
name|responseTO
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseTO
operator|!=
literal|null
operator|&&
operator|!
name|responseTO
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// extracting the user
name|int
name|index
init|=
name|responseTO
operator|.
name|indexOf
argument_list|(
literal|"<username>"
argument_list|)
decl_stmt|;
while|while
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|responseTO
operator|=
name|responseTO
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|"<username>"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|end
init|=
name|responseTO
operator|.
name|indexOf
argument_list|(
literal|"</username>"
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
name|username
init|=
name|responseTO
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|end
argument_list|)
decl_stmt|;
name|users
operator|.
name|add
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
name|username
argument_list|)
argument_list|)
expr_stmt|;
name|responseTO
operator|=
name|responseTO
operator|.
name|substring
argument_list|(
name|end
operator|+
literal|"</username>"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|index
operator|=
name|responseTO
operator|.
name|indexOf
argument_list|(
literal|"<username>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error listing users"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|users
return|;
block|}
specifier|public
name|List
argument_list|<
name|RolePrincipal
argument_list|>
name|listRoles
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
name|List
argument_list|<
name|RolePrincipal
argument_list|>
name|roles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|HttpGet
name|request
init|=
operator|new
name|HttpGet
argument_list|(
name|address
operator|+
literal|"/users?username="
operator|+
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|request
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|String
name|responseTO
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseTO
operator|!=
literal|null
operator|&&
operator|!
name|responseTO
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|index
init|=
name|responseTO
operator|.
name|indexOf
argument_list|(
literal|"<roleName>"
argument_list|)
decl_stmt|;
while|while
condition|(
name|index
operator|!=
literal|1
condition|)
block|{
name|responseTO
operator|=
name|responseTO
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
name|responseTO
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
break|break;
block|}
name|String
name|role
init|=
name|responseTO
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
operator|new
name|RolePrincipal
argument_list|(
name|role
argument_list|)
argument_list|)
expr_stmt|;
name|responseTO
operator|=
name|responseTO
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
name|responseTO
operator|.
name|indexOf
argument_list|(
literal|"<roleName>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error listing roles"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|roles
return|;
block|}
specifier|public
name|void
name|addRole
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|role
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Roles management should be done on the Syncope side"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|deleteRole
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|role
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Roles management should be done on the Syncope side"
argument_list|)
throw|;
block|}
specifier|public
name|List
argument_list|<
name|GroupPrincipal
argument_list|>
name|listGroups
parameter_list|(
name|UserPrincipal
name|principal
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
specifier|public
name|void
name|addGroup
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|group
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Group management is not supported by Syncope backend"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|deleteGroup
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|group
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Group management is not supported by Syncope backend"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|addGroupRole
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|role
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Group management is not supported by Syncope backend"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|deleteGroupRole
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|role
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Group management is not supported by Syncope backend"
argument_list|)
throw|;
block|}
specifier|public
name|Map
argument_list|<
name|GroupPrincipal
argument_list|,
name|String
argument_list|>
name|listGroups
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Group management is not supported by Syncope backend"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|createGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Group management is not supported by Syncope backend"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

