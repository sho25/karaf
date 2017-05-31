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
name|region
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
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
name|Deque
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|Region
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionDigraphVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|equinox
operator|.
name|region
operator|.
name|RegionFilter
import|;
end_import

begin_comment
comment|/**  * {@link AbstractRegionDigraphVisitor} is an abstract base class for {@link RegionDigraphVisitor} implementations  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRegionDigraphVisitor
parameter_list|<
name|C
parameter_list|>
implements|implements
name|RegionDigraphVisitor
block|{
specifier|private
specifier|final
name|Collection
argument_list|<
name|C
argument_list|>
name|allCandidates
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|Set
argument_list|<
name|C
argument_list|>
argument_list|>
name|allowedDeque
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|Collection
argument_list|<
name|C
argument_list|>
argument_list|>
name|filteredDeque
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|C
argument_list|>
name|allowed
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|AbstractRegionDigraphVisitor
parameter_list|(
name|Collection
argument_list|<
name|C
argument_list|>
name|candidates
parameter_list|)
block|{
name|this
operator|.
name|allCandidates
operator|=
name|candidates
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|C
argument_list|>
name|getAllowed
parameter_list|()
block|{
return|return
name|allowed
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|visit
parameter_list|(
name|Region
name|region
parameter_list|)
block|{
name|Collection
argument_list|<
name|C
argument_list|>
name|candidates
init|=
name|filteredDeque
operator|.
name|isEmpty
argument_list|()
condition|?
name|allCandidates
else|:
name|filteredDeque
operator|.
name|peek
argument_list|()
decl_stmt|;
for|for
control|(
name|C
name|candidate
range|:
name|candidates
control|)
block|{
if|if
condition|(
name|contains
argument_list|(
name|region
argument_list|,
name|candidate
argument_list|)
condition|)
block|{
name|allowed
operator|.
name|add
argument_list|(
name|candidate
argument_list|)
expr_stmt|;
block|}
block|}
comment|// there is no need to traverse edges of this region,
comment|// it contains all the remaining filtered candidates
return|return
operator|!
name|allowed
operator|.
name|containsAll
argument_list|(
name|candidates
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|preEdgeTraverse
parameter_list|(
name|RegionFilter
name|regionFilter
parameter_list|)
block|{
comment|// Find the candidates filtered by the previous edge
name|Collection
argument_list|<
name|C
argument_list|>
name|filtered
init|=
name|filteredDeque
operator|.
name|isEmpty
argument_list|()
condition|?
name|allCandidates
else|:
name|filteredDeque
operator|.
name|peek
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|C
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|filtered
argument_list|)
decl_stmt|;
comment|// remove any candidates contained in the current region
name|candidates
operator|.
name|removeAll
argument_list|(
name|allowed
argument_list|)
expr_stmt|;
comment|// apply the filter across remaining candidates
name|Iterator
argument_list|<
name|C
argument_list|>
name|i
init|=
name|candidates
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|C
name|candidate
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isAllowed
argument_list|(
name|candidate
argument_list|,
name|regionFilter
argument_list|)
condition|)
block|{
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
comment|// this filter does not apply; avoid traversing this edge
block|}
comment|// push the filtered candidates for the next region
name|filteredDeque
operator|.
name|push
argument_list|(
name|candidates
argument_list|)
expr_stmt|;
comment|// push the allowed
name|allowedDeque
operator|.
name|push
argument_list|(
name|allowed
argument_list|)
expr_stmt|;
name|allowed
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|void
name|postEdgeTraverse
parameter_list|(
name|RegionFilter
name|regionFilter
parameter_list|)
block|{
name|filteredDeque
operator|.
name|poll
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|C
argument_list|>
name|candidates
init|=
name|allowed
decl_stmt|;
name|allowed
operator|=
name|allowedDeque
operator|.
name|pop
argument_list|()
expr_stmt|;
name|allowed
operator|.
name|addAll
argument_list|(
name|candidates
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determines whether the given region contains the given candidate.      *      * @param region    the {@link Region}      * @param candidate the candidate      * @return<code>true</code> if and only if the given region contains the given candidate      */
specifier|protected
specifier|abstract
name|boolean
name|contains
parameter_list|(
name|Region
name|region
parameter_list|,
name|C
name|candidate
parameter_list|)
function_decl|;
comment|/**      * Determines whether the given candidate is allowed by the given {@link RegionFilter}.      *      * @param candidate the candidate      * @param filter    the filter      * @return<code>true</code> if and only if the given candidate is allowed by the given filter      */
specifier|protected
specifier|abstract
name|boolean
name|isAllowed
parameter_list|(
name|C
name|candidate
parameter_list|,
name|RegionFilter
name|filter
parameter_list|)
function_decl|;
block|}
end_class

end_unit

