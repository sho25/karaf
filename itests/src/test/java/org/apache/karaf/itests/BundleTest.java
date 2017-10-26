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
name|TabularDataSupport
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
name|bundle
operator|.
name|core
operator|.
name|BundleService
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
name|BundleTest
extends|extends
name|KarafTestSupport
block|{
specifier|private
specifier|static
specifier|final
name|RolePrincipal
index|[]
name|ADMIN_ROLES
init|=
block|{
operator|new
name|RolePrincipal
argument_list|(
name|BundleService
operator|.
name|SYSTEM_BUNDLES_ROLE
argument_list|)
block|,
operator|new
name|RolePrincipal
argument_list|(
literal|"admin"
argument_list|)
block|,
operator|new
name|RolePrincipal
argument_list|(
literal|"manager"
argument_list|)
block|}
decl_stmt|;
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
name|listOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:list -t 0"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|listOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|listOutput
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
literal|"org.apache.karaf:type=bundle,name=root"
argument_list|)
decl_stmt|;
name|TabularDataSupport
name|value
init|=
operator|(
name|TabularDataSupport
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"Bundles"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|value
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
name|capabilitiesCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|allCapabilitiesOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:capabilities"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|allCapabilitiesOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|allCapabilitiesOutput
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|jmxWhiteboardBundleCapabilitiesOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:capabilities org.apache.aries.jmx.whiteboard"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|jmxWhiteboardBundleCapabilitiesOutput
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"osgi.wiring.bundle; org.apache.aries.jmx.whiteboard 1.1.5 [UNUSED]"
argument_list|,
name|jmxWhiteboardBundleCapabilitiesOutput
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|classesCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|allClassesOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:classes"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|allClassesOutput
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|jmxWhiteboardBundleClassesOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:classes org.apache.aries.jmx.whiteboard"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|jmxWhiteboardBundleClassesOutput
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"org/apache/aries/jmx/whiteboard/Activator$MBeanTracker.class"
argument_list|,
name|jmxWhiteboardBundleClassesOutput
argument_list|)
expr_stmt|;
block|}
comment|/**      * TODO We need some more thorough tests for diag      */
annotation|@
name|Test
specifier|public
name|void
name|diagCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|allDiagOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:diag"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|allDiagOutput
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
name|findClassCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|findClassOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:find-class jmx"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|findClassOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|findClassOutput
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
name|headersCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|headersOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:headers org.apache.aries.jmx.whiteboard"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|headersOutput
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"Bundle-Activator = org.apache.aries.jmx.whiteboard.Activator"
argument_list|,
name|headersOutput
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|infoCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|infoOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:info org.apache.karaf.management.server"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|infoOutput
argument_list|)
expr_stmt|;
name|assertContains
argument_list|(
literal|"This bundle starts the Karaf embedded MBean server"
argument_list|,
name|infoOutput
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
name|executeCommand
argument_list|(
literal|"bundle:install mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.commons-lang/2.4_6"
argument_list|,
name|ADMIN_ROLES
argument_list|)
expr_stmt|;
name|assertBundleInstalled
argument_list|(
literal|"org.apache.servicemix.bundles.commons-lang"
argument_list|)
expr_stmt|;
name|executeCommand
argument_list|(
literal|"bundle:uninstall org.apache.servicemix.bundles.commons-lang"
argument_list|,
name|ADMIN_ROLES
argument_list|)
expr_stmt|;
name|assertBundleNotInstalled
argument_list|(
literal|"org.apache.servicemix.bundles.commons-lang"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|showTreeCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|bundleTreeOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:tree-show org.apache.karaf.management.server"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|bundleTreeOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|bundleTreeOutput
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
name|statusCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|statusOutput
init|=
name|executeCommand
argument_list|(
literal|"bundle:status org.apache.karaf.management.server"
argument_list|,
name|ADMIN_ROLES
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|statusOutput
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|statusOutput
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

