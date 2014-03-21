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
package|;
end_package

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
name|Arrays
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|BootFinished
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
name|FeaturesService
operator|.
name|Option
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
comment|/**  * Manages installation of the boot features in the background  */
end_comment

begin_class
specifier|public
class|class
name|BootFeaturesInstaller
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
name|BootFeaturesInstaller
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
name|VERSION_PREFIX
init|=
literal|"version="
decl_stmt|;
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
specifier|final
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|private
specifier|final
name|String
name|boot
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|bootAsynchronous
decl_stmt|;
comment|/**      *       * @param featuresService      * @param boot list of boot features separated by comma. Optionally contains ;version=x.x.x to specify a specific feature version      */
specifier|public
name|BootFeaturesInstaller
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|FeaturesService
name|featuresService
parameter_list|,
name|String
name|boot
parameter_list|,
name|boolean
name|bootAsynchronous
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
name|featuresService
operator|=
name|featuresService
expr_stmt|;
name|this
operator|.
name|boot
operator|=
name|boot
expr_stmt|;
name|this
operator|.
name|bootAsynchronous
operator|=
name|bootAsynchronous
expr_stmt|;
block|}
comment|/**      * Install boot features      * @throws Exception      */
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|boot
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bootAsynchronous
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
name|installBootFeatures
argument_list|()
expr_stmt|;
name|publishBootFinished
argument_list|()
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|installBootFeatures
argument_list|()
expr_stmt|;
name|publishBootFinished
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|publishBootFinished
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|installBootFeatures
parameter_list|()
block|{
try|try
block|{
name|List
argument_list|<
name|Feature
argument_list|>
name|installedFeatures
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresService
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|stagedFeatureNames
init|=
name|parseBootFeatures
argument_list|(
name|boot
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Set
argument_list|<
name|Feature
argument_list|>
argument_list|>
name|stagedFeatures
init|=
name|toFeatureSetList
argument_list|(
name|stagedFeatureNames
argument_list|)
decl_stmt|;
for|for
control|(
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
range|:
name|stagedFeatures
control|)
block|{
name|features
operator|.
name|removeAll
argument_list|(
name|installedFeatures
argument_list|)
expr_stmt|;
name|featuresService
operator|.
name|installFeatures
argument_list|(
name|features
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|Option
operator|.
name|NoCleanIfFailure
argument_list|,
name|Option
operator|.
name|ContinueBatchOnFailure
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
name|error
argument_list|(
literal|"Error installing boot features"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Set
argument_list|<
name|Feature
argument_list|>
argument_list|>
name|toFeatureSetList
parameter_list|(
name|List
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|stagedFeatures
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|Set
argument_list|<
name|Feature
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Set
argument_list|<
name|Feature
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Set
argument_list|<
name|String
argument_list|>
name|features
range|:
name|stagedFeatures
control|)
block|{
name|HashSet
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
for|for
control|(
name|String
name|featureName
range|:
name|features
control|)
block|{
try|try
block|{
name|Feature
name|feature
init|=
name|getFeature
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
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error Boot feature "
operator|+
name|featureName
operator|+
literal|" not found"
argument_list|)
expr_stmt|;
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
literal|"Error getting feature for feature string "
operator|+
name|featureName
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|add
argument_list|(
name|featureSet
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/** 	 *  	 * @param featureSt either feature name or<featurename>;version=<version> 	 * @return feature matching the feature string 	 * @throws Exception 	 */
specifier|private
name|Feature
name|getFeature
parameter_list|(
name|String
name|featureSt
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|parts
init|=
name|featureSt
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
name|String
name|featureName
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|String
name|featureVersion
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
comment|// if the part starts with "version=" it contains the version info
if|if
condition|(
name|part
operator|.
name|startsWith
argument_list|(
name|VERSION_PREFIX
argument_list|)
condition|)
block|{
name|featureVersion
operator|=
name|part
operator|.
name|substring
argument_list|(
name|VERSION_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|featureVersion
operator|==
literal|null
condition|)
block|{
comment|// no version specified - use default version
name|featureVersion
operator|=
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
operator|.
name|DEFAULT_VERSION
expr_stmt|;
block|}
return|return
name|featuresService
operator|.
name|getFeature
argument_list|(
name|featureName
argument_list|,
name|featureVersion
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|parseBootFeatures
parameter_list|(
name|String
name|bootFeatures
parameter_list|)
block|{
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(\\((.+))\\),|.+"
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|bootFeatures
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|group
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
operator|!=
literal|null
condition|?
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
else|:
name|matcher
operator|.
name|group
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|parseFeatureList
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|parseFeatureList
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|HashSet
argument_list|<
name|String
argument_list|>
name|features
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|group
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|"\\s*,\\s*"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|features
return|;
block|}
specifier|private
name|void
name|publishBootFinished
parameter_list|()
block|{
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
name|BootFinished
name|bootFinished
init|=
operator|new
name|BootFinished
argument_list|()
block|{}
decl_stmt|;
name|bundleContext
operator|.
name|registerService
argument_list|(
name|BootFinished
operator|.
name|class
argument_list|,
name|bootFinished
argument_list|,
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

