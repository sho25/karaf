begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|client
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
name|tooling
operator|.
name|utils
operator|.
name|MojoSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|agent
operator|.
name|SshAgent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|agent
operator|.
name|local
operator|.
name|AgentImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|agent
operator|.
name|local
operator|.
name|LocalAgentFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|SshClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|auth
operator|.
name|keyboard
operator|.
name|UserInteraction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|channel
operator|.
name|ClientChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|channel
operator|.
name|ClientChannelEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|future
operator|.
name|ConnectFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|session
operator|.
name|ClientSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|RuntimeSshException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|keyprovider
operator|.
name|AbstractFileKeyPairProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|SecurityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|AnsiConsole
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|management
operator|.
name|remote
operator|.
name|MBeanServerForwarder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|Console
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
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOError
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Deploy MOJO to deploy an artifact remotely on a running Karaf instance, using ssh or JMX  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"deploy"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PACKAGE
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
specifier|public
class|class
name|DeployMojo
extends|extends
name|MojoSupport
block|{
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"8101"
argument_list|)
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"localhost"
argument_list|)
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"karaf"
argument_list|)
specifier|private
name|String
name|user
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"karaf"
argument_list|)
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"karaf-root"
argument_list|)
specifier|private
name|String
name|instance
init|=
literal|"karaf-root"
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
specifier|private
name|int
name|attempts
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"2"
argument_list|)
specifier|private
name|int
name|delay
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|useSsh
init|=
literal|true
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|useProjectArtifact
init|=
literal|true
decl_stmt|;
annotation|@
name|Parameter
name|List
argument_list|<
name|String
argument_list|>
name|artifactLocations
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|File
name|keyFile
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NEW_LINE
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|useProjectArtifact
condition|)
block|{
name|Artifact
name|projectArtifact
init|=
name|project
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
literal|"mvn:"
operator|+
name|projectArtifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|"/"
operator|+
name|projectArtifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"/"
operator|+
name|projectArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|artifacts
operator|.
name|addAll
argument_list|(
name|artifactLocations
argument_list|)
expr_stmt|;
if|if
condition|(
name|useSsh
condition|)
name|deployWithSsh
argument_list|(
name|artifactLocations
argument_list|)
expr_stmt|;
else|else
name|deployWithJmx
argument_list|(
name|artifactLocations
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|deployWithJmx
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|JMXServiceURL
name|jmxServiceURL
init|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|instance
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
name|list
operator|.
name|add
argument_list|(
name|user
argument_list|)
expr_stmt|;
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
name|list
operator|.
name|add
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|HashMap
name|env
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|String
index|[]
name|credentials
init|=
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|JMXConnector
operator|.
name|CREDENTIALS
argument_list|,
name|credentials
argument_list|)
expr_stmt|;
name|JMXConnector
name|jmxConnector
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|credentials
operator|.
name|length
operator|>
literal|0
condition|)
name|jmxConnector
operator|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
name|jmxServiceURL
argument_list|,
name|env
argument_list|)
expr_stmt|;
else|else
name|jmxConnector
operator|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
name|jmxServiceURL
argument_list|)
expr_stmt|;
name|MBeanServerConnection
name|mBeanServerConnection
init|=
name|jmxConnector
operator|.
name|getMBeanServerConnection
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|location
range|:
name|locations
control|)
block|{
name|mBeanServerConnection
operator|.
name|invoke
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=bundle,name=*"
argument_list|)
argument_list|,
literal|"install"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|location
block|,
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"boolean"
block|}
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
name|MojoExecutionException
argument_list|(
literal|"Can't deploy using JMX"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|deployWithSsh
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|SshClient
name|client
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|Console
name|console
init|=
name|System
operator|.
name|console
argument_list|()
decl_stmt|;
name|client
operator|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
expr_stmt|;
name|setupAgent
argument_list|(
name|user
argument_list|,
name|keyFile
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|client
operator|.
name|setUserInteraction
argument_list|(
operator|new
name|UserInteraction
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|welcome
parameter_list|(
name|ClientSession
name|s
parameter_list|,
name|String
name|banner
parameter_list|,
name|String
name|lang
parameter_list|)
block|{
name|console
operator|.
name|printf
argument_list|(
name|banner
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|interactive
parameter_list|(
name|ClientSession
name|s
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|instruction
parameter_list|,
name|String
name|lang
parameter_list|,
name|String
index|[]
name|prompt
parameter_list|,
name|boolean
index|[]
name|echo
parameter_list|)
block|{
name|String
index|[]
name|answers
init|=
operator|new
name|String
index|[
name|prompt
operator|.
name|length
index|]
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|prompt
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|console
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|echo
index|[
name|i
index|]
condition|)
block|{
name|answers
index|[
name|i
index|]
operator|=
name|console
operator|.
name|readLine
argument_list|(
name|prompt
index|[
name|i
index|]
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answers
index|[
name|i
index|]
operator|=
operator|new
name|String
argument_list|(
name|console
operator|.
name|readPassword
argument_list|(
name|prompt
index|[
name|i
index|]
operator|+
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOError
name|e
parameter_list|)
block|{                     }
return|return
name|answers
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isInteractionAllowed
parameter_list|(
name|ClientSession
name|session
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|serverVersionInfo
parameter_list|(
name|ClientSession
name|session
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|)
block|{                 }
annotation|@
name|Override
specifier|public
name|String
name|getUpdatedPassword
parameter_list|(
name|ClientSession
name|session
parameter_list|,
name|String
name|prompt
parameter_list|,
name|String
name|lang
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|console
operator|!=
literal|null
condition|)
block|{
name|console
operator|.
name|printf
argument_list|(
literal|"Logging in as %s\n"
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
name|ClientSession
name|session
init|=
name|connect
argument_list|(
name|client
argument_list|)
decl_stmt|;
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|addPasswordIdentity
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|auth
argument_list|()
operator|.
name|verify
argument_list|()
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|print
init|=
operator|new
name|PrintWriter
argument_list|(
name|writer
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|location
range|:
name|locations
control|)
block|{
name|print
operator|.
name|println
argument_list|(
literal|"bundle:install -s "
operator|+
name|location
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ClientChannel
name|channel
init|=
name|session
operator|.
name|createChannel
argument_list|(
literal|"exec"
argument_list|,
name|print
operator|.
name|toString
argument_list|()
operator|.
name|concat
argument_list|(
name|NEW_LINE
argument_list|)
argument_list|)
decl_stmt|;
name|channel
operator|.
name|setIn
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|ByteArrayOutputStream
name|sout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|ByteArrayOutputStream
name|serr
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|channel
operator|.
name|setOut
argument_list|(
name|AnsiConsole
operator|.
name|wrapOutputStream
argument_list|(
name|sout
argument_list|)
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setErr
argument_list|(
name|AnsiConsole
operator|.
name|wrapOutputStream
argument_list|(
name|serr
argument_list|)
argument_list|)
expr_stmt|;
name|channel
operator|.
name|open
argument_list|()
expr_stmt|;
name|channel
operator|.
name|waitFor
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|ClientChannelEvent
operator|.
name|CLOSED
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|sout
operator|.
name|writeTo
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|serr
operator|.
name|writeTo
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
comment|// Expects issue KARAF-2623 is fixed
specifier|final
name|boolean
name|isError
init|=
operator|(
name|channel
operator|.
name|getExitStatus
argument_list|()
operator|!=
literal|null
operator|&&
name|channel
operator|.
name|getExitStatus
argument_list|()
operator|.
name|intValue
argument_list|()
operator|!=
literal|0
operator|)
decl_stmt|;
if|if
condition|(
name|isError
condition|)
block|{
specifier|final
name|String
name|errorMarker
init|=
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Color
operator|.
name|RED
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|int
name|fromIndex
init|=
name|sout
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|errorMarker
argument_list|)
operator|+
name|errorMarker
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|int
name|toIndex
init|=
name|sout
operator|.
name|toString
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|Ansi
operator|.
name|ansi
argument_list|()
operator|.
name|fg
argument_list|(
name|Color
operator|.
name|DEFAULT
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|NEW_LINE
operator|+
name|sout
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|t
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|t
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|setupAgent
parameter_list|(
name|String
name|user
parameter_list|,
name|File
name|keyFile
parameter_list|,
name|SshClient
name|client
parameter_list|)
block|{
name|URL
name|builtInPrivateKey
init|=
name|ClientMojo
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"karaf.key"
argument_list|)
decl_stmt|;
name|SshAgent
name|agent
init|=
name|startAgent
argument_list|(
name|user
argument_list|,
name|builtInPrivateKey
argument_list|,
name|keyFile
argument_list|)
decl_stmt|;
name|client
operator|.
name|setAgentFactory
argument_list|(
operator|new
name|LocalAgentFactory
argument_list|(
name|agent
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SshAgent
operator|.
name|SSH_AUTHSOCKET_ENV_NAME
argument_list|,
literal|"local"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SshAgent
name|startAgent
parameter_list|(
name|String
name|user
parameter_list|,
name|URL
name|privateKeyUrl
parameter_list|,
name|File
name|keyFile
parameter_list|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|privateKeyUrl
operator|.
name|openStream
argument_list|()
init|)
block|{
name|SshAgent
name|agent
init|=
operator|new
name|AgentImpl
argument_list|()
decl_stmt|;
name|ObjectInputStream
name|r
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|KeyPair
name|keyPair
init|=
operator|(
name|KeyPair
operator|)
name|r
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|agent
operator|.
name|addIdentity
argument_list|(
name|keyPair
argument_list|,
name|user
argument_list|)
expr_stmt|;
if|if
condition|(
name|keyFile
operator|!=
literal|null
condition|)
block|{
name|AbstractFileKeyPairProvider
name|fileKeyPairProvider
init|=
name|SecurityUtils
operator|.
name|createFileKeyPairProvider
argument_list|()
decl_stmt|;
name|fileKeyPairProvider
operator|.
name|setPaths
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|keyFile
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|KeyPair
name|key
range|:
name|fileKeyPairProvider
operator|.
name|loadKeys
argument_list|()
control|)
block|{
name|agent
operator|.
name|addIdentity
argument_list|(
name|key
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|agent
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Error starting ssh agent for: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|ClientSession
name|connect
parameter_list|(
name|SshClient
name|client
parameter_list|)
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
name|int
name|retries
init|=
literal|0
decl_stmt|;
name|ClientSession
name|session
init|=
literal|null
decl_stmt|;
do|do
block|{
specifier|final
name|ConnectFuture
name|future
init|=
name|client
operator|.
name|connect
argument_list|(
name|user
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|future
operator|.
name|await
argument_list|()
expr_stmt|;
try|try
block|{
name|session
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeSshException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|retries
operator|++
operator|<
name|attempts
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|toMillis
argument_list|(
name|delay
argument_list|)
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"retrying (attempt "
operator|+
name|retries
operator|+
literal|") ..."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
do|while
condition|(
name|session
operator|==
literal|null
condition|)
do|;
return|return
name|session
return|;
block|}
block|}
end_class

end_unit

