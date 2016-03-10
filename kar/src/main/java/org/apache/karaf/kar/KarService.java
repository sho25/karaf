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
name|kar
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * The service managing KAR.  */
end_comment

begin_interface
specifier|public
interface|interface
name|KarService
block|{
comment|/**      * Install KAR from a given URI      *       * Resources will be copied to the karaf base dir      * Repository contents will be copied to a subdir in the       * karaf data directory      *      * @param karUri Uri of the kar to be installed      * @throws Exception in case of installation failure.      */
name|void
name|install
parameter_list|(
name|URI
name|karUri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Install a kar with manually given repository and       * resource directories.      *       * @param karUri Uri of the kar to be installed      * @param repoDir destination for the repository contents of the kar      * @param resourceDir destination for the resource contents of the kar      * @throws Exception in case of installation failure.      */
name|void
name|install
parameter_list|(
name|URI
name|karUri
parameter_list|,
name|File
name|repoDir
parameter_list|,
name|File
name|resourceDir
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Uninstall the given KAR      *      * @param name the name of the KAR      * @throws Exception in case of failure      */
name|void
name|uninstall
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * List the KAR stored in the data folder.      *       * @return the list of KAR stored.      * @throws Exception in case of listing failure.      */
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Create a kar from the given feature and repo names.      * Each named feature including all transitive deps will be added.      * For each named repo all features in the repo and their transitive deps will be added.      *       * @param repoName the feature repository to use to create the kar.      * @param features the list of features to include in the created kar.      * @param console the console stream where to print details.      */
name|void
name|create
parameter_list|(
name|String
name|repoName
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|features
parameter_list|,
name|PrintStream
name|console
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

