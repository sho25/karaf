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
name|BaseTest
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
name|itests
operator|.
name|util
operator|.
name|RunIfRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|Configuration
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
name|Option
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
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|composite
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|options
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|features
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
name|PaxCdiFeaturesTest
extends|extends
name|BaseTest
block|{
annotation|@
name|Rule
specifier|public
name|RunIfRule
name|rule
init|=
operator|new
name|RunIfRule
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|installPaxCdiFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"pax-cdi"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installPaxCdiWeldFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"pax-cdi-weld"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installPaxCdiOpenwebbeansFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"pax-cdi-openwebbeans"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installPaxCdiWebFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"pax-cdi-web"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installPaxCdiWebWeldFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"pax-cdi-weld"
argument_list|,
literal|"pax-cdi-web"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installPaxCdiWebOpenwebbeansFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"pax-cdi-openwebbeans"
argument_list|,
literal|"pax-cdi-web"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
return|return
name|options
argument_list|(
name|composite
argument_list|(
name|super
operator|.
name|config
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|(
name|maven
argument_list|()
operator|.
name|groupId
argument_list|(
literal|"org.ops4j.pax.cdi"
argument_list|)
operator|.
name|artifactId
argument_list|(
literal|"pax-cdi-features"
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
operator|.
name|type
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|classifier
argument_list|(
literal|"features"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

