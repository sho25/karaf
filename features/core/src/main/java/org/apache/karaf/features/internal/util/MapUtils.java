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
name|Iterator
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
specifier|final
class|class
name|MapUtils
block|{
specifier|private
name|MapUtils
parameter_list|()
block|{     }
specifier|public
interface|interface
name|Function
parameter_list|<
name|T
parameter_list|,
name|U
parameter_list|>
block|{
name|U
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|invert
parameter_list|(
name|Map
argument_list|<
name|T
argument_list|,
name|S
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|inverted
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|T
argument_list|,
name|S
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|addToMapSet
argument_list|(
name|inverted
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|inverted
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|,
name|U
parameter_list|>
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|U
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|mapset
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|function
parameter_list|)
block|{
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|U
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|mapset
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|entry
range|:
name|mapset
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|apply
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|function
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|U
parameter_list|,
name|T
parameter_list|>
name|Set
argument_list|<
name|U
argument_list|>
name|apply
parameter_list|(
name|Set
argument_list|<
name|T
argument_list|>
name|set
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|function
parameter_list|)
block|{
name|Set
argument_list|<
name|U
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|set
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|T
name|t
range|:
name|set
control|)
block|{
name|U
name|u
init|=
name|function
operator|.
name|apply
argument_list|(
name|t
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|,
name|U
parameter_list|>
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|build
parameter_list|(
name|Collection
argument_list|<
name|S
argument_list|>
name|col
parameter_list|,
name|Function
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|key
parameter_list|,
name|Function
argument_list|<
name|S
argument_list|,
name|U
argument_list|>
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|col
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|S
name|s
range|:
name|col
control|)
block|{
name|T
name|t
init|=
name|key
operator|.
name|apply
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|U
name|u
init|=
name|value
operator|.
name|apply
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
name|u
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|t
argument_list|,
name|u
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|,
name|U
parameter_list|>
name|Function
argument_list|<
name|S
argument_list|,
name|U
argument_list|>
name|compose
parameter_list|(
specifier|final
name|Function
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|f1
parameter_list|,
specifier|final
name|Function
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|f2
parameter_list|)
block|{
return|return
operator|new
name|Function
argument_list|<
name|S
argument_list|,
name|U
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|U
name|apply
parameter_list|(
name|S
name|s
parameter_list|)
block|{
return|return
name|f2
operator|.
name|apply
argument_list|(
name|f1
operator|.
name|apply
argument_list|(
name|s
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|U
parameter_list|>
name|MapUtils
operator|.
name|Function
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|map
parameter_list|(
specifier|final
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|MapUtils
operator|.
name|Function
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|U
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|t
argument_list|)
return|;
block|}
block|}
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|boolean
name|contains
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|mapset
parameter_list|,
name|S
name|key
parameter_list|,
name|T
name|val
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|set
init|=
name|mapset
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|set
operator|!=
literal|null
operator|&&
name|set
operator|.
name|contains
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|flatten
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|mapset
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Set
argument_list|<
name|T
argument_list|>
name|s
range|:
name|mapset
operator|.
name|values
argument_list|()
control|)
block|{
name|set
operator|.
name|addAll
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|set
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|diff
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|to
parameter_list|)
block|{
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|diff
init|=
name|copyMapSet
argument_list|(
name|from
argument_list|)
decl_stmt|;
name|remove
argument_list|(
name|diff
argument_list|,
name|to
argument_list|)
expr_stmt|;
return|return
name|diff
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|void
name|add
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|toAdd
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|entry
range|:
name|toAdd
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|s
init|=
name|from
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|from
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|addAll
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|void
name|retain
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|toRetain
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|>
name|iterator
init|=
name|from
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|entry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|T
argument_list|>
name|s
init|=
name|toRetain
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|retainAll
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|void
name|remove
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|toRemove
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|entry
range|:
name|toRemove
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|s
init|=
name|from
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|removeAll
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|from
operator|.
name|remove
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|S
name|copy
parameter_list|(
name|S
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|List
condition|)
block|{
name|List
name|r
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
operator|(
name|List
operator|)
name|obj
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|copy
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|S
operator|)
name|r
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Set
condition|)
block|{
name|Set
name|r
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
operator|(
name|Set
operator|)
name|obj
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|copy
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|S
operator|)
name|r
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Map
condition|)
block|{
name|Map
name|r
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|e
range|:
operator|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
operator|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|r
operator|.
name|put
argument_list|(
name|copy
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|copy
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|S
operator|)
name|r
return|;
block|}
return|return
name|obj
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|void
name|copy
parameter_list|(
name|S
name|s1
parameter_list|,
name|S
name|s2
parameter_list|)
block|{
if|if
condition|(
name|s1
operator|instanceof
name|Collection
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
operator|(
name|Collection
operator|)
name|s1
control|)
block|{
operator|(
operator|(
name|Collection
operator|)
name|s2
operator|)
operator|.
name|add
argument_list|(
name|copy
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|s1
operator|instanceof
name|Map
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|e
range|:
operator|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|s1
operator|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
operator|(
operator|(
name|Map
operator|)
name|s2
operator|)
operator|.
name|put
argument_list|(
name|copy
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|copy
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Source is not a Collection or a Map"
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|copyMapSet
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|from
parameter_list|)
block|{
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|to
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|copyMapSet
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
return|return
name|to
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|void
name|copyMapSet
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|to
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|entry
range|:
name|from
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|to
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|void
name|addToMapSet
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|map
parameter_list|,
name|S
name|key
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|values
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
name|values
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
name|void
name|removeFromMapSet
parameter_list|(
name|Map
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
name|map
parameter_list|,
name|S
name|key
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|values
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
condition|)
block|{
name|values
operator|.
name|remove
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

