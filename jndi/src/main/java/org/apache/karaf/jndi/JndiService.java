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
name|jndi
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
name|Map
import|;
end_import

begin_comment
comment|/**  * JNDI Service.  */
end_comment

begin_interface
specifier|public
interface|interface
name|JndiService
block|{
comment|/**      * List the current JNDI names (with the bound class name).      *      * @return The JNDI names.      * @throws Exception If the service fails.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * List the current JNDI names in the given context.      *      * @param context The JNDI context.      * @return The JNDI names in the context.      * @throws Exception If the service fails.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|names
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * List all JNDI sub-contexts.      *      * @return A {@link List} containing the sub-context names.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|contexts
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * List the JNDI sub-context from a given context.      *      * @param context The base JNDI context.      * @return A {@link List} containing the sub-context names.      * @throws Exception If the service fails.      */
name|List
argument_list|<
name|String
argument_list|>
name|contexts
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Create a sub-context.      *      * @param context The new sub-context name to create.      * @throws Exception If the service fails.      */
name|void
name|create
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Delete a sub-context.      *      * @param context The sub-context name to delete.      * @throws Exception If the service fails.      */
name|void
name|delete
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Create an alias on a given JNDI name.      *      * @param name The JNDI name.      * @param alias The alias.      * @throws Exception If the service fails.      */
name|void
name|alias
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|alias
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Bind a given OSGi service to a JNDI name.      *      * @param serviceId The OSGi service ID.      * @param name The JNDI name.      * @throws Exception If the service fails.      */
name|void
name|bind
parameter_list|(
name|long
name|serviceId
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Unbind an existing name.      *      * @param name The JNDI name to unbind.      * @throws Exception If the service fails.      */
name|void
name|unbind
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

