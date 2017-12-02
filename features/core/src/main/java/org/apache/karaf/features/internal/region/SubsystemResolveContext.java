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
name|region
package|;
end_package

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
name|Comparator
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|internal
operator|.
name|download
operator|.
name|Downloader
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
name|repository
operator|.
name|BaseRepository
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
name|CapabilityImpl
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
name|ResolverUtil
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
name|ResourceImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|Region
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionFilter
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
name|namespace
operator|.
name|PackageNamespace
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
name|wiring
operator|.
name|BundleRevision
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|namespace
operator|.
name|service
operator|.
name|ServiceNamespace
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
name|resource
operator|.
name|Wiring
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
name|osgi
operator|.
name|service
operator|.
name|resolver
operator|.
name|HostedCapability
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
name|resolver
operator|.
name|ResolveContext
import|;
end_import

begin_import
import|import static
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
name|ResourceUtils
operator|.
name|addIdentityRequirement
import|;
end_import

begin_import
import|import static
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
name|ResourceUtils
operator|.
name|getUri
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionFilter
operator|.
name|VISIBLE_BUNDLE_NAMESPACE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME_ATTRIBUTE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
operator|.
name|BUNDLE_VERSION_ATTRIBUTE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
operator|.
name|RESOLUTION_DIRECTIVE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
operator|.
name|RESOLUTION_OPTIONAL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Namespace
operator|.
name|REQUIREMENT_RESOLUTION_DIRECTIVE
import|;
end_import

