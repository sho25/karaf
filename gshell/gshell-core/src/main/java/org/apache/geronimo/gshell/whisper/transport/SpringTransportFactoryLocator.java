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
name|geronimo
operator|.
name|gshell
operator|.
name|whisper
operator|.
name|transport
package|;
end_package

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

begin_comment
comment|/**  * Spring based implementation of the TransportFactory locator  */
end_comment

begin_class
specifier|public
class|class
name|SpringTransportFactoryLocator
parameter_list|<
name|T
extends|extends
name|TransportFactory
parameter_list|>
implements|implements
name|TransportFactoryLocator
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|T
argument_list|>
name|factories
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|factoryMap
decl_stmt|;
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|getFactories
parameter_list|()
block|{
return|return
name|factories
return|;
block|}
specifier|public
name|void
name|setFactories
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|factories
parameter_list|)
block|{
name|this
operator|.
name|factories
operator|=
name|factories
expr_stmt|;
name|this
operator|.
name|factoryMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|T
name|factory
range|:
name|factories
control|)
block|{
name|factoryMap
operator|.
name|put
argument_list|(
name|factory
operator|.
name|getScheme
argument_list|()
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|T
name|locate
parameter_list|(
name|URI
name|location
parameter_list|)
throws|throws
name|TransportException
block|{
assert|assert
name|location
operator|!=
literal|null
assert|;
name|String
name|scheme
init|=
name|location
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheme
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidLocationException
argument_list|(
name|location
argument_list|)
throw|;
block|}
name|T
name|factory
init|=
name|factoryMap
operator|.
name|get
argument_list|(
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|LookupException
argument_list|(
name|scheme
argument_list|)
throw|;
block|}
return|return
name|factory
return|;
block|}
block|}
end_class

end_unit

