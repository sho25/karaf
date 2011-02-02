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
name|shell
operator|.
name|obr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|bundlerepository
operator|.
name|Capability
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
name|Requirement
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
literal|"info"
argument_list|,
name|description
operator|=
literal|"Prints information about OBR bundles."
argument_list|)
specifier|public
class|class
name|InfoCommand
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
literal|"bundles"
argument_list|,
name|description
operator|=
literal|"Specify bundles to query for information (separated by whitespaces)"
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
index|[]
name|resources
init|=
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
decl_stmt|;
if|if
condition|(
name|resources
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
for|for
control|(
name|int
name|resIdx
init|=
literal|0
init|;
name|resIdx
operator|<
name|resources
operator|.
name|length
condition|;
name|resIdx
operator|++
control|)
block|{
if|if
condition|(
name|resIdx
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
name|printResource
argument_list|(
name|System
operator|.
name|out
argument_list|,
name|resources
index|[
name|resIdx
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|printResource
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|Resource
name|resource
parameter_list|)
block|{
name|printUnderline
argument_list|(
name|out
argument_list|,
name|resource
operator|.
name|getPresentationName
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|resource
operator|.
name|getPresentationName
argument_list|()
argument_list|)
expr_stmt|;
name|printUnderline
argument_list|(
name|out
argument_list|,
name|resource
operator|.
name|getPresentationName
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
name|resource
operator|.
name|getProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|Array
operator|.
name|getLength
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|;
name|j
operator|++
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|Array
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|": "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Requirement
index|[]
name|reqs
init|=
name|resource
operator|.
name|getRequirements
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|reqs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|reqs
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Requires:"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reqs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|reqs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|reqs
index|[
name|i
index|]
operator|.
name|getFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Capability
index|[]
name|caps
init|=
name|resource
operator|.
name|getCapabilities
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|caps
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|caps
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Capabilities:"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|caps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|caps
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|caps
index|[
name|i
index|]
operator|.
name|getPropertiesAsMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

