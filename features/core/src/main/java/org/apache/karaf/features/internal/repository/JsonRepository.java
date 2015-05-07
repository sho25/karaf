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
name|repository
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
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|locks
operator|.
name|ReadWriteLock
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
name|locks
operator|.
name|ReentrantReadWriteLock
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
name|resolver
operator|.
name|ResourceBuilder
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
name|JsonReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Capability
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Requirement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
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

begin_comment
comment|/**  * Repository using a JSON representation of resource metadata.  * The json should be a map: the key is the resource uri and the  * value is a map of resource headers.  * The content of the URL can be gzipped.  */
end_comment

begin_class
specifier|public
class|class
name|JsonRepository
extends|extends
name|BaseRepository
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JsonRepository
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|ignoreFailures
decl_stmt|;
specifier|private
specifier|final
name|UrlLoader
name|loader
decl_stmt|;
specifier|private
specifier|final
name|ReadWriteLock
name|lock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
specifier|public
name|JsonRepository
parameter_list|(
name|String
name|url
parameter_list|,
name|long
name|expiration
parameter_list|,
name|boolean
name|ignoreFailures
parameter_list|)
block|{
name|loader
operator|=
operator|new
name|UrlLoader
argument_list|(
name|url
argument_list|,
name|expiration
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|boolean
name|doRead
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|JsonRepository
operator|.
name|this
operator|.
name|doRead
argument_list|(
name|is
argument_list|)
return|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|ignoreFailures
operator|=
name|ignoreFailures
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Resource
argument_list|>
name|getResources
parameter_list|()
block|{
name|checkAndLoadCache
argument_list|()
expr_stmt|;
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|getResources
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Requirement
argument_list|,
name|Collection
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|findProviders
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Requirement
argument_list|>
name|requirements
parameter_list|)
block|{
name|checkAndLoadCache
argument_list|()
expr_stmt|;
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|findProviders
argument_list|(
name|requirements
argument_list|)
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkAndLoadCache
parameter_list|()
block|{
try|try
block|{
name|loader
operator|.
name|checkAndLoadCache
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|ignoreFailures
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Ignoring failure: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|doRead
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|metadatas
init|=
name|verify
argument_list|(
name|JsonReader
operator|.
name|read
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|resources
operator|.
name|clear
argument_list|()
expr_stmt|;
name|capSets
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|metadata
range|:
name|metadatas
operator|.
name|entrySet
argument_list|()
control|)
block|{
try|try
block|{
name|Resource
name|resource
init|=
name|ResourceBuilder
operator|.
name|build
argument_list|(
name|metadata
operator|.
name|getKey
argument_list|()
argument_list|,
name|metadata
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|addResource
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Unable to build resource for "
operator|+
name|metadata
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|verify
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|obj
init|=
name|Map
operator|.
name|class
operator|.
name|cast
argument_list|(
name|value
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|obj
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
operator|.
name|class
operator|.
name|cast
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|child
init|=
name|Map
operator|.
name|class
operator|.
name|cast
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|ce
range|:
name|child
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
operator|.
name|class
operator|.
name|cast
argument_list|(
name|ce
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|String
operator|.
name|class
operator|.
name|cast
argument_list|(
name|ce
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
operator|)
name|obj
return|;
block|}
block|}
end_class

end_unit

