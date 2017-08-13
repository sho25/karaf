begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2017 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|command
operator|.
name|support
operator|.
name|TriggerJob
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Action
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
name|Argument
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
name|Command
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
name|Option
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
literal|"trigger"
argument_list|,
name|description
operator|=
literal|"Manually trigger a scheduled job"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Trigger
implements|implements
name|Action
block|{
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Name of the job to trigger"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
name|String
name|name
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
literal|"background"
argument_list|,
name|description
operator|=
literal|"schedule the trigger in the background"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
name|boolean
name|background
init|=
literal|false
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
name|background
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Scheduling background trigger for job "
operator|+
name|name
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|schedule
argument_list|(
operator|new
name|TriggerJob
argument_list|(
name|scheduler
argument_list|,
name|name
argument_list|)
argument_list|,
name|scheduler
operator|.
name|NOW
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Triggering job "
operator|+
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|scheduler
operator|.
name|trigger
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Could not find a scheduled job with name "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

