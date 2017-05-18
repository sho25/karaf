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
name|internal
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
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

begin_interface
interface|interface
name|DestinationSource
block|{
enum|enum
name|DestinationType
block|{
name|Queue
block|,
name|Topic
block|}
interface|interface
name|Factory
block|{
name|DestinationSource
name|create
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|JMSException
function_decl|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|(
name|DestinationType
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

