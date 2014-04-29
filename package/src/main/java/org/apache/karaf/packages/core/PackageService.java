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
name|packages
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
name|SortedMap
import|;
end_import

begin_interface
specifier|public
interface|interface
name|PackageService
block|{
comment|/** 	 * Gets the simplified package exports of a bundle. This does not show the  	 * package versions. 	 *  	 * @param bundleId 	 * @return 	 */
name|List
argument_list|<
name|String
argument_list|>
name|getExports
parameter_list|(
name|long
name|bundleId
parameter_list|)
function_decl|;
name|List
argument_list|<
name|String
argument_list|>
name|getImports
parameter_list|(
name|long
name|bundleId
parameter_list|)
function_decl|;
comment|/** 	 * Gets a map of all exported packages with their version and the bundles that export them 	 * The key is in the form packagename:version. 	 *  	 * @return  	 */
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PackageVersion
argument_list|>
name|getExports
parameter_list|()
function_decl|;
comment|/**      * Gets a map of all package imports.       * The key is the import filter.      *        * @return      */
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PackageRequirement
argument_list|>
name|getImports
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

