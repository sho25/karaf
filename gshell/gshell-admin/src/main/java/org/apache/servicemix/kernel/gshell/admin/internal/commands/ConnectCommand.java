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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|admin
operator|.
name|internal
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|ShellContextHolder
import|;
end_import

begin_class
specifier|public
class|class
name|ConnectCommand
extends|extends
name|AdminCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"The instance name"
argument_list|)
specifier|private
name|String
name|instance
init|=
literal|null
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
block|{
literal|"--username"
block|}
argument_list|,
name|token
operator|=
literal|"USERNAME"
argument_list|,
name|description
operator|=
literal|"Remote user name"
argument_list|)
specifier|private
name|String
name|username
init|=
literal|"smx"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--password"
block|}
argument_list|,
name|token
operator|=
literal|"PASSWORD"
argument_list|,
name|description
operator|=
literal|"Remote user password"
argument_list|)
specifier|private
name|String
name|password
init|=
literal|"smx"
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|port
init|=
name|getExistingInstance
argument_list|(
name|instance
argument_list|)
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|ShellContextHolder
operator|.
name|get
argument_list|()
operator|.
name|getShell
argument_list|()
operator|.
name|execute
argument_list|(
literal|"ssh -l "
operator|+
name|username
operator|+
literal|" -P "
operator|+
name|password
operator|+
literal|" -p "
operator|+
name|port
operator|+
literal|" localhost"
argument_list|)
expr_stmt|;
return|return
name|Result
operator|.
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

