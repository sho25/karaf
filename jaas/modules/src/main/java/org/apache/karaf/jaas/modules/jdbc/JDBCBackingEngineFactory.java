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
name|jdbc
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
name|BackingEngineFactory
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
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
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
name|sql
operator|.
name|DataSource
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

begin_comment
comment|/**  * @author iocanel  */
end_comment

begin_class
specifier|public
class|class
name|JDBCBackingEngineFactory
implements|implements
name|BackingEngineFactory
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
name|JDBCBackingEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Build a Backing engine for the JDBCLoginModule.      *      * @param options      * @return      */
specifier|public
name|BackingEngine
name|build
parameter_list|(
name|Map
name|options
parameter_list|)
block|{
name|JDBCBackingEngine
name|instance
init|=
literal|null
decl_stmt|;
name|String
name|datasourceURL
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCUtils
operator|.
name|DATASOURCE
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
operator|(
name|BundleContext
operator|)
name|options
operator|.
name|get
argument_list|(
name|BundleContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|addUserStatement
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|INSERT_USER_STATEMENT
argument_list|)
decl_stmt|;
name|String
name|addRoleStatement
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|INSERT_ROLE_STATEMENT
argument_list|)
decl_stmt|;
name|String
name|deleteRoleStatement
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|DELETE_ROLE_STATEMENT
argument_list|)
decl_stmt|;
name|String
name|deleteAllUserRolesStatement
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|DELETE_ROLES_STATEMENT
argument_list|)
decl_stmt|;
name|String
name|deleteUserStatement
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|DELETE_USER_STATEMENT
argument_list|)
decl_stmt|;
name|String
name|selectUsersQuery
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|PASSWORD_QUERY
argument_list|)
decl_stmt|;
name|String
name|selectRolesQuery
init|=
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
name|JDBCLoginModule
operator|.
name|ROLE_QUERY
argument_list|)
decl_stmt|;
try|try
block|{
name|DataSource
name|dataSource
init|=
operator|(
name|DataSource
operator|)
name|JDBCUtils
operator|.
name|createDatasource
argument_list|(
name|bundleContext
argument_list|,
name|datasourceURL
argument_list|)
decl_stmt|;
name|EncryptionSupport
name|encryptionSupport
init|=
operator|new
name|EncryptionSupport
argument_list|(
name|options
argument_list|)
decl_stmt|;
name|instance
operator|=
operator|new
name|JDBCBackingEngine
argument_list|(
name|dataSource
argument_list|,
name|encryptionSupport
argument_list|)
expr_stmt|;
if|if
condition|(
name|addUserStatement
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setAddUserStatement
argument_list|(
name|addUserStatement
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|addRoleStatement
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setAddRoleStatement
argument_list|(
name|addRoleStatement
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deleteRoleStatement
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setDeleteRoleStatement
argument_list|(
name|deleteRoleStatement
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deleteAllUserRolesStatement
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setDeleteAllUserRolesStatement
argument_list|(
name|deleteAllUserRolesStatement
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deleteUserStatement
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setDeleteUserStatement
argument_list|(
name|deleteUserStatement
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|selectUsersQuery
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setSelectUsersQuery
argument_list|(
name|selectUsersQuery
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|selectRolesQuery
operator|!=
literal|null
condition|)
block|{
name|instance
operator|.
name|setSelectRolesQuery
argument_list|(
name|selectRolesQuery
argument_list|)
expr_stmt|;
block|}
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
literal|"Error creating JDBCBackingEngine."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Returns the login module class, that this factory can build.      *      * @return      */
specifier|public
name|String
name|getModuleClass
parameter_list|()
block|{
return|return
name|JDBCLoginModule
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

