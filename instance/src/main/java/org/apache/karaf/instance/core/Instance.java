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
name|instance
operator|.
name|core
package|;
end_package

begin_interface
specifier|public
interface|interface
name|Instance
block|{
name|String
name|STOPPED
init|=
literal|"Stopped"
decl_stmt|;
name|String
name|STARTING
init|=
literal|"Starting"
decl_stmt|;
name|String
name|STARTED
init|=
literal|"Started"
decl_stmt|;
name|String
name|ERROR
init|=
literal|"Error"
decl_stmt|;
name|String
name|getName
parameter_list|()
function_decl|;
name|boolean
name|isRoot
parameter_list|()
function_decl|;
name|String
name|getLocation
parameter_list|()
function_decl|;
name|int
name|getPid
parameter_list|()
function_decl|;
name|int
name|getSshPort
parameter_list|()
function_decl|;
name|void
name|changeSshPort
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|String
name|getSshHost
parameter_list|()
function_decl|;
name|int
name|getRmiRegistryPort
parameter_list|()
function_decl|;
name|void
name|changeRmiRegistryPort
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|String
name|getRmiRegistryHost
parameter_list|()
function_decl|;
name|int
name|getRmiServerPort
parameter_list|()
function_decl|;
name|void
name|changeRmiServerPort
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|String
name|getRmiServerHost
parameter_list|()
function_decl|;
name|String
name|getJavaOpts
parameter_list|()
function_decl|;
name|void
name|changeJavaOpts
parameter_list|(
name|String
name|javaOpts
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|restart
parameter_list|(
name|String
name|javaOpts
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|start
parameter_list|(
name|String
name|javaOpts
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|String
name|getState
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|void
name|changeSshHost
parameter_list|(
name|String
name|host
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

