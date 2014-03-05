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
name|action
package|;
end_package

begin_comment
comment|/**  * An action is the default implementation of the commands in karaf.  * In OSGi, Actions are discovered using an extender and a new instance  * of the class is created when the command is invoked, so that the  * implementation does not need to be thread safe.  *  * Before the call to the execute method the action is checked for  * fields annotated with @Reference and injected with services coming  * from the SessionFactory's Registry or from the OSGi registry.  * Methods annotated with @Init are then called.  The next step is to  * inject command line parameters into fields annotated with @Option  * and @Argument and then call the execute method.  *   * Any class implementing Action must have a no argument constructor. This  * is necessary so the help generator can instantiate the class and get the   * default values.  *  * In order to make commands available from the non-OSGi shell,  * the commands must be listed in a file available at  * META-INF/services/org/apache/karaf/shell/commmands.  *  * @see org.apache.karaf.shell.api.action.Command  * @see org.apache.karaf.shell.api.action.lifecycle.Service  */
end_comment

begin_interface
specifier|public
interface|interface
name|Action
block|{
comment|/**      * Execute the action which has been injected with services from the      * registry, options and arguments from the command line.      *      * @return<code>null</code> or the result of the action execution      * @throws Exception      */
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

