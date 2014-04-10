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
name|jndi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|naming
operator|.
name|context
operator|.
name|WritableContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|naming
operator|.
name|global
operator|.
name|GlobalContextManager
import|;
end_import

begin_comment
comment|/**  * A very simple writable initial context factory.  * @see org.apache.xbean.naming.context.WritableContext for details.  */
end_comment

begin_class
specifier|public
class|class
name|KarafInitialContextFactory
extends|extends
name|GlobalContextManager
block|{
specifier|public
name|KarafInitialContextFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|()
expr_stmt|;
name|setGlobalContext
argument_list|(
operator|new
name|WritableContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

