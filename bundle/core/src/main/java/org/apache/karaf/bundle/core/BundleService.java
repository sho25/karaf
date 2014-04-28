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
name|bundle
operator|.
name|core
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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|wiring
operator|.
name|BundleRequirement
import|;
end_import

begin_interface
specifier|public
interface|interface
name|BundleService
block|{
name|String
name|SYSTEM_BUNDLES_ROLE
init|=
literal|"systembundles"
decl_stmt|;
name|BundleInfo
name|getInfo
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|selectBundles
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|boolean
name|defaultAllBundles
parameter_list|)
function_decl|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|selectBundles
parameter_list|(
name|String
name|context
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|,
name|boolean
name|defaultAllBundles
parameter_list|)
function_decl|;
name|Bundle
name|getBundle
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
name|Bundle
name|getBundle
parameter_list|(
name|String
name|context
parameter_list|,
name|String
name|id
parameter_list|)
function_decl|;
name|String
name|getDiag
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
name|List
argument_list|<
name|BundleRequirement
argument_list|>
name|getUnsatisfiedRequirements
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|String
name|namespace
parameter_list|)
function_decl|;
name|Map
argument_list|<
name|String
argument_list|,
name|Bundle
argument_list|>
name|getWiredBundles
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
name|boolean
name|isDynamicImport
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
name|void
name|enableDynamicImports
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
name|void
name|disableDynamicImports
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
function_decl|;
name|int
name|getSystemBundleThreshold
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

