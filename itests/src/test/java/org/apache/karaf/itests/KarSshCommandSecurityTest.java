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

begin_comment
comment|/**  * This test exercises the Shell Command ACL for the kar scope commands as defined in  * /framework/src/main/resources/resources/etc/org.apache.karaf.command.acl.kar.cfg  */
end_comment

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
name|KarSshCommandSecurityTest
extends|extends
name|SshCommandTestBase
block|{
specifier|private
specifier|static
name|int
name|counter
init|=
literal|0
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testKarCommandSecurityViaSsh
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|vieweruser
init|=
literal|"view"
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
literal|"_"
operator|+
name|counter
operator|++
decl_stmt|;
name|addViewer
argument_list|(
name|vieweruser
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"kar:list"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"kar:install"
argument_list|,
name|Result
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"kar:uninstall"
argument_list|,
name|Result
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
literal|"karaf"
argument_list|,
literal|"kar:list"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
literal|"karaf"
argument_list|,
literal|"kar:install"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
literal|"karaf"
argument_list|,
literal|"kar:uninstall"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

