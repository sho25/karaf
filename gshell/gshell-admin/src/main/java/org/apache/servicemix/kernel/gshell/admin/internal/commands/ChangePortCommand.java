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
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
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
name|Argument
import|;
end_import

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"admin:change-port"
argument_list|,
name|description
operator|=
literal|"Change the port of an instance"
argument_list|)
specifier|public
class|class
name|ChangePortCommand
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
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"The new port"
argument_list|)
specifier|private
name|int
name|port
init|=
literal|0
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|getExistingInstance
argument_list|(
name|instance
argument_list|)
operator|.
name|changePort
argument_list|(
name|port
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

