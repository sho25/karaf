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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jpm
operator|.
name|impl
operator|.
name|ProcessBuilderFactoryImpl
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ConnectException
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

begin_comment
comment|/**  * Main class used to check the status of the root Karaf instance.  */
end_comment

begin_class
specifier|public
class|class
name|Status
block|{
comment|/**      * Checks if the shutdown port is bound. The shutdown port can be configured in config.properties      * or in the shutdown port file.      *      * @param args The arguments to the status main method.      * @throws Exception If an error occurs while checking the status.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|ConfigProperties
name|config
init|=
operator|new
name|ConfigProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|shutdownPort
operator|==
literal|0
operator|&&
name|config
operator|.
name|portFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|config
operator|.
name|shutdownPort
operator|=
name|getPortFromShutdownPortFile
argument_list|(
name|config
operator|.
name|portFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|fnfe
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|config
operator|.
name|portFile
operator|+
literal|" shutdown port file doesn't exist. The container is not running."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Can't read "
operator|+
name|config
operator|.
name|portFile
operator|+
literal|" port file: "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|config
operator|.
name|shutdownPort
operator|>
literal|0
condition|)
block|{
try|try
init|(
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
name|config
operator|.
name|shutdownHost
argument_list|,
name|config
operator|.
name|shutdownPort
argument_list|)
init|)
block|{
if|if
condition|(
name|s
operator|.
name|isBound
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Running ..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Not Running ..."
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
block|}
catch|catch
parameter_list|(
name|ConnectException
name|connectException
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Not Running ..."
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
block|}
else|else
block|{
comment|// using the pid file
name|int
name|pid
init|=
name|getPidFromPidFile
argument_list|(
name|config
operator|.
name|pidFile
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|jpm
operator|.
name|Process
name|process
init|=
operator|new
name|ProcessBuilderFactoryImpl
argument_list|()
operator|.
name|newBuilder
argument_list|()
operator|.
name|attach
argument_list|(
name|pid
argument_list|)
decl_stmt|;
if|if
condition|(
name|process
operator|.
name|isRunning
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Running ... (pid "
operator|+
name|pid
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Not Running ..."
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
block|}
block|}
specifier|private
specifier|static
name|int
name|getPortFromShutdownPortFile
parameter_list|(
name|String
name|portFile
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|port
decl_stmt|;
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|portFile
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|portStr
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portStr
argument_list|)
expr_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|port
return|;
block|}
specifier|private
specifier|static
name|int
name|getPidFromPidFile
parameter_list|(
name|String
name|pidFile
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|pid
decl_stmt|;
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|pidFile
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|pidString
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|pid
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|pidString
argument_list|)
expr_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|pid
return|;
block|}
block|}
end_class

end_unit

