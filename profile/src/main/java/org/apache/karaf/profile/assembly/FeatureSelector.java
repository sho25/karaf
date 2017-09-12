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
name|profile
operator|.
name|assembly
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|internal
operator|.
name|model
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
name|internal
operator|.
name|model
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
name|internal
operator|.
name|service
operator|.
name|FeatureReq
import|;
end_import

begin_class
specifier|public
class|class
name|FeatureSelector
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Feature
argument_list|>
argument_list|>
name|featuresCache
decl_stmt|;
specifier|public
name|FeatureSelector
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
parameter_list|)
block|{
name|featuresCache
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|features
control|)
block|{
name|featuresCache
operator|.
name|computeIfAbsent
argument_list|(
name|feature
operator|.
name|getName
argument_list|()
argument_list|,
name|fn
lambda|->
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Features matching the given feature selectors including dependent features      *       * @param features feature selector name, name/version, name/version-range      *       * @return matching features       */
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|>
name|getMatching
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|features
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|selected
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|feature
range|:
name|features
control|)
block|{
name|addFeatures
argument_list|(
name|feature
argument_list|,
name|selected
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|selected
return|;
block|}
specifier|private
name|void
name|addFeatures
parameter_list|(
name|String
name|feature
parameter_list|,
name|Set
argument_list|<
name|Feature
argument_list|>
name|features
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|>
name|set
init|=
name|getMatching
argument_list|(
name|feature
argument_list|)
decl_stmt|;
if|if
condition|(
name|mandatory
operator|&&
name|set
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Could not find matching feature for "
operator|+
name|feature
argument_list|)
throw|;
block|}
for|for
control|(
name|Feature
name|f
range|:
name|set
control|)
block|{
if|if
condition|(
name|features
operator|.
name|add
argument_list|(
name|f
argument_list|)
condition|)
block|{
for|for
control|(
name|Dependency
name|dep
range|:
name|f
operator|.
name|getFeature
argument_list|()
control|)
block|{
name|addFeatures
argument_list|(
name|dep
operator|.
name|toString
argument_list|()
argument_list|,
name|features
argument_list|,
name|isMandatory
argument_list|(
name|dep
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isMandatory
parameter_list|(
name|Dependency
name|dep
parameter_list|)
block|{
return|return
operator|!
name|dep
operator|.
name|isDependency
argument_list|()
operator|&&
operator|!
name|dep
operator|.
name|isPrerequisite
argument_list|()
return|;
block|}
specifier|private
name|Set
argument_list|<
name|Feature
argument_list|>
name|getMatching
parameter_list|(
name|String
name|nameAndVersion
parameter_list|)
block|{
name|FeatureReq
name|req
init|=
operator|new
name|FeatureReq
argument_list|(
name|nameAndVersion
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Feature
argument_list|>
name|versionToFeatures
init|=
name|featuresCache
operator|.
name|get
argument_list|(
name|req
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionToFeatures
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
return|return
name|versionToFeatures
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|f
lambda|->
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|req
operator|.
name|getVersionRange
argument_list|()
operator|.
name|contains
argument_list|(
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|f
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit
