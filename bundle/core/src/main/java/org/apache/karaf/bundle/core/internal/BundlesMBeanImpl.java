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
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|bundle
operator|.
name|core
operator|.
name|BundleInfo
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
name|bundle
operator|.
name|core
operator|.
name|BundleService
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
name|bundle
operator|.
name|core
operator|.
name|BundlesMBean
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
name|shell
operator|.
name|support
operator|.
name|ShellUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|startlevel
operator|.
name|BundleStartLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|wiring
operator|.
name|FrameworkWiring
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * BundlesMBean implementation.  */
end_comment

begin_class
specifier|public
class|class
name|BundlesMBeanImpl
extends|extends
name|StandardMBean
implements|implements
name|BundlesMBean
block|{
specifier|private
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BundlesMBeanImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|private
specifier|final
name|BundleService
name|bundleService
decl_stmt|;
specifier|public
name|BundlesMBeanImpl
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|BundleService
name|bundleService
parameter_list|)
throws|throws
name|NotCompliantMBeanException
block|{
name|super
argument_list|(
name|BundlesMBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|bundleService
operator|=
name|bundleService
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|Bundle
argument_list|>
name|selectBundles
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|this
operator|.
name|bundleService
operator|.
name|selectBundles
argument_list|(
name|ids
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|TabularData
name|getBundles
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|bundleType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Bundle"
argument_list|,
literal|"OSGi Bundle"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|,
literal|"Name"
block|,
literal|"Version"
block|,
literal|"Start Level"
block|,
literal|"State"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID of the Bundle"
block|,
literal|"Name of the Bundle"
block|,
literal|"Version of the Bundle"
block|,
literal|"Start Level of the Bundle"
block|,
literal|"Current State of the Bundle"
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
block|}
argument_list|)
decl_stmt|;
name|TabularType
name|tableType
init|=
operator|new
name|TabularType
argument_list|(
literal|"Bundles"
argument_list|,
literal|"Tables of all bundles"
argument_list|,
name|bundleType
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
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bundles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Bundle
name|bundle
init|=
name|bundles
index|[
name|i
index|]
decl_stmt|;
name|BundleInfo
name|info
init|=
name|bundleService
operator|.
name|getInfo
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|String
name|bundleStateString
init|=
name|info
operator|.
name|getState
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|bundleType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|,
literal|"Name"
block|,
literal|"Version"
block|,
literal|"Start Level"
block|,
literal|"State"
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
name|getSymbolicName
argument_list|()
block|,
name|info
operator|.
name|getVersion
argument_list|()
block|,
name|info
operator|.
name|getStartLevel
argument_list|()
block|,
name|bundleStateString
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
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|int
name|getStartLevel
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundles
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Provided bundle Id doesn't return any bundle or more than one bundle selected"
argument_list|)
throw|;
block|}
return|return
name|getBundleStartLevel
argument_list|(
name|bundles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|getStartLevel
argument_list|()
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
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
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|getBundleStartLevel
argument_list|(
name|bundle
argument_list|)
operator|.
name|setStartLevel
argument_list|(
name|bundleStartLevel
argument_list|)
expr_stmt|;
block|}
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|BundleStartLevel
name|getBundleStartLevel
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
return|return
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|refresh
parameter_list|()
throws|throws
name|MBeanException
block|{
name|getFrameworkWiring
argument_list|()
operator|.
name|refreshBundles
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|refresh
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getFrameworkWiring
argument_list|()
operator|.
name|refreshBundles
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getFrameworkWiring
argument_list|()
operator|.
name|refreshBundles
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
block|}
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|update
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
name|update
argument_list|(
name|bundleId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
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
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|bundle
operator|.
name|update
argument_list|()
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|bundles
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Provided bundle Id doesn't return any bundle or more than one bundle selected"
argument_list|)
throw|;
block|}
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|location
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|bundles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|update
argument_list|(
name|is
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|resolve
parameter_list|()
throws|throws
name|MBeanException
block|{
name|getFrameworkWiring
argument_list|()
operator|.
name|resolveBundles
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|resolve
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
name|getFrameworkWiring
argument_list|()
operator|.
name|resolveBundles
argument_list|(
name|bundles
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|FrameworkWiring
name|getFrameworkWiring
parameter_list|()
block|{
return|return
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
literal|0
argument_list|)
operator|.
name|adapt
argument_list|(
name|FrameworkWiring
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|restart
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|bundle
operator|.
name|stop
argument_list|()
expr_stmt|;
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|long
name|install
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MBeanException
block|{
return|return
name|install
argument_list|(
name|url
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
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
block|{
try|try
block|{
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
condition|)
block|{
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
return|return
name|bundle
operator|.
name|getBundleId
argument_list|()
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|start
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|bundle
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|uninstall
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|selectBundles
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|bundle
operator|.
name|uninstall
argument_list|()
expr_stmt|;
block|}
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|bundleContext
return|;
block|}
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|TabularData
name|getDiag
parameter_list|()
throws|throws
name|MBeanException
block|{
try|try
block|{
name|CompositeType
name|diagType
init|=
operator|new
name|CompositeType
argument_list|(
literal|"Diag"
argument_list|,
literal|"OSGi Bundle Diag"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"Status"
block|,
literal|"Diag"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Bundle Name"
block|,
literal|"Current Status"
block|,
literal|"Diagnostic"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
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
literal|"Diagnostics"
argument_list|,
literal|"Tables of all bundles diagnostic"
argument_list|,
name|diagType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
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
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|BundleInfo
name|bundleInfo
init|=
name|bundleService
operator|.
name|getInfo
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|ShellUtil
operator|.
name|getBundleName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|String
name|status
init|=
name|bundleInfo
operator|.
name|getState
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|diag
init|=
name|bundleService
operator|.
name|getDiag
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|diagType
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"Status"
block|,
literal|"Diag"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|status
block|,
name|diag
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDiag
parameter_list|(
name|long
name|bundleId
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Bundle with id "
operator|+
name|bundleId
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
name|bundleService
operator|.
name|getDiag
argument_list|(
name|bundle
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getStatus
parameter_list|(
name|String
name|bundleId
parameter_list|)
throws|throws
name|MBeanException
block|{
try|try
block|{
return|return
name|bundleService
operator|.
name|getStatus
argument_list|(
name|bundleId
argument_list|)
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
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

