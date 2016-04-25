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
name|jdbc
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
comment|/**  * JDBC MBean  */
end_comment

begin_interface
specifier|public
interface|interface
name|JdbcMBean
block|{
comment|/**      * Get the list of JDBC datasources.      *      * @return A {@link TabularData} containing the list of JDBC datasources.      * @throws MBeanException In case of MBean failure.      */
name|TabularData
name|getDatasources
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create a JDBC datasource.      *      * @param name The JDBC datasource name.      * @param driverName The {@code org.osgi.driver.name} of the DataSourceFactory to use.      * @param driverClass The {@code org.osgi.driver.class} of the DataSourceFactory to use.      * @param databaseName The name of the database to access.      * @param url The JDBC URL.      * @param user The database username.      * @param password The database password.      * @param databaseType The database type (ConnectionPoolDataSource, XADataSource or DataSource).      * @throws MBeanException In case of MBean failure.      */
name|void
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|driverName
parameter_list|,
name|String
name|driverClass
parameter_list|,
name|String
name|databaseName
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|databaseType
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Delete a JDBC datasource.      *      * @param name The JDBC datasource name (the one used at creation time).      * @throws MBeanException In case of MBean failure.      */
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get details about a JDBC datasource.      *      * @param datasource The JDBC datasource name.      * @return A {@link Map} (property/value) containing JDBC datasource details.      * @throws MBeanException In case of MBean failure.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
parameter_list|(
name|String
name|datasource
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get the tables available on a JDBC datasource.      *      * @param datasource the JDBC datasource name.      * @return A {@link TabularData} containing the datasource tables.      * @throws MBeanException In case of MBean failure.      */
name|TabularData
name|tables
parameter_list|(
name|String
name|datasource
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Execute a SQL command on a JDBC datasource.      *      * @param datasource The JDBC datasource name.      * @param command The SQL command to execute.      * @throws MBeanException In case of MBean failure.      */
name|void
name|execute
parameter_list|(
name|String
name|datasource
parameter_list|,
name|String
name|command
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Execute a SQL query on a JDBC datasource.      *      * @param datasource The JDBC datasource name.      * @param query The SQL query to execute.      * @return A {@link TabularData} with the result of execute (columns/values).      * @throws MBeanException In case of MBean failure.      */
name|TabularData
name|query
parameter_list|(
name|String
name|datasource
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

