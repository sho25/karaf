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
comment|/**  * Column definition.  *   * @author ldywicki  */
end_comment

begin_class
specifier|public
class|class
name|Column
extends|extends
name|TableElement
block|{
comment|/**      * Column header.      */
specifier|private
name|String
name|header
decl_stmt|;
comment|/**      * Preferred size.      */
specifier|private
name|int
name|size
decl_stmt|;
comment|/**      * This flag allows to grow column when value in row is longer than initial column size. After growing 'size' will      * be increased.      */
specifier|private
name|boolean
name|mayGrow
decl_stmt|;
comment|/**      * Default align.      */
specifier|private
name|HAlign
name|align
init|=
name|HAlign
operator|.
name|left
decl_stmt|;
comment|/**      * Optional element which allows to set style dynamically.      */
specifier|private
name|StyleCalculator
name|styleCalculator
decl_stmt|;
specifier|public
name|Column
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
specifier|public
name|Column
parameter_list|()
block|{     }
specifier|public
name|Column
parameter_list|(
name|int
name|size
parameter_list|,
name|boolean
name|mayGrow
parameter_list|,
name|HAlign
name|align
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|mayGrow
operator|=
name|mayGrow
expr_stmt|;
name|this
operator|.
name|align
operator|=
name|align
expr_stmt|;
block|}
specifier|public
name|Column
parameter_list|(
name|int
name|size
parameter_list|,
name|boolean
name|mayGrow
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|mayGrow
operator|=
name|mayGrow
expr_stmt|;
block|}
specifier|public
name|Column
parameter_list|(
name|int
name|size
parameter_list|,
name|HAlign
name|align
parameter_list|)
block|{
name|this
argument_list|(
name|size
argument_list|,
literal|false
argument_list|,
name|align
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Column
parameter_list|(
name|boolean
name|mayGrow
parameter_list|)
block|{
name|this
argument_list|(
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Column
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
argument_list|(
name|size
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
block|}
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMayGrow
parameter_list|()
block|{
return|return
name|mayGrow
return|;
block|}
specifier|public
name|void
name|setMayGrow
parameter_list|(
name|boolean
name|mayGrow
parameter_list|)
block|{
name|this
operator|.
name|mayGrow
operator|=
name|mayGrow
expr_stmt|;
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
specifier|public
name|StyleCalculator
name|getStyleCalculator
parameter_list|()
block|{
return|return
name|styleCalculator
return|;
block|}
specifier|public
name|void
name|setStyleCalculator
parameter_list|(
name|StyleCalculator
name|styleCalculator
parameter_list|)
block|{
name|this
operator|.
name|styleCalculator
operator|=
name|styleCalculator
expr_stmt|;
block|}
block|}
end_class

end_unit

