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
name|karaf
operator|.
name|shell
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
name|commands
operator|.
name|ansi
operator|.
name|SimpleAnsi
import|;
end_import

begin_comment
comment|/**  * Base class for exceptions thrown when executing commands.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Deprecated
specifier|public
class|class
name|CommandException
extends|extends
name|Exception
block|{
specifier|private
name|String
name|help
decl_stmt|;
specifier|public
name|CommandException
parameter_list|()
block|{     }
specifier|public
name|CommandException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CommandException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CommandException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CommandException
parameter_list|(
name|String
name|help
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|help
operator|=
name|help
expr_stmt|;
block|}
specifier|public
name|CommandException
parameter_list|(
name|String
name|help
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|help
operator|=
name|help
expr_stmt|;
block|}
specifier|public
name|String
name|getNiceHelp
parameter_list|()
block|{
return|return
name|help
operator|!=
literal|null
condition|?
name|help
else|:
name|SimpleAnsi
operator|.
name|COLOR_RED
operator|+
literal|"Error executing command: "
operator|+
operator|(
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
name|getMessage
argument_list|()
else|:
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|)
operator|+
name|SimpleAnsi
operator|.
name|COLOR_DEFAULT
return|;
block|}
block|}
end_class

end_unit

