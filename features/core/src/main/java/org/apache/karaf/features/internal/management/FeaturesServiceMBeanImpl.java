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
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanNotificationInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanRegistration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationBroadcasterSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardEmitterMBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|FeatureEvent
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
name|FeaturesListener
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
name|Repository
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
name|RepositoryEvent
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
name|management
operator|.
name|FeaturesServiceMBean
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
name|management
operator|.
name|codec
operator|.
name|JmxFeature
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
name|management
operator|.
name|codec
operator|.
name|JmxFeatureEvent
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
name|management
operator|.
name|codec
operator|.
name|JmxRepository
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
name|management
operator|.
name|codec
operator|.
name|JmxRepositoryEvent
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
name|BundleContext
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
name|ServiceRegistration
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link FeaturesServiceMBean}.  */
end_comment

begin_class
specifier|public
class|class
name|FeaturesServiceMBeanImpl
extends|extends
name|StandardEmitterMBean
implements|implements
name|MBeanRegistration
implements|,
name|FeaturesServiceMBean
block|{
specifier|private
name|ServiceRegistration
argument_list|<
name|FeaturesListener
argument_list|>
name|registration
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
name|ObjectName
name|objectName
decl_stmt|;
specifier|private
specifier|volatile
name|long
name|sequenceNumber
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|public
name|FeaturesServiceMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|FeaturesServiceMBean
operator|.
name|class
argument_list|,
operator|new
name|NotificationBroadcasterSupport
argument_list|(
name|getBroadcastInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjectName
name|preRegister
parameter_list|(
name|MBeanServer
name|server
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|objectName
operator|=
name|name
expr_stmt|;
return|return
name|name
return|;
block|}
specifier|public
name|void
name|postRegister
parameter_list|(
name|Boolean
name|registrationDone
parameter_list|)
block|{
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|FeaturesListener
operator|.
name|class
argument_list|,
name|getFeaturesListener
argument_list|()
argument_list|,
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|preDeregister
parameter_list|()
throws|throws
name|Exception
block|{
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|postDeregister
parameter_list|()
block|{     }
annotation|@
name|Override
specifier|public
name|TabularData
name|getFeatures
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|List
argument_list|<
name|Feature
argument_list|>
name|allFeatures
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresService
operator|.
name|listFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Feature
argument_list|>
name|insFeatures
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresService
operator|.
name|listInstalledFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|JmxFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|allFeatures
control|)
block|{
try|try
block|{
name|features
operator|.
name|add
argument_list|(
operator|new
name|JmxFeature
argument_list|(
name|feature
argument_list|,
name|insFeatures
operator|.
name|contains
argument_list|(
name|feature
argument_list|)
argument_list|,
name|featuresService
operator|.
name|isRequired
argument_list|(
name|feature
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|JmxFeature
operator|.
name|tableFrom
argument_list|(
name|features
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|getRepositories
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|List
argument_list|<
name|Repository
argument_list|>
name|allRepositories
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|featuresService
operator|.
name|listRepositories
argument_list|()
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|JmxRepository
argument_list|>
name|repositories
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|repository
range|:
name|allRepositories
control|)
block|{
try|try
block|{
name|repositories
operator|.
name|add
argument_list|(
operator|new
name|JmxRepository
argument_list|(
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|JmxRepository
operator|.
name|tableFrom
argument_list|(
name|repositories
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|repositoryProvidedFeatures
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|infoFeature
argument_list|(
name|featuresService
operator|.
name|repositoryProvidedFeatures
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addRepository
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|repoUri
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|featuresService
operator|.
name|isRepositoryUriBlacklisted
argument_list|(
name|repoUri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Feature URL "
operator|+
name|uri
operator|+
literal|" is blacklisted"
argument_list|)
throw|;
block|}
name|featuresService
operator|.
name|addRepository
argument_list|(
name|repoUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addRepository
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|install
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|repoUri
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|featuresService
operator|.
name|isRepositoryUriBlacklisted
argument_list|(
name|repoUri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Feature URL "
operator|+
name|uri
operator|+
literal|" is blacklisted"
argument_list|)
throw|;
block|}
name|featuresService
operator|.
name|addRepository
argument_list|(
name|repoUri
argument_list|,
name|install
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeRepository
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|removeRepository
argument_list|(
name|uri
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeRepository
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|uninstall
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|uri
argument_list|)
decl_stmt|;
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
if|if
condition|(
name|repository
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|repository
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// first regex on the repository name
name|Matcher
name|nameMatcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameMatcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|uris
operator|.
name|add
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// fallback to repository URI regex
name|Matcher
name|uriMatcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriMatcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|uris
operator|.
name|add
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// repository name is not defined, fallback to repository URI regex
name|Matcher
name|uriMatcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriMatcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|uris
operator|.
name|add
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|URI
name|u
range|:
name|uris
control|)
block|{
name|featuresService
operator|.
name|removeRepository
argument_list|(
name|u
argument_list|,
name|uninstall
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|refreshRepository
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|uri
operator|==
literal|null
operator|||
name|uri
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
name|featuresService
operator|.
name|refreshRepository
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// regex support
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
name|URI
name|u
init|=
name|repository
operator|.
name|getURI
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|u
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|uris
operator|.
name|add
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|URI
name|u
range|:
name|uris
control|)
block|{
name|featuresService
operator|.
name|refreshRepository
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|noRefresh
parameter_list|)
throws|throws
name|Exception
block|{
name|EnumSet
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
name|featuresService
operator|.
name|installFeature
argument_list|(
name|name
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|noRefresh
parameter_list|,
name|boolean
name|noStart
parameter_list|)
throws|throws
name|Exception
block|{
name|EnumSet
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|noStart
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoStartBundles
argument_list|)
expr_stmt|;
block|}
name|featuresService
operator|.
name|installFeature
argument_list|(
name|name
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|featuresService
operator|.
name|installFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|boolean
name|noRefresh
parameter_list|)
throws|throws
name|Exception
block|{
name|EnumSet
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
name|featuresService
operator|.
name|installFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|installFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|boolean
name|noRefresh
parameter_list|,
name|boolean
name|noStart
parameter_list|)
throws|throws
name|Exception
block|{
name|EnumSet
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|noStart
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoStartBundles
argument_list|)
expr_stmt|;
block|}
name|featuresService
operator|.
name|installFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|infoFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|Feature
index|[]
name|features
init|=
name|featuresService
operator|.
name|getFeatures
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|infoFeature
argument_list|(
name|features
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|infoFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|Feature
index|[]
name|features
init|=
name|featuresService
operator|.
name|getFeatures
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
decl_stmt|;
return|return
name|infoFeature
argument_list|(
name|features
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|TabularData
name|infoFeature
parameter_list|(
name|Feature
index|[]
name|f
parameter_list|)
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|JmxFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
name|feature
range|:
name|f
control|)
block|{
name|boolean
name|installed
init|=
name|featuresService
operator|.
name|isInstalled
argument_list|(
name|feature
argument_list|)
decl_stmt|;
name|boolean
name|required
init|=
name|featuresService
operator|.
name|isRequired
argument_list|(
name|feature
argument_list|)
decl_stmt|;
name|JmxFeature
name|jmxFeature
init|=
operator|new
name|JmxFeature
argument_list|(
name|feature
argument_list|,
name|installed
argument_list|,
name|required
argument_list|)
decl_stmt|;
name|features
operator|.
name|add
argument_list|(
name|jmxFeature
argument_list|)
expr_stmt|;
block|}
return|return
name|JmxFeature
operator|.
name|tableFrom
argument_list|(
name|features
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|noRefresh
parameter_list|)
throws|throws
name|Exception
block|{
name|uninstallFeature
argument_list|(
name|name
argument_list|,
name|noRefresh
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|noRefresh
parameter_list|,
name|boolean
name|deleteConfigurations
parameter_list|)
throws|throws
name|Exception
block|{
name|EnumSet
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deleteConfigurations
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|DeleteConfigurations
argument_list|)
expr_stmt|;
block|}
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|name
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|boolean
name|noRefresh
parameter_list|)
throws|throws
name|Exception
block|{
name|uninstallFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|noRefresh
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|uninstallFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|boolean
name|noRefresh
parameter_list|,
name|boolean
name|deleteConfigurations
parameter_list|)
throws|throws
name|Exception
block|{
name|EnumSet
argument_list|<
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
argument_list|>
name|options
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|FeaturesService
operator|.
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|noRefresh
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|NoAutoRefreshBundles
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deleteConfigurations
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|FeaturesService
operator|.
name|Option
operator|.
name|DeleteConfigurations
argument_list|)
expr_stmt|;
block|}
name|featuresService
operator|.
name|uninstallFeature
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
specifier|public
name|FeaturesListener
name|getFeaturesListener
parameter_list|()
block|{
return|return
operator|new
name|FeaturesListener
argument_list|()
block|{
specifier|public
name|void
name|featureEvent
parameter_list|(
name|FeatureEvent
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|event
operator|.
name|isReplay
argument_list|()
condition|)
block|{
name|Notification
name|notification
init|=
operator|new
name|Notification
argument_list|(
name|FEATURE_EVENT_TYPE
argument_list|,
name|objectName
argument_list|,
name|sequenceNumber
operator|++
argument_list|)
decl_stmt|;
name|notification
operator|.
name|setUserData
argument_list|(
operator|new
name|JmxFeatureEvent
argument_list|(
name|event
argument_list|)
operator|.
name|asCompositeData
argument_list|()
argument_list|)
expr_stmt|;
name|sendNotification
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|repositoryEvent
parameter_list|(
name|RepositoryEvent
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|event
operator|.
name|isReplay
argument_list|()
condition|)
block|{
name|Notification
name|notification
init|=
operator|new
name|Notification
argument_list|(
name|REPOSITORY_EVENT_TYPE
argument_list|,
name|objectName
argument_list|,
name|sequenceNumber
operator|++
argument_list|)
decl_stmt|;
name|notification
operator|.
name|setUserData
argument_list|(
operator|new
name|JmxRepositoryEvent
argument_list|(
name|event
argument_list|)
operator|.
name|asCompositeData
argument_list|()
argument_list|)
expr_stmt|;
name|sendNotification
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
specifier|private
specifier|static
name|MBeanNotificationInfo
index|[]
name|getBroadcastInfo
parameter_list|()
block|{
name|String
name|type
init|=
name|Notification
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
name|MBeanNotificationInfo
name|info1
init|=
operator|new
name|MBeanNotificationInfo
argument_list|(
operator|new
name|String
index|[]
block|{
name|FEATURE_EVENT_EVENT_TYPE
block|}
argument_list|,
name|type
argument_list|,
literal|"Some features notification"
argument_list|)
decl_stmt|;
name|MBeanNotificationInfo
name|info2
init|=
operator|new
name|MBeanNotificationInfo
argument_list|(
operator|new
name|String
index|[]
block|{
name|REPOSITORY_EVENT_EVENT_TYPE
block|}
argument_list|,
name|type
argument_list|,
literal|"Some repository notification"
argument_list|)
decl_stmt|;
return|return
operator|new
name|MBeanNotificationInfo
index|[]
block|{
name|info1
block|,
name|info2
block|}
return|;
block|}
block|}
end_class

end_unit

