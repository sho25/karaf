begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|config
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|console
operator|.
name|completer
operator|.
name|StringsCompleter
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
name|karaf
operator|.
name|gshell
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
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
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
name|cm
operator|.
name|ConfigurationAdmin
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
name|cm
operator|.
name|ConfigurationEvent
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
name|cm
operator|.
name|ConfigurationListener
import|;
end_import

begin_comment
comment|/**  * {@link jline.Completor} for Configuration Admin configurations.  *  * Displays a list of existing config admin configurations for completion.  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationCompleter
implements|implements
name|Completer
implements|,
name|ConfigurationListener
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
specifier|private
name|ConfigurationAdmin
name|admin
decl_stmt|;
specifier|public
name|void
name|setAdmin
parameter_list|(
name|ConfigurationAdmin
name|admin
parameter_list|)
block|{
name|this
operator|.
name|admin
operator|=
name|admin
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{
name|Configuration
index|[]
name|configs
decl_stmt|;
try|try
block|{
name|configs
operator|=
name|admin
operator|.
name|listConfigurations
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|pids
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Configuration
name|config
range|:
name|configs
control|)
block|{
if|if
condition|(
name|config
operator|.
name|getFactoryPid
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pids
operator|.
name|add
argument_list|(
name|config
operator|.
name|getFactoryPid
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pids
operator|.
name|add
argument_list|(
name|config
operator|.
name|getPid
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
specifier|public
name|int
name|complete
parameter_list|(
specifier|final
name|String
name|buffer
parameter_list|,
specifier|final
name|int
name|cursor
parameter_list|,
specifier|final
name|List
name|candidates
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
return|;
block|}
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|configurationEvent
parameter_list|)
block|{
name|String
name|pid
init|=
name|configurationEvent
operator|.
name|getFactoryPid
argument_list|()
operator|!=
literal|null
condition|?
name|configurationEvent
operator|.
name|getFactoryPid
argument_list|()
else|:
name|configurationEvent
operator|.
name|getPid
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurationEvent
operator|.
name|getType
argument_list|()
operator|==
name|ConfigurationEvent
operator|.
name|CM_DELETED
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|remove
argument_list|(
name|pid
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configurationEvent
operator|.
name|getType
argument_list|()
operator|==
name|ConfigurationEvent
operator|.
name|CM_UPDATED
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|pid
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

