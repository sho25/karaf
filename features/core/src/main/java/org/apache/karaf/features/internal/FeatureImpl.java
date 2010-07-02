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
name|internal
package|;
end_package

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

begin_comment
comment|/**  * A feature  */
end_comment

begin_class
specifier|public
class|class
name|FeatureImpl
implements|implements
name|Feature
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|resolver
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Feature
argument_list|>
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|>
argument_list|()
decl_stmt|;
comment|// TODO
comment|// private List<String> bundles = new ArrayList<String>();
specifier|private
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|bundles
init|=
operator|new
name|ArrayList
argument_list|<
name|BundleInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
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
name|configs
init|=
operator|new
name|HashMap
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
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|String
name|SPLIT_FOR_NAME_AND_VERSION
init|=
literal|"_split_for_name_and_version_"
decl_stmt|;
specifier|public
specifier|static
name|String
name|DEFAULT_VERSION
init|=
literal|"0.0.0"
decl_stmt|;
specifier|public
name|FeatureImpl
parameter_list|()
block|{     }
specifier|public
name|FeatureImpl
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|DEFAULT_VERSION
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FeatureImpl
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|name
operator|+
literal|"-"
operator|+
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
specifier|public
name|void
name|setResolver
parameter_list|(
name|String
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Feature
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|List
argument_list|<
name|BundleInfo
argument_list|>
name|getBundles
parameter_list|()
block|{
return|return
name|bundles
return|;
block|}
specifier|public
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
name|getConfigurations
parameter_list|()
block|{
return|return
name|configs
return|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|Feature
name|dependency
parameter_list|)
block|{
name|dependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addBundle
parameter_list|(
name|BundleInfo
name|bundle
parameter_list|)
block|{
name|bundles
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfig
parameter_list|(
name|String
name|name
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|configs
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|ret
init|=
name|getName
argument_list|()
operator|+
name|SPLIT_FOR_NAME_AND_VERSION
operator|+
name|getVersion
argument_list|()
decl_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|Feature
name|valueOf
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|.
name|indexOf
argument_list|(
name|SPLIT_FOR_NAME_AND_VERSION
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|String
name|strName
init|=
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|indexOf
argument_list|(
name|SPLIT_FOR_NAME_AND_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|strVersion
init|=
name|str
operator|.
name|substring
argument_list|(
name|str
operator|.
name|indexOf
argument_list|(
name|SPLIT_FOR_NAME_AND_VERSION
argument_list|)
operator|+
name|SPLIT_FOR_NAME_AND_VERSION
operator|.
name|length
argument_list|()
argument_list|,
name|str
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|FeatureImpl
argument_list|(
name|strName
argument_list|,
name|strVersion
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|FeatureImpl
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|FeatureImpl
name|feature
init|=
operator|(
name|FeatureImpl
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|name
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|version
operator|.
name|equals
argument_list|(
name|feature
operator|.
name|version
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|name
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|version
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

