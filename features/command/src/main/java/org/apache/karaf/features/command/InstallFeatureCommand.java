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
name|HashSet
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
name|karaf
operator|.
name|features
operator|.
name|command
operator|.
name|completers
operator|.
name|AvailableFeatureCompleter
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"feature"
argument_list|,
name|name
operator|=
literal|"install"
argument_list|,
name|description
operator|=
literal|"Installs a feature with the specified name and version."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|InstallFeatureCommand
extends|extends
name|FeaturesCommandSupport
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
literal|"features"
argument_list|,
name|description
operator|=
literal|"The name and version of the features to install. A feature id looks like name/version. The version is optional."
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
annotation|@
name|Completion
argument_list|(
name|AvailableFeatureCompleter
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
literal|"-s"
argument_list|,
name|aliases
operator|=
literal|"--no-auto-start"
argument_list|,
name|description
operator|=
literal|"Do not start the bundles"
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
name|noStart
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-m"
argument_list|,
name|aliases
operator|=
literal|"--no-auto-manage"
argument_list|,
name|description
operator|=
literal|"Do not automatically manage bundles"
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
name|noManage
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-t"
argument_list|,
name|aliases
operator|=
literal|"--simulate"
argument_list|,
name|description
operator|=
literal|"Perform a simulation only"
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
name|simulate
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|aliases
operator|=
literal|"--upgrade"
argument_list|,
name|description
operator|=
literal|"Perform an upgrade of feature if previous version are installed or install it"
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
name|upgrade
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--store"
argument_list|,
name|description
operator|=
literal|"Store the resolution into the given file and result for offline analysis"
argument_list|)
name|String
name|outputFile
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--features-wiring"
argument_list|,
name|description
operator|=
literal|"Print the wiring between features"
argument_list|)
name|boolean
name|featuresWiring
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--all-wiring"
argument_list|,
name|description
operator|=
literal|"Print the full wiring"
argument_list|)
name|boolean
name|allWiring
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-g"
argument_list|,
name|aliases
operator|=
literal|"--region"
argument_list|,
name|description
operator|=
literal|"Region to install to"
argument_list|)
name|String
name|region
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
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|Simulate
argument_list|,
name|simulate
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoStartBundles
argument_list|,
name|noStart
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|,
name|noRefresh
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoManageBundles
argument_list|,
name|noManage
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|Verbose
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|Upgrade
argument_list|,
name|upgrade
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|DisplayFeaturesWiring
argument_list|,
name|featuresWiring
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|DisplayAllWiring
argument_list|,
name|allWiring
argument_list|)
expr_stmt|;
name|admin
operator|.
name|setResolutionOutputFile
argument_list|(
name|outputFile
argument_list|)
expr_stmt|;
name|admin
operator|.
name|installFeatures
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|features
argument_list|)
argument_list|,
name|region
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

