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
name|BufferedWriter
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
name|Flushable
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
name|OutputStream
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

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
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
name|StandardCopyOption
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
name|StandardOpenOption
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|Executor
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
name|ThreadFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|FileEventLogger
implements|implements
name|EventLogger
block|{
specifier|private
specifier|final
name|Charset
name|encoding
decl_stmt|;
specifier|private
specifier|final
name|String
name|policy
decl_stmt|;
specifier|private
specifier|final
name|int
name|files
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|compress
decl_stmt|;
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
specifier|private
specifier|final
name|EventLayout
name|layout
decl_stmt|;
specifier|private
name|boolean
name|daily
decl_stmt|;
specifier|private
name|long
name|maxSize
decl_stmt|;
specifier|private
name|long
name|size
decl_stmt|;
specifier|private
name|Path
name|path
decl_stmt|;
specifier|private
name|Writer
name|writer
decl_stmt|;
specifier|private
name|FastDateFormat
name|fastDateFormat
init|=
operator|new
name|FastDateFormat
argument_list|()
decl_stmt|;
specifier|public
name|FileEventLogger
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|encoding
parameter_list|,
name|String
name|policy
parameter_list|,
name|int
name|files
parameter_list|,
name|boolean
name|compress
parameter_list|,
name|ThreadFactory
name|factory
parameter_list|,
name|EventLayout
name|layout
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|path
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
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
name|policy
operator|=
name|policy
expr_stmt|;
name|this
operator|.
name|files
operator|=
name|files
expr_stmt|;
name|this
operator|.
name|compress
operator|=
name|compress
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|this
operator|.
name|path
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|pol
range|:
name|policy
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|split
argument_list|(
literal|"\\s+"
argument_list|)
control|)
block|{
if|if
condition|(
literal|"daily"
operator|.
name|equals
argument_list|(
name|pol
argument_list|)
condition|)
block|{
name|daily
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pol
operator|.
name|matches
argument_list|(
literal|"size\\([0-9]+(kb|mb|gb)?\\)"
argument_list|)
condition|)
block|{
name|String
name|str
init|=
name|pol
operator|.
name|substring
argument_list|(
literal|5
argument_list|,
name|pol
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|long
name|mult
decl_stmt|;
if|if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"kb"
argument_list|)
condition|)
block|{
name|mult
operator|=
literal|1024
expr_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"mb"
argument_list|)
condition|)
block|{
name|mult
operator|=
literal|1024
operator|*
literal|1024
expr_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
literal|"gb"
argument_list|)
condition|)
block|{
name|mult
operator|=
literal|1024
operator|*
literal|1024
operator|*
literal|1024
expr_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mult
operator|=
literal|1
expr_stmt|;
block|}
try|try
block|{
name|maxSize
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|str
argument_list|)
operator|*
name|mult
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|t
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|maxSize
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported policy: "
operator|+
name|pol
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported policy: "
operator|+
name|pol
argument_list|)
throw|;
block|}
block|}
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
name|long
name|timestamp
init|=
name|event
operator|.
name|timestamp
argument_list|()
decl_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|check
argument_list|(
name|timestamp
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
specifier|private
name|void
name|init
parameter_list|()
throws|throws
name|IOException
block|{
name|long
name|timestamp
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|size
operator|=
name|Files
operator|.
name|size
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|fastDateFormat
operator|.
name|sameDay
argument_list|(
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|path
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|trigger
argument_list|(
name|timestamp
argument_list|)
condition|)
block|{
name|Path
name|temp
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|path
operator|.
name|getParent
argument_list|()
argument_list|,
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|move
argument_list|(
name|path
argument_list|,
name|temp
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|rotate
argument_list|(
name|temp
argument_list|,
name|timestamp
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|fastDateFormat
operator|.
name|sameDay
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|Writer
argument_list|(
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|path
argument_list|,
name|encoding
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE
argument_list|,
name|StandardOpenOption
operator|.
name|APPEND
argument_list|)
argument_list|)
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
block|}
specifier|private
name|void
name|check
parameter_list|(
name|long
name|timestamp
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|trigger
argument_list|(
name|timestamp
argument_list|)
condition|)
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
if|if
condition|(
name|Files
operator|.
name|size
argument_list|(
name|path
argument_list|)
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Path
name|temp
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
name|path
operator|.
name|getParent
argument_list|()
argument_list|,
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"."
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|delete
argument_list|(
name|temp
argument_list|)
expr_stmt|;
name|Files
operator|.
name|move
argument_list|(
name|path
argument_list|,
name|temp
argument_list|,
name|StandardCopyOption
operator|.
name|ATOMIC_MOVE
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|rotate
argument_list|(
name|temp
argument_list|,
name|timestamp
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|Writer
argument_list|(
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|path
argument_list|,
name|encoding
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE
argument_list|,
name|StandardOpenOption
operator|.
name|APPEND
argument_list|)
argument_list|)
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|trigger
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
return|return
name|maxSize
operator|>
literal|0
operator|&&
name|size
operator|>
name|maxSize
operator|||
name|daily
operator|&&
operator|!
name|fastDateFormat
operator|.
name|sameDay
argument_list|(
name|timestamp
argument_list|)
return|;
block|}
specifier|private
name|void
name|rotate
parameter_list|(
name|Path
name|path
parameter_list|,
name|long
name|timestamp
parameter_list|)
block|{
try|try
block|{
comment|// Compute final name
name|String
index|[]
name|fix
init|=
name|getFileNameFix
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|paths
init|=
name|Files
operator|.
name|list
argument_list|(
name|path
operator|.
name|getParent
argument_list|()
argument_list|)
operator|.
name|filter
argument_list|(
name|p
lambda|->
operator|!
name|p
operator|.
name|equals
argument_list|(
name|this
operator|.
name|path
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|getFileName
argument_list|)
operator|.
name|map
argument_list|(
name|Path
operator|::
name|toString
argument_list|)
operator|.
name|filter
argument_list|(
name|p
lambda|->
name|p
operator|.
name|startsWith
argument_list|(
name|fix
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|p
lambda|->
operator|!
name|p
operator|.
name|endsWith
argument_list|(
literal|".tmp"
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|date
init|=
operator|new
name|FastDateFormat
argument_list|()
operator|.
name|getDate
argument_list|(
name|timestamp
argument_list|,
name|FastDateFormat
operator|.
name|YYYY_MM_DD
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sameDate
init|=
name|paths
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|p
lambda|->
name|p
operator|.
name|matches
argument_list|(
literal|"\\Q"
operator|+
name|fix
index|[
literal|0
index|]
operator|+
literal|"-"
operator|+
name|date
operator|+
literal|"\\E(-[0-9]+)?\\Q"
operator|+
name|fix
index|[
literal|1
index|]
operator|+
literal|"\\E"
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|fix
index|[
literal|0
index|]
operator|+
literal|"-"
operator|+
name|date
operator|+
name|fix
index|[
literal|1
index|]
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|sameDate
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|fix
index|[
literal|0
index|]
operator|+
literal|"-"
operator|+
name|date
operator|+
literal|"-"
operator|+
name|Integer
operator|.
name|toString
argument_list|(
operator|++
name|idx
argument_list|)
operator|+
name|fix
index|[
literal|1
index|]
expr_stmt|;
block|}
name|paths
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|Path
name|finalPath
init|=
name|path
operator|.
name|resolveSibling
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// Compress or move the file
if|if
condition|(
name|compress
condition|)
block|{
try|try
init|(
name|OutputStream
name|out
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|finalPath
argument_list|,
name|StandardOpenOption
operator|.
name|WRITE
argument_list|,
name|StandardOpenOption
operator|.
name|CREATE_NEW
argument_list|)
init|;                      GZIPOutputStream zip = new GZIPOutputStream(out)
block|)
block|{
name|Files
operator|.
name|copy
argument_list|(
name|path
argument_list|,
name|zip
argument_list|)
expr_stmt|;
block|}
name|Files
operator|.
name|delete
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Files
operator|.
name|move
argument_list|(
name|path
argument_list|,
name|finalPath
argument_list|)
expr_stmt|;
block|}
comment|// Check number of files
if|if
condition|(
name|files
operator|>
literal|0
operator|&&
name|paths
operator|.
name|size
argument_list|()
operator|>
name|files
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|paths
argument_list|)
expr_stmt|;
name|paths
operator|.
name|subList
argument_list|(
name|paths
operator|.
name|size
argument_list|()
operator|-
name|files
argument_list|,
name|paths
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|p
range|:
name|paths
control|)
block|{
name|Files
operator|.
name|delete
argument_list|(
name|path
operator|.
name|resolveSibling
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
end_class

begin_function
specifier|private
name|String
index|[]
name|getFileNameFix
parameter_list|()
block|{
name|String
name|str
init|=
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|sfx
init|=
name|compress
condition|?
literal|".gz"
else|:
literal|""
decl_stmt|;
name|int
name|idx
init|=
name|str
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
block|,
name|str
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
operator|+
name|sfx
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[]
block|{
name|str
block|,
name|sfx
block|}
return|;
block|}
block|}
end_function

begin_function
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
end_function

begin_function
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
end_function

begin_class
class|class
name|Writer
extends|extends
name|java
operator|.
name|io
operator|.
name|Writer
implements|implements
name|Appendable
implements|,
name|Closeable
implements|,
name|Flushable
block|{
specifier|private
specifier|final
name|BufferedWriter
name|writer
decl_stmt|;
specifier|public
name|Writer
parameter_list|(
name|BufferedWriter
name|writer
parameter_list|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
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
name|writer
operator|.
name|flush
argument_list|()
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
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
literal|1
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
name|len
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
name|len
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|s
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
name|cbuf
operator|.
name|length
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|cbuf
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
name|str
operator|.
name|length
argument_list|()
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|java
operator|.
name|io
operator|.
name|Writer
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
name|csq
operator|.
name|length
argument_list|()
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|csq
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|java
operator|.
name|io
operator|.
name|Writer
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
name|end
operator|-
name|start
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|csq
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|java
operator|.
name|io
operator|.
name|Writer
name|append
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|size
operator|+=
literal|1
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

unit|}
end_unit
