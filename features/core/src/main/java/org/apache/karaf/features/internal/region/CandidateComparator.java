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
name|region
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
name|Comparator
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
name|function
operator|.
name|ToIntFunction
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
name|resolver
operator|.
name|ResolverUtil
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
name|BundleNamespace
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

begin_class
specifier|public
class|class
name|CandidateComparator
implements|implements
name|Comparator
argument_list|<
name|Capability
argument_list|>
block|{
specifier|private
specifier|final
name|ToIntFunction
argument_list|<
name|Resource
argument_list|>
name|cost
decl_stmt|;
specifier|public
name|CandidateComparator
parameter_list|(
name|ToIntFunction
argument_list|<
name|Resource
argument_list|>
name|cost
parameter_list|)
block|{
name|this
operator|.
name|cost
operator|=
name|cost
expr_stmt|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|Capability
name|cap1
parameter_list|,
name|Capability
name|cap2
parameter_list|)
block|{
name|int
name|c
init|=
literal|0
decl_stmt|;
comment|// Always prefer system bundle
if|if
condition|(
name|cap1
operator|instanceof
name|BundleCapability
operator|&&
operator|!
operator|(
name|cap2
operator|instanceof
name|BundleCapability
operator|)
condition|)
block|{
name|c
operator|=
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|cap1
operator|instanceof
name|BundleCapability
operator|)
operator|&&
name|cap2
operator|instanceof
name|BundleCapability
condition|)
block|{
name|c
operator|=
literal|1
expr_stmt|;
block|}
comment|// Always prefer mandatory resources
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|int
name|c1
init|=
name|cost
operator|.
name|applyAsInt
argument_list|(
name|cap1
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|c2
init|=
name|cost
operator|.
name|applyAsInt
argument_list|(
name|cap2
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
name|c
operator|=
name|Integer
operator|.
name|compare
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
expr_stmt|;
block|}
comment|// Compare revision capabilities.
if|if
condition|(
operator|(
name|c
operator|==
literal|0
operator|)
operator|&&
name|cap1
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|BundleNamespace
operator|.
name|BUNDLE_NAMESPACE
argument_list|)
condition|)
block|{
name|c
operator|=
name|compareNames
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|BundleNamespace
operator|.
name|BUNDLE_NAMESPACE
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|c
operator|=
name|compareVersions
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
block|}
comment|// Compare package capabilities.
block|}
elseif|else
if|if
condition|(
operator|(
name|c
operator|==
literal|0
operator|)
operator|&&
name|cap1
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
condition|)
block|{
name|c
operator|=
name|compareNames
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|c
operator|=
name|compareVersions
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|PackageNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
comment|// if same version, rather compare on the bundle version
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|c
operator|=
name|compareVersions
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Compare feature capabilities
block|}
elseif|else
if|if
condition|(
operator|(
name|c
operator|==
literal|0
operator|)
operator|&&
name|cap1
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
name|c
operator|=
name|compareNames
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|IdentityNamespace
operator|.
name|IDENTITY_NAMESPACE
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|c
operator|=
name|compareVersions
argument_list|(
name|cap1
argument_list|,
name|cap2
argument_list|,
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
comment|// We just want to have a deterministic heuristic
specifier|final
name|Resource
name|resource1
init|=
name|cap1
operator|.
name|getResource
argument_list|()
decl_stmt|;
specifier|final
name|Resource
name|resource2
init|=
name|cap2
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|String
name|n1
init|=
name|ResolverUtil
operator|.
name|getSymbolicName
argument_list|(
name|resource1
argument_list|)
decl_stmt|;
name|String
name|n2
init|=
name|ResolverUtil
operator|.
name|getSymbolicName
argument_list|(
name|resource2
argument_list|)
decl_stmt|;
name|c
operator|=
name|n1
operator|.
name|compareTo
argument_list|(
name|n2
argument_list|)
expr_stmt|;
comment|// Resources looks like identical, but it required by different features/subsystems/regions
comment|// so use this difference for deterministic heuristic
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
name|String
name|o1
init|=
name|ResolverUtil
operator|.
name|getOwnerName
argument_list|(
name|resource1
argument_list|)
decl_stmt|;
name|String
name|o2
init|=
name|ResolverUtil
operator|.
name|getOwnerName
argument_list|(
name|resource2
argument_list|)
decl_stmt|;
if|if
condition|(
name|o1
operator|!=
literal|null
operator|&&
name|o2
operator|!=
literal|null
condition|)
block|{
comment|// In case the owners are the same but with different version, prefer the latest one
comment|// TODO: this may not be fully correct, as we'd need to separate names/versions
comment|// TODO: and do a real version comparison
name|c
operator|=
operator|-
name|o1
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|c
return|;
block|}
specifier|private
name|int
name|compareNames
parameter_list|(
name|Capability
name|cap1
parameter_list|,
name|Capability
name|cap2
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
name|Object
name|o1
init|=
name|cap1
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
name|Object
name|o2
init|=
name|cap2
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|o1
operator|instanceof
name|List
operator|||
name|o2
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|l1
init|=
name|o1
operator|instanceof
name|List
condition|?
operator|(
name|List
operator|)
name|o1
else|:
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|String
operator|)
name|o1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|l2
init|=
name|o2
operator|instanceof
name|List
condition|?
operator|(
name|List
operator|)
name|o2
else|:
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|String
operator|)
name|o2
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|l1
control|)
block|{
if|if
condition|(
name|l2
operator|.
name|contains
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
block|}
return|return
name|l1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|compareTo
argument_list|(
name|l2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
operator|(
name|String
operator|)
name|o1
operator|)
operator|.
name|compareTo
argument_list|(
operator|(
name|String
operator|)
name|o2
argument_list|)
return|;
block|}
block|}
specifier|private
name|int
name|compareVersions
parameter_list|(
name|Capability
name|cap1
parameter_list|,
name|Capability
name|cap2
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
name|Version
name|v1
init|=
operator|(
operator|!
name|cap1
operator|.
name|getAttributes
argument_list|()
operator|.
name|containsKey
argument_list|(
name|attribute
argument_list|)
operator|)
condition|?
name|Version
operator|.
name|emptyVersion
else|:
operator|(
name|Version
operator|)
name|cap1
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
name|Version
name|v2
init|=
operator|(
operator|!
name|cap2
operator|.
name|getAttributes
argument_list|()
operator|.
name|containsKey
argument_list|(
name|attribute
argument_list|)
operator|)
condition|?
name|Version
operator|.
name|emptyVersion
else|:
operator|(
name|Version
operator|)
name|cap2
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
comment|// Compare these in reverse order, since we want
comment|// highest version to have priority.
return|return
name|v2
operator|.
name|compareTo
argument_list|(
name|v1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

