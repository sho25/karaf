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
name|command
operator|.
name|completers
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
name|karaf
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
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
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
name|api
operator|.
name|action
operator|.
name|lifecycle
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
name|api
operator|.
name|console
operator|.
name|CommandLine
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
name|console
operator|.
name|Completer
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
name|console
operator|.
name|Session
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
name|completers
operator|.
name|StringsCompleter
import|;
end_import

begin_comment
comment|/**  * {@link Completer} for Feature Repository URLs.  *  * Displays a list of currently installed Feature repositories.  *  */
end_comment

begin_class
annotation|@
name|Service
specifier|public
class|class
name|InstalledRepoUriCompleter
implements|implements
name|Completer
block|{
annotation|@
name|Reference
specifier|private
name|FeaturesService
name|featuresService
decl_stmt|;
specifier|public
name|void
name|setFeaturesService
parameter_list|(
name|FeaturesService
name|featuresService
parameter_list|)
block|{
name|this
operator|.
name|featuresService
operator|=
name|featuresService
expr_stmt|;
block|}
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
specifier|final
name|CommandLine
name|commandLine
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|Repository
name|repository
range|:
name|featuresService
operator|.
name|listRepositories
argument_list|()
control|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|repository
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
return|;
block|}
block|}
end_class

end_unit

