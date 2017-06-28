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
name|features
operator|.
name|internal
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|Feature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraph
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
name|BundleException
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
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Wire
import|;
end_import

begin_interface
specifier|public
interface|interface
name|BundleInstallSupport
block|{
name|void
name|print
parameter_list|(
name|String
name|message
parameter_list|,
name|boolean
name|verbose
parameter_list|)
function_decl|;
name|void
name|refreshPackages
parameter_list|(
name|Collection
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
name|Bundle
name|installBundle
parameter_list|(
name|String
name|region
parameter_list|,
name|String
name|uri
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|BundleException
function_decl|;
name|void
name|updateBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|String
name|uri
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|BundleException
function_decl|;
name|void
name|uninstall
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|BundleException
function_decl|;
name|void
name|startBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|BundleException
function_decl|;
name|void
name|stopBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|int
name|options
parameter_list|)
throws|throws
name|BundleException
function_decl|;
name|void
name|setBundleStartLevel
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|int
name|startLevel
parameter_list|)
function_decl|;
name|void
name|resolveBundles
parameter_list|(
name|Set
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|,
name|Map
argument_list|<
name|Resource
argument_list|,
name|List
argument_list|<
name|Wire
argument_list|>
argument_list|>
name|wiring
parameter_list|,
name|Map
argument_list|<
name|Resource
argument_list|,
name|Bundle
argument_list|>
name|resToBnd
parameter_list|)
function_decl|;
name|void
name|replaceDigraph
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|>
argument_list|>
name|policies
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|BundleException
throws|,
name|InvalidSyntaxException
function_decl|;
name|void
name|saveState
parameter_list|()
function_decl|;
name|RegionDigraph
name|getDiGraphCopy
parameter_list|()
throws|throws
name|BundleException
function_decl|;
name|void
name|installConfigs
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidSyntaxException
function_decl|;
name|void
name|installLibraries
parameter_list|(
name|Feature
name|feature
parameter_list|)
throws|throws
name|IOException
function_decl|;
name|File
name|getDataFile
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|FrameworkInfo
name|getInfo
parameter_list|()
function_decl|;
class|class
name|FrameworkInfo
block|{
specifier|public
name|Bundle
name|ourBundle
decl_stmt|;
specifier|public
name|Bundle
name|systemBundle
decl_stmt|;
specifier|public
name|int
name|initialBundleStartLevel
decl_stmt|;
specifier|public
name|int
name|currentStartLevel
decl_stmt|;
specifier|public
name|Map
argument_list|<
name|Long
argument_list|,
name|Bundle
argument_list|>
name|bundles
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
block|}
block|}
end_interface

end_unit
