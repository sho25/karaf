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
name|jms
operator|.
name|command
operator|.
name|completers
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
name|jms
operator|.
name|JmsService
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
name|shell
operator|.
name|console
operator|.
name|Completer
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
name|shell
operator|.
name|console
operator|.
name|completer
operator|.
name|StringsCompleter
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

begin_comment
comment|/**  * Completer on the JMS connection factories.  */
end_comment

begin_class
specifier|public
class|class
name|ConnectionFactoriesCompleter
implements|implements
name|Completer
block|{
specifier|private
name|JmsService
name|jmsService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|complete
parameter_list|(
name|String
name|buffer
parameter_list|,
name|int
name|cursor
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|connectionFactory
range|:
name|jmsService
operator|.
name|connectionFactories
argument_list|()
control|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// nothing to do
block|}
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|buffer
argument_list|,
name|cursor
argument_list|,
name|candidates
argument_list|)
return|;
block|}
specifier|public
name|JmsService
name|getJmsService
parameter_list|()
block|{
return|return
name|jmsService
return|;
block|}
specifier|public
name|void
name|setJmsService
parameter_list|(
name|JmsService
name|jmsService
parameter_list|)
block|{
name|this
operator|.
name|jmsService
operator|=
name|jmsService
expr_stmt|;
block|}
block|}
end_class

end_unit

