begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*   * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Contains various methods for helping with layout no commands  */
end_comment

begin_class
specifier|public
class|class
name|CommandUtils
block|{
comment|/** 	 * The message is either enlarged or trimmed to the given size.  	 *  	 * @param message - the message to be trimmed or enlarged 	 * @param length - the length of the message text 	 * @return the optimized message 	 */
specifier|public
specifier|static
name|String
name|trimToSize
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|StringBuilder
name|messageBuilder
init|=
operator|new
name|StringBuilder
argument_list|(
name|message
argument_list|)
decl_stmt|;
while|while
condition|(
name|messageBuilder
operator|.
name|length
argument_list|()
operator|<
name|length
condition|)
block|{
name|messageBuilder
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
return|return
name|messageBuilder
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|length
argument_list|)
return|;
block|}
block|}
end_class

end_unit

