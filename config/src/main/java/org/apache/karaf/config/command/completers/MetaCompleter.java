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
name|config
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|config
operator|.
name|core
operator|.
name|impl
operator|.
name|MetaServiceCaller
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
name|config
operator|.
name|core
operator|.
name|impl
operator|.
name|MetatypeCallable
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
name|Init
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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleEvent
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
name|BundleListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|metatype
operator|.
name|MetaTypeInformation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|metatype
operator|.
name|MetaTypeService
import|;
end_import

begin_class
annotation|@
name|Service
specifier|public
class|class
name|MetaCompleter
implements|implements
name|Completer
implements|,
name|BundleListener
block|{
specifier|private
specifier|final
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
annotation|@
name|Reference
name|BundleContext
name|context
decl_stmt|;
annotation|@
name|Init
specifier|public
name|void
name|init
parameter_list|()
block|{
name|context
operator|.
name|registerService
argument_list|(
name|BundleListener
operator|.
name|class
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|updateMeta
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|int
name|complete
parameter_list|(
specifier|final
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
annotation|@
name|Override
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|event
parameter_list|)
block|{
name|updateMeta
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|updateMeta
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|pids
init|=
name|MetaServiceCaller
operator|.
name|withMetaTypeService
argument_list|(
name|context
argument_list|,
operator|new
name|MetatypeCallable
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|callWith
parameter_list|(
name|MetaTypeService
name|metatypeService
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|pids
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Bundle
index|[]
name|bundles
init|=
name|context
operator|.
name|getBundles
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
name|MetaTypeInformation
name|info
init|=
name|metatypeService
operator|.
name|getMetaTypeInformation
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|info
operator|.
name|getFactoryPids
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pids
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|info
operator|.
name|getFactoryPids
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getPids
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pids
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|info
operator|.
name|getPids
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|pids
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|pids
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|addAll
argument_list|(
name|pids
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

