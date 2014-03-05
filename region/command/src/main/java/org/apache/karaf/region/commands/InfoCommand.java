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
name|region
operator|.
name|commands
package|;
end_package

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
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|Region
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionFilter
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

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"region"
argument_list|,
name|name
operator|=
literal|"info"
argument_list|,
name|description
operator|=
literal|"Prints information about region digraph."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|InfoCommand
extends|extends
name|RegionCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-v"
argument_list|,
name|aliases
operator|=
literal|"--verbose"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Show all info."
argument_list|)
name|boolean
name|verbose
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-b"
argument_list|,
name|aliases
operator|=
literal|"--bundles"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Show bundles in each region."
argument_list|)
name|boolean
name|bundles
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-f"
argument_list|,
name|aliases
operator|=
literal|"--filters"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Show filters."
argument_list|)
name|boolean
name|filters
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-n"
argument_list|,
name|aliases
operator|=
literal|"--namespaces"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|description
operator|=
literal|"Show namespaces in each filter."
argument_list|)
name|boolean
name|namespaces
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"regions"
argument_list|,
name|description
operator|=
literal|"Regions to provide detailed info for."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|regions
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|RegionDigraph
name|regionDigraph
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Regions"
argument_list|)
expr_stmt|;
if|if
condition|(
name|regions
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Region
name|region
range|:
name|regionDigraph
operator|.
name|getRegions
argument_list|()
control|)
block|{
name|showRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|bundles
operator|=
literal|true
expr_stmt|;
name|filters
operator|=
literal|true
expr_stmt|;
name|namespaces
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|String
name|regionName
range|:
name|regions
control|)
block|{
name|Region
name|region
init|=
name|regionDigraph
operator|.
name|getRegion
argument_list|(
name|regionName
argument_list|)
decl_stmt|;
if|if
condition|(
name|region
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No region "
operator|+
name|regionName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|showRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|showRegion
parameter_list|(
name|Region
name|region
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|region
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
operator|||
name|bundles
condition|)
block|{
for|for
control|(
name|Long
name|id
range|:
name|region
operator|.
name|getBundleIds
argument_list|()
control|)
block|{
name|Bundle
name|b
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|id
operator|+
literal|"  "
operator|+
name|getStateString
argument_list|(
name|b
argument_list|)
operator|+
name|b
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|verbose
operator|||
name|filters
operator|||
name|namespaces
condition|)
block|{
for|for
control|(
name|RegionDigraph
operator|.
name|FilteredRegion
name|f
range|:
name|region
operator|.
name|getEdges
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  filter to "
operator|+
name|f
operator|.
name|getRegion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|verbose
operator|||
name|namespaces
condition|)
block|{
name|RegionFilter
name|rf
init|=
name|f
operator|.
name|getFilter
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|policy
range|:
name|rf
operator|.
name|getSharingPolicy
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|namespace
init|=
name|policy
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  namespace: "
operator|+
name|namespace
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|e
range|:
name|policy
operator|.
name|getValue
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|public
name|String
name|getStateString
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
if|if
condition|(
name|bundle
operator|==
literal|null
condition|)
block|{
return|return
literal|"Bundle null"
return|;
block|}
name|int
name|state
init|=
name|bundle
operator|.
name|getState
argument_list|()
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
return|return
literal|"Active     "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|INSTALLED
condition|)
block|{
return|return
literal|"Installed  "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|RESOLVED
condition|)
block|{
return|return
literal|"Resolved   "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|STARTING
condition|)
block|{
return|return
literal|"Starting   "
return|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|Bundle
operator|.
name|STOPPING
condition|)
block|{
return|return
literal|"Stopping   "
return|;
block|}
else|else
block|{
return|return
literal|"Unknown    "
return|;
block|}
block|}
block|}
end_class

end_unit

