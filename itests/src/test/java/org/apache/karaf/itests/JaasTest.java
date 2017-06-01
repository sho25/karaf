begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|itests
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|NameCallback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|PasswordCallback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactInstaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|PerClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|JaasTest
extends|extends
name|KarafTestSupport
block|{
annotation|@
name|Test
specifier|public
name|void
name|realmListCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|listRealmsOutput
init|=
name|executeCommand
argument_list|(
literal|"jaas:realm-list"
argument_list|)
decl_stmt|;
name|assertContains
argument_list|(
literal|"PropertiesLoginModule"
argument_list|,
name|listRealmsOutput
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"PublickeyLoginModule"
argument_list|,
name|listRealmsOutput
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
comment|//ignore it as this is too time consuming
specifier|public
name|void
name|testLoginNoLeak
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|200000
condition|;
name|i
operator|++
control|)
block|{
name|doLogin
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Test
comment|// shows the leak afaics
specifier|public
name|void
name|testLoginSingleReg
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|doLogin
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bundleContext
operator|.
name|getServiceReferences
argument_list|(
name|ArtifactInstaller
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doLogin
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|userPassRealm
init|=
literal|"karaf"
decl_stmt|;
name|LoginContext
name|lc
init|=
operator|new
name|LoginContext
argument_list|(
name|userPassRealm
argument_list|,
operator|new
name|CallbackHandler
argument_list|()
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|callback
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|callback
operator|instanceof
name|PasswordCallback
condition|)
block|{
name|PasswordCallback
name|passwordCallback
init|=
operator|(
name|PasswordCallback
operator|)
name|callback
decl_stmt|;
name|passwordCallback
operator|.
name|setPassword
argument_list|(
name|userPassRealm
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|callback
operator|instanceof
name|NameCallback
condition|)
block|{
name|NameCallback
name|nameCallback
init|=
operator|(
name|NameCallback
operator|)
name|callback
decl_stmt|;
name|nameCallback
operator|.
name|setName
argument_list|(
name|userPassRealm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|lc
operator|.
name|login
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|lc
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

