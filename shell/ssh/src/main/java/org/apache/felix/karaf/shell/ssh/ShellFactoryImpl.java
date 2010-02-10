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
name|felix
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
name|FilterOutputStream
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
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|Completer
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
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|completer
operator|.
name|AggregateCompleter
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
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|jline
operator|.
name|Console
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
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|ReifiedType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
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
name|osgi
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
import|;
end_import

begin_comment
comment|/**  * SSHD {@link org.apache.sshd.server.Command} factory which provides access to Shell.  *  * @version $Rev: 731517 $ $Date: 2009-01-05 11:25:19 +0100 (Mon, 05 Jan 2009) $  */
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
name|CommandProcessor
name|commandProcessor
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
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
name|void
name|setCompleters
parameter_list|(
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
parameter_list|)
block|{
name|this
operator|.
name|completers
operator|=
name|completers
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
name|Callable
argument_list|<
name|Boolean
argument_list|>
name|printStackTraces
init|=
operator|new
name|Callable
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
specifier|public
name|Boolean
name|call
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|Console
operator|.
name|PRINT_STACK_TRACES
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Console
name|console
init|=
operator|new
name|Console
argument_list|(
name|commandProcessor
argument_list|,
name|in
argument_list|,
operator|new
name|PrintStream
argument_list|(
operator|new
name|LfToCrLfFilterOutputStream
argument_list|(
name|out
argument_list|)
argument_list|,
literal|true
argument_list|)
argument_list|,
operator|new
name|PrintStream
argument_list|(
operator|new
name|LfToCrLfFilterOutputStream
argument_list|(
name|err
argument_list|)
argument_list|,
literal|true
argument_list|)
argument_list|,
operator|new
name|SshTerminal
argument_list|(
name|env
argument_list|)
argument_list|,
operator|new
name|AggregateCompleter
argument_list|(
name|completers
argument_list|)
argument_list|,
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|printStackTraces
argument_list|)
decl_stmt|;
name|CommandSession
name|session
init|=
name|console
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"APPLICATION"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|,
literal|"root"
argument_list|)
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
operator|new
name|Thread
argument_list|(
name|console
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|ShellFactoryImpl
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
literal|0
argument_list|)
expr_stmt|;
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
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
specifier|public
specifier|static
name|Converter
name|getConverter
parameter_list|()
block|{
return|return
operator|new
name|Converter
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|Converter
implements|implements
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|Converter
block|{
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Object
name|sourceObject
parameter_list|,
name|ReifiedType
name|targetType
parameter_list|)
block|{
return|return
name|ShellFactoryImpl
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|sourceObject
operator|.
name|getClass
argument_list|()
argument_list|)
operator|&&
name|Factory
operator|.
name|class
operator|.
name|equals
argument_list|(
name|targetType
operator|.
name|getRawClass
argument_list|()
argument_list|)
operator|&&
name|Command
operator|.
name|class
operator|.
name|equals
argument_list|(
name|targetType
operator|.
name|getActualTypeArgument
argument_list|(
literal|0
argument_list|)
operator|.
name|getRawClass
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Object
name|convert
parameter_list|(
name|Object
name|sourceObject
parameter_list|,
name|ReifiedType
name|targetType
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|sourceObject
return|;
block|}
block|}
specifier|public
class|class
name|LfToCrLfFilterOutputStream
extends|extends
name|FilterOutputStream
block|{
specifier|private
name|boolean
name|lastWasCr
decl_stmt|;
specifier|public
name|LfToCrLfFilterOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|super
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|lastWasCr
operator|&&
name|b
operator|==
literal|'\n'
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|'\r'
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
name|lastWasCr
operator|=
name|b
operator|==
literal|'\r'
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

