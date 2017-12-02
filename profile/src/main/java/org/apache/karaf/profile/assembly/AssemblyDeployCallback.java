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
name|profile
operator|.
name|assembly
package|;
end_package

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
name|StandardCopyOption
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
name|HashMap
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
name|Map
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
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
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
name|features
operator|.
name|DeploymentEvent
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
name|FeatureEvent
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
name|DownloadManager
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
name|model
operator|.
name|Config
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
name|ConfigFile
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
name|Library
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
name|Deployer
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
name|FeaturesProcessor
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
name|State
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
name|StaticInstallSupport
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
name|util
operator|.
name|MapUtils
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
name|maven
operator|.
name|Parser
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
name|BundleRevision
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
comment|/**  * Callback through which {@link Deployer} will interact with the distribution that's being assembled.  */
end_comment

begin_class
specifier|public
class|class
name|AssemblyDeployCallback
extends|extends
name|StaticInstallSupport
implements|implements
name|Deployer
operator|.
name|DeployCallback
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
name|Builder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|DownloadManager
name|manager
decl_stmt|;
specifier|private
specifier|final
name|Builder
name|builder
decl_stmt|;
specifier|private
specifier|final
name|Path
name|homeDirectory
decl_stmt|;
specifier|private
specifier|final
name|int
name|defaultStartLevel
decl_stmt|;
specifier|private
specifier|final
name|Path
name|etcDirectory
decl_stmt|;
specifier|private
specifier|final
name|Path
name|systemDirectory
decl_stmt|;
specifier|private
specifier|final
name|Deployer
operator|.
name|DeploymentState
name|dstate
decl_stmt|;
specifier|private
specifier|final
name|AtomicLong
name|nextBundleId
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|FeaturesProcessor
name|processor
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Bundle
argument_list|>
name|bundles
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Create a {@link Deployer.DeployCallback} performing actions on runtime with single system bundle installed      * and with access to all non-blacklisted features.      * @param manager      * @param builder      * @param systemBundle      * @param repositories      * @param processor      */
specifier|public
name|AssemblyDeployCallback
parameter_list|(
name|DownloadManager
name|manager
parameter_list|,
name|Builder
name|builder
parameter_list|,
name|BundleRevision
name|systemBundle
parameter_list|,
name|Collection
argument_list|<
name|Features
argument_list|>
name|repositories
parameter_list|,
name|FeaturesProcessor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|this
operator|.
name|homeDirectory
operator|=
name|builder
operator|.
name|homeDirectory
expr_stmt|;
name|this
operator|.
name|etcDirectory
operator|=
name|homeDirectory
operator|.
name|resolve
argument_list|(
literal|"etc"
argument_list|)
expr_stmt|;
name|this
operator|.
name|systemDirectory
operator|=
name|homeDirectory
operator|.
name|resolve
argument_list|(
literal|"system"
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultStartLevel
operator|=
name|builder
operator|.
name|defaultStartLevel
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|dstate
operator|=
operator|new
name|Deployer
operator|.
name|DeploymentState
argument_list|()
expr_stmt|;
name|dstate
operator|.
name|bundles
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|dstate
operator|.
name|bundlesPerRegion
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|dstate
operator|.
name|filtersPerRegion
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|dstate
operator|.
name|state
operator|=
operator|new
name|State
argument_list|()
expr_stmt|;
name|MapUtils
operator|.
name|addToMapSet
argument_list|(
name|dstate
operator|.
name|bundlesPerRegion
argument_list|,
name|FeaturesService
operator|.
name|ROOT_REGION
argument_list|,
literal|0l
argument_list|)
expr_stmt|;
name|dstate
operator|.
name|bundles
operator|.
name|put
argument_list|(
literal|0l
argument_list|,
name|systemBundle
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
argument_list|>
name|features
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Features
name|repo
range|:
name|repositories
control|)
block|{
if|if
condition|(
name|repo
operator|.
name|isBlacklisted
argument_list|()
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Feature
name|f
range|:
name|repo
operator|.
name|getFeature
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|isBlacklisted
argument_list|()
condition|)
block|{
name|features
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|dstate
operator|.
name|partitionFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get startup bundles with related start-level      * @return      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getStartupBundles
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|startup
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
name|String
argument_list|,
name|Bundle
argument_list|>
name|bundle
range|:
name|bundles
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|level
init|=
name|bundle
operator|.
name|getValue
argument_list|()
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
operator|<=
literal|0
condition|)
block|{
name|level
operator|=
name|defaultStartLevel
expr_stmt|;
block|}
name|startup
operator|.
name|put
argument_list|(
name|bundle
operator|.
name|getKey
argument_list|()
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
return|return
name|startup
return|;
block|}
specifier|public
name|Deployer
operator|.
name|DeploymentState
name|getDeploymentState
parameter_list|()
block|{
return|return
name|dstate
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveState
parameter_list|(
name|State
name|state
parameter_list|)
block|{
name|dstate
operator|.
name|state
operator|.
name|replace
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|persistResolveRequest
parameter_list|(
name|Deployer
operator|.
name|DeploymentRequest
name|request
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|installConfigs
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
block|{
name|assertNotBlacklisted
argument_list|(
name|feature
argument_list|)
expr_stmt|;
comment|// Install
name|Downloader
name|downloader
init|=
name|manager
operator|.
name|createDownloader
argument_list|()
decl_stmt|;
for|for
control|(
name|Config
name|config
range|:
operator|(
operator|(
name|Feature
operator|)
name|feature
operator|)
operator|.
name|getConfig
argument_list|()
control|)
block|{
if|if
condition|(
name|config
operator|.
name|isExternal
argument_list|()
condition|)
block|{
name|downloader
operator|.
name|download
argument_list|(
name|config
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|provider
lambda|->
block|{
name|Path
name|input
init|=
name|provider
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|Files
operator|.
name|readAllBytes
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|Path
name|configFile
init|=
name|etcDirectory
operator|.
name|resolve
argument_list|(
name|config
operator|.
name|getName
argument_list|()
operator|+
literal|".cfg"
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"      adding config file: {}"
argument_list|,
name|homeDirectory
operator|.
name|relativize
argument_list|(
name|configFile
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|configFile
argument_list|)
condition|)
block|{
name|Files
operator|.
name|write
argument_list|(
name|configFile
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|isAppend
argument_list|()
condition|)
block|{
name|Files
operator|.
name|write
argument_list|(
name|configFile
argument_list|,
name|data
argument_list|,
name|StandardOpenOption
operator|.
name|APPEND
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|byte
index|[]
name|data
init|=
name|config
operator|.
name|getValue
argument_list|()
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|Path
name|configFile
init|=
name|etcDirectory
operator|.
name|resolve
argument_list|(
name|config
operator|.
name|getName
argument_list|()
operator|+
literal|".cfg"
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"      adding config file: {}"
argument_list|,
name|homeDirectory
operator|.
name|relativize
argument_list|(
name|configFile
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|configFile
argument_list|)
condition|)
block|{
name|Files
operator|.
name|write
argument_list|(
name|configFile
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|isAppend
argument_list|()
condition|)
block|{
name|Files
operator|.
name|write
argument_list|(
name|configFile
argument_list|,
name|data
argument_list|,
name|StandardOpenOption
operator|.
name|APPEND
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
specifier|final
name|ConfigFile
name|configFile
range|:
operator|(
operator|(
name|Feature
operator|)
name|feature
operator|)
operator|.
name|getConfigfile
argument_list|()
control|)
block|{
name|downloader
operator|.
name|download
argument_list|(
name|configFile
operator|.
name|getLocation
argument_list|()
argument_list|,
name|provider
lambda|->
block|{
name|Path
name|input
init|=
name|provider
operator|.
name|getFile
argument_list|()
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|configFile
operator|.
name|getFinalname
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
name|substFinalName
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Path
name|output
init|=
name|homeDirectory
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"      adding config file: {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
argument_list|)
expr_stmt|;
block|}
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
block|{
name|assertNotBlacklisted
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|Downloader
name|downloader
init|=
name|manager
operator|.
name|createDownloader
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|libraries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Library
name|library
range|:
operator|(
operator|(
name|Feature
operator|)
name|feature
operator|)
operator|.
name|getLibraries
argument_list|()
control|)
block|{
name|String
name|lib
init|=
name|library
operator|.
name|getLocation
argument_list|()
operator|+
literal|";type:="
operator|+
name|library
operator|.
name|getType
argument_list|()
operator|+
literal|";export:="
operator|+
name|library
operator|.
name|isExport
argument_list|()
operator|+
literal|";delegate:="
operator|+
name|library
operator|.
name|isDelegate
argument_list|()
decl_stmt|;
name|libraries
operator|.
name|add
argument_list|(
name|lib
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|libraries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Path
name|configPropertiesPath
init|=
name|etcDirectory
operator|.
name|resolve
argument_list|(
literal|"config.properties"
argument_list|)
decl_stmt|;
name|Properties
name|configProperties
init|=
operator|new
name|Properties
argument_list|(
name|configPropertiesPath
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|downloadLibraries
argument_list|(
name|downloader
argument_list|,
name|configProperties
argument_list|,
name|libraries
argument_list|,
literal|"   "
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|downloader
operator|.
name|await
argument_list|()
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
name|IOException
argument_list|(
literal|"Error downloading configuration files"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|assertNotBlacklisted
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
name|feature
parameter_list|)
block|{
if|if
condition|(
name|feature
operator|.
name|isBlacklisted
argument_list|()
condition|)
block|{
if|if
condition|(
name|builder
operator|.
name|getBlacklistPolicy
argument_list|()
operator|==
name|Builder
operator|.
name|BlacklistPolicy
operator|.
name|Fail
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Feature "
operator|+
name|feature
operator|.
name|getId
argument_list|()
operator|+
literal|" is blacklisted"
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|callListeners
parameter_list|(
name|DeploymentEvent
name|deployEvent
parameter_list|)
block|{     }
annotation|@
name|Override
specifier|public
name|void
name|callListeners
parameter_list|(
name|FeatureEvent
name|featureEvent
parameter_list|)
block|{     }
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
comment|// Check blacklist
if|if
condition|(
name|processor
operator|.
name|isBundleBlacklisted
argument_list|(
name|uri
argument_list|)
condition|)
block|{
if|if
condition|(
name|builder
operator|.
name|getBlacklistPolicy
argument_list|()
operator|==
name|Builder
operator|.
name|BlacklistPolicy
operator|.
name|Fail
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Bundle "
operator|+
name|uri
operator|+
literal|" is blacklisted"
argument_list|)
throw|;
block|}
block|}
comment|// Install
name|LOGGER
operator|.
name|info
argument_list|(
literal|"      adding maven artifact: "
operator|+
name|uri
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|regUri
decl_stmt|;
name|String
name|path
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
name|regUri
operator|=
name|uri
expr_stmt|;
name|path
operator|=
name|Parser
operator|.
name|pathFromMaven
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|uri
operator|=
name|uri
operator|.
name|replaceAll
argument_list|(
literal|"[^0-9a-zA-Z.\\-_]+"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|.
name|length
argument_list|()
operator|>
literal|256
condition|)
block|{
comment|//to avoid the File name too long exception
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|255
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
literal|"generated/"
operator|+
name|uri
expr_stmt|;
name|regUri
operator|=
literal|"file:"
operator|+
name|path
expr_stmt|;
block|}
specifier|final
name|Path
name|bundleSystemFile
init|=
name|systemDirectory
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|bundleSystemFile
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|Files
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bundleSystemFile
argument_list|,
name|StandardCopyOption
operator|.
name|REPLACE_EXISTING
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|JarFile
name|jar
init|=
operator|new
name|JarFile
argument_list|(
name|bundleSystemFile
operator|.
name|toFile
argument_list|()
argument_list|)
init|)
block|{
name|Attributes
name|attributes
init|=
name|jar
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|attr
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|attr
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|attr
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|BundleRevision
name|revision
init|=
operator|new
name|FakeBundleRevision
argument_list|(
name|headers
argument_list|,
name|uri
argument_list|,
name|nextBundleId
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|revision
operator|.
name|getBundle
argument_list|()
decl_stmt|;
name|MapUtils
operator|.
name|addToMapSet
argument_list|(
name|dstate
operator|.
name|bundlesPerRegion
argument_list|,
name|region
argument_list|,
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|dstate
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
name|bundles
operator|.
name|put
argument_list|(
name|regUri
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
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
literal|"Unable to install bundle"
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
name|bundleBlacklisted
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"      skipping blacklisted bundle: {}"
argument_list|,
name|bundleInfo
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|substFinalName
parameter_list|(
name|String
name|finalname
parameter_list|)
block|{
specifier|final
name|String
name|markerVarBeg
init|=
literal|"${"
decl_stmt|;
specifier|final
name|String
name|markerVarEnd
init|=
literal|"}"
decl_stmt|;
name|boolean
name|startsWithVariable
init|=
name|finalname
operator|.
name|startsWith
argument_list|(
name|markerVarBeg
argument_list|)
operator|&&
name|finalname
operator|.
name|contains
argument_list|(
name|markerVarEnd
argument_list|)
decl_stmt|;
if|if
condition|(
name|startsWithVariable
condition|)
block|{
name|String
name|marker
init|=
name|finalname
operator|.
name|substring
argument_list|(
name|markerVarBeg
operator|.
name|length
argument_list|()
argument_list|,
name|finalname
operator|.
name|indexOf
argument_list|(
name|markerVarEnd
argument_list|)
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|marker
condition|)
block|{
case|case
literal|"karaf.base"
case|:
return|return
name|this
operator|.
name|homeDirectory
operator|+
name|finalname
operator|.
name|substring
argument_list|(
name|finalname
operator|.
name|indexOf
argument_list|(
name|markerVarEnd
argument_list|)
operator|+
name|markerVarEnd
operator|.
name|length
argument_list|()
argument_list|)
return|;
case|case
literal|"karaf.etc"
case|:
return|return
name|this
operator|.
name|etcDirectory
operator|+
name|finalname
operator|.
name|substring
argument_list|(
name|finalname
operator|.
name|indexOf
argument_list|(
name|markerVarEnd
argument_list|)
operator|+
name|markerVarEnd
operator|.
name|length
argument_list|()
argument_list|)
return|;
default|default:
break|break;
block|}
block|}
return|return
name|finalname
return|;
block|}
block|}
end_class

end_unit

