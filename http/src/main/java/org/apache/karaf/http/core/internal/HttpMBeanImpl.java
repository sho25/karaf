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
name|util
operator|.
name|Arrays
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
name|List
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
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StandardMBean
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
name|CompositeData
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
name|CompositeDataSupport
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
name|CompositeType
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
name|OpenType
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
name|SimpleType
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
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
name|TabularType
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
name|http
operator|.
name|core
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Implementation of the HTTP MBean.  */
end_comment

begin_class
specifier|public
class|class
name|HttpMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|HttpMBean
block|{
specifier|private
name|ServletService
name|servletService
decl_stmt|;
specifier|private
name|ProxyService
name|proxyService
decl_stmt|;
specifier|public
name|HttpMBeanImpl
parameter_list|(
name|ServletService
name|servletService
parameter_list|,
name|ProxyService
name|proxyService
parameter_list|)
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|HttpMBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletService
operator|=
name|servletService
expr_stmt|;
name|this
operator|.
name|proxyService
operator|=
name|proxyService
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|getServlets
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|servletType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Servlet"
argument_list|,
literal|"HTTP Servlet"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Bundle-ID"
block|,
literal|"Servlet"
block|,
literal|"Servlet Name"
block|,
literal|"State"
block|,
literal|"Alias"
block|,
literal|"URL"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID of the bundle that registered the servlet"
block|,
literal|"Class name of the servlet"
block|,
literal|"Servlet Name"
block|,
literal|"Current state of the servlet"
block|,
literal|"Aliases of the servlet"
block|,
literal|"URL of the servlet"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"Servlets"
argument_list|,
literal|"Table of all HTTP servlets"
argument_list|,
name|servletType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Bundle-ID"
block|,
literal|"Servlet Name"
block|,
literal|"State"
block|}
argument_list|)
decl_stmt|;
name|TabularData
name|table
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|tableType
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ServletInfo
argument_list|>
name|servletInfos
init|=
name|servletService
operator|.
name|getServlets
argument_list|()
decl_stmt|;
for|for
control|(
name|ServletInfo
name|info
range|:
name|servletInfos
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|servletType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Bundle-ID"
block|,
literal|"Servlet"
block|,
literal|"Servlet Name"
block|,
literal|"State"
block|,
literal|"Alias"
block|,
literal|"URL"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|info
operator|.
name|getBundleId
argument_list|()
block|,
name|info
operator|.
name|getClassName
argument_list|()
block|,
name|info
operator|.
name|getName
argument_list|()
block|,
name|info
operator|.
name|getStateString
argument_list|()
block|,
name|info
operator|.
name|getAlias
argument_list|()
block|,
name|Arrays
operator|.
name|toString
argument_list|(
name|info
operator|.
name|getUrls
argument_list|()
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProxies
parameter_list|()
throws|throws
name|MBeanException
block|{
return|return
name|proxyService
operator|.
name|getProxies
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addProxy
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|proxyTo
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|proxyService
operator|.
name|addProxy
argument_list|(
name|url
argument_list|,
name|proxyTo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeProxy
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|proxyService
operator|.
name|removeProxy
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MBeanException
argument_list|(
literal|null
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

