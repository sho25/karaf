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
name|packages
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|packages
operator|.
name|core
operator|.
name|PackageService
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
name|packages
operator|.
name|core
operator|.
name|PackageVersion
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
name|Action
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
name|table
operator|.
name|Col
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
name|Version
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
name|wiring
operator|.
name|BundleCapability
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
name|wiring
operator|.
name|BundleRevision
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"package"
argument_list|,
name|name
operator|=
literal|"exports"
argument_list|,
name|description
operator|=
literal|"Lists exported packages and the bundles that export them"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Exports
implements|implements
name|Action
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|description
operator|=
literal|"Only show packages that are exported by more than one bundle"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|boolean
name|onlyDuplicates
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-format"
argument_list|,
name|description
operator|=
literal|"Disable table rendered output"
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
name|noFormat
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|description
operator|=
literal|"Only show packages exported by given bundle id"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|Integer
name|bundleId
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-p"
argument_list|,
name|description
operator|=
literal|"Only show package starting with given name"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|packageFilter
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|PackageService
name|packageService
decl_stmt|;
annotation|@
name|Reference
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|onlyDuplicates
condition|)
block|{
name|checkDuplicateExports
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|showExports
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|showExports
parameter_list|()
block|{
name|List
argument_list|<
name|PackageVersion
argument_list|>
name|exports
init|=
name|packageService
operator|.
name|getExports
argument_list|()
decl_stmt|;
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
literal|"Package Name"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Bundle Name"
argument_list|)
expr_stmt|;
for|for
control|(
name|PackageVersion
name|pVer
range|:
name|exports
control|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|pVer
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|matchesFilter
argument_list|(
name|pVer
argument_list|,
name|bundle
argument_list|)
condition|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|pVer
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|pVer
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|!
name|noFormat
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|matchesFilter
parameter_list|(
name|PackageVersion
name|pVer
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
return|return
operator|(
name|bundleId
operator|==
literal|null
operator|||
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|==
name|bundleId
operator|)
operator|&&
operator|(
name|packageFilter
operator|==
literal|null
operator|||
name|pVer
operator|.
name|getPackageName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|packageFilter
argument_list|)
operator|)
return|;
block|}
specifier|private
name|void
name|checkDuplicateExports
parameter_list|()
block|{
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PackageVersion
argument_list|>
name|packageVersionMap
init|=
name|getDuplicatePackages
argument_list|(
name|bundles
argument_list|)
decl_stmt|;
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
operator|new
name|Col
argument_list|(
literal|"Package Name"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Version"
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Exporting bundles (ID)"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|packageVersionMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|PackageVersion
name|pVer
init|=
name|packageVersionMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|pVer
operator|.
name|getBundles
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|String
name|pBundles
init|=
name|getBundlesSt
argument_list|(
name|pVer
operator|.
name|getBundles
argument_list|()
argument_list|)
decl_stmt|;
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|pVer
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|pVer
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|pBundles
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|!
name|noFormat
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getBundlesSt
parameter_list|(
name|Set
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
block|{
name|StringBuilder
name|st
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|st
operator|.
name|append
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
return|return
name|st
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PackageVersion
argument_list|>
name|getDuplicatePackages
parameter_list|(
name|Bundle
index|[]
name|bundles
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PackageVersion
argument_list|>
name|packageVersionMap
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|PackageVersion
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|BundleRevision
name|rev
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rev
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|caps
init|=
name|rev
operator|.
name|getDeclaredCapabilities
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attr
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|attr
operator|.
name|get
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
name|Version
name|version
init|=
operator|(
name|Version
operator|)
name|attr
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|packageName
operator|+
literal|":"
operator|+
name|version
operator|.
name|toString
argument_list|()
decl_stmt|;
name|PackageVersion
name|pVer
init|=
name|packageVersionMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|pVer
operator|==
literal|null
condition|)
block|{
name|pVer
operator|=
operator|new
name|PackageVersion
argument_list|(
name|packageName
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|packageVersionMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|pVer
argument_list|)
expr_stmt|;
block|}
name|pVer
operator|.
name|addBundle
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|packageVersionMap
return|;
block|}
block|}
end_class

end_unit

