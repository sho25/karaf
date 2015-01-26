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
name|Spring3FeaturesTest
extends|extends
name|KarafTestSupport
block|{
comment|// Spring DM
annotation|@
name|Test
specifier|public
name|void
name|installSpringDmFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"spring-dm"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringDmWebFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"spring-dm-web"
argument_list|)
expr_stmt|;
block|}
comment|// Spring 3.1.x
annotation|@
name|Test
specifier|public
name|void
name|installSpring31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringAspects31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-aspects"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringInstrument31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-instrument"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringJdbc31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-jdbc"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringJms31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-jms"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Skipped due to version range not matching to Servlet 3.1"
argument_list|)
specifier|public
name|void
name|installSpringStruts31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-struts"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringTest31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-test"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringOrm31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-orm"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringOxm31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-oxm"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringTx31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-tx"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringWeb31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-web"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringWebPortlet31Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-web-portlet"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring31.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Spring 3.2.x
annotation|@
name|Test
specifier|public
name|void
name|installSpringFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringAspectsFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-aspects"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringInstrumentFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-instrument"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringJdbcFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-jdbc"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringJmsFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-jms"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringStrutsFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-struts"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringTestFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-test"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringOrmFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-orm"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringOxmFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-oxm"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringTxFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-tx"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringWebFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-web"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringWebPortletFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-web-portlet"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring32.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Spring Security
annotation|@
name|Test
specifier|public
name|void
name|installSpringSecurityFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"spring-security"
argument_list|)
expr_stmt|;
block|}
comment|// Gemini
annotation|@
name|Test
specifier|public
name|void
name|installGeminiBlueprintFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"gemini-blueprint"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

