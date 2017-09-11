begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|impl
operator|.
name|action
operator|.
name|command
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|runtime
operator|.
name|CommandProcessorImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|runtime
operator|.
name|threadio
operator|.
name|ThreadIOImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|threadio
operator|.
name|ThreadIO
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Action
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Argument
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Option
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
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
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|SessionFactory
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
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|HeadlessSessionImpl
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
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|SessionFactoryImpl
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
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|parsing
operator|.
name|KarafParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|ParsedLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|Parser
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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|InputStream
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|ActionMaskingCallbackTest
block|{
specifier|private
name|Parser
name|parser
decl_stmt|;
specifier|private
name|ActionMaskingCallback
name|cb
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|ThreadIO
name|tio
init|=
operator|new
name|ThreadIOImpl
argument_list|()
decl_stmt|;
name|CommandProcessor
name|cp
init|=
operator|new
name|CommandProcessorImpl
argument_list|(
name|tio
argument_list|)
decl_stmt|;
name|SessionFactory
name|sf
init|=
operator|new
name|SessionFactoryImpl
argument_list|(
name|tio
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|PrintStream
name|os
init|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
operator|new
name|HeadlessSessionImpl
argument_list|(
name|sf
argument_list|,
name|cp
argument_list|,
name|is
argument_list|,
name|os
argument_list|,
name|os
argument_list|)
decl_stmt|;
name|parser
operator|=
operator|new
name|KarafParser
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|ActionCommand
name|cmd
init|=
operator|new
name|ActionCommand
argument_list|(
literal|null
argument_list|,
name|UserAddCommand
operator|.
name|class
argument_list|)
decl_stmt|;
name|cb
operator|=
name|ActionMaskingCallback
operator|.
name|build
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaasUserAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|check
argument_list|(
literal|"user-add user password "
argument_list|,
literal|"user-add user ######## "
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --help user password "
argument_list|,
literal|"user-add --help user ######## "
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --help user password foo"
argument_list|,
literal|"user-add --help user ######## foo"
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --opt1 user password foo"
argument_list|,
literal|"user-add --opt1 user ######## foo"
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --opt2 valOpt2 user password foo"
argument_list|,
literal|"user-add --opt2 valOpt2 user ######## foo"
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --opt2=valOpt2 user password foo"
argument_list|,
literal|"user-add --opt2=valOpt2 user ######## foo"
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --opt1 --opt2 valOpt2 --opt3=valOpt3 user password foo"
argument_list|,
literal|"user-add --opt1 --opt2 valOpt2 --opt3=@@@@@@@ user ######## foo"
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"user-add --opt1 --opt2 valOpt2 --opt3 valOpt3 user password foo"
argument_list|,
literal|"user-add --opt1 --opt2 valOpt2 --opt3 @@@@@@@ user ######## foo"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|check
parameter_list|(
name|String
name|input
parameter_list|,
name|String
name|expected
parameter_list|)
block|{
name|String
name|output
init|=
name|cb
operator|.
name|filter
argument_list|(
name|input
argument_list|,
name|parser
operator|.
name|parse
argument_list|(
name|input
argument_list|,
name|input
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jaas"
argument_list|,
name|name
operator|=
literal|"user-add"
argument_list|,
name|description
operator|=
literal|"Add a user"
argument_list|)
annotation|@
name|Service
specifier|public
specifier|static
class|class
name|UserAddCommand
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--opt1"
argument_list|)
specifier|private
name|boolean
name|opt1
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--opt2"
argument_list|)
specifier|private
name|String
name|opt2
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--opt3"
argument_list|,
name|censor
operator|=
literal|true
argument_list|,
name|mask
operator|=
literal|'@'
argument_list|)
specifier|private
name|String
name|opt3
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"username"
argument_list|)
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"password"
argument_list|,
name|censor
operator|=
literal|true
argument_list|,
name|mask
operator|=
literal|'#'
argument_list|)
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|2
argument_list|,
name|name
operator|=
literal|"foo"
argument_list|)
specifier|private
name|String
name|foo
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

