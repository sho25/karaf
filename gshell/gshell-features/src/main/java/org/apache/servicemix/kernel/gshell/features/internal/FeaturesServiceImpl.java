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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|features
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
name|BufferedInputStream
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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

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
name|Collection
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
name|jar
operator|.
name|JarInputStream
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
name|Manifest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|features
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
name|Version
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
name|osgi
operator|.
name|service
operator|.
name|prefs
operator|.
name|BackingStoreException
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
name|prefs
operator|.
name|Preferences
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
name|prefs
operator|.
name|PreferencesService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|BundleContextAware
import|;
end_import

begin_comment
comment|/**  * The Features service implementation.  * Adding a repository url will load the features contained in this repository and  * create dummy sub shells.  When invoked, these commands will prompt the user for  * installing the needed bundles.  *  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesServiceImpl
implements|implements
name|FeaturesService
implements|,
name|BundleContextAware
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ALIAS_KEY
init|=
literal|"_alias_factory_pid"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FeaturesServiceImpl
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
name|configAdmin
decl_stmt|;
specifier|private
name|PreferencesService
name|preferences
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|URI
argument_list|>
name|uris
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|URI
argument_list|,
name|RepositoryImpl
argument_list|>
name|repositories
init|=
operator|new
name|HashMap
argument_list|<
name|URI
argument_list|,
name|RepositoryImpl
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|features
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|installed
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|boot
decl_stmt|;
specifier|private
name|boolean
name|bootFeaturesInstalled
decl_stmt|;
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
name|ConfigurationAdmin
name|getConfigAdmin
parameter_list|()
block|{
return|return
name|configAdmin
return|;
block|}
specifier|public
name|void
name|setConfigAdmin
parameter_list|(
name|ConfigurationAdmin
name|configAdmin
parameter_list|)
block|{
name|this
operator|.
name|configAdmin
operator|=
name|configAdmin
expr_stmt|;
block|}
specifier|public
name|PreferencesService
name|getPreferences
parameter_list|()
block|{
return|return
name|preferences
return|;
block|}
specifier|public
name|void
name|setPreferences
parameter_list|(
name|PreferencesService
name|preferences
parameter_list|)
block|{
name|this
operator|.
name|preferences
operator|=
name|preferences
expr_stmt|;
block|}
specifier|public
name|void
name|setUrls
parameter_list|(
name|String
name|uris
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
index|[]
name|s
init|=
name|uris
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|this
operator|.
name|uris
operator|=
operator|new
name|HashSet
argument_list|<
name|URI
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|uris
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
name|s
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBoot
parameter_list|(
name|String
name|boot
parameter_list|)
block|{
name|this
operator|.
name|boot
operator|=
name|boot
expr_stmt|;
block|}
specifier|public
name|void
name|addRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|repositories
operator|.
name|values
argument_list|()
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|internalAddRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|saveState
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|internalAddRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|repositories
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|features
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|removeRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
if|if
condition|(
name|repositories
operator|.
name|values
argument_list|()
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|internalRemoveRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|saveState
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|internalRemoveRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|repositories
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|features
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|Repository
index|[]
name|listRepositories
parameter_list|()
block|{
name|Collection
argument_list|<
name|RepositoryImpl
argument_list|>
name|repos
init|=
name|repositories
operator|.
name|values
argument_list|()
decl_stmt|;
return|return
name|repos
operator|.
name|toArray
argument_list|(
operator|new
name|Repository
index|[
name|repos
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Feature
name|f
init|=
name|getFeature
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No feature named '"
operator|+
name|name
operator|+
literal|"' available"
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|dependency
range|:
name|f
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|installFeature
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|config
range|:
name|f
operator|.
name|getConfigurations
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|f
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
name|config
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
name|pid
init|=
name|parsePid
argument_list|(
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
name|pid
index|[
literal|1
index|]
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|ALIAS_KEY
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
name|Configuration
name|cfg
init|=
name|getConfiguration
argument_list|(
name|configAdmin
argument_list|,
name|pid
index|[
literal|0
index|]
argument_list|,
name|pid
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfg
operator|.
name|getBundleLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfg
operator|.
name|setBundleLocation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|cfg
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|Long
argument_list|>
name|bundles
init|=
operator|new
name|HashSet
argument_list|<
name|Long
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|bundleLocation
range|:
name|f
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|Bundle
name|b
init|=
name|installBundleIfNeeded
argument_list|(
name|bundleLocation
argument_list|)
decl_stmt|;
name|b
operator|.
name|start
argument_list|()
expr_stmt|;
name|bundles
operator|.
name|add
argument_list|(
name|b
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|installed
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|bundles
argument_list|)
expr_stmt|;
name|saveState
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|Bundle
name|installBundleIfNeeded
parameter_list|(
name|String
name|bundleLocation
parameter_list|)
throws|throws
name|IOException
throws|,
name|BundleException
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Checking "
operator|+
name|bundleLocation
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|URL
argument_list|(
name|bundleLocation
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|is
operator|.
name|mark
argument_list|(
literal|256
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|JarInputStream
name|jar
init|=
operator|new
name|JarInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Manifest
name|m
init|=
name|jar
operator|.
name|getManifest
argument_list|()
decl_stmt|;
name|String
name|sn
init|=
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|)
decl_stmt|;
name|String
name|vStr
init|=
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
decl_stmt|;
name|Version
name|v
init|=
name|vStr
operator|==
literal|null
condition|?
name|Version
operator|.
name|emptyVersion
else|:
name|Version
operator|.
name|parseVersion
argument_list|(
name|vStr
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|!=
literal|null
operator|&&
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|sn
argument_list|)
condition|)
block|{
name|vStr
operator|=
operator|(
name|String
operator|)
name|b
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
expr_stmt|;
name|Version
name|bv
init|=
name|vStr
operator|==
literal|null
condition|?
name|Version
operator|.
name|emptyVersion
else|:
name|Version
operator|.
name|parseVersion
argument_list|(
name|vStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|.
name|equals
argument_list|(
name|bv
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"  found installed bundle: "
operator|+
name|b
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
block|}
block|}
try|try
block|{
name|is
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|is
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|URL
argument_list|(
name|bundleLocation
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Installing bundle "
operator|+
name|bundleLocation
argument_list|)
expr_stmt|;
return|return
name|getBundleContext
argument_list|()
operator|.
name|installBundle
argument_list|(
name|bundleLocation
argument_list|,
name|is
argument_list|)
return|;
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
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|installed
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Feature named '"
operator|+
name|name
operator|+
literal|"' is not installed"
argument_list|)
throw|;
block|}
comment|// Grab all the bundles installed by this feature
comment|// and remove all those who will still be in use.
comment|// This gives this list of bundles to uninstall.
name|Set
argument_list|<
name|Long
argument_list|>
name|bundles
init|=
name|installed
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|Set
argument_list|<
name|Long
argument_list|>
name|b
range|:
name|installed
operator|.
name|values
argument_list|()
control|)
block|{
name|bundles
operator|.
name|removeAll
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|long
name|bundleId
range|:
name|bundles
control|)
block|{
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
name|bundleId
argument_list|)
operator|.
name|uninstall
argument_list|()
expr_stmt|;
block|}
name|saveState
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|listFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|f
range|:
name|getFeatures
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|String
name|installStatus
init|=
name|installed
operator|.
name|containsKey
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|)
condition|?
literal|"installed  "
else|:
literal|"uninstalled"
decl_stmt|;
name|String
name|version
init|=
name|f
operator|.
name|getVersion
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|version
operator|.
name|length
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
name|version
operator|=
literal|"       "
operator|+
name|version
expr_stmt|;
case|case
literal|2
case|:
name|version
operator|=
literal|"      "
operator|+
name|version
expr_stmt|;
case|case
literal|3
case|:
name|version
operator|=
literal|"     "
operator|+
name|version
expr_stmt|;
case|case
literal|4
case|:
name|version
operator|=
literal|"    "
operator|+
name|version
expr_stmt|;
case|case
literal|5
case|:
name|version
operator|=
literal|"   "
operator|+
name|version
expr_stmt|;
case|case
literal|6
case|:
name|version
operator|=
literal|"  "
operator|+
name|version
expr_stmt|;
case|case
literal|7
case|:
name|version
operator|=
literal|" "
operator|+
name|version
expr_stmt|;
block|}
name|features
operator|.
name|add
argument_list|(
literal|"["
operator|+
name|installStatus
operator|+
literal|"] "
operator|+
literal|" ["
operator|+
name|version
operator|+
literal|"] "
operator|+
name|f
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|features
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|features
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|listInstalledFeatures
parameter_list|()
block|{
return|return
name|installed
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|installed
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|protected
name|Feature
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getFeatures
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|getFeatures
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
comment|// Two phase load:
comment|// * first load dependent repositories
for|for
control|(
init|;
condition|;
control|)
block|{
name|boolean
name|newRepo
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|listRepositories
argument_list|()
control|)
block|{
for|for
control|(
name|URI
name|uri
range|:
name|repo
operator|.
name|getRepositories
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|repositories
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|internalAddRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|newRepo
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|newRepo
condition|)
block|{
break|break;
block|}
block|}
comment|// * then load all features
for|for
control|(
name|Repository
name|repo
range|:
name|repositories
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|Feature
name|f
range|:
name|repo
operator|.
name|getFeatures
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|features
operator|=
name|map
expr_stmt|;
block|}
return|return
name|features
return|;
block|}
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|loadState
argument_list|()
condition|)
block|{
if|if
condition|(
name|uris
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|URI
name|uri
range|:
name|uris
control|)
block|{
name|internalAddRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
name|saveState
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|boot
operator|!=
literal|null
operator|&&
operator|!
name|bootFeaturesInstalled
condition|)
block|{
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|String
index|[]
name|list
init|=
name|boot
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|f
range|:
name|list
control|)
block|{
if|if
condition|(
name|f
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|installFeature
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error installing boot feature "
operator|+
name|f
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|bootFeaturesInstalled
operator|=
literal|true
expr_stmt|;
name|saveState
argument_list|()
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|uris
operator|=
operator|new
name|HashSet
argument_list|<
name|URI
argument_list|>
argument_list|(
name|repositories
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|repositories
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|internalRemoveRepository
argument_list|(
name|repositories
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
index|[]
name|parsePid
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|int
name|n
init|=
name|pid
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|String
name|factoryPid
init|=
name|pid
operator|.
name|substring
argument_list|(
name|n
operator|+
literal|1
argument_list|)
decl_stmt|;
name|pid
operator|=
name|pid
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
name|pid
block|,
name|factoryPid
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[]
block|{
name|pid
block|,
literal|null
block|}
return|;
block|}
block|}
specifier|protected
name|Configuration
name|getConfiguration
parameter_list|(
name|ConfigurationAdmin
name|configurationAdmin
parameter_list|,
name|String
name|pid
parameter_list|,
name|String
name|factoryPid
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
if|if
condition|(
name|factoryPid
operator|!=
literal|null
condition|)
block|{
name|Configuration
index|[]
name|configs
init|=
name|configurationAdmin
operator|.
name|listConfigurations
argument_list|(
literal|"(|("
operator|+
name|ALIAS_KEY
operator|+
literal|"="
operator|+
name|pid
operator|+
literal|")(.alias_factory_pid="
operator|+
name|factoryPid
operator|+
literal|"))"
argument_list|)
decl_stmt|;
if|if
condition|(
name|configs
operator|==
literal|null
operator|||
name|configs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|configurationAdmin
operator|.
name|createFactoryConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|configs
index|[
literal|0
index|]
return|;
block|}
block|}
else|else
block|{
return|return
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
specifier|protected
name|void
name|saveState
parameter_list|()
block|{
try|try
block|{
name|Preferences
name|prefs
init|=
name|preferences
operator|.
name|getUserPreferences
argument_list|(
literal|"FeaturesServiceState"
argument_list|)
decl_stmt|;
name|saveSet
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"repositories"
argument_list|)
argument_list|,
name|repositories
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|saveMap
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"features"
argument_list|)
argument_list|,
name|installed
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|putBoolean
argument_list|(
literal|"bootFeaturesInstalled"
argument_list|,
name|bootFeaturesInstalled
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error persisting FeaturesService state"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|loadState
parameter_list|()
block|{
try|try
block|{
name|Preferences
name|prefs
init|=
name|preferences
operator|.
name|getUserPreferences
argument_list|(
literal|"FeaturesServiceState"
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefs
operator|.
name|nodeExists
argument_list|(
literal|"repositories"
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|URI
argument_list|>
name|repositories
init|=
name|loadSet
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"repositories"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|URI
name|repo
range|:
name|repositories
control|)
block|{
name|internalAddRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
name|installed
operator|=
name|loadMap
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"features"
argument_list|)
argument_list|)
expr_stmt|;
name|bootFeaturesInstalled
operator|=
name|prefs
operator|.
name|getBoolean
argument_list|(
literal|"bootFeaturesInstalled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error loading FeaturesService state"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|saveSet
parameter_list|(
name|Preferences
name|node
parameter_list|,
name|Set
argument_list|<
name|URI
argument_list|>
name|set
parameter_list|)
throws|throws
name|BackingStoreException
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|node
operator|.
name|clear
argument_list|()
expr_stmt|;
name|node
operator|.
name|putInt
argument_list|(
literal|"count"
argument_list|,
name|l
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|l
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|node
operator|.
name|put
argument_list|(
literal|"item."
operator|+
name|i
argument_list|,
name|l
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Set
argument_list|<
name|URI
argument_list|>
name|loadSet
parameter_list|(
name|Preferences
name|node
parameter_list|)
block|{
name|Set
argument_list|<
name|URI
argument_list|>
name|l
init|=
operator|new
name|HashSet
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|node
operator|.
name|getInt
argument_list|(
literal|"count"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|l
operator|.
name|add
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|"item."
operator|+
name|i
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|l
return|;
block|}
specifier|protected
name|void
name|saveMap
parameter_list|(
name|Preferences
name|node
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
name|map
parameter_list|)
throws|throws
name|BackingStoreException
block|{
name|node
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|createValue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|loadMap
parameter_list|(
name|Preferences
name|node
parameter_list|)
throws|throws
name|BackingStoreException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|node
operator|.
name|keys
argument_list|()
control|)
block|{
name|String
name|val
init|=
name|node
operator|.
name|get
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Long
argument_list|>
name|set
init|=
name|readValue
argument_list|(
name|val
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|set
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|protected
name|String
name|createValue
parameter_list|(
name|Set
argument_list|<
name|Long
argument_list|>
name|set
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
range|:
name|set
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|i
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
specifier|protected
name|Set
argument_list|<
name|Long
argument_list|>
name|readValue
parameter_list|(
name|String
name|val
parameter_list|)
block|{
name|Set
argument_list|<
name|Long
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|Long
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|str
range|:
name|val
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|set
operator|.
name|add
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|str
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|set
return|;
block|}
block|}
end_class

end_unit

