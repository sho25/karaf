begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
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
name|table
package|;
end_package

begin_comment
comment|/**  * Cell information.  */
end_comment

begin_class
specifier|public
class|class
name|Cell
extends|extends
name|TableElement
block|{
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|int
name|colSpan
decl_stmt|;
specifier|private
name|HAlign
name|align
decl_stmt|;
specifier|public
name|Cell
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|value
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Cell
parameter_list|(
name|Object
name|value
parameter_list|,
name|HAlign
name|align
parameter_list|)
block|{
name|this
argument_list|(
name|value
argument_list|,
name|align
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Cell
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|colSpan
parameter_list|)
block|{
name|this
argument_list|(
name|value
argument_list|,
literal|null
argument_list|,
name|colSpan
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Cell
parameter_list|(
name|Object
name|value
parameter_list|,
name|HAlign
name|align
parameter_list|,
name|int
name|colSpan
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|colSpan
operator|=
name|colSpan
expr_stmt|;
name|this
operator|.
name|align
operator|=
name|align
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setColSpan
parameter_list|(
name|int
name|colSpan
parameter_list|)
block|{
name|this
operator|.
name|colSpan
operator|=
name|colSpan
expr_stmt|;
block|}
specifier|public
name|int
name|getColSpan
parameter_list|()
block|{
return|return
name|colSpan
return|;
block|}
specifier|public
name|HAlign
name|getAlign
parameter_list|()
block|{
return|return
name|align
return|;
block|}
specifier|public
name|void
name|setAlign
parameter_list|(
name|HAlign
name|align
parameter_list|)
block|{
name|this
operator|.
name|align
operator|=
name|align
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"[Cell: "
operator|+
name|value
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

