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
name|MalformedURLException
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
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
name|karaf
operator|.
name|features
operator|.
name|FeaturePattern
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
name|LocationPattern
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
comment|/**  * Helper class to deal with blacklisted features and bundles. It doesn't process JAXB model at all - it only  * provides information about repository/feature/bundle being blacklisted.  * The task of actual blacklisting (altering JAXB model) is performed in {@link FeaturesProcessor}  */
end_comment

begin_class
specifier|public
class|class
name|Blacklist
block|{
specifier|public
specifier|static
name|Logger
name|LOG
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
name|BLACKLIST_TYPE
init|=
literal|"type"
decl_stmt|;
comment|// null -> "feature"
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
name|Clause
index|[]
name|clauses
decl_stmt|;
specifier|private
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|repositoryBlacklist
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|FeaturePattern
argument_list|>
name|featureBlacklist
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|bundleBlacklist
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|Blacklist
parameter_list|()
block|{
name|this
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Blacklist
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|blacklist
parameter_list|)
block|{
name|this
operator|.
name|clauses
operator|=
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
expr_stmt|;
name|compileClauses
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Blacklist
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
init|;                 BufferedReader reader = new BufferedReader(new InputStreamReader(is)
block|)
block|)
block|{
name|reader
operator|.
name|lines
argument_list|()
comment|//
operator|.
name|map
argument_list|(
name|String
operator|::
name|trim
argument_list|)
comment|//
operator|.
name|filter
argument_list|(
name|line
lambda|->
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
argument_list|)
operator|.
name|forEach
argument_list|(
name|blacklist
operator|::
name|add
argument_list|)
expr_stmt|;
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
block|}
end_class

begin_expr_stmt
name|this
operator|.
name|clauses
operator|=
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
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|compileClauses
argument_list|()
expr_stmt|;
end_expr_stmt

begin_comment
unit|}
comment|/**      * Extracts blacklisting clauses related to bundles, features and repositories and changes them to more      * usable form.      */
end_comment

