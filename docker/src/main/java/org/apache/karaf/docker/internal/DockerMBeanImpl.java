begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|docker
operator|.
name|internal
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
name|docker
operator|.
name|*
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
specifier|public
class|class
name|DockerMBeanImpl
implements|implements
name|DockerMBean
block|{
specifier|private
name|DockerService
name|dockerService
decl_stmt|;
specifier|public
name|void
name|setDockerService
parameter_list|(
name|DockerService
name|dockerService
parameter_list|)
block|{
name|this
operator|.
name|dockerService
operator|=
name|dockerService
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|ps
parameter_list|(
name|boolean
name|showAll
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|containerType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"container"
argument_list|,
literal|"Docker Container"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Id"
block|,
literal|"Names"
block|,
literal|"Command"
block|,
literal|"Created"
block|,
literal|"Image"
block|,
literal|"Status"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Container ID"
block|,
literal|"Container Names"
block|,
literal|"Command run in the container"
block|,
literal|"Container creation time"
block|,
literal|"Image used by the container"
block|,
literal|"Current container status"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"containers"
argument_list|,
literal|"Docker containers"
argument_list|,
name|containerType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Id"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
for|for
control|(
name|Container
name|container
range|:
name|dockerService
operator|.
name|ps
argument_list|(
name|showAll
argument_list|,
name|url
argument_list|)
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|containerType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Id"
block|,
literal|"Names"
block|,
literal|"Command"
block|,
literal|"Created"
block|,
literal|"Image"
block|,
literal|"Status"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|container
operator|.
name|getId
argument_list|()
block|,
name|container
operator|.
name|getNames
argument_list|()
block|,
name|container
operator|.
name|getCommand
argument_list|()
block|,
name|container
operator|.
name|getCreated
argument_list|()
block|,
name|container
operator|.
name|getImage
argument_list|()
block|,
name|container
operator|.
name|getStatus
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Info
name|info
init|=
name|dockerService
operator|.
name|info
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|infoMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"Containers"
argument_list|,
operator|new
name|Integer
argument_list|(
name|info
operator|.
name|getContainers
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"Debug"
argument_list|,
operator|new
name|Boolean
argument_list|(
name|info
operator|.
name|isDebug
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"Driver"
argument_list|,
name|info
operator|.
name|getDriver
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"ExecutionDriver"
argument_list|,
name|info
operator|.
name|getExecutionDriver
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"IPv4Forwarding"
argument_list|,
operator|new
name|Boolean
argument_list|(
name|info
operator|.
name|isIpv4Forwarding
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"Images"
argument_list|,
operator|new
name|Integer
argument_list|(
name|info
operator|.
name|getImages
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"IndexServerAddress"
argument_list|,
name|info
operator|.
name|getIndexServerAddress
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"InitPath"
argument_list|,
name|info
operator|.
name|getInitPath
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"InitSha1"
argument_list|,
name|info
operator|.
name|getInitSha1
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"KernelVersion"
argument_list|,
name|info
operator|.
name|getKernelVersion
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"MemoryLimit"
argument_list|,
operator|new
name|Boolean
argument_list|(
name|info
operator|.
name|isMemoryLimit
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"NEventsListener"
argument_list|,
operator|new
name|Boolean
argument_list|(
name|info
operator|.
name|isnEventsListener
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"NFd"
argument_list|,
operator|new
name|Integer
argument_list|(
name|info
operator|.
name|getNfd
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"NGoroutines"
argument_list|,
operator|new
name|Integer
argument_list|(
name|info
operator|.
name|getNgoroutines
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|infoMap
operator|.
name|put
argument_list|(
literal|"SwapLimit"
argument_list|,
operator|new
name|Boolean
argument_list|(
name|info
operator|.
name|isSwapLimit
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|infoMap
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|provision
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|sshPort
parameter_list|,
name|String
name|jmxRmiPort
parameter_list|,
name|String
name|jmxRmiRegistryPort
parameter_list|,
name|String
name|httpPort
parameter_list|,
name|boolean
name|copy
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|provision
argument_list|(
name|name
argument_list|,
name|sshPort
argument_list|,
name|jmxRmiPort
argument_list|,
name|jmxRmiRegistryPort
argument_list|,
name|httpPort
argument_list|,
name|copy
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|rm
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|removeVolumes
parameter_list|,
name|boolean
name|force
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|rm
argument_list|(
name|name
argument_list|,
name|removeVolumes
argument_list|,
name|force
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|start
argument_list|(
name|name
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|timeToWait
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|stop
argument_list|(
name|name
argument_list|,
name|timeToWait
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|logs
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|stdout
parameter_list|,
name|boolean
name|stderr
parameter_list|,
name|boolean
name|timestamps
parameter_list|,
name|boolean
name|details
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
if|if
condition|(
operator|!
name|stdout
operator|&&
operator|!
name|stderr
condition|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
literal|"You have to choose at least one stream: stdout or stderr"
argument_list|)
throw|;
block|}
return|return
name|dockerService
operator|.
name|logs
argument_list|(
name|name
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|,
name|timestamps
argument_list|,
name|details
argument_list|,
name|url
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|commit
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|repo
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|commit
argument_list|(
name|name
argument_list|,
name|repo
argument_list|,
name|url
argument_list|,
name|tag
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|images
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|type
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Image"
argument_list|,
literal|"Docker Image"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Id"
block|,
literal|"Created"
block|,
literal|"RepoTags"
block|,
literal|"Size"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Image Id"
block|,
literal|"Image Creation Date"
block|,
literal|"Image repository and tag"
block|,
literal|"Image size"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|LONG
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"Images"
argument_list|,
literal|"List of Docker Image"
argument_list|,
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Id"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
for|for
control|(
name|Image
name|image
range|:
name|dockerService
operator|.
name|images
argument_list|(
name|url
argument_list|)
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Id"
block|,
literal|"Created"
block|,
literal|"RepoTags"
block|,
literal|"Size"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|image
operator|.
name|getId
argument_list|()
block|,
name|image
operator|.
name|getCreated
argument_list|()
block|,
name|image
operator|.
name|getRepoTags
argument_list|()
block|,
name|image
operator|.
name|getSize
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|pull
parameter_list|(
name|String
name|image
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|pull
argument_list|(
name|image
argument_list|,
name|tag
argument_list|,
literal|false
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|version
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|Version
name|version
init|=
name|dockerService
operator|.
name|version
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|versionMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"Experimental"
argument_list|,
name|version
operator|.
name|getExperimental
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"ApiVersion"
argument_list|,
name|version
operator|.
name|getApiVersion
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"Arch"
argument_list|,
name|version
operator|.
name|getArch
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"BuildTime"
argument_list|,
name|version
operator|.
name|getBuildTime
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"GitCommit"
argument_list|,
name|version
operator|.
name|getGitCommit
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"GoVersion"
argument_list|,
name|version
operator|.
name|getGoVersion
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"KernelVersion"
argument_list|,
name|version
operator|.
name|getKernelVersion
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"OS"
argument_list|,
name|version
operator|.
name|getOs
argument_list|()
argument_list|)
expr_stmt|;
name|versionMap
operator|.
name|put
argument_list|(
literal|"Version"
argument_list|,
name|version
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|versionMap
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|rename
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|newName
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|rename
argument_list|(
name|container
argument_list|,
name|newName
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|restart
parameter_list|(
name|String
name|container
parameter_list|,
name|int
name|timeToWait
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|restart
argument_list|(
name|container
argument_list|,
name|timeToWait
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|kill
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|signal
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
if|if
condition|(
name|signal
operator|==
literal|null
condition|)
block|{
name|signal
operator|=
literal|"SIGKILL"
expr_stmt|;
block|}
name|dockerService
operator|.
name|kill
argument_list|(
name|container
argument_list|,
name|signal
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|pause
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|pause
argument_list|(
name|container
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|unpause
parameter_list|(
name|String
name|container
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|unpause
argument_list|(
name|container
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|search
parameter_list|(
name|String
name|term
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|imageType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"image"
argument_list|,
literal|"Image"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"StarCount"
block|,
literal|"Official"
block|,
literal|"Automated"
block|,
literal|"Description"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"StarCount"
block|,
literal|"Official"
block|,
literal|"Automated"
block|,
literal|"Description"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|INTEGER
block|,
name|SimpleType
operator|.
name|BOOLEAN
block|,
name|SimpleType
operator|.
name|BOOLEAN
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"images"
argument_list|,
literal|"Images"
argument_list|,
name|imageType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ImageSearch
argument_list|>
name|images
init|=
name|dockerService
operator|.
name|search
argument_list|(
name|term
argument_list|,
name|url
argument_list|)
decl_stmt|;
for|for
control|(
name|ImageSearch
name|image
range|:
name|images
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|imageType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"StarCount"
block|,
literal|"Official"
block|,
literal|"Automated"
block|,
literal|"Description"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|image
operator|.
name|getName
argument_list|()
block|,
name|image
operator|.
name|getStarCount
argument_list|()
block|,
name|image
operator|.
name|isOfficial
argument_list|()
block|,
name|image
operator|.
name|isAutomated
argument_list|()
block|,
name|image
operator|.
name|getDescription
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|tag
parameter_list|(
name|String
name|image
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|repo
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|tag
argument_list|(
name|image
argument_list|,
name|tag
argument_list|,
name|repo
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|rmi
parameter_list|(
name|String
name|image
parameter_list|,
name|boolean
name|force
parameter_list|,
name|boolean
name|noprune
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|rmi
argument_list|(
name|image
argument_list|,
name|force
argument_list|,
name|noprune
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|push
parameter_list|(
name|String
name|image
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|dockerService
operator|.
name|push
argument_list|(
name|image
argument_list|,
name|tag
argument_list|,
literal|false
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
