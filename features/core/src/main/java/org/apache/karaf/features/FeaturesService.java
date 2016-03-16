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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * The service managing features repositories.  */
end_comment

begin_interface
specifier|public
interface|interface
name|FeaturesService
block|{
name|String
name|ROOT_REGION
init|=
literal|"root"
decl_stmt|;
name|String
name|UPDATE_SNAPSHOTS_NONE
init|=
literal|"none"
decl_stmt|;
name|String
name|UPDATE_SNAPSHOTS_CRC
init|=
literal|"crc"
decl_stmt|;
name|String
name|DEFAULT_UPDATE_SNAPSHOTS
init|=
name|UPDATE_SNAPSHOTS_CRC
decl_stmt|;
name|String
name|UPDATE_SNAPSHOTS_ALWAYS
init|=
literal|"always"
decl_stmt|;
name|String
name|DEFAULT_FEATURE_RESOLUTION_RANGE
init|=
literal|"${range;[====,====]}"
decl_stmt|;
name|String
name|DEFAULT_BUNDLE_UPDATE_RANGE
init|=
literal|"${range;[==,=+)}"
decl_stmt|;
name|String
name|UPDATEABLE_URIS
init|=
literal|"mvn:.*SNAPSHOT|(?!mvn:).*"
decl_stmt|;
name|String
name|SERVICE_REQUIREMENTS_DISABLE
init|=
literal|"disable"
decl_stmt|;
name|String
name|SERVICE_REQUIREMENTS_DEFAULT
init|=
literal|"default"
decl_stmt|;
name|String
name|SERVICE_REQUIREMENTS_ENFORCE
init|=
literal|"enforce"
decl_stmt|;
name|int
name|DEFAULT_DOWNLOAD_THREADS
init|=
literal|8
decl_stmt|;
name|long
name|DEFAULT_SCHEDULE_DELAY
init|=
literal|250
decl_stmt|;
name|int
name|DEFAULT_SCHEDULE_MAX_RUN
init|=
literal|9
decl_stmt|;
name|long
name|DEFAULT_REPOSITORY_EXPIRATION
init|=
literal|60000
decl_stmt|;
comment|// 1 minute
enum|enum
name|Option
block|{
name|NoFailOnFeatureNotFound
block|,
name|NoAutoRefreshManagedBundles
block|,
name|NoAutoRefreshUnmanagedBundles
block|,
name|NoAutoRefreshBundles
block|,
name|NoAutoStartBundles
block|,
name|NoAutoManageBundles
block|,
name|Simulate
block|,
name|Verbose
block|,
name|Upgrade
block|,
name|DisplayFeaturesWiring
block|,
name|DisplayAllWiring
block|}
comment|/**      * Validate repository contents.      *      * @param uri Repository uri.      * @throws Exception When validation fails.      */
name|void
name|validateRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|addRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|addRepository
parameter_list|(
name|URI
name|uri
parameter_list|,
name|boolean
name|install
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|removeRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|removeRepository
parameter_list|(
name|URI
name|uri
parameter_list|,
name|boolean
name|uninstall
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|restoreRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|Repository
index|[]
name|listRequiredRepositories
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|Repository
index|[]
name|listRepositories
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|Repository
name|getRepository
parameter_list|(
name|String
name|repoName
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|Repository
name|getRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|String
name|getRepositoryName
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|setResolutionOutputFile
parameter_list|(
name|String
name|outputFile
parameter_list|)
function_decl|;
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|installFeature
parameter_list|(
name|Feature
name|f
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|installFeatures
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|installFeatures
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|String
name|region
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|addRequirements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|requirements
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|uninstallFeatures
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|uninstallFeatures
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|String
name|region
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|removeRequirements
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|requirements
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|updateFeaturesState
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|FeatureState
argument_list|>
argument_list|>
name|stateChanges
parameter_list|,
name|EnumSet
argument_list|<
name|Option
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|Feature
index|[]
name|listFeatures
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|Feature
index|[]
name|listRequiredFeatures
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|Feature
index|[]
name|listInstalledFeatures
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listRequirements
parameter_list|()
function_decl|;
name|boolean
name|isRequired
parameter_list|(
name|Feature
name|f
parameter_list|)
function_decl|;
name|boolean
name|isInstalled
parameter_list|(
name|Feature
name|f
parameter_list|)
function_decl|;
name|Feature
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|Feature
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|Feature
index|[]
name|getFeatures
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|Feature
index|[]
name|getFeatures
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|refreshRepository
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|URI
name|getRepositoryUriFor
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
function_decl|;
name|String
index|[]
name|getRepositoryNames
parameter_list|()
function_decl|;
name|void
name|registerListener
parameter_list|(
name|FeaturesListener
name|listener
parameter_list|)
function_decl|;
name|void
name|unregisterListener
parameter_list|(
name|FeaturesListener
name|listener
parameter_list|)
function_decl|;
name|FeatureState
name|getState
parameter_list|(
name|String
name|featureId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

