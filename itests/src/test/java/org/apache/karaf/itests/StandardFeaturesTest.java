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
name|assertFeaturesInstalled
argument_list|(
literal|"standard"
argument_list|,
literal|"config2"
argument_list|,
literal|"region"
argument_list|,
literal|"package"
argument_list|,
literal|"kar"
argument_list|,
literal|"ssh"
argument_list|,
literal|"management"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|installAndAssertFeature
parameter_list|(
name|String
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
name|featureService
operator|.
name|installFeature
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
name|feature
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
name|installAndAssertFeature
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
name|installAndAssertFeature
argument_list|(
literal|"obr"
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
name|installAndAssertFeature
argument_list|(
literal|"jetty"
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
name|installAndAssertFeature
argument_list|(
literal|"http"
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
name|installAndAssertFeature
argument_list|(
literal|"http-whiteboard"
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
name|installAndAssertFeature
argument_list|(
literal|"war"
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
name|installAndAssertFeature
argument_list|(
literal|"webconsole"
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
name|installAndAssertFeature
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
name|installAndAssertFeature
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
name|installAndAssertFeature
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
name|installAndAssertFeature
argument_list|(
literal|"scr"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

