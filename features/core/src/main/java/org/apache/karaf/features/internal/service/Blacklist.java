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
name|features
operator|.
name|internal
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|utils
operator|.
name|manifest
operator|.
name|Clause
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
name|utils
operator|.
name|manifest
operator|.
name|Parser
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
name|utils
operator|.
name|version
operator|.
name|VersionRange
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
name|utils
operator|.
name|version
operator|.
name|VersionTable
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Bundle
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Conditional
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Feature
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
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Features
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Helper class to deal with blacklisted features and bundles.  */
end_comment

begin_class
specifier|public
class|class
name|Blacklist
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BLACKLIST_URL
init|=
literal|"url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLACKLIST_RANGE
init|=
literal|"range"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLACKLIST_TYPE
init|=
literal|"type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_FEATURE
init|=
literal|"feature"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_BUNDLE
init|=
literal|"bundle"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_REPOSITORY
init|=
literal|"repository"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Blacklist
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Blacklist
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|blacklist
parameter_list|(
name|Features
name|features
parameter_list|,
name|String
name|blacklisted
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|blacklist
init|=
name|loadBlacklist
argument_list|(
name|blacklisted
argument_list|)
decl_stmt|;
name|blacklist
argument_list|(
name|features
argument_list|,
name|blacklist
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|blacklist
parameter_list|(
name|Features
name|features
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|blacklist
parameter_list|)
block|{
if|if
condition|(
operator|!
name|blacklist
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseClauses
argument_list|(
name|blacklist
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|blacklist
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|blacklist
argument_list|(
name|features
argument_list|,
name|clauses
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|blacklist
parameter_list|(
name|Features
name|features
parameter_list|,
name|Clause
index|[]
name|clauses
parameter_list|)
block|{
name|features
operator|.
name|getFeature
argument_list|()
operator|.
name|removeIf
argument_list|(
name|feature
lambda|->
name|blacklist
argument_list|(
name|feature
argument_list|,
name|clauses
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|blacklist
parameter_list|(
name|Feature
name|feature
parameter_list|,
name|Clause
index|[]
name|clauses
parameter_list|)
block|{
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
comment|// Check feature name
if|if
condition|(
name|clause
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// Check feature version
name|VersionRange
name|range
init|=
name|VersionRange
operator|.
name|ANY_VERSION
decl_stmt|;
name|String
name|vr
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_RANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|vr
operator|!=
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|VersionRange
argument_list|(
name|vr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|type
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|TYPE_FEATURE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
comment|// Check bundles
name|blacklist
argument_list|(
name|feature
operator|.
name|getBundle
argument_list|()
argument_list|,
name|clauses
argument_list|)
expr_stmt|;
comment|// Check conditional bundles
for|for
control|(
name|Conditional
name|cond
range|:
name|feature
operator|.
name|getConditional
argument_list|()
control|)
block|{
name|blacklist
argument_list|(
name|cond
operator|.
name|getBundle
argument_list|()
argument_list|,
name|clauses
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
name|void
name|blacklist
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|,
name|Clause
index|[]
name|clauses
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Bundle
argument_list|>
name|iterator
init|=
name|bundles
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Bundle
name|info
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
name|String
name|url
init|=
name|clause
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|String
name|type
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|TYPE_BUNDLE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|loadBlacklist
parameter_list|(
name|String
name|blacklistUrl
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|blacklist
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|blacklistUrl
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|blacklistUrl
argument_list|)
operator|.
name|openStream
argument_list|()
init|)
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|blacklist
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Unable to load blacklist bundles list"
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Unable to load blacklist bundles list"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|blacklist
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isFeatureBlacklisted
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|blacklist
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseClauses
argument_list|(
name|blacklist
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|blacklist
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
if|if
condition|(
name|clause
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// Check feature version
name|VersionRange
name|range
init|=
name|VersionRange
operator|.
name|ANY_VERSION
decl_stmt|;
name|String
name|vr
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_RANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|vr
operator|!=
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|VersionRange
argument_list|(
name|vr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|version
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|type
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|TYPE_FEATURE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isBundleBlacklisted
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|blacklist
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
return|return
name|isBlacklisted
argument_list|(
name|blacklist
argument_list|,
name|uri
argument_list|,
name|TYPE_BUNDLE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isBlacklisted
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|blacklist
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|btype
parameter_list|)
block|{
name|Clause
index|[]
name|clauses
init|=
name|Parser
operator|.
name|parseClauses
argument_list|(
name|blacklist
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|blacklist
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|isBlacklisted
argument_list|(
name|clauses
argument_list|,
name|uri
argument_list|,
name|btype
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isBlacklisted
parameter_list|(
name|Clause
index|[]
name|clauses
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|btype
parameter_list|)
block|{
for|for
control|(
name|Clause
name|clause
range|:
name|clauses
control|)
block|{
name|String
name|url
init|=
name|clause
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|String
name|type
init|=
name|clause
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|btype
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

