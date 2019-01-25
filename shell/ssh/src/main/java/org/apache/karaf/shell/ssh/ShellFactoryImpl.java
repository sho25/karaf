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
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|common
operator|.
name|Factory
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

begin_comment
comment|/**  * SSHD {@link org.apache.sshd.server.Command} factory which provides access to  * Shell.  */
end_comment

begin_class
specifier|public
class|class
name|ShellFactoryImpl
implements|implements
name|Factory
argument_list|<
name|Command
argument_list|>
block|{
specifier|private
name|SessionFactory
name|sessionFactory
decl_stmt|;
specifier|public
name|ShellFactoryImpl
parameter_list|(
name|SessionFactory
name|sessionFactory
parameter_list|)
block|{
name|this
operator|.
name|sessionFactory
operator|=
name|sessionFactory
expr_stmt|;
block|}
specifier|public
name|Command
name|create
parameter_list|()
block|{
return|return
operator|new
name|ShellImpl
argument_list|()
return|;
block|}
specifier|public
class|class
name|ShellImpl
implements|implements
name|Command
implements|,
name|SessionAware
block|{
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
name|Session
name|shell
decl_stmt|;
specifier|private
name|SshTerminal
name|terminal
decl_stmt|;
specifier|private
name|boolean
name|closed
decl_stmt|;
specifier|public
name|void
name|setInputStream
parameter_list|(
specifier|final
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
specifier|final
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
specifier|final
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
name|Subject
name|subject
init|=
name|ShellImpl
operator|.
name|this
operator|.
name|session
operator|!=
literal|null
condition|?
name|ShellImpl
operator|.
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
name|String
name|encoding
init|=
name|getEncoding
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|terminal
operator|=
operator|new
name|SshTerminal
argument_list|(
name|env
argument_list|,
name|in
argument_list|,
name|out
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
specifier|final
name|PrintStream
name|pout
init|=
operator|new
name|PrintStream
argument_list|(
name|terminal
operator|.
name|output
argument_list|()
argument_list|,
literal|true
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
specifier|final
name|PrintStream
name|perr
init|=
name|err
operator|instanceof
name|PrintStream
condition|?
operator|(
name|PrintStream
operator|)
name|err
else|:
name|out
operator|==
name|err
condition|?
name|pout
else|:
operator|new
name|PrintStream
argument_list|(
name|err
argument_list|,
literal|true
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|shell
operator|=
name|sessionFactory
operator|.
name|create
argument_list|(
name|in
argument_list|,
name|pout
argument_list|,
name|perr
argument_list|,
name|terminal
argument_list|,
name|encoding
argument_list|,
name|this
operator|::
name|destroy
argument_list|)
expr_stmt|;
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
name|shell
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
name|JaasHelper
operator|.
name|runAs
argument_list|(
name|subject
argument_list|,
parameter_list|()
lambda|->
operator|new
name|Thread
argument_list|(
name|shell
argument_list|,
literal|"Karaf ssh console user "
operator|+
name|ShellUtil
operator|.
name|getCurrentUserName
argument_list|()
argument_list|)
operator|.
name|start
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to start shell"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
name|flush
argument_list|(
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
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
block|}
comment|/**      * Get the default encoding.  Will first look at the LC_CTYPE environment variable, then the input.encoding      * system property, then the default charset according to the JVM.      *      * @return The default encoding to use when none is specified.      */
specifier|public
specifier|static
name|String
name|getEncoding
parameter_list|(
name|Environment
name|env
parameter_list|)
block|{
comment|// LC_CTYPE is usually in the form en_US.UTF-8
name|String
name|ctype
init|=
name|env
operator|.
name|getEnv
argument_list|()
operator|.
name|getOrDefault
argument_list|(
literal|"LC_TYPE"
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
literal|"LC_CTYPE"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|envEncoding
init|=
name|extractEncodingFromCtype
argument_list|(
name|ctype
argument_list|)
decl_stmt|;
if|if
condition|(
name|envEncoding
operator|!=
literal|null
condition|)
block|{
return|return
name|envEncoding
return|;
block|}
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"input.encoding"
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Parses the LC_CTYPE value to extract the encoding according to the POSIX standard, which says that the LC_CTYPE      * environment variable may be of the format<code>[language[_territory][.codeset][@modifier]]</code>      *      * @param ctype The ctype to parse, may be null      * @return The encoding, if one was present, otherwise null      */
specifier|static
name|String
name|extractEncodingFromCtype
parameter_list|(
name|String
name|ctype
parameter_list|)
block|{
if|if
condition|(
name|ctype
operator|!=
literal|null
operator|&&
name|ctype
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|String
name|encodingAndModifier
init|=
name|ctype
operator|.
name|substring
argument_list|(
name|ctype
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|encodingAndModifier
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|encodingAndModifier
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|encodingAndModifier
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|encodingAndModifier
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|void
name|flush
parameter_list|(
name|OutputStream
modifier|...
name|streams
parameter_list|)
block|{
for|for
control|(
name|OutputStream
name|s
range|:
name|streams
control|)
block|{
try|try
block|{
name|s
operator|.
name|flush
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
name|Exception
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

