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
name|instance
operator|.
name|command
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
name|Arrays
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
name|features
operator|.
name|command
operator|.
name|completers
operator|.
name|AllFeatureCompleter
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
name|features
operator|.
name|command
operator|.
name|completers
operator|.
name|InstalledRepoUriCompleter
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
name|instance
operator|.
name|core
operator|.
name|InstanceSettings
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

begin_comment
comment|/**  * Creates a new instance.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"instance"
argument_list|,
name|name
operator|=
literal|"create"
argument_list|,
name|description
operator|=
literal|"Creates a new container instance."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|CreateCommand
extends|extends
name|InstanceCommandSupport
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FEATURES_SERVICE_CONFIG_FILE
init|=
literal|"org.apache.karaf.features.cfg"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
literal|"--bare"
argument_list|,
name|description
operator|=
literal|"Do not use add default features"
argument_list|)
name|boolean
name|bare
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ssh-port"
block|}
argument_list|,
name|description
operator|=
literal|"Port number for remote secure shell connection"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|sshPort
init|=
literal|0
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-r"
argument_list|,
name|aliases
operator|=
block|{
literal|"-rr"
block|,
literal|"--rmi-port"
block|,
literal|"--rmi-registry-port"
block|}
argument_list|,
name|description
operator|=
literal|"Port number for RMI registry connection"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|rmiRegistryPort
init|=
literal|0
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-rs"
argument_list|,
name|aliases
operator|=
block|{
literal|"--rmi-server-port"
block|}
argument_list|,
name|description
operator|=
literal|"Port number for RMI server connection"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|rmiServerPort
init|=
literal|0
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-l"
argument_list|,
name|aliases
operator|=
block|{
literal|"--location"
block|}
argument_list|,
name|description
operator|=
literal|"Location of the new container instance in the file system"
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
name|location
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|aliases
operator|=
block|{
literal|"--java-opts"
block|}
argument_list|,
name|description
operator|=
literal|"JVM options to use when launching the instance"
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
name|javaOpts
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-f"
argument_list|,
name|aliases
operator|=
block|{
literal|"--feature"
block|}
argument_list|,
name|description
operator|=
literal|"Initial features. This option can be specified multiple times to enable multiple initial features"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
annotation|@
name|Completion
argument_list|(
name|AllFeatureCompleter
operator|.
name|class
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|features
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-furl"
argument_list|,
name|aliases
operator|=
block|{
literal|"--featureURL"
block|}
argument_list|,
name|description
operator|=
literal|"Additional feature descriptor URLs. This option can be specified multiple times to add multiple URLs"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
annotation|@
name|Completion
argument_list|(
name|InstalledRepoUriCompleter
operator|.
name|class
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|featureURLs
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|aliases
operator|=
block|{
literal|"--verbose"
block|}
argument_list|,
name|description
operator|=
literal|"Display actions performed by the command (disabled by default)"
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
name|verbose
init|=
literal|false
decl_stmt|;
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
literal|"The name of the new container instance"
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
name|instance
init|=
literal|null
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|bare
condition|)
block|{
name|Properties
name|configuration
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|File
name|configFile
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|,
name|FEATURES_SERVICE_CONFIG_FILE
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|load
argument_list|(
name|configFile
argument_list|)
expr_stmt|;
name|String
name|featuresRepositories
init|=
name|configuration
operator|.
name|getProperty
argument_list|(
literal|"featuresRepositories"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|featuresBoot
init|=
name|configuration
operator|.
name|getProperty
argument_list|(
literal|"featuresBoot"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|featureURLs
operator|==
literal|null
condition|)
block|{
name|featureURLs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|featureURLs
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresRepositories
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|features
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|features
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresBoot
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
name|sshPort
argument_list|,
name|rmiRegistryPort
argument_list|,
name|rmiServerPort
argument_list|,
name|location
argument_list|,
name|javaOpts
argument_list|,
name|featureURLs
argument_list|,
name|features
argument_list|)
decl_stmt|;
name|getInstanceService
argument_list|()
operator|.
name|createInstance
argument_list|(
name|instance
argument_list|,
name|settings
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

