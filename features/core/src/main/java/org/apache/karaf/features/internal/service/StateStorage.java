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
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|features
operator|.
name|internal
operator|.
name|util
operator|.
name|JsonReader
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
name|features
operator|.
name|internal
operator|.
name|util
operator|.
name|JsonWriter
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|StateStorage
block|{
specifier|public
name|void
name|load
parameter_list|(
name|State
name|state
parameter_list|)
throws|throws
name|IOException
block|{
name|state
operator|.
name|repositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|state
operator|.
name|requirements
operator|.
name|clear
argument_list|()
expr_stmt|;
name|state
operator|.
name|installedFeatures
operator|.
name|clear
argument_list|()
expr_stmt|;
name|state
operator|.
name|managedBundles
operator|.
name|clear
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|getInputStream
argument_list|()
init|)
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|Map
name|json
init|=
operator|(
name|Map
operator|)
name|JsonReader
operator|.
name|read
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|state
operator|.
name|bootDone
operator|.
name|set
argument_list|(
operator|(
name|Boolean
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"bootDone"
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|repositories
operator|.
name|addAll
argument_list|(
name|toStringSet
argument_list|(
operator|(
name|Collection
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"repositories"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|requirements
operator|.
name|putAll
argument_list|(
name|toStringStringSetMap
argument_list|(
operator|(
name|Map
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"features"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|installedFeatures
operator|.
name|putAll
argument_list|(
name|toStringStringSetMap
argument_list|(
operator|(
name|Map
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"installed"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|stateFeatures
operator|.
name|putAll
argument_list|(
name|toStringStringStringMapMap
argument_list|(
operator|(
name|Map
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|managedBundles
operator|.
name|putAll
argument_list|(
name|toStringLongSetMap
argument_list|(
operator|(
name|Map
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"managed"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|bundleChecksums
operator|.
name|putAll
argument_list|(
name|toLongLongMap
argument_list|(
operator|(
name|Map
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"checksums"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|save
parameter_list|(
name|State
name|state
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|OutputStream
name|os
init|=
name|getOutputStream
argument_list|()
init|)
block|{
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|json
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"bootDone"
argument_list|,
name|state
operator|.
name|bootDone
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"repositories"
argument_list|,
name|state
operator|.
name|repositories
argument_list|)
expr_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"features"
argument_list|,
name|state
operator|.
name|requirements
argument_list|)
expr_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"installed"
argument_list|,
name|state
operator|.
name|installedFeatures
argument_list|)
expr_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"state"
argument_list|,
name|state
operator|.
name|stateFeatures
argument_list|)
expr_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"managed"
argument_list|,
name|state
operator|.
name|managedBundles
argument_list|)
expr_stmt|;
name|json
operator|.
name|put
argument_list|(
literal|"checksums"
argument_list|,
name|toStringLongMap
argument_list|(
name|state
operator|.
name|bundleChecksums
argument_list|)
argument_list|)
expr_stmt|;
name|JsonWriter
operator|.
name|write
argument_list|(
name|os
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
specifier|protected
specifier|abstract
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|toStringStringStringMapMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|nm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|nm
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|toStringStringMap
argument_list|(
operator|(
name|Map
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nm
return|;
block|}
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toStringStringMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|nm
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|nm
return|;
block|}
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|toStringStringSetMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|nm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|nm
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|toStringSet
argument_list|(
operator|(
name|Collection
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nm
return|;
block|}
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|toStringLongSetMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Long
argument_list|>
argument_list|>
name|nm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|nm
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|toLongSet
argument_list|(
operator|(
name|Collection
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nm
return|;
block|}
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|toStringSet
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|col
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|ns
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|col
control|)
block|{
name|ns
operator|.
name|add
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
specifier|static
name|Set
argument_list|<
name|Long
argument_list|>
name|toLongSet
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|set
parameter_list|)
block|{
name|Set
argument_list|<
name|Long
argument_list|>
name|ns
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|set
control|)
block|{
name|ns
operator|.
name|add
argument_list|(
name|toLong
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
specifier|static
name|Map
argument_list|<
name|Long
argument_list|,
name|Long
argument_list|>
name|toLongLongMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|Long
argument_list|,
name|Long
argument_list|>
name|nm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|nm
operator|.
name|put
argument_list|(
name|toLong
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|toLong
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nm
return|;
block|}
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|toStringLongMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|nm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|nm
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|toLong
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nm
return|;
block|}
specifier|static
name|long
name|toLong
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

