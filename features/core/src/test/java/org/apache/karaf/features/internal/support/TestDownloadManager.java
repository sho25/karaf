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
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|File
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
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
name|download
operator|.
name|DownloadCallback
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
name|download
operator|.
name|DownloadManager
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
name|download
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
name|download
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
name|TestDownloadManager
implements|implements
name|DownloadManager
implements|,
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
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|StreamProvider
argument_list|>
name|providers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|loader
decl_stmt|;
specifier|private
specifier|final
name|String
name|dir
decl_stmt|;
specifier|public
name|TestDownloadManager
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|loader
parameter_list|,
name|String
name|dir
parameter_list|)
block|{
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
name|this
operator|.
name|dir
operator|=
name|dir
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Downloader
name|createDownloader
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|StreamProvider
argument_list|>
name|getProviders
parameter_list|()
block|{
return|return
name|providers
return|;
block|}
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
if|if
condition|(
operator|!
name|providers
operator|.
name|containsKey
argument_list|(
name|location
argument_list|)
condition|)
block|{
name|providers
operator|.
name|putIfAbsent
argument_list|(
name|location
argument_list|,
name|createProvider
argument_list|(
name|location
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|downloadCallback
operator|!=
literal|null
condition|)
block|{
name|downloadCallback
operator|.
name|downloaded
argument_list|(
name|providers
operator|.
name|get
argument_list|(
name|location
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exception
operator|.
name|addSuppressed
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|StreamProvider
name|createProvider
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
operator|new
name|TestProvider
argument_list|(
name|location
argument_list|)
return|;
block|}
class|class
name|TestProvider
implements|implements
name|StreamProvider
block|{
specifier|private
specifier|final
name|String
name|location
decl_stmt|;
specifier|private
specifier|final
name|IOException
name|exception
decl_stmt|;
specifier|private
specifier|final
name|byte
index|[]
name|data
decl_stmt|;
name|TestProvider
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|byte
index|[]
name|data
init|=
literal|null
decl_stmt|;
name|IOException
name|exception
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|loc
init|=
name|dir
operator|+
literal|"/"
operator|+
name|location
operator|+
literal|".mf"
decl_stmt|;
name|InputStream
name|is
init|=
name|loader
operator|.
name|getResourceAsStream
argument_list|(
name|loc
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Could not find resource: "
operator|+
name|loc
argument_list|)
throw|;
block|}
name|Manifest
name|man
init|=
operator|new
name|Manifest
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|JarOutputStream
name|jos
init|=
operator|new
name|JarOutputStream
argument_list|(
name|baos
argument_list|,
name|man
argument_list|)
decl_stmt|;
name|jos
operator|.
name|close
argument_list|()
expr_stmt|;
name|data
operator|=
name|baos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|exception
operator|=
name|e
expr_stmt|;
block|}
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|location
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|open
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
throw|throw
name|exception
throw|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|File
name|getFile
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

