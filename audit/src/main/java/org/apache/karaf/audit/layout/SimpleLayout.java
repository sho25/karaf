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
name|layout
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
name|util
operator|.
name|Buffer
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
name|FastDateFormat
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

begin_class
specifier|public
class|class
name|SimpleLayout
extends|extends
name|AbstractLayout
block|{
specifier|protected
name|String
name|hdr
decl_stmt|;
specifier|protected
name|FastDateFormat
name|fastDateFormat
init|=
operator|new
name|FastDateFormat
argument_list|()
decl_stmt|;
specifier|public
name|SimpleLayout
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|Buffer
argument_list|(
name|Buffer
operator|.
name|Format
operator|.
name|Json
argument_list|,
literal|4096
argument_list|)
argument_list|)
expr_stmt|;
name|hdr
operator|=
literal|" "
operator|+
name|hostName
operator|+
literal|" "
operator|+
name|appName
operator|+
literal|" "
operator|+
name|procId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|header
parameter_list|(
name|Event
name|event
parameter_list|)
throws|throws
name|IOException
block|{
name|datetime
argument_list|(
name|event
operator|.
name|timestamp
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|footer
parameter_list|(
name|Event
name|event
parameter_list|)
throws|throws
name|IOException
block|{     }
annotation|@
name|Override
specifier|protected
name|void
name|append
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|val
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|key
condition|)
block|{
case|case
literal|"subject"
case|:
case|case
literal|"type"
case|:
case|case
literal|"subtype"
case|:
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|format
argument_list|(
name|val
argument_list|)
expr_stmt|;
break|break;
default|default:
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
operator|.
name|format
argument_list|(
name|val
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|protected
name|void
name|datetime
parameter_list|(
name|long
name|millis
parameter_list|)
throws|throws
name|IOException
block|{
name|buffer
operator|.
name|append
argument_list|(
name|fastDateFormat
operator|.
name|getDate
argument_list|(
name|millis
argument_list|,
name|FastDateFormat
operator|.
name|YYYY_MM_DD
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'T'
argument_list|)
expr_stmt|;
name|fastDateFormat
operator|.
name|writeTime
argument_list|(
name|millis
argument_list|,
literal|true
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|fastDateFormat
operator|.
name|getDate
argument_list|(
name|millis
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

