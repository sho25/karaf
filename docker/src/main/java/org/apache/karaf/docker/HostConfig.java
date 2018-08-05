begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|docker
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|false
argument_list|)
specifier|public
class|class
name|HostConfig
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"Binds"
argument_list|)
specifier|private
name|String
index|[]
name|binds
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Links"
argument_list|)
specifier|private
name|String
index|[]
name|links
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Memory"
argument_list|)
specifier|private
name|long
name|memory
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"MemorySwap"
argument_list|)
specifier|private
name|long
name|memorySwap
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"LxcConf"
argument_list|)
specifier|private
name|String
index|[]
name|lxcConf
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"PortBindings"
argument_list|)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|HostPortBinding
argument_list|>
argument_list|>
name|portBindings
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"PublishAllPorts"
argument_list|)
specifier|private
name|boolean
name|publishAllPorts
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Privileged"
argument_list|)
specifier|private
name|boolean
name|privileged
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Dns"
argument_list|)
specifier|private
name|String
index|[]
name|dns
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"VolumesFrom"
argument_list|)
specifier|private
name|String
index|[]
name|volumesFrom
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"NetworkMode"
argument_list|)
specifier|private
name|String
name|networkMode
decl_stmt|;
specifier|public
name|String
index|[]
name|getBinds
parameter_list|()
block|{
return|return
name|binds
return|;
block|}
specifier|public
name|void
name|setBinds
parameter_list|(
name|String
index|[]
name|binds
parameter_list|)
block|{
name|this
operator|.
name|binds
operator|=
name|binds
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getLinks
parameter_list|()
block|{
return|return
name|links
return|;
block|}
specifier|public
name|void
name|setLinks
parameter_list|(
name|String
index|[]
name|links
parameter_list|)
block|{
name|this
operator|.
name|links
operator|=
name|links
expr_stmt|;
block|}
specifier|public
name|long
name|getMemory
parameter_list|()
block|{
return|return
name|memory
return|;
block|}
specifier|public
name|void
name|setMemory
parameter_list|(
name|long
name|memory
parameter_list|)
block|{
name|this
operator|.
name|memory
operator|=
name|memory
expr_stmt|;
block|}
specifier|public
name|long
name|getMemorySwap
parameter_list|()
block|{
return|return
name|memorySwap
return|;
block|}
specifier|public
name|void
name|setMemorySwap
parameter_list|(
name|long
name|memorySwap
parameter_list|)
block|{
name|this
operator|.
name|memorySwap
operator|=
name|memorySwap
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getLxcConf
parameter_list|()
block|{
return|return
name|lxcConf
return|;
block|}
specifier|public
name|void
name|setLxcConf
parameter_list|(
name|String
index|[]
name|lxcConf
parameter_list|)
block|{
name|this
operator|.
name|lxcConf
operator|=
name|lxcConf
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|HostPortBinding
argument_list|>
argument_list|>
name|getPortBindings
parameter_list|()
block|{
return|return
name|portBindings
return|;
block|}
specifier|public
name|void
name|setPortBindings
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|HostPortBinding
argument_list|>
argument_list|>
name|portBindings
parameter_list|)
block|{
name|this
operator|.
name|portBindings
operator|=
name|portBindings
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPublishAllPorts
parameter_list|()
block|{
return|return
name|publishAllPorts
return|;
block|}
specifier|public
name|void
name|setPublishAllPorts
parameter_list|(
name|boolean
name|publishAllPorts
parameter_list|)
block|{
name|this
operator|.
name|publishAllPorts
operator|=
name|publishAllPorts
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPrivileged
parameter_list|()
block|{
return|return
name|privileged
return|;
block|}
specifier|public
name|void
name|setPrivileged
parameter_list|(
name|boolean
name|privileged
parameter_list|)
block|{
name|this
operator|.
name|privileged
operator|=
name|privileged
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getDns
parameter_list|()
block|{
return|return
name|dns
return|;
block|}
specifier|public
name|void
name|setDns
parameter_list|(
name|String
index|[]
name|dns
parameter_list|)
block|{
name|this
operator|.
name|dns
operator|=
name|dns
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getVolumesFrom
parameter_list|()
block|{
return|return
name|volumesFrom
return|;
block|}
specifier|public
name|void
name|setVolumesFrom
parameter_list|(
name|String
index|[]
name|volumesFrom
parameter_list|)
block|{
name|this
operator|.
name|volumesFrom
operator|=
name|volumesFrom
expr_stmt|;
block|}
specifier|public
name|String
name|getNetworkMode
parameter_list|()
block|{
return|return
name|networkMode
return|;
block|}
specifier|public
name|void
name|setNetworkMode
parameter_list|(
name|String
name|networkMode
parameter_list|)
block|{
name|this
operator|.
name|networkMode
operator|=
name|networkMode
expr_stmt|;
block|}
block|}
end_class

end_unit

