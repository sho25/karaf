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
name|admin
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
name|File
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
name|Arrays
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Action
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
name|Command
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
name|DefaultActionPreparator
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
name|admin
operator|.
name|command
operator|.
name|AdminCommandSupport
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
name|admin
operator|.
name|command
operator|.
name|ChangePortCommand
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
name|admin
operator|.
name|command
operator|.
name|CreateCommand
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
name|admin
operator|.
name|command
operator|.
name|DestroyCommand
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
name|admin
operator|.
name|command
operator|.
name|ListCommand
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
name|admin
operator|.
name|command
operator|.
name|StartCommand
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
name|admin
operator|.
name|command
operator|.
name|StopCommand
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
name|admin
operator|.
name|internal
operator|.
name|AdminServiceImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|AnsiConsole
import|;
end_import

begin_class
specifier|public
class|class
name|Execute
block|{
specifier|static
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|x
init|=
name|CreateCommand
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|COMMAND_CLASSES
init|=
operator|new
name|Class
index|[]
block|{
name|CreateCommand
operator|.
name|class
block|,
name|StartCommand
operator|.
name|class
block|,
name|StopCommand
operator|.
name|class
block|,
name|DestroyCommand
operator|.
name|class
block|,
name|ListCommand
operator|.
name|class
block|,
name|ChangePortCommand
operator|.
name|class
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|COMMANDS
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|COMMAND_CLASSES
control|)
block|{
name|Command
name|ann
init|=
name|c
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|COMMANDS
operator|.
name|put
argument_list|(
name|ann
operator|.
name|name
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
comment|// For testing
specifier|static
name|boolean
name|exitAllowed
init|=
literal|true
decl_stmt|;
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|AnsiConsole
operator|.
name|systemInstall
argument_list|()
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|listCommands
argument_list|()
expr_stmt|;
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|COMMANDS
operator|.
name|get
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Command not found: "
operator|+
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|storage
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.instances"
argument_list|)
decl_stmt|;
if|if
condition|(
name|storage
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"System property 'karaf.instances' is not set. \n"
operator|+
literal|"This property needs to be set to the full path of the instance.properties file."
argument_list|)
expr_stmt|;
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|File
name|storageFile
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"user.dir"
argument_list|,
name|storageFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|command
init|=
name|cls
operator|.
name|newInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|command
operator|instanceof
name|AdminCommandSupport
condition|)
block|{
name|execute
argument_list|(
operator|(
name|AdminCommandSupport
operator|)
name|command
argument_list|,
name|storageFile
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Not an admin command: "
operator|+
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
name|void
name|execute
parameter_list|(
name|AdminCommandSupport
name|command
parameter_list|,
name|File
name|storageFile
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|DefaultActionPreparator
name|dap
init|=
operator|new
name|DefaultActionPreparator
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
argument_list|)
decl_stmt|;
name|params
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// this is the actual command name
if|if
condition|(
operator|!
name|dap
operator|.
name|prepare
argument_list|(
name|command
argument_list|,
literal|null
argument_list|,
name|params
argument_list|)
condition|)
block|{
return|return;
block|}
name|AdminServiceImpl
name|admin
init|=
operator|new
name|AdminServiceImpl
argument_list|()
decl_stmt|;
name|admin
operator|.
name|setStorageLocation
argument_list|(
name|storageFile
argument_list|)
expr_stmt|;
name|admin
operator|.
name|init
argument_list|()
expr_stmt|;
name|command
operator|.
name|setAdminService
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|listCommands
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Available commands:"
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|COMMANDS
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Command
name|ann
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Command
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"  %s - %s\n"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|ann
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Type 'command --help' for more help on the specified command."
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|exit
parameter_list|(
name|int
name|rc
parameter_list|)
block|{
if|if
condition|(
name|exitAllowed
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
name|rc
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|""
operator|+
name|rc
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

