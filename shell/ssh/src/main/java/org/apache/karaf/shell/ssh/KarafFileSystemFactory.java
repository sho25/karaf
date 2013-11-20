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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
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
name|sshd
operator|.
name|common
operator|.
name|file
operator|.
name|FileSystemFactory
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
name|FileSystemView
import|;
end_import

begin_comment
comment|/**  * SSHd file system factory to reduce the visibility to the KARAF_BASE.  */
end_comment

begin_class
specifier|public
class|class
name|KarafFileSystemFactory
implements|implements
name|FileSystemFactory
block|{
specifier|public
name|FileSystemView
name|createFileSystemView
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
return|return
operator|new
name|KarafFileSystemView
argument_list|(
name|session
operator|.
name|getUsername
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

