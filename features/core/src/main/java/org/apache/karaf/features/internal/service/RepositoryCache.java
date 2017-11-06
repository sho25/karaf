begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
operator|.
name|internal
operator|.
name|service
package|;
end_package

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
name|features
operator|.
name|Repository
import|;
end_import

begin_comment
comment|/**  *<p>An interface for accessing repository/features information. Simple implementations  * may just map feature XMLs directly to JAXB model  * (see: {@link org.apache.karaf.features.internal.model.Features}).</p>  *  *<p>In more complex cases, additional processing (blacklisting, overrides, patching)  * may be performed.</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryCache
block|{
comment|/**      * Creates {@link Repository} without adding it to cache      * @param uri an URI (e.g.,<code>mvn:groupId/artifactId/version/xml/features</code> of repository      * @param validate whether to perform XML Schema validation of loaded features XML      * @return a {@link Repository} that may be inspected or added to cache      */
name|Repository
name|create
parameter_list|(
name|URI
name|uri
parameter_list|,
name|boolean
name|validate
parameter_list|)
function_decl|;
comment|/**      * Adds existing {@link Repository} to be tracked/managed by this cache and later be available e.g., via      * {@link #getRepository(String)}      * @param repository existing repository to add to cache      */
name|void
name|addRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Removes existing {@link Repository} by its {@link URI}      * @param repositoryUri {@link URI} of the {@link Repository} to remove      */
name|void
name|removeRepository
parameter_list|(
name|URI
name|repositoryUri
parameter_list|)
function_decl|;
comment|/**      * Gets {@link Repository} by its {@link URI}      * @param uri {@link URI} of the repository      * @return {@link Repository} as it's stored inside the cache      */
name|Repository
name|getRepository
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Gets {@link Repository} by its name      * @param name Name of the repository      * @return {@link Repository} as it's stored inside the cache      */
name|Repository
name|getRepositoryByName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns an array of all cached {@link Repository repositories}      * @return list of all {@link Repository repositories}      */
name|Repository
index|[]
name|listRepositories
parameter_list|()
function_decl|;
comment|/**      * Returns an array of cached {@link Repository repositories} for a set of {@link URI repository URIs}      * @return list of matched {@link Repository repositories}      */
name|Repository
index|[]
name|listMatchingRepositories
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|uris
parameter_list|)
function_decl|;
comment|/**      * Returns a set of {@link Repository repositories} including passed repository and all referenced repositories.      * @param repo A {@link Repository}, that possibly references other feature repositories.      * @return A closure of {@link Repository repositories}      */
name|Set
argument_list|<
name|Repository
argument_list|>
name|getRepositoryClosure
parameter_list|(
name|Repository
name|repo
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

