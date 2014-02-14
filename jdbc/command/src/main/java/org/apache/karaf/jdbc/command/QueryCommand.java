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
name|command
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
name|command
operator|.
name|completers
operator|.
name|DataSourcesNameCompleter
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
name|shell
operator|.
name|commands
operator|.
name|Argument
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
name|shell
operator|.
name|commands
operator|.
name|Command
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
name|shell
operator|.
name|commands
operator|.
name|Completer
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
name|shell
operator|.
name|table
operator|.
name|Row
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
name|shell
operator|.
name|table
operator|.
name|ShellTable
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
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jdbc"
argument_list|,
name|name
operator|=
literal|"query"
argument_list|,
name|description
operator|=
literal|"Execute a SQL query on a JDBC datasource"
argument_list|)
specifier|public
class|class
name|QueryCommand
extends|extends
name|JdbcCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"datasource"
argument_list|,
name|description
operator|=
literal|"The JDBC datasource to use"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completer
argument_list|(
name|DataSourcesNameCompleter
operator|.
name|class
argument_list|)
name|String
name|datasource
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"query"
argument_list|,
name|description
operator|=
literal|"The SQL query to execute"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|query
decl_stmt|;
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
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
name|this
operator|.
name|getJdbcService
argument_list|()
operator|.
name|query
argument_list|(
name|datasource
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|int
name|rowCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|column
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|table
operator|.
name|column
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|rowCount
operator|=
name|map
operator|.
name|get
argument_list|(
name|column
argument_list|)
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
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
name|Row
name|row
init|=
name|table
operator|.
name|addRow
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|column
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|row
operator|.
name|addContent
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|column
argument_list|)
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

