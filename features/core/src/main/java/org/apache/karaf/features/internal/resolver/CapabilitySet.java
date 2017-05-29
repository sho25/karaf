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
name|resolver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
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
name|Constructor
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
name|Map
operator|.
name|Entry
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
name|TreeMap
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
name|utils
operator|.
name|version
operator|.
name|VersionTable
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Capability
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
class|class
name|CapabilitySet
block|{
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|STRING_CLASS
init|=
operator|new
name|Class
index|[]
block|{
name|String
operator|.
name|class
block|}
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
argument_list|>
name|indices
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Capability
argument_list|>
name|capSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|CapabilitySet
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|indexProps
parameter_list|)
block|{
name|indices
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|indexProps
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|i
operator|<
name|indexProps
operator|.
name|size
argument_list|()
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|indices
operator|.
name|put
argument_list|(
name|indexProps
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dump
parameter_list|()
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
argument_list|>
name|entry
range|:
name|indices
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|boolean
name|header1
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|entry2
range|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|boolean
name|header2
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Capability
name|cap
range|:
name|entry2
operator|.
name|getValue
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|header1
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
argument_list|)
expr_stmt|;
name|header1
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|header2
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"   "
operator|+
name|entry2
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|header2
operator|=
literal|true
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"      "
operator|+
name|cap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|addCapability
parameter_list|(
name|Capability
name|cap
parameter_list|)
block|{
name|capSet
operator|.
name|add
argument_list|(
name|cap
argument_list|)
expr_stmt|;
comment|// Index capability.
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
argument_list|>
name|entry
range|:
name|indices
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
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
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|value
operator|=
name|convertArrayToList
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|index
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
name|c
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|c
control|)
block|{
name|indexCapability
argument_list|(
name|index
argument_list|,
name|cap
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|indexCapability
argument_list|(
name|index
argument_list|,
name|cap
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|indexCapability
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|index
parameter_list|,
name|Capability
name|cap
parameter_list|,
name|Object
name|capValue
parameter_list|)
block|{
name|Set
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
name|index
operator|.
name|get
argument_list|(
name|capValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|caps
operator|==
literal|null
condition|)
block|{
name|caps
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|index
operator|.
name|put
argument_list|(
name|capValue
argument_list|,
name|caps
argument_list|)
expr_stmt|;
block|}
name|caps
operator|.
name|add
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeCapability
parameter_list|(
name|Capability
name|cap
parameter_list|)
block|{
if|if
condition|(
name|capSet
operator|.
name|remove
argument_list|(
name|cap
argument_list|)
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
argument_list|>
name|entry
range|:
name|indices
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
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
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|value
operator|=
name|convertArrayToList
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|index
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
name|c
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|c
control|)
block|{
name|deindexCapability
argument_list|(
name|index
argument_list|,
name|cap
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|deindexCapability
argument_list|(
name|index
argument_list|,
name|cap
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|deindexCapability
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|index
parameter_list|,
name|Capability
name|cap
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Set
argument_list|<
name|Capability
argument_list|>
name|caps
init|=
name|index
operator|.
name|get
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|caps
operator|!=
literal|null
condition|)
block|{
name|caps
operator|.
name|remove
argument_list|(
name|cap
argument_list|)
expr_stmt|;
if|if
condition|(
name|caps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|index
operator|.
name|remove
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Set
argument_list|<
name|Capability
argument_list|>
name|match
parameter_list|(
name|SimpleFilter
name|sf
parameter_list|,
name|boolean
name|obeyMandatory
parameter_list|)
block|{
name|Set
argument_list|<
name|Capability
argument_list|>
name|matches
init|=
name|match
argument_list|(
name|capSet
argument_list|,
name|sf
argument_list|)
decl_stmt|;
return|return
name|obeyMandatory
condition|?
name|matchMandatory
argument_list|(
name|matches
argument_list|,
name|sf
argument_list|)
else|:
name|matches
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Set
argument_list|<
name|Capability
argument_list|>
name|match
parameter_list|(
name|Set
argument_list|<
name|Capability
argument_list|>
name|caps
parameter_list|,
name|SimpleFilter
name|sf
parameter_list|)
block|{
name|Set
argument_list|<
name|Capability
argument_list|>
name|matches
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|MATCH_ALL
condition|)
block|{
name|matches
operator|.
name|addAll
argument_list|(
name|caps
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|AND
condition|)
block|{
comment|// Evaluate each subfilter against the remaining capabilities.
comment|// For AND we calculate the intersection of each subfilter.
comment|// We can short-circuit the AND operation if there are no
comment|// remaining capabilities.
name|List
argument_list|<
name|SimpleFilter
argument_list|>
name|sfs
init|=
operator|(
name|List
argument_list|<
name|SimpleFilter
argument_list|>
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|(
name|caps
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
operator|&&
operator|(
name|i
operator|<
name|sfs
operator|.
name|size
argument_list|()
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|matches
operator|=
name|match
argument_list|(
name|caps
argument_list|,
name|sfs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|caps
operator|=
name|matches
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|OR
condition|)
block|{
comment|// Evaluate each subfilter against the remaining capabilities.
comment|// For OR we calculate the union of each subfilter.
name|List
argument_list|<
name|SimpleFilter
argument_list|>
name|sfs
init|=
operator|(
name|List
argument_list|<
name|SimpleFilter
argument_list|>
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleFilter
name|sf1
range|:
name|sfs
control|)
block|{
name|matches
operator|.
name|addAll
argument_list|(
name|match
argument_list|(
name|caps
argument_list|,
name|sf1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|NOT
condition|)
block|{
comment|// Evaluate each subfilter against the remaining capabilities.
comment|// For OR we calculate the union of each subfilter.
name|matches
operator|.
name|addAll
argument_list|(
name|caps
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SimpleFilter
argument_list|>
name|sfs
init|=
operator|(
name|List
argument_list|<
name|SimpleFilter
argument_list|>
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleFilter
name|sf1
range|:
name|sfs
control|)
block|{
name|matches
operator|.
name|removeAll
argument_list|(
name|match
argument_list|(
name|caps
argument_list|,
name|sf1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Set
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|index
init|=
name|indices
operator|.
name|get
argument_list|(
name|sf
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|EQ
operator|)
operator|&&
operator|(
name|index
operator|!=
literal|null
operator|)
condition|)
block|{
name|Set
argument_list|<
name|Capability
argument_list|>
name|existingCaps
init|=
name|index
operator|.
name|get
argument_list|(
name|sf
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCaps
operator|!=
literal|null
condition|)
block|{
name|matches
operator|.
name|addAll
argument_list|(
name|existingCaps
argument_list|)
expr_stmt|;
name|matches
operator|.
name|retainAll
argument_list|(
name|caps
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|Capability
name|cap
range|:
name|caps
control|)
block|{
name|Object
name|lhs
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|sf
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lhs
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|compare
argument_list|(
name|lhs
argument_list|,
name|sf
operator|.
name|getValue
argument_list|()
argument_list|,
name|sf
operator|.
name|getOperation
argument_list|()
argument_list|)
condition|)
block|{
name|matches
operator|.
name|add
argument_list|(
name|cap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|matches
return|;
block|}
specifier|public
specifier|static
name|boolean
name|matches
parameter_list|(
name|Capability
name|cap
parameter_list|,
name|SimpleFilter
name|sf
parameter_list|)
block|{
return|return
name|matchesInternal
argument_list|(
name|cap
argument_list|,
name|sf
argument_list|)
operator|&&
name|matchMandatory
argument_list|(
name|cap
argument_list|,
name|sf
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
name|boolean
name|matchesInternal
parameter_list|(
name|Capability
name|cap
parameter_list|,
name|SimpleFilter
name|sf
parameter_list|)
block|{
name|boolean
name|matched
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|MATCH_ALL
condition|)
block|{
name|matched
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|AND
condition|)
block|{
comment|// Evaluate each subfilter against the remaining capabilities.
comment|// For AND we calculate the intersection of each subfilter.
comment|// We can short-circuit the AND operation if there are no
comment|// remaining capabilities.
name|List
argument_list|<
name|SimpleFilter
argument_list|>
name|sfs
init|=
operator|(
name|List
argument_list|<
name|SimpleFilter
argument_list|>
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|matched
operator|&&
operator|(
name|i
operator|<
name|sfs
operator|.
name|size
argument_list|()
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|matched
operator|=
name|matchesInternal
argument_list|(
name|cap
argument_list|,
name|sfs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|OR
condition|)
block|{
comment|// Evaluate each subfilter against the remaining capabilities.
comment|// For OR we calculate the union of each subfilter.
name|matched
operator|=
literal|false
expr_stmt|;
name|List
argument_list|<
name|SimpleFilter
argument_list|>
name|sfs
init|=
operator|(
name|List
argument_list|<
name|SimpleFilter
argument_list|>
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|!
name|matched
operator|&&
operator|(
name|i
operator|<
name|sfs
operator|.
name|size
argument_list|()
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|matched
operator|=
name|matchesInternal
argument_list|(
name|cap
argument_list|,
name|sfs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|NOT
condition|)
block|{
comment|// Evaluate each subfilter against the remaining capabilities.
comment|// For OR we calculate the union of each subfilter.
name|List
argument_list|<
name|SimpleFilter
argument_list|>
name|sfs
init|=
operator|(
name|List
argument_list|<
name|SimpleFilter
argument_list|>
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleFilter
name|sf1
range|:
name|sfs
control|)
block|{
name|matched
operator|=
operator|!
operator|(
name|matchesInternal
argument_list|(
name|cap
argument_list|,
name|sf1
argument_list|)
operator|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|matched
operator|=
literal|false
expr_stmt|;
name|Object
name|lhs
init|=
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|sf
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lhs
operator|!=
literal|null
condition|)
block|{
name|matched
operator|=
name|compare
argument_list|(
name|lhs
argument_list|,
name|sf
operator|.
name|getValue
argument_list|()
argument_list|,
name|sf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|matched
return|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|Capability
argument_list|>
name|matchMandatory
parameter_list|(
name|Set
argument_list|<
name|Capability
argument_list|>
name|caps
parameter_list|,
name|SimpleFilter
name|sf
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Capability
argument_list|>
name|it
init|=
name|caps
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Capability
name|cap
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|matchMandatory
argument_list|(
name|cap
argument_list|,
name|sf
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|caps
return|;
block|}
specifier|private
specifier|static
name|boolean
name|matchMandatory
parameter_list|(
name|Capability
name|cap
parameter_list|,
name|SimpleFilter
name|sf
parameter_list|)
block|{
if|if
condition|(
name|cap
operator|instanceof
name|CapabilityImpl
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|CapabilityImpl
operator|)
name|cap
operator|)
operator|.
name|isAttributeMandatory
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
operator|!
name|matchMandatoryAttribute
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|sf
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
else|else
block|{
name|String
name|value
init|=
name|cap
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|MANDATORY_DIRECTIVE
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ResourceBuilder
operator|.
name|parseDelimitedString
argument_list|(
name|value
argument_list|,
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|cap
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|names
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
operator|!
name|matchMandatoryAttribute
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|sf
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
name|boolean
name|matchMandatoryAttribute
parameter_list|(
name|String
name|attrName
parameter_list|,
name|SimpleFilter
name|sf
parameter_list|)
block|{
if|if
condition|(
operator|(
name|sf
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
name|sf
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|attrName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|sf
operator|.
name|getOperation
argument_list|()
operator|==
name|SimpleFilter
operator|.
name|AND
condition|)
block|{
name|List
name|list
init|=
operator|(
name|List
operator|)
name|sf
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|aList
range|:
name|list
control|)
block|{
name|SimpleFilter
name|sf2
init|=
operator|(
name|SimpleFilter
operator|)
name|aList
decl_stmt|;
if|if
condition|(
operator|(
name|sf2
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
name|sf2
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|attrName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
specifier|static
name|boolean
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhsUnknown
parameter_list|,
name|int
name|op
parameter_list|)
block|{
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// If this is a PRESENT operation, then just return true immediately
comment|// since we wouldn't be here if the attribute wasn't present.
if|if
condition|(
name|op
operator|==
name|SimpleFilter
operator|.
name|PRESENT
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// If the type is comparable, then we can just return the
comment|// result immediately.
if|if
condition|(
name|lhs
operator|instanceof
name|Comparable
condition|)
block|{
comment|// Spec says SUBSTRING is false for all types other than string.
if|if
condition|(
operator|(
name|op
operator|==
name|SimpleFilter
operator|.
name|SUBSTRING
operator|)
operator|&&
operator|!
operator|(
name|lhs
operator|instanceof
name|String
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|rhs
decl_stmt|;
if|if
condition|(
name|op
operator|==
name|SimpleFilter
operator|.
name|SUBSTRING
condition|)
block|{
name|rhs
operator|=
name|rhsUnknown
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|rhs
operator|=
name|coerceType
argument_list|(
name|lhs
argument_list|,
operator|(
name|String
operator|)
name|rhsUnknown
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
switch|switch
condition|(
name|op
condition|)
block|{
case|case
name|SimpleFilter
operator|.
name|EQ
case|:
try|try
block|{
return|return
operator|(
operator|(
name|Comparable
operator|)
name|lhs
operator|)
operator|.
name|compareTo
argument_list|(
name|rhs
argument_list|)
operator|==
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
case|case
name|SimpleFilter
operator|.
name|GTE
case|:
try|try
block|{
return|return
operator|(
operator|(
name|Comparable
operator|)
name|lhs
operator|)
operator|.
name|compareTo
argument_list|(
name|rhs
argument_list|)
operator|>=
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
case|case
name|SimpleFilter
operator|.
name|LTE
case|:
try|try
block|{
return|return
operator|(
operator|(
name|Comparable
operator|)
name|lhs
operator|)
operator|.
name|compareTo
argument_list|(
name|rhs
argument_list|)
operator|<=
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
case|case
name|SimpleFilter
operator|.
name|APPROX
case|:
return|return
name|compareApproximate
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
return|;
case|case
name|SimpleFilter
operator|.
name|SUBSTRING
case|:
return|return
name|SimpleFilter
operator|.
name|compareSubstring
argument_list|(
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|rhs
argument_list|,
operator|(
name|String
operator|)
name|lhs
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unknown comparison operator: "
operator|+
name|op
argument_list|)
throw|;
block|}
block|}
comment|// If the LHS is not a comparable or boolean, check if it is an
comment|// array. If so, convert it to a list so we can treat it as a
comment|// collection.
if|if
condition|(
name|lhs
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|lhs
operator|=
name|convertArrayToList
argument_list|(
name|lhs
argument_list|)
expr_stmt|;
block|}
comment|// If LHS is a collection, then call compare() on each element
comment|// of the collection until a match is found.
if|if
condition|(
name|lhs
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
name|lhs
control|)
block|{
if|if
condition|(
name|compare
argument_list|(
name|o
argument_list|,
name|rhsUnknown
argument_list|,
name|op
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
comment|// Spec says SUBSTRING is false for all types other than string.
if|if
condition|(
name|op
operator|==
name|SimpleFilter
operator|.
name|SUBSTRING
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Since we cannot identify the LHS type, then we can only perform
comment|// equality comparison.
try|try
block|{
return|return
name|lhs
operator|.
name|equals
argument_list|(
name|coerceType
argument_list|(
name|lhs
argument_list|,
operator|(
name|String
operator|)
name|rhsUnknown
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|compareApproximate
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|rhs
operator|instanceof
name|String
condition|)
block|{
return|return
name|removeWhitespace
argument_list|(
operator|(
name|String
operator|)
name|lhs
argument_list|)
operator|.
name|equalsIgnoreCase
argument_list|(
name|removeWhitespace
argument_list|(
operator|(
name|String
operator|)
name|rhs
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|rhs
operator|instanceof
name|Character
condition|)
block|{
return|return
name|Character
operator|.
name|toLowerCase
argument_list|(
operator|(
name|Character
operator|)
name|lhs
argument_list|)
operator|==
name|Character
operator|.
name|toLowerCase
argument_list|(
operator|(
name|Character
operator|)
name|rhs
argument_list|)
return|;
block|}
return|return
name|lhs
operator|.
name|equals
argument_list|(
name|rhs
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|removeWhitespace
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|s
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Object
name|coerceType
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|String
name|rhsString
parameter_list|)
throws|throws
name|Exception
block|{
comment|// If the LHS expects a string, then we can just return
comment|// the RHS since it is a string.
if|if
condition|(
name|lhs
operator|.
name|getClass
argument_list|()
operator|==
name|rhsString
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
name|rhsString
return|;
block|}
comment|// Try to convert the RHS type to the LHS type by using
comment|// the string constructor of the LHS class, if it has one.
name|Object
name|rhs
decl_stmt|;
try|try
block|{
if|if
condition|(
name|lhs
operator|instanceof
name|Version
condition|)
block|{
name|rhs
operator|=
name|VersionTable
operator|.
name|getVersion
argument_list|(
name|rhsString
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
comment|// The Character class is a special case, since its constructor
comment|// does not take a string, so handle it separately.
if|if
condition|(
name|lhs
operator|instanceof
name|Character
condition|)
block|{
name|rhs
operator|=
name|rhsString
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Spec says we should trim number types.
if|if
condition|(
operator|(
name|lhs
operator|instanceof
name|Number
operator|)
operator|||
operator|(
name|lhs
operator|instanceof
name|Boolean
operator|)
condition|)
block|{
name|rhsString
operator|=
name|rhsString
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|Constructor
name|ctor
init|=
name|lhs
operator|.
name|getClass
argument_list|()
operator|.
name|getConstructor
argument_list|(
name|STRING_CLASS
argument_list|)
decl_stmt|;
name|ctor
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rhs
operator|=
name|ctor
operator|.
name|newInstance
argument_list|(
name|rhsString
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Could not instantiate class "
operator|+
name|lhs
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" from string constructor with argument '"
operator|+
name|rhsString
operator|+
literal|"' because "
operator|+
name|ex
argument_list|)
throw|;
block|}
return|return
name|rhs
return|;
block|}
comment|/**      * This is an ugly utility method to convert an array of primitives      * to an array of primitive wrapper objects. This method simplifies      * processing LDAP filters since the special case of primitive arrays      * can be ignored.      *      * @param array An array of primitive types.      * @return An corresponding array using pritive wrapper objects.      */
specifier|private
specifier|static
name|List
argument_list|<
name|Object
argument_list|>
name|convertArrayToList
parameter_list|(
name|Object
name|array
parameter_list|)
block|{
name|int
name|len
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|len
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|Array
operator|.
name|get
argument_list|(
name|array
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

