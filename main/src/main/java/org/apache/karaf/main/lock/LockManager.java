begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|main
operator|.
name|lock
package|;
end_package

begin_class
specifier|public
class|class
name|LockManager
block|{
specifier|private
name|Lock
name|lock
decl_stmt|;
specifier|private
name|boolean
name|exiting
init|=
literal|false
decl_stmt|;
comment|/**      * If a lock should be used before starting the runtime      */
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_USE_LOCK
init|=
literal|"karaf.lock"
decl_stmt|;
comment|/**      * The lock implementation      */
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_CLASS
init|=
literal|"karaf.lock.class"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_CLASS_DEFAULT
init|=
name|SimpleFileLock
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|LockCallBack
name|lockCallback
decl_stmt|;
specifier|private
specifier|final
name|int
name|lockCheckInterval
decl_stmt|;
specifier|public
name|LockManager
parameter_list|(
name|Lock
name|lock
parameter_list|,
name|LockCallBack
name|lockCallback
parameter_list|,
name|int
name|lockCheckIntervalSeconds
parameter_list|)
block|{
name|this
operator|.
name|lock
operator|=
name|lock
expr_stmt|;
name|this
operator|.
name|lockCallback
operator|=
name|lockCallback
expr_stmt|;
name|this
operator|.
name|lockCheckInterval
operator|=
name|lockCheckIntervalSeconds
expr_stmt|;
block|}
specifier|public
name|void
name|startLockMonitor
parameter_list|()
block|{
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|runLockManager
argument_list|()
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|stopLockMonitor
parameter_list|()
block|{
name|this
operator|.
name|exiting
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
name|void
name|runLockManager
parameter_list|()
block|{
while|while
condition|(
operator|!
name|exiting
condition|)
block|{
try|try
block|{
if|if
condition|(
name|lock
operator|.
name|lock
argument_list|()
condition|)
block|{
name|lockCallback
operator|.
name|lockAquired
argument_list|()
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
if|if
condition|(
operator|!
name|lock
operator|.
name|isAlive
argument_list|()
operator|||
name|exiting
condition|)
block|{
break|break;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|lockCheckInterval
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|exiting
condition|)
block|{
name|lockCallback
operator|.
name|lockLost
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|lockCallback
operator|.
name|waitingForLock
argument_list|()
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|lockCheckInterval
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|unlock
parameter_list|()
throws|throws
name|Exception
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

