begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
comment|/**  * A<code>CommandLine</code> object will be created and  * given the {@link org.apache.karaf.shell.api.console.Completer}s to ease  * their work.  Arguments are separated and the cursor position within the  * current argument is given.  */
end_comment

begin_interface
specifier|public
interface|interface
name|CommandLine
block|{
comment|/**      * Retrieve the argument index for the cursor position      *      * @return the cursor argument index      */
name|int
name|getCursorArgumentIndex
parameter_list|()
function_decl|;
comment|/**      * Retrieve the argument for the cursor position      *      * @return the cursor argument at position      */
name|String
name|getCursorArgument
parameter_list|()
function_decl|;
comment|/**      * Retrieve the position of the cursor within the argument      *      * @return the position of the argument      */
name|int
name|getArgumentPosition
parameter_list|()
function_decl|;
comment|/**      * List of arguments on the current command.      * If the command line contains multiple commands, only the command corresponding      * to the cursor position is available.      *      * @return array of arguments      */
name|String
index|[]
name|getArguments
parameter_list|()
function_decl|;
comment|/**      * Retrieve the position of the cursor within the command line.      *      * @return the buffer position      */
name|int
name|getBufferPosition
parameter_list|()
function_decl|;
comment|/**      * Retrieve the full buffer.      *      * @return the buffer      */
name|String
name|getBuffer
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

