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
name|shell
operator|.
name|dev
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|LinkedList
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

begin_comment
comment|/**  * Represents a node in a {@link org.apache.karaf.shell.dev.util.Tree}  */
end_comment

begin_class
specifier|public
class|class
name|Node
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
specifier|final
name|T
name|value
decl_stmt|;
specifier|private
name|Node
argument_list|<
name|T
argument_list|>
name|parent
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Node
argument_list|<
name|T
argument_list|>
argument_list|>
name|children
init|=
operator|new
name|LinkedList
argument_list|<
name|Node
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new node. Only meant for wrapper use,      * new nodes should be added using the {@link #addChild(Object)} method      *      * @param value the node value      */
specifier|protected
name|Node
parameter_list|(
name|T
name|value
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Creates a new node. Only meant for wrapper use,      * new nodes should be added using the {@link #addChild(Object)} method      *      * @param value the node value      */
specifier|protected
name|Node
parameter_list|(
name|T
name|value
parameter_list|,
name|Node
argument_list|<
name|T
argument_list|>
name|parent
parameter_list|)
block|{
name|this
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|/**      * Access the node's value      */
specifier|public
name|T
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Access the node's child nodes      */
specifier|public
name|List
argument_list|<
name|Node
argument_list|<
name|T
argument_list|>
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|children
return|;
block|}
comment|/**      * Adds a child to this node      *      * @param value the child's value      * @return the child node      */
specifier|public
name|Node
name|addChild
parameter_list|(
name|T
name|value
parameter_list|)
block|{
name|Node
name|node
init|=
operator|new
name|Node
argument_list|(
name|value
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|children
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
comment|/**      * Give a set of values in the tree.      *      * @return      */
specifier|public
name|Set
argument_list|<
name|T
argument_list|>
name|flatten
parameter_list|()
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Node
argument_list|<
name|T
argument_list|>
name|child
range|:
name|getChildren
argument_list|()
control|)
block|{
name|result
operator|.
name|addAll
argument_list|(
name|child
operator|.
name|flatten
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Check if the node has an ancestor that represents the given value      *      * @param value the node value      * @return<code>true</code> it there's an ancestor that represents the value      */
specifier|public
name|boolean
name|hasAncestor
parameter_list|(
name|T
name|value
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|value
operator|.
name|equals
argument_list|(
name|parent
operator|.
name|value
argument_list|)
operator|||
name|parent
operator|.
name|hasAncestor
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
comment|/*      * Write this node to the PrintWriter.  It should be indented one step      * further for every element in the indents array.  If an element in the      * array is<code>true</code>, there should be a | to connect to the next      * sibling.      */
specifier|protected
name|void
name|write
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|Tree
operator|.
name|Converter
argument_list|<
name|T
argument_list|>
name|converter
parameter_list|,
name|boolean
modifier|...
name|indents
parameter_list|)
block|{
for|for
control|(
name|boolean
name|indent
range|:
name|indents
control|)
block|{
name|writer
operator|.
name|printf
argument_list|(
literal|"%-3s"
argument_list|,
name|indent
condition|?
literal|"|"
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|printf
argument_list|(
literal|"+- %s%n"
argument_list|,
name|converter
operator|.
name|toString
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Node
argument_list|<
name|T
argument_list|>
name|child
range|:
name|getChildren
argument_list|()
control|)
block|{
name|child
operator|.
name|write
argument_list|(
name|writer
argument_list|,
name|converter
argument_list|,
name|concat
argument_list|(
name|indents
argument_list|,
name|hasNextSibling
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Is this node the last child node for its parent      * or is there a next sibling?      */
specifier|private
name|boolean
name|hasNextSibling
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|parent
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
name|parent
operator|.
name|getChildren
argument_list|()
operator|.
name|indexOf
argument_list|(
name|this
argument_list|)
operator|<
name|parent
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
return|;
block|}
block|}
comment|/*      * Add an element to the end of the array      */
specifier|private
name|boolean
index|[]
name|concat
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
name|element
parameter_list|)
block|{
name|boolean
index|[]
name|result
init|=
operator|new
name|boolean
index|[
name|array
operator|.
name|length
operator|+
literal|1
index|]
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
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|result
index|[
name|i
index|]
operator|=
name|array
index|[
name|i
index|]
expr_stmt|;
block|}
name|result
index|[
name|array
operator|.
name|length
index|]
operator|=
name|element
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

