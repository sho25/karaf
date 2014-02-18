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
name|net
operator|.
name|URI
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
name|obr
operator|.
name|command
operator|.
name|util
operator|.
name|FileUtil
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
name|inject
operator|.
name|Service
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
literal|"source"
argument_list|,
name|description
operator|=
literal|"Downloads the sources for an OBR bundle."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|SourceCommand
extends|extends
name|ObrCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-x"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Extract the archive"
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
name|extract
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"folder"
argument_list|,
name|description
operator|=
literal|"Local folder for storing sources"
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
name|localDir
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
literal|"bundles"
argument_list|,
name|description
operator|=
literal|"List of bundles to download the sources for"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|bundles
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|bundle
range|:
name|bundles
control|)
block|{
name|String
index|[]
name|target
init|=
name|getTarget
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|Resource
name|resource
init|=
name|selectNewestVersion
argument_list|(
name|searchRepository
argument_list|(
name|admin
argument_list|,
name|target
index|[
literal|0
index|]
argument_list|,
name|target
index|[
literal|1
index|]
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unknown bundle and/or version: "
operator|+
name|target
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URI
name|srcURL
init|=
operator|(
name|URI
operator|)
name|resource
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Resource
operator|.
name|SOURCE_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|srcURL
operator|!=
literal|null
condition|)
block|{
name|FileUtil
operator|.
name|downloadSource
argument_list|(
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|,
name|srcURL
operator|.
name|toURL
argument_list|()
argument_list|,
name|localDir
argument_list|,
name|extract
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Missing source URL: "
operator|+
name|target
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

