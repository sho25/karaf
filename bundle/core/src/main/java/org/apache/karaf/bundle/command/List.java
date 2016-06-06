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
name|bundle
operator|.
name|command
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
name|bundle
operator|.
name|core
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
name|bundle
operator|.
name|core
operator|.
name|BundleService
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
name|bundle
operator|.
name|core
operator|.
name|BundleState
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
name|api
operator|.
name|console
operator|.
name|Terminal
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
name|startlevel
operator|.
name|FrameworkStartLevel
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"list"
argument_list|,
name|description
operator|=
literal|"Lists all installed bundles."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|List
extends|extends
name|BundlesCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-name"
argument_list|,
name|description
operator|=
literal|"Don't show bundle name"
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
name|dontShowName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-l"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Show the locations"
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
name|showLocation
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-s"
argument_list|,
name|description
operator|=
literal|"Shows the symbolic name"
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
name|showSymbolic
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-u"
argument_list|,
name|description
operator|=
literal|"Shows the update locations"
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
name|showUpdate
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-r"
argument_list|,
name|description
operator|=
literal|"Shows the bundle revisions"
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
name|showRevisions
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-t"
argument_list|,
name|valueToShowInHelp
operator|=
literal|""
argument_list|,
name|description
operator|=
literal|"Specifies the bundle threshold; bundles with a start-level less than this value will not get printed out."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|int
name|bundleLevelThreshold
init|=
operator|-
literal|1
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
literal|"--no-ellipsis"
argument_list|)
name|boolean
name|noEllipsis
decl_stmt|;
annotation|@
name|Reference
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Reference
name|BundleService
name|bundleService
decl_stmt|;
annotation|@
name|Reference
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
name|Terminal
name|terminal
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|executeOnBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|noFormat
condition|)
block|{
name|noEllipsis
operator|=
literal|true
expr_stmt|;
block|}
name|determineBundleLevelThreshold
argument_list|()
expr_stmt|;
comment|// Display active start level.
name|FrameworkStartLevel
name|fsl
init|=
name|this
operator|.
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|fsl
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"START LEVEL "
operator|+
name|fsl
operator|.
name|getStartLevel
argument_list|()
operator|+
literal|" , List Threshold: "
operator|+
name|bundleLevelThreshold
argument_list|)
expr_stmt|;
block|}
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|noEllipsis
operator|&&
name|showLocation
operator|&&
name|terminal
operator|!=
literal|null
operator|&&
name|terminal
operator|.
name|getWidth
argument_list|()
operator|>
literal|0
condition|)
block|{
name|table
operator|.
name|size
argument_list|(
name|terminal
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|column
argument_list|(
literal|"ID"
argument_list|)
operator|.
name|alignRight
argument_list|()
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"State"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Lvl"
argument_list|)
operator|.
name|alignRight
argument_list|()
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Version"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dontShowName
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showLocation
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
operator|new
name|Col
argument_list|(
literal|"Location"
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|String
name|cut
parameter_list|(
name|String
name|value
parameter_list|,
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|showLocation
operator|&&
name|value
operator|.
name|length
argument_list|()
operator|>
name|size
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|value
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|cut
init|=
literal|""
decl_stmt|;
name|int
name|c
init|=
name|parts
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
operator|+
literal|4
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
name|parts
operator|.
name|length
operator|-
literal|1
init|;
name|idx
operator|>
literal|0
condition|;
name|idx
operator|--
control|)
block|{
if|if
condition|(
name|cut
operator|.
name|length
argument_list|()
operator|+
name|c
operator|+
name|parts
index|[
name|idx
index|]
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|<
name|size
condition|)
block|{
name|cut
operator|=
literal|"/"
operator|+
name|parts
index|[
name|idx
index|]
operator|+
name|cut
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|cut
operator|=
name|parts
index|[
literal|0
index|]
operator|+
literal|"/..."
operator|+
name|cut
expr_stmt|;
return|return
name|cut
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|cut
argument_list|(
name|value
argument_list|,
name|size
argument_list|)
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Symbolic name"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showUpdate
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Update location"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showRevisions
condition|)
block|{
name|table
operator|.
name|column
argument_list|(
literal|"Revisions"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|BundleInfo
name|info
init|=
name|this
operator|.
name|bundleService
operator|.
name|getInfo
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getStartLevel
argument_list|()
operator|>=
name|bundleLevelThreshold
condition|)
block|{
name|String
name|version
init|=
name|info
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|Object
argument_list|>
name|rowData
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|rowData
operator|.
name|add
argument_list|(
name|info
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|rowData
operator|.
name|add
argument_list|(
name|getStateString
argument_list|(
name|info
operator|.
name|getState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|rowData
operator|.
name|add
argument_list|(
name|info
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
name|rowData
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dontShowName
condition|)
block|{
name|String
name|bundleName
init|=
operator|(
name|info
operator|.
name|getName
argument_list|()
operator|==
literal|null
operator|)
condition|?
name|info
operator|.
name|getSymbolicName
argument_list|()
else|:
name|info
operator|.
name|getName
argument_list|()
decl_stmt|;
name|bundleName
operator|=
operator|(
name|bundleName
operator|==
literal|null
operator|)
condition|?
name|info
operator|.
name|getUpdateLocation
argument_list|()
else|:
name|bundleName
expr_stmt|;
name|String
name|name
init|=
name|bundleName
operator|+
name|printFragments
argument_list|(
name|info
argument_list|)
operator|+
name|printHosts
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|rowData
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showLocation
condition|)
block|{
name|rowData
operator|.
name|add
argument_list|(
name|info
operator|.
name|getUpdateLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showSymbolic
condition|)
block|{
name|rowData
operator|.
name|add
argument_list|(
name|info
operator|.
name|getSymbolicName
argument_list|()
operator|==
literal|null
condition|?
literal|"<no symbolic name>"
else|:
name|info
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showUpdate
condition|)
block|{
name|rowData
operator|.
name|add
argument_list|(
name|info
operator|.
name|getUpdateLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showRevisions
condition|)
block|{
name|rowData
operator|.
name|add
argument_list|(
name|info
operator|.
name|getRevisions
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|rowData
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
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|determineBundleLevelThreshold
parameter_list|()
block|{
if|if
condition|(
name|bundleLevelThreshold
operator|<
literal|0
condition|)
block|{
name|bundleLevelThreshold
operator|=
name|bundleService
operator|.
name|getSystemBundleThreshold
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|printHosts
parameter_list|(
name|BundleInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|getFragmentHosts
argument_list|()
operator|.
name|size
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", Hosts: "
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Bundle
name|host
range|:
name|info
operator|.
name|getFragmentHosts
argument_list|()
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
operator|(
name|first
condition|?
literal|""
else|:
literal|", "
operator|)
operator|+
name|host
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|printFragments
parameter_list|(
name|BundleInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|info
operator|.
name|getFragments
argument_list|()
operator|.
name|size
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", Fragments: "
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Bundle
name|host
range|:
name|info
operator|.
name|getFragments
argument_list|()
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
operator|(
name|first
condition|?
literal|""
else|:
literal|", "
operator|)
operator|+
name|host
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getStateString
parameter_list|(
name|BundleState
name|state
parameter_list|)
block|{
return|return
operator|(
name|state
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|state
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

