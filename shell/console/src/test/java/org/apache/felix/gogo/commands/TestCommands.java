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
name|felix
operator|.
name|gogo
operator|.
name|commands
package|;
end_package

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
name|Arrays
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
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|basic
operator|.
name|SimpleCommand
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
name|CommandSession
import|;
end_import

begin_class
specifier|public
class|class
name|TestCommands
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testPrompt
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|c
init|=
operator|new
name|Context
argument_list|()
decl_stmt|;
name|c
operator|.
name|addCommand
argument_list|(
literal|"echo"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|c
operator|.
name|set
argument_list|(
literal|"USER"
argument_list|,
literal|"gnodet"
argument_list|)
expr_stmt|;
name|c
operator|.
name|set
argument_list|(
literal|"APPLICATION"
argument_list|,
literal|"karaf"
argument_list|)
expr_stmt|;
comment|//c.set("SCOPE", "");
name|Object
name|p
init|=
name|c
operator|.
name|execute
argument_list|(
literal|"echo \"@|bold ${USER}|@${APPLICATION}:@|bold ${SCOPE}|> \""
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Prompt: "
operator|+
name|p
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|c
init|=
operator|new
name|Context
argument_list|()
decl_stmt|;
name|c
operator|.
name|addCommand
argument_list|(
literal|"capture"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|c
operator|.
name|addCommand
argument_list|(
literal|"my-action"
argument_list|,
operator|new
name|SimpleCommand
argument_list|(
name|MyAction
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test help
name|Object
name|help
init|=
name|c
operator|.
name|execute
argument_list|(
literal|"my-action --help | capture"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|help
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|help
operator|)
operator|.
name|indexOf
argument_list|(
literal|"My Action"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|help
operator|)
operator|.
name|indexOf
argument_list|(
literal|"First option"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|help
operator|)
operator|.
name|indexOf
argument_list|(
literal|"Bundle ids"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
comment|// Test required argument
try|try
block|{
name|c
operator|.
name|execute
argument_list|(
literal|"my-action"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Action should have thrown an exception because of a missing argument"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CommandException
name|e
parameter_list|)
block|{         }
comment|// Test required argument
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|3
argument_list|)
argument_list|,
name|c
operator|.
name|execute
argument_list|(
literal|"my-action 3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test required argument
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|3
argument_list|)
argument_list|,
name|c
operator|.
name|execute
argument_list|(
literal|"my-action 3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test required argument
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|,
name|c
operator|.
name|execute
argument_list|(
literal|"my-action 3 5"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test option
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|4
argument_list|)
argument_list|,
name|c
operator|.
name|execute
argument_list|(
literal|"my-action -i 3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test option alias
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|4
argument_list|)
argument_list|,
name|c
operator|.
name|execute
argument_list|(
literal|"my-action --increment 3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|capture
parameter_list|()
throws|throws
name|IOException
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|BufferedReader
name|rdr
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|System
operator|.
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|rdr
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|sw
operator|.
name|write
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|s
operator|=
name|rdr
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
return|return
name|sw
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|CharSequence
name|echo
parameter_list|(
name|Object
name|args
index|[]
parameter_list|)
block|{
if|if
condition|(
name|args
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|del
init|=
literal|""
decl_stmt|;
for|for
control|(
name|Object
name|arg
range|:
name|args
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|del
argument_list|)
expr_stmt|;
if|if
condition|(
name|arg
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|arg
argument_list|)
expr_stmt|;
name|del
operator|=
literal|" "
expr_stmt|;
block|}
block|}
return|return
name|sb
return|;
block|}
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"test"
argument_list|,
name|name
operator|=
literal|"my-action"
argument_list|,
name|description
operator|=
literal|"My Action"
argument_list|)
specifier|public
specifier|static
class|class
name|MyAction
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--increment"
block|}
argument_list|,
name|description
operator|=
literal|"First option"
argument_list|)
specifier|private
name|boolean
name|increment
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"ids"
argument_list|,
name|description
operator|=
literal|"Bundle ids"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|ids
decl_stmt|;
specifier|public
name|Object
name|execute
parameter_list|(
name|CommandSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|increment
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ids
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|ids
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ids
return|;
block|}
block|}
block|}
end_class

end_unit

