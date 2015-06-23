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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A feature is a list of bundles associated identified by its name.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Feature
block|{
name|String
name|DEFAULT_INSTALL_MODE
init|=
literal|"auto"
decl_stmt|;
name|String
name|getId
parameter_list|()
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
name|String
name|getDescription
parameter_list|()
function_decl|;
name|String
name|getDetails
parameter_list|()
function_decl|;
name|String
name|getVersion
parameter_list|()
function_decl|;
name|boolean
name|hasVersion
parameter_list|()
function_decl|;
name|String
name|getResolver
parameter_list|()
function_decl|;
name|String
name|getInstall
parameter_list|()
function_decl|;
name|boolean
name|isHidden
parameter_list|()
function_decl|;
name|List
argument_list|<
name|Dependency
argument_list|>
name|getDependencies
parameter_list|()
function_decl|;
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|getBundles
parameter_list|()
function_decl|;
comment|//    Map<String, Map<String, String>> getConfigurations();
name|List
argument_list|<
name|ConfigInfo
argument_list|>
name|getConfigurations
parameter_list|()
function_decl|;
name|List
argument_list|<
name|ConfigFileInfo
argument_list|>
name|getConfigurationFiles
parameter_list|()
function_decl|;
name|List
argument_list|<
name|?
extends|extends
name|Conditional
argument_list|>
name|getConditional
parameter_list|()
function_decl|;
name|int
name|getStartLevel
parameter_list|()
function_decl|;
name|List
argument_list|<
name|?
extends|extends
name|Capability
argument_list|>
name|getCapabilities
parameter_list|()
function_decl|;
name|List
argument_list|<
name|?
extends|extends
name|Requirement
argument_list|>
name|getRequirements
parameter_list|()
function_decl|;
name|Scoping
name|getScoping
parameter_list|()
function_decl|;
name|List
argument_list|<
name|?
extends|extends
name|Library
argument_list|>
name|getLibraries
parameter_list|()
function_decl|;
name|String
name|getNamespace
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

