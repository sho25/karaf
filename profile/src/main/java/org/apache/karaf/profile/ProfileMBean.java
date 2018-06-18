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
name|profile
package|;
end_package

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

begin_comment
comment|/**  * MBean to manipulate profiles.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProfileMBean
block|{
comment|/**      * List profile IDs with parents.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProfiles
parameter_list|()
function_decl|;
comment|/**      * Rename a profile.      */
name|void
name|rename
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|newName
parameter_list|)
function_decl|;
comment|/**      * Delete a profile.      */
name|void
name|delete
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Create a new profile.      */
name|void
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|parents
parameter_list|)
function_decl|;
comment|/**      * Copy a profile definition on another one.      */
name|void
name|copy
parameter_list|(
name|String
name|source
parameter_list|,
name|String
name|target
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

