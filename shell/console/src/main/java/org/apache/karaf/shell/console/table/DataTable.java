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

begin_import
import|import static
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
operator|.
name|StringUtil
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|List
import|;
end_import

begin_comment
comment|/**  * Simple data table.  *  * This class contains set of formatting code needed to print tabular data in console. There are two ways to construct  * table.  * 1) Provide output and number of columns  * 2) Provide output and manually configure columns  * First way is very fast and takes small amount of code, but you cannot configure column behavior. All columns will be  * expandable and aligned to left side.  * Second way takes more code but allows you to control column format.  *  * To avoid performance issues data table flushes output once per 10 rows. If you wish to change this - just set value  * for flushAfter field.  * Important notice - last column always don't have borders. It is designed to put mark last column as expandable and  * put there longer values.  *  * Code samples:  *<code>  *      DataTable table = new DataTable(System.out);  *  *      table.addColumn(new Column(3, true));  *      table.addColumn(new Column(5));  *      table.addColumn(new Column(12));  *      table.addColumn(new Column(12));  *      table.addColumn(new Column(5));  *      table.addColumn(new Column(true));  *  *      Row row = new Row();  *      row.addCell(new Cell("OSGi", HAlign.center, 2));  *      row.addCell(new Cell("Extender", HAlign.center, 2));  *      row.addCell(new Cell("Misc", 2));  *      table.addRow(row);  *  *      row = new Row();  *      row.addCell("ID");  *      row.addCell("State");  *      row.addCell("Spring");  *      row.addCell("Blueprint");  *      row.addCell("Level");  *      row.addCell("Name");  *      table.addRow(row);  *  *      // load sample data  *      for (int i = 0; i< 5; i++) {  *          table.addRow(new Object[] {i, i, i, i, i, i});  *      }  *      table.flush();  *</code>  * And expected output:  *<code>  * [   OSGi   ][         Extender         ] Misc  * [ID ][State][Spring      ][Blueprint   ][Level] Name  * [0  ][0    ][0           ][0           ][0    ] 0  * [1  ][1    ][1           ][1           ][1    ] 1  * [2  ][2    ][2           ][2           ][2    ] 2  * [3  ][3    ][3           ][3           ][3    ] 3  * [4  ][4    ][4           ][4           ][4    ] 4  *</code>  */
end_comment

