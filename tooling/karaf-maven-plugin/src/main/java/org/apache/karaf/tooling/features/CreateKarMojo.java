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
operator|.
name|features
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
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
name|Date
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|deployer
operator|.
name|kar
operator|.
name|KarArtifactInstaller
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
name|ConfigFileInfo
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
name|model
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
name|internal
operator|.
name|model
operator|.
name|Features
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
name|model
operator|.
name|JaxbUtil
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
name|maven
operator|.
name|archiver
operator|.
name|MavenArchiveConfiguration
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
name|archiver
operator|.
name|MavenArchiver
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|repository
operator|.
name|layout
operator|.
name|DefaultRepositoryLayout
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
name|repository
operator|.
name|metadata
operator|.
name|Metadata
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
name|repository
operator|.
name|metadata
operator|.
name|Snapshot
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
name|repository
operator|.
name|metadata
operator|.
name|SnapshotVersion
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
name|repository
operator|.
name|metadata
operator|.
name|Versioning
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
name|repository
operator|.
name|metadata
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MetadataXpp3Writer
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
name|resolver
operator|.
name|ArtifactNotFoundException
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
name|resolver
operator|.
name|ArtifactResolutionException
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
name|Component
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|archiver
operator|.
name|Archiver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|archiver
operator|.
name|jar
operator|.
name|JarArchiver
import|;
end_import

