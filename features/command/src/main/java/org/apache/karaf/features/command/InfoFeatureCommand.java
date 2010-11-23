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
name|features
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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

begin_comment
comment|/**  * Utility command to display info about features.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"features"
argument_list|,
name|name
operator|=
literal|"info"
argument_list|,
name|description
operator|=
literal|"Shows information about selected information."
argument_list|)
specifier|public
class|class
name|InfoFeatureCommand
extends|extends
name|FeaturesCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"The name of the feature"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"version"
argument_list|,
name|description
operator|=
literal|"The version of the feature"
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
name|version
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-c"
argument_list|,
name|aliases
operator|=
block|{
literal|"--configuration"
block|}
argument_list|,
name|description
operator|=
literal|"Display configuration info"
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
name|config
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-d"
argument_list|,
name|aliases
operator|=
block|{
literal|"--dependency"
block|}
argument_list|,
name|description
operator|=
literal|"Display dependencies info"
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
name|dependency
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
block|{
literal|"--bundle"
block|}
argument_list|,
name|description
operator|=
literal|"Display bundles info"
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
name|bundle
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-t"
argument_list|,
name|aliases
operator|=
block|{
literal|"--tree"
block|}
argument_list|,
name|description
operator|=
literal|"Display feature tree"
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
name|tree
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|Feature
name|feature
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
operator|&&
name|version
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|feature
operator|=
name|admin
operator|.
name|getFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|feature
operator|=
name|admin
operator|.
name|getFeature
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|feature
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
literal|"Feature not found"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// default behavior
if|if
condition|(
operator|!
name|config
operator|&&
operator|!
name|dependency
operator|&&
operator|!
name|bundle
condition|)
block|{
name|config
operator|=
literal|true
expr_stmt|;
name|dependency
operator|=
literal|true
expr_stmt|;
name|bundle
operator|=
literal|true
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Description of "
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
operator|+
literal|" feature"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----------------------------------------------------------------"
argument_list|)
expr_stmt|;
if|if
condition|(
name|feature
operator|.
name|getDetails
argument_list|()
operator|!=
literal|null
operator|&&
name|feature
operator|.
name|getDetails
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|feature
operator|.
name|getDetails
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----------------------------------------------------------------"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
condition|)
block|{
name|displayConfigInformation
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dependency
condition|)
block|{
name|displayDependencyInformation
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bundle
condition|)
block|{
name|displayBundleInformation
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tree
condition|)
block|{
if|if
condition|(
name|config
operator|||
name|dependency
operator|||
name|bundle
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nFeature tree"
argument_list|)
expr_stmt|;
block|}
name|int
name|unresolved
init|=
name|displayFeatureTree
argument_list|(
name|admin
argument_list|,
name|feature
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|unresolved
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Tree contains "
operator|+
name|unresolved
operator|+
literal|" unresolved dependencies"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" * means that node declares dependency but the dependant feature is not available."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|displayBundleInformation
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|bundleInfos
init|=
name|feature
operator|.
name|getBundles
argument_list|()
decl_stmt|;
if|if
condition|(
name|bundleInfos
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Feature has no bundles."
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
literal|"Feature contains followed bundles:"
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleInfo
name|featureBundle
range|:
name|bundleInfos
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|featureBundle
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|displayDependencyInformation
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
name|List
argument_list|<
name|Feature
argument_list|>
name|dependencies
init|=
name|feature
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependencies
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Feature has no dependencies."
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
literal|"Feature depends on:"
argument_list|)
expr_stmt|;
for|for
control|(
name|Feature
name|featureDependency
range|:
name|dependencies
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|featureDependency
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|featureDependency
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|displayConfigInformation
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|configurations
init|=
name|feature
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Feature has no configuration"
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
literal|"Feature configuration:"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|configurations
operator|.
name|keySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|int
name|displayFeatureTree
parameter_list|(
name|FeaturesService
name|admin
parameter_list|,
name|Feature
name|feature
parameter_list|,
name|int
name|level
parameter_list|,
name|boolean
name|last
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|unresolved
init|=
literal|0
decl_stmt|;
name|String
name|prefix
init|=
name|repeat
argument_list|(
literal|"   "
argument_list|,
name|level
argument_list|)
decl_stmt|;
name|Feature
name|resolved
init|=
name|resolveFeature
argument_list|(
name|admin
argument_list|,
name|feature
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolved
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
name|prefix
operator|+
literal|" "
operator|+
name|resolved
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|resolved
operator|.
name|getVersion
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
name|prefix
operator|+
literal|" "
operator|+
name|feature
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
operator|+
literal|" *"
argument_list|)
expr_stmt|;
name|unresolved
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|bundle
condition|)
block|{
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|bundles
init|=
name|resolved
operator|!=
literal|null
condition|?
name|resolved
operator|.
name|getBundles
argument_list|()
else|:
name|feature
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
name|bundles
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|j
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|prefix
operator|+
literal|" "
operator|+
operator|(
name|i
operator|+
literal|1
operator|==
name|j
condition|?
literal|"\\"
else|:
literal|"+"
operator|)
operator|+
literal|" "
operator|+
name|bundles
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|Feature
argument_list|>
name|dependencies
init|=
name|resolved
operator|!=
literal|null
condition|?
name|resolved
operator|.
name|getDependencies
argument_list|()
else|:
name|feature
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
name|dependencies
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|j
condition|;
name|i
operator|++
control|)
block|{
name|Feature
name|toDisplay
init|=
name|resolveFeature
argument_list|(
name|admin
argument_list|,
name|dependencies
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|toDisplay
operator|==
literal|null
condition|)
block|{
name|toDisplay
operator|=
name|dependencies
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|unresolved
operator|+=
name|displayFeatureTree
argument_list|(
name|admin
argument_list|,
name|toDisplay
argument_list|,
name|level
operator|+
literal|1
argument_list|,
name|i
operator|+
literal|1
operator|==
name|j
argument_list|)
expr_stmt|;
block|}
return|return
name|unresolved
return|;
block|}
specifier|private
name|Feature
name|resolveFeature
parameter_list|(
name|FeaturesService
name|admin
parameter_list|,
name|Feature
name|feature
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|admin
operator|.
name|getFeature
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|repeat
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|times
parameter_list|)
block|{
if|if
condition|(
name|times
operator|<=
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
elseif|else
if|if
condition|(
name|times
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
return|return
name|repeat
argument_list|(
name|string
operator|+
name|string
argument_list|,
name|times
operator|/
literal|2
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|string
operator|+
name|repeat
argument_list|(
name|string
operator|+
name|string
argument_list|,
name|times
operator|/
literal|2
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

