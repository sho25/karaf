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
name|repository
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|concurrent
operator|.
name|locks
operator|.
name|ReadWriteLock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantReadWriteLock
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
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
name|CapabilitySet
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
name|SimpleFilter
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

begin_comment
comment|/**  * Repository conforming to the OSGi Repository specification.  * The content of the URL can be gzipped.  */
end_comment

begin_class
specifier|public
class|class
name|XmlRepository
extends|extends
name|BaseRepository
block|{
specifier|protected
specifier|final
name|String
name|url
decl_stmt|;
specifier|protected
specifier|final
name|long
name|expiration
decl_stmt|;
specifier|protected
specifier|final
name|boolean
name|ignoreFailures
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|XmlLoader
argument_list|>
name|loaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|XmlLoader
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
specifier|final
name|ReadWriteLock
name|lock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
specifier|public
name|XmlRepository
parameter_list|(
name|String
name|url
parameter_list|,
name|long
name|expiration
parameter_list|,
name|boolean
name|ignoreFailures
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|expiration
operator|=
name|expiration
expr_stmt|;
name|this
operator|.
name|ignoreFailures
operator|=
name|ignoreFailures
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Resource
argument_list|>
name|getResources
parameter_list|()
block|{
name|checkAndLoadCache
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|getResources
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Requirement
argument_list|,
name|Collection
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|findProviders
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Requirement
argument_list|>
name|requirements
parameter_list|)
block|{
name|checkAndLoadCache
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|findProviders
argument_list|(
name|requirements
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|XmlLoader
argument_list|>
name|getLoaders
parameter_list|()
block|{
return|return
name|loaders
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|addResource
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|Capability
argument_list|>
name|identities
init|=
name|resource
operator|.
name|getCapabilities
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|identities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid resource: a capability with 'osgi.identity' namespace is required"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|identities
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid resource: multiple 'osgi.identity' capabilities found"
argument_list|)
throw|;
block|}
name|Capability
name|identity
init|=
name|identities
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|name
init|=
name|identity
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
decl_stmt|;
name|Object
name|type
init|=
name|identity
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|)
decl_stmt|;
name|Object
name|vers
init|=
name|identity
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|String
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|name
argument_list|)
operator|||
operator|!
name|String
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|type
argument_list|)
operator|||
operator|!
name|Version
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|vers
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid osgi.identity capability: "
operator|+
name|identity
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|hasResource
argument_list|(
operator|(
name|String
operator|)
name|type
argument_list|,
operator|(
name|String
operator|)
name|name
argument_list|,
operator|(
name|Version
operator|)
name|vers
argument_list|)
condition|)
block|{
name|super
operator|.
name|addResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasResource
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|name
parameter_list|,
name|Version
name|version
parameter_list|)
block|{
name|CapabilitySet
name|set
init|=
name|capSets
operator|.
name|get
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|set
operator|!=
literal|null
condition|)
block|{
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
name|CAPABILITY_TYPE_ATTRIBUTE
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|IDENTITY_NAMESPACE
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|attrs
operator|.
name|put
argument_list|(
name|CAPABILITY_VERSION_ATTRIBUTE
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|SimpleFilter
name|sf
init|=
name|SimpleFilter
operator|.
name|convert
argument_list|(
name|attrs
argument_list|)
decl_stmt|;
return|return
operator|!
name|set
operator|.
name|match
argument_list|(
name|sf
argument_list|,
literal|true
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|void
name|checkAndLoadCache
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|checkAndLoadReferrals
argument_list|(
name|url
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
condition|)
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|resources
operator|.
name|clear
argument_list|()
expr_stmt|;
name|capSets
operator|.
name|clear
argument_list|()
expr_stmt|;
name|populate
argument_list|(
name|loaders
operator|.
name|get
argument_list|(
name|url
argument_list|)
operator|.
name|xml
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|ignoreFailures
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Ignoring failure: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|populate
parameter_list|(
name|StaxParser
operator|.
name|XmlRepository
name|xml
parameter_list|,
name|int
name|hopCount
parameter_list|)
block|{
if|if
condition|(
name|hopCount
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Resource
name|resource
range|:
name|xml
operator|.
name|resources
control|)
block|{
name|addResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|StaxParser
operator|.
name|Referral
name|referral
range|:
name|xml
operator|.
name|referrals
control|)
block|{
name|populate
argument_list|(
name|loaders
operator|.
name|get
argument_list|(
name|referral
operator|.
name|url
argument_list|)
operator|.
name|xml
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|referral
operator|.
name|depth
argument_list|,
name|hopCount
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|checkAndLoadReferrals
parameter_list|(
name|String
name|url
parameter_list|,
name|int
name|hopCount
parameter_list|)
block|{
name|boolean
name|modified
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|hopCount
operator|>
literal|0
condition|)
block|{
name|XmlLoader
name|loader
init|=
name|loaders
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
operator|new
name|XmlLoader
argument_list|(
name|url
argument_list|,
name|expiration
argument_list|)
expr_stmt|;
name|loaders
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|loader
argument_list|)
expr_stmt|;
block|}
name|modified
operator|=
name|loader
operator|.
name|checkAndLoadCache
argument_list|()
expr_stmt|;
for|for
control|(
name|StaxParser
operator|.
name|Referral
name|referral
range|:
name|loader
operator|.
name|xml
operator|.
name|referrals
control|)
block|{
name|modified
operator||=
name|checkAndLoadReferrals
argument_list|(
name|referral
operator|.
name|url
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|referral
operator|.
name|depth
argument_list|,
name|hopCount
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|modified
return|;
block|}
specifier|protected
specifier|static
class|class
name|XmlLoader
extends|extends
name|UrlLoader
block|{
specifier|protected
name|StaxParser
operator|.
name|XmlRepository
name|xml
decl_stmt|;
specifier|public
name|XmlLoader
parameter_list|(
name|String
name|url
parameter_list|,
name|long
name|expiration
parameter_list|)
block|{
name|super
argument_list|(
name|url
argument_list|,
name|expiration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XmlLoader
parameter_list|(
name|String
name|url
parameter_list|,
name|long
name|expiration
parameter_list|,
name|StaxParser
operator|.
name|XmlRepository
name|xml
parameter_list|)
block|{
name|super
argument_list|(
name|url
argument_list|,
name|expiration
argument_list|)
expr_stmt|;
name|this
operator|.
name|xml
operator|=
name|xml
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|doRead
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|StaxParser
operator|.
name|XmlRepository
name|oldXml
init|=
name|xml
decl_stmt|;
name|xml
operator|=
name|StaxParser
operator|.
name|parse
argument_list|(
name|is
argument_list|,
name|oldXml
argument_list|)
expr_stmt|;
return|return
name|oldXml
operator|!=
name|xml
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to read xml repository"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

