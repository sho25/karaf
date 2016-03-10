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
name|BufferedReader
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
name|FileNotFoundException
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
name|InputStreamReader
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
comment|/**  * Main class used to stop the root Karaf instance  */
end_comment

begin_class
specifier|public
class|class
name|Stop
block|{
comment|/**      * Send the shutdown command to the running Karaf instance. Uses either a shut down port configured in config.properties or      * the port from the shutdown port file.      *      * @param args The arguments to the stop main method.      * @throws Exception In case of failure while stopping.      */
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
literal|" shutdown port file: "
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
name|Socket
name|s
init|=
literal|null
decl_stmt|;
try|try
block|{
name|s
operator|=
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
expr_stmt|;
name|s
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|config
operator|.
name|shutdownCommand
operator|.
name|getBytes
argument_list|()
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
catch|catch
parameter_list|(
name|ConnectException
name|connectException
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Can't connect to the container. The container is not running."
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
finally|finally
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable to find port..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
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
name|FileNotFoundException
throws|,
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
block|}
end_class

end_unit

