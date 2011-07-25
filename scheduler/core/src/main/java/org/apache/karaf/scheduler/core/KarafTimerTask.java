begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TimerTask
import|;
end_import

begin_class
specifier|public
class|class
name|KarafTimerTask
parameter_list|<
name|R
extends|extends
name|Runnable
parameter_list|>
extends|extends
name|TimerTask
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ID_PROPERTY
init|=
literal|"org.apache.karaf.scheduler.task.id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PERIOD_PROPERTY
init|=
literal|"org.apache.karaf.scheduler.task.period"
decl_stmt|;
specifier|protected
name|String
name|id
decl_stmt|;
specifier|protected
name|R
name|task
decl_stmt|;
specifier|protected
name|Long
name|schedulePeriod
init|=
literal|0L
decl_stmt|;
comment|/**      * Constructor      * @param id  the id of the task. Used for reference.      * @param task the task to be scheduled      */
specifier|public
name|KarafTimerTask
parameter_list|(
name|String
name|id
parameter_list|,
name|R
name|task
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
block|}
comment|/**      * Constructor      * @param id  the id of the task. Used for reference.      * @param task the task to be scheduled      * @param schedulePeriod the schedule period.      */
specifier|public
name|KarafTimerTask
parameter_list|(
name|String
name|id
parameter_list|,
name|R
name|task
parameter_list|,
name|Long
name|schedulePeriod
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
name|this
operator|.
name|schedulePeriod
operator|=
name|schedulePeriod
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|R
name|getTask
parameter_list|()
block|{
return|return
name|task
return|;
block|}
specifier|public
name|void
name|setTask
parameter_list|(
name|R
name|task
parameter_list|)
block|{
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
block|}
specifier|public
name|Long
name|getSchedulePeriod
parameter_list|()
block|{
return|return
name|schedulePeriod
return|;
block|}
specifier|public
name|void
name|setSchedulePeriod
parameter_list|(
name|Long
name|schedulePeriod
parameter_list|)
block|{
name|this
operator|.
name|schedulePeriod
operator|=
name|schedulePeriod
expr_stmt|;
block|}
block|}
end_class

end_unit

