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
operator|.
name|completers
package|;
end_package

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
name|Candidate
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

begin_class
annotation|@
name|Service
specifier|public
class|class
name|BundleSymbolicNameCompleter
implements|implements
name|Completer
block|{
annotation|@
name|Reference
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|List
argument_list|<
name|Candidate
argument_list|>
name|cands
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|completeCandidates
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|cands
argument_list|)
expr_stmt|;
for|for
control|(
name|Candidate
name|cand
range|:
name|cands
control|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|cand
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeCandidates
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|Candidate
argument_list|>
name|candidates
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|candidates
operator|.
name|add
argument_list|(
operator|new
name|Candidate
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

