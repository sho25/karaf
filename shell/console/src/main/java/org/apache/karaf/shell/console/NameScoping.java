begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|console
package|;
end_package

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

begin_comment
comment|/**  * A helper class for name scoping  */
end_comment

begin_class
specifier|public
class|class
name|NameScoping
block|{
specifier|public
specifier|static
specifier|final
name|String
name|MULTI_SCOPE_MODE_KEY
init|=
literal|"MULTI_SCOPE_MODE"
decl_stmt|;
comment|/**      * Returns the name of the command which can omit the global scope prefix if the command starts with the      * same prefix as the current application      */
specifier|public
specifier|static
name|String
name|getCommandNameWithoutGlobalPrefix
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isMultiScopeMode
argument_list|(
name|session
argument_list|)
condition|)
block|{
name|String
name|globalScope
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|get
argument_list|(
literal|"APPLICATION"
argument_list|)
decl_stmt|;
if|if
condition|(
name|globalScope
operator|!=
literal|null
condition|)
block|{
name|String
name|prefix
init|=
name|globalScope
operator|+
literal|":"
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
comment|// TODO we may only want to do this for single-scope mode when outside of OSGi?
comment|// so we may want to also check for a isMultiScope mode == false
return|return
name|key
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|key
return|;
block|}
comment|/**      * Returns true if the given scope is the global scope so that it can be hidden from help messages      */
specifier|public
specifier|static
name|boolean
name|isGlobalScope
parameter_list|(
name|CommandSession
name|session
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isMultiScopeMode
argument_list|(
name|session
argument_list|)
condition|)
block|{
name|String
name|globalScope
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|get
argument_list|(
literal|"APPLICATION"
argument_list|)
decl_stmt|;
if|if
condition|(
name|globalScope
operator|!=
literal|null
condition|)
block|{
return|return
name|scope
operator|.
name|equals
argument_list|(
name|globalScope
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns true if we are in multi-scope mode (the default) or if we are in single scope mode which means we      * avoid prefixing commands with their scope      */
specifier|public
specifier|static
name|boolean
name|isMultiScopeMode
parameter_list|(
name|CommandSession
name|session
parameter_list|)
block|{
name|Object
name|value
init|=
name|session
operator|.
name|get
argument_list|(
name|MULTI_SCOPE_MODE_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

