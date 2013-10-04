begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|main
operator|.
name|lock
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
name|io
operator|.
name|RandomAccessFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileLock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|main
operator|.
name|util
operator|.
name|BootstrapLogManager
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleFileLock
implements|implements
name|Lock
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|SimpleFileLock
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_LOCK_DIR
init|=
literal|"karaf.lock.dir"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROP_KARAF_BASE
init|=
literal|"karaf.base"
decl_stmt|;
specifier|private
name|RandomAccessFile
name|lockFile
decl_stmt|;
specifier|private
name|File
name|lockPath
decl_stmt|;
specifier|private
name|FileLock
name|lock
decl_stmt|;
specifier|public
name|SimpleFileLock
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|addHandler
argument_list|(
name|BootstrapLogManager
operator|.
name|getDefaultHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|lock
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_DIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|lock
operator|!=
literal|null
condition|)
block|{
name|File
name|karafLock
init|=
name|getKarafLock
argument_list|(
operator|new
name|File
argument_list|(
name|lock
argument_list|)
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
name|PROPERTY_LOCK_DIR
argument_list|,
name|karafLock
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|PROPERTY_LOCK_DIR
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|PROP_KARAF_BASE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|File
name|base
init|=
operator|new
name|File
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
name|PROPERTY_LOCK_DIR
argument_list|)
argument_list|)
decl_stmt|;
name|lockPath
operator|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"lock"
argument_list|)
expr_stmt|;
name|lockFile
operator|=
operator|new
name|RandomAccessFile
argument_list|(
name|lockPath
argument_list|,
literal|"rw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Karaf can't startup, make sure the log file can be accessed and written by the user starting Karaf : "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not create file lock"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|lock
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Trying to lock "
operator|+
name|lockPath
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|lock
operator|==
literal|null
condition|)
block|{
name|lock
operator|=
name|lockFile
operator|.
name|getChannel
argument_list|()
operator|.
name|tryLock
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|lock
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Lock acquired"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Lock failed"
argument_list|)
expr_stmt|;
block|}
return|return
name|lock
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|release
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|lock
operator|!=
literal|null
operator|&&
name|lock
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Releasing lock "
operator|+
name|lockPath
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
name|lock
operator|.
name|channel
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|lock
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAlive
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|lock
operator|!=
literal|null
operator|&&
name|lock
operator|.
name|isValid
argument_list|()
operator|&&
name|lockPath
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|File
name|getKarafLock
parameter_list|(
name|File
name|lock
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
name|File
name|rc
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
name|lock
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|rc
operator|=
name|validateDirectoryExists
argument_list|(
name|path
argument_list|,
literal|"Invalid "
operator|+
name|PROPERTY_LOCK_DIR
operator|+
literal|" system property"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rc
operator|==
literal|null
condition|)
block|{
name|path
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROP_KARAF_BASE
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|rc
operator|=
name|validateDirectoryExists
argument_list|(
name|path
argument_list|,
literal|"Invalid "
operator|+
name|PROP_KARAF_BASE
operator|+
literal|" property"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|rc
operator|==
literal|null
condition|)
block|{
name|rc
operator|=
name|lock
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
specifier|private
specifier|static
name|File
name|validateDirectoryExists
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|errPrefix
parameter_list|)
block|{
name|File
name|rc
decl_stmt|;
try|try
block|{
name|rc
operator|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|getCanonicalFile
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|errPrefix
operator|+
literal|" '"
operator|+
name|path
operator|+
literal|"' : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|rc
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|errPrefix
operator|+
literal|" '"
operator|+
name|path
operator|+
literal|"' : does not exist"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|rc
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|errPrefix
operator|+
literal|" '"
operator|+
name|path
operator|+
literal|"' : is not a directory"
argument_list|)
throw|;
block|}
return|return
name|rc
return|;
block|}
block|}
end_class

end_unit

