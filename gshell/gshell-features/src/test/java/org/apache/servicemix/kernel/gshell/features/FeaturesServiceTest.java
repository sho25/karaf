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
package|;
end_package

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
name|io
operator|.
name|InputStream
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
name|*
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
name|*
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
operator|.
name|FeaturesServiceImpl
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
name|Bundle
import|;
end_import

begin_class
specifier|public
class|class
name|FeaturesServiceTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInstallFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo2.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|Preferences
name|prefs
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Preferences
operator|.
name|class
argument_list|)
decl_stmt|;
name|PreferencesService
name|preferencesService
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|PreferencesService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Preferences
name|repositoriesNode
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Preferences
operator|.
name|class
argument_list|)
decl_stmt|;
name|Preferences
name|featuresNode
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Preferences
operator|.
name|class
argument_list|)
decl_stmt|;
name|BundleContext
name|bundleContext
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bundle
name|installedBundle
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|preferencesService
operator|.
name|getUserPreferences
argument_list|(
literal|"FeaturesServiceState"
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|prefs
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"repositories"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|repositoriesNode
argument_list|)
expr_stmt|;
name|repositoriesNode
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repositoriesNode
operator|.
name|putInt
argument_list|(
literal|"count"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|repositoriesNode
operator|.
name|put
argument_list|(
literal|"item.0"
argument_list|,
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"features"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|featuresNode
argument_list|)
expr_stmt|;
name|featuresNode
operator|.
name|clear
argument_list|()
expr_stmt|;
name|prefs
operator|.
name|putBoolean
argument_list|(
literal|"bootFeaturesInstalled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|flush
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|preferencesService
argument_list|,
name|prefs
argument_list|,
name|repositoriesNode
argument_list|,
name|featuresNode
argument_list|,
name|bundleContext
argument_list|,
name|installedBundle
argument_list|)
expr_stmt|;
name|FeaturesServiceImpl
name|svc
init|=
operator|new
name|FeaturesServiceImpl
argument_list|()
decl_stmt|;
name|svc
operator|.
name|setPreferences
argument_list|(
name|preferencesService
argument_list|)
expr_stmt|;
name|svc
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|svc
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|Repository
index|[]
name|repositories
init|=
name|svc
operator|.
name|listRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repositories
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repositories
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Feature
index|[]
name|features
init|=
name|repositories
index|[
literal|0
index|]
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f1"
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|0
index|]
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|0
index|]
operator|.
name|getBundles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://repo1.maven.org/maven2/org/apache/geronimo/specs/geronimo-stax-api_1.0_spec/1.0.1/geronimo-stax-api_1.0_spec-1.0.1.jar"
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getBundles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|preferencesService
argument_list|,
name|prefs
argument_list|,
name|repositoriesNode
argument_list|,
name|featuresNode
argument_list|,
name|bundleContext
argument_list|,
name|installedBundle
argument_list|)
expr_stmt|;
name|reset
argument_list|(
name|preferencesService
argument_list|,
name|prefs
argument_list|,
name|repositoriesNode
argument_list|,
name|featuresNode
argument_list|,
name|bundleContext
argument_list|,
name|installedBundle
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|getBundles
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Bundle
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|isA
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|installedBundle
argument_list|)
expr_stmt|;
name|installedBundle
operator|.
name|start
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|installedBundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|12345L
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|preferencesService
operator|.
name|getUserPreferences
argument_list|(
literal|"FeaturesServiceState"
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|prefs
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"repositories"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|repositoriesNode
argument_list|)
expr_stmt|;
name|repositoriesNode
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repositoriesNode
operator|.
name|putInt
argument_list|(
literal|"count"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|repositoriesNode
operator|.
name|put
argument_list|(
literal|"item.0"
argument_list|,
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|prefs
operator|.
name|node
argument_list|(
literal|"features"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|featuresNode
argument_list|)
expr_stmt|;
name|featuresNode
operator|.
name|clear
argument_list|()
expr_stmt|;
name|featuresNode
operator|.
name|put
argument_list|(
literal|"f1"
argument_list|,
literal|"12345"
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|putBoolean
argument_list|(
literal|"bootFeaturesInstalled"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|flush
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|preferencesService
argument_list|,
name|prefs
argument_list|,
name|repositoriesNode
argument_list|,
name|featuresNode
argument_list|,
name|bundleContext
argument_list|,
name|installedBundle
argument_list|)
expr_stmt|;
name|svc
operator|.
name|installFeature
argument_list|(
literal|"f1"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

