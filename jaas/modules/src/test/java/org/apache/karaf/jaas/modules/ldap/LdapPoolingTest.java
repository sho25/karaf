begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
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
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|InitialDirContext
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
name|SSLContext
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
name|TrustManagerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateLdapServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|annotations
operator|.
name|CreateTransport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|ApplyLdifFiles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreateDS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|annotations
operator|.
name|CreatePartition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|AbstractLdapTestUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|directory
operator|.
name|server
operator|.
name|core
operator|.
name|integ
operator|.
name|FrameworkRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|FrameworkRunner
operator|.
name|class
argument_list|)
annotation|@
name|CreateLdapServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"LDAPS"
argument_list|,
name|ssl
operator|=
literal|true
argument_list|)
block|}
argument_list|,
name|keyStore
operator|=
literal|"src/test/resources/org/apache/karaf/jaas/modules/ldap/ldaps.jks"
argument_list|,
name|certificatePassword
operator|=
literal|"123456"
argument_list|)
annotation|@
name|CreateDS
argument_list|(
name|name
operator|=
literal|"LdapPoolingTest-class"
argument_list|,
name|partitions
operator|=
block|{
annotation|@
name|CreatePartition
argument_list|(
name|name
operator|=
literal|"example"
argument_list|,
name|suffix
operator|=
literal|"dc=example,dc=com"
argument_list|)
block|}
argument_list|)
annotation|@
name|ApplyLdifFiles
argument_list|(
literal|"org/apache/karaf/jaas/modules/ldap/example.com.ldif"
argument_list|)
specifier|public
class|class
name|LdapPoolingTest
extends|extends
name|AbstractLdapTestUnit
block|{
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|keystore
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyManagerFactory
name|kmf
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|TrustManagerFactory
name|tmf
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|KeyStore
name|ks
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|ks
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/org/apache/karaf/jaas/modules/ldap/ldaps.jks"
argument_list|)
argument_list|,
literal|"123456"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|kmf
operator|.
name|init
argument_list|(
name|ks
argument_list|,
literal|"123456"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|tmf
operator|.
name|init
argument_list|(
name|ks
argument_list|)
expr_stmt|;
name|sslContext
operator|=
name|SSLContext
operator|.
name|getInstance
argument_list|(
literal|"TLSv1.2"
argument_list|)
expr_stmt|;
name|sslContext
operator|.
name|init
argument_list|(
name|kmf
operator|.
name|getKeyManagers
argument_list|()
argument_list|,
name|tmf
operator|.
name|getTrustManagers
argument_list|()
argument_list|,
operator|new
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see<a href="http://docs.oracle.com/javase/jndi/tutorial/ldap/connect/config.html">LDAP connection pool</a>      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testSSLConnectionPool
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool.maxsize"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool.protocol"
argument_list|,
literal|"ssl"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool.debug"
argument_list|,
literal|"all"
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|env
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.factory.initial"
argument_list|,
literal|"com.sun.jndi.ldap.LdapCtxFactory"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.provider.url"
argument_list|,
literal|"ldaps://localhost:"
operator|+
name|getLdapServer
argument_list|()
operator|.
name|getPortSSL
argument_list|()
operator|+
literal|"/ou=system"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.ldap.factory.socket"
argument_list|,
name|ManagedSSLSocketFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.protocol"
argument_list|,
literal|"ssl"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.principal"
argument_list|,
literal|"uid=admin,ou=system"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.credentials"
argument_list|,
literal|"secret"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.authentication"
argument_list|,
literal|"simple"
argument_list|)
expr_stmt|;
specifier|final
name|int
index|[]
name|socketsCreated
init|=
operator|new
name|int
index|[]
block|{
literal|0
block|}
decl_stmt|;
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
operator|new
name|ManagedSSLSocketFactory
argument_list|(
name|sslContext
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
name|socketsCreated
index|[
literal|0
index|]
operator|++
expr_stmt|;
return|return
name|super
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|InitialDirContext
name|context
init|=
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|socketsCreated
index|[
literal|0
index|]
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSSLConnectionWithoutPool
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool.maxsize"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool.protocol"
argument_list|,
literal|"ssl"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool.debug"
argument_list|,
literal|"all"
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|env
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"com.sun.jndi.ldap.connect.pool"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.factory.initial"
argument_list|,
literal|"com.sun.jndi.ldap.LdapCtxFactory"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.provider.url"
argument_list|,
literal|"ldaps://localhost:"
operator|+
name|getLdapServer
argument_list|()
operator|.
name|getPortSSL
argument_list|()
operator|+
literal|"/ou=system"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.ldap.factory.socket"
argument_list|,
name|ManagedSSLSocketFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.protocol"
argument_list|,
literal|"ssl"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.principal"
argument_list|,
literal|"uid=admin,ou=system"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.credentials"
argument_list|,
literal|"secret"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
literal|"java.naming.security.authentication"
argument_list|,
literal|"simple"
argument_list|)
expr_stmt|;
specifier|final
name|int
index|[]
name|socketsCreated
init|=
operator|new
name|int
index|[]
block|{
literal|0
block|}
decl_stmt|;
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
operator|new
name|ManagedSSLSocketFactory
argument_list|(
name|sslContext
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
name|socketsCreated
index|[
literal|0
index|]
operator|++
expr_stmt|;
return|return
name|super
operator|.
name|createSocket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|InitialDirContext
name|context
init|=
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
operator|new
name|InitialDirContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
name|ManagedSSLSocketFactory
operator|.
name|setSocketFactory
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|socketsCreated
index|[
literal|0
index|]
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

