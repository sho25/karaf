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
name|commands
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
import|;
end_import

begin_comment
comment|/**  * An action allows to easily execute commands in karaf.  * It can be assumed that each action is only accessed by a single thread at a time.  *   * An Action is always part of an AbstractCommand. The AbstractCommand makes sure  * the single threaded assumption above is true. Before the call to the execute method  * the action is checked for annotated fields (@Argument, @Option). These fields  * are populated from the command arguments before the action is called.  *   * Any class implementing Action must have a no argument constructor. This  * is necessary so the help generator can instantiate the class and get the   * default values.   */
end_comment

begin_interface
annotation|@
name|Deprecated
specifier|public
interface|interface
name|Action
extends|extends
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Action
block|{
name|Object
name|execute
parameter_list|(
name|CommandSession
name|session
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

