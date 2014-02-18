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
name|BundleState
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
name|Command
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
name|Service
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
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"diag"
argument_list|,
name|description
operator|=
literal|"Displays diagnostic information why a bundle is not Active"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Diag
extends|extends
name|BundlesCommand
block|{
specifier|public
name|Diag
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
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
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
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
if|if
condition|(
name|info
operator|.
name|getState
argument_list|()
operator|==
name|BundleState
operator|.
name|Failure
operator|||
name|info
operator|.
name|getState
argument_list|()
operator|==
name|BundleState
operator|.
name|Waiting
operator|||
name|info
operator|.
name|getState
argument_list|()
operator|==
name|BundleState
operator|.
name|GracePeriod
operator|||
name|info
operator|.
name|getState
argument_list|()
operator|==
name|BundleState
operator|.
name|Installed
condition|)
block|{
name|String
name|title
init|=
name|ShellUtil
operator|.
name|getBundleName
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|ShellUtil
operator|.
name|getUnderlineString
argument_list|(
name|title
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Status: "
operator|+
name|info
operator|.
name|getState
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|this
operator|.
name|bundleService
operator|.
name|getDiag
argument_list|(
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

