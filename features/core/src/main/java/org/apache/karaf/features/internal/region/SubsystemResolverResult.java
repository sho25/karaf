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
name|region
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
name|features
operator|.
name|BundleInfo
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
name|internal
operator|.
name|download
operator|.
name|StreamProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraph
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
name|BundleException
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
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Wire
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|resolver
operator|.
name|ResolveContext
import|;
end_import

begin_comment
comment|/**  * Public API of {@link SubsystemResolver} - for the purpose of documentation and categorization to public and internal  * methods. This interface groups methods invoked after performing resolution of {@link Subsystem subsystems}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SubsystemResolverResult
block|{
comment|/**      * Get a map between regions, bundle locations and actual {@link BundleInfo}      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|BundleInfo
argument_list|>
argument_list|>
name|getBundleInfos
parameter_list|()
function_decl|;
comment|/**      * Get map of all downloaded resources (location -&gt; provider)      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|StreamProvider
argument_list|>
name|getProviders
parameter_list|()
function_decl|;
comment|/**      * Returns a result of {@link org.osgi.service.resolver.Resolver#resolve(ResolveContext)}      * @return      */
name|Map
argument_list|<
name|Resource
argument_list|,
name|List
argument_list|<
name|Wire
argument_list|>
argument_list|>
name|getWiring
parameter_list|()
function_decl|;
comment|/**      * Return directed graph of {@link org.eclipse.equinox.region.Region regions} after resolution.      * @return      * @throws BundleException      * @throws InvalidSyntaxException      */
name|RegionDigraph
name|getFlatDigraph
parameter_list|()
throws|throws
name|BundleException
throws|,
name|InvalidSyntaxException
function_decl|;
comment|/**      * Returns a mapping between regions and a set of bundle {@link Resource resources}      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Resource
argument_list|>
argument_list|>
name|getBundlesPerRegions
parameter_list|()
function_decl|;
comment|/**      * Returns a mapping between regions and a set of feature {@link Resource resources}      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Resource
argument_list|>
argument_list|>
name|getFeaturesPerRegions
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

