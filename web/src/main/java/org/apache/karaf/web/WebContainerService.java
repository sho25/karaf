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
name|web
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
comment|/**  * Describe the WebContainer service.  */
end_comment

begin_interface
specifier|public
interface|interface
name|WebContainerService
block|{
comment|/**      * List of web bundles deployed in the web container.      *      * @return the list of web bundles.      * @throws Exception in case of listing failure.      */
name|List
argument_list|<
name|WebBundle
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Get a string representation of the web state of a bundle (identified by id).      *      * @param bundleId the bundle ID.      * @return the string representation of the bundle web state.      * @throws Exception in case of "mapping" failure.      */
name|String
name|state
parameter_list|(
name|long
name|bundleId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Start the web context of given bundles (identified by an ID).      *      * @param bundleIds the list of bundle IDs (TODO use a BundleSelector service).      * @throws Exception in case of deploy failure.      */
name|void
name|start
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stop the web context of given bundles (identified by an ID).      *      * @param bundleIds the list of bundle IDs (TODO use a BundleSelector service).      * @throws Exception in case of undeploy failure.      */
name|void
name|stop
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Retrieve the Web-ContextPath of the corresponding bundle.      *       * @param id The ID of the bundle.      * @return The web context associated with the given bundle.      */
name|String
name|getWebContextPath
parameter_list|(
name|Long
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

