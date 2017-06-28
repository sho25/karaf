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
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|FeaturesService
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
name|Repository
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
name|lifecycle
operator|.
name|Reference
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|FeaturesCommandSupport
implements|implements
name|Action
block|{
specifier|protected
name|EnumSet
argument_list|<
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|FeaturesService
name|featuresService
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
if|if
condition|(
name|featuresService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"FeaturesService not found"
argument_list|)
throw|;
block|}
name|doExecute
argument_list|(
name|featuresService
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|admin
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
specifier|protected
name|void
name|addOption
parameter_list|(
name|FeaturesService
operator|.
name|Option
name|option
parameter_list|,
name|boolean
name|shouldAdd
parameter_list|)
block|{
if|if
condition|(
name|shouldAdd
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Set
argument_list|<
name|URI
argument_list|>
name|selectRepositories
parameter_list|(
name|String
name|nameOrUrl
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|URI
argument_list|>
name|uris
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|effectiveVersion
init|=
operator|(
name|version
operator|==
literal|null
operator|)
condition|?
literal|"LATEST"
else|:
name|version
decl_stmt|;
name|URI
name|uri
init|=
name|featuresService
operator|.
name|getRepositoryUriFor
argument_list|(
name|nameOrUrl
argument_list|,
name|effectiveVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
comment|// add regex support on installed repositories
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|nameOrUrl
argument_list|)
decl_stmt|;
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
name|URI
name|u
init|=
name|repository
operator|.
name|getURI
argument_list|()
decl_stmt|;
name|String
name|rname
init|=
name|repository
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|.
name|matcher
argument_list|(
name|u
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
operator|||
name|rname
operator|!=
literal|null
operator|&&
name|pattern
operator|.
name|matcher
argument_list|(
name|rname
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|uris
operator|.
name|add
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|uris
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|uris
return|;
block|}
specifier|protected
name|String
name|getFeatureId
parameter_list|(
name|FeaturesService
name|admin
parameter_list|,
name|String
name|nameOrId
parameter_list|)
throws|throws
name|Exception
block|{
name|Feature
index|[]
name|matchingFeatures
init|=
name|admin
operator|.
name|getFeatures
argument_list|(
name|nameOrId
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchingFeatures
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No matching feature found for "
operator|+
name|nameOrId
argument_list|)
throw|;
block|}
if|if
condition|(
name|matchingFeatures
operator|.
name|length
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"More than one matching feature found for "
operator|+
name|nameOrId
argument_list|)
throw|;
block|}
return|return
name|matchingFeatures
index|[
literal|0
index|]
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
end_class

end_unit

