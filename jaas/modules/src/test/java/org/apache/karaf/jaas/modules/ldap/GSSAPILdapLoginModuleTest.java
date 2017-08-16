begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|lang
operator|.
name|SystemUtils
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
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|constants
operator|.
name|SupportedSaslMechanisms
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
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|entry
operator|.
name|DefaultEntry
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
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|entry
operator|.
name|Entry
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
name|api
operator|.
name|ldap
operator|.
name|model
operator|.
name|exception
operator|.
name|LdapException
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
name|api
operator|.
name|util
operator|.
name|Strings
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
name|CreateKdcServer
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
name|annotations
operator|.
name|SaslMechanism
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
name|ApplyLdifs
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
name|ContextEntry
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
name|CreateIndex
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
name|FrameworkRunner
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
name|kerberos
operator|.
name|KeyDerivationInterceptor
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
name|kerberos
operator|.
name|kdc
operator|.
name|AbstractKerberosITest
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
name|kerberos
operator|.
name|kdc
operator|.
name|KerberosTestUtils
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
name|ldap
operator|.
name|handlers
operator|.
name|sasl
operator|.
name|cramMD5
operator|.
name|CramMd5MechanismHandler
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
name|ldap
operator|.
name|handlers
operator|.
name|sasl
operator|.
name|digestMD5
operator|.
name|DigestMd5MechanismHandler
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
name|ldap
operator|.
name|handlers
operator|.
name|sasl
operator|.
name|gssapi
operator|.
name|GssapiMechanismHandler
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
name|ldap
operator|.
name|handlers
operator|.
name|sasl
operator|.
name|ntlm
operator|.
name|NtlmMechanismHandler
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
name|ldap
operator|.
name|handlers
operator|.
name|sasl
operator|.
name|plain
operator|.
name|PlainMechanismHandler
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
name|protocol
operator|.
name|shared
operator|.
name|transport
operator|.
name|TcpTransport
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
name|protocol
operator|.
name|shared
operator|.
name|transport
operator|.
name|Transport
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
name|shared
operator|.
name|kerberos
operator|.
name|codec
operator|.
name|types
operator|.
name|EncryptionType
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
name|shared
operator|.
name|kerberos
operator|.
name|crypto
operator|.
name|checksum
operator|.
name|ChecksumType
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
name|utils
operator|.
name|properties
operator|.
name|Properties
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|RolePrincipal
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|UserPrincipal
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
name|jaas
operator|.
name|modules
operator|.
name|NamePasswordCallbackHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|kerberos
operator|.
name|KerberosPrincipal
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
name|kerberos
operator|.
name|KerberosTicket
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
name|login
operator|.
name|LoginException
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|security
operator|.
name|Principal
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertFalse
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
name|assertTrue
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
name|CreateDS
argument_list|(
name|name
operator|=
literal|"GSSAPILdapLoginModuleTest-class"
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
argument_list|,
name|contextEntry
operator|=
annotation|@
name|ContextEntry
argument_list|(
name|entryLdif
operator|=
literal|"dn: dc=example,dc=com\n"
operator|+
literal|"dc: example\n"
operator|+
literal|"objectClass: top\n"
operator|+
literal|"objectClass: domain\n\n"
argument_list|)
argument_list|,
name|indexes
operator|=
block|{
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"objectClass"
argument_list|)
block|,
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"dc"
argument_list|)
block|,
annotation|@
name|CreateIndex
argument_list|(
name|attribute
operator|=
literal|"ou"
argument_list|)
block|}
argument_list|)
block|}
argument_list|,
name|additionalInterceptors
operator|=
block|{
name|KeyDerivationInterceptor
operator|.
name|class
block|}
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
literal|"LDAP"
argument_list|)
block|}
argument_list|,
name|saslHost
operator|=
literal|"localhost"
argument_list|,
name|saslPrincipal
operator|=
literal|"ldap/localhost@EXAMPLE.COM"
argument_list|,
name|saslMechanisms
operator|=
block|{
annotation|@
name|SaslMechanism
argument_list|(
name|name
operator|=
name|SupportedSaslMechanisms
operator|.
name|PLAIN
argument_list|,
name|implClass
operator|=
name|PlainMechanismHandler
operator|.
name|class
argument_list|)
block|,
annotation|@
name|SaslMechanism
argument_list|(
name|name
operator|=
name|SupportedSaslMechanisms
operator|.
name|CRAM_MD5
argument_list|,
name|implClass
operator|=
name|CramMd5MechanismHandler
operator|.
name|class
argument_list|)
block|,
annotation|@
name|SaslMechanism
argument_list|(
name|name
operator|=
name|SupportedSaslMechanisms
operator|.
name|DIGEST_MD5
argument_list|,
name|implClass
operator|=
name|DigestMd5MechanismHandler
operator|.
name|class
argument_list|)
block|,
annotation|@
name|SaslMechanism
argument_list|(
name|name
operator|=
name|SupportedSaslMechanisms
operator|.
name|GSSAPI
argument_list|,
name|implClass
operator|=
name|GssapiMechanismHandler
operator|.
name|class
argument_list|)
block|,
annotation|@
name|SaslMechanism
argument_list|(
name|name
operator|=
name|SupportedSaslMechanisms
operator|.
name|NTLM
argument_list|,
name|implClass
operator|=
name|NtlmMechanismHandler
operator|.
name|class
argument_list|)
block|,
annotation|@
name|SaslMechanism
argument_list|(
name|name
operator|=
name|SupportedSaslMechanisms
operator|.
name|GSS_SPNEGO
argument_list|,
name|implClass
operator|=
name|NtlmMechanismHandler
operator|.
name|class
argument_list|)
block|}
argument_list|)
annotation|@
name|CreateKdcServer
argument_list|(
name|transports
operator|=
block|{
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"UDP"
argument_list|,
name|port
operator|=
literal|6088
argument_list|)
block|,
annotation|@
name|CreateTransport
argument_list|(
name|protocol
operator|=
literal|"TCP"
argument_list|,
name|port
operator|=
literal|6088
argument_list|)
block|}
argument_list|)
annotation|@
name|ApplyLdifs
argument_list|(
block|{
literal|"dn: ou=users,dc=example,dc=com"
block|,
literal|"objectClass: top"
block|,
literal|"objectClass: organizationalUnit"
block|,
literal|"ou: users"
block|,
literal|"dn: ou=groups,dc=example,dc=com"
block|,
literal|"objectClass: top"
block|,
literal|"objectClass: organizationalUnit"
block|,
literal|"ou: groups"
block|,
literal|"dn: cn=admin,ou=groups,dc=example,dc=com"
block|,
literal|"objectClass: top"
block|,
literal|"objectClass: groupOfNames"
block|,
literal|"cn: admin"
block|,
literal|"member: uid=hnelson,ou=users,dc=example,dc=com"
block|}
argument_list|)
specifier|public
class|class
name|GSSAPILdapLoginModuleTest
extends|extends
name|AbstractKerberosITest
block|{
specifier|private
specifier|static
name|boolean
name|loginConfigUpdated
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// Set up a partition for EXAMPLE.COM and add user and service principals to test authentication with.
name|KerberosTestUtils
operator|.
name|fixServicePrincipalName
argument_list|(
literal|"ldap/"
operator|+
name|KerberosTestUtils
operator|.
name|getHostName
argument_list|()
operator|+
literal|"@EXAMPLE.COM"
argument_list|,
literal|null
argument_list|,
name|getLdapServer
argument_list|()
argument_list|)
expr_stmt|;
name|setupEnv
argument_list|(
name|TcpTransport
operator|.
name|class
argument_list|,
name|EncryptionType
operator|.
name|AES128_CTS_HMAC_SHA1_96
argument_list|,
name|ChecksumType
operator|.
name|HMAC_SHA1_96_AES128
argument_list|)
expr_stmt|;
name|kdcServer
operator|.
name|getConfig
argument_list|()
operator|.
name|setPaEncTimestampRequired
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
name|File
name|config
init|=
operator|new
name|File
argument_list|(
name|basedir
operator|+
literal|"/target/test-classes/org/apache/karaf/jaas/modules/ldap/gssapi.login.config"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.auth.login.config"
argument_list|,
name|config
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|updatePort
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|updatePort
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|loginConfigUpdated
condition|)
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
comment|// Read in ldap.properties and substitute in the correct port
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|basedir
operator|+
literal|"/src/test/resources/org/apache/karaf/jaas/modules/ldap/gssapi.ldap.properties"
argument_list|)
decl_stmt|;
name|FileInputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|String
name|content
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|inputStream
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"portno"
argument_list|,
literal|""
operator|+
name|getLdapServer
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|content
operator|=
name|content
operator|.
name|replaceAll
argument_list|(
literal|"address"
argument_list|,
name|KerberosTestUtils
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|f2
init|=
operator|new
name|File
argument_list|(
name|basedir
operator|+
literal|"/target/test-classes/org/apache/karaf/jaas/modules/ldap/gssapi.ldap.properties"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|outputStream
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f2
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|write
argument_list|(
name|content
argument_list|,
name|outputStream
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|loginConfigUpdated
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|LDAPCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|GSSAPILdapLoginModule
name|module
init|=
operator|new
name|GSSAPILdapLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
operator|new
name|NamePasswordCallbackHandler
argument_list|(
literal|"hnelson"
argument_list|,
literal|"secret"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|commit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|foundKrb5User
init|=
literal|false
decl_stmt|;
name|boolean
name|foundUser
init|=
literal|false
decl_stmt|;
name|boolean
name|foundRole
init|=
literal|false
decl_stmt|;
name|boolean
name|foundTicket
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Principal
name|pr
range|:
name|subject
operator|.
name|getPrincipals
argument_list|()
control|)
block|{
if|if
condition|(
name|pr
operator|instanceof
name|KerberosPrincipal
condition|)
block|{
name|assertEquals
argument_list|(
literal|"hnelson@EXAMPLE.COM"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundKrb5User
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pr
operator|instanceof
name|UserPrincipal
condition|)
block|{
name|assertEquals
argument_list|(
literal|"hnelson"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundUser
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pr
operator|instanceof
name|RolePrincipal
condition|)
block|{
name|assertEquals
argument_list|(
literal|"admin"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundRole
operator|=
literal|true
expr_stmt|;
block|}
block|}
for|for
control|(
name|Object
name|crd
range|:
name|subject
operator|.
name|getPrivateCredentials
argument_list|()
control|)
block|{
if|if
condition|(
name|crd
operator|instanceof
name|KerberosTicket
condition|)
block|{
name|assertEquals
argument_list|(
literal|"hnelson@EXAMPLE.COM"
argument_list|,
operator|(
operator|(
name|KerberosTicket
operator|)
name|crd
operator|)
operator|.
name|getClient
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"krbtgt/EXAMPLE.COM@EXAMPLE.COM"
argument_list|,
operator|(
operator|(
name|KerberosTicket
operator|)
name|crd
operator|)
operator|.
name|getServer
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|foundTicket
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Principals should contains kerberos user"
argument_list|,
name|foundKrb5User
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Principals should contains ldap user"
argument_list|,
name|foundUser
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Principals should contains ldap role"
argument_list|,
name|foundRole
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"PricatePrincipals should contains kerberos ticket"
argument_list|,
name|foundTicket
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|logout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Principals should be gone as the user has logged out"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|LoginException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUsernameFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|GSSAPILdapLoginModule
name|module
init|=
operator|new
name|GSSAPILdapLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
operator|new
name|NamePasswordCallbackHandler
argument_list|(
literal|"hnelson0"
argument_list|,
literal|"secret"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
comment|// should throw LoginException
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|LoginException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testPasswordFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|GSSAPILdapLoginModule
name|module
init|=
operator|new
name|GSSAPILdapLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
operator|new
name|NamePasswordCallbackHandler
argument_list|(
literal|"hnelson"
argument_list|,
literal|"secret0"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|LoginException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUserNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|GSSAPILdapLoginModule
name|module
init|=
operator|new
name|GSSAPILdapLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
operator|new
name|NamePasswordCallbackHandler
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|LoginException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNoRealm
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|options
init|=
name|ldapLoginModuleOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|remove
argument_list|(
name|GSSAPILdapLoginModule
operator|.
name|REALM_PROPERTY
argument_list|)
expr_stmt|;
name|GSSAPILdapLoginModule
name|module
init|=
operator|new
name|GSSAPILdapLoginModule
argument_list|()
decl_stmt|;
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|module
operator|.
name|initialize
argument_list|(
name|subject
argument_list|,
operator|new
name|NamePasswordCallbackHandler
argument_list|(
literal|"hnelson0"
argument_list|,
literal|"secret"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Precondition"
argument_list|,
literal|0
argument_list|,
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|module
operator|.
name|login
argument_list|()
argument_list|)
expr_stmt|;
comment|// should throw LoginException
block|}
specifier|protected
name|void
name|setupEnv
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Transport
argument_list|>
name|transport
parameter_list|,
name|EncryptionType
name|encryptionType
parameter_list|,
name|ChecksumType
name|checksumType
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create krb5.conf with proper encryption type
name|String
name|krb5confPath
init|=
name|createKrb5Conf
argument_list|(
name|checksumType
argument_list|,
name|encryptionType
argument_list|,
name|transport
operator|==
name|TcpTransport
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.security.krb5.conf"
argument_list|,
name|krb5confPath
argument_list|)
expr_stmt|;
comment|// change encryption type in KDC
name|kdcServer
operator|.
name|getConfig
argument_list|()
operator|.
name|setEncryptionTypes
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|encryptionType
argument_list|)
argument_list|)
expr_stmt|;
comment|// create principals
name|createPrincipal
argument_list|(
literal|"uid="
operator|+
name|USER_UID
argument_list|,
literal|"Last"
argument_list|,
literal|"admin"
argument_list|,
name|USER_UID
argument_list|,
name|USER_PASSWORD
argument_list|,
name|USER_UID
operator|+
literal|"@"
operator|+
name|REALM
argument_list|)
expr_stmt|;
name|createPrincipal
argument_list|(
literal|"uid=krbtgt"
argument_list|,
literal|"KDC Service"
argument_list|,
literal|"KDC Service"
argument_list|,
literal|"krbtgt"
argument_list|,
literal|"secret"
argument_list|,
literal|"krbtgt/"
operator|+
name|REALM
operator|+
literal|"@"
operator|+
name|REALM
argument_list|)
expr_stmt|;
name|String
name|servicePrincipal
init|=
name|LDAP_SERVICE_NAME
operator|+
literal|"/"
operator|+
name|HOSTNAME
operator|+
literal|"@"
operator|+
name|REALM
decl_stmt|;
name|createPrincipal
argument_list|(
literal|"uid=ldap"
argument_list|,
literal|"Service"
argument_list|,
literal|"LDAP Service"
argument_list|,
literal|"ldap"
argument_list|,
literal|"randall"
argument_list|,
name|servicePrincipal
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|createKrb5Conf
parameter_list|(
name|ChecksumType
name|checksumType
parameter_list|,
name|EncryptionType
name|encryptionType
parameter_list|,
name|boolean
name|isTcp
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|folder
operator|.
name|newFile
argument_list|(
literal|"krb5.conf"
argument_list|)
decl_stmt|;
name|String
name|data
init|=
literal|""
decl_stmt|;
name|data
operator|+=
literal|"[libdefaults]"
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"default_realm = "
operator|+
name|REALM
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"default_tkt_enctypes = "
operator|+
name|encryptionType
operator|.
name|getName
argument_list|()
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"default_tgs_enctypes = "
operator|+
name|encryptionType
operator|.
name|getName
argument_list|()
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"permitted_enctypes = "
operator|+
name|encryptionType
operator|.
name|getName
argument_list|()
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
comment|//        data += "default_checksum = " + checksumType.getName() + SystemUtils.LINE_SEPARATOR;
comment|//        data += "ap_req_checksum_type = " + checksumType.getName() + SystemUtils.LINE_SEPARATOR;
name|data
operator|+=
literal|"default-checksum_type = "
operator|+
name|checksumType
operator|.
name|getName
argument_list|()
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
if|if
condition|(
name|isTcp
condition|)
block|{
name|data
operator|+=
literal|"udp_preference_limit = 1"
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
block|}
name|data
operator|+=
literal|"[realms]"
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
name|REALM
operator|+
literal|" = {"
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"kdc = "
operator|+
name|HOSTNAME
operator|+
literal|":"
operator|+
name|kdcServer
operator|.
name|getTransports
argument_list|()
index|[
literal|0
index|]
operator|.
name|getPort
argument_list|()
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"}"
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"[domain_realm]"
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
literal|"."
operator|+
name|Strings
operator|.
name|lowerCaseAscii
argument_list|(
name|REALM
argument_list|)
operator|+
literal|" = "
operator|+
name|REALM
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|data
operator|+=
name|Strings
operator|.
name|lowerCaseAscii
argument_list|(
name|REALM
argument_list|)
operator|+
literal|" = "
operator|+
name|REALM
operator|+
name|SystemUtils
operator|.
name|LINE_SEPARATOR
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|file
argument_list|,
name|data
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|file
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
specifier|private
name|void
name|createPrincipal
parameter_list|(
name|String
name|rdn
parameter_list|,
name|String
name|sn
parameter_list|,
name|String
name|cn
parameter_list|,
name|String
name|uid
parameter_list|,
name|String
name|userPassword
parameter_list|,
name|String
name|principalName
parameter_list|)
throws|throws
name|LdapException
block|{
name|Entry
name|entry
init|=
operator|new
name|DefaultEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|setDn
argument_list|(
name|rdn
operator|+
literal|","
operator|+
name|USERS_DN
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"objectClass"
argument_list|,
literal|"top"
argument_list|,
literal|"person"
argument_list|,
literal|"inetOrgPerson"
argument_list|,
literal|"krb5principal"
argument_list|,
literal|"krb5kdcentry"
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"cn"
argument_list|,
name|cn
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"sn"
argument_list|,
name|sn
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"uid"
argument_list|,
name|uid
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"userPassword"
argument_list|,
name|userPassword
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"krb5PrincipalName"
argument_list|,
name|principalName
argument_list|)
expr_stmt|;
name|entry
operator|.
name|add
argument_list|(
literal|"krb5KeyVersionNumber"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Properties
name|ldapLoginModuleOptions
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|basedir
operator|+
literal|"/target/test-classes/org/apache/karaf/jaas/modules/ldap/gssapi.ldap.properties"
argument_list|)
decl_stmt|;
return|return
operator|new
name|Properties
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
end_class

end_unit

