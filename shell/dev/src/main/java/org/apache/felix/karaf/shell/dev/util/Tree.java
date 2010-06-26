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
name|felix
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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

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
name|Set
import|;
end_import

begin_comment
comment|/**  * Represents a tree that can be written to the console.  *  * The output will look like this:  *<pre>  * root  * +- child1  * |  +- grandchild  * +- child2  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|Tree
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Node
argument_list|<
name|T
argument_list|>
block|{
comment|/**      * Creates a new tree with the given root node      *      * @param root the root node      */
specifier|public
name|Tree
parameter_list|(
name|T
name|root
parameter_list|)
block|{
name|super
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write the tree to a PrintStream, using the default toString() method to output the node values      *      * @param stream      */
specifier|public
name|void
name|write
parameter_list|(
name|PrintStream
name|stream
parameter_list|)
block|{
name|write
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write the tree to a PrintStream, using the provided converter to output the node values      *      * @param stream      * @param converter      */
specifier|public
name|void
name|write
parameter_list|(
name|PrintStream
name|stream
parameter_list|,
name|Converter
argument_list|<
name|T
argument_list|>
name|converter
parameter_list|)
block|{
name|write
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|stream
argument_list|)
argument_list|,
name|converter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write the tree to a PrintWriter, using the default toString() method to output the node values      *      * @param writer      */
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|write
argument_list|(
name|writer
argument_list|,
operator|new
name|Converter
argument_list|()
block|{
specifier|public
name|String
name|toString
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
name|node
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write the tree to a PrintWriter, using the provided converter to output the node values      *      * @param writer      * @param converter      */
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|Converter
argument_list|<
name|T
argument_list|>
name|converter
parameter_list|)
block|{
name|writer
operator|.
name|printf
argument_list|(
literal|"%s%n"
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
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * Interface to convert node values to string      *      * @param<T> the object type for the node value      */
specifier|public
specifier|static
interface|interface
name|Converter
parameter_list|<
name|T
parameter_list|>
block|{
specifier|public
name|String
name|toString
parameter_list|(
name|Node
argument_list|<
name|T
argument_list|>
name|node
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

