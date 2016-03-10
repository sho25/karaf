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
name|web
operator|.
name|management
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
name|List
import|;
end_import

begin_comment
comment|/**  * Describe the Web MBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|WebMBean
block|{
comment|/**      * Return the list of web bundles.      *       * @return a tabular data of web bundles.      * @throws MBeanException in case of lookup failure.      */
name|TabularData
name|getWebBundles
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Start web context of the given web bundle (identified by ID).      *      * @param bundleId the bundle ID.      * @throws MBeanException in case of start failure.      */
name|void
name|start
parameter_list|(
name|Long
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Start web context of the given web bundles (identified by ID).      *       * @param bundleIds the list of bundle IDs.      *                  TODO use a BundleSelector service      * @throws MBeanException in case of start failure.      */
name|void
name|start
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Stop web context of the given web bundle (identified by ID).      *      * @param bundleId the bundle ID.      * @throws MBeanException in case of stop failure.      */
name|void
name|stop
parameter_list|(
name|Long
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
comment|/**      * Stop web context of the given web bundles (identified by ID).      *      * @param bundleIds the list of bundle IDs.      *                  TODO use a BundleSelector service      * @throws MBeanException in case of stop failure      */
name|void
name|stop
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
block|}
end_interface

end_unit

