begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|karaf
operator|.
name|gshell
operator|.
name|core
operator|.
name|config
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|NamespaceHandlerSupport
import|;
end_import

begin_class
specifier|public
class|class
name|NamespaceHandler
extends|extends
name|NamespaceHandlerSupport
block|{
specifier|public
name|void
name|init
parameter_list|()
block|{
name|registerBeanDefinitionParser
argument_list|(
name|CommandParser
operator|.
name|COMMAND_BUNDLE
argument_list|,
operator|new
name|CommandParser
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

