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
name|obr
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|RepositoryAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|Resource
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"obr"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists OBR bundles, optionally providing the given packages."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ListCommand
extends|extends
name|ObrCommandSupport
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
literal|"packages"
argument_list|,
name|description
operator|=
literal|"A list of packages separated by whitespaces."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|packages
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
annotation|@
name|Override
name|void
name|doExecute
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|substr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|packages
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|packageName
range|:
name|packages
control|)
block|{
name|substr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|substr
operator|.
name|append
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|query
decl_stmt|;
if|if
condition|(
operator|(
name|substr
operator|==
literal|null
operator|)
operator|||
operator|(
name|substr
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
name|query
operator|=
literal|"(|(presentationname=*)(symbolicname=*))"
expr_stmt|;
block|}
else|else
block|{
name|query
operator|=
literal|"(|(presentationname=*"
operator|+
name|substr
operator|+
literal|"*)(symbolicname=*"
operator|+
name|substr
operator|+
literal|"*))"
expr_stmt|;
block|}
name|Resource
index|[]
name|resources
init|=
name|admin
operator|.
name|discoverResources
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
name|maxPName
init|=
literal|4
decl_stmt|;
name|int
name|maxSName
init|=
literal|13
decl_stmt|;
name|int
name|maxVersion
init|=
literal|7
decl_stmt|;
for|for
control|(
name|Resource
name|resource
range|:
name|resources
control|)
block|{
name|maxPName
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxPName
argument_list|,
name|emptyIfNull
argument_list|(
name|resource
operator|.
name|getPresentationName
argument_list|()
argument_list|)
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|maxSName
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxSName
argument_list|,
name|emptyIfNull
argument_list|(
name|resource
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|maxVersion
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxVersion
argument_list|,
name|emptyIfNull
argument_list|(
name|resource
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"Symbolic Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
expr_stmt|;
name|table
operator|.
name|emptyTableText
argument_list|(
literal|"No matching bundles"
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|resource
range|:
name|resources
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|emptyIfNull
argument_list|(
name|resource
operator|.
name|getPresentationName
argument_list|()
argument_list|)
argument_list|,
name|emptyIfNull
argument_list|(
name|resource
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
argument_list|,
name|emptyIfNull
argument_list|(
name|resource
operator|.
name|getVersion
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
block|}
specifier|private
name|String
name|emptyIfNull
parameter_list|(
name|Object
name|st
parameter_list|)
block|{
return|return
name|st
operator|==
literal|null
condition|?
literal|""
else|:
name|st
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

