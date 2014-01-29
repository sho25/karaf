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
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|TemplateUtils
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
name|ServiceReference
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
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|*
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
comment|/**  * Default implementation of the JDBC Service.  */
end_comment

begin_class
specifier|public
class|class
name|JdbcServiceImpl
implements|implements
name|JdbcService
block|{
specifier|public
specifier|static
enum|enum
name|TYPES
block|{
name|DB2
argument_list|(
literal|"wrap:mvn:com.ibm.db2.jdbc/db2jcc/"
argument_list|,
literal|"9.7"
argument_list|,
literal|"datasource-db2.xml"
argument_list|)
block|,
name|DERBY
argument_list|(
literal|"mvn:org.apache.derby/derby/"
argument_list|,
literal|"10.8.2.2"
argument_list|,
literal|"datasource-derby.xml"
argument_list|)
block|,
name|GENERIC
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"datasource-generic.xml"
argument_list|)
block|,
name|H2
argument_list|(
literal|"mvn:com.h2database/h2/"
argument_list|,
literal|"1.3.163"
argument_list|,
literal|"datasource-h2.xml"
argument_list|)
block|,
name|HSQL
argument_list|(
literal|"mvn:com.h2database/h2/"
argument_list|,
literal|"1.3.163"
argument_list|,
literal|"datasource-hsql.xml"
argument_list|)
block|,
name|MYSQL
argument_list|(
literal|"mvn:mysql/mysql-connector-java/"
argument_list|,
literal|"5.1.18"
argument_list|,
literal|"datasource-mysql.xml"
argument_list|)
block|,
name|ORACLE
argument_list|(
literal|"wrap:mvn:ojdbc/ojdbc/"
argument_list|,
literal|"11.2.0.2.0"
argument_list|,
literal|"datasource-oracle.xml"
argument_list|)
block|,
name|POSTGRES
argument_list|(
literal|"wrap:mvn:postgresql/postgresql/"
argument_list|,
literal|"9.1-901.jdbc4"
argument_list|,
literal|"datasource-postgres.xml"
argument_list|)
block|;
specifier|private
specifier|final
name|String
name|bundleUrl
decl_stmt|;
specifier|private
specifier|final
name|String
name|defaultVersion
decl_stmt|;
specifier|private
specifier|final
name|String
name|templateFile
decl_stmt|;
name|TYPES
parameter_list|(
name|String
name|bundleUrl
parameter_list|,
name|String
name|defaultVersion
parameter_list|,
name|String
name|templateFile
parameter_list|)
block|{
name|this
operator|.
name|bundleUrl
operator|=
name|bundleUrl
expr_stmt|;
name|this
operator|.
name|defaultVersion
operator|=
name|defaultVersion
expr_stmt|;
name|this
operator|.
name|templateFile
operator|=
name|templateFile
expr_stmt|;
block|}
specifier|public
name|void
name|installBundle
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|location
init|=
name|this
operator|.
name|bundleUrl
operator|+
name|getWithDefault
argument_list|(
name|version
argument_list|,
name|this
operator|.
name|defaultVersion
argument_list|)
decl_stmt|;
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|location
argument_list|,
literal|null
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getWithDefault
parameter_list|(
name|String
name|st
parameter_list|,
name|String
name|defaultSt
parameter_list|)
block|{
return|return
operator|(
name|st
operator|==
literal|null
operator|)
condition|?
name|defaultSt
else|:
name|st
return|;
block|}
specifier|public
name|void
name|copyDataSourceFile
parameter_list|(
name|File
name|outFile
parameter_list|,
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|templateFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Template resource "
operator|+
name|templateFile
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|TemplateUtils
operator|.
name|createFromTemplate
argument_list|(
name|outFile
argument_list|,
name|is
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|BundleContext
name|bundleContext
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
name|type
parameter_list|,
name|String
name|driverClassName
parameter_list|,
name|String
name|version
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
name|boolean
name|tryToInstallBundles
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No database type supplied"
argument_list|)
throw|;
block|}
name|TYPES
name|dbType
init|=
name|TYPES
operator|.
name|valueOf
argument_list|(
name|type
operator|.
name|toUpperCase
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tryToInstallBundles
condition|)
block|{
name|dbType
operator|.
name|installBundle
argument_list|(
name|bundleContext
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
name|File
name|karafBase
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|deployFolder
init|=
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"deploy"
argument_list|)
decl_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|deployFolder
argument_list|,
literal|"datasource-"
operator|+
name|name
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"driver"
argument_list|,
name|driverClassName
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|dbType
operator|.
name|copyDataSourceFile
argument_list|(
name|outFile
argument_list|,
name|properties
argument_list|)
expr_stmt|;
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
name|File
name|karafBase
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|deployFolder
init|=
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"deploy"
argument_list|)
decl_stmt|;
name|File
name|datasourceFile
init|=
operator|new
name|File
argument_list|(
name|deployFolder
argument_list|,
literal|"datasource-"
operator|+
name|name
operator|+
literal|".xml"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|datasourceFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The JDBC datasource file "
operator|+
name|datasourceFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" doesn't exist"
argument_list|)
throw|;
block|}
name|datasourceFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
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
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ServiceReference
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
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
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|)
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
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"datasource"
argument_list|)
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
operator|(
name|String
operator|)
name|reference
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
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
name|String
argument_list|>
name|datasourceFileNames
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|karafBase
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|deployFolder
init|=
operator|new
name|File
argument_list|(
name|karafBase
argument_list|,
literal|"deploy"
argument_list|)
decl_stmt|;
name|String
index|[]
name|datasourceFileNames
init|=
name|deployFolder
operator|.
name|list
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
literal|"datasource-"
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|datasourceFileNames
argument_list|)
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
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|ServiceReference
name|reference
init|=
name|this
operator|.
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
name|Statement
name|statement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DataSource
name|ds
init|=
operator|(
name|DataSource
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|connection
operator|=
name|ds
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|statement
operator|=
name|connection
operator|.
name|createStatement
argument_list|()
expr_stmt|;
name|ResultSet
name|resultSet
init|=
name|statement
operator|.
name|executeQuery
argument_list|(
name|query
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
argument_list|<
name|String
argument_list|>
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
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|statement
operator|!=
literal|null
condition|)
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
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
name|ServiceReference
name|reference
init|=
name|this
operator|.
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
name|Statement
name|statement
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DataSource
name|ds
init|=
operator|(
name|DataSource
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|connection
operator|=
name|ds
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|statement
operator|=
name|connection
operator|.
name|createStatement
argument_list|()
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|statement
operator|!=
literal|null
condition|)
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
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
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|ServiceReference
name|reference
init|=
name|this
operator|.
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DataSource
name|ds
init|=
operator|(
name|DataSource
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|connection
operator|=
name|ds
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|DatabaseMetaData
name|dbMetaData
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|ResultSet
name|resultSet
init|=
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
argument_list|<
name|String
argument_list|>
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
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ServiceReference
name|reference
init|=
name|this
operator|.
name|lookupDataSource
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DataSource
name|ds
init|=
operator|(
name|DataSource
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|connection
operator|=
name|ds
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|DatabaseMetaData
name|dbMetaData
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
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
block|}
finally|finally
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|private
name|ServiceReference
name|lookupDataSource
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceReference
index|[]
name|references
init|=
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|DataSource
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
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
literal|"))"
argument_list|)
decl_stmt|;
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple JDBC datasource found for "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|references
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
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
block|}
end_class

end_unit

