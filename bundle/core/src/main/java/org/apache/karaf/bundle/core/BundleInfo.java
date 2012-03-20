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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_interface
specifier|public
interface|interface
name|BundleInfo
block|{
name|long
name|getBundleId
parameter_list|()
function_decl|;
name|String
name|getSymbolicName
parameter_list|()
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
name|String
name|getUpdateLocation
parameter_list|()
function_decl|;
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**      * Combined bundle state from OSGi and all BundleStateServices      * @return      */
name|BundleState
name|getState
parameter_list|()
function_decl|;
name|int
name|getStartLevel
parameter_list|()
function_decl|;
name|boolean
name|isFragment
parameter_list|()
function_decl|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|getFragments
parameter_list|()
function_decl|;
name|List
argument_list|<
name|Bundle
argument_list|>
name|getFragmentHosts
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

