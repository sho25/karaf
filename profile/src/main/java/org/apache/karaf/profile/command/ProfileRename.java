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
name|profile
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
name|profile
operator|.
name|Profile
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
name|profile
operator|.
name|ProfileBuilder
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
name|profile
operator|.
name|ProfileService
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
name|profile
operator|.
name|command
operator|.
name|completers
operator|.
name|ProfileCompleter
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
name|Action
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
name|Reference
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

begin_class
annotation|@
name|Command
argument_list|(
name|name
operator|=
literal|"rename"
argument_list|,
name|scope
operator|=
literal|"profile"
argument_list|,
name|description
operator|=
literal|"Rename the specified source profile"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|ProfileRename
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--version"
argument_list|,
name|description
operator|=
literal|"The profile version to rename. Defaults to the current default version."
argument_list|)
specifier|private
name|String
name|versionId
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
literal|"--force"
argument_list|,
name|description
operator|=
literal|"Flag to allow replacing the target profile (if exists)."
argument_list|)
specifier|private
name|boolean
name|force
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|name
operator|=
literal|"profile name"
argument_list|,
name|description
operator|=
literal|"Name of the profile."
argument_list|)
annotation|@
name|Completion
argument_list|(
name|ProfileCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|profileName
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|name
operator|=
literal|"new profile name"
argument_list|,
name|description
operator|=
literal|"New name of the profile."
argument_list|)
specifier|private
name|String
name|newName
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|ProfileService
name|profileService
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
name|Profile
name|profile
init|=
name|ProfileBuilder
operator|.
name|Factory
operator|.
name|createFrom
argument_list|(
name|profileService
operator|.
name|getProfile
argument_list|(
name|profileName
argument_list|)
argument_list|)
operator|.
name|identity
argument_list|(
name|newName
argument_list|)
operator|.
name|getProfile
argument_list|()
decl_stmt|;
name|profileService
operator|.
name|createProfile
argument_list|(
name|profile
argument_list|)
expr_stmt|;
name|profileService
operator|.
name|deleteProfile
argument_list|(
name|profileName
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit
