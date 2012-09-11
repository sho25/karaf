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
package|;
end_package

begin_comment
comment|/**  * Describe a system service  */
end_comment

begin_interface
specifier|public
interface|interface
name|SystemService
block|{
comment|/**      * Halt the Karaf container.      */
name|void
name|halt
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Halt the Karaf container.      *      * @param time shutdown delay. The time argument can have different formats.      *  First, it can be an absolute time in the format hh:mm, in which hh is the hour (1 or 2 digits) and mm      *  is the minute of the hour (in two digits). Second, it can be in the format +m, in which m is the number of minutes      *  to wait. The word now is an alias for +0.      */
name|void
name|halt
parameter_list|(
name|String
name|time
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Reboot the Karaf container.      *      * @throws Exception      */
name|void
name|reboot
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Reboot the Karaf container.      *      * @param time reboot delay. The time argument can have different formats.      *  First, it can be an absolute time in the format hh:mm, in which hh is the hour (1 or 2 digits) and mm      *  is the minute of the hour (in two digits). Second, it can be in the format +m, in which m is the number of minutes      *  to wait. The word now is an alias for +0.      *  @param clean Force a clean restart by deleting the working directory.      */
name|void
name|reboot
parameter_list|(
name|String
name|time
parameter_list|,
name|boolean
name|clean
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Set the system start level.      *      * @param startLevel the new system start level.      */
name|void
name|setStartLevel
parameter_list|(
name|int
name|startLevel
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get the system start level.      *      * @return the current system start level.      */
name|int
name|getStartLevel
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Get the version of the current Karaf instance      *      * @return instance version      */
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**      * Get the name of the current Karaf instance      *      * @return instance name      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Set the name of the Karaf instance      *      * @param name new instance name      */
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Get the current OSGi framework in use.      *      * @return the name of the OSGi framework in use.      * @throws Exception      */
name|FrameworkType
name|getFramework
parameter_list|()
function_decl|;
comment|/**      * change OSGi framework      *      * @param framework to use.      */
name|void
name|setFramework
parameter_list|(
name|FrameworkType
name|framework
parameter_list|)
function_decl|;
comment|/**      * Enable or diable debgging      * @param debug enable if true      */
name|void
name|setFrameworkDebug
parameter_list|(
name|boolean
name|debug
parameter_list|)
function_decl|;
comment|/**      * Set a system property and persist to etc/system.properties      * @param key      */
name|String
name|setSystemProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|persist
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

