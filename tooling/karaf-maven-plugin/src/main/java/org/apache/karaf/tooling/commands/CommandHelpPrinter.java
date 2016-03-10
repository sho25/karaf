begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
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
name|action
operator|.
name|Action
import|;
end_import

begin_interface
specifier|public
interface|interface
name|CommandHelpPrinter
block|{
comment|/**      * Print help for a single action to the out stream.      *       * @param action The command {@link Action}.      * @param out The stream where to print the help.      * @param includeHelpOption True to include the help option in the doc, false else.      */
name|void
name|printHelp
parameter_list|(
name|Action
name|action
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|boolean
name|includeHelpOption
parameter_list|)
function_decl|;
comment|/**      * Print the overview of all given commands to the out stream.      *       * @param commands The {@link Map} of commands to consider in the overview.      * @param out The stream where to write the overview.      */
name|void
name|printOverview
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|commands
parameter_list|,
name|PrintStream
name|out
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

