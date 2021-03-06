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
name|api
operator|.
name|action
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
name|api
operator|.
name|action
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
name|util
operator|.
name|jaas
operator|.
name|JaasHelper
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
name|startlevel
operator|.
name|BundleStartLevel
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
literal|"start-level"
argument_list|,
name|description
operator|=
literal|"Gets or sets the start level of a bundle."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|StartLevel
extends|extends
name|BundleCommand
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"startLevel"
argument_list|,
name|description
operator|=
literal|"The bundle's new start level"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|Integer
name|level
decl_stmt|;
annotation|@
name|Reference
name|BundleService
name|bundleService
decl_stmt|;
annotation|@
name|Reference
name|Session
name|session
decl_stmt|;
specifier|protected
name|Object
name|doExecute
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Get package instance service.
name|BundleStartLevel
name|bsl
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleStartLevel
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|level
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Level "
operator|+
name|bsl
operator|.
name|getStartLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|sbsl
init|=
name|bundleService
operator|.
name|getSystemBundleThreshold
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|level
operator|<
name|sbsl
operator|)
operator|&&
operator|(
name|bsl
operator|.
name|getStartLevel
argument_list|()
operator|>=
name|sbsl
operator|)
condition|)
block|{
if|if
condition|(
operator|!
name|JaasHelper
operator|.
name|currentUserHasRole
argument_list|(
name|BundleService
operator|.
name|SYSTEM_BUNDLES_ROLE
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Insufficient priviledges"
argument_list|)
throw|;
block|}
block|}
name|bsl
operator|.
name|setStartLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

