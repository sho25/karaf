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
name|KarafFeatureCommandsTest
extends|extends
name|KarafTestSupport
block|{
comment|/*     public void testBootFeatures() throws Exception {      }     */
annotation|@
name|Test
specifier|public
name|void
name|testFeatureListCommand
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
name|testFeatureInstallUninstallCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|featureInstallOutput
init|=
name|executeCommand
argument_list|(
literal|"feature:install -v eventadmin"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|featureInstallOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|featureInstallOutput
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|featureListOutput
init|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep eventadmin"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|executeCommand
argument_list|(
literal|"feature:uninstall eventadmin"
argument_list|)
argument_list|)
expr_stmt|;
name|featureListOutput
operator|=
name|executeCommand
argument_list|(
literal|"feature:list -i | grep eventadmin"
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
name|assertTrue
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
name|testRepoAddRemoveCommand
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
literal|"feature:repo-add mvn:org.apache.karaf.cellar/apache-karaf-cellar/2.2.4/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|repoListOutput
init|=
name|executeCommand
argument_list|(
literal|"feature:repo-list"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|repoListOutput
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoListOutput
operator|.
name|contains
argument_list|(
literal|"apache-karaf-cellar"
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
literal|"feature:repo-remove mvn:org.apache.karaf.cellar/apache-karaf-cellar/2.2.4/xml/features"
argument_list|)
argument_list|)
expr_stmt|;
name|repoListOutput
operator|=
name|executeCommand
argument_list|(
literal|"feature:repo-list"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|repoListOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoListOutput
operator|.
name|contains
argument_list|(
literal|"apache-karaf-cellar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

