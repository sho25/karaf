begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|diagnostic
operator|.
name|common
package|;
end_package

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
name|OutputStreamWriter
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
name|Map
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
name|diagnostic
operator|.
name|core
operator|.
name|common
operator|.
name|TextDumpProvider
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

begin_comment
comment|/**  * Dump provider which produces file named bundles.txt with list of  * installed bundles and it's state.  */
end_comment

begin_class
specifier|public
class|class
name|BundleDumpProvider
extends|extends
name|TextDumpProvider
block|{
comment|/**      * Static map with state mask to string representation.      */
specifier|private
specifier|static
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|stateMap
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Map bundle states to string representation.      */
static|static
block|{
name|stateMap
operator|.
name|put
argument_list|(
literal|0x00000001
argument_list|,
literal|"UNINSTALLED"
argument_list|)
expr_stmt|;
name|stateMap
operator|.
name|put
argument_list|(
literal|0x00000002
argument_list|,
literal|"INSTALLED"
argument_list|)
expr_stmt|;
name|stateMap
operator|.
name|put
argument_list|(
literal|0x00000004
argument_list|,
literal|"RESOLVED"
argument_list|)
expr_stmt|;
name|stateMap
operator|.
name|put
argument_list|(
literal|0x00000008
argument_list|,
literal|"STARTING"
argument_list|)
expr_stmt|;
name|stateMap
operator|.
name|put
argument_list|(
literal|0x00000010
argument_list|,
literal|"STOPPING"
argument_list|)
expr_stmt|;
name|stateMap
operator|.
name|put
argument_list|(
literal|0x00000020
argument_list|,
literal|"ACTIVE"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Bundle context.      */
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
comment|/**      * Creates new bundle information file.      *        * @param context Bundle context to access framework state.      */
specifier|public
name|BundleDumpProvider
parameter_list|(
name|BundleContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
literal|"bundles.txt"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|protected
name|void
name|writeDump
parameter_list|(
name|OutputStreamWriter
name|writer
parameter_list|)
throws|throws
name|IOException
block|{
comment|// get bundle states
name|Bundle
index|[]
name|bundles
init|=
name|bundleContext
operator|.
name|getBundles
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Number of installed bundles "
operator|+
name|bundles
operator|.
name|length
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
comment|// create file header
name|writer
operator|.
name|write
argument_list|(
literal|"Id\tSymbolic name\tState\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
comment|// write row :)
name|writer
operator|.
name|write
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|"\t"
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|"\t"
operator|+
name|stateMap
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getState
argument_list|()
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

