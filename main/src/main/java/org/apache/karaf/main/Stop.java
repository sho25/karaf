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
name|Socket
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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|main
operator|.
name|util
operator|.
name|Utils
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
comment|/**      * Sends the shutdown command to the running karaf instance. Uses either a shut down port configured in config.properties or      * the port from the shutdown port file.      *       * @param args      * @throws Exception      */
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
name|File
name|karafHome
init|=
name|Utils
operator|.
name|getKarafHome
argument_list|(
name|Stop
operator|.
name|class
argument_list|,
name|Main
operator|.
name|PROP_KARAF_HOME
argument_list|,
name|Main
operator|.
name|ENV_KARAF_HOME
argument_list|)
decl_stmt|;
name|File
name|karafBase
init|=
name|Utils
operator|.
name|getKarafDirectory
argument_list|(
name|Main
operator|.
name|PROP_KARAF_BASE
argument_list|,
name|Main
operator|.
name|ENV_KARAF_BASE
argument_list|,
name|karafHome
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|File
name|karafData
init|=
name|Utils
operator|.
name|getKarafDirectory
argument_list|(
name|Main
operator|.
name|PROP_KARAF_DATA
argument_list|,
name|Main
operator|.
name|ENV_KARAF_DATA
argument_list|,
operator|new
name|File
argument_list|(
name|karafBase
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"data"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|Main
operator|.
name|PROP_KARAF_HOME
argument_list|,
name|karafHome
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|Main
operator|.
name|PROP_KARAF_BASE
argument_list|,
name|karafBase
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|Main
operator|.
name|PROP_KARAF_DATA
argument_list|,
name|karafData
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// Load system properties.
name|PropertiesLoader
operator|.
name|loadSystemProperties
argument_list|(
name|karafBase
argument_list|)
expr_stmt|;
name|Properties
name|props
init|=
name|PropertiesLoader
operator|.
name|loadConfigProperties
argument_list|(
name|karafBase
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|Main
operator|.
name|KARAF_SHUTDOWN_PORT
argument_list|,
literal|"0"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|Main
operator|.
name|KARAF_SHUTDOWN_HOST
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|String
name|portFile
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|Main
operator|.
name|KARAF_SHUTDOWN_PORT_FILE
argument_list|)
decl_stmt|;
name|String
name|shutdown
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|Main
operator|.
name|KARAF_SHUTDOWN_COMMAND
argument_list|,
name|Main
operator|.
name|DEFAULT_SHUTDOWN_COMMAND
argument_list|)
decl_stmt|;
if|if
condition|(
name|port
operator|==
literal|0
operator|&&
name|portFile
operator|!=
literal|null
condition|)
block|{
name|port
operator|=
name|getPortFromShutdownPortFile
argument_list|(
name|portFile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|Socket
name|s
init|=
operator|new
name|Socket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|s
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
name|shutdown
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|.
name|close
argument_list|()
expr_stmt|;
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

