begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|kar
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
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|EnumSet
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
name|TimeUnit
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
name|jar
operator|.
name|JarEntry
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
name|JarOutputStream
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
name|ConfigFileInfo
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
name|Dependency
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
name|Repository
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
name|kar
operator|.
name|KarService
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
name|StreamUtils
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
comment|/**  * Implementation of the KAR service.  */
end_comment

begin_class
specifier|public
class|class
name|KarServiceImpl
implements|implements
name|KarService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FEATURE_CONFIG_FILE
init|=
literal|"features.cfg"
decl_stmt|;
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
name|KarServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|File
name|storage
decl_stmt|;
specifier|private
name|File
name|base
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|private
name|boolean
name|noAutoRefreshBundles
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Kar
argument_list|>
name|unsatisfiedKars
decl_stmt|;
specifier|private
name|AtomicBoolean
name|busy
decl_stmt|;
specifier|private
name|DelayedDeployerThread
name|delayedDeployerThread
decl_stmt|;
specifier|public
name|KarServiceImpl
parameter_list|(
name|String
name|karafBase
parameter_list|,
name|String
name|karStorage
parameter_list|,
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
operator|new
name|File
argument_list|(
name|karafBase
argument_list|)
expr_stmt|;
name|this
operator|.
name|storage
operator|=
operator|new
name|File
argument_list|(
name|karStorage
argument_list|)
expr_stmt|;
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
name|this
operator|.
name|storage
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|storage
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"KAR storage "
operator|+
name|storage
operator|+
literal|" is not a directory"
argument_list|)
throw|;
block|}
name|unsatisfiedKars
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Kar
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|busy
operator|=
operator|new
name|AtomicBoolean
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|install
parameter_list|(
name|URI
name|karUri
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|karName
init|=
operator|new
name|Kar
argument_list|(
name|karUri
argument_list|)
operator|.
name|getKarName
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Installing KAR {} from {}"
argument_list|,
name|karName
argument_list|,
name|karUri
argument_list|)
expr_stmt|;
name|File
name|karDir
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|karName
argument_list|)
decl_stmt|;
name|install
argument_list|(
name|karUri
argument_list|,
name|karDir
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|install
parameter_list|(
name|URI
name|karUri
parameter_list|,
name|File
name|repoDir
parameter_list|,
name|File
name|resourceDir
parameter_list|)
throws|throws
name|Exception
block|{
name|busy
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|Kar
name|kar
init|=
operator|new
name|Kar
argument_list|(
name|karUri
argument_list|)
decl_stmt|;
name|kar
operator|.
name|extract
argument_list|(
name|repoDir
argument_list|,
name|resourceDir
argument_list|)
expr_stmt|;
name|writeToFile
argument_list|(
name|kar
operator|.
name|getFeatureRepos
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
name|FEATURE_CONFIG_FILE
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|URI
name|uri
range|:
name|kar
operator|.
name|getFeatureRepos
argument_list|()
control|)
block|{
name|addToFeaturesRepositories
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|kar
operator|.
name|isShouldInstallFeatures
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|featureRepos
init|=
name|kar
operator|.
name|getFeatureRepos
argument_list|()
decl_stmt|;
name|Dependency
name|missingDependency
init|=
name|findMissingDependency
argument_list|(
name|featureRepos
argument_list|)
decl_stmt|;
if|if
condition|(
name|missingDependency
operator|==
literal|null
condition|)
block|{
name|installFeatures
argument_list|(
name|featureRepos
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Feature dependency {} is not available. Kar deployment postponed to see if it is about to be deployed"
argument_list|,
name|missingDependency
argument_list|)
expr_stmt|;
name|unsatisfiedKars
operator|.
name|add
argument_list|(
name|kar
argument_list|)
expr_stmt|;
if|if
condition|(
name|delayedDeployerThread
operator|==
literal|null
condition|)
block|{
name|delayedDeployerThread
operator|=
operator|new
name|DelayedDeployerThread
argument_list|()
expr_stmt|;
name|delayedDeployerThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|unsatisfiedKars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Kar
argument_list|>
name|iterator
init|=
name|unsatisfiedKars
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Kar
name|delayedKar
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|findMissingDependency
argument_list|(
name|delayedKar
operator|.
name|getFeatureRepos
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Dependencies of kar {} are now satisfied. Installing"
argument_list|,
name|delayedKar
operator|.
name|getKarName
argument_list|()
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|installFeatures
argument_list|(
name|delayedKar
operator|.
name|getFeatureRepos
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|unsatisfiedKars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|delayedDeployerThread
operator|!=
literal|null
condition|)
block|{
name|delayedDeployerThread
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
name|delayedDeployerThread
operator|=
literal|null
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|busy
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * checks if all required features are available      * @param featureRepos the repositories within the kar      * @return<code>null</code> if the contained features have no unresolvable dependencies. Otherwise the first missing dependency      * @throws Exception      */
specifier|private
name|Dependency
name|findMissingDependency
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|featureRepos
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|URI
name|uri
range|:
name|featureRepos
control|)
block|{
name|Feature
index|[]
name|includedFeatures
init|=
name|featuresService
operator|.
name|getRepository
argument_list|(
name|uri
argument_list|)
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|includedFeature
range|:
name|includedFeatures
control|)
block|{
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
init|=
name|includedFeature
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
for|for
control|(
name|Dependency
name|dependency
range|:
name|dependencies
control|)
block|{
name|Feature
name|feature
init|=
name|featuresService
operator|.
name|getFeature
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|,
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|feature
operator|==
literal|null
condition|)
block|{
return|return
name|dependency
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|URI
argument_list|>
name|readFromFile
parameter_list|(
name|File
name|repoListFile
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|URI
argument_list|>
name|uriList
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|FileReader
name|fr
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fr
operator|=
operator|new
name|FileReader
argument_list|(
name|repoListFile
argument_list|)
expr_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
name|fr
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|uriList
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"Error reading repo list from file "
operator|+
name|repoListFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|fr
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error closing reader for file "
operator|+
name|repoListFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|uriList
return|;
block|}
specifier|private
name|void
name|writeToFile
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositoriesInKar
parameter_list|,
name|File
name|repoListFile
parameter_list|)
block|{
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
name|PrintStream
name|ps
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|repoListFile
argument_list|)
expr_stmt|;
name|ps
operator|=
operator|new
name|PrintStream
argument_list|(
name|fos
argument_list|)
expr_stmt|;
for|for
control|(
name|URI
name|uri
range|:
name|featuresRepositoriesInKar
control|)
block|{
name|ps
operator|.
name|println
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|ps
operator|.
name|close
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
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
name|RuntimeException
argument_list|(
literal|"Error writing feature repo list to file "
operator|+
name|repoListFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeStream
argument_list|(
name|ps
argument_list|)
expr_stmt|;
name|closeStream
argument_list|(
name|fos
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|deleteRecursively
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
if|if
condition|(
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|children
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|child
range|:
name|children
control|)
block|{
name|deleteRecursively
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
name|dir
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstall
parameter_list|(
name|String
name|karName
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|karDir
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|,
name|karName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|karDir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The KAR "
operator|+
name|karName
operator|+
literal|" is not installed"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositories
init|=
name|readFromFile
argument_list|(
operator|new
name|File
argument_list|(
name|karDir
argument_list|,
name|FEATURE_CONFIG_FILE
argument_list|)
argument_list|)
decl_stmt|;
name|uninstallFeatures
argument_list|(
name|featuresRepositories
argument_list|)
expr_stmt|;
for|for
control|(
name|URI
name|featuresRepository
range|:
name|featuresRepositories
control|)
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|featuresRepository
argument_list|)
expr_stmt|;
block|}
name|deleteRecursively
argument_list|(
name|karDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|kars
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
name|File
name|kar
range|:
name|storage
operator|.
name|listFiles
argument_list|()
control|)
block|{
if|if
condition|(
name|kar
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|kars
operator|.
name|add
argument_list|(
name|kar
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|kars
return|;
block|}
comment|/**      * Add an URI to the list of features repositories.      *      * @param uri the URI to add.      * @throws Exception in case of add failure.      */
specifier|private
name|void
name|addToFeaturesRepositories
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|featuresService
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Added feature repository '{}'"
argument_list|,
name|uri
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
name|warn
argument_list|(
literal|"Unable to add repository '{}'"
argument_list|,
name|uri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Install all features contained in the list of features XML.      *      * @param featuresRepositories the list of features XML.      */
specifier|private
name|void
name|installFeatures
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositories
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
for|for
control|(
name|URI
name|karFeatureRepoUri
range|:
name|featuresRepositories
control|)
block|{
if|if
condition|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|karFeatureRepoUri
argument_list|)
condition|)
block|{
try|try
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
try|try
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"noAutoRefreshBundles is "
operator|+
name|isNoAutoRefreshBundles
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNoAutoRefreshBundles
argument_list|()
condition|)
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|warn
argument_list|(
literal|"Unable to install Kar feature {}"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|warn
argument_list|(
literal|"Can't get features for KAR {}"
argument_list|,
name|karFeatureRepoUri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|create
parameter_list|(
name|String
name|repoName
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|PrintStream
name|console
parameter_list|)
block|{
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
name|JarOutputStream
name|jos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Repository
name|repo
init|=
name|featuresService
operator|.
name|getRepository
argument_list|(
name|repoName
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find a repository with name "
operator|+
name|repoName
argument_list|)
throw|;
block|}
name|String
name|karPath
init|=
name|storage
operator|+
name|File
operator|.
name|separator
operator|+
name|repoName
operator|+
literal|".kar"
decl_stmt|;
name|File
name|karFile
init|=
operator|new
name|File
argument_list|(
name|karPath
argument_list|)
decl_stmt|;
name|karFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|karFile
argument_list|)
expr_stmt|;
name|Manifest
name|manifest
init|=
name|createNonAutoStartManifest
argument_list|(
name|repo
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
name|jos
operator|=
operator|new
name|JarOutputStream
argument_list|(
operator|new
name|BufferedOutputStream
argument_list|(
name|fos
argument_list|,
literal|100000
argument_list|)
argument_list|,
name|manifest
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|URI
argument_list|,
name|Integer
argument_list|>
name|locationMap
init|=
operator|new
name|HashMap
argument_list|<
name|URI
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|copyResourceToJar
argument_list|(
name|jos
argument_list|,
name|repo
operator|.
name|getURI
argument_list|()
argument_list|,
name|locationMap
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featureMap
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
for|for
control|(
name|Feature
name|feature
range|:
name|repo
operator|.
name|getFeatures
argument_list|()
control|)
block|{
name|featureMap
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|feature
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresToCopy
init|=
name|getFeatures
argument_list|(
name|featureMap
argument_list|,
name|features
argument_list|,
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|featuresToCopy
control|)
block|{
if|if
condition|(
name|console
operator|!=
literal|null
condition|)
name|console
operator|.
name|println
argument_list|(
literal|"Adding feature "
operator|+
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|copyFeatureToJar
argument_list|(
name|jos
argument_list|,
name|feature
argument_list|,
name|locationMap
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|console
operator|!=
literal|null
condition|)
name|console
operator|.
name|println
argument_list|(
literal|"Kar file created : "
operator|+
name|karPath
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
literal|"Error creating kar: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeStream
argument_list|(
name|jos
argument_list|)
expr_stmt|;
name|closeStream
argument_list|(
name|fos
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Set
argument_list|<
name|Feature
argument_list|>
name|getFeatures
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
name|featureMap
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|featureSet
init|=
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|depth
operator|>
literal|5
condition|)
block|{
comment|// Break after some recursions to avoid endless loops
return|return
name|featureSet
return|;
block|}
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|featureSet
operator|.
name|addAll
argument_list|(
name|featureMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|featureSet
return|;
block|}
for|for
control|(
name|String
name|featureName
range|:
name|features
control|)
block|{
name|Feature
name|feature
init|=
name|featureMap
operator|.
name|get
argument_list|(
name|featureName
argument_list|)
decl_stmt|;
if|if
condition|(
name|feature
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Feature "
operator|+
name|featureName
operator|+
literal|" not found in repository."
argument_list|)
expr_stmt|;
comment|//throw new RuntimeException();
block|}
else|else
block|{
name|featureSet
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Dependency
argument_list|>
name|deps
init|=
name|feature
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|depNames
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
name|Dependency
name|dependency
range|:
name|deps
control|)
block|{
name|depNames
operator|.
name|add
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|featureSet
operator|.
name|addAll
argument_list|(
name|getFeatures
argument_list|(
name|featureMap
argument_list|,
name|depNames
argument_list|,
name|depth
operator|++
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|featureSet
return|;
block|}
specifier|private
name|Manifest
name|createNonAutoStartManifest
parameter_list|(
name|URI
name|repoUri
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|IOException
block|{
name|String
name|manifestSt
init|=
literal|"Manifest-Version: 1.0\n"
operator|+
name|Kar
operator|.
name|MANIFEST_ATTR_KARAF_FEATURE_START
operator|+
literal|": false\n"
operator|+
name|Kar
operator|.
name|MANIFEST_ATTR_KARAF_FEATURE_REPOS
operator|+
literal|": "
operator|+
name|repoUri
operator|.
name|toString
argument_list|()
operator|+
literal|"\n"
decl_stmt|;
name|InputStream
name|manifestIs
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|manifestSt
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Manifest
name|manifest
init|=
operator|new
name|Manifest
argument_list|(
name|manifestIs
argument_list|)
decl_stmt|;
return|return
name|manifest
return|;
block|}
specifier|private
name|void
name|closeStream
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error closing stream"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|copyFeatureToJar
parameter_list|(
name|JarOutputStream
name|jos
parameter_list|,
name|Feature
name|feature
parameter_list|,
name|Map
argument_list|<
name|URI
argument_list|,
name|Integer
argument_list|>
name|locationMap
parameter_list|)
throws|throws
name|URISyntaxException
block|{
for|for
control|(
name|BundleInfo
name|bundleInfo
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|URI
name|location
init|=
operator|new
name|URI
argument_list|(
name|bundleInfo
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|copyResourceToJar
argument_list|(
name|jos
argument_list|,
name|location
argument_list|,
name|locationMap
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ConfigFileInfo
name|configFileInfo
range|:
name|feature
operator|.
name|getConfigurationFiles
argument_list|()
control|)
block|{
name|URI
name|location
init|=
operator|new
name|URI
argument_list|(
name|configFileInfo
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|copyResourceToJar
argument_list|(
name|jos
argument_list|,
name|location
argument_list|,
name|locationMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|copyResourceToJar
parameter_list|(
name|JarOutputStream
name|jos
parameter_list|,
name|URI
name|location
parameter_list|,
name|Map
argument_list|<
name|URI
argument_list|,
name|Integer
argument_list|>
name|locationMap
parameter_list|)
block|{
if|if
condition|(
name|locationMap
operator|.
name|containsKey
argument_list|(
name|location
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
block|{
name|String
name|noPrefixLocation
init|=
name|location
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
name|location
operator|.
name|toString
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
name|noPrefixLocation
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|"repository/"
operator|+
name|parser
operator|.
name|getArtifactPath
argument_list|()
decl_stmt|;
name|jos
operator|.
name|putNextEntry
argument_list|(
operator|new
name|JarEntry
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|location
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|jos
argument_list|)
expr_stmt|;
block|}
name|locationMap
operator|.
name|put
argument_list|(
name|location
argument_list|,
literal|1
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
literal|"Error adding "
operator|+
name|location
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Uninstall all features contained in the list of features XML.      *      * @param featuresRepositories the list of features XML.      */
specifier|private
name|void
name|uninstallFeatures
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|featuresRepositories
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
for|for
control|(
name|URI
name|karFeatureRepoUri
range|:
name|featuresRepositories
control|)
block|{
if|if
condition|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|karFeatureRepoUri
argument_list|)
condition|)
block|{
try|try
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
try|try
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|feature
operator|.
name|getVersion
argument_list|()
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
name|warn
argument_list|(
literal|"Unable to uninstall Kar feature {}"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|warn
argument_list|(
literal|"Can't get features for KAR {}"
argument_list|,
name|karFeatureRepoUri
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|boolean
name|isNoAutoRefreshBundles
parameter_list|()
block|{
return|return
name|noAutoRefreshBundles
return|;
block|}
specifier|public
name|void
name|setNoAutoRefreshBundles
parameter_list|(
name|boolean
name|noAutoRefreshBundles
parameter_list|)
block|{
name|this
operator|.
name|noAutoRefreshBundles
operator|=
name|noAutoRefreshBundles
expr_stmt|;
block|}
specifier|private
class|class
name|DelayedDeployerThread
extends|extends
name|Thread
block|{
specifier|private
name|AtomicBoolean
name|cancel
decl_stmt|;
specifier|public
name|DelayedDeployerThread
parameter_list|()
block|{
name|super
argument_list|(
literal|"Delayed kar deployment"
argument_list|)
expr_stmt|;
name|cancel
operator|=
operator|new
name|AtomicBoolean
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|cancel
parameter_list|()
block|{
name|cancel
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
name|busy
operator|.
name|get
argument_list|()
operator|&&
operator|!
name|cancel
operator|.
name|get
argument_list|()
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|toMillis
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// nothing to do
block|}
if|if
condition|(
operator|!
name|cancel
operator|.
name|get
argument_list|()
condition|)
block|{
name|installDelayedKars
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|installDelayedKars
parameter_list|()
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Kar
argument_list|>
name|iterator
init|=
name|unsatisfiedKars
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Kar
name|kar
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
try|try
block|{
name|installFeatures
argument_list|(
name|kar
operator|.
name|getFeatureRepos
argument_list|()
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
literal|"Delayed deployment of kar "
operator|+
name|kar
operator|.
name|getKarName
argument_list|()
operator|+
literal|" failed"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

