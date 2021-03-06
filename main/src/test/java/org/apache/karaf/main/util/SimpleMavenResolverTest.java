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
name|main
operator|.
name|util
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
name|Collections
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
specifier|public
class|class
name|SimpleMavenResolverTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_COORDS
init|=
literal|"mvn:org.apache.karaf.features/framework/1.0.0/xml/features"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|mavenToPath
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|String
name|resolvedPath
init|=
name|Parser
operator|.
name|pathFromMaven
argument_list|(
name|ARTIFACT_COORDS
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org/apache/karaf/features/framework/1.0.0/framework-1.0.0-features.xml"
argument_list|,
name|resolvedPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolve
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|File
name|basedir
init|=
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|File
name|home
init|=
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
literal|"test-karaf-home"
argument_list|)
decl_stmt|;
name|File
name|system
init|=
operator|new
name|File
argument_list|(
name|home
argument_list|,
literal|"system"
argument_list|)
decl_stmt|;
name|SimpleMavenResolver
name|resolver
init|=
operator|new
name|SimpleMavenResolver
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|system
argument_list|)
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|resolve
argument_list|(
operator|new
name|URI
argument_list|(
name|ARTIFACT_COORDS
argument_list|)
argument_list|)
expr_stmt|;
comment|// Will throw exception if the artifact can not be resolved
block|}
block|}
end_class

end_unit

