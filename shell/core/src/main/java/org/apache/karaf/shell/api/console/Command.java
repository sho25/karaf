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
name|shell
operator|.
name|api
operator|.
name|console
package|;
end_package

begin_comment
comment|/**  * A<code>Command</code> is a named  * {@link org.apache.karaf.shell.api.console.Function}  * which also provides completion.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Command
extends|extends
name|Function
block|{
comment|/**      * Retrieve the scope of this command.      */
name|String
name|getScope
parameter_list|()
function_decl|;
comment|/**      * Retrieve the name of this command.      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Retrieve the description of this command.      * This short command description will be printed      * when using the<code>help</code> command.      */
name|String
name|getDescription
parameter_list|()
function_decl|;
comment|/**      * Retrieve the completer associated with this command.      *      * @param scoped whether the command is invoked from a subshell or not      * @return the {@link Completer} to use      */
name|Completer
name|getCompleter
parameter_list|(
name|boolean
name|scoped
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

