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
name|console
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
name|PrintStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

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
name|service
operator|.
name|command
operator|.
name|CommandProcessor
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ConsoleFactory
block|{
name|Console
name|create
parameter_list|(
name|CommandProcessor
name|processor
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
specifier|final
name|Terminal
name|term
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|)
function_decl|;
name|Console
name|createLocal
parameter_list|(
name|CommandProcessor
name|processor
parameter_list|,
name|Terminal
name|terminal
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|)
function_decl|;
name|void
name|startConsoleAs
parameter_list|(
name|Console
name|console
parameter_list|,
name|Subject
name|subject
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

