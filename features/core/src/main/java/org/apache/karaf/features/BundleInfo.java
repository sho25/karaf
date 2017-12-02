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

begin_comment
comment|/**  * A bundle info holds info about a Bundle.  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundleInfo
extends|extends
name|Blacklisting
block|{
name|String
name|getLocation
parameter_list|()
function_decl|;
name|String
name|getOriginalLocation
parameter_list|()
function_decl|;
name|int
name|getStartLevel
parameter_list|()
function_decl|;
name|boolean
name|isStart
parameter_list|()
function_decl|;
name|boolean
name|isDependency
parameter_list|()
function_decl|;
name|BundleInfo
operator|.
name|BundleOverrideMode
name|isOverriden
parameter_list|()
function_decl|;
specifier|public
enum|enum
name|BundleOverrideMode
block|{
comment|/**          * No override          */
name|NONE
block|,
comment|/**          * Compatibility with<code>${karaf.etc}/overrides.properties</code> - requires access to original and          * replacement bundle's headers to compare version and symbolic name.          */
name|OSGI
block|,
comment|/**          * Simpler option that's just static override - doesn't require accessing and checking the bundle/resource          * being overriden.          */
name|MAVEN
block|}
block|}
end_interface

end_unit

