begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|ssh
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Manager
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|CommandLoggingFilter
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|SessionFactory
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
name|shell
operator|.
name|support
operator|.
name|RegexCommandLoggingFilter
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
name|util
operator|.
name|tracker
operator|.
name|BaseActivator
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|Managed
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|RequireService
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|Services
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
name|NamedFactory
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
name|file
operator|.
name|virtualfs
operator|.
name|VirtualFileSystemFactory
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
name|server
operator|.
name|SshServer
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
name|server
operator|.
name|forward
operator|.
name|AcceptAllForwardingFilter
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
name|server
operator|.
name|keyprovider
operator|.
name|AbstractGeneratorHostKeyProvider
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
name|server
operator|.
name|keyprovider
operator|.
name|SimpleGeneratorHostKeyProvider
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
name|server
operator|.
name|scp
operator|.
name|ScpCommandFactory
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
name|server
operator|.
name|subsystem
operator|.
name|sftp
operator|.
name|SftpSubsystemFactory
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
name|service
operator|.
name|cm
operator|.
name|ManagedService
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

begin_comment
comment|/**  * Activate this bundle  */
end_comment

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
annotation|@
name|RequireService
argument_list|(
name|SessionFactory
operator|.
name|class
argument_list|)
argument_list|)
annotation|@
name|Managed
argument_list|(
literal|"org.apache.karaf.shell"
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
implements|implements
name|ManagedService
block|{
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Activator
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServiceTracker
argument_list|<
name|Session
argument_list|,
name|Session
argument_list|>
name|sessionTracker
decl_stmt|;
name|SessionFactory
name|sessionFactory
decl_stmt|;
name|SshServer
name|server
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doOpen
argument_list|()
expr_stmt|;
name|sessionTracker
operator|=
operator|new
name|ServiceTracker
argument_list|<
name|Session
argument_list|,
name|Session
argument_list|>
argument_list|(
name|bundleContext
argument_list|,
name|Session
operator|.
name|class
argument_list|,
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Session
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|Session
argument_list|>
name|reference
parameter_list|)
block|{
name|Session
name|session
init|=
name|super
operator|.
name|addingService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|KarafAgentFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|registerSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|Session
argument_list|>
name|reference
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
name|KarafAgentFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|unregisterSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|super
operator|.
name|removedService
argument_list|(
name|reference
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|sessionTracker
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doClose
parameter_list|()
block|{
name|sessionTracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|doClose
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|SessionFactory
name|sf
init|=
name|getTrackedService
argument_list|(
name|SessionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sf
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|RegexCommandLoggingFilter
name|filter
init|=
operator|new
name|RegexCommandLoggingFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|setPattern
argument_list|(
literal|"ssh (.*?)-P +([^ ]+)"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setGroup
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|CommandLoggingFilter
operator|.
name|class
argument_list|,
name|filter
argument_list|)
expr_stmt|;
name|filter
operator|=
operator|new
name|RegexCommandLoggingFilter
argument_list|()
expr_stmt|;
name|filter
operator|.
name|setPattern
argument_list|(
literal|"ssh (.*?)--password +([^ ]+)"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setGroup
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|CommandLoggingFilter
operator|.
name|class
argument_list|,
name|filter
argument_list|)
expr_stmt|;
name|sessionFactory
operator|=
name|sf
expr_stmt|;
name|sessionFactory
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|SshAction
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|bundleContext
operator|.
name|getProperty
argument_list|(
literal|"karaf.startRemoteShell"
argument_list|)
argument_list|)
condition|)
block|{
name|server
operator|=
name|createSshServer
argument_list|(
name|sessionFactory
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|.
name|registerService
argument_list|(
name|SshServer
operator|.
name|class
argument_list|,
name|server
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
return|return;
comment|// can result from bad specification.
block|}
try|try
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Exception caught while starting SSH server"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
if|if
condition|(
name|sessionFactory
operator|!=
literal|null
condition|)
block|{
name|sessionFactory
operator|.
name|getRegistry
argument_list|()
operator|.
name|getService
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
operator|.
name|unregister
argument_list|(
name|SshAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|sessionFactory
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|server
operator|.
name|stop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Exception caught while stopping SSH server"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|server
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|SshServer
name|createSshServer
parameter_list|(
name|SessionFactory
name|sessionFactory
parameter_list|)
block|{
name|int
name|sshPort
init|=
name|getInt
argument_list|(
literal|"sshPort"
argument_list|,
literal|8181
argument_list|)
decl_stmt|;
name|String
name|sshHost
init|=
name|getString
argument_list|(
literal|"sshHost"
argument_list|,
literal|"0.0.0.0"
argument_list|)
decl_stmt|;
name|long
name|sshIdleTimeout
init|=
name|getLong
argument_list|(
literal|"sshIdleTimeout"
argument_list|,
literal|1800000
argument_list|)
decl_stmt|;
name|int
name|nioWorkers
init|=
name|getInt
argument_list|(
literal|"nio-workers"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|String
name|sshRealm
init|=
name|getString
argument_list|(
literal|"sshRealm"
argument_list|,
literal|"karaf"
argument_list|)
decl_stmt|;
name|String
name|hostKey
init|=
name|getString
argument_list|(
literal|"hostKey"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
operator|+
literal|"/host.key"
argument_list|)
decl_stmt|;
name|String
name|hostKeyFormat
init|=
name|getString
argument_list|(
literal|"hostKeyFormat"
argument_list|,
literal|"simple"
argument_list|)
decl_stmt|;
name|String
index|[]
name|authMethods
init|=
name|getStringArray
argument_list|(
literal|"authMethods"
argument_list|,
literal|"keyboard-interactive,password,publickey"
argument_list|)
decl_stmt|;
name|int
name|keySize
init|=
name|getInt
argument_list|(
literal|"keySize"
argument_list|,
literal|4096
argument_list|)
decl_stmt|;
name|String
name|algorithm
init|=
name|getString
argument_list|(
literal|"algorithm"
argument_list|,
literal|"RSA"
argument_list|)
decl_stmt|;
name|String
index|[]
name|macs
init|=
name|getStringArray
argument_list|(
literal|"macs"
argument_list|,
literal|"hmac-sha2-512,hmac-sha2-256,hmac-sha1"
argument_list|)
decl_stmt|;
name|String
index|[]
name|ciphers
init|=
name|getStringArray
argument_list|(
literal|"ciphers"
argument_list|,
literal|"aes128-ctr,arcfour128,aes128-cbc,3des-cbc,blowfish-cbc"
argument_list|)
decl_stmt|;
name|String
index|[]
name|kexAlgorithms
init|=
name|getStringArray
argument_list|(
literal|"kexAlgorithms"
argument_list|,
literal|"diffie-hellman-group-exchange-sha256,ecdh-sha2-nistp521,ecdh-sha2-nistp384,ecdh-sha2-nistp256,diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1"
argument_list|)
decl_stmt|;
name|String
name|welcomeBanner
init|=
name|getString
argument_list|(
literal|"welcomeBanner"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|moduliUrl
init|=
name|getString
argument_list|(
literal|"moduli-url"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|AbstractGeneratorHostKeyProvider
name|keyPairProvider
decl_stmt|;
if|if
condition|(
literal|"simple"
operator|.
name|equalsIgnoreCase
argument_list|(
name|hostKeyFormat
argument_list|)
condition|)
block|{
name|keyPairProvider
operator|=
operator|new
name|SimpleGeneratorHostKeyProvider
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"PEM"
operator|.
name|equalsIgnoreCase
argument_list|(
name|hostKeyFormat
argument_list|)
condition|)
block|{
name|keyPairProvider
operator|=
operator|new
name|OpenSSHGeneratorFileKeyProvider
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Invalid host key format "
operator|+
name|hostKeyFormat
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|keyPairProvider
operator|.
name|setPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|hostKey
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|new
name|File
argument_list|(
name|hostKey
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// do not trash key file if there's something wrong with it.
name|keyPairProvider
operator|.
name|setOverwriteAllowed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyPairProvider
operator|.
name|setKeySize
argument_list|(
name|keySize
argument_list|)
expr_stmt|;
name|keyPairProvider
operator|.
name|setAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
name|KarafJaasAuthenticator
name|authenticator
init|=
operator|new
name|KarafJaasAuthenticator
argument_list|(
name|sshRealm
argument_list|)
decl_stmt|;
name|UserAuthFactoriesFactory
name|authFactoriesFactory
init|=
operator|new
name|UserAuthFactoriesFactory
argument_list|()
decl_stmt|;
name|authFactoriesFactory
operator|.
name|setAuthMethods
argument_list|(
name|authMethods
argument_list|)
expr_stmt|;
name|SshServer
name|server
init|=
name|SshServer
operator|.
name|setUpDefaultServer
argument_list|()
decl_stmt|;
name|server
operator|.
name|setPort
argument_list|(
name|sshPort
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHost
argument_list|(
name|sshHost
argument_list|)
expr_stmt|;
name|server
operator|.
name|setMacFactories
argument_list|(
name|SshUtils
operator|.
name|buildMacs
argument_list|(
name|macs
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|setCipherFactories
argument_list|(
name|SshUtils
operator|.
name|buildCiphers
argument_list|(
name|ciphers
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|setKeyExchangeFactories
argument_list|(
name|SshUtils
operator|.
name|buildKexAlgorithms
argument_list|(
name|kexAlgorithms
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|setShellFactory
argument_list|(
operator|new
name|ShellFactoryImpl
argument_list|(
name|sessionFactory
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|setCommandFactory
argument_list|(
operator|new
name|ScpCommandFactory
operator|.
name|Builder
argument_list|()
operator|.
name|withDelegate
argument_list|(
operator|new
name|ShellCommandFactory
argument_list|(
name|sessionFactory
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|setSubsystemFactories
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|SftpSubsystemFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|setKeyPairProvider
argument_list|(
name|keyPairProvider
argument_list|)
expr_stmt|;
name|server
operator|.
name|setPasswordAuthenticator
argument_list|(
name|authenticator
argument_list|)
expr_stmt|;
name|server
operator|.
name|setPublickeyAuthenticator
argument_list|(
name|authenticator
argument_list|)
expr_stmt|;
name|server
operator|.
name|setFileSystemFactory
argument_list|(
operator|new
name|VirtualFileSystemFactory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|setUserAuthFactories
argument_list|(
name|authFactoriesFactory
operator|.
name|getFactories
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|setAgentFactory
argument_list|(
name|KarafAgentFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|setTcpipForwardingFilter
argument_list|(
name|AcceptAllForwardingFilter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|server
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SshServer
operator|.
name|IDLE_TIMEOUT
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|sshIdleTimeout
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SshServer
operator|.
name|NIO_WORKERS
argument_list|,
operator|new
name|Integer
argument_list|(
name|nioWorkers
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|moduliUrl
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SshServer
operator|.
name|MODULI_URL
argument_list|,
name|moduliUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|welcomeBanner
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|SshServer
operator|.
name|WELCOME_BANNER
argument_list|,
name|welcomeBanner
argument_list|)
expr_stmt|;
block|}
return|return
name|server
return|;
block|}
block|}
end_class

end_unit

