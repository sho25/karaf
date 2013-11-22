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

begin_comment
comment|/**  * Bundles MBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|BundlesMBean
block|{
name|TabularData
name|getBundles
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
name|int
name|getStartLevel
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|setStartLevel
parameter_list|(
name|String
name|bundleId
parameter_list|,
name|int
name|bundleStartLevel
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|refresh
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
name|void
name|refresh
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|update
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|update
parameter_list|(
name|String
name|bundleId
parameter_list|,
name|String
name|location
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|resolve
parameter_list|()
throws|throws
name|MBeanException
function_decl|;
name|void
name|resolve
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|restart
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|long
name|install
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|long
name|install
parameter_list|(
name|String
name|url
parameter_list|,
name|boolean
name|start
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|start
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|stop
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|void
name|uninstall
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
function_decl|;
name|String
name|getDiag
parameter_list|(
name|long
name|bundleId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

