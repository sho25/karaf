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
name|scr
operator|.
name|management
package|;
end_package

begin_comment
comment|/**  * The management interface for SCR Components.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ScrServiceMBean
block|{
comment|/**      * Presents a {@String} array of components currently registered with the SCR.      *      * @return String[]      * @throws Exception      */
name|String
index|[]
name|listComponents
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Verifies if the named component is currently in an ACTIVE state.      *      * @param componentName the components name      * @return true if ACTIVE, otherwise false      * @throws Exception      */
name|boolean
name|isComponentActive
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the named components state      *      * @param componentName the components name      * @return      * @throws Exception      */
name|int
name|componentState
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Activates a component that is currently in a DISABLED state.      *      * @param componentName the components name      * @throws Exception      */
name|void
name|activateComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Disables a component that is not in an ACTIVE state.      *      * @param componentName the components name      * @throws Exception      */
name|void
name|deactivateComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

