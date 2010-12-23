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
name|karaf
operator|.
name|shell
operator|.
name|ssh
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
name|OutputStream
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
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
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
name|CommandSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
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
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Environment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|ExitCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|SessionAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|session
operator|.
name|ServerSession
import|;
end_import

begin_class
specifier|public
class|class
name|ShellCommandFactory
implements|implements
name|CommandFactory
block|{
specifier|private
name|CommandProcessor
name|commandProcessor
decl_stmt|;
specifier|public
name|void
name|setCommandProcessor
parameter_list|(
name|CommandProcessor
name|commandProcessor
parameter_list|)
block|{
name|this
operator|.
name|commandProcessor
operator|=
name|commandProcessor
expr_stmt|;
block|}
specifier|public
name|Command
name|createCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
return|return
operator|new
name|ShellCommand
argument_list|(
name|command
argument_list|)
return|;
block|}
specifier|public
class|class
name|ShellCommand
implements|implements
name|Command
implements|,
name|SessionAware
block|{
specifier|private
name|String
name|command
decl_stmt|;
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|private
name|OutputStream
name|out
decl_stmt|;
specifier|private
name|OutputStream
name|err
decl_stmt|;
specifier|private
name|ExitCallback
name|callback
decl_stmt|;
specifier|private
name|ServerSession
name|session
decl_stmt|;
specifier|public
name|ShellCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
specifier|public
name|void
name|setInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
specifier|public
name|void
name|setOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
specifier|public
name|void
name|setErrorStream
parameter_list|(
name|OutputStream
name|err
parameter_list|)
block|{
name|this
operator|.
name|err
operator|=
name|err
expr_stmt|;
block|}
specifier|public
name|void
name|setExitCallback
parameter_list|(
name|ExitCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
specifier|public
name|void
name|setSession
parameter_list|(
name|ServerSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|Environment
name|env
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|CommandSession
name|session
init|=
name|commandProcessor
operator|.
name|createSession
argument_list|(
name|in
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|out
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|err
argument_list|)
argument_list|)
decl_stmt|;
name|Subject
name|subject
init|=
name|this
operator|.
name|session
operator|!=
literal|null
condition|?
name|this
operator|.
name|session
operator|.
name|getAttribute
argument_list|(
name|KarafJaasPasswordAuthenticator
operator|.
name|SUBJECT_ATTRIBUTE_KEY
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Subject
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|(
literal|"Unable to start shell"
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|close
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onExit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{ 		}
block|}
specifier|private
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
modifier|...
name|closeables
parameter_list|)
block|{
for|for
control|(
name|Closeable
name|c
range|:
name|closeables
control|)
block|{
try|try
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
end_class

end_unit

