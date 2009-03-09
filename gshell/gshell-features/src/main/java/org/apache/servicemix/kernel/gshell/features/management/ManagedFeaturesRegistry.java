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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|features
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
name|ConcurrentHashMap
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|features
operator|.
name|FeaturesRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_comment
comment|/**  * The FeaturesServiceRegistry maintains the managed Features and Repositories  * for JMX management.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Features Service Registry and Management"
argument_list|)
specifier|public
class|class
name|ManagedFeaturesRegistry
implements|implements
name|InitializingBean
implements|,
name|FeaturesRegistry
block|{
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ManagedFeaturesRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|NamingStrategy
name|namingStrategy
decl_stmt|;
specifier|private
name|ManagementAgent
name|managementAgent
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedFeature
argument_list|>
name|availableFeatures
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedFeature
argument_list|>
name|installedFeatures
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|repositories
decl_stmt|;
specifier|private
name|boolean
name|mbeanServerInitialized
decl_stmt|;
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
annotation|@
name|ManagedOperation
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
name|ManagedOperation
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
name|ManagedOperation
specifier|public
name|void
name|installRepository
parameter_list|(
name|String
name|repositoryUri
parameter_list|)
throws|throws
name|Exception
block|{
name|featuresService
operator|.
name|addRepository
argument_list|(
operator|new
name|URI
argument_list|(
name|repositoryUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ManagedFeaturesRegistry
parameter_list|()
block|{
name|availableFeatures
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|ManagedFeature
argument_list|>
argument_list|()
expr_stmt|;
name|installedFeatures
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|ManagedFeature
argument_list|>
argument_list|()
expr_stmt|;
name|repositories
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedFeature
argument_list|>
name|getAvailableFeatures
parameter_list|()
block|{
return|return
name|availableFeatures
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedFeature
argument_list|>
name|getInstalledFeatures
parameter_list|()
block|{
return|return
name|installedFeatures
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|getRepositories
parameter_list|()
block|{
return|return
name|repositories
return|;
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
name|void
name|setNamingStrategy
parameter_list|(
name|NamingStrategy
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
expr_stmt|;
block|}
specifier|public
name|void
name|setManagementAgent
parameter_list|(
name|ManagementAgent
name|managementAgent
parameter_list|)
block|{
name|this
operator|.
name|managementAgent
operator|=
name|managementAgent
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
try|try
block|{
name|ManagedFeature
name|mf
init|=
operator|new
name|ManagedFeature
argument_list|(
name|feature
argument_list|,
name|featuresService
argument_list|)
decl_stmt|;
name|availableFeatures
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|,
name|mf
argument_list|)
expr_stmt|;
if|if
condition|(
name|mbeanServerInitialized
condition|)
block|{
name|managementAgent
operator|.
name|register
argument_list|(
name|mf
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|mf
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to register managed feature: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
try|try
block|{
name|ManagedFeature
name|mf
init|=
name|availableFeatures
operator|.
name|remove
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mbeanServerInitialized
condition|)
block|{
name|managementAgent
operator|.
name|unregister
argument_list|(
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|mf
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to unregister managed feature: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|registerInstalled
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
try|try
block|{
name|ManagedFeature
name|mf
init|=
operator|new
name|ManagedFeature
argument_list|(
name|feature
argument_list|,
name|featuresService
argument_list|)
decl_stmt|;
name|installedFeatures
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|,
name|mf
argument_list|)
expr_stmt|;
if|if
condition|(
name|mbeanServerInitialized
condition|)
block|{
name|managementAgent
operator|.
name|register
argument_list|(
name|mf
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|mf
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to register managed feature: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unregisterInstalled
parameter_list|(
name|Feature
name|feature
parameter_list|)
block|{
try|try
block|{
name|ManagedFeature
name|mf
init|=
name|installedFeatures
operator|.
name|remove
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mbeanServerInitialized
condition|)
block|{
name|managementAgent
operator|.
name|unregister
argument_list|(
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|mf
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to unregister managed feature: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|register
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
try|try
block|{
name|ManagedRepository
name|mr
init|=
operator|new
name|ManagedRepository
argument_list|(
name|repository
argument_list|,
name|featuresService
argument_list|)
decl_stmt|;
name|repositories
operator|.
name|put
argument_list|(
name|repository
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|mr
argument_list|)
expr_stmt|;
for|for
control|(
name|Feature
name|f
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
comment|// TODO: Associate the feature with the Repo?
name|register
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mbeanServerInitialized
condition|)
block|{
name|managementAgent
operator|.
name|register
argument_list|(
name|mr
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|mr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to register managed repository: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
try|try
block|{
name|ManagedRepository
name|mr
init|=
name|repositories
operator|.
name|remove
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
for|for
control|(
name|Feature
name|f
range|:
name|repository
operator|.
name|getFeatures
argument_list|()
control|)
block|{
comment|// TODO: Associate the feature with the Repo?
name|unregister
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mbeanServerInitialized
condition|)
block|{
name|managementAgent
operator|.
name|unregister
argument_list|(
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|mr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to unregister managed repository: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|managementAgent
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"managementAgent must not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|namingStrategy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"namingStrategy must not be null"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|registerMBeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|,
name|Map
name|props
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|mbeanServer
operator|!=
literal|null
condition|)
block|{
name|mbeanServerInitialized
operator|=
literal|true
expr_stmt|;
block|}
name|managementAgent
operator|.
name|register
argument_list|(
name|this
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ManagedRepository
name|repository
range|:
name|repositories
operator|.
name|values
argument_list|()
control|)
block|{
name|managementAgent
operator|.
name|register
argument_list|(
name|repository
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ManagedFeature
name|feature
range|:
name|availableFeatures
operator|.
name|values
argument_list|()
control|)
block|{
name|managementAgent
operator|.
name|register
argument_list|(
name|feature
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ManagedFeature
name|feature
range|:
name|installedFeatures
operator|.
name|values
argument_list|()
control|)
block|{
name|installedFeatures
operator|.
name|put
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|,
name|feature
argument_list|)
expr_stmt|;
name|managementAgent
operator|.
name|register
argument_list|(
name|feature
argument_list|,
name|namingStrategy
operator|.
name|getObjectName
argument_list|(
name|feature
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

