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
name|bundle
operator|.
name|core
operator|.
name|internal
package|;
end_package

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
name|ArrayList
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
name|CopyOnWriteArrayList
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
name|bundle
operator|.
name|core
operator|.
name|BundleInfo
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
name|bundle
operator|.
name|core
operator|.
name|BundleService
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
name|bundle
operator|.
name|core
operator|.
name|BundleState
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
name|bundle
operator|.
name|core
operator|.
name|BundleStateService
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
name|jaas
operator|.
name|JaasHelper
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
name|BundleRevisions
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
name|BundleWire
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
name|BundleWiring
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

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_class
specifier|public
class|class
name|BundleServiceImpl
implements|implements
name|BundleService
block|{
specifier|private
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BundleService
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The header key where we store the active wires when we enable DynamicImport=*      */
specifier|private
specifier|static
specifier|final
name|String
name|ORIGINAL_WIRES
init|=
literal|"Original-Wires"
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|BundleStateService
argument_list|>
name|stateServices
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|BundleServiceImpl
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|void
name|registerBundleStateService
parameter_list|(
name|BundleStateService
name|service
parameter_list|)
block|{
name|stateServices
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|unregisterBundleStateService
parameter_list|(
name|BundleStateService
name|service
parameter_list|)
block|{
name|stateServices
operator|.
name|remove
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Bundle
argument_list|>
name|selectBundles
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|boolean
name|defaultAllBundles
parameter_list|)
block|{
return|return
name|selectBundles
argument_list|(
literal|null
argument_list|,
name|ids
argument_list|,
name|defaultAllBundles
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Bundle
argument_list|>
name|selectBundles
parameter_list|(
name|String
name|context
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|boolean
name|defaultAllBundles
parameter_list|)
block|{
return|return
name|doSelectBundles
argument_list|(
name|doGetBundleContext
argument_list|(
name|context
argument_list|)
argument_list|,
name|ids
argument_list|,
name|defaultAllBundles
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Bundle
name|getBundle
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
literal|null
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Bundle
name|getBundle
parameter_list|(
name|String
name|context
parameter_list|,
name|String
name|id
parameter_list|)
block|{
return|return
name|doGetBundle
argument_list|(
name|doGetBundleContext
argument_list|(
name|context
argument_list|)
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|BundleInfo
name|getInfo
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|BundleState
name|combinedState
init|=
name|BundleState
operator|.
name|Unknown
decl_stmt|;
for|for
control|(
name|BundleStateService
name|stateService
range|:
name|this
operator|.
name|stateServices
control|)
block|{
name|BundleState
name|extState
init|=
name|stateService
operator|.
name|getState
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|extState
operator|!=
name|BundleState
operator|.
name|Unknown
condition|)
block|{
name|combinedState
operator|=
name|extState
expr_stmt|;
block|}
block|}
return|return
operator|new
name|BundleInfoImpl
argument_list|(
name|bundle
argument_list|,
name|combinedState
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDiag
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleStateService
name|bundleStateService
range|:
name|stateServices
control|)
block|{
name|String
name|part
init|=
name|bundleStateService
operator|.
name|getDiag
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|append
argument_list|(
name|bundleStateService
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|INSTALLED
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Unsatisfied Requirements:"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BundleRequirement
argument_list|>
name|reqs
init|=
name|getUnsatisfiedRequirements
argument_list|(
name|bundle
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleRequirement
name|req
range|:
name|reqs
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|req
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|message
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|BundleRequirement
argument_list|>
name|getUnsatisfiedRequirements
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|List
argument_list|<
name|BundleRequirement
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|BundleRevision
name|rev
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rev
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|BundleRequirement
argument_list|>
name|reqs
init|=
name|rev
operator|.
name|getDeclaredRequirements
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleRequirement
name|req
range|:
name|reqs
control|)
block|{
if|if
condition|(
operator|!
name|canBeSatisfied
argument_list|(
name|req
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|req
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getSystemBundleThreshold
parameter_list|()
block|{
name|int
name|sbsl
init|=
literal|50
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|sbslProp
init|=
name|bundleContext
operator|.
name|getProperty
argument_list|(
literal|"karaf.systemBundlesStartLevel"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sbslProp
operator|!=
literal|null
condition|)
block|{
name|sbsl
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|sbslProp
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|sbsl
return|;
block|}
specifier|private
name|BundleContext
name|doGetBundleContext
parameter_list|(
name|String
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
operator|||
name|context
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|bundleContext
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
operator|new
name|BundleSelectorImpl
argument_list|(
name|bundleContext
argument_list|)
operator|.
name|selectBundles
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|context
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Context "
operator|+
name|context
operator|+
literal|" does not evaluate to a bundle"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|bundles
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Context "
operator|+
name|context
operator|+
literal|" is ambiguous"
argument_list|)
throw|;
block|}
name|BundleContext
name|bundleContext
init|=
name|bundles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundleContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundleContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Context "
operator|+
name|context
operator|+
literal|" is not resolved"
argument_list|)
throw|;
block|}
return|return
name|bundleContext
return|;
block|}
block|}
specifier|private
name|Bundle
name|doGetBundle
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|doSelectBundles
argument_list|(
name|bundleContext
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|id
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bundle "
operator|+
name|id
operator|+
literal|" does not match any bundle"
argument_list|)
throw|;
block|}
else|else
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|filtered
init|=
name|filter
argument_list|(
name|bundles
argument_list|)
decl_stmt|;
if|if
condition|(
name|filtered
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Access to bundle "
operator|+
name|id
operator|+
literal|" is forbidden"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|filtered
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple bundles matching "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
name|filtered
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Bundle
argument_list|>
name|doSelectBundles
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|boolean
name|defaultAllBundles
parameter_list|)
block|{
return|return
name|filter
argument_list|(
operator|new
name|BundleSelectorImpl
argument_list|(
name|bundleContext
argument_list|)
operator|.
name|selectBundles
argument_list|(
name|ids
argument_list|,
name|defaultAllBundles
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|Bundle
argument_list|>
name|filter
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
block|{
if|if
condition|(
name|JaasHelper
operator|.
name|currentUserHasRole
argument_list|(
name|BundleService
operator|.
name|SYSTEM_BUNDLES_ROLE
argument_list|)
condition|)
block|{
return|return
name|bundles
return|;
block|}
name|int
name|sbsl
init|=
name|getSystemBundleThreshold
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|filtered
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|int
name|level
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
operator|.
name|getStartLevel
argument_list|()
decl_stmt|;
if|if
condition|(
name|level
operator|>=
name|sbsl
condition|)
block|{
name|filtered
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filtered
return|;
block|}
specifier|private
name|boolean
name|canBeSatisfied
parameter_list|(
name|BundleRequirement
name|req
parameter_list|)
block|{
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|BundleWiring
name|wiring
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wiring
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|caps
init|=
name|wiring
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
if|if
condition|(
name|req
operator|.
name|matches
argument_list|(
name|cap
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
comment|/*      * Enable DynamicImport=* on the bundle      */
specifier|public
name|void
name|enableDynamicImports
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|String
name|location
init|=
name|String
operator|.
name|format
argument_list|(
literal|"wrap:%s$"
operator|+
literal|"Bundle-UpdateLocation=%s&"
operator|+
literal|"DynamicImport-Package=*&"
operator|+
literal|"%s=%s&"
operator|+
literal|"overwrite=merge"
argument_list|,
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
name|ORIGINAL_WIRES
argument_list|,
name|explode
argument_list|(
name|getWiredBundles
argument_list|(
name|bundle
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Updating %s with URL %s"
argument_list|,
name|bundle
argument_list|,
name|location
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|location
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|update
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkWiring
operator|.
name|class
argument_list|)
operator|.
name|refreshBundles
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error enabling dynamic imports on bundle"
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*      * Disable DynamicImport=* on the bundle      *      * At this time, we will also calculate the difference in package wiring for the bundle compared to      * when we enabled the DynamicImport      */
specifier|public
name|void
name|disableDynamicImports
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|current
init|=
name|getWiredBundles
argument_list|(
name|bundle
argument_list|)
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|original
range|:
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|ORIGINAL_WIRES
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|current
operator|.
name|remove
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No additional packages have been wired since dynamic import was enabled"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Additional packages wired since dynamic import was enabled"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|current
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"- "
operator|+
name|pkg
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|bundle
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BundleException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error disabling dynamic imports on bundle"
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/*      * Explode a set of string values in to a ,-delimited string      */
specifier|private
name|String
name|explode
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|set
parameter_list|)
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|set
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|result
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|"--none--"
return|;
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/*      * Get the list of bundles from which the given bundle imports packages      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Bundle
argument_list|>
name|getWiredBundles
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
comment|// the set of bundles from which the bundle imports packages
name|Map
argument_list|<
name|String
argument_list|,
name|Bundle
argument_list|>
name|exporters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleRevision
name|revision
range|:
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevisions
operator|.
name|class
argument_list|)
operator|.
name|getRevisions
argument_list|()
control|)
block|{
name|BundleWiring
name|wiring
init|=
name|revision
operator|.
name|getWiring
argument_list|()
decl_stmt|;
if|if
condition|(
name|wiring
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|BundleWire
argument_list|>
name|wires
init|=
name|wiring
operator|.
name|getRequiredWires
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|wires
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BundleWire
name|wire
range|:
name|wires
control|)
block|{
if|if
condition|(
name|wire
operator|.
name|getProviderWiring
argument_list|()
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|exporters
operator|.
name|put
argument_list|(
name|wire
operator|.
name|getCapability
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|wire
operator|.
name|getProviderWiring
argument_list|()
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|exporters
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDynamicImport
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
return|return
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|ORIGINAL_WIRES
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getStatus
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|getState
argument_list|(
name|bundle
argument_list|)
return|;
block|}
comment|/**      * Return a String representing current bundle state      *      * @param bundle the bundle      * @return bundle state String      */
specifier|private
name|String
name|getState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
switch|switch
condition|(
name|bundle
operator|.
name|getState
argument_list|()
condition|)
block|{
case|case
name|Bundle
operator|.
name|UNINSTALLED
case|:
return|return
literal|"Uninstalled"
return|;
case|case
name|Bundle
operator|.
name|INSTALLED
case|:
return|return
literal|"Installed"
return|;
case|case
name|Bundle
operator|.
name|RESOLVED
case|:
return|return
literal|"Resolved"
return|;
case|case
name|Bundle
operator|.
name|STARTING
case|:
return|return
literal|"Starting"
return|;
case|case
name|Bundle
operator|.
name|STOPPING
case|:
return|return
literal|"Stopping"
return|;
case|case
name|Bundle
operator|.
name|ACTIVE
case|:
return|return
literal|"Active"
return|;
default|default:
return|return
literal|"Unknown"
return|;
block|}
block|}
block|}
end_class

end_unit

