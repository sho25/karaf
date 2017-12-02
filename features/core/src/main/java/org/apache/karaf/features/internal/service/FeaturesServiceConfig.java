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
operator|.
name|internal
operator|.
name|service
package|;
end_package

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

begin_class
specifier|public
class|class
name|FeaturesServiceConfig
block|{
comment|/**      * Range to use when a version is specified on a feature dependency.      * The default is {@link org.apache.karaf.features.FeaturesService#DEFAULT_FEATURE_RESOLUTION_RANGE}      */
specifier|public
specifier|final
name|String
name|featureResolutionRange
decl_stmt|;
comment|/**      * Range to use when verifying if a bundle should be updated or      * new bundle installed.      * The default is {@link org.apache.karaf.features.FeaturesService#DEFAULT_BUNDLE_UPDATE_RANGE}      */
specifier|public
specifier|final
name|String
name|bundleUpdateRange
decl_stmt|;
comment|/**      * Use CRC to check snapshot bundles and update them if changed.      * Either:      * - none : never update snapshots      * - always : always update snapshots      * - crc : use CRC to detect changes      */
specifier|public
specifier|final
name|String
name|updateSnapshots
decl_stmt|;
specifier|public
specifier|final
name|int
name|downloadThreads
decl_stmt|;
specifier|public
specifier|final
name|long
name|scheduleDelay
decl_stmt|;
specifier|public
specifier|final
name|int
name|scheduleMaxRun
decl_stmt|;
comment|/**      * Service requirements enforcement      */
specifier|public
specifier|final
name|String
name|serviceRequirements
decl_stmt|;
comment|/**      * Location of<code>etc/blacklisted.properties</code>      */
annotation|@
name|Deprecated
specifier|public
specifier|final
name|String
name|blacklisted
decl_stmt|;
comment|/**      * Location of<code>etc/org.apache.karaf.features.xml</code>      */
specifier|public
specifier|final
name|String
name|featureModifications
decl_stmt|;
comment|/**      * Location of<code>etc/overrides.properties</code>      */
annotation|@
name|Deprecated
specifier|public
specifier|final
name|String
name|overrides
decl_stmt|;
specifier|public
name|FeaturesServiceConfig
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FeaturesServiceConfig
parameter_list|(
name|String
name|featureModifications
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_BUNDLE_UPDATE_RANGE
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
name|featureModifications
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|FeaturesServiceConfig
parameter_list|(
name|String
name|overrides
parameter_list|,
name|String
name|blacklisted
parameter_list|,
name|String
name|featureModifications
parameter_list|)
block|{
name|this
argument_list|(
name|overrides
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_FEATURE_RESOLUTION_RANGE
argument_list|,
name|FeaturesService
operator|.
name|DEFAULT_BUNDLE_UPDATE_RANGE
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|blacklisted
argument_list|,
name|featureModifications
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FeaturesServiceConfig
parameter_list|(
name|String
name|featureResolutionRange
parameter_list|,
name|String
name|bundleUpdateRange
parameter_list|,
name|String
name|updateSnapshots
parameter_list|,
name|int
name|downloadThreads
parameter_list|,
name|long
name|scheduleDelay
parameter_list|,
name|int
name|scheduleMaxRun
parameter_list|,
name|String
name|featureModifications
parameter_list|,
name|String
name|serviceRequirements
parameter_list|)
block|{
name|this
operator|.
name|overrides
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|featureResolutionRange
operator|=
name|featureResolutionRange
expr_stmt|;
name|this
operator|.
name|bundleUpdateRange
operator|=
name|bundleUpdateRange
expr_stmt|;
name|this
operator|.
name|updateSnapshots
operator|=
name|updateSnapshots
expr_stmt|;
name|this
operator|.
name|downloadThreads
operator|=
name|downloadThreads
expr_stmt|;
name|this
operator|.
name|scheduleDelay
operator|=
name|scheduleDelay
expr_stmt|;
name|this
operator|.
name|scheduleMaxRun
operator|=
name|scheduleMaxRun
expr_stmt|;
name|this
operator|.
name|blacklisted
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|featureModifications
operator|=
name|featureModifications
expr_stmt|;
name|this
operator|.
name|serviceRequirements
operator|=
name|serviceRequirements
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|FeaturesServiceConfig
parameter_list|(
name|String
name|overrides
parameter_list|,
name|String
name|featureResolutionRange
parameter_list|,
name|String
name|bundleUpdateRange
parameter_list|,
name|String
name|updateSnapshots
parameter_list|,
name|int
name|downloadThreads
parameter_list|,
name|long
name|scheduleDelay
parameter_list|,
name|int
name|scheduleMaxRun
parameter_list|,
name|String
name|blacklisted
parameter_list|,
name|String
name|featureModifications
parameter_list|,
name|String
name|serviceRequirements
parameter_list|)
block|{
name|this
operator|.
name|overrides
operator|=
name|overrides
expr_stmt|;
name|this
operator|.
name|featureResolutionRange
operator|=
name|featureResolutionRange
expr_stmt|;
name|this
operator|.
name|bundleUpdateRange
operator|=
name|bundleUpdateRange
expr_stmt|;
name|this
operator|.
name|updateSnapshots
operator|=
name|updateSnapshots
expr_stmt|;
name|this
operator|.
name|downloadThreads
operator|=
name|downloadThreads
expr_stmt|;
name|this
operator|.
name|scheduleDelay
operator|=
name|scheduleDelay
expr_stmt|;
name|this
operator|.
name|scheduleMaxRun
operator|=
name|scheduleMaxRun
expr_stmt|;
name|this
operator|.
name|blacklisted
operator|=
name|blacklisted
expr_stmt|;
name|this
operator|.
name|featureModifications
operator|=
name|featureModifications
expr_stmt|;
name|this
operator|.
name|serviceRequirements
operator|=
name|serviceRequirements
expr_stmt|;
block|}
block|}
end_class

end_unit

