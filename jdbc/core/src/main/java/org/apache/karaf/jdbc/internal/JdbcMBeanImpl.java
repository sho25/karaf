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
name|JdbcMBean
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
name|*
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

begin_comment
comment|/**  * Default implementation of the JDBC MBean.  */
end_comment

begin_class
specifier|public
class|class
name|JdbcMBeanImpl
implements|implements
name|JdbcMBean
block|{
specifier|private
name|JdbcService
name|jdbcService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|TabularData
name|getDatasources
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|type
init|=
operator|new
name|CompositeType
argument_list|(
literal|"DataSource"
argument_list|,
literal|"JDBC DataSource"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|,
literal|"product"
block|,
literal|"version"
block|,
literal|"url "
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"JDBC DataSource Name"
block|,
literal|"Database product"
block|,
literal|"Database version"
block|,
literal|"JDBC URL"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"JDBC DataSources"
argument_list|,
literal|"Table of the JDBC DataSources"
argument_list|,
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|datasource
range|:
name|jdbcService
operator|.
name|datasources
argument_list|()
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
init|=
name|jdbcService
operator|.
name|info
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|,
literal|"product"
block|,
literal|"version"
block|,
literal|"url"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|datasource
block|,
name|info
operator|.
name|get
argument_list|(
literal|"db.product"
argument_list|)
block|,
name|info
operator|.
name|get
argument_list|(
literal|"db.version"
argument_list|)
block|,
name|info
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
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
name|driver
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
name|installBundles
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|jdbcService
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|driver
argument_list|,
name|version
argument_list|,
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|,
name|installBundles
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
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
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
name|MBeanException
block|{
try|try
block|{
name|jdbcService
operator|.
name|delete
argument_list|(
name|name
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
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
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
name|MBeanException
block|{
try|try
block|{
return|return
name|jdbcService
operator|.
name|info
argument_list|(
name|datasource
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|tables
parameter_list|(
name|String
name|datasource
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
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
name|result
init|=
name|jdbcService
operator|.
name|tables
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|OpenType
index|[]
name|stringTypes
init|=
operator|new
name|OpenType
index|[
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|stringTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|stringTypes
index|[
name|i
index|]
operator|=
name|SimpleType
operator|.
name|STRING
expr_stmt|;
block|}
name|String
index|[]
name|columns
init|=
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|CompositeType
name|type
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Columns"
argument_list|,
literal|"Columns"
argument_list|,
name|columns
argument_list|,
name|columns
argument_list|,
name|stringTypes
argument_list|)
decl_stmt|;
name|TabularType
name|rows
init|=
operator|new
name|TabularType
argument_list|(
literal|"Result"
argument_list|,
literal|"Result Rows"
argument_list|,
name|type
argument_list|,
name|columns
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|int
name|rowCount
init|=
name|result
operator|.
name|get
argument_list|(
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rowCount
condition|;
name|i
operator|++
control|)
block|{
name|Object
index|[]
name|row
init|=
operator|new
name|Object
index|[
name|columns
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|columns
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|row
index|[
name|j
index|]
operator|=
name|result
operator|.
name|get
argument_list|(
name|columns
index|[
name|j
index|]
argument_list|)
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|type
argument_list|,
name|columns
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
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
name|MBeanException
block|{
try|try
block|{
name|jdbcService
operator|.
name|execute
argument_list|(
name|datasource
argument_list|,
name|command
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
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
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
block|{
try|try
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
name|result
init|=
name|jdbcService
operator|.
name|query
argument_list|(
name|datasource
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|OpenType
index|[]
name|stringTypes
init|=
operator|new
name|OpenType
index|[
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|stringTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|stringTypes
index|[
name|i
index|]
operator|=
name|SimpleType
operator|.
name|STRING
expr_stmt|;
block|}
name|String
index|[]
name|columns
init|=
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|CompositeType
name|type
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Columns"
argument_list|,
literal|"Columns"
argument_list|,
name|columns
argument_list|,
name|columns
argument_list|,
name|stringTypes
argument_list|)
decl_stmt|;
name|TabularType
name|rows
init|=
operator|new
name|TabularType
argument_list|(
literal|"Result"
argument_list|,
literal|"Result Rows"
argument_list|,
name|type
argument_list|,
name|columns
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|rows
argument_list|)
decl_stmt|;
name|int
name|rowCount
init|=
name|result
operator|.
name|get
argument_list|(
name|result
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rowCount
condition|;
name|i
operator|++
control|)
block|{
name|Object
index|[]
name|row
init|=
operator|new
name|Object
index|[
name|columns
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|columns
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|row
index|[
name|j
index|]
operator|=
name|result
operator|.
name|get
argument_list|(
name|columns
index|[
name|j
index|]
argument_list|)
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|type
argument_list|,
name|columns
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|JdbcService
name|getJdbcService
parameter_list|()
block|{
return|return
name|jdbcService
return|;
block|}
specifier|public
name|void
name|setJdbcService
parameter_list|(
name|JdbcService
name|jdbcService
parameter_list|)
block|{
name|this
operator|.
name|jdbcService
operator|=
name|jdbcService
expr_stmt|;
block|}
block|}
end_class

end_unit

