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
operator|.
name|keygenerator
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|interfaces
operator|.
name|ECPrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|ECPublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPrivateCrtKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
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
name|common
operator|.
name|config
operator|.
name|keys
operator|.
name|KeyUtils
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
name|OpenSSHGeneratorKeyFileProviderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|writeSshKey
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|privateKeyTemp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|".priv"
argument_list|)
decl_stmt|;
name|privateKeyTemp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|File
name|publicKeyTemp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|".pub"
argument_list|)
decl_stmt|;
name|publicKeyTemp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|KeyPair
name|kp
init|=
operator|new
name|OpenSSHKeyPairGenerator
argument_list|(
name|KeyUtils
operator|.
name|RSA_ALGORITHM
argument_list|,
literal|1024
argument_list|)
operator|.
name|generate
argument_list|()
decl_stmt|;
operator|new
name|PemWriter
argument_list|(
name|privateKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|publicKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|)
operator|.
name|writeKeyPair
argument_list|(
name|KeyUtils
operator|.
name|RSA_ALGORITHM
argument_list|,
name|kp
argument_list|)
expr_stmt|;
comment|//File path = new File("/home/cschneider/.ssh/id_rsa");
name|OpenSSHKeyPairProvider
name|prov
init|=
operator|new
name|OpenSSHKeyPairProvider
argument_list|(
name|privateKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|publicKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|KeyUtils
operator|.
name|RSA_ALGORITHM
argument_list|,
literal|1024
argument_list|)
decl_stmt|;
name|KeyPair
name|keys
init|=
name|prov
operator|.
name|loadKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Loaded key is not RSA Key"
argument_list|,
name|keys
operator|.
name|getPrivate
argument_list|()
operator|instanceof
name|RSAPrivateCrtKey
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Loaded key is not RSA Key"
argument_list|,
name|keys
operator|.
name|getPublic
argument_list|()
operator|instanceof
name|RSAPublicKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|convertSimpleKey
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|privateKeyTemp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|".priv"
argument_list|)
decl_stmt|;
name|privateKeyTemp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|File
name|publicKeyTemp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|".pub"
argument_list|)
decl_stmt|;
name|publicKeyTemp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|SimpleGeneratorHostKeyProvider
name|simpleGenerator
init|=
operator|new
name|SimpleGeneratorHostKeyProvider
argument_list|(
name|privateKeyTemp
argument_list|)
decl_stmt|;
name|simpleGenerator
operator|.
name|setKeySize
argument_list|(
literal|2048
argument_list|)
expr_stmt|;
name|simpleGenerator
operator|.
name|setAlgorithm
argument_list|(
literal|"DSA"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|KeyPair
argument_list|>
name|keys
init|=
name|simpleGenerator
operator|.
name|loadKeys
argument_list|()
decl_stmt|;
name|KeyPair
name|simpleKeyPair
init|=
name|keys
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"DSA"
argument_list|,
name|simpleKeyPair
operator|.
name|getPrivate
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|OpenSSHKeyPairProvider
name|provider
init|=
operator|new
name|OpenSSHKeyPairProvider
argument_list|(
name|privateKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|publicKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
literal|"DSA"
argument_list|,
literal|2048
argument_list|)
decl_stmt|;
name|KeyPair
name|convertedKeyPair
init|=
name|provider
operator|.
name|loadKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"DSA"
argument_list|,
name|convertedKeyPair
operator|.
name|getPrivate
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|simpleKeyPair
operator|.
name|getPrivate
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|convertedKeyPair
operator|.
name|getPrivate
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|simpleKeyPair
operator|.
name|getPublic
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|convertedKeyPair
operator|.
name|getPublic
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
comment|//also test that the original file has been replaced
name|PKCS8Key
name|pkcs8
init|=
operator|new
name|PKCS8Key
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|privateKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|KeyPair
name|keyPair
init|=
operator|new
name|KeyPair
argument_list|(
name|pkcs8
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|pkcs8
operator|.
name|getPrivateKey
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|simpleKeyPair
operator|.
name|getPrivate
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|keyPair
operator|.
name|getPrivate
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|writeECSshKey
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|privateKeyTemp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|".priv"
argument_list|)
decl_stmt|;
name|privateKeyTemp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|File
name|publicKeyTemp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|".pub"
argument_list|)
decl_stmt|;
name|publicKeyTemp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|KeyPair
name|kp
init|=
operator|new
name|OpenSSHKeyPairGenerator
argument_list|(
name|KeyUtils
operator|.
name|EC_ALGORITHM
argument_list|,
literal|256
argument_list|)
operator|.
name|generate
argument_list|()
decl_stmt|;
operator|new
name|PemWriter
argument_list|(
name|privateKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|publicKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|)
operator|.
name|writeKeyPair
argument_list|(
name|KeyUtils
operator|.
name|EC_ALGORITHM
argument_list|,
name|kp
argument_list|)
expr_stmt|;
name|OpenSSHKeyPairProvider
name|prov
init|=
operator|new
name|OpenSSHKeyPairProvider
argument_list|(
name|privateKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|publicKeyTemp
operator|.
name|toPath
argument_list|()
argument_list|,
name|KeyUtils
operator|.
name|EC_ALGORITHM
argument_list|,
literal|256
argument_list|)
decl_stmt|;
name|KeyPair
name|keys
init|=
name|prov
operator|.
name|loadKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Loaded key is not EC Key"
argument_list|,
name|keys
operator|.
name|getPrivate
argument_list|()
operator|instanceof
name|ECPrivateKey
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Loaded key is not EC Key"
argument_list|,
name|keys
operator|.
name|getPublic
argument_list|()
operator|instanceof
name|ECPublicKey
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

