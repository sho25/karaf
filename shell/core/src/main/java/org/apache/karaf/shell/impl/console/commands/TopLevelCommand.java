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
name|console
operator|.
name|commands
package|;
end_package

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
name|List
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
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|CommandException
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
operator|.
name|COLOR_DEFAULT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
operator|.
name|COLOR_RED
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|TopLevelCommand
implements|implements
name|Command
block|{
annotation|@
name|Override
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|Session
operator|.
name|SCOPE_GLOBAL
return|;
block|}
annotation|@
name|Override
specifier|public
name|Completer
name|getCompleter
parameter_list|(
name|boolean
name|scoped
parameter_list|)
block|{
return|return
literal|null
return|;
comment|//        return new StringsCompleter(new String[] { getName() });
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|Session
name|session
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|arguments
operator|.
name|contains
argument_list|(
literal|"--help"
argument_list|)
condition|)
block|{
name|printHelp
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|arguments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
name|COLOR_RED
operator|+
literal|"Error executing command "
operator|+
name|INTENSITY_BOLD
operator|+
name|getName
argument_list|()
operator|+
name|INTENSITY_NORMAL
operator|+
name|COLOR_DEFAULT
operator|+
literal|": "
operator|+
literal|"too many arguments specified"
decl_stmt|;
throw|throw
operator|new
name|CommandException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|doExecute
argument_list|(
name|session
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|printHelp
parameter_list|(
name|PrintStream
name|out
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
name|INTENSITY_BOLD
operator|+
literal|"DESCRIPTION"
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"        "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INTENSITY_BOLD
operator|+
name|getName
argument_list|()
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INTENSITY_BOLD
operator|+
literal|"SYNTAX"
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"        "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|getName
argument_list|()
operator|+
literal|" [options]"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INTENSITY_BOLD
operator|+
literal|"OPTIONS"
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"        "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INTENSITY_BOLD
operator|+
literal|"--help"
operator|+
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"                "
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Display this help message"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|doExecute
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

