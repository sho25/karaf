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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * Service registry.  *  * The registry can be used to register various services used during injection along  * with {@link Command}s.  *  * @see org.apache.karaf.shell.api.console.SessionFactory  * @see org.apache.karaf.shell.api.console.Session  */
end_comment

begin_interface
specifier|public
interface|interface
name|Registry
block|{
comment|/**      * Return a list of available commands.      */
name|List
argument_list|<
name|Command
argument_list|>
name|getCommands
parameter_list|()
function_decl|;
comment|/**      *      * @param scope      * @param name      * @return      */
name|Command
name|getCommand
parameter_list|(
name|String
name|scope
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Register a delayed service (or factory).      * In cases where instances must be created for each injection,      * a {@link Callable} can be registered and each injection will      * call it to obtain the actual service implementation.      *      * @param factory      * @param clazz      * @param<T>      */
parameter_list|<
name|T
parameter_list|>
name|void
name|register
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|factory
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Register a service.      */
name|void
name|register
parameter_list|(
name|Object
name|service
parameter_list|)
function_decl|;
comment|/**      * Unregister a service.      * If the registration has been done using a factory, the same      * factory should be used to unregister.      */
name|void
name|unregister
parameter_list|(
name|Object
name|service
parameter_list|)
function_decl|;
comment|/**      * Obtain a service implementing the given class.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|getService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Obtain a list of services implementing the given class.      */
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getServices
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Check whether the registry has a service of the given class.      */
name|boolean
name|hasService
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

