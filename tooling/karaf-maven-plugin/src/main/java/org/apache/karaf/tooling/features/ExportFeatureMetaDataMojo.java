begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|features
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
name|IOException
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
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|model
operator|.
name|Bundle
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
name|JaxbUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
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

begin_comment
comment|/**  * Export meta data about features  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"features-export-meta-data"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|COMPILE
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|)
specifier|public
class|class
name|ExportFeatureMetaDataMojo
extends|extends
name|AbstractFeatureMojo
block|{
comment|/**      * If set to true then all bundles will be merged into one combined feature.      * In this case duplicates will be eliminated      */
annotation|@
name|Parameter
specifier|private
name|boolean
name|mergedFeature
decl_stmt|;
comment|/**      * If set to true then for each bundle symbolic name only the highest version will be used      */
annotation|@
name|Parameter
specifier|protected
name|boolean
name|oneVersion
decl_stmt|;
comment|/**      * Name of the file for exported feature meta data      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/features.xml"
argument_list|)
specifier|private
name|File
name|metaDataFile
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresSet
init|=
name|resolveFeatures
argument_list|()
decl_stmt|;
if|if
condition|(
name|mergedFeature
condition|)
block|{
name|Feature
name|feature
init|=
name|oneVersion
condition|?
name|mergeFeatureOneVersion
argument_list|(
name|featuresSet
argument_list|)
else|:
name|mergeFeature
argument_list|(
name|featuresSet
argument_list|)
decl_stmt|;
name|featuresSet
operator|=
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|()
expr_stmt|;
name|featuresSet
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|metaDataFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|Features
name|features
init|=
operator|new
name|Features
argument_list|()
decl_stmt|;
name|features
operator|.
name|getFeature
argument_list|()
operator|.
name|addAll
argument_list|(
name|featuresSet
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|metaDataFile
argument_list|)
init|)
block|{
name|JaxbUtil
operator|.
name|marshal
argument_list|(
name|features
argument_list|,
name|os
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
literal|"Error writing feature meta data to "
operator|+
name|metaDataFile
operator|+
literal|": "
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
block|}
specifier|private
name|Feature
name|mergeFeature
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresSet
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|Feature
name|merged
init|=
operator|new
name|Feature
argument_list|(
literal|"merged"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|bundleIds
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|featuresSet
control|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|feature
operator|.
name|getBundle
argument_list|()
control|)
block|{
name|String
name|symbolicName
init|=
name|getBundleSymbolicName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|symbolicName
operator|==
literal|null
condition|)
block|{
name|logIgnored
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|String
name|bundleId
init|=
name|symbolicName
operator|+
literal|":"
operator|+
name|getBundleVersion
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|bundleIds
operator|.
name|contains
argument_list|(
name|bundleId
argument_list|)
condition|)
block|{
name|bundleIds
operator|.
name|add
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
name|merged
operator|.
name|getBundle
argument_list|()
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
name|merged
return|;
block|}
specifier|private
name|Feature
name|mergeFeatureOneVersion
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresSet
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|Feature
name|merged
init|=
operator|new
name|Feature
argument_list|(
literal|"merged"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Bundle
argument_list|>
name|bundleVersions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|featuresSet
control|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|feature
operator|.
name|getBundle
argument_list|()
control|)
block|{
name|String
name|symbolicName
init|=
name|getBundleSymbolicName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|symbolicName
operator|==
literal|null
condition|)
block|{
name|logIgnored
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Bundle
name|existingBundle
init|=
name|bundleVersions
operator|.
name|get
argument_list|(
name|symbolicName
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingBundle
operator|!=
literal|null
condition|)
block|{
name|Version
name|existingVersion
init|=
operator|new
name|Version
argument_list|(
name|getBundleVersion
argument_list|(
name|existingBundle
argument_list|)
argument_list|)
decl_stmt|;
name|Version
name|newVersion
init|=
operator|new
name|Version
argument_list|(
name|getBundleVersion
argument_list|(
name|bundle
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|newVersion
operator|.
name|compareTo
argument_list|(
name|existingVersion
argument_list|)
operator|>
literal|0
condition|)
block|{
name|bundleVersions
operator|.
name|put
argument_list|(
name|symbolicName
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|bundleVersions
operator|.
name|put
argument_list|(
name|symbolicName
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleVersions
operator|.
name|values
argument_list|()
control|)
block|{
name|merged
operator|.
name|getBundles
argument_list|()
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
return|return
name|merged
return|;
block|}
specifier|private
name|void
name|logIgnored
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Ignoring jar without BundleSymbolicName: "
operator|+
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Attributes
argument_list|>
name|manifests
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|getBundleVersion
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
return|return
name|getManifest
argument_list|(
name|bundle
argument_list|)
operator|.
name|getValue
argument_list|(
literal|"Bundle-Version"
argument_list|)
return|;
block|}
specifier|private
name|String
name|getBundleSymbolicName
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
return|return
name|getManifest
argument_list|(
name|bundle
argument_list|)
operator|.
name|getValue
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|)
return|;
block|}
specifier|private
name|Attributes
name|getManifest
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|Attributes
name|attributes
init|=
name|manifests
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
name|skipNonMavenProtocols
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
name|resolveArtifact
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
argument_list|)
init|)
block|{
name|Manifest
name|manifest
init|=
name|jis
operator|.
name|getManifest
argument_list|()
decl_stmt|;
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
name|attributes
operator|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|attributes
operator|=
operator|new
name|Attributes
argument_list|()
expr_stmt|;
block|}
name|manifests
operator|.
name|put
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
name|attributes
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
name|MojoExecutionException
argument_list|(
literal|"Error reading bundle manifest from "
operator|+
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|attributes
return|;
block|}
block|}
end_class

end_unit

