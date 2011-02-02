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
name|shell
operator|.
name|dev
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|LinkedHashSet
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|console
operator|.
name|OsgiCommandSupport
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
name|dev
operator|.
name|util
operator|.
name|Bundles
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
name|dev
operator|.
name|util
operator|.
name|Import
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
name|dev
operator|.
name|util
operator|.
name|Node
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
name|dev
operator|.
name|util
operator|.
name|Tree
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|packageadmin
operator|.
name|ExportedPackage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|packageadmin
operator|.
name|PackageAdmin
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
comment|/**  * Command for showing the full tree of bundles that have been used to resolve  * a given bundle.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"dev"
argument_list|,
name|name
operator|=
literal|"show-tree"
argument_list|,
name|description
operator|=
literal|"Shows the tree of bundles based on the wiring information."
argument_list|)
specifier|public
class|class
name|ShowBundleTree
extends|extends
name|AbstractBundleCommand
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
name|ShowBundleTree
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Tree
argument_list|<
name|Bundle
argument_list|>
name|tree
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doExecute
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// let's do the real work here
name|printHeader
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|tree
operator|=
operator|new
name|Tree
argument_list|<
name|Bundle
argument_list|>
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|createTree
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
name|printTree
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|printDuplicatePackages
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Dependency tree calculated in %d ms"
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Print the header      */
specifier|private
name|void
name|printHeader
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"Bundle %s [%s] is currently %s%n"
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|Bundles
operator|.
name|toString
argument_list|(
name|bundle
operator|.
name|getState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Print the dependency tree      */
specifier|private
name|void
name|printTree
parameter_list|(
name|Tree
argument_list|<
name|Bundle
argument_list|>
name|tree
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"%n"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|write
argument_list|(
name|System
operator|.
name|out
argument_list|,
operator|new
name|Tree
operator|.
name|Converter
argument_list|<
name|Bundle
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|toString
parameter_list|(
name|Node
argument_list|<
name|Bundle
argument_list|>
name|node
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s [%s]"
argument_list|,
name|node
operator|.
name|getValue
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|node
operator|.
name|getValue
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/*      * Check for bundles in the tree exporting the same package      * as a possible cause for 'Unresolved constraint...' on a uses-conflict      */
specifier|private
name|void
name|printDuplicatePackages
parameter_list|(
name|Tree
argument_list|<
name|Bundle
argument_list|>
name|tree
parameter_list|)
block|{
name|Set
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|tree
operator|.
name|flatten
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Bundle
argument_list|>
argument_list|>
name|exports
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Bundle
argument_list|>
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
name|ExportedPackage
index|[]
name|packages
init|=
name|getPackageAdmin
argument_list|()
operator|.
name|getExportedPackages
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|packages
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ExportedPackage
name|p
range|:
name|packages
control|)
block|{
if|if
condition|(
name|exports
operator|.
name|get
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exports
operator|.
name|put
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|<
name|Bundle
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exports
operator|.
name|get
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|String
name|pkg
range|:
name|exports
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|exports
operator|.
name|get
argument_list|(
name|pkg
argument_list|)
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"%n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"WARNING: multiple bundles are exporting package %s%n"
argument_list|,
name|pkg
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|exports
operator|.
name|get
argument_list|(
name|pkg
argument_list|)
control|)
block|{
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"- %s%n"
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/*      * Creates the bundle tree      */
specifier|protected
name|void
name|createTree
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
if|if
condition|(
name|bundle
operator|.
name|getState
argument_list|()
operator|>=
name|Bundle
operator|.
name|RESOLVED
condition|)
block|{
name|createNode
argument_list|(
name|tree
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createNodesForImports
argument_list|(
name|tree
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Creates nodes for the imports of the bundle (instead of reporting wiring information      */
specifier|private
name|void
name|createNodesForImports
parameter_list|(
name|Node
name|node
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
for|for
control|(
name|Import
name|i
range|:
name|Import
operator|.
name|parse
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"Import-Package"
argument_list|)
argument_list|)
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"Export-Package"
argument_list|)
argument_list|)
argument_list|)
control|)
block|{
name|createNodeForImport
argument_list|(
name|node
argument_list|,
name|bundle
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Create a child node for a given import (by finding a matching export in the currently installed bundles)      */
specifier|private
name|void
name|createNodeForImport
parameter_list|(
name|Node
name|node
parameter_list|,
name|Bundle
name|bundle
parameter_list|,
name|Import
name|i
parameter_list|)
block|{
name|ExportedPackage
index|[]
name|exporters
init|=
name|getPackageAdmin
argument_list|()
operator|.
name|getExportedPackages
argument_list|(
name|i
operator|.
name|getPackage
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|foundMatch
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|exporters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ExportedPackage
name|ep
range|:
name|exporters
control|)
block|{
if|if
condition|(
name|i
operator|.
name|getVersion
argument_list|()
operator|.
name|contains
argument_list|(
name|ep
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|bundle
operator|.
name|equals
argument_list|(
name|ep
operator|.
name|getExportingBundle
argument_list|()
argument_list|)
condition|)
block|{
name|foundMatch
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|Node
name|child
init|=
name|node
operator|.
name|addChild
argument_list|(
name|ep
operator|.
name|getExportingBundle
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"- import %s: resolved using %s%n"
argument_list|,
name|i
argument_list|,
name|ep
operator|.
name|getExportingBundle
argument_list|()
argument_list|)
expr_stmt|;
name|foundMatch
operator|=
literal|true
expr_stmt|;
name|createNode
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|foundMatch
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"- import %s: WARNING - unable to find matching export%n"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Creates a node in the bundle tree      */
specifier|private
name|void
name|createNode
parameter_list|(
name|Node
argument_list|<
name|Bundle
argument_list|>
name|node
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|node
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Bundle
argument_list|>
name|exporters
init|=
operator|new
name|HashSet
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
name|exporters
operator|.
name|addAll
argument_list|(
name|getWiredBundles
argument_list|(
name|bundle
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|exporter
range|:
name|exporters
control|)
block|{
if|if
condition|(
name|node
operator|.
name|hasAncestor
argument_list|(
name|exporter
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Skipping %s (already exists in the current branch)"
argument_list|,
name|exporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|existing
init|=
name|tree
operator|.
name|flatten
argument_list|()
operator|.
name|contains
argument_list|(
name|exporter
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Adding %s as a dependency for %s"
argument_list|,
name|exporter
argument_list|,
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|child
init|=
name|node
operator|.
name|addChild
argument_list|(
name|exporter
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
name|format
argument_list|(
literal|"Skipping children of %s (already exists in another branch)"
argument_list|,
name|exporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createNode
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

