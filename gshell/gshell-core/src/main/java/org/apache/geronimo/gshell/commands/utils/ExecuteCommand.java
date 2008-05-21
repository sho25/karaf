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
name|geronimo
operator|.
name|gshell
operator|.
name|commands
operator|.
name|utils
package|;
end_package

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
name|OutputStream
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
name|command
operator|.
name|IO
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
name|common
operator|.
name|io
operator|.
name|PumpStreamHandler
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
name|common
operator|.
name|io
operator|.
name|StreamPumper
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
name|spring
operator|.
name|ProxyIO
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
name|support
operator|.
name|OsgiCommandSupport
import|;
end_import

begin_comment
comment|/**  * Execute system processes.  *  * @version $Rev: 593392 $ $Date: 2007-11-09 03:14:15 +0100 (Fri, 09 Nov 2007) $  */
end_comment

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"utils:exec"
argument_list|,
name|description
operator|=
literal|"Execute system processes"
argument_list|)
specifier|public
class|class
name|ExecuteCommand
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Argument"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|args
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|ProcessBuilder
name|builder
init|=
operator|new
name|ProcessBuilder
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Executing: {}"
argument_list|,
name|builder
operator|.
name|command
argument_list|()
argument_list|)
expr_stmt|;
name|Process
name|p
init|=
name|builder
operator|.
name|start
argument_list|()
decl_stmt|;
name|PumpStreamHandler
name|handler
init|=
operator|new
name|PumpStreamHandler
argument_list|(
name|io
operator|.
name|inputStream
argument_list|,
name|io
operator|.
name|outputStream
argument_list|,
name|io
operator|.
name|errorStream
argument_list|)
decl_stmt|;
name|handler
operator|.
name|attach
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|handler
operator|.
name|start
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Waiting for process to exit..."
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|p
operator|.
name|waitFor
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Process exited w/status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|handler
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|status
return|;
block|}
block|}
end_class

end_unit

