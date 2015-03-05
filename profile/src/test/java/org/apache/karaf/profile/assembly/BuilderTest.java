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
name|profile
operator|.
name|assembly
package|;
end_package

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
name|nio
operator|.
name|file
operator|.
name|DirectoryStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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

begin_class
specifier|public
class|class
name|BuilderTest
block|{
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"This test can not run at this position as it needs the staticFramework kar which is not yet available"
argument_list|)
specifier|public
name|void
name|testBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|workDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/distrib"
argument_list|)
decl_stmt|;
name|recursiveDelete
argument_list|(
name|workDir
argument_list|)
expr_stmt|;
name|Builder
name|builder
init|=
name|Builder
operator|.
name|newInstance
argument_list|()
operator|.
name|staticFramework
argument_list|()
operator|.
name|profilesUris
argument_list|(
literal|"jar:mvn:org.apache.karaf.demos.profiles/registry/4.0.0-SNAPSHOT!/"
argument_list|)
operator|.
name|environment
argument_list|(
literal|"static"
argument_list|)
operator|.
name|profiles
argument_list|(
literal|"karaf"
argument_list|,
literal|"example-loanbroker-bank1"
argument_list|,
literal|"example-loanbroker-bank2"
argument_list|,
literal|"example-loanbroker-bank3"
argument_list|,
literal|"example-loanbroker-broker"
argument_list|,
literal|"activemq-broker"
argument_list|)
operator|.
name|homeDirectory
argument_list|(
name|workDir
argument_list|)
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|generateAssembly
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
specifier|private
specifier|static
name|void
name|recursiveDelete
parameter_list|(
name|Path
name|path
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|path
argument_list|)
condition|)
block|{
if|if
condition|(
name|Files
operator|.
name|isDirectory
argument_list|(
name|path
argument_list|)
condition|)
block|{
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|children
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|path
argument_list|)
init|)
block|{
for|for
control|(
name|Path
name|child
range|:
name|children
control|)
block|{
name|recursiveDelete
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Files
operator|.
name|delete
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

