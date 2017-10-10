begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractCollection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
name|Collection
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
name|Iterator
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
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|StringArrayMap
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Map
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|Object
index|[]
name|table
decl_stmt|;
specifier|private
name|int
name|size
decl_stmt|;
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|reduceMemory
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|map
parameter_list|)
block|{
switch|switch
condition|(
name|map
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
case|case
literal|1
case|:
name|Entry
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|e
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|intern
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
default|default:
if|if
condition|(
name|map
operator|instanceof
name|StringArrayMap
condition|)
block|{
name|StringArrayMap
argument_list|<
name|T
argument_list|>
name|m
init|=
operator|(
name|StringArrayMap
operator|)
name|map
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|size
operator|==
name|m
operator|.
name|table
operator|.
name|length
operator|/
literal|2
condition|)
block|{
return|return
name|map
return|;
block|}
block|}
return|return
operator|new
name|StringArrayMap
argument_list|<>
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
specifier|public
name|StringArrayMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|instanceof
name|StringArrayMap
condition|)
block|{
name|size
operator|=
operator|(
operator|(
name|StringArrayMap
operator|)
name|map
operator|)
operator|.
name|size
expr_stmt|;
name|table
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
operator|(
operator|(
name|StringArrayMap
operator|)
name|map
operator|)
operator|.
name|table
argument_list|,
name|size
operator|*
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|size
operator|=
literal|0
expr_stmt|;
name|table
operator|=
operator|new
name|Object
index|[
name|map
operator|.
name|size
argument_list|()
operator|*
literal|2
index|]
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|e
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|int
name|i
init|=
name|size
operator|++
operator|<<
literal|1
decl_stmt|;
name|table
index|[
name|i
operator|++
index|]
operator|=
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|intern
argument_list|()
expr_stmt|;
name|table
index|[
name|i
index|]
operator|=
name|e
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|StringArrayMap
parameter_list|()
block|{
name|this
argument_list|(
literal|32
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringArrayMap
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|table
operator|=
operator|new
name|Object
index|[
name|capacity
operator|*
literal|2
index|]
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|k
init|=
operator|(
operator|(
name|String
operator|)
name|key
operator|)
operator|.
name|intern
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|<<
literal|1
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|k
operator|==
name|table
index|[
name|i
index|]
condition|)
block|{
return|return
operator|(
name|V
operator|)
name|table
index|[
name|i
operator|+
literal|1
index|]
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|V
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|key
operator|=
name|key
operator|.
name|intern
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|<<
literal|1
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|key
operator|==
name|table
index|[
name|i
index|]
condition|)
block|{
name|V
name|old
init|=
operator|(
name|V
operator|)
name|table
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
name|table
index|[
name|i
operator|+
literal|1
index|]
operator|=
name|value
expr_stmt|;
return|return
name|old
return|;
block|}
block|}
if|if
condition|(
name|table
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|table
operator|=
operator|new
name|Object
index|[
literal|2
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|*
literal|2
operator|==
name|table
operator|.
name|length
condition|)
block|{
name|Object
index|[]
name|n
init|=
operator|new
name|Object
index|[
name|table
operator|.
name|length
operator|*
literal|2
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|table
argument_list|,
literal|0
argument_list|,
name|n
argument_list|,
literal|0
argument_list|,
name|table
operator|.
name|length
argument_list|)
expr_stmt|;
name|table
operator|=
name|n
expr_stmt|;
block|}
name|int
name|i
init|=
name|size
operator|++
operator|<<
literal|1
decl_stmt|;
name|table
index|[
name|i
operator|++
index|]
operator|=
name|key
expr_stmt|;
name|table
index|[
name|i
index|]
operator|=
name|value
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|index
operator|<
name|size
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|next
parameter_list|()
block|{
if|if
condition|(
name|index
operator|>=
name|size
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
operator|(
name|String
operator|)
name|table
index|[
operator|(
name|index
operator|++
operator|<<
literal|1
operator|)
index|]
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|AbstractCollection
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|index
operator|<
name|size
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|V
name|next
parameter_list|()
block|{
if|if
condition|(
name|index
operator|>=
name|size
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
operator|(
name|V
operator|)
name|table
index|[
operator|(
name|index
operator|++
operator|<<
literal|1
operator|)
operator|+
literal|1
index|]
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|index
operator|<
name|size
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Entry
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
name|index
operator|>=
name|size
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|int
name|i
init|=
name|index
operator|<<
literal|1
decl_stmt|;
name|index
operator|++
expr_stmt|;
return|return
operator|new
name|Entry
argument_list|<
name|String
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|table
index|[
name|i
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
operator|(
name|V
operator|)
name|table
index|[
name|i
operator|+
literal|1
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|k
init|=
operator|(
operator|(
name|String
operator|)
name|key
operator|)
operator|.
name|intern
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|*
literal|2
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|table
index|[
name|i
index|]
operator|==
name|k
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|*
literal|2
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|table
index|[
name|i
operator|+
literal|1
index|]
argument_list|,
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|k
init|=
operator|(
operator|(
name|String
operator|)
name|key
operator|)
operator|.
name|intern
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|*
literal|2
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|table
index|[
name|i
index|]
operator|==
name|k
condition|)
block|{
name|Object
name|v
init|=
name|table
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|i
operator|<
name|l
operator|-
literal|2
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|table
argument_list|,
name|i
operator|+
literal|2
argument_list|,
name|table
argument_list|,
name|i
argument_list|,
name|l
operator|-
literal|2
operator|-
name|i
argument_list|)
expr_stmt|;
block|}
name|table
index|[
name|l
operator|-
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|table
index|[
name|l
operator|-
literal|2
index|]
operator|=
literal|null
expr_stmt|;
name|size
operator|--
expr_stmt|;
return|return
operator|(
name|V
operator|)
name|v
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|m
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|e
range|:
name|m
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|size
operator|=
literal|0
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|table
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|table
argument_list|,
name|size
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
name|this
condition|)
return|return
literal|true
return|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Map
operator|)
condition|)
return|return
literal|false
return|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|m
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|size
argument_list|()
operator|!=
name|size
argument_list|()
condition|)
return|return
literal|false
return|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|*
literal|2
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|Object
name|key
init|=
name|table
index|[
name|i
index|]
decl_stmt|;
name|Object
name|value
init|=
name|table
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|m
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
literal|null
operator|&&
name|m
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|)
condition|)
return|return
literal|false
return|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|value
operator|.
name|equals
argument_list|(
name|m
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
condition|)
return|return
literal|false
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
decl||
name|NullPointerException
name|unused
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|size
operator|==
literal|0
condition|)
return|return
literal|"{}"
return|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|l
init|=
name|size
operator|*
literal|2
init|;
name|i
operator|<
name|l
condition|;
name|i
operator|+=
literal|2
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|table
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|table
index|[
name|i
operator|+
literal|1
index|]
operator|==
name|this
condition|?
literal|"(this Map)"
else|:
name|table
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

