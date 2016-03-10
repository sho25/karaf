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
name|shell
operator|.
name|api
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

begin_comment
comment|/**  * The<code>SessionFactory</code> can be used to create  * {@link Session} to execute commands.  *  * The {@link org.apache.karaf.shell.api.console.Registry} associated  * with this<code>SessionFactory</code> will contain:<ul>  *<li>{@link SessionFactory}</li>  *<li>{@link Registry}</li>  *<li>{@link Command}s</li>  *</ul>  */
end_comment

begin_interface
specifier|public
interface|interface
name|SessionFactory
block|{
comment|/**      * Retrieve the {@link Registry} used by this<code>SessionFactory</code>.      *      * @return a registry built by the factory.      */
name|Registry
name|getRegistry
parameter_list|()
function_decl|;
comment|/**      * Create new interactive session.      *      * @param in the input stream, can be<code>null</code> if the session is only used to execute a command using {@link Session#execute(CharSequence)}      * @param out the output stream      * @param err the error stream      * @param term the {@link Terminal} to use, may be<code>null</code>      * @param encoding the encoding to use for the input stream, may be<code>null</code>      * @param closeCallback a callback to be called when the session is closed, may be<code>null</code>      * @return the new session      */
name|Session
name|create
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Terminal
name|term
parameter_list|,
name|String
name|encoding
parameter_list|,
name|Runnable
name|closeCallback
parameter_list|)
function_decl|;
comment|/**      * Create a new headless session.      * Headless session can only be used to execute commands, so that      * {@link org.apache.karaf.shell.api.console.Session#run()} can not be used.      *      * @param in the input stream, can be<code>null</code> if the session is only used to execute a command using {@link Session#execute(CharSequence)}      * @param out the output stream      * @param err the error stream      * @return the new session      */
name|Session
name|create
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
function_decl|;
comment|/**      * Create a new headless session inheriting from the parent session.      * Headless session can only be used to execute commands, so that      * {@link org.apache.karaf.shell.api.console.Session#run()} can not be used.      * All variables and the terminal properties from the parent session will be available.      *      * @param in the input stream, can be<code>null</code> if the session is only used to execute a command using {@link Session#execute(CharSequence)}      * @param out the output stream      * @param err the error stream      * @param session the parent session      * @return the new session      */
name|Session
name|create
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|,
name|Session
name|session
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

