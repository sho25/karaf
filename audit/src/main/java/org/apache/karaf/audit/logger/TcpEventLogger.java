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
name|IOException
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
name|Charset
import|;
end_import

begin_class
specifier|public
class|class
name|TcpEventLogger
implements|implements
name|EventLogger
block|{
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
specifier|private
specifier|final
name|Charset
name|encoding
decl_stmt|;
specifier|private
specifier|final
name|EventLayout
name|layout
decl_stmt|;
specifier|private
name|BufferedWriter
name|writer
decl_stmt|;
specifier|public
name|TcpEventLogger
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|encoding
parameter_list|,
name|EventLayout
name|layout
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|encoding
operator|=
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|Event
name|event
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
name|Socket
name|socket
init|=
operator|new
name|Socket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|this
operator|.
name|writer
operator|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|socket
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

