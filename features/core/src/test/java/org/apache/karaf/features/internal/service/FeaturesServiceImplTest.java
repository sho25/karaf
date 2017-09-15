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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|*
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
name|Hashtable
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|resolver
operator|.
name|ResolverImpl
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
name|FeatureState
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
name|TestBase
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
name|resolver
operator|.
name|Slf4jResolverLog
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
name|BundleInstallSupport
operator|.
name|FrameworkInfo
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
name|support
operator|.
name|TestDownloadManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|Capture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|osgi
operator|.
name|service
operator|.
name|resolver
operator|.
name|Resolver
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
name|easymock
operator|.
name|EasyMock
operator|.
name|anyObject
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expectLastCall
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
name|assertEquals
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
name|assertNotNull
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
name|assertSame
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

begin_comment
comment|/**  * Test cases for {@link org.apache.karaf.features.internal.service.FeaturesServiceImpl}  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesServiceImplTest
extends|extends
name|TestBase
block|{
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FeaturesServiceImplTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|Resolver
name|resolver
init|=
operator|new
name|ResolverImpl
argument_list|(
operator|new
name|Slf4jResolverLog
argument_list|(
name|logger
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|dataFile
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|dataFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"features"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|URL
operator|.
name|setURLStreamHandlerFactory
argument_list|(
name|protocol
lambda|->
name|protocol
operator|.
name|equals
argument_list|(
literal|"custom"
argument_list|)
condition|?
operator|new
name|URLStreamHandler
argument_list|()
block|{
block|@Override             protected URLConnection openConnection(URL u
condition|)
throws|throws
name|IOException
block|{
condition|return getClass(
argument_list|)
operator|.
name|getResource
argument_list|(
name|u
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|openConnection
argument_list|()
expr_stmt|;
block|}
block|}
end_class

begin_expr_stmt
unit|:
literal|null
end_expr_stmt

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}          @
name|After
specifier|public
name|void
name|after
parameter_list|()
throws|throws
name|Exception
block|{
name|Field
name|field
init|=
name|URL
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"factory"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testListFeatureWithoutVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|Feature
name|transactionFeature
init|=
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|impl
init|=
name|featuresServiceWithFeatures
argument_list|(
name|transactionFeature
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|transactionFeature
argument_list|,
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
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
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testGetFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|Feature
name|transactionFeature
init|=
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|impl
init|=
name|featuresServiceWithFeatures
argument_list|(
name|transactionFeature
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|transactionFeature
argument_list|,
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
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
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureStripVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|Feature
name|transactionFeature
init|=
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|impl
init|=
name|featuresServiceWithFeatures
argument_list|(
name|transactionFeature
argument_list|)
decl_stmt|;
name|Feature
index|[]
name|features
init|=
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
literal|"  1.0.0  "
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
operator|.
name|length
argument_list|)
expr_stmt|;
name|Feature
name|feature
init|=
name|features
index|[
literal|0
index|]
decl_stmt|;
name|assertNotNull
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"transaction"
argument_list|,
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureNotAvailable
parameter_list|()
throws|throws
name|Exception
block|{
name|Feature
name|transactionFeature
init|=
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|FeaturesServiceImpl
name|impl
init|=
name|featuresServiceWithFeatures
argument_list|(
name|transactionFeature
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"activemq"
argument_list|,
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
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureHighestAvailable
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesServiceImpl
name|impl
init|=
name|featuresServiceWithFeatures
argument_list|(
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.0.0"
argument_list|)
argument_list|,
name|feature
argument_list|(
literal|"transaction"
argument_list|,
literal|"2.0.0"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0.0"
argument_list|,
name|impl
operator|.
name|getFeatures
argument_list|(
literal|"transaction"
argument_list|,
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
argument_list|)
index|[
literal|0
index|]
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testCyclicFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesServiceConfig
name|cfg
init|=
operator|new
name|FeaturesServiceConfig
argument_list|()
decl_stmt|;
name|BundleInstallSupport
name|installSupport
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|BundleInstallSupport
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|installSupport
argument_list|)
expr_stmt|;
specifier|final
name|FeaturesServiceImpl
name|impl
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|resolver
argument_list|,
name|installSupport
argument_list|,
literal|null
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
name|impl
operator|.
name|addRepository
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"custom:cycle/a-references-b.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|impl
operator|.
name|getFeatureCache
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testRemoveRepo1
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FeaturesService
name|featureService
init|=
name|createTestFeatureService
argument_list|()
decl_stmt|;
name|URI
name|repoA
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"custom:remove/a.xml"
argument_list|)
decl_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|Feature
name|a1Feature
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"a1"
argument_list|)
decl_stmt|;
name|installFeature
argument_list|(
name|featureService
argument_list|,
name|a1Feature
argument_list|)
expr_stmt|;
name|Feature
name|b1Feature
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"b1"
argument_list|)
decl_stmt|;
name|installFeature
argument_list|(
name|featureService
argument_list|,
name|b1Feature
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|removeRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|assertNotInstalled
argument_list|(
name|featureService
argument_list|,
name|a1Feature
argument_list|)
expr_stmt|;
name|assertNotInstalled
argument_list|(
name|featureService
argument_list|,
name|b1Feature
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testGetFeatureWithVersion
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FeaturesService
name|featureService
init|=
name|createTestFeatureService
argument_list|()
decl_stmt|;
name|URI
name|repoA
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"custom:f09.xml"
argument_list|)
decl_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|Feature
name|feature
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"test"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1.0.0"
argument_list|,
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testInstallAndUpdate
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FeaturesService
name|featureService
init|=
name|createTestFeatureService
argument_list|()
decl_stmt|;
name|URI
name|repoA
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"custom:f09.xml"
argument_list|)
decl_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|Feature
name|test100
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"test"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|installFeature
argument_list|(
name|featureService
argument_list|,
name|test100
argument_list|)
expr_stmt|;
name|assertInstalled
argument_list|(
name|featureService
argument_list|,
name|test100
argument_list|)
expr_stmt|;
name|Feature
name|test110
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"test"
argument_list|,
literal|"1.1.0"
argument_list|)
decl_stmt|;
name|featureService
operator|.
name|installFeature
argument_list|(
name|test110
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|Option
operator|.
name|Upgrade
argument_list|)
argument_list|)
expr_stmt|;
name|waitInstalled
argument_list|(
name|featureService
argument_list|,
name|test110
argument_list|)
expr_stmt|;
name|assertNotInstalled
argument_list|(
name|featureService
argument_list|,
name|test100
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testInstallAndStop
parameter_list|()
throws|throws
name|Exception
block|{
name|Capture
argument_list|<
name|Bundle
argument_list|>
name|stoppedBundle
init|=
name|Capture
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Bundle
name|bundle
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleStartLevel
name|bundleStartLevel
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleRevision
name|bundleRevision
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
name|FeaturesServiceConfig
name|cfg
init|=
operator|new
name|FeaturesServiceConfig
argument_list|()
decl_stmt|;
name|BundleInstallSupport
name|installSupport
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|BundleInstallSupport
operator|.
name|class
argument_list|)
decl_stmt|;
name|FrameworkInfo
name|dummyInfo
init|=
operator|new
name|FrameworkInfo
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|installSupport
operator|.
name|getInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|dummyInfo
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|installSupport
operator|.
name|installBundle
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"root"
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
literal|"a100"
argument_list|)
argument_list|,
name|anyObject
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|installSupport
operator|.
name|startBundle
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|expectLastCall
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1L
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"a"
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Version
argument_list|(
literal|"1.0.0"
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Hashtable
argument_list|<>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundleStartLevel
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundleRevision
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleRevision
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleRevision
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleRevision
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|installSupport
argument_list|,
name|bundle
argument_list|,
name|bundleStartLevel
argument_list|,
name|bundleRevision
argument_list|)
expr_stmt|;
name|FeaturesService
name|featureService
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|resolver
argument_list|,
name|installSupport
argument_list|,
literal|null
argument_list|,
name|cfg
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|DownloadManager
name|createDownloadManager
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|TestDownloadManager
argument_list|(
name|FeaturesServiceImplTest
operator|.
name|class
argument_list|,
literal|"data1"
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|URI
name|repoA
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"custom:data1/features.xml"
argument_list|)
decl_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|Feature
name|test100
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"f"
argument_list|,
literal|"1.0.0"
argument_list|)
decl_stmt|;
name|installFeature
argument_list|(
name|featureService
argument_list|,
name|test100
argument_list|)
expr_stmt|;
name|assertInstalled
argument_list|(
name|featureService
argument_list|,
name|test100
argument_list|)
expr_stmt|;
name|dummyInfo
operator|.
name|bundles
operator|.
name|put
argument_list|(
literal|1L
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|FeatureState
argument_list|>
argument_list|>
name|states
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|states
operator|.
name|computeIfAbsent
argument_list|(
literal|"root"
argument_list|,
name|k
lambda|->
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"f/1.0.0"
argument_list|,
name|FeatureState
operator|.
name|Resolved
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reset
argument_list|(
name|installSupport
argument_list|,
name|bundle
argument_list|,
name|bundleRevision
argument_list|,
name|bundleStartLevel
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|installSupport
operator|.
name|getInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|dummyInfo
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|installSupport
operator|.
name|stopBundle
argument_list|(
name|EasyMock
operator|.
name|capture
argument_list|(
name|stoppedBundle
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|anyInt
argument_list|()
argument_list|)
expr_stmt|;
name|expectLastCall
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|1L
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"a"
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Version
argument_list|(
literal|"1.0.0"
argument_list|)
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Hashtable
argument_list|<>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundleStartLevel
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundleRevision
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleRevision
operator|.
name|getBundle
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bundle
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleRevision
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|bundleRevision
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|installSupport
argument_list|,
name|bundle
argument_list|,
name|bundleRevision
argument_list|,
name|bundleStartLevel
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|updateFeaturesState
argument_list|(
name|states
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|Option
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bundle
argument_list|,
name|stoppedBundle
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testRemoveRepo2
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FeaturesService
name|featureService
init|=
name|createTestFeatureService
argument_list|()
decl_stmt|;
name|URI
name|repoA
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"custom:remove/a.xml"
argument_list|)
decl_stmt|;
name|URI
name|repoB
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"custom:remove/b.xml"
argument_list|)
decl_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|addRepository
argument_list|(
name|repoB
argument_list|)
expr_stmt|;
name|Feature
name|a1Feature
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"a1"
argument_list|)
decl_stmt|;
name|installFeature
argument_list|(
name|featureService
argument_list|,
name|a1Feature
argument_list|)
expr_stmt|;
name|Feature
name|b1Feature
init|=
name|featureService
operator|.
name|getFeature
argument_list|(
literal|"b1"
argument_list|)
decl_stmt|;
name|installFeature
argument_list|(
name|featureService
argument_list|,
name|b1Feature
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|removeRepository
argument_list|(
name|repoA
argument_list|)
expr_stmt|;
name|assertNotInstalled
argument_list|(
name|featureService
argument_list|,
name|a1Feature
argument_list|)
expr_stmt|;
name|assertInstalled
argument_list|(
name|featureService
argument_list|,
name|b1Feature
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|FeaturesServiceImpl
name|featuresServiceWithFeatures
parameter_list|(
name|Feature
modifier|...
name|staticFeatures
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|features
init|=
name|features
argument_list|(
name|staticFeatures
argument_list|)
decl_stmt|;
name|FeaturesServiceConfig
name|cfg
init|=
operator|new
name|FeaturesServiceConfig
argument_list|()
decl_stmt|;
name|BundleInstallSupport
name|installSupport
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|BundleInstallSupport
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|installSupport
argument_list|)
expr_stmt|;
specifier|final
name|FeaturesServiceImpl
name|impl
init|=
operator|new
name|FeaturesServiceImpl
argument_list|(
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|resolver
argument_list|,
name|installSupport
argument_list|,
literal|null
argument_list|,
name|cfg
argument_list|)
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Feature
argument_list|>
argument_list|>
name|getFeatureCache
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|features
return|;
block|}
block|}
decl_stmt|;
return|return
name|impl
return|;
block|}
end_function

begin_function
specifier|private
name|FeaturesServiceImpl
name|createTestFeatureService
parameter_list|()
block|{
name|FeaturesServiceConfig
name|cfg
init|=
operator|new
name|FeaturesServiceConfig
argument_list|()
decl_stmt|;
name|BundleInstallSupport
name|installSupport
init|=
name|EasyMock
operator|.
name|niceMock
argument_list|(
name|BundleInstallSupport
operator|.
name|class
argument_list|)
decl_stmt|;
name|FrameworkInfo
name|dummyInfo
init|=
operator|new
name|FrameworkInfo
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|installSupport
operator|.
name|getInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|dummyInfo
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|installSupport
argument_list|)
expr_stmt|;
return|return
operator|new
name|FeaturesServiceImpl
argument_list|(
operator|new
name|Storage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|resolver
argument_list|,
name|installSupport
argument_list|,
literal|null
argument_list|,
name|cfg
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
name|void
name|assertNotInstalled
parameter_list|(
name|FeaturesService
name|featureService
parameter_list|,
name|Feature
name|feature
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"Feature "
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|" should not be installed anymore after removal of repo"
argument_list|,
name|featureService
operator|.
name|isInstalled
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|void
name|assertInstalled
parameter_list|(
name|FeaturesService
name|featureService
parameter_list|,
name|Feature
name|feature
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Feature "
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|" should still be installed after removal of repo"
argument_list|,
name|featureService
operator|.
name|isInstalled
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|void
name|installFeature
parameter_list|(
specifier|final
name|FeaturesService
name|featureService
parameter_list|,
name|Feature
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
name|featureService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|Option
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|waitInstalled
argument_list|(
name|featureService
argument_list|,
name|feature
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|void
name|waitInstalled
parameter_list|(
specifier|final
name|FeaturesService
name|featureService
parameter_list|,
name|Feature
name|feature
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|int
name|count
init|=
literal|40
decl_stmt|;
while|while
condition|(
operator|!
name|featureService
operator|.
name|isInstalled
argument_list|(
name|feature
argument_list|)
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|count
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Feature "
operator|+
name|feature
operator|+
literal|" not installed."
argument_list|)
throw|;
block|}
block|}
end_function

begin_comment
comment|/**      * This test ensures that every feature get installed only once, even if it appears multiple times in the list      * of transitive feature dependencies (KARAF-1600)      */
end_comment

