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
name|felix
operator|.
name|karaf
operator|.
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|server
operator|.
name|UnicastRemoteObject
import|;
end_import

begin_comment
comment|/**  *   * @author gnodet  */
end_comment

begin_class
specifier|public
class|class
name|RmiRegistryFactory
block|{
specifier|private
name|int
name|port
init|=
name|Registry
operator|.
name|REGISTRY_PORT
decl_stmt|;
specifier|private
name|Registry
name|registry
decl_stmt|;
specifier|private
name|boolean
name|locate
decl_stmt|;
specifier|private
name|boolean
name|create
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|locallyCreated
decl_stmt|;
comment|/**      * @return the create      */
specifier|public
name|boolean
name|isCreate
parameter_list|()
block|{
return|return
name|create
return|;
block|}
comment|/**      * @param create the create to set      */
specifier|public
name|void
name|setCreate
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
name|this
operator|.
name|create
operator|=
name|create
expr_stmt|;
block|}
comment|/**      * @return the locate      */
specifier|public
name|boolean
name|isLocate
parameter_list|()
block|{
return|return
name|locate
return|;
block|}
comment|/**      * @param locate the locate to set      */
specifier|public
name|void
name|setLocate
parameter_list|(
name|boolean
name|locate
parameter_list|)
block|{
name|this
operator|.
name|locate
operator|=
name|locate
expr_stmt|;
block|}
comment|/**      * @return the port      */
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * @param port the port to set      */
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|registry
return|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|RemoteException
block|{
if|if
condition|(
name|registry
operator|==
literal|null
operator|&&
name|locate
condition|)
block|{
try|try
block|{
name|Registry
name|reg
init|=
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|reg
operator|.
name|list
argument_list|()
expr_stmt|;
name|registry
operator|=
name|reg
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RemoteException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
name|registry
operator|==
literal|null
operator|&&
name|create
condition|)
block|{
name|registry
operator|=
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|locallyCreated
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|RemoteException
block|{
if|if
condition|(
name|registry
operator|!=
literal|null
operator|&&
name|locallyCreated
condition|)
block|{
name|Registry
name|reg
init|=
name|registry
decl_stmt|;
name|registry
operator|=
literal|null
expr_stmt|;
name|UnicastRemoteObject
operator|.
name|unexportObject
argument_list|(
name|reg
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

