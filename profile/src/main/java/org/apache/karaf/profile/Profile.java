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
comment|/**  * The immutable view of a profile  */
end_comment

begin_interface
specifier|public
interface|interface
name|Profile
block|{
comment|/**      * The attribute key for the list of parents      */
name|String
name|PARENTS
init|=
literal|"parents"
decl_stmt|;
comment|/**      * The attribute key for the description of the profile      */
name|String
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
comment|/**      * The attribute key for the abstract flag      */
name|String
name|ABSTRACT
init|=
literal|"abstract"
decl_stmt|;
comment|/**      * The attribute key for the hidden flag      */
name|String
name|HIDDEN
init|=
literal|"hidden"
decl_stmt|;
comment|/**      * Key indicating a deletion.      * This value can appear as the value of a key in a configuration      * or as a key itself.  If used as a key, the whole configuration      * is flagged has been deleted from its parent when computing the      * overlay.      */
name|String
name|DELETED
init|=
literal|"#deleted#"
decl_stmt|;
comment|/**      * The pid of the configuration holding internal profile attributes      */
name|String
name|INTERNAL_PID
init|=
literal|"profile"
decl_stmt|;
comment|/**      * The file suffix for a configuration      */
name|String
name|PROPERTIES_SUFFIX
init|=
literal|".cfg"
decl_stmt|;
comment|/**      * The attribute prefix for in the agent configuration      */
name|String
name|ATTRIBUTE_PREFIX
init|=
literal|"attribute."
decl_stmt|;
comment|/**      * The config prefix for in the agent configuration      */
name|String
name|CONFIG_PREFIX
init|=
literal|"config."
decl_stmt|;
comment|/**      * The config prefix for in the agent configuration      */
name|String
name|SYSTEM_PREFIX
init|=
literal|"system."
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getConfig
parameter_list|()
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSystem
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getParentIds
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getLibraries
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getBundles
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getFeatures
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getOverrides
parameter_list|()
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getOptionals
parameter_list|()
function_decl|;
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Get the configuration file names that are available on this profile.      *      * @return The configuration file names in the profile.      */
name|Set
argument_list|<
name|String
argument_list|>
name|getConfigurationFileNames
parameter_list|()
function_decl|;
comment|/**      * Get all file configurations.      *      * @return The file configurations in the profile.      */
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|getFileConfigurations
parameter_list|()
function_decl|;
comment|/**      * Get the configuration file for the given name.      *      * @param fileName The file configuration name to look for in the profile.      * @return The file configuration in the profile.      */
name|byte
index|[]
name|getFileConfiguration
parameter_list|(
name|String
name|fileName
parameter_list|)
function_decl|;
comment|/**      * Get all configuration properties.      *      * @return The configurations in the profile.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|getConfigurations
parameter_list|()
function_decl|;
comment|/**      * Get the configuration properties for the given PID.      *      * @param pid The configuration PID to look for.      * @return An empty map if the there is no configuration for the given pid.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getConfiguration
parameter_list|(
name|String
name|pid
parameter_list|)
function_decl|;
comment|/**      * Indicate if this profile is an overlay or not.      *      * @return True if the profile is an overlay, false else.      */
name|boolean
name|isOverlay
parameter_list|()
function_decl|;
comment|/**      * Return true if this profile is Abstract.      * Abstract profiles should not be provisioned by default, they are intended to be inherited.      *      * @return True if the profile is abstract, false else.      */
name|boolean
name|isAbstract
parameter_list|()
function_decl|;
comment|/**      * Return true if this profile is hidden.      * Hidden profiles are not listed by default.      *      * @return True if the profile is hidden, false else.      */
name|boolean
name|isHidden
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

