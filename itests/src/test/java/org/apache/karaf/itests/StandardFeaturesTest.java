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
name|junit
operator|.
name|JUnit4TestRunner
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
name|AllConfinedStagedReactorFactory
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
name|assertFalse
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|AllConfinedStagedReactorFactory
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
name|testBootFeatures
parameter_list|()
throws|throws
name|Exception
block|{
comment|// standard feature
name|String
name|standardFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep standard"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"standard feature is not installed"
argument_list|,
name|standardFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// config feature
name|String
name|configFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep config"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"config feature is not installed"
argument_list|,
name|configFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// region feature
name|String
name|regionFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep region"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"region feature is not installed"
argument_list|,
name|regionFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// package feature
name|String
name|packageFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep package"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"package feature is not installed"
argument_list|,
name|packageFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// kar feature
name|String
name|karFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep kar"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"kar feature is not installed"
argument_list|,
name|karFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// ssh feature
name|String
name|sshFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep ssh"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"ssh feature is not installed"
argument_list|,
name|sshFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// management feature
name|String
name|managementFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep management"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"management feature is not installed"
argument_list|,
name|managementFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install wrapper"
argument_list|)
expr_stmt|;
name|String
name|wrapperFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep wrapper"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"wrapper feature is not installed"
argument_list|,
name|wrapperFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install obr"
argument_list|)
expr_stmt|;
name|String
name|obrFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep obr"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"obr feature is not installed"
argument_list|,
name|obrFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installJettyFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|executeCommand
argument_list|(
literal|"feature:install jetty"
argument_list|)
expr_stmt|;
name|String
name|jettyFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep jetty"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"jetty feature is not installed"
argument_list|,
name|jettyFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install http"
argument_list|)
expr_stmt|;
name|String
name|httpFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep http"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"http feature is not installed"
argument_list|,
name|httpFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install http-whiteboard"
argument_list|)
expr_stmt|;
name|String
name|httpWhiteboardFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep http-whiteboard"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"http-whiteboard feature is not installed"
argument_list|,
name|httpWhiteboardFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install war"
argument_list|)
expr_stmt|;
name|String
name|warFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep war"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"war feature is not installed"
argument_list|,
name|warFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install webconsole"
argument_list|)
expr_stmt|;
name|String
name|webConsoleFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep webconsole"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"webconsole feature is not installed"
argument_list|,
name|webConsoleFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install scheduler"
argument_list|)
expr_stmt|;
name|String
name|schedulerFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep scheduler"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"scheduler feature is not installed"
argument_list|,
name|schedulerFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install eventadmin"
argument_list|)
expr_stmt|;
name|String
name|eventAdminFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep eventadmin"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"eventadmin feature is not installed"
argument_list|,
name|eventAdminFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install jasypt-encryption"
argument_list|)
expr_stmt|;
name|String
name|jasyptEncryptionFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep jasypt-encryption"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"jasypt-encryption feature is not installed"
argument_list|,
name|jasyptEncryptionFeatureStatus
operator|.
name|isEmpty
argument_list|()
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
name|executeCommand
argument_list|(
literal|"feature:install scr"
argument_list|)
expr_stmt|;
name|String
name|scrFeatureStatus
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep scr"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"scr feature is not installed"
argument_list|,
name|scrFeatureStatus
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

