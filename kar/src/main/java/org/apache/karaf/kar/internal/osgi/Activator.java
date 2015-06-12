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
name|kar
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
name|features
operator|.
name|FeaturesService
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
name|kar
operator|.
name|KarService
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
name|kar
operator|.
name|internal
operator|.
name|KarServiceImpl
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
name|kar
operator|.
name|internal
operator|.
name|KarsMBeanImpl
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
name|Managed
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
name|ManagedService
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
name|FeaturesService
operator|.
name|class
argument_list|)
argument_list|,
name|provides
operator|=
annotation|@
name|ProvideService
argument_list|(
name|KarService
operator|.
name|class
argument_list|)
argument_list|)
annotation|@
name|Managed
argument_list|(
literal|"org.apache.karaf.kar"
argument_list|)
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
implements|implements
name|ManagedService
block|{
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesService
name|featuresService
init|=
name|getTrackedService
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|featuresService
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|noAutoRefreshBundles
init|=
name|getBoolean
argument_list|(
literal|"noAutoRefreshBundles"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|karStorage
init|=
name|getString
argument_list|(
literal|"storage"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.base"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|"kar"
argument_list|)
decl_stmt|;
name|KarServiceImpl
name|karService
init|=
operator|new
name|KarServiceImpl
argument_list|(
name|karStorage
argument_list|,
name|featuresService
argument_list|)
decl_stmt|;
name|karService
operator|.
name|setNoAutoRefreshBundles
argument_list|(
name|noAutoRefreshBundles
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|KarService
operator|.
name|class
argument_list|,
name|karService
argument_list|)
expr_stmt|;
name|KarsMBeanImpl
name|mbean
init|=
operator|new
name|KarsMBeanImpl
argument_list|()
decl_stmt|;
name|mbean
operator|.
name|setKarService
argument_list|(
name|karService
argument_list|)
expr_stmt|;
name|registerMBean
argument_list|(
name|mbean
argument_list|,
literal|"type=kar"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

