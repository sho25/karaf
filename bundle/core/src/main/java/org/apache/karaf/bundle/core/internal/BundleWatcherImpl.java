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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|AtomicInteger
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
name|BundleWatcher
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

begin_comment
comment|/**  * A Runnable singleton which watches at the defined location for bundle  * updates.  */
end_comment

begin_class
specifier|public
class|class
name|BundleWatcherImpl
implements|implements
name|Runnable
implements|,
name|BundleListener
implements|,
name|BundleWatcher
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BundleWatcherImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
specifier|final
name|BundleService
name|bundleService
decl_stmt|;
specifier|private
specifier|final
name|MavenConfigService
name|localRepoDetector
decl_stmt|;
specifier|private
name|AtomicBoolean
name|running
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|private
name|long
name|interval
init|=
literal|1000L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|watchURLs
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|public
name|BundleWatcherImpl
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|MavenConfigService
name|mavenConfigService
parameter_list|,
name|BundleService
name|bundleService
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|localRepoDetector
operator|=
name|mavenConfigService
expr_stmt|;
name|this
operator|.
name|bundleService
operator|=
name|bundleService
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.dev.core.internal.BundleWatcher#bundleChanged(org.osgi.framework.BundleEvent)      */
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
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|INSTALLED
operator|||
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNINSTALLED
condition|)
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Bundle watcher thread started"
argument_list|)
expr_stmt|;
name|int
name|oldCounter
init|=
operator|-
literal|1
decl_stmt|;
name|Set
argument_list|<
name|Bundle
argument_list|>
name|watchedBundles
init|=
operator|new
name|HashSet
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|running
operator|.
name|get
argument_list|()
operator|&&
name|watchURLs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|oldCounter
operator|!=
name|counter
operator|.
name|get
argument_list|()
condition|)
block|{
name|oldCounter
operator|=
name|counter
operator|.
name|get
argument_list|()
expr_stmt|;
name|watchedBundles
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|bundleURL
range|:
name|watchURLs
control|)
block|{
comment|// Transform into regexp
name|bundleURL
operator|=
name|bundleURL
operator|.
name|replaceAll
argument_list|(
literal|"\\*"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleService
operator|.
name|selectBundles
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|bundleURL
argument_list|)
argument_list|,
literal|false
argument_list|)
control|)
block|{
if|if
condition|(
name|isMavenSnapshotUrl
argument_list|(
name|getLocation
argument_list|(
name|bundle
argument_list|)
argument_list|)
condition|)
block|{
name|watchedBundles
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|watchedBundles
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// Get the wiring before any in case of a refresh of a dependency
name|FrameworkWiring
name|wiring
init|=
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
decl_stmt|;
name|File
name|localRepository
init|=
name|this
operator|.
name|localRepoDetector
operator|.
name|getLocalRepository
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|updated
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|watchedBundles
control|)
block|{
try|try
block|{
name|updateBundleIfNecessary
argument_list|(
name|localRepository
argument_list|,
name|updated
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Error watching bundle."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BundleException
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Error updating bundle."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|updated
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
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
name|wiring
operator|.
name|refreshBundles
argument_list|(
name|updated
argument_list|,
operator|new
name|FrameworkListener
argument_list|()
block|{
specifier|public
name|void
name|frameworkEvent
parameter_list|(
name|FrameworkEvent
name|event
parameter_list|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|running
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Bundle
name|bundle
range|:
name|updated
control|)
block|{
try|try
block|{
if|if
condition|(
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|FRAGMENT_HOST
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"[Watch] Bundle {} is a fragment, so it's not started"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|.
name|start
argument_list|(
name|Bundle
operator|.
name|START_TRANSIENT
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BundleException
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"[Watch] Error starting bundle"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
name|running
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Bundle watcher thread stopped"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getLocation
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|String
name|location
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_UPDATELOCATION
argument_list|)
decl_stmt|;
return|return
name|location
operator|!=
literal|null
condition|?
name|location
else|:
name|bundle
operator|.
name|getLocation
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|isMavenSnapshotUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
return|return
name|url
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
operator|&&
name|url
operator|.
name|contains
argument_list|(
literal|"SNAPSHOT"
argument_list|)
return|;
block|}
specifier|private
name|void
name|updateBundleIfNecessary
parameter_list|(
name|File
name|localRepository
parameter_list|,
name|List
argument_list|<
name|Bundle
argument_list|>
name|updated
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|BundleException
throws|,
name|IOException
block|{
name|File
name|location
init|=
name|getBundleExternalLocation
argument_list|(
name|localRepository
argument_list|,
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|!=
literal|null
operator|&&
name|location
operator|.
name|exists
argument_list|()
operator|&&
name|location
operator|.
name|lastModified
argument_list|()
operator|>
name|bundle
operator|.
name|getLastModified
argument_list|()
condition|)
block|{
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|location
argument_list|)
decl_stmt|;
try|try
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"[Watch] Updating watched bundle: {} ({})"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|FRAGMENT_HOST
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"[Watch] Bundle {} is a fragment, so it's not stopped"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|.
name|stop
argument_list|(
name|Bundle
operator|.
name|STOP_TRANSIENT
argument_list|)
expr_stmt|;
block|}
comment|// We don't really want to loose the update-location
name|String
name|updateLocation
init|=
name|getLocation
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|updateLocation
operator|.
name|equals
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|)
condition|)
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
name|updateLocation
argument_list|)
decl_stmt|;
try|try
init|(
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|bundle
operator|.
name|update
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|.
name|update
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|updated
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.dev.core.internal.BundleWatcher#add(java.lang.String)      */
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|boolean
name|shouldStart
init|=
name|running
operator|.
name|get
argument_list|()
operator|&&
operator|(
name|watchURLs
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
decl_stmt|;
if|if
condition|(
operator|!
name|watchURLs
operator|.
name|contains
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|watchURLs
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|shouldStart
condition|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.dev.core.internal.BundleWatcher#remove(java.lang.String)      */
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|watchURLs
operator|.
name|remove
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
comment|/**      * Return the location of the Bundle inside the local maven repository.      *       * @param localRepository the repository where to look for bundle update.      * @param bundle the bundle to check update.      * @return the updated file.      */
specifier|private
name|File
name|getBundleExternalLocation
parameter_list|(
name|File
name|localRepository
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
try|try
block|{
name|Parser
name|p
init|=
operator|new
name|Parser
argument_list|(
name|getLocation
argument_list|(
name|bundle
argument_list|)
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|localRepository
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|p
operator|.
name|getArtifactPath
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Could not parse artifact path for bundle"
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
name|bundleContext
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// start the watch thread
if|if
condition|(
name|running
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
if|if
condition|(
name|watchURLs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Stop the execution of the thread and releases the singleton instance.      */
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|running
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.dev.core.internal.BundleWatcher#getWatchURLs()      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getWatchURLs
parameter_list|()
block|{
return|return
name|watchURLs
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.karaf.dev.core.internal.BundleWatcher#setWatchURLs(java.util.List)      */
annotation|@
name|Override
specifier|public
name|void
name|setWatchURLs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|watchURLs
parameter_list|)
block|{
name|this
operator|.
name|watchURLs
operator|=
name|watchURLs
expr_stmt|;
block|}
specifier|public
name|long
name|getInterval
parameter_list|()
block|{
return|return
name|interval
return|;
block|}
specifier|public
name|void
name|setInterval
parameter_list|(
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|interval
operator|=
name|interval
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|running
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Bundle
argument_list|>
name|getBundlesByURL
parameter_list|(
name|String
name|urlFilter
parameter_list|)
block|{
name|urlFilter
operator|=
name|urlFilter
operator|.
name|replaceAll
argument_list|(
literal|"\\*"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
return|return
name|bundleService
operator|.
name|selectBundles
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|urlFilter
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

