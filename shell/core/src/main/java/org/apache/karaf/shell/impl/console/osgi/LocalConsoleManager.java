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
name|impl
operator|.
name|console
operator|.
name|osgi
package|;
end_package

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
name|security
operator|.
name|PrivilegedAction
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|ClientPrincipal
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|RolePrincipal
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
name|jaas
operator|.
name|boot
operator|.
name|principal
operator|.
name|UserPrincipal
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
name|impl
operator|.
name|console
operator|.
name|JLineTerminal
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
name|jline
operator|.
name|terminal
operator|.
name|Terminal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|terminal
operator|.
name|TerminalBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
import|;
end_import

begin_class
specifier|public
class|class
name|LocalConsoleManager
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INPUT_ENCODING
init|=
literal|"input.encoding"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_DELAY_CONSOLE
init|=
literal|"karaf.delay.console"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_LOCAL_USER
init|=
literal|"karaf.local.user"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_LOCAL_ROLES
init|=
literal|"karaf.local.roles"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KARAF_LOCAL_ROLES_DEFAULT
init|=
literal|"admin,manager,viewer,systembundles"
decl_stmt|;
specifier|private
name|SessionFactory
name|sessionFactory
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|Session
name|session
decl_stmt|;
specifier|private
name|ServiceRegistration
argument_list|<
name|?
argument_list|>
name|registration
decl_stmt|;
specifier|private
name|boolean
name|closing
decl_stmt|;
specifier|private
name|DelayedStarted
name|watcher
decl_stmt|;
specifier|public
name|LocalConsoleManager
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|SessionFactory
name|sessionFactory
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|sessionFactory
operator|=
name|sessionFactory
expr_stmt|;
block|}
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Terminal
name|terminal
init|=
name|TerminalBuilder
operator|.
name|builder
argument_list|()
operator|.
name|nativeSignals
argument_list|(
literal|true
argument_list|)
operator|.
name|signalHandler
argument_list|(
name|Terminal
operator|.
name|SignalHandler
operator|.
name|SIG_IGN
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Subject
name|subject
init|=
name|createLocalKarafSubject
argument_list|()
decl_stmt|;
name|this
operator|.
name|session
operator|=
name|JaasHelper
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
call|(
name|PrivilegedAction
argument_list|<
name|Session
argument_list|>
call|)
argument_list|()
operator|->
block|{
name|String
name|encoding
operator|=
name|getEncoding
argument_list|()
block|;
name|session
operator|=
name|sessionFactory
operator|.
name|create
argument_list|(
name|terminal
operator|.
name|input
argument_list|()
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|terminal
operator|.
name|output
argument_list|()
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
name|terminal
operator|.
name|output
argument_list|()
argument_list|)
argument_list|,
operator|new
name|JLineTerminal
argument_list|(
name|terminal
argument_list|)
argument_list|,
name|encoding
argument_list|,
name|LocalConsoleManager
operator|.
name|this
operator|::
name|close
argument_list|)
block|;
name|session
operator|.
name|put
argument_list|(
name|Session
operator|.
name|IS_LOCAL
argument_list|,
literal|true
argument_list|)
block|;
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|Session
operator|.
name|class
argument_list|,
name|session
argument_list|,
literal|null
argument_list|)
block|;
name|String
name|name
operator|=
literal|"Karaf local console user "
operator|+
name|ShellUtil
operator|.
name|getCurrentUserName
argument_list|()
block|;
name|boolean
name|delayconsole
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|KARAF_DELAY_CONSOLE
argument_list|)
argument_list|)
block|;
if|if
condition|(
name|delayconsole
condition|)
block|{
name|watcher
operator|=
operator|new
name|DelayedStarted
argument_list|(
name|session
argument_list|,
name|name
argument_list|,
name|bundleContext
argument_list|,
name|System
operator|.
name|in
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
name|watcher
argument_list|,
name|name
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
operator|new
name|Thread
argument_list|(
name|session
argument_list|,
name|name
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
argument_list|return
name|session
argument_list|;
block|}
block|)
class|;
end_class

begin_comment
comment|// TODO: register the local session so that ssh can add the agent
end_comment

begin_comment
comment|//        registration = bundleContext.register(CommandSession.class, console.getSession(), null);
end_comment

begin_comment
unit|}
comment|/**      * Get the default encoding.  Will first look at the LC_CTYPE environment variable, then the input.encoding      * system property, then the default charset according to the JVM.      *      * @return The default encoding to use when none is specified.      */
end_comment

begin_function
unit|public
specifier|static
name|String
name|getEncoding
parameter_list|()
block|{
comment|// LC_CTYPE is usually in the form en_US.UTF-8
name|String
name|envEncoding
init|=
name|extractEncodingFromCtype
argument_list|(
name|System
operator|.
name|getenv
argument_list|(
literal|"LC_CTYPE"
argument_list|)
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
name|INPUT_ENCODING
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
end_function

begin_comment
comment|/**      * Parses the LC_CTYPE value to extract the encoding according to the POSIX standard, which says that the LC_CTYPE      * environment variable may be of the format<code>[language[_territory][.codeset][@modifier]]</code>      *      * @param ctype The ctype to parse, may be null      * @return The encoding, if one was present, otherwise null      */
end_comment

begin_function
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
end_function

begin_function
specifier|private
name|Subject
name|createLocalKarafSubject
parameter_list|()
block|{
name|String
name|userName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|KARAF_LOCAL_USER
argument_list|)
decl_stmt|;
if|if
condition|(
name|userName
operator|==
literal|null
condition|)
block|{
name|userName
operator|=
literal|"karaf"
expr_stmt|;
block|}
specifier|final
name|Subject
name|subject
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|UserPrincipal
argument_list|(
name|userName
argument_list|)
argument_list|)
expr_stmt|;
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ClientPrincipal
argument_list|(
literal|"local"
argument_list|,
literal|"localhost"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|roles
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|KARAF_LOCAL_ROLES
argument_list|,
name|KARAF_LOCAL_ROLES_DEFAULT
argument_list|)
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|role
range|:
name|roles
operator|.
name|split
argument_list|(
literal|"[,]"
argument_list|)
control|)
block|{
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
name|role
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|subject
return|;
block|}
end_function

begin_function
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|closing
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|registration
operator|!=
literal|null
condition|)
block|{
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
comment|// The bundle is stopped
comment|// so close the console and remove the callback so that the
comment|// osgi framework isn't stopped
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|watcher
operator|!=
literal|null
condition|)
block|{
name|watcher
operator|.
name|stopDelayed
argument_list|()
expr_stmt|;
block|}
block|}
end_function

begin_function
specifier|protected
name|void
name|close
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|closing
condition|)
block|{
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
end_function

unit|}
end_unit

