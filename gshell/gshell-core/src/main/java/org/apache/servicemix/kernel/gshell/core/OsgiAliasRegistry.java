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
name|servicemix
operator|.
name|kernel
operator|.
name|gshell
operator|.
name|core
package|;
end_package

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
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|Alias
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
name|registry
operator|.
name|AliasRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiAliasRegistry
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALIAS
init|=
literal|"alias"
decl_stmt|;
specifier|private
name|AliasRegistry
name|aliasRegistry
decl_stmt|;
specifier|public
name|OsgiAliasRegistry
parameter_list|(
name|AliasRegistry
name|aliasRegistry
parameter_list|)
block|{
name|this
operator|.
name|aliasRegistry
operator|=
name|aliasRegistry
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|Alias
name|alias
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|aliasRegistry
operator|.
name|registerAlias
argument_list|(
name|alias
operator|.
name|getName
argument_list|()
argument_list|,
name|alias
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
specifier|final
name|Alias
name|alias
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|aliasRegistry
operator|.
name|removeAlias
argument_list|(
name|alias
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

