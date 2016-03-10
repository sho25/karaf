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
name|profile
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * The profile service  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProfileService
block|{
comment|/**      * Acquire a write lock for the profile.      *      * @return The write lock handler.      */
name|LockHandle
name|acquireWriteLock
parameter_list|()
function_decl|;
comment|/**      * Acquire a read lock for the profile.      * A read lock cannot be upgraded to a write lock.      *      * @return The read lock handler.      */
name|LockHandle
name|acquireReadLock
parameter_list|()
function_decl|;
comment|/**      * Register the given resolver.      *      * @param resolver The resolver to register.      */
name|void
name|registerResolver
parameter_list|(
name|PlaceholderResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Unregister the given resolver.      *      * @param resolver The resolver to unregister.      */
name|void
name|unregisterResolver
parameter_list|(
name|PlaceholderResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Create the given profile in the data store.      *      * @param profile The profile to create.      */
name|void
name|createProfile
parameter_list|(
name|Profile
name|profile
parameter_list|)
function_decl|;
comment|/**      * Create the given profile in the data store.      *      * @param profile The profile to update.      */
name|void
name|updateProfile
parameter_list|(
name|Profile
name|profile
parameter_list|)
function_decl|;
comment|/**      * True if the given profile exists in the given version.      *      * @param profileId The profile ID.      * @return True if the given profile exists, false else.      */
name|boolean
name|hasProfile
parameter_list|(
name|String
name|profileId
parameter_list|)
function_decl|;
comment|/**      * Get the profile for the given version and id.      *      * @param profileId The profile ID.      * @return The profile or null if not found.      */
name|Profile
name|getProfile
parameter_list|(
name|String
name|profileId
parameter_list|)
function_decl|;
comment|/**      * Get the profile for the given version and id.      *      * @param profileId The profile ID.      * @return The profile or null if not found.      */
name|Profile
name|getRequiredProfile
parameter_list|(
name|String
name|profileId
parameter_list|)
function_decl|;
comment|/**       * Get the list of profiles associated with the given version.      *      * @return The collection of all profiles.      */
name|Collection
argument_list|<
name|String
argument_list|>
name|getProfiles
parameter_list|()
function_decl|;
comment|/**      * Delete the given profile from the data store.      *      * @param profileId The profile ID to remove.      */
name|void
name|deleteProfile
parameter_list|(
name|String
name|profileId
parameter_list|)
function_decl|;
comment|/**      * Compute the overlay profile.      *      * The overlay profile is computed by getting all the parent profiles      * and overriding the settings by children profiles.      *      * @param profile The profile.      * @return The overlay profile.      */
name|Profile
name|getOverlayProfile
parameter_list|(
name|Profile
name|profile
parameter_list|)
function_decl|;
comment|/**      * Compute the overlay profile.      *      * The overlay profile is computed by getting all the parent profiles      * and overriding the settings by children profiles.      *      * @param profile The profile.      * @param environment The environment.      * @return The overlay profile.      */
name|Profile
name|getOverlayProfile
parameter_list|(
name|Profile
name|profile
parameter_list|,
name|String
name|environment
parameter_list|)
function_decl|;
comment|/**      * Compute the effective profile.      *      * The effective profile is computed by performing all substitutions      * in the given profile configurations.      *      * @param profile The profile to compute.      * @return The effective profile.      */
name|Profile
name|getEffectiveProfile
parameter_list|(
name|Profile
name|profile
parameter_list|)
function_decl|;
comment|/**      * Compute the effective profile.      *      * The effective profile is computed by performing all substitutions      * in the given profile configurations.      *      * @param profile The profile to compute.      * @param defaultsToEmptyString if no substitution is valid, defaults to an empty string.      * @return The effective profile.      */
name|Profile
name|getEffectiveProfile
parameter_list|(
name|Profile
name|profile
parameter_list|,
name|boolean
name|defaultsToEmptyString
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

