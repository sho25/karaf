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
name|Feature
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
name|FeaturesService
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
name|Region
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
name|BundleRevision
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
name|repository
operator|.
name|Repository
import|;
end_import

begin_comment
comment|/**  * Public API of {@link SubsystemResolver} - for the purpose of documentation and categorization to public and internal  * methods. This interface groups methods related to resolution of {@link Subsystem subsystems}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SubsystemResolverResolution
block|{
comment|/**      * Prepares the resolver by configuring {@link Subsystem} hierarchy.      * The input is a mapping from {@link Region region names} to a set of logical requirements.      * The effect is:<ul>      *<li>A tree of {@link Subsystem subsystems} where the root subsystem represents {@link FeaturesService#ROOT_REGION}      *      with regions like<code>root/app1</code> represented as child subsystems.</li>      *<li>A subsystem is created for each feature requirement and added as child and requirement for given region's subsystem</li>      *<li>Each subsystem for a feature has optional requirements for conditional features</li>      *</ul>      *      * @param allFeatures all currently available features partitioned by name.      * @param requirements desired mapping from regions to logical requirements.      * @param system mapping from regions to unmanaged {@link BundleRevision}s.      */
name|void
name|prepare
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Feature
argument_list|>
argument_list|>
name|allFeatures
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|requirements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|BundleRevision
argument_list|>
argument_list|>
name|system
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Before attempting {@link #resolve resolution}, we can collect features' prerequisites. If there are any,      * caller may decide to deploy another set of requirements<strong>before</strong> the initial ones.      * Prerequisites allow to install for example<code>wrap</code> feature before installing a feature with bundle      * using<code>wrap:</code> protocol.      *      * @return The collected prerequisites.      */
name|Set
argument_list|<
name|String
argument_list|>
name|collectPrerequisites
parameter_list|()
function_decl|;
specifier|public
name|Map
argument_list|<
name|Resource
argument_list|,
name|List
argument_list|<
name|Wire
argument_list|>
argument_list|>
name|resolve
parameter_list|(
name|String
name|featureResolutionRange
parameter_list|,
name|FeaturesService
operator|.
name|ServiceRequirementsBehavior
name|serviceRequirements
parameter_list|,
specifier|final
name|Repository
name|globalRepository
parameter_list|,
name|String
name|outputFile
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

