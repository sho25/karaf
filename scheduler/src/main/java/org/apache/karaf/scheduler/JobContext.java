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
comment|/**  * The context for a {@link Job}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|JobContext
block|{
comment|/**      * Get the name of the scheduled job.      * @return The name of the job.      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Get the configuration provided when the job was scheduled.      * @return A non-null map of values.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|getConfiguration
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

