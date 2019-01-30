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
comment|/**  * Scheduler options provide an extensible way of defining how to schedule a job.  * An option can be created via the scheduler.  *  * @since 2.3  */
end_comment

begin_interface
specifier|public
interface|interface
name|ScheduleOptions
extends|extends
name|Serializable
block|{
comment|/**      * Add optional configuration for the job.      *      * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.      * @return The {@code ScheduleOptions}.      */
name|ScheduleOptions
name|config
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|config
parameter_list|)
function_decl|;
comment|/**      * Sets the name of the job.      * A job only needs a name if it is scheduled and should be cancelled later on. The name can then be used to cancel the job.      * If a second job with the same name is started, the second one replaces the first one.      *      * @param name The job name.      * @return The {@code ScheduleOptions}.      */
name|ScheduleOptions
name|name
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Flag indicating whether the job can be run concurrently.      * This defaults to false.      *      * @param flag Whether this job can run even if previous scheduled runs are still running.      * @return The {@code ScheduleOptions}.      */
name|ScheduleOptions
name|canRunConcurrently
parameter_list|(
specifier|final
name|boolean
name|flag
parameter_list|)
function_decl|;
name|String
name|name
parameter_list|()
function_decl|;
name|boolean
name|canRunConcurrently
parameter_list|()
function_decl|;
name|String
name|schedule
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

