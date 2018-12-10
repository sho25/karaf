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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|After
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
name|ops4j
operator|.
name|pax
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
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|startlevel
operator|.
name|FrameworkStartLevel
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
name|MainLockingTest
block|{
specifier|private
name|File
name|home
decl_stmt|;
specifier|private
name|File
name|data
decl_stmt|;
specifier|private
name|File
name|log
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
name|home
operator|=
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
literal|"test-karaf-home"
argument_list|)
expr_stmt|;
name|data
operator|=
operator|new
name|File
argument_list|(
name|home
argument_list|,
literal|"data"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|=
operator|new
name|File
argument_list|(
name|home
argument_list|,
literal|"log"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|Utils
operator|.
name|deleteDirectory
argument_list|(
name|data
argument_list|)
expr_stmt|;
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
literal|"karaf.log"
argument_list|,
name|log
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.lock"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.lock.delay"
argument_list|,
literal|"1000"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.lock.class"
argument_list|,
literal|"org.apache.karaf.main.MockLock"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|home
operator|=
literal|null
expr_stmt|;
name|data
operator|=
literal|null
expr_stmt|;
name|log
operator|=
literal|null
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.home"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.data"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.log"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.framework.factory"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.lock"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.lock.delay"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.lock.class"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.pid.file"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLostMasterLock
parameter_list|()
throws|throws
name|Exception
block|{
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
name|Main
name|main
init|=
operator|new
name|Main
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|main
operator|.
name|launch
argument_list|()
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
name|bundle
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
name|FrameworkStartLevel
name|sl
init|=
name|framework
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockLock
name|lock
init|=
operator|(
name|MockLock
operator|)
name|main
operator|.
name|getLock
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|sl
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
comment|// simulate losing a lock
name|lock
operator|.
name|setIsAlive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|lock
operator|.
name|setLock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// lets wait until the start level change is complete
name|lock
operator|.
name|waitForLock
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sl
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// get lock back
name|lock
operator|.
name|setIsAlive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|lock
operator|.
name|setLock
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// exit framework + lock loop
name|main
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMasterWritesPid
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use data because it's always deleted at the beginning of the test
name|File
name|pidFile
init|=
operator|new
name|File
argument_list|(
name|data
argument_list|,
literal|"test-karaf.pid"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.pid.file"
argument_list|,
name|pidFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|pidFile
operator|.
name|isFile
argument_list|()
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
name|Main
name|main
init|=
operator|new
name|Main
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|main
operator|.
name|launch
argument_list|()
expr_stmt|;
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
name|FrameworkStartLevel
name|sl
init|=
name|framework
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|sl
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
name|MockLock
name|lock
init|=
operator|(
name|MockLock
operator|)
name|main
operator|.
name|getLock
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lock
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|pidFile
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.pid.file"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSlaveWritesPid
parameter_list|()
throws|throws
name|Exception
block|{
comment|// simulate that the lock is not acquired (i.e. instance runs as slave)
name|System
operator|.
name|setProperty
argument_list|(
literal|"test.karaf.mocklock.initiallyLocked"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.lock.level"
argument_list|,
literal|"59"
argument_list|)
expr_stmt|;
comment|// use data because it's always deleted at the beginning of the test
name|File
name|pidFile
init|=
operator|new
name|File
argument_list|(
name|data
argument_list|,
literal|"test-karaf.pid"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.pid.file"
argument_list|,
name|pidFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|pidFile
operator|.
name|isFile
argument_list|()
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
name|Main
name|main
init|=
operator|new
name|Main
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|main
operator|.
name|launch
argument_list|()
expr_stmt|;
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
name|FrameworkStartLevel
name|sl
init|=
name|framework
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|59
argument_list|,
name|sl
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
name|MockLock
name|lock
init|=
operator|(
name|MockLock
operator|)
name|main
operator|.
name|getLock
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|lock
operator|.
name|lock
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lock
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|pidFile
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"test.karaf.mocklock.initiallyLocked"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.lock.level"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"karaf.pid.file"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

