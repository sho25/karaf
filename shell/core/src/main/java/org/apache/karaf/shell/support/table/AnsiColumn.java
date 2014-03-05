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
name|support
operator|.
name|table
package|;
end_package

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|jansi
operator|.
name|Ansi
import|;
end_import

begin_comment
comment|/**  * Colored support for column.  */
end_comment

begin_class
specifier|public
class|class
name|AnsiColumn
extends|extends
name|Col
block|{
specifier|private
name|Ansi
operator|.
name|Color
name|color
decl_stmt|;
specifier|private
name|boolean
name|bold
decl_stmt|;
specifier|public
name|AnsiColumn
parameter_list|(
name|String
name|header
parameter_list|,
name|Ansi
operator|.
name|Color
name|color
parameter_list|,
name|boolean
name|bold
parameter_list|)
block|{
name|super
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|this
operator|.
name|color
operator|=
name|color
expr_stmt|;
name|this
operator|.
name|bold
operator|=
name|bold
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContent
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|String
name|in
init|=
name|super
operator|.
name|getContent
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|Ansi
name|ansi
init|=
name|Ansi
operator|.
name|ansi
argument_list|()
decl_stmt|;
name|ansi
operator|.
name|fg
argument_list|(
name|color
argument_list|)
expr_stmt|;
if|if
condition|(
name|bold
condition|)
name|ansi
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD
argument_list|)
expr_stmt|;
name|ansi
operator|.
name|a
argument_list|(
name|in
argument_list|)
expr_stmt|;
if|if
condition|(
name|bold
condition|)
name|ansi
operator|.
name|a
argument_list|(
name|Ansi
operator|.
name|Attribute
operator|.
name|INTENSITY_BOLD_OFF
argument_list|)
expr_stmt|;
name|ansi
operator|.
name|fg
argument_list|(
name|Ansi
operator|.
name|Color
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
return|return
name|ansi
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

