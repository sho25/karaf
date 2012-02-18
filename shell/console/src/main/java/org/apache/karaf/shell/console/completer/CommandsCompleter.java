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
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|completer
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|basic
operator|.
name|AbstractCommand
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
name|runtime
operator|.
name|Closure
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
name|runtime
operator|.
name|CommandProxy
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
name|runtime
operator|.
name|CommandSessionImpl
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
name|service
operator|.
name|command
operator|.
name|CommandSession
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
name|service
operator|.
name|command
operator|.
name|Function
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
name|Completer
import|;
end_import

begin_comment
comment|/**  * Like the {@link org.apache.karaf.shell.console.completer.CommandsCompleter} but does not use OSGi but is  * instead used from the non-OSGi {@link org.apache.karaf.shell.console.Main}  */
end_comment

begin_class
specifier|public
class|class
name|CommandsCompleter
implements|implements
name|Completer
block|{
specifier|private
name|CommandSession
name|session
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Completer
argument_list|>
name|completers
init|=
operator|new
name|ArrayList
argument_list|<
name|Completer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|commands
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CommandsCompleter
parameter_list|(
name|CommandSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
specifier|public
name|int
name|complete
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|cursor
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|checkData
argument_list|()
expr_stmt|;
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|completers
argument_list|)
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|candidates
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
specifier|protected
specifier|synchronized
name|void
name|checkData
parameter_list|()
block|{
comment|// Copy the set to avoid concurrent modification exceptions
comment|// TODO: fix that in gogo instead
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
operator|(
name|Set
argument_list|<
name|String
argument_list|>
operator|)
name|session
operator|.
name|get
argument_list|(
name|CommandSessionImpl
operator|.
name|COMMANDS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|names
operator|.
name|equals
argument_list|(
name|commands
argument_list|)
condition|)
block|{
name|commands
operator|.
name|clear
argument_list|()
expr_stmt|;
name|completers
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// get command aliases
name|Set
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|this
operator|.
name|getAliases
argument_list|()
decl_stmt|;
name|completers
operator|.
name|add
argument_list|(
operator|new
name|StringsCompleter
argument_list|(
name|aliases
argument_list|)
argument_list|)
expr_stmt|;
comment|// add argument completers for each command
for|for
control|(
name|String
name|command
range|:
name|names
control|)
block|{
name|Function
name|function
init|=
operator|(
name|Function
operator|)
name|session
operator|.
name|get
argument_list|(
name|command
argument_list|)
decl_stmt|;
name|function
operator|=
name|unProxy
argument_list|(
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|AbstractCommand
condition|)
block|{
name|completers
operator|.
name|add
argument_list|(
operator|new
name|ArgumentCompleter
argument_list|(
name|session
argument_list|,
operator|(
name|AbstractCommand
operator|)
name|function
argument_list|,
name|command
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|commands
operator|.
name|add
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Get the aliases defined in the console session.      *      * @return the aliases set      */
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getAliases
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|vars
init|=
operator|(
name|Set
argument_list|<
name|String
argument_list|>
operator|)
name|session
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|aliases
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|var
range|:
name|vars
control|)
block|{
name|Object
name|content
init|=
name|session
operator|.
name|get
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|Closure
condition|)
block|{
name|aliases
operator|.
name|add
argument_list|(
name|var
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|aliases
return|;
block|}
specifier|protected
name|Function
name|unProxy
parameter_list|(
name|Function
name|function
parameter_list|)
block|{
if|if
condition|(
name|function
operator|instanceof
name|CommandProxy
condition|)
block|{
name|CommandProxy
name|proxy
init|=
operator|(
name|CommandProxy
operator|)
name|function
decl_stmt|;
name|Object
name|target
init|=
name|proxy
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|Function
name|result
decl_stmt|;
if|if
condition|(
name|target
operator|instanceof
name|Function
condition|)
block|{
name|result
operator|=
operator|(
name|Function
operator|)
name|target
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|function
expr_stmt|;
block|}
name|proxy
operator|.
name|ungetTarget
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
else|else
block|{
return|return
name|function
return|;
block|}
block|}
block|}
end_class

end_unit

