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
name|Before
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
name|MavenUtils
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
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|editConfigurationFilePut
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
name|Spring40FeaturesTest
extends|extends
name|KarafTestSupport
block|{
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
name|String
name|version
init|=
name|MavenUtils
operator|.
name|getArtifactVersion
argument_list|(
literal|"org.apache.karaf"
argument_list|,
literal|"apache-karaf"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Option
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|super
operator|.
name|config
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|editConfigurationFilePut
argument_list|(
literal|"etc/org.apache.karaf.features.cfg"
argument_list|,
literal|"featuresRepositories"
argument_list|,
literal|"mvn:org.apache.karaf.features/framework/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/spring/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/spring-legacy/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/enterprise/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/enterprise-legacy/"
operator|+
name|version
operator|+
literal|"/xml/features, "
operator|+
literal|"mvn:org.apache.karaf.features/standard/"
operator|+
name|version
operator|+
literal|"/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|Option
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
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
literal|"spring40.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installSpringWebSocketFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"spring-websocket"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring40.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

