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

begin_class
specifier|public
class|class
name|FeaturesServiceConfig
block|{
specifier|public
name|String
name|overrides
decl_stmt|;
comment|/**      * Range to use when a version is specified on a feature dependency.      * The default is {@link org.apache.karaf.features.FeaturesService#DEFAULT_FEATURE_RESOLUTION_RANGE}      */
specifier|public
name|String
name|featureResolutionRange
decl_stmt|;
comment|/**      * Range to use when verifying if a bundle should be updated or      * new bundle installed.      * The default is {@link org.apache.karaf.features.FeaturesService#DEFAULT_BUNDLE_UPDATE_RANGE}      */
specifier|public
name|String
name|bundleUpdateRange
decl_stmt|;
comment|/**      * Use CRC to check snapshot bundles and update them if changed.      * Either:      * - none : never update snapshots      * - always : always update snapshots      * - crc : use CRC to detect changes      */
specifier|public
name|String
name|updateSnapshots
decl_stmt|;
specifier|public
name|int
name|downloadThreads
init|=
literal|1
decl_stmt|;
specifier|public
name|long
name|scheduleDelay
decl_stmt|;
specifier|public
name|int
name|scheduleMaxRun
decl_stmt|;
comment|/**      * Service requirements enforcement      */
specifier|public
name|String
name|serviceRequirements
decl_stmt|;
specifier|public
name|String
name|blacklisted
decl_stmt|;
block|}
end_class

end_unit

