begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|config
operator|.
name|core
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
comment|/**  * MBean to manipulate the Config layer.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfigMBean
block|{
comment|/**      * Get the list of all configuration PIDs.      *      * @return the list of all configuration PIDs.      * @throws MBeanException in case of MBean failure.      */
name|List
argument_list|<
name|String
argument_list|>
name|getConfigs
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create a new configuration for the given PID.      *      * @param pid the configuration PID.      * @throws MBeanException in case of MBean failure.      */
name|void
name|create
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Delete a configuration identified by the given PID.      *      * @param pid the configuration PID to delete.      * @throws MBeanException in case of MBean failure.      */
name|void
name|delete
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get the list of properties for a configuration PID.      *      * @param pid the configuration PID.      * @return the list of properties.      * @throws MBeanException in case of MBean failure.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|listProperties
parameter_list|(
name|String
name|pid
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Remove the configuration property identified by the given key.      *      * @param pid the configuration PID.      * @param key the property key.      * @throws MBeanException in case of MBean failure.      */
name|void
name|deleteProperty
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Append (or add) a value for the given configuration key.      *      * @param pid the configuration PID.      * @param key the property key.      * @param value the value to append to the current property value.      * @throws MBeanException in case of MBean failure.      */
name|void
name|appendProperty
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Set a configuration property.      *      * @param pid the configuration PID.      * @param key the property key.      * @param value the property value.      * @throws MBeanException in case of MBean failure.      */
name|void
name|setProperty
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get a configuration property.      *      * @param pid the configuration PID.      * @param key the property key.      * @throws MBeanException in case of MBean failure.      */
name|String
name|getProperty
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Update a complete configuration.      *      * @param pid the configuration PID.      * @param properties the new properties to set in the configuration.      * @throws MBeanException in case of MBean failure.      */
name|void
name|update
parameter_list|(
name|String
name|pid
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Create a factory based configuration.      *      * @param factoryPid the configuration factory PID.      * @param properties the new properties to set in the configuration.      * @return the created PID.      * @throws MBeanException in case of MBean failure.      */
name|String
name|createFactoryConfiguration
parameter_list|(
name|String
name|factoryPid
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

