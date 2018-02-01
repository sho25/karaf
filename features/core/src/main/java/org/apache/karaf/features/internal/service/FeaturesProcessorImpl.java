begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|FileNotFoundException
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|FeaturePattern
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
name|LocationPattern
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
name|Conditional
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
name|processing
operator|.
name|BundleReplacements
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
name|processing
operator|.
name|FeatureReplacements
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
name|processing
operator|.
name|FeaturesProcessing
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
name|processing
operator|.
name|OverrideBundleDependency
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
comment|/**  * Configurable {@link FeaturesProcessor}, controlled by several files from<code>etc/</code> directory:<ul>  *<li><code>etc/overrides.properties</code>: may alter bundle versions in features</li>  *<li><code>etc/blacklisted.properties</code>: may filter out some features/bundles</li>  *<li><code>etc/org.apache.karaf.features.xml</code> (<strong>new!</strong>): incorporates two above files  *     and may define additional processing (changing G/A/V, adding bundles to features, changing<code>dependency</code>  *     attributes, ...)</li>  *</ul>  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesProcessorImpl
implements|implements
name|FeaturesProcessor
block|{
specifier|public
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FeaturesProcessorImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|FeaturesProcessingSerializer
name|serializer
init|=
operator|new
name|FeaturesProcessingSerializer
argument_list|()
decl_stmt|;
comment|// empty, but fully functional features processing configuration
specifier|private
name|FeaturesProcessing
name|processing
init|=
operator|new
name|FeaturesProcessing
argument_list|()
decl_stmt|;
comment|/**      * Creates instance of features processor using 1 external URI, additional {@link Blacklist} instance      * and additional set of override clauses.      */
specifier|public
name|FeaturesProcessorImpl
parameter_list|(
name|String
name|featureModificationsURI
parameter_list|,
name|String
name|featureProcessingVersions
parameter_list|,
name|Blacklist
name|blacklistDefinitions
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|overrides
parameter_list|)
block|{
if|if
condition|(
name|featureModificationsURI
operator|!=
literal|null
condition|)
block|{
try|try
block|{
try|try
init|(
name|InputStream
name|stream
init|=
operator|new
name|URL
argument_list|(
name|featureModificationsURI
argument_list|)
operator|.
name|openStream
argument_list|()
init|)
block|{
name|Properties
name|versions
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|featureProcessingVersions
operator|!=
literal|null
condition|)
block|{
name|File
name|versionsProperties
init|=
operator|new
name|File
argument_list|(
operator|new
name|URL
argument_list|(
name|featureProcessingVersions
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionsProperties
operator|.
name|isFile
argument_list|()
condition|)
block|{
try|try
init|(
name|InputStream
name|propsStream
init|=
operator|new
name|URL
argument_list|(
name|featureProcessingVersions
argument_list|)
operator|.
name|openStream
argument_list|()
init|)
block|{
name|versions
operator|.
name|load
argument_list|(
name|propsStream
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|processing
operator|=
name|serializer
operator|.
name|read
argument_list|(
name|stream
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Can't find feature processing file ("
operator|+
name|featureModificationsURI
operator|+
literal|"), skipping"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't initialize feature processor: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|processing
operator|.
name|postUnmarshall
argument_list|(
name|blacklistDefinitions
argument_list|,
name|overrides
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates instance of features processor using 3 external (optional) URIs.      */
specifier|public
name|FeaturesProcessorImpl
parameter_list|(
name|String
name|featureModificationsURI
parameter_list|,
name|String
name|featureProcessingVersions
parameter_list|,
name|String
name|blacklistedURI
parameter_list|,
name|String
name|overridesURI
parameter_list|)
block|{
name|this
argument_list|(
name|featureModificationsURI
argument_list|,
name|featureProcessingVersions
argument_list|,
operator|new
name|Blacklist
argument_list|(
name|blacklistedURI
argument_list|)
argument_list|,
name|Overrides
operator|.
name|loadOverrides
argument_list|(
name|overridesURI
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates instance of features processor using {@link FeaturesServiceConfig configuration object} where      * three files may be specified: overrides.properties, blacklisted.properties and org.apache.karaf.features.xml.      */
specifier|public
name|FeaturesProcessorImpl
parameter_list|(
name|FeaturesServiceConfig
name|configuration
parameter_list|)
block|{
name|this
argument_list|(
name|configuration
operator|.
name|featureModifications
argument_list|,
name|configuration
operator|.
name|featureProcessingVersions
argument_list|,
name|configuration
operator|.
name|blacklisted
argument_list|,
name|configuration
operator|.
name|overrides
argument_list|)
expr_stmt|;
block|}
comment|/**      * Writes model to output stream.      */
specifier|public
name|void
name|writeInstructions
parameter_list|(
name|OutputStream
name|output
parameter_list|)
block|{
name|serializer
operator|.
name|write
argument_list|(
name|processing
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FeaturesProcessing
name|getInstructions
parameter_list|()
block|{
return|return
name|processing
return|;
block|}
comment|/**      * For the purpose of assembly builder, we can configure additional overrides that are read from profiles.      */
specifier|public
name|void
name|addOverrides
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|overrides
parameter_list|)
block|{
name|processing
operator|.
name|getBundleReplacements
argument_list|()
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|addAll
argument_list|(
name|FeaturesProcessing
operator|.
name|parseOverridesClauses
argument_list|(
name|overrides
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Features
name|features
parameter_list|)
block|{
name|List
argument_list|<
name|Feature
argument_list|>
name|featureList
init|=
name|features
operator|.
name|getFeature
argument_list|()
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
name|featureList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Feature
name|f
init|=
name|featureList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// overriding features first, so we can further override their bundles
for|for
control|(
name|FeatureReplacements
operator|.
name|OverrideFeature
name|override
range|:
name|getInstructions
argument_list|()
operator|.
name|getFeatureReplacements
argument_list|()
operator|.
name|getReplacements
argument_list|()
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|override
operator|.
name|getFeature
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
switch|switch
condition|(
name|override
operator|.
name|getMode
argument_list|()
condition|)
block|{
case|case
name|REPLACE
case|:
name|featureList
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|override
operator|.
name|getFeature
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|MERGE
case|:
name|f
operator|.
name|getBundle
argument_list|()
operator|.
name|addAll
argument_list|(
name|override
operator|.
name|getFeature
argument_list|()
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REMOVE
case|:
comment|// TODO
break|break;
block|}
block|}
block|}
block|}
for|for
control|(
name|Feature
name|feature
range|:
name|features
operator|.
name|getFeature
argument_list|()
control|)
block|{
comment|// blacklisting features
name|boolean
name|allBlacklisted
init|=
name|features
operator|.
name|isBlacklisted
argument_list|()
decl_stmt|;
name|feature
operator|.
name|setBlacklisted
argument_list|(
name|allBlacklisted
operator|||
name|isFeatureBlacklisted
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
comment|// blacklisting feature's dependencies and conditionals
for|for
control|(
name|Conditional
name|conditional
range|:
name|feature
operator|.
name|getConditional
argument_list|()
control|)
block|{
name|boolean
name|isConditionBlacklisted
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|cond
range|:
name|conditional
operator|.
name|getCondition
argument_list|()
control|)
block|{
name|isConditionBlacklisted
operator||=
name|isFeatureBlacklisted
argument_list|(
operator|new
name|Feature
argument_list|(
name|cond
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|conditional
operator|.
name|setBlacklisted
argument_list|(
name|feature
operator|.
name|isBlacklisted
argument_list|()
operator|||
name|isConditionBlacklisted
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Dependency
name|dep
range|:
name|feature
operator|.
name|getFeature
argument_list|()
control|)
block|{
name|dep
operator|.
name|setBlacklisted
argument_list|(
name|feature
operator|.
name|isBlacklisted
argument_list|()
operator|||
name|isFeatureBlacklisted
argument_list|(
operator|new
name|Feature
argument_list|(
name|dep
operator|.
name|getName
argument_list|()
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// override dependency flag (null - don't touch, false - change to false, true - change to true)
name|Boolean
name|dependency
init|=
literal|null
decl_stmt|;
for|for
control|(
name|OverrideBundleDependency
operator|.
name|OverrideFeatureDependency
name|overrideFeatureDependency
range|:
name|getInstructions
argument_list|()
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getFeatures
argument_list|()
control|)
block|{
name|FeaturePattern
name|pattern
init|=
operator|new
name|FeaturePattern
argument_list|(
name|overrideFeatureDependency
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|overrideFeatureDependency
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|.
name|matches
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
condition|)
block|{
name|dependency
operator|=
name|overrideFeatureDependency
operator|.
name|isDependency
argument_list|()
expr_stmt|;
block|}
block|}
comment|// blacklisting bundles and processing bundles
name|processBundles
argument_list|(
name|feature
operator|.
name|getBundle
argument_list|()
argument_list|,
name|allBlacklisted
argument_list|,
name|dependency
argument_list|)
expr_stmt|;
for|for
control|(
name|Conditional
name|c
range|:
name|feature
operator|.
name|getConditional
argument_list|()
control|)
block|{
name|processBundles
argument_list|(
name|c
operator|.
name|getBundle
argument_list|()
argument_list|,
name|allBlacklisted
argument_list|,
name|dependency
argument_list|)
expr_stmt|;
block|}
comment|// TODO: think about overriding at repository level
comment|//            for (OverrideBundleDependency.OverrideDependency overrideDependency : getInstructions().getOverrideBundleDependency().getRepositories()) {
comment|//            }
block|}
block|}
specifier|private
name|void
name|processBundles
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|,
name|boolean
name|allBlacklisted
parameter_list|,
name|Boolean
name|forceDependency
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|boolean
name|bundleBlacklisted
init|=
name|allBlacklisted
operator|||
name|isBundleBlacklisted
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundleBlacklisted
condition|)
block|{
comment|// blacklisting has higher priority
name|bundle
operator|.
name|setBlacklisted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if not blacklisted, it may be overriden
name|staticOverrideBundle
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
comment|// and may have dependency flag altered
if|if
condition|(
name|forceDependency
operator|!=
literal|null
condition|)
block|{
comment|// set at feature level
name|bundle
operator|.
name|setDependency
argument_list|(
name|forceDependency
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// may have dependency overriden at bundle level
for|for
control|(
name|OverrideBundleDependency
operator|.
name|OverrideDependency
name|overrideBundleDependency
range|:
name|getInstructions
argument_list|()
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|LocationPattern
name|pattern
init|=
operator|new
name|LocationPattern
argument_list|(
name|overrideBundleDependency
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|.
name|matches
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|)
condition|)
block|{
name|bundle
operator|.
name|setDependency
argument_list|(
name|overrideBundleDependency
operator|.
name|isDependency
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
comment|/**      * Processes {@link Bundle bundle definition} and (according to override instructions) maybe sets different target      * location and {@link BundleInfo#isOverriden()} flag.      */
specifier|private
name|void
name|staticOverrideBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|bundle
operator|.
name|setOverriden
argument_list|(
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|NONE
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleReplacements
operator|.
name|OverrideBundle
name|override
range|:
name|this
operator|.
name|getInstructions
argument_list|()
operator|.
name|getBundleReplacements
argument_list|()
operator|.
name|getOverrideBundles
argument_list|()
control|)
block|{
name|String
name|originalLocation
init|=
name|bundle
operator|.
name|getLocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|override
operator|.
name|getOriginalUriPattern
argument_list|()
operator|.
name|matches
argument_list|(
name|originalLocation
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Overriding bundle location \""
operator|+
name|originalLocation
operator|+
literal|"\" with \""
operator|+
name|override
operator|.
name|getReplacement
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|setOriginalLocation
argument_list|(
name|originalLocation
argument_list|)
expr_stmt|;
if|if
condition|(
name|override
operator|.
name|getMode
argument_list|()
operator|==
name|BundleReplacements
operator|.
name|BundleOverrideMode
operator|.
name|MAVEN
condition|)
block|{
name|bundle
operator|.
name|setOverriden
argument_list|(
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|MAVEN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|.
name|setOverriden
argument_list|(
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|OSGI
argument_list|)
expr_stmt|;
block|}
name|bundle
operator|.
name|setLocation
argument_list|(
name|override
operator|.
name|getReplacement
argument_list|()
argument_list|)
expr_stmt|;
comment|// TOCHECK: last rule wins - no break!!!
comment|//break;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRepositoryBlacklisted
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
for|for
control|(
name|LocationPattern
name|lp
range|:
name|processing
operator|.
name|getBlacklistedRepositoryLocationPatterns
argument_list|()
control|)
block|{
if|if
condition|(
name|lp
operator|.
name|matches
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Matching name and version of given feature, checks whether this feature is blacklisted.      */
specifier|private
name|boolean
name|isFeatureBlacklisted
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
return|return
name|getInstructions
argument_list|()
operator|.
name|getBlacklist
argument_list|()
operator|.
name|isFeatureBlacklisted
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
return|;
block|}
comment|/**      * Matching location of the bundle, checks whether this bundle is blacklisted.      */
annotation|@
name|Override
specifier|public
name|boolean
name|isBundleBlacklisted
parameter_list|(
name|String
name|location
parameter_list|)
block|{
return|return
name|getInstructions
argument_list|()
operator|.
name|getBlacklist
argument_list|()
operator|.
name|isBundleBlacklisted
argument_list|(
name|location
argument_list|)
return|;
block|}
comment|/**      * Checks whether the configuration in this processor contains any instructions (for bundles, repositories,      * overrides, ...).      */
specifier|public
name|boolean
name|hasInstructions
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getBlacklistedRepositories
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getBlacklistedFeatures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getBlacklistedBundles
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getFeatures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getBundleReplacements
argument_list|()
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|count
operator|+=
name|getInstructions
argument_list|()
operator|.
name|getFeatureReplacements
argument_list|()
operator|.
name|getReplacements
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
return|return
name|count
operator|>
literal|0
return|;
block|}
block|}
end_class

end_unit

