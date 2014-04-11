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
name|HashSet
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

begin_class
specifier|public
class|class
name|RequirementSort
parameter_list|<
name|T
extends|extends
name|Resource
parameter_list|>
block|{
comment|/**      * Sorts {@link Resource} based on their {@link Requirement}s and {@link Capability}s.      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Resource
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|sort
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|resources
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Resource
name|r
range|:
name|resources
control|)
block|{
for|for
control|(
name|Capability
name|cap
range|:
name|r
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
control|)
block|{
name|namespaces
operator|.
name|add
argument_list|(
name|cap
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|CapabilitySet
name|capSet
init|=
operator|new
name|CapabilitySet
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|namespaces
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|r
range|:
name|resources
control|)
block|{
for|for
control|(
name|Capability
name|cap
range|:
name|r
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
control|)
block|{
name|capSet
operator|.
name|addCapability
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
block|}
name|Set
argument_list|<
name|T
argument_list|>
name|sorted
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|T
argument_list|>
name|visited
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|r
range|:
name|resources
control|)
block|{
name|visit
argument_list|(
name|r
argument_list|,
name|visited
argument_list|,
name|sorted
argument_list|,
name|capSet
argument_list|)
expr_stmt|;
block|}
return|return
name|sorted
return|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Resource
parameter_list|>
name|void
name|visit
parameter_list|(
name|T
name|resource
parameter_list|,
name|Set
argument_list|<
name|T
argument_list|>
name|visited
parameter_list|,
name|Set
argument_list|<
name|T
argument_list|>
name|sorted
parameter_list|,
name|CapabilitySet
name|capSet
parameter_list|)
block|{
if|if
condition|(
operator|!
name|visited
operator|.
name|add
argument_list|(
name|resource
argument_list|)
condition|)
block|{
return|return;
block|}
for|for
control|(
name|T
name|r
range|:
name|collectDependencies
argument_list|(
name|resource
argument_list|,
name|capSet
argument_list|)
control|)
block|{
name|visit
argument_list|(
name|r
argument_list|,
name|visited
argument_list|,
name|sorted
argument_list|,
name|capSet
argument_list|)
expr_stmt|;
block|}
name|sorted
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Resource
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|collectDependencies
parameter_list|(
name|T
name|resource
parameter_list|,
name|CapabilitySet
name|capSet
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Requirement
name|requirement
range|:
name|resource
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
control|)
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
name|SimpleFilter
name|sf
init|=
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
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|capSet
operator|.
name|match
argument_list|(
name|sf
argument_list|,
literal|true
argument_list|)
control|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|(
name|T
operator|)
name|cap
operator|.
name|getResource
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

