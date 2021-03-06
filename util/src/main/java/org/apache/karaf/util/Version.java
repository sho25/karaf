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
name|util
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
name|Properties
import|;
end_import

begin_class
specifier|public
class|class
name|Version
block|{
specifier|private
specifier|static
name|String
name|version
init|=
literal|"unknown"
decl_stmt|;
static|static
block|{
name|Properties
name|versions
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|Version
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"versions.properties"
argument_list|)
init|)
block|{
name|versions
operator|.
name|load
argument_list|(
name|is
argument_list|)
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
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|version
operator|=
name|versions
operator|.
name|getProperty
argument_list|(
literal|"karaf-version"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|karafVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
block|}
end_class

end_unit

