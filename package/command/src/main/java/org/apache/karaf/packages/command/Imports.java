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
name|packages
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
name|SortedMap
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
name|packages
operator|.
name|core
operator|.
name|PackageRequirement
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
name|packages
operator|.
name|core
operator|.
name|PackageService
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
name|console
operator|.
name|OsgiCommandSupport
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
name|Bundle
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"package"
argument_list|,
name|name
operator|=
literal|"imports"
argument_list|,
name|description
operator|=
literal|"Lists imported packages and the bundles that import them"
argument_list|)
specifier|public
class|class
name|Imports
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|description
operator|=
literal|"Only show package instead of full filter"
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
name|onlyPackage
decl_stmt|;
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
specifier|private
name|PackageService
name|packageService
decl_stmt|;
specifier|public
name|Imports
parameter_list|(
name|PackageService
name|packageService
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|packageService
operator|=
name|packageService
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PackageRequirement
argument_list|>
name|imports
init|=
name|packageService
operator|.
name|getImports
argument_list|()
decl_stmt|;
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
name|onlyPackage
condition|?
literal|"Package name"
else|:
literal|"Filter"
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
literal|"Optional"
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
literal|"Bundle Name"
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
literal|"Resolveable"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|filter
range|:
name|imports
operator|.
name|keySet
argument_list|()
control|)
block|{
name|PackageRequirement
name|req
init|=
name|imports
operator|.
name|get
argument_list|(
name|filter
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|req
operator|.
name|getBundle
argument_list|()
decl_stmt|;
name|String
name|firstCol
init|=
name|onlyPackage
condition|?
name|req
operator|.
name|getPackageName
argument_list|()
else|:
name|req
operator|.
name|getFilter
argument_list|()
decl_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|firstCol
argument_list|,
name|req
operator|.
name|isOptional
argument_list|()
condition|?
literal|"optional"
else|:
literal|""
argument_list|,
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|req
operator|.
name|isResolveable
argument_list|()
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
block|}
end_class

end_unit

