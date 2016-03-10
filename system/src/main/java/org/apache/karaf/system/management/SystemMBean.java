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
name|system
operator|.
name|management
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Describe the system MBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SystemMBean
block|{
comment|/**      * Stop the Karaf instance.      *      * @throws MBeanException If a failure occurs.      */
name|void
name|halt
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Stop the Karaf instance at a given time.      *      * @param time the time when to stop the Karaf instance.      * @throws MBeanException If a failure occurs.      */
name|void
name|halt
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Reboot the Karaf instance.      *      * @throws MBeanException If a failure occurs.      */
name|void
name|reboot
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Reboot the Karaf instance at a given time.      *      * @param time the time when to reboot the Karaf instance.      * @throws MBeanException If a failure occurs.      */
name|void
name|reboot
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Reboot the Karaf instance at a given time and clean the cache.      *      * @param time the time when to reboot the Karaf instance.      * @throws MBeanException If a failure occurs.      */
name|void
name|rebootCleanCache
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Reboot the Karaf instance at a given time and clean all working files.      *      * @param time the time when to reboot the Karaf instance.      * @throws MBeanException If a failure occurs.      */
name|void
name|rebootCleanAll
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Set the system bundle start level.      *      * @param startLevel the new system bundle start level.      * @throws MBeanException If a failure occurs.      */
name|void
name|setStartLevel
parameter_list|(
name|int
name|startLevel
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get the current system bundle start level.      *      * @return the current system bundle start level.      * @throws MBeanException If a failure occurs.      */
name|int
name|getStartLevel
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get the current OSGi framework in use.      *      * @return the name of the OSGi framework in use.      */
name|String
name|getFramework
parameter_list|()
function_decl|;
comment|/**      * Change OSGi framework      *      * @param framework The framework to use.      */
name|void
name|setFramework
parameter_list|(
name|String
name|framework
parameter_list|)
function_decl|;
comment|/**      * Enable or disable debugging      *      * @param debug enable if true      */
name|void
name|setFrameworkDebug
parameter_list|(
name|boolean
name|debug
parameter_list|)
function_decl|;
comment|/**      * Get the current Karaf instance name.      *      * @return the current Karaf instance name.      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Change Karaf instance name.      *      * @param name the new Karaf instance name.      */
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Get the version of the current Karaf instance.      *      * @return the current Karaf instance version.      */
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**      * Get all system properties.      *      * @param unset if true, display the OSGi properties even if they are not defined (with "undef" value).      * @param dumpToFile if true, dump the properties into a file in the data folder.      * @return the list of system properties.      * @throws MBeanException If a failure occurs.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|(
name|boolean
name|unset
parameter_list|,
name|boolean
name|dumpToFile
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Get the value of a given system property.      *      * @param key the system property key.      * @return the system property value.      */
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Set the value of a system property.      *      * @param key the system property key.      * @param value the new system property value.      * @param persistent if true, persist the new value to the etc/system.properties file.      */
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|persistent
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

