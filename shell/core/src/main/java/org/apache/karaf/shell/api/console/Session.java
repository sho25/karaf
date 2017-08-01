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
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_comment
comment|/**  * A<code>Session</code> can be used to execute commands.  *  * The {@link org.apache.karaf.shell.api.console.Registry} associated  * with this<code>Session</code> will contain:<ul>  *<li>{@link SessionFactory}</li>  *<li>{@link Command}s</li>  *<li>{@link Session}</li>  *<li>{@link Registry}</li>  *<li>{@link History}</li>  *<li>{@link Terminal}</li>  *</ul>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Session
extends|extends
name|Runnable
extends|,
name|Closeable
block|{
comment|//
comment|// Session properties
comment|//
comment|// Property names starting with "karaf." are reserved for karaf
comment|//
name|String
name|SCOPE
init|=
literal|"SCOPE"
decl_stmt|;
name|String
name|SUBSHELL
init|=
literal|"SUBSHELL"
decl_stmt|;
name|String
name|PRINT_STACK_TRACES
init|=
literal|"karaf.printStackTraces"
decl_stmt|;
name|String
name|LAST_EXCEPTION
init|=
literal|"karaf.lastException"
decl_stmt|;
name|String
name|IGNORE_INTERRUPTS
init|=
literal|"karaf.ignoreInterrupts"
decl_stmt|;
name|String
name|IS_LOCAL
init|=
literal|"karaf.shell.local"
decl_stmt|;
name|String
name|COMPLETION_MODE
init|=
literal|"karaf.completionMode"
decl_stmt|;
name|String
name|COMPLETION_MODE_GLOBAL
init|=
literal|"global"
decl_stmt|;
name|String
name|COMPLETION_MODE_SUBSHELL
init|=
literal|"subshell"
decl_stmt|;
name|String
name|COMPLETION_MODE_FIRST
init|=
literal|"first"
decl_stmt|;
name|String
name|SCOPE_GLOBAL
init|=
literal|"*"
decl_stmt|;
comment|/**      * Execute a program in this session.      *      * @param commandline the provided command line      * @return the result of the execution      * @throws Exception in case of execution failure.      */
name|Object
name|execute
parameter_list|(
name|CharSequence
name|commandline
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get the value of a variable.      *      * @param name the key name in the session      * @return the corresponding object      */
name|Object
name|get
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Set the value of a variable.      *      * @param name  Name of the variable.      * @param value Value of the variable      */
name|void
name|put
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Return the input stream that is the first of the pipeline. This stream is      * sometimes necessary to communicate directly to the end user. For example,      * a "less" or "more" command needs direct input from the keyboard to      * control the paging.      *      * @return InputStream used closest to the user or null if input is from a      *         file.      */
name|InputStream
name|getKeyboard
parameter_list|()
function_decl|;
comment|/**      * Return the PrintStream for the console. This must always be the stream      * "closest" to the user. This stream can be used to post messages that      * bypass the piping. If the output is piped to a file, then the object      * returned must be null.      *      * @return the console stream      */
name|PrintStream
name|getConsole
parameter_list|()
function_decl|;
comment|/**      * Prompt the user for a line.      *      * @param prompt the session prompt      * @param mask the session mask      * @return the corresponding line      * @throws java.io.IOException in case of prompting failure      */
name|String
name|readLine
parameter_list|(
name|String
name|prompt
parameter_list|,
specifier|final
name|Character
name|mask
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Retrieve the {@link org.apache.karaf.shell.api.console.Terminal} associated      * with this<code>Session</code> or<code>null</code> if this<code>Session</code>      * is headless.      *      * @return the session terminal      */
name|Terminal
name|getTerminal
parameter_list|()
function_decl|;
comment|/**      * Retrieve the {@link org.apache.karaf.shell.api.console.History} associated      * with this<code>Session</code> or<code>null</code> if this<code>Session</code>      * is headless.      *      * @return the session history      */
name|History
name|getHistory
parameter_list|()
function_decl|;
comment|/**      * Retrieve the {@link org.apache.karaf.shell.api.console.Registry} associated      * with this<code>Session</code>.      *      * @return the session registry      */
name|Registry
name|getRegistry
parameter_list|()
function_decl|;
comment|/**      * Retrieve the {@link org.apache.karaf.shell.api.console.SessionFactory} associated      * with this<code>Session</code>.      *      * @return the session factory      */
name|SessionFactory
name|getFactory
parameter_list|()
function_decl|;
comment|/**      * Resolve a command name.  If the command name has no specified scope, the fully      * qualified command name will be returned, depending on the scopes and current      * subshell.      *      * @param name the command name      * @return the full qualified command name      */
name|String
name|resolveCommand
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|Path
name|currentDir
parameter_list|()
function_decl|;
name|void
name|currentDir
parameter_list|(
name|Path
name|path
parameter_list|)
function_decl|;
comment|/**      * Close this session. After the session is closed, it will throw      * IllegalStateException when it is used.      */
name|void
name|close
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

