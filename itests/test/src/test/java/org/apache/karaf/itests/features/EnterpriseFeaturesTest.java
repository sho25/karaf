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
name|RunIfRules
operator|.
name|RunIfNotOnJdk8
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
name|Ignore
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
name|EnterpriseFeaturesTest
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
name|installTransaction130Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"transaction"
argument_list|,
literal|"1.3.3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installTransaction20Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"transaction"
argument_list|,
literal|"2.0.0"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installConnector311Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"connector"
argument_list|,
literal|"3.1.1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"jpa feature is installed two times causing error. Test disabled to investigate."
argument_list|)
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
name|installOpenJpa222Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"openjpa"
argument_list|,
literal|"2.2.2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|RunIfNotOnJdk8
specifier|public
name|void
name|installOpenJpa230Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"openjpa"
argument_list|,
literal|"2.3.0"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installOpenJpa240Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"openjpa"
argument_list|,
literal|"2.4.2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installOpenJpa3Feature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"openjpa3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernate4215FinalFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"hibernate"
argument_list|,
literal|"4.2.15.Final"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernateEnvers4215FinalFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"hibernate-envers"
argument_list|,
literal|"4.2.15.Final"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernate436FinalFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"hibernate"
argument_list|,
literal|"4.3.6.Final"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernateEnvers436FinalFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeature
argument_list|(
literal|"hibernate-envers"
argument_list|,
literal|"4.3.6.Final"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installHibernateValidatorFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-validator"
argument_list|)
expr_stmt|;
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-validator-jsoup"
argument_list|)
expr_stmt|;
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-validator-joda-time"
argument_list|)
expr_stmt|;
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-validator-javax-money"
argument_list|)
expr_stmt|;
name|installAssertAndUninstallFeatures
argument_list|(
literal|"hibernate-validator-groovy"
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
annotation|@
name|Test
specifier|public
name|void
name|installSubsystems
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"subsystems"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installDocker
parameter_list|()
throws|throws
name|Exception
block|{
name|installAssertAndUninstallFeatures
argument_list|(
literal|"docker"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

