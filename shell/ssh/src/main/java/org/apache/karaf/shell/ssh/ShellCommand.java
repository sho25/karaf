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
name|CharArrayWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|InputStreamReader
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
name|io
operator|.
name|Reader
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
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
name|console
operator|.
name|SessionFactory
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
name|ShellUtil
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
name|util
operator|.
name|StreamUtils
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
name|util
operator|.
name|jaas
operator|.
name|JaasHelper
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ShellCommand
implements|implements
name|Command
implements|,
name|SessionAware
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SHELL_INIT_SCRIPT
init|=
literal|"karaf.shell.init.script"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ShellCommand
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
index|[]
name|SECURITY_BUGFIX
init|=
block|{
name|JaasHelper
operator|.
name|class
block|,
name|JaasHelper
operator|.
name|OsgiSubjectDomainCombiner
operator|.
name|class
block|,
name|JaasHelper
operator|.
name|DelegatingProtectionDomain
operator|.
name|class
block|,             }
decl_stmt|;
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
specifier|private
name|SessionFactory
name|sessionFactory
decl_stmt|;
specifier|public
name|ShellCommand
parameter_list|(
name|SessionFactory
name|sessionFactory
parameter_list|,
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|sessionFactory
operator|=
name|sessionFactory
expr_stmt|;
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
name|int
name|exitStatus
init|=
literal|0
decl_stmt|;
try|try
block|{
specifier|final
name|Session
name|session
init|=
name|sessionFactory
operator|.
name|create
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|env
operator|.
name|getEnv
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|session
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
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
name|KarafJaasAuthenticator
operator|.
name|SUBJECT_ATTRIBUTE_KEY
argument_list|)
else|:
literal|null
decl_stmt|;
name|Object
name|result
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
name|String
name|scriptFileName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SHELL_INIT_SCRIPT
argument_list|)
decl_stmt|;
name|executeScript
argument_list|(
name|scriptFileName
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|result
operator|=
name|JaasHelper
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
return|return
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
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
name|String
name|scriptFileName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SHELL_INIT_SCRIPT
argument_list|)
decl_stmt|;
name|executeScript
argument_list|(
name|scriptFileName
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
comment|// TODO: print the result of the command ?
comment|//                    session.getConsole().println(session.format(result, Converter.INSPECT));
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|exitStatus
operator|=
literal|1
expr_stmt|;
name|ShellUtil
operator|.
name|logException
argument_list|(
name|session
argument_list|,
name|t
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
name|exitStatus
operator|=
literal|1
expr_stmt|;
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
name|StreamUtils
operator|.
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
name|exitStatus
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{ 	}
specifier|private
name|void
name|executeScript
parameter_list|(
name|String
name|scriptFileName
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
if|if
condition|(
name|scriptFileName
operator|!=
literal|null
condition|)
block|{
name|Reader
name|r
init|=
literal|null
decl_stmt|;
try|try
block|{
name|File
name|scriptFile
init|=
operator|new
name|File
argument_list|(
name|scriptFileName
argument_list|)
decl_stmt|;
name|r
operator|=
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|scriptFile
argument_list|)
argument_list|)
expr_stmt|;
name|CharArrayWriter
name|w
init|=
operator|new
name|CharArrayWriter
argument_list|()
decl_stmt|;
name|int
name|n
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|8192
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|r
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|w
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|execute
argument_list|(
operator|new
name|String
argument_list|(
name|w
operator|.
name|toCharArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Error in initialization script"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|r
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
block|}
block|}
end_class

end_unit

