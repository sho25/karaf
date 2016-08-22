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
name|com
operator|.
name|sun
operator|.
name|jna
operator|.
name|Library
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jna
operator|.
name|Native
import|;
end_import

begin_interface
specifier|public
interface|interface
name|SystemdDaemon
extends|extends
name|Library
block|{
name|SystemdDaemon
name|INSTANCE
init|=
operator|(
name|SystemdDaemon
operator|)
name|Native
operator|.
name|loadLibrary
argument_list|(
literal|"systemd-daemon"
argument_list|,
name|SystemdDaemon
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|sd_notify
parameter_list|(
name|int
name|unset_environment
parameter_list|,
name|String
name|state
parameter_list|)
function_decl|;
name|int
name|sd_notifyf
parameter_list|(
name|int
name|unset_environment
parameter_list|,
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
function_decl|;
comment|// Not available in all systemd version, should replace used of MAINPID
comment|// int sd_pid_notify(int pid, int unset_environment, String state);
comment|// int sd_pid_notifyff(int pid, int unset_environment, String format, Object... args);
block|}
end_interface

end_unit

