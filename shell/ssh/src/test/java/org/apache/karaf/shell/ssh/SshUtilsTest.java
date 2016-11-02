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
name|IOException
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
name|sshd
operator|.
name|common
operator|.
name|cipher
operator|.
name|Cipher
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
name|kex
operator|.
name|KeyExchange
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
name|mac
operator|.
name|Mac
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
name|SshUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCiphersDefault
parameter_list|()
throws|throws
name|IOException
block|{
comment|// verify our default configuration...
name|String
name|ciphers
init|=
literal|"aes128-ctr,arcfour128,aes128-cbc,3des-cbc,blowfish-cbc"
decl_stmt|;
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Cipher
argument_list|>
argument_list|>
name|list
init|=
name|SshUtils
operator|.
name|buildCiphers
argument_list|(
name|ciphers
argument_list|)
decl_stmt|;
comment|// verify that all configured ciphers are actually resolved...
for|for
control|(
name|String
name|cipher
range|:
name|ciphers
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|NamedFactory
argument_list|<
name|Cipher
argument_list|>
name|factory
range|:
name|list
control|)
block|{
if|if
condition|(
name|factory
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|cipher
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Configured default cipher '"
operator|+
name|cipher
operator|+
literal|"' cannot be resolved"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMacsDefault
parameter_list|()
throws|throws
name|IOException
block|{
comment|// verify our default configuration...
name|String
name|macs
init|=
literal|"hmac-sha2-512,hmac-sha2-256,hmac-sha1"
decl_stmt|;
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|Mac
argument_list|>
argument_list|>
name|list
init|=
name|SshUtils
operator|.
name|buildMacs
argument_list|(
name|macs
argument_list|)
decl_stmt|;
comment|// verify that all configured HMACs are actually resolved...
for|for
control|(
name|String
name|mac
range|:
name|macs
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|NamedFactory
argument_list|<
name|Mac
argument_list|>
name|factory
range|:
name|list
control|)
block|{
if|if
condition|(
name|factory
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|mac
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Configured default HMAC '"
operator|+
name|mac
operator|+
literal|"' cannot be resolved"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testKexAlgorithmsDefault
parameter_list|()
throws|throws
name|IOException
block|{
comment|// verify our default configuration...
name|String
name|kexAlgorithms
init|=
literal|"diffie-hellman-group-exchange-sha256,ecdh-sha2-nistp521,ecdh-sha2-nistp384,ecdh-sha2-nistp256,diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1"
decl_stmt|;
name|List
argument_list|<
name|NamedFactory
argument_list|<
name|KeyExchange
argument_list|>
argument_list|>
name|list
init|=
name|SshUtils
operator|.
name|buildKexAlgorithms
argument_list|(
name|kexAlgorithms
argument_list|)
decl_stmt|;
comment|// verify that all configured key exchange algorithms are actually resolved...
for|for
control|(
name|String
name|kex
range|:
name|kexAlgorithms
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|NamedFactory
argument_list|<
name|KeyExchange
argument_list|>
name|factory
range|:
name|list
control|)
block|{
if|if
condition|(
name|factory
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|kex
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Configured default key exchange algorithm '"
operator|+
name|kex
operator|+
literal|"' cannot be resolved"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

