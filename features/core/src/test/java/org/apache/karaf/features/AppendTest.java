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
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|eq
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
name|assertTrue
import|;
end_import

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
name|FileNotFoundException
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
name|util
operator|.
name|Dictionary
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
name|Properties
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
name|FeatureConfigInstaller
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
name|easymock
operator|.
name|IMocksControl
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
name|InvalidSyntaxException
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
name|cm
operator|.
name|Configuration
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
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_class
specifier|public
class|class
name|AppendTest
block|{
specifier|private
name|IMocksControl
name|c
decl_stmt|;
specifier|private
name|Feature
name|feature
decl_stmt|;
specifier|private
name|ConfigurationAdmin
name|admin
decl_stmt|;
specifier|private
name|FeatureConfigInstaller
name|installer
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.data"
argument_list|,
literal|"data"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
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
literal|"internal/service/f08.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|Feature
index|[]
name|features
init|=
name|r
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|feature
operator|=
name|features
index|[
literal|0
index|]
expr_stmt|;
name|checkFeature
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|c
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
name|admin
operator|=
name|c
operator|.
name|createMock
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
expr_stmt|;
name|installer
operator|=
operator|new
name|FeatureConfigInstaller
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoChange
parameter_list|()
throws|throws
name|Exception
block|{
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|original
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|original
operator|.
name|put
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|expectConfig
argument_list|(
name|admin
argument_list|,
name|original
argument_list|)
expr_stmt|;
name|c
operator|.
name|replay
argument_list|()
expr_stmt|;
name|installer
operator|.
name|installFeatureConfigs
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|c
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendWhenFileExists
parameter_list|()
throws|throws
name|Exception
block|{
name|testAppendInternal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendWhenNoFileExists
parameter_list|()
throws|throws
name|Exception
block|{
name|testAppendInternal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testAppendInternal
parameter_list|(
name|boolean
name|cfgFileExists
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
throws|,
name|FileNotFoundException
block|{
name|File
name|cfgFile
init|=
operator|new
name|File
argument_list|(
literal|"target/org.ops4j.pax.web.cfg"
argument_list|)
decl_stmt|;
name|cfgFile
operator|.
name|delete
argument_list|()
expr_stmt|;
if|if
condition|(
name|cfgFileExists
condition|)
block|{
name|cfgFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|original
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|original
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|expectConfig
argument_list|(
name|admin
argument_list|,
name|original
argument_list|)
decl_stmt|;
name|Capture
argument_list|<
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|captured
init|=
name|EasyMock
operator|.
name|newCapture
argument_list|()
decl_stmt|;
name|config
operator|.
name|update
argument_list|(
name|EasyMock
operator|.
name|capture
argument_list|(
name|captured
argument_list|)
argument_list|)
expr_stmt|;
name|expectLastCall
argument_list|()
expr_stmt|;
name|c
operator|.
name|replay
argument_list|()
expr_stmt|;
name|installer
operator|.
name|installFeatureConfigs
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|c
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"data/pax-web-jsp"
argument_list|,
name|captured
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|)
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|cfgFile
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"data/pax-web-jsp"
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|expectConfig
parameter_list|(
name|ConfigurationAdmin
name|admin
parameter_list|,
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|original
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
block|{
name|Configuration
name|config
init|=
name|c
operator|.
name|createMock
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|admin
operator|.
name|listConfigurations
argument_list|(
name|eq
argument_list|(
literal|"(service.pid=org.ops4j.pax.web)"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Configuration
index|[]
block|{
name|config
block|}
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|config
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|original
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
return|return
name|config
return|;
block|}
specifier|private
name|void
name|checkFeature
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
name|ConfigInfo
name|configInfo
init|=
name|feature
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|configInfo
operator|.
name|isAppend
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
name|configInfo
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|tempDir
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"data/pax-web-jsp"
argument_list|,
name|tempDir
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

