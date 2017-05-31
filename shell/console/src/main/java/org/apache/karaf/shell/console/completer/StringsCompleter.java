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
name|Arrays
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
comment|/**  * Completer for a set of strings.  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|StringsCompleter
implements|implements
name|Completer
block|{
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|String
argument_list|>
name|strings
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|caseSensitive
decl_stmt|;
specifier|public
name|StringsCompleter
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringsCompleter
parameter_list|(
specifier|final
name|boolean
name|caseSensitive
parameter_list|)
block|{
name|this
operator|.
name|caseSensitive
operator|=
name|caseSensitive
expr_stmt|;
block|}
specifier|public
name|StringsCompleter
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|strings
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
assert|assert
name|strings
operator|!=
literal|null
assert|;
name|getStrings
argument_list|()
operator|.
name|addAll
argument_list|(
name|strings
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringsCompleter
parameter_list|(
specifier|final
name|String
index|[]
name|strings
parameter_list|,
name|boolean
name|caseSensitive
parameter_list|)
block|{
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|strings
argument_list|)
argument_list|,
name|caseSensitive
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringsCompleter
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|strings
parameter_list|,
name|boolean
name|caseSensitive
parameter_list|)
block|{
name|this
argument_list|(
name|caseSensitive
argument_list|)
expr_stmt|;
assert|assert
name|strings
operator|!=
literal|null
assert|;
name|getStrings
argument_list|()
operator|.
name|addAll
argument_list|(
name|strings
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringsCompleter
parameter_list|(
specifier|final
name|String
index|[]
name|strings
parameter_list|)
block|{
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|strings
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SortedSet
argument_list|<
name|String
argument_list|>
name|getStrings
parameter_list|()
block|{
return|return
name|strings
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|int
name|complete
parameter_list|(
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
comment|// buffer could be null
assert|assert
name|candidates
operator|!=
literal|null
assert|;
if|if
condition|(
name|buffer
operator|==
literal|null
condition|)
block|{
name|buffer
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|caseSensitive
condition|)
block|{
name|buffer
operator|=
name|buffer
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
comment|// KARAF-421, use getStrings() instead strings field.
name|SortedSet
argument_list|<
name|String
argument_list|>
name|matches
init|=
name|getStrings
argument_list|()
operator|.
name|tailSet
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|match
range|:
name|matches
control|)
block|{
name|String
name|s
init|=
name|caseSensitive
condition|?
name|match
else|:
name|match
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|s
operator|.
name|startsWith
argument_list|(
name|buffer
argument_list|)
condition|)
block|{
break|break;
block|}
comment|// noinspection unchecked
name|candidates
operator|.
name|add
argument_list|(
name|match
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// noinspection unchecked
name|candidates
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|candidates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
return|return
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
block|}
end_class

end_unit

