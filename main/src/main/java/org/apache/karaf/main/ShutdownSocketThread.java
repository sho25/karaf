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
name|main
package|;
end_package

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
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessControlException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|launch
operator|.
name|Framework
import|;
end_import

begin_class
class|class
name|ShutdownSocketThread
extends|extends
name|Thread
block|{
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|shutdown
decl_stmt|;
specifier|private
name|Random
name|random
init|=
literal|null
decl_stmt|;
specifier|private
name|ServerSocket
name|shutdownSocket
decl_stmt|;
specifier|private
name|Framework
name|framework
decl_stmt|;
specifier|public
name|ShutdownSocketThread
parameter_list|(
name|String
name|shutdown
parameter_list|,
name|ServerSocket
name|shutdownSocket
parameter_list|,
name|Framework
name|framework
parameter_list|)
block|{
name|super
argument_list|(
literal|"Karaf Shutdown Socket Thread"
argument_list|)
expr_stmt|;
name|this
operator|.
name|shutdown
operator|=
name|shutdown
expr_stmt|;
name|this
operator|.
name|shutdownSocket
operator|=
name|shutdownSocket
expr_stmt|;
name|this
operator|.
name|framework
operator|=
name|framework
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
comment|// Wait for the next connection
name|Socket
name|socket
init|=
literal|null
decl_stmt|;
name|InputStream
name|stream
init|=
literal|null
decl_stmt|;
name|long
name|acceptStartTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
try|try
block|{
name|socket
operator|=
name|shutdownSocket
operator|.
name|accept
argument_list|()
expr_stmt|;
name|socket
operator|.
name|setSoTimeout
argument_list|(
literal|10
operator|*
literal|1000
argument_list|)
expr_stmt|;
comment|// Ten seconds
name|stream
operator|=
name|socket
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SocketTimeoutException
name|ste
parameter_list|)
block|{
comment|// This should never happen but bug 3325 suggests that it does
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Karaf shutdown socket: "
operator|+
literal|"The socket listening for the shutdown command experienced "
operator|+
literal|"an unexpected timeout "
operator|+
literal|"["
operator|+
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|acceptStartTime
operator|)
operator|+
literal|"] milliseconds "
operator|+
literal|"after the call to accept(). Is this an instance of bug 3325?"
argument_list|,
name|ste
argument_list|)
expr_stmt|;
continue|continue;
block|}
catch|catch
parameter_list|(
name|AccessControlException
name|ace
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Karaf shutdown socket: security exception: "
operator|+
name|ace
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ace
argument_list|)
expr_stmt|;
continue|continue;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Karaf shutdown socket: accept: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// Read a set of characters from the socket
name|StringBuilder
name|command
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|expected
init|=
literal|1024
decl_stmt|;
comment|// Cut off to avoid DoS attack
while|while
condition|(
name|expected
operator|<
name|shutdown
operator|.
name|length
argument_list|()
condition|)
block|{
if|if
condition|(
name|random
operator|==
literal|null
condition|)
block|{
name|random
operator|=
operator|new
name|Random
argument_list|()
expr_stmt|;
block|}
name|expected
operator|+=
operator|(
name|random
operator|.
name|nextInt
argument_list|()
operator|%
literal|1024
operator|)
expr_stmt|;
block|}
while|while
condition|(
name|expected
operator|>
literal|0
condition|)
block|{
name|int
name|ch
decl_stmt|;
try|try
block|{
name|ch
operator|=
name|stream
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Karaf shutdown socket:  read: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ch
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|ch
operator|<
literal|32
condition|)
block|{
comment|// Control character or EOF terminates loop
break|break;
block|}
name|command
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|ch
argument_list|)
expr_stmt|;
name|expected
operator|--
expr_stmt|;
block|}
comment|// Close the socket now that we are done with it
try|try
block|{
name|socket
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
comment|// Match against our command string
name|boolean
name|match
init|=
name|command
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|shutdown
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Karaf shutdown socket: received shutdown command. Stopping framework..."
argument_list|)
expr_stmt|;
name|framework
operator|.
name|stop
argument_list|()
expr_stmt|;
break|break;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Karaf shutdown socket:  Invalid command '"
operator|+
name|command
operator|.
name|toString
argument_list|()
operator|+
literal|"' received"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|shutdownSocket
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

