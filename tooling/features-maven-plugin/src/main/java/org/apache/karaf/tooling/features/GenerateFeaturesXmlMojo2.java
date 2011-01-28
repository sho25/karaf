begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|version
operator|.
name|VersionRange
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
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|ObjectFactory
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
name|factory
operator|.
name|ArtifactFactory
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
name|metadata
operator|.
name|ArtifactMetadataSource
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
name|ArtifactRepository
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
name|ArtifactCollector
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
name|artifact
operator|.
name|resolver
operator|.
name|ArtifactResolutionResult
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
name|plugin
operator|.
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|SystemStreamLog
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|MavenProjectHelper
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
name|project
operator|.
name|artifact
operator|.
name|InvalidDependencyVersionException
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyNode
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilder
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeResolutionListener
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
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_comment
comment|/**  * Generates the features XML file  *   * @version $Revision: 1.1 $  * @goal generate-features-xml2  * @phase compile  * @execute phase="compile"  * @requiresDependencyResolution runtime  * @inheritByDefault true  * @description Generates the features XML file  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
class|class
name|GenerateFeaturesXmlMojo2
extends|extends
name|AbstractLogEnabled
implements|implements
name|Mojo
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"/"
decl_stmt|;
comment|/**      * The dependency tree builder to use.      *       * @component      * @required      * @readonly      */
specifier|private
name|DependencyTreeBuilder
name|dependencyTreeBuilder
decl_stmt|;
comment|/**      * The file to generate      *       * @parameter default-value="${project.build.directory}/feature/feature.xml"      */
specifier|private
name|File
name|outputFile
decl_stmt|;
comment|/**      * The artifact type for attaching the generated file to the project      *       * @parameter default-value="xml"      */
specifier|private
name|String
name|attachmentArtifactType
init|=
literal|"xml"
decl_stmt|;
comment|/**      * The artifact classifier for attaching the generated file to the project      *       * @parameter default-value="features"      */
specifier|private
name|String
name|attachmentArtifactClassifier
init|=
literal|"features"
decl_stmt|;
comment|/**      * The kernel version for which to generate the bundle      *       * @parameter      */
specifier|private
name|String
name|kernelVersion
decl_stmt|;
comment|/*      * A list of packages exported by the kernel      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|VersionRange
argument_list|>
name|kernelExports
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|VersionRange
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * A file containing the list of bundles      *       * @parameter      */
specifier|private
name|File
name|bundles
decl_stmt|;
comment|/*      * A set of known bundles      */
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|knownBundles
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/*      * A list of exports by the bundles      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|VersionRange
argument_list|,
name|Artifact
argument_list|>
argument_list|>
name|bundleExports
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|VersionRange
argument_list|,
name|Artifact
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/*      * The set of system exports      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|systemExports
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/*      * These bundles are the features that will be built      */
comment|//    private Map<Artifact, Feature> features = new HashMap<Artifact, Feature>();
comment|//new
comment|/**      * The maven project.      *      * @parameter expression="${project}"      * @required      * @readonly      */
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The maven project's helper.      *      * @component      * @required      * @readonly      */
specifier|protected
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|//maven log
specifier|private
name|Log
name|log
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|PrintStream
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|File
name|dir
init|=
name|outputFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|out
operator|=
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outputFile
argument_list|)
argument_list|)
expr_stmt|;
name|readSystemPackages
argument_list|()
expr_stmt|;
comment|//            readKernelBundles();
comment|//            readBundles();
comment|//            discoverBundles();
name|getDependencies
argument_list|(
name|project
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writeFeatures
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// now lets attach it
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
name|attachmentArtifactType
argument_list|,
name|attachmentArtifactClassifier
argument_list|,
name|outputFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Unable to create features.xml file: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/*     * Read all the system provided packages from the<code>config.properties</code> file     */
specifier|private
name|void
name|readSystemPackages
parameter_list|()
throws|throws
name|IOException
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"config.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|readSystemPackages
argument_list|(
name|properties
argument_list|,
literal|"jre-1.5"
argument_list|)
expr_stmt|;
name|readSystemPackages
argument_list|(
name|properties
argument_list|,
literal|"osgi"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readSystemPackages
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|String
name|packages
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packages
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
control|)
block|{
name|systemExports
operator|.
name|add
argument_list|(
name|pkg
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Download a Kernel distro and check the list of bundles provided by the Kernel      */
comment|//    private void readKernelBundles() throws ArtifactResolutionException, ArtifactNotFoundException, MojoExecutionException,
comment|//        ZipException, IOException, DependencyTreeBuilderException {
comment|//        final Collection<Artifact> kernelArtifacts;
comment|//        if (kernelVersion == null) {
comment|//           getLogger().info("Step 1: Building list of provided bundle exports");
comment|//           kernelArtifacts = new HashSet<Artifact>();
comment|//           DependencyNode tree = dependencyTreeBuilder.buildDependencyTree(project, localRepo, factory, artifactMetadataSource, new ArtifactFilter() {
comment|//
comment|//            public boolean include(Artifact artifact) {
comment|//                return true;
comment|//            }
comment|//
comment|//           }, new DefaultArtifactCollector());
comment|//           tree.accept(new DependencyNodeVisitor() {
comment|//                public boolean endVisit(DependencyNode node) {
comment|//                    // we want the next sibling too
comment|//                    return true;
comment|//                }
comment|//                public boolean visit(DependencyNode node) {
comment|//                    if (node.getState() != DependencyNode.OMITTED_FOR_CONFLICT) {
comment|//                        Artifact artifact = node.getArtifact();
comment|//                        if (Artifact.SCOPE_PROVIDED.equals(artifact.getScope())&& !artifact.getType().equals("pom")) {
comment|//                            kernelArtifacts.add(artifact);
comment|//                        }
comment|//                    }
comment|//                    // we want the children too
comment|//                    return true;
comment|//                }
comment|//            });
comment|//        } else {
comment|//            getLogger().info("Step 1 : Building list of kernel exports");
comment|//            getLogger().warn("Use of 'kernelVersion' is deprecated -- use a dependency with scope 'provided' instead");
comment|//            Artifact kernel = factory.createArtifact("org.apache.karaf", "apache-karaf", kernelVersion, Artifact.SCOPE_PROVIDED, "pom");
comment|//            resolver.resolve(kernel, remoteRepos, localRepo);
comment|//            kernelArtifacts = getDependencies(kernel);
comment|//        }
comment|//        for (Artifact artifact : kernelArtifacts) {
comment|//            registerKernelBundle(artifact);
comment|//        }
comment|//        getLogger().info("...done!");
comment|//    }
comment|/*      * Read the list of bundles we can use to satisfy links      */
comment|//    private void readBundles() throws IOException, ArtifactResolutionException, ArtifactNotFoundException {
comment|//        BufferedReader reader = null;
comment|//        try {
comment|//            if (bundles != null) {
comment|//                getLogger().info("Step 2 : Building a list of exports for bundles in " + bundles.getAbsolutePath());
comment|//                reader = new BufferedReader(new FileReader(bundles));
comment|//                String line = reader.readLine();
comment|//                while (line != null) {
comment|//                    if (line.contains("/")&& !line.startsWith("#")) {
comment|//                        String[] elements = line.split("/");
comment|//                        Artifact artifact = factory.createArtifact(elements[0], elements[1], elements[2], Artifact.SCOPE_PROVIDED,
comment|//                                                                   elements[3]);
comment|//                        registerBundle(artifact);
comment|//                    }
comment|//                    line = reader.readLine();
comment|//                }
comment|//            } else {
comment|//                getLogger().info("Step 2 : No Bundle file supplied for building list of exports");
comment|//            }
comment|//        } finally {
comment|//            if (reader != null) {
comment|//                reader.close();
comment|//            }
comment|//        }
comment|//        getLogger().info("...done!");
comment|//    }
comment|/*      * Auto-discover bundles currently in the dependencies      */
comment|//    private void discoverBundles() throws ArtifactResolutionException, ArtifactNotFoundException, ZipException, IOException {
comment|//    	getLogger().info("Step 3 : Discovering bundles in Maven dependencies");
comment|//		for (Artifact dependency : (Set<Artifact>) project.getArtifacts()) {
comment|//			// we will generate a feature for this afterwards
comment|//			if (project.getDependencyArtifacts().contains(dependency)) {
comment|//				continue;
comment|//			}
comment|//			// this is a provided bundle, has been handled in step 1
comment|//			if (dependency.getScope().equals(Artifact.SCOPE_PROVIDED)) {
comment|//			    continue;
comment|//			}
comment|//			if (isDiscoverableBundle(dependency)) {
comment|//				getLogger().info("  Discovered " + dependency);
comment|//				registerBundle(dependency);
comment|//			}
comment|//		}
comment|//		getLogger().info("...done!");
comment|//	}
comment|/*      * Write all project dependencies as feature      */
specifier|private
name|void
name|writeFeatures
parameter_list|(
name|PrintStream
name|out
parameter_list|)
throws|throws
name|ArtifactResolutionException
throws|,
name|ArtifactNotFoundException
throws|,
name|IOException
throws|,
name|JAXBException
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Step 4 : Generating "
operator|+
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectFactory
name|objectFactory
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
name|FeaturesRoot
name|featuresRoot
init|=
name|objectFactory
operator|.
name|createFeaturesRoot
argument_list|()
decl_stmt|;
name|Feature
name|feature
init|=
name|objectFactory
operator|.
name|createFeature
argument_list|()
decl_stmt|;
name|featuresRoot
operator|.
name|getFeature
argument_list|()
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setName
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setVersion
argument_list|(
name|project
operator|.
name|getArtifact
argument_list|()
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|localDependencies
control|)
block|{
name|String
name|bundleName
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
name|bundleName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundleName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"mvn:%s/%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Bundle
name|bundle
init|=
name|objectFactory
operator|.
name|createBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setValue
argument_list|(
name|bundleName
argument_list|)
expr_stmt|;
name|feature
operator|.
name|getBundle
argument_list|()
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
name|JaxbUtil
operator|.
name|marshal
argument_list|(
name|FeaturesRoot
operator|.
name|class
argument_list|,
name|featuresRoot
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|//        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
comment|//        out.println("<features>");
comment|//        out.println("<feature name='" + project.getArtifactId() + "' version='"
comment|//                + project.getArtifact().getBaseVersion() + "'>");
comment|//
comment|//        for (Artifact artifact : localDependencies) {
comment|//            if (artifact.getType().equals("jar")) {
comment|//                out.println(String.format("<bundle>mvn:%s/%s/%s</bundle>", artifact.getGroupId(), artifact.getArtifactId(), artifact.getBaseVersion()));
comment|//            } else {
comment|//                out.println(String.format("<bundle>mvn:%s/%s/%s/%s</bundle>", artifact.getGroupId(), artifact.getArtifactId(), artifact.getBaseVersion(), artifact.getType()));
comment|//            }
comment|////            if (!artifact.getScope().equals(Artifact.SCOPE_PROVIDED)&& !artifact.getType().equals("pom")) {
comment|////                getLogger().info(" Generating feature " + artifact.getArtifactId() + " from " + artifact);
comment|////                Feature feature = getFeature(artifact);
comment|////                feature.write(out);
comment|//////                registerFeature(artifact, feature);
comment|////            }
comment|//        }
comment|//        out.println("</feature>");
comment|//        out.println("</features>");
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"...done!"
argument_list|)
expr_stmt|;
block|}
comment|/*      * Get the feature for an artifact       */
comment|//    private Feature getFeature(Artifact artifact) throws ArtifactResolutionException, ArtifactNotFoundException, ZipException, IOException {
comment|//        Feature feature = new Feature(artifact);
comment|////        addRequirements(artifact, feature);
comment|//        return feature;
comment|//    }
comment|/*      * Only auto-discover an OSGi bundle      * - if it is not already known as a feature itself      * - if it is not another version of an already known bundle      */
comment|//    private boolean isDiscoverableBundle(Artifact artifact) {
comment|//        if (isBundle(artifact)&& !isFeature(artifact)&& !artifact.getScope().equals(Artifact.SCOPE_PROVIDED)) {
comment|//            for (String known : knownBundles) {
comment|//                String[] elements = known.split("/");
comment|//                if (artifact.getGroupId().equals(elements[0])&&
comment|//                    artifact.getArtifactId().equals(elements[1])) {
comment|//                    getLogger().debug(String.format("  Avoid auto-discovery for %s because of existing bundle %s",
comment|//                                                 toString(artifact), known));
comment|//                    return false;
comment|//                }
comment|//            }
comment|//            return true;
comment|//        }
comment|//        return false;
comment|//    }
comment|/*      * Check if the given artifact is a bundle      */
comment|//    private boolean isBundle(Artifact artifact) {
comment|//        if (knownBundles.contains(toString(artifact)) || artifact.getArtifactHandler().getPackaging().equals("bundle")) {
comment|//            return true;
comment|//        } else {
comment|//            try {
comment|//                Manifest manifest = getManifest(artifact);
comment|//                if (ManifestUtils.getBsn(manifest) != null) {
comment|//                    getLogger().debug(String.format("MANIFEST.MF for '%s' contains Bundle-Name '%s'",
comment|//                                                 artifact, ManifestUtils.getBsn(manifest)));
comment|//                    return true;
comment|//                }
comment|//            } catch (ZipException e) {
comment|//                getLogger().debug("Unable to determine if " + artifact + " is a bundle; defaulting to false", e);
comment|//            } catch (IOException e) {
comment|//                getLogger().debug("Unable to determine if " + artifact + " is a bundle; defaulting to false", e);
comment|//            } catch (Exception e) {
comment|//                getLogger().debug("Unable to determine if " + artifact + " is a bundle; defaulting to false", e);
comment|//            }
comment|//        }
comment|//        return false;
comment|//     }
comment|/*      * Add requirements for an artifact to a feature      */
comment|//    private void addRequirements(Artifact artifact, Feature feature) throws ArtifactResolutionException, ArtifactNotFoundException, ZipException, IOException {
comment|//        Manifest manifest = getManifest(artifact);
comment|//        Collection<Clause> remaining = getRemainingImports(manifest);
comment|//        Artifact previous = null;
comment|//        for (Clause clause : remaining) {
comment|//            Artifact add = null;
comment|//            Map<VersionRange, Artifact> versions = bundleExports.get(clause.getName());
comment|//            if (versions != null) {
comment|//                for (VersionRange range : versions.keySet()) {
comment|//                    add = versions.get(range);
comment|//                    if (range.intersect(ManifestUtils.getVersionRange(clause)) != null) {
comment|//                        add = versions.get(range);
comment|//                    }
comment|//                }
comment|//            }
comment|//            if (add == null) {
comment|//                if (ManifestUtils.isOptional(clause)) {
comment|//                    // debug logging for optional dependency...
comment|//                    getLogger().debug(String.format("  Unable to find suitable bundle for optional dependency %s (%s)",
comment|//                                                 clause.getName(), ManifestUtils.getVersionRange(clause)));
comment|//                } else {
comment|//                    // ...but a warning for a mandatory dependency
comment|//                    getLogger().warn(
comment|//                                  String.format("  Unable to find suitable bundle for dependency %s (%s) (required by %s)",
comment|//                                                clause.getName(), ManifestUtils.getVersionRange(clause), artifact.getArtifactId()));
comment|//                }
comment|//            } else {
comment|//                if (!add.equals(previous)&& feature.push(add)&& !isFeature(add)) {
comment|//                    //and get requirements for the bundle we just added
comment|//                    getLogger().debug("  Getting requirements for " + add);
comment|//                    addRequirements(add, feature);
comment|//                }
comment|//            }
comment|//            previous = add;
comment|//        }
comment|//    }
comment|/*      * Check if a given bundle is itself being generated as a feature      */
comment|//    private boolean isFeature(Artifact artifact) {
comment|//        return features.containsKey(artifact);
comment|//    }
comment|/*      * Register a bundle, enlisting all packages it provides      */
comment|//    private void registerBundle(Artifact artifact) throws ArtifactResolutionException, ArtifactNotFoundException, ZipException,
comment|//        IOException {
comment|//        getLogger().debug("Registering bundle " + artifact);
comment|//        knownBundles.add(toString(artifact));
comment|//        Manifest manifest = getManifest(artifact);
comment|//        for (Clause clause : getManifestEntries(ManifestUtils.getExports(manifest))) {
comment|//            Map<VersionRange, Artifact> versions = bundleExports.get(clause.getName());
comment|//            if (versions == null) {
comment|//                versions = new HashMap<VersionRange, Artifact>();
comment|//            }
comment|//            versions.put(ManifestUtils.getVersionRange(clause), artifact);
comment|//            getLogger().debug(String.format(" %s exported by bundle %s", clause.getName(), artifact));
comment|//            bundleExports.put(clause.getName(), versions);
comment|//        }
comment|//    }
comment|/*      * Register a feature and also register the bundle for the feature      */
comment|//    private void registerFeature(Artifact artifact, Feature feature) throws ArtifactResolutionException, ArtifactNotFoundException, ZipException,
comment|//        IOException {
comment|//        features.put(artifact, feature);
comment|//        registerBundle(artifact);
comment|//    }
comment|/*      * Determine the list of imports to be resolved      */
comment|//    private Collection<Clause> getRemainingImports(Manifest manifest) {
comment|//        // take all imports
comment|//        Collection<Clause> input = getManifestEntries(ManifestUtils.getImports(manifest));
comment|//        Collection<Clause> output = new LinkedList<Clause>(input);
comment|//        // remove imports satisfied by exports in the same bundle
comment|//        for (Clause clause : input) {
comment|//            for (Clause export : getManifestEntries(ManifestUtils.getExports(manifest))) {
comment|//                if (clause.getName().equals(export.getName())) {
comment|//                    output.remove(clause);
comment|//                }
comment|//            }
comment|//        }
comment|//        // remove imports for packages exported by the kernel
comment|//        for (Clause clause : input) {
comment|//            for (String export : kernelExports.keySet()) {
comment|//                if (clause.getName().equals(export)) {
comment|//                    output.remove(clause);
comment|//                }
comment|//            }
comment|//        }
comment|//        // remove imports for packages exported by the system bundle
comment|//        for (Clause clause : input) {
comment|//            if (systemExports.contains(clause.getName())) {
comment|//                output.remove(clause);
comment|//            }
comment|//        }
comment|//        return output;
comment|//    }
comment|//    private Collection<Clause> getManifestEntries(List imports) {
comment|//        if (imports == null) {
comment|//            return new LinkedList<Clause>();
comment|//        } else {
comment|//            return (Collection<Clause>)imports;
comment|//        }
comment|//    }
comment|//    private Manifest getManifest(Artifact artifact) throws ArtifactResolutionException, ArtifactNotFoundException, ZipException,
comment|//        IOException {
comment|//        File localFile = new File(localRepo.pathOf(artifact));
comment|//        ZipFile file;
comment|//        if (localFile.exists()) {
comment|//            //avoid going over to the repository if the file is already on the disk
comment|//            file = new ZipFile(localFile);
comment|//        } else {
comment|//            resolver.resolve(artifact, remoteRepos, localRepo);
comment|//            file = new ZipFile(artifact.getFile());
comment|//        }
comment|//        return new Manifest(file.getInputStream(file.getEntry("META-INF/MANIFEST.MF")));
comment|//    }
comment|//    private List<Artifact> getDependencies(Artifact artifact) {
comment|//        List<Artifact> list = new ArrayList<Artifact>();
comment|//        try {
comment|//            ResolutionGroup pom = artifactMetadataSource.retrieve(artifact, localRepo, remoteRepos);
comment|//            if (pom != null) {
comment|//                list.addAll(pom.getArtifacts());
comment|//            }
comment|//        } catch (ArtifactMetadataRetrievalException e) {
comment|//            getLogger().warn("Unable to retrieve metadata for " + artifact + ", not including dependencies for it");
comment|//        } catch (InvalidArtifactRTException e) {
comment|//            getLogger().warn("Unable to retrieve metadata for " + artifact + ", not including dependencies for it");
comment|//        }
comment|//        return list;
comment|//    }
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
name|artifact
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s/%s/%s"
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|//    private class Feature {
comment|//
comment|//        private Stack<Artifact> artifacts = new Stack<Artifact>();
comment|//        private final Artifact artifact;
comment|//
comment|//        private Feature(Artifact artifact) {
comment|//            super();
comment|//            this.artifact = artifact;
comment|//            artifacts.push(artifact);
comment|//        }
comment|//
comment|//        public boolean push(Artifact item) {
comment|//            if (artifacts.contains(item)) {
comment|//                artifacts.remove(item);
comment|//                artifacts.push(item);
comment|//                return false;
comment|//            }
comment|//            if (!artifacts.contains(item)) {
comment|//                artifacts.push(item);
comment|//                return true;
comment|//            }
comment|//            return false;
comment|//        }
comment|//
comment|//        public void write(PrintStream out) {
comment|//            out.println("<feature name='" + artifact.getArtifactId() + "' version='"
comment|//            		+ artifact.getBaseVersion() + "'>");
comment|//
comment|//            Stack<Artifact> resulting = new Stack<Artifact>();
comment|//            resulting.addAll(artifacts);
comment|//
comment|//            // remove dependencies for included features
comment|//            for (Artifact next : artifacts) {
comment|//                if (isFeature(next)) {
comment|//                    resulting.removeAll(features.get(next).getDependencies());
comment|//                }
comment|//            }
comment|//
comment|//            while (!resulting.isEmpty()) {
comment|//            	Artifact next = resulting.pop();
comment|//                if (isFeature(next)) {
comment|//                    out.println("<feature version='"
comment|//            		+ next.getBaseVersion() + "'>" + String.format("%s</feature>", next.getArtifactId()));
comment|//                } else {
comment|//                    if (next.getType().equals("jar")) {
comment|//                        out.println(String.format("<bundle>mvn:%s/%s/%s</bundle>", next.getGroupId(), next.getArtifactId(), next.getBaseVersion()));
comment|//                    } else {
comment|//                        out.println(String.format("<bundle>mvn:%s/%s/%s/%s</bundle>", next.getGroupId(), next.getArtifactId(), next.getBaseVersion(), next.getType()));
comment|//                    }
comment|//                }
comment|//            }
comment|//            out.println("</feature>");
comment|//        }
comment|//
comment|//        public List<Artifact> getDependencies() {
comment|//            List<Artifact> dependencies = new LinkedList<Artifact>(artifacts);
comment|//            dependencies.remove(artifact);
comment|//            return dependencies;
comment|//        }
comment|//    }
comment|//artifact search code adapted from geronimo car plugin
comment|/**      * The artifact factory to use.      *      * @component      * @required      * @readonly      */
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * The artifact repository to use.      *      * @parameter expression="${localRepository}"      * @required      * @readonly      */
specifier|private
name|ArtifactRepository
name|localRepository
decl_stmt|;
comment|/**      * The artifact metadata source to use.      *      * @component      * @required      * @readonly      */
specifier|private
name|ArtifactMetadataSource
name|artifactMetadataSource
decl_stmt|;
comment|/**      * The artifact collector to use.      *      * @component      * @required      * @readonly      */
specifier|private
name|ArtifactCollector
name|artifactCollector
decl_stmt|;
comment|//all dependencies
specifier|protected
name|Set
argument_list|<
name|Artifact
argument_list|>
name|dependencyArtifacts
decl_stmt|;
comment|//dependencies we are interested in
specifier|protected
name|Set
argument_list|<
name|Artifact
argument_list|>
name|localDependencies
decl_stmt|;
comment|//log of what happened during search
specifier|protected
name|String
name|treeListing
decl_stmt|;
specifier|protected
name|void
name|getDependencies
parameter_list|(
name|MavenProject
name|project
parameter_list|,
name|boolean
name|useTransitiveDependencies
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|DependencyTreeResolutionListener
name|listener
init|=
operator|new
name|DependencyTreeResolutionListener
argument_list|(
name|getLogger
argument_list|()
argument_list|)
decl_stmt|;
name|DependencyNode
name|rootNode
decl_stmt|;
try|try
block|{
name|Map
name|managedVersions
init|=
name|project
operator|.
name|getManagedVersionMap
argument_list|()
decl_stmt|;
name|Set
name|dependencyArtifacts
init|=
name|project
operator|.
name|getDependencyArtifacts
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependencyArtifacts
operator|==
literal|null
condition|)
block|{
name|dependencyArtifacts
operator|=
name|project
operator|.
name|createArtifacts
argument_list|(
name|artifactFactory
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|ArtifactResolutionResult
name|result
init|=
name|artifactCollector
operator|.
name|collect
argument_list|(
name|dependencyArtifacts
argument_list|,
name|project
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|managedVersions
argument_list|,
name|localRepository
argument_list|,
name|project
operator|.
name|getRemoteArtifactRepositories
argument_list|()
argument_list|,
name|artifactMetadataSource
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|dependencyArtifacts
operator|=
name|result
operator|.
name|getArtifacts
argument_list|()
expr_stmt|;
name|rootNode
operator|=
name|listener
operator|.
name|getRootNode
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArtifactResolutionException
name|exception
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Cannot build project dependency tree"
argument_list|,
name|exception
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidDependencyVersionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Invalid dependency version for artifact "
operator|+
name|project
operator|.
name|getArtifact
argument_list|()
argument_list|)
throw|;
block|}
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|()
decl_stmt|;
name|scanner
operator|.
name|scan
argument_list|(
name|rootNode
argument_list|,
name|useTransitiveDependencies
argument_list|)
expr_stmt|;
name|localDependencies
operator|=
name|scanner
operator|.
name|localDependencies
operator|.
name|keySet
argument_list|()
expr_stmt|;
name|treeListing
operator|=
name|scanner
operator|.
name|getLog
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setLog
parameter_list|(
name|Log
name|log
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
block|}
specifier|public
name|Log
name|getLog
parameter_list|()
block|{
if|if
condition|(
name|log
operator|==
literal|null
condition|)
block|{
name|setLog
argument_list|(
operator|new
name|SystemStreamLog
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|log
return|;
block|}
specifier|private
specifier|static
class|class
name|Scanner
block|{
specifier|private
specifier|static
enum|enum
name|Accept
block|{
name|ACCEPT
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
block|,
name|PROVIDED
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
block|,
name|STOP
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
block|;
specifier|private
specifier|final
name|boolean
name|more
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|local
decl_stmt|;
specifier|private
name|Accept
parameter_list|(
name|boolean
name|more
parameter_list|,
name|boolean
name|local
parameter_list|)
block|{
name|this
operator|.
name|more
operator|=
name|more
expr_stmt|;
name|this
operator|.
name|local
operator|=
name|local
expr_stmt|;
block|}
specifier|public
name|boolean
name|isContinue
parameter_list|()
block|{
return|return
name|more
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|local
return|;
block|}
block|}
comment|//all the dependencies needed for this car, with provided dependencies removed
specifier|private
specifier|final
name|Map
argument_list|<
name|Artifact
argument_list|,
name|Set
argument_list|<
name|Artifact
argument_list|>
argument_list|>
name|localDependencies
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Artifact
argument_list|,
name|Set
argument_list|<
name|Artifact
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|//dependencies from ancestor cars, to be removed from localDependencies.
specifier|private
specifier|final
name|Set
argument_list|<
name|Artifact
argument_list|>
name|carDependencies
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|StringBuilder
name|log
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|public
name|void
name|scan
parameter_list|(
name|DependencyNode
name|rootNode
parameter_list|,
name|boolean
name|useTransitiveDependencies
parameter_list|)
block|{
name|Set
argument_list|<
name|Artifact
argument_list|>
name|children
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DependencyNode
name|child
range|:
operator|(
name|List
argument_list|<
name|DependencyNode
argument_list|>
operator|)
name|rootNode
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|scan
argument_list|(
name|child
argument_list|,
name|Accept
operator|.
name|ACCEPT
argument_list|,
name|useTransitiveDependencies
argument_list|,
literal|false
argument_list|,
literal|""
argument_list|,
name|children
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useTransitiveDependencies
condition|)
block|{
name|localDependencies
operator|.
name|keySet
argument_list|()
operator|.
name|removeAll
argument_list|(
name|carDependencies
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|scan
parameter_list|(
name|DependencyNode
name|rootNode
parameter_list|,
name|Accept
name|parentAccept
parameter_list|,
name|boolean
name|useTransitiveDependencies
parameter_list|,
name|boolean
name|isFromCar
parameter_list|,
name|String
name|indent
parameter_list|,
name|Set
argument_list|<
name|Artifact
argument_list|>
name|parentsChildren
parameter_list|)
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
name|rootNode
argument_list|)
decl_stmt|;
name|Accept
name|accept
init|=
name|accept
argument_list|(
name|artifact
argument_list|,
name|parentAccept
argument_list|)
decl_stmt|;
if|if
condition|(
name|accept
operator|.
name|isContinue
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|Artifact
argument_list|>
name|children
init|=
name|localDependencies
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFromCar
condition|)
block|{
if|if
condition|(
operator|!
name|isFeature
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"from feature:"
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|carDependencies
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"is feature:"
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"local:"
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|carDependencies
operator|.
name|contains
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
name|indent
argument_list|)
operator|.
name|append
argument_list|(
literal|"already in feature, returning:"
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|parentsChildren
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
return|return;
block|}
name|parentsChildren
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
name|children
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|Artifact
argument_list|>
argument_list|()
expr_stmt|;
name|localDependencies
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|children
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isFeature
argument_list|(
name|artifact
argument_list|)
operator|||
operator|!
name|useTransitiveDependencies
condition|)
block|{
name|isFromCar
operator|=
literal|true
expr_stmt|;
block|}
block|}
for|for
control|(
name|DependencyNode
name|child
range|:
operator|(
name|List
argument_list|<
name|DependencyNode
argument_list|>
operator|)
name|rootNode
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|scan
argument_list|(
name|child
argument_list|,
name|accept
argument_list|,
name|useTransitiveDependencies
argument_list|,
name|isFromCar
argument_list|,
name|indent
operator|+
literal|"  "
argument_list|,
name|children
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isFeature
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|artifact
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"kar"
argument_list|)
operator|||
literal|"feature"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getLog
parameter_list|()
block|{
return|return
name|log
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|Artifact
name|getArtifact
parameter_list|(
name|DependencyNode
name|rootNode
parameter_list|)
block|{
name|Artifact
name|artifact
init|=
name|rootNode
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootNode
operator|.
name|getRelatedArtifact
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|artifact
operator|=
name|rootNode
operator|.
name|getRelatedArtifact
argument_list|()
expr_stmt|;
block|}
return|return
name|artifact
return|;
block|}
specifier|private
name|Accept
name|accept
parameter_list|(
name|Artifact
name|dependency
parameter_list|,
name|Accept
name|previous
parameter_list|)
block|{
comment|//            if (dependency.getGroupId().startsWith("org.apache.geronimo.genesis")) {
comment|//                return Accept.STOP;
comment|//            }
name|String
name|scope
init|=
name|dependency
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
name|scope
operator|==
literal|null
operator|||
literal|"runtime"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scope
argument_list|)
operator|||
literal|"compile"
operator|.
name|equalsIgnoreCase
argument_list|(
name|scope
argument_list|)
condition|)
block|{
return|return
name|previous
return|;
block|}
return|return
name|Accept
operator|.
name|STOP
return|;
block|}
block|}
block|}
end_class

end_unit

