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
operator|.
name|ds
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Set
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
name|command
operator|.
name|JdbcCommandSupport
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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|support
operator|.
name|table
operator|.
name|ShellTable
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
name|jdbc
operator|.
name|DataSourceFactory
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
literal|"ds-factories"
argument_list|,
name|description
operator|=
literal|"List the JDBC DataSourceFactories"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|DSFactoriesCommand
extends|extends
name|JdbcCommandSupport
block|{
annotation|@
name|Reference
name|BundleContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
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
name|table
operator|.
name|column
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Class"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ServiceReference
argument_list|<
name|DataSourceFactory
argument_list|>
argument_list|>
name|refs
init|=
name|context
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
for|for
control|(
name|ServiceReference
argument_list|<
name|DataSourceFactory
argument_list|>
name|ref
range|:
name|refs
control|)
block|{
name|String
name|driverName
init|=
operator|(
name|String
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_NAME
argument_list|)
decl_stmt|;
name|String
name|driverClass
init|=
operator|(
name|String
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_CLASS
argument_list|)
decl_stmt|;
name|String
name|driverVersion
init|=
operator|(
name|String
operator|)
name|ref
operator|.
name|getProperty
argument_list|(
name|DataSourceFactory
operator|.
name|OSGI_JDBC_DRIVER_VERSION
argument_list|)
decl_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|driverName
argument_list|,
name|driverClass
argument_list|,
name|driverVersion
argument_list|)
expr_stmt|;
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

