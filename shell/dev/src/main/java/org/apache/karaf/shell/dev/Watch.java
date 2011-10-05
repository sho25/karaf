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
name|shell
operator|.
name|dev
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
name|felix
operator|.
name|gogo
operator|.
name|commands
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
name|felix
operator|.
name|gogo
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
name|felix
operator|.
name|gogo
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
name|console
operator|.
name|OsgiCommandSupport
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
name|dev
operator|.
name|watch
operator|.
name|BundleWatcher
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
name|Constants
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"dev"
argument_list|,
name|name
operator|=
literal|"watch"
argument_list|,
name|description
operator|=
literal|"Watches and updates bundles."
argument_list|,
name|detailedDescription
operator|=
literal|"classpath:watch.txt"
argument_list|)
specifier|public
class|class
name|Watch
extends|extends
name|OsgiCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"urls"
argument_list|,
name|description
operator|=
literal|"The bundle IDs or URLs"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|urls
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-i"
argument_list|,
name|aliases
operator|=
block|{}
argument_list|,
name|description
operator|=
literal|"Watch interval"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|private
name|long
name|interval
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--start"
argument_list|,
name|description
operator|=
literal|"Starts watching the selected bundles"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|protected
name|boolean
name|start
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--stop"
argument_list|,
name|description
operator|=
literal|"Stops watching all bundles"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|protected
name|boolean
name|stop
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--remove"
argument_list|,
name|description
operator|=
literal|"Removes bundles from the watch list"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|protected
name|boolean
name|remove
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--list"
argument_list|,
name|description
operator|=
literal|"Displays the watch list"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
specifier|protected
name|boolean
name|list
decl_stmt|;
specifier|private
name|BundleWatcher
name|watcher
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|start
operator|&&
name|stop
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Please use only one of --start and --stop options!"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|interval
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Setting watch interval to "
operator|+
name|interval
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
name|watcher
operator|.
name|setInterval
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stop
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Stopping watch"
argument_list|)
expr_stmt|;
name|watcher
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|urls
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|remove
condition|)
block|{
for|for
control|(
name|String
name|url
range|:
name|urls
control|)
block|{
name|watcher
operator|.
name|remove
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|String
name|url
range|:
name|urls
control|)
block|{
name|watcher
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|start
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting watch"
argument_list|)
expr_stmt|;
name|watcher
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|list
condition|)
block|{
comment|//List the watched bundles.
name|String
name|format
init|=
literal|"%-40s %6s %-80s"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|format
argument_list|,
literal|"URL"
argument_list|,
literal|"ID"
argument_list|,
literal|"Bundle Name"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|url
range|:
name|watcher
operator|.
name|getWatchURLs
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundleList
init|=
name|watcher
operator|.
name|getBundlesByURL
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundleList
operator|!=
literal|null
operator|&&
name|bundleList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundleList
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|url
argument_list|,
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|bundle
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_NAME
argument_list|)
argument_list|)
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
name|String
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|url
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|urls
init|=
name|watcher
operator|.
name|getWatchURLs
argument_list|()
decl_stmt|;
if|if
condition|(
name|urls
operator|!=
literal|null
operator|&&
name|urls
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Watched URLs/IDs: "
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|url
range|:
name|watcher
operator|.
name|getWatchURLs
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|url
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
literal|"No watched URLs/IDs"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|BundleWatcher
name|getWatcher
parameter_list|()
block|{
return|return
name|watcher
return|;
block|}
specifier|public
name|void
name|setWatcher
parameter_list|(
name|BundleWatcher
name|watcher
parameter_list|)
block|{
name|this
operator|.
name|watcher
operator|=
name|watcher
expr_stmt|;
block|}
block|}
end_class

end_unit

