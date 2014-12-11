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
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
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
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnector
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
name|FeatureTest
extends|extends
name|KarafTestSupport
block|{
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
name|JMXConnector
name|connector
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connector
operator|=
name|this
operator|.
name|getJMXConnector
argument_list|()
expr_stmt|;
name|MBeanServerConnection
name|connection
init|=
name|connector
operator|.
name|getMBeanServerConnection
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
name|connection
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
finally|finally
block|{
name|close
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
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
literal|"feature:install -v wrapper"
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
literal|"feature:uninstall wrapper"
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
name|installUninstallViaMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|JMXConnector
name|connector
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connector
operator|=
name|this
operator|.
name|getJMXConnector
argument_list|()
expr_stmt|;
name|MBeanServerConnection
name|connection
init|=
name|connector
operator|.
name|getMBeanServerConnection
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
name|connection
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
name|assertFeatureInstalled
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
name|connection
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
name|assertFeatureNotInstalled
argument_list|(
literal|"wrapper"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
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
literal|"feature:repo-remove .*apache-karaf-cellar.*"
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
name|JMXConnector
name|connector
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connector
operator|=
name|this
operator|.
name|getJMXConnector
argument_list|()
expr_stmt|;
name|MBeanServerConnection
name|connection
init|=
name|connector
operator|.
name|getMBeanServerConnection
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
name|connection
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
name|connection
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
finally|finally
block|{
name|close
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
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
literal|"feature:repo-refresh .*pax.*"
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
name|JMXConnector
name|connector
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connector
operator|=
name|this
operator|.
name|getJMXConnector
argument_list|()
expr_stmt|;
name|MBeanServerConnection
name|connection
init|=
name|connector
operator|.
name|getMBeanServerConnection
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
name|connection
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
finally|finally
block|{
name|close
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

