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
name|console
package|;
end_package

begin_interface
annotation|@
name|Deprecated
specifier|public
interface|interface
name|SubShell
block|{
comment|/**      * Return the name of the command if used inside a shell.      *      * @return The name of the command in the shell.      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Return the description of the command which is used to generate command line help.      *      * @return The description of the command in the shell.      */
name|String
name|getDescription
parameter_list|()
function_decl|;
comment|/**      * Return a detailed description of the command.      *      * @return The detailed description of the command in the shell.      */
name|String
name|getDetailedDescription
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

