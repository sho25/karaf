begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|examples
operator|.
name|rest
operator|.
name|client
operator|.
name|http
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Action
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Argument
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Option
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
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
annotation|@
name|Service
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"booking"
argument_list|,
name|name
operator|=
literal|"add"
argument_list|,
name|description
operator|=
literal|"Add booking"
argument_list|)
specifier|public
class|class
name|AddBookingCommand
implements|implements
name|Action
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"id"
argument_list|,
name|description
operator|=
literal|"Booking ID"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|long
name|id
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"customer"
argument_list|,
name|description
operator|=
literal|"Customer name"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|customer
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|2
argument_list|,
name|name
operator|=
literal|"flight"
argument_list|,
name|description
operator|=
literal|"Flight number"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|flight
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--url"
argument_list|,
name|description
operator|=
literal|"Location of the REST service"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
name|String
name|restLocation
init|=
literal|"http://localhost:8181/cxf/booking/"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|restLocation
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setRequestMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|json
init|=
literal|"{"
operator|+
literal|"\"id\": "
operator|+
name|id
operator|+
literal|","
operator|+
literal|"\"flight\": \""
operator|+
name|flight
operator|+
literal|"\","
operator|+
literal|"\"customer\": \""
operator|+
name|customer
operator|+
literal|"\""
operator|+
literal|"}"
decl_stmt|;
name|OutputStreamWriter
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|connection
operator|.
name|getOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|connection
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

