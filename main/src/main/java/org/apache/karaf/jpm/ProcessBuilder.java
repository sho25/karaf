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
name|jpm
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

begin_comment
comment|/**  * Interface used to create new processes.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProcessBuilder
block|{
comment|/**      * Specify the current directory to run the command from.      *      * @param dir The directory to run the command from.      * @return The {@link ProcessBuilder} instance.      */
name|ProcessBuilder
name|directory
parameter_list|(
name|File
name|dir
parameter_list|)
function_decl|;
comment|/**      * Set the command to execute.      *      * @param command The command to execute.      * @return The {@link ProcessBuilder} instance.      */
name|ProcessBuilder
name|command
parameter_list|(
name|String
name|command
parameter_list|)
function_decl|;
comment|/**      * Create and start the process.      *      * @return The process that has been started.      * @throws IOException If the process can not be created.      */
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jpm
operator|.
name|Process
name|start
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Attach to an existing process.      *      * @param pid The process PID to attach.      * @return The process that has been attached.      * @throws IOException if the process can not be attached to.      */
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jpm
operator|.
name|Process
name|attach
parameter_list|(
name|int
name|pid
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

