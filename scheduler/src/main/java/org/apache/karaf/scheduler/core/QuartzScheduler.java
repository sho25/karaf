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
operator|.
name|core
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scheduler
operator|.
name|Job
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scheduler
operator|.
name|ScheduleOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scheduler
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Trigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|impl
operator|.
name|DirectSchedulerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|impl
operator|.
name|matchers
operator|.
name|GroupMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|simpl
operator|.
name|RAMJobStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|spi
operator|.
name|ThreadPool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The quartz based implementation of the scheduler.  *  */
end_comment

begin_class
specifier|public
class|class
name|QuartzScheduler
implements|implements
name|Scheduler
block|{
comment|/** Default logger. */
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"Apache Karaf Quartz Scheduler "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|QUARTZ_SCHEDULER_NAME
init|=
literal|"ApacheKaraf"
decl_stmt|;
comment|/** Map key for the job object */
specifier|static
specifier|final
name|String
name|DATA_MAP_OBJECT
init|=
literal|"QuartzJobScheduler.Object"
decl_stmt|;
comment|/** Map key for the job name */
specifier|static
specifier|final
name|String
name|DATA_MAP_NAME
init|=
literal|"QuartzJobScheduler.JobName"
decl_stmt|;
comment|/** Map key for the scheduling options. */
specifier|static
specifier|final
name|String
name|DATA_MAP_OPTIONS
init|=
literal|"QuartzJobScheduler.Options"
decl_stmt|;
comment|/** Map key for the logger. */
specifier|static
specifier|final
name|String
name|DATA_MAP_LOGGER
init|=
literal|"QuartzJobScheduler.Logger"
decl_stmt|;
comment|/** The quartz scheduler. */
specifier|private
specifier|volatile
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|scheduler
decl_stmt|;
specifier|public
name|QuartzScheduler
parameter_list|(
name|ThreadPool
name|threadPool
parameter_list|)
throws|throws
name|SchedulerException
block|{
comment|// SLING-2261 Prevent Quartz from checking for updates
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.terracotta.quartz.skipUpdateCheck"
argument_list|,
name|Boolean
operator|.
name|TRUE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|DirectSchedulerFactory
name|factory
init|=
name|DirectSchedulerFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
comment|// unique run id
specifier|final
name|String
name|runID
init|=
operator|new
name|Date
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|' '
argument_list|,
literal|'_'
argument_list|)
decl_stmt|;
name|factory
operator|.
name|createScheduler
argument_list|(
name|QUARTZ_SCHEDULER_NAME
argument_list|,
name|runID
argument_list|,
name|threadPool
argument_list|,
operator|new
name|RAMJobStore
argument_list|()
argument_list|)
expr_stmt|;
comment|// quartz does not provide a way to get the scheduler by name AND runID, so we have to iterate!
specifier|final
name|Iterator
argument_list|<
name|org
operator|.
name|quartz
operator|.
name|Scheduler
argument_list|>
name|allSchedulersIter
init|=
name|factory
operator|.
name|getAllSchedulers
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|scheduler
operator|==
literal|null
operator|&&
name|allSchedulersIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|current
init|=
name|allSchedulersIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|QUARTZ_SCHEDULER_NAME
operator|.
name|equals
argument_list|(
name|current
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
operator|&&
name|runID
operator|.
name|equals
argument_list|(
name|current
operator|.
name|getSchedulerInstanceId
argument_list|()
argument_list|)
condition|)
block|{
name|scheduler
operator|=
name|current
expr_stmt|;
block|}
block|}
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Unable to find new scheduler with name "
operator|+
name|QUARTZ_SCHEDULER_NAME
operator|+
literal|" and run ID "
operator|+
name|runID
argument_list|)
throw|;
block|}
name|scheduler
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
name|PREFIX
operator|+
literal|"started."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Deactivate this component.      * Stop the scheduler.      */
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
specifier|final
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|s
init|=
name|this
operator|.
name|scheduler
decl_stmt|;
name|this
operator|.
name|scheduler
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|dispose
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**      * Dispose the quartz scheduler      * @param s The scheduler.      */
specifier|private
name|void
name|dispose
parameter_list|(
specifier|final
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|s
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Exception during shutdown of scheduler."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
name|PREFIX
operator|+
literal|"stopped."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Initialize the data map for the job executor.      */
specifier|private
name|JobDataMap
name|initDataMap
parameter_list|(
specifier|final
name|String
name|jobName
parameter_list|,
specifier|final
name|Object
name|job
parameter_list|,
specifier|final
name|InternalScheduleOptions
name|options
parameter_list|)
block|{
specifier|final
name|JobDataMap
name|jobDataMap
init|=
operator|new
name|JobDataMap
argument_list|()
decl_stmt|;
name|jobDataMap
operator|.
name|put
argument_list|(
name|DATA_MAP_OBJECT
argument_list|,
name|job
argument_list|)
expr_stmt|;
name|jobDataMap
operator|.
name|put
argument_list|(
name|DATA_MAP_NAME
argument_list|,
name|jobName
argument_list|)
expr_stmt|;
name|jobDataMap
operator|.
name|put
argument_list|(
name|DATA_MAP_LOGGER
argument_list|,
name|this
operator|.
name|logger
argument_list|)
expr_stmt|;
name|jobDataMap
operator|.
name|put
argument_list|(
name|DATA_MAP_OPTIONS
argument_list|,
name|options
argument_list|)
expr_stmt|;
return|return
name|jobDataMap
return|;
block|}
comment|/**      * Create the job detail.      */
specifier|private
name|JobDetail
name|createJobDetail
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|JobDataMap
name|jobDataMap
parameter_list|,
specifier|final
name|boolean
name|concurrent
parameter_list|)
block|{
return|return
name|JobBuilder
operator|.
name|newJob
argument_list|(
operator|(
name|concurrent
condition|?
name|QuartzJobExecutor
operator|.
name|class
else|:
name|NonParallelQuartzJobExecutor
operator|.
name|class
operator|)
argument_list|)
operator|.
name|withIdentity
argument_list|(
name|name
argument_list|)
operator|.
name|usingJobData
argument_list|(
name|jobDataMap
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Check the job object, either runnable or job is allowed      */
specifier|private
name|void
name|checkJob
parameter_list|(
specifier|final
name|Object
name|job
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
operator|!
operator|(
name|job
operator|instanceof
name|Runnable
operator|)
operator|&&
operator|!
operator|(
name|job
operator|instanceof
name|Job
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Job object is neither an instance of "
operator|+
name|Runnable
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" nor "
operator|+
name|Job
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/** Used by the web console plugin. */
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|getScheduler
parameter_list|()
block|{
return|return
name|this
operator|.
name|scheduler
return|;
block|}
comment|/**      * @see org.apache.karaf.scheduler.Scheduler#NOW()      */
specifier|public
name|ScheduleOptions
name|NOW
parameter_list|()
block|{
return|return
name|AT
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.karaf.scheduler.Scheduler#NOW(int, long)      */
specifier|public
name|ScheduleOptions
name|NOW
parameter_list|(
name|int
name|times
parameter_list|,
name|long
name|period
parameter_list|)
block|{
return|return
name|AT
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|,
name|times
argument_list|,
name|period
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.karaf.scheduler.Scheduler#AT(java.util.Date)      */
specifier|public
name|ScheduleOptions
name|AT
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
return|return
operator|new
name|InternalScheduleOptions
argument_list|(
name|date
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.karaf.scheduler.Scheduler#AT(java.util.Date, int, long)      */
specifier|public
name|ScheduleOptions
name|AT
parameter_list|(
name|Date
name|date
parameter_list|,
name|int
name|times
parameter_list|,
name|long
name|period
parameter_list|)
block|{
return|return
operator|new
name|InternalScheduleOptions
argument_list|(
name|date
argument_list|,
name|times
argument_list|,
name|period
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.karaf.scheduler.Scheduler#EXPR(java.lang.String)      */
specifier|public
name|ScheduleOptions
name|EXPR
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|InternalScheduleOptions
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Schedule a job      * @see org.apache.karaf.scheduler.Scheduler#schedule(java.lang.Object, org.apache.karaf.scheduler.ScheduleOptions)      * @throws SchedulerException if the job can't be scheduled      * @throws IllegalArgumentException If the preconditions are not met      */
specifier|public
name|void
name|schedule
parameter_list|(
specifier|final
name|Object
name|job
parameter_list|,
specifier|final
name|ScheduleOptions
name|options
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|SchedulerException
block|{
name|this
operator|.
name|checkJob
argument_list|(
name|job
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|options
operator|instanceof
name|InternalScheduleOptions
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Options has not been created via schedule or is null."
argument_list|)
throw|;
block|}
specifier|final
name|InternalScheduleOptions
name|opts
init|=
operator|(
name|InternalScheduleOptions
operator|)
name|options
decl_stmt|;
if|if
condition|(
name|opts
operator|.
name|argumentException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|opts
operator|.
name|argumentException
throw|;
block|}
comment|// as this method might be called from unbind and during
comment|// unbind a deactivate could happen, we check the scheduler first
specifier|final
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|s
init|=
name|this
operator|.
name|scheduler
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Scheduler is not available anymore."
argument_list|)
throw|;
block|}
specifier|final
name|String
name|name
decl_stmt|;
if|if
condition|(
name|opts
operator|.
name|name
operator|!=
literal|null
condition|)
block|{
comment|// if there is already a job with the name, remove it first
try|try
block|{
specifier|final
name|JobKey
name|key
init|=
name|JobKey
operator|.
name|jobKey
argument_list|(
name|opts
operator|.
name|name
argument_list|)
decl_stmt|;
specifier|final
name|JobDetail
name|jobdetail
init|=
name|s
operator|.
name|getJobDetail
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|jobdetail
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|deleteJob
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Unscheduling job with name {}"
argument_list|,
name|opts
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|SchedulerException
name|ignored
parameter_list|)
block|{
comment|// ignore
block|}
name|name
operator|=
name|opts
operator|.
name|name
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|job
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|':'
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
expr_stmt|;
name|opts
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|final
name|Trigger
name|trigger
init|=
name|opts
operator|.
name|trigger
operator|.
name|withIdentity
argument_list|(
name|name
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// create the data map
specifier|final
name|JobDataMap
name|jobDataMap
init|=
name|this
operator|.
name|initDataMap
argument_list|(
name|name
argument_list|,
name|job
argument_list|,
name|opts
argument_list|)
decl_stmt|;
specifier|final
name|JobDetail
name|detail
init|=
name|this
operator|.
name|createJobDetail
argument_list|(
name|name
argument_list|,
name|jobDataMap
argument_list|,
name|opts
operator|.
name|canRunConcurrently
argument_list|)
decl_stmt|;
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Scheduling job {} with name {} and trigger {}"
argument_list|,
name|job
argument_list|,
name|name
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
name|s
operator|.
name|scheduleJob
argument_list|(
name|detail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.karaf.scheduler.Scheduler#unschedule(java.lang.String)      */
specifier|public
name|boolean
name|unschedule
parameter_list|(
specifier|final
name|String
name|jobName
parameter_list|)
block|{
specifier|final
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|s
init|=
name|this
operator|.
name|scheduler
decl_stmt|;
if|if
condition|(
name|jobName
operator|!=
literal|null
operator|&&
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
specifier|final
name|JobKey
name|key
init|=
name|JobKey
operator|.
name|jobKey
argument_list|(
name|jobName
argument_list|)
decl_stmt|;
specifier|final
name|JobDetail
name|jobdetail
init|=
name|s
operator|.
name|getJobDetail
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|jobdetail
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|deleteJob
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
literal|"Unscheduling job with name {}"
argument_list|,
name|jobName
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|SchedulerException
name|ignored
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|ScheduleOptions
argument_list|>
name|getJobs
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|ScheduleOptions
argument_list|>
name|jobs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|org
operator|.
name|quartz
operator|.
name|Scheduler
name|s
init|=
name|this
operator|.
name|scheduler
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|group
range|:
name|s
operator|.
name|getJobGroupNames
argument_list|()
control|)
block|{
for|for
control|(
name|JobKey
name|key
range|:
name|s
operator|.
name|getJobKeys
argument_list|(
name|GroupMatcher
operator|.
name|jobGroupEquals
argument_list|(
name|group
argument_list|)
argument_list|)
control|)
block|{
name|JobDetail
name|detail
init|=
name|s
operator|.
name|getJobDetail
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|ScheduleOptions
name|options
init|=
operator|(
name|ScheduleOptions
operator|)
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|DATA_MAP_OPTIONS
argument_list|)
decl_stmt|;
name|Object
name|job
init|=
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|DATA_MAP_OBJECT
argument_list|)
decl_stmt|;
name|jobs
operator|.
name|put
argument_list|(
name|job
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|jobs
return|;
block|}
block|}
end_class

end_unit

