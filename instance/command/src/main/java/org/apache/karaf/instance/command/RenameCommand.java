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
name|command
package|;
end_package

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
name|completers
operator|.
name|InstanceCompleter
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
name|commands
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
name|karaf
operator|.
name|shell
operator|.
name|commands
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
name|commands
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
name|inject
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"instance"
argument_list|,
name|name
operator|=
literal|"rename"
argument_list|,
name|description
operator|=
literal|"Rename an existing container instance."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|RenameCommand
extends|extends
name|InstanceCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|aliases
operator|=
block|{
literal|"--verbose"
block|}
argument_list|,
name|description
operator|=
literal|"Display actions performed by the command (disabled by default)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|verbose
init|=
literal|false
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
literal|"name"
argument_list|,
name|description
operator|=
literal|"The name of the container instance to rename"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completer
argument_list|(
name|InstanceCompleter
operator|.
name|class
argument_list|)
name|String
name|instance
init|=
literal|null
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
literal|"new-name"
argument_list|,
name|description
operator|=
literal|"The new name of the container instance"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|newName
init|=
literal|null
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstanceService
argument_list|()
operator|.
name|renameInstance
argument_list|(
name|instance
argument_list|,
name|newName
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

