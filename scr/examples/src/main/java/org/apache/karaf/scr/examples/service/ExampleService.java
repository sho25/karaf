begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|examples
operator|.
name|service
package|;
end_package

begin_comment
comment|/**  *   *   */
end_comment

begin_interface
specifier|public
interface|interface
name|ExampleService
block|{
comment|/** 	 *  	 */
name|void
name|printGreetings
parameter_list|()
function_decl|;
comment|/** 	 * @param salutation 	 *            the salutation to set 	 */
name|void
name|setSalutation
parameter_list|(
name|String
name|salutation
parameter_list|)
function_decl|;
comment|/** 	 * @param name 	 *            the name to set 	 */
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

