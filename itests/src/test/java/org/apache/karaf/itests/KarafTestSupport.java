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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|configureSecurity
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|keepRuntimeFolder
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
name|replaceConfigurationFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|Callable
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
name|ExecutionException
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
name|FutureTask
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
name|TimeoutException
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
name|management
operator|.
name|remote
operator|.
name|JMXConnector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXServiceURL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|BootFinished
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
name|Rule
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
name|options
operator|.
name|MavenArtifactUrlReference
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
name|Filter
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
name|FrameworkUtil
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
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
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

begin_class
specifier|public
class|class
name|KarafTestSupport
block|{
specifier|public
specifier|static
specifier|final
name|String
name|RMI_SERVER_PORT
init|=
literal|"44445"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_PORT
init|=
literal|"9081"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RMI_REG_PORT
init|=
literal|"1100"
decl_stmt|;
specifier|static
specifier|final
name|Long
name|COMMAND_TIMEOUT
init|=
literal|30000L
decl_stmt|;
specifier|static
specifier|final
name|Long
name|SERVICE_TIMEOUT
init|=
literal|30000L
decl_stmt|;
specifier|private
specifier|static
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KarafTestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Rule
specifier|public
name|KarafTestWatcher
name|baseTestWatcher
init|=
operator|new
name|KarafTestWatcher
argument_list|()
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|FeaturesService
name|featureService
decl_stmt|;
comment|/**      * To make sure the tests run only when the boot features are fully installed      */
annotation|@
name|Inject
name|BootFinished
name|bootFinished
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
specifier|public
name|File
name|getConfigFile
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|URL
name|res
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Config resource "
operator|+
name|path
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|File
argument_list|(
name|res
operator|.
name|getFile
argument_list|()
argument_list|)
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
return|return
operator|new
name|Option
index|[]
block|{
comment|// KarafDistributionOption.debugConfiguration("8889", true),
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
name|enableKarafMBeanServerBuilder
argument_list|()
block|,
name|keepRuntimeFolder
argument_list|()
block|,
name|replaceConfigurationFile
argument_list|(
literal|"etc/org.ops4j.pax.logging.cfg"
argument_list|,
name|getConfigFile
argument_list|(
literal|"/etc/org.ops4j.pax.logging.cfg"
argument_list|)
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.features.cfg"
argument_list|,
literal|"featuresBoot"
argument_list|,
literal|"config,standard,region,package,kar,management"
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.ops4j.pax.web.cfg"
argument_list|,
literal|"org.osgi.service.http.port"
argument_list|,
name|HTTP_PORT
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.management.cfg"
argument_list|,
literal|"rmiRegistryPort"
argument_list|,
name|RMI_REG_PORT
argument_list|)
block|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.management.cfg"
argument_list|,
literal|"rmiServerPort"
argument_list|,
name|RMI_SERVER_PORT
argument_list|)
block|}
return|;
block|}
comment|/**      * Executes a shell command and returns output as a String.      * Commands have a default timeout of 10 seconds.      *      * @param command The command to execute      * @param principals The principals (e.g. RolePrincipal objects) to run the command under      * @return      */
specifier|protected
name|String
name|executeCommand
parameter_list|(
specifier|final
name|String
name|command
parameter_list|,
name|Principal
modifier|...
name|principals
parameter_list|)
block|{
return|return
name|executeCommand
argument_list|(
name|command
argument_list|,
name|COMMAND_TIMEOUT
argument_list|,
literal|false
argument_list|,
name|principals
argument_list|)
return|;
block|}
comment|/**      * Executes a shell command and returns output as a String.      * Commands have a default timeout of 10 seconds.      *      * @param command    The command to execute.      * @param timeout    The amount of time in millis to wait for the command to execute.      * @param silent     Specifies if the command should be displayed in the screen.      * @param principals The principals (e.g. RolePrincipal objects) to run the command under      * @return      */
specifier|protected
name|String
name|executeCommand
parameter_list|(
specifier|final
name|String
name|command
parameter_list|,
specifier|final
name|Long
name|timeout
parameter_list|,
specifier|final
name|Boolean
name|silent
parameter_list|,
specifier|final
name|Principal
modifier|...
name|principals
parameter_list|)
block|{
name|waitForCommandService
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|String
name|response
decl_stmt|;
specifier|final
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|PrintStream
name|printStream
init|=
operator|new
name|PrintStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
decl_stmt|;
specifier|final
name|CommandProcessor
name|commandProcessor
init|=
name|getOsgiService
argument_list|(
name|CommandProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|CommandSession
name|commandSession
init|=
name|commandProcessor
operator|.
name|createSession
argument_list|(
name|System
operator|.
name|in
argument_list|,
name|printStream
argument_list|,
name|System
operator|.
name|err
argument_list|)
decl_stmt|;
specifier|final
name|Callable
argument_list|<
name|String
argument_list|>
name|commandCallable
init|=
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
if|if
condition|(
operator|!
name|silent
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
name|commandSession
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|printStream
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|byteArrayOutputStream
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|FutureTask
argument_list|<
name|String
argument_list|>
name|commandFuture
decl_stmt|;
if|if
condition|(
name|principals
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|commandFuture
operator|=
operator|new
name|FutureTask
argument_list|<
name|String
argument_list|>
argument_list|(
name|commandCallable
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// If principals are defined, run the command callable via Subject.doAs()
name|commandFuture
operator|=
operator|new
name|FutureTask
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|principals
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Subject
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|run
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|commandCallable
operator|.
name|call
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
name|commandFuture
argument_list|)
expr_stmt|;
name|response
operator|=
name|commandFuture
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|response
operator|=
literal|"SHELL COMMAND TIMED OUT: "
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|response
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|ServiceTracker
name|tracker
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|flt
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|filter
operator|+
literal|"))"
expr_stmt|;
block|}
block|}
else|else
block|{
name|flt
operator|=
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
name|Filter
name|osgiFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
name|flt
argument_list|)
decl_stmt|;
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|osgiFilter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Note that the tracker is not closed to keep the reference
comment|// This is buggy, as the service reference may change i think
name|Object
name|svc
init|=
name|type
operator|.
name|cast
argument_list|(
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|svc
operator|==
literal|null
condition|)
block|{
name|Dictionary
name|dic
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Test bundle headers: "
operator|+
name|explode
argument_list|(
name|dic
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"ServiceReference: "
operator|+
name|ref
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
name|flt
argument_list|)
argument_list|)
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Filtered ServiceReference: "
operator|+
name|ref
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Gave up waiting for service "
operator|+
name|flt
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|svc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid filter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|waitForCommandService
parameter_list|(
name|String
name|command
parameter_list|)
block|{
comment|// the commands are represented by services. Due to the asynchronous nature of services they may not be
comment|// immediately available. This code waits the services to be available, in their secured form. It
comment|// means that the code waits for the command service to appear with the roles defined.
if|if
condition|(
name|command
operator|==
literal|null
operator|||
name|command
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|int
name|spaceIdx
init|=
name|command
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|spaceIdx
operator|>
literal|0
condition|)
block|{
name|command
operator|=
name|command
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|spaceIdx
argument_list|)
expr_stmt|;
block|}
name|int
name|colonIndx
init|=
name|command
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|colonIndx
operator|>
literal|0
condition|)
block|{
name|String
name|scope
init|=
name|command
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colonIndx
argument_list|)
decl_stmt|;
name|String
name|function
init|=
name|command
operator|.
name|substring
argument_list|(
name|colonIndx
operator|+
literal|1
argument_list|)
decl_stmt|;
name|waitForService
argument_list|(
literal|"(&(osgi.command.scope="
operator|+
name|scope
operator|+
literal|")(osgi.command.function="
operator|+
name|function
operator|+
literal|"))"
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|waitForService
argument_list|(
literal|"(osgi.command.function="
operator|+
name|command
operator|+
literal|")"
argument_list|,
name|SERVICE_TIMEOUT
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|waitForService
parameter_list|(
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|InvalidSyntaxException
throws|,
name|InterruptedException
block|{
name|ServiceTracker
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|st
init|=
operator|new
name|ServiceTracker
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|bundleContext
argument_list|,
name|bundleContext
operator|.
name|createFilter
argument_list|(
name|filter
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|st
operator|.
name|open
argument_list|()
expr_stmt|;
name|st
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|st
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*     * Explode the dictionary into a ,-delimited list of key=value pairs     */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
specifier|static
name|String
name|explode
parameter_list|(
name|Dictionary
name|dictionary
parameter_list|)
block|{
name|Enumeration
name|keys
init|=
name|dictionary
operator|.
name|keys
argument_list|()
decl_stmt|;
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s=%s"
argument_list|,
name|key
argument_list|,
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Provides an iterable collection of references, even if the original array is null      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|private
specifier|static
name|Collection
argument_list|<
name|ServiceReference
argument_list|>
name|asCollection
parameter_list|(
name|ServiceReference
index|[]
name|references
parameter_list|)
block|{
return|return
name|references
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
name|references
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|ServiceReference
operator|>
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|JMXConnector
name|getJMXConnector
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getJMXConnector
argument_list|(
literal|"karaf"
argument_list|,
literal|"karaf"
argument_list|)
return|;
block|}
specifier|public
name|JMXConnector
name|getJMXConnector
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|passWord
parameter_list|)
throws|throws
name|Exception
block|{
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:"
operator|+
name|RMI_REG_PORT
operator|+
literal|"/karaf-root"
argument_list|)
decl_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|env
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|credentials
init|=
operator|new
name|String
index|[]
block|{
name|userName
block|,
name|passWord
block|}
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"jmx.remote.credentials"
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
name|JMXConnector
name|connector
init|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
name|url
argument_list|,
name|env
argument_list|)
decl_stmt|;
return|return
name|connector
return|;
block|}
specifier|public
name|void
name|assertFeatureInstalled
parameter_list|(
name|String
name|featureName
parameter_list|)
block|{
name|Feature
index|[]
name|features
init|=
name|featureService
operator|.
name|listInstalledFeatures
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|features
control|)
block|{
if|if
condition|(
name|featureName
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
name|Assert
operator|.
name|fail
argument_list|(
literal|"Feature "
operator|+
name|featureName
operator|+
literal|" should be installed but is not"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertFeaturesInstalled
parameter_list|(
name|String
modifier|...
name|expectedFeatures
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|expectedFeaturesSet
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expectedFeatures
argument_list|)
argument_list|)
decl_stmt|;
name|Feature
index|[]
name|features
init|=
name|featureService
operator|.
name|listInstalledFeatures
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|installedFeatures
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|features
control|)
block|{
name|installedFeatures
operator|.
name|add
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|msg
init|=
literal|"Expecting the following features to be installed : "
operator|+
name|expectedFeaturesSet
operator|+
literal|" but found "
operator|+
name|installedFeatures
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|msg
argument_list|,
name|installedFeatures
operator|.
name|containsAll
argument_list|(
name|expectedFeaturesSet
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertContains
parameter_list|(
name|String
name|expectedPart
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should contain '"
operator|+
name|expectedPart
operator|+
literal|"' but was : "
operator|+
name|actual
argument_list|,
name|actual
operator|.
name|contains
argument_list|(
name|expectedPart
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertContainsNot
parameter_list|(
name|String
name|expectedPart
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"Should not contain '"
operator|+
name|expectedPart
operator|+
literal|"' but was : "
operator|+
name|actual
argument_list|,
name|actual
operator|.
name|contains
argument_list|(
name|expectedPart
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertBundleInstalled
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Bundle "
operator|+
name|name
operator|+
literal|" should be installed"
argument_list|,
name|findBundleByName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertBundleNotInstalled
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Assert
operator|.
name|assertNull
argument_list|(
literal|"Bundle "
operator|+
name|name
operator|+
literal|" should not be installed"
argument_list|,
name|findBundleByName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Bundle
name|findBundleByName
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|symbolicName
argument_list|)
condition|)
block|{
return|return
name|bundle
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|installAndAssertFeature
parameter_list|(
name|String
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
name|featureService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|installAssertAndUninstallFeature
parameter_list|(
name|String
modifier|...
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresBefore
init|=
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|featureService
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|curFeature
range|:
name|feature
control|)
block|{
name|featureService
operator|.
name|installFeature
argument_list|(
name|curFeature
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
name|curFeature
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|uninstallNewFeatures
argument_list|(
name|featuresBefore
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * The feature service does not uninstall feature dependencies when uninstalling a single feature.      * So we need to make sure we uninstall all features that were newly installed.      *      * @param featuresBefore      * @throws Exception      */
specifier|protected
name|void
name|uninstallNewFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|>
name|featuresBefore
parameter_list|)
throws|throws
name|Exception
block|{
name|Feature
index|[]
name|features
init|=
name|featureService
operator|.
name|listInstalledFeatures
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|curFeature
range|:
name|features
control|)
block|{
if|if
condition|(
operator|!
name|featuresBefore
operator|.
name|contains
argument_list|(
name|curFeature
argument_list|)
condition|)
block|{
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Uninstalling "
operator|+
name|curFeature
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|featureService
operator|.
name|uninstallFeature
argument_list|(
name|curFeature
operator|.
name|getName
argument_list|()
argument_list|,
name|curFeature
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|close
parameter_list|(
name|Closeable
name|closeAble
parameter_list|)
block|{
if|if
condition|(
name|closeAble
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|closeAble
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

