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
name|api
operator|.
name|action
operator|.
name|Option
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
name|MultiException
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

begin_comment
comment|/**  * Command related to bundles requiring read-write access to the bundle (including system bundle).  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BundlesCommandWithConfirmation
extends|extends
name|BundlesCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--force"
argument_list|,
name|aliases
operator|=
block|{
literal|"-f"
block|}
argument_list|,
name|description
operator|=
literal|"Forces the command to execute"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|boolean
name|force
decl_stmt|;
specifier|protected
name|String
name|errorMessage
init|=
literal|"Unable to execute command on bundle "
decl_stmt|;
specifier|public
name|BundlesCommandWithConfirmation
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
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
name|force
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
if|if
condition|(
name|bundles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No bundles specified."
argument_list|)
expr_stmt|;
return|return;
block|}
name|List
argument_list|<
name|Exception
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|Exception
argument_list|>
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
try|try
block|{
name|executeOnBundle
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exceptions
operator|.
name|add
argument_list|(
operator|new
name|Exception
argument_list|(
name|errorMessage
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|MultiException
operator|.
name|throwIf
argument_list|(
literal|"Error executing command on bundles"
argument_list|,
name|exceptions
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|executeOnBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

