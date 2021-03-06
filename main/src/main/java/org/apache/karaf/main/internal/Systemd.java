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
name|internal
package|;
end_package

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

begin_class
specifier|public
class|class
name|Systemd
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ENV_WATCHDOG_USEC
init|=
literal|"WATCHDOG_USEC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENV_MAIN_PID
init|=
literal|"SYSTEMD_MAIN_PID"
decl_stmt|;
specifier|private
specifier|final
name|String
name|mainPid
decl_stmt|;
specifier|public
name|Systemd
parameter_list|()
block|{
name|this
operator|.
name|mainPid
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.systemd.main.pid"
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
name|ENV_MAIN_PID
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|notifyWatchdog
parameter_list|()
block|{
name|int
name|rc
init|=
operator|-
literal|1
decl_stmt|;
comment|// WATCHDOG : tells the service manager to update the watchdog timestamp.
comment|//            This is the keep-alive ping that services need to issue in
comment|//            regular intervals if WatchdogSec= is enabled for it.
comment|// MAINPID  : the main process ID (PID) of the service, in case the service
comment|//            manager did not fork off the process itself. Example: "MAINPID=4711"
comment|//            This does not seem to work reliably so used only if system
comment|//            property karaf.systemd.main.pid or env variable SYSTEMD_MAIN_PID
comment|//            are set (system property takes the precedence)
if|if
condition|(
name|SystemdDaemon
operator|.
name|INSTANCE
operator|!=
literal|null
condition|)
block|{
name|rc
operator|=
name|SystemdDaemon
operator|.
name|INSTANCE
operator|.
name|sd_notify
argument_list|(
literal|0
argument_list|,
operator|(
name|mainPid
operator|==
literal|null
operator|)
condition|?
literal|"WATCHDOG=1"
else|:
operator|(
literal|"MAINPID="
operator|+
name|mainPid
operator|+
literal|"\nWATCHDOG=1"
operator|)
argument_list|)
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
specifier|public
name|long
name|getWatchdogTimeout
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|String
name|timeouts
init|=
name|System
operator|.
name|getenv
argument_list|(
name|ENV_WATCHDOG_USEC
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeouts
operator|!=
literal|null
condition|)
block|{
name|long
name|micros
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|timeouts
argument_list|)
decl_stmt|;
return|return
name|timeUnit
operator|.
name|convert
argument_list|(
name|micros
argument_list|,
name|TimeUnit
operator|.
name|MICROSECONDS
argument_list|)
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

