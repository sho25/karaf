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
name|command
package|;
end_package

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
name|apache
operator|.
name|karaf
operator|.
name|scheduler
operator|.
name|command
operator|.
name|completers
operator|.
name|JobNameCompleter
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|*
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Reference
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"scheduler"
argument_list|,
name|name
operator|=
literal|"reschedule"
argument_list|,
name|description
operator|=
literal|"Update scheduling of an existing job"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Reschedule
implements|implements
name|Action
block|{
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"name"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"The job name"
argument_list|)
annotation|@
name|Completion
argument_list|(
name|JobNameCompleter
operator|.
name|class
argument_list|)
name|String
name|name
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--concurrent"
argument_list|,
name|description
operator|=
literal|"Should jobs run concurrently or not (defaults to false)"
argument_list|)
name|boolean
name|concurrent
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--cron"
argument_list|,
name|description
operator|=
literal|"The cron expression"
argument_list|)
name|String
name|cron
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--at"
argument_list|,
name|description
operator|=
literal|"Absolute date in ISO format (ex: 2014-05-13T13:56:45)"
argument_list|)
name|String
name|at
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--times"
argument_list|,
name|description
operator|=
literal|"Number of times this job should be executed"
argument_list|)
name|int
name|times
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--period"
argument_list|,
name|description
operator|=
literal|"Time during executions (in seconds)"
argument_list|)
name|long
name|period
decl_stmt|;
annotation|@
name|Reference
name|Scheduler
name|scheduler
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|cron
operator|!=
literal|null
operator|&&
operator|(
name|at
operator|!=
literal|null
operator|||
name|times
operator|!=
operator|-
literal|1
operator|||
name|period
operator|!=
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Both cron expression and explicit execution time can not be specified"
argument_list|)
throw|;
block|}
name|ScheduleOptions
name|options
decl_stmt|;
if|if
condition|(
name|cron
operator|!=
literal|null
condition|)
block|{
name|options
operator|=
name|scheduler
operator|.
name|EXPR
argument_list|(
name|cron
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|date
decl_stmt|;
if|if
condition|(
name|at
operator|!=
literal|null
condition|)
block|{
name|date
operator|=
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
name|at
argument_list|)
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|date
operator|=
operator|new
name|Date
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|period
operator|>
literal|0
condition|)
block|{
name|options
operator|=
name|scheduler
operator|.
name|AT
argument_list|(
name|date
argument_list|,
name|times
argument_list|,
name|period
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|options
operator|=
name|scheduler
operator|.
name|AT
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
block|}
name|options
operator|.
name|name
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|concurrent
condition|)
block|{
name|options
operator|.
name|canRunConcurrently
argument_list|(
name|concurrent
argument_list|)
expr_stmt|;
block|}
name|scheduler
operator|.
name|reschedule
argument_list|(
name|name
argument_list|,
name|options
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

