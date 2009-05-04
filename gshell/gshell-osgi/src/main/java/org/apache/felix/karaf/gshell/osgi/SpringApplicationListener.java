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
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|osgi
package|;
end_package

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
name|BundleListener
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
name|DisposableBean
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
name|osgi
operator|.
name|context
operator|.
name|BundleContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|event
operator|.
name|OsgiBundleApplicationContextEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|event
operator|.
name|OsgiBundleApplicationContextListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|event
operator|.
name|OsgiBundleContextFailedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|event
operator|.
name|OsgiBundleContextRefreshedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|extender
operator|.
name|event
operator|.
name|BootstrappingDependencyEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|service
operator|.
name|importer
operator|.
name|event
operator|.
name|OsgiServiceDependencyEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|service
operator|.
name|importer
operator|.
name|event
operator|.
name|OsgiServiceDependencyWaitStartingEvent
import|;
end_import

begin_class
specifier|public
class|class
name|SpringApplicationListener
implements|implements
name|OsgiBundleApplicationContextListener
implements|,
name|BundleListener
block|{
specifier|public
specifier|static
enum|enum
name|SpringState
block|{
name|Unknown
block|,
name|Waiting
block|,
name|Started
block|,
name|Failed
block|,     }
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SpringApplicationListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|SpringState
argument_list|>
name|states
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|SpringApplicationListener
parameter_list|()
block|{
name|this
operator|.
name|states
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Long
argument_list|,
name|SpringState
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SpringState
name|getSpringState
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|SpringState
name|state
init|=
name|states
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|!=
name|Bundle
operator|.
name|ACTIVE
condition|)
block|{
name|state
operator|=
name|SpringState
operator|.
name|Unknown
expr_stmt|;
block|}
return|return
name|state
return|;
block|}
specifier|public
name|void
name|onOsgiApplicationEvent
parameter_list|(
name|OsgiBundleApplicationContextEvent
name|event
parameter_list|)
block|{
name|SpringState
name|state
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|event
operator|instanceof
name|BootstrappingDependencyEvent
condition|)
block|{
name|OsgiServiceDependencyEvent
name|de
init|=
operator|(
operator|(
name|BootstrappingDependencyEvent
operator|)
name|event
operator|)
operator|.
name|getDependencyEvent
argument_list|()
decl_stmt|;
if|if
condition|(
name|de
operator|instanceof
name|OsgiServiceDependencyWaitStartingEvent
condition|)
block|{
name|state
operator|=
name|SpringState
operator|.
name|Waiting
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|OsgiBundleContextFailedEvent
condition|)
block|{
name|state
operator|=
name|SpringState
operator|.
name|Failed
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|OsgiBundleContextRefreshedEvent
condition|)
block|{
name|state
operator|=
name|SpringState
operator|.
name|Started
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Spring app state changed to "
operator|+
name|state
operator|+
literal|" for bundle "
operator|+
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|states
operator|.
name|put
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
block|}
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
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNINSTALLED
condition|)
block|{
name|states
operator|.
name|remove
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

