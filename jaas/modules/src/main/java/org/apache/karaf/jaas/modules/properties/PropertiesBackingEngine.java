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
name|properties
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
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
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
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
name|encryption
operator|.
name|EncryptionSupport
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

begin_comment
comment|/**  * @author iocanel  */
end_comment

begin_class
specifier|public
class|class
name|PropertiesBackingEngine
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
name|PropertiesBackingEngine
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Properties
name|users
decl_stmt|;
specifier|private
name|EncryptionSupport
name|encryptionSupport
decl_stmt|;
comment|/**      * Constructor      *      * @param users      */
specifier|public
name|PropertiesBackingEngine
parameter_list|(
name|Properties
name|users
parameter_list|)
block|{
name|this
operator|.
name|users
operator|=
name|users
expr_stmt|;
block|}
specifier|public
name|PropertiesBackingEngine
parameter_list|(
name|Properties
name|users
parameter_list|,
name|EncryptionSupport
name|encryptionSupport
parameter_list|)
block|{
name|this
operator|.
name|users
operator|=
name|users
expr_stmt|;
name|this
operator|.
name|encryptionSupport
operator|=
name|encryptionSupport
expr_stmt|;
block|}
comment|/**      * Add a user.      *      * @param username      * @param password      */
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
name|String
index|[]
name|infos
init|=
literal|null
decl_stmt|;
name|StringBuffer
name|userInfoBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|newPassword
init|=
name|password
decl_stmt|;
comment|//If encryption support is enabled, encrypt password
if|if
condition|(
name|encryptionSupport
operator|!=
literal|null
operator|&&
name|encryptionSupport
operator|.
name|getEncryption
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|newPassword
operator|=
name|encryptionSupport
operator|.
name|getEncryption
argument_list|()
operator|.
name|encryptPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
name|String
name|userInfos
init|=
name|users
operator|.
name|get
argument_list|(
name|username
argument_list|)
decl_stmt|;
comment|//If user already exists, update password
if|if
condition|(
name|userInfos
operator|!=
literal|null
operator|&&
name|userInfos
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|infos
operator|=
name|userInfos
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|userInfoBuffer
operator|.
name|append
argument_list|(
name|newPassword
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|infos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|userInfoBuffer
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|userInfoBuffer
operator|.
name|append
argument_list|(
name|infos
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|String
name|newUserInfo
init|=
name|userInfoBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|users
operator|.
name|put
argument_list|(
name|username
argument_list|,
name|newUserInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|users
operator|.
name|put
argument_list|(
name|username
argument_list|,
name|newPassword
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|users
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Cannot update users file,"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Delete a User.      *      * @param username      */
specifier|public
name|void
name|deleteUser
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|users
operator|.
name|remove
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
comment|/**      * List Users      *      * @return      */
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
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|UserPrincipal
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|userNames
range|:
name|users
operator|.
name|keySet
argument_list|()
control|)
block|{
name|UserPrincipal
name|userPrincipal
init|=
operator|new
name|UserPrincipal
argument_list|(
name|userNames
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|userPrincipal
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * List the Roles of the {@param user}      *      * @param user      * @return      */
specifier|public
name|List
argument_list|<
name|RolePrincipal
argument_list|>
name|listRoles
parameter_list|(
name|UserPrincipal
name|user
parameter_list|)
block|{
name|List
argument_list|<
name|RolePrincipal
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|RolePrincipal
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|userInfo
init|=
name|users
operator|.
name|get
argument_list|(
name|user
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|infos
init|=
name|userInfo
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|infos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|infos
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Add a role to a User.      *      * @param username      * @param role      */
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
name|String
name|userInfos
init|=
name|users
operator|.
name|get
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|userInfos
operator|!=
literal|null
condition|)
block|{
name|String
name|newUserInfos
init|=
name|userInfos
operator|+
literal|","
operator|+
name|role
decl_stmt|;
name|users
operator|.
name|put
argument_list|(
name|username
argument_list|,
name|newUserInfos
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|users
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Cannot update users file,"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Delete a Role form User.      *      * @param username      * @param role      */
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
name|String
index|[]
name|infos
init|=
literal|null
decl_stmt|;
name|StringBuffer
name|userInfoBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|userInfos
init|=
name|users
operator|.
name|get
argument_list|(
name|username
argument_list|)
decl_stmt|;
comment|//If user already exists, remove the role
if|if
condition|(
name|userInfos
operator|!=
literal|null
operator|&&
name|userInfos
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|infos
operator|=
name|userInfos
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|String
name|password
init|=
name|infos
index|[
literal|0
index|]
decl_stmt|;
name|userInfoBuffer
operator|.
name|append
argument_list|(
name|password
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|infos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|infos
index|[
name|i
index|]
operator|!=
literal|null
operator|&&
operator|!
name|infos
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|role
argument_list|)
condition|)
block|{
name|userInfoBuffer
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|userInfoBuffer
operator|.
name|append
argument_list|(
name|infos
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|newUserInfo
init|=
name|userInfoBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|users
operator|.
name|put
argument_list|(
name|username
argument_list|,
name|newUserInfo
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|users
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Cannot update users file,"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

