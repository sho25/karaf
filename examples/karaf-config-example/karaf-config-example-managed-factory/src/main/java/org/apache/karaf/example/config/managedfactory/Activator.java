begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|example
operator|.
name|config
operator|.
name|managedfactory
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
name|Constants
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
name|service
operator|.
name|cm
operator|.
name|ConfigurationException
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
name|ManagedServiceFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|ServiceRegistration
argument_list|<
name|ManagedServiceFactory
argument_list|>
name|registration
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
block|{
name|ManagedServiceFactory
name|factory
init|=
operator|new
name|ManagedServiceFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Example Managed Factory"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updated
parameter_list|(
name|String
name|pid
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|ConfigurationException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"New configuration with pid "
operator|+
name|pid
argument_list|)
expr_stmt|;
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|properties
operator|.
name|keys
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|key
operator|+
literal|" = "
operator|+
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleted
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Delete configuration with pid "
operator|+
name|pid
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serviceProperties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|serviceProperties
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
literal|"org.apache.karaf.example.config"
argument_list|)
expr_stmt|;
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|ManagedServiceFactory
operator|.
name|class
argument_list|,
name|factory
argument_list|,
name|serviceProperties
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
block|{
if|if
condition|(
name|registration
operator|!=
literal|null
condition|)
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

