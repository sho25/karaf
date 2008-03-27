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
name|gshell
operator|.
name|features
operator|.
name|internal
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|gshell
operator|.
name|features
operator|.
name|FeaturesService
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
name|gshell
operator|.
name|features
operator|.
name|Repository
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
name|command
operator|.
name|annotation
operator|.
name|CommandComponent
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

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"features:list"
argument_list|,
name|description
operator|=
literal|"List existing features."
argument_list|)
specifier|public
class|class
name|ListFeaturesCommand
extends|extends
name|FeaturesCommandSupport
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{
literal|"--installed"
block|}
argument_list|,
name|description
operator|=
literal|"Display the list of installed features"
argument_list|)
name|boolean
name|installed
decl_stmt|;
specifier|protected
name|void
name|doExecute
parameter_list|(
name|FeaturesService
name|admin
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|features
decl_stmt|;
if|if
condition|(
name|installed
condition|)
block|{
name|features
operator|=
name|admin
operator|.
name|listInstalledFeatures
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|features
operator|=
name|admin
operator|.
name|listFeatures
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|features
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|features
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|features
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
name|features
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|installed
condition|)
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No features installed."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|io
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No features available."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

