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
name|net
operator|.
name|InetSocketAddress
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
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPairGenerator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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

begin_class
specifier|public
class|class
name|ServerKeyVerifierImplTest
block|{
specifier|private
specifier|static
specifier|final
name|InetSocketAddress
name|LOCALHOST
init|=
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|1001
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALGORITHM
init|=
literal|"DSA"
decl_stmt|;
specifier|private
name|PublicKey
name|createPubKey
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
block|{
name|KeyPairGenerator
name|gen
init|=
name|KeyPairGenerator
operator|.
name|getInstance
argument_list|(
name|ALGORITHM
argument_list|)
decl_stmt|;
name|KeyPair
name|keyPair
init|=
name|gen
operator|.
name|generateKeyPair
argument_list|()
decl_stmt|;
return|return
name|keyPair
operator|.
name|getPublic
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewKey
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
throws|,
name|InvalidKeySpecException
block|{
name|SocketAddress
name|address
init|=
name|LOCALHOST
decl_stmt|;
name|PublicKey
name|validServerKey
init|=
name|createPubKey
argument_list|()
decl_stmt|;
name|KnownHostsManager
name|knowHostsManager
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|KnownHostsManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|knowHostsManager
operator|.
name|getKnownKey
argument_list|(
name|address
argument_list|,
name|ALGORITHM
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|knowHostsManager
operator|.
name|storeKeyForHost
argument_list|(
name|address
argument_list|,
name|validServerKey
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|knowHostsManager
argument_list|)
expr_stmt|;
name|ServerKeyVerifierImpl
name|verifier
init|=
operator|new
name|ServerKeyVerifierImpl
argument_list|(
name|knowHostsManager
argument_list|)
decl_stmt|;
name|boolean
name|verified
init|=
name|verifier
operator|.
name|verifyServerKey
argument_list|(
literal|null
argument_list|,
name|address
argument_list|,
name|validServerKey
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Key should be verified as the key is new"
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testKnownAndCorrectKey
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
throws|,
name|InvalidKeySpecException
block|{
name|SocketAddress
name|address
init|=
name|LOCALHOST
decl_stmt|;
name|PublicKey
name|validServerKey
init|=
name|createPubKey
argument_list|()
decl_stmt|;
name|KnownHostsManager
name|knowHostsManager
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|KnownHostsManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|knowHostsManager
operator|.
name|getKnownKey
argument_list|(
name|address
argument_list|,
name|ALGORITHM
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|validServerKey
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|knowHostsManager
argument_list|)
expr_stmt|;
name|ServerKeyVerifierImpl
name|verifier
init|=
operator|new
name|ServerKeyVerifierImpl
argument_list|(
name|knowHostsManager
argument_list|)
decl_stmt|;
name|boolean
name|verified
init|=
name|verifier
operator|.
name|verifyServerKey
argument_list|(
literal|null
argument_list|,
name|address
argument_list|,
name|validServerKey
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Key should be verified as the key is known and matches the key we verify"
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testKnownAndIncorrectKey
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
throws|,
name|InvalidKeySpecException
block|{
name|SocketAddress
name|address
init|=
name|LOCALHOST
decl_stmt|;
name|PublicKey
name|validServerKey
init|=
name|createPubKey
argument_list|()
decl_stmt|;
name|PublicKey
name|otherServerKey
init|=
name|createPubKey
argument_list|()
decl_stmt|;
name|KnownHostsManager
name|knowHostsManager
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|KnownHostsManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|knowHostsManager
operator|.
name|getKnownKey
argument_list|(
name|address
argument_list|,
name|ALGORITHM
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|otherServerKey
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|knowHostsManager
argument_list|)
expr_stmt|;
name|ServerKeyVerifierImpl
name|verifier
init|=
operator|new
name|ServerKeyVerifierImpl
argument_list|(
name|knowHostsManager
argument_list|)
decl_stmt|;
name|boolean
name|verified
init|=
name|verifier
operator|.
name|verifyServerKey
argument_list|(
literal|null
argument_list|,
name|address
argument_list|,
name|validServerKey
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"Key should not be verified as the key is known and does not match the key we verify"
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

