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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A<code>Completer</code> is used by the console to complete the command line.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Completer
block|{
comment|/**      * populate possible completion candidates.      *      * @param session the current {@link Session}      * @param commandLine the pre-parsed {@link CommandLine}      * @param candidates a list to fill with possible completion candidates      * @return the index of the{@link CommandLine} for which the completion will be relative      */
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

