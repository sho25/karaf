begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|ArrayList
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
name|namespace
operator|.
name|IdentityNamespace
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
name|namespace
operator|.
name|PackageNamespace
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
name|Namespace
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
name|Requirement
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
name|ResourceUtils
operator|.
name|TYPE_SUBSYSTEM
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
name|service
operator|.
name|subsystem
operator|.
name|SubsystemConstants
operator|.
name|SUBSYSTEM_TYPE
import|;
end_import

begin_class
specifier|public
class|class
name|ResolverUtil
block|{
specifier|public
specifier|static
name|String
name|getSymbolicName
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
name|IdentityNamespace
operator|.
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
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
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
specifier|public
specifier|static
name|String
name|getOwnerName
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|Requirement
argument_list|>
name|reqs
init|=
name|resource
operator|.
name|getRequirements
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|Requirement
name|req
range|:
name|reqs
control|)
block|{
if|if
condition|(
name|req
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
operator|&&
name|TYPE_SUBSYSTEM
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|req
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
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
specifier|public
specifier|static
name|Version
name|getVersion
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
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
condition|)
block|{
return|return
operator|(
name|Version
operator|)
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isFragment
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
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
condition|)
block|{
name|String
name|type
init|=
operator|(
name|String
operator|)
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
decl_stmt|;
return|return
operator|(
name|type
operator|!=
literal|null
operator|)
operator|&&
name|type
operator|.
name|equals
argument_list|(
name|IdentityNamespace
operator|.
name|TYPE_FRAGMENT
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isOptional
parameter_list|(
name|Requirement
name|req
parameter_list|)
block|{
name|String
name|resolution
init|=
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Namespace
operator|.
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|)
decl_stmt|;
return|return
name|Namespace
operator|.
name|RESOLUTION_OPTIONAL
operator|.
name|equalsIgnoreCase
argument_list|(
name|resolution
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isMultiple
parameter_list|(
name|Requirement
name|req
parameter_list|)
block|{
return|return
name|Namespace
operator|.
name|CARDINALITY_MULTIPLE
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Namespace
operator|.
name|REQUIREMENT_CARDINALITY_DIRECTIVE
argument_list|)
argument_list|)
operator|&&
operator|!
name|isDynamic
argument_list|(
name|req
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isDynamic
parameter_list|(
name|Requirement
name|req
parameter_list|)
block|{
return|return
name|PackageNamespace
operator|.
name|RESOLUTION_DYNAMIC
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Namespace
operator|.
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Requirement
argument_list|>
name|getDynamicRequirements
parameter_list|(
name|List
argument_list|<
name|Requirement
argument_list|>
name|reqs
parameter_list|)
block|{
name|List
argument_list|<
name|Requirement
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|reqs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Requirement
name|req
range|:
name|reqs
control|)
block|{
name|String
name|resolution
init|=
name|req
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|PackageNamespace
operator|.
name|REQUIREMENT_RESOLUTION_DIRECTIVE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|resolution
operator|!=
literal|null
operator|)
operator|&&
name|resolution
operator|.
name|equals
argument_list|(
name|PackageNamespace
operator|.
name|RESOLUTION_DYNAMIC
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|req
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

