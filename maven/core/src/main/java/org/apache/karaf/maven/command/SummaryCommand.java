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
name|maven
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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
name|maven
operator|.
name|core
operator|.
name|MavenRepositoryURL
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Option
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|shell
operator|.
name|support
operator|.
name|table
operator|.
name|Row
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
name|shell
operator|.
name|support
operator|.
name|table
operator|.
name|ShellTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|settings
operator|.
name|Proxy
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"maven"
argument_list|,
name|name
operator|=
literal|"summary"
argument_list|,
name|description
operator|=
literal|"Maven configuration summary."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|SummaryCommand
extends|extends
name|MavenSecuritySupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|aliases
operator|=
block|{
literal|"--property-ids"
block|}
argument_list|,
name|description
operator|=
literal|"Use PID property identifiers instead of their names"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|propertyIds
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|aliases
operator|=
block|{
literal|"--source"
block|}
argument_list|,
name|description
operator|=
literal|"Adds information about where the value is configured"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|source
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--description"
block|}
argument_list|,
name|description
operator|=
literal|"Adds description of Maven configuration options"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|description
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doAction
parameter_list|(
name|String
name|prefix
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Option"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Value"
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Source"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|description
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Description"
argument_list|)
expr_stmt|;
block|}
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_LOCAL_REPOSITORY
else|:
literal|"Local repository"
argument_list|,
name|localRepository
argument_list|,
literal|"Maven repository to store artifacts resolved in *remote repositories*"
argument_list|)
expr_stmt|;
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_SETTINGS_FILE
else|:
literal|"Settings file"
argument_list|,
name|settings
argument_list|,
literal|"Settings file that may contain configuration of additional repositories, http proxies and mirrors"
argument_list|)
expr_stmt|;
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_SECURITY_FILE
else|:
literal|"Security settings file"
argument_list|,
name|securitySettings
argument_list|,
literal|"Settings file that contain (or relocates to) master Maven password"
argument_list|)
expr_stmt|;
if|if
condition|(
name|showPasswords
condition|)
block|{
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
literal|"<master>"
else|:
literal|"Master password"
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|securitySettings
operator|.
name|source
argument_list|,
name|masterPassword
argument_list|)
argument_list|,
literal|"Master password used to decrypt proxy and server passwords"
argument_list|)
expr_stmt|;
block|}
comment|// for default update/checksum policies specified at repository URI level, see
comment|// org.ops4j.pax.url.mvn.internal.AetherBasedResolver.addRepo()
comment|// see org.eclipse.aether.internal.impl.DefaultUpdatePolicyAnalyzer#isUpdatedRequired()
name|SourceAnd
argument_list|<
name|String
argument_list|>
name|updatePolicy
init|=
name|updatePolicy
argument_list|(
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|PROPERTY_GLOBAL_UPDATE_POLICY
argument_list|)
argument_list|)
decl_stmt|;
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_GLOBAL_UPDATE_POLICY
else|:
literal|"Global update policy"
argument_list|,
name|updatePolicy
argument_list|,
literal|"Overrides update policy specified at repository level (if specified)"
argument_list|)
expr_stmt|;
comment|// see org.eclipse.aether.internal.impl.DefaultChecksumPolicyProvider#newChecksumPolicy()
name|SourceAnd
argument_list|<
name|String
argument_list|>
name|checksumPolicy
init|=
name|checksumPolicy
argument_list|(
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|PROPERTY_GLOBAL_CHECKSUM_POLICY
argument_list|)
argument_list|)
decl_stmt|;
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_GLOBAL_CHECKSUM_POLICY
else|:
literal|"Global checksum policy"
argument_list|,
name|checksumPolicy
argument_list|,
literal|"Checksum policy for all repositories"
argument_list|)
expr_stmt|;
name|String
name|updateReleasesProperty
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|PROPERTY_UPDATE_RELEASES
argument_list|)
decl_stmt|;
name|boolean
name|updateReleases
init|=
literal|false
decl_stmt|;
name|String
name|sourceInfo
init|=
name|String
operator|.
name|format
argument_list|(
name|PATTERN_PID_PROPERTY
argument_list|,
name|PID
argument_list|,
name|prefix
operator|+
name|PROPERTY_UPDATE_RELEASES
argument_list|)
decl_stmt|;
if|if
condition|(
name|updateReleasesProperty
operator|==
literal|null
condition|)
block|{
name|sourceInfo
operator|=
literal|"Default \"false\""
expr_stmt|;
block|}
else|else
block|{
name|updateReleases
operator|=
literal|"true"
operator|.
name|equals
argument_list|(
name|updateReleasesProperty
argument_list|)
expr_stmt|;
block|}
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_UPDATE_RELEASES
else|:
literal|"Update releases"
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|sourceInfo
argument_list|,
name|updateReleases
argument_list|)
argument_list|,
literal|"Whether to download non-SNAPSHOT artifacts according to update policy"
argument_list|)
expr_stmt|;
comment|// see org.ops4j.pax.url.mvn.internal.config.MavenConfigurationImpl.isValid()
comment|// ANY non null value (even "false"!) means that we require configadmin
name|String
name|requireConfigAdminProperty
init|=
name|context
operator|.
name|getProperty
argument_list|(
name|prefix
operator|+
name|REQUIRE_CONFIG_ADMIN_CONFIG
argument_list|)
decl_stmt|;
name|boolean
name|requireConfigAdmin
init|=
name|requireConfigAdminProperty
operator|!=
literal|null
decl_stmt|;
name|sourceInfo
operator|=
literal|"Default \"false\""
expr_stmt|;
if|if
condition|(
name|requireConfigAdmin
condition|)
block|{
name|sourceInfo
operator|=
literal|"BundleContext property ("
operator|+
name|prefix
operator|+
name|REQUIRE_CONFIG_ADMIN_CONFIG
operator|+
literal|")"
expr_stmt|;
block|}
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|REQUIRE_CONFIG_ADMIN_CONFIG
else|:
literal|"Require Config Admin"
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|sourceInfo
argument_list|,
name|requireConfigAdmin
argument_list|)
argument_list|,
literal|"Whether MavenResolver service is registered ONLY with proper "
operator|+
name|PID
operator|+
literal|" PID configuration"
argument_list|)
expr_stmt|;
comment|// see org.ops4j.pax.url.mvn.internal.config.MavenConfigurationImpl.buildSettings()
name|String
name|useFallbackRepositoriesProperty
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|PROPERTY_USE_FALLBACK_REPOSITORIES
argument_list|)
decl_stmt|;
name|boolean
name|useFallbackRepositories
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|useFallbackRepositoriesProperty
argument_list|)
decl_stmt|;
name|sourceInfo
operator|=
literal|"Default \"false\""
expr_stmt|;
if|if
condition|(
name|useFallbackRepositoriesProperty
operator|!=
literal|null
condition|)
block|{
name|sourceInfo
operator|=
name|String
operator|.
name|format
argument_list|(
name|PATTERN_PID_PROPERTY
argument_list|,
name|PID
argument_list|,
name|prefix
operator|+
name|PROPERTY_USE_FALLBACK_REPOSITORIES
argument_list|)
expr_stmt|;
block|}
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_USE_FALLBACK_REPOSITORIES
else|:
literal|"Use fallback repository"
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|sourceInfo
argument_list|,
name|useFallbackRepositories
argument_list|)
argument_list|,
literal|"Whether Maven Central is used as implicit, additional remote repository"
argument_list|)
expr_stmt|;
comment|// see org.ops4j.pax.url.mvn.internal.config.MavenConfigurationImpl.enableProxy()
comment|// "proxySupport" and "proxies" are not used in "new" MavenResolver
comment|// see org.eclipse.aether.internal.impl.DefaultOfflineController#checkOffline()
name|String
name|offlineProperty
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|PROPERTY_OFFLINE
argument_list|)
decl_stmt|;
name|boolean
name|offline
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|offlineProperty
argument_list|)
decl_stmt|;
name|sourceInfo
operator|=
literal|"Default \"false\""
expr_stmt|;
if|if
condition|(
name|offlineProperty
operator|!=
literal|null
condition|)
block|{
name|sourceInfo
operator|=
name|String
operator|.
name|format
argument_list|(
name|PATTERN_PID_PROPERTY
argument_list|,
name|PID
argument_list|,
name|prefix
operator|+
name|PROPERTY_OFFLINE
argument_list|)
expr_stmt|;
block|}
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_OFFLINE
else|:
literal|"Offline mode"
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|sourceInfo
argument_list|,
name|offline
argument_list|)
argument_list|,
literal|"Disables access to external remote repositories (file:// based ones are still used)"
argument_list|)
expr_stmt|;
comment|// see org.ops4j.pax.url.mvn.internal.HttpClients.createConnManager()
name|String
name|certificateCheckProperty
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|PROPERTY_CERTIFICATE_CHECK
argument_list|)
decl_stmt|;
name|boolean
name|certificateCheck
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|certificateCheckProperty
argument_list|)
decl_stmt|;
name|sourceInfo
operator|=
literal|"Default \"false\""
expr_stmt|;
if|if
condition|(
name|certificateCheckProperty
operator|!=
literal|null
condition|)
block|{
name|sourceInfo
operator|=
name|String
operator|.
name|format
argument_list|(
name|PATTERN_PID_PROPERTY
argument_list|,
name|PID
argument_list|,
name|prefix
operator|+
name|PROPERTY_CERTIFICATE_CHECK
argument_list|)
expr_stmt|;
block|}
name|addRow
argument_list|(
name|table
argument_list|,
name|propertyIds
condition|?
name|PROPERTY_CERTIFICATE_CHECK
else|:
literal|"SSL/TLS certificate check"
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|sourceInfo
argument_list|,
name|certificateCheck
argument_list|)
argument_list|,
literal|"Turns on server certificate validation for HTTPS remote repositories"
argument_list|)
expr_stmt|;
comment|// repositories (short list)
name|MavenRepositoryURL
index|[]
name|remoteRepositories
init|=
name|repositories
argument_list|(
name|config
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|MavenRepositoryURL
name|url
range|:
name|remoteRepositories
control|)
block|{
name|addRow
argument_list|(
name|table
argument_list|,
name|first
condition|?
operator|(
name|propertyIds
condition|?
name|PROPERTY_REPOSITORIES
else|:
literal|"Remote repositories"
operator|)
else|:
literal|""
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|url
operator|.
name|getFrom
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|,
name|url
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|first
condition|?
literal|"Remote repositories where artifacts are being resolved if not found locally"
else|:
literal|""
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
comment|// default repositories (short list)
name|MavenRepositoryURL
index|[]
name|defaultRepositories
init|=
name|repositories
argument_list|(
name|config
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|first
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|MavenRepositoryURL
name|url
range|:
name|defaultRepositories
control|)
block|{
name|addRow
argument_list|(
name|table
argument_list|,
name|first
condition|?
operator|(
name|propertyIds
condition|?
name|PROPERTY_DEFAULT_REPOSITORIES
else|:
literal|"Default repositories"
operator|)
else|:
literal|""
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|url
operator|.
name|getFrom
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|,
name|url
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|first
condition|?
literal|"Repositories where artifacts are looked up before trying remote resolution"
else|:
literal|""
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
comment|// proxies (short list)
if|if
condition|(
name|mavenSettings
operator|!=
literal|null
operator|&&
name|mavenSettings
operator|.
name|getProxies
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|first
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|Proxy
name|proxy
range|:
name|mavenSettings
operator|.
name|getProxies
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s:%s"
argument_list|,
name|proxy
operator|.
name|getHost
argument_list|()
argument_list|,
name|proxy
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|addRow
argument_list|(
name|table
argument_list|,
name|first
condition|?
operator|(
name|propertyIds
condition|?
literal|"<proxies>"
else|:
literal|"HTTP proxies"
operator|)
else|:
literal|""
argument_list|,
operator|new
name|SourceAnd
argument_list|<>
argument_list|(
name|MavenRepositoryURL
operator|.
name|FROM
operator|.
name|SETTINGS
operator|.
name|getSource
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|,
name|first
condition|?
literal|"Maven HTTP proxies"
else|:
literal|""
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
comment|/**      * Helper to add row to {@link ShellTable}      * @param table      * @param label      * @param value      * @param descriptionText      */
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|addRow
parameter_list|(
name|ShellTable
name|table
parameter_list|,
name|String
name|label
parameter_list|,
name|SourceAnd
argument_list|<
name|T
argument_list|>
name|value
parameter_list|,
name|String
name|descriptionText
parameter_list|)
block|{
name|Row
name|row
init|=
name|table
operator|.
name|addRow
argument_list|()
decl_stmt|;
name|row
operator|.
name|addContent
argument_list|(
name|label
argument_list|,
name|value
operator|.
name|val
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
condition|)
block|{
name|row
operator|.
name|addContent
argument_list|(
name|value
operator|.
name|source
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|description
condition|)
block|{
name|row
operator|.
name|addContent
argument_list|(
name|descriptionText
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

