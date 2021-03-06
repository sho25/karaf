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
name|instance
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
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
name|instance
operator|.
name|core
operator|.
name|Instance
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
name|instance
operator|.
name|core
operator|.
name|InstanceService
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
name|action
operator|.
name|Action
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
name|action
operator|.
name|lifecycle
operator|.
name|Reference
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|InstanceCommandSupport
implements|implements
name|Action
block|{
annotation|@
name|Reference
specifier|private
name|InstanceService
name|instanceService
decl_stmt|;
specifier|public
name|InstanceService
name|getInstanceService
parameter_list|()
block|{
return|return
name|instanceService
return|;
block|}
specifier|public
name|void
name|setInstanceService
parameter_list|(
name|InstanceService
name|instanceService
parameter_list|)
block|{
name|this
operator|.
name|instanceService
operator|=
name|instanceService
expr_stmt|;
block|}
specifier|protected
name|Instance
name|getExistingInstance
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Instance
name|i
init|=
name|instanceService
operator|.
name|getInstance
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Instances '"
operator|+
name|name
operator|+
literal|"' does not exist"
argument_list|)
throw|;
block|}
return|return
name|i
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Instance
argument_list|>
name|getMatchingInstances
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|)
block|{
name|List
argument_list|<
name|Instance
argument_list|>
name|instances
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Instance
index|[]
name|allInstances
init|=
name|instanceService
operator|.
name|getInstances
argument_list|()
decl_stmt|;
for|for
control|(
name|Instance
name|instance
range|:
name|allInstances
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|instance
operator|.
name|getName
argument_list|()
argument_list|,
name|patterns
argument_list|)
condition|)
block|{
name|instances
operator|.
name|add
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|instances
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No matching instances"
argument_list|)
throw|;
block|}
return|return
name|instances
return|;
block|}
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|URL
argument_list|>
name|getResources
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|resources
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|URL
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|resource
range|:
name|resources
control|)
block|{
name|String
name|path
init|=
name|resource
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|resource
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|location
init|=
name|resource
operator|.
name|substring
argument_list|(
name|path
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|location
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|boolean
name|match
parameter_list|(
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
if|if
condition|(
name|name
operator|.
name|matches
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|doExecute
argument_list|()
return|;
block|}
specifier|protected
specifier|abstract
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

