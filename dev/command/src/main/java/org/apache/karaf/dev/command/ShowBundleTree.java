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
name|dev
operator|.
name|command
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
name|dev
operator|.
name|command
operator|.
name|bundletree
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
name|dev
operator|.
name|command
operator|.
name|bundletree
operator|.
name|Tree
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
name|commands
operator|.
name|Command
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
name|BundleRevisions
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
name|BundleWire
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
name|BundleWiring
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
literal|"tree-show"
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
comment|/**      * Return a String representation of a bundle state      */
specifier|private
name|String
name|getState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
switch|switch
condition|(
name|bundle
operator|.
name|getState
argument_list|()
condition|)
block|{
case|case
name|Bundle
operator|.
name|UNINSTALLED
case|:
return|return
literal|"UNINSTALLED"
return|;
case|case
name|Bundle
operator|.
name|INSTALLED
case|:
return|return
literal|"INSTALLED"
return|;
case|case
name|Bundle
operator|.
name|RESOLVED
case|:
return|return
literal|"RESOLVED"
return|;
case|case
name|Bundle
operator|.
name|STARTING
case|:
return|return
literal|"STARTING"
return|;
case|case
name|Bundle
operator|.
name|STOPPING
case|:
return|return
literal|"STOPPING"
return|;
case|case
name|Bundle
operator|.
name|ACTIVE
case|:
return|return
literal|"ACTIVE"
return|;
default|default :
return|return
literal|"UNKNOWN"
return|;
block|}
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
name|getState
argument_list|(
name|bundle
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
for|for
control|(
name|BundleRevision
name|revision
range|:
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevisions
operator|.
name|class
argument_list|)
operator|.
name|getRevisions
argument_list|()
control|)
block|{
name|BundleWiring
name|wiring
init|=
name|revision
operator|.
name|getWiring
argument_list|()
decl_stmt|;
if|if
condition|(
name|wiring
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|BundleWire
argument_list|>
name|wires
init|=
name|wiring
operator|.
name|getProvidedWires
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|wires
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BundleWire
name|wire
range|:
name|wires
control|)
block|{
name|String
name|name
init|=
name|wire
operator|.
name|getCapability
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|exports
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exports
operator|.
name|put
argument_list|(
name|name
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
name|name
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
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"\nWarning: the below tree is a rough approximation of a possible resolution"
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
argument_list|<
name|Bundle
argument_list|>
name|node
parameter_list|,
name|Bundle
name|bundle
parameter_list|)
block|{
name|Clause
index|[]
name|imports
init|=
name|Parser
operator|.
name|parseHeader
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
decl_stmt|;
name|Clause
index|[]
name|exports
init|=
name|Parser
operator|.
name|parseHeader
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
decl_stmt|;
for|for
control|(
name|Clause
name|i
range|:
name|imports
control|)
block|{
name|boolean
name|exported
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Clause
name|e
range|:
name|exports
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|i
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|exported
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|exported
condition|)
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
block|}
comment|/*      * Create a child node for a given import (by finding a matching export in the currently installed bundles)      */
specifier|private
name|void
name|createNodeForImport
parameter_list|(
name|Node
argument_list|<
name|Bundle
argument_list|>
name|node
parameter_list|,
name|Bundle
name|bundle
parameter_list|,
name|Clause
name|i
parameter_list|)
block|{
name|VersionRange
name|range
init|=
name|VersionRange
operator|.
name|parseVersionRange
argument_list|(
name|i
operator|.
name|getAttribute
argument_list|(
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|foundMatch
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|BundleWiring
name|wiring
init|=
name|b
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wiring
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
name|wiring
operator|.
name|getCapabilities
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|caps
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
name|String
name|n
init|=
name|getAttribute
argument_list|(
name|cap
argument_list|,
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
name|String
name|v
init|=
name|getAttribute
argument_list|(
name|cap
argument_list|,
name|Constants
operator|.
name|VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|n
argument_list|)
operator|&&
name|range
operator|.
name|contains
argument_list|(
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|v
argument_list|)
argument_list|)
condition|)
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
name|b
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
name|b
argument_list|)
expr_stmt|;
name|foundMatch
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|hasChild
argument_list|(
name|b
argument_list|)
condition|)
block|{
name|Node
argument_list|<
name|Bundle
argument_list|>
name|child
init|=
name|node
operator|.
name|addChild
argument_list|(
name|b
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|existing
condition|)
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
specifier|private
name|String
name|getAttribute
parameter_list|(
name|BundleCapability
name|capability
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|o
init|=
name|capability
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|o
operator|!=
literal|null
condition|?
name|o
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
comment|/*     * Creates a node in the bundle tree     */
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
name|devService
operator|.
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
argument_list|<
name|Bundle
argument_list|>
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

