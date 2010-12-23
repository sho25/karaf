begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|webconsole
operator|.
name|internal
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

begin_comment
comment|/**  * This is the main, starting class of the Bundle. It initializes and disposes  * the Apache Web Console upon bundle lifecycle requests.  */
end_comment

begin_class
specifier|public
class|class
name|KarafOsgiManagerActivator
implements|implements
name|BundleActivator
block|{
specifier|private
name|KarafOsgiManager
name|osgiManager
decl_stmt|;
comment|/**      * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)      */
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|osgiManager
operator|=
operator|new
name|KarafOsgiManager
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)      */
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|arg0
parameter_list|)
block|{
if|if
condition|(
name|osgiManager
operator|!=
literal|null
condition|)
block|{
name|osgiManager
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