begin_class
specifier|public
class|class
name|DataTable
extends|extends
name|TableElement
block|{
comment|/**      * Output destination.      */
specifier|private
specifier|final
name|Appendable
name|target
decl_stmt|;
comment|/**      * Table border style.      */
specifier|private
name|Style
name|borderStyle
init|=
operator|new
name|Style
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Column
argument_list|>
name|columns
init|=
operator|new
name|ArrayList
argument_list|<
name|Column
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
comment|/**      * Number of rows to add before flushing rows to stream.      */
specifier|private
name|int
name|flushAfter
init|=
literal|10
decl_stmt|;
specifier|public
name|DataTable
parameter_list|(
name|Appendable
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
specifier|public
name|DataTable
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|int
name|colCount
parameter_list|)
block|{
name|this
argument_list|(
name|out
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|colCount
condition|;
name|i
operator|++
control|)
block|{
name|addColumn
argument_list|(
operator|new
name|Column
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addColumn
parameter_list|(
name|Column
name|column
parameter_list|)
block|{
name|this
operator|.
name|columns
operator|.
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printBorder
parameter_list|(
name|Row
name|row
parameter_list|,
name|String
name|border
parameter_list|)
block|{
if|if
condition|(
name|row
operator|.
name|isBorders
argument_list|()
condition|)
block|{
name|append
argument_list|(
name|borderStyle
operator|.
name|apply
argument_list|(
name|border
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|append
argument_list|(
name|repeat
argument_list|(
literal|" "
argument_list|,
name|border
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|flush
parameter_list|()
block|{
name|printRows
argument_list|()
expr_stmt|;
name|rows
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|printRows
parameter_list|()
block|{
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|List
argument_list|<
name|Cell
argument_list|>
name|cells
init|=
name|row
operator|.
name|getCells
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|colIndex
init|=
literal|0
init|,
name|size
init|=
name|cells
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Cell
name|cell
init|=
name|cells
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|colSpan
init|=
name|cell
operator|.
name|getColSpan
argument_list|()
decl_stmt|;
name|Column
name|column
init|=
name|columns
operator|.
name|get
argument_list|(
name|colIndex
argument_list|)
decl_stmt|;
name|int
name|colSize
init|=
literal|0
decl_stmt|;
name|boolean
name|first
init|=
name|i
operator|==
literal|0
decl_stmt|;
name|boolean
name|last
init|=
name|i
operator|+
literal|1
operator|==
name|size
decl_stmt|;
if|if
condition|(
name|colSpan
operator|>
literal|1
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|colSpan
condition|;
name|j
operator|++
control|)
block|{
name|colSize
operator|+=
name|columns
operator|.
name|get
argument_list|(
name|colIndex
operator|+
name|j
argument_list|)
operator|.
name|getSize
argument_list|()
expr_stmt|;
block|}
name|colSize
operator|+=
name|colSpan
expr_stmt|;
name|colIndex
operator|+=
name|colSpan
expr_stmt|;
block|}
else|else
block|{
name|colSize
operator|=
name|column
operator|.
name|getSize
argument_list|()
expr_stmt|;
name|colIndex
operator|++
expr_stmt|;
block|}
name|Style
name|style
init|=
name|calculateStyle
argument_list|(
name|column
argument_list|,
name|row
argument_list|,
name|cell
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|printBorder
argument_list|(
name|row
argument_list|,
literal|"["
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|last
condition|)
block|{
name|printBorder
argument_list|(
name|row
argument_list|,
literal|"]["
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|printBorder
argument_list|(
name|row
argument_list|,
literal|"] "
argument_list|)
expr_stmt|;
block|}
name|String
name|value
init|=
name|cell
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|>
name|colSize
condition|)
block|{
if|if
condition|(
name|column
operator|.
name|isMayGrow
argument_list|()
condition|)
block|{
name|column
operator|.
name|setSize
argument_list|(
name|value
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
name|value
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
operator|+
literal|".."
expr_stmt|;
block|}
block|}
name|append
argument_list|(
name|style
operator|.
name|apply
argument_list|(
name|calculateAlign
argument_list|(
name|column
argument_list|,
name|cell
argument_list|)
operator|.
name|position
argument_list|(
name|value
argument_list|,
name|colSize
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|HAlign
name|calculateAlign
parameter_list|(
name|Column
name|column
parameter_list|,
name|Cell
name|cell
parameter_list|)
block|{
if|if
condition|(
name|cell
operator|.
name|getAlign
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|cell
operator|.
name|getAlign
argument_list|()
return|;
block|}
return|return
name|column
operator|.
name|getAlign
argument_list|()
return|;
block|}
specifier|private
name|Style
name|calculateStyle
parameter_list|(
name|Column
name|column
parameter_list|,
name|Row
name|row
parameter_list|,
name|Cell
name|cell
parameter_list|)
block|{
name|StyleCalculator
name|styleCalculator
init|=
name|column
operator|.
name|getStyleCalculator
argument_list|()
decl_stmt|;
name|Style
name|dynamic
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|styleCalculator
operator|!=
literal|null
condition|)
block|{
name|dynamic
operator|=
name|styleCalculator
operator|.
name|calculate
argument_list|(
name|cell
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|cell
operator|.
name|getStyle
argument_list|()
operator|.
name|isClean
argument_list|()
condition|)
block|{
return|return
name|cell
operator|.
name|getStyle
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|dynamic
operator|!=
literal|null
condition|)
block|{
return|return
name|dynamic
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|row
operator|.
name|getStyle
argument_list|()
operator|.
name|isClean
argument_list|()
condition|)
block|{
return|return
name|row
operator|.
name|getStyle
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|column
operator|.
name|getStyle
argument_list|()
operator|.
name|isClean
argument_list|()
condition|)
block|{
return|return
name|column
operator|.
name|getStyle
argument_list|()
return|;
block|}
return|return
operator|new
name|Style
argument_list|()
return|;
block|}
specifier|private
name|void
name|append
parameter_list|(
name|String
name|string
parameter_list|)
block|{
try|try
block|{
name|target
operator|.
name|append
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addRow
parameter_list|(
name|Object
index|[]
name|row
parameter_list|)
block|{
name|addRow
argument_list|(
operator|new
name|Row
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addRow
parameter_list|(
name|Row
name|row
parameter_list|)
block|{
name|rows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|rows
operator|.
name|size
argument_list|()
operator|%
name|flushAfter
operator|)
operator|==
literal|0
condition|)
block|{
name|printRows
argument_list|()
expr_stmt|;
name|rows
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBorderStyle
parameter_list|(
name|Style
name|style
parameter_list|)
block|{
name|this
operator|.
name|borderStyle
operator|=
name|style
expr_stmt|;
block|}
specifier|public
name|void
name|setFlushAfter
parameter_list|(
name|int
name|flushAfter
parameter_list|)
block|{
name|this
operator|.
name|flushAfter
operator|=
name|flushAfter
expr_stmt|;
block|}
block|}
end_class

end_unit

