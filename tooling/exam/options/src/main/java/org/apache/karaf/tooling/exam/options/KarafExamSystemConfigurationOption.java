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
name|tooling
operator|.
name|exam
operator|.
name|options
package|;
end_package

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_comment
comment|/**  * Option to configure the internal invoker for integration tests to be used.  */
end_comment

begin_class
specifier|public
class|class
name|KarafExamSystemConfigurationOption
implements|implements
name|Option
block|{
specifier|private
name|String
name|invoker
decl_stmt|;
comment|/**      * define the pax.exam.invoker property as system property in the environment during creating the subproject.      */
specifier|public
name|KarafExamSystemConfigurationOption
parameter_list|(
name|String
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
specifier|public
name|String
name|getInvoker
parameter_list|()
block|{
return|return
name|invoker
return|;
block|}
block|}
end_class

end_unit

