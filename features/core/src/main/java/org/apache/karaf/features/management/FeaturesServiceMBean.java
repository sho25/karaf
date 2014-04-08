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
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_interface
specifier|public
interface|interface
name|FeaturesServiceMBean
block|{
name|TabularData
name|getFeatures
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|TabularData
name|getRepositories
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|void
name|addRepository
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|addRepository
parameter_list|(
name|String
name|url
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
name|String
name|url
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|void
name|removeRepository
parameter_list|(
name|String
name|url
parameter_list|,
name|boolean
name|uninstall
parameter_list|)
throws|throws
name|Exception
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
name|boolean
name|noClean
parameter_list|,
name|boolean
name|noRefresh
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
name|boolean
name|noClean
parameter_list|,
name|boolean
name|noRefresh
parameter_list|,
name|boolean
name|noStart
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
name|boolean
name|noClean
parameter_list|,
name|boolean
name|noRefresh
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
name|boolean
name|noClean
parameter_list|,
name|boolean
name|noRefresh
parameter_list|,
name|boolean
name|noStart
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|TabularData
name|infoFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|TabularData
name|infoFeature
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
name|boolean
name|noRefresh
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
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|boolean
name|noRefresh
parameter_list|)
throws|throws
name|Exception
function_decl|;
name|String
name|FEATURE_NAME
init|=
literal|"Name"
decl_stmt|;
name|String
name|FEATURE_VERSION
init|=
literal|"Version"
decl_stmt|;
name|String
name|FEATURE_DEPENDENCIES
init|=
literal|"Dependencies"
decl_stmt|;
name|String
name|FEATURE_BUNDLES
init|=
literal|"Bundles"
decl_stmt|;
name|String
name|FEATURE_CONFIGURATIONS
init|=
literal|"Configurations"
decl_stmt|;
name|String
name|FEATURE_CONFIGURATIONFILES
init|=
literal|"Configuration Files"
decl_stmt|;
name|String
name|FEATURE_INSTALLED
init|=
literal|"Installed"
decl_stmt|;
name|String
name|FEATURE_CONFIG_PID
init|=
literal|"Pid"
decl_stmt|;
name|String
name|FEATURE_CONFIG_ELEMENTS
init|=
literal|"Elements"
decl_stmt|;
name|String
name|FEATURE_CONFIG_ELEMENT_KEY
init|=
literal|"Key"
decl_stmt|;
name|String
name|FEATURE_CONFIG_ELEMENT_VALUE
init|=
literal|"Value"
decl_stmt|;
name|String
name|FEATURE_CONFIG_FILES_ELEMENTS
init|=
literal|"Files"
decl_stmt|;
comment|/**      * The type of the event which is emitted for features events      */
name|String
name|FEATURE_EVENT_TYPE
init|=
literal|"org.apache.karaf.features.featureEvent"
decl_stmt|;
name|String
name|FEATURE_EVENT_EVENT_TYPE
init|=
literal|"Type"
decl_stmt|;
name|String
name|FEATURE_EVENT_EVENT_TYPE_INSTALLED
init|=
literal|"Installed"
decl_stmt|;
name|String
name|FEATURE_EVENT_EVENT_TYPE_UNINSTALLED
init|=
literal|"Uninstalled"
decl_stmt|;
comment|/**      * The item names in the CompositeData representing a feature      */
name|String
index|[]
name|FEATURE
init|=
block|{
name|FEATURE_NAME
block|,
name|FEATURE_VERSION
block|,
name|FEATURE_DEPENDENCIES
block|,
name|FEATURE_BUNDLES
block|,
name|FEATURE_CONFIGURATIONS
block|,
name|FEATURE_CONFIGURATIONFILES
block|,
name|FEATURE_INSTALLED
block|}
decl_stmt|;
name|String
index|[]
name|FEATURE_IDENTIFIER
init|=
block|{
name|FEATURE_NAME
block|,
name|FEATURE_VERSION
block|}
decl_stmt|;
name|String
index|[]
name|FEATURE_CONFIG
init|=
block|{
name|FEATURE_CONFIG_PID
block|,
name|FEATURE_CONFIG_ELEMENTS
block|}
decl_stmt|;
name|String
index|[]
name|FEATURE_CONFIG_FILES
init|=
block|{
name|FEATURE_CONFIG_FILES_ELEMENTS
block|}
decl_stmt|;
name|String
index|[]
name|FEATURE_CONFIG_ELEMENT
init|=
block|{
name|FEATURE_CONFIG_ELEMENT_KEY
block|,
name|FEATURE_CONFIG_ELEMENT_VALUE
block|}
decl_stmt|;
comment|/**      * The item names in the CompositeData representing the event raised for      * feature events within the OSGi container by this bean      */
name|String
index|[]
name|FEATURE_EVENT
init|=
block|{
name|FEATURE_NAME
block|,
name|FEATURE_VERSION
block|,
name|FEATURE_EVENT_EVENT_TYPE
block|}
decl_stmt|;
name|String
name|REPOSITORY_NAME
init|=
literal|"Name"
decl_stmt|;
name|String
name|REPOSITORY_URI
init|=
literal|"Uri"
decl_stmt|;
name|String
name|REPOSITORY_REPOSITORIES
init|=
literal|"Repositories"
decl_stmt|;
name|String
name|REPOSITORY_FEATURES
init|=
literal|"Features"
decl_stmt|;
comment|/**      * The type of the event which is emitted for repositories events      */
name|String
name|REPOSITORY_EVENT_TYPE
init|=
literal|"org.apache.karaf.features.repositoryEvent"
decl_stmt|;
name|String
name|REPOSITORY_EVENT_EVENT_TYPE
init|=
literal|"Type"
decl_stmt|;
name|String
name|REPOSITORY_EVENT_EVENT_TYPE_ADDED
init|=
literal|"Added"
decl_stmt|;
name|String
name|REPOSITORY_EVENT_EVENT_TYPE_REMOVED
init|=
literal|"Removed"
decl_stmt|;
comment|/**      * The item names in the CompositeData representing a feature      */
name|String
index|[]
name|REPOSITORY
init|=
block|{
name|REPOSITORY_NAME
block|,
name|REPOSITORY_URI
block|,
name|REPOSITORY_REPOSITORIES
block|,
name|REPOSITORY_FEATURES
block|}
decl_stmt|;
comment|/**      * The item names in the CompositeData representing the event raised for      * feature events within the OSGi container by this bean      */
name|String
index|[]
name|REPOSITORY_EVENT
init|=
block|{
name|REPOSITORY_URI
block|,
name|REPOSITORY_EVENT_EVENT_TYPE
block|}
decl_stmt|;
block|}
end_interface

end_unit

