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
operator|.
name|features
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|itests
operator|.
name|KarafTestSupport
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
name|StandardFeaturesTest
extends|extends
name|KarafTestSupport
block|{
annotation|@
name|Test
specifier|public
name|void
name|installAriesAnnotationFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"aries-annotation"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installWrapperFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installObrFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"obr"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installConfigFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"config"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installRegionFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"region"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installPackageFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"package"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHttpFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
comment|// TODO: Check why uninstalling http does not uninstall pax-http
name|featureService
operator|.
name|uninstallFeature
argument_list|(
literal|"pax-http"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHttpWhiteboardFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"http-whiteboard"
argument_list|)
expr_stmt|;
comment|// TODO: Check why uninstalling http does not uninstall pax-http-whiteboard
name|featureService
operator|.
name|uninstallFeature
argument_list|(
literal|"pax-http-whiteboard"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installWarFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"war"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installKarFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"kar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installWebConsoleFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"webconsole"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSSHFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"ssh"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installManagementFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"management"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSchedulerFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"scheduler"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installEventAdminFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"eventadmin"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installJasyptEncryptionFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"jasypt-encryption"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installScrFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"scr"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

