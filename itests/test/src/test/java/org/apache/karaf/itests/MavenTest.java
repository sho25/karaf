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
name|itests
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
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
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|NetworkConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|AbstractHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|ProbeBuilder
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
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|mvn
operator|.
name|MavenResolver
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
name|BundleContext
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
name|ServiceEvent
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
name|ServiceListener
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|itests
operator|.
name|KarafTestSupport
operator|.
name|*
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
name|junit
operator|.
name|Assert
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
name|CoreOptions
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
name|*
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
name|MavenTest
comment|/*extends BaseTest*/
block|{
specifier|public
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Server
name|server
decl_stmt|;
specifier|private
specifier|static
name|int
name|port
decl_stmt|;
specifier|private
specifier|static
name|ExecutorService
name|pool
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|AtomicBoolean
name|requestAtPort3333Done
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
comment|// don't extend, because we don't want @Rule Retry
specifier|private
specifier|static
name|KarafTestSupport
name|karafTestSupport
init|=
operator|new
name|KarafTestSupport
argument_list|()
decl_stmt|;
comment|/**      * This server will act as HTTP proxy. @Test methods will change maven settings, where proxy is configured      * and update<code>org.ops4j.pax.ul.mvn</code> PID, which will republish {@link MavenResolver} service.      * @throws Exception      */
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startJetty
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
operator|new
name|Server
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
operator|new
name|AbstractHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|String
name|target
parameter_list|,
name|Request
name|baseRequest
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
try|try
block|{
name|int
name|port
init|=
name|baseRequest
operator|.
name|getServerPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|==
literal|3333
operator|&&
name|request
operator|.
name|getRequestURI
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|requestAtPort3333Done
operator|.
name|get
argument_list|()
condition|)
block|{
name|requestAtPort3333Done
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// explicit timeout at first attempt - higher than the one set by Aether
name|Thread
operator|.
name|sleep
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
name|response
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
literal|0x42
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{                 }
finally|finally
block|{
name|baseRequest
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|port
operator|=
operator|(
operator|(
name|NetworkConnector
operator|)
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|)
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopJetty
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|pool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
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
name|Option
index|[]
name|baseOptions
init|=
operator|new
name|Option
index|[]
block|{
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
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.ops4j.pax.logging.cfg"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/etc/org.ops4j.pax.logging.cfg"
argument_list|)
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.features.cfg"
argument_list|,
literal|"updateSnapshots"
argument_list|,
literal|"none"
argument_list|)
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
block|,         }
decl_stmt|;
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|baseOptions
argument_list|)
argument_list|)
decl_stmt|;
comment|// Prepare default pax-url-aether configuration
name|options
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
comment|//                new TimeoutOption(3600000),
comment|//                KarafDistributionOption.debugConfiguration("8889", false),
name|bundle
argument_list|(
literal|"mvn:commons-io/commons-io/2.5"
argument_list|)
argument_list|,
name|mavenBundle
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"javax.servlet"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"javax.servlet-api"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|mavenBundle
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.eclipse.jetty"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"jetty-server"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|mavenBundle
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.eclipse.jetty"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"jetty-http"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|mavenBundle
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.eclipse.jetty"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"jetty-util"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|mavenBundle
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.eclipse.jetty"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"jetty-io"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
name|replaceConfigurationFile
argument_list|(
literal|"etc/maven-settings.xml"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/etc/maven-settings.xml"
argument_list|)
argument_list|)
argument_list|,
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.ops4j.pax.url.mvn.cfg"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/etc/org.ops4j.pax.url.mvn.cfg"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|options
operator|.
name|toArray
argument_list|(
operator|new
name|Option
index|[
name|options
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|smartRetriesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|karafTestSupport
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
specifier|final
name|ConfigurationAdmin
name|cm
init|=
name|karafTestSupport
operator|.
name|getOsgiService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|,
literal|3000
argument_list|)
decl_stmt|;
name|updateSettings
argument_list|()
expr_stmt|;
name|awaitMavenResolver
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
name|config
init|=
name|cm
operator|.
name|getConfiguration
argument_list|(
literal|"org.ops4j.pax.url.mvn"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
name|config
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.ops4j.pax.url.mvn.globalChecksumPolicy"
argument_list|,
literal|"ignore"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.ops4j.pax.url.mvn.socket.readTimeout"
argument_list|,
literal|"2000"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.ops4j.pax.url.mvn.connection.retryCount"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"org.ops4j.pax.url.mvn.repositories"
argument_list|,
literal|"http://localhost:1111/repository@id=r1,"
operator|+
literal|"http://localhost:2222/repository@id=r2,"
operator|+
literal|"http://localhost:3333/repository@id=r3"
argument_list|)
expr_stmt|;
name|config
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// grab modified resolver
name|MavenResolver
name|resolver
init|=
name|karafTestSupport
operator|.
name|getOsgiService
argument_list|(
name|MavenResolver
operator|.
name|class
argument_list|,
literal|15000
argument_list|)
decl_stmt|;
try|try
block|{
name|resolver
operator|.
name|resolve
argument_list|(
literal|"mvn:commons-universalis/commons-universalis/42"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail at first attempt"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|File
name|f
init|=
name|resolver
operator|.
name|resolve
argument_list|(
literal|"mvn:commons-universalis/commons-universalis/42"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|byte
index|[]
name|commonsUniversalis
init|=
name|FileUtils
operator|.
name|readFileToByteArray
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|commonsUniversalis
operator|.
name|length
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|commonsUniversalis
index|[
literal|0
index|]
argument_list|,
name|equalTo
argument_list|(
operator|(
name|byte
operator|)
literal|0x42
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateSettings
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|settingsFile
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|)
argument_list|,
literal|"etc/maven-settings.xml"
argument_list|)
decl_stmt|;
name|String
name|settings
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|settingsFile
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|settings
operator|=
name|settings
operator|.
name|replace
argument_list|(
literal|"@@port@@"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|write
argument_list|(
name|settingsFile
argument_list|,
name|settings
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invoke config admin task and await reregistration of {@link MavenResolver} service      */
specifier|private
name|void
name|awaitMavenResolver
parameter_list|(
name|Runnable
name|task
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|ServiceListener
name|listener
init|=
name|event
lambda|->
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ServiceEvent
operator|.
name|UNREGISTERING
operator|||
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ServiceEvent
operator|.
name|REGISTERED
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|bundleContext
operator|.
name|addServiceListener
argument_list|(
name|listener
argument_list|,
literal|"(objectClass=org.ops4j.pax.url.mvn.MavenResolver)"
argument_list|)
expr_stmt|;
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|removeServiceListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

