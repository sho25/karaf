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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|FeatureResource
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
name|osgi
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|r
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo1.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check repo
name|URI
index|[]
name|repos
init|=
name|r
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repos
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"urn:r1"
argument_list|)
argument_list|,
name|repos
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// Check features
name|Feature
index|[]
name|features
init|=
name|r
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
literal|3
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
name|getConfigurations
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
name|getConfigurations
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
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|"c1"
argument_list|)
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
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|"c1"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"v"
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|"c1"
argument_list|)
operator|.
name|get
argument_list|(
literal|"k"
argument_list|)
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
literal|2
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
literal|"b1"
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
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b2"
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
literal|1
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f2"
argument_list|,
name|features
index|[
literal|1
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
literal|1
index|]
operator|.
name|getConfigurations
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|features
index|[
literal|1
index|]
operator|.
name|getConfigurations
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
literal|1
index|]
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
index|[
literal|1
index|]
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f1"
operator|+
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
name|SPLIT_FOR_NAME_AND_VERSION
operator|+
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
argument_list|,
name|features
index|[
literal|1
index|]
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|1
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
literal|1
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
literal|"b3"
argument_list|,
name|features
index|[
literal|1
index|]
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
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f3"
argument_list|,
name|features
index|[
literal|2
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
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cf1"
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFinalname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isOverride
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cfloc"
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLoadFormattedRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|r
init|=
operator|new
name|RepositoryImpl
argument_list|(
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
argument_list|)
decl_stmt|;
comment|// Check repo
name|URI
index|[]
name|repos
init|=
name|r
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repos
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"urn:r1"
argument_list|)
argument_list|,
name|repos
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// Check features
name|Feature
index|[]
name|features
init|=
name|r
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
literal|3
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
name|getConfigurations
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
name|getConfigurations
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
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|"c1"
argument_list|)
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
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|"c1"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"v"
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|"c1"
argument_list|)
operator|.
name|get
argument_list|(
literal|"k"
argument_list|)
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
literal|2
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
literal|"b1"
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
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b2"
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
literal|1
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f2"
argument_list|,
name|features
index|[
literal|1
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
literal|1
index|]
operator|.
name|getConfigurations
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|features
index|[
literal|1
index|]
operator|.
name|getConfigurations
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
literal|1
index|]
operator|.
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
index|[
literal|1
index|]
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f1"
operator|+
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
name|SPLIT_FOR_NAME_AND_VERSION
operator|+
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
argument_list|,
name|features
index|[
literal|1
index|]
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|features
index|[
literal|1
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
literal|1
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
literal|"b3"
argument_list|,
name|features
index|[
literal|1
index|]
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
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"f3"
argument_list|,
name|features
index|[
literal|2
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
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cf1"
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFinalname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isOverride
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cfloc"
argument_list|,
name|features
index|[
literal|2
index|]
operator|.
name|getConfigurationFiles
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLoadRepoWithCapabilitiesAndRequirement
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryImpl
name|r
init|=
operator|new
name|RepositoryImpl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"repo3.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check features
name|Feature
index|[]
name|features
init|=
name|r
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getCapabilities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cap"
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getCapabilities
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|trim
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
name|getRequirements
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"req"
argument_list|,
name|features
index|[
literal|0
index|]
operator|.
name|getRequirements
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|Resource
name|res
init|=
name|FeatureResource
operator|.
name|build
argument_list|(
name|features
index|[
literal|0
index|]
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|res
operator|.
name|getCapabilities
argument_list|(
literal|"cap"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|res
operator|.
name|getRequirements
argument_list|(
literal|"req"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testShowWrongUriInException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
literal|"src/test/resources/org/apache/karaf/shell/features/repo1.xml"
decl_stmt|;
name|RepositoryImpl
name|r
init|=
operator|new
name|RepositoryImpl
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|r
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

