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
name|util
operator|.
name|Date
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
comment|/**  * A scheduler to schedule time/cron based jobs.  * A job is an object that is executed/fired by the scheduler. The object  * should either implement the {@link Job} interface or the {@link Runnable}  * interface.  *  * A job can be scheduled either by creating a {@link ScheduleOptions} instance  * through one of the scheduler methods and then calling {@link #schedule(Object, ScheduleOptions)}  * or  * by using the whiteboard pattern and registering a Runnable service with either  * the {@link #PROPERTY_SCHEDULER_EXPRESSION} or {@link #PROPERTY_SCHEDULER_PERIOD}  * property. Services registered by the whiteboard pattern can by default run concurrently,  * which usually is not wanted. Therefore it is advisable to also set the  * {@link #PROPERTY_SCHEDULER_CONCURRENT} property with Boolean.FALSE.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Scheduler
block|{
comment|/**      * Name of the configuration property to define the period for a job.      * The period is expressed in seconds.      * This property needs to be of type Long.      */
name|String
name|PROPERTY_SCHEDULER_PERIOD
init|=
literal|"scheduler.period"
decl_stmt|;
comment|/**      * Name of the configuration property to define if a periodically job should be scheduled immediate.      * Default is to not startup immediate, the job is started the first time after the period has expired.      * This property needs to be of type Boolean.      */
name|String
name|PROPERTY_SCHEDULER_IMMEDIATE
init|=
literal|"scheduler.immediate"
decl_stmt|;
comment|/** Name of the configuration property to define the cron expression for a job. */
name|String
name|PROPERTY_SCHEDULER_EXPRESSION
init|=
literal|"scheduler.expression"
decl_stmt|;
comment|/** Name of the configuration property to define if the job can be run concurrently. */
name|String
name|PROPERTY_SCHEDULER_CONCURRENT
init|=
literal|"scheduler.concurrent"
decl_stmt|;
comment|/** Name of the configuration property to define the job name. */
name|String
name|PROPERTY_SCHEDULER_NAME
init|=
literal|"scheduler.name"
decl_stmt|;
comment|/**      * Schedule a job based on the options.      *      * Note that if a job with the same name has already been added, the old job is cancelled and this new job replaces      * the old job.      *      * The job object needs either to be a {@link Job} or a {@link Runnable}. The options have to be created      * by one of the provided methods from this scheduler.      *      * @param job The job to execute (either {@link Job} or {@link Runnable}).      * @param options Required options defining how to schedule the job.      * @throws SchedulerError if the job can't be scheduled.      * @throws IllegalArgumentException If the preconditions are not met.      * @see #NOW()      * @see #NOW(int, long)      * @see #AT(Date)      * @see #AT(Date, int, long)      * @see #EXPR(String)      */
name|void
name|schedule
parameter_list|(
name|Object
name|job
parameter_list|,
name|ScheduleOptions
name|options
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|SchedulerError
function_decl|;
comment|/**      * Remove a scheduled job by name.      *      * @param jobName The name of the job.      * @return<code>True</code> if the job existed and could be stopped,<code>false</code> otherwise.      */
name|boolean
name|unschedule
parameter_list|(
name|String
name|jobName
parameter_list|)
function_decl|;
name|Map
argument_list|<
name|Object
argument_list|,
name|ScheduleOptions
argument_list|>
name|getJobs
parameter_list|()
throws|throws
name|SchedulerError
function_decl|;
comment|/**      * Create a schedule options to fire a job immediately and only once.      *      * @return The corresponding {@link ScheduleOptions}.      */
name|ScheduleOptions
name|NOW
parameter_list|()
function_decl|;
comment|/**      * Create a schedule options to fire a job immediately more than once.      * @param times The number of times this job should be started (must be higher than 1 or -1 for endless).      * @param period Every period seconds this job is started (must be at higher than 0).      * @return The corresponding {@link ScheduleOptions}.      */
name|ScheduleOptions
name|NOW
parameter_list|(
name|int
name|times
parameter_list|,
name|long
name|period
parameter_list|)
function_decl|;
comment|/**      * Create a schedule options to fire a job once at a specific date.      *      * @param date The date this job should be run.      * @return The corresponding {@link ScheduleOptions}.      */
name|ScheduleOptions
name|AT
parameter_list|(
specifier|final
name|Date
name|date
parameter_list|)
function_decl|;
comment|/**      * Create a schedule options to fire a job period starting at a specific date.      *      * @param date The date this job should be run.      * @param times The number of times this job should be started (must be higher than 1 or -1 for endless).      * @param period Every period seconds this job is started (must be at higher than 0).      * @return The corresponding {@link ScheduleOptions}.      */
name|ScheduleOptions
name|AT
parameter_list|(
specifier|final
name|Date
name|date
parameter_list|,
name|int
name|times
parameter_list|,
name|long
name|period
parameter_list|)
function_decl|;
comment|/**      * Create a schedule options to schedule the job based on the expression.      *      * @param expression The cron exception.      * @return The corresponding {@link ScheduleOptions}.      */
name|ScheduleOptions
name|EXPR
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

