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
name|itests
package|;
end_package

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|bootClasspathLibrary
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
name|felix
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
name|options
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
name|systemPackages
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
name|systemProperty
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
name|equinox
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
name|wrappedBundle
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
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
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
name|osgi
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
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
extends|extends
name|AbstractIntegrationTest
block|{
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
literal|5000
argument_list|)
expr_stmt|;
name|CommandProcessor
name|cp
init|=
name|getOsgiService
argument_list|(
name|CommandProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"osgi:list --help"
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|CommandProcessor
name|cp
init|=
name|getOsgiService
argument_list|(
name|CommandProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"log:display"
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
name|Bundle
name|b
init|=
name|getInstalledBundle
argument_list|(
literal|"org.apache.felix.karaf.shell.log"
argument_list|)
decl_stmt|;
name|b
operator|.
name|start
argument_list|()
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
literal|"log:display"
argument_list|)
expr_stmt|;
name|b
operator|.
name|stop
argument_list|()
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
literal|"log:display"
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
comment|//    @Test
comment|//    public void testCommandGroup() throws Exception {
comment|//        Thread.sleep(5000);
comment|//
comment|//        Shell shell = getOsgiService(Shell.class);
comment|//        shell.execute("osgi");
comment|//        shell.execute("help");
comment|//        shell.execute("..");
comment|//    }
comment|//
comment|//    @Test
comment|//    public void testCommandGroupAfterInstall() throws Exception {
comment|//        Bundle b = getInstalledBundle("org.apache.felix.karaf.shell.log");
comment|//        b.start();
comment|//
comment|//        Thread.sleep(5000);
comment|//
comment|//        Shell shell = getOsgiService(Shell.class);
comment|//        shell.execute("log");
comment|//        shell.execute("help");
comment|//        shell.execute("..");
comment|//    }
comment|//
annotation|@
name|Configuration
specifier|public
specifier|static
name|Option
index|[]
name|configuration
parameter_list|()
block|{
name|Option
index|[]
name|options
init|=
name|options
argument_list|(
comment|// this is how you set the default log level when using pax logging (logProfile)
name|systemProperty
argument_list|(
literal|"org.ops4j.pax.logging.DefaultServiceLog.level"
argument_list|)
operator|.
name|value
argument_list|(
literal|"DEBUG"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"karaf.name"
argument_list|)
operator|.
name|value
argument_list|(
literal|"root"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"karaf.home"
argument_list|)
operator|.
name|value
argument_list|(
literal|"target/karaf.home"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"karaf.base"
argument_list|)
operator|.
name|value
argument_list|(
literal|"target/karaf.home"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"karaf.startLocalConsole"
argument_list|)
operator|.
name|value
argument_list|(
literal|"false"
argument_list|)
argument_list|,
name|systemProperty
argument_list|(
literal|"karaf.startRemoteShell"
argument_list|)
operator|.
name|value
argument_list|(
literal|"false"
argument_list|)
argument_list|,
comment|// hack system packages
name|systemPackages
argument_list|(
literal|"org.apache.felix.karaf.jaas.boot;version=1.99"
argument_list|)
argument_list|,
name|bootClasspathLibrary
argument_list|(
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.jaas"
argument_list|,
literal|"org.apache.felix.karaf.jaas.boot"
argument_list|)
argument_list|)
operator|.
name|afterFramework
argument_list|()
argument_list|,
name|bootClasspathLibrary
argument_list|(
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf"
argument_list|,
literal|"org.apache.felix.karaf.main"
argument_list|)
argument_list|)
operator|.
name|afterFramework
argument_list|()
argument_list|,
comment|// Log
name|mavenBundle
argument_list|(
literal|"org.ops4j.pax.logging"
argument_list|,
literal|"pax-logging-api"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.ops4j.pax.logging"
argument_list|,
literal|"pax-logging-service"
argument_list|)
argument_list|,
comment|// Felix Config Admin
name|mavenBundle
argument_list|(
literal|"org.apache.felix"
argument_list|,
literal|"org.apache.felix.configadmin"
argument_list|)
argument_list|,
comment|// Blueprint
name|mavenBundle
argument_list|(
literal|"org.apache.geronimo.blueprint"
argument_list|,
literal|"geronimo-blueprint"
argument_list|)
argument_list|,
comment|// Bundles
name|mavenBundle
argument_list|(
literal|"org.apache.mina"
argument_list|,
literal|"mina-core"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.sshd"
argument_list|,
literal|"sshd-core"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.jaas"
argument_list|,
literal|"org.apache.felix.karaf.jaas.config"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.felix.gogo"
argument_list|,
literal|"org.apache.felix.gogo.runtime"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.shell"
argument_list|,
literal|"org.apache.felix.karaf.shell.console"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.shell"
argument_list|,
literal|"org.apache.felix.karaf.shell.osgi"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
literal|"org.apache.felix.karaf.shell"
argument_list|,
literal|"org.apache.felix.karaf.shell.log"
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|equinox
argument_list|()
argument_list|,
name|felix
argument_list|()
argument_list|)
decl_stmt|;
comment|// We need to add pax-exam-junit here when running with the ibm
comment|// jdk to avoid the following exception during the test run:
comment|// ClassNotFoundException: org.ops4j.pax.exam.junit.Configuration
if|if
condition|(
literal|"IBM Corporation"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
argument_list|)
condition|)
block|{
name|Option
index|[]
name|ibmOptions
init|=
name|options
argument_list|(
name|wrappedBundle
argument_list|(
name|maven
argument_list|(
literal|"org.ops4j.pax.exam"
argument_list|,
literal|"pax-exam-junit"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|options
operator|=
name|combine
argument_list|(
name|ibmOptions
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

