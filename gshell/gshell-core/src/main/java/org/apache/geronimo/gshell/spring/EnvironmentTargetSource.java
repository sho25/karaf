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
name|geronimo
operator|.
name|gshell
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|command
operator|.
name|IO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|gshell
operator|.
name|shell
operator|.
name|Environment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|aop
operator|.
name|TargetSource
import|;
end_import

begin_comment
comment|/**  * A TargetSource that provides an Environment that has to be  * previously set in a thread local storage.  */
end_comment

begin_class
specifier|public
class|class
name|EnvironmentTargetSource
implements|implements
name|TargetSource
block|{
specifier|private
specifier|static
name|ThreadLocal
argument_list|<
name|Environment
argument_list|>
name|tls
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Environment
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|void
name|setEnvironment
parameter_list|(
name|Environment
name|env
parameter_list|)
block|{
name|tls
operator|.
name|set
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Class
name|getTargetClass
parameter_list|()
block|{
return|return
name|Environment
operator|.
name|class
return|;
block|}
specifier|public
name|boolean
name|isStatic
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Object
name|getTarget
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|tls
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|void
name|releaseTarget
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

