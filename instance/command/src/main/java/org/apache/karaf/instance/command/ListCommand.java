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
name|core
operator|.
name|Instance
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
name|table
operator|.
name|ShellTable
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
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists all existing container instances."
argument_list|)
specifier|public
class|class
name|ListCommand
extends|extends
name|InstanceCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-l"
argument_list|,
name|aliases
operator|=
block|{
literal|"--location"
block|}
argument_list|,
name|description
operator|=
literal|"Displays the location of the container instances"
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
name|location
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|aliases
operator|=
block|{
literal|"--java-opts"
block|}
argument_list|,
name|description
operator|=
literal|"Displays the Java options used to launch the JVM"
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
name|javaOpts
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
name|refreshInstance
argument_list|()
expr_stmt|;
name|Instance
index|[]
name|instances
init|=
name|getInstanceService
argument_list|()
operator|.
name|getInstances
argument_list|()
decl_stmt|;
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"SSH Port"
argument_list|)
operator|.
name|alignRight
argument_list|()
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"RMI Registry"
argument_list|)
operator|.
name|alignRight
argument_list|()
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"RMI Server"
argument_list|)
operator|.
name|alignRight
argument_list|()
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"State"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"PID"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
name|getRightColumnHeader
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Instance
name|instance
range|:
name|instances
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|instance
operator|.
name|getSshPort
argument_list|()
argument_list|,
name|instance
operator|.
name|getRmiRegistryPort
argument_list|()
argument_list|,
name|instance
operator|.
name|getRmiServerPort
argument_list|()
argument_list|,
name|instance
operator|.
name|getState
argument_list|()
argument_list|,
name|instance
operator|.
name|getPid
argument_list|()
argument_list|,
name|getRightColumnValue
argument_list|(
name|instance
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|print
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
specifier|private
name|String
name|getRightColumnHeader
parameter_list|()
block|{
if|if
condition|(
name|javaOpts
condition|)
block|{
return|return
literal|"JavaOpts"
return|;
block|}
elseif|else
if|if
condition|(
name|location
condition|)
block|{
return|return
literal|"Location"
return|;
block|}
else|else
block|{
return|return
literal|"Name"
return|;
block|}
block|}
specifier|private
name|String
name|getRightColumnValue
parameter_list|(
name|Instance
name|instance
parameter_list|)
block|{
if|if
condition|(
name|javaOpts
condition|)
block|{
return|return
name|instance
operator|.
name|getJavaOpts
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|location
condition|)
block|{
return|return
name|instance
operator|.
name|getLocation
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|instance
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

