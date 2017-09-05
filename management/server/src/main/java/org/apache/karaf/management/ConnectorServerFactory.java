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
name|management
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
name|jaas
operator|.
name|config
operator|.
name|KeystoreManager
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
name|management
operator|.
name|internal
operator|.
name|MBeanInvocationHandler
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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|BindException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|NetworkInterface
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ServerSocketChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|server
operator|.
name|RMIClientSocketFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|server
operator|.
name|RMIServerSocketFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|JMXConnectorServer
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
name|JMXConnectorServerFactory
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
name|rmi
operator|.
name|RMIConnectorServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ServerSocketFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLParameters
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLServerSocket
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLServerSocketFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|rmi
operator|.
name|ssl
operator|.
name|SslRMIClientSocketFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ConnectorServerFactory
block|{
specifier|private
enum|enum
name|AuthenticatorType
block|{
name|NONE
block|,
name|PASSWORD
block|,
name|CERTIFICATE
block|}
specifier|private
name|MBeanServer
name|server
decl_stmt|;
specifier|private
name|KarafMBeanServerGuard
name|guard
decl_stmt|;
specifier|private
name|String
name|serviceUrl
decl_stmt|;
specifier|private
name|String
name|rmiServerHost
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|environment
decl_stmt|;
specifier|private
name|ObjectName
name|objectName
decl_stmt|;
specifier|private
name|boolean
name|threaded
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|daemon
init|=
literal|false
decl_stmt|;
specifier|private
name|JMXConnectorServer
name|connectorServer
decl_stmt|;
specifier|private
name|long
name|keyStoreAvailabilityTimeout
init|=
literal|5000
decl_stmt|;
specifier|private
name|AuthenticatorType
name|authenticatorType
init|=
name|AuthenticatorType
operator|.
name|PASSWORD
decl_stmt|;
specifier|private
name|boolean
name|secured
decl_stmt|;
specifier|private
name|KeystoreManager
name|keystoreManager
decl_stmt|;
specifier|private
name|String
name|algorithm
decl_stmt|;
specifier|private
name|String
name|secureProtocol
decl_stmt|;
specifier|private
name|String
name|keyStore
decl_stmt|;
specifier|private
name|String
name|trustStore
decl_stmt|;
specifier|private
name|String
name|keyAlias
decl_stmt|;
specifier|public
name|MBeanServer
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
specifier|public
name|void
name|setServer
parameter_list|(
name|MBeanServer
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
specifier|public
name|KarafMBeanServerGuard
name|getGuard
parameter_list|()
block|{
return|return
name|guard
return|;
block|}
specifier|public
name|void
name|setGuard
parameter_list|(
name|KarafMBeanServerGuard
name|guard
parameter_list|)
block|{
name|this
operator|.
name|guard
operator|=
name|guard
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceUrl
parameter_list|()
block|{
return|return
name|serviceUrl
return|;
block|}
specifier|public
name|void
name|setServiceUrl
parameter_list|(
name|String
name|serviceUrl
parameter_list|)
block|{
name|this
operator|.
name|serviceUrl
operator|=
name|serviceUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getRmiServerHost
parameter_list|()
block|{
return|return
name|this
operator|.
name|rmiServerHost
return|;
block|}
specifier|public
name|void
name|setRmiServerHost
parameter_list|(
name|String
name|rmiServerHost
parameter_list|)
block|{
name|this
operator|.
name|rmiServerHost
operator|=
name|rmiServerHost
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEnvironment
parameter_list|()
block|{
return|return
name|environment
return|;
block|}
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|environment
parameter_list|)
block|{
name|this
operator|.
name|environment
operator|=
name|environment
expr_stmt|;
block|}
specifier|public
name|ObjectName
name|getObjectName
parameter_list|()
block|{
return|return
name|objectName
return|;
block|}
specifier|public
name|void
name|setObjectName
parameter_list|(
name|ObjectName
name|objectName
parameter_list|)
block|{
name|this
operator|.
name|objectName
operator|=
name|objectName
expr_stmt|;
block|}
specifier|public
name|boolean
name|isThreaded
parameter_list|()
block|{
return|return
name|threaded
return|;
block|}
specifier|public
name|void
name|setThreaded
parameter_list|(
name|boolean
name|threaded
parameter_list|)
block|{
name|this
operator|.
name|threaded
operator|=
name|threaded
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDaemon
parameter_list|()
block|{
return|return
name|daemon
return|;
block|}
specifier|public
name|void
name|setDaemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthenticatorType
parameter_list|()
block|{
return|return
name|this
operator|.
name|authenticatorType
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
comment|/**      * Authenticator type to use. Acceptable values are "none", "password", and "certificate"      *      * @param value The authenticator type: "none", "password", "certificate".      */
specifier|public
name|void
name|setAuthenticatorType
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|authenticatorType
operator|=
name|AuthenticatorType
operator|.
name|valueOf
argument_list|(
name|value
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use this param to allow KeyStoreManager to wait for expected keystores to be loaded by other bundle      *      * @param keyStoreAvailabilityTimeout The keystore timeout.      */
specifier|public
name|void
name|setKeyStoreAvailabilityTimeout
parameter_list|(
name|long
name|keyStoreAvailabilityTimeout
parameter_list|)
block|{
name|this
operator|.
name|keyStoreAvailabilityTimeout
operator|=
name|keyStoreAvailabilityTimeout
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSecured
parameter_list|()
block|{
return|return
name|this
operator|.
name|secured
return|;
block|}
specifier|public
name|void
name|setSecured
parameter_list|(
name|boolean
name|secured
parameter_list|)
block|{
name|this
operator|.
name|secured
operator|=
name|secured
expr_stmt|;
block|}
specifier|public
name|void
name|setKeystoreManager
parameter_list|(
name|KeystoreManager
name|keystoreManager
parameter_list|)
block|{
name|this
operator|.
name|keystoreManager
operator|=
name|keystoreManager
expr_stmt|;
block|}
specifier|public
name|KeystoreManager
name|getKeystoreManager
parameter_list|()
block|{
return|return
name|this
operator|.
name|keystoreManager
return|;
block|}
specifier|public
name|String
name|getKeyStore
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyStore
return|;
block|}
specifier|public
name|void
name|setKeyStore
parameter_list|(
name|String
name|keyStore
parameter_list|)
block|{
name|this
operator|.
name|keyStore
operator|=
name|keyStore
expr_stmt|;
block|}
specifier|public
name|String
name|getTrustStore
parameter_list|()
block|{
return|return
name|this
operator|.
name|trustStore
return|;
block|}
specifier|public
name|void
name|setTrustStore
parameter_list|(
name|String
name|trustStore
parameter_list|)
block|{
name|this
operator|.
name|trustStore
operator|=
name|trustStore
expr_stmt|;
block|}
specifier|public
name|String
name|getKeyAlias
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyAlias
return|;
block|}
specifier|public
name|void
name|setKeyAlias
parameter_list|(
name|String
name|keyAlias
parameter_list|)
block|{
name|this
operator|.
name|keyAlias
operator|=
name|keyAlias
expr_stmt|;
block|}
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|this
operator|.
name|algorithm
return|;
block|}
comment|/**      * Algorithm to use.      * As different JVMs have different implementations available, the default algorithm can be used by supplying the value "Default".      *      * @param algorithm the algorithm to use, or "Default" to use the default from {@link javax.net.ssl.KeyManagerFactory#getDefaultAlgorithm()}      */
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
if|if
condition|(
literal|"default"
operator|.
name|equalsIgnoreCase
argument_list|(
name|algorithm
argument_list|)
condition|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getSecureProtocol
parameter_list|()
block|{
return|return
name|this
operator|.
name|secureProtocol
return|;
block|}
specifier|public
name|void
name|setSecureProtocol
parameter_list|(
name|String
name|secureProtocol
parameter_list|)
block|{
name|this
operator|.
name|secureProtocol
operator|=
name|secureProtocol
expr_stmt|;
block|}
specifier|private
name|boolean
name|isClientAuth
parameter_list|()
block|{
return|return
name|this
operator|.
name|authenticatorType
operator|.
name|equals
argument_list|(
name|AuthenticatorType
operator|.
name|CERTIFICATE
argument_list|)
return|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|server
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"server must be set"
argument_list|)
throw|;
block|}
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
name|this
operator|.
name|serviceUrl
argument_list|)
decl_stmt|;
if|if
condition|(
name|isClientAuth
argument_list|()
condition|)
block|{
name|this
operator|.
name|secured
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|secured
condition|)
block|{
name|setupSsl
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|setupKarafRMIServerSocketFactory
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|AuthenticatorType
operator|.
name|PASSWORD
operator|.
name|equals
argument_list|(
name|this
operator|.
name|authenticatorType
argument_list|)
condition|)
block|{
name|this
operator|.
name|environment
operator|.
name|remove
argument_list|(
literal|"jmx.remote.authenticator"
argument_list|)
expr_stmt|;
block|}
name|MBeanInvocationHandler
name|handler
init|=
operator|new
name|MBeanInvocationHandler
argument_list|(
name|server
argument_list|,
name|guard
argument_list|)
decl_stmt|;
name|MBeanServer
name|guardedServer
init|=
operator|(
name|MBeanServer
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|server
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|MBeanServer
operator|.
name|class
block|}
argument_list|,
name|handler
argument_list|)
decl_stmt|;
name|this
operator|.
name|connectorServer
operator|=
name|JMXConnectorServerFactory
operator|.
name|newJMXConnectorServer
argument_list|(
name|url
argument_list|,
name|this
operator|.
name|environment
argument_list|,
name|guardedServer
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|objectName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|server
operator|.
name|registerMBean
argument_list|(
name|this
operator|.
name|connectorServer
argument_list|,
name|this
operator|.
name|objectName
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|this
operator|.
name|threaded
condition|)
block|{
name|Thread
name|connectorThread
init|=
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|ConnectorServerFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|connectorServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|BindException
condition|)
block|{
comment|// we want just the port message
name|int
name|endIndex
init|=
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"nested exception is"
argument_list|)
decl_stmt|;
comment|// check to make sure we do not get an index out of range
if|if
condition|(
name|endIndex
operator|>
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|length
argument_list|()
operator|||
name|endIndex
operator|<
literal|0
condition|)
block|{
name|endIndex
operator|=
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"\n"
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endIndex
argument_list|)
operator|+
literal|"\nYou may have started two containers.  If you need to start a second container or the default ports are already in use "
operator|+
literal|"update the config file etc/org.apache.karaf.management.cfg and change the Registry Port and Server Port to unused ports"
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not start JMX connector server"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
argument_list|)
decl_stmt|;
name|connectorThread
operator|.
name|setName
argument_list|(
literal|"JMX Connector Thread ["
operator|+
name|this
operator|.
name|serviceUrl
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|connectorThread
operator|.
name|setDaemon
argument_list|(
name|this
operator|.
name|daemon
argument_list|)
expr_stmt|;
name|connectorThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|connectorServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|doUnregister
argument_list|(
name|this
operator|.
name|objectName
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
if|if
condition|(
name|this
operator|.
name|connectorServer
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|connectorServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|doUnregister
argument_list|(
name|this
operator|.
name|objectName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doUnregister
parameter_list|(
name|ObjectName
name|objectName
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|this
operator|.
name|objectName
operator|!=
literal|null
operator|&&
name|this
operator|.
name|server
operator|.
name|isRegistered
argument_list|(
name|objectName
argument_list|)
condition|)
block|{
name|this
operator|.
name|server
operator|.
name|unregisterMBean
argument_list|(
name|objectName
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMException
name|ex
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
specifier|private
name|void
name|setupSsl
parameter_list|()
throws|throws
name|GeneralSecurityException
block|{
name|SSLServerSocketFactory
name|sssf
init|=
name|keystoreManager
operator|.
name|createSSLServerFactory
argument_list|(
literal|null
argument_list|,
name|secureProtocol
argument_list|,
name|algorithm
argument_list|,
name|keyStore
argument_list|,
name|keyAlias
argument_list|,
name|trustStore
argument_list|,
name|keyStoreAvailabilityTimeout
argument_list|)
decl_stmt|;
name|RMIServerSocketFactory
name|rssf
init|=
operator|new
name|KarafSslRMIServerSocketFactory
argument_list|(
name|sssf
argument_list|,
name|isClientAuth
argument_list|()
argument_list|,
name|getRmiServerHost
argument_list|()
argument_list|)
decl_stmt|;
name|RMIClientSocketFactory
name|rcsf
init|=
operator|new
name|SslRMIClientSocketFactory
argument_list|()
decl_stmt|;
name|environment
operator|.
name|put
argument_list|(
name|RMIConnectorServer
operator|.
name|RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE
argument_list|,
name|rssf
argument_list|)
expr_stmt|;
name|environment
operator|.
name|put
argument_list|(
name|RMIConnectorServer
operator|.
name|RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE
argument_list|,
name|rcsf
argument_list|)
expr_stmt|;
comment|//@TODO secure RMI connector as well?
comment|//env.put("com.sun.jndi.rmi.factory.socket", rcsf);
block|}
specifier|private
name|void
name|setupKarafRMIServerSocketFactory
parameter_list|()
block|{
name|RMIServerSocketFactory
name|rssf
init|=
operator|new
name|KarafRMIServerSocketFactory
argument_list|(
name|getRmiServerHost
argument_list|()
argument_list|)
decl_stmt|;
name|environment
operator|.
name|put
argument_list|(
name|RMIConnectorServer
operator|.
name|RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE
argument_list|,
name|rssf
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|KarafSslRMIServerSocketFactory
implements|implements
name|RMIServerSocketFactory
block|{
specifier|private
name|SSLServerSocketFactory
name|sssf
decl_stmt|;
specifier|private
name|boolean
name|clientAuth
decl_stmt|;
specifier|private
name|String
name|rmiServerHost
decl_stmt|;
specifier|public
name|KarafSslRMIServerSocketFactory
parameter_list|(
name|SSLServerSocketFactory
name|sssf
parameter_list|,
name|boolean
name|clientAuth
parameter_list|,
name|String
name|rmiServerHost
parameter_list|)
block|{
name|this
operator|.
name|sssf
operator|=
name|sssf
expr_stmt|;
name|this
operator|.
name|clientAuth
operator|=
name|clientAuth
expr_stmt|;
name|this
operator|.
name|rmiServerHost
operator|=
name|rmiServerHost
expr_stmt|;
block|}
specifier|public
name|ServerSocket
name|createServerSocket
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
name|InetAddress
name|host
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|rmiServerHost
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|isLoopbackAddress
argument_list|()
condition|)
block|{
specifier|final
name|SSLServerSocket
name|ss
init|=
operator|(
name|SSLServerSocket
operator|)
name|sssf
operator|.
name|createServerSocket
argument_list|(
name|port
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|ss
operator|.
name|setNeedClientAuth
argument_list|(
name|clientAuth
argument_list|)
expr_stmt|;
return|return
operator|new
name|LocalOnlySSLServerSocket
argument_list|(
name|ss
argument_list|)
return|;
block|}
else|else
block|{
specifier|final
name|SSLServerSocket
name|ss
init|=
operator|(
name|SSLServerSocket
operator|)
name|sssf
operator|.
name|createServerSocket
argument_list|(
name|port
argument_list|,
literal|50
argument_list|,
name|InetAddress
operator|.
name|getByName
argument_list|(
name|rmiServerHost
argument_list|)
argument_list|)
decl_stmt|;
name|ss
operator|.
name|setNeedClientAuth
argument_list|(
name|clientAuth
argument_list|)
expr_stmt|;
return|return
name|ss
return|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|KarafRMIServerSocketFactory
implements|implements
name|RMIServerSocketFactory
block|{
specifier|private
name|String
name|rmiServerHost
decl_stmt|;
specifier|public
name|KarafRMIServerSocketFactory
parameter_list|(
name|String
name|rmiServerHost
parameter_list|)
block|{
name|this
operator|.
name|rmiServerHost
operator|=
name|rmiServerHost
expr_stmt|;
block|}
specifier|public
name|ServerSocket
name|createServerSocket
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
name|InetAddress
name|host
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|rmiServerHost
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|.
name|isLoopbackAddress
argument_list|()
condition|)
block|{
specifier|final
name|ServerSocket
name|ss
init|=
name|ServerSocketFactory
operator|.
name|getDefault
argument_list|()
operator|.
name|createServerSocket
argument_list|(
name|port
argument_list|,
literal|50
argument_list|)
decl_stmt|;
return|return
operator|new
name|LocalOnlyServerSocket
argument_list|(
name|ss
argument_list|)
return|;
block|}
else|else
block|{
specifier|final
name|ServerSocket
name|ss
init|=
name|ServerSocketFactory
operator|.
name|getDefault
argument_list|()
operator|.
name|createServerSocket
argument_list|(
name|port
argument_list|,
literal|50
argument_list|,
name|InetAddress
operator|.
name|getByName
argument_list|(
name|rmiServerHost
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|ss
return|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|LocalOnlyServerSocket
extends|extends
name|ServerSocket
block|{
specifier|private
specifier|final
name|ServerSocket
name|ss
decl_stmt|;
specifier|public
name|LocalOnlyServerSocket
parameter_list|(
name|ServerSocket
name|ss
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|ss
operator|=
name|ss
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bind
parameter_list|(
name|SocketAddress
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|ss
operator|.
name|bind
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bind
parameter_list|(
name|SocketAddress
name|endpoint
parameter_list|,
name|int
name|backlog
parameter_list|)
throws|throws
name|IOException
block|{
name|ss
operator|.
name|bind
argument_list|(
name|endpoint
argument_list|,
name|backlog
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InetAddress
name|getInetAddress
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getInetAddress
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|SocketAddress
name|getLocalSocketAddress
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getLocalSocketAddress
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Socket
name|accept
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|checkLocal
argument_list|(
name|ss
operator|.
name|accept
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|ss
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerSocketChannel
name|getChannel
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getChannel
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isBound
parameter_list|()
block|{
return|return
name|ss
operator|.
name|isBound
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|ss
operator|.
name|isClosed
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSoTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
throws|throws
name|SocketException
block|{
name|ss
operator|.
name|setSoTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getSoTimeout
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ss
operator|.
name|getSoTimeout
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setReuseAddress
parameter_list|(
name|boolean
name|on
parameter_list|)
throws|throws
name|SocketException
block|{
name|ss
operator|.
name|setReuseAddress
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|getReuseAddress
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|ss
operator|.
name|getReuseAddress
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ss
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|SocketException
block|{
name|ss
operator|.
name|setReceiveBufferSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getReceiveBufferSize
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|ss
operator|.
name|getReceiveBufferSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPerformancePreferences
parameter_list|(
name|int
name|connectionTime
parameter_list|,
name|int
name|latency
parameter_list|,
name|int
name|bandwidth
parameter_list|)
block|{
name|ss
operator|.
name|setPerformancePreferences
argument_list|(
name|connectionTime
argument_list|,
name|latency
argument_list|,
name|bandwidth
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|LocalOnlySSLServerSocket
extends|extends
name|SSLServerSocket
block|{
specifier|private
specifier|final
name|SSLServerSocket
name|ss
decl_stmt|;
specifier|public
name|LocalOnlySSLServerSocket
parameter_list|(
name|SSLServerSocket
name|ss
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|ss
operator|=
name|ss
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bind
parameter_list|(
name|SocketAddress
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|ss
operator|.
name|bind
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bind
parameter_list|(
name|SocketAddress
name|endpoint
parameter_list|,
name|int
name|backlog
parameter_list|)
throws|throws
name|IOException
block|{
name|ss
operator|.
name|bind
argument_list|(
name|endpoint
argument_list|,
name|backlog
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InetAddress
name|getInetAddress
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getInetAddress
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|SocketAddress
name|getLocalSocketAddress
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getLocalSocketAddress
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Socket
name|accept
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|checkLocal
argument_list|(
name|ss
operator|.
name|accept
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|ss
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerSocketChannel
name|getChannel
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getChannel
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isBound
parameter_list|()
block|{
return|return
name|ss
operator|.
name|isBound
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|ss
operator|.
name|isClosed
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSoTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
throws|throws
name|SocketException
block|{
name|ss
operator|.
name|setSoTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getSoTimeout
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ss
operator|.
name|getSoTimeout
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setReuseAddress
parameter_list|(
name|boolean
name|on
parameter_list|)
throws|throws
name|SocketException
block|{
name|ss
operator|.
name|setReuseAddress
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|getReuseAddress
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|ss
operator|.
name|getReuseAddress
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ss
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|SocketException
block|{
name|ss
operator|.
name|setReceiveBufferSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getReceiveBufferSize
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|ss
operator|.
name|getReceiveBufferSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPerformancePreferences
parameter_list|(
name|int
name|connectionTime
parameter_list|,
name|int
name|latency
parameter_list|,
name|int
name|bandwidth
parameter_list|)
block|{
name|ss
operator|.
name|setPerformancePreferences
argument_list|(
name|connectionTime
argument_list|,
name|latency
argument_list|,
name|bandwidth
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getEnabledCipherSuites
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getEnabledCipherSuites
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEnabledCipherSuites
parameter_list|(
name|String
index|[]
name|strings
parameter_list|)
block|{
name|ss
operator|.
name|setEnabledCipherSuites
argument_list|(
name|strings
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getSupportedCipherSuites
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getSupportedCipherSuites
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getSupportedProtocols
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getSupportedProtocols
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getEnabledProtocols
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getEnabledProtocols
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEnabledProtocols
parameter_list|(
name|String
index|[]
name|strings
parameter_list|)
block|{
name|ss
operator|.
name|setEnabledProtocols
argument_list|(
name|strings
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setNeedClientAuth
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|ss
operator|.
name|setNeedClientAuth
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getNeedClientAuth
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getNeedClientAuth
argument_list|()
return|;
block|}
specifier|public
name|void
name|setWantClientAuth
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|ss
operator|.
name|setWantClientAuth
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getWantClientAuth
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getWantClientAuth
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUseClientMode
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|ss
operator|.
name|setUseClientMode
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getUseClientMode
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getUseClientMode
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEnableSessionCreation
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|ss
operator|.
name|setEnableSessionCreation
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getEnableSessionCreation
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getEnableSessionCreation
argument_list|()
return|;
block|}
specifier|public
name|SSLParameters
name|getSSLParameters
parameter_list|()
block|{
return|return
name|ss
operator|.
name|getSSLParameters
argument_list|()
return|;
block|}
specifier|public
name|void
name|setSSLParameters
parameter_list|(
name|SSLParameters
name|sslParameters
parameter_list|)
block|{
name|ss
operator|.
name|setSSLParameters
argument_list|(
name|sslParameters
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|Socket
name|checkLocal
parameter_list|(
name|Socket
name|socket
parameter_list|)
throws|throws
name|IOException
block|{
name|InetAddress
name|addr
init|=
name|socket
operator|.
name|getInetAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|addr
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|addr
operator|.
name|isLoopbackAddress
argument_list|()
condition|)
block|{
return|return
name|socket
return|;
block|}
else|else
block|{
try|try
block|{
name|Enumeration
argument_list|<
name|NetworkInterface
argument_list|>
name|nis
init|=
name|NetworkInterface
operator|.
name|getNetworkInterfaces
argument_list|()
decl_stmt|;
while|while
condition|(
name|nis
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|NetworkInterface
name|ni
init|=
name|nis
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|InetAddress
argument_list|>
name|ads
init|=
name|ni
operator|.
name|getInetAddresses
argument_list|()
decl_stmt|;
while|while
condition|(
name|ads
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|InetAddress
name|ad
init|=
name|ads
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|ad
operator|.
name|equals
argument_list|(
name|addr
argument_list|)
condition|)
block|{
return|return
name|socket
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SocketException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
try|try
block|{
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Only connections from clients running on the host where the RMI remote objects have been exported are accepted."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

