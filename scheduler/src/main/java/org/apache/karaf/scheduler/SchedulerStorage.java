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
name|scheduler
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * A job storage definition. It's easily extensible to match user needs.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SchedulerStorage
block|{
comment|/**      * Retrieve a job from the store.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
specifier|final
name|Serializable
name|key
parameter_list|)
function_decl|;
comment|/**      * Add a job in the store.      */
name|void
name|put
parameter_list|(
specifier|final
name|Serializable
name|key
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Check if the job exists in the store.      */
name|boolean
name|contains
parameter_list|(
specifier|final
name|Serializable
name|key
parameter_list|)
function_decl|;
comment|/**      * Release a job from the store.      */
name|void
name|release
parameter_list|(
specifier|final
name|Serializable
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

