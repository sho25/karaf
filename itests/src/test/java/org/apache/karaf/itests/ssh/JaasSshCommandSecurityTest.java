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
name|ssh
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * This test exercises the Shell Command ACL for the jaas scope commands as defined in  * /framework/src/main/resources/resources/etc/org.apache.karaf.command.acl.jaas.cfg  */
end_comment

begin_class
specifier|public
class|class
name|JaasSshCommandSecurityTest
extends|extends
name|SshCommandTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testJaasCommandSecurityViaSsh
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|vieweruser
init|=
literal|"viewer"
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
literal|"_jaas"
decl_stmt|;
name|addViewer
argument_list|(
name|vieweruser
argument_list|)
expr_stmt|;
name|String
name|userName
init|=
literal|"XXX"
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"jaas:realm-manage --realm karaf;"
operator|+
literal|"jaas:user-add "
operator|+
name|userName
operator|+
literal|" pwd;"
operator|+
literal|"jaas:update"
argument_list|,
name|Result
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|String
name|r
init|=
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"jaas:realm-manage --realm karaf;"
operator|+
literal|"jaas:user-list"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The viewer should not have the credentials to add the new user"
argument_list|,
name|r
operator|.
name|contains
argument_list|(
name|userName
argument_list|)
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
literal|"karaf"
argument_list|,
literal|"jaas:realm-manage --realm karaf;"
operator|+
literal|"jaas:user-add "
operator|+
name|userName
operator|+
literal|" pwd;"
operator|+
literal|"jaas:update"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|String
name|r2
init|=
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"jaas:realm-manage --realm karaf;"
operator|+
literal|"jaas:user-list"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The admin user should have the rights to add the new user"
argument_list|,
name|r2
operator|.
name|contains
argument_list|(
name|userName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

