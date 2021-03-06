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
operator|.
name|internal
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
name|web
operator|.
name|WebBundle
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
name|web
operator|.
name|WebContainerService
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
name|web
operator|.
name|management
operator|.
name|WebMBean
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
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
comment|/**  * Implementation of the Web MBean.  */
end_comment

begin_class
specifier|public
class|class
name|WebMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|WebMBean
block|{
specifier|private
name|WebContainerService
name|webContainerService
decl_stmt|;
specifier|public
name|WebMBeanImpl
parameter_list|()
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|WebMBean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setWebContainerService
parameter_list|(
name|WebContainerService
name|webContainerService
parameter_list|)
block|{
name|this
operator|.
name|webContainerService
operator|=
name|webContainerService
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|getWebBundles
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|webType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Web Bundle"
argument_list|,
literal|"An OSGi Web bundle"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|,
literal|"State"
block|,
literal|"Web-State"
block|,
literal|"Level"
block|,
literal|"Web-ContextPath"
block|,
literal|"Name"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID of the bundle"
block|,
literal|"OSGi state of the bundle"
block|,
literal|"Web state of the bundle"
block|,
literal|"Start level of the bundle"
block|,
literal|"Web context path"
block|,
literal|"Name of the bundle"
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
name|INTEGER
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
literal|"Web Bundles"
argument_list|,
literal|"Table of web bundles"
argument_list|,
name|webType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
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
for|for
control|(
name|WebBundle
name|webBundle
range|:
name|webContainerService
operator|.
name|list
argument_list|()
control|)
block|{
try|try
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|webType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|,
literal|"State"
block|,
literal|"Web-State"
block|,
literal|"Level"
block|,
literal|"Web-ContextPath"
block|,
literal|"Name"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|webBundle
operator|.
name|getBundleId
argument_list|()
block|,
name|webBundle
operator|.
name|getState
argument_list|()
block|,
name|webBundle
operator|.
name|getWebState
argument_list|()
block|,
name|webBundle
operator|.
name|getLevel
argument_list|()
block|,
name|webBundle
operator|.
name|getContextPath
argument_list|()
block|,
name|webBundle
operator|.
name|getName
argument_list|()
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
name|void
name|install
parameter_list|(
name|String
name|location
parameter_list|,
name|String
name|contextPath
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|webContainerService
operator|.
name|install
argument_list|(
name|location
argument_list|,
name|contextPath
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
name|uninstall
parameter_list|(
name|Long
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Long
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
name|webContainerService
operator|.
name|uninstall
argument_list|(
name|list
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
name|uninstall
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|bundleIds
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|webContainerService
operator|.
name|uninstall
argument_list|(
name|bundleIds
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
name|start
parameter_list|(
name|Long
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Long
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
name|webContainerService
operator|.
name|start
argument_list|(
name|list
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
block|{
try|try
block|{
name|webContainerService
operator|.
name|start
argument_list|(
name|bundleIds
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
name|stop
parameter_list|(
name|Long
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Long
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|bundleId
argument_list|)
expr_stmt|;
name|webContainerService
operator|.
name|stop
argument_list|(
name|list
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
block|{
try|try
block|{
name|webContainerService
operator|.
name|stop
argument_list|(
name|bundleIds
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

