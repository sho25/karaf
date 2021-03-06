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
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSetMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|javax
operator|.
name|sql
operator|.
name|XADataSource
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
name|jdbc
operator|.
name|JdbcService
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
name|osgi
operator|.
name|framework
operator|.
name|Constants
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
name|InvalidSyntaxException
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|jdbc
operator|.
name|DataSourceFactory
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

begin_comment
comment|/**  * Default implementation of the JDBC Service.  */
end_comment

begin_class
specifier|public
class|class
name|JdbcServiceImpl
implements|implements
name|JdbcService
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JdbcServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|configAdmin
decl_stmt|;
annotation|@
name|Override
specifier|public
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
name|Exception
block|{
if|if
condition|(
name|driverName
operator|==
literal|null
operator|&&
name|driverClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No driverName or driverClass supplied"
argument_list|)
throw|;
block|}
if|if
condition|(
name|datasources
argument_list|()
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There is already a DataSource with the name "
operator|+
name|name
argument_list|)
throw|;
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|DataSourceFactory
operator|.
name|JDBC_DATASOURCE_NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_NAME
argument_list|,
name|driverName
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_CLASS
argument_list|,
name|driverClass
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|DataSourceFactory
operator|.
name|JDBC_DATABASE_NAME
argument_list|,
name|databaseName
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|DataSourceFactory
operator|.
name|JDBC_URL
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|DataSourceFactory
operator|.
name|JDBC_USER
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
name|DataSourceFactory
operator|.
name|JDBC_PASSWORD
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|properties
argument_list|,
literal|"dataSourceType"
argument_list|,
name|databaseType
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
literal|"org.ops4j.datasource"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|config
operator|.
name|update
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|put
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|filter
init|=
name|String
operator|.
name|format
argument_list|(
literal|"(&(service.factoryPid=org.ops4j.datasource)(%s=%s))"
argument_list|,
name|DataSourceFactory
operator|.
name|JDBC_DATASOURCE_NAME
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|Configuration
index|[]
name|configs
init|=
name|configAdmin
operator|.
name|listConfigurations
argument_list|(
name|filter
argument_list|)
decl_stmt|;
for|for
control|(
name|Configuration
name|config
range|:
name|configs
control|)
block|{
name|config
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|datasources
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|datasources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|"(|("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|XADataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"))"
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|datasources
operator|.
name|add
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"datasource"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|datasources
operator|.
name|add
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"datasource"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|datasources
operator|.
name|add
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|JDBC_DATASOURCE_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|datasources
operator|.
name|add
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|JDBC_DATASOURCE_NAME
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|datasources
operator|.
name|add
argument_list|(
name|reference
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|datasources
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Long
argument_list|>
name|datasourceServiceIds
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Long
argument_list|>
name|datasources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|"(|("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|XADataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"))"
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceReference
name|reference
range|:
name|references
control|)
block|{
name|datasources
operator|.
name|add
argument_list|(
operator|(
name|Long
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|datasources
return|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
init|(
name|JdbcConnector
name|jdbcConnector
init|=
operator|new
name|JdbcConnector
argument_list|(
name|bundleContext
argument_list|,
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
argument_list|)
init|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Statement
name|statement
init|=
name|jdbcConnector
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|jdbcConnector
operator|.
name|register
argument_list|(
name|statement
operator|.
name|executeQuery
argument_list|(
name|query
argument_list|)
argument_list|)
decl_stmt|;
name|ResultSetMetaData
name|metaData
init|=
name|resultSet
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|1
init|;
name|c
operator|<=
name|metaData
operator|.
name|getColumnCount
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|metaData
operator|.
name|getColumnLabel
argument_list|(
name|c
argument_list|)
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|resultSet
operator|.
name|next
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|c
init|=
literal|1
init|;
name|c
operator|<=
name|metaData
operator|.
name|getColumnCount
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|map
operator|.
name|get
argument_list|(
name|metaData
operator|.
name|getColumnLabel
argument_list|(
name|c
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|resultSet
operator|.
name|getString
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
init|(
name|JdbcConnector
name|jdbcConnector
init|=
operator|new
name|JdbcConnector
argument_list|(
name|bundleContext
argument_list|,
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
argument_list|)
init|)
block|{
name|jdbcConnector
operator|.
name|createStatement
argument_list|()
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
init|(
name|JdbcConnector
name|jdbcConnector
init|=
operator|new
name|JdbcConnector
argument_list|(
name|bundleContext
argument_list|,
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
argument_list|)
init|)
block|{
name|DatabaseMetaData
name|dbMetaData
init|=
name|jdbcConnector
operator|.
name|connect
argument_list|()
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|jdbcConnector
operator|.
name|register
argument_list|(
name|dbMetaData
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|ResultSetMetaData
name|metaData
init|=
name|resultSet
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|1
init|;
name|c
operator|<=
name|metaData
operator|.
name|getColumnCount
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|metaData
operator|.
name|getColumnLabel
argument_list|(
name|c
argument_list|)
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|resultSet
operator|.
name|next
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|c
init|=
literal|1
init|;
name|c
operator|<=
name|metaData
operator|.
name|getColumnCount
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|map
operator|.
name|get
argument_list|(
name|metaData
operator|.
name|getColumnLabel
argument_list|(
name|c
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|resultSet
operator|.
name|getString
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|reference
init|=
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|String
name|dsName
init|=
name|datasource
decl_stmt|;
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|dsName
operator|=
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"datasource"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|dsName
operator|=
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"datasource"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|dsName
operator|=
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|JdbcConnector
name|jdbcConnector
init|=
operator|new
name|JdbcConnector
argument_list|(
name|bundleContext
argument_list|,
name|reference
argument_list|)
init|)
block|{
name|DatabaseMetaData
name|dbMetaData
init|=
name|jdbcConnector
operator|.
name|connect
argument_list|()
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|dsName
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"service.id"
argument_list|,
name|reference
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_ID
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"db.product"
argument_list|,
name|dbMetaData
operator|.
name|getDatabaseProductName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"db.version"
argument_list|,
name|dbMetaData
operator|.
name|getDatabaseProductVersion
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"url"
argument_list|,
name|dbMetaData
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
name|dbMetaData
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"driver.name"
argument_list|,
name|dbMetaData
operator|.
name|getDriverName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"driver.version"
argument_list|,
name|dbMetaData
operator|.
name|getDriverVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|map
return|;
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
literal|"Can't get information about datasource {}"
argument_list|,
name|datasource
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
specifier|private
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|lookupDataSource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
index|[]
name|references
decl_stmt|;
try|try
block|{
name|references
operator|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|"(&(|("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|XADataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"))"
operator|+
literal|"(|(osgi.jndi.service.name="
operator|+
name|name
operator|+
literal|")(datasource="
operator|+
name|name
operator|+
literal|")(name="
operator|+
name|name
operator|+
literal|")(service.id="
operator|+
name|name
operator|+
literal|")))"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error finding datasource with name "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|references
operator|==
literal|null
operator|||
name|references
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No JDBC datasource found for "
operator|+
name|name
argument_list|)
throw|;
block|}
if|if
condition|(
name|references
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|references
argument_list|)
expr_stmt|;
if|if
condition|(
name|getRank
argument_list|(
name|references
index|[
name|references
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
operator|==
name|getRank
argument_list|(
name|references
index|[
name|references
operator|.
name|length
operator|-
literal|2
index|]
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Multiple JDBC datasources found with the same service ranking for "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|references
index|[
name|references
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
specifier|private
name|int
name|getRank
parameter_list|(
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|reference
parameter_list|)
block|{
name|Object
name|rankObj
init|=
name|reference
operator|.
name|getProperty
argument_list|(
name|Constants
operator|.
name|SERVICE_RANKING
argument_list|)
decl_stmt|;
comment|// If no rank, then spec says it defaults to zero.
name|rankObj
operator|=
operator|(
name|rankObj
operator|==
literal|null
operator|)
condition|?
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
else|:
name|rankObj
expr_stmt|;
comment|// If rank is not Integer, then spec says it defaults to zero.
return|return
operator|(
name|rankObj
operator|instanceof
name|Integer
operator|)
condition|?
operator|(
name|Integer
operator|)
name|rankObj
else|:
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|factoryNames
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|factories
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|DataSourceFactory
argument_list|>
argument_list|>
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|references
operator|==
literal|null
condition|)
block|{
return|return
name|factories
return|;
block|}
for|for
control|(
name|ServiceReference
argument_list|<
name|DataSourceFactory
argument_list|>
name|reference
range|:
name|references
control|)
block|{
name|String
name|driverName
init|=
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|driverName
operator|!=
literal|null
condition|)
block|{
name|factories
operator|.
name|add
argument_list|(
name|driverName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|factories
return|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|void
name|setConfigAdmin
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

