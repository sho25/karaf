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
name|Bundle
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
name|FeaturesRoot
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
name|model
operator|.
name|Resource
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
comment|/**  * assembles a kar archive from a features.xml file  *  * @version $Revision: 1.1 $  * @goal archive-kar  * @phase compile  * @execute phase="package"  * @requiresDependencyResolution runtime  * @inheritByDefault true  * @description Assemble a kar archive from a features.xml file  */
end_comment

begin_class
specifier|public
class|class
name|ArchiveKarMojo
extends|extends
name|MojoSupport
block|{
comment|/**      * The maven archive configuration to use.      *<p/>      * See<a href="http://maven.apache.org/ref/current/maven-archiver/apidocs/org/apache/maven/archiver/MavenArchiveConfiguration.html">the Javadocs for MavenArchiveConfiguration</a>.      *      * @parameter      */
specifier|private
name|MavenArchiveConfiguration
name|archive
init|=
operator|new
name|MavenArchiveConfiguration
argument_list|()
decl_stmt|;
comment|/**      * The Jar archiver.      *      * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="jar"      * @required      * @readonly      */
specifier|private
name|JarArchiver
name|jarArchiver
init|=
literal|null
decl_stmt|;
comment|/**      * The module base directory.      *      * @parameter expression="${project.basedir}"      * @required      * @readonly      */
specifier|private
name|File
name|baseDirectory
init|=
literal|null
decl_stmt|;
comment|/**      * Directory containing the generated archive.      *      * @parameter expression="${project.build.directory}"      * @required      */
specifier|private
name|File
name|outputDirectory
init|=
literal|null
decl_stmt|;
comment|/**      * Name of the generated archive.      *      * @parameter expression="${project.build.finalName}"      * @required      */
specifier|private
name|String
name|finalName
init|=
literal|null
decl_stmt|;
comment|/**      * The Geronimo repository where modules will be packaged up from.      *      * @parameter expression="${project.build.directory}/repository"      * @required      */
specifier|private
name|File
name|targetRepository
init|=
literal|null
decl_stmt|;
comment|/**      * Location of resources directory for additional content to include in the car.      *      * @parameter expression="${project.build.directory}/classes/resources"      */
specifier|private
name|File
name|resourcesDir
decl_stmt|;
comment|/**      * The features file to use as instructions      *      * @parameter default-value="${project.build.directory}/feature/feature.xml"      */
specifier|private
name|File
name|featuresFile
decl_stmt|;
comment|/**      * The internal repository in the kar.      *      * @parameter default-value="${repositoryPath}"      */
specifier|private
name|String
name|repositoryPath
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
name|List
argument_list|<
name|Artifact
argument_list|>
name|bundles
init|=
name|readBundles
argument_list|()
decl_stmt|;
comment|// Build the archive
name|File
name|archive
init|=
name|createArchive
argument_list|(
name|bundles
argument_list|)
decl_stmt|;
comment|// Attach the generated archive for install/deploy
name|project
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
specifier|private
name|List
argument_list|<
name|Artifact
argument_list|>
name|readBundles
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|bundles
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
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|featuresFile
argument_list|)
decl_stmt|;
try|try
block|{
name|FeaturesRoot
name|features
init|=
name|JaxbUtil
operator|.
name|unmarshal
argument_list|(
name|FeaturesRoot
operator|.
name|class
argument_list|,
name|in
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
name|Bundle
name|bundle
range|:
name|feature
operator|.
name|getBundle
argument_list|()
control|)
block|{
if|if
condition|(
name|bundle
operator|.
name|isDependency
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|bundle
operator|.
name|isDependency
argument_list|()
condition|)
block|{
name|bundles
operator|.
name|add
argument_list|(
name|bundleToArtifact
argument_list|(
name|bundle
operator|.
name|getValue
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|bundles
return|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * Generates the configuration archive.      * @param bundles      */
specifier|private
name|File
name|createArchive
parameter_list|(
name|List
argument_list|<
name|Artifact
argument_list|>
name|bundles
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
literal|null
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
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|project
operator|.
name|getVersion
argument_list|()
argument_list|,
literal|"xml"
argument_list|,
literal|"feature"
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
for|for
control|(
name|Artifact
name|artifact
range|:
name|bundles
control|)
block|{
name|resolver
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
comment|//            // Include the generated artifact contents
comment|//            File artifactDirectory = this.getArtifactInRepositoryDir();
comment|//
comment|//            if (artifactDirectory.exists()) {
comment|//                archiver.addArchivedFileSet(artifactDirectory);
comment|//            }
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
argument_list|,
literal|"resources/"
argument_list|)
expr_stmt|;
block|}
comment|//
comment|//            for (Resource resource: (List<Resource>)project.getResources()) {
comment|//                File resourceDir = new File(resource.getDirectory());
comment|//                if (resourceDir.exists()) {
comment|//                    jarArchiver.addDirectory(resourceDir, resource.getTargetPath());
comment|//                }
comment|//            }
comment|//
comment|// HACK: Include legal files here for sanity
comment|//
comment|//
comment|// NOTE: Would be nice to share this with the copy-legal-files mojo
comment|//
comment|//            String[] includes = {
comment|//                    "LICENSE.txt",
comment|//                    "LICENSE",
comment|//
comment|//                    "NOTICE.txt",
comment|//                    "NOTICE",
comment|//                    "DISCLAIMER.txt",
comment|//                    "DISCLAIMER"
comment|//            };
comment|//
comment|//            archiver.getArchiver().addDirectory(baseDirectory, "META-INF/", includes, new String[0]);
comment|//For no plan car, do nothing
comment|//            if (artifactDirectory.exists()) {
comment|//
comment|//                JarFile includedJarFile = new JarFile(artifactDirectory) ;
comment|//
comment|//                if (includedJarFile.getEntry("META-INF/MANIFEST.MF") != null) {
comment|//                    JarArchiver.FilesetManifestConfig mergeFilesetManifestConfig = new JarArchiver.FilesetManifestConfig();
comment|//                    mergeFilesetManifestConfig.setValue("merge");
comment|//                    archiver.getArchiver().setFilesetmanifest(mergeFilesetManifestConfig);
comment|//                } else {
comment|//                    //File configFile = new File(new File(getArtifactInRepositoryDir(), "META-INF"), "imports.txt");
comment|//                    ZipEntry importTxtEntry = includedJarFile.getEntry("META-INF/imports.txt");
comment|//                    if (importTxtEntry != null) {
comment|//                        StringBuilder imports = new StringBuilder("org.apache.geronimo.kernel.osgi,");
comment|//                        if (boot) {
comment|//                            archive.addManifestEntry(Constants.BUNDLE_ACTIVATOR, BootActivator.class.getName());
comment|//                            imports.append("org.apache.geronimo.system.osgi,");
comment|//                        } else {
comment|//                            archive.addManifestEntry(Constants.BUNDLE_ACTIVATOR, ConfigurationActivator.class.getName());
comment|//                        }
comment|//                        archive.addManifestEntry(Constants.BUNDLE_NAME, project.getName());
comment|//                        archive.addManifestEntry(Constants.BUNDLE_VENDOR, project.getOrganization().getName());
comment|//                        ArtifactVersion version = project.getArtifact().getSelectedVersion();
comment|//                        String versionString = "" + version.getMajorVersion() + "." + version.getMinorVersion() + "." + version.getIncrementalVersion();
comment|//                        if (version.getQualifier() != null) {
comment|//                            versionString += "." + version.getQualifier();
comment|//                        }
comment|//                        archive.addManifestEntry(Constants.BUNDLE_VERSION, versionString);
comment|//                        archive.addManifestEntry(Constants.BUNDLE_MANIFESTVERSION, "2");
comment|//                        archive.addManifestEntry(Constants.BUNDLE_DESCRIPTION, project.getDescription());
comment|//                        // NB, no constant for this one
comment|//                        archive.addManifestEntry("Bundle-License", ((License) project.getLicenses().get(0)).getUrl());
comment|//                        archive.addManifestEntry(Constants.BUNDLE_DOCURL, project.getUrl());
comment|//                        archive.addManifestEntry(Constants.BUNDLE_SYMBOLICNAME, project.getGroupId() + "." + project.getArtifactId());
comment|//                        Reader in = new InputStreamReader(includedJarFile.getInputStream(importTxtEntry));
comment|//                        char[] buf = new char[1024];
comment|//                        try {
comment|//                            int i;
comment|//                            while ((i = in.read(buf))> 0) {
comment|//                                imports.append(buf, 0, i);
comment|//                            }
comment|//                        } finally {
comment|//                            in.close();
comment|//                        }
comment|//                        // do we have any additional processing directives?
comment|//                        if (instructions != null) {
comment|//                            String explicitImports = (String) instructions.get(Constants.IMPORT_PACKAGE);
comment|//                            // if there is an Import-Package instructions, then add these imports to the
comment|//                            // list
comment|//                            if (explicitImports != null) {
comment|//                                // if specified on multiple lines, remove the line-ends.
comment|//                                explicitImports = explicitImports.replaceAll("[\r\n]", "");
comment|//                                imports.append(',');
comment|//                                imports.append(explicitImports);
comment|//                            }
comment|//                            String requiredBundles = (String) instructions.get(Constants.REQUIRE_BUNDLE);
comment|//                            if (requiredBundles != null) {
comment|//                                requiredBundles = requiredBundles.replaceAll("[\r\n]", "");
comment|//                                archive.addManifestEntry(Constants.REQUIRE_BUNDLE, requiredBundles);
comment|//                            }
comment|//                        }
comment|//                        archive.addManifestEntry(Constants.IMPORT_PACKAGE, imports.toString());
comment|//                        archive.addManifestEntry(Constants.DYNAMICIMPORT_PACKAGE, "*");
comment|//                    }
comment|//                }
comment|//            }
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
comment|//        } finally {
comment|//            archiver.cleanup();
block|}
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
comment|//    private static class GeronimoArchiver extends MavenArchiver {
comment|//
comment|//        private ArchiverManager archiverManager;
comment|//        private List<File> tmpDirs = new ArrayList<File>();
comment|//
comment|//        public GeronimoArchiver(ArchiverManager archiverManager) {
comment|//            this.archiverManager = archiverManager;
comment|//        }
comment|//
comment|//        public void addArchivedFileSet(File archiveFile) throws ArchiverException {
comment|//            UnArchiver unArchiver;
comment|//            try {
comment|//                unArchiver = archiverManager.getUnArchiver(archiveFile);
comment|//            } catch (NoSuchArchiverException e) {
comment|//                throw new ArchiverException(
comment|//                        "Error adding archived file-set. UnArchiver not found for: " + archiveFile,
comment|//                        e);
comment|//            }
comment|//
comment|//            File tempDir = FileUtils.createTempFile("archived-file-set.", ".tmp", null);
comment|//
comment|//            tempDir.mkdirs();
comment|//
comment|//            tmpDirs.add(tempDir);
comment|//
comment|//            unArchiver.setSourceFile(archiveFile);
comment|//            unArchiver.setDestDirectory(tempDir);
comment|//
comment|//            try {
comment|//                unArchiver.extract();
comment|//            } catch (IOException e) {
comment|//                throw new ArchiverException("Error adding archived file-set. Failed to extract: "
comment|//                        + archiveFile, e);
comment|//            }
comment|//
comment|//            getArchiver().addDirectory(tempDir, null, null, null);
comment|//        }
comment|//
comment|//        public void cleanup() {
comment|//            for (File dir : tmpDirs) {
comment|//                try {
comment|//                    FileUtils.deleteDirectory(dir);
comment|//                } catch (IOException e) {
comment|//                    e.printStackTrace();
comment|//                }
comment|//            }
comment|//            tmpDirs.clear();
comment|//        }
comment|//
comment|//    }
block|}
end_class

end_unit

