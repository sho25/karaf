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
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|Command
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
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
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
name|support
operator|.
name|table
operator|.
name|ShellTable
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"jms"
argument_list|,
name|name
operator|=
literal|"info"
argument_list|,
name|description
operator|=
literal|"Provides details about a JMS connection factory."
argument_list|)
annotation|@
name|Service
specifier|public
class|class
name|InfoCommand
extends|extends
name|JmsConnectionCommandSupport
block|{
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|ShellTable
name|table
init|=
operator|new
name|ShellTable
argument_list|()
decl_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Property"
argument_list|)
expr_stmt|;
name|table
operator|.
name|column
argument_list|(
literal|"Value"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
init|=
name|getJmsService
argument_list|()
operator|.
name|info
argument_list|(
name|connectionFactory
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|info
operator|.
name|keySet
argument_list|()
control|)
block|{
name|table
operator|.
name|addRow
argument_list|()
operator|.
name|addContent
argument_list|(
name|key
argument_list|,
name|info
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|print
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

