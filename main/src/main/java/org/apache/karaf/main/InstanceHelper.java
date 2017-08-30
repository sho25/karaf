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
name|RandomAccessFile
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
name|nio
operator|.
name|channels
operator|.
name|FileLock
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
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|TypedProperties
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
name|locks
operator|.
name|FileLockUtils
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
specifier|final
name|File
name|karafHome
parameter_list|,
specifier|final
name|File
name|karafBase
parameter_list|,
specifier|final
name|boolean
name|isStartingInstance
parameter_list|)
block|{
try|try
block|{
specifier|final
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
specifier|final
name|String
name|pid
init|=
name|isStartingInstance
condition|?
name|getPid
argument_list|()
else|:
literal|"0"
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
specifier|final
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
if|if
condition|(
operator|!
name|propertiesFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to create directory "
operator|+
name|propertiesFile
operator|.
name|getParentFile
argument_list|()
argument_list|)
throw|;
block|}
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
comment|// don't instance.properties if we're stopping and can't acquire lock
if|if
condition|(
operator|!
name|isStartingInstance
condition|)
block|{
name|boolean
name|proceed
init|=
literal|true
decl_stmt|;
try|try
init|(
name|RandomAccessFile
name|raf
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|propertiesFile
argument_list|,
literal|"rw"
argument_list|)
init|)
block|{
name|FileLock
name|lock
init|=
name|raf
operator|.
name|getChannel
argument_list|()
operator|.
name|tryLock
argument_list|()
decl_stmt|;
if|if
condition|(
name|lock
operator|==
literal|null
condition|)
block|{
name|proceed
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
comment|// if proceed is true than we got the lock or OverlappingFileLockException
comment|// but we may proceed in either case
if|if
condition|(
operator|!
name|proceed
condition|)
block|{
comment|// we didn't acquire lock, it may mean that root container is holding the lock when
comment|// stopping the child
return|return;
block|}
block|}
name|FileLockUtils
operator|.
name|execute
argument_list|(
name|propertiesFile
argument_list|,
parameter_list|(
name|TypedProperties
name|props
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|props
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// it's the first instance running, so we consider as root
name|props
operator|.
name|put
argument_list|(
literal|"count"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"item.0.name"
argument_list|,
name|instanceName
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"item.0.loc"
argument_list|,
name|karafBase
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"item.0.pid"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"item.0.root"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|count
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|props
operator|.
name|get
argument_list|(
literal|"count"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
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
name|get
argument_list|(
literal|"item."
operator|+
name|i
operator|+
literal|".name"
argument_list|)
operator|.
name|toString
argument_list|()
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
name|put
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
return|return;
block|}
block|}
comment|// it's not found, let assume it's the root instance, so 0
name|props
operator|.
name|put
argument_list|(
literal|"item.0.name"
argument_list|,
name|instanceName
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"item.0.pid"
argument_list|,
name|pid
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|true
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
name|String
name|getPid
parameter_list|()
block|{
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
return|return
name|pid
return|;
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
name|AutoCloseable
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
name|File
name|portF
init|=
operator|new
name|File
argument_list|(
name|portFile
argument_list|)
decl_stmt|;
name|portF
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|Writer
name|w
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|portF
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
name|ShutdownSocketThread
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
name|start
argument_list|()
expr_stmt|;
return|return
name|thread
return|;
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

