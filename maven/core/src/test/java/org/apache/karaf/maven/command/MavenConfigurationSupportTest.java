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
name|maven
operator|.
name|command
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
name|FileWriter
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
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
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
name|shaded
operator|.
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
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

begin_class
specifier|public
class|class
name|MavenConfigurationSupportTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|sequenceFiles
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|dataDir
init|=
operator|new
name|File
argument_list|(
literal|"target/data"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|dataDir
argument_list|)
expr_stmt|;
name|dataDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|MavenConfigurationSupport
name|support
init|=
operator|new
name|MavenConfigurationSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doAction
parameter_list|(
name|String
name|prefix
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|)
throws|throws
name|Exception
block|{ }
block|}
decl_stmt|;
name|File
name|newFile
init|=
name|support
operator|.
name|nextSequenceFile
argument_list|(
name|dataDir
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|"file-(\\d+).txt"
argument_list|)
argument_list|,
literal|"file-%04d.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^file-\\d+\\.txt$"
argument_list|)
operator|.
name|matcher
argument_list|(
name|newFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|FileWriter
name|fw
init|=
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|dataDir
argument_list|,
literal|"file-abcd.txt"
argument_list|)
argument_list|)
init|)
block|{
name|fw
operator|.
name|write
argument_list|(
literal|"~"
argument_list|)
expr_stmt|;
block|}
name|newFile
operator|=
name|support
operator|.
name|nextSequenceFile
argument_list|(
name|dataDir
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|"file-(\\d+).txt"
argument_list|)
argument_list|,
literal|"file-%04d.txt"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^file-\\d+\\.txt$"
argument_list|)
operator|.
name|matcher
argument_list|(
name|newFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|FileWriter
name|fw
init|=
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|dataDir
argument_list|,
literal|"file-0004.txt"
argument_list|)
argument_list|)
init|)
block|{
name|fw
operator|.
name|write
argument_list|(
literal|"~"
argument_list|)
expr_stmt|;
block|}
name|newFile
operator|=
name|support
operator|.
name|nextSequenceFile
argument_list|(
name|dataDir
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|"file-(\\d+).txt"
argument_list|)
argument_list|,
literal|"file-%04d.txt"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^file-\\d+\\.txt$"
argument_list|)
operator|.
name|matcher
argument_list|(
name|newFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
