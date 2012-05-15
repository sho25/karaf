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
name|table
package|;
end_package

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
name|Arrays
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

begin_class
specifier|public
class|class
name|ShellTable
block|{
specifier|private
name|List
argument_list|<
name|Col
argument_list|>
name|cols
init|=
operator|new
name|ArrayList
argument_list|<
name|Col
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
operator|new
name|ArrayList
argument_list|<
name|Row
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|showHeaders
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|separator
init|=
literal|" | "
decl_stmt|;
specifier|private
name|int
name|size
decl_stmt|;
specifier|public
name|ShellTable
parameter_list|()
block|{              }
specifier|public
name|ShellTable
name|noHeaders
parameter_list|()
block|{
name|this
operator|.
name|showHeaders
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ShellTable
name|separator
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
name|this
operator|.
name|separator
operator|=
name|separator
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ShellTable
name|size
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
return|return
name|this
return|;
block|}
specifier|public
name|ShellTable
name|column
parameter_list|(
name|Col
name|colunmn
parameter_list|)
block|{
name|cols
operator|.
name|add
argument_list|(
name|colunmn
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Col
name|column
parameter_list|(
name|String
name|header
parameter_list|)
block|{
return|return
operator|new
name|Col
argument_list|(
name|header
argument_list|)
return|;
block|}
specifier|public
name|Row
name|addRow
parameter_list|()
block|{
name|Row
name|row
init|=
operator|new
name|Row
argument_list|()
decl_stmt|;
name|rows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
return|return
name|row
return|;
block|}
specifier|public
name|void
name|print
parameter_list|(
name|PrintStream
name|out
parameter_list|)
block|{
name|Row
name|headerRow
init|=
operator|new
name|Row
argument_list|(
name|cols
argument_list|)
decl_stmt|;
name|headerRow
operator|.
name|formatContent
argument_list|(
name|cols
argument_list|)
expr_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|row
operator|.
name|formatContent
argument_list|(
name|cols
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|tryGrowToMaxSize
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|showHeaders
condition|)
block|{
name|String
name|headerLine
init|=
name|headerRow
operator|.
name|getContent
argument_list|(
name|cols
argument_list|,
name|separator
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|headerLine
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|underline
argument_list|(
name|headerLine
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|out
operator|.
name|println
argument_list|(
name|row
operator|.
name|getContent
argument_list|(
name|cols
argument_list|,
name|separator
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|tryGrowToMaxSize
parameter_list|()
block|{
name|int
name|currentSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Col
name|col
range|:
name|cols
control|)
block|{
name|currentSize
operator|+=
name|col
operator|.
name|size
operator|+
name|separator
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
name|currentSize
operator|-=
name|separator
operator|.
name|length
argument_list|()
expr_stmt|;
name|int
name|sizeToGrow
init|=
name|size
operator|-
name|currentSize
decl_stmt|;
for|for
control|(
name|Col
name|col
range|:
name|cols
control|)
block|{
if|if
condition|(
name|col
operator|.
name|maxSize
operator|==
operator|-
literal|1
condition|)
block|{
name|col
operator|.
name|size
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|col
operator|.
name|size
operator|+
name|sizeToGrow
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
specifier|private
name|String
name|underline
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|char
index|[]
name|exmarks
init|=
operator|new
name|char
index|[
name|length
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|exmarks
argument_list|,
literal|'-'
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|exmarks
argument_list|)
return|;
block|}
block|}
end_class

end_unit

