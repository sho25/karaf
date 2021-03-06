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
name|assertFalse
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
name|assertTrue
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

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|RolePrincipal
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
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
name|FeatureTest
extends|extends
name|BaseTest
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
name|bootFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFeaturesInstalled
argument_list|(
literal|"jaas"
argument_list|,
literal|"ssh"
argument_list|,
literal|"management"
argument_list|,
literal|"bundle"
argument_list|,
literal|"config"
argument_list|,
literal|"deployer"
argument_list|,
literal|"diagnostic"
argument_list|,
literal|"instance"
argument_list|,
literal|"kar"
argument_list|,
literal|"log"
argument_list|,
literal|"package"
argument_list|,
literal|"service"
argument_list|,
literal|"system"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|listCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|featureListOutput
init|=
name|executeCommand
argument_list|(
literal|"feature:list"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|featureListOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|featureListOutput
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|featureListOutput
operator|=
name|executeCommand
argument_list|(
literal|"feature:list -i"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|featureListOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|featureListOutput
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
name|listViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|TabularData
name|features
init|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"Features"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|features
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installUninstallCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:install -v -r wrapper"
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:uninstall -r wrapper"
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFeatureNotInstalled
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installWithUpgradeCommand
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|featureToUpgrade
init|=
literal|"transaction-api"
decl_stmt|;
specifier|final
name|String
name|oldVersion
init|=
literal|"1.1.0"
decl_stmt|;
specifier|final
name|String
name|newVersion
init|=
literal|"1.2.0"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:install -v -r "
operator|+
name|featureToUpgrade
operator|+
literal|"/"
operator|+
name|oldVersion
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
name|featureToUpgrade
argument_list|,
name|oldVersion
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:install -r --upgrade "
operator|+
name|featureToUpgrade
operator|+
literal|"/"
operator|+
name|newVersion
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFeatureNotInstalled
argument_list|(
name|featureToUpgrade
argument_list|,
name|oldVersion
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
name|featureToUpgrade
argument_list|,
name|newVersion
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|installUninstallViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"installFeature"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"wrapper"
block|,
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
name|assertFeatureInstalled
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"uninstallFeature"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"wrapper"
block|,
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
name|assertFeatureNotInstalled
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|repoAddRemoveCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:repo-add mvn:org.apache.karaf.cellar/apache-karaf-cellar/3.0.0/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"apache-karaf-cellar"
argument_list|,
name|executeCommand
argument_list|(
literal|"feature:repo-list"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:repo-remove mvn:org.apache.karaf.cellar/apache-karaf-cellar/3.0.0/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|assertContainsNot
argument_list|(
literal|"apache-karaf-cellar"
argument_list|,
name|executeCommand
argument_list|(
literal|"feature:repo-list"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|repoAddRemoveCommandWithRegex
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:repo-add mvn:org.apache.karaf.cellar/apache-karaf-cellar/3.0.0/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"apache-karaf-cellar"
argument_list|,
name|executeCommand
argument_list|(
literal|"feature:repo-list"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:repo-remove '.*apache-karaf-cellar.*'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertContainsNot
argument_list|(
literal|"apache-karaf-cellar"
argument_list|,
name|executeCommand
argument_list|(
literal|"feature:repo-list"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|repoAddRemoveViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"addRepository"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"mvn:org.apache.karaf.cellar/apache-karaf-cellar/3.0.0/xml/features"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"removeRepository"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"mvn:org.apache.karaf.cellar/apache-karaf-cellar/3.0.0/xml/features"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|repoAddRemoveWithRegexViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mBeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"addRepository"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"mvn:org.apache.karaf.cellar/apache-karaf-cellar/3.0.0/xml/features"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"removeRepository"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|".*apache-karaf-cellar.*"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|repoRefreshCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|refreshedRepo
init|=
name|executeCommand
argument_list|(
literal|"feature:repo-refresh '.*org.ops4j.pax.[wc].*'"
argument_list|)
decl_stmt|;
name|assertContains
argument_list|(
literal|"pax-cdi"
argument_list|,
name|refreshedRepo
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"pax-web"
argument_list|,
name|refreshedRepo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|repoRefreshViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"refreshRepository"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|".*pax-web.*"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|configRegularLifecycle
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:install http"
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|executeCommand
argument_list|(
literal|"config:exists org.ops4j.pax.web"
argument_list|)
decl_stmt|;
name|assertContains
argument_list|(
literal|"true"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|File
name|jetty
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"jetty.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"jetty.xml should exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:uninstall http"
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"config:exists org.ops4j.pax.web"
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"true"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|jetty
operator|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"jetty.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"jetty.xml should still exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|configDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:install http"
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|executeCommand
argument_list|(
literal|"config:exists org.ops4j.pax.web"
argument_list|)
decl_stmt|;
name|assertContains
argument_list|(
literal|"true"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|File
name|jetty
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"jetty.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"etc/jetty.xml should exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:uninstall -c http"
argument_list|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|=
name|executeCommand
argument_list|(
literal|"config:exists org.ops4j.pax.web"
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"false"
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|jetty
operator|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
literal|"jetty.xml"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"jetty.xml should not still exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|configRegularLifecycleViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|featureMBean
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|ObjectName
name|configMBean
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=config,name=root"
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|featureMBean
argument_list|,
literal|"installFeature"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"http"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|boolean
name|exist
init|=
operator|(
name|boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|configMBean
argument_list|,
literal|"exists"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"org.ops4j.pax.web"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"true"
argument_list|,
name|exist
argument_list|)
expr_stmt|;
name|File
name|jetty
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
literal|"jetty.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"jetty.xml should exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|featureMBean
argument_list|,
literal|"uninstallFeature"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"http"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|exist
operator|=
operator|(
name|boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|configMBean
argument_list|,
literal|"exists"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"org.ops4j.pax.web"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"true"
argument_list|,
name|exist
argument_list|)
expr_stmt|;
name|jetty
operator|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
literal|"jetty.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"jetty.xml should exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|configDeleteViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|featureMBean
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=feature,name=root"
argument_list|)
decl_stmt|;
name|ObjectName
name|configMBean
init|=
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=config,name=root"
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|featureMBean
argument_list|,
literal|"installFeature"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"http"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|boolean
name|exist
init|=
operator|(
name|boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|configMBean
argument_list|,
literal|"exists"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"org.ops4j.pax.web"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"true"
argument_list|,
name|exist
argument_list|)
expr_stmt|;
name|File
name|jetty
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|,
literal|"jetty.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"jetty.xml should exist"
argument_list|,
name|jetty
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|featureMBean
argument_list|,
literal|"uninstallFeature"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"http"
block|,
literal|false
block|,
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"boolean"
block|,
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
name|exist
operator|=
operator|(
name|boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|configMBean
argument_list|,
literal|"exists"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"org.ops4j.pax.web"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"false"
argument_list|,
name|exist
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

