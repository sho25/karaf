begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|test
operator|.
name|d
package|;
end_package

begin_import
import|import
name|test
operator|.
name|a
operator|.
name|*
import|;
end_import

begin_import
import|import
name|test
operator|.
name|c
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|D
block|{
specifier|public
name|String
name|createStringWithDependencies
parameter_list|()
block|{
return|return
name|A
operator|.
name|ASTRING
operator|+
literal|" "
operator|+
name|C
operator|.
name|createStringWithDependencies
argument_list|()
operator|+
literal|" "
operator|+
literal|"D-string"
return|;
block|}
block|}
end_class

end_unit

