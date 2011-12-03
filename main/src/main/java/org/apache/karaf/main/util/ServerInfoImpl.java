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
name|main
operator|.
name|util
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
name|net
operator|.
name|URI
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
name|info
operator|.
name|ServerInfo
import|;
end_import

begin_comment
comment|/**  * @version $Rev:$ $Date:$  */
end_comment

begin_class
specifier|public
class|class
name|ServerInfoImpl
implements|implements
name|ServerInfo
block|{
specifier|private
specifier|final
name|File
name|base
decl_stmt|;
specifier|private
specifier|final
name|File
name|home
decl_stmt|;
specifier|private
specifier|final
name|File
name|data
decl_stmt|;
specifier|private
specifier|final
name|File
name|instances
decl_stmt|;
specifier|private
specifier|final
name|String
index|[]
name|args
decl_stmt|;
specifier|public
name|ServerInfoImpl
parameter_list|(
name|String
index|[]
name|args
parameter_list|,
name|File
name|base
parameter_list|,
name|File
name|data
parameter_list|,
name|File
name|home
parameter_list|,
name|File
name|instances
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|home
operator|=
name|home
expr_stmt|;
name|this
operator|.
name|instances
operator|=
name|instances
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|getHomeDirectory
parameter_list|()
block|{
return|return
name|home
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|resolveHomePath
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
return|return
name|resolveWithBase
argument_list|(
name|home
argument_list|,
name|filename
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|resolveHome
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
return|return
name|resolveWithBase
argument_list|(
name|home
argument_list|,
name|filename
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|resolveHome
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
name|home
operator|.
name|toURI
argument_list|()
operator|.
name|resolve
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|getBaseDirectory
parameter_list|()
block|{
return|return
name|base
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|resolveBasePath
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
return|return
name|resolveWithBase
argument_list|(
name|base
argument_list|,
name|filename
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|resolveBase
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
return|return
name|resolveWithBase
argument_list|(
name|base
argument_list|,
name|filename
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URI
name|resolveBase
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
name|base
operator|.
name|toURI
argument_list|()
operator|.
name|resolve
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|getDataDirectory
parameter_list|()
block|{
return|return
name|data
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|getInstancesDirectory
parameter_list|()
block|{
return|return
name|instances
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getArgs
parameter_list|()
block|{
return|return
name|args
operator|.
name|clone
argument_list|()
return|;
block|}
specifier|private
name|File
name|resolveWithBase
parameter_list|(
name|File
name|baseDir
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
return|return
name|file
return|;
block|}
return|return
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
name|filename
argument_list|)
return|;
block|}
block|}
end_class

end_unit

