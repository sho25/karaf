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
name|impl
operator|.
name|action
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
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jline
operator|.
name|reader
operator|.
name|ParsedLine
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
specifier|public
class|class
name|ActionMaskingCallback
block|{
specifier|private
specifier|final
name|ActionCommand
name|command
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|booleanOptions
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Option
argument_list|>
name|typedOptions
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
decl_stmt|;
specifier|public
specifier|static
name|ActionMaskingCallback
name|build
parameter_list|(
name|ActionCommand
name|command
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|booleanOptions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Option
argument_list|>
name|typedOptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|censor
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|command
operator|.
name|getActionClass
argument_list|()
init|;
name|type
operator|!=
literal|null
condition|;
name|type
operator|=
name|type
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
for|for
control|(
name|Field
name|field
range|:
name|type
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|Option
name|option
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Option
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|field
operator|.
name|getType
argument_list|()
operator|==
name|boolean
operator|.
name|class
operator|||
name|field
operator|.
name|getType
argument_list|()
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
name|booleanOptions
operator|.
name|add
argument_list|(
name|option
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|booleanOptions
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|option
operator|.
name|aliases
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|typedOptions
operator|.
name|put
argument_list|(
name|option
operator|.
name|name
argument_list|()
argument_list|,
name|option
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|asList
argument_list|(
name|option
operator|.
name|aliases
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|action
lambda|->
name|typedOptions
operator|.
name|put
argument_list|(
name|option
operator|.
name|name
argument_list|()
argument_list|,
name|option
argument_list|)
argument_list|)
expr_stmt|;
name|censor
operator||=
name|option
operator|.
name|censor
argument_list|()
expr_stmt|;
block|}
block|}
name|Argument
name|argument
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Argument
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|argument
operator|!=
literal|null
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
name|argument
argument_list|)
expr_stmt|;
name|censor
operator||=
name|argument
operator|.
name|censor
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|arguments
operator|.
name|sort
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|Argument
operator|::
name|index
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|censor
condition|?
operator|new
name|ActionMaskingCallback
argument_list|(
name|command
argument_list|,
name|booleanOptions
argument_list|,
name|typedOptions
argument_list|,
name|arguments
argument_list|)
else|:
literal|null
return|;
block|}
specifier|private
name|ActionMaskingCallback
parameter_list|(
name|ActionCommand
name|command
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|booleanOptions
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Option
argument_list|>
name|typedOptions
parameter_list|,
name|List
argument_list|<
name|Argument
argument_list|>
name|arguments
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
name|this
operator|.
name|booleanOptions
operator|=
name|booleanOptions
expr_stmt|;
name|this
operator|.
name|typedOptions
operator|=
name|typedOptions
expr_stmt|;
name|this
operator|.
name|arguments
operator|=
name|arguments
expr_stmt|;
block|}
specifier|public
name|String
name|filter
parameter_list|(
name|String
name|line
parameter_list|,
name|ParsedLine
name|parsed
parameter_list|)
block|{
name|int
name|prev
init|=
literal|0
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|cur
init|=
name|line
operator|.
name|indexOf
argument_list|(
name|parsed
operator|.
name|line
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|words
init|=
name|parsed
operator|.
name|words
argument_list|()
decl_stmt|;
name|int
name|state
init|=
literal|0
decl_stmt|;
name|int
name|arg
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|word
init|=
literal|0
init|;
name|word
operator|<
name|words
operator|.
name|size
argument_list|()
condition|;
name|word
operator|++
control|)
block|{
name|String
name|wordStr
init|=
name|words
operator|.
name|get
argument_list|(
name|word
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
comment|// command
case|case
literal|0
case|:
name|cur
operator|=
name|line
operator|.
name|indexOf
argument_list|(
name|wordStr
argument_list|,
name|cur
argument_list|)
operator|+
name|wordStr
operator|.
name|length
argument_list|()
expr_stmt|;
name|state
operator|++
expr_stmt|;
break|break;
comment|// option
case|case
literal|1
case|:
if|if
condition|(
name|wordStr
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|int
name|idxEq
init|=
name|wordStr
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|idxEq
operator|>
literal|0
condition|)
block|{
name|String
name|name
init|=
name|wordStr
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idxEq
argument_list|)
decl_stmt|;
if|if
condition|(
name|booleanOptions
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
break|break;
block|}
name|Option
name|option
init|=
name|typedOptions
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
operator|&&
name|option
operator|.
name|censor
argument_list|()
condition|)
block|{
name|cur
operator|=
name|line
operator|.
name|indexOf
argument_list|(
name|wordStr
argument_list|,
name|cur
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|prev
argument_list|,
name|cur
operator|+
name|idxEq
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|idxEq
operator|+
literal|1
init|;
name|i
operator|<
name|wordStr
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|option
operator|.
name|mask
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|prev
operator|=
name|cur
operator|=
name|cur
operator|+
name|wordStr
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|name
init|=
name|wordStr
decl_stmt|;
if|if
condition|(
name|booleanOptions
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
break|break;
block|}
name|Option
name|option
init|=
name|typedOptions
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|option
operator|!=
literal|null
condition|)
block|{
comment|// skip value
name|word
operator|++
expr_stmt|;
if|if
condition|(
name|option
operator|.
name|censor
argument_list|()
operator|&&
name|word
operator|<
name|words
operator|.
name|size
argument_list|()
condition|)
block|{
name|String
name|val
init|=
name|words
operator|.
name|get
argument_list|(
name|word
argument_list|)
decl_stmt|;
name|cur
operator|=
name|line
operator|.
name|indexOf
argument_list|(
name|val
argument_list|,
name|cur
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|prev
argument_list|,
name|cur
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|val
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|option
operator|.
name|mask
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|prev
operator|=
name|cur
operator|=
name|cur
operator|+
name|val
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
block|}
break|break;
block|}
else|else
block|{
name|state
operator|=
literal|2
expr_stmt|;
comment|// fall through
block|}
comment|// argument
case|case
literal|2
case|:
if|if
condition|(
name|arg
operator|<
name|arguments
operator|.
name|size
argument_list|()
condition|)
block|{
name|Argument
name|argument
init|=
name|arguments
operator|.
name|get
argument_list|(
name|arg
argument_list|)
decl_stmt|;
if|if
condition|(
name|argument
operator|.
name|censor
argument_list|()
condition|)
block|{
name|cur
operator|=
name|line
operator|.
name|indexOf
argument_list|(
name|wordStr
argument_list|,
name|cur
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|prev
argument_list|,
name|cur
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|wordStr
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|argument
operator|.
name|mask
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|prev
operator|=
name|cur
operator|=
name|cur
operator|+
name|wordStr
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|argument
operator|.
name|multiValued
argument_list|()
condition|)
block|{
name|arg
operator|++
expr_stmt|;
block|}
block|}
break|break;
block|}
block|}
if|if
condition|(
name|prev
operator|<
name|line
operator|.
name|length
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|line
argument_list|,
name|prev
argument_list|,
name|line
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

