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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|core
package|;
end_package

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
name|Application
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
name|ApplicationConfiguration
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
name|event
operator|.
name|EventPublisher
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
name|wisdom
operator|.
name|application
operator|.
name|ShellCreatedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_class
specifier|public
class|class
name|ApplicationManagerImpl
implements|implements
name|ApplicationManager
block|{
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|EventPublisher
name|eventPublisher
decl_stmt|;
specifier|private
name|Application
name|application
decl_stmt|;
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
specifier|public
name|ApplicationManagerImpl
parameter_list|(
name|EventPublisher
name|eventPublisher
parameter_list|,
name|Application
name|application
parameter_list|,
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|eventPublisher
operator|=
name|eventPublisher
expr_stmt|;
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|SystemOutputHijacker
operator|.
name|isInstalled
argument_list|()
condition|)
block|{
name|SystemOutputHijacker
operator|.
name|install
argument_list|()
expr_stmt|;
block|}
comment|//SystemOutputHijacker.register(application.getIo().outputStream, application.getIo().errorStream);
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|SystemOutputHijacker
operator|.
name|uninstall
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|configure
parameter_list|(
name|ApplicationConfiguration
name|applicationConfiguration
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Application
name|getApplication
parameter_list|()
block|{
return|return
name|application
return|;
block|}
specifier|public
name|Shell
name|create
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Shell
name|shell
init|=
operator|(
name|Shell
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
literal|"shell"
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created shell instance: {}"
argument_list|,
name|shell
argument_list|)
expr_stmt|;
name|eventPublisher
operator|.
name|publish
argument_list|(
operator|new
name|ShellCreatedEvent
argument_list|(
name|shell
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|shell
return|;
block|}
block|}
end_class

end_unit

