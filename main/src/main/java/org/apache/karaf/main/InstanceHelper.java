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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|RuntimeMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
specifier|public
class|class
name|InstanceHelper
block|{
specifier|static
name|void
name|updateInstancePid
parameter_list|(
name|File
name|karafHome
parameter_list|,
name|File
name|karafBase
parameter_list|)
block|{
try|try
block|{
name|String
name|instanceName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.name"
argument_list|)
decl_stmt|;
name|String
name|pid
init|=
name|ManagementFactory
operator|.
name|getRuntimeMXBean
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|pid
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|pid
operator|=
name|pid
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pid
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isRoot
init|=
name|karafHome
operator|.
name|equals
argument_list|(
name|karafBase
argument_list|)
decl_stmt|;
if|if
condition|(
name|instanceName
operator|!=
literal|null
condition|)
block|{
name|String
name|storage
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.instances"
argument_list|)
decl_stmt|;
if|if
condition|(
name|storage
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"System property 'karaf.instances' is not set. \n"
operator|+
literal|"This property needs to be set to the full path of the instance.properties file."
argument_list|)
throw|;
block|}
name|File
name|storageFile
init|=
operator|new
name|File
argument_list|(
name|storage
argument_list|)
decl_stmt|;
name|File
name|propertiesFile
init|=
operator|new
name|File
argument_list|(
name|storageFile
argument_list|,
literal|"instance.properties"
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertiesFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|propertiesFile
argument_list|)
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"count"
argument_list|)
argument_list|)
decl_stmt|;
comment|// update root name if karaf.name got updated since the last container start
name|String
name|savedRootName
init|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"item.0.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|savedRootName
operator|!=
literal|null
operator|&&
name|isRoot
operator|&&
operator|!
name|savedRootName
operator|.
name|equals
argument_list|(
name|instanceName
argument_list|)
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
literal|"item.0.name"
argument_list|,
name|instanceName
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"item."
operator|+
name|i
operator|+
literal|".name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|instanceName
argument_list|)
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
literal|"item."
operator|+
name|i
operator|+
literal|".pid"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|propertiesFile
argument_list|)
decl_stmt|;
name|props
operator|.
name|store
argument_list|(
name|fos
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isRoot
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Instance "
operator|+
name|instanceName
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|isRoot
condition|)
block|{
if|if
condition|(
operator|!
name|propertiesFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|propertiesFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|se
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|props
operator|.
name|setProperty
argument_list|(
literal|"count"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"item.0.name"
argument_list|,
name|instanceName
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"item.0.loc"
argument_list|,
name|karafHome
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"item.0.pid"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"item.0.root"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|propertiesFile
argument_list|)
decl_stmt|;
name|props
operator|.
name|store
argument_list|(
name|fos
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable to update instance pid: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|writePid
parameter_list|(
name|String
name|pidFile
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|pidFile
operator|!=
literal|null
condition|)
block|{
name|RuntimeMXBean
name|rtb
init|=
name|ManagementFactory
operator|.
name|getRuntimeMXBean
argument_list|()
decl_stmt|;
name|String
name|processName
init|=
name|rtb
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^([0-9]+)@.+$"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|processName
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|int
name|pid
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|Writer
name|w
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|pidFile
argument_list|)
argument_list|)
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|pid
argument_list|)
argument_list|)
expr_stmt|;
name|w
operator|.
name|close
argument_list|()
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
block|}
specifier|static
name|void
name|setupShutdown
parameter_list|(
name|ConfigProperties
name|config
parameter_list|,
name|Framework
name|framework
parameter_list|)
block|{
name|writePid
argument_list|(
name|config
operator|.
name|pidFile
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|port
init|=
name|config
operator|.
name|shutdownPort
decl_stmt|;
name|String
name|host
init|=
name|config
operator|.
name|shutdownHost
decl_stmt|;
name|String
name|portFile
init|=
name|config
operator|.
name|portFile
decl_stmt|;
specifier|final
name|String
name|shutdown
init|=
name|config
operator|.
name|shutdownCommand
decl_stmt|;
if|if
condition|(
name|port
operator|>=
literal|0
condition|)
block|{
name|ServerSocket
name|shutdownSocket
init|=
operator|new
name|ServerSocket
argument_list|(
name|port
argument_list|,
literal|1
argument_list|,
name|InetAddress
operator|.
name|getByName
argument_list|(
name|host
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|port
operator|==
literal|0
condition|)
block|{
name|port
operator|=
name|shutdownSocket
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|portFile
operator|!=
literal|null
condition|)
block|{
name|Writer
name|w
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|portFile
argument_list|)
argument_list|)
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|w
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Thread
name|thread
init|=
operator|new
name|ShutdownSocketThread
argument_list|(
name|shutdown
argument_list|,
name|shutdownSocket
argument_list|,
name|framework
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|thread
operator|.
name|start
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
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

