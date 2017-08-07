begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|util
operator|.
name|HashMap
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
name|tooling
operator|.
name|utils
operator|.
name|MojoSupport
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
name|artifact
operator|.
name|factory
operator|.
name|DefaultArtifactFactory
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
name|handler
operator|.
name|manager
operator|.
name|ArtifactHandlerManager
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
name|handler
operator|.
name|manager
operator|.
name|DefaultArtifactHandlerManager
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
name|junit
operator|.
name|Assert
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
name|BundleToArtifactTest
extends|extends
name|MojoSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|BundleToArtifactTest
parameter_list|()
throws|throws
name|NoSuchFieldException
throws|,
name|IllegalAccessException
block|{
name|factory
operator|=
operator|new
name|DefaultArtifactFactory
argument_list|()
expr_stmt|;
name|ArtifactHandlerManager
name|artifactHandlerManager
init|=
operator|new
name|DefaultArtifactHandlerManager
argument_list|()
decl_stmt|;
name|Field
name|f
init|=
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"artifactHandlerManager"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|factory
argument_list|,
name|artifactHandlerManager
argument_list|)
expr_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|f
operator|=
name|artifactHandlerManager
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"artifactHandlers"
argument_list|)
expr_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|artifactHandlerManager
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|testSimpleURL
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
literal|"mvn:org.foo/bar/1.0/kar"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
assert|assert
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.foo"
argument_list|)
assert|;
assert|assert
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"bar"
argument_list|)
assert|;
assert|assert
name|artifact
operator|.
name|getBaseVersion
argument_list|()
operator|.
name|equals
argument_list|(
literal|"1.0"
argument_list|)
assert|;
assert|assert
name|artifact
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"kar"
argument_list|)
assert|;
assert|assert
name|artifact
operator|.
name|getRepository
argument_list|()
operator|==
literal|null
assert|;
assert|assert
name|artifact
operator|.
name|getClassifier
argument_list|()
operator|==
literal|null
assert|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testURLWithClassifier
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
literal|"mvn:org.foo/bar/1.0/kar/type"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org.foo"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"kar"
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|artifact
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"type"
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoteRepoURL
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
literal|"mvn:http://baz.com!org.foo/bar/1.0/kar"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org.foo"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"kar"
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://baz.com"
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoteRepoURLWithId
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|resourceToArtifact
argument_list|(
literal|"mvn:http://baz.com@id=baz!org.foo/bar/1.0/kar"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org.foo"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"kar"
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"http://baz.com"
argument_list|,
name|artifact
operator|.
name|getRepository
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

