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
name|log
operator|.
name|core
package|;
end_package

begin_comment
comment|/**  * Enumeration of available log levels for the log:set command and  * the command completer  */
end_comment

begin_enum
specifier|public
enum|enum
name|Level
block|{
name|TRACE
block|,
name|DEBUG
block|,
name|INFO
block|,
name|WARN
block|,
name|ERROR
block|,
name|OFF
block|,
name|DEFAULT
block|;
comment|/**      * Convert the list of values into a String array      *       * @return all the values as a String array      */
specifier|public
specifier|static
name|String
index|[]
name|strings
parameter_list|()
block|{
name|String
index|[]
name|values
init|=
operator|new
name|String
index|[
name|values
argument_list|()
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
name|values
argument_list|()
index|[
name|i
index|]
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
return|return
name|values
return|;
block|}
comment|/**      * Check if the string value represents the default level      *       * @param level the level value      * @return<code>true</code> if the value represents the {@link #DEFAULT} level      */
specifier|public
specifier|static
name|boolean
name|isDefault
parameter_list|(
name|String
name|level
parameter_list|)
block|{
return|return
name|valueOf
argument_list|(
name|level
argument_list|)
operator|.
name|equals
argument_list|(
name|DEFAULT
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

