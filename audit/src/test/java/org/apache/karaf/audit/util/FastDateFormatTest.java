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
name|audit
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|FastDateFormatTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|FastDateFormat
name|cal
init|=
operator|new
name|FastDateFormat
argument_list|()
decl_stmt|;
name|long
name|time
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
operator|.
name|parse
argument_list|(
literal|"2017-11-05"
argument_list|)
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Nov  5"
argument_list|,
name|cal
operator|.
name|getDate
argument_list|(
name|time
argument_list|,
name|FastDateFormat
operator|.
name|MMM_D2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2017-11-05"
argument_list|,
name|cal
operator|.
name|getDate
argument_list|(
name|time
argument_list|,
name|FastDateFormat
operator|.
name|YYYY_MM_DD
argument_list|)
argument_list|)
expr_stmt|;
name|time
operator|+=
name|TimeUnit
operator|.
name|DAYS
operator|.
name|toMillis
argument_list|(
literal|5
argument_list|)
operator|+
name|TimeUnit
operator|.
name|HOURS
operator|.
name|toMillis
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Nov 10"
argument_list|,
name|cal
operator|.
name|getDate
argument_list|(
name|time
argument_list|,
name|FastDateFormat
operator|.
name|MMM_D2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2017-11-10"
argument_list|,
name|cal
operator|.
name|getDate
argument_list|(
name|time
argument_list|,
name|FastDateFormat
operator|.
name|YYYY_MM_DD
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimeZone
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|time
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
operator|.
name|parse
argument_list|(
literal|"2017-11-05"
argument_list|)
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|FastDateFormat
name|cal
init|=
operator|new
name|FastDateFormat
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT+5"
argument_list|)
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"+05:00"
argument_list|,
name|cal
operator|.
name|getDate
argument_list|(
name|time
argument_list|,
name|FastDateFormat
operator|.
name|XXX
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

