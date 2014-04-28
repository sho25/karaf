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
name|features
operator|.
name|internal
operator|.
name|resolver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Capability
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_class
specifier|public
class|class
name|CapabilityImpl
extends|extends
name|BaseClause
implements|implements
name|Capability
block|{
specifier|private
specifier|final
name|Resource
name|resource
decl_stmt|;
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|mandatory
decl_stmt|;
specifier|public
name|CapabilityImpl
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dirs
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attrs
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
name|this
operator|.
name|dirs
operator|=
name|dirs
expr_stmt|;
name|this
operator|.
name|attrs
operator|=
name|attrs
expr_stmt|;
comment|// Handle mandatory directive
name|Set
argument_list|<
name|String
argument_list|>
name|mandatory
init|=
name|Collections
operator|.
name|emptySet
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|this
operator|.
name|dirs
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|MANDATORY_DIRECTIVE
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|ResourceBuilder
operator|.
name|parseDelimitedString
argument_list|(
name|value
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|mandatory
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
comment|// If attribute exists, then record it as mandatory.
if|if
condition|(
name|this
operator|.
name|attrs
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|mandatory
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
comment|// Otherwise, report an error.
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Mandatory attribute '"
operator|+
name|name
operator|+
literal|"' does not exist."
argument_list|)
throw|;
block|}
block|}
block|}
name|this
operator|.
name|mandatory
operator|=
name|mandatory
expr_stmt|;
block|}
specifier|public
name|Resource
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDirectives
parameter_list|()
block|{
return|return
name|dirs
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|attrs
return|;
block|}
specifier|public
name|boolean
name|isAttributeMandatory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|!
name|mandatory
operator|.
name|isEmpty
argument_list|()
operator|&&
name|mandatory
operator|.
name|contains
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

