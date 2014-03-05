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
operator|.
name|lifecycle
package|;
end_package

begin_comment
comment|/**  * The<code>Manager</code> service can be used to programmatically  * register {@link org.apache.karaf.shell.api.action.Action}s or  * {@link org.apache.karaf.shell.api.console.Completer}s.  *  * Registered objects must be annotated with the {@link Service} annotation.  *  * Objects will be registered in the {@link org.apache.karaf.shell.api.console.Registry}  * associated with this<code>Manager</code>.  *  * @see org.apache.karaf.shell.api.console.Registry  * @see org.apache.karaf.shell.api.action.lifecycle.Service  */
end_comment

begin_interface
specifier|public
interface|interface
name|Manager
block|{
comment|/**      * Register a service.      * If the given class is an {@link org.apache.karaf.shell.api.action.Action},      * a {@link org.apache.karaf.shell.api.console.Command} will be created and registered,      * else, an instance of the class will be created, injected and registered.      */
name|void
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Unregister a previously registered class.      */
name|void
name|unregister
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

