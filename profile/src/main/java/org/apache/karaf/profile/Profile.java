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
name|FeaturePattern
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
name|LocationPattern
import|;
end_import

begin_comment
comment|/**  *<p>A<em>profile</em> is a container for configuration that can be applied to Karaf distribution.</p>  *  *<p>Profiles may inherit from other (single or multiple) profiles. An<em>overlay</em> profile is single  * profile with all the configurations, attributes and files from parent profiles, while configurations,  * attributes and files from<em>child</em> profile overwrites corresponding data from parent profiles.</p>  *  *<p>Configuration include:<ul>  *<li>Attributes</li>  *<li>ConfigAdmin configurations (PIDs) to put into<code>${karaf.etc}</code> directory</li>  *<li>Other resources to put into<code>${karaf.etc}</code> directory</li>  *</ul></p>  *  *<p>Attributes are properties in special file<code>profile.cfg</code> (<code>profile</code> PID) and may specify:<ul>  *<li>OSGi bundles to install (prefix:<code>bundle.</code>)</li>  *<li>Karaf features to install (prefix:<code>feature.</code>)</li>  *<li>Feature XML repositories to use to resolve bundles and features (prefix:<code>repository.</code>)</li>  *<li>Identifiers of parent profiles (property name:<code>attribute.parents</code>)</li>  *<li>Indication of abstract profile (property name:<code>abstract</code>)</li>  *<li>Indication of hidden profile (property name:<code>hidden</code>)</li>  *<li>Different attributes (prefix:<code>attribute.</code>)</li>  *<li>Properties to be added to<code>etc/config.properties</code> (prefix:<code>config.</code>)</li>  *<li>Properties to be added to<code>etc/system.properties</code> (prefix:<code>system.</code>)</li>  *<li>Additional libraries to be added to<code>lib</code> (prefix:<code>library.</code>)</li>  *<li>Additional libraries to be added to<code>lib/boot</code> (prefix:<code>boot.</code>)</li>  *<li>Additional libraries to be added to<code>lib/endorsed</code> (prefix:<code>endorsed.</code>)</li>  *<li>Additional libraries to be added to<code>lib/ext</code> (prefix:<code>ext.</code>)</li>  *<li>Bundle override definitions to be added to<code>etc/overrides.properties</code> (prefix:<code>override.</code>)</li>  *<li>Optional {@link org.osgi.resource.Resource resources} to be used during resolution (prefix:<code>optional.</code>)</li>  *</ul></p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Profile
extends|extends
name|ProfileConstants
block|{
comment|/**      * Returns an attribute map of this profile      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/**      * Returns a property map for additional properties to be added to<code>${karaf.etc}/config.properties</code>      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getConfig
parameter_list|()
function_decl|;
comment|/**      * Returns a property map for additional properties to be added to<code>${karaf.etc}/system.properties</code>      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSystem
parameter_list|()
function_decl|;
comment|/**      * Returns a unique identifier of this profile      * @return      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Returns a list of parent profile identifiers for this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getParentIds
parameter_list|()
function_decl|;
comment|/**      * Returns a list of bundles (bundle URIs) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getBundles
parameter_list|()
function_decl|;
comment|/**      * Returns a list of features (<code>feature-name[/feature-version]</code>) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getFeatures
parameter_list|()
function_decl|;
comment|/**      * Returns a list of features XML repositories (URIs) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
function_decl|;
comment|/**      * Returns a list of blacklisted bundles (URIs) (as {@link LocationPattern location patterns}      * @return      */
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|getBlacklistedBundles
parameter_list|()
function_decl|;
comment|/**      * Returns a list of blacklisted {@link FeaturePattern feature patterns}      * @return      */
name|List
argument_list|<
name|FeaturePattern
argument_list|>
name|getBlacklistedFeatures
parameter_list|()
function_decl|;
comment|/**      * Returns a list of blacklisted features XML repositories (URIs) (as {@link LocationPattern location patterns}      * @return      */
name|List
argument_list|<
name|LocationPattern
argument_list|>
name|getBlacklistedRepositories
parameter_list|()
function_decl|;
comment|/**      * Returns a list of libraries (to be added to<code>${karaf.home}/lib</code>) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getLibraries
parameter_list|()
function_decl|;
comment|/**      * Returns a list of boot libraries (to be added to<code>${karaf.home}/lib/boot</code>) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getBootLibraries
parameter_list|()
function_decl|;
comment|/**      * Returns a list of endorsed libraries (to be added to<code>${karaf.home}/lib/endorsed</code>) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getEndorsedLibraries
parameter_list|()
function_decl|;
comment|/**      * Returns a list of extension libraries (to be added to<code>${karaf.home}/lib/ext</code>) defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getExtLibraries
parameter_list|()
function_decl|;
comment|/**      * Returns a list of bundle override definitions (to be added to<code>${karaf.etc}/overrides.properties</code>)      * defined in this profile      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getOverrides
parameter_list|()
function_decl|;
comment|/**      * Returns a list of optional {@link org.osgi.resource.Resource resources} (URIs) to be used during      * resolution      * @return      */
name|List
argument_list|<
name|String
argument_list|>
name|getOptionals
parameter_list|()
function_decl|;
comment|/**      * Get the configuration file names that are available on this profile. This list should contain at least      *<code>profile.cfg</code> file.      *      * @return The configuration file names in the profile.      */
name|Set
argument_list|<
name|String
argument_list|>
name|getConfigurationFileNames
parameter_list|()
function_decl|;
comment|/**      * Get all file configurations. This list should contain at least      *<code>profile.cfg</code> file.      *      * @return The file configurations in the profile.      */
name|Map
argument_list|<
name|String
argument_list|,
name|byte
index|[]
argument_list|>
name|getFileConfigurations
parameter_list|()
function_decl|;
comment|/**      * Get the configuration file for the given name.      *      * @param fileName The file configuration name to look for in the profile.      * @return The file configuration in the profile.      */
name|byte
index|[]
name|getFileConfiguration
parameter_list|(
name|String
name|fileName
parameter_list|)
function_decl|;
comment|/**      * Get all configuration properties.This list should contain at least      * configuration from main profile file -<code>profile.cfg</code>.      *      * @return The configurations in the profile.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|getConfigurations
parameter_list|()
function_decl|;
comment|/**      * Get the configuration properties for the given PID.      *      * @param pid The configuration PID to look for.      * @return An empty map if the there is no configuration for the given pid.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getConfiguration
parameter_list|(
name|String
name|pid
parameter_list|)
function_decl|;
comment|/**      * Indicate if this profile is an overlay or not. An<em>overlay</em> profile includes configurations and      * attributes of parent profiles, while descendant profiles always have priority over parent profiles.      *      * @return True if the profile is an overlay, false else.      */
name|boolean
name|isOverlay
parameter_list|()
function_decl|;
comment|/**      * Return true if this profile is Abstract.      * Abstract profiles should not be provisioned by default, they are intended to be inherited.      *      * @return True if the profile is abstract, false else.      */
name|boolean
name|isAbstract
parameter_list|()
function_decl|;
comment|/**      * Return true if this profile is hidden.      * Hidden profiles are not listed by default.      *      * @return True if the profile is hidden, false else.      */
name|boolean
name|isHidden
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

