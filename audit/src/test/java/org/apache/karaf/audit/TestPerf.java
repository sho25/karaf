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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|audit
operator|.
name|layout
operator|.
name|Rfc3164Layout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|audit
operator|.
name|layout
operator|.
name|Rfc5424Layout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|audit
operator|.
name|util
operator|.
name|Buffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

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
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Formatter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|Callable
import|;
end_import

begin_class
annotation|@
name|Ignore
specifier|public
class|class
name|TestPerf
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INVOKE
init|=
literal|"invoke"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|INVOKE_SIG
init|=
operator|new
name|String
index|[]
block|{
name|ObjectName
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Object
index|[]
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|String
index|[]
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testFormatString
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|iterations
init|=
literal|10000000
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Buffer
name|buffer0
init|=
operator|new
name|Buffer
argument_list|(
name|Buffer
operator|.
name|Format
operator|.
name|Json
argument_list|)
decl_stmt|;
name|long
name|t0
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|buffer0
operator|.
name|clear
argument_list|()
expr_stmt|;
name|buffer0
operator|.
name|format
argument_list|(
literal|"This is \"\n\tquite\n\tq\n\ta\n\tlong\n\tquote.\"\nIndeed !\n"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|iterations
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"json = "
operator|+
name|t0
argument_list|)
expr_stmt|;
specifier|final
name|Buffer
name|buffer1
init|=
operator|new
name|Buffer
argument_list|(
name|Buffer
operator|.
name|Format
operator|.
name|Syslog
argument_list|)
decl_stmt|;
name|long
name|t1
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|buffer1
operator|.
name|clear
argument_list|()
expr_stmt|;
name|buffer1
operator|.
name|format
argument_list|(
literal|"This is \"\n\tquite\n\tq\n\ta\n\tlong\n\tquote.\"\nIndeed !\n"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|iterations
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"syslog = "
operator|+
name|t1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGelfTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|timestamp
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|int
name|iterations
init|=
literal|1000000
decl_stmt|;
for|for
control|(
name|int
name|p
init|=
literal|0
init|;
name|p
operator|<
literal|10
condition|;
name|p
operator|++
control|)
block|{
name|Buffer
name|buffer
init|=
operator|new
name|Buffer
argument_list|(
name|Buffer
operator|.
name|Format
operator|.
name|Json
argument_list|)
decl_stmt|;
name|long
name|t0
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|buffer
operator|.
name|clear
argument_list|()
expr_stmt|;
name|long
name|secs
init|=
name|timestamp
operator|/
literal|1000
decl_stmt|;
name|int
name|ms
init|=
call|(
name|int
call|)
argument_list|(
name|timestamp
operator|-
name|secs
operator|*
literal|1000
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|format
argument_list|(
name|secs
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|int
name|temp
init|=
name|ms
operator|/
literal|100
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|temp
operator|+
literal|'0'
argument_list|)
argument_list|)
expr_stmt|;
name|ms
operator|-=
literal|100
operator|*
name|temp
expr_stmt|;
name|temp
operator|=
name|ms
operator|/
literal|10
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|temp
operator|+
literal|'0'
argument_list|)
argument_list|)
expr_stmt|;
name|ms
operator|-=
literal|10
operator|*
name|temp
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|ms
operator|+
literal|'0'
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|iterations
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"t0 = "
operator|+
name|t0
argument_list|)
expr_stmt|;
name|long
name|t1
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|buffer
operator|.
name|clear
argument_list|()
expr_stmt|;
name|long
name|secs
init|=
name|timestamp
operator|/
literal|1000
decl_stmt|;
name|int
name|ms
init|=
call|(
name|int
call|)
argument_list|(
name|timestamp
operator|-
name|secs
operator|*
literal|1000
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|format
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|secs
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|int
name|temp
init|=
name|ms
operator|/
literal|100
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|temp
operator|+
literal|'0'
argument_list|)
argument_list|)
expr_stmt|;
name|ms
operator|-=
literal|100
operator|*
name|temp
expr_stmt|;
name|temp
operator|=
name|ms
operator|/
literal|10
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|temp
operator|+
literal|'0'
argument_list|)
argument_list|)
expr_stmt|;
name|ms
operator|-=
literal|10
operator|*
name|temp
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|ms
operator|+
literal|'0'
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|iterations
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"t1 = "
operator|+
name|t1
argument_list|)
expr_stmt|;
name|long
name|t2
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|buffer
operator|.
name|clear
argument_list|()
expr_stmt|;
operator|new
name|Formatter
argument_list|(
name|buffer
argument_list|)
operator|.
name|format
argument_list|(
literal|"%.3f"
argument_list|,
operator|(
operator|(
name|double
operator|)
name|timestamp
operator|)
operator|/
literal|1000.0
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
name|iterations
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"t2 = "
operator|+
name|t2
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerialize
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|Event
operator|.
name|TYPE_JMX
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"subtype"
argument_list|,
name|INVOKE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"method"
argument_list|,
name|INVOKE
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"signature"
argument_list|,
name|INVOKE_SIG
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"params"
argument_list|,
operator|new
name|Object
index|[]
block|{
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.karaf.Mbean:type=foo"
argument_list|)
block|,
literal|"myMethod"
block|,
operator|new
name|Object
index|[]
block|{
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
block|,
operator|new
name|String
index|[]
block|{
literal|"the-param "
block|}
block|}
argument_list|)
expr_stmt|;
name|Event
name|event
init|=
operator|new
name|MapEvent
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|EventLayout
name|layout
init|=
operator|new
name|Rfc3164Layout
argument_list|(
literal|16
argument_list|,
literal|5
argument_list|,
name|Rfc5424Layout
operator|.
name|DEFAULT_ENTERPRISE_NUMBER
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"CET"
argument_list|)
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Writer
name|writer
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|baos
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|dt0
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|baos
operator|.
name|reset
argument_list|()
expr_stmt|;
name|layout
operator|.
name|format
argument_list|(
name|event
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
literal|10000000
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|dt0
argument_list|)
expr_stmt|;
name|long
name|dt1
init|=
name|measure
argument_list|(
parameter_list|()
lambda|->
block|{
name|baos
operator|.
name|reset
argument_list|()
expr_stmt|;
name|layout
operator|.
name|format
argument_list|(
name|event
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|,
literal|10000000
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|dt1
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|long
name|measure
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|runnable
parameter_list|,
name|int
name|runs
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|gc
argument_list|()
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
name|runs
operator|/
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|runnable
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|long
name|t0
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|runs
condition|;
name|i
operator|++
control|)
block|{
name|runnable
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
return|return
name|t1
operator|-
name|t0
return|;
block|}
block|}
end_class

end_unit

