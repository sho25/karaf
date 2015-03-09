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
name|impl
operator|.
name|ProfileBuilderImpl
import|;
end_import

begin_comment
comment|/**  * A profile builder.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProfileBuilder
block|{
name|ProfileBuilder
name|addAttribute
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setAttributes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
parameter_list|)
function_decl|;
name|ProfileBuilder
name|from
parameter_list|(
name|Profile
name|profile
parameter_list|)
function_decl|;
name|ProfileBuilder
name|identity
parameter_list|(
name|String
name|profileId
parameter_list|)
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getParents
parameter_list|()
function_decl|;
name|ProfileBuilder
name|addParent
parameter_list|(
name|String
name|parentId
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addParents
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|parentIds
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setParents
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|parentIds
parameter_list|)
function_decl|;
name|ProfileBuilder
name|removeParent
parameter_list|(
name|String
name|parentId
parameter_list|)
function_decl|;
name|Set
argument_list|<
name|String
argument_list|>
name|getConfigurationKeys
parameter_list|()
function_decl|;
comment|/**      * Returns a copy of the configuration with the specified pid      * or an empty map if it does not exist yet.      * The copy should be used for updates and then used with      * {@link #addConfiguration(String, java.util.Map)} to keep      * the layout and comments.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getConfiguration
parameter_list|(
name|String
name|pid
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addConfiguration
parameter_list|(
name|String
name|pid
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|config
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addConfiguration
parameter_list|(
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setConfigurations
parameter_list|(
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
name|configs
parameter_list|)
function_decl|;
name|ProfileBuilder
name|deleteConfiguration
parameter_list|(
name|String
name|pid
parameter_list|)
function_decl|;
name|Set
argument_list|<
name|String
argument_list|>
name|getFileConfigurationKeys
parameter_list|()
function_decl|;
name|byte
index|[]
name|getFileConfiguration
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addFileConfiguration
parameter_list|(
name|String
name|fileName
parameter_list|,
name|byte
index|[]
name|data
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setFileConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|configs
parameter_list|)
function_decl|;
name|ProfileBuilder
name|deleteFileConfiguration
parameter_list|(
name|String
name|fileName
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setBundles
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addBundle
parameter_list|(
name|String
name|value
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setFeatures
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addFeature
parameter_list|(
name|String
name|value
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setRepositories
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
function_decl|;
name|ProfileBuilder
name|addRepository
parameter_list|(
name|String
name|value
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setOverrides
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setOptionals
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
function_decl|;
name|ProfileBuilder
name|setOverlay
parameter_list|(
name|boolean
name|overlay
parameter_list|)
function_decl|;
name|Profile
name|getProfile
parameter_list|()
function_decl|;
specifier|final
class|class
name|Factory
block|{
specifier|public
specifier|static
name|ProfileBuilder
name|create
parameter_list|()
block|{
return|return
operator|new
name|ProfileBuilderImpl
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|ProfileBuilder
name|create
parameter_list|(
name|String
name|profileId
parameter_list|)
block|{
return|return
operator|new
name|ProfileBuilderImpl
argument_list|()
operator|.
name|identity
argument_list|(
name|profileId
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ProfileBuilder
name|createFrom
parameter_list|(
name|Profile
name|profile
parameter_list|)
block|{
return|return
operator|new
name|ProfileBuilderImpl
argument_list|()
operator|.
name|from
argument_list|(
name|profile
argument_list|)
return|;
block|}
comment|// Hide ctor
specifier|private
name|Factory
parameter_list|()
block|{         }
block|}
block|}
end_interface

end_unit

