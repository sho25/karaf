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
name|admin
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_interface
specifier|public
interface|interface
name|AdminServiceMBean
block|{
name|String
name|INSTANCE_PID
init|=
literal|"Pid"
decl_stmt|;
name|String
name|INSTANCE_NAME
init|=
literal|"Name"
decl_stmt|;
name|String
name|INSTANCE_IS_ROOT
init|=
literal|"Is Root"
decl_stmt|;
name|String
name|INSTANCE_PORT
init|=
literal|"Port"
decl_stmt|;
name|String
name|INSTANCE_STATE
init|=
literal|"State"
decl_stmt|;
name|String
name|INSTANCE_LOCATION
init|=
literal|"Location"
decl_stmt|;
name|String
name|INSTANCE_JAVAOPTS
init|=
literal|"JavaOpts"
decl_stmt|;
name|String
index|[]
name|INSTANCE
init|=
block|{
name|INSTANCE_PID
block|,
name|INSTANCE_NAME
block|,
name|INSTANCE_IS_ROOT
block|,
name|INSTANCE_PORT
block|,
name|INSTANCE_STATE
block|,
name|INSTANCE_LOCATION
block|,
name|INSTANCE_JAVAOPTS
block|}
decl_stmt|;
comment|// Operations
name|int
name|createInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|location
parameter_list|,
name|String
name|javaOpts
parameter_list|,
name|String
name|features
parameter_list|,
name|String
name|featureURLs
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|changePort
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|changeJavaOpts
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|javaopts
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|destroyInstance
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|startInstance
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|opts
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|stopInstance
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// Attributes
name|TabularData
name|getInstances
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

