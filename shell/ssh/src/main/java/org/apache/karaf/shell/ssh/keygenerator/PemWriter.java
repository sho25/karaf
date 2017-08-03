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
name|io
operator|.
name|FileNotFoundException
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
name|Collection
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
name|PEMItem
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
name|PEMUtil
import|;
end_import

begin_class
specifier|public
class|class
name|PemWriter
block|{
specifier|private
name|File
name|keyFile
decl_stmt|;
specifier|public
name|PemWriter
parameter_list|(
name|File
name|keyFile
parameter_list|)
block|{
name|this
operator|.
name|keyFile
operator|=
name|keyFile
expr_stmt|;
block|}
specifier|public
name|void
name|writeKeyPair
parameter_list|(
name|String
name|resource
parameter_list|,
name|KeyPair
name|kp
parameter_list|)
throws|throws
name|IOException
throws|,
name|FileNotFoundException
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|items
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|items
operator|.
name|add
argument_list|(
operator|new
name|PEMItem
argument_list|(
name|kp
operator|.
name|getPrivate
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|,
literal|"PRIVATE KEY"
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|PEMUtil
operator|.
name|encode
argument_list|(
name|items
argument_list|)
decl_stmt|;
name|FileOutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|keyFile
argument_list|)
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