begin_function
unit|private
name|void
name|compileClauses
parameter_list|()
block|{
for|for
control|(
name|Clause
name|c
range|:
name|clauses
control|)
block|{
name|String
name|type
init|=
name|c
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
condition|)
block|{
name|String
name|url
init|=
name|c
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|||
name|c
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
comment|// some special rules from etc/blacklisted.properties
name|type
operator|=
name|TYPE_BUNDLE
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|TYPE_FEATURE
expr_stmt|;
block|}
block|}
name|String
name|location
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|TYPE_REPOSITORY
case|:
name|location
operator|=
name|c
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|location
operator|=
name|c
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
comment|// should not happen?
name|LOG
operator|.
name|warn
argument_list|(
literal|"Repository blacklist URI is empty. Ignoring."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|repositoryBlacklist
operator|.
name|add
argument_list|(
operator|new
name|LocationPattern
argument_list|(
name|location
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem parsing repository blacklist URI \""
operator|+
name|location
operator|+
literal|"\": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
case|case
name|TYPE_FEATURE
case|:
try|try
block|{
name|featureBlacklist
operator|.
name|add
argument_list|(
operator|new
name|FeaturePattern
argument_list|(
name|c
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem parsing blacklisted feature identifier \""
operator|+
name|c
operator|.
name|toString
argument_list|()
operator|+
literal|"\": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|TYPE_BUNDLE
case|:
name|location
operator|=
name|c
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|location
operator|=
name|c
operator|.
name|getAttribute
argument_list|(
name|BLACKLIST_URL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
comment|// should not happen?
name|LOG
operator|.
name|warn
argument_list|(
literal|"Bundle blacklist URI is empty. Ignoring."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|bundleBlacklist
operator|.
name|add
argument_list|(
operator|new
name|LocationPattern
argument_list|(
name|location
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem parsing bundle blacklist URI \""
operator|+
name|location
operator|+
literal|"\": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
block|}
block|}
block|}
end_function

begin_comment
comment|/**      * Checks whether features XML repository URI is blacklisted.      * @param uri      * @return      */
end_comment

begin_function
specifier|public
name|boolean
name|isRepositoryBlacklisted
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
for|for
control|(
name|LocationPattern
name|pattern
range|:
name|repositoryBlacklist
control|)
block|{
if|if
condition|(
name|pattern
operator|.
name|matches
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_comment
comment|/**      * Checks whether the feature is blacklisted according to configured rules by name      * (possibly with wildcards) and optional version (possibly specified as version range)      * @param name      * @param version      * @return      */
end_comment

begin_function
specifier|public
name|boolean
name|isFeatureBlacklisted
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
for|for
control|(
name|FeaturePattern
name|pattern
range|:
name|featureBlacklist
control|)
block|{
if|if
condition|(
name|pattern
operator|.
name|matches
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_comment
comment|/**      * Checks whether the bundle URI is blacklisted according to configured rules      * @param uri      * @return      */
end_comment

begin_function
specifier|public
name|boolean
name|isBundleBlacklisted
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
for|for
control|(
name|LocationPattern
name|pattern
range|:
name|bundleBlacklist
control|)
block|{
if|if
condition|(
name|pattern
operator|.
name|matches
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_comment
comment|/**      * Merge clauses from another {@link Blacklist} into this object      * @param others      */
end_comment

begin_function
specifier|public
name|void
name|merge
parameter_list|(
name|Blacklist
name|others
parameter_list|)
block|{
name|Clause
index|[]
name|ours
init|=
name|this
operator|.
name|clauses
decl_stmt|;
if|if
condition|(
name|ours
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|clauses
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|others
operator|.
name|clauses
argument_list|,
name|others
operator|.
name|clauses
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|others
operator|!=
literal|null
operator|&&
name|others
operator|.
name|clauses
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|clauses
operator|=
operator|new
name|Clause
index|[
name|ours
operator|.
name|length
operator|+
name|others
operator|.
name|clauses
operator|.
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|ours
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|clauses
argument_list|,
literal|0
argument_list|,
name|ours
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|others
operator|.
name|clauses
argument_list|,
name|ours
operator|.
name|length
argument_list|,
name|this
operator|.
name|clauses
argument_list|,
literal|0
argument_list|,
name|others
operator|.
name|clauses
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|others
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|repositoryBlacklist
operator|.
name|addAll
argument_list|(
name|others
operator|.
name|repositoryBlacklist
argument_list|)
expr_stmt|;
name|this
operator|.
name|featureBlacklist
operator|.
name|addAll
argument_list|(
name|others
operator|.
name|featureBlacklist
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleBlacklist
operator|.
name|addAll
argument_list|(
name|others
operator|.
name|bundleBlacklist
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
specifier|public
name|Clause
index|[]
name|getClauses
parameter_list|()
block|{
return|return
name|clauses
return|;
block|}
end_function

begin_function
specifier|public
name|void
name|blacklist
parameter_list|(
name|Features
name|featuresModel
parameter_list|)
block|{     }
end_function

begin_comment
comment|/**      * Directly add {@link LocationPattern} as blacklisted features XML repository URI      * @param locationPattern      */
end_comment

begin_function
specifier|public
name|void
name|blacklistRepository
parameter_list|(
name|LocationPattern
name|locationPattern
parameter_list|)
block|{
name|repositoryBlacklist
operator|.
name|add
argument_list|(
name|locationPattern
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**      * Directly add {@link FeaturePattern} as blacklisted feature ID      * @param featurePattern      */
end_comment

begin_function
specifier|public
name|void
name|blacklistFeature
parameter_list|(
name|FeaturePattern
name|featurePattern
parameter_list|)
block|{
name|featureBlacklist
operator|.
name|add
argument_list|(
name|featurePattern
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**      * Directly add {@link LocationPattern} as blacklisted bundle URI      * @param locationPattern      */
end_comment

begin_function
specifier|public
name|void
name|blacklistBundle
parameter_list|(
name|LocationPattern
name|locationPattern
parameter_list|)
block|{
name|bundleBlacklist
operator|.
name|add
argument_list|(
name|locationPattern
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|public
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|getRepositoryBlacklist
parameter_list|()
block|{
return|return
name|repositoryBlacklist
return|;
block|}
end_function

begin_function
specifier|public
name|List
argument_list|<
name|FeaturePattern
argument_list|>
name|getFeatureBlacklist
parameter_list|()
block|{
return|return
name|featureBlacklist
return|;
block|}
end_function

begin_function
specifier|public
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|getBundleBlacklist
parameter_list|()
block|{
return|return
name|bundleBlacklist
return|;
block|}
end_function

unit|}
end_unit

