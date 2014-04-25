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
name|FilterInputStream
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
name|InterruptedIOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Repository
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
name|model
operator|.
name|Features
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
name|model
operator|.
name|JaxbUtil
import|;
end_import

begin_comment
comment|/**  * The repository implementation.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryImpl
implements|implements
name|Repository
block|{
specifier|private
specifier|final
name|URI
name|uri
decl_stmt|;
specifier|private
name|Features
name|features
decl_stmt|;
specifier|public
name|RepositoryImpl
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|URI
name|getURI
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
throws|throws
name|IOException
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|features
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|URI
index|[]
name|getRepositories
parameter_list|()
throws|throws
name|IOException
block|{
name|load
argument_list|()
expr_stmt|;
name|URI
index|[]
name|result
init|=
operator|new
name|URI
index|[
name|features
operator|.
name|getRepository
argument_list|()
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|features
operator|.
name|getRepository
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|uri
init|=
name|features
operator|.
name|getRepository
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|uri
operator|=
name|uri
operator|.
name|trim
argument_list|()
expr_stmt|;
name|result
index|[
name|i
index|]
operator|=
name|URI
operator|.
name|create
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
index|[]
name|getFeatures
parameter_list|()
throws|throws
name|IOException
block|{
name|load
argument_list|()
expr_stmt|;
return|return
name|features
operator|.
name|getFeature
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|Feature
index|[
name|features
operator|.
name|getFeature
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|void
name|load
parameter_list|()
throws|throws
name|IOException
block|{
name|load
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|load
parameter_list|(
name|boolean
name|validate
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
try|try
init|(
name|InputStream
name|inputStream
init|=
operator|new
name|InterruptibleInputStream
argument_list|(
name|uri
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
init|)
block|{
name|features
operator|=
name|JaxbUtil
operator|.
name|unmarshal
argument_list|(
name|uri
operator|.
name|toASCIIString
argument_list|()
argument_list|,
name|inputStream
argument_list|,
name|validate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" : "
operator|+
name|uri
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|static
class|class
name|InterruptibleInputStream
extends|extends
name|FilterInputStream
block|{
name|InterruptibleInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
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
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|InterruptedIOException
argument_list|()
throw|;
block|}
return|return
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

