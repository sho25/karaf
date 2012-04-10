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
name|instance
operator|.
name|internal
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|Properties
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
name|instance
operator|.
name|Instance
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
name|instance
operator|.
name|InstanceSettings
import|;
end_import

begin_class
specifier|public
class|class
name|InstanceServiceImplTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testHandleFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|InstanceServiceImpl
name|as
init|=
operator|new
name|InstanceServiceImpl
argument_list|()
decl_stmt|;
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|getName
argument_list|()
argument_list|,
literal|".test"
argument_list|)
decl_stmt|;
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"featuresBoot"
argument_list|,
literal|"abc,def "
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"featuresRepositories"
argument_list|,
literal|"somescheme://xyz"
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
try|try
block|{
name|p
operator|.
name|store
argument_list|(
name|os
argument_list|,
literal|"Test comment"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|InstanceSettings
name|s
init|=
operator|new
name|InstanceSettings
argument_list|(
literal|8122
argument_list|,
literal|1122
argument_list|,
literal|44444
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|as
operator|.
name|handleFeatures
argument_list|(
name|f
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|Properties
name|p2
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
try|try
block|{
name|p2
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|p2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc,def,test"
argument_list|,
name|p2
operator|.
name|get
argument_list|(
literal|"featuresBoot"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somescheme://xyz"
argument_list|,
name|p2
operator|.
name|get
argument_list|(
literal|"featuresRepositories"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Ensure the instance:create generates all the required configuration files      * //TODO: fix this test so it can run in an IDE      */
specifier|public
name|void
name|testConfigurationFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|InstanceServiceImpl
name|service
init|=
operator|new
name|InstanceServiceImpl
argument_list|()
decl_stmt|;
name|service
operator|.
name|setStorageLocation
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/instances/"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.base"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/test-classes/"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
literal|8122
argument_list|,
literal|1122
argument_list|,
literal|44444
argument_list|,
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Instance
name|instance
init|=
name|service
operator|.
name|createInstance
argument_list|(
name|getName
argument_list|()
argument_list|,
name|settings
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/config.properties"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/users.properties"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/startup.properties"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/java.util.logging.properties"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/org.apache.karaf.features.cfg"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/org.apache.felix.fileinstall-deploy.cfg"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/org.apache.karaf.log.cfg"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/org.apache.karaf.management.cfg"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/org.ops4j.pax.logging.cfg"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|instance
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>      * Test the renaming of an existing instance.      *</p>      */
specifier|public
name|void
name|testRenameInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|InstanceServiceImpl
name|service
init|=
operator|new
name|InstanceServiceImpl
argument_list|()
decl_stmt|;
name|service
operator|.
name|setStorageLocation
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/instances/"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.base"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/test-classes/"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
literal|8122
argument_list|,
literal|1122
argument_list|,
literal|44444
argument_list|,
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|service
operator|.
name|createInstance
argument_list|(
name|getName
argument_list|()
argument_list|,
name|settings
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|service
operator|.
name|renameInstance
argument_list|(
name|getName
argument_list|()
argument_list|,
name|getName
argument_list|()
operator|+
literal|"b"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|service
operator|.
name|getInstance
argument_list|(
name|getName
argument_list|()
operator|+
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertFileExists
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expected "
operator|+
name|file
operator|.
name|getCanonicalPath
argument_list|()
operator|+
literal|" to exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

