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
name|features
operator|.
name|internal
operator|.
name|service
package|;
end_package

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
operator|.
name|deployment
operator|.
name|Downloader
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
name|features
operator|.
name|internal
operator|.
name|deployment
operator|.
name|StreamProvider
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
name|features
operator|.
name|internal
operator|.
name|util
operator|.
name|MultiException
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleDownloader
implements|implements
name|Downloader
block|{
specifier|private
specifier|final
name|MultiException
name|exception
init|=
operator|new
name|MultiException
argument_list|(
literal|"Error"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|await
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|MultiException
block|{
name|exception
operator|.
name|throwIfExceptions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|download
parameter_list|(
specifier|final
name|String
name|location
parameter_list|,
specifier|final
name|DownloadCallback
name|downloadCallback
parameter_list|)
throws|throws
name|MalformedURLException
block|{
specifier|final
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|location
argument_list|)
decl_stmt|;
try|try
block|{
name|downloadCallback
operator|.
name|downloaded
argument_list|(
operator|new
name|StreamProvider
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|open
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exception
operator|.
name|addException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

