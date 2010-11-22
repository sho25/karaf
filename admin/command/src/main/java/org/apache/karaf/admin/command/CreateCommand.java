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
name|admin
operator|.
name|command
package|;
end_package

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
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|admin
operator|.
name|InstanceSettings
import|;
end_import

begin_comment
comment|/**  * Creates a new Karaf instance   *  * @version $Rev: 679826 $ $Date: 2008-07-25 17:00:12 +0200 (Fri, 25 Jul 2008) $  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"admin"
argument_list|,
name|name
operator|=
literal|"create"
argument_list|,
name|description
operator|=
literal|"Creates a new container instance."
argument_list|)
specifier|public
class|class
name|CreateCommand
extends|extends
name|AdminCommandSupport
block|{
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
name|rmiPort
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
name|List
argument_list|<
name|String
argument_list|>
name|featureURLs
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
name|InstanceSettings
name|settings
init|=
operator|new
name|InstanceSettings
argument_list|(
name|sshPort
argument_list|,
name|rmiPort
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
name|getAdminService
argument_list|()
operator|.
name|createInstance
argument_list|(
name|instance
argument_list|,
name|settings
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

