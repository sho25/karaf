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

begin_interface
specifier|public
interface|interface
name|Lock
block|{
comment|/**      * A KeepAlive function to maintain the lock.      * Indicates whether or not the lock could be acquired.      *      * @return True if connection lock retained, false otherwise.      * @throws Exception If the lock can't be acquired.      */
name|boolean
name|lock
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Terminate the lock connection safely.      *      * @throws Exception If the lock can't be released.      */
name|void
name|release
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Indicate whether or not the lock still exists.      *      * @return True, if the lock still exists, otherwise false.      * @throws Exception If an error occurs while checking if the lock is alive.      */
name|boolean
name|isAlive
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

