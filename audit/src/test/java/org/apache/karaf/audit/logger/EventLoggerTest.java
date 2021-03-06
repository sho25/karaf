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
name|logger
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
name|Event
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
name|EventLayout
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
name|EventLogger
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
name|MapEvent
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
name|GelfLayout
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
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
name|Collections
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
name|List
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
name|Executors
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
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|EventLoggerTest
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
name|testUdp
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
argument_list|,
literal|1510902000000L
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|getNewPort
argument_list|()
decl_stmt|;
name|DatagramSocket
name|socket
init|=
operator|new
name|DatagramSocket
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DatagramPacket
argument_list|>
name|packets
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|DatagramPacket
name|dp
init|=
operator|new
name|DatagramPacket
argument_list|(
operator|new
name|byte
index|[
literal|1024
index|]
argument_list|,
literal|1024
argument_list|)
decl_stmt|;
name|socket
operator|.
name|receive
argument_list|(
name|dp
argument_list|)
expr_stmt|;
name|packets
operator|.
name|add
argument_list|(
name|dp
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
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
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
literal|"GMT+01:00"
argument_list|)
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
name|EventLogger
name|logger
init|=
operator|new
name|UdpEventLogger
argument_list|(
literal|"localhost"
argument_list|,
name|port
argument_list|,
literal|"UTF-8"
argument_list|,
name|layout
argument_list|)
decl_stmt|;
name|logger
operator|.
name|write
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|packets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DatagramPacket
name|p
init|=
name|packets
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|str
init|=
operator|new
name|String
argument_list|(
name|p
operator|.
name|getData
argument_list|()
argument_list|,
literal|0
argument_list|,
name|p
operator|.
name|getLength
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|startsWith
argument_list|(
literal|"<133>Nov 17 08:00:00 "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|endsWith
argument_list|(
literal|" jmx [jmx@18060 type=\"jmx\" subtype=\"invoke\" method=\"invoke\" signature=\"[javax.management.ObjectName, java.lang.String, [Ljava.lang.Object;, [Ljava.lang.String;\\]\" params=\"[org.apache.karaf.Mbean:type=foo, myMethod, [java.lang.String\\], [the-param \\]\\]\"]"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTcp
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
argument_list|,
literal|1510902000000L
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|getNewPort
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|packets
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
init|(
name|ServerSocket
name|ssocket
init|=
operator|new
name|ServerSocket
argument_list|(
name|port
argument_list|)
init|)
block|{
name|ssocket
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
init|(
name|Socket
name|socket
init|=
name|ssocket
operator|.
name|accept
argument_list|()
init|)
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|nb
init|=
name|socket
operator|.
name|getInputStream
argument_list|()
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|packets
operator|.
name|add
argument_list|(
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|nb
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|EventLayout
name|layout
init|=
operator|new
name|Rfc5424Layout
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
literal|"GMT+01:00"
argument_list|)
argument_list|)
decl_stmt|;
name|EventLogger
name|logger
init|=
operator|new
name|TcpEventLogger
argument_list|(
literal|"localhost"
argument_list|,
name|port
argument_list|,
literal|"UTF-8"
argument_list|,
name|layout
argument_list|)
decl_stmt|;
name|logger
operator|.
name|write
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|logger
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|packets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|packets
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|startsWith
argument_list|(
literal|"<133>1 2017-11-17T08:00:00.000+01:00 "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|indexOf
argument_list|(
literal|" jmx [jmx@18060 type=\"jmx\" subtype=\"invoke\" method=\"invoke\" signature=\"[javax.management.ObjectName, java.lang.String, [Ljava.lang.Object;, [Ljava.lang.String;\\]\" params=\"[org.apache.karaf.Mbean:type=foo, myMethod, [java.lang.String\\], [the-param \\]\\]\"]"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFile
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
name|EventLayout
name|layout
init|=
operator|new
name|GelfLayout
argument_list|()
decl_stmt|;
name|Path
name|path
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"file-logger"
argument_list|)
decl_stmt|;
name|String
name|file
init|=
name|path
operator|.
name|resolve
argument_list|(
literal|"file.log"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EventLogger
name|logger
init|=
operator|new
name|FileEventLogger
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|,
literal|"daily"
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|,
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
argument_list|,
name|layout
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT+01:00"
argument_list|)
argument_list|)
decl_stmt|;
name|logger
operator|.
name|write
argument_list|(
operator|new
name|MapEvent
argument_list|(
name|map
argument_list|,
literal|1510902000000L
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|write
argument_list|(
operator|new
name|MapEvent
argument_list|(
name|map
argument_list|,
literal|1510984800000L
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Path
argument_list|>
name|paths
init|=
name|Files
operator|.
name|list
argument_list|(
name|path
argument_list|)
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|paths
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|paths
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file-2017-11-18.log"
argument_list|,
name|paths
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file.log"
argument_list|,
name|paths
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Files
operator|.
name|readAllLines
argument_list|(
name|paths
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|lines
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|lines
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|startsWith
argument_list|(
literal|"{ version=\"1.1\" host=\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"timestamp=1510902000.000 short_message=\"jmx.invoke\" _type=\"jmx\" _subtype=\"invoke\" _method=\"invoke\" _signature=\"[javax.management.ObjectName, java.lang.String, [Ljava.lang.Object;, [Ljava.lang.String;]\" _params=\"[org.apache.karaf.Mbean:type=foo, myMethod, [java.lang.String], [the-param ]]\" }"
argument_list|)
argument_list|)
expr_stmt|;
name|lines
operator|=
name|Files
operator|.
name|readAllLines
argument_list|(
name|paths
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|lines
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|str
operator|=
name|lines
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|str
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|startsWith
argument_list|(
literal|"{ version=\"1.1\" host=\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"timestamp=1510984800.000 short_message=\"jmx.invoke\" _type=\"jmx\" _subtype=\"invoke\" _method=\"invoke\" _signature=\"[javax.management.ObjectName, java.lang.String, [Ljava.lang.Object;, [Ljava.lang.String;]\" _params=\"[org.apache.karaf.Mbean:type=foo, myMethod, [java.lang.String], [the-param ]]\" }"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileMaxFiles
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
name|TYPE_SHELL
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"subtype"
argument_list|,
literal|"executed"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"script"
argument_list|,
literal|"a-script"
argument_list|)
expr_stmt|;
name|EventLayout
name|layout
init|=
operator|new
name|GelfLayout
argument_list|()
decl_stmt|;
name|Path
name|path
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"file-logger"
argument_list|)
decl_stmt|;
name|String
name|file
init|=
name|path
operator|.
name|resolve
argument_list|(
literal|"file.log"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EventLogger
name|logger
init|=
operator|new
name|FileEventLogger
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|,
literal|"daily"
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|,
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
argument_list|,
name|layout
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT+01:00"
argument_list|)
argument_list|)
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
name|logger
operator|.
name|write
argument_list|(
operator|new
name|MapEvent
argument_list|(
name|map
argument_list|,
literal|1510902000000L
operator|+
name|TimeUnit
operator|.
name|DAYS
operator|.
name|toMillis
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|paths
init|=
name|Files
operator|.
name|list
argument_list|(
name|path
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|getFileName
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|toString
argument_list|)
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"file-2017-11-25.log"
argument_list|,
literal|"file-2017-11-26.log"
argument_list|,
literal|"file.log"
argument_list|)
argument_list|,
name|paths
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileSize
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
name|TYPE_SHELL
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"subtype"
argument_list|,
literal|"executed"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"script"
argument_list|,
literal|"a-script"
argument_list|)
expr_stmt|;
name|EventLayout
name|layout
init|=
operator|new
name|GelfLayout
argument_list|()
decl_stmt|;
name|Path
name|path
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"file-logger"
argument_list|)
decl_stmt|;
name|String
name|file
init|=
name|path
operator|.
name|resolve
argument_list|(
literal|"file.log"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EventLogger
name|logger
init|=
operator|new
name|FileEventLogger
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|,
literal|"size(10)"
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|,
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
argument_list|,
name|layout
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT+01:00"
argument_list|)
argument_list|)
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
name|logger
operator|.
name|write
argument_list|(
operator|new
name|MapEvent
argument_list|(
name|map
argument_list|,
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2017
argument_list|,
literal|11
argument_list|,
literal|17
argument_list|,
literal|7
argument_list|,
literal|0
argument_list|)
operator|.
name|toInstant
argument_list|(
name|ZoneOffset
operator|.
name|of
argument_list|(
literal|"+01:00"
argument_list|)
argument_list|)
operator|.
name|toEpochMilli
argument_list|()
operator|+
name|TimeUnit
operator|.
name|HOURS
operator|.
name|toMillis
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|paths
init|=
name|Files
operator|.
name|list
argument_list|(
name|path
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|getFileName
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|toString
argument_list|)
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"file-2017-11-17-2.log"
argument_list|,
literal|"file-2017-11-17.log"
argument_list|,
literal|"file.log"
argument_list|)
argument_list|,
name|paths
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileSizeCompress
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
name|TYPE_SHELL
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"subtype"
argument_list|,
literal|"executed"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"script"
argument_list|,
literal|"a-script"
argument_list|)
expr_stmt|;
name|EventLayout
name|layout
init|=
operator|new
name|GelfLayout
argument_list|()
decl_stmt|;
name|Path
name|path
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"file-logger"
argument_list|)
decl_stmt|;
name|String
name|file
init|=
name|path
operator|.
name|resolve
argument_list|(
literal|"file.log"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|EventLogger
name|logger
init|=
operator|new
name|FileEventLogger
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|,
literal|"size(10)"
argument_list|,
literal|2
argument_list|,
literal|true
argument_list|,
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
argument_list|,
name|layout
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT+01:00"
argument_list|)
argument_list|)
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
name|logger
operator|.
name|write
argument_list|(
operator|new
name|MapEvent
argument_list|(
name|map
argument_list|,
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2017
argument_list|,
literal|11
argument_list|,
literal|17
argument_list|,
literal|7
argument_list|,
literal|0
argument_list|)
operator|.
name|toInstant
argument_list|(
name|ZoneOffset
operator|.
name|of
argument_list|(
literal|"+01:00"
argument_list|)
argument_list|)
operator|.
name|toEpochMilli
argument_list|()
operator|+
name|TimeUnit
operator|.
name|HOURS
operator|.
name|toMillis
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|paths
init|=
name|Files
operator|.
name|list
argument_list|(
name|path
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|getFileName
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|toString
argument_list|)
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"file-2017-11-17-2.log.gz"
argument_list|,
literal|"file-2017-11-17.log.gz"
argument_list|,
literal|"file.log"
argument_list|)
argument_list|,
name|paths
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|getNewPort
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|ServerSocket
name|socket
init|=
operator|new
name|ServerSocket
argument_list|()
init|)
block|{
name|socket
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|socket
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|socket
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

