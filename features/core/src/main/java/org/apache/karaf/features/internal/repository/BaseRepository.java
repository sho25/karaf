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
name|repository
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
name|Collection
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
name|HashMap
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
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|resolver
operator|.
name|CapabilitySet
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
name|resolver
operator|.
name|RequirementImpl
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
name|resolver
operator|.
name|SimpleFilter
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
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
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
name|resource
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
name|resource
operator|.
name|Resource
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
name|repository
operator|.
name|Repository
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
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|BaseRepository
implements|implements
name|Repository
block|{
specifier|protected
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|List
argument_list|<
name|Resource
argument_list|>
name|resources
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|CapabilitySet
argument_list|>
name|capSets
decl_stmt|;
specifier|public
name|BaseRepository
parameter_list|()
block|{
name|this
operator|.
name|resources
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|capSets
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|BaseRepository
parameter_list|(
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
for|for
control|(
name|Resource
name|resource
range|:
name|resources
control|)
block|{
name|addResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|addResource
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
for|for
control|(
name|Capability
name|cap
range|:
name|resource
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
control|)
block|{
name|String
name|ns
init|=
name|cap
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
name|capSets
operator|.
name|computeIfAbsent
argument_list|(
name|ns
argument_list|,
name|n
lambda|->
operator|new
name|CapabilitySet
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|n
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addCapability
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
name|resources
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Resource
argument_list|>
name|getResources
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Requirement
argument_list|,
name|Collection
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|findProviders
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Requirement
argument_list|>
name|requirements
parameter_list|)
block|{
name|Map
argument_list|<
name|Requirement
argument_list|,
name|Collection
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Requirement
name|requirement
range|:
name|requirements
control|)
block|{
name|CapabilitySet
name|set
init|=
name|capSets
operator|.
name|get
argument_list|(
name|requirement
operator|.
name|getNamespace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|set
operator|!=
literal|null
condition|)
block|{
name|SimpleFilter
name|sf
decl_stmt|;
if|if
condition|(
name|requirement
operator|instanceof
name|RequirementImpl
condition|)
block|{
name|sf
operator|=
operator|(
operator|(
name|RequirementImpl
operator|)
name|requirement
operator|)
operator|.
name|getFilter
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|filter
init|=
name|requirement
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|FILTER_DIRECTIVE
argument_list|)
decl_stmt|;
name|sf
operator|=
operator|(
name|filter
operator|!=
literal|null
operator|)
condition|?
name|SimpleFilter
operator|.
name|parse
argument_list|(
name|filter
argument_list|)
else|:
operator|new
name|SimpleFilter
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|SimpleFilter
operator|.
name|MATCH_ALL
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|put
argument_list|(
name|requirement
argument_list|,
name|set
operator|.
name|match
argument_list|(
name|sf
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|put
argument_list|(
name|requirement
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

