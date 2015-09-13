begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|tooling
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
name|profile
operator|.
name|assembly
operator|.
name|Builder
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
name|tooling
operator|.
name|utils
operator|.
name|IoUtils
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
name|tooling
operator|.
name|utils
operator|.
name|MavenUtil
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
name|tooling
operator|.
name|utils
operator|.
name|MojoSupport
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
name|tools
operator|.
name|utils
operator|.
name|KarafPropertiesEditor
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
name|tools
operator|.
name|utils
operator|.
name|model
operator|.
name|KarafPropertyEdits
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
name|tools
operator|.
name|utils
operator|.
name|model
operator|.
name|io
operator|.
name|stax
operator|.
name|KarafPropertyInstructionsModelStaxReader
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
name|artifact
operator|.
name|Artifact
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
name|plugin
operator|.
name|MojoExecutionException
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
name|plugin
operator|.
name|MojoFailureException
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
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
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
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
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
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
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
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|PosixFilePermissions
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * Creates a customized Karaf distribution by installing features and setting up  * configuration files. The plugin gets features from feature.xml files and KAR  * archives declared as dependencies or as files configured with the  * featureRespositories parameter. It picks up other files, such as config files,  * from ${project.build.directory}/classes. Thus, a file in src/main/resources/etc  * will be copied by the resource plugin to ${project.build.directory}/classes/etc,  * and then added to the assembly by this goal.  *<br>  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"assembly"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PROCESS_RESOURCES
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|)
specifier|public
class|class
name|AssemblyMojo
extends|extends
name|MojoSupport
block|{
comment|/**      * Base directory used to copy the resources during the build (working directory).      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/assembly"
argument_list|)
specifier|protected
name|File
name|workDirectory
decl_stmt|;
comment|/**      * Features configuration file (etc/org.apache.karaf.features.cfg).      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/assembly/etc/org.apache.karaf.features.cfg"
argument_list|)
specifier|protected
name|File
name|featuresCfgFile
decl_stmt|;
comment|/**      * startup.properties file.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/assembly/etc/startup.properties"
argument_list|)
specifier|protected
name|File
name|startupPropertiesFile
decl_stmt|;
comment|/**      * Directory used during build to construction the Karaf system repository.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/assembly/system"
argument_list|)
specifier|protected
name|File
name|systemDirectory
decl_stmt|;
comment|/**      * default start level for bundles in features that don't specify it.      */
annotation|@
name|Parameter
specifier|protected
name|int
name|defaultStartLevel
init|=
literal|30
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|startupRepositories
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bootRepositories
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|installedRepositories
decl_stmt|;
comment|/**      * List of features from runtime-scope features xml and kars to be installed into system and listed in startup.properties.      */
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|startupFeatures
decl_stmt|;
comment|/**      * List of features from runtime-scope features xml and kars to be installed into system repo and listed in features service boot features.      */
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bootFeatures
decl_stmt|;
comment|/**      * List of features from runtime-scope features xml and kars to be installed into system repo and not mentioned elsewhere.      */
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|installedFeatures
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blacklistedFeatures
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|startupBundles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bootBundles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|installedBundles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blacklistedBundles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|String
name|profilesUri
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|bootProfiles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|startupProfiles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|installedProfiles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|blacklistedProfiles
decl_stmt|;
annotation|@
name|Parameter
specifier|private
name|Builder
operator|.
name|BlacklistPolicy
name|blacklistPolicy
decl_stmt|;
comment|/**      * Ignore the dependency attribute (dependency="[true|false]") on bundle      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|protected
name|boolean
name|ignoreDependencyFlag
decl_stmt|;
comment|/**      * Additional feature repositories      */
annotation|@
name|Parameter
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|featureRepositories
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|libraries
decl_stmt|;
comment|/**      * Use reference: style urls in startup.properties      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|protected
name|boolean
name|useReferenceUrls
decl_stmt|;
comment|/**      * Include project build output directory in the assembly      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|protected
name|boolean
name|includeBuildOutputDirectory
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|boolean
name|installAllFeaturesByDefault
init|=
literal|true
decl_stmt|;
annotation|@
name|Parameter
specifier|protected
name|Builder
operator|.
name|KarafVersion
name|karafVersion
init|=
name|Builder
operator|.
name|KarafVersion
operator|.
name|v4x
decl_stmt|;
comment|/**      * Specify the version of Java SE to be assumed for osgi.ee.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"1.7"
argument_list|)
specifier|protected
name|String
name|javase
decl_stmt|;
comment|/**      * Specify an XML file that instructs this goal to apply edits to      * one or more standard Karaf property files.      * The contents of this file are documented in detail on      *<a href="karaf-property-instructions-model.html">this page</a>.      * This allows you to      * customize these files without making copies in your resources      * directories. Here's a simple example:      *<pre>      * {@literal<property-edits xmlns="http://karaf.apache.org/tools/property-edits/1.0.0"><edits><edit><file>config.properties</file><operation>put</operation><key>karaf.framework</key><value>equinox</value></edit><edit><file>config.properties</file><operation>extend</operation><key>org.osgi.framework.system.capabilities</key><value>my-magic-capability</value></edit><edit><file>config.properties</file><operation prepend='true'>extend</operation><key>some-other-list</key><value>my-value-goes-first</value></edit></edits></property-edits></pre>     }      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.basedir}/src/main/karaf/assembly-property-edits.xml"
argument_list|)
specifier|protected
name|String
name|propertyFileEdits
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
try|try
block|{
name|doExecute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
decl||
name|MojoFailureException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Unable to build assembly"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|startupRepositories
operator|=
name|nonNullList
argument_list|(
name|startupRepositories
argument_list|)
expr_stmt|;
name|bootRepositories
operator|=
name|nonNullList
argument_list|(
name|bootRepositories
argument_list|)
expr_stmt|;
name|installedRepositories
operator|=
name|nonNullList
argument_list|(
name|installedRepositories
argument_list|)
expr_stmt|;
name|startupBundles
operator|=
name|nonNullList
argument_list|(
name|startupBundles
argument_list|)
expr_stmt|;
name|bootBundles
operator|=
name|nonNullList
argument_list|(
name|bootBundles
argument_list|)
expr_stmt|;
name|installedBundles
operator|=
name|nonNullList
argument_list|(
name|installedBundles
argument_list|)
expr_stmt|;
name|blacklistedBundles
operator|=
name|nonNullList
argument_list|(
name|blacklistedBundles
argument_list|)
expr_stmt|;
name|startupFeatures
operator|=
name|nonNullList
argument_list|(
name|startupFeatures
argument_list|)
expr_stmt|;
name|bootFeatures
operator|=
name|nonNullList
argument_list|(
name|bootFeatures
argument_list|)
expr_stmt|;
name|installedFeatures
operator|=
name|nonNullList
argument_list|(
name|installedFeatures
argument_list|)
expr_stmt|;
name|blacklistedFeatures
operator|=
name|nonNullList
argument_list|(
name|blacklistedFeatures
argument_list|)
expr_stmt|;
name|startupProfiles
operator|=
name|nonNullList
argument_list|(
name|startupProfiles
argument_list|)
expr_stmt|;
name|bootProfiles
operator|=
name|nonNullList
argument_list|(
name|bootProfiles
argument_list|)
expr_stmt|;
name|installedProfiles
operator|=
name|nonNullList
argument_list|(
name|installedProfiles
argument_list|)
expr_stmt|;
name|blacklistedProfiles
operator|=
name|nonNullList
argument_list|(
name|blacklistedProfiles
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|startupProfiles
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|bootProfiles
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|installedProfiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|profilesUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"profilesDirectory must be specified"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|featureRepositories
operator|!=
literal|null
operator|&&
operator|!
name|featureRepositories
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Use of featureRepositories is deprecated, use startupRepositories, bootRepositories or installedRepositories instead"
argument_list|)
expr_stmt|;
name|startupRepositories
operator|.
name|addAll
argument_list|(
name|featureRepositories
argument_list|)
expr_stmt|;
name|bootRepositories
operator|.
name|addAll
argument_list|(
name|featureRepositories
argument_list|)
expr_stmt|;
name|installedRepositories
operator|.
name|addAll
argument_list|(
name|featureRepositories
argument_list|)
expr_stmt|;
block|}
name|Builder
name|builder
init|=
name|Builder
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|builder
operator|.
name|offline
argument_list|(
name|mavenSession
operator|.
name|isOffline
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|javase
argument_list|(
name|javase
argument_list|)
expr_stmt|;
comment|// Set up blacklisted items
name|builder
operator|.
name|blacklistBundles
argument_list|(
name|blacklistedBundles
argument_list|)
expr_stmt|;
name|builder
operator|.
name|blacklistFeatures
argument_list|(
name|blacklistedFeatures
argument_list|)
expr_stmt|;
name|builder
operator|.
name|blacklistProfiles
argument_list|(
name|blacklistedProfiles
argument_list|)
expr_stmt|;
name|builder
operator|.
name|blacklistPolicy
argument_list|(
name|blacklistPolicy
argument_list|)
expr_stmt|;
if|if
condition|(
name|propertyFileEdits
operator|!=
literal|null
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|propertyFileEdits
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|KarafPropertyEdits
name|edits
decl_stmt|;
try|try
init|(
name|InputStream
name|editsStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|propertyFileEdits
argument_list|)
init|)
block|{
name|KarafPropertyInstructionsModelStaxReader
name|kipmsr
init|=
operator|new
name|KarafPropertyInstructionsModelStaxReader
argument_list|()
decl_stmt|;
name|edits
operator|=
name|kipmsr
operator|.
name|read
argument_list|(
name|editsStream
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|propertyEdits
argument_list|(
name|edits
argument_list|)
expr_stmt|;
block|}
block|}
comment|// creating system directory
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Creating work directory"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|homeDirectory
argument_list|(
name|workDirectory
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|IoUtils
operator|.
name|deleteRecursive
argument_list|(
name|workDirectory
argument_list|)
expr_stmt|;
name|workDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|startupKars
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|bootKars
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|installedKars
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Loading kars and features repositories
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Loading kar and features repositories dependencies"
argument_list|)
expr_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|project
operator|.
name|getDependencyArtifacts
argument_list|()
control|)
block|{
name|Builder
operator|.
name|Stage
name|stage
decl_stmt|;
switch|switch
condition|(
name|artifact
operator|.
name|getScope
argument_list|()
condition|)
block|{
case|case
literal|"compile"
case|:
name|stage
operator|=
name|Builder
operator|.
name|Stage
operator|.
name|Startup
expr_stmt|;
break|break;
case|case
literal|"runtime"
case|:
name|stage
operator|=
name|Builder
operator|.
name|Stage
operator|.
name|Boot
expr_stmt|;
break|break;
case|case
literal|"provided"
case|:
name|stage
operator|=
name|Builder
operator|.
name|Stage
operator|.
name|Installed
expr_stmt|;
break|break;
default|default:
continue|continue;
block|}
if|if
condition|(
literal|"kar"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|uri
init|=
name|artifactToMvn
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|stage
condition|)
block|{
case|case
name|Startup
case|:
name|startupKars
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|Boot
case|:
name|bootKars
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|Installed
case|:
name|installedKars
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
elseif|else
if|if
condition|(
literal|"features"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|uri
init|=
name|artifactToMvn
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|stage
condition|)
block|{
case|case
name|Startup
case|:
name|startupRepositories
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|Boot
case|:
name|bootRepositories
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|Installed
case|:
name|installedRepositories
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
elseif|else
if|if
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|||
literal|"bundle"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|uri
init|=
name|artifactToMvn
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|stage
condition|)
block|{
case|case
name|Startup
case|:
name|startupBundles
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|Boot
case|:
name|bootBundles
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|Installed
case|:
name|installedBundles
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
name|builder
operator|.
name|karafVersion
argument_list|(
name|karafVersion
argument_list|)
operator|.
name|useReferenceUrls
argument_list|(
name|useReferenceUrls
argument_list|)
operator|.
name|defaultAddAll
argument_list|(
name|installAllFeaturesByDefault
argument_list|)
operator|.
name|ignoreDependencyFlag
argument_list|(
name|ignoreDependencyFlag
argument_list|)
expr_stmt|;
if|if
condition|(
name|profilesUri
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|profilesUris
argument_list|(
name|profilesUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|libraries
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|libraries
argument_list|(
name|libraries
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|libraries
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Startup
name|builder
operator|.
name|defaultStage
argument_list|(
name|Builder
operator|.
name|Stage
operator|.
name|Startup
argument_list|)
operator|.
name|kars
argument_list|(
name|toArray
argument_list|(
name|startupKars
argument_list|)
argument_list|)
operator|.
name|repositories
argument_list|(
name|startupFeatures
operator|.
name|isEmpty
argument_list|()
operator|&&
name|startupProfiles
operator|.
name|isEmpty
argument_list|()
operator|&&
name|installAllFeaturesByDefault
argument_list|,
name|toArray
argument_list|(
name|startupRepositories
argument_list|)
argument_list|)
operator|.
name|features
argument_list|(
name|toArray
argument_list|(
name|startupFeatures
argument_list|)
argument_list|)
operator|.
name|bundles
argument_list|(
name|toArray
argument_list|(
name|startupBundles
argument_list|)
argument_list|)
operator|.
name|profiles
argument_list|(
name|toArray
argument_list|(
name|startupProfiles
argument_list|)
argument_list|)
expr_stmt|;
comment|// Boot
name|builder
operator|.
name|defaultStage
argument_list|(
name|Builder
operator|.
name|Stage
operator|.
name|Boot
argument_list|)
operator|.
name|kars
argument_list|(
name|toArray
argument_list|(
name|bootKars
argument_list|)
argument_list|)
operator|.
name|repositories
argument_list|(
name|bootFeatures
operator|.
name|isEmpty
argument_list|()
operator|&&
name|bootProfiles
operator|.
name|isEmpty
argument_list|()
operator|&&
name|installAllFeaturesByDefault
argument_list|,
name|toArray
argument_list|(
name|bootRepositories
argument_list|)
argument_list|)
operator|.
name|features
argument_list|(
name|toArray
argument_list|(
name|bootFeatures
argument_list|)
argument_list|)
operator|.
name|bundles
argument_list|(
name|toArray
argument_list|(
name|bootBundles
argument_list|)
argument_list|)
operator|.
name|profiles
argument_list|(
name|toArray
argument_list|(
name|bootProfiles
argument_list|)
argument_list|)
expr_stmt|;
comment|// Installed
name|builder
operator|.
name|defaultStage
argument_list|(
name|Builder
operator|.
name|Stage
operator|.
name|Installed
argument_list|)
operator|.
name|kars
argument_list|(
name|toArray
argument_list|(
name|installedKars
argument_list|)
argument_list|)
operator|.
name|repositories
argument_list|(
name|installedFeatures
operator|.
name|isEmpty
argument_list|()
operator|&&
name|installedProfiles
operator|.
name|isEmpty
argument_list|()
operator|&&
name|installAllFeaturesByDefault
argument_list|,
name|toArray
argument_list|(
name|installedRepositories
argument_list|)
argument_list|)
operator|.
name|features
argument_list|(
name|toArray
argument_list|(
name|installedFeatures
argument_list|)
argument_list|)
operator|.
name|bundles
argument_list|(
name|toArray
argument_list|(
name|installedBundles
argument_list|)
argument_list|)
operator|.
name|profiles
argument_list|(
name|toArray
argument_list|(
name|installedProfiles
argument_list|)
argument_list|)
expr_stmt|;
comment|// Generate the assembly
name|builder
operator|.
name|generateAssembly
argument_list|()
expr_stmt|;
comment|// Include project classes content
if|if
condition|(
name|includeBuildOutputDirectory
condition|)
name|IoUtils
operator|.
name|copyDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|)
argument_list|,
name|workDirectory
argument_list|)
expr_stmt|;
comment|// Chmod the bin/* scripts
name|File
index|[]
name|files
init|=
operator|new
name|File
argument_list|(
name|workDirectory
argument_list|,
literal|"bin"
argument_list|)
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".bat"
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|setPosixFilePermissions
argument_list|(
name|file
operator|.
name|toPath
argument_list|()
argument_list|,
name|PosixFilePermissions
operator|.
name|fromString
argument_list|(
literal|"rwxr-xr-x"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ignore
parameter_list|)
block|{
comment|// we tried our best, perhaps the OS does not support posix file perms.
block|}
block|}
block|}
block|}
block|}
specifier|private
name|String
name|artifactToMvn
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|String
name|uri
decl_stmt|;
name|String
name|groupId
init|=
name|artifact
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
name|String
name|artifactId
init|=
name|artifact
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|artifact
operator|.
name|getBaseVersion
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|artifact
operator|.
name|getArtifactHandler
argument_list|()
operator|.
name|getExtension
argument_list|()
decl_stmt|;
name|String
name|classifier
init|=
name|artifact
operator|.
name|getClassifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|MavenUtil
operator|.
name|isEmpty
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|uri
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s"
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|uri
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s/%s"
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|uri
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s/%s/%s"
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
block|}
specifier|private
name|String
index|[]
name|toArray
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|strings
parameter_list|)
block|{
return|return
name|strings
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|strings
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|nonNullList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|list
operator|==
literal|null
condition|?
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
else|:
name|list
return|;
block|}
block|}
end_class

end_unit

