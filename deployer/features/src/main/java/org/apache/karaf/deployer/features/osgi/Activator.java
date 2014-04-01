begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|deployer
operator|.
name|features
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|fileinstall
operator|.
name|ArtifactUrlTransformer
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
name|deployer
operator|.
name|features
operator|.
name|FeatureDeploymentListener
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
name|deployer
operator|.
name|features
operator|.
name|FeatureURLHandler
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
name|osgi
operator|.
name|service
operator|.
name|url
operator|.
name|URLStreamHandlerService
import|;
end_import

begin_class
specifier|public
class|class
name|Activator
extends|extends
name|BaseActivator
block|{
specifier|private
name|FeatureDeploymentListener
name|listener
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|trackService
argument_list|(
name|FeaturesService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|FeaturesService
name|service
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
name|service
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"url.handler.protocol"
argument_list|,
literal|"feature"
argument_list|)
expr_stmt|;
name|FeatureURLHandler
name|handler
init|=
operator|new
name|FeatureURLHandler
argument_list|()
decl_stmt|;
name|register
argument_list|(
name|URLStreamHandlerService
operator|.
name|class
argument_list|,
name|handler
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|listener
operator|=
operator|new
name|FeatureDeploymentListener
argument_list|()
expr_stmt|;
name|listener
operator|.
name|setFeaturesService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|listener
operator|.
name|setBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|listener
operator|.
name|init
argument_list|()
expr_stmt|;
name|register
argument_list|(
operator|new
name|String
index|[]
block|{
name|ArtifactUrlTransformer
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|ArtifactListener
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|listener
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|listener
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

