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
name|JobContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Job
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
name|JobExecutionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionException
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
comment|/**  * This component is responsible to launch a {@link org.apache.karaf.scheduler.Job}  * or {@link Runnable} in a Quartz Scheduler.  *  */
end_comment

begin_class
specifier|public
class|class
name|QuartzJobExecutor
implements|implements
name|Job
block|{
specifier|private
specifier|final
specifier|static
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|QuartzJobExecutor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)      */
specifier|public
name|void
name|execute
parameter_list|(
specifier|final
name|JobExecutionContext
name|context
parameter_list|)
throws|throws
name|JobExecutionException
block|{
specifier|final
name|KarafStdScheduler
name|scheduler
init|=
operator|(
name|KarafStdScheduler
operator|)
name|context
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
specifier|final
name|JobDataMap
name|data
init|=
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getJobDataMap
argument_list|()
decl_stmt|;
specifier|final
name|String
name|contextKey
init|=
operator|(
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getKey
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
specifier|final
name|JobDataMap
name|karafContext
init|=
operator|(
name|contextKey
operator|!=
literal|null
operator|)
condition|?
name|scheduler
operator|.
name|getStorage
argument_list|()
operator|.
name|get
argument_list|(
name|contextKey
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|Object
name|job
init|=
operator|(
name|karafContext
operator|!=
literal|null
operator|)
condition|?
name|karafContext
operator|.
name|get
argument_list|(
name|QuartzScheduler
operator|.
name|DATA_MAP_OBJECT
argument_list|)
else|:
name|context
operator|.
name|getJobInstance
argument_list|()
decl_stmt|;
specifier|final
name|Logger
name|logger
init|=
operator|(
name|karafContext
operator|!=
literal|null
operator|)
condition|?
operator|(
name|Logger
operator|)
name|karafContext
operator|.
name|get
argument_list|(
name|QuartzScheduler
operator|.
name|DATA_MAP_LOGGER
argument_list|)
else|:
name|LOGGER
decl_stmt|;
try|try
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Executing job {} with name {}"
argument_list|,
name|job
argument_list|,
name|data
operator|.
name|get
argument_list|(
name|QuartzScheduler
operator|.
name|DATA_MAP_NAME
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|job
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scheduler
operator|.
name|Job
condition|)
block|{
specifier|final
name|InternalScheduleOptions
name|options
init|=
operator|(
name|InternalScheduleOptions
operator|)
name|data
operator|.
name|get
argument_list|(
name|QuartzScheduler
operator|.
name|DATA_MAP_OPTIONS
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
operator|(
name|String
operator|)
name|data
operator|.
name|get
argument_list|(
name|QuartzScheduler
operator|.
name|DATA_MAP_NAME
argument_list|)
decl_stmt|;
specifier|final
name|JobContext
name|jobCtx
init|=
operator|new
name|JobContextImpl
argument_list|(
name|name
argument_list|,
name|options
operator|.
name|configuration
argument_list|)
decl_stmt|;
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|scheduler
operator|.
name|Job
operator|)
name|job
operator|)
operator|.
name|execute
argument_list|(
name|jobCtx
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|job
operator|instanceof
name|Runnable
condition|)
block|{
operator|(
operator|(
name|Runnable
operator|)
name|job
operator|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Scheduled job {} is neither a job nor a runnable."
argument_list|,
name|job
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
comment|// there is nothing we can do here, so we just log
name|logger
operator|.
name|error
argument_list|(
literal|"Exception during job execution of "
operator|+
name|job
operator|+
literal|" : "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
specifier|final
class|class
name|JobContextImpl
implements|implements
name|JobContext
block|{
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|configuration
decl_stmt|;
specifier|protected
specifier|final
name|String
name|name
decl_stmt|;
specifier|public
name|JobContextImpl
parameter_list|(
name|String
name|name
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|config
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|config
expr_stmt|;
block|}
comment|/**          * @see org.apache.karaf.scheduler.JobContext#getConfiguration()          */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
comment|/**          * @see org.apache.karaf.scheduler.JobContext#getName()          */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
block|}
block|}
end_class

end_unit

