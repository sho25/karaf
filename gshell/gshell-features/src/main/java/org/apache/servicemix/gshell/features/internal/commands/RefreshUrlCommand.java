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
name|java
operator|.
name|net
operator|.
name|URI
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

begin_class
annotation|@
name|CommandComponent
argument_list|(
name|id
operator|=
literal|"features:refreshUrl"
argument_list|,
name|description
operator|=
literal|"Reload the repositories to obtain a fresh list of features"
argument_list|)
specifier|public
class|class
name|RefreshUrlCommand
extends|extends
name|FeaturesCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Repository URLs (leave empty for all)"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|urls
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
if|if
condition|(
name|urls
operator|==
literal|null
operator|||
name|urls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|urls
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Repository
name|repo
range|:
name|admin
operator|.
name|listRepositories
argument_list|()
control|)
block|{
name|urls
operator|.
name|add
argument_list|(
name|repo
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|strUri
range|:
name|urls
control|)
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|strUri
argument_list|)
decl_stmt|;
name|admin
operator|.
name|removeRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|admin
operator|.
name|addRepository
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

