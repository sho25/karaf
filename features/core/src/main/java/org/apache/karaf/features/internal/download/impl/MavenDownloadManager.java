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
name|download
operator|.
name|impl
package|;
end_package

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
name|ScheduledExecutorService
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

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|url
operator|.
name|mvn
operator|.
name|MavenResolver
import|;
end_import

begin_class
specifier|public
class|class
name|MavenDownloadManager
implements|implements
name|DownloadManager
block|{
specifier|private
specifier|final
name|MavenResolver
name|mavenResolver
decl_stmt|;
specifier|private
specifier|final
name|ScheduledExecutorService
name|executorService
decl_stmt|;
specifier|private
name|File
name|tmpPath
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractDownloadTask
argument_list|>
name|downloaded
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractDownloadTask
argument_list|>
name|downloading
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Object
name|lock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
specifier|volatile
name|int
name|allPending
init|=
literal|0
decl_stmt|;
specifier|public
name|MavenDownloadManager
parameter_list|(
name|MavenResolver
name|mavenResolver
parameter_list|,
name|ScheduledExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|mavenResolver
operator|=
name|mavenResolver
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|String
name|karafRoot
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.home"
argument_list|,
literal|"karaf"
argument_list|)
decl_stmt|;
name|String
name|karafData
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|,
name|karafRoot
operator|+
literal|"/data"
argument_list|)
decl_stmt|;
name|this
operator|.
name|tmpPath
operator|=
operator|new
name|File
argument_list|(
name|karafData
argument_list|,
literal|"tmp"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getPending
parameter_list|()
block|{
return|return
name|allPending
return|;
block|}
annotation|@
name|Override
specifier|public
name|Downloader
name|createDownloader
parameter_list|()
block|{
return|return
operator|new
name|MavenDownloader
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
operator|(
name|Map
operator|)
name|Collections
operator|.
name|synchronizedMap
argument_list|(
name|downloaded
argument_list|)
return|;
block|}
class|class
name|MavenDownloader
implements|implements
name|Downloader
block|{
specifier|private
specifier|volatile
name|int
name|pending
init|=
literal|0
decl_stmt|;
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
specifier|public
name|int
name|pending
parameter_list|()
block|{
return|return
name|pending
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
synchronized|synchronized
init|(
name|lock
init|)
block|{
while|while
condition|(
name|pending
operator|!=
literal|0
condition|)
block|{
name|lock
operator|.
name|wait
argument_list|()
expr_stmt|;
block|}
block|}
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
name|AbstractDownloadTask
name|task
decl_stmt|;
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|task
operator|=
name|downloaded
operator|.
name|get
argument_list|(
name|location
argument_list|)
expr_stmt|;
if|if
condition|(
name|task
operator|==
literal|null
condition|)
block|{
name|task
operator|=
name|downloading
operator|.
name|get
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|task
operator|==
literal|null
condition|)
block|{
name|task
operator|=
name|createDownloadTask
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|AbstractDownloadTask
name|prev
init|=
name|downloaded
operator|.
name|get
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|prev
operator|==
literal|null
condition|)
block|{
name|prev
operator|=
name|downloading
operator|.
name|get
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prev
operator|==
literal|null
condition|)
block|{
name|downloading
operator|.
name|put
argument_list|(
name|location
argument_list|,
name|task
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|execute
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|task
operator|=
name|prev
expr_stmt|;
block|}
name|pending
operator|++
expr_stmt|;
name|allPending
operator|++
expr_stmt|;
block|}
specifier|final
name|AbstractDownloadTask
name|downloadTask
init|=
name|task
decl_stmt|;
name|task
operator|.
name|addListener
argument_list|(
operator|new
name|FutureListener
argument_list|<
name|AbstractDownloadTask
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|operationComplete
parameter_list|(
name|AbstractDownloadTask
name|future
parameter_list|)
block|{
try|try
block|{
comment|// Call the callback
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
name|downloadTask
argument_list|)
expr_stmt|;
block|}
comment|// Make sure we log any download error if the callback suppressed it
name|downloadTask
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
finally|finally
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|downloading
operator|.
name|remove
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|downloaded
operator|.
name|put
argument_list|(
name|location
argument_list|,
name|downloadTask
argument_list|)
expr_stmt|;
operator|--
name|allPending
expr_stmt|;
if|if
condition|(
operator|--
name|pending
operator|==
literal|0
condition|)
block|{
name|lock
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|AbstractDownloadTask
name|createDownloadTask
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
specifier|final
name|String
name|mvnUrl
init|=
name|DownloadManagerHelper
operator|.
name|stripUrl
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|mvnUrl
operator|.
name|startsWith
argument_list|(
literal|"mvn:"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|mvnUrl
operator|.
name|equals
argument_list|(
name|url
argument_list|)
condition|)
block|{
return|return
operator|new
name|ChainedDownloadTask
argument_list|(
name|executorService
argument_list|,
name|url
argument_list|,
name|mvnUrl
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|MavenDownloadTask
argument_list|(
name|executorService
argument_list|,
name|mavenResolver
argument_list|,
name|mvnUrl
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|SimpleDownloadTask
argument_list|(
name|executorService
argument_list|,
name|url
argument_list|,
name|tmpPath
argument_list|)
return|;
block|}
block|}
class|class
name|ChainedDownloadTask
extends|extends
name|AbstractDownloadTask
block|{
specifier|private
name|String
name|innerUrl
decl_stmt|;
specifier|public
name|ChainedDownloadTask
parameter_list|(
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|innerUrl
parameter_list|)
block|{
name|super
argument_list|(
name|executorService
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|this
operator|.
name|innerUrl
operator|=
name|innerUrl
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|MavenDownloader
operator|.
name|this
operator|.
name|download
argument_list|(
name|innerUrl
argument_list|,
operator|new
name|DownloadCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|downloaded
parameter_list|(
name|StreamProvider
name|provider
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|AbstractDownloadTask
name|future
init|=
operator|(
name|AbstractDownloadTask
operator|)
name|provider
decl_stmt|;
name|String
name|file
init|=
name|future
operator|.
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|String
name|real
init|=
name|url
operator|.
name|replace
argument_list|(
name|innerUrl
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|MavenDownloader
operator|.
name|this
operator|.
name|download
argument_list|(
name|real
argument_list|,
operator|new
name|DownloadCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|downloaded
parameter_list|(
name|StreamProvider
name|provider
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|setFile
argument_list|(
name|provider
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|setException
argument_list|(
operator|new
name|IOException
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|setException
argument_list|(
operator|new
name|IOException
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|setException
argument_list|(
operator|new
name|IOException
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

