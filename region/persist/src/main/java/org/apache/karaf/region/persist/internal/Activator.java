begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|region
operator|.
name|persist
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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
name|region
operator|.
name|persist
operator|.
name|RegionsPersistence
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
name|SingleServiceTracker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraph
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
name|Bundle
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
name|ServiceRegistration
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

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Activator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SingleServiceTracker
argument_list|<
name|RegionDigraph
argument_list|>
name|tracker
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|RegionsPersistenceImpl
argument_list|>
name|persistence
init|=
operator|new
name|AtomicReference
argument_list|<
name|RegionsPersistenceImpl
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|RegionsBundleTracker
argument_list|>
name|bundleTracker
init|=
operator|new
name|AtomicReference
argument_list|<
name|RegionsBundleTracker
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ServiceRegistration
argument_list|<
name|RegionsPersistence
argument_list|>
name|reg
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|tracker
operator|=
operator|new
name|SingleServiceTracker
argument_list|<
name|RegionDigraph
argument_list|>
argument_list|(
name|bundleContext
argument_list|,
name|RegionDigraph
operator|.
name|class
argument_list|,
operator|new
name|SingleServiceTracker
operator|.
name|SingleServiceListener
argument_list|()
block|{
specifier|public
name|void
name|serviceFound
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found RegionDigraph service, initializing"
argument_list|)
expr_stmt|;
name|RegionDigraph
name|regionDigraph
init|=
name|tracker
operator|.
name|getService
argument_list|()
decl_stmt|;
name|Bundle
name|framework
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|RegionsPersistenceImpl
name|persistence
init|=
literal|null
decl_stmt|;
try|try
block|{
name|persistence
operator|=
operator|new
name|RegionsPersistenceImpl
argument_list|(
name|regionDigraph
argument_list|,
name|framework
argument_list|)
expr_stmt|;
name|reg
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|RegionsPersistence
operator|.
name|class
argument_list|,
name|persistence
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|RegionsBundleTracker
name|bundleTracker
init|=
operator|new
name|RegionsBundleTracker
argument_list|()
decl_stmt|;
name|bundleTracker
operator|.
name|start
argument_list|(
name|bundleContext
argument_list|,
name|persistence
argument_list|)
expr_stmt|;
name|Activator
operator|.
name|this
operator|.
name|bundleTracker
operator|.
name|set
argument_list|(
name|bundleTracker
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Could not create RegionsPersistenceImpl"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|Activator
operator|.
name|this
operator|.
name|persistence
operator|.
name|set
argument_list|(
name|persistence
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|serviceLost
parameter_list|()
block|{
if|if
condition|(
name|reg
operator|!=
literal|null
condition|)
block|{
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|reg
operator|=
literal|null
expr_stmt|;
block|}
name|Activator
operator|.
name|this
operator|.
name|persistence
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Activator
operator|.
name|this
operator|.
name|bundleTracker
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|serviceReplaced
parameter_list|()
block|{
comment|//??
block|}
block|}
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|()
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
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|persistence
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bundleTracker
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

