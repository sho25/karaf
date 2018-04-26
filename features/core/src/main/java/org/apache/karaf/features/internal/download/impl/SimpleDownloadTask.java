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
name|FileOutputStream
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
name|io
operator|.
name|OutputStream
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
name|util
operator|.
name|StreamUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleDownloadTask
extends|extends
name|AbstractRetryableDownloadTask
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BLUEPRINT_PREFIX
init|=
literal|"blueprint:"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SPRING_PREFIX
init|=
literal|"spring:"
decl_stmt|;
comment|/**      * Logger.      */
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractDownloadTask
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|File
name|basePath
decl_stmt|;
specifier|public
name|SimpleDownloadTask
parameter_list|(
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|String
name|url
parameter_list|,
name|File
name|basePath
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
name|basePath
operator|=
name|basePath
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|File
name|download
parameter_list|(
name|Exception
name|previousExceptionNotUsed
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Downloading ["
operator|+
name|url
operator|+
literal|"]"
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
name|BLUEPRINT_PREFIX
argument_list|)
operator|||
name|url
operator|.
name|startsWith
argument_list|(
name|SPRING_PREFIX
argument_list|)
condition|)
block|{
return|return
name|downloadBlueprintOrSpring
argument_list|()
return|;
block|}
try|try
block|{
name|basePath
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|basePath
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to create directory "
operator|+
name|basePath
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|URL
name|urlObj
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|basePath
argument_list|,
name|getFileName
argument_list|(
name|urlObj
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|file
return|;
block|}
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|)
argument_list|,
literal|"tmp"
argument_list|)
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to create directory "
operator|+
name|dir
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|File
name|tmpFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"download-"
argument_list|,
literal|null
argument_list|,
name|dir
argument_list|)
decl_stmt|;
name|urlObj
operator|=
operator|new
name|URL
argument_list|(
name|DownloadManagerHelper
operator|.
name|stripStartLevel
argument_list|(
name|urlObj
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|urlObj
operator|.
name|openStream
argument_list|()
init|;
name|OutputStream
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|tmpFile
argument_list|)
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|file
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to delete file: "
operator|+
name|file
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
comment|// check: this will move the file to CHILD_HOME root directory...
if|if
condition|(
operator|!
name|tmpFile
operator|.
name|renameTo
argument_list|(
name|file
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unable to rename file "
operator|+
name|tmpFile
operator|.
name|toString
argument_list|()
operator|+
literal|" to "
operator|+
name|file
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|file
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not download ["
operator|+
name|this
operator|.
name|url
operator|+
literal|"]"
argument_list|,
name|ignore
argument_list|)
throw|;
block|}
block|}
comment|// we only want the filename itself, not the whole path
specifier|private
name|String
name|getFileName
parameter_list|(
name|URL
name|urlObj
parameter_list|)
block|{
name|String
name|url
init|=
name|urlObj
operator|.
name|getFile
argument_list|()
decl_stmt|;
comment|// ENTESB-1394: we do not want all these decorators from wrap: protocol
comment|// or any inlined maven repos
name|url
operator|=
name|DownloadManagerHelper
operator|.
name|stripUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|url
operator|=
name|DownloadManagerHelper
operator|.
name|removeInlinedMavenRepositoryUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|int
name|unixPos
init|=
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|int
name|windowsPos
init|=
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
decl_stmt|;
name|url
operator|=
name|url
operator|.
name|substring
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|unixPos
argument_list|,
name|windowsPos
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|url
operator|=
name|Integer
operator|.
name|toHexString
argument_list|(
name|urlObj
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|+
literal|"-"
operator|+
name|url
expr_stmt|;
return|return
name|url
return|;
block|}
specifier|protected
name|File
name|downloadBlueprintOrSpring
parameter_list|()
throws|throws
name|Exception
block|{
comment|// when downloading an embedded blueprint or spring xml file, then it must be as a temporary file
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.data"
argument_list|)
argument_list|,
literal|"tmp"
argument_list|)
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|tmpFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"download-"
argument_list|,
literal|null
argument_list|,
name|dir
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
operator|.
name|openStream
argument_list|()
init|;
name|OutputStream
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|tmpFile
argument_list|)
init|)
block|{
name|StreamUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
return|return
name|tmpFile
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Retry
name|isRetryable
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO: check http errors, etc.
return|return
name|super
operator|.
name|isRetryable
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
end_class

end_unit

