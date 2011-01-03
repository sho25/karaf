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
name|features
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
name|EnumSet
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
name|features
operator|.
name|FeaturesService
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"features"
argument_list|,
name|name
operator|=
literal|"install"
argument_list|,
name|description
operator|=
literal|"Installs a feature with the specified name and version."
argument_list|)
specifier|public
class|class
name|InstallFeatureCommand
extends|extends
name|FeaturesCommandSupport
block|{
specifier|private
specifier|static
name|String
name|DEFAULT_VERSION
init|=
literal|"0.0.0"
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
literal|"The name of the feature"
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
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"version"
argument_list|,
name|description
operator|=
literal|"The version of the feature"
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
name|version
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-c"
argument_list|,
name|aliases
operator|=
literal|"--no-clean"
argument_list|,
name|description
operator|=
literal|"Do not uninstall bundles on failure"
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
name|noClean
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
literal|"--no-auto-refresh"
argument_list|,
name|description
operator|=
literal|"Do not automatically refresh bundles"
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
name|noRefresh
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
literal|"--verbose"
argument_list|,
name|description
operator|=
literal|"Explain what is being done"
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
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|version
operator|=
name|DEFAULT_VERSION
expr_stmt|;
block|}
name|EnumSet
argument_list|<
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|of
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|PrintBundlesToRefresh
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|noClean
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoCleanIfFailure
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verbose
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|Verbose
argument_list|)
expr_stmt|;
block|}
name|admin
operator|.
name|installFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

