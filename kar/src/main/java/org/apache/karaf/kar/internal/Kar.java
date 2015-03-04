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
name|kar
operator|.
name|internal
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
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
name|util
operator|.
name|StreamUtils
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
name|util
operator|.
name|maven
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Representation of a Karaf Kar archive  *   * A Kar archive is a jar file with a special structure that can be used  * to deploy feature repositories, maven repo contents and resources for the  * karaf installation.  *   * meta-inf/Manifest:   *   Karaf-Feature-Start: (true|false) Controls if the features in the feature repos should be started on deploy  *   Karaf-Feature-Repos: (uri)* If present then only the given feature repo urls are added to karaf if it is not  *      present then the karaf file is scanned for repo files  *        * repository/  *   Everything below this directory is treated as a maven repository. On deploy the contents  *   will be copied to a directory below data. This directory will then be added to the   *   maven repos of pax url maven  *     * resource/  *   Everything below this directory will be copied to the karaf base dir on deploy  *   */
end_comment

begin_class
specifier|public
class|class
name|Kar
block|{
specifier|public
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KarServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MANIFEST_ATTR_KARAF_FEATURE_START
init|=
literal|"Karaf-Feature-Start"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MANIFEST_ATTR_KARAF_FEATURE_REPOS
init|=
literal|"Karaf-Feature-Repos"
decl_stmt|;
specifier|private
specifier|final
name|URI
name|karUri
decl_stmt|;
specifier|private
name|boolean
name|shouldInstallFeatures
decl_stmt|;
specifier|private
name|List
argument_list|<
name|URI
argument_list|>
name|featureRepos
decl_stmt|;
specifier|public
name|Kar
parameter_list|(
name|URI
name|karUri
parameter_list|)
block|{
name|this
operator|.
name|karUri
operator|=
name|karUri
expr_stmt|;
block|}
comment|/**      * Extract a kar from a given URI into a repository dir and resource dir      * and populate shouldInstallFeatures and featureRepos      *      * @param repoDir directory to write the repository contents of the kar to      * @param resourceDir directory to write the resource contents of the kar to      */
specifier|public
name|void
name|extract
parameter_list|(
name|File
name|repoDir
parameter_list|,
name|File
name|resourceDir
parameter_list|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|JarInputStream
name|zipIs
init|=
literal|null
decl_stmt|;
name|FeatureDetector
name|featureDetector
init|=
operator|new
name|FeatureDetector
argument_list|()
decl_stmt|;
name|this
operator|.
name|featureRepos
operator|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|shouldInstallFeatures
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|is
operator|=
name|karUri
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|repoDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The KAR file "
operator|+
name|karUri
operator|+
literal|" is already installed"
argument_list|)
throw|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Uncompress the KAR file {} into directory {}"
argument_list|,
name|karUri
argument_list|,
name|repoDir
argument_list|)
expr_stmt|;
name|zipIs
operator|=
operator|new
name|JarInputStream
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|boolean
name|scanForRepos
init|=
literal|true
decl_stmt|;
name|Manifest
name|manifest
init|=
name|zipIs
operator|.
name|getManifest
argument_list|()
decl_stmt|;
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
name|Attributes
name|attr
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
name|String
name|featureStartSt
init|=
operator|(
name|String
operator|)
name|attr
operator|.
name|get
argument_list|(
operator|new
name|Attributes
operator|.
name|Name
argument_list|(
name|MANIFEST_ATTR_KARAF_FEATURE_START
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"false"
operator|.
name|equals
argument_list|(
name|featureStartSt
argument_list|)
condition|)
block|{
name|shouldInstallFeatures
operator|=
literal|false
expr_stmt|;
block|}
name|String
name|featureReposAttr
init|=
operator|(
name|String
operator|)
name|attr
operator|.
name|get
argument_list|(
operator|new
name|Attributes
operator|.
name|Name
argument_list|(
name|MANIFEST_ATTR_KARAF_FEATURE_REPOS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|featureReposAttr
operator|!=
literal|null
condition|)
block|{
name|featureRepos
operator|.
name|add
argument_list|(
operator|new
name|URI
argument_list|(
name|featureReposAttr
argument_list|)
argument_list|)
expr_stmt|;
name|scanForRepos
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|ZipEntry
name|entry
init|=
name|zipIs
operator|.
name|getNextEntry
argument_list|()
decl_stmt|;
while|while
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"repository"
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|"repository/"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|extract
argument_list|(
name|zipIs
argument_list|,
name|entry
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|scanForRepos
operator|&&
name|featureDetector
operator|.
name|isFeaturesRepository
argument_list|(
name|destFile
argument_list|)
condition|)
block|{
name|String
name|uri
init|=
name|Parser
operator|.
name|pathToMaven
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|featureRepos
operator|.
name|add
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"resource"
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|"resource/"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|resourceDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|extract
argument_list|(
name|zipIs
argument_list|,
name|entry
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
name|entry
operator|=
name|zipIs
operator|.
name|getNextEntry
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error extracting kar file "
operator|+
name|karUri
operator|+
literal|" into dir "
operator|+
name|repoDir
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeStream
argument_list|(
name|zipIs
argument_list|)
expr_stmt|;
name|closeStream
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Extract an entry from a KAR file      *       * @param is      * @param zipEntry      * @param dest      * @return      * @throws Exception      */
specifier|private
specifier|static
name|File
name|extract
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|ZipEntry
name|zipEntry
parameter_list|,
name|File
name|dest
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|zipEntry
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Creating directory {}"
argument_list|,
name|dest
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|dest
return|;
block|}
specifier|private
specifier|static
name|void
name|closeStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error closing stream"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getKarName
parameter_list|()
block|{
try|try
block|{
name|String
name|url
init|=
name|karUri
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
literal|"mvn"
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|url
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|url
operator|=
name|url
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|index
operator|=
name|url
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|url
operator|=
name|url
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
return|return
name|url
return|;
block|}
else|else
block|{
name|String
name|karName
init|=
operator|new
name|File
argument_list|(
name|karUri
operator|.
name|toURL
argument_list|()
operator|.
name|getFile
argument_list|()
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|karName
operator|=
name|karName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|karName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|karName
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid kar URI "
operator|+
name|karUri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|URI
name|getKarUri
parameter_list|()
block|{
return|return
name|karUri
return|;
block|}
specifier|public
name|boolean
name|isShouldInstallFeatures
parameter_list|()
block|{
return|return
name|shouldInstallFeatures
return|;
block|}
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|getFeatureRepos
parameter_list|()
block|{
return|return
name|featureRepos
return|;
block|}
block|}
end_class

end_unit

