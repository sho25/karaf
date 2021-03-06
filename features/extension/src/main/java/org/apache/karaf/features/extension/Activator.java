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
name|extension
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|BundleEvent
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
name|osgi
operator|.
name|framework
operator|.
name|SynchronousBundleListener
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
name|resolver
operator|.
name|ResolverHookFactory
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
name|FrameworkWiring
import|;
end_import

begin_class
specifier|public
class|class
name|Activator
implements|implements
name|BundleActivator
implements|,
name|SynchronousBundleListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|WIRING_PATH
init|=
literal|"wiring"
decl_stmt|;
specifier|private
name|StoredWiringResolver
name|resolver
decl_stmt|;
specifier|private
name|BundleContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|resolver
operator|=
operator|new
name|StoredWiringResolver
argument_list|(
name|context
operator|.
name|getDataFile
argument_list|(
name|WIRING_PATH
argument_list|)
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addBundleListener
argument_list|(
name|this
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
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|==
literal|0
operator|&&
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|STARTED
condition|)
block|{
name|resolveAll
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|RESOLVED
condition|)
block|{
name|resolver
operator|.
name|update
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNRESOLVED
condition|)
block|{
name|resolver
operator|.
name|delete
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|resolveAll
parameter_list|()
block|{
name|ServiceRegistration
argument_list|<
name|ResolverHookFactory
argument_list|>
name|registration
init|=
name|context
operator|.
name|registerService
argument_list|(
name|ResolverHookFactory
operator|.
name|class
argument_list|,
parameter_list|(
name|triggers
parameter_list|)
lambda|->
name|resolver
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|adapt
argument_list|(
name|FrameworkWiring
operator|.
name|class
argument_list|)
operator|.
name|resolveBundles
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|context
operator|.
name|getBundles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

