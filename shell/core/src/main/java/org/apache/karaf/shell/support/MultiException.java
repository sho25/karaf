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
name|support
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
specifier|public
class|class
name|MultiException
extends|extends
name|Exception
block|{
specifier|public
name|MultiException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultiException
parameter_list|(
name|String
name|message
parameter_list|,
name|List
argument_list|<
name|Exception
argument_list|>
name|exceptions
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|exceptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Exception
name|exception
range|:
name|exceptions
control|)
block|{
name|addSuppressed
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Deprecated
specifier|public
name|void
name|addException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|addSuppressed
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|throwIfExceptions
parameter_list|()
throws|throws
name|MultiException
block|{
if|if
condition|(
name|getSuppressed
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
throw|throw
name|this
throw|;
block|}
block|}
annotation|@
name|Deprecated
specifier|public
name|Throwable
index|[]
name|getCauses
parameter_list|()
block|{
return|return
name|getSuppressed
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|throwIf
parameter_list|(
name|String
name|message
parameter_list|,
name|List
argument_list|<
name|Exception
argument_list|>
name|exceptions
parameter_list|)
throws|throws
name|MultiException
block|{
if|if
condition|(
name|exceptions
operator|!=
literal|null
operator|&&
operator|!
name|exceptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
for|for
control|(
name|Exception
name|e
range|:
name|exceptions
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n\t"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|MultiException
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|exceptions
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

