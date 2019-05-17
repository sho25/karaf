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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|support
operator|.
name|ansi
operator|.
name|SimpleAnsi
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
name|BundleCapability
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
name|BundleRevision
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
literal|"Displays a list of classes/resources contained in the bundle"
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
annotation|@
name|Override
specifier|protected
name|void
name|executeOnBundle
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
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
name|List
argument_list|<
name|String
argument_list|>
name|exports
init|=
name|getExports
argument_list|(
name|bundle
argument_list|)
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
name|Collection
argument_list|<
name|String
argument_list|>
name|localResources
decl_stmt|;
if|if
condition|(
name|displayAllFiles
condition|)
block|{
name|localResources
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
operator||
name|BundleWiring
operator|.
name|LISTRESOURCES_LOCAL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|localResources
operator|=
name|wiring
operator|.
name|listResources
argument_list|(
literal|"/"
argument_list|,
literal|"/*.class"
argument_list|,
name|BundleWiring
operator|.
name|LISTRESOURCES_RECURSE
operator||
name|BundleWiring
operator|.
name|LISTRESOURCES_LOCAL
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
name|StringBuilder
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|localResource
init|=
name|localResources
operator|.
name|contains
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|localResource
condition|)
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|SimpleAnsi
operator|.
name|INTENSITY_BOLD
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ids
operator|==
literal|null
operator|||
name|ids
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" | "
argument_list|)
expr_stmt|;
block|}
name|stringBuilder
operator|.
name|append
argument_list|(
name|resource
argument_list|)
operator|.
name|append
argument_list|(
literal|" | "
argument_list|)
expr_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
literal|"exported: "
argument_list|)
operator|.
name|append
argument_list|(
name|isExported
argument_list|(
name|resource
argument_list|,
name|exports
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|localResource
condition|)
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|SimpleAnsi
operator|.
name|INTENSITY_NORMAL
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|stringBuilder
operator|.
name|toString
argument_list|()
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
specifier|private
name|boolean
name|isExported
parameter_list|(
name|String
name|className
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|exports
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|exported
init|=
literal|false
decl_stmt|;
name|String
name|packageName
init|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|exports
operator|.
name|contains
argument_list|(
name|packageName
argument_list|)
condition|)
block|{
name|exported
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|exported
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getExports
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|exports
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|BundleRevision
name|rev
init|=
name|bundle
operator|.
name|adapt
argument_list|(
name|BundleRevision
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BundleCapability
argument_list|>
name|caps
init|=
name|rev
operator|.
name|getDeclaredCapabilities
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
for|for
control|(
name|BundleCapability
name|cap
range|:
name|caps
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attr
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|attr
operator|.
name|get
argument_list|(
name|BundleRevision
operator|.
name|PACKAGE_NAMESPACE
argument_list|)
decl_stmt|;
name|exports
operator|.
name|add
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
block|}
return|return
name|exports
return|;
block|}
block|}
end_class

end_unit

