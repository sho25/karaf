begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|instance
operator|.
name|main
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|instance
operator|.
name|command
operator|.
name|InstanceCommandSupport
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
name|instance
operator|.
name|core
operator|.
name|InstanceService
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
name|instance
operator|.
name|core
operator|.
name|internal
operator|.
name|InstanceServiceImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IAnswer
import|;
end_import

begin_class
specifier|public
class|class
name|ExecuteTest
extends|extends
name|TestCase
block|{
specifier|private
name|String
name|userDir
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Execute
operator|.
name|exitAllowed
operator|=
literal|false
expr_stmt|;
name|userDir
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|Execute
operator|.
name|exitAllowed
operator|=
literal|true
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"user.dir"
argument_list|,
name|userDir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testListCommands
parameter_list|()
throws|throws
name|Exception
block|{
name|PrintStream
name|oldOut
init|=
name|System
operator|.
name|out
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|capturedOut
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|System
operator|.
name|setOut
argument_list|(
name|capturedOut
argument_list|)
expr_stmt|;
try|try
block|{
name|Execute
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"0"
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"list"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"create"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"destroy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setOut
argument_list|(
name|oldOut
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testNonexistingCommand
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Execute
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"bheuaark"
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"-1"
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testNoStorageFile
parameter_list|()
throws|throws
name|Exception
block|{
name|PrintStream
name|oldErr
init|=
name|System
operator|.
name|err
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|capturedErr
init|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|System
operator|.
name|setErr
argument_list|(
name|capturedErr
argument_list|)
expr_stmt|;
try|try
block|{
name|Execute
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"create"
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"-1"
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"karaf.instances"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"instance.properties"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setErr
argument_list|(
name|oldErr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testSetDir
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|oldProps
init|=
operator|(
name|Properties
operator|)
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
specifier|final
name|File
name|tempFile
init|=
name|createTempDir
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Precondition failed"
argument_list|,
name|tempFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|getCanonicalPath
argument_list|()
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"karaf.instances"
argument_list|,
name|tempFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Execute
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"list"
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tempFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|getCanonicalPath
argument_list|()
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setProperties
argument_list|(
name|oldProps
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Postcondition failed"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.instances"
argument_list|)
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testExecute
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|File
name|tempFile
init|=
name|createTempDir
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"ssh.port"
argument_list|,
literal|"1302"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"rmi.registry.port"
argument_list|,
literal|"1122"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"rmi.server.port"
argument_list|,
literal|"44444"
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|tempFile
argument_list|,
name|InstanceServiceImpl
operator|.
name|STORAGE_FILE
argument_list|)
argument_list|)
decl_stmt|;
name|p
operator|.
name|store
argument_list|(
name|fos
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
specifier|final
name|List
argument_list|<
name|InstanceServiceImpl
argument_list|>
name|instanceServices
init|=
operator|new
name|ArrayList
argument_list|<
name|InstanceServiceImpl
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|InstanceCommandSupport
name|mockCommand
init|=
name|EasyMock
operator|.
name|createStrictMock
argument_list|(
name|InstanceCommandSupport
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockCommand
operator|.
name|setInstanceService
argument_list|(
operator|(
name|InstanceService
operator|)
name|EasyMock
operator|.
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
name|InstanceServiceImpl
name|instanceService
init|=
operator|(
name|InstanceServiceImpl
operator|)
name|EasyMock
operator|.
name|getCurrentArguments
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
name|tempFile
argument_list|,
name|instanceService
operator|.
name|getStorageLocation
argument_list|()
argument_list|)
expr_stmt|;
name|instanceServices
operator|.
name|add
argument_list|(
name|instanceService
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mockCommand
operator|.
name|execute
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|answer
parameter_list|()
throws|throws
name|Throwable
block|{
comment|// The Instances Service should be initialized at this point.
comment|// One way to find this out is by reading out the port number
name|InstanceServiceImpl
name|instanceService
init|=
name|instanceServices
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Field
name|sshField
init|=
name|InstanceServiceImpl
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"defaultSshPortStart"
argument_list|)
decl_stmt|;
name|sshField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1302
argument_list|,
name|sshField
operator|.
name|get
argument_list|(
name|instanceService
argument_list|)
argument_list|)
expr_stmt|;
name|Field
name|rmiRegistryField
init|=
name|InstanceServiceImpl
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"defaultRmiRegistryPortStart"
argument_list|)
decl_stmt|;
name|rmiRegistryField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1122
argument_list|,
name|rmiRegistryField
operator|.
name|get
argument_list|(
name|instanceService
argument_list|)
argument_list|)
expr_stmt|;
name|Field
name|rmiServerField
init|=
name|InstanceServiceImpl
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"defaultRmiServerPortStart"
argument_list|)
decl_stmt|;
name|rmiServerField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|44444
argument_list|,
name|rmiServerField
operator|.
name|get
argument_list|(
name|instanceService
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mockCommand
argument_list|)
expr_stmt|;
name|Execute
operator|.
name|execute
argument_list|(
name|mockCommand
argument_list|,
name|tempFile
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"test"
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mockCommand
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|delete
argument_list|(
name|tempFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|File
name|createTempDir
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|File
name|tempFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tempFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tempFile
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
return|return
name|tempFile
operator|.
name|getCanonicalFile
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|delete
parameter_list|(
name|File
name|tmp
parameter_list|)
block|{
if|if
condition|(
name|tmp
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
for|for
control|(
name|File
name|f
range|:
name|tmp
operator|.
name|listFiles
argument_list|()
control|)
block|{
name|delete
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|tmp
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

