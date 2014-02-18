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
name|command
package|;
end_package

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
name|shell
operator|.
name|commands
operator|.
name|Argument
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
name|console
operator|.
name|OsgiCommandSupport
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
name|inject
operator|.
name|Reference
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
name|util
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

begin_class
specifier|public
specifier|abstract
class|class
name|BundlesCommand
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"ids"
argument_list|,
name|description
operator|=
literal|"The list of bundle (identified by IDs or name or name/version) separated by whitespaces"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|ids
decl_stmt|;
name|boolean
name|defaultAllBundles
init|=
literal|true
decl_stmt|;
annotation|@
name|Reference
name|BundleService
name|bundleService
decl_stmt|;
specifier|public
name|BundlesCommand
parameter_list|(
name|boolean
name|defaultAllBundles
parameter_list|)
block|{
name|this
operator|.
name|defaultAllBundles
operator|=
name|defaultAllBundles
expr_stmt|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|doExecute
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|protected
name|Object
name|doExecute
parameter_list|(
name|boolean
name|force
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
name|bundleService
operator|.
name|selectBundles
argument_list|(
name|ids
argument_list|,
name|defaultAllBundles
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|force
condition|)
block|{
name|assertNoSystemBundles
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
block|}
name|doExecute
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|assertNoSystemBundles
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
if|if
condition|(
name|ShellUtil
operator|.
name|isASystemBundle
argument_list|(
name|bundleContext
argument_list|,
name|bundle
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Access to system bundle "
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|" denied. You can override with -f"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|doExecute
parameter_list|(
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|public
name|void
name|setBundleService
parameter_list|(
name|BundleService
name|bundleService
parameter_list|)
block|{
name|this
operator|.
name|bundleService
operator|=
name|bundleService
expr_stmt|;
block|}
block|}
end_class

end_unit

