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
name|FileWriter
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
name|Properties
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
name|JAXBContext
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
name|Marshaller
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
name|ObjectFactory
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
name|junit
operator|.
name|Test
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|FeaturesProcessorTest
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
name|FeaturesProcessorTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|jaxbModelForProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBContext
name|jaxb
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|ObjectFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|FeaturesProcessing
name|fp
init|=
operator|(
name|FeaturesProcessing
operator|)
name|jaxb
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/karaf/features/internal/service/org.apache.karaf.features.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|fp
operator|.
name|getFeatureReplacements
argument_list|()
operator|.
name|getReplacements
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFeature
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"pax-jsf-resources-support"
argument_list|)
argument_list|)
expr_stmt|;
name|Marshaller
name|marshaller
init|=
name|jaxb
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FORMATTED_OUTPUT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|fp
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|versionRanges
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
operator|new
name|VersionRange
argument_list|(
literal|"1"
argument_list|,
literal|false
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
operator|new
name|VersionRange
argument_list|(
literal|"[2,3)"
argument_list|,
literal|true
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|mavenURIs
parameter_list|()
throws|throws
name|Exception
block|{
name|Parser
name|p
init|=
operator|new
name|Parser
argument_list|(
literal|"group/artifact/[1,2)/xml/features*"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getVersion
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"[1,2)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"features*"
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|=
operator|new
name|Parser
argument_list|(
literal|"org.springframework*/*cloud*/*"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getVersion
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"*cloud*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getGroup
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"org.springframework*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getType
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|=
operator|new
name|Parser
argument_list|(
literal|"org.ops4j/org.ops4j*/*//uber"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getVersion
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"org.ops4j*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getGroup
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"org.ops4j"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getType
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"uber"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readingLegacyOverrides
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/overrides2.properties"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|FeaturesProcessing
name|instructions
init|=
name|processor
operator|.
name|getInstructions
argument_list|()
decl_stmt|;
name|BundleReplacements
name|bundleReplacements
init|=
name|instructions
operator|.
name|getBundleReplacements
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|BundleReplacements
operator|.
name|OverrideBundle
name|o1
init|=
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o1
operator|.
name|getOriginalUri
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.command/[2.3.0,2.3.0.61033X)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o1
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.command/2.3.0.61033X"
argument_list|)
argument_list|)
expr_stmt|;
name|BundleReplacements
operator|.
name|OverrideBundle
name|o2
init|=
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o2
operator|.
name|getOriginalUri
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.core/[2.2.0,2.4.0)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o2
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.core/2.3.0.61033X"
argument_list|)
argument_list|)
expr_stmt|;
name|BundleReplacements
operator|.
name|OverrideBundle
name|o3
init|=
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o3
operator|.
name|getOriginalUri
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.resources/[2.3.0,2.3.14)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o3
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.resources/2.3.14"
argument_list|)
argument_list|)
expr_stmt|;
name|BundleReplacements
operator|.
name|OverrideBundle
name|o4
init|=
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o4
operator|.
name|getOriginalUri
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.kernel/[2.0.0,2.0.0]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o4
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.kernel/2.3.14"
argument_list|)
argument_list|)
expr_stmt|;
name|BundleReplacements
operator|.
name|OverrideBundle
name|o5
init|=
name|bundleReplacements
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|o5
operator|.
name|getOriginalUri
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.infinity/[1.0.0,*)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|o5
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.apache.karaf.admin/org.apache.karaf.admin.infinity/2.3.14"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readingLegacyBlacklist
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/blacklisted2.properties"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|FeaturesProcessing
name|instructions
init|=
name|processor
operator|.
name|getInstructions
argument_list|()
decl_stmt|;
name|Blacklist
name|blacklist
init|=
name|instructions
operator|.
name|getBlacklist
argument_list|()
decl_stmt|;
name|Clause
index|[]
name|clauses
init|=
name|blacklist
operator|.
name|getClauses
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|clauses
operator|.
name|length
argument_list|,
name|equalTo
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|blacklist
operator|.
name|isFeatureBlacklisted
argument_list|(
literal|"spring"
argument_list|,
literal|"2.5.6.SEC02"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|blacklist
operator|.
name|isFeatureBlacklisted
argument_list|(
literal|"spring"
argument_list|,
literal|"2.5.7.SEC02"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|blacklist
operator|.
name|isFeatureBlacklisted
argument_list|(
literal|"jclouds"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|blacklist
operator|.
name|isBundleBlacklisted
argument_list|(
literal|"mvn:org.spring/spring-infinity/42"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|blacklist
operator|.
name|isBundleBlacklisted
argument_list|(
literal|"mvn:org.spring/spring-infinity/41"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|blacklist
operator|.
name|isBundleBlacklisted
argument_list|(
literal|"mvn:org.spring/spring-eternity/42"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|blacklist
operator|.
name|isBundleBlacklisted
argument_list|(
literal|"mvn:jclouds/jclouds/1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|blacklistingRepositories
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi01.xml"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fp01.xml"
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo
init|=
operator|(
name|RepositoryImpl
operator|)
operator|new
name|RepositoryCacheImpl
argument_list|(
name|processor
argument_list|)
operator|.
name|create
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getRepositories
argument_list|()
operator|.
name|length
argument_list|,
name|equalTo
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|processor
operator|.
name|isRepositoryBlacklisted
argument_list|(
name|repo
operator|.
name|getRepositories
argument_list|()
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|isRepositoryBlacklisted
argument_list|(
name|repo
operator|.
name|getRepositories
argument_list|()
index|[
literal|1
index|]
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|processor
operator|.
name|isRepositoryBlacklisted
argument_list|(
name|repo
operator|.
name|getRepositories
argument_list|()
index|[
literal|2
index|]
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|blacklistingFeatures
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi01.xml"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fp02.xml"
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo
init|=
operator|(
name|RepositoryImpl
operator|)
operator|new
name|RepositoryCacheImpl
argument_list|(
name|processor
argument_list|)
operator|.
name|create
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Feature
index|[]
name|features
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|features
index|[
literal|0
index|]
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|features
index|[
literal|1
index|]
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|features
index|[
literal|2
index|]
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|features
index|[
literal|3
index|]
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|features
index|[
literal|4
index|]
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|blacklistingBundles
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi01.xml"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fp03.xml"
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo
init|=
operator|(
name|RepositoryImpl
operator|)
operator|new
name|RepositoryCacheImpl
argument_list|(
name|processor
argument_list|)
operator|.
name|create
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Feature
name|f1
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertFalse
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isBlacklisted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|overridingBundles
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi02.xml"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fp03.xml"
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo
init|=
operator|(
name|RepositoryImpl
operator|)
operator|new
name|RepositoryCacheImpl
argument_list|(
name|processor
argument_list|)
operator|.
name|create
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Feature
name|f1
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isOverriden
argument_list|()
operator|==
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isOverriden
argument_list|()
operator|==
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|OSGI
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-io/commons-io/1.3.5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOriginalLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-io/commons-io/1.3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|isOverriden
argument_list|()
operator|==
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|MAVEN
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-codec/commons-codec/1.4.2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOriginalLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-codec/commons-codec/0.4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|isOverriden
argument_list|()
operator|==
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isOverriden
argument_list|()
operator|==
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|OSGI
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f1
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.glassfish/something-strangest/4.3.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f1
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOriginalLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.glassfish/something-strangest/4.3.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f1
operator|.
name|getConditional
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isOverriden
argument_list|()
operator|==
name|BundleInfo
operator|.
name|BundleOverrideMode
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|replaceFeatures
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi04.xml"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fp04.xml"
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo
init|=
operator|(
name|RepositoryImpl
operator|)
operator|new
name|RepositoryCacheImpl
argument_list|(
name|processor
argument_list|)
operator|.
name|create
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Feature
name|f11_0
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|Feature
name|f11_1
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
index|[
literal|1
index|]
decl_stmt|;
name|assertThat
argument_list|(
name|f11_0
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f11_0
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-io/commons-io/1.4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f11_1
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f11_1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-io/commons-io/1.3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|f11_1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-codec/commons-codec/0.5"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|overrideDependencyFlag
parameter_list|()
block|{
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi05.xml"
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fp04.xml"
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo
init|=
operator|(
name|RepositoryImpl
operator|)
operator|new
name|RepositoryCacheImpl
argument_list|(
name|processor
argument_list|)
operator|.
name|create
argument_list|(
name|uri
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Feature
name|f11_0
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|Feature
name|f11_1
init|=
name|repo
operator|.
name|getFeatures
argument_list|()
index|[
literal|1
index|]
decl_stmt|;
name|assertTrue
argument_list|(
name|f11_0
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isDependency
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f11_0
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isDependency
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f11_1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isDependency
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f11_1
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isDependency
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|resolvePlaceholders
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"version.jclouds"
argument_list|,
literal|"1.9"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"version.commons-io"
argument_list|,
literal|"2.5"
argument_list|)
expr_stmt|;
name|props
operator|.
name|store
argument_list|(
operator|new
name|FileWriter
argument_list|(
literal|"target/versions.properties"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|FeaturesProcessorImpl
name|processor
init|=
operator|new
name|FeaturesProcessorImpl
argument_list|(
operator|new
name|FeaturesServiceConfig
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"file:src/test/resources/org/apache/karaf/features/internal/service/fpi03.xml"
argument_list|,
literal|"file:target/versions.properties"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|processor
operator|.
name|getInstructions
argument_list|()
operator|.
name|getBlacklistedRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:org.jclouds/jclouds-features/1.9/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|processor
operator|.
name|getInstructions
argument_list|()
operator|.
name|getBundleReplacements
argument_list|()
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getReplacement
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"mvn:commons-io/commons-io/2.5"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|serializeWithComments
parameter_list|()
block|{
name|FeaturesProcessingSerializer
name|serializer
init|=
operator|new
name|FeaturesProcessingSerializer
argument_list|()
decl_stmt|;
name|FeaturesProcessing
name|featuresProcessing
init|=
operator|new
name|FeaturesProcessing
argument_list|()
decl_stmt|;
name|featuresProcessing
operator|.
name|getBlacklistedRepositories
argument_list|()
operator|.
name|add
argument_list|(
literal|"repository 1"
argument_list|)
expr_stmt|;
name|OverrideBundleDependency
operator|.
name|OverrideDependency
name|d1
init|=
operator|new
name|OverrideBundleDependency
operator|.
name|OverrideDependency
argument_list|()
decl_stmt|;
name|d1
operator|.
name|setDependency
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|d1
operator|.
name|setUri
argument_list|(
literal|"uri 1"
argument_list|)
expr_stmt|;
name|featuresProcessing
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getRepositories
argument_list|()
operator|.
name|add
argument_list|(
name|d1
argument_list|)
expr_stmt|;
name|OverrideBundleDependency
operator|.
name|OverrideFeatureDependency
name|d2
init|=
operator|new
name|OverrideBundleDependency
operator|.
name|OverrideFeatureDependency
argument_list|()
decl_stmt|;
name|d2
operator|.
name|setDependency
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|d2
operator|.
name|setName
argument_list|(
literal|"n"
argument_list|)
expr_stmt|;
name|d2
operator|.
name|setVersion
argument_list|(
literal|"1.2.3"
argument_list|)
expr_stmt|;
name|featuresProcessing
operator|.
name|getOverrideBundleDependency
argument_list|()
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|d2
argument_list|)
expr_stmt|;
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
name|setOriginalUri
argument_list|(
literal|"original"
argument_list|)
expr_stmt|;
name|override
operator|.
name|setReplacement
argument_list|(
literal|"replacement"
argument_list|)
expr_stmt|;
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
name|featuresProcessing
operator|.
name|getBundleReplacements
argument_list|()
operator|.
name|getOverrideBundles
argument_list|()
operator|.
name|add
argument_list|(
name|override
argument_list|)
expr_stmt|;
name|FeatureReplacements
operator|.
name|OverrideFeature
name|of
init|=
operator|new
name|FeatureReplacements
operator|.
name|OverrideFeature
argument_list|()
decl_stmt|;
name|of
operator|.
name|setMode
argument_list|(
name|FeatureReplacements
operator|.
name|FeatureOverrideMode
operator|.
name|REPLACE
argument_list|)
expr_stmt|;
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
name|f
init|=
operator|new
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
argument_list|()
decl_stmt|;
name|f
operator|.
name|setName
argument_list|(
literal|"f1"
argument_list|)
expr_stmt|;
name|Bundle
name|b
init|=
operator|new
name|Bundle
argument_list|()
decl_stmt|;
name|b
operator|.
name|setLocation
argument_list|(
literal|"location"
argument_list|)
expr_stmt|;
name|f
operator|.
name|getBundle
argument_list|()
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|of
operator|.
name|setFeature
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|featuresProcessing
operator|.
name|getFeatureReplacements
argument_list|()
operator|.
name|getReplacements
argument_list|()
operator|.
name|add
argument_list|(
name|of
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|write
argument_list|(
name|featuresProcessing
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
