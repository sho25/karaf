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
name|impl
operator|.
name|console
operator|.
name|TerminalFactory
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
name|SessionFactory
name|sessionFactory
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|TerminalFactory
name|terminalFactory
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
specifier|public
name|LocalConsoleManager
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|TerminalFactory
name|terminalFactory
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
name|terminalFactory
operator|=
name|terminalFactory
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
name|jline
operator|.
name|Terminal
name|terminal
init|=
name|terminalFactory
operator|.
name|getTerminal
argument_list|()
decl_stmt|;
specifier|final
name|Runnable
name|callback
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
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
block|}
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
operator|new
name|PrivilegedAction
argument_list|<
name|Session
argument_list|>
argument_list|()
block|{
specifier|public
name|Session
name|run
parameter_list|()
block|{
name|String
name|encoding
init|=
name|getEncoding
argument_list|()
decl_stmt|;
name|session
operator|=
name|sessionFactory
operator|.
name|create
argument_list|(
name|StreamWrapUtil
operator|.
name|reWrapIn
argument_list|(
name|terminal
argument_list|,
name|System
operator|.
name|in
argument_list|)
argument_list|,
name|StreamWrapUtil
operator|.
name|reWrap
argument_list|(
name|System
operator|.
name|out
argument_list|)
argument_list|,
name|StreamWrapUtil
operator|.
name|reWrap
argument_list|(
name|System
operator|.
name|err
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
name|callback
argument_list|)
expr_stmt|;
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
expr_stmt|;
name|String
name|name
init|=
literal|"Karaf local console user "
operator|+
name|ShellUtil
operator|.
name|getCurrentUserName
argument_list|()
decl_stmt|;
name|boolean
name|delayconsole
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.delay.console"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|delayconsole
condition|)
block|{
name|DelayedStarted
name|watcher
init|=
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
decl_stmt|;
operator|new
name|Thread
argument_list|(
name|watcher
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
return|return
name|session
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// TODO: register the local session so that ssh can add the agent
comment|//        registration = bundleContext.register(CommandSession.class, console.getSession(), null);
block|}
comment|/**      * Get the default encoding.  Will first look at the LC_CTYPE environment variable, then the input.encoding      * system property, then the default charset according to the JVM.      *      * @return The default encoding to use when none is specified.      */
specifier|public
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
literal|"karaf.local.user"
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
name|String
name|roles
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.local.roles"
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
block|}
block|}
end_class

end_unit

