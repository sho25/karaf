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
name|profile
operator|.
name|assembly
package|;
end_package

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
name|impl
operator|.
name|AbstractDownloadTask
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
name|impl
operator|.
name|MavenDownloadManager
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
name|profile
operator|.
name|Profile
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
name|CustomDownloadManager
extends|extends
name|MavenDownloadManager
block|{
specifier|private
specifier|final
name|Profile
name|profile
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|translatedUrls
decl_stmt|;
specifier|public
name|CustomDownloadManager
parameter_list|(
name|MavenResolver
name|resolver
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|this
argument_list|(
name|resolver
argument_list|,
name|executor
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CustomDownloadManager
parameter_list|(
name|MavenResolver
name|resolver
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Profile
name|profile
parameter_list|)
block|{
name|this
argument_list|(
name|resolver
argument_list|,
name|executor
argument_list|,
name|profile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CustomDownloadManager
parameter_list|(
name|MavenResolver
name|resolver
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Profile
name|profile
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|translatedUrls
parameter_list|)
block|{
name|super
argument_list|(
name|resolver
argument_list|,
name|executor
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|profile
operator|=
name|profile
expr_stmt|;
name|this
operator|.
name|translatedUrls
operator|=
name|translatedUrls
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|AbstractDownloadTask
name|createCustomDownloadTask
parameter_list|(
name|String
name|url
parameter_list|)
block|{
return|return
operator|new
name|CustomSimpleDownloadTask
argument_list|(
name|executorService
argument_list|,
name|profile
argument_list|,
name|url
argument_list|)
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
name|CustomMavenDownloader
argument_list|()
return|;
block|}
class|class
name|CustomMavenDownloader
extends|extends
name|MavenDownloader
block|{
specifier|protected
name|AbstractDownloadTask
name|createDownloadTask
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|translatedUrls
operator|!=
literal|null
operator|&&
name|translatedUrls
operator|.
name|containsKey
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|url
operator|=
name|translatedUrls
operator|.
name|get
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|createDownloadTask
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

