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
name|shell
operator|.
name|itests
package|;
end_package

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|exam
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|karafDistributionConfiguration
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
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|service
operator|.
name|command
operator|.
name|CommandProcessor
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|exam
operator|.
name|Option
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
name|exam
operator|.
name|TestProbeBuilder
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
name|exam
operator|.
name|junit
operator|.
name|Configuration
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
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
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
name|exam
operator|.
name|junit
operator|.
name|ProbeBuilder
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|CoreTest
block|{
annotation|@
name|Inject
specifier|private
name|CommandProcessor
name|cp
decl_stmt|;
annotation|@
name|ProbeBuilder
specifier|public
name|TestProbeBuilder
name|probeConfiguration
parameter_list|(
name|TestProbeBuilder
name|probe
parameter_list|)
block|{
name|probe
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*,org.apache.felix.service.*;status=provisional"
argument_list|)
expr_stmt|;
return|return
name|probe
return|;
block|}
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
return|return
operator|new
name|Option
index|[]
block|{
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"apache-karaf"
argument_list|)
operator|.
name|type
argument_list|(
literal|"zip"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelp
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|CommandSession
name|cs
init|=
name|cp
operator|.
name|createSession
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
decl_stmt|;
name|cs
operator|.
name|execute
argument_list|(
literal|"bundle:list --help"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstallCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|12000
argument_list|)
expr_stmt|;
name|CommandSession
name|cs
init|=
name|cp
operator|.
name|createSession
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
decl_stmt|;
try|try
block|{
name|cs
operator|.
name|execute
argument_list|(
literal|"obr:url-list"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"command should not exist"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Command not found"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|execute
argument_list|(
literal|"feature:install obr"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|cs
operator|.
name|execute
argument_list|(
literal|"obr:url-list"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|execute
argument_list|(
literal|"feature:uninstall obr"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
try|try
block|{
name|cs
operator|.
name|execute
argument_list|(
literal|"obr:url-list"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"command should not exist"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Command not found"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
name|cs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

