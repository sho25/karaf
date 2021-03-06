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
name|service
operator|.
name|guard
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
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
name|Filter
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
name|hooks
operator|.
name|service
operator|.
name|EventListenerHook
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
name|hooks
operator|.
name|service
operator|.
name|FindHook
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|// This bundle is quite low-level and benefits from starting early in the process. Therefore it does not depend
end_comment

begin_comment
comment|// on Blueprint but rather uses direct OSGi framework APIs and Service Trackers ...
end_comment

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|GuardingEventHook
name|guardingEventHook
decl_stmt|;
name|GuardingFindHook
name|guardingFindHook
decl_stmt|;
name|GuardProxyCatalog
name|guardProxyCatalog
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|f
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|GuardProxyCatalog
operator|.
name|KARAF_SECURED_SERVICES_SYSPROP
argument_list|)
decl_stmt|;
name|Filter
name|securedServicesFilter
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
comment|// no services need to be secured
name|logger
operator|.
name|info
argument_list|(
literal|"No role-based security for services as its system property is not set: {}"
argument_list|,
name|GuardProxyCatalog
operator|.
name|KARAF_SECURED_SERVICES_SYSPROP
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|securedServicesFilter
operator|=
name|bundleContext
operator|.
name|createFilter
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Adding role-based security to services with filter: {}"
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
name|guardProxyCatalog
operator|=
operator|new
name|GuardProxyCatalog
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|guardingEventHook
operator|=
operator|new
name|GuardingEventHook
argument_list|(
name|bundleContext
argument_list|,
name|guardProxyCatalog
argument_list|,
name|securedServicesFilter
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|registerService
argument_list|(
name|EventListenerHook
operator|.
name|class
argument_list|,
name|guardingEventHook
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|guardingFindHook
operator|=
operator|new
name|GuardingFindHook
argument_list|(
name|bundleContext
argument_list|,
name|guardProxyCatalog
argument_list|,
name|securedServicesFilter
argument_list|)
expr_stmt|;
name|bundleContext
operator|.
name|registerService
argument_list|(
name|FindHook
operator|.
name|class
argument_list|,
name|guardingFindHook
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|guardProxyCatalog
operator|!=
literal|null
condition|)
block|{
name|guardProxyCatalog
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

