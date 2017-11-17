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
name|profile
package|;
end_package

begin_interface
specifier|public
interface|interface
name|ProfileConstants
block|{
comment|/**      * The attribute prefix for the profile configuration (<code>profile.cfg</code>)      */
name|String
name|ATTRIBUTE_PREFIX
init|=
literal|"attribute."
decl_stmt|;
comment|/**      * The attribute key for whitespace-separated list of parent profile IDs      */
name|String
name|PARENTS
init|=
name|ATTRIBUTE_PREFIX
operator|+
literal|"parents"
decl_stmt|;
comment|/**      * The attribute key for the description of the profile      */
name|String
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
comment|/**      * The attribute key for the<em>abstract</em> flag      */
name|String
name|ABSTRACT
init|=
literal|"abstract"
decl_stmt|;
comment|/**      * The attribute key for the<em>hidden</em> flag      */
name|String
name|HIDDEN
init|=
literal|"hidden"
decl_stmt|;
comment|/**      * The attribute key for the<em>overlay</em> flag meaning the parents are already included/merged in the profile.      */
name|String
name|OVERLAY
init|=
literal|"overlay"
decl_stmt|;
comment|/**      *<p>Key indicating a deletion.</p>      *<p>This value can appear as the value of a key in a configuration      * or as a key itself.  If used as a key, the whole configuration      * is flagged as deleted from its parent when computing the overlay.</p>      */
name|String
name|DELETED
init|=
literal|"#deleted#"
decl_stmt|;
comment|/**      * The pid of the configuration holding internal profile attributes      */
name|String
name|INTERNAL_PID
init|=
literal|"profile"
decl_stmt|;
comment|/**      * The file suffix for a configuration      */
name|String
name|PROPERTIES_SUFFIX
init|=
literal|".cfg"
decl_stmt|;
comment|/**      * The prefix for attributes that are targeted for<code>${karaf.etc}/config.properties</code> file      */
name|String
name|CONFIG_PREFIX
init|=
literal|"config."
decl_stmt|;
comment|/**      * The prefix for attributes that are targeted for<code>${karaf.etc}/system.properties</code> file      */
name|String
name|SYSTEM_PREFIX
init|=
literal|"system."
decl_stmt|;
comment|/**      * The prefix for attributes that specify URIs of features XML files      */
name|String
name|REPOSITORY_PREFIX
init|=
literal|"repository."
decl_stmt|;
comment|/**      * The prefix for attributes that specify feature names (<code>name[/version]</code>) to install/use      */
name|String
name|FEATURE_PREFIX
init|=
literal|"feature."
decl_stmt|;
comment|/**      * The prefix for attributes that specify bundle URIs to install      */
name|String
name|BUNDLE_PREFIX
init|=
literal|"bundle."
decl_stmt|;
comment|/**      * The prefix for attributes that specify additional libraries to add to<code>${karaf.home}/lib</code>.      * These are native libraries only. JARs that should be available in app classpath should go to      *<code>${karaf.home}/lib/boot</code> and use {@link #BOOT_PREFIX}.      */
name|String
name|LIB_PREFIX
init|=
literal|"library."
decl_stmt|;
comment|/**      * The prefix for attributes that specify additional endorsed libraries to add to      *<code>${karaf.home}/lib/endorsed</code>      */
name|String
name|ENDORSED_PREFIX
init|=
literal|"endorsed."
decl_stmt|;
comment|/**      * The prefix for attributes that specify additional extension libraries to add to      *<code>${karaf.home}/lib/ext</code>      */
name|String
name|EXT_PREFIX
init|=
literal|"ext."
decl_stmt|;
comment|/**      * The prefix for attributes that specify additional endorsed libraries to add to      *<code>${karaf.home}/lib/boot</code>      */
name|String
name|BOOT_PREFIX
init|=
literal|"boot."
decl_stmt|;
comment|/**      * The prefix for attributes that specify bundle overrides      * (see {@link org.apache.karaf.features.internal.service.Overrides}). In version 4.2 it's better to use      * {@link org.apache.karaf.features.internal.service.FeaturesProcessor} configuration.      */
name|String
name|OVERRIDE_PREFIX
init|=
literal|"override."
decl_stmt|;
comment|/**      * The prefix for attributes that specify optional resources      */
name|String
name|OPTIONAL_PREFIX
init|=
literal|"optional."
decl_stmt|;
block|}
end_interface

end_unit

