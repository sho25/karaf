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
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

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
comment|/**  * JNDI Service MBean  */
end_comment

begin_interface
specifier|public
interface|interface
name|JndiMBean
block|{
comment|/**      * Get a map of JNDI names/class names (as attribute).      *      * @return The MBean attribute containing the {@link Map} of names/class names.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNames
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get a list of JNDI sub-contexts (as attribute).      *      * @return The MBean attribute containing the {@link List} of sub-contexts.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getContexts
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get a {@link Map} of JNDI names/class names children of a given base context.      *      * @param context The base context.      * @return The {@link Map} of names/class names.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNames
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get a {@link List} of JNDI sub-contexts children of a given base context.      *      * @param context The base context.      * @return The {@link List} of sub-contexts.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getContexts
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create a JNDI sub-context.      *      * @param context The JNDI sub-context name.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|void
name|create
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Delete a JNDI sub-context.      *      * @param context The JNDI sub-context name.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|context
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create another JNDI name (alias) for a given one.      *      * @param name The "source" JNDI name.      * @param alias The JNDI alias name.      * @throws MBeanException If the MBean fails.      */
specifier|public
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
name|MBeanException
function_decl|;
comment|/**      * Bind an OSGi service with a JNDI name.      *      * @param serviceId The OSGi service id (service.id property on the service, created by the framework).      * @param name The JNDI name.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|void
name|bind
parameter_list|(
name|Long
name|serviceId
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Unbind a given JNDI name.      *      * @param name The JNDI name.      * @throws MBeanException If the MBean fails.      */
specifier|public
name|void
name|unbind
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

