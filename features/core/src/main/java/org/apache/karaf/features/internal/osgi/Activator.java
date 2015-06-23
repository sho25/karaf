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
name|osgi
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|io
operator|.
name|OutputStream
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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|utils
operator|.
name|properties
operator|.
name|Properties
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
name|FeaturesListener
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
name|management
operator|.
name|FeaturesServiceMBeanImpl
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
name|features
operator|.
name|internal
operator|.
name|region
operator|.
name|SubsystemResolveContext
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
name|AggregateRepository
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
name|JsonRepository
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
name|XmlRepository
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
name|service
operator|.
name|BootFeaturesInstaller
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
name|service
operator|.
name|EventAdminListener
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
name|service
operator|.
name|FeatureFinder
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
name|service
operator|.
name|FeaturesServiceImpl
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
name|service
operator|.
name|StateStorage
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
name|tracker
operator|.
name|BaseActivator
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
name|tracker
operator|.
name|annotation
operator|.
name|ProvideService
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
name|tracker
operator|.
name|annotation
operator|.
name|RequireService
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
name|tracker
operator|.
name|annotation
operator|.
name|Services
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
name|internal
operator|.
name|region
operator|.
name|CollisionHookHelper
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
name|internal
operator|.
name|region
operator|.
name|StandardRegionDigraph
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
name|internal
operator|.
name|region
operator|.
name|management
operator|.
name|StandardManageableRegionDigraph
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
name|framework
operator|.
name|ServiceReference
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
name|bundle
operator|.
name|CollisionHook
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
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
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
name|cm
operator|.
name|ManagedService
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
name|Resolver
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
name|url
operator|.
name|URLStreamHandlerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTrackerCustomizer
import|;
end_import

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
block|{
annotation|@
name|RequireService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
block|,
annotation|@
name|RequireService
argument_list|(
name|Resolver
operator|.
name|class
argument_list|)
block|,
annotation|@
name|RequireService
argument_list|(
name|value
operator|=
name|URLStreamHandlerService
operator|.
name|class
argument_list|,
name|filter
operator|=
literal|"(url.handler.protocol=mvn)"
argument_list|)
block|}
argument_list|,
name|provides
operator|=
block|{
annotation|@
name|ProvideService
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
block|,
annotation|@
name|ProvideService
argument_list|(
name|RegionDigraph
operator|.
name|class
argument_list|)
block|}
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FEATURES_REPOS_PID
init|=
literal|"org.apache.karaf.features.repos"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FEATURES_SERVICE_CONFIG_FILE
init|=
literal|"org.apache.karaf.features.cfg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STATE_FILE
init|=
literal|"state.json"
decl_stmt|;
specifier|private
name|ServiceTracker
argument_list|<
name|FeaturesListener
argument_list|,
name|FeaturesListener
argument_list|>
name|featuresListenerTracker
decl_stmt|;
specifier|private
name|FeaturesServiceImpl
name|featuresService
decl_stmt|;
specifier|private
name|StandardRegionDigraph
name|digraph
decl_stmt|;
specifier|private
name|StandardManageableRegionDigraph
name|digraphMBean
decl_stmt|;
specifier|public
name|Activator
parameter_list|()
block|{
comment|// Special case here, as we don't want the activator to wait for current job to finish,
comment|// else it would forbid the features service to refresh itself
name|setSchedulerStopTimeout
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doOpen
argument_list|()
expr_stmt|;
name|Properties
name|configuration
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|File
name|configFile
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
name|FEATURES_SERVICE_CONFIG_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|configFile
operator|.
name|isFile
argument_list|()
operator|&&
name|configFile
operator|.
name|canRead
argument_list|()
condition|)
block|{
try|try
block|{
name|configuration
operator|.
name|load
argument_list|(
operator|new
name|FileReader
argument_list|(
name|configFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error reading configuration file "
operator|+
name|configFile
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
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
name|String
argument_list|>
name|entry
range|:
name|configuration
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|props
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|updated
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationAdmin
name|configurationAdmin
init|=
name|getTrackedService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
name|Resolver
name|resolver
init|=
name|getTrackedService
argument_list|(
name|Resolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|URLStreamHandlerService
name|mvnUrlHandler
init|=
name|getTrackedService
argument_list|(
name|URLStreamHandlerService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationAdmin
operator|==
literal|null
operator|||
name|mvnUrlHandler
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Resolver
comment|//        register(Resolver.class, new ResolverImpl(new Slf4jResolverLog(LoggerFactory.getLogger(ResolverImpl.class))));
comment|// RegionDigraph
name|digraph
operator|=
name|DigraphHelper
operator|.
name|loadDigraph
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ResolverHookFactory
operator|.
name|class
argument_list|,
name|digraph
operator|.
name|getResolverHookFactory
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|CollisionHook
operator|.
name|class
argument_list|,
name|CollisionHookHelper
operator|.
name|getCollisionHook
argument_list|(
name|digraph
argument_list|)
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|hooks
operator|.
name|bundle
operator|.
name|FindHook
operator|.
name|class
argument_list|,
name|digraph
operator|.
name|getBundleFindHook
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|hooks
operator|.
name|bundle
operator|.
name|EventHook
operator|.
name|class
argument_list|,
name|digraph
operator|.
name|getBundleEventHook
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|hooks
operator|.
name|service
operator|.
name|FindHook
operator|.
name|class
argument_list|,
name|digraph
operator|.
name|getServiceFindHook
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|hooks
operator|.
name|service
operator|.
name|EventHook
operator|.
name|class
argument_list|,
name|digraph
operator|.
name|getServiceEventHook
argument_list|()
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|RegionDigraph
operator|.
name|class
argument_list|,
name|digraph
argument_list|)
expr_stmt|;
name|digraphMBean
operator|=
operator|new
name|StandardManageableRegionDigraph
argument_list|(
name|digraph
argument_list|,
literal|"org.apache.karaf"
argument_list|,
name|bundleContext
argument_list|)
expr_stmt|;
name|digraphMBean
operator|.
name|registerMBean
argument_list|()
expr_stmt|;
name|FeatureFinder
name|featureFinder
init|=
operator|new
name|FeatureFinder
argument_list|()
decl_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
name|FEATURES_REPOS_PID
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ManagedService
operator|.
name|class
argument_list|,
name|featureFinder
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Repository
argument_list|>
name|repositories
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|resourceRepositories
init|=
name|getString
argument_list|(
literal|"resourceRepositories"
argument_list|,
literal|""
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|long
name|repositoryExpiration
init|=
name|getLong
argument_list|(
literal|"repositoryExpiration"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_REPOSITORY_EXPIRATION
argument_list|)
decl_stmt|;
name|boolean
name|repositoryIgnoreFailures
init|=
name|getBoolean
argument_list|(
literal|"repositoryIgnoreFailures"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|url
range|:
name|resourceRepositories
control|)
block|{
name|url
operator|=
name|url
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|url
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"json:"
argument_list|)
condition|)
block|{
name|repositories
operator|.
name|add
argument_list|(
operator|new
name|JsonRepository
argument_list|(
name|url
operator|.
name|substring
argument_list|(
literal|"json:"
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|repositoryExpiration
argument_list|,
name|repositoryIgnoreFailures
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"xml:"
argument_list|)
condition|)
block|{
name|repositories
operator|.
name|add
argument_list|(
operator|new
name|XmlRepository
argument_list|(
name|url
operator|.
name|substring
argument_list|(
literal|"xml:"
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|repositoryExpiration
argument_list|,
name|repositoryIgnoreFailures
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Unrecognized resource repository: "
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Repository
name|globalRepository
decl_stmt|;
switch|switch
condition|(
name|repositories
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
name|globalRepository
operator|=
literal|null
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|globalRepository
operator|=
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
break|break;
default|default:
name|globalRepository
operator|=
operator|new
name|AggregateRepository
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
break|break;
block|}
name|String
name|overrides
init|=
name|getString
argument_list|(
literal|"overrides"
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"overrides.properties"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|featureResolutionRange
init|=
name|getString
argument_list|(
literal|"featureResolutionRange"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|)
decl_stmt|;
name|String
name|bundleUpdateRange
init|=
name|getString
argument_list|(
literal|"bundleUpdateRange"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_BUNDLE_UPDATE_RANGE
argument_list|)
decl_stmt|;
name|String
name|updateSnapshots
init|=
name|getString
argument_list|(
literal|"updateSnapshots"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_UPDATE_SNAPSHOTS
argument_list|)
decl_stmt|;
name|int
name|downloadThreads
init|=
name|getInt
argument_list|(
literal|"downloadThreads"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_DOWNLOAD_THREADS
argument_list|)
decl_stmt|;
name|long
name|scheduleDelay
init|=
name|getLong
argument_list|(
literal|"scheduleDelay"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_SCHEDULE_DELAY
argument_list|)
decl_stmt|;
name|int
name|scheduleMaxRun
init|=
name|getInt
argument_list|(
literal|"scheduleMaxRun"
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_SCHEDULE_MAX_RUN
argument_list|)
decl_stmt|;
name|String
name|blacklisted
init|=
name|getString
argument_list|(
literal|"blacklisted"
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"blacklisted.properties"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|serviceRequirements
init|=
name|getString
argument_list|(
literal|"serviceRequirements"
argument_list|,
name|FeaturesService
operator|.
name|SERVICE_REQUIREMENTS_DEFAULT
argument_list|)
decl_stmt|;
name|StateStorage
name|stateStorage
init|=
operator|new
name|StateStorage
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|bundleContext
operator|.
name|getDataFile
argument_list|(
name|STATE_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|bundleContext
operator|.
name|getDataFile
argument_list|(
name|STATE_FILE
argument_list|)
decl_stmt|;
return|return
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|EventAdminListener
name|eventAdminListener
decl_stmt|;
try|try
block|{
name|eventAdminListener
operator|=
operator|new
name|EventAdminListener
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|eventAdminListener
operator|=
literal|null
expr_stmt|;
block|}
name|featuresService
operator|=
operator|new
name|FeaturesServiceImpl
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundleContext
argument_list|()
argument_list|,
name|stateStorage
argument_list|,
name|featureFinder
argument_list|,
name|eventAdminListener
argument_list|,
name|configurationAdmin
argument_list|,
name|resolver
argument_list|,
name|digraph
argument_list|,
name|overrides
argument_list|,
name|featureResolutionRange
argument_list|,
name|bundleUpdateRange
argument_list|,
name|updateSnapshots
argument_list|,
name|serviceRequirements
argument_list|,
name|globalRepository
argument_list|,
name|downloadThreads
argument_list|,
name|scheduleDelay
argument_list|,
name|scheduleMaxRun
argument_list|,
name|blacklisted
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|,
name|featuresService
argument_list|)
expr_stmt|;
name|featuresListenerTracker
operator|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|FeaturesListener
operator|.
name|class
argument_list|,
operator|new
name|ServiceTrackerCustomizer
argument_list|<
name|FeaturesListener
argument_list|,
name|FeaturesListener
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|FeaturesListener
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|FeaturesListener
argument_list|>
name|reference
parameter_list|)
block|{
name|FeaturesListener
name|service
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|featuresService
operator|.
name|registerListener
argument_list|(
name|service
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|modifiedService
parameter_list|(
name|ServiceReference
argument_list|<
name|FeaturesListener
argument_list|>
name|reference
parameter_list|,
name|FeaturesListener
name|service
parameter_list|)
block|{                     }
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|FeaturesListener
argument_list|>
name|reference
parameter_list|,
name|FeaturesListener
name|service
parameter_list|)
block|{
name|featuresService
operator|.
name|unregisterListener
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|featuresListenerTracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|String
name|featuresRepositories
init|=
name|getString
argument_list|(
literal|"featuresRepositories"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|featuresBoot
init|=
name|getString
argument_list|(
literal|"featuresBoot"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|boolean
name|featuresBootAsynchronous
init|=
name|getBoolean
argument_list|(
literal|"featuresBootAsynchronous"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BootFeaturesInstaller
name|bootFeaturesInstaller
init|=
operator|new
name|BootFeaturesInstaller
argument_list|(
name|bundleContext
argument_list|,
name|featuresService
argument_list|,
name|featuresRepositories
argument_list|,
name|featuresBoot
argument_list|,
name|featuresBootAsynchronous
argument_list|)
decl_stmt|;
name|bootFeaturesInstaller
operator|.
name|start
argument_list|()
expr_stmt|;
name|FeaturesServiceMBeanImpl
name|featuresServiceMBean
init|=
operator|new
name|FeaturesServiceMBeanImpl
argument_list|()
decl_stmt|;
name|featuresServiceMBean
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|featuresServiceMBean
operator|.
name|setFeaturesService
argument_list|(
name|featuresService
argument_list|)
expr_stmt|;
name|registerMBean
argument_list|(
name|featuresServiceMBean
argument_list|,
literal|"type=feature"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
if|if
condition|(
name|digraphMBean
operator|!=
literal|null
condition|)
block|{
name|digraphMBean
operator|.
name|unregisterMbean
argument_list|()
expr_stmt|;
name|digraphMBean
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|featuresListenerTracker
operator|!=
literal|null
condition|)
block|{
name|featuresListenerTracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|featuresListenerTracker
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|featuresService
operator|!=
literal|null
condition|)
block|{
name|featuresService
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|digraph
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|DigraphHelper
operator|.
name|saveDigraph
argument_list|(
name|bundleContext
argument_list|,
name|digraph
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
name|digraph
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

