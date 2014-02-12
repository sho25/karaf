begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

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
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|InputStreamReader
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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
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
name|manifest
operator|.
name|Clause
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
name|manifest
operator|.
name|Parser
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
name|felix
operator|.
name|utils
operator|.
name|version
operator|.
name|VersionTable
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
name|internal
operator|.
name|model
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
name|Version
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
comment|/**  * Helper class to deal with overriden bundles at feature installation time.  \*/
end_comment

begin_class
specifier|public
class|class
name|Overrides
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Overrides
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OVERRIDE_RANGE
init|=
literal|"range"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VENDOR_WARNING
init|=
literal|"Bundle Vendor has changed, please check if this is intentional. Bundle: "
decl_stmt|;
comment|/**      * Compute a list of bundles to install, taking into account overrides.      *      * The file containing the overrides will be loaded from the given url.      * Blank lines and lines starting with a '#' will be ignored, all other lines      * are considered as urls to override bundles.      *      * The list of bundles to install will be scanned and for each bundle,      * if a bundle override matches that bundle, it will be used instead.      *      * Matching is done on bundle symbolic name (they have to be the same)      * and version (the bundle override version needs to be greater than the      * bundle to be installed, and less than the next minor version.  A range      * directive can be added to the override url in which case, the matching      * will succeed if the bundle to be installed is within the given range.      *      * @param infos the list of bundles to install      * @param overridesUrl url pointing to the file containing the list of override bundles      * @return a new list of bundles to install      */
specifier|public
specifier|static
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|override
parameter_list|(
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|infos
parameter_list|,
name|String
name|overridesUrl
parameter_list|)
block|{
name|List
argument_list|<
name|Clause
argument_list|>
name|overrides
init|=
name|loadOverrides
argument_list|(
name|overridesUrl
argument_list|)
decl_stmt|;
if|if
condition|(
name|overrides
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|infos
return|;
block|}
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Manifest
argument_list|>
name|manifests
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Manifest
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Clause
name|override
range|:
name|overrides
control|)
block|{
name|Manifest
name|manifest
init|=
name|getManifest
argument_list|(
name|override
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|manifests
operator|.
name|put
argument_list|(
name|override
operator|.
name|getName
argument_list|()
argument_list|,
name|manifest
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|newInfos
init|=
operator|new
name|ArrayList
argument_list|<
name|BundleInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleInfo
name|info
range|:
name|infos
control|)
block|{
name|Manifest
name|manifest
init|=
name|getManifest
argument_list|(
name|info
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
name|String
name|bsn
init|=
name|getBundleSymbolicName
argument_list|(
name|manifest
argument_list|)
decl_stmt|;
name|Version
name|ver
init|=
name|getBundleVersion
argument_list|(
name|manifest
argument_list|)
decl_stmt|;
name|String
name|ven
init|=
name|getBundleVendor
argument_list|(
name|manifest
argument_list|)
decl_stmt|;
name|String
name|url
init|=
name|info
operator|.
name|getLocation
argument_list|()
decl_stmt|;
for|for
control|(
name|Clause
name|override
range|:
name|overrides
control|)
block|{
name|Manifest
name|overMan
init|=
name|manifests
operator|.
name|get
argument_list|(
name|override
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|overMan
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|oBsn
init|=
name|getBundleSymbolicName
argument_list|(
name|overMan
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|bsn
operator|.
name|equals
argument_list|(
name|oBsn
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Version
name|oVer
init|=
name|getBundleVersion
argument_list|(
name|overMan
argument_list|)
decl_stmt|;
name|VersionRange
name|range
decl_stmt|;
name|String
name|vr
init|=
name|extractVersionRange
argument_list|(
name|override
argument_list|)
decl_stmt|;
if|if
condition|(
name|vr
operator|==
literal|null
condition|)
block|{
comment|// default to micro version compatibility
name|Version
name|v2
init|=
operator|new
name|Version
argument_list|(
name|oVer
operator|.
name|getMajor
argument_list|()
argument_list|,
name|oVer
operator|.
name|getMinor
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|v2
operator|.
name|equals
argument_list|(
name|oVer
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|range
operator|=
operator|new
name|VersionRange
argument_list|(
literal|false
argument_list|,
name|v2
argument_list|,
name|oVer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|range
operator|=
name|VersionRange
operator|.
name|parseVersionRange
argument_list|(
name|vr
argument_list|)
expr_stmt|;
block|}
name|String
name|vendor
init|=
name|getBundleVendor
argument_list|(
name|overMan
argument_list|)
decl_stmt|;
comment|// Before we do a replace, lets check if vendors change
if|if
condition|(
name|ven
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|vendor
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
name|VENDOR_WARNING
operator|+
name|bsn
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|vendor
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
name|VENDOR_WARNING
operator|+
name|bsn
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|vendor
operator|.
name|equals
argument_list|(
name|ven
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
name|VENDOR_WARNING
operator|+
name|bsn
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// The resource matches, so replace it with the overridden resource
comment|// if the override is actually a newer version than what we currently have
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|ver
argument_list|)
operator|&&
name|ver
operator|.
name|compareTo
argument_list|(
name|oVer
argument_list|)
operator|<
literal|0
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Overriding original bundle "
operator|+
name|url
operator|+
literal|" to "
operator|+
name|override
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ver
operator|=
name|oVer
expr_stmt|;
name|url
operator|=
name|override
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|info
operator|.
name|getLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|Bundle
name|b
init|=
operator|new
name|Bundle
argument_list|()
decl_stmt|;
name|b
operator|.
name|setLocation
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|b
operator|.
name|setStartLevel
argument_list|(
name|info
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|setStart
argument_list|(
name|info
operator|.
name|isStart
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|setDependency
argument_list|(
name|info
operator|.
name|isDependency
argument_list|()
argument_list|)
expr_stmt|;
name|newInfos
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newInfos
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|newInfos
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|newInfos
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
name|info
argument_list|(
literal|"Unable to process bundle overrides"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|infos
return|;
block|}
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Clause
argument_list|>
name|loadOverrides
parameter_list|(
name|String
name|overridesUrl
parameter_list|)
block|{
name|List
argument_list|<
name|Clause
argument_list|>
name|overrides
init|=
operator|new
name|ArrayList
argument_list|<
name|Clause
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|overridesUrl
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|overridesUrl
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|Clause
index|[]
name|cs
init|=
name|Parser
operator|.
name|parseHeader
argument_list|(
name|line
argument_list|)
decl_stmt|;
for|for
control|(
name|Clause
name|c
range|:
name|cs
control|)
block|{
name|overrides
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
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
name|debug
argument_list|(
literal|"Unable to load overrides bundles list"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|overrides
return|;
block|}
specifier|private
specifier|static
name|Version
name|getBundleVersion
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|String
name|ver
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
decl_stmt|;
return|return
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|ver
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|getBundleSymbolicName
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|String
name|bsn
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|)
decl_stmt|;
name|bsn
operator|=
name|stripSymbolicName
argument_list|(
name|bsn
argument_list|)
expr_stmt|;
return|return
name|bsn
return|;
block|}
specifier|private
specifier|static
name|String
name|getBundleVendor
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
block|{
name|String
name|ven
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|Constants
operator|.
name|BUNDLE_VENDOR
argument_list|)
decl_stmt|;
return|return
name|ven
return|;
block|}
specifier|private
specifier|static
name|Manifest
name|getManifest
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|ZipInputStream
name|zis
init|=
operator|new
name|ZipInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|ZipEntry
name|entry
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|zis
operator|.
name|getNextEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"META-INF/MANIFEST.MF"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|Manifest
argument_list|(
name|zis
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|extractVersionRange
parameter_list|(
name|Clause
name|override
parameter_list|)
block|{
return|return
name|override
operator|.
name|getAttribute
argument_list|(
name|OVERRIDE_RANGE
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|stripSymbolicName
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
name|Clause
index|[]
name|cs
init|=
name|Parser
operator|.
name|parseHeader
argument_list|(
name|symbolicName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|==
literal|null
operator|||
name|cs
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad Bundle-SymbolicName header: "
operator|+
name|symbolicName
argument_list|)
throw|;
block|}
return|return
name|cs
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

