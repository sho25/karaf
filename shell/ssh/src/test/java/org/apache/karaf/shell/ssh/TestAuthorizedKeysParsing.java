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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|interfaces
operator|.
name|DSAPublicKey
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
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
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

begin_comment
comment|/**  * Test parsing an authorized_keys file.  */
end_comment

begin_class
specifier|public
class|class
name|TestAuthorizedKeysParsing
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testAuthorizedKeysParsing
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
throws|,
name|InvalidKeySpecException
throws|,
name|IOException
block|{
name|InputStream
name|is
init|=
name|TestAuthorizedKeysParsing
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/karaf/shell/ssh/authorized_keys"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|PublicKey
argument_list|,
name|KarafPublickeyAuthenticator
operator|.
name|AuthorizedKey
argument_list|>
name|keys
init|=
name|KarafPublickeyAuthenticator
operator|.
name|parseAuthorizedKeys
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|PublicKey
argument_list|,
name|KarafPublickeyAuthenticator
operator|.
name|AuthorizedKey
argument_list|>
name|e
range|:
name|keys
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|assertSame
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"ssh-dss"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getFormat
argument_list|()
argument_list|)
operator|||
literal|"ssh-rsa"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getFormat
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"ssh-dss"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getFormat
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|instanceof
name|DSAPublicKey
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dsa-test"
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"ssh-rsa"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getFormat
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|instanceof
name|RSAPublicKey
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rsa-test"
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

