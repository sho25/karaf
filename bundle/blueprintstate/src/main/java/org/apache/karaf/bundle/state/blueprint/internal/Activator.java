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
name|state
operator|.
name|blueprint
operator|.
name|internal
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
name|BundleStateService
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
name|BundleListener
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
name|blueprint
operator|.
name|container
operator|.
name|BlueprintListener
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
name|registration
decl_stmt|;
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|BlueprintStateService
name|service
init|=
operator|new
name|BlueprintStateService
argument_list|()
decl_stmt|;
name|String
index|[]
name|classes
init|=
operator|new
name|String
index|[]
block|{
name|BlueprintListener
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|BundleStateService
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|BundleListener
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
name|registration
operator|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|classes
argument_list|,
name|service
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|registration
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
