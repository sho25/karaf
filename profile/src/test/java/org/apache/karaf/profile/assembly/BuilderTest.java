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
name|BufferedWriter
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|model
operator|.
name|Features
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
name|RepositoryCacheImpl
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
name|osgi
operator|.
name|framework
operator|.
name|Constants
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

begin_class
specifier|public
class|class
name|BuilderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCyclicRepos
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
comment|// Create dummy etc/config.properties file
name|Path
name|config
init|=
name|workDir
operator|.
name|resolve
argument_list|(
literal|"etc/config.properties"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|config
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|BufferedWriter
name|w
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|config
argument_list|)
init|)
block|{
name|w
operator|.
name|write
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_SYSTEMPACKAGES
operator|+
literal|"= org.osgi.dto"
argument_list|)
expr_stmt|;
name|w
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
name|Constants
operator|.
name|FRAMEWORK_SYSTEMCAPABILITIES
operator|+
literal|"= "
argument_list|)
expr_stmt|;
name|w
operator|.
name|newLine
argument_list|()
expr_stmt|;
block|}
name|Path
name|mvnRepo
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-classes/repo"
argument_list|)
decl_stmt|;
name|Builder
name|builder
init|=
name|Builder
operator|.
name|newInstance
argument_list|()
operator|.
name|repositories
argument_list|(
name|Builder
operator|.
name|Stage
operator|.
name|Startup
argument_list|,
literal|true
argument_list|,
literal|"mvn:foo/baz/1.0/xml/features"
argument_list|)
operator|.
name|homeDirectory
argument_list|(
name|workDir
argument_list|)
operator|.
name|localRepository
argument_list|(
name|mvnRepo
operator|.
name|toString
argument_list|()
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
annotation|@
name|Test
specifier|public
name|void
name|testPidsToExtract
parameter_list|()
block|{
name|String
name|pidsToExtract
init|=
literal|"\n"
operator|+
literal|"                        !jmx.acl.*,\n"
operator|+
literal|"                        !org.apache.karaf.command.acl.*,\n"
operator|+
literal|"                        *\n"
operator|+
literal|"                    "
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|p2e
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|pidsToExtract
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
decl_stmt|;
name|Builder
name|builder
init|=
name|Builder
operator|.
name|newInstance
argument_list|()
operator|.
name|pidsToExtract
argument_list|(
name|p2e
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|builder
operator|.
name|getPidsToExtract
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"!jmx.acl.*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|builder
operator|.
name|getPidsToExtract
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"!org.apache.karaf.command.acl.*"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|builder
operator|.
name|getPidsToExtract
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|//@Test
comment|//@Ignore("no need to run this test")
specifier|public
name|void
name|consistencyReport
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Features
argument_list|>
name|features
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Builder
name|builder
init|=
operator|new
name|Builder
argument_list|()
decl_stmt|;
name|File
index|[]
name|uris
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/repositories"
argument_list|)
operator|.
name|listFiles
argument_list|(
parameter_list|(
name|dir
parameter_list|,
name|name
parameter_list|)
lambda|->
name|name
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|uris
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|f
range|:
name|uris
control|)
block|{
name|features
operator|.
name|put
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|RepositoryImpl
argument_list|(
name|f
operator|.
name|toURI
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|getFeaturesInternal
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|builder
operator|.
name|generateConsistencyReport
argument_list|(
name|features
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/consistency.xml"
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|builder
operator|.
name|generateConsistencyReport
argument_list|(
name|features
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/consistency-full.xml"
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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

