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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|obr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|gogo
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
name|felix
operator|.
name|gogo
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
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
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
name|Version
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
name|obr
operator|.
name|Capability
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
name|obr
operator|.
name|Repository
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
name|obr
operator|.
name|RepositoryAdmin
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
name|obr
operator|.
name|Requirement
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
name|obr
operator|.
name|Resource
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
literal|"find"
argument_list|,
name|description
operator|=
literal|"Find OBR bundles for a given filter"
argument_list|)
specifier|public
class|class
name|FindCommand
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
literal|"requirement"
argument_list|,
name|description
operator|=
literal|"Requirement"
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
name|requirement
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
name|List
argument_list|<
name|Resource
argument_list|>
name|matching
init|=
operator|new
name|ArrayList
argument_list|<
name|Resource
argument_list|>
argument_list|()
decl_stmt|;
name|Repository
index|[]
name|repos
init|=
name|admin
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
name|Requirement
name|req
init|=
name|parseRequirement
argument_list|(
name|admin
argument_list|,
name|requirement
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|repoIdx
init|=
literal|0
init|;
operator|(
name|repos
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|repoIdx
operator|<
name|repos
operator|.
name|length
operator|)
condition|;
name|repoIdx
operator|++
control|)
block|{
name|Resource
index|[]
name|resources
init|=
name|repos
index|[
name|repoIdx
index|]
operator|.
name|getResources
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
operator|(
name|resources
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|resIdx
operator|<
name|resources
operator|.
name|length
operator|)
condition|;
name|resIdx
operator|++
control|)
block|{
name|Capability
index|[]
name|caps
init|=
name|resources
index|[
name|resIdx
index|]
operator|.
name|getCapabilities
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|capIdx
init|=
literal|0
init|;
operator|(
name|caps
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|capIdx
operator|<
name|caps
operator|.
name|length
operator|)
condition|;
name|capIdx
operator|++
control|)
block|{
if|if
condition|(
name|req
operator|.
name|isSatisfied
argument_list|(
name|caps
index|[
name|capIdx
index|]
argument_list|)
condition|)
block|{
name|matching
operator|.
name|add
argument_list|(
name|resources
index|[
name|resIdx
index|]
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
name|matching
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No matching resources."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Resource
name|resource
range|:
name|matching
control|)
block|{
name|String
name|name
init|=
name|resource
operator|.
name|getPresentationName
argument_list|()
decl_stmt|;
name|Version
name|version
init|=
name|resource
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|version
operator|!=
literal|null
condition|?
name|name
operator|+
literal|" ("
operator|+
name|version
operator|+
literal|")"
else|:
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Requirement
name|parseRequirement
parameter_list|(
name|RepositoryAdmin
name|admin
parameter_list|,
name|String
name|req
parameter_list|)
throws|throws
name|InvalidSyntaxException
block|{
name|int
name|p
init|=
name|req
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|filter
decl_stmt|;
if|if
condition|(
name|p
operator|>
literal|0
condition|)
block|{
name|name
operator|=
name|req
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|filter
operator|=
name|req
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|req
operator|.
name|contains
argument_list|(
literal|"package"
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"package"
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
literal|"bundle"
expr_stmt|;
block|}
name|filter
operator|=
name|req
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|filter
operator|=
literal|"("
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
return|return
name|admin
operator|.
name|requirement
argument_list|(
name|name
argument_list|,
name|filter
argument_list|)
return|;
block|}
block|}
end_class

end_unit

