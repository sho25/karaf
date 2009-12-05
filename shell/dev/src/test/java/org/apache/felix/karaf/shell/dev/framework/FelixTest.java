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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|dev
operator|.
name|framework
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
name|IOException
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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|dev
operator|.
name|util
operator|.
name|IO
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

begin_comment
comment|/**  * Test cases for {@link org.apache.felix.karaf.shell.dev.framework.Felix}  */
end_comment

begin_class
specifier|public
class|class
name|FelixTest
block|{
specifier|private
name|File
name|base
decl_stmt|;
specifier|private
name|File
name|etc
decl_stmt|;
specifier|private
name|Felix
name|felix
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
comment|// creating a dummy karaf instance folder
name|base
operator|=
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
expr_stmt|;
name|base
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// make sure the etc directory exists
name|etc
operator|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"etc"
argument_list|)
expr_stmt|;
name|etc
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|felix
operator|=
operator|new
name|Felix
argument_list|(
name|base
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|enableDebug
parameter_list|()
throws|throws
name|IOException
block|{
name|IO
operator|.
name|copyTextToFile
argument_list|(
name|FelixTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"config.properties"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"config.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|felix
operator|.
name|enableDebug
argument_list|(
name|base
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"config.properties"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"felix.log.level"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"4"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"felix.log.level"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDisableDebug
parameter_list|()
throws|throws
name|IOException
block|{
name|IO
operator|.
name|copyTextToFile
argument_list|(
name|FelixTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"enabled-config.properties"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"config.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|felix
operator|.
name|disableDebug
argument_list|(
name|base
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|etc
argument_list|,
literal|"config.properties"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|properties
operator|.
name|containsKey
argument_list|(
literal|"felix.log.level"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

