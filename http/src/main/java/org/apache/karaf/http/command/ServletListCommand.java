begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*   * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|http
operator|.
name|command
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|http
operator|.
name|core
operator|.
name|ServletInfo
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
name|http
operator|.
name|core
operator|.
name|ServletService
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
name|Action
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
name|Option
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
name|Col
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"http"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists details for servlets."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ServletListCommand
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-format"
argument_list|,
name|description
operator|=
literal|"Disable table rendered output"
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
name|noFormat
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|ServletService
name|servletService
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
operator|new
name|Col
argument_list|(
literal|"ID"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Servlet"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Servlet-Name"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"State"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Alias"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Url"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ServletInfo
name|info
range|:
name|servletService
operator|.
name|getServlets
argument_list|()
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|info
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|info
operator|.
name|getClassName
argument_list|()
argument_list|,
name|info
operator|.
name|getName
argument_list|()
argument_list|,
name|info
operator|.
name|getStateString
argument_list|()
argument_list|,
name|info
operator|.
name|getAlias
argument_list|()
argument_list|,
name|Arrays
operator|.
name|toString
argument_list|(
name|info
operator|.
name|getUrls
argument_list|()
argument_list|)
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
argument_list|,
operator|!
name|noFormat
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setServletService
parameter_list|(
name|ServletService
name|servletService
parameter_list|)
block|{
name|this
operator|.
name|servletService
operator|=
name|servletService
expr_stmt|;
block|}
block|}
end_class

end_unit

