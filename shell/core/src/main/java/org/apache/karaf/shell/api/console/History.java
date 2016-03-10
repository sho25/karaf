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
name|shell
operator|.
name|api
operator|.
name|console
package|;
end_package

begin_comment
comment|/**  * Session history.  */
end_comment

begin_interface
specifier|public
interface|interface
name|History
block|{
comment|/**      * First available index.      *      * @return first index in the history      */
name|int
name|first
parameter_list|()
function_decl|;
comment|/**      * Last available index.      *      * @return last index in the history      */
name|int
name|last
parameter_list|()
function_decl|;
comment|/**      * Command at the given index.      * Indices can range from<code>first()</code> to<code>last()</code>.      *      * @param index the index in the history.      * @return the command in the history at the given index.      */
name|CharSequence
name|get
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|/**      * Clear the history.      */
name|void
name|clear
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

