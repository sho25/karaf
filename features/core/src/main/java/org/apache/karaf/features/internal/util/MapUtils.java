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
name|MapUtils
block|{
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
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
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
argument_list|<
name|S
argument_list|,
name|Set
argument_list|<
name|T
argument_list|>
argument_list|>
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
argument_list|<
name|T
argument_list|>
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
argument_list|<
name|T
argument_list|>
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

