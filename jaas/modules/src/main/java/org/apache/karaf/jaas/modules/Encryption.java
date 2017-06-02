begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
package|;
end_package

begin_comment
comment|/**  * Interface describing the password encryption service.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Encryption
block|{
comment|/**      * Encrypt a password.      *       * @param plain the password in plain format.      * @return the encrypted password format.      */
name|String
name|encryptPassword
parameter_list|(
name|String
name|plain
parameter_list|)
function_decl|;
comment|/**      * Check password.      *       * @param input password provided in plain format.      * @param password the encrypted format to compare with.      * @return true if the password match, false else.      */
name|boolean
name|checkPassword
parameter_list|(
name|String
name|input
parameter_list|,
name|String
name|password
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

