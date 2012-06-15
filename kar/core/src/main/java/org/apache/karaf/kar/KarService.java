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
comment|/**  * The service managing KAR.  */
end_comment

begin_interface
specifier|public
interface|interface
name|KarService
block|{
comment|/**      * Install KAR from a given URL.      *      * @param url the KAR URL.      * @throws Exception in case of installation failure.      */
name|void
name|install
parameter_list|(
name|URI
name|url
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Uninstall the given KAR.      * NB: the system folder is not cleaned.      *      * @param name the name of the KAR.      * @throws Exception in case of uninstall failure.      */
name|void
name|uninstall
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Uninstall the given KAR and, eventually, cleanup the repository from the KAR content.      *      * @param name the name of the KAR.      * @param clean true to cleanup the repository folder, false else.      * @throws Exception in case of uninstall failure.      */
name|void
name|uninstall
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|clean
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
comment|/**      * Create a kar from the given feature and repo names.      * Each named feature including all transitive deps will be added.      * For each named repo all features in the repo and their transitive deps will be added.      *       * @param repoName      * @param features       * @param console      */
name|void
name|create
parameter_list|(
name|String
name|repoName
parameter_list|,
name|Set
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

