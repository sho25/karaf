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
name|EnumSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|apache
operator|.
name|sshd
operator|.
name|client
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
name|channel
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
name|client
operator|.
name|channel
operator|.
name|ClientChannelEvent
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
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|session
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
name|client
operator|.
name|session
operator|.
name|ClientSession
operator|.
name|ClientSessionEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
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
name|Assert
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
name|SshCommandTestBase
extends|extends
name|KarafTestSupport
block|{
enum|enum
name|Result
block|{
name|OK
block|,
name|NOT_FOUND
block|,
name|NO_CREDENTIALS
block|}
specifier|private
name|SshClient
name|client
decl_stmt|;
specifier|private
name|ClientChannel
name|channel
decl_stmt|;
specifier|private
name|ClientSession
name|session
decl_stmt|;
specifier|private
name|HashSet
argument_list|<
name|Feature
argument_list|>
name|featuresBefore
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
argument_list|<>
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
name|void
name|addViewer
parameter_list|(
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
name|String
name|sshPort
init|=
name|getSshPort
argument_list|()
decl_stmt|;
name|Awaitility
operator|.
name|await
argument_list|()
operator|.
name|ignoreExceptions
argument_list|()
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
block|{
name|ConnectFuture
name|future
init|=
name|client
operator|.
name|connect
argument_list|(
name|username
argument_list|,
literal|"localhost"
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|sshPort
argument_list|)
argument_list|)
decl_stmt|;
name|future
operator|.
name|await
argument_list|()
expr_stmt|;
name|session
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ClientSessionEvent
argument_list|>
name|ret
init|=
name|EnumSet
operator|.
name|of
argument_list|(
name|ClientSessionEvent
operator|.
name|WAIT_AUTH
argument_list|)
decl_stmt|;
while|while
condition|(
name|ret
operator|.
name|contains
argument_list|(
name|ClientSessionEvent
operator|.
name|WAIT_AUTH
argument_list|)
condition|)
block|{
name|session
operator|.
name|addPasswordIdentity
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|session
operator|.
name|auth
argument_list|()
operator|.
name|verify
argument_list|()
expr_stmt|;
name|ret
operator|=
name|session
operator|.
name|waitFor
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|ClientSessionEvent
operator|.
name|WAIT_AUTH
argument_list|,
name|ClientSessionEvent
operator|.
name|CLOSED
argument_list|,
name|ClientSessionEvent
operator|.
name|AUTHED
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ret
operator|.
name|contains
argument_list|(
name|ClientSessionEvent
operator|.
name|CLOSED
argument_list|)
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
name|EnumSet
operator|.
name|of
argument_list|(
name|ClientChannelEvent
operator|.
name|CLOSED
argument_list|)
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