begin_class
specifier|public
class|class
name|SubsystemResolveContext
extends|extends
name|ResolveContext
block|{
specifier|private
specifier|final
name|Subsystem
name|root
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Region
argument_list|>
name|regions
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Resource
argument_list|,
name|Integer
argument_list|>
name|distance
decl_stmt|;
specifier|private
specifier|final
name|CandidateComparator
name|candidateComparator
init|=
operator|new
name|CandidateComparator
argument_list|(
name|this
operator|::
name|getResourceCost
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Resource
argument_list|,
name|Subsystem
argument_list|>
name|resToSub
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Repository
name|repository
decl_stmt|;
specifier|private
specifier|final
name|Repository
name|globalRepository
decl_stmt|;
specifier|private
specifier|final
name|Downloader
name|downloader
decl_stmt|;
specifier|private
specifier|final
name|FeaturesService
operator|.
name|ServiceRequirementsBehavior
name|serviceRequirements
decl_stmt|;
specifier|public
name|SubsystemResolveContext
parameter_list|(
name|Subsystem
name|root
parameter_list|,
name|RegionDigraph
name|digraph
parameter_list|,
name|Repository
name|globalRepository
parameter_list|,
name|Downloader
name|downloader
parameter_list|,
name|FeaturesService
operator|.
name|ServiceRequirementsBehavior
name|serviceRequirements
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
name|this
operator|.
name|globalRepository
operator|=
name|globalRepository
operator|!=
literal|null
condition|?
operator|new
name|SubsystemRepository
argument_list|(
name|globalRepository
argument_list|)
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|downloader
operator|=
name|downloader
expr_stmt|;
name|this
operator|.
name|serviceRequirements
operator|=
name|serviceRequirements
expr_stmt|;
name|prepare
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|repository
operator|=
operator|new
name|BaseRepository
argument_list|(
name|resToSub
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|regions
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Region
name|region
range|:
name|digraph
control|)
block|{
name|regions
operator|.
name|put
argument_list|(
name|region
operator|.
name|getName
argument_list|()
argument_list|,
name|region
argument_list|)
expr_stmt|;
block|}
comment|// Add a heuristic to sort capabilities :
comment|//  if a capability comes from a resource which needs to be installed,
comment|//  prefer that one over any capabilities from other resources
name|distance
operator|=
name|computeDistances
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|Repository
name|getGlobalRepository
parameter_list|()
block|{
return|return
name|globalRepository
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Resource
argument_list|,
name|Integer
argument_list|>
name|computeDistances
parameter_list|(
name|Resource
name|root
parameter_list|)
block|{
name|Map
argument_list|<
name|Resource
argument_list|,
name|Integer
argument_list|>
name|distance
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Resource
argument_list|>
name|settledNodes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|distance
operator|.
name|put
argument_list|(
name|root
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Resource
argument_list|>
name|unSettledNodes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|unSettledNodes
operator|.
name|add
argument_list|(
name|root
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|unSettledNodes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|unSettledNodes
operator|.
name|sort
argument_list|(
name|Comparator
operator|.
name|comparingInt
argument_list|(
name|r
lambda|->
name|distance
operator|.
name|getOrDefault
argument_list|(
name|r
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Resource
name|node
init|=
name|unSettledNodes
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|settledNodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|Resource
argument_list|,
name|Integer
argument_list|>
name|edge
init|=
name|computeEdges
argument_list|(
name|node
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|target
range|:
name|edge
operator|.
name|keySet
argument_list|()
control|)
block|{
name|int
name|d
init|=
name|distance
operator|.
name|getOrDefault
argument_list|(
name|node
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
operator|+
name|edge
operator|.
name|get
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|distance
operator|.
name|merge
argument_list|(
name|target
argument_list|,
name|d
argument_list|,
name|Math
operator|::
name|min
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|settledNodes
operator|.
name|contains
argument_list|(
name|target
argument_list|)
condition|)
block|{
name|unSettledNodes
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|distance
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Resource
argument_list|,
name|Integer
argument_list|>
name|computeEdges
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|Map
argument_list|<
name|Resource
argument_list|,
name|Integer
argument_list|>
name|edges
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|owner
init|=
name|ResolverUtil
operator|.
name|getOwnerName
argument_list|(
name|resource
argument_list|)
decl_stmt|;
for|for
control|(
name|Requirement
name|req
range|:
name|resource
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
control|)
block|{
if|if
condition|(
name|isOptional
argument_list|(
name|req
argument_list|)
operator|||
name|isDynamic
argument_list|(
name|req
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
name|findProviders
argument_list|(
name|req
argument_list|)
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|caps
control|)
block|{
name|Resource
name|r
init|=
name|cap
operator|.
name|getResource
argument_list|()
decl_stmt|;
comment|// If there's a single provider for any kind of mandatory requirement,
comment|// this means the resource is also mandatory.
comment|// Else prefer resources from the same subsystem (with the same owner).
name|int
name|v
init|=
operator|(
name|caps
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|)
condition|?
literal|0
else|:
operator|(
name|Objects
operator|.
name|equals
argument_list|(
name|ResolverUtil
operator|.
name|getOwnerName
argument_list|(
name|r
argument_list|)
argument_list|,
name|owner
argument_list|)
operator|)
condition|?
literal|1
else|:
literal|10
decl_stmt|;
name|edges
operator|.
name|merge
argument_list|(
name|r
argument_list|,
name|v
argument_list|,
name|Math
operator|::
name|min
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|edges
return|;
block|}
specifier|private
name|int
name|getResourceCost
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
return|return
name|distance
operator|.
name|getOrDefault
argument_list|(
name|resource
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
return|;
block|}
specifier|static
name|boolean
name|isOptional
parameter_list|(
name|Requirement
name|req
parameter_list|)
block|{
name|String
name|resolution
init|=
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|)
decl_stmt|;
return|return
name|RESOLUTION_OPTIONAL
operator|.
name|equals
argument_list|(
name|resolution
argument_list|)
return|;
block|}
specifier|static
name|boolean
name|isDynamic
parameter_list|(
name|Requirement
name|req
parameter_list|)
block|{
name|String
name|resolution
init|=
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|)
decl_stmt|;
return|return
name|PackageNamespace
operator|.
name|RESOLUTION_DYNAMIC
operator|.
name|equals
argument_list|(
name|resolution
argument_list|)
return|;
block|}
comment|/**      * {@link #resToSub} will quickly map all {@link Subsystem#getInstallable() installable resources} to their      * {@link Subsystem}      * @param subsystem      */
name|void
name|prepare
parameter_list|(
name|Subsystem
name|subsystem
parameter_list|)
block|{
name|resToSub
operator|.
name|put
argument_list|(
name|subsystem
argument_list|,
name|subsystem
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|res
range|:
name|subsystem
operator|.
name|getInstallable
argument_list|()
control|)
block|{
name|resToSub
operator|.
name|put
argument_list|(
name|res
argument_list|,
name|subsystem
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Subsystem
name|child
range|:
name|subsystem
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|prepare
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Resource
argument_list|>
name|getMandatoryResources
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|root
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Capability
argument_list|>
name|findProviders
parameter_list|(
name|Requirement
name|requirement
parameter_list|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Region
name|requirerRegion
init|=
name|getRegion
argument_list|(
name|requirement
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|requirerRegion
operator|!=
literal|null
condition|)
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
name|resMap
init|=
name|repository
operator|.
name|findProviders
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|requirement
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Capability
argument_list|>
name|res
init|=
name|resMap
operator|!=
literal|null
condition|?
name|resMap
operator|.
name|get
argument_list|(
name|requirement
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|res
operator|!=
literal|null
operator|&&
operator|!
name|res
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|caps
operator|.
name|addAll
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|globalRepository
operator|!=
literal|null
condition|)
block|{
comment|// Only bring in external resources for non optional requirements
if|if
condition|(
operator|!
name|RESOLUTION_OPTIONAL
operator|.
name|equals
argument_list|(
name|requirement
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|RESOLUTION_DIRECTIVE
argument_list|)
argument_list|)
condition|)
block|{
name|resMap
operator|=
name|globalRepository
operator|.
name|findProviders
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|requirement
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|=
name|resMap
operator|!=
literal|null
condition|?
name|resMap
operator|.
name|get
argument_list|(
name|requirement
argument_list|)
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|res
operator|!=
literal|null
operator|&&
operator|!
name|res
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|caps
operator|.
name|addAll
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Use the digraph to prune non visible capabilities
name|Visitor
name|visitor
init|=
operator|new
name|Visitor
argument_list|(
name|caps
argument_list|)
decl_stmt|;
name|requirerRegion
operator|.
name|visitSubgraph
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Capability
argument_list|>
name|allowed
init|=
name|visitor
operator|.
name|getAllowed
argument_list|()
decl_stmt|;
name|caps
operator|.
name|retainAll
argument_list|(
name|allowed
argument_list|)
expr_stmt|;
comment|// Handle cases where the same bundle is requested from both
comment|// a subsystem and one of its ascendant.  In such cases, we
comment|// need to remove the one from the child if it can view
comment|// the parent one
if|if
condition|(
name|caps
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Set
argument_list|<
name|Resource
argument_list|>
name|providers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|caps
control|)
block|{
name|Resource
name|resource
init|=
name|cap
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|ResolverUtil
operator|.
name|getSymbolicName
argument_list|(
name|resource
argument_list|)
operator|+
literal|"|"
operator|+
name|ResolverUtil
operator|.
name|getVersion
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|providers
operator|.
name|contains
argument_list|(
name|resource
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|Resource
argument_list|>
name|oldRes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|providers
argument_list|)
decl_stmt|;
name|providers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|String
name|r1
init|=
name|getRegion
argument_list|(
name|resource
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|superceded
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Resource
name|r
range|:
name|oldRes
control|)
block|{
name|String
name|id2
init|=
name|ResolverUtil
operator|.
name|getSymbolicName
argument_list|(
name|r
argument_list|)
operator|+
literal|"|"
operator|+
name|ResolverUtil
operator|.
name|getVersion
argument_list|(
name|r
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|id2
argument_list|)
condition|)
block|{
name|String
name|r2
init|=
name|getRegion
argument_list|(
name|r
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|r1
operator|.
name|equals
argument_list|(
name|r2
argument_list|)
condition|)
block|{
if|if
condition|(
name|r
operator|instanceof
name|BundleRevision
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|superceded
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resource
operator|instanceof
name|BundleRevision
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InternalError
argument_list|()
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|r1
operator|.
name|startsWith
argument_list|(
name|r2
argument_list|)
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|superceded
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|r2
operator|.
name|startsWith
argument_list|(
name|r1
argument_list|)
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|providers
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|providers
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|superceded
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|caps
operator|.
name|removeIf
argument_list|(
name|cap
lambda|->
operator|!
name|providers
operator|.
name|contains
argument_list|(
name|cap
operator|.
name|getResource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Sort caps
if|if
condition|(
name|distance
operator|!=
literal|null
operator|&&
name|caps
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|caps
operator|.
name|sort
argument_list|(
name|candidateComparator
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|caps
return|;
block|}
specifier|private
name|Subsystem
name|getSubsystem
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
return|return
name|resToSub
operator|.
name|get
argument_list|(
name|resource
argument_list|)
return|;
block|}
specifier|private
name|Region
name|getRegion
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
return|return
name|regions
operator|.
name|get
argument_list|(
name|getSubsystem
argument_list|(
name|resource
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|insertHostedCapability
parameter_list|(
name|List
argument_list|<
name|Capability
argument_list|>
name|capabilities
parameter_list|,
name|HostedCapability
name|hostedCapability
parameter_list|)
block|{
name|int
name|idx
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|capabilities
argument_list|,
name|hostedCapability
argument_list|,
name|candidateComparator
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|idx
operator|=
name|Math
operator|.
name|abs
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|capabilities
operator|.
name|add
argument_list|(
name|idx
argument_list|,
name|hostedCapability
argument_list|)
expr_stmt|;
return|return
name|idx
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEffective
parameter_list|(
name|Requirement
name|requirement
parameter_list|)
block|{
name|boolean
name|isServiceReq
init|=
name|ServiceNamespace
operator|.
name|SERVICE_NAMESPACE
operator|.
name|equals
argument_list|(
name|requirement
operator|.
name|getNamespace
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|!
operator|(
name|isServiceReq
operator|&&
name|FeaturesService
operator|.
name|ServiceRequirementsBehavior
operator|.
name|Disable
operator|==
name|serviceRequirements
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Resource
argument_list|,
name|Wiring
argument_list|>
name|getWirings
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
class|class
name|Visitor
extends|extends
name|AbstractRegionDigraphVisitor
argument_list|<
name|Capability
argument_list|>
block|{
name|Visitor
parameter_list|(
name|Collection
argument_list|<
name|Capability
argument_list|>
name|candidates
parameter_list|)
block|{
name|super
argument_list|(
name|candidates
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|contains
parameter_list|(
name|Region
name|region
parameter_list|,
name|Capability
name|candidate
parameter_list|)
block|{
return|return
name|region
operator|.
name|equals
argument_list|(
name|getRegion
argument_list|(
name|candidate
operator|.
name|getResource
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isAllowed
parameter_list|(
name|Capability
name|candidate
parameter_list|,
name|RegionFilter
name|filter
parameter_list|)
block|{
if|if
condition|(
name|filter
operator|.
name|isAllowed
argument_list|(
name|candidate
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|candidate
operator|.
name|getAttributes
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Resource
name|resource
init|=
name|candidate
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Capability
argument_list|>
name|identities
init|=
name|resource
operator|.
name|getCapabilities
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|identities
operator|!=
literal|null
operator|&&
operator|!
name|identities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Capability
name|identity
init|=
name|identities
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|BUNDLE_SYMBOLICNAME_ATTRIBUTE
argument_list|,
name|identity
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|BUNDLE_VERSION_ATTRIBUTE
argument_list|,
name|identity
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|filter
operator|.
name|isAllowed
argument_list|(
name|VISIBLE_BUNDLE_NAMESPACE
argument_list|,
name|attrs
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
class|class
name|SubsystemRepository
implements|implements
name|Repository
block|{
specifier|private
specifier|final
name|Repository
name|repository
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Subsystem
argument_list|,
name|Map
argument_list|<
name|Capability
argument_list|,
name|Capability
argument_list|>
argument_list|>
name|mapping
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|SubsystemRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
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
name|base
init|=
name|repository
operator|.
name|findProviders
argument_list|(
name|requirements
argument_list|)
decl_stmt|;
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
name|Map
operator|.
name|Entry
argument_list|<
name|Requirement
argument_list|,
name|Collection
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|entry
range|:
name|base
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Subsystem
name|ss
init|=
name|getSubsystem
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|ss
operator|.
name|isAcceptDependencies
argument_list|()
condition|)
block|{
name|ss
operator|=
name|ss
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
name|Map
argument_list|<
name|Capability
argument_list|,
name|Capability
argument_list|>
name|map
init|=
name|mapping
operator|.
name|computeIfAbsent
argument_list|(
name|ss
argument_list|,
name|k
lambda|->
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|Capability
name|wrapped
init|=
name|map
operator|.
name|get
argument_list|(
name|cap
argument_list|)
decl_stmt|;
if|if
condition|(
name|wrapped
operator|==
literal|null
condition|)
block|{
name|wrap
argument_list|(
name|map
argument_list|,
name|ss
argument_list|,
name|cap
operator|.
name|getResource
argument_list|()
argument_list|)
expr_stmt|;
name|wrapped
operator|=
name|map
operator|.
name|get
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
name|caps
operator|.
name|add
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|caps
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|void
name|wrap
parameter_list|(
name|Map
argument_list|<
name|Capability
argument_list|,
name|Capability
argument_list|>
name|map
parameter_list|,
name|Subsystem
name|subsystem
parameter_list|,
name|Resource
name|resource
parameter_list|)
block|{
name|ResourceImpl
name|wrapped
init|=
operator|new
name|ResourceImpl
argument_list|()
decl_stmt|;
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
name|CapabilityImpl
name|wCap
init|=
operator|new
name|CapabilityImpl
argument_list|(
name|wrapped
argument_list|,
name|cap
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|cap
operator|.
name|getDirectives
argument_list|()
argument_list|,
name|cap
operator|.
name|getAttributes
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|cap
argument_list|,
name|wCap
argument_list|)
expr_stmt|;
name|wrapped
operator|.
name|addCapability
argument_list|(
name|wCap
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Requirement
name|req
range|:
name|resource
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
control|)
block|{
name|RequirementImpl
name|wReq
init|=
operator|new
name|RequirementImpl
argument_list|(
name|wrapped
argument_list|,
name|req
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|req
operator|.
name|getDirectives
argument_list|()
argument_list|,
name|req
operator|.
name|getAttributes
argument_list|()
argument_list|)
decl_stmt|;
name|wrapped
operator|.
name|addRequirement
argument_list|(
name|wReq
argument_list|)
expr_stmt|;
block|}
name|addIdentityRequirement
argument_list|(
name|wrapped
argument_list|,
name|subsystem
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|resToSub
operator|.
name|put
argument_list|(
name|wrapped
argument_list|,
name|subsystem
argument_list|)
expr_stmt|;
try|try
block|{
name|downloader
operator|.
name|download
argument_list|(
name|getUri
argument_list|(
name|wrapped
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to download resource: "
operator|+
name|getUri
argument_list|(
name|wrapped
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

