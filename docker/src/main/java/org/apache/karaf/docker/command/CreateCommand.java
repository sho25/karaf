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
name|command
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
name|ContainerConfig
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
name|docker
operator|.
name|HostConfig
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
name|docker
operator|.
name|HostPortBinding
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
name|docker
operator|.
name|command
operator|.
name|completers
operator|.
name|ImagesRepoTagsCompleter
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
name|action
operator|.
name|Argument
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
name|action
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Completion
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
name|action
operator|.
name|Option
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
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"docker"
argument_list|,
name|name
operator|=
literal|"create"
argument_list|,
name|description
operator|=
literal|"Create a new container"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|CreateCommand
extends|extends
name|DockerCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"The container of the Docker container"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|name
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--hostname"
argument_list|,
name|description
operator|=
literal|"Hostname of the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|hostname
init|=
literal|"docker"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--user"
argument_list|,
name|description
operator|=
literal|"User of the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|user
init|=
literal|""
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--tty"
argument_list|,
name|description
operator|=
literal|"Enable TTY for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|tty
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--attachStdout"
argument_list|,
name|description
operator|=
literal|"Attach stdout for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|attachStdout
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--attachStderr"
argument_list|,
name|description
operator|=
literal|"Attach stderr for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|attachStderr
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--attachStdin"
argument_list|,
name|description
operator|=
literal|"Attach stdin for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|attachStdin
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--image"
argument_list|,
name|description
operator|=
literal|"Image to use for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|ImagesRepoTagsCompleter
operator|.
name|class
argument_list|)
name|String
name|image
init|=
literal|"karaf:4.2.0"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--cmd"
argument_list|,
name|description
operator|=
literal|"Command to execute when starting the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|cmd
init|=
literal|"/bin/karaf"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--workingDir"
argument_list|,
name|description
operator|=
literal|"Working directory of the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|workingDir
init|=
literal|""
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--openStdin"
argument_list|,
name|description
operator|=
literal|"Enable and open stdin for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|openStdin
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--stdinOnce"
argument_list|,
name|description
operator|=
literal|"Enable single use of std in the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|stdinOnce
init|=
literal|true
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--exposedPort"
argument_list|,
name|description
operator|=
literal|"Port to expose from the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|exposedPort
init|=
literal|"8101/tcp"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--hostPrivileged"
argument_list|,
name|description
operator|=
literal|"Set host config privileges for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|hostPrivileged
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--hostPublishAllPorts"
argument_list|,
name|description
operator|=
literal|"Expose all ports for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|hostPublishAllPorts
init|=
literal|false
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--hostNetworkMode"
argument_list|,
name|description
operator|=
literal|"Define the network mode for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|hostNetworkMode
init|=
literal|"bridge"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--hostPortBinding"
argument_list|,
name|description
operator|=
literal|"Define the port binding for the Docker container"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|hostPortBinding
init|=
literal|"8101"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|ContainerConfig
name|containerConfig
init|=
operator|new
name|ContainerConfig
argument_list|()
decl_stmt|;
name|containerConfig
operator|.
name|setTty
argument_list|(
name|tty
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setAttachStdin
argument_list|(
name|attachStdin
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setAttachStderr
argument_list|(
name|attachStderr
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setAttachStdout
argument_list|(
name|attachStdout
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setImage
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setHostname
argument_list|(
name|hostname
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setCmd
argument_list|(
operator|new
name|String
index|[]
block|{
name|cmd
block|}
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setWorkingDir
argument_list|(
name|workingDir
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setOpenStdin
argument_list|(
name|openStdin
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setStdinOnce
argument_list|(
name|stdinOnce
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|exposedPorts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|exposedPorts
operator|.
name|put
argument_list|(
name|exposedPort
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|containerConfig
operator|.
name|setExposedPorts
argument_list|(
name|exposedPorts
argument_list|)
expr_stmt|;
name|HostConfig
name|hostConfig
init|=
operator|new
name|HostConfig
argument_list|()
decl_stmt|;
name|hostConfig
operator|.
name|setPrivileged
argument_list|(
name|hostPrivileged
argument_list|)
expr_stmt|;
name|hostConfig
operator|.
name|setPublishAllPorts
argument_list|(
name|hostPublishAllPorts
argument_list|)
expr_stmt|;
name|hostConfig
operator|.
name|setNetworkMode
argument_list|(
name|hostNetworkMode
argument_list|)
expr_stmt|;
name|hostConfig
operator|.
name|setLxcConf
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|HostPortBinding
argument_list|>
argument_list|>
name|portBindings
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|HostPortBinding
argument_list|>
name|hostPortBindings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|HostPortBinding
name|portBinding
init|=
operator|new
name|HostPortBinding
argument_list|()
decl_stmt|;
name|portBinding
operator|.
name|setHostIp
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|portBinding
operator|.
name|setHostPort
argument_list|(
name|hostPortBinding
argument_list|)
expr_stmt|;
name|hostPortBindings
operator|.
name|add
argument_list|(
name|portBinding
argument_list|)
expr_stmt|;
name|portBindings
operator|.
name|put
argument_list|(
name|exposedPort
argument_list|,
name|hostPortBindings
argument_list|)
expr_stmt|;
name|getDockerService
argument_list|()
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|url
argument_list|,
name|containerConfig
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

