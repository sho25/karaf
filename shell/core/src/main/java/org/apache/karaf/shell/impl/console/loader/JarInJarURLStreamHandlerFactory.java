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
name|shell
operator|.
name|impl
operator|.
name|console
operator|.
name|loader
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLStreamHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLStreamHandlerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|JarInJarURLStreamHandlerFactory
implements|implements
name|URLStreamHandlerFactory
block|{
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|private
name|URLStreamHandlerFactory
name|chainFac
decl_stmt|;
specifier|public
name|JarInJarURLStreamHandlerFactory
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|cl
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|URLStreamHandler
name|createURLStreamHandler
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
if|if
condition|(
name|JarInJarConstants
operator|.
name|INTERNAL_URL_PROTOCOL
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
return|return
operator|new
name|JarInJarStreamHandler
argument_list|(
name|classLoader
argument_list|)
return|;
if|if
condition|(
name|chainFac
operator|!=
literal|null
condition|)
return|return
name|chainFac
operator|.
name|createURLStreamHandler
argument_list|(
name|protocol
argument_list|)
return|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

