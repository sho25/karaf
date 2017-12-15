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
name|log
operator|.
name|core
operator|.
name|internal
operator|.
name|layout
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|FieldPosition
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParsePosition
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_comment
comment|/**  * Copied from log4j  */
end_comment

begin_comment
comment|/**    Formats a {@link Date} in the format "HH:mm:ss,SSS" for example,    "15:49:37,459".     @since 0.7.5 */
end_comment

begin_class
specifier|public
class|class
name|AbsoluteTimeDateFormat
extends|extends
name|DateFormat
block|{
comment|/**      String constant used to specify {@link AbsoluteTimeDateFormat} in layouts. Current      value is<b>ABSOLUTE</b>.  */
specifier|public
specifier|final
specifier|static
name|String
name|ABS_TIME_DATE_FORMAT
init|=
literal|"ABSOLUTE"
decl_stmt|;
comment|/**      String constant used to specify {@link DateTimeDateFormat} in layouts.  Current      value is<b>DATE</b>.   */
specifier|public
specifier|final
specifier|static
name|String
name|DATE_AND_TIME_DATE_FORMAT
init|=
literal|"DATE"
decl_stmt|;
comment|/**      String constant used to specify {@link ISO8601DateFormat} in layouts. Current      value is<b>ISO8601</b>.   */
specifier|public
specifier|final
specifier|static
name|String
name|ISO8601_DATE_FORMAT
init|=
literal|"ISO8601"
decl_stmt|;
specifier|public
name|AbsoluteTimeDateFormat
parameter_list|()
block|{
name|setCalendar
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbsoluteTimeDateFormat
parameter_list|(
name|TimeZone
name|timeZone
parameter_list|)
block|{
name|setCalendar
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|(
name|timeZone
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|long
name|previousTime
decl_stmt|;
specifier|private
specifier|static
name|char
index|[]
name|previousTimeWithoutMillis
init|=
operator|new
name|char
index|[
literal|9
index|]
decl_stmt|;
comment|// "HH:mm:ss."
comment|/**      Appends to<code>sbuf</code> the time in the format      "HH:mm:ss,SSS" for example, "15:49:37,459"       @param date the date to format      @param sbuf the string buffer to write to      @param fieldPosition remains untouched     */
specifier|public
name|StringBuffer
name|format
parameter_list|(
name|Date
name|date
parameter_list|,
name|StringBuffer
name|sbuf
parameter_list|,
name|FieldPosition
name|fieldPosition
parameter_list|)
block|{
name|long
name|now
init|=
name|date
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|int
name|millis
init|=
call|(
name|int
call|)
argument_list|(
name|now
operator|%
literal|1000
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|now
operator|-
name|millis
operator|)
operator|!=
name|previousTime
condition|)
block|{
comment|// We reach this point at most once per second
comment|// across all threads instead of each time format()
comment|// is called. This saves considerable CPU time.
name|calendar
operator|.
name|setTime
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|int
name|start
init|=
name|sbuf
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|hour
init|=
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
decl_stmt|;
if|if
condition|(
name|hour
operator|<
literal|10
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|sbuf
operator|.
name|append
argument_list|(
name|hour
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|int
name|mins
init|=
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|mins
operator|<
literal|10
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|sbuf
operator|.
name|append
argument_list|(
name|mins
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|int
name|secs
init|=
name|calendar
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
decl_stmt|;
if|if
condition|(
name|secs
operator|<
literal|10
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|sbuf
operator|.
name|append
argument_list|(
name|secs
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
comment|// store the time string for next time to avoid recomputation
name|sbuf
operator|.
name|getChars
argument_list|(
name|start
argument_list|,
name|sbuf
operator|.
name|length
argument_list|()
argument_list|,
name|previousTimeWithoutMillis
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|previousTime
operator|=
name|now
operator|-
name|millis
expr_stmt|;
block|}
else|else
block|{
name|sbuf
operator|.
name|append
argument_list|(
name|previousTimeWithoutMillis
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|millis
operator|<
literal|100
condition|)
name|sbuf
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
if|if
condition|(
name|millis
operator|<
literal|10
condition|)
name|sbuf
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
name|sbuf
operator|.
name|append
argument_list|(
name|millis
argument_list|)
expr_stmt|;
return|return
name|sbuf
return|;
block|}
comment|/**      This method does not do anything but return<code>null</code>.    */
specifier|public
name|Date
name|parse
parameter_list|(
name|String
name|s
parameter_list|,
name|ParsePosition
name|pos
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

