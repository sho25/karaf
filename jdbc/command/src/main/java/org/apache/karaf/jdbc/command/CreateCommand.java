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
name|Option
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
literal|"create"
argument_list|,
name|description
operator|=
literal|"Create a JDBC datasource"
argument_list|)
specifier|public
class|class
name|CreateCommand
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
literal|"name"
argument_list|,
name|description
operator|=
literal|"The JDBC datasource name"
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
name|name
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-t"
argument_list|,
name|aliases
operator|=
block|{
literal|"--type"
block|}
argument_list|,
name|description
operator|=
literal|"The JDBC datasource type (generic, MySQL, Oracle, Postgres, H2, HSQL, Derby)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|type
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--driver"
block|}
argument_list|,
name|description
operator|=
literal|"The classname of the JDBC driver to use. NB: this option is used only the type generic"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|driver
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|aliases
operator|=
block|{
literal|"--version"
block|}
argument_list|,
name|description
operator|=
literal|"The version of the driver to use"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|version
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-url"
argument_list|,
name|description
operator|=
literal|"The JDBC URL to use"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|url
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{
literal|"--username"
block|}
argument_list|,
name|description
operator|=
literal|"The database username"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|username
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--password"
block|}
argument_list|,
name|description
operator|=
literal|"The database password"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|password
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--install-bundles"
block|}
argument_list|,
name|description
operator|=
literal|"Try to install the bundles providing the JDBC driver"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|installBundles
init|=
literal|false
decl_stmt|;
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|getJdbcService
argument_list|()
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
name|username
argument_list|,
name|password
argument_list|,
name|installBundles
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

