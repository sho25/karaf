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
name|jasypt
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  *<p>  * Test<code>JasyptEncryption</code>.  *</p>  *   * @author jbonofre  */
end_comment

begin_class
specifier|public
class|class
name|JasyptEncryptionTest
extends|extends
name|TestCase
block|{
specifier|private
name|JasyptEncryption
name|encryption
decl_stmt|;
comment|/*      * (non-Javadoc)      * @see junit.framework.TestCase#setUp()      */
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|this
operator|.
name|encryption
operator|=
operator|new
name|JasyptEncryption
argument_list|(
literal|"MD5"
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>      * Test<code>checkPassword()</p> method.      *</p>      *       * @throws Exception in case of test error.      */
specifier|public
name|void
name|testCheckPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|password
init|=
name|this
operator|.
name|encryption
operator|.
name|encryptPassword
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|this
operator|.
name|encryption
operator|.
name|checkPassword
argument_list|(
literal|"test"
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

