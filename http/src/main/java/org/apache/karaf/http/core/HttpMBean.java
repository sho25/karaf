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
name|http
operator|.
name|core
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * HTTP MBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|HttpMBean
block|{
comment|/**      * List details for servlets.      *      * @return A {@link TabularData} containing the servlets information.      * @throws MBeanException In case of MBean failure.      */
name|TabularData
name|getServlets
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * List configured HTTP proxies.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Proxy
argument_list|>
name|getProxies
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * List the available balancing policies.      */
name|Collection
argument_list|<
name|String
argument_list|>
name|getProxyBalancingPolicies
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Add a new HTTP proxy using URL, proxyTo and prefix.      */
name|void
name|addProxy
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|proxyTo
parameter_list|,
name|String
name|balancingPolicy
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Remove an existing HTTP proxy identified by URL.      */
name|void
name|removeProxy
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

