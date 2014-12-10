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
name|profile
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

begin_interface
specifier|public
interface|interface
name|PlaceholderResolver
block|{
name|String
name|CATCH_ALL_SCHEME
init|=
literal|"*"
decl_stmt|;
comment|/**      * The placeholder scheme.      */
specifier|public
name|String
name|getScheme
parameter_list|()
function_decl|;
comment|/**      * Resolves the placeholder found inside the value, for the specific key of the pid.      * @param profile   The current profile      * @param pid       The pid that contains the placeholder.      * @param key       The key of the configuration value that contains the placeholder.      * @param value     The value with the placeholder.      * @return          The resolved value or EMPTY_STRING.      */
specifier|public
name|String
name|resolve
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|profile
parameter_list|,
name|String
name|pid
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

