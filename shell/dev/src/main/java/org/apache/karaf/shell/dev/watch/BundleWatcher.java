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
name|shell
operator|.
name|dev
operator|.
name|watch
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
name|Dictionary
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
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|maven
operator|.
name|commons
operator|.
name|MavenConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|maven
operator|.
name|commons
operator|.
name|MavenConfigurationImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|maven
operator|.
name|commons
operator|.
name|MavenRepositoryURL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|mvn
operator|.
name|ServiceConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|mvn
operator|.
name|internal
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|util
operator|.
name|property
operator|.
name|DictionaryPropertyResolver
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
name|BundleEvent
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
name|BundleListener
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
name|Configuration
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
comment|/**  * A Runnable singleton which watches at the defined location for bundle updates.  */
end_comment

begin_class
specifier|public
class|class
name|BundleWatcher
implements|implements
name|Runnable
implements|,
name|BundleListener
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
name|BundleWatcher
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|configurationAdmin
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
comment|/**      * Constructor      */
specifier|public
name|BundleWatcher
parameter_list|()
block|{     }
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
for|for
control|(
name|Bundle
name|bundle
range|:
name|getBundlesByURL
argument_list|(
name|bundleURL
argument_list|)
control|)
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
name|File
name|localRepository
init|=
name|getLocalRepository
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"[Watch] Updating watched bundle: "
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" ("
operator|+
name|bundle
operator|.
name|getVersion
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|update
argument_list|(
name|is
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
comment|/**      * Adds a Bundle URLs to the watch list.      * @param url      */
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
comment|/**      * Removes a bundle URLs from the watch list.      * @param url      */
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
comment|/**      * Returns the location of the Bundle inside the local maven repository.      * @param bundle      * @return      */
specifier|public
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
name|bundle
operator|.
name|getLocation
argument_list|()
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
name|File
name|getLocalRepository
parameter_list|()
block|{
comment|// Attempt to retrieve local repository location from MavenConfiguration
name|MavenConfiguration
name|configuration
init|=
name|retrieveMavenConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|MavenRepositoryURL
name|localRepositoryURL
init|=
name|configuration
operator|.
name|getLocalRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|localRepositoryURL
operator|!=
literal|null
condition|)
block|{
return|return
name|localRepositoryURL
operator|.
name|getFile
argument_list|()
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
block|}
comment|// If local repository not found assume default.
name|String
name|localRepo
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|".m2"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"repository"
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|localRepo
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
return|;
block|}
specifier|protected
name|MavenConfiguration
name|retrieveMavenConfiguration
parameter_list|()
block|{
name|MavenConfiguration
name|mavenConfiguration
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|ServiceConstants
operator|.
name|PID
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|Dictionary
name|dictonary
init|=
name|configuration
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|dictonary
operator|!=
literal|null
condition|)
block|{
name|DictionaryPropertyResolver
name|resolver
init|=
operator|new
name|DictionaryPropertyResolver
argument_list|(
name|dictonary
argument_list|)
decl_stmt|;
name|mavenConfiguration
operator|=
operator|new
name|MavenConfigurationImpl
argument_list|(
name|resolver
argument_list|,
name|ServiceConstants
operator|.
name|PID
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Error retrieving maven configuration"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|mavenConfiguration
return|;
block|}
comment|/**      * Returns the bundles that match      * @param url      * @return      */
specifier|public
name|List
argument_list|<
name|Bundle
argument_list|>
name|getBundlesByURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundleList
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|Long
name|id
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|bundleList
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bundleContext
operator|.
name|getBundles
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|isMavenSnapshotUrl
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|)
operator|&&
name|wildCardMatch
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
name|url
argument_list|)
condition|)
block|{
name|bundleList
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|bundleList
return|;
block|}
specifier|protected
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
comment|/**      * Matches text using a pattern containing wildcards.      *      * @param text      * @param pattern      * @return      */
specifier|protected
name|boolean
name|wildCardMatch
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|String
index|[]
name|cards
init|=
name|pattern
operator|.
name|split
argument_list|(
literal|"\\*"
argument_list|)
decl_stmt|;
comment|// Iterate over the cards.
for|for
control|(
name|String
name|card
range|:
name|cards
control|)
block|{
name|int
name|idx
init|=
name|text
operator|.
name|indexOf
argument_list|(
name|card
argument_list|)
decl_stmt|;
comment|// Card not detected in the text.
if|if
condition|(
name|idx
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Move ahead, towards the right of the text.
name|text
operator|=
name|text
operator|.
name|substring
argument_list|(
name|idx
operator|+
name|card
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// register the bundle listener
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
comment|/**      * Stops the execution of the thread and releases the singleton instance      */
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
comment|// unregister the bundle listener
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConfigurationAdmin
name|getConfigurationAdmin
parameter_list|()
block|{
return|return
name|configurationAdmin
return|;
block|}
specifier|public
name|void
name|setConfigurationAdmin
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|)
block|{
name|this
operator|.
name|configurationAdmin
operator|=
name|configurationAdmin
expr_stmt|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
specifier|public
name|void
name|setBundleContext
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
block|}
end_class

end_unit

