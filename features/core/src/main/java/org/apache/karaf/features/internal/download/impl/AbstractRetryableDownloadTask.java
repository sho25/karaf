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
name|features
operator|.
name|internal
operator|.
name|download
operator|.
name|impl
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRetryableDownloadTask
extends|extends
name|AbstractDownloadTask
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractRetryableDownloadTask
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|long
name|scheduleDelay
init|=
literal|250
decl_stmt|;
specifier|private
name|int
name|scheduleMaxRun
init|=
literal|9
decl_stmt|;
specifier|private
name|int
name|scheduleNbRun
init|=
literal|0
decl_stmt|;
specifier|private
name|Exception
name|previousException
init|=
literal|null
decl_stmt|;
specifier|public
name|AbstractRetryableDownloadTask
parameter_list|(
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|super
argument_list|(
name|executorService
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|long
name|getScheduleDelay
parameter_list|()
block|{
return|return
name|scheduleDelay
return|;
block|}
specifier|public
name|void
name|setScheduleDelay
parameter_list|(
name|long
name|scheduleDelay
parameter_list|)
block|{
name|this
operator|.
name|scheduleDelay
operator|=
name|scheduleDelay
expr_stmt|;
block|}
specifier|public
name|int
name|getScheduleMaxRun
parameter_list|()
block|{
return|return
name|scheduleMaxRun
return|;
block|}
specifier|public
name|void
name|setScheduleMaxRun
parameter_list|(
name|int
name|scheduleMaxRun
parameter_list|)
block|{
name|this
operator|.
name|scheduleMaxRun
operator|=
name|scheduleMaxRun
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
try|try
block|{
name|File
name|file
init|=
name|download
argument_list|(
name|previousException
argument_list|)
decl_stmt|;
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Retry
name|retry
init|=
name|isRetryable
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|int
name|retryCount
init|=
name|scheduleMaxRun
decl_stmt|;
if|if
condition|(
name|retry
operator|==
name|Retry
operator|.
name|QUICK_RETRY
condition|)
block|{
name|retryCount
operator|=
name|retryCount
operator|/
literal|2
expr_stmt|;
comment|// arbitrary number...
block|}
elseif|else
if|if
condition|(
name|retry
operator|==
name|Retry
operator|.
name|NO_RETRY
condition|)
block|{
name|retryCount
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
operator|++
name|scheduleNbRun
operator|<
name|retryCount
condition|)
block|{
name|previousException
operator|=
name|e
expr_stmt|;
name|long
name|delay
init|=
call|(
name|long
call|)
argument_list|(
name|scheduleDelay
operator|*
literal|3
operator|/
literal|2
operator|+
name|Math
operator|.
name|random
argument_list|()
operator|*
name|scheduleDelay
operator|/
literal|2
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Error downloading "
operator|+
name|url
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". "
operator|+
name|retry
operator|+
literal|" in approx "
operator|+
name|delay
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|scheduleDelay
operator|*=
literal|2
expr_stmt|;
block|}
else|else
block|{
name|setException
argument_list|(
operator|new
name|IOException
argument_list|(
literal|"Error downloading "
operator|+
name|url
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|setException
argument_list|(
operator|new
name|IOException
argument_list|(
literal|"Error downloading "
operator|+
name|url
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Retry
name|isRetryable
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|Retry
operator|.
name|DEFAULT_RETRY
return|;
block|}
comment|/**      * Abstract download operation that may use<em>previous exception</em> as hint for optimized retry      * @param previousException      * @return      * @throws Exception      */
specifier|protected
specifier|abstract
name|File
name|download
parameter_list|(
name|Exception
name|previousException
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * What kind of retry may be attempted      */
specifier|protected
enum|enum
name|Retry
block|{
comment|/** Each retry would lead to the same result */
name|NO_RETRY
block|,
comment|/** It's ok to retry 2, 3 times, but no more */
name|QUICK_RETRY
block|,
comment|/** Retry with high expectation of success at some point */
name|DEFAULT_RETRY
block|}
block|}
end_class

end_unit