begin_comment
comment|/*     @Test     @SuppressWarnings("unchecked")     public void testNoDuplicateFeaturesInstallation() throws Exception {         final List<Feature> installed = new LinkedList<Feature>();         BundleManager bundleManager = EasyMock.createMock(BundleManager.class);         expect(bundleManager.installBundleIfNeeded(EasyMock.anyObject(String.class), EasyMock.anyInt(), EasyMock.anyObject(String.class)))             .andReturn(new BundleInstallerResult(createDummyBundle(1l, "", headers()), true)).anyTimes();         bundleManager.refreshBundles(EasyMock.anyObject(Set.class), EasyMock.anyObject(Set.class), EasyMock.anyObject(EnumSet.class));         EasyMock.expectLastCall();         final FeaturesServiceImpl impl = new FeaturesServiceImpl(bundleManager, null) {             // override methods which refers to bundle context to avoid mocking everything             @Override             protected boolean loadState() {                 return true;             }              @Override             protected void saveDigraph() {              }              @Override             protected void doInstallFeature(InstallationState state, Feature feature, boolean verbose) throws Exception {                 installed.add(feature);                  super.doInstallFeature(state, feature, verbose);             }          };         replay(bundleManager);         impl.addRepository(getClass().getResource("repo2.xml").toURI());         impl.installFeature("all");          // copying the features to a set to filter out the duplicates         Set<Feature> noduplicates = new HashSet<Feature>();         noduplicates.addAll(installed);          assertEquals("Every feature should only have been installed once", installed.size(), noduplicates.size());     }      @Test     public void testGetOptionalImportsOnly() {         BundleManager bundleManager = new BundleManager(null, 0l);          List<Clause> result = bundleManager.getOptionalImports("org.apache.karaf,org.apache.karaf.optional;resolution:=optional");         assertEquals("One optional import expected", 1, result.size());         assertEquals("org.apache.karaf.optional", result.get(0).getName());          result = bundleManager.getOptionalImports(null);         assertNotNull(result);         assertEquals("No optional imports expected", 0, result.size());     }     */
end_comment

begin_class
specifier|static
class|class
name|Storage
extends|extends
name|StateStorage
block|{
annotation|@
name|Override
specifier|protected
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

unit|}
end_unit

