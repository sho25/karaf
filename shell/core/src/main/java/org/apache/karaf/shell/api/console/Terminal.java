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
name|shell
operator|.
name|api
operator|.
name|console
package|;
end_package

begin_comment
comment|/**  * Session terminal.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Terminal
block|{
comment|/**      * Width of the terminal.      */
name|int
name|getWidth
parameter_list|()
function_decl|;
comment|/**      * Height of the terminal.      */
name|int
name|getHeight
parameter_list|()
function_decl|;
comment|/**      * Whether ansi sequences are supported or not.      */
name|boolean
name|isAnsiSupported
parameter_list|()
function_decl|;
comment|/**      * Whether echo is enabled or not.      */
name|boolean
name|isEchoEnabled
parameter_list|()
function_decl|;
comment|/**      * Enable or disable echo.      */
name|void
name|setEchoEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

