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
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedOutputStream
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
name|HashSet
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|features
operator|.
name|Feature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|ClientChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|ClientSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|SshClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|future
operator|.
name|ConnectFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|SshCommandSecurityTest
extends|extends
name|KarafTestSupport
block|{
specifier|private
enum|enum
name|Result
block|{
name|OK
block|,
name|NOT_FOUND
block|,
name|NO_CREDENTIALS
block|}
empty_stmt|;
specifier|private
specifier|static
name|int
name|counter
init|=
literal|0
decl_stmt|;
specifier|private
name|HashSet
argument_list|<
name|Feature
argument_list|>
name|featuresBefore
decl_stmt|;
specifier|private
name|SshClient
name|client
decl_stmt|;
specifier|private
name|ClientSession
name|session
decl_stmt|;
specifier|private
name|ClientChannel
name|channel
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|installSshFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|featuresBefore
operator|=
operator|new
name|HashSet
argument_list|<
name|Feature
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|featureService
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|installAndAssertFeature
argument_list|(
literal|"ssh"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|uninstallSshFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|uninstallNewFeatures
argument_list|(
name|featuresBefore
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBundleCommandSecurityViaSsh
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|manageruser
init|=
literal|"man"
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
name|addUsers
argument_list|(
name|manageruser
argument_list|,
name|vieweruser
argument_list|)
expr_stmt|;
comment|// TODO viewer user
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:refresh -f 999"
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:refresh 999"
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
literal|"bundle:refresh -f 999"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:restart -f 999"
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:restart 999"
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
literal|"bundle:restart -f 999"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:start -f 999"
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:start 999"
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
literal|"bundle:start -f 999"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:stop -f 999"
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:stop 999"
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
literal|"bundle:stop -f 999"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:uninstall -f 999"
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:uninstall 999"
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
literal|"bundle:uninstall -f 999"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:update -f 999"
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:update 999"
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
literal|"bundle:update -f 999"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|assertCommand
argument_list|(
name|manageruser
argument_list|,
literal|"bundle:install xyz"
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
literal|"bundle:install xyz"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigCommandSecurityViaSsh
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|manageruser
init|=
literal|"man"
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
name|addUsers
argument_list|(
name|manageruser
argument_list|,
name|vieweruser
argument_list|)
expr_stmt|;
comment|// A viewer cannot do anything to ConfigAdmin
name|assertCommand
argument_list|(
name|vieweruser
argument_list|,
literal|"config:edit cfg."
operator|+
name|vieweruser
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
literal|"config:delete cfg."
operator|+
name|vieweruser
argument_list|,
name|Result
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
name|manageruser
argument_list|,
name|Result
operator|.
name|OK
argument_list|,
literal|"cfg."
operator|+
name|manageruser
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
name|manageruser
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|,
literal|"jmx.acl.test_"
operator|+
name|counter
operator|++
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
name|manageruser
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|,
literal|"org.apache.karaf.command.acl.test_"
operator|+
name|counter
operator|++
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
name|manageruser
argument_list|,
name|Result
operator|.
name|NO_CREDENTIALS
argument_list|,
literal|"org.apache.karaf.service.acl.test_"
operator|+
name|counter
operator|++
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
literal|"karaf"
argument_list|,
name|Result
operator|.
name|OK
argument_list|,
literal|"cfg.karaf_"
operator|+
name|counter
operator|++
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
literal|"karaf"
argument_list|,
name|Result
operator|.
name|OK
argument_list|,
literal|"org.apache.karaf.command.acl.test_"
operator|+
name|counter
operator|++
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testConfigEdits
argument_list|(
literal|"karaf"
argument_list|,
name|Result
operator|.
name|OK
argument_list|,
literal|"org.apache.karaf.service.acl.test_"
operator|+
name|counter
operator|++
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testConfigEdits
parameter_list|(
name|String
name|user
parameter_list|,
name|Result
name|expectedEditResult
parameter_list|,
name|String
name|pid
parameter_list|,
name|boolean
name|isAdmin
parameter_list|)
throws|throws
name|Exception
throws|,
name|IOException
block|{
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:edit "
operator|+
name|pid
operator|+
literal|"\n"
operator|+
literal|"config:property-set x y\n"
operator|+
literal|"config:property-set a b\n"
operator|+
literal|"config:property-append x z\n"
operator|+
literal|"config:update"
argument_list|,
name|expectedEditResult
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedEditResult
operator|!=
name|Result
operator|.
name|OK
condition|)
comment|// If we're expecting failure, don't continue any further...
return|return;
name|String
name|result
init|=
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:edit "
operator|+
name|pid
operator|+
literal|"\n"
operator|+
literal|"config:property-list\n"
operator|+
literal|"config:cancel"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"x = yz"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
operator|.
name|contains
argument_list|(
literal|"a = b"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|result2
init|=
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:edit "
operator|+
name|pid
operator|+
literal|"\n"
operator|+
literal|"config:property-delete a\n"
operator|+
literal|"config:property-list\n"
operator|+
literal|"config:update"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result2
operator|.
name|contains
argument_list|(
literal|"x = yz"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result2
operator|.
name|contains
argument_list|(
literal|"a = b"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAdmin
condition|)
block|{
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:delete "
operator|+
name|pid
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
name|String
name|result3
init|=
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:edit "
operator|+
name|pid
operator|+
literal|"\n"
operator|+
literal|"config:property-list"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result3
operator|.
name|contains
argument_list|(
literal|"x = yz"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result3
operator|.
name|contains
argument_list|(
literal|"a = b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:delete "
operator|+
name|pid
argument_list|,
name|Result
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|String
name|result3
init|=
name|assertCommand
argument_list|(
name|user
argument_list|,
literal|"config:edit "
operator|+
name|pid
operator|+
literal|"\n"
operator|+
literal|"config:property-list"
argument_list|,
name|Result
operator|.
name|OK
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"The delete command should have had no effect"
argument_list|,
name|result3
operator|.
name|contains
argument_list|(
literal|"x = yz"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result3
operator|.
name|contains
argument_list|(
literal|"a = b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|assertCommand
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|command
parameter_list|,
name|Result
name|result
parameter_list|)
throws|throws
name|Exception
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|command
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
name|command
operator|+=
literal|"\n"
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|OutputStream
name|pipe
init|=
name|openSshChannel
argument_list|(
name|user
argument_list|,
name|user
argument_list|,
name|out
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|pipe
operator|.
name|write
argument_list|(
name|command
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|flush
argument_list|()
expr_stmt|;
name|closeSshChannel
argument_list|(
name|pipe
argument_list|)
expr_stmt|;
name|String
name|output
init|=
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|OK
case|:
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"Should not contain 'Insufficient credentials' or 'Command not found': "
operator|+
name|output
argument_list|,
name|output
operator|.
name|contains
argument_list|(
literal|"Insufficient credentials"
argument_list|)
operator|||
name|output
operator|.
name|contains
argument_list|(
literal|"Command not found"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|NOT_FOUND
case|:
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Should contain 'Command not found': "
operator|+
name|output
argument_list|,
name|output
operator|.
name|contains
argument_list|(
literal|"Command not found"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_CREDENTIALS
case|:
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Should contain 'Insufficient credentials': "
operator|+
name|output
argument_list|,
name|output
operator|.
name|contains
argument_list|(
literal|"Insufficient credentials"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
name|Assert
operator|.
name|fail
argument_list|(
literal|"Unexpected enum value: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|output
return|;
block|}
specifier|private
name|void
name|addUsers
parameter_list|(
name|String
name|manageruser
parameter_list|,
name|String
name|vieweruser
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|OutputStream
name|pipe
init|=
name|openSshChannel
argument_list|(
literal|"karaf"
argument_list|,
literal|"karaf"
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|pipe
operator|.
name|write
argument_list|(
operator|(
literal|"jaas:realm-manage --realm=karaf"
operator|+
literal|";jaas:user-add "
operator|+
name|manageruser
operator|+
literal|" "
operator|+
name|manageruser
operator|+
literal|";jaas:role-add "
operator|+
name|manageruser
operator|+
literal|" manager"
operator|+
literal|";jaas:role-add "
operator|+
name|manageruser
operator|+
literal|" viewer"
operator|+
literal|";jaas:user-add "
operator|+
name|vieweruser
operator|+
literal|" "
operator|+
name|vieweruser
operator|+
literal|";jaas:role-add "
operator|+
name|vieweruser
operator|+
literal|" viewer"
operator|+
literal|";jaas:update;jaas:realm-manage --realm=karaf;jaas:user-list\n"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|flush
argument_list|()
expr_stmt|;
name|closeSshChannel
argument_list|(
name|pipe
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|OutputStream
name|openSshChannel
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|OutputStream
modifier|...
name|outputs
parameter_list|)
throws|throws
name|Exception
block|{
name|client
operator|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
name|ConnectFuture
name|future
init|=
name|client
operator|.
name|connect
argument_list|(
literal|"localhost"
argument_list|,
literal|8101
argument_list|)
operator|.
name|await
argument_list|()
decl_stmt|;
name|session
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|int
name|ret
init|=
name|ClientSession
operator|.
name|WAIT_AUTH
decl_stmt|;
while|while
condition|(
operator|(
name|ret
operator|&
name|ClientSession
operator|.
name|WAIT_AUTH
operator|)
operator|!=
literal|0
condition|)
block|{
name|session
operator|.
name|authPassword
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|ret
operator|=
name|session
operator|.
name|waitFor
argument_list|(
name|ClientSession
operator|.
name|WAIT_AUTH
operator||
name|ClientSession
operator|.
name|CLOSED
operator||
name|ClientSession
operator|.
name|AUTHED
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ret
operator|&
name|ClientSession
operator|.
name|CLOSED
operator|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Could not open SSH channel"
argument_list|)
throw|;
block|}
name|channel
operator|=
name|session
operator|.
name|createChannel
argument_list|(
literal|"shell"
argument_list|)
expr_stmt|;
name|PipedOutputStream
name|pipe
init|=
operator|new
name|PipedOutputStream
argument_list|()
decl_stmt|;
name|channel
operator|.
name|setIn
argument_list|(
operator|new
name|PipedInputStream
argument_list|(
name|pipe
argument_list|)
argument_list|)
expr_stmt|;
name|OutputStream
name|out
decl_stmt|;
if|if
condition|(
name|outputs
operator|.
name|length
operator|>=
literal|1
condition|)
block|{
name|out
operator|=
name|outputs
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
name|channel
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|OutputStream
name|err
decl_stmt|;
if|if
condition|(
name|outputs
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
name|err
operator|=
name|outputs
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|err
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
name|channel
operator|.
name|setErr
argument_list|(
name|err
argument_list|)
expr_stmt|;
name|channel
operator|.
name|open
argument_list|()
expr_stmt|;
return|return
name|pipe
return|;
block|}
specifier|private
name|void
name|closeSshChannel
parameter_list|(
name|OutputStream
name|pipe
parameter_list|)
throws|throws
name|IOException
block|{
name|pipe
operator|.
name|write
argument_list|(
literal|"logout\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|flush
argument_list|()
expr_stmt|;
name|channel
operator|.
name|waitFor
argument_list|(
name|ClientChannel
operator|.
name|CLOSED
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
name|channel
operator|=
literal|null
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

