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
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|HttpMBeanImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRegisterMBean
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpMBeanImpl
name|httpMBean
init|=
operator|new
name|HttpMBeanImpl
argument_list|(
operator|new
name|ServletServiceImpl
argument_list|(
operator|new
name|ServletEventHandler
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ProxyServiceImpl
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|MBeanServer
name|mbeanServer
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|mbeanServer
operator|.
name|registerMBean
argument_list|(
name|httpMBean
argument_list|,
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf:type=http,name=root"
argument_list|)
argument_list|)
expr_stmt|;
name|TabularData
name|data
init|=
name|httpMBean
operator|.
name|getServlets
argument_list|()
decl_stmt|;
block|}
block|}
end_class

end_unit

