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
name|region
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|RepositoryImpl
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
name|namespace
operator|.
name|IdentityNamespace
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
name|resource
operator|.
name|Capability
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Wire
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
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|util
operator|.
name|MapUtils
operator|.
name|addToMapSet
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

begin_class
specifier|public
class|class
name|SubsystemTest
block|{
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SubsystemTest
operator|.
name|class
argument_list|)
decl_stmt|;
empty_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|test1
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data1/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f2"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"b/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data1"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test2
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data2/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f3"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps2"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"b/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"e/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1#f1"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1#f1"
argument_list|,
literal|"d/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps2"
argument_list|,
literal|"b/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps2"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps2#f1"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data2"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverrides
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data3/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"a/1.0.1"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data3"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"b"
argument_list|)
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConditionalUnsatisfiedWithOptional
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data4/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data4"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConditionalSatisfiedWithOptional
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data4/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f2"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"b/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data4"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBundle
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data1/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"bundle:a"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"bundle:c;dependency=true"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data1"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFeatureOptional
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data5/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"b/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data5"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFeatureOptionalAlreadyProvided
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data5/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"f3"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data5"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFeatureOptionalAlreadyProvided2
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data6/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"pax-http"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"pax-http-tomcat"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data6"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResourceRepositories
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data7/features.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"f1"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"f2"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"a/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"c/1.0.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root/apps1"
argument_list|,
literal|"b/1.0.0"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data7"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleVersionsForFeatureDependency
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|repo1
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data9/pax-web-6.0.3.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|RepositoryImpl
name|repo2
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"data9/pax-web-6.0.4.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|allFeatures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|allFeatures
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo1
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|allFeatures
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repo2
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|features
argument_list|,
literal|"root"
argument_list|,
literal|"pax-war-tomcat"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"pax-url-war/2.5.0"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"pax-web-extender-war/6.0.4"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"pax-web-tomcat/6.0.4"
argument_list|)
expr_stmt|;
name|addToMapSet
argument_list|(
name|expected
argument_list|,
literal|"root"
argument_list|,
literal|"pax-web-api/6.0.4"
argument_list|)
expr_stmt|;
name|SubsystemResolver
name|resolver
init|=
operator|new
name|SubsystemResolver
argument_list|(
name|this
operator|.
name|resolver
argument_list|,
operator|new
name|TestDownloadManager
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"data9"
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|prepare
argument_list|(
name|allFeatures
argument_list|,
name|features
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|resolver
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verify
parameter_list|(
name|SubsystemResolver
name|resolver
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|expected
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|mapping
init|=
name|getBundleNamesPerRegions
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expected
operator|.
name|equals
argument_list|(
name|mapping
argument_list|)
condition|)
block|{
name|dumpBundles
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|dumpWiring
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Resolution failed"
argument_list|,
name|expected
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dumpBundles
parameter_list|(
name|SubsystemResolver
name|resolver
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Bundle mapping"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Resource
argument_list|>
argument_list|>
name|bundles
init|=
name|resolver
operator|.
name|getBundlesPerRegions
argument_list|()
decl_stmt|;
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
name|Resource
argument_list|>
argument_list|>
name|entry
range|:
name|bundles
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|b
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"        "
operator|+
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|getBundleNamesPerRegions
parameter_list|(
name|SubsystemResolver
name|resolver
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|mapping
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Resource
argument_list|>
argument_list|>
name|bundles
init|=
name|resolver
operator|.
name|getBundlesPerRegions
argument_list|()
decl_stmt|;
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
name|Resource
argument_list|>
argument_list|>
name|entry
range|:
name|bundles
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|Resource
name|r
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|addToMapSet
argument_list|(
name|mapping
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mapping
return|;
block|}
specifier|private
name|void
name|dumpWiring
parameter_list|(
name|SubsystemResolver
name|resolver
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Wiring"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Resource
argument_list|,
name|List
argument_list|<
name|Wire
argument_list|>
argument_list|>
name|wiring
init|=
name|resolver
operator|.
name|getWiring
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Resource
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|wiring
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|resources
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Resource
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Resource
name|o1
parameter_list|,
name|Resource
name|o2
parameter_list|)
block|{
return|return
name|getName
argument_list|(
name|o1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|getName
argument_list|(
name|o2
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|Resource
name|resource
range|:
name|resources
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    "
operator|+
name|getName
argument_list|(
name|resource
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Wire
name|wire
range|:
name|wiring
operator|.
name|get
argument_list|(
name|resource
argument_list|)
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"        "
operator|+
name|wire
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getName
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|Capability
name|cap
init|=
name|resource
operator|.
name|getCapabilities
argument_list|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IdentityNamespace
operator|.
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
operator|+
literal|": "
operator|+
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
operator|+
literal|"/"
operator|+
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

