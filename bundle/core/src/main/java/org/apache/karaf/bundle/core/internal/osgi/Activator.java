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
name|bundle
operator|.
name|core
operator|.
name|internal
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|bundle
operator|.
name|core
operator|.
name|BundleService
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
name|bundle
operator|.
name|core
operator|.
name|BundleStateService
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
name|bundle
operator|.
name|core
operator|.
name|BundleWatcher
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
name|bundle
operator|.
name|core
operator|.
name|internal
operator|.
name|BundleServiceImpl
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
name|bundle
operator|.
name|core
operator|.
name|internal
operator|.
name|BundleWatcherImpl
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
name|bundle
operator|.
name|core
operator|.
name|internal
operator|.
name|BundlesMBeanImpl
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
name|bundle
operator|.
name|core
operator|.
name|internal
operator|.
name|MavenConfigService
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
name|util
operator|.
name|tracker
operator|.
name|BaseActivator
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|ProvideService
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|RequireService
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
name|util
operator|.
name|tracker
operator|.
name|annotation
operator|.
name|Services
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTrackerCustomizer
import|;
end_import

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
annotation|@
name|RequireService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
argument_list|,
name|provides
operator|=
annotation|@
name|ProvideService
argument_list|(
name|BundleService
operator|.
name|class
argument_list|)
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
block|{
specifier|private
name|ServiceTracker
argument_list|<
name|BundleStateService
argument_list|,
name|BundleStateService
argument_list|>
name|bundleStateServicesTracker
decl_stmt|;
specifier|private
name|BundleWatcherImpl
name|bundleWatcher
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationAdmin
name|configurationAdmin
init|=
name|getTrackedService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationAdmin
operator|==
literal|null
condition|)
block|{
return|return;
block|}
specifier|final
name|BundleServiceImpl
name|bundleService
init|=
operator|new
name|BundleServiceImpl
argument_list|(
name|bundleContext
argument_list|)
decl_stmt|;
name|register
argument_list|(
name|BundleService
operator|.
name|class
argument_list|,
name|bundleService
argument_list|)
expr_stmt|;
name|bundleStateServicesTracker
operator|=
operator|new
name|ServiceTracker
argument_list|<
name|BundleStateService
argument_list|,
name|BundleStateService
argument_list|>
argument_list|(
name|bundleContext
argument_list|,
name|BundleStateService
operator|.
name|class
argument_list|,
operator|new
name|ServiceTrackerCustomizer
argument_list|<
name|BundleStateService
argument_list|,
name|BundleStateService
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|BundleStateService
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|BundleStateService
argument_list|>
name|reference
parameter_list|)
block|{
name|BundleStateService
name|service
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|bundleService
operator|.
name|registerBundleStateService
argument_list|(
name|service
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|modifiedService
parameter_list|(
name|ServiceReference
argument_list|<
name|BundleStateService
argument_list|>
name|reference
parameter_list|,
name|BundleStateService
name|service
parameter_list|)
block|{             }
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|BundleStateService
argument_list|>
name|reference
parameter_list|,
name|BundleStateService
name|service
parameter_list|)
block|{
name|bundleService
operator|.
name|unregisterBundleStateService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bundleStateServicesTracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|bundleWatcher
operator|=
operator|new
name|BundleWatcherImpl
argument_list|(
name|bundleContext
argument_list|,
operator|new
name|MavenConfigService
argument_list|(
name|configurationAdmin
argument_list|)
argument_list|,
name|bundleService
argument_list|)
expr_stmt|;
name|bundleWatcher
operator|.
name|start
argument_list|()
expr_stmt|;
name|register
argument_list|(
name|BundleWatcher
operator|.
name|class
argument_list|,
name|bundleWatcher
argument_list|)
expr_stmt|;
name|BundlesMBeanImpl
name|bundlesMBeanImpl
init|=
operator|new
name|BundlesMBeanImpl
argument_list|(
name|bundleContext
argument_list|,
name|bundleService
argument_list|)
decl_stmt|;
name|registerMBean
argument_list|(
name|bundlesMBeanImpl
argument_list|,
literal|"type=bundle"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
if|if
condition|(
name|bundleStateServicesTracker
operator|!=
literal|null
condition|)
block|{
name|bundleStateServicesTracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|bundleStateServicesTracker
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|bundleWatcher
operator|!=
literal|null
condition|)
block|{
name|bundleWatcher
operator|.
name|stop
argument_list|()
expr_stmt|;
name|bundleWatcher
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

