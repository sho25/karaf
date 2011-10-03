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
name|junit
operator|.
name|framework
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
name|main
operator|.
name|util
operator|.
name|Utils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
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
name|Constants
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
name|launch
operator|.
name|Framework
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|withBnd
import|;
end_import

begin_class
specifier|public
class|class
name|MainStartTest
block|{
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Unit test for KARAF-334 disabled for now"
argument_list|)
specifier|public
name|void
name|testAutoStart
parameter_list|()
throws|throws
name|Exception
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
name|data
init|=
operator|new
name|File
argument_list|(
name|home
argument_list|,
literal|"data"
argument_list|)
decl_stmt|;
name|Utils
operator|.
name|deleteDirectory
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
name|String
name|fileMVNbundle
init|=
operator|new
name|File
argument_list|(
name|home
argument_list|,
literal|"bundles/pax-url-mvn.jar"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|String
name|mvnUrl
init|=
literal|"mvn:org.bundles/org.bundles.compendium/4.2.0"
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.home"
argument_list|,
name|home
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.data"
argument_list|,
name|data
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.auto.start.1"
argument_list|,
literal|"\""
operator|+
name|fileMVNbundle
operator|+
literal|"|unused\""
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.auto.start.2"
argument_list|,
literal|"\""
operator|+
name|mvnUrl
operator|+
literal|"|unused\""
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.maven.convert"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|Main
name|main
init|=
operator|new
name|Main
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|LifecycleManager
name|lifecacleManager
init|=
name|main
operator|.
name|launch
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|Framework
name|framework
init|=
name|main
operator|.
name|getFramework
argument_list|()
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
name|framework
operator|.
name|getBundleContext
argument_list|()
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|bundles
operator|.
name|length
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fileMVNbundle
argument_list|,
name|bundles
index|[
literal|1
index|]
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|mvnUrl
argument_list|,
name|bundles
index|[
literal|2
index|]
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Bundle
operator|.
name|ACTIVE
argument_list|,
name|bundles
index|[
literal|1
index|]
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Bundle
operator|.
name|ACTIVE
argument_list|,
name|bundles
index|[
literal|2
index|]
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|lifecacleManager
operator|.
name|destroyKaraf
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStopWithTimeout
parameter_list|()
throws|throws
name|Exception
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
name|data
init|=
operator|new
name|File
argument_list|(
name|home
argument_list|,
literal|"data"
argument_list|)
decl_stmt|;
name|Utils
operator|.
name|deleteDirectory
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.home"
argument_list|,
name|home
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.data"
argument_list|,
name|data
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.framework.factory"
argument_list|,
literal|"org.apache.felix.framework.FrameworkFactory"
argument_list|)
expr_stmt|;
name|Main
name|main
init|=
operator|new
name|Main
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|LifecycleManager
name|lifecycleManager
init|=
name|main
operator|.
name|launch
argument_list|()
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|Framework
name|framework
init|=
name|main
operator|.
name|getFramework
argument_list|()
decl_stmt|;
name|String
name|activatorName
init|=
name|TimeoutShutdownActivator
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
name|Bundle
name|bundle
init|=
name|framework
operator|.
name|getBundleContext
argument_list|()
operator|.
name|installBundle
argument_list|(
literal|"foo"
argument_list|,
name|TinyBundles
operator|.
name|newBundle
argument_list|()
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_ACTIVATOR
argument_list|,
name|TimeoutShutdownActivator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|activatorName
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|activatorName
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
name|withBnd
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
name|long
name|t0
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|lifecycleManager
operator|.
name|destroyKaraf
argument_list|()
expr_stmt|;
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|//        System.err.println("Shutdown duration: " + (t1 - t0) + " ms");
name|Assert
operator|.
name|assertTrue
argument_list|(
operator|(
name|t1
operator|-
name|t0
operator|)
operator|>
name|TimeoutShutdownActivator
operator|.
name|TIMEOUT
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

