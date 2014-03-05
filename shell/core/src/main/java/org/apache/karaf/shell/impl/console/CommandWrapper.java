begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|impl
operator|.
name|console
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|runtime
operator|.
name|Closure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|CommandSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|service
operator|.
name|command
operator|.
name|Function
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
name|console
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
name|console
operator|.
name|Session
import|;
end_import

begin_class
specifier|public
class|class
name|CommandWrapper
implements|implements
name|Function
block|{
specifier|private
specifier|final
name|Command
name|command
decl_stmt|;
specifier|public
name|CommandWrapper
parameter_list|(
name|Command
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
specifier|public
name|Command
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
specifier|final
name|CommandSession
name|commandSession
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: remove the hack for .session
name|Session
name|session
init|=
operator|(
name|Session
operator|)
name|commandSession
operator|.
name|get
argument_list|(
literal|".session"
argument_list|)
decl_stmt|;
comment|// When need to translate closures to a compatible type for the command
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|arguments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|v
init|=
name|arguments
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|instanceof
name|Closure
condition|)
block|{
specifier|final
name|Closure
name|closure
init|=
operator|(
name|Closure
operator|)
name|v
decl_stmt|;
name|arguments
operator|.
name|set
argument_list|(
name|i
argument_list|,
operator|new
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
operator|.
name|Function
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|(
name|Session
name|session
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|closure
operator|.
name|execute
argument_list|(
name|commandSession
argument_list|,
name|arguments
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|command
operator|.
name|execute
argument_list|(
name|session
argument_list|,
name|arguments
argument_list|)
return|;
block|}
block|}
end_class

end_unit

