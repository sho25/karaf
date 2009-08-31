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
name|felix
operator|.
name|karaf
operator|.
name|deployer
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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|JarFile
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
name|JarOutputStream
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
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
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|felix
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
name|felix
operator|.
name|karaf
operator|.
name|features
operator|.
name|Repository
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
name|fileinstall
operator|.
name|listener
operator|.
name|ArtifactTransformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|SynchronousBundleListener
import|;
end_import

begin_comment
comment|/**  * A deployment listener able to hot deploy a feature descriptor  */
end_comment

begin_class
specifier|public
class|class
name|FeatureDeploymentListener
implements|implements
name|ArtifactTransformer
implements|,
name|SynchronousBundleListener
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FEATURE_PATH
init|=
literal|"org.apache.felix.karaf.gshell.features"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FeatureDeploymentListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DocumentBuilderFactory
name|dbf
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
specifier|public
name|FeaturesService
name|getFeaturesService
parameter_list|()
block|{
return|return
name|featuresService
return|;
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|bundleChanged
argument_list|(
operator|new
name|BundleEvent
argument_list|(
name|BundleEvent
operator|.
name|INSTALLED
argument_list|,
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|File
name|artifact
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|artifact
operator|.
name|isFile
argument_list|()
operator|&&
name|artifact
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
name|parse
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
operator|(
name|uri
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to parse deployed file "
operator|+
name|artifact
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|File
name|transform
parameter_list|(
name|File
name|artifact
parameter_list|,
name|File
name|tmpDir
parameter_list|)
block|{
comment|// We can't really install the feature right now and just return nothing.
comment|// We would not be aware of the fact that the bundle has been uninstalled
comment|// and therefore require the feature to be uninstalled.
comment|// So instead, create a fake bundle with the file inside, which will be listened by
comment|// this deployer: installation / uninstallation of the feature will be done
comment|// while the bundle is installed / uninstalled.
try|try
block|{
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
operator|+
literal|".jar"
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|destFile
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|artifact
operator|.
name|getCanonicalPath
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|str
init|=
name|extractNameVersionType
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// Create manifest
name|Manifest
name|m
init|=
operator|new
name|Manifest
argument_list|()
decl_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_MANIFESTVERSION
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|str
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|m
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|putValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
name|str
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
comment|// Put content
name|JarOutputStream
name|out
init|=
operator|new
name|JarOutputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|ZipEntry
name|e
init|=
operator|new
name|ZipEntry
argument_list|(
name|JarFile
operator|.
name|MANIFEST_NAME
argument_list|)
decl_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|m
operator|.
name|write
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"META-INF/"
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"META-INF/"
operator|+
name|FEATURE_PATH
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|e
operator|=
operator|new
name|ZipEntry
argument_list|(
literal|"META-INF/"
operator|+
name|FEATURE_PATH
operator|+
literal|"/"
operator|+
name|name
argument_list|)
expr_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|InputStream
name|fis
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
name|copyInputStream
argument_list|(
name|fis
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|destFile
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to build spring application bundle"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|bundleEvent
parameter_list|)
block|{
try|try
block|{
name|Bundle
name|bundle
init|=
name|bundleEvent
operator|.
name|getBundle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundleEvent
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|INSTALLED
condition|)
block|{
name|Enumeration
name|featuresUrlEnumeration
init|=
name|bundle
operator|.
name|findEntries
argument_list|(
literal|"/META-INF/"
operator|+
name|FEATURE_PATH
operator|+
literal|"/"
argument_list|,
literal|"*.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
while|while
condition|(
name|featuresUrlEnumeration
operator|!=
literal|null
operator|&&
name|featuresUrlEnumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|featuresUrlEnumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|featuresService
operator|.
name|addRepository
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
if|if
condition|(
name|repo
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|Feature
name|f
range|:
name|repo
operator|.
name|getFeatures
argument_list|()
control|)
block|{
try|try
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to install feature: "
operator|+
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|bundleEvent
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNINSTALLED
condition|)
block|{
name|Enumeration
name|featuresUrlEnumeration
init|=
name|bundle
operator|.
name|findEntries
argument_list|(
literal|"/META-INF/"
operator|+
name|FEATURE_PATH
operator|+
literal|"/"
argument_list|,
literal|"*.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
while|while
condition|(
name|featuresUrlEnumeration
operator|!=
literal|null
operator|&&
name|featuresUrlEnumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|featuresUrlEnumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
if|if
condition|(
name|repo
operator|.
name|getURI
argument_list|()
operator|.
name|equals
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|Feature
name|f
range|:
name|repo
operator|.
name|getFeatures
argument_list|()
control|)
block|{
try|try
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to uninstall feature: "
operator|+
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Unable to install / uninstall feature"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Document
name|parse
parameter_list|(
name|File
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|dbf
operator|==
literal|null
condition|)
block|{
name|dbf
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|db
operator|.
name|parse
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|copyInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|len
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
name|len
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|len
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_VERSION
init|=
literal|"0.0.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|ARTIFACT_MATCHER
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(.+)(?:-(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?(?:[^a-zA-Z0-9](.*))?)(?:\\.([^\\.]+))"
argument_list|,
name|Pattern
operator|.
name|DOTALL
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|FUZZY_MODIFIDER
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(?:\\d+[.-])*(.*)"
argument_list|,
name|Pattern
operator|.
name|DOTALL
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
index|[]
name|extractNameVersionType
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|ARTIFACT_MATCHER
operator|.
name|matcher
argument_list|(
name|url
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
operator|new
name|String
index|[]
block|{
name|url
block|,
name|DEFAULT_VERSION
block|}
return|;
block|}
else|else
block|{
comment|//System.err.println(m.groupCount());
comment|//for (int i = 1; i<= m.groupCount(); i++) {
comment|//    System.err.println("Group " + i + ": " + m.group(i));
comment|//}
name|StringBuffer
name|v
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|d1
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|d2
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|String
name|d3
init|=
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|String
name|d4
init|=
name|m
operator|.
name|group
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|String
name|d5
init|=
name|m
operator|.
name|group
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|String
name|d6
init|=
name|m
operator|.
name|group
argument_list|(
literal|6
argument_list|)
decl_stmt|;
if|if
condition|(
name|d2
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
name|d2
argument_list|)
expr_stmt|;
if|if
condition|(
name|d3
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|v
operator|.
name|append
argument_list|(
name|d3
argument_list|)
expr_stmt|;
if|if
condition|(
name|d4
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|v
operator|.
name|append
argument_list|(
name|d4
argument_list|)
expr_stmt|;
if|if
condition|(
name|d5
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|cleanupModifier
argument_list|(
name|v
argument_list|,
name|d5
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|d5
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|".0."
argument_list|)
expr_stmt|;
name|cleanupModifier
argument_list|(
name|v
argument_list|,
name|d5
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|d5
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|".0.0."
argument_list|)
expr_stmt|;
name|cleanupModifier
argument_list|(
name|v
argument_list|,
name|d5
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|String
index|[]
block|{
name|d1
block|,
name|v
operator|.
name|toString
argument_list|()
block|,
name|d6
block|}
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|cleanupModifier
parameter_list|(
name|StringBuffer
name|result
parameter_list|,
name|String
name|modifier
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|FUZZY_MODIFIDER
operator|.
name|matcher
argument_list|(
name|modifier
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|modifier
operator|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|modifier
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|modifier
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|c
operator|>=
literal|'0'
operator|&&
name|c
operator|<=
literal|'9'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'z'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|)
operator|||
name|c
operator|==
literal|'_'
operator|||
name|c
operator|==
literal|'-'
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

