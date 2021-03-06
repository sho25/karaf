begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|subsystem
operator|.
name|commands
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
name|support
operator|.
name|table
operator|.
name|ShellTable
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
name|subsystem
operator|.
name|Subsystem
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"subsystem"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"List all subsystems"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ListAction
extends|extends
name|SubsystemSupport
implements|implements
name|Action
block|{
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"ID"
argument_list|)
operator|.
name|alignRight
argument_list|()
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"SymbolicName"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
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
literal|"Type"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Parents"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Children"
argument_list|)
expr_stmt|;
for|for
control|(
name|Subsystem
name|ss
range|:
name|getSubsystems
argument_list|()
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|ss
operator|.
name|getSubsystemId
argument_list|()
argument_list|,
name|ss
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|ss
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ss
operator|.
name|getState
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|getType
argument_list|(
name|ss
argument_list|)
argument_list|,
name|getSubsytemIds
argument_list|(
name|ss
operator|.
name|getParents
argument_list|()
argument_list|)
argument_list|,
name|getSubsytemIds
argument_list|(
name|ss
operator|.
name|getChildren
argument_list|()
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
name|getType
parameter_list|(
name|Subsystem
name|subsystem
parameter_list|)
block|{
name|String
name|type
init|=
name|subsystem
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"osgi.subsystem."
argument_list|)
condition|)
block|{
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|"osgi.subsystem."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

