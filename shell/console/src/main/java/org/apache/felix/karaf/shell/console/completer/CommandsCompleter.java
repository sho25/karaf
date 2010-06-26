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
name|java
operator|.
name|util
operator|.
name|Set
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|shell
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
name|felix
operator|.
name|karaf
operator|.
name|shell
operator|.
name|console
operator|.
name|CompletableFunction
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
name|ServiceReference
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
name|service
operator|.
name|command
operator|.
name|CommandProcessor
import|;
end_import

begin_class
specifier|public
class|class
name|CommandsCompleter
implements|implements
name|Completer
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|ServiceReference
argument_list|,
name|Completer
argument_list|>
name|completers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|ServiceReference
argument_list|,
name|Completer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|ServiceReference
name|reference
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|functions
init|=
name|getNames
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|functions
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Completer
argument_list|>
name|cl
init|=
operator|new
name|ArrayList
argument_list|<
name|Completer
argument_list|>
argument_list|()
decl_stmt|;
name|cl
operator|.
name|add
argument_list|(
operator|new
name|StringsCompleter
argument_list|(
name|functions
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Object
name|function
init|=
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|CompletableFunction
condition|)
block|{
name|List
argument_list|<
name|Completer
argument_list|>
name|fcl
init|=
operator|(
operator|(
name|CompletableFunction
operator|)
name|function
operator|)
operator|.
name|getCompleters
argument_list|()
decl_stmt|;
if|if
condition|(
name|fcl
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Completer
name|c
range|:
name|fcl
control|)
block|{
name|cl
operator|.
name|add
argument_list|(
name|c
operator|==
literal|null
condition|?
name|NullCompleter
operator|.
name|INSTANCE
else|:
name|c
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cl
operator|.
name|add
argument_list|(
name|NullCompleter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cl
operator|.
name|add
argument_list|(
name|NullCompleter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
name|ArgumentCompleter
name|c
init|=
operator|new
name|ArgumentCompleter
argument_list|(
name|cl
argument_list|)
decl_stmt|;
name|completers
operator|.
name|put
argument_list|(
name|reference
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|ServiceReference
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|!=
literal|null
condition|)
block|{
name|completers
operator|.
name|remove
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|(
name|ServiceReference
name|reference
parameter_list|)
block|{
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
argument_list|()
decl_stmt|;
name|Object
name|scope
init|=
name|reference
operator|.
name|getProperty
argument_list|(
name|CommandProcessor
operator|.
name|COMMAND_SCOPE
argument_list|)
decl_stmt|;
name|Object
name|function
init|=
name|reference
operator|.
name|getProperty
argument_list|(
name|CommandProcessor
operator|.
name|COMMAND_FUNCTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
operator|&&
name|function
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|function
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
for|for
control|(
name|Object
name|f
range|:
operator|(
operator|(
name|Object
index|[]
operator|)
name|function
operator|)
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|scope
operator|+
literal|":"
operator|+
name|f
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|names
operator|.
name|add
argument_list|(
name|scope
operator|+
literal|":"
operator|+
name|function
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
return|return
literal|null
return|;
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
name|int
name|res
init|=
operator|new
name|AggregateCompleter
argument_list|(
name|completers
operator|.
name|values
argument_list|()
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
block|}
end_class

end_unit

