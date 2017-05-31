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
name|jndi
operator|.
name|internal
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
name|Hashtable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|spi
operator|.
name|InitialContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|proxy
operator|.
name|ProxyManager
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
name|jndi
operator|.
name|JndiService
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
name|jndi
operator|.
name|KarafInitialContextFactory
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
name|jndi
operator|.
name|internal
operator|.
name|JndiMBeanImpl
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
name|jndi
operator|.
name|internal
operator|.
name|JndiServiceImpl
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

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
annotation|@
name|RequireService
argument_list|(
name|ProxyManager
operator|.
name|class
argument_list|)
argument_list|,
name|provides
operator|=
annotation|@
name|ProvideService
argument_list|(
name|JndiService
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
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ProxyManager
name|proxyManager
init|=
name|getTrackedService
argument_list|(
name|ProxyManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|register
argument_list|(
name|InitialContextFactory
operator|.
name|class
argument_list|,
operator|new
name|KarafInitialContextFactory
argument_list|()
argument_list|)
expr_stmt|;
name|JndiServiceImpl
name|service
init|=
operator|new
name|JndiServiceImpl
argument_list|()
decl_stmt|;
name|service
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|service
operator|.
name|setProxyManager
argument_list|(
name|proxyManager
argument_list|)
expr_stmt|;
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
comment|// bind the JNDI service itself in the JNDI context
name|props
operator|.
name|put
argument_list|(
literal|"osgi.jndi.service.name"
argument_list|,
literal|"jndi"
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|JndiService
operator|.
name|class
argument_list|,
name|service
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|JndiMBeanImpl
name|mbean
init|=
operator|new
name|JndiMBeanImpl
argument_list|()
decl_stmt|;
name|mbean
operator|.
name|setJndiService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|registerMBean
argument_list|(
name|mbean
argument_list|,
literal|"type=jndi"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

