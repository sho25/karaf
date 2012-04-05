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
name|dev
operator|.
name|core
package|;
end_package

begin_comment
comment|/**  * MBean providing dev actions.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DevMBean
block|{
comment|/**      * Get the current OSGi framework in use.      *      * @return the name of the OSGi framework in use.      * @throws Exception      */
name|String
name|getFramework
parameter_list|()
function_decl|;
comment|/**      * change OSGi framework      *      * @param framework to use.      */
name|void
name|setFramework
parameter_list|(
name|String
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
comment|/**      * Restart Karaf and optionally clean the bundles      *       * @param clean all bundle states if true      */
name|void
name|restart
parameter_list|(
name|boolean
name|clean
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

