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
name|Comparator
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
operator|(
operator|(
name|Comparable
operator|)
name|cap1
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|BundleNamespace
operator|.
name|BUNDLE_NAMESPACE
argument_list|)
operator|)
operator|.
name|compareTo
argument_list|(
name|cap2
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|BundleNamespace
operator|.
name|BUNDLE_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
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
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
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
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
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
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
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
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
comment|// Compare these in reverse order, since we want
comment|// highest version to have priority.
name|c
operator|=
name|compareVersions
argument_list|(
name|v2
argument_list|,
name|v1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Compare package capabilities.
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
operator|(
operator|(
name|Comparable
operator|)
name|cap1
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
operator|)
operator|.
name|compareTo
argument_list|(
name|cap2
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|PackageNamespace
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
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
name|PackageNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
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
name|PackageNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
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
name|PackageNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
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
name|PackageNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
comment|// Compare these in reverse order, since we want
comment|// highest version to have priority.
name|c
operator|=
name|compareVersions
argument_list|(
name|v2
argument_list|,
name|v1
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
name|v1
operator|=
operator|(
operator|!
name|cap1
operator|.
name|getAttributes
argument_list|()
operator|.
name|containsKey
argument_list|(
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
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
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
name|v2
operator|=
operator|(
operator|!
name|cap2
operator|.
name|getAttributes
argument_list|()
operator|.
name|containsKey
argument_list|(
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
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
name|BundleNamespace
operator|.
name|CAPABILITY_BUNDLE_VERSION_ATTRIBUTE
argument_list|)
expr_stmt|;
comment|// Compare these in reverse order, since we want
comment|// highest version to have priority.
name|c
operator|=
name|compareVersions
argument_list|(
name|v2
argument_list|,
name|v1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Compare feature capabilities
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
operator|(
operator|(
name|Comparable
operator|)
name|cap1
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
operator|)
operator|.
name|compareTo
argument_list|(
name|cap2
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
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
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
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
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
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
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
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
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
name|IdentityNamespace
operator|.
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
comment|// Compare these in reverse order, since we want
comment|// highest version to have priority.
name|c
operator|=
name|compareVersions
argument_list|(
name|v2
argument_list|,
name|v1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|c
return|;
block|}
specifier|private
name|int
name|compareVersions
parameter_list|(
name|Version
name|v1
parameter_list|,
name|Version
name|v2
parameter_list|)
block|{
name|int
name|c
init|=
name|v1
operator|.
name|getMajor
argument_list|()
operator|-
name|v2
operator|.
name|getMajor
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|0
condition|)
block|{
return|return
name|c
return|;
block|}
name|c
operator|=
name|v1
operator|.
name|getMinor
argument_list|()
operator|-
name|v2
operator|.
name|getMinor
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|0
condition|)
block|{
return|return
name|c
return|;
block|}
name|c
operator|=
name|v1
operator|.
name|getMicro
argument_list|()
operator|-
name|v2
operator|.
name|getMicro
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|0
condition|)
block|{
return|return
name|c
return|;
block|}
return|return
name|v1
operator|.
name|getQualifier
argument_list|()
operator|.
name|compareTo
argument_list|(
name|v2
operator|.
name|getQualifier
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit
