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
name|config
operator|.
name|core
operator|.
name|impl
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
name|config
operator|.
name|core
operator|.
name|ConfigRepository
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
name|config
operator|.
name|core
operator|.
name|impl
operator|.
name|ConfigMBeanImpl
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
name|config
operator|.
name|core
operator|.
name|impl
operator|.
name|ConfigRepositoryImpl
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
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|ConfigRepository
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
name|ConfigRepository
name|configRepository
init|=
operator|new
name|ConfigRepositoryImpl
argument_list|(
name|configurationAdmin
argument_list|,
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|register
argument_list|(
name|ConfigRepository
operator|.
name|class
argument_list|,
name|configRepository
argument_list|)
expr_stmt|;
name|ConfigMBeanImpl
name|configMBean
init|=
operator|new
name|ConfigMBeanImpl
argument_list|()
decl_stmt|;
name|configMBean
operator|.
name|setConfigRepo
argument_list|(
name|configRepository
argument_list|)
expr_stmt|;
name|registerMBean
argument_list|(
name|configMBean
argument_list|,
literal|"type=config"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

