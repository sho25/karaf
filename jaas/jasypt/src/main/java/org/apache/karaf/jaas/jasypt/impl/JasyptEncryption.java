begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
end_comment

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
name|jasypt
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jaas
operator|.
name|modules
operator|.
name|Encryption
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jasypt
operator|.
name|util
operator|.
name|password
operator|.
name|ConfigurablePasswordEncryptor
import|;
end_import

begin_comment
comment|/**  *<p>  * Jasypt implementation of the Encryption service.  *</p>  *   * @author jbonofre  */
end_comment

begin_class
specifier|public
class|class
name|JasyptEncryption
implements|implements
name|Encryption
block|{
specifier|private
name|ConfigurablePasswordEncryptor
name|passwordEncryptor
decl_stmt|;
comment|/**      *<p>      * Default constructor with the encryption algorithm.      *</p>      *       * @param params encryption parameters      */
specifier|public
name|JasyptEncryption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|passwordEncryptor
operator|=
operator|new
name|ConfigurablePasswordEncryptor
argument_list|()
expr_stmt|;
comment|// TODO: configure
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.karaf.jaas.modules.Encryption#encryptPassword(java.lang.String)      */
specifier|public
name|String
name|encryptPassword
parameter_list|(
name|String
name|plain
parameter_list|)
block|{
return|return
name|this
operator|.
name|passwordEncryptor
operator|.
name|encryptPassword
argument_list|(
name|plain
argument_list|)
return|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.karaf.jaas.modules.Encryption#checkPassword(java.lang.String, java.lang.String)      */
specifier|public
name|boolean
name|checkPassword
parameter_list|(
name|String
name|input
parameter_list|,
name|String
name|password
parameter_list|)
block|{
return|return
name|passwordEncryptor
operator|.
name|checkPassword
argument_list|(
name|input
argument_list|,
name|password
argument_list|)
return|;
block|}
block|}
end_class

end_unit

