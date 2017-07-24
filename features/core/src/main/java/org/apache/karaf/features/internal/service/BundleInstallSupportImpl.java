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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|concurrent
operator|.
name|CountDownLatch
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
name|internal
operator|.
name|region
operator|.
name|DigraphHelper
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
name|util
operator|.
name|bundles
operator|.
name|BundleUtils
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
name|RegionFilterBuilder
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
name|Bundle
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
name|BundleContext
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
name|BundleException
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
name|FrameworkEvent
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
name|FrameworkListener
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
name|ServiceRegistration
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
name|ExecutionEnvironmentNamespace
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
name|startlevel
operator|.
name|BundleStartLevel
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
name|startlevel
operator|.
name|FrameworkStartLevel
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
name|BundleCapability
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
name|BundleRequirement
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
name|framework
operator|.
name|wiring
operator|.
name|FrameworkWiring
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
name|Wire
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

begin_class
specifier|public
class|class
name|BundleInstallSupportImpl
implements|implements
name|BundleInstallSupport
block|{
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
name|BundleInstallSupportImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|RegionDigraph
name|digraph
decl_stmt|;
specifier|private
specifier|final
name|Bundle
name|ourBundle
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|ourBundleContext
decl_stmt|;
specifier|private
specifier|final
name|FeatureConfigInstaller
name|configInstaller
decl_stmt|;
comment|/**      * The system bundle context.      * For all bundles related operations, we use the system bundle context      * to allow this bundle to be stopped and still allow the deployment to      * take place.      */
specifier|private
specifier|final
name|BundleContext
name|systemBundleContext
decl_stmt|;
specifier|public
name|BundleInstallSupportImpl
parameter_list|(
name|Bundle
name|ourBundle
parameter_list|,
name|BundleContext
name|ourBundleContext
parameter_list|,
name|BundleContext
name|systemBundleContext
parameter_list|,
name|FeatureConfigInstaller
name|configInstaller
parameter_list|,
name|RegionDigraph
name|digraph
parameter_list|)
block|{
name|this
operator|.
name|ourBundle
operator|=
name|ourBundle
expr_stmt|;
name|this
operator|.
name|ourBundleContext
operator|=
name|ourBundleContext
expr_stmt|;
name|this
operator|.
name|systemBundleContext
operator|=
name|systemBundleContext
expr_stmt|;
name|this
operator|.
name|configInstaller
operator|=
name|configInstaller
expr_stmt|;
name|this
operator|.
name|digraph
operator|=
name|digraph
expr_stmt|;
block|}
specifier|public
name|void
name|print
parameter_list|(
name|String
name|message
parameter_list|,
name|boolean
name|verbose
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|refreshPackages
parameter_list|(
name|Collection
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|InterruptedException
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|FrameworkWiring
name|fw
init|=
name|systemBundleContext
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
decl_stmt|;
name|fw
operator|.
name|refreshBundles
argument_list|(
name|bundles
argument_list|,
operator|(
name|FrameworkListener
operator|)
name|event
lambda|->
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|FrameworkEvent
operator|.
name|ERROR
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Framework error"
argument_list|,
name|event
operator|.
name|getThrowable
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Bundle
name|installBundle
parameter_list|(
name|String
name|region
parameter_list|,
name|String
name|uri
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|BundleException
block|{
if|if
condition|(
name|FeaturesService
operator|.
name|ROOT_REGION
operator|.
name|equals
argument_list|(
name|region
argument_list|)
condition|)
block|{
return|return
name|digraph
operator|.
name|getRegion
argument_list|(
name|region
argument_list|)
operator|.
name|installBundleAtLocation
argument_list|(
name|uri
argument_list|,
name|is
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|digraph
operator|.
name|getRegion
argument_list|(
name|region
argument_list|)
operator|.
name|installBundle
argument_list|(
name|uri
argument_list|,
name|is
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|String
name|uri
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|BundleException
block|{
comment|// We need to wrap the bundle to insert a Bundle-UpdateLocation header
try|try
block|{
name|File
name|file
init|=
name|BundleUtils
operator|.
name|fixBundleWithUpdateLocation
argument_list|(
name|is
argument_list|,
name|uri
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|update
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|file
operator|.
name|delete
argument_list|()
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
name|BundleException
argument_list|(
literal|"Unable to update bundle"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstall
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|BundleException
block|{
name|bundle
operator|.
name|uninstall
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|BundleException
block|{
if|if
condition|(
name|bundle
operator|!=
name|this
operator|.
name|ourBundle
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|!=
name|Bundle
operator|.
name|STARTING
condition|)
block|{
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stopBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|int
name|options
parameter_list|)
throws|throws
name|BundleException
block|{
name|bundle
operator|.
name|stop
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setBundleStartLevel
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|int
name|startLevel
parameter_list|)
block|{
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
operator|.
name|setStartLevel
argument_list|(
name|startLevel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|resolveBundles
parameter_list|(
name|Set
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|,
specifier|final
name|Map
argument_list|<
name|Resource
argument_list|,
name|List
argument_list|<
name|Wire
argument_list|>
argument_list|>
name|wiring
parameter_list|,
name|Map
argument_list|<
name|Resource
argument_list|,
name|Bundle
argument_list|>
name|resToBnd
parameter_list|)
block|{
comment|// Make sure it's only used for us
specifier|final
name|Thread
name|thread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
comment|// Translate wiring
specifier|final
name|Map
argument_list|<
name|Bundle
argument_list|,
name|Resource
argument_list|>
name|bndToRes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Resource
name|res
range|:
name|resToBnd
operator|.
name|keySet
argument_list|()
control|)
block|{
name|bndToRes
operator|.
name|put
argument_list|(
name|resToBnd
operator|.
name|get
argument_list|(
name|res
argument_list|)
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
comment|// Hook
specifier|final
name|ResolverHook
name|hook
init|=
operator|new
name|ResolverHook
argument_list|()
block|{
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
block|{             }
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
block|{             }
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
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|==
name|thread
condition|)
block|{
comment|// osgi.ee capabilities are provided by the system bundle, so just ignore those
if|if
condition|(
name|ExecutionEnvironmentNamespace
operator|.
name|EXECUTION_ENVIRONMENT_NAMESPACE
operator|.
name|equals
argument_list|(
name|requirement
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|Bundle
name|sourceBundle
init|=
name|requirement
operator|.
name|getRevision
argument_list|()
operator|.
name|getBundle
argument_list|()
decl_stmt|;
name|Resource
name|sourceResource
init|=
name|bndToRes
operator|.
name|get
argument_list|(
name|sourceBundle
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Resource
argument_list|>
name|wired
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Get a list of allowed wired resources
name|wired
operator|.
name|add
argument_list|(
name|sourceResource
argument_list|)
expr_stmt|;
for|for
control|(
name|Wire
name|wire
range|:
name|wiring
operator|.
name|get
argument_list|(
name|sourceResource
argument_list|)
control|)
block|{
name|wired
operator|.
name|add
argument_list|(
name|wire
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|HostNamespace
operator|.
name|HOST_NAMESPACE
operator|.
name|equals
argument_list|(
name|wire
operator|.
name|getRequirement
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|Wire
name|hostWire
range|:
name|wiring
operator|.
name|get
argument_list|(
name|wire
operator|.
name|getProvider
argument_list|()
argument_list|)
control|)
block|{
name|wired
operator|.
name|add
argument_list|(
name|hostWire
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Remove candidates that are not allowed
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
name|BundleRevision
name|br
init|=
name|cand
operator|.
name|getRevision
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|br
operator|.
name|getTypes
argument_list|()
operator|&
name|BundleRevision
operator|.
name|TYPE_FRAGMENT
operator|)
operator|!=
literal|0
condition|)
block|{
name|br
operator|=
name|br
operator|.
name|getWiring
argument_list|()
operator|.
name|getRequiredWires
argument_list|(
literal|null
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProvider
argument_list|()
expr_stmt|;
block|}
name|Resource
name|res
init|=
name|bndToRes
operator|.
name|get
argument_list|(
name|br
operator|.
name|getBundle
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|wired
operator|.
name|contains
argument_list|(
name|br
argument_list|)
operator|&&
operator|!
name|wired
operator|.
name|contains
argument_list|(
name|res
argument_list|)
condition|)
block|{
name|candIter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|end
parameter_list|()
block|{             }
block|}
decl_stmt|;
name|ResolverHookFactory
name|factory
init|=
name|triggers
lambda|->
name|hook
decl_stmt|;
name|ServiceRegistration
argument_list|<
name|ResolverHookFactory
argument_list|>
name|registration
init|=
name|systemBundleContext
operator|.
name|registerService
argument_list|(
name|ResolverHookFactory
operator|.
name|class
argument_list|,
name|factory
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|FrameworkWiring
name|frameworkWiring
init|=
name|systemBundleContext
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
decl_stmt|;
name|frameworkWiring
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
annotation|@
name|Override
specifier|public
name|void
name|replaceDigraph
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|>
argument_list|>
name|policies
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|BundleException
throws|,
name|InvalidSyntaxException
block|{
name|RegionDigraph
name|temp
init|=
name|digraph
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// Remove everything
for|for
control|(
name|Region
name|region
range|:
name|temp
operator|.
name|getRegions
argument_list|()
control|)
block|{
name|temp
operator|.
name|removeRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
comment|// Re-create regions
for|for
control|(
name|String
name|name
range|:
name|policies
operator|.
name|keySet
argument_list|()
control|)
block|{
name|temp
operator|.
name|createRegion
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|// Dispatch bundles
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|entry
range|:
name|bundles
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Region
name|region
init|=
name|temp
operator|.
name|getRegion
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|long
name|bundleId
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|region
operator|.
name|addBundle
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Add policies
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|>
argument_list|>
name|entry1
range|:
name|policies
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Region
name|region1
init|=
name|temp
operator|.
name|getRegion
argument_list|(
name|entry1
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|>
name|entry2
range|:
name|entry1
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Region
name|region2
init|=
name|temp
operator|.
name|getRegion
argument_list|(
name|entry2
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|RegionFilterBuilder
name|rfb
init|=
name|temp
operator|.
name|createRegionFilterBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry3
range|:
name|entry2
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|flt
range|:
name|entry3
operator|.
name|getValue
argument_list|()
control|)
block|{
name|rfb
operator|.
name|allow
argument_list|(
name|entry3
operator|.
name|getKey
argument_list|()
argument_list|,
name|flt
argument_list|)
expr_stmt|;
block|}
block|}
name|region1
operator|.
name|connectRegion
argument_list|(
name|region2
argument_list|,
name|rfb
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|digraph
operator|.
name|replace
argument_list|(
name|temp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveDigraph
parameter_list|()
block|{
name|DigraphHelper
operator|.
name|saveDigraph
argument_list|(
name|getDataFile
argument_list|(
name|DigraphHelper
operator|.
name|DIGRAPH_FILE
argument_list|)
argument_list|,
name|digraph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RegionDigraph
name|getDiGraphCopy
parameter_list|()
throws|throws
name|BundleException
block|{
return|return
name|digraph
operator|.
name|copy
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|installConfigs
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
if|if
condition|(
name|configInstaller
operator|!=
literal|null
condition|)
block|{
name|configInstaller
operator|.
name|installFeatureConfigs
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|installLibraries
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
comment|// TODO: install libraries
block|}
annotation|@
name|Override
specifier|public
name|File
name|getDataFile
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
return|return
name|ourBundleContext
operator|.
name|getDataFile
argument_list|(
name|fileName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|FrameworkInfo
name|getInfo
parameter_list|()
block|{
name|FrameworkInfo
name|info
init|=
operator|new
name|FrameworkInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|ourBundle
operator|=
name|ourBundle
expr_stmt|;
name|FrameworkStartLevel
name|fsl
init|=
name|systemBundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|info
operator|.
name|initialBundleStartLevel
operator|=
name|fsl
operator|.
name|getInitialBundleStartLevel
argument_list|()
expr_stmt|;
name|info
operator|.
name|currentStartLevel
operator|=
name|fsl
operator|.
name|getStartLevel
argument_list|()
expr_stmt|;
name|info
operator|.
name|bundles
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|systemBundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|info
operator|.
name|bundles
operator|.
name|put
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|systemBundle
operator|=
name|info
operator|.
name|bundles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

