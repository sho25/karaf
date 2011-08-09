begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|console
package|;
end_package

begin_import
import|import
name|jline
operator|.
name|Terminal
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
name|CommandProcessorImpl
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
name|console
operator|.
name|jline
operator|.
name|Console
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

begin_comment
comment|/**  * This class is mostly here so that folks can see an example of how you can extend the Karaf Main shell.  Also  * lets Karaf developers see how changes the Main class can affect the interface comparability  * with sub classes.  */
end_comment

begin_class
specifier|public
class|class
name|ExampleSubclassMain
extends|extends
name|Main
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|ExampleSubclassMain
name|main
init|=
operator|new
name|ExampleSubclassMain
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|ExampleSubclassMain
parameter_list|()
block|{
comment|// Sets the name of the shell and the current user.
name|setApplication
argument_list|(
literal|"example"
argument_list|)
expr_stmt|;
name|setUser
argument_list|(
literal|"unknown"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Console
name|createConsole
parameter_list|(
name|CommandProcessorImpl
name|commandProcessor
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Terminal
name|terminal
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|Console
argument_list|(
name|commandProcessor
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|,
name|terminal
argument_list|,
literal|null
argument_list|)
block|{
comment|/**              * If you don't overwrite, then karaf will use the welcome message found in the              * following resource files:              *<ul>              *<li>org/apache/karaf/shell/console/branding.properties</li>              *<li>org/apache/karaf/branding/branding.properties</li>              *<ul>              */
annotation|@
name|Override
specifier|protected
name|void
name|welcome
parameter_list|()
block|{
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
literal|"==============================================="
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
literal|" Example Shell "
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConsole
argument_list|()
operator|.
name|println
argument_list|(
literal|"==============================================="
argument_list|)
expr_stmt|;
block|}
comment|/**              * If you don't overwrite then Karaf builds a prompt based on the current app and user.              * @return              */
annotation|@
name|Override
specifier|protected
name|String
name|getPrompt
parameter_list|()
block|{
return|return
literal|"example>"
return|;
block|}
comment|/**              * If you don't overwrite, then karaf automatically adds session properties              * found in the following resource files:              *<ul>              *<li>org/apache/karaf/shell/console/branding.properties</li>              *<li>org/apache/karaf/branding/branding.properties</li>              *<ul>              */
annotation|@
name|Override
specifier|protected
name|void
name|setSessionProperties
parameter_list|()
block|{
comment|// we won't add any session properties.
block|}
block|}
return|;
block|}
comment|/**      * if you don't override, then Karaf will discover the commands listed in the      * "META-INF/services/org/apache/karaf/shell/commands" resource file.      *      * @return      */
annotation|@
name|Override
specifier|public
name|String
name|getDiscoveryResource
parameter_list|()
block|{
return|return
literal|"META-INF/services/org/example/commands.index"
return|;
block|}
block|}
end_class

end_unit

