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
comment|/**  * Table element.  *   * @author ldywicki  */
end_comment

begin_class
specifier|public
class|class
name|TableElement
block|{
comment|/**      * Style to apply.      */
specifier|private
name|Style
name|style
init|=
operator|new
name|Style
argument_list|()
decl_stmt|;
specifier|public
name|TableElement
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setStyle
parameter_list|(
name|Style
name|style
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Style
name|getStyle
parameter_list|()
block|{
return|return
name|style
return|;
block|}
block|}
end_class

end_unit

