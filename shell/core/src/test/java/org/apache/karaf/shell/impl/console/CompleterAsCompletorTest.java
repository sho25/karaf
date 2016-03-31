begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|console
package|;
end_package

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
name|PrintStream
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|CommandLine
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
name|Completer
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
name|junit
operator|.
name|Test
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

begin_class
specifier|public
class|class
name|CompleterAsCompletorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCompletionMultiCommand
parameter_list|()
block|{
name|SessionFactoryImpl
name|sessionFactory
init|=
operator|new
name|SessionFactoryImpl
argument_list|(
operator|new
name|ThreadIOImpl
argument_list|()
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
operator|new
name|HeadlessSessionImpl
argument_list|(
name|sessionFactory
argument_list|,
name|sessionFactory
operator|.
name|getCommandProcessor
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Completer
name|completer
init|=
operator|new
name|Completer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|" bundle:l"
argument_list|,
name|commandLine
operator|.
name|getBuffer
argument_list|()
argument_list|)
expr_stmt|;
name|candidates
operator|.
name|add
argument_list|(
literal|"bundle:list"
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
block|}
decl_stmt|;
name|jline
operator|.
name|console
operator|.
name|completer
operator|.
name|Completer
name|cmp
init|=
operator|new
name|CompleterAsCompletor
argument_list|(
name|session
argument_list|,
name|completer
argument_list|)
decl_stmt|;
name|String
name|cmd
init|=
literal|"bundle:list ; bundle:l"
decl_stmt|;
name|List
argument_list|<
name|CharSequence
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|cmp
operator|.
name|complete
argument_list|(
name|cmd
argument_list|,
name|cmd
operator|.
name|length
argument_list|()
argument_list|,
name|candidates
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bundle:list ; "
operator|.
name|length
argument_list|()
argument_list|,
name|pos
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
