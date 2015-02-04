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

begin_comment
comment|/**  * JDBC Service.  */
end_comment

begin_interface
specifier|public
interface|interface
name|JdbcService
block|{
comment|/**      * Create a JDBC datasource configuration.      *      * @param name Datasource name       * @param driverName Backend database type (osgi.jdbc.driver.name of DataSourceFactory)      * @param url JDBC URL      * @param user Database user name      * @param password Database password      * @param password2       */
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
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Delete a JDBC datasource identified by a name. Works only      * for datasources that have a corresponding configuration      *      * @param name Datasource name      */
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * List the JDBC DataSourceFactories available.      *      * @return a list of DataSourceFactory names      */
name|List
argument_list|<
name|String
argument_list|>
name|factoryNames
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * List the JDBC datasources available.      *      * @return a list of datasources names      */
name|List
argument_list|<
name|String
argument_list|>
name|datasources
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Execute a SQL query on a given JDBC datasource.      *      * @param datasource the JDBC datasource name.      * @param query the SQL query to execute.      * @return the SQL query result (as a String).      */
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|query
parameter_list|(
name|String
name|datasource
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Execute a SQL command on a given JDBC datasource.      *      * @param datasource the JDBC datasource name.      * @param command the SQL command to execute.      */
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
name|Exception
function_decl|;
comment|/**      * List the tables available on a given JDBC datasource.      *      * @param datasource the JDBC datasource name.      * @return the list of table names.      */
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|tables
parameter_list|(
name|String
name|datasource
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get detailed info about a JDBC datasource.      *      * @param datasource the JDBC datasource name.      * @return a map of info (name/value).      */
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
name|Exception
function_decl|;
block|}
end_interface

end_unit