begin_comment
comment|/**  * Assemble a kar archive from a features.xml file  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"features-create-kar"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PACKAGE
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|RUNTIME
argument_list|)
specifier|public
class|class
name|CreateKarMojo
extends|extends
name|MojoSupport
block|{
comment|/**      * The maven archive configuration to use.      *<p/>      * See<a href="http://maven.apache.org/ref/current/maven-archiver/apidocs/org/apache/maven/archiver/MavenArchiveConfiguration.html">the Javadocs for MavenArchiveConfiguration</a>.      */
annotation|@
name|Parameter
specifier|private
name|MavenArchiveConfiguration
name|archive
init|=
operator|new
name|MavenArchiveConfiguration
argument_list|()
decl_stmt|;
comment|/**      * The Jar archiver.      */
annotation|@
name|Component
argument_list|(
name|role
operator|=
name|Archiver
operator|.
name|class
argument_list|,
name|hint
operator|=
literal|"jar"
argument_list|)
specifier|private
name|JarArchiver
name|jarArchiver
init|=
literal|null
decl_stmt|;
comment|/**      * Directory containing the generated archive.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}"
argument_list|)
specifier|private
name|File
name|outputDirectory
init|=
literal|null
decl_stmt|;
comment|/**      * Name of the generated archive.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.finalName}"
argument_list|)
specifier|private
name|String
name|finalName
init|=
literal|null
decl_stmt|;
comment|/**      * Ignore the dependency flag on the bundles in the features XML      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
name|ignoreDependencyFlag
decl_stmt|;
comment|/**      * Classifier to add to the artifact generated. If given, the artifact will be attached.      * If it's not given, it will merely be written to the output directory according to the finalName.      */
annotation|@
name|Parameter
specifier|protected
name|String
name|classifier
decl_stmt|;
comment|/**      * Location of resources directory for additional content to include in the kar.      * Note that it includes everything under classes so as to include maven-remote-resources      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/classes"
argument_list|)
specifier|private
name|File
name|resourcesDir
decl_stmt|;
comment|/**      * The features file to use as instructions      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/feature/feature.xml"
argument_list|)
specifier|private
name|String
name|featuresFile
decl_stmt|;
comment|/**      * The wrapper repository in the kar.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${repositoryPath}"
argument_list|)
specifier|private
name|String
name|repositoryPath
init|=
literal|"repository/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|mvnPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"mvn:([^/ ]+)/([^/ ]+)/([^/ ]*)(/([^/ ]+)(/([^/ ]+))?)?"
argument_list|)
decl_stmt|;
comment|//
comment|// Mojo
comment|//
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|File
name|featuresFileResolved
init|=
name|resolveFile
argument_list|(
name|featuresFile
argument_list|)
decl_stmt|;
name|String
name|groupId
init|=
name|project
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
name|String
name|artifactId
init|=
name|project
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|project
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|isMavenUrl
argument_list|(
name|featuresFile
argument_list|)
condition|)
block|{
name|Artifact
name|artifactTemp
init|=
name|resourceToArtifact
argument_list|(
name|featuresFile
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifactTemp
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
condition|)
name|groupId
operator|=
name|artifactTemp
operator|.
name|getGroupId
argument_list|()
expr_stmt|;
if|if
condition|(
name|artifactTemp
operator|.
name|getArtifactHandler
argument_list|()
operator|!=
literal|null
condition|)
name|artifactId
operator|=
name|artifactTemp
operator|.
name|getArtifactId
argument_list|()
expr_stmt|;
if|if
condition|(
name|artifactTemp
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
name|version
operator|=
name|artifactTemp
operator|.
name|getVersion
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|Artifact
argument_list|>
name|resources
init|=
name|readResources
argument_list|(
name|featuresFileResolved
argument_list|)
decl_stmt|;
comment|// Build the archive
name|File
name|archive
init|=
name|createArchive
argument_list|(
name|resources
argument_list|,
name|featuresFileResolved
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
comment|// if no classifier is specified and packaging is not kar, display a warning
comment|// and attach artifact
if|if
condition|(
name|classifier
operator|==
literal|null
operator|&&
operator|!
name|this
operator|.
name|getProject
argument_list|()
operator|.
name|getPackaging
argument_list|()
operator|.
name|equals
argument_list|(
literal|"kar"
argument_list|)
condition|)
block|{
name|this
operator|.
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Your project should use the \"kar\" packaging or configure a \"classifier\" for kar attachment"
argument_list|)
expr_stmt|;
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|getProject
argument_list|()
argument_list|,
literal|"kar"
argument_list|,
literal|null
argument_list|,
name|archive
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Attach the generated archive for install/deploy
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|getProject
argument_list|()
argument_list|,
literal|"kar"
argument_list|,
name|classifier
argument_list|,
name|archive
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getProject
argument_list|()
operator|.
name|getArtifact
argument_list|()
operator|.
name|setFile
argument_list|(
name|archive
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|File
name|resolveFile
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|File
name|fileResolved
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isMavenUrl
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|fileResolved
operator|=
operator|new
name|File
argument_list|(
name|fromMaven
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Artifact
name|artifactTemp
init|=
name|resourceToArtifact
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fileResolved
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|artifactResolver
operator|.
name|resolve
argument_list|(
name|artifactTemp
argument_list|,
name|remoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
name|fileResolved
operator|=
name|artifactTemp
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Artifact was not resolved"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactNotFoundException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
literal|"Artifact was not found"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|fileResolved
operator|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|fileResolved
return|;
block|}
comment|/**      * Read bundles and configuration files in the features file.      *      * @return      * @throws MojoExecutionException      */
specifier|private
name|List
argument_list|<
name|Artifact
argument_list|>
name|readResources
parameter_list|(
name|File
name|featuresFile
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|Features
name|features
init|=
name|JaxbUtil
operator|.
name|unmarshal
argument_list|(
name|featuresFile
operator|.
name|toURI
argument_list|()
operator|.
name|toASCIIString
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|features
operator|.
name|getFeature
argument_list|()
control|)
block|{
for|for
control|(
name|BundleInfo
name|bundle
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|ignoreDependencyFlag
operator|||
operator|(
operator|!
name|ignoreDependencyFlag
operator|&&
operator|!
name|bundle
operator|.
name|isDependency
argument_list|()
operator|)
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|resourceToArtifact
argument_list|(
name|bundle
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ConfigFileInfo
name|configFile
range|:
name|feature
operator|.
name|getConfigurationFiles
argument_list|()
control|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|resourceToArtifact
argument_list|(
name|configFile
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resources
return|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
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
literal|"Could not interpret features.xml"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Generates the configuration archive.      *      * @param bundles      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
name|File
name|createArchive
parameter_list|(
name|List
argument_list|<
name|Artifact
argument_list|>
name|bundles
parameter_list|,
name|File
name|featuresFile
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|ArtifactRepositoryLayout
name|layout
init|=
operator|new
name|DefaultRepositoryLayout
argument_list|()
decl_stmt|;
name|File
name|archiveFile
init|=
name|getArchiveFile
argument_list|(
name|outputDirectory
argument_list|,
name|finalName
argument_list|,
name|classifier
argument_list|)
decl_stmt|;
name|MavenArchiver
name|archiver
init|=
operator|new
name|MavenArchiver
argument_list|()
decl_stmt|;
name|archiver
operator|.
name|setArchiver
argument_list|(
name|jarArchiver
argument_list|)
expr_stmt|;
name|archiver
operator|.
name|setOutputFile
argument_list|(
name|archiveFile
argument_list|)
expr_stmt|;
try|try
block|{
comment|//TODO should .kar be a bundle?
comment|//            archive.addManifestEntry(Constants.BUNDLE_NAME, project.getName());
comment|//            archive.addManifestEntry(Constants.BUNDLE_VENDOR, project.getOrganization().getName());
comment|//            ArtifactVersion version = project.getArtifact().getSelectedVersion();
comment|//            String versionString = "" + version.getMajorVersion() + "." + version.getMinorVersion() + "." + version.getIncrementalVersion();
comment|//            if (version.getQualifier() != null) {
comment|//                versionString += "." + version.getQualifier();
comment|//            }
comment|//            archive.addManifestEntry(Constants.BUNDLE_VERSION, versionString);
comment|//            archive.addManifestEntry(Constants.BUNDLE_MANIFESTVERSION, "2");
comment|//            archive.addManifestEntry(Constants.BUNDLE_DESCRIPTION, project.getDescription());
comment|//            // NB, no constant for this one
comment|//            archive.addManifestEntry("Bundle-License", ((License) project.getLicenses().get(0)).getUrl());
comment|//            archive.addManifestEntry(Constants.BUNDLE_DOCURL, project.getUrl());
comment|//            //TODO this might need some help
comment|//            archive.addManifestEntry(Constants.BUNDLE_SYMBOLICNAME, project.getArtifactId());
comment|//include the feature.xml
name|Artifact
name|featureArtifact
init|=
name|factory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"xml"
argument_list|,
name|KarArtifactInstaller
operator|.
name|FEATURE_CLASSIFIER
argument_list|)
decl_stmt|;
name|jarArchiver
operator|.
name|addFile
argument_list|(
name|featuresFile
argument_list|,
name|repositoryPath
operator|+
name|layout
operator|.
name|pathOf
argument_list|(
name|featureArtifact
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|featureArtifact
operator|.
name|isSnapshot
argument_list|()
condition|)
block|{
comment|// the artifact is a snapshot, create the maven-metadata-local.xml
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Feature artifact is a SNAPSHOT, handling the maven-metadata-local.xml"
argument_list|)
expr_stmt|;
name|File
name|metadataTarget
init|=
operator|new
name|File
argument_list|(
name|featuresFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
literal|"maven-metadata-local.xml"
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Looking for "
operator|+
name|metadataTarget
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|metadataTarget
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// the maven-metadata-local.xml doesn't exist, create it
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|metadataTarget
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" doesn't exist, create it"
argument_list|)
expr_stmt|;
name|Metadata
name|metadata
init|=
operator|new
name|Metadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setGroupId
argument_list|(
name|featureArtifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setArtifactId
argument_list|(
name|featureArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setVersion
argument_list|(
name|featureArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setModelVersion
argument_list|(
literal|"1.1.0"
argument_list|)
expr_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|setLastUpdatedTimestamp
argument_list|(
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Snapshot
name|snapshot
init|=
operator|new
name|Snapshot
argument_list|()
decl_stmt|;
name|snapshot
operator|.
name|setLocalCopy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|setSnapshot
argument_list|(
name|snapshot
argument_list|)
expr_stmt|;
name|SnapshotVersion
name|snapshotVersion
init|=
operator|new
name|SnapshotVersion
argument_list|()
decl_stmt|;
name|snapshotVersion
operator|.
name|setClassifier
argument_list|(
name|featureArtifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|snapshotVersion
operator|.
name|setVersion
argument_list|(
name|featureArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|snapshotVersion
operator|.
name|setExtension
argument_list|(
name|featureArtifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|snapshotVersion
operator|.
name|setUpdated
argument_list|(
name|versioning
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addSnapshotVersion
argument_list|(
name|snapshotVersion
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|setVersioning
argument_list|(
name|versioning
argument_list|)
expr_stmt|;
name|MetadataXpp3Writer
name|metadataWriter
init|=
operator|new
name|MetadataXpp3Writer
argument_list|()
decl_stmt|;
try|try
block|{
name|Writer
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|metadataTarget
argument_list|)
decl_stmt|;
name|metadataWriter
operator|.
name|write
argument_list|(
name|writer
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Could not create maven-metadata-local.xml"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"It means that this SNAPSHOT could be overwritten by an older one present on remote repositories"
argument_list|)
expr_stmt|;
block|}
block|}
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Adding file "
operator|+
name|metadataTarget
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" in the jar path "
operator|+
name|repositoryPath
operator|+
name|layout
operator|.
name|pathOf
argument_list|(
name|featureArtifact
argument_list|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|layout
operator|.
name|pathOf
argument_list|(
name|featureArtifact
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
operator|+
literal|"/maven-metadata-local.xml"
argument_list|)
expr_stmt|;
name|jarArchiver
operator|.
name|addFile
argument_list|(
name|metadataTarget
argument_list|,
name|repositoryPath
operator|+
name|layout
operator|.
name|pathOf
argument_list|(
name|featureArtifact
argument_list|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|layout
operator|.
name|pathOf
argument_list|(
name|featureArtifact
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
operator|+
literal|"/maven-metadata-local.xml"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Artifact
name|artifact
range|:
name|bundles
control|)
block|{
name|artifactResolver
operator|.
name|resolve
argument_list|(
name|artifact
argument_list|,
name|remoteRepos
argument_list|,
name|localRepo
argument_list|)
expr_stmt|;
name|File
name|localFile
init|=
name|artifact
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|isSnapshot
argument_list|()
condition|)
block|{
comment|// the artifact is a snapshot, create the maven-metadata-local.xml
name|File
name|metadataTarget
init|=
operator|new
name|File
argument_list|(
name|localFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
literal|"maven-metadata-local.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|metadataTarget
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// the maven-metadata-local.xml doesn't exist, create it
try|try
block|{
name|MavenUtil
operator|.
name|generateMavenMetadata
argument_list|(
name|artifact
argument_list|,
name|metadataTarget
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Could not create maven-metadata-local.xml"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"It means that this SNAPSHOT could be overwritten by an older one present on remote repositories"
argument_list|)
expr_stmt|;
block|}
block|}
name|jarArchiver
operator|.
name|addFile
argument_list|(
name|metadataTarget
argument_list|,
name|repositoryPath
operator|+
name|layout
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|layout
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
operator|+
literal|"/maven-metadata-local.xml"
argument_list|)
expr_stmt|;
block|}
comment|//TODO this may not be reasonable, but... resolved snapshot artifacts have timestamped versions
comment|//which do not work in startup.properties.
name|artifact
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|targetFileName
init|=
name|repositoryPath
operator|+
name|layout
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|jarArchiver
operator|.
name|addFile
argument_list|(
name|localFile
argument_list|,
name|targetFileName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resourcesDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|archiver
operator|.
name|getArchiver
argument_list|()
operator|.
name|addDirectory
argument_list|(
name|resourcesDir
argument_list|)
expr_stmt|;
block|}
name|archiver
operator|.
name|createArchive
argument_list|(
name|project
argument_list|,
name|archive
argument_list|)
expr_stmt|;
return|return
name|archiveFile
return|;
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
literal|"Failed to create archive"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|static
name|boolean
name|isMavenUrl
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|mvnPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|matches
argument_list|()
return|;
block|}
comment|/**      * Return a path for an artifact:      * - if the input is already a path (doesn't contain ':'), the same path is returned.      * - if the input is a Maven URL, the input is converted to a default repository location path, type and classifier      *   are optional.      *      * @param name artifact data      * @return path as supplied or a default Maven repository path      */
specifier|private
specifier|static
name|String
name|fromMaven
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|mvnPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|name
return|;
block|}
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|b
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|b
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'.'
condition|)
block|{
name|b
operator|.
name|setCharAt
argument_list|(
name|i
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
block|}
name|b
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|// groupId
name|String
name|artifactId
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|String
name|extension
init|=
name|m
operator|.
name|group
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|m
operator|.
name|group
argument_list|(
literal|7
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|// artifactId
name|b
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
comment|// version
name|b
operator|.
name|append
argument_list|(
name|artifactId
argument_list|)
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|present
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|present
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|extension
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
literal|".jar"
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|boolean
name|present
parameter_list|(
name|String
name|part
parameter_list|)
block|{
return|return
name|part
operator|!=
literal|null
operator|&&
operator|!
name|part
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|protected
specifier|static
name|File
name|getArchiveFile
parameter_list|(
specifier|final
name|File
name|basedir
parameter_list|,
specifier|final
name|String
name|finalName
parameter_list|,
name|String
name|classifier
parameter_list|)
block|{
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
name|classifier
operator|=
literal|""
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|classifier
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
operator|!
name|classifier
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|classifier
operator|=
literal|"-"
operator|+
name|classifier
expr_stmt|;
block|}
return|return
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
name|finalName
operator|+
name|classifier
operator|+
literal|".kar"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

