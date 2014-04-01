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
name|EnterpriseFeaturesTest
extends|extends
name|KarafTestSupport
block|{
annotation|@
name|Test
specifier|public
name|void
name|installTransactionFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"transaction"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installJpaFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"jpa"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installOpenJpaFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"openjpa"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernateFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernateEnversFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-envers"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernateValidatorFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-validator"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installJndiFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"jndi"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installJdbcFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"jdbc"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installJmsFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"jms"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Pax-cdi depends on scr feature [2.3,3.5) so it does not work with 4.0"
argument_list|)
specifier|public
name|void
name|installOpenWebBeansFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"openwebbeans"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Pax-cdi depends on scr feature [2.3,3.5) so it does not work with 4.0"
argument_list|)
specifier|public
name|void
name|installWeldFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"weld"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installApplicationWithoutIsolationFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"application-without-isolation"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

