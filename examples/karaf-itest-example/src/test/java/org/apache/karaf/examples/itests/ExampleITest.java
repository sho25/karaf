begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|examples
operator|.
name|itests
package|;
end_package

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
name|Feature
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
name|FeaturesService
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
name|itests
operator|.
name|KarafTestSupport
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
name|junit
operator|.
name|PaxExam
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
name|karaf
operator|.
name|options
operator|.
name|LogLevelOption
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
name|options
operator|.
name|MavenArtifactUrlReference
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
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
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
name|spi
operator|.
name|reactors
operator|.
name|PerClass
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
name|mavenBundle
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
name|karaf
operator|.
name|container
operator|.
name|internal
operator|.
name|JavaVersionUtil
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|*
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|editConfigurationFilePut
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
name|options
operator|.
name|extra
operator|.
name|VMOption
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|ExampleITest
extends|extends
name|KarafTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
name|MavenArtifactUrlReference
name|karafUrl
init|=
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
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"tar.gz"
argument_list|)
decl_stmt|;
name|String
name|httpPort
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|getAvailablePort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|MIN_HTTP_PORT
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|MAX_HTTP_PORT
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|rmiRegistryPort
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|getAvailablePort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|MIN_RMI_REG_PORT
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|MAX_RMI_REG_PORT
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|rmiServerPort
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|getAvailablePort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|MIN_RMI_SERVER_PORT
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|MAX_RMI_SERVER_PORT
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|sshPort
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|getAvailablePort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|MIN_SSH_PORT
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|MAX_SSH_PORT
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|localRepository
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|)
decl_stmt|;
if|if
condition|(
name|localRepository
operator|==
literal|null
condition|)
block|{
name|localRepository
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|JavaVersionUtil
operator|.
name|getMajorVersion
argument_list|()
operator|>=
literal|9
condition|)
block|{
return|return
operator|new
name|Option
index|[]
block|{
comment|//KarafDistributionOption.debugConfiguration("8889", true),
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|karafUrl
argument_list|)
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/exam"
argument_list|)
argument_list|)
block|,
comment|// enable JMX RBAC security, thanks to the KarafMBeanServerBuilder
name|configureSecurity
argument_list|()
operator|.
name|disableKarafMBeanServerBuilder
argument_list|()
block|,
comment|// configureConsole().ignoreLocalConsole(),
name|keepRuntimeFolder
argument_list|()
block|,
name|logLevel
argument_list|(
name|LogLevelOption
operator|.
name|LogLevel
operator|.
name|INFO
argument_list|)
block|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.awaitility"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"awaitility"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.servicemix.bundles"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"org.apache.servicemix.bundles.hamcrest"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf.itests"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"common"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.web.cfg"
argument_list|,
literal|"org.osgi.service.http.port"
argument_list|,
name|httpPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.management.cfg"
argument_list|,
literal|"rmiRegistryPort"
argument_list|,
name|rmiRegistryPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.management.cfg"
argument_list|,
literal|"rmiServerPort"
argument_list|,
name|rmiServerPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.shell.cfg"
argument_list|,
literal|"sshPort"
argument_list|,
name|sshPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|,
name|localRepository
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-reads=java.xml=java.logging"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/org.apache.karaf.specs.locator=java.xml,ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--patch-module"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.base=lib/endorsed/org.apache.karaf.specs.locator-"
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
operator|+
literal|".jar"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--patch-module"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.xml=lib/endorsed/org.apache.karaf.specs.java.xml-"
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.version"
argument_list|)
operator|+
literal|".jar"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.security=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.net=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.lang=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.base/java.util=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.naming/javax.naming.spi=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-opens"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"java.rmi/sun.rmi.transport.tcp=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/sun.net.www.protocol.http=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/sun.net.www.protocol.https=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=java.base/sun.net.www.protocol.jar=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"--add-exports=jdk.naming.rmi/com.sun.jndi.url.rmi=ALL-UNNAMED"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"-classpath"
argument_list|)
block|,
operator|new
name|VMOption
argument_list|(
literal|"lib/jdk9plus/*"
operator|+
name|File
operator|.
name|pathSeparator
operator|+
literal|"lib/boot/*"
argument_list|)
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|Option
index|[]
block|{
comment|//KarafDistributionOption.debugConfiguration("8889", true),
name|karafDistributionConfiguration
argument_list|()
operator|.
name|frameworkUrl
argument_list|(
name|karafUrl
argument_list|)
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/exam"
argument_list|)
argument_list|)
block|,
comment|// enable JMX RBAC security, thanks to the KarafMBeanServerBuilder
name|configureSecurity
argument_list|()
operator|.
name|disableKarafMBeanServerBuilder
argument_list|()
block|,
comment|// configureConsole().ignoreLocalConsole(),
name|keepRuntimeFolder
argument_list|()
block|,
name|logLevel
argument_list|(
name|LogLevelOption
operator|.
name|LogLevel
operator|.
name|INFO
argument_list|)
block|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.awaitility"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"awaitility"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.servicemix.bundles"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"org.apache.servicemix.bundles.hamcrest"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|,
name|mavenBundle
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf.itests"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"common"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.web.cfg"
argument_list|,
literal|"org.osgi.service.http.port"
argument_list|,
name|httpPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.management.cfg"
argument_list|,
literal|"rmiRegistryPort"
argument_list|,
name|rmiRegistryPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.management.cfg"
argument_list|,
literal|"rmiServerPort"
argument_list|,
name|rmiServerPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.shell.cfg"
argument_list|,
literal|"sshPort"
argument_list|,
name|sshPort
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
literal|"org.ops4j.pax.url.mvn.localRepository"
argument_list|,
name|localRepository
argument_list|)
block|}
return|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|listBundleCommand
parameter_list|()
throws|throws
name|Exception
block|{
comment|// assert on an available service
name|assertServiceAvailable
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// installing a feature and verifying that it's correctly installed
name|installAndAssertFeature
argument_list|(
literal|"scr"
argument_list|)
expr_stmt|;
comment|// testing a command execution
name|String
name|bundles
init|=
name|executeCommand
argument_list|(
literal|"bundle:list -t 0"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"junit"
argument_list|,
name|bundles
argument_list|)
expr_stmt|;
name|String
name|features
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"scr"
argument_list|,
name|features
argument_list|)
expr_stmt|;
comment|// using a service and assert state or result
name|FeaturesService
name|featuresService
init|=
name|getOsgiService
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Feature
name|scr
init|=
name|featuresService
operator|.
name|getFeature
argument_list|(
literal|"scr"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"scr"
argument_list|,
name|scr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

