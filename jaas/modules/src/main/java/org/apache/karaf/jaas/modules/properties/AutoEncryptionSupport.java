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
name|jaas
operator|.
name|modules
operator|.
name|properties
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardWatchEventKinds
operator|.
name|ENTRY_MODIFY
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|nio
operator|.
name|file
operator|.
name|FileSystems
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
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|WatchEvent
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
name|WatchKey
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
name|WatchService
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
name|ExecutorService
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
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
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
name|jaas
operator|.
name|modules
operator|.
name|Encryption
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
name|jaas
operator|.
name|modules
operator|.
name|encryption
operator|.
name|EncryptionSupport
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
name|AutoEncryptionSupport
implements|implements
name|Runnable
implements|,
name|Closeable
block|{
specifier|private
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AutoEncryptionSupport
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|running
decl_stmt|;
specifier|private
specifier|volatile
name|EncryptionSupport
name|encryptionSupport
decl_stmt|;
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
specifier|public
name|AutoEncryptionSupport
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|running
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|encryptionSupport
operator|=
operator|new
name|EncryptionSupport
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|executor
operator|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|running
operator|=
literal|false
expr_stmt|;
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|WatchService
name|watchService
init|=
literal|null
decl_stmt|;
try|try
block|{
name|watchService
operator|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|newWatchService
argument_list|()
expr_stmt|;
name|Path
name|dir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"karaf.etc"
argument_list|)
argument_list|)
decl_stmt|;
name|dir
operator|.
name|register
argument_list|(
name|watchService
argument_list|,
name|ENTRY_MODIFY
argument_list|)
expr_stmt|;
name|Path
name|file
init|=
name|dir
operator|.
name|resolve
argument_list|(
literal|"users.properties"
argument_list|)
decl_stmt|;
name|encryptedPassword
argument_list|(
operator|new
name|Properties
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
name|running
condition|)
block|{
try|try
block|{
name|WatchKey
name|key
init|=
name|watchService
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|WatchEvent
argument_list|<
name|?
argument_list|>
name|event
range|:
name|key
operator|.
name|pollEvents
argument_list|()
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|WatchEvent
argument_list|<
name|Path
argument_list|>
name|ev
init|=
operator|(
name|WatchEvent
argument_list|<
name|Path
argument_list|>
operator|)
name|event
decl_stmt|;
comment|// Context for directory entry event is the file name of entry
name|Path
name|name
init|=
name|dir
operator|.
name|resolve
argument_list|(
name|ev
operator|.
name|context
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|encryptedPassword
argument_list|(
operator|new
name|Properties
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|key
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Ignore as this happens on shutdown
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|StreamUtils
operator|.
name|close
argument_list|(
name|watchService
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|encryptedPassword
parameter_list|(
name|Properties
name|users
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|userName
range|:
name|users
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|user
init|=
name|userName
decl_stmt|;
name|String
name|userInfos
init|=
name|users
operator|.
name|get
argument_list|(
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|.
name|startsWith
argument_list|(
name|PropertiesBackingEngine
operator|.
name|GROUP_PREFIX
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// the password is in the first position
name|String
index|[]
name|infos
init|=
name|userInfos
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|String
name|storedPassword
init|=
name|infos
index|[
literal|0
index|]
decl_stmt|;
comment|// check if the stored password is flagged as encrypted
name|String
name|encryptedPassword
init|=
name|getEncryptedPassword
argument_list|(
name|storedPassword
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|storedPassword
operator|.
name|equals
argument_list|(
name|encryptedPassword
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"The password isn't flagged as encrypted, encrypt it."
argument_list|)
expr_stmt|;
name|userInfos
operator|=
name|encryptedPassword
operator|+
literal|","
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|infos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
operator|(
name|infos
operator|.
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|userInfos
operator|=
name|userInfos
operator|+
name|infos
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|userInfos
operator|=
name|userInfos
operator|+
name|infos
index|[
name|i
index|]
operator|+
literal|","
expr_stmt|;
block|}
block|}
if|if
condition|(
name|user
operator|.
name|contains
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
name|users
operator|.
name|remove
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|user
operator|=
name|user
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"\\\\"
argument_list|)
expr_stmt|;
block|}
name|users
operator|.
name|put
argument_list|(
name|user
argument_list|,
name|userInfos
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|changed
condition|)
block|{
name|users
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|getEncryptedPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|Encryption
name|encryption
init|=
name|encryptionSupport
operator|.
name|getEncryption
argument_list|()
decl_stmt|;
name|String
name|encryptionPrefix
init|=
name|encryptionSupport
operator|.
name|getEncryptionPrefix
argument_list|()
decl_stmt|;
name|String
name|encryptionSuffix
init|=
name|encryptionSupport
operator|.
name|getEncryptionSuffix
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryption
operator|==
literal|null
condition|)
block|{
return|return
name|password
return|;
block|}
else|else
block|{
name|boolean
name|prefix
init|=
name|encryptionPrefix
operator|==
literal|null
operator|||
name|password
operator|.
name|startsWith
argument_list|(
name|encryptionPrefix
argument_list|)
decl_stmt|;
name|boolean
name|suffix
init|=
name|encryptionSuffix
operator|==
literal|null
operator|||
name|password
operator|.
name|endsWith
argument_list|(
name|encryptionSuffix
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|&&
name|suffix
condition|)
block|{
return|return
name|password
return|;
block|}
else|else
block|{
name|String
name|p
init|=
name|encryption
operator|.
name|encryptPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptionPrefix
operator|!=
literal|null
condition|)
block|{
name|p
operator|=
name|encryptionPrefix
operator|+
name|p
expr_stmt|;
block|}
if|if
condition|(
name|encryptionSuffix
operator|!=
literal|null
condition|)
block|{
name|p
operator|=
name|p
operator|+
name|encryptionSuffix
expr_stmt|;
block|}
return|return
name|p
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

