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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
package|;
end_package

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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|Shell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|application
operator|.
name|ApplicationManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|io
operator|.
name|SystemOutputHijacker
import|;
end_import

begin_class
specifier|public
class|class
name|Test
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"startLocalConsole"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ClassPathXmlApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"META-INF/spring/gshell.xml"
block|,
literal|"META-INF/spring/gshell-vfs.xml"
block|,
literal|"META-INF/spring/gshell-commands.xml"
block|,
literal|"org/apache/servicemix/kernel/gshell/core/gshell-test.xml"
block|}
argument_list|)
decl_stmt|;
name|ApplicationManager
name|appMgr
init|=
operator|(
name|ApplicationManager
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"applicationManager"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|appMgr
argument_list|)
expr_stmt|;
name|Shell
name|shell
init|=
name|appMgr
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|shell
argument_list|)
expr_stmt|;
name|shell
operator|.
name|execute
argument_list|(
literal|"help"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

