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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ResourceImpl
implements|implements
name|Resource
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Capability
argument_list|>
name|m_caps
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Requirement
argument_list|>
name|m_reqs
decl_stmt|;
specifier|public
name|ResourceImpl
parameter_list|(
name|String
name|name
parameter_list|,
name|Version
name|version
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|IdentityNamespace
operator|.
name|TYPE_BUNDLE
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResourceImpl
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|Version
name|version
parameter_list|)
block|{
name|m_caps
operator|=
operator|new
name|ArrayList
argument_list|<
name|Capability
argument_list|>
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IdentityNamespace
operator|.
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|CapabilityImpl
name|identity
init|=
operator|new
name|CapabilityImpl
argument_list|(
name|this
argument_list|,
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|,
name|dirs
argument_list|,
name|attrs
argument_list|)
decl_stmt|;
name|m_caps
operator|.
name|add
argument_list|(
name|identity
argument_list|)
expr_stmt|;
name|m_reqs
operator|=
operator|new
name|ArrayList
argument_list|<
name|Requirement
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addCapability
parameter_list|(
name|Capability
name|capability
parameter_list|)
block|{
assert|assert
name|capability
operator|.
name|getResource
argument_list|()
operator|==
name|this
assert|;
name|m_caps
operator|.
name|add
argument_list|(
name|capability
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCapabilities
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Capability
argument_list|>
name|capabilities
parameter_list|)
block|{
for|for
control|(
name|Capability
name|cap
range|:
name|capabilities
control|)
block|{
name|addCapability
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addRequirement
parameter_list|(
name|Requirement
name|requirement
parameter_list|)
block|{
assert|assert
name|requirement
operator|.
name|getResource
argument_list|()
operator|==
name|this
assert|;
name|m_reqs
operator|.
name|add
argument_list|(
name|requirement
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addRequirements
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Requirement
argument_list|>
name|requirements
parameter_list|)
block|{
for|for
control|(
name|Requirement
name|req
range|:
name|requirements
control|)
block|{
name|addRequirement
argument_list|(
name|req
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|Capability
argument_list|>
name|getCapabilities
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|result
init|=
name|m_caps
decl_stmt|;
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|<
name|Capability
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|m_caps
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
name|namespace
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
name|List
argument_list|<
name|Requirement
argument_list|>
name|getRequirements
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|List
argument_list|<
name|Requirement
argument_list|>
name|result
init|=
name|m_reqs
decl_stmt|;
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|<
name|Requirement
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Requirement
name|req
range|:
name|m_reqs
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
name|namespace
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
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|Capability
name|cap
init|=
name|getCapabilities
argument_list|(
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
operator|+
literal|"/"
operator|+
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
end_class

end_unit

