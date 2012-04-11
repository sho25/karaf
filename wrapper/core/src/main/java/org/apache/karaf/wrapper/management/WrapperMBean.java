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
name|wrapper
operator|.
name|management
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

begin_comment
comment|/**  * Describe the WrapperMBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|WrapperMBean
block|{
comment|/**      * Install the service wrapper.      *      * @throws Exception in case of installation failure.      */
name|void
name|install
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Install the service wrapper.      *      * @param name the service name.      * @param displayName the service display name.      * @param description the service description.      * @param startType the start type.      * @return the wrapper configuration (index 0) and service files (index 1).      * @throws Exception in case of installation failure.      */
name|File
index|[]
name|install
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|displayName
parameter_list|,
name|String
name|description
parameter_list|,
name|String
name|startType
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

