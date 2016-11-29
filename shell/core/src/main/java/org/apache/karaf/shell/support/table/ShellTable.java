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
name|support
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
name|OutputStreamWriter
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|CharsetEncoder
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
specifier|static
specifier|final
name|char
name|SEP_HORIZONTAL
init|=
literal|'─'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|SEP_VERTICAL
init|=
literal|'|'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|SEP_CROSS
init|=
literal|'┼'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|SEP_HORIZONTAL_ASCII
init|=
literal|'-'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|SEP_VERTICAL_ASCII
init|=
literal|'|'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|SEP_CROSS_ASCII
init|=
literal|'+'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_SEPARATOR
init|=
literal|" "
operator|+
name|SEP_VERTICAL
operator|+
literal|" "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_SEPARATOR_ASCII
init|=
literal|" "
operator|+
name|SEP_VERTICAL_ASCII
operator|+
literal|" "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_SEPARATOR_NO_FORMAT
init|=
literal|"\t"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Col
argument_list|>
name|cols
init|=
operator|new
name|ArrayList
argument_list|<>
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|showHeaders
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|separator
init|=
name|DEFAULT_SEPARATOR
decl_stmt|;
specifier|private
name|int
name|size
decl_stmt|;
specifier|private
name|String
name|emptyTableText
decl_stmt|;
specifier|public
name|ShellTable
parameter_list|()
block|{      }
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
name|Col
name|col
init|=
operator|new
name|Col
argument_list|(
name|header
argument_list|)
decl_stmt|;
name|cols
operator|.
name|add
argument_list|(
name|col
argument_list|)
expr_stmt|;
return|return
name|col
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
comment|/**      * Set text to display if there are no rows in the table.      *      * @param text the text to display when the table is empty.      * @return the shell table.      */
specifier|public
name|ShellTable
name|emptyTableText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|emptyTableText
operator|=
name|text
expr_stmt|;
return|return
name|this
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
name|print
argument_list|(
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|print
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|boolean
name|format
parameter_list|)
block|{
name|boolean
name|supported
init|=
literal|false
decl_stmt|;
name|String
name|encoding
init|=
name|getEncoding
argument_list|(
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|CharsetEncoder
name|encoder
init|=
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
operator|.
name|newEncoder
argument_list|()
decl_stmt|;
name|supported
operator|=
name|encoder
operator|.
name|canEncode
argument_list|(
name|separator
argument_list|)
operator|&&
name|encoder
operator|.
name|canEncode
argument_list|(
name|SEP_HORIZONTAL
argument_list|)
operator|&&
name|encoder
operator|.
name|canEncode
argument_list|(
name|SEP_CROSS
argument_list|)
expr_stmt|;
block|}
name|String
name|separator
init|=
name|supported
condition|?
name|this
operator|.
name|separator
else|:
name|DEFAULT_SEPARATOR_ASCII
decl_stmt|;
comment|// "normal" table rendering, with borders
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
name|adjustSize
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|format
operator|&&
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
name|int
name|iCol
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
if|if
condition|(
name|iCol
operator|++
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
name|underline
argument_list|(
name|col
operator|.
name|getSize
argument_list|()
argument_list|,
literal|false
argument_list|,
name|supported
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
name|underline
argument_list|(
name|col
operator|.
name|getSize
argument_list|()
operator|+
literal|3
argument_list|,
literal|true
argument_list|,
name|supported
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|iCol
operator|++
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
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
if|if
condition|(
operator|!
name|format
condition|)
block|{
if|if
condition|(
name|separator
operator|==
literal|null
operator|||
name|separator
operator|.
name|equals
argument_list|(
name|DEFAULT_SEPARATOR
argument_list|)
condition|)
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
name|DEFAULT_SEPARATOR_NO_FORMAT
argument_list|)
argument_list|)
expr_stmt|;
else|else
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
else|else
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
if|if
condition|(
name|format
operator|&&
name|rows
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|emptyTableText
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|emptyTableText
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getEncoding
parameter_list|(
name|PrintStream
name|ps
parameter_list|)
block|{
if|if
condition|(
name|ps
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.felix.gogo.runtime.threadio.ThreadPrintStream"
argument_list|)
condition|)
block|{
try|try
block|{
name|ps
operator|=
operator|(
name|PrintStream
operator|)
name|ps
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getCurrent"
argument_list|)
operator|.
name|invoke
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
block|}
try|try
block|{
name|Field
name|f
init|=
name|ps
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"charOut"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|OutputStreamWriter
name|osw
init|=
operator|(
name|OutputStreamWriter
operator|)
name|f
operator|.
name|get
argument_list|(
name|ps
argument_list|)
decl_stmt|;
return|return
name|osw
operator|.
name|getEncoding
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|adjustSize
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
name|int
name|i
init|=
name|cols
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|Col
name|col
init|=
name|cols
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
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
parameter_list|,
name|boolean
name|crossAtBeg
parameter_list|,
name|boolean
name|supported
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
name|supported
condition|?
name|SEP_HORIZONTAL
else|:
name|SEP_HORIZONTAL_ASCII
argument_list|)
expr_stmt|;
if|if
condition|(
name|crossAtBeg
condition|)
block|{
name|exmarks
index|[
literal|1
index|]
operator|=
name|supported
condition|?
name|SEP_CROSS
else|:
name|SEP_CROSS_ASCII
expr_stmt|;
block|}
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

