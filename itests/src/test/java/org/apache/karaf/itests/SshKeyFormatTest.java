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
name|itests
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|ByteSource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|Resources
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
name|ssl
operator|.
name|PKCS8Key
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
name|keyverifier
operator|.
name|RequiredServerKeyVerifier
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
name|net
operator|.
name|URL
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
name|editConfigurationFilePut
import|;
end_import

begin_comment
comment|/**  * Test use of PEM keys.  */
end_comment

begin_class
specifier|public
class|class
name|SshKeyFormatTest
extends|extends
name|SshCommandTestBase
block|{
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
name|File
name|keyFile
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/karaf/itests/test.pem"
argument_list|)
decl_stmt|;
return|return
name|options
argument_list|(
name|composite
argument_list|(
name|super
operator|.
name|config
argument_list|()
argument_list|)
argument_list|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.shell.cfg"
argument_list|,
literal|"hostKey"
argument_list|,
name|keyFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|,
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.shell.cfg"
argument_list|,
literal|"hostKeyFormat"
argument_list|,
literal|"PEM"
argument_list|)
argument_list|,
name|bundle
argument_list|(
literal|"mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.not-yet-commons-ssl/0.3.11_1"
argument_list|)
argument_list|,
name|bundle
argument_list|(
literal|"mvn:com.google.guava/guava/16.0.1"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|usePemKey
parameter_list|()
throws|throws
name|Exception
block|{
name|SshClient
name|client
init|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
decl_stmt|;
name|URL
name|testPemURL
init|=
name|Resources
operator|.
name|getResource
argument_list|(
name|SshKeyFormatTest
operator|.
name|class
argument_list|,
literal|"test.pem"
argument_list|)
decl_stmt|;
name|ByteSource
name|source
init|=
name|Resources
operator|.
name|asByteSource
argument_list|(
name|testPemURL
argument_list|)
decl_stmt|;
name|PKCS8Key
name|pkcs8
init|=
operator|new
name|PKCS8Key
argument_list|(
name|source
operator|.
name|openStream
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|client
operator|.
name|setServerKeyVerifier
argument_list|(
operator|new
name|RequiredServerKeyVerifier
argument_list|(
name|pkcs8
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
name|ConnectFuture
name|future
init|=
name|client
operator|.
name|connect
argument_list|(
literal|"karaf"
argument_list|,
literal|"localhost"
argument_list|,
literal|8101
argument_list|)
operator|.
name|await
argument_list|()
decl_stmt|;
name|ClientSession
name|session
init|=
name|future
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|int
name|ret
init|=
name|ClientSession
operator|.
name|WAIT_AUTH
decl_stmt|;
while|while
condition|(
operator|(
name|ret
operator|&
name|ClientSession
operator|.
name|WAIT_AUTH
operator|)
operator|!=
literal|0
condition|)
block|{
name|session
operator|.
name|addPasswordIdentity
argument_list|(
literal|"karaf"
argument_list|)
expr_stmt|;
name|session
operator|.
name|auth
argument_list|()
operator|.
name|verify
argument_list|()
expr_stmt|;
name|ret
operator|=
name|session
operator|.
name|waitFor
argument_list|(
name|ClientSession
operator|.
name|WAIT_AUTH
operator||
name|ClientSession
operator|.
name|CLOSED
operator||
name|ClientSession
operator|.
name|AUTHED
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ret
operator|&
name|ClientSession
operator|.
name|CLOSED
operator|)
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|format
argument_list|(
literal|"ret %d%n"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Could not open SSH channel"
argument_list|)
throw|;
block|}
name|session
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
