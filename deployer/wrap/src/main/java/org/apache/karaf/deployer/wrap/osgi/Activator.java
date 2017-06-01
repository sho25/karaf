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
name|wrap
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
name|wrap
operator|.
name|WrapDeploymentListener
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
name|url
operator|.
name|URLStreamHandlerService
import|;
end_import

begin_class
annotation|@
name|Services
argument_list|(
name|requires
operator|=
block|{
annotation|@
name|RequireService
argument_list|(
name|value
operator|=
name|URLStreamHandlerService
operator|.
name|class
argument_list|,
name|filter
operator|=
literal|"(url.handler.protocol=wrap)"
argument_list|)
block|}
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
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"service.ranking"
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|register
argument_list|(
name|ArtifactUrlTransformer
operator|.
name|class
argument_list|,
operator|new
name|WrapDeploymentListener
argument_list|()
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

