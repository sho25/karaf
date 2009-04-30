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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|osgi
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|geronimo
operator|.
name|gshell
operator|.
name|clp
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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
operator|.
name|OsgiCommandSupport
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
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Bundle IDs"
argument_list|)
name|List
argument_list|<
name|Long
argument_list|>
name|ids
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--force"
argument_list|)
name|boolean
name|force
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ids
operator|!=
literal|null
operator|&&
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|long
name|id
range|:
name|ids
control|)
block|{
name|Bundle
name|bundle
init|=
name|getBundleContext
argument_list|()
operator|.
name|getBundle
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|==
literal|null
condition|)
block|{
name|io
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Bundle ID"
operator|+
name|id
operator|+
literal|" is invalid"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|force
operator|||
operator|!
name|Util
operator|.
name|isASystemBundle
argument_list|(
name|getBundleContext
argument_list|()
argument_list|,
name|bundle
argument_list|)
operator|||
name|Util
operator|.
name|accessToSystemBundleIsAllowed
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|io
argument_list|)
condition|)
block|{
name|bundles
operator|.
name|add
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|doExecute
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
return|return
name|Result
operator|.
name|SUCCESS
return|;
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
block|}
end_class

end_unit

