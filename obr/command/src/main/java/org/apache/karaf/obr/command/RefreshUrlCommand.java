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
name|Repository
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
literal|"obr"
argument_list|,
name|name
operator|=
literal|"url-refresh"
argument_list|,
name|description
operator|=
literal|"Reloads the repositories to obtain a fresh list of bundles."
argument_list|)
specifier|public
class|class
name|RefreshUrlCommand
extends|extends
name|ObrCommandSupport
block|{
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
literal|"--index"
block|}
argument_list|,
name|description
operator|=
literal|"Use index to identify URL"
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
name|useIndex
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
literal|"ids"
argument_list|,
name|description
operator|=
literal|"Repository URLs (or indexes if you use -i) to refresh (leave empty for all)"
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
name|ids
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
if|if
condition|(
name|ids
operator|!=
literal|null
operator|&&
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
name|useIndex
condition|)
block|{
name|Repository
index|[]
name|repos
init|=
name|admin
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
operator|&&
name|index
operator|<
name|repos
operator|.
name|length
condition|)
block|{
name|admin
operator|.
name|addRepository
argument_list|(
name|repos
index|[
name|index
index|]
operator|.
name|getURI
argument_list|()
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
literal|"Invalid index"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|admin
operator|.
name|addRepository
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Repository
index|[]
name|repos
init|=
name|admin
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|repos
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|repos
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|admin
operator|.
name|addRepository
argument_list|(
name|repos
index|[
name|i
index|]
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

