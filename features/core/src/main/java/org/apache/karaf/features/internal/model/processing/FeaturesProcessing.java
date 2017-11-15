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
name|model
operator|.
name|processing
package|;
end_package

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
name|net
operator|.
name|URI
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementWrapper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlValue
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
name|manifest
operator|.
name|Clause
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
name|manifest
operator|.
name|Parser
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
name|version
operator|.
name|VersionCleaner
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
name|version
operator|.
name|VersionRange
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
name|Blacklist
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
name|Overrides
operator|.
name|OVERRIDE_RANGE
import|;
end_import

begin_comment
comment|/**  * A set of instructions to process {@link org.apache.karaf.features.internal.model.Features} model. The actual  * use of these instructions is moved to {@link org.apache.karaf.features.internal.service.FeaturesProcessorImpl}  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"featuresProcessing"
argument_list|,
name|namespace
operator|=
name|FeaturesProcessing
operator|.
name|FEATURES_PROCESSING_NS
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"featuresProcessing"
argument_list|,
name|propOrder
operator|=
block|{
literal|"blacklistedRepositories"
block|,
literal|"blacklistedFeatures"
block|,
literal|"blacklistedBundles"
block|,
literal|"overrideBundleDependency"
block|,
literal|"bundleReplacements"
block|,
literal|"featureReplacements"
block|}
argument_list|)
specifier|public
class|class
name|FeaturesProcessing
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
name|FeaturesProcessing
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FEATURES_PROCESSING_NS
init|=
literal|"http://karaf.apache.org/xmlns/features-processing/v1.0.0"
decl_stmt|;
annotation|@
name|XmlElementWrapper
argument_list|(
name|name
operator|=
literal|"blacklistedRepositories"
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"repository"
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blacklistedRepositories
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
specifier|private
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|blacklistedRepositoryLocationPatterns
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementWrapper
argument_list|(
name|name
operator|=
literal|"blacklistedFeatures"
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"feature"
argument_list|)
specifier|private
name|List
argument_list|<
name|BlacklistedFeature
argument_list|>
name|blacklistedFeatures
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementWrapper
argument_list|(
name|name
operator|=
literal|"blacklistedBundles"
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"bundle"
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blacklistedBundles
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
specifier|private
name|OverrideBundleDependency
name|overrideBundleDependency
decl_stmt|;
annotation|@
name|XmlElement
specifier|private
name|BundleReplacements
name|bundleReplacements
decl_stmt|;
annotation|@
name|XmlElement
specifier|private
name|FeatureReplacements
name|featureReplacements
decl_stmt|;
annotation|@
name|XmlTransient
specifier|private
name|Blacklist
name|blacklist
decl_stmt|;
specifier|public
name|FeaturesProcessing
parameter_list|()
block|{
name|overrideBundleDependency
operator|=
operator|new
name|OverrideBundleDependency
argument_list|()
expr_stmt|;
name|bundleReplacements
operator|=
operator|new
name|BundleReplacements
argument_list|()
expr_stmt|;
name|featureReplacements
operator|=
operator|new
name|FeatureReplacements
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBlacklistedRepositories
parameter_list|()
block|{
return|return
name|blacklistedRepositories
return|;
block|}
specifier|public
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|getBlacklistedRepositoryLocationPatterns
parameter_list|()
block|{
return|return
name|blacklistedRepositoryLocationPatterns
return|;
block|}
specifier|public
name|List
argument_list|<
name|BlacklistedFeature
argument_list|>
name|getBlacklistedFeatures
parameter_list|()
block|{
return|return
name|blacklistedFeatures
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBlacklistedBundles
parameter_list|()
block|{
return|return
name|blacklistedBundles
return|;
block|}
specifier|public
name|OverrideBundleDependency
name|getOverrideBundleDependency
parameter_list|()
block|{
return|return
name|overrideBundleDependency
return|;
block|}
specifier|public
name|void
name|setOverrideBundleDependency
parameter_list|(
name|OverrideBundleDependency
name|overrideBundleDependency
parameter_list|)
block|{
name|this
operator|.
name|overrideBundleDependency
operator|=
name|overrideBundleDependency
expr_stmt|;
block|}
specifier|public
name|BundleReplacements
name|getBundleReplacements
parameter_list|()
block|{
return|return
name|bundleReplacements
return|;
block|}
specifier|public
name|void
name|setBundleReplacements
parameter_list|(
name|BundleReplacements
name|bundleReplacements
parameter_list|)
block|{
name|this
operator|.
name|bundleReplacements
operator|=
name|bundleReplacements
expr_stmt|;
block|}
specifier|public
name|FeatureReplacements
name|getFeatureReplacements
parameter_list|()
block|{
return|return
name|featureReplacements
return|;
block|}
specifier|public
name|void
name|setFeatureReplacements
parameter_list|(
name|FeatureReplacements
name|featureReplacements
parameter_list|)
block|{
name|this
operator|.
name|featureReplacements
operator|=
name|featureReplacements
expr_stmt|;
block|}
specifier|public
name|Blacklist
name|getBlacklist
parameter_list|()
block|{
return|return
name|blacklist
return|;
block|}
comment|/**      * Perform<em>compilation</em> of rules declared in feature processing XML file.      * @param blacklist additional {@link Blacklist} definition with lower priority      * @param overrides additional overrides definition with lower priority      */
specifier|public
name|void
name|postUnmarshall
parameter_list|(
name|Blacklist
name|blacklist
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|overrides
parameter_list|)
block|{
comment|// compile blacklisted repository URIs
for|for
control|(
name|String
name|repositoryURI
range|:
name|this
operator|.
name|getBlacklistedRepositories
argument_list|()
control|)
block|{
try|try
block|{
name|blacklistedRepositoryLocationPatterns
operator|.
name|add
argument_list|(
operator|new
name|LocationPattern
argument_list|(
name|repositoryURI
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't parse blacklisted repository location pattern: "
operator|+
name|repositoryURI
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// verify bundle override definitions
for|for
control|(
name|Iterator
argument_list|<
name|BundleReplacements
operator|.
name|OverrideBundle
argument_list|>
name|iterator
init|=
name|this
operator|.
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
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
name|BundleReplacements
operator|.
name|OverrideBundle
name|overrideBundle
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|overrideBundle
operator|.
name|getOriginalUri
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// we have to derive it from replacement - as with etc/overrides.properties entry
if|if
condition|(
name|overrideBundle
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't override bundle in maven mode without explicit original URL. Switching to osgi mode."
argument_list|)
expr_stmt|;
name|overrideBundle
operator|.
name|setMode
argument_list|(
name|BundleReplacements
operator|.
name|BundleOverrideMode
operator|.
name|OSGI
argument_list|)
expr_stmt|;
block|}
name|String
name|originalUri
init|=
name|calculateOverridenURI
argument_list|(
name|overrideBundle
operator|.
name|getReplacement
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|originalUri
operator|!=
literal|null
condition|)
block|{
name|overrideBundle
operator|.
name|setOriginalUri
argument_list|(
name|originalUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
block|}
try|try
block|{
name|overrideBundle
operator|.
name|compile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't parse override URL location pattern: "
operator|+
name|overrideBundle
operator|.
name|getOriginalUri
argument_list|()
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|// etc/blacklisted.properties
comment|// blacklisted bundle from XML to instruction for Blacklist class
name|List
argument_list|<
name|String
argument_list|>
name|blacklisted
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|bl
range|:
name|this
operator|.
name|getBlacklistedBundles
argument_list|()
control|)
block|{
name|blacklisted
operator|.
name|add
argument_list|(
name|bl
operator|+
literal|";type=bundle"
argument_list|)
expr_stmt|;
block|}
comment|// blacklisted features - XML type to String instruction for Blacklist class
name|blacklisted
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|getBlacklistedFeatures
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|bf
lambda|->
name|bf
operator|.
name|getName
argument_list|()
operator|+
literal|";type=feature"
operator|+
operator|(
name|bf
operator|.
name|getVersion
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
literal|";range=\""
operator|+
name|bf
operator|.
name|getVersion
argument_list|()
operator|+
literal|"\""
operator|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|blacklist
operator|=
operator|new
name|Blacklist
argument_list|(
name|blacklisted
argument_list|)
expr_stmt|;
name|this
operator|.
name|blacklist
operator|.
name|merge
argument_list|(
name|blacklist
argument_list|)
expr_stmt|;
comment|// etc/overrides.properties (mvn: URIs)
for|for
control|(
name|Clause
name|clause
range|:
name|Parser
operator|.
name|parseClauses
argument_list|(
name|overrides
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|overrides
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
control|)
block|{
comment|// name of the clause will become a bundle replacement
name|String
name|mvnURI
init|=
name|clause
operator|.
name|getName
argument_list|()
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|mvnURI
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"mvn"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Override URI \""
operator|+
name|mvnURI
operator|+
literal|"\" should use mvn: scheme. Ignoring."
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|BundleReplacements
operator|.
name|OverrideBundle
name|override
init|=
operator|new
name|BundleReplacements
operator|.
name|OverrideBundle
argument_list|()
decl_stmt|;
name|override
operator|.
name|setMode
argument_list|(
name|BundleReplacements
operator|.
name|BundleOverrideMode
operator|.
name|OSGI
argument_list|)
expr_stmt|;
name|override
operator|.
name|setReplacement
argument_list|(
name|mvnURI
argument_list|)
expr_stmt|;
name|String
name|originalUri
init|=
name|calculateOverridenURI
argument_list|(
name|mvnURI
argument_list|,
name|clause
operator|.
name|getAttribute
argument_list|(
name|OVERRIDE_RANGE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|originalUri
operator|!=
literal|null
condition|)
block|{
name|override
operator|.
name|setOriginalUri
argument_list|(
name|originalUri
argument_list|)
expr_stmt|;
try|try
block|{
name|override
operator|.
name|compile
argument_list|()
expr_stmt|;
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|add
argument_list|(
name|override
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't parse override URL location pattern: "
operator|+
name|originalUri
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * For<code>etc/overrides.properties</code>, we know what is the target URI for bundles we should use. We need      * a pattern of original bundle URIs that are candidates for replacement      * @param replacement      * @param range      * @return      */
specifier|private
name|String
name|calculateOverridenURI
parameter_list|(
name|String
name|replacement
parameter_list|,
name|String
name|range
parameter_list|)
block|{
try|try
block|{
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
name|parser
init|=
operator|new
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
argument_list|(
name|replacement
argument_list|)
decl_stmt|;
if|if
condition|(
name|parser
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|parser
operator|.
name|getVersion
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|||
name|parser
operator|.
name|getVersion
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
operator|)
condition|)
block|{
comment|// replacement URI should not contain ranges
throw|throw
operator|new
name|MalformedURLException
argument_list|(
literal|"Override URI should use single version."
argument_list|)
throw|;
block|}
if|if
condition|(
name|range
operator|!=
literal|null
condition|)
block|{
comment|// explicit range determines originalUri
name|VersionRange
name|vr
init|=
operator|new
name|VersionRange
argument_list|(
name|range
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|vr
operator|.
name|isOpenCeiling
argument_list|()
operator|&&
name|vr
operator|.
name|getCeiling
argument_list|()
operator|==
name|VersionRange
operator|.
name|INFINITE_VERSION
condition|)
block|{
comment|// toString() will give only floor version
name|parser
operator|.
name|setVersion
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s%s,*)"
argument_list|,
name|vr
operator|.
name|isOpenFloor
argument_list|()
condition|?
literal|"("
else|:
literal|"["
argument_list|,
name|vr
operator|.
name|getFloor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parser
operator|.
name|setVersion
argument_list|(
name|vr
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// no range: originalUri based on replacemenet URI with range deducted using default rules
comment|// assume version in override URI is NOT a range
name|Version
name|v
decl_stmt|;
try|try
block|{
name|v
operator|=
operator|new
name|Version
argument_list|(
name|VersionCleaner
operator|.
name|clean
argument_list|(
name|parser
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem parsing override URI \""
operator|+
name|replacement
operator|+
literal|"\": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Version ranges are not handled. Ignoring."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|Version
name|vfloor
init|=
operator|new
name|Version
argument_list|(
name|v
operator|.
name|getMajor
argument_list|()
argument_list|,
name|v
operator|.
name|getMinor
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setVersion
argument_list|(
operator|new
name|VersionRange
argument_list|(
literal|false
argument_list|,
name|vfloor
argument_list|,
name|v
argument_list|,
literal|true
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|parser
operator|.
name|toMvnURI
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Problem parsing override URI \""
operator|+
name|replacement
operator|+
literal|"\": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Ignoring."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"blacklistedFeature"
argument_list|)
specifier|public
specifier|static
class|class
name|BlacklistedFeature
block|{
annotation|@
name|XmlValue
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|XmlAttribute
specifier|private
name|String
name|version
decl_stmt|;
specifier|public
name|BlacklistedFeature
parameter_list|()
block|{         }
specifier|public
name|BlacklistedFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

