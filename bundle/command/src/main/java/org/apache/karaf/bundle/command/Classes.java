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
name|commands
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
name|inject
operator|.
name|Service
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
name|wiring
operator|.
name|BundleWiring
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Command
argument_list|(
name|scope
operator|=
literal|"bundle"
argument_list|,
name|name
operator|=
literal|"classes"
argument_list|,
name|description
operator|=
literal|"Displays a list of classes contained in the bundle"
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|Classes
extends|extends
name|BundlesCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-a"
argument_list|,
name|aliases
operator|=
block|{
literal|"--display-all-files"
block|}
argument_list|,
name|description
operator|=
literal|"List all classes and files in the bundle"
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
name|displayAllFiles
decl_stmt|;
specifier|public
name|Classes
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
name|printResources
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|printResources
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|BundleWiring
name|wiring
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleWiring
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wiring
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|resources
decl_stmt|;
if|if
condition|(
name|displayAllFiles
condition|)
block|{
name|resources
operator|=
name|wiring
operator|.
name|listResources
argument_list|(
literal|"/"
argument_list|,
literal|null
argument_list|,
name|BundleWiring
operator|.
name|LISTRESOURCES_RECURSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resources
operator|=
name|wiring
operator|.
name|listResources
argument_list|(
literal|"/"
argument_list|,
literal|"*class"
argument_list|,
name|BundleWiring
operator|.
name|LISTRESOURCES_RECURSE
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|resource
range|:
name|resources
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Bundle "
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|" is not resolved."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

