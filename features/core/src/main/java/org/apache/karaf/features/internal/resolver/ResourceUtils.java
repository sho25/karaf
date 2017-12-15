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
operator|.
name|resolver
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
name|internal
operator|.
name|util
operator|.
name|StringArrayMap
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
name|osgi
operator|.
name|resource
operator|.
name|Capability
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import static
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
name|resolver
operator|.
name|FeatureResource
operator|.
name|CONDITIONAL_TRUE
import|;
end_import

begin_import
import|import static
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
name|resolver
operator|.
name|FeatureResource
operator|.
name|REQUIREMENT_CONDITIONAL_DIRECTIVE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|IdentityNamespace
operator|.
name|CAPABILITY_TYPE_ATTRIBUTE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|namespace
operator|.
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Namespace
operator|.
name|REQUIREMENT_RESOLUTION_DIRECTIVE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Namespace
operator|.
name|RESOLUTION_MANDATORY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Namespace
operator|.
name|RESOLUTION_OPTIONAL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|repository
operator|.
name|ContentNamespace
operator|.
name|CAPABILITY_URL_ATTRIBUTE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|repository
operator|.
name|ContentNamespace
operator|.
name|CONTENT_NAMESPACE
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ResourceUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_SUBSYSTEM
init|=
literal|"karaf.subsystem"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_FEATURE
init|=
literal|"karaf.feature"
decl_stmt|;
specifier|private
name|ResourceUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
name|resource
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|caps
control|)
block|{
if|if
condition|(
name|cap
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
condition|)
block|{
return|return
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|// TODO: Correct name should probably be toUrl
specifier|public
specifier|static
name|String
name|getUri
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
name|resource
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|caps
control|)
block|{
if|if
condition|(
name|cap
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|CONTENT_NAMESPACE
argument_list|)
condition|)
block|{
return|return
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_URL_ATTRIBUTE
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * If the resource has<code>type=karaf.feature</code> capability, returns its ID (name[/version]).      */
specifier|public
specifier|static
name|String
name|getFeatureId
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
name|resource
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|caps
control|)
block|{
if|if
condition|(
name|cap
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|TYPE_FEATURE
operator|.
name|equals
argument_list|(
name|attributes
operator|.
name|get
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
decl_stmt|;
name|Version
name|version
init|=
operator|(
name|Version
operator|)
name|attributes
operator|.
name|get
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
return|return
name|version
operator|!=
literal|null
condition|?
name|name
operator|+
literal|"/"
operator|+
name|version
else|:
name|name
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|RequirementImpl
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|range
parameter_list|)
block|{
return|return
name|addIdentityRequirement
argument_list|(
name|resource
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|range
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RequirementImpl
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|range
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
return|return
name|addIdentityRequirement
argument_list|(
name|resource
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|range
operator|!=
literal|null
condition|?
operator|new
name|VersionRange
argument_list|(
name|range
argument_list|)
else|:
literal|null
argument_list|,
name|mandatory
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RequirementImpl
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|VersionRange
name|range
parameter_list|)
block|{
return|return
name|addIdentityRequirement
argument_list|(
name|resource
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|range
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RequirementImpl
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|VersionRange
name|range
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
return|return
name|addIdentityRequirement
argument_list|(
name|resource
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|range
argument_list|,
name|mandatory
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RequirementImpl
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|VersionRange
name|range
parameter_list|,
name|boolean
name|mandatory
parameter_list|,
name|boolean
name|conditional
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
init|=
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
operator|(
name|mandatory
condition|?
literal|0
else|:
literal|1
operator|)
operator|+
operator|(
name|conditional
condition|?
literal|1
else|:
literal|0
operator|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
init|=
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
operator|(
name|name
operator|!=
literal|null
condition|?
literal|1
else|:
literal|0
operator|)
operator|+
operator|(
name|type
operator|!=
literal|null
condition|?
literal|1
else|:
literal|0
operator|)
operator|+
operator|(
name|range
operator|!=
literal|null
condition|?
literal|1
else|:
literal|0
operator|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|mandatory
condition|)
block|{
name|dirs
operator|.
name|put
argument_list|(
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|,
name|RESOLUTION_OPTIONAL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conditional
condition|)
block|{
name|dirs
operator|.
name|put
argument_list|(
name|REQUIREMENT_CONDITIONAL_DIRECTIVE
argument_list|,
name|CONDITIONAL_TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|attrs
operator|.
name|put
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|range
operator|!=
literal|null
condition|)
block|{
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|,
name|range
argument_list|)
expr_stmt|;
block|}
name|RequirementImpl
name|requirement
init|=
operator|new
name|RequirementImpl
argument_list|(
name|resource
argument_list|,
name|IDENTITY_NAMESPACE
argument_list|,
name|dirs
argument_list|,
name|attrs
argument_list|)
decl_stmt|;
name|resource
operator|.
name|addRequirement
argument_list|(
name|requirement
argument_list|)
expr_stmt|;
return|return
name|requirement
return|;
block|}
specifier|public
specifier|static
name|void
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|Resource
name|required
parameter_list|)
block|{
name|addIdentityRequirement
argument_list|(
name|resource
argument_list|,
name|required
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|addIdentityRequirement
parameter_list|(
name|ResourceImpl
name|resource
parameter_list|,
name|Resource
name|required
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
for|for
control|(
name|Capability
name|cap
range|:
name|required
operator|.
name|getCapabilities
argument_list|(
literal|null
argument_list|)
control|)
block|{
if|if
condition|(
name|cap
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
init|=
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|dirs
operator|.
name|put
argument_list|(
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|,
name|mandatory
condition|?
name|RESOLUTION_MANDATORY
else|:
name|RESOLUTION_OPTIONAL
argument_list|)
expr_stmt|;
name|Version
name|version
init|=
operator|(
name|Version
operator|)
name|attributes
operator|.
name|get
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
init|=
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
name|version
operator|!=
literal|null
condition|?
literal|3
else|:
literal|2
argument_list|)
decl_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|,
name|attributes
operator|.
name|get
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|,
name|attributes
operator|.
name|get
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|,
operator|new
name|VersionRange
argument_list|(
name|version
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|resource
operator|.
name|addRequirement
argument_list|(
operator|new
name|RequirementImpl
argument_list|(
name|resource
argument_list|,
name|IDENTITY_NAMESPACE
argument_list|,
name|dirs
argument_list|,
name|attrs
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Changes feature identifier (<code>name[/version]</code>) into a requirement specification.      * The OSGi manifest header for a feature will be:<code>osgi.identity;osgi.identity=feature-name;type=karaf.feature[;version=feature-version];filter:=filter-from-attrs</code>.      *      * @param feature The feature name.      * @return The feature requirement.      */
specifier|public
specifier|static
name|String
name|toFeatureRequirement
parameter_list|(
name|String
name|feature
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|feature
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
init|=
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|?
literal|3
else|:
literal|2
argument_list|)
decl_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|,
name|TYPE_FEATURE
argument_list|)
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|,
operator|new
name|VersionRange
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Constants
operator|.
name|FILTER_DIRECTIVE
argument_list|,
name|SimpleFilter
operator|.
name|convert
argument_list|(
name|attrs
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|RequirementImpl
argument_list|(
literal|null
argument_list|,
name|IDENTITY_NAMESPACE
argument_list|,
name|dirs
argument_list|,
name|attrs
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|toFeatureCapability
parameter_list|(
name|String
name|feature
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|feature
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
init|=
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|?
literal|3
else|:
literal|2
argument_list|)
decl_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|,
name|TYPE_FEATURE
argument_list|)
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|,
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|CapabilityImpl
argument_list|(
literal|null
argument_list|,
name|IDENTITY_NAMESPACE
argument_list|,
name|dirs
argument_list|,
name|attrs
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

