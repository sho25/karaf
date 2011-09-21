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
name|management
operator|.
name|mbeans
operator|.
name|config
package|;
end_package

begin_comment
comment|/**  * MBean to manipulate the Config layer.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfigMBean
block|{
comment|/**      * Return the list of all configuration PIDs.      *      * @return the list of all configuration PIDs.      * @throws Exception      */
name|String
index|[]
name|list
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

