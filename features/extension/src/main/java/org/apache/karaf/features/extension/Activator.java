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
name|extension
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|*
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
name|hooks
operator|.
name|resolver
operator|.
name|ResolverHook
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
name|hooks
operator|.
name|resolver
operator|.
name|ResolverHookFactory
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
name|HostNamespace
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
name|IdentityNamespace
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
name|*
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
name|Namespace
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
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardOpenOption
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
implements|,
name|ResolverHook
implements|,
name|SynchronousBundleListener
implements|,
name|ResolverHookFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WIRING_PATH
init|=
literal|"wiring"
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|wiring
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|bundleContext
operator|=
name|context
expr_stmt|;
name|load
argument_list|()
expr_stmt|;
name|context
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|==
literal|0
operator|&&
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|STARTED
condition|)
block|{
name|ServiceRegistration
argument_list|<
name|ResolverHookFactory
argument_list|>
name|registration
init|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|ResolverHookFactory
operator|.
name|class
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|wiring
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|id
lambda|->
name|bundleContext
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|adapt
argument_list|(
name|FrameworkWiring
operator|.
name|class
argument_list|)
operator|.
name|resolveBundles
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|RESOLVED
operator|||
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNRESOLVED
condition|)
block|{
synchronized|synchronized
init|(
name|wiring
init|)
block|{
name|long
name|id
init|=
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|RESOLVED
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|bw
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleWire
name|wire
range|:
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
operator|.
name|getRequiredWires
argument_list|(
literal|null
argument_list|)
control|)
block|{
name|bw
operator|.
name|put
argument_list|(
name|getRequirementId
argument_list|(
name|wire
operator|.
name|getRequirement
argument_list|()
argument_list|)
argument_list|,
name|getCapabilityId
argument_list|(
name|wire
operator|.
name|getCapability
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|wiring
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|bw
argument_list|)
expr_stmt|;
name|saveWiring
argument_list|(
name|id
argument_list|,
name|bw
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wiring
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|saveWiring
argument_list|(
name|id
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|filterResolvable
parameter_list|(
name|Collection
argument_list|<
name|BundleRevision
argument_list|>
name|candidates
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|filterSingletonCollisions
parameter_list|(
name|BundleCapability
name|singleton
parameter_list|,
name|Collection
argument_list|<
name|BundleCapability
argument_list|>
name|collisionCandidates
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|filterMatches
parameter_list|(
name|BundleRequirement
name|requirement
parameter_list|,
name|Collection
argument_list|<
name|BundleCapability
argument_list|>
name|candidates
parameter_list|)
block|{
name|long
name|sourceId
init|=
name|requirement
operator|.
name|getRevision
argument_list|()
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
decl_stmt|;
if|if
condition|(
name|isFragment
argument_list|(
name|requirement
operator|.
name|getRevision
argument_list|()
argument_list|)
operator|&&
operator|!
name|requirement
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|HostNamespace
operator|.
name|HOST_NAMESPACE
argument_list|)
condition|)
block|{
name|sourceId
operator|=
name|wiring
operator|.
name|get
argument_list|(
name|sourceId
argument_list|)
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|HostNamespace
operator|.
name|HOST_NAMESPACE
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getValue
argument_list|)
operator|.
name|mapToLong
argument_list|(
name|s
lambda|->
block|{
name|int
name|idx
operator|=
name|s
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
argument_list|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|s
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
block|)
function|.findFirst
parameter_list|()
function|.orElse
parameter_list|(
function|-1
block|)
class|;
end_class

begin_expr_stmt
unit|}         Map
operator|<
name|String
operator|,
name|String
operator|>
name|bw
operator|=
name|wiring
operator|.
name|get
argument_list|(
name|sourceId
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|String
name|cap
init|=
name|bw
operator|.
name|get
argument_list|(
name|getRequirementId
argument_list|(
name|requirement
argument_list|)
argument_list|)
decl_stmt|;
end_decl_stmt

begin_for
for|for
control|(
name|Iterator
argument_list|<
name|BundleCapability
argument_list|>
name|candIter
init|=
name|candidates
operator|.
name|iterator
argument_list|()
init|;
name|candIter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|BundleCapability
name|cand
init|=
name|candIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|cap
operator|!=
literal|null
operator|&&
operator|!
name|cap
operator|.
name|equals
argument_list|(
name|getCapabilityId
argument_list|(
name|cand
argument_list|)
argument_list|)
operator|||
name|cap
operator|==
literal|null
operator|&&
name|cand
operator|.
name|getRevision
argument_list|()
operator|!=
name|requirement
operator|.
name|getRevision
argument_list|()
condition|)
block|{
name|candIter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
end_for

begin_function
unit|}      @
name|Override
specifier|public
name|void
name|end
parameter_list|()
block|{     }
end_function

begin_function
annotation|@
name|Override
specifier|public
name|ResolverHook
name|begin
parameter_list|(
name|Collection
argument_list|<
name|BundleRevision
argument_list|>
name|triggers
parameter_list|)
block|{
return|return
name|this
return|;
block|}
end_function

begin_function
specifier|private
name|void
name|load
parameter_list|()
block|{
try|try
block|{
name|Path
name|dir
init|=
name|bundleContext
operator|.
name|getDataFile
argument_list|(
name|WIRING_PATH
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|dir
argument_list|)
expr_stmt|;
name|Files
operator|.
name|list
argument_list|(
name|dir
argument_list|)
operator|.
name|forEach
argument_list|(
name|p
lambda|->
block|{
name|String
name|name
init|=
name|p
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|matches
argument_list|(
literal|"[0-9]+"
argument_list|)
condition|)
block|{
try|try
init|(
name|BufferedReader
name|reader
init|=
name|Files
operator|.
name|newBufferedReader
argument_list|(
name|p
argument_list|)
init|)
block|{
name|long
name|id
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|key
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|val
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|wiring
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UncheckedIOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UncheckedIOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|private
name|void
name|saveWiring
parameter_list|(
name|long
name|id
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wiring
parameter_list|)
block|{
try|try
block|{
name|Path
name|dir
init|=
name|bundleContext
operator|.
name|getDataFile
argument_list|(
name|WIRING_PATH
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|dir
argument_list|)
expr_stmt|;
name|Path
name|file
init|=
name|dir
operator|.
name|resolve
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|wiring
operator|!=
literal|null
condition|)
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|file
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|BufferedWriter
name|fw
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|file
argument_list|,
name|StandardOpenOption
operator|.
name|TRUNCATE_EXISTING
argument_list|,
name|StandardOpenOption
operator|.
name|WRITE
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE
argument_list|)
init|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wire
range|:
name|wiring
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|fw
operator|.
name|append
argument_list|(
name|wire
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|fw
operator|.
name|append
argument_list|(
name|wire
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UncheckedIOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|private
name|String
name|getRequirementId
parameter_list|(
name|Requirement
name|requirement
parameter_list|)
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
name|Namespace
operator|.
name|REQUIREMENT_FILTER_DIRECTIVE
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
return|return
name|requirement
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"; "
operator|+
name|filter
return|;
block|}
else|else
block|{
return|return
name|requirement
operator|.
name|getNamespace
argument_list|()
return|;
block|}
block|}
end_function

begin_function
specifier|private
name|String
name|getCapabilityId
parameter_list|(
name|BundleCapability
name|capability
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|64
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|capability
operator|.
name|getRevision
argument_list|()
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|v
init|=
name|capability
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; version="
argument_list|)
operator|.
name|append
argument_list|(
name|v
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

begin_function
specifier|private
specifier|static
name|boolean
name|isFragment
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
if|if
condition|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
operator|.
name|equals
argument_list|(
name|cap
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|IdentityNamespace
operator|.
name|TYPE_FRAGMENT
operator|.
name|equals
argument_list|(
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IdentityNamespace
operator|.
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

unit|}
end_unit
