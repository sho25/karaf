begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|command
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
name|FileNotFoundException
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
name|Dependency
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
name|FeaturesService
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
name|command
operator|.
name|completers
operator|.
name|AvailableFeatureCompleter
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
name|Argument
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
name|Completion
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
name|Reference
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
name|completers
operator|.
name|FileCompleter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|mvn
operator|.
name|MavenResolver
import|;
end_import

begin_comment
comment|/**  * Simple {@link FeaturesCommandSupport} implementation that allows a user in  * the karaf shell to export the bundles associated with a given feature to the  * file system. This is useful for several use cases, such as in the event you  * need to deploy the functionality offered by a particular feature to an OBR  * repository.  *   */
end_comment

begin_class
annotation|@
name|Service
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"feature"
argument_list|,
name|name
operator|=
literal|"export-bundles"
argument_list|,
name|description
operator|=
literal|"Export all of the bundles that make up a specified feature to a directory on the file system."
argument_list|)
specifier|public
class|class
name|FeatureExport
extends|extends
name|FeaturesCommandSupport
block|{
comment|/**      * Inject a {@link MavenResolver} so we can translate from a      * {@link BundleInfo} in a {@link Feature} into the raw bundle from maven.      */
annotation|@
name|Reference
specifier|private
name|MavenResolver
name|resolver
decl_stmt|;
comment|/**      * The name of the feature you want to export.      */
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"featureName"
argument_list|,
name|description
operator|=
literal|"The name of the feature you want to export bundles for"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
annotation|@
name|Completion
argument_list|(
name|value
operator|=
name|AvailableFeatureCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|featureName
init|=
literal|null
decl_stmt|;
comment|/**      * The location we'll export the bundles to.      */
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"exportLocation"
argument_list|,
name|description
operator|=
literal|"Where you want to export the bundles"
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|Completion
argument_list|(
name|value
operator|=
name|FileCompleter
operator|.
name|class
argument_list|)
specifier|private
name|String
name|exportLocation
decl_stmt|;
comment|/**      * The version of the feature you want to export.      */
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|aliases
operator|=
block|{
literal|"--version"
block|}
argument_list|,
name|description
operator|=
literal|"The version of the feature you want to export bundles for.  Default is latest"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|featureVersion
init|=
literal|null
decl_stmt|;
comment|/**      * Option indicating that only bundles marked as a dependency should be      * exported.      */
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|aliases
operator|=
block|{
literal|"--dependencies-only"
block|}
argument_list|,
name|description
operator|=
literal|"This flag indicates that only bundles marked as a dependency will be exported."
argument_list|,
name|required
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|onlyDependencies
init|=
literal|false
decl_stmt|;
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|doExecute
parameter_list|(
specifier|final
name|FeaturesService
name|featuresService
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No maven resolver implementation found."
argument_list|)
throw|;
block|}
else|else
block|{
specifier|final
name|File
name|destination
init|=
operator|new
name|File
argument_list|(
name|exportLocation
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|prepareDestination
argument_list|(
name|destination
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Invalid exportLocation specified: "
operator|+
name|exportLocation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Feature
name|feature
init|=
name|featureVersion
operator|!=
literal|null
condition|?
name|featuresService
operator|.
name|getFeature
argument_list|(
name|featureName
argument_list|,
name|featureVersion
argument_list|)
else|:
name|featuresService
operator|.
name|getFeature
argument_list|(
name|featureName
argument_list|)
decl_stmt|;
if|if
condition|(
name|feature
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Could not find specified feature: '"
operator|+
name|featureName
operator|+
literal|"' version '"
operator|+
name|featureVersion
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Save feature content bundles.
name|saveBundles
argument_list|(
name|destination
argument_list|,
name|feature
argument_list|,
name|featuresService
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Prepare the target destination directory.      *       * @param destination      *            Where we'll save the bundles      * @return true if it is valid, false otherwise      */
specifier|private
name|boolean
name|prepareDestination
parameter_list|(
specifier|final
name|File
name|destination
parameter_list|)
block|{
return|return
operator|(
name|destination
operator|.
name|isDirectory
argument_list|()
operator|||
name|destination
operator|.
name|mkdirs
argument_list|()
operator|)
return|;
block|}
comment|/**      * Save the feature bundles, and all of its transitive dependency bundles.      *       * @param dest      *            The target directory where we'll save the feature bundles      * @param feature      *            The {@link Feature} we're saving      * @throws Exception      *             If there is an issue saving the bundles or resolving the      *             feature      */
specifier|private
name|void
name|saveBundles
parameter_list|(
specifier|final
name|File
name|dest
parameter_list|,
specifier|final
name|Feature
name|feature
parameter_list|,
specifier|final
name|FeaturesService
name|featuresService
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Save this feature's bundles.
for|for
control|(
specifier|final
name|BundleInfo
name|info
range|:
name|feature
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|onlyDependencies
operator|||
operator|(
name|onlyDependencies
operator|&&
name|info
operator|.
name|isDependency
argument_list|()
operator|)
condition|)
block|{
specifier|final
name|File
name|resolvedLocation
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|info
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|copyFileToDirectory
argument_list|(
name|resolvedLocation
argument_list|,
name|dest
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Exported '"
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
operator|+
literal|"' bundle: "
operator|+
name|info
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Already exported bundle: "
operator|+
name|info
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Save feature's dependency bundles.
for|for
control|(
specifier|final
name|Dependency
name|dependency
range|:
name|feature
operator|.
name|getDependencies
argument_list|()
control|)
block|{
specifier|final
name|Feature
name|dFeature
init|=
name|featuresService
operator|.
name|getFeature
argument_list|(
name|dependency
operator|.
name|getName
argument_list|()
argument_list|,
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dFeature
operator|!=
literal|null
condition|)
block|{
name|saveBundles
argument_list|(
name|dest
argument_list|,
name|dFeature
argument_list|,
name|featuresService
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable to resolve dependency feature! '"
operator|+
name|dependency
operator|.
name|getName
argument_list|()
operator|+
literal|"' '"
operator|+
name|dependency
operator|.
name|getVersion
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to resolve dependency feature '"
operator|+
name|dependency
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|dependency
operator|.
name|getVersion
argument_list|()
operator|+
literal|"' while exporting '"
operator|+
name|featureName
operator|+
literal|"/"
operator|+
name|featureVersion
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Simple method to copy a file to a target destination directory.      *       * @param file      *            The file to copy      * @param directory      *            The directory to copy it to      * @return true if successful, false if it wasn't      * @throws FileNotFoundException      *             If the file specified doesn't exist      * @throws IOException      *             If there is an issue performing the copy      */
specifier|private
specifier|static
name|boolean
name|copyFileToDirectory
parameter_list|(
specifier|final
name|File
name|file
parameter_list|,
specifier|final
name|File
name|directory
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|directory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't copy to non-directory specified: "
operator|+
name|directory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|boolean
name|copied
init|=
literal|false
decl_stmt|;
specifier|final
name|File
name|newFile
init|=
operator|new
name|File
argument_list|(
name|directory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/"
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|newFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
try|try
init|(
specifier|final
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
try|try
init|(
specifier|final
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|newFile
argument_list|)
init|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
operator|*
literal|8
index|]
decl_stmt|;
name|int
name|read
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|fis
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|fos
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|copied
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|copied
return|;
block|}
block|}
block|}
end_class

end_unit

